package com.mutecsoft.healthvision.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = "com.mutecsoft.healthvision.api.controller.web")
public class WebGlobalExceptionHandler {

    private final MessageUtil messageUtil;

    @ExceptionHandler(CustomException.class)
    public Object handleBusinessException(HttpServletRequest req, RedirectAttributes redirectAttributes, CustomException e) {

        String msg = "";
        if (StringUtils.hasText(e.getErrorCd())) {
            String messageKey = "web." + e.getErrorCd();
            msg = messageUtil.getMessage(messageKey, e.getArgs());
        }

        if (!StringUtils.hasLength(msg)) {
            msg = messageUtil.getMessage("web.default.error"); //default message
        }

        //AJAX request
        if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
            ResponseDto responseDto = new ResponseDto(false, msg, e.getErrorCd());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        } else {
            // Non-AJAX request
            redirectAttributes.addFlashAttribute("errorMessage", msg);

            String referer = req.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/web/main");
        }
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest req, RedirectAttributes redirectAttributes, Exception e) {

        String msg = e.getMessage();
        log.error("## Exception : {}", e.getMessage());

        //AJAX request
        if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
            ResponseDto responseDto = new ResponseDto(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        } else {
            // Non-AJAX request
            redirectAttributes.addFlashAttribute("errorMessage", msg);

            String referer = req.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/web/main");
        }
    }
}
