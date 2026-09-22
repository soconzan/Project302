package org.example.project302.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.notification.dto.FCMMessageDto;
import org.example.project302.notification.dto.FCMSendDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private String API_URL = "https://fcm.googleapis.com/v1/projects/project302-1daf6/messages:send";
    private final ObjectMapper objectMapper;


    /*
     *  메시지 수신
     */
    public void sendMessageTo(FCMSendDto fcmSendDto) throws IOException {
        String message = makeMessage(fcmSendDto);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("FCM Status code : " + response.statusCode());
            log.info("FCM Status body : " + response.body());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* FCM DTO -> JSON 문자열 .-≡=★ */
    private String makeMessage(FCMSendDto fcmSendDto) throws JsonProcessingException {
        FCMMessageDto fcmMessageDto = FCMMessageDto.builder()   // FCM 메시지 build
                .message(FCMMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FCMMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessageDto);    // FCM 객체 -> JSON 문자열로 변환하여 반환
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/project302-firebase-key.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}