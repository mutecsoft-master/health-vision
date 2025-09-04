package com.mutecsoft.healthvision.api.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.api.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Common", description = "공통기능")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class CommonController {
	
	private final FileService fileService;

	//해당 URL이 application.yml에 정의되어 있어야 햠
	@Operation(summary = "이미지 출력", description = "application/octet-stream 응답")
	@GetMapping("/img/{imgFileId}")
    public ResponseEntity<?> getImg(
    		@PathVariable("imgFileId") Long fileId
    		, HttpServletRequest request
            , HttpServletResponse response
    		) {
		
		return fileService.getFile(fileId, response);

    }
	
	
}
