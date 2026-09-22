package org.example.project302.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.file.service.FileService;
import org.example.project302.univ.University;
import org.example.project302.user.dto.EditprofileReq;
import org.example.project302.user.dto.UpdateEvent;
import org.example.project302.user.service.CalendarService;
import org.example.project302.user.service.MypageService;
import org.example.project302.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MypageController {
    private final CalendarService calendarService;
    private final MypageService mypageService;
    private final UserService userService;
    private final FileService fileService;

    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(Principal principal,
                         Model model) {
        model.addAttribute("editProfile",mypageService.getEditprofile(principal));
        return "users/mypage/mypage";
    }

    @GetMapping("/events")
    @ResponseBody
    public List<Map<String, Object>> getEvents(Principal principal) {
        return calendarService.getEvents(principal);
    }

    @GetMapping("/calendar/subscribeDate")
    @ResponseBody
    public String getSubcribeDate(Principal principal) {
        return calendarService.getSubscribeDate(principal);
    }

    @PostMapping("/updateEvent")
    @ResponseBody
    public String updateEvent(@RequestBody List<UpdateEvent> eventData) {
        calendarService.updateSchedule(eventData);
        // eventData를 사용하여 받은 데이터를 처리합니다.
        eventData.forEach(event -> {
            System.out.println("Received event ID: " + event.getId());
            System.out.println("Received event start: " + event.getStart());
        });
        return "Event data received successfully!";
    }

    // 프로필 이미지 등록
    @PostMapping("/uploadProfileImage")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file,
                             Principal principal) {
        log.info("file = {}",file);
        return mypageService.profileUpload(file, principal);

    }

    @PostMapping("/deleteProfileImage")
    @ResponseBody
    public String fileDelete(Principal principal) {
        return mypageService.profileDelete(principal);
    }



    @PostMapping("/mypage/editProfile")
    @ResponseBody
    public String editProfile(@RequestBody EditprofileReq currentProfile,
                              Principal principal) {
        log.info("currentProfile = {}", currentProfile);
         mypageService.updateProfile(currentProfile, principal);
        return "redirect:/mypage";
    }

    // 대학명 검색
    @PostMapping("/mypage/univNameSearch")
    @ResponseBody
    public List<University> univNameCheck(@RequestParam("univName") String univName) throws IOException {
        log.info("univName = {}",mypageService.univNameSearch(univName));
        return mypageService.univNameSearch(univName);
//        return true;
    }

}
