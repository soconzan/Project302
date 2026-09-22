package org.example.project302.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.project302.notification.service.SqsMessageSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sqs")
public class MainController {

    private final SqsMessageSender messageSender;

    @PostMapping("/message")
    public void sendMessage(@RequestBody String message) {
        messageSender.sendMessage(message);
    }

}