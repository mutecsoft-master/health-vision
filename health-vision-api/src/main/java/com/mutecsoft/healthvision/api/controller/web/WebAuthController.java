package com.mutecsoft.healthvision.api.controller.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/auth")
public class WebAuthController {

    //로그인 페이지
    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView mav, HttpServletRequest request) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	if (authentication != null 
    			&& authentication.isAuthenticated()
    			&& !"anonymousUser".equals(authentication.getPrincipal())) {
    		mav.setViewName("redirect:/web/main"); //이미 로그인된 경우 main으로
    		return mav;
    	}
    	
    	HttpSession session = request.getSession();
    	if (session != null) {
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
            	
                mav.addObject("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }
        }
    	
        mav.setViewName("web/login");
        return mav;
    }

}
