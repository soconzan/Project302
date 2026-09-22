package org.example.project302.univ;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/univs")
public class UnivController {
    private final UniversityService univService;

    @GetMapping("/coordinate")
    public String returnCoordinate(Model model) {
        List<University> univs = univService.getAllUniv();
        model.addAttribute("univs", univs);
        return "univs/coordinate";
    }
}
