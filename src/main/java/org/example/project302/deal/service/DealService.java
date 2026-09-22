package org.example.project302.deal.service;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import lombok.RequiredArgsConstructor;
import org.example.project302.chat.dto.GetChatProdResponse;
import org.example.project302.chat.dto.GetPartUserResponse;
import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.chat.repository.ChatParticipationRepository;
import org.example.project302.chat.repository.ChatroomRepository;
import org.example.project302.chat.service.ChatService;
import org.example.project302.deal.dto.*;
import org.example.project302.deal.entity.Participation;
import org.example.project302.deal.entity.ParticipationId;
import org.example.project302.deal.repository.ParticipationRepository;
import org.example.project302.deal.repository.ReviewRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealService {
    private final ProductRepository productRepository;
    private final GroupRepository groupRepository;
    private final ParticipationRepository partRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatParticipationRepository chatPartRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final ReviewRepository reviewRepository;
    private final ChatService chatService;


    /*
     *  상품 거래 상태 수정
     */
    @Transactional
    public GetChatProdResponse updateDealStatus(Long chatId, String updatedStatus) {
        DealStatus dealStatus = getDealStatus(updatedStatus);

        // 1-1. 상품, 거래자 없으면 예외처리
        Chatroom chatroom = chatroomRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("상태 변경 불가" + " : 대상 회원 없음"));
        Product product = productRepository.findById(chatroom.getProduct().getPdId())
                .orElseThrow(() -> new IllegalArgumentException("상태 변경 불가" + " : 대상 상품 없음"));

        // 1-2. 거래 취소 상품 예외처리
        if (product.getDealStatus() == DealStatus.CANC)
            new IllegalArgumentException("상태 변경 불가" + " : 취소한 거래는 다시 거래할 수 없음");

        // 1-3. 미전달 공구 상품 예외처리
        List<Participation> partList = partRepository.findAllByProduct(product);
        if (dealStatus == DealStatus.SOLD && !isAllTaked(partList)) {
            new IllegalArgumentException("상태 변경 불가" + " : 모두 전달 받지 못 함");
        }

        // 1-4. 거래완료 상품 예외처리 (숨김만 가능)
        if (product.getDealStatus() == DealStatus.SOLD && dealStatus != DealStatus.HIDE)
            new IllegalArgumentException("상태 변경 불가" + " : 거래 완료된 상품");

        // -> 예약중/입금중 : 거래 참여자 생성
        if (dealStatus == DealStatus.RESV || dealStatus == DealStatus.DEP)
            createParticipation(chatroom);

        Product updated = updateDealStatus(product, dealStatus);

        // 반환 객체 생성
        Group updatedDetail = groupRepository.findById(updated.getPdId()).orElse(null);

        return new GetChatProdResponse(
                updated,
                updatedDetail,
                fileService.getFileLink(product),
                fileService.getFileLink(updated.getUser()));
    }


    /*
     *  참가자 입금/전달 여부 체크 .-≡=★
     */
    @Transactional
    public ResponseEntity checkPartUser(User user, Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));
        Participation part = partRepository.findById(new ParticipationId(user, product))
                .orElseThrow(() -> new IllegalArgumentException("상태 변경 불가" + " : 대상 참여자 없음"));

        updateDealDate(product, part);

        if (checkAllUsers(product)) {
            updateDealStatus(product);
            return ResponseEntity.ok(true);
        } else
            return ResponseEntity.ok(false);
    }


    /*
     *  참가 현황 업데이트 (입금/전달)
     */
    @Transactional
    public void updateDealDate(Product product, Participation participation) {
        switch (product.getDealStatus()) {
            case DEP -> participation.setDepositDate(
                    participation.getDepositDate() == null ? LocalDateTime.now() : null);
            case SEND -> participation.setTakedDate(
                    participation.getTakedDate() == null ? LocalDateTime.now() : null);
        }
        partRepository.save(participation);
    }


    /*
     *  상품 거래 상태 업데이트 (입금 -> 구매, 전달 -> 완료)
     */
    @Transactional
    public void updateDealStatus(Product product) {
        switch (product.getDealStatus()) {
            case DEP -> updateDealStatus(product, DealStatus.BUY);
            case SEND -> updateDealStatus(product, DealStatus.SOLD);
        };
    }


    /*
     *  상품 거래 상태 찐.업데이트
     */
    @Transactional
    public Product updateDealStatus(Product product, DealStatus dealStatus) {
        switch (dealStatus) {
            case SELL -> {
                partRepository.deleteAllByProduct(product);
                product.setScheduleDate(null);
            }
            case DEP -> {
                Participation seller = partRepository.findByProductAndUser(product, product.getUser())
                        .orElseThrow(() -> new IllegalArgumentException("판매자가 없을리가 : 근데 그게 됨"));
                seller.setDepositDate(LocalDateTime.now());
                partRepository.save(seller);
            }
            case SEND -> {
                Participation seller = partRepository.findByProductAndUser(product, product.getUser())
                        .orElseThrow(() -> new IllegalArgumentException("판매자가 없을리가 : 근데 그게 됨"));
                seller.setTakedDate(LocalDateTime.now());
                partRepository.save(seller);
            }
            case SOLD -> {
                product.setCompletionDate(LocalDateTime.now());
            }
            case CANC -> {
                partRepository.deleteAllByProduct(product);
                product.setScheduleDate(null);
            }
        }
        product.setDealStatus(dealStatus);
        return productRepository.save(product);
    }


    /*
     *  모두 체크했는지 확인
     */
    private boolean checkAllUsers(Product product) {
        Integer headCnt = partRepository.countAllByProduct(product);
        Integer checkedCnt = switch (product.getDealStatus()) {
            case DEP -> partRepository.countAllByProductAndDepositDateIsNotNull(product);
            case SEND -> partRepository.countAllByProductAndTakedDateIsNotNull(product);
            default -> 0;
        };
        return checkedCnt.equals(headCnt);
    }


    /*
     *  출발 확인 - 중간 발표 이후
     */
    @Transactional
    public Participation updatePartUserStart(User user, Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));
        Participation part = selectPartUser(user, product);
        if (part.getStartDate() == null)
            part.setStartDate(LocalDateTime.now());
        else part.setStartDate(null);
        return partRepository.save(part);
    }


    /*
     *  참여자 거래 정보 조회 .-≡=★
     */
    public List<GetDealPartRes> getDealParts(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        return partRepository.findAllByProduct(product).stream().map(part -> {
            User user = part.getUser();
            return getDealPart(product, user, part);
        }).collect(Collectors.toList());
    }


    /*
     *  본인 거래 정보 조회
     */
    public GetDealPartRes getSelfDeal(User user, Long chatId) {
        Product product = chatroomRepository.findById(chatId).get().getProduct();
        Participation part = partRepository.findById(new ParticipationId(user, product))
                .orElse(null);
        if (part == null)
            return null;
        else
            return getDealPart(product, user, part);
    }

    /* 거래 정보 조회 */
    private GetDealPartRes getDealPart(Product product, User user, Participation part) {
        boolean checked = switch (product.getDealStatus()) {
            case DEP -> part.getDepositDate() != null;
            case SEND -> part.getTakedDate() != null;
            default -> part.getStartDate() != null;
        };
        return new GetDealPartRes(user, fileService.getFileLink(user), checked);
    }


    /*
     *  입금 현황 .-≡=★
     */
    public GetCheckCntRes countDepUsers(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("조회 불가" + " : 대상 상품 없음"));

        // 모든 참여자 수 & 확인된 참여자 수
        Integer headCnt = partRepository.countAllByProduct(product);
        Integer checkCnt = partRepository.countAllByProductAndDepositDateIsNotNull(product);
        return new GetCheckCntRes(headCnt, checkCnt);
    }


    /*
     *  전달 현황 .-≡=★
     */
    public GetCheckCntRes countTakedUsers(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("조회 불가" + " : 대상 상품 없음"));

        // 모든 참여자 수 & 확인된 참여자 수
        Integer headCnt = partRepository.countAllByProduct(product);
        Integer checkCnt = partRepository.countAllByProductAndTakedDateIsNotNull(product);
        return new GetCheckCntRes(headCnt, checkCnt);
    }


    /*
     *  거래 참가 여부 .-≡=★
     */
    public boolean checkDealingChat(Long chatId) {
        return chatPartRepository.findAllByChatroom_ChatId(chatId)
                .stream()
                .map(ChatParticipation::getUser)
                .allMatch(user -> partRepository.existsById(new ParticipationId(user, chatroomRepository.findById(chatId).get().getProduct())));
    }


    /*
     *  후기 작성 여부
     */
    public boolean checkWrittenReview(User user, Long chatId) {
        Chatroom chatroom = chatroomRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방"));
        Product product = chatroom.getProduct();

        // 공동구매 판매자 입장일 때
        if (product.getPdStatus() == ProductStatus.GROUP && product.getUser().equals(user))
            return partRepository.countByProductAndUserIsNot(product, user) == reviewRepository.countByProductAndSender(product, user);
        // 중고거래이거나 공동구매 구매자 입장일 때
        return reviewRepository.existsByProductAndSender(product, user);
    }


    /*
     *  후기 작성 여부 - 판매자 화면
     */
    public List<GetUserRvwChckRes> getUserRvwChckResList(User user, Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        return partRepository.findAllByProductAndUserIsNot(product, user).stream()
                .map(part -> {
                    User buyer = part.getUser();
                    return new GetUserRvwChckRes(
                            buyer,
                            fileService.getFileLink(buyer),
                            reviewRepository.existsByProductAndReceiver(product, buyer)
                    );
                })
                .collect(Collectors.toList());
    }


    /*
     *  일정 조회
     */
    public GetScheduleRes getScheduleRes(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        if (product.getScheduleDate() == null)
            new IllegalArgumentException("예정된 일정 없음 : " + pdId);

        return new GetScheduleRes(product, fileService.getFileLink(product));
    }


    /*
     *  일정 등록 폼 .-≡=★
     */
    public GetScheduleRes getScheduleRes(Long pdId, User user) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        if (!user.equals(product.getUser()))
            new IllegalArgumentException("판매자 불일치 : " + pdId);

        // res
        GetScheduleRes res = new GetScheduleRes(product, null);
        if (res.getLatitude() == null || res.getLongitude() == null) {
            res.setLatitude(user.getUniversity().getUnivLatitude());
            res.setLongitude(user.getUniversity().getUnivLongitude());
        }
        return res;
    }


    /*
     *  일정 등록 및 수정 .-≡=★
     */
    public Chatroom saveSchedule(ScheduleForm form) {
        Product product = productRepository.findById(form.getPdId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));

        product.setScheduleDate(form.getScheduleDate());
        product.setLongitude(form.getLongitude());
        product.setLatitude(form.getLatitude());
        product.setDetailAddr(form.getDetailAddr());
        Product updated = productRepository.save(product);

        // res
        return chatService.getChatroom(updated);
    }


    /*
     *  일정 취소
     */
    public Chatroom deleteSchedule(Long pdId, User user) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음"));
        if (product.getUser().equals(user))
            new IllegalArgumentException("판매자 불일치");

        product.setScheduleDate(null);
        Product updated = productRepository.save(product);

        // res
        return chatService.getChatroom(updated);
    }




    /* 거래 참가 회원 생성 */
    private void createParticipation(Chatroom chatroom) {
        // 현재 채팅방 참여자 확인
        List<ChatParticipation> chatPartList = chatPartRepository.findAllByChatroom(chatroom);

        // 상품 거래 참여자 리스트 생성
        List<Participation> partList = chatPartList.stream()
                .map(chatPart ->
//                        new Participation(
//                        chatPart.getChatroom().getProduct(),
//                        chatPart.getUser(),
//                        chatPart.getUser() == chatroom.getProduct().getUser() ? LocalDateTime.now() : null,
//                        null,
//                        null)
                        Participation.builder()
                                .product(chatPart.getChatroom().getProduct())
                                .user(chatPart.getUser())
                                .build()
                )
                .collect(Collectors.toList());

        // create
        partRepository.saveAll(partList);
    }

    /* 거래 참여자 모두 조회 */
    private List<Participation> selectPartUsers(Product product) {
        List<Participation> partList = partRepository.findAllByProduct(product);
        return partList;
    }

    /* 거래 참여자 조회 */
    private Participation selectPartUser(User user, Product product) {
        Participation part = partRepository.findById(new ParticipationId(user, product))
                .orElseThrow(() -> new IllegalArgumentException("상태 변경 불가" + " : 대상 참여자 없음"));
        return part;
    }

    /* 전체 참여자 입금 확인 업데이트 */
    private void updatePartUsersDep(List<Participation> partList) {
        partList.forEach(part -> {
            if (part.getDepositDate() == null) {
                part.setDepositDate(LocalDateTime.now());
                partRepository.save(part);
            }
        });
    }

    /* 공동구매 전달 완료 확인 */
    private boolean isAllTaked(List<Participation> partList) {
        if (partList == null || partList.isEmpty())
            return false;
        return !partList.stream().anyMatch(part -> part.getTakedDate() == null);
    }

    /* DealStatus 반환 */
    private DealStatus getDealStatus(String dealStatus) {
        switch (dealStatus) {
            case "SELL":
                return DealStatus.SELL;
            case "RESV":
                return DealStatus.RESV;
            case "DEP":
                return DealStatus.DEP;
            case "BUY":
                return DealStatus.BUY;
            case "SEND":
                return DealStatus.SEND;
            case "SOLD":
                return DealStatus.SOLD;
            case "CANC":
                return DealStatus.CANC;
            default:
                return DealStatus.HIDE;
        }
    }
}
