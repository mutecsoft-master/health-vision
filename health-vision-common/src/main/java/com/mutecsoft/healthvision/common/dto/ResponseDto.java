package com.mutecsoft.healthvision.common.dto;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ResponseDto {

    @JsonProperty(value = "isSuccess")
    private boolean isSuccess;
    private String message;
    private Object data;
    
    @JsonInclude(Include.NON_NULL)
    private String resultCd;

    public ResponseDto() {
    }

    public ResponseDto(boolean isSuccess) {
        this.isSuccess = isSuccess;
        setDefaultMessage();
    }

    public ResponseDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        if(StringUtils.hasText(message)) {
        	this.message = message;
        }else {
        	setDefaultMessage();
        }
    }
    
    public ResponseDto(boolean isSuccess, String message, String resultCd) {
        this.isSuccess = isSuccess;
        if(StringUtils.hasText(message)) {
        	this.message = message;
        }else {
        	setDefaultMessage();
        }
        this.resultCd = resultCd;
    }

    public ResponseDto(boolean isSuccess, Object data) {
        this(isSuccess);
        this.data = data;
    }
    
    public ResponseDto(boolean isSuccess, Object data, String resultCd) {
        this(isSuccess);
        this.data = data;
        this.resultCd = resultCd;
    }

    public ResponseDto(boolean isSuccess, String message, @Nullable Object data) {
        this.isSuccess = isSuccess;
        if(StringUtils.hasText(message)) {
        	this.message = message;
        }else {
        	setDefaultMessage();
        }
        this.data = data;
    }
    
    public ResponseDto(boolean isSuccess, String message, @Nullable Object data, String resultCd) {
        this.isSuccess = isSuccess;
        if(StringUtils.hasText(message)) {
        	this.message = message;
        }else {
        	setDefaultMessage();
        }
        this.data = data;
        this.resultCd = resultCd;
    }
    
    private void setDefaultMessage() {
    	if (isSuccess) {
            this.message = "success";
        }else {
        	this.message = "failed";
        }
    }

}
