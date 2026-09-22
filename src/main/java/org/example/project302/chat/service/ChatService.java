package org.example.project302.chat.service;

import jakarta.mail.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.chat.dto.*;
import org.example.project302.chat.entity.ChatNotice;
import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.chat.entity.ChatParticipationId;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.chat.repository.ChatNoticeRepository;
import org.example.project302.chat.repository.ChatParticipationRepository;
import org.example.project302.chat.repository.ChatUserRepository;
import org.example.project302.chat.repository.ChatroomRepository;
import org.example.project302.deal.entity.Participation;
import org.example.project302.deal.repository.ParticipationRepository;
import org.example.project302.file.entity.File;
import org.example.project302.file.repository.FileRepository;
import org.example.project302.file.service.FileService;
import org.example.project302.group.entity.Group;
import org.example.project302.group.repository.GroupRepository;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final FileService fileService;
    private final ChatroomRepository chatroomRepository;
    private final ChatParticipationRepository chatPartRepository;
    private final ChatUserRepository chatUserRepository;
    private final ProductRepository productRepository;
    private final GroupRepository groupRepository;
    private final ParticipationRepository partRepository;
    private final ChatNoticeRepository chatNoticeRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    private void sendMessage(GetMessageResponse message, String topic) {
        List<GetMessageResponse> messages = Collections.singletonList(message);
        messagingTemplate.convertAndSend(topic, messages);
    }

    private GetChatListResponse toChatListResponse(ChatroomWithUserId chat, User user) {
        GetChatListResponse res = new GetChatListResponse(chat);
        Product product = productRepository.findById(chat.getPd_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + chat.getPd_id()));
        res.setPdName(product.getPdName());
        res.setFileLink(fileService.getFileLink(product));
        res.setPartUsers(getPartUserNoUser(chat.getChat_id(), user));
        return res;
    }

    /*
     *  웹소켓 - 채팅방 번호 조회
     */
    public List<Long> getChatIdList(User user) {
        return chatPartRepository.findByUser(user).stream()
                .map(participation -> participation.getChatroom().getChatId())
                .collect(Collectors.toList());
    }


    /*
     *   채팅방 전체 조회 .-≡=★
     */
    public List<GetChatListResponse> getChatList(User user) {
        return chatroomRepository.findAllByUser_UserId(user.getUserId()).stream()
                .map(chat -> toChatListResponse(chat, user))
                .collect(Collectors.toList());
    }


    /*
     *   중고 채팅방 전체 조회 .-≡=★
     */
    public List<GetChatListResponse> getUsedChatList(User user) {
        return chatroomRepository.findUsedByUser_UserId(user.getUserId()).stream()
                .map(chat -> toChatListResponse(chat, user))
                .collect(Collectors.toList());
    }


    /*
     *   공구 채팅방 전체 조회 .-≡=★
     */
    public List<GetChatListResponse> getGroupChatList(User user) {
        return chatroomRepository.findGroupByUser_UserId(user.getUserId()).stream()
                .map(chat -> toChatListResponse(chat, user))
                .collect(Collectors.toList());
    }


    /*
     *  거래중인 채팅방 찾기
     */
    public Chatroom getChatroom(Product product) {

        if (product.getPdStatus() == ProductStatus.GROUP)
            return chatroomRepository.findByProduct(product);

        List<Chatroom> chatrooms = chatroomRepository.findAllByProduct(product);
        Participation participation = partRepository.findByProductAndUserIsNot(product, product.getUser())
                .orElseThrow(() -> new IllegalArgumentException("거래 대상 없음 : " + product.getPdId()));
        ChatParticipation chatParticipation = chatPartRepository.findByUserAndChatroomIsIn(participation.getUser(), chatrooms)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        return chatParticipation.getChatroom();
    }


    /*
     *   채팅 참가자 조회 .-≡=★
     */
    public List<GetPartUserResponse> getPartUser(Long chatId) {
        return chatUserRepository.findAllByChatId(chatId).stream()
                .map(user -> new GetPartUserResponse(user, fileService.getFileLink(user)))
                .collect(Collectors.toList());
    }


    /*
     *   채팅 참가자 조회 - 본인 제외 .-≡=★
     */
    public List<GetPartUserResponse> getPartUserNoUser(Long chatId, User user) {
        return chatUserRepository.findAllByChatIdNotUserId(chatId, user.getUserId()).stream()
                .map(data -> new GetPartUserResponse(data, fileService.getFileLink(data)))
                .collect(Collectors.toList());
    }


    /*
     *  유저 정보 조회
     */
    public GetPartUserResponse getSelfInfo(User user) {
        return new GetPartUserResponse(user, fileService.getFileLink(user));
    }


    /*
     *   단체 채팅방 생성 .-≡=★
     */
    @Transactional
    public void createGroupChat(Product pd, User user) {  // 공동구매 상품 등록과 동시에 생성
        // 1. 채팅방 생성
        Chatroom chatroom = chatroomRepository.save(new Chatroom(null, pd));
        // 2. 판매자 채팅방 참가
        chatPartRepository.save(new ChatParticipation(user, chatroom, LocalDateTime.now(), LocalDateTime.now()));
    }


    /*
     *   단체 채팅방 참가 .-≡=★
     */
    @Transactional
    public Chatroom joinGroupChat(Product product, User user) {
        Chatroom chatroom = chatroomRepository.findByProduct(product);
        if (!chatPartRepository.existsById(new ChatParticipationId(chatroom, user))) {
            chatPartRepository.save(
                    new ChatParticipation(user, chatroom, LocalDateTime.now(), null)
            );
            ChatNotice notice = chatNoticeRepository.save(new ChatNotice(
                    null,
                    chatroom,
                    LocalDateTime.now(),
                    user.getNickname() + " 님이 입장하셨습니다"
            ));
            sendMessage(new GetMessageResponse(notice), "/chatroom/" + chatroom.getChatId());
        }
        return chatroom;
    }


    /*
     *   1:1 채팅방 생성 및 참가 .-≡=★
     */
    @Transactional
    public Chatroom joinUsedChat(Product product, User user) {
        return chatPartRepository.findByUserAndChatroom_Product(user, product).stream()
                .map(ChatParticipation::getChatroom)
                .findAny()
                .orElseGet(() -> {
                    Chatroom newChatroom = chatroomRepository.save(new Chatroom(null, product));
                    chatPartRepository.saveAll(Arrays.asList(
                            new ChatParticipation(user, newChatroom, LocalDateTime.now(), null),
                            new ChatParticipation(product.getUser(), newChatroom, LocalDateTime.now(), null)));
                    return newChatroom;
                });
    }


    /*
     *   채팅방 및 채팅 참가자 생성 .-≡=★
     */
    @Transactional
    public Chatroom joinChat(Long pdId, User user) {
        Product targetPd = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        if (targetPd.getDealStatus() != DealStatus.SELL || targetPd.getDealStatus() != DealStatus.RESV)
            new IllegalArgumentException("채팅 참가 불가 : 거래중인 상품 : " + pdId);

        // 2-1. 공동구매 -> 기존 그룹채팅방 참가
        if (targetPd.getPdStatus() == ProductStatus.GROUP)
            return joinGroupChat(targetPd, user);
        else // 2-2. 중고거래
            return joinUsedChat(targetPd, user);
    }


    /*
     *   채팅방 나가기 .-≡=★
     */
    @Transactional
    public void deleteChatPart(Long chatId, User user) {
        // 1. 채팅 참가 조회
        Chatroom chat = chatroomRepository.findById(chatId).orElse(null);
        ChatParticipationId chatpartId = new ChatParticipationId(chat, user);
        ChatParticipation target = chatPartRepository.findById(chatpartId).orElse(null);

        // 1-2. 상품 거래 상태 확인
        DealStatus dealStatus = chat.getProduct().getDealStatus();
        if (dealStatus.getValue() == 2)
            new IllegalArgumentException("거래중인 채팅방은 나갈 수 없습니다.");

        // 2. 채팅 참가 내역이 있다면 삭제
        if (target != null)
            chatPartRepository.deleteById(chatpartId);

        // 3. 단체채팅방 noticeMsg 추가
        if (chat.getProduct().getPdStatus() == ProductStatus.GROUP) {
            ChatNotice notice = chatNoticeRepository.save(new ChatNotice(
                    null,
                    chat,
                    LocalDateTime.now(),
                    user.getNickname() + " 님이 나갔습니다"
            ));
            sendMessage(new GetMessageResponse(notice), "/chatroom/" + chat.getChatId());
        }
    }


    /*
     *   상품 정보 조회 .-≡=★
     */
    public GetChatProdResponse selectProdByChatId(Long chatId) {
        Chatroom chatroom = chatroomRepository.findById(chatId).
                orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));
        Product product = chatroom.getProduct();
        return selectProd(product);
    }


    /*
     *   상품 정보 조회 .-≡=★
     */
    public GetChatProdResponse selectProdByPdId(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));
        return selectProd(product);
    }

    /* 상품 정보 반환 */
    private GetChatProdResponse selectProd(Product product) {
        // 숨김 또는 삭제된 상품이라면
        if (product == null || product.getDealStatus() == DealStatus.HIDE)
            return null;

        Group targetDetail = groupRepository.findById(product.getPdId()).orElse(null);

        return new GetChatProdResponse(
                product,
                targetDetail,
                fileService.getFileLink(product),
                fileService.getFileLink(product.getUser()));
    }
}
