package com.mutecsoft.healthvision.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.mutecsoft.healthvision.api.controller.api")
public class ApiGlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ResponseDto> handleCustomException(CustomException e) {
    	
    	String msg = "";
     	if(e.getMessage() != null) {
     		msg = e.getMessage();
     	}
    	
        ResponseDto responseDto = new ResponseDto(false, msg, e.getErrorCd());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
    
    //BindException 보다 윗줄에 정의할 것.
    //@RequestBody + @Valid 요청 실패 시 동작
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        
        String errorCode = fieldError != null ? fieldError.getDefaultMessage() : ResultCdEnum.E006.getValue(); //기본 코드 

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
    
    
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseDto> handleException(Exception e) {
        log.error("## Exception 발생 : {}", e.getMessage());
        ResponseDto responseDto = new ResponseDto(false, "An unexpected error occurred", ResultCdEnum.E001.getValue());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
    
}
