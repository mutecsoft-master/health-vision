package com.mutecsoft.healthvision.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.common.dto.FileDto.FileInsertDto;
import com.mutecsoft.healthvision.common.model.FileModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    @Value("${file.base-path}")
    private String basePath;

    @Value("${img-api-url}")
    private String imgApiUrl;
    
    //첨부파일 저장
    public FileModel saveFile(FileInsertDto fileDto) throws IOException {

        FileModel fileModel = new FileModel();
        MultipartFile mFile = fileDto.getFile();

        if (mFile != null && !mFile.isEmpty()) {
            String originFiileNm = mFile.getOriginalFilename();
            Long fileSize = mFile.getSize();

            Calendar date = Calendar.getInstance();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
            String currentDate = formatDate.format(date.getTime());
            String directoryName = basePath + "/" + currentDate + "/" + fileDto.getCateCd();

            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryName + "/" + originFiileNm);
            file = rename(file);
            mFile.transferTo(file.toPath());

            String fullPath = currentDate + "/" + fileDto.getCateCd() + "/" + file.getName();    //basePath를 제외한 경로

            fileModel.setOriginFileNm(originFiileNm);
            fileModel.setFileSize(fileSize);
            fileModel.setFilePath(fullPath);
            fileModel.setFileNm(file.getName());
            fileModel.setFileCateCd(fileDto.getCateCd());
            fileModel.setKeyIndex(fileDto.getKeyIndex());
        }

        return fileModel;
    }

    //첨부파일 저장(리스트)
    public List<FileModel> saveFileList(List<FileInsertDto> fileList) throws IOException {

        List<FileModel> fileModelList = new ArrayList<>();

        for (int i = 0; i < fileList.size(); i++) {
            FileInsertDto fileDto = fileList.get(i);

            MultipartFile mFile = fileDto.getFile();

            if (mFile != null && !mFile.isEmpty()) {
                String originFiileNm = mFile.getOriginalFilename();
                Long fileSize = mFile.getSize();

                Calendar date = Calendar.getInstance();
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
                String currentDate = formatDate.format(date.getTime());
                String directoryName = basePath + "/" + currentDate + "/" + fileDto.getCateCd();

                File directory = new File(directoryName);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File file = new File(directoryName + "/" + originFiileNm);
                file = rename(file);
                mFile.transferTo(file.toPath());

                String fullPath = currentDate + "/" + fileDto.getCateCd() + "/" + file.getName();    //basePath를 제외한 경로

                FileModel fileModel = new FileModel();
                fileModel.setOriginFileNm(originFiileNm);
                fileModel.setFileSize(fileSize);
                fileModel.setFilePath(fullPath);
                fileModel.setFileNm(file.getName());
                fileModel.setFileCateCd(fileDto.getCateCd());
                fileModel.setKeyIndex(fileDto.getKeyIndex());
                fileModelList.add(fileModel);
            }
        }
        return fileModelList;
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
        
        throw new RuntimeException("파일 생성에 실패했습니다.");
    }

    private boolean createNewFile(File f) {

        try {
            return !f.exists() && f.createNewFile();
        } catch (IOException e) {
            log.error("## Failed to create file: {}", f.getAbsolutePath());
            return false;
        }
    }

    //기존 파일 삭제
    public void deleteFile(String filePath) throws IOException {

        //보안
        Path normalizedPath = Paths.get(basePath, filePath).normalize();
        if (!normalizedPath.startsWith(Paths.get(basePath).normalize())) {
            log.error("## Invalid file path: Outside of base directory");
        }

        if (!filePath.isEmpty()) {
            Files.deleteIfExists(normalizedPath);
        }

    }

    public String makeImgApiUrl(Long fileId) {
        if (fileId == null) {
            return null;
        }
        return imgApiUrl + "/" + fileId;
    }
    
    public String makeReportApiUrl(String prefixUrl, Long fileId) {
        if (fileId == null) {
            return null;
        }
        return prefixUrl + "/" + fileId;
    }
    
    public void downloadFileByFileModel(FileModel fm, HttpServletResponse response) {
    	try {
    		Path path = Paths.get(basePath, fm.getFilePath());
    		File file = new File(path.normalize().toString());
    		
    		response.setContentType("application/octet-stream");
            response.setContentLength((int)file.length());
    		response.setHeader("Content-disposition", "attachment;filename=\"" + URLEncoder.encode(fm.getOriginFileNm(), "UTF-8") + "\"");
    		
            try (
    			FileInputStream fis = new FileInputStream(file);
    			ServletOutputStream os = response.getOutputStream();
    		) {
            	IOUtils.copy(fis, os);
            }
    	} catch (IOException e) {
        	throw new RuntimeException("파일 다운로드에 실패했습니다.");
		}
	}
    
    
    public static byte[] toZip(Map<String, byte[]> files) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(byteOut)) {

            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                zipOut.write(entry.getValue());
                zipOut.closeEntry();
            }

            zipOut.finish();
            return byteOut.toByteArray();
        }
    }
}
