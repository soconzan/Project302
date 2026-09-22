package org.example.project302.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.chat.dto.AddMessageRequest;
import org.example.project302.chat.dto.GetMessageResponse;
import org.example.project302.chat.dto.MessageAndFile;
import org.example.project302.chat.entity.ChatFile;
import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.chat.entity.Chatroom;
import org.example.project302.chat.entity.Message;
import org.example.project302.chat.repository.ChatFileRepository;
import org.example.project302.chat.repository.ChatParticipationRepository;
import org.example.project302.chat.repository.ChatroomRepository;
import org.example.project302.chat.repository.MessageRepository;
import org.example.project302.file.service.FileService;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;
    private final ChatParticipationRepository chatPartRepository;
    private final ChatFileRepository chatFileRepository;
    private final FileService fileService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private GetMessageResponse toMessageResponse(MessageAndFile maf) {
        User user = (maf.getUser_id() == null) ? null : userRepository.findById(maf.getUser_id()).orElse(null);
        if (user == null)
            return new GetMessageResponse(
                    null,
                    null,
                    null,
                    maf.getContent(),
                    null,
                    maf.getCreat_date(),
                    0,
                    maf.getChat_id()
            );
        return new GetMessageResponse(
                user.getUserId(),
                user.getNickname(),
                fileService.getFileLink(user),
                maf.getContent(),
                fileService.getFileLink(maf.getChat_file_id()),
                maf.getCreat_date(),
                maf.getRead_not(),
                maf.getChat_id()
        );
    }

    private GetMessageResponse toMessageResponse(Message msg) {
        User user = userRepository.findById(msg.getUser().getUserId()).orElse(null);
        return new GetMessageResponse(
                user.getUserId(),
                user.getNickname(),
                fileService.getFileLink(user),
                msg.getContent(),
                null,
                msg.getCreatDate(),
                msg.getReadNot(),
                msg.getChatroom().getChatId()
        );
    }

    private GetMessageResponse toMessageResponse(ChatFile chatFile) {
        User user = userRepository.findById(chatFile.getUser().getUserId()).orElse(null);
        return new GetMessageResponse(
                user.getUserId(),
                user.getNickname(),
                fileService.getFileLink(user),
                null,
                fileService.getFileLink(chatFile.getChatFileId()),
                chatFile.getCreatDate(),
                chatFile.getReadNot(),
                chatFile.getChatroom().getChatId()
        );
    }


    /*
     *  대화 내역 조회 .-≡=★
     */
    public List<GetMessageResponse> getMsgList(Long chatId, User user) {
        return messageRepository.findMsgsFilesByChatId(chatId, user.getUserId()).stream()
                .map(msg -> toMessageResponse(msg))
                .collect(Collectors.toList());
    }


    /*
     *  메시지 저장 .-≡=★
     */
    public List<GetMessageResponse> saveMessage(AddMessageRequest messageRequest) {
        List<GetMessageResponse> messages = new ArrayList<>();

        User user = userRepository.findById(messageRequest.getUserId()).orElse(null);
        Chatroom chatroom = chatroomRepository.findById(messageRequest.getChatId()).orElse(null);

        /* 메시지 저장 */
        Message message = Message.createMessage(messageRequest, user, chatroom);
        message.setReadNot(chatPartRepository.countAllByChatroom(chatroom));
        Message saved = messageRepository.save(message);
        messages.add(toMessageResponse(saved));
        return messages;
    }


    /*
     *  파일 저장 .-≡=★
     */
    public List<GetMessageResponse> saveChatFiles(User user, Long chatId, List<MultipartFile> files) {
        List<GetMessageResponse> messages = new ArrayList<>();

        Chatroom chatroom = chatroomRepository.findById(chatId).orElse(null);

        /* 파일 저장 */
        for (MultipartFile file : files) {
            String savedName = fileService.uploadFile(file);
            ChatFile chatFile = ChatFile.createChatFile(savedName, user, chatroom, bucket);
            chatFile.setReadNot(chatPartRepository.countAllByChatroom(chatroom));
            ChatFile saved = chatFileRepository.save(chatFile);
            messages.add(toMessageResponse(saved));
        }

        return messages;
    }
}
