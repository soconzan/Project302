package org.example.project302.schedule.api;

import lombok.RequiredArgsConstructor;
import org.example.project302.schedule.dto.GetPartRes;
import org.example.project302.schedule.dto.GetSchdlRes;
import org.example.project302.schedule.service.ScheduleService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleApiController {
    private final UserService userService;
    private final ScheduleService scheduleService;


    /*
     *  거래 일정 전체 조회 (캘린더)
     */
    @GetMapping("")
public ResponseEntity<List<GetSchdlRes>> schedules(Principal principal) {
        User user = userService.findById(principal.getName());
        List<GetSchdlRes> res = scheduleService.selectSchedules(user);
        return ResponseEntity.ok(res);
    }

    /*
     *  거래 일정 한건 조회
     */
    @GetMapping("/{pdId}")
    public ResponseEntity<GetSchdlRes> schedule(Principal principal,
                                                @PathVariable("pdId") Long pdId) {
        User user = userService.findById(principal.getName());
        GetSchdlRes res = scheduleService.getScheduleRes(pdId);
        return ResponseEntity.ok(res);
    }

    /*
     *  거래 현황 조회
     */
    @GetMapping("/{pdId}/dealing")
    public ResponseEntity<List<GetPartRes>> participations(Principal principal,
                                                           @PathVariable("pdId") Long pdId) {
        User user = userService.findById(principal.getName());
        List<GetPartRes> res = scheduleService.getParticipations(user, pdId);
        return ResponseEntity.ok(res);
    }

    /*
     *  출발 알람
     */
    @GetMapping("/{pdId}/dealing/start")
    public ResponseEntity<GetPartRes> start(Principal principal,
                                            @PathVariable("pdId") Long pdId,
                                            @RequestParam Float latitude,
                                            @RequestParam Float longitude) {
        User user = userService.findById(principal.getName());
        GetPartRes res = scheduleService.updateStarted(user, pdId, latitude, longitude);
        return ResponseEntity.ok(res);
    }

    /*
     *  회원 위치 업데이트
     */
    @GetMapping("/{pdId}/dealing/now")
    public ResponseEntity<GetPartRes> now(Principal principal,
                                          @PathVariable("pdId") Long pdId,
                                          @RequestParam Float latitude,
                                          @RequestParam Float longitude) {
        User user = userService.findById(principal.getName());
        GetPartRes res = scheduleService.updateLatLng(user, pdId, latitude, longitude);
        return ResponseEntity.ok(res);
    }

    /*
     *  전달 확인/취소
     */
    @GetMapping("/{pdId}/dealing/taked")
    public ResponseEntity<GetPartRes> taked(Principal principal,
                                            @PathVariable("pdId") Long pdId) {
        User user = userService.findById(principal.getName());
        GetPartRes res = scheduleService.updateTaked(user, pdId);
        return ResponseEntity.ok(res);
    }
}
