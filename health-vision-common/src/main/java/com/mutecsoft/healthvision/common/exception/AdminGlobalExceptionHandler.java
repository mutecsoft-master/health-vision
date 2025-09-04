package com.mutecsoft.healthvision.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.util.MessageUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = "com.mutecsoft.healthvision.admin")
public class AdminGlobalExceptionHandler {

    private final MessageUtil messageUtil;

    @ExceptionHandler(CustomException.class)
    public Object handleBusinessException(HttpServletRequest req, RedirectAttributes redirectAttributes, CustomException e) {
        
    	String msg = "";
    	if(StringUtils.hasText(e.getErrorCd())) {
    		String messageKey = "web." + e.getErrorCd();
    		msg = messageUtil.getMessage(messageKey, e.getArgs());
    	}
    	
    	if(!StringUtils.hasText(msg)) {
    		msg = messageUtil.getMessage("web.default.error"); //default message
    	}
    	
    	log.error("## CustomException : {}", msg);
    	
    	//AJAX request
    	if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
            ResponseDto responseDto = new ResponseDto(false, msg, e.getErrorCd());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
	    } else {
	        // Non-AJAX request
	    	redirectAttributes.addFlashAttribute("errorMessage", msg);
	        
	        String referer = req.getHeader("Referer");
	        return "redirect:" + (referer != null ? referer : "/admin/main");
	    }
    	
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        
        String errorCode = fieldError != null ? fieldError.getDefaultMessage() : "E006"; //기본 코드 

        ResultCdEnum resultCd = ResultCdEnum.fromValue(errorCode);
        ResponseDto responseDto = new ResponseDto(false, resultCd.getDescValue(), resultCd.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDto> handleBindException(BindException e) {

        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = null;

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            log.error("## [{}] {}", fieldError.getField(), fieldError.getDefaultMessage());
            errorMessage = "[" + fieldError.getField() + "] " + fieldError.getDefaultMessage();
        }

        log.debug("## Bind excpetion : {}", e.getMessage());

        ResponseDto responseDto = new ResponseDto(false, errorMessage, ResultCdEnum.E006.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
    
    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest req, RedirectAttributes redirectAttributes, Exception e) {
    	
    	String msg = e.getMessage();
    	if(!StringUtils.hasText(msg)) {
    		msg = messageUtil.getMessage("web.default.error"); //default message
    	}
        log.error("## Exception : {}", e.getMessage());
        
        //AJAX request
    	if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
            ResponseDto responseDto = new ResponseDto(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
	    } else {
	        // Non-AJAX request
	    	redirectAttributes.addFlashAttribute("errorMessage", msg);
	        
	        String referer = req.getHeader("Referer");
	        return "redirect:" + (referer != null ? referer : "/admin/main");
	    }

    }
    
    
}
