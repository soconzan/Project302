package org.example.project302.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.schedule.dto.GetPartRes;
import org.example.project302.deal.dto.GetScheduleRes;
import org.example.project302.deal.entity.Participation;
import org.example.project302.deal.repository.ParticipationRepository;
import org.example.project302.file.service.FileService;
import org.example.project302.group.repository.GroupRepository;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.Product;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.schedule.dto.GetSchdlRes;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ProductRepository productRepository;
    private final GroupRepository groupRepository;
    private final ParticipationRepository partRepository;
    private final UserRepository userRepository;
    private final FileService fileService;


    /*
     *  회원별 일정 조회 .-≡=★
     */
    public List<GetSchdlRes> selectSchedules(User user) {
        List<Participation> parts = partRepository.findAllByUserAndProduct_ScheduleDateIsNotNull(user);

        return parts.stream()
                .map(part -> {
                    Product product = productRepository.findById(part.getProduct().getPdId())
                            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + part.getProduct().getPdId()));
                    return new GetSchdlRes(product, fileService.getFileLink(product));
                })
                .collect(Collectors.toList());
    }

    /*
     *  일정 조회
     */
    public GetSchdlRes getScheduleRes(Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        if (product.getScheduleDate() == null)
            new IllegalArgumentException("예정된 일정 없음 : " + pdId);

        return new GetSchdlRes(product, fileService.getFileLink(product));
    }

    /*
     *  참여자 거래 현황 조회 헤
     */
    public List<GetPartRes> getParticipations(User user, Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));

        return partRepository.findAllByProduct(product).stream()
                .map(part -> {
                    if (part.getUser().equals(user))
                        part.getUser().setNickname(user.getNickname() + " (나)");
                    return new GetPartRes(part, fileService.getFileLink(part.getUser()));
                })
                .collect(Collectors.toList());
    }

    /*
     *  출발 알람
     */
    public GetPartRes updateStarted(User user, Long pdId, Float lat, Float lng) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));

        if (product.getDealStatus() != DealStatus.SEND) {
            product.setDealStatus(DealStatus.SEND);
            productRepository.save(product);
        }

        return updateLatLng(user, pdId, lat, lng);
    }

    /*
     *  실시간 위치 알람
     */
    public GetPartRes updateLatLng(User user, Long pdId, Float lat, Float lng) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        Participation participation = partRepository.findByProductAndUser(product, user)
                .orElseThrow(() -> new IllegalArgumentException("참여자 정보 없음 : " + user.getUserId()));

        participation.setLatitudeNow(lat);
        participation.setLongitudeNow(lng);
        Participation saved = partRepository.save(participation);

        return new GetPartRes(saved, fileService.getFileLink(user));
    }

    /*
     *  전달 확인
     */
    public GetPartRes updateTaked(User user, Long pdId) {
        Product product = productRepository.findById(pdId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 없음 : " + pdId));
        Participation participation = partRepository.findByProductAndUser(product, user)
                .orElseThrow(() -> new IllegalArgumentException("참여자 정보 없음 : " + user.getUserId()));

        participation.setTakedDate(participation.getTakedDate() == null ?
                LocalDateTime.now() : null);

        Participation saved = partRepository.save(participation);

        return new GetPartRes(saved, fileService.getFileLink(user));
    }

    /*
     *  모두 전달 확인
     */
    private void productSold(Product product) {
        List<Participation> participations = partRepository.findAllByProduct(product);

        if (participations.stream().allMatch(participation -> participation.getTakedDate() != null)) {
            product.setDealStatus(DealStatus.SOLD);
            productRepository.save(product);
        }
    }
}
