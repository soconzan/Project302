package org.example.project302.group.service;

import com.sun.tools.jconsole.JConsoleContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.project302.chat.service.ChatService;
import org.example.project302.file.entity.File;
import org.example.project302.file.repository.FileRepository;

import org.example.project302.file.service.FileService;
import org.example.project302.group.dto.*;
import org.example.project302.group.repository.GetGroupList;
import org.example.project302.group.entity.Group;
import org.example.project302.group.repository.GetGroupOttList;
import org.example.project302.group.repository.GetOneGroup;
import org.example.project302.group.repository.GroupRepository;
import org.example.project302.product.dto.GetProductResponse;
import org.example.project302.product.dto.ProductManagementResponse;
import org.example.project302.product.dto.ProductSearchCond;
import org.example.project302.product.entity.Product;
import org.example.project302.product.entity.ProductStatus;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.example.project302.user.service.LikeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final LikeService likeService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("${file.dir}")    // 저장할 폴더 경로
    private String fileDir;

    // Group 전체 조회
    private GetGroupListResponse entityToResponse(GetGroupList group) {
        String formattedClos = formatDday(group.getClos_date());
        String formattedPull = formatRelativeDate(group.getPull_up_date());
        return new GetGroupListResponse(
                group.getUniv_id(),
                group.getPd_id(), // pd_id -> pdId
                group.getPd_name(), // pd_name -> pdName
                group.getPd_content(), // pd_content -> pdContent
                group.getViews(),
                group.getPd_price(),
                group.getCreat_date(), // creat_date -> creatDate
                formattedPull,
                group.getLatitude(),
                group.getLongitude(),
                group.getDetail_addr(), // detail_addr -> detailAddr
                group.getCtgr_name(), // ctgr_name -> ctgrName
                group.getMin_people(), // min_people -> minPeople
                group.getMax_people(), // max_people -> maxPeople
                group.getRound_yn(), // round_yn -> roundYn
                formattedClos,
                fileService.getFileLink(group.getFile_id()),
                group.getThumbnail_yn(),
                group.getUniv_latitude(),
                group.getUniv_longitude(),
                group.getChat_count(),
                group.getDeal_status(),
                group.getPrice_range());
    }

    // Group ott 전체 조회
    private GetGroupOttListResponse ottEntityResponse(GetGroupOttList ott) {
        String formattedClos = formatDday(ott.getD_day());
        String formattedPull = formatRelativeDate(ott.getPull_up_date());
        return new GetGroupOttListResponse(
                ott.getUniv_id(),
                ott.getPd_id(),
                ott.getPd_name(),
                ott.getPd_content(),
                ott.getViews(),
                ott.getPd_price(),
                ott.getCreat_date(),
                formattedPull,
                ott.getDetail_addr(),
                ott.getCtgr_name(),
                ott.getMin_people(),
                ott.getMax_people(),
                ott.getRound_yn(),
                ott.getClos_date(),
                formattedClos,
                ott.getChat_count());
    }

    // Group 한 건 조회
    private GetGroupOneResponse groupOneToEntity(GetOneGroup group) {
        String formattedDday = formatDday(group.getD_day());

        return new GetGroupOneResponse(
                group.getNickname(),
                group.getUniv_id(),
                group.getPd_id(),
                group.getPd_name(),
                group.getDeal_status(),
                group.getPd_content(),
                group.getPd_price(),
                group.getCreat_date(),
                group.getPull_up_date(),
                group.getViews(),
                group.getLatitude(),
                group.getLongitude(),
                group.getDetail_addr(),
                group.getCtgr_name(),
                group.getMin_people(),
                group.getMax_people(),
                group.getRound_yn(),
                formattedDday,
                getScheduleDate(group.getClos_date()),
                fileService.getFileLink(group.getFile_id()),
                group.getUniv_addr(),
                group.getLike_count(),
                group.getChat_count(),
                group.getUser_id()
        );
    }

    // 상품 관리 목록 가져오기
    public ProductManagementResponse manageProductToEntity(Product product) {
        return new ProductManagementResponse(
                product.getUser().getUserId(),
                product.getPdId(),
                formatLocalDateTime(product.getPullUpDate()),
                product.getPullUpCnt(),
                fileService.getFileLink(product),
                product.getDealStatus(),
                product.getPdName(),
                product.getPdPrice(),
                likeService.cntLike(product.getPdId())
        );
    }

    // product 저장 DTO
    public Product prodToEntity(ProductSaveForm product, User user) {
        return new Product(
                null,
                product.getPdName(),
                product.getPdContent(),
                product.getPdPrice(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                0,
                product.getLatitude(),
                product.getLongitude(),
                product.getDetailAddr(),
                null,
                null,
                null,
                null,
                0,
                product.getCategory(),
                user,
                null,
                null
        );
    }

    // 그룹 저장 DTO
    private Group groupToEntity(GroupSaveForm group, Long saved) {
        return new Group(
                saved,
                null,
                group.getMinPeople(),
                group.getMaxPeople(),
                group.isRoundYn(),
                group.getClosDate(),
                null,
                null
        );
    }

    // LocalDateTime 바꾸기
    private String getScheduleDate(LocalDateTime scheduleDate) {
        if (scheduleDate == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return scheduleDate.format(formatter);
    }

    // 마감일 시간
    private String formatDday(Integer date) {
        if (date > 0) {
            return "D-" + date;
        } else if (date == 0) {
            return "D-DAY";
        } else return "마감";
    }

    // 시간 표기 메서드
    private String formatRelativeDate(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(date, now);
        if (seconds < 60) {
            return seconds + "초 전";
        }
        long minutes = ChronoUnit.MINUTES.between(date, now);
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = ChronoUnit.HOURS.between(date, now);
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = ChronoUnit.DAYS.between(date, now);
        return days + "일 전";
    }

    // 상품 관리 화면에서 보여지는 날짜 포맷팅               == 실례합니다
    private String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    // 전체 조회
    public Page<GetGroupListResponse> getGroupList(Long univ_id, Pageable pageable, Float latitude, Float longitude, int distance, Float defaultLatitude, Float defaultLongitude, Long ctgr_id, String sort, String keyword) {
        // 초기 -> 대학 좌표, 사용자 위치 변경 -> 중심 좌표
        Float searchLatitude = (latitude != null) ? latitude : defaultLatitude;
        Float searchLongitude = (longitude != null) ? longitude : defaultLongitude;

        // sort
        Sort sortCriteria = getSortCriteria(sort);

        Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortCriteria);

        // 페이지 객체
        Page<GetGroupList> groupPage = groupRepository.findByGroup(univ_id, sortedPage, searchLatitude, searchLongitude, distance, ctgr_id, keyword);
        // List로 반환되는 객체
        List<GetGroupListResponse> group = groupPage.stream()
                .filter(g -> Boolean.TRUE.equals(g.getThumbnail_yn()))
                .filter(g -> g.getDeal_status().toString().equals("SELL"))
                .map(this::entityToResponse)
                .collect(Collectors.toList());
        // Page 인터페이스를 사용하여 페이징된 데이터 직접 반환
        return new PageImpl<>(group, pageable, groupPage.getTotalElements());
    }

    // ott 전체 조회
    public Page<GetGroupOttListResponse> getGroupOttList(Long univ_id, Pageable pageable, String keyword) {

        Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<GetGroupOttList> ottPage = groupRepository.findByGroupOtt(univ_id, keyword, sortedPage);

        List<GetGroupOttListResponse> ott = ottPage.stream()
                .map(this::ottEntityResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(ott, pageable, ottPage.getTotalElements());
    }

    // 메인 전체 조회
    public Page<GetGroupListResponse> getGroupMainSearch(Long univ_id, Pageable pageable, String keyword) {
        // 초기 -> 대학 좌표, 사용자 위치 변경 -> 중심 좌표

        Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<GetGroupList> mainPage = groupRepository.findByGroup(univ_id, sortedPage, 0F, 0F, Integer.MAX_VALUE, null, keyword);

        // List로 반환되는 객체
        List<GetGroupListResponse> group = mainPage.getContent().stream()
                .filter(g -> !g.getDeal_status().toString().equals("HIDE"))
                .map(g -> {
                    GetGroupListResponse res = entityToResponse(g);
                    res.setFileId(fileService.getFileLink(productRepository.findById(g.getPd_id()).orElse(null)));
                    return res;
                })
                .collect(Collectors.toList());

        // Page 인터페이스를 사용하여 페이징된 데이터 직접 반환
        return new PageImpl<>(group, pageable, mainPage.getTotalElements());
    }

    // 필터링 조건
    public Sort getSortCriteria(String sort) {
        switch (sort) {
            case "views":
                return Sort.by(Sort.Direction.DESC, "p.views");
            case "clos_date":
                return Sort.by(Sort.Direction.ASC, "g.clos_date");
            case "chat_count":
                return JpaSort.unsafe(Sort.Direction.DESC, "COALESCE(COUNT(cp.chat_id), 0)");
            default:
                return Sort.by(Sort.Direction.DESC, "p.creat_date");
        }
    }


    // 작성
    @Transactional
    public void addGroup(ProductSaveForm productForm, GroupSaveForm group, User user, List<String> imgPaths) throws IOException {

        Product product = prodToEntity(productForm, user);

        Product saved = productRepository.save(product);

        List<String> imgs = new ArrayList<>();
        File file = new File();
        for (String i : imgPaths) {
            if (imgPaths.get(0).equals(i)) {
                file = new File(i, null, true, saved);
            } else {
                file = new File(i, null, false, saved);
            }
            imgs.add(file.getFilePath());
            fileRepository.save(file);
        }
        System.out.println(imgs.toString() + "봐");


        // product에 저장된 아이디를 가져오기
        Long pdId = saved.getPdId();

        // DTO에 아이디랑 group 다 넣기
        Group groupSaved = groupToEntity(group, pdId);

        Group result = groupRepository.save(groupSaved);
        System.out.println(result + "그룹");

        // 실례 - 공구 등록시 채팅방 자동 생성
        chatService.createGroupChat(saved, user);
    }

    // 한 건 조회
    @Transactional
    public GetGroupOneResponse getGroupOneList(Long univ_id, Long id) {
        int updated = productRepository.updateView(id);
        if (updated > 0) {
            return groupOneToEntity(groupRepository.findByPdId(univ_id, id));
        } else return null;
    }

    // 판매자 상품 전체 조회(공동구매 관리)
    public List<ProductManagementResponse> getGroupListByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));
        return productRepository.findAllByUserAndPdStatus(user, ProductStatus.GROUP).stream()
                .map(getProductList -> manageProductToEntity(getProductList))
                .collect(Collectors.toList());
    }

    // 상품 등록자 여부(등록자면 true, 아니면 false)
    public boolean isProductOwner(GetGroupOneResponse group, User user) {
        return group.getUserId() == user.getUserId() ? true : false;
    }

    // 한 건 조회 사진들
    public List<String> getGroupFiles(Long pd_id) {
        List<File> files = fileRepository.findByProduct_PdId(pd_id);

        // File 객체를 FileSaveForm으로 변환하여 리스트에 추가
        List<String> fileSaveUrl = files.stream()
                .sorted(Comparator.comparing(File::isThumbnailYn).reversed())
                .map(file -> fileService.getFileLink(file.getFileId()))
                .collect(Collectors.toList());
        return fileSaveUrl;
    }

    // 삭제
    public void deleteProd(Long pdId) {
        List<File> file = fileRepository.findByProduct_PdId(pdId);
        for (File f : file) {
            fileRepository.delete(f);
            fileService.fileDelete(f.getFileId());
        }
        productRepository.deleteById(pdId);
    }

    // 수정 form
    public GetGroupOneResponse getGroupOneUpdate(Long univ_id, Long pd_id) {
        return groupOneToEntity(groupRepository.findByPdId(univ_id, pd_id));
    }


    // 수정
    @Transactional
    public void updateGroup(ProductSaveForm productSaveForm, GroupSaveForm group, Long id, List<String> imgPaths) {
        Product target = productRepository.findById(id).orElse(null);
        // ott면 좌표값 제외하고 update
        if(target.getCategory().getCtgrId() == 12){
            target.ottUpdate(
                    productSaveForm.getPdName(),
                    productSaveForm.getPdContent(),
                    productSaveForm.getPdPrice(),
                    productSaveForm.getDetailAddr(),
                    productSaveForm.getDealStatus(),
                    productSaveForm.getCategory(),
                    productSaveForm.getViews()
            );
        // 그 외 카테고리는 다 업데이트
        }else{
            target.update(
                    productSaveForm.getPdName(),
                    productSaveForm.getPdContent(),
                    productSaveForm.getPdPrice(),
                    productSaveForm.getLongitude(),
                    productSaveForm.getLatitude(),
                    productSaveForm.getDetailAddr(),
                    productSaveForm.getDealStatus(),
                    productSaveForm.getCategory(),
                    productSaveForm.getViews()
            );
        }

        Product pd = productRepository.save(target);

        List<File> file = fileRepository.findByProduct_PdId(target.getPdId());

        // 기존 파일 수정
        for (String i : imgPaths) {
            boolean thumb = i.equals(imgPaths.get(0));
            boolean exist = false;
            for (File existFile : file) {
                if (existFile.getFileId().equals(i)) {
                    exist = true;
                    existFile.setThumbnailYn(thumb);
                    fileRepository.save(existFile);
                    break;
                }
            }
            // 없는 파일 추가
            if (!exist) {
                File newFile = new File(i, null, thumb, pd);
                fileRepository.save(newFile);
            }
        }

        // 삭제된 파일
        for (File f : file) {
            if (!imgPaths.contains(f.getFileId())) {
                fileRepository.delete(f);
                fileService.fileDelete(f.getFileId());
            }
        }

        Group updateGroup = groupRepository.findById(id).orElse(null);

        // 기존값 update
        updateGroup.update(
                group.getMinPeople(),
                group.getMaxPeople(),
                group.isRoundYn(),
                group.getClosDate()
        );

        Group result = groupRepository.save(updateGroup);
        System.out.println(result + "그룹");
    }

}
