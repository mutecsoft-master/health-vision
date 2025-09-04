package com.mutecsoft.healthvision.api.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.exception.CustomException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/main")
public class WebMainController {

    //Main 페이지
    @GetMapping
    public ModelAndView MainPage(ModelAndView mav) {
        return mav;
    }

    //TODO[csm] 삭제 예정 - web 예외처리 메시지 테스트
    @GetMapping("/test")
    public ModelAndView test(ModelAndView mav) throws CustomException {
    	
    	throw new CustomException(ResultCdEnum.E001.getValue());
    	
    }
}
