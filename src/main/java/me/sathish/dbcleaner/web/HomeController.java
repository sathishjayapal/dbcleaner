package me.sathish.dbcleaner.web;

import me.sathish.dbcleaner.base.HomeStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private final HomeStatsService homeStatsService;

    public HomeController(final HomeStatsService homeStatsService) {
        this.homeStatsService = homeStatsService;
    }

    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("projectStats", homeStatsService.projectStats());
        return "home/index";
    }

}
