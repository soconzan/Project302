package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.file.service.FileService;
import org.example.project302.group.entity.Group;
import org.example.project302.group.repository.GroupRepository;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.user.dto.UpdateEvent;
import org.example.project302.user.dto.UserScheduleRes;
import org.example.project302.user.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final GroupRepository groupRepository;
    private final UserService userService;

    //
    public List<UserScheduleRes> findByScheduleDateIsNotNull(Long userId) {
        return calendarRepository.findByScheduleDateIsNotNull(userId).stream()
                .map(product -> new UserScheduleRes(product, fileService.getFileLink(product)))
                .collect(Collectors.toList());
    }

    // 일정 가져오기
    public List<Map<String, Object>> getEvents(Principal principal) {
        List<UserScheduleRes> userSchedules = findByScheduleDateIsNotNull(Long.valueOf(principal.getName()));
        List<Map<String, Object>> events = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        for (UserScheduleRes schedule : userSchedules) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", String.valueOf(schedule.getPdId()));
            event.put("title", schedule.getPdName());

            Map<String, Object> extendedProps = new HashMap<>();
            extendedProps.put("detailAddr", schedule.getDetailAddr());
            extendedProps.put("fileLink", schedule.getFileLink());
            extendedProps.put("longitude", schedule.getLongitude());
            extendedProps.put("latitude", schedule.getLatitude());



            if (schedule.getPdStatus() == ProductStatus.GROUP) {
                Group group = groupRepository.findByProduct_PdId(schedule.getPdId()).get();
                extendedProps.put("price",group.getPriceRange());
                event.put("url", "/groups/" + schedule.getPdId());
                event.put("color", "#D1E44D");
            } else {
                extendedProps.put("price",schedule.getPrice());
                event.put("color", "#ffef00");
                event.put("url", "/products/" + schedule.getPdId());
            }

            // Editable, Display, Text Color 설정
            // 판매자가 너니? && 오늘 이후 날짜?
            boolean isEditable = Objects.equals(schedule.getUserId(), Long.valueOf(principal.getName())) && schedule.getScheduleDate().isAfter(LocalDateTime.now());
            event.put("editable", isEditable);
            event.put("display", isEditable ? "block" : "list-item");
            event.put("textColor", isEditable ? "black" : "");

            // Schedule Date 설정
            String formattedDateTime = schedule.getScheduleDate().format(formatter);
            event.put("start", formattedDateTime);

            // Schedule Date가 현재 시간 이전인 경우 Color 및 Editable 설정
            if (schedule.getScheduleDate().isBefore(LocalDateTime.now())) {
                event.put("color", "gray");
                event.put("editable", false);
            }

            event.put("extendedProps", extendedProps);
            events.add(event);
        }

        return events;
    }

    // 일정 변경 저장
    public void updateSchedule(List<UpdateEvent> eventData) {
        List<Product> products = eventData.stream()
                .map(updateEvent -> productRepository.findById(Long.valueOf(updateEvent.getId()))
                        .map(product -> {
                            product.setScheduleDate(updateEvent.convertStringToLocalDateTime());
                            return productRepository.save(product);
                        }))
                .filter(Optional::isPresent) // Filter out Optional.empty()
                .map(Optional::get)
                .collect(Collectors.toList());

    }
    public String getSubscribeDate(Principal principal) {
        return userService.findById(principal.getName()).getSubscribeDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}

