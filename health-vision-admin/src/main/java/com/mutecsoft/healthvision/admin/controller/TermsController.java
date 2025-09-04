package com.mutecsoft.healthvision.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mutecsoft.healthvision.admin.service.TermsService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.RegisterTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.SearchTerms;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.TermsResponse;
import com.mutecsoft.healthvision.common.dto.admin.AdminTermsDto.UpdateTerms;
import com.mutecsoft.healthvision.common.util.DatatableUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/terms")
public class TermsController {

    private final TermsService termsService;
    private final DatatableUtil datatableUtil;

    //약관관리
    @GetMapping("/termsList")
    public ModelAndView termsList(ModelAndView mav) {

        mav.setViewName("terms/termsList");
        return mav;
    }

    //약관목록 조회
    @GetMapping("/termsList/data")
    public Map<String, Object> getTermsList(Pageable pageable,
            @ModelAttribute SearchTerms searchParam) {

        PageImpl<TermsResponse> termsListPage = termsService.selectTermsListPage(searchParam, pageable);
        Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(termsListPage, searchParam.getDraw());

        return resultMap;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> registerTerms(@ModelAttribute RegisterTerms registerTerms) {

    	termsService.registerTerms(registerTerms);
        return ResponseEntity.ok(new ResponseDto(true));
    }

    @PutMapping
    public ResponseEntity<ResponseDto> updateTerms(@ModelAttribute UpdateTerms updateTerms) {

    	termsService.updateTerms(updateTerms);
        return ResponseEntity.ok(new ResponseDto(true));
    }

    @DeleteMapping("/termsList")
    public ResponseEntity<ResponseDto> deleteTermsList(@RequestBody List<Long> termsIdList) {

    	termsService.deleteTermsList(termsIdList);
        return ResponseEntity.ok(new ResponseDto(true));
    }

}
