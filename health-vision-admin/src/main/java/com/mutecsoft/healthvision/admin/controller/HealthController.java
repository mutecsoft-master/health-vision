package com.mutecsoft.healthvision.admin.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mutecsoft.healthvision.admin.service.HealthService;
import com.mutecsoft.healthvision.common.dto.admin.AdminBgDto.SearchBgFile;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.BgFileResponse;
import com.mutecsoft.healthvision.common.util.DatatableUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/health")
public class HealthController {

    private final HealthService healthService;
    private final DatatableUtil datatableUtil;

    //데이터관리
    @GetMapping("/bgFileList")
    public ModelAndView bgFileList(ModelAndView mav) {
    	
        mav.setViewName("bg/bgFileList");
        
        return mav;
    }
    
    @GetMapping("/bgFileList/data")
    public Map<String, Object> getBgFileList(Pageable pageable,
    		@ModelAttribute SearchBgFile searchParam) {
    	
    	PageImpl<BgFileResponse> bgFileListPage = healthService.selectBgFileListPage(searchParam, pageable);
    	Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(bgFileListPage, searchParam.getDraw());
    	
        return resultMap;
    }
    
    
    @PostMapping("/data/download")
    public void downloadData(@RequestParam("bgFileId") Long bgFileId, HttpServletResponse response) throws IOException {
    	
    	healthService.downloadData(bgFileId, response);
    	
    }
    
}
