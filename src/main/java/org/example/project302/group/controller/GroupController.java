package org.example.project302.group.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project302.category.entity.Category;
import org.example.project302.category.repository.CategoryRepository;
import org.example.project302.file.service.FileService;
import org.example.project302.group.dto.*;
import org.example.project302.group.service.GroupService;
import org.example.project302.product.dto.ProductManagementResponse;
import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.service.ProductService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.LikeService;
import org.example.project302.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final FileService fileService;
    private final LikeService likeService;


    // 전체 조회
    @GetMapping("/groups")
    public String groups(
            Model model,
            Principal user,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(value = "latitude", required = false) Float latitude,
            @RequestParam(value = "longitude", required = false) Float longitude,
            @RequestParam(value = "distance", defaultValue = "1") int distance,
            @RequestParam(value = "ctgr_id", required = false) Long ctgr_id,
            @RequestParam(value = "sort", defaultValue = "creat_date") String sort,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            @RequestParam(value = "keyword", required = false) String keyword){
        User logUser = userService.findById(user.getName());

        // 실례합니다..
        Float univLatitude = logUser.getUniversity().getUnivLatitude();
        Float univLongitude = logUser.getUniversity().getUnivLongitude();

        Page<GetGroupListResponse> group = groupService.getGroupList(logUser.getUniversity().getUnivId(), pageable, latitude, longitude, distance, univLatitude, univLongitude, ctgr_id, sort, keyword);

        model.addAttribute("groups", group);
        model.addAttribute("categories", categoryRepository.findAll());

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX 요청일 경우 프래그먼트만 반환
            return "groups/_groupList";
        } else {
            // 일반 요청일 경우 전체 페이지 반환
            return "groups/groups";
        }
    }

    // OTT 페이지
    @GetMapping("/groups/ott")
    public String ott(Model model,
                      Principal user,
                      @PageableDefault(size = 20) Pageable pageable,
                      @RequestParam(value = "keyword", required = false) String keyword){
        User logUser = userService.findById(user.getName());

        Page<GetGroupOttListResponse> ott = groupService.getGroupOttList(logUser.getUniversity().getUnivId(), pageable, keyword);
        model.addAttribute("otts", ott);
        return "groups/ott";
    }


    // 공구 상품 프래그먼트 로드
    @GetMapping("/groups/manage/{userId}/usedProducts")
    public String loadUsedProductsFragment(@PathVariable Long userId, Model model) {
        List<ProductManagementResponse> usedProducts = groupService.getGroupListByUser(userId);
        model.addAttribute("groups", usedProducts);
        return "groups/_groupManage";
    }

    // 등록된 상품이 없을 때 보여줄 페이지
    @GetMapping("/groups/none")
    public String noneList(){
        return "groups/_noneList";
    }


    // 저장 form
    @GetMapping("/groups/save")
    public String createForm(Model model, Principal user){
        User logUser = userService.findById(user.getName());

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("univ", logUser.getUniversity());

        return "groups/create";
    }


    // 저장
    @PostMapping("/groups/save")
    public String createGroup(@RequestParam("ctgr_id") Long ctgrId, GroupSaveForm group, ProductSaveForm product, @RequestParam("imgList[]") List<MultipartFile> files, Principal user) throws IOException {
        User logUser = userService.findById(user.getName());

        Category category = categoryRepository.findById(ctgrId).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));

        product.setCategory(category);
        product.setPdId(logUser.getUserId());
        List<String> imgPaths;
        if(ctgrId == 12){
            imgPaths = fileService.s3Upload(null);
        }else{
            imgPaths = fileService.s3Upload(files);
        }
        groupService.addGroup(product, group, logUser, imgPaths);


        return "redirect:/groups";
    }

    // 한 건 조회
    @GetMapping("/groups/{pd_id}")
    public String getOneList(@PathVariable("pd_id") Long pd_id, Model model, Principal user){
        User logUser = userService.findById(user.getName());
        GetGroupOneResponse group = groupService.getGroupOneList(logUser.getUniversity().getUnivId(), pd_id);
//
//        if(isUserDeleted(group.getUserId().toString())){
//            return "groups/nonePage";
//        }

        List<String> files = groupService.getGroupFiles((pd_id));
        boolean isOwner = groupService.isProductOwner(group, logUser);  // 상품 등록자인지 확인
        boolean isLiked = likeService.isLikedGroup(group, logUser); // 찜하기 여부 확인
        int follower = productService.getFollower(group.getUserId());
        int cntProduct = productService.getCntProduct(group.getUserId());
        model.addAttribute("group", groupService.getGroupOneList(logUser.getUniversity().getUnivId(), pd_id));
        model.addAttribute("files", groupService.getGroupFiles(pd_id));
        model.addAttribute("isOwner", isOwner); // 판매자 여부
        model.addAttribute("liked", isLiked);   // 찜하기 여부
        model.addAttribute("user", logUser);    // 현재 로그인된 사용자
        model.addAttribute("follower", follower); // 판매자 팔로워 수
        model.addAttribute("cntProduct", cntProduct);   // 판매자 판매 상품 수
        return "groups/getGroup";
    }

    // 삭제한 회원 유무
    private boolean isUserDeleted(String userId){
        User user = userService.findById(userId);
        return user != null;
    }

    // 삭제
    @PostMapping("/groups/delete/{pd_id}")
    public String deleteProd(@PathVariable("pd_id") Long pd_id){
        groupService.deleteProd(pd_id);
        return "redirect:/groups";
    }

    // 상품 상태
    @ModelAttribute("dealStatus")
    public DealStatus[] dealStatuses() {
        return DealStatus.values();
    }

    // 수정 form
    @GetMapping("/groups/updateForm/{pd_id}")
    public String updateForm(@PathVariable("pd_id") Long pd_id, Model model, Principal user){
        User logUser = userService.findById(user.getName());
        model.addAttribute("univ", logUser.getUniversity());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("group", groupService.getGroupOneUpdate(logUser.getUniversity().getUnivId(), pd_id));
        model.addAttribute("files", groupService.getGroupFiles(pd_id));
        model.addAttribute("dealStatus", dealStatuses());
        return "groups/updateForm";
    }


    // 수정
    @PostMapping("/groups/update/{pd_id}")
    public String update(@PathVariable("pd_id") Long pd_id, @RequestParam("ctgr_id") Long ctgrId, GroupSaveForm group, ProductSaveForm product, @RequestParam("imgList[]") List<MultipartFile> files) throws IOException {
        Category category = categoryRepository.findById(ctgrId).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));

        product.setCategory(category);
        // ott는 사진 없어요
        List<String> imgPaths;
        if(ctgrId == 12){
            imgPaths = fileService.s3Upload(null);
        }else{
            imgPaths = fileService.s3Upload(files);
        }


        groupService.updateGroup(product, group, pd_id, imgPaths);

        return "redirect:/groups";
    }
}
