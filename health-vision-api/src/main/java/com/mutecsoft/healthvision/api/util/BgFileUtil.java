package com.mutecsoft.healthvision.api.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.web.BgFileDto;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.mutecsoft.healthvision.common.model.BgFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BgFileUtil {

    @Value("${file.base-path}")
    private String basePath;

    // bg file 저장
    public BgFile saveBgFile(BgFileDto.BgFileInsertDto bgFileInsertDto) {
        try {
            BgFile bgFile = new BgFile();
            MultipartFile mfile = bgFileInsertDto.getFile();

            if (mfile != null && !mfile.isEmpty()) {
                String originFileNm = mfile.getOriginalFilename();
                Long fileSize = mfile.getSize();

                Calendar date = Calendar.getInstance();
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
                String currentDate = formatDate.format(date.getTime());
                String directoryName = basePath + "/" + currentDate;

                File directory = new File(directoryName);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = new File(directoryName + "/" + originFileNm);
                file = rename(file);
                mfile.transferTo(file.toPath());

                String fullPath = currentDate + "/" + file.getName(); // basePath를 제외한 경로

                bgFile.setFileNm(file.getName());
                bgFile.setFilePath(fullPath);
                bgFile.setOriginFileNm(originFileNm);
                bgFile.setFileSize(fileSize);
                bgFile.setRecordStartDate(bgFileInsertDto.getRecordStartDate());
                bgFile.setRecordEndDate(bgFileInsertDto.getRecordEndDate());
                bgFile.setUserId(bgFileInsertDto.getUserId());
            }

            return bgFile;
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        }
    }

    //파일명 UUID로 변경
    private File rename(File file) {
        String newFileName = UUID.randomUUID().toString();
        String ext = FilenameUtils.getExtension(file.getName());
        newFileName = newFileName + (ext == null ? "" : "." + ext);
        file = new File(file.getParent(), newFileName);

        //파일 생성
        if (createNewFile(file)) {
            return file;
        }
        return null;
    }

    private boolean createNewFile(File f) {
        try {
            return !f.exists() && f.createNewFile();
        } catch (IOException e) {
            log.error("## Failed to create file: {}", f.getAbsolutePath());
            return false;
        }
    }
}
