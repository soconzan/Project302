package org.example.project302.user.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "zhdzhdvksvks@gmail.com";
    private String authCode; // 인스턴스 필드로 선언

    // 인증번호 생성 6자리 영어대문자, 숫자
    public void authCodeMaker() {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) { // 6자리로 수정
            int rIndex = rnd.nextInt(2); // 0 또는 1만 선택하도록 수정
            switch (rIndex) {
                case 0:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 1:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }

        authCode = temp.toString();
        System.out.println(authCode);
    }

    public MimeMessage createMail(String mail) {
        authCodeMaker();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("PROJECT302이메일 인증");
            String body = "";
            body += "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body { font-family: Arial, sans-serif; }" +
                    "        .container { max-width: 600px; margin: 0 auto; }" +
                    "        .header { background-color: #d1e44d; color: #000; padding: 20px; text-align: center; }" +
                    "        .content { padding: 20px; }" +
                    "        .footer { background-color: #f2f2f2; padding: 10px; text-align: center; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>" +
                    "            <h2>PROJECT302 이메일 인증</h2>" +
                    "        </div>" +
                    "        <div class='content'>" +
                    "            <p>Dear User,</p>" +
                    "            <p>인증 번호는:  <strong>" + authCode + "</strong> 입니다.</p>" +
                    "            <p>이 코드를 입력하여 이메일 주소를 인증해주세요.</p>" +
                    "        </div>" +
                    "        <div class='footer'>" +
                    "            <p>PROJECT302를 이용해주셔서 감사합니다.</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            message.setContent(body, "text/html; charset=UTF-8");
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return message;
    }

    public String sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);

        return authCode;
    }
}
