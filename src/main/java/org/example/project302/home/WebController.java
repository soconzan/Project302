package org.example.project302.home;

import lombok.RequiredArgsConstructor;
import org.example.project302.group.dto.GetGroupListResponse;
import org.example.project302.group.service.GroupService;
import org.example.project302.product.dto.ProductSearchCond;
import org.example.project302.product.entity.Product;
import org.example.project302.product.service.ProductService;
import org.example.project302.user.entity.User;
import org.example.project302.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final UserService userService;
    private final ProductService productService;
    private final GroupService groupService;

    @GetMapping({"/", ""})
    public String index(Principal principal,
                        Model model) {
        System.out.println("principal = " + principal);
        if (principal != null) {
            User user = userService.findById(principal.getName());
            model.addAttribute("certified", user.getUniversity());
            if (user.getUniversity() != null)
                return "redirect:/main";
        }
        return "index";
    }

    @GetMapping("/main")
    public String main(Model model,
                       Principal principal,
                       @PageableDefault(size = 8) Pageable pageable,
                       @RequestParam(value = "latitude", required = false) Float latitude,
                       @RequestParam(value = "longitude", required = false) Float longitude,
                       @RequestParam(value = "distance", defaultValue = "1") int distance,
                       @RequestParam(value = "ctgr_id", required = false) Long ctgr_id,
                       @RequestParam(value = "sort", defaultValue = "creat_date") String sort,
                       @RequestParam(value = "keyword", required = false) String keyword) {
        User logUser = userService.findById(principal.getName());
        Long univId = logUser.getUniversity().getUnivId();
        model.addAttribute("certified", univId);

        ProductSearchCond searchConditions = new ProductSearchCond();
        searchConditions.setUnivId(univId);
        searchConditions.setSorting("date");
        searchConditions.setPdStatus(null); // null로 설정하여 모든 상품 상태를 포함
        // 페이지 요청 생성, 한 페이지당 40개 항목

        Page<Product> products = productService.findProducts(searchConditions, pageable);
        model.addAttribute("products", products);

        // 실례합니다..
        Float univLatitude = logUser.getUniversity().getUnivLatitude();
        Float univLongitude = logUser.getUniversity().getUnivLongitude();

        Page<GetGroupListResponse> group = groupService.getGroupList(logUser.getUniversity().getUnivId(), pageable, latitude, longitude, distance, univLatitude, univLongitude, ctgr_id, "clos_date", keyword);

        model.addAttribute("groups", group);
        return "index";
    }
}
