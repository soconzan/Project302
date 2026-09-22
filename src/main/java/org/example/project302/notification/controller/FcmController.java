//package org.example.project302.notification.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.project302.notification.dto.FCMSendDto;
//import org.example.project302.notification.service.FcmService;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/v1/fcm")
//@RequiredArgsConstructor
//@Slf4j
//public class FcmController {
//
//    private final FcmService fcmService;
//
//    @PostMapping("/send")
//    public void pushMessage(
//            @RequestBody @Validated FCMSendDto fcmSendDto) throws IOException {
//        log.debug("[+] 푸시 메시지 전송");
//
//        int result = fcmService.sendMessageTo(fcmSendDto);

//        ApiResponseWrapper<Object> arw = ApiResponseWrapper
//                .builder()
//                .result(result)
//                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
//                .resultMsg(SuccessCode.SELECT_SUCCESS.getMessage())
//                .build();
//        return new ResponseEntity().getStatusCode();
//        썅
//    }
//}
