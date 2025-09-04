package com.mutecsoft.healthvision.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/main")
public class MainController {

    @GetMapping
    public ModelAndView main(ModelAndView mav) {
        mav.setViewName("main");
        return mav;
    }
}
