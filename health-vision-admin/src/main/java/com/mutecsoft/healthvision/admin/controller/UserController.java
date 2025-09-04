package com.mutecsoft.healthvision.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mutecsoft.healthvision.admin.service.UserService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.RegisterAnalystRequest;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.SearchUser;
import com.mutecsoft.healthvision.common.dto.admin.AdminUserDto.UserUpdateRequest;
import com.mutecsoft.healthvision.common.util.DatatableUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserController {

    private final UserService userService;
    private final DatatableUtil datatableUtil;

    //사용자관리
    @GetMapping("/userList")
    public ModelAndView userList(ModelAndView mav) {
    	
        mav.setViewName("user/userList");
        return mav;
    }
    
    @GetMapping("/userList/data")
    public Map<String, Object> getUsers(Pageable pageable,
    		@ModelAttribute SearchUser searchParam) {
    	
    	PageImpl<UserInfo> userListPage = userService.selectUserListPage(searchParam, pageable);
    	Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(userListPage, searchParam.getDraw());
    	
        return resultMap;
    }
    
  	@PatchMapping
  	public ResponseEntity<ResponseDto> updateUser(@RequestBody UserUpdateRequest updateReq) throws IOException {
  		
  		userService.updateUser(updateReq);
  		return ResponseEntity.ok(new ResponseDto(true));
  		
      }
    
    @DeleteMapping("/userList")
    public ResponseEntity<ResponseDto> deleteUserList(@RequestBody List<Long> userIdList) {

        userService.deleteUserList(userIdList);

        return ResponseEntity.ok(new ResponseDto(true));
    }
    
    
    //분석가관리
    @GetMapping("/analystList")
    public ModelAndView analystMng(ModelAndView mav) {
        mav.setViewName("user/analystList");
        return mav;
    }

    @GetMapping("/analystList/data")
    public Map<String, Object> getAnalysts(Pageable pageable,
            @ModelAttribute SearchUser searchParam) {
        PageImpl<UserInfo> analystListPage = userService.selectAnalystListPage(searchParam, pageable);
        Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(analystListPage, searchParam.getDraw());

        return resultMap;
    }

    @PostMapping("/analyst")
    public ResponseEntity<ResponseDto> registerAnalyst(@Valid @RequestBody RegisterAnalystRequest registerAnlystReq) {
    	
    	ResponseDto responseDto = userService.registerAnalyst(registerAnlystReq);
    	
    	return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/analystList")
    public ResponseEntity<ResponseDto> deleteAnalystList(@RequestBody List<Long> userIdList) {
    	
    	userService.deleteAnalystList(userIdList);
    	
    	return ResponseEntity.ok(new ResponseDto(true));
    }
    
}
