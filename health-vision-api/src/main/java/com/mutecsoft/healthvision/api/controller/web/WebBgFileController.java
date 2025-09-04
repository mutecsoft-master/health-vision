package com.mutecsoft.healthvision.api.controller.web;

import com.mutecsoft.healthvision.api.service.BgFileService;
import com.mutecsoft.healthvision.common.dto.ResponseDto;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.SearchBgFile;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto.UploadRequest;
import com.mutecsoft.healthvision.common.model.BgFile;
import com.mutecsoft.healthvision.common.util.DatatableUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/web/bg")
public class WebBgFileController {

    private final BgFileService bgFileService;
    private final DatatableUtil datatableUtil;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> bgUpload(@ModelAttribute UploadRequest uploadReq) {
        bgFileService.bgFileUpload(uploadReq.getFiles());

        return ResponseEntity.ok(new ResponseDto(true, "혈당 데이터 파일 업로드 완료"));
    }

    @GetMapping("/list")
    public Map<String, Object> getFileList(Pageable pageable,
           @ModelAttribute SearchBgFile searchParam) {

        PageImpl<BgFile> bgFileListPage = bgFileService.getBgFileList(pageable, searchParam);
        Map<String, Object> resultMap = datatableUtil.makeDatatablePagingMap(bgFileListPage, searchParam.getDraw());

        return resultMap;
    }
}
