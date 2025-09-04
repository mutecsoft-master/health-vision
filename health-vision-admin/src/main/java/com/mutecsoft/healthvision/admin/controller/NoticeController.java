package com.mutecsoft.healthvision.admin.controller;

import com.mutecsoft.healthvision.admin.service.NoticeService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.NoticeDto.NoticeInfo;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.CreateNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.SearchNotice;
import com.mutecsoft.healthvision.common.dto.admin.AdminNoticeDto.UpdateNotice;
import com.mutecsoft.healthvision.common.util.DatatableUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class NoticeController {

    private final NoticeService noticeService;
    private final DatatableUtil datatableUtil;

    //공지사항관리
    @GetMapping("/noticeList")
    public ModelAndView noticeList(ModelAndView mav) {

        mav.setViewName("notice/noticeList");
        return mav;
    }

    // 공지사항 조회
    @GetMapping("/noticeList/data")
    public Map<String, Object> getNotices(Pageable pageable,
            @ModelAttribute SearchNotice searchParam) {

        PageImpl<NoticeInfo> noticeListPage = noticeService.selectNoticeListPage(searchParam, pageable);
        Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(noticeListPage, searchParam.getDraw());

        return resultMap;
    }

    // 공지사항 작성
    @PostMapping
    public ResponseEntity<ResponseDto> createNotice(@ModelAttribute CreateNotice createNotice) {

        noticeService.createNotice(createNotice);
        return ResponseEntity.ok(new ResponseDto(true));
    }

    // 공지사항 수정
    @PutMapping
    public ResponseEntity<ResponseDto> updateNotice(@ModelAttribute UpdateNotice updateNotice) {

        noticeService.updateNotice(updateNotice);
        return ResponseEntity.ok(new ResponseDto(true));
    }

    // 공지사항 삭제
    @DeleteMapping("/noticeList")
    public ResponseEntity<ResponseDto> deleteNotices(@RequestBody List<Long> noticeIdList) {

        noticeService.deleteNotices(noticeIdList);
        return ResponseEntity.ok(new ResponseDto(true));
    }

}
