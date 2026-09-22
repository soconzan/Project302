package org.example.project302.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.notification.dto.FCMSendDto;
import org.example.project302.notification.service.FCMService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationApiController {

    private final FCMService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody FCMSendDto fcmSendDto) {
        try {
            fcmService.sendMessageTo(fcmSendDto);
            return ResponseEntity.ok("Message sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message.");
        }
    }
}
