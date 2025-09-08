package com.mutecsoft.healthvision.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mutecsoft.healthvision.common.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.mutecsoft.healthvision.api.controller.api")
public class ApiGlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ResponseDto> handleCustomException(CustomException e) {
        ResponseDto responseDto = new ResponseDto(false, e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(responseDto);
    }
    
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseDto> handleException(Exception e) {
        ResponseDto responseDto = new ResponseDto(false, "알 수 없는 오류가 발생했습니다.", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
    
}
