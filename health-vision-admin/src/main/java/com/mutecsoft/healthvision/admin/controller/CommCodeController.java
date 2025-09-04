package com.mutecsoft.healthvision.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutecsoft.healthvision.admin.service.CommCodeService;
import com.mutecsoft.healthvision.admin.util.UserUtil;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.CommCode;
import com.mutecsoft.healthvision.common.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commCode")
public class CommCodeController {

    private final CommCodeService commCodeService;
    private final UserUtil userUtil;

    @GetMapping
    public ResponseEntity<ResponseDto> getCommCodeList(CommCode commCode) {
        
        List<CommCode> commCodeList = commCodeService.getCommCodeList(commCode);
        ResponseDto responseDto = new ResponseDto(true, commCodeList);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> insertCommCode(@RequestBody CommCode commCode) {
    	
    	UserInfo userInfo = userUtil.getUserInfo();
        commCode.setRegId(userInfo.getUserId());
        
        commCodeService.insertCommCode(commCode);
        
        ResponseDto responseDto = new ResponseDto(true);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateCommCodeByCodeId(@RequestBody CommCode commCode) {
        
    	UserInfo userInfo = userUtil.getUserInfo();
        commCode.setUpdId(userInfo.getUserId());

        commCodeService.updateCommCodeByCodeId(commCode);
        
        ResponseDto responseDto = new ResponseDto(true);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteCommCode(CommCode commCode) {
    	
        commCodeService.deleteCommCode(commCode);
        
        ResponseDto responseDto = new ResponseDto(true);
        return ResponseEntity.ok(responseDto);
    }

}
