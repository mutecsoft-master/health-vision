package com.mutecsoft.healthvision.api.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mutecsoft.healthvision.api.util.NumberUtils;
import com.mutecsoft.healthvision.common.constant.BgFileTypeCdEnum;
import com.mutecsoft.healthvision.common.constant.BgUnitCdEnum;
import com.mutecsoft.healthvision.common.constant.FileExtenstionCdEnum;
import com.mutecsoft.healthvision.common.constant.ResultCdEnum;
import com.mutecsoft.healthvision.common.dto.CsvDto.BgDataDto;
import com.mutecsoft.healthvision.common.dto.CsvDto.BgFileParseResult;
import com.mutecsoft.healthvision.common.exception.CustomException;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BgFileProcessor {
    private static final int MAX_LINE_SCAN_FOR_HEADER = 10;

    private static final int MEDTRONIC_SKIP_HEADER_LINES = 10;
    private final static String MEDTRONIC_HEADER_KEYWORD = "Medtronic Diabetes CareLink";
    private final static String MEDTRONIC_COL_TIMESTAMP = "Timestamp";
    private final static String MEDTRONIC_COL_GLUCOSE = "BG Reading (mg/dL)";
    private final static String MEDTRONIC_COL_RAW_DEVICE_TYPE = "Raw-Device Type";
    private final static String MEDTRONIC_DATE_PATTERN = "yy/M/d H:mm:ss";

    private final static String DEXCOM_HEADER_KEYWORD = "Dexcom";
    private final static Integer DEXCOM_COL_TIMESTAMP_INDEX = 1;
    private final static Integer DEXCOM_COL_GLUCOSE_INDEX = 7;
    private final static Integer DEXCOM_COL_DEVICE_INFO_INDEX = 5;
    private final static String DEXCOM_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    
    private final static String LIBRE_HEADER_KEYWORD = "Historic Glucose";
    private final static Integer LIBRE_COL_TIMESTAMP_INDEX = 1;
    private final static Integer LIBRE_COL_GLUCOSE_TYPE_INDEX = 2;
    private final static Integer LIBRE_COL_HISTORIC_GLUCOSE_INDEX = 3;
    private final static Integer LIBRE_COL_SCAN_GLUCOSE_INDEX = 4;
    private final static String LIBRE_DATE_PATTERN = "yyyy/MM/dd HH:mm";
    

    // 파일 타입에 맞춰 데이터 추출
    public BgFileParseResult parseByType(MultipartFile file) {
        BgFileParseResult result = new BgFileParseResult();
        List<BgDataDto> data = new ArrayList<>();
        String unit = null;
        String deviceName = null;

        // 1. 파일 확장자 추출
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        // 2. 파일 확장자 검증
        if (!FileExtenstionCdEnum.CSV.getValue().equals(extension)
                && !FileExtenstionCdEnum.TSV.getValue().equals(extension)) {
            throw new CustomException(ResultCdEnum.E101.getValue());
        }

        // 3. 파일 타입 판별 [초반 10줄의 내용을 확인하여 각 파일에 맞는 타입 부여]
        BgFileTypeCdEnum type = bgFileTypeParser(file);

        // 4. 파일의 타입에 맞춰 데이터 추출
        switch (type) {
            case MEDTRONIC:
                data = parseMedtronic(file);
                unit = BgUnitCdEnum.MG_DL.getValue();
                deviceName = extractDeviceForMedtronic(file);
                break;
            case DEXCOM:
                data = parseDexcom(file);
                unit = BgUnitCdEnum.MG_DL.getValue();
                deviceName = extractDeviceForDexcom(file);
                break;
                
            case LIBRE:
                data = parseLibre(file);
                unit = BgUnitCdEnum.MMOL_L.getValue();
                deviceName = "LIBRE"; //파일에 디바이스정보가 없으므로 하드코딩
                break;
            case UNKNOWN:
                throw new CustomException(ResultCdEnum.E102.getValue());
        }

        // 5. 데이터에서 측정 시작일, 종료일 추출
        LocalDate recordStartDate = data.stream().map(BgDataDto::getTimestamp)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .map(LocalDateTime::toLocalDate)
                .orElseThrow(() -> new CustomException(ResultCdEnum.E105.getValue()));

        LocalDate recordEndDate = data.stream().map(BgDataDto::getTimestamp)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .map(LocalDateTime::toLocalDate)
                .orElseThrow(() -> new CustomException(ResultCdEnum.E105.getValue()));

        // 6. 추출 데이터 조립 및 결과 리턴.
        result.setBgDataList(data);
        result.setUnit(unit);
        result.setDeviceName(deviceName);
        result.setRecordStartDate(recordStartDate);
        result.setRecordEndDate(recordEndDate);

        return result;
    }

	private BgFileTypeCdEnum bgFileTypeParser(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            BgFileTypeCdEnum type = BgFileTypeCdEnum.UNKNOWN;

            for (int i = 0; i < MAX_LINE_SCAN_FOR_HEADER; i++) {
                String line = reader.readLine();

                if (line == null) break;

                if (line.contains(MEDTRONIC_HEADER_KEYWORD)) {
                    type = BgFileTypeCdEnum.MEDTRONIC;
                    break;
                } else if (line.contains(DEXCOM_HEADER_KEYWORD)) {
                    type = BgFileTypeCdEnum.DEXCOM;
                    break;
                } else if (line.contains(LIBRE_HEADER_KEYWORD)) {
                	type = BgFileTypeCdEnum.LIBRE;
                    break;
                }
            }

            if (type.equals(BgFileTypeCdEnum.UNKNOWN)) {
                throw new CustomException(ResultCdEnum.E102.getValue());
            }

            return type;
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        }
    }

    // MEDTRONIC 타입의 데이터 추출
    private List<BgDataDto> parseMedtronic(MultipartFile file) {
        List<BgDataDto> result = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream()))
                .withSkipLines(MEDTRONIC_SKIP_HEADER_LINES)
                .build()
        ) {
            String[] headers = reader.readNext();

            if (headers == null) {
                throw new CustomException(ResultCdEnum.E103.getValue());
            }

            int timestampIdx = -1;
            int glucoseIdx = -1;

            for (int i = 0; i < headers.length; i++) {
                if (MEDTRONIC_COL_TIMESTAMP.equals(headers[i].trim())) timestampIdx = i;
                if (MEDTRONIC_COL_GLUCOSE.equals(headers[i].trim())) glucoseIdx = i;
                if (timestampIdx != -1 && glucoseIdx != -1) break;
            }

            if (timestampIdx == -1 || glucoseIdx == -1) {
                throw new CustomException(ResultCdEnum.E104.getValue());
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                String timestamp = line.length > timestampIdx ? line[timestampIdx].trim() : "";
                String glucose = line.length > glucoseIdx ? line[glucoseIdx].trim() : "";

                if (!timestamp.isEmpty() && !glucose.isEmpty()) {
                    BgDataDto dto = new BgDataDto();
                    dto.setTimestamp(LocalDateTime.parse(timestamp,
                            DateTimeFormatter.ofPattern(MEDTRONIC_DATE_PATTERN)));
                    dto.setGlucoseValue(new BigDecimal(glucose));
                    result.add(dto);
                }
            }
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        } catch (CsvValidationException e) {
            throw new CustomException(ResultCdEnum.E108.getValue());
        }

        return result;
    }

    // DEXCOM 타입의 데이터 추출
    private List<BgDataDto> parseDexcom(MultipartFile file) {
        List<BgDataDto> result = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder
                (new InputStreamReader(file.getInputStream()))
                .build()
        ) {
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new CustomException(ResultCdEnum.E103.getValue());
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
            	
            	String timestamp = line.length > DEXCOM_COL_TIMESTAMP_INDEX ? line[DEXCOM_COL_TIMESTAMP_INDEX].trim() : "";
            	String glucose = line.length > DEXCOM_COL_GLUCOSE_INDEX ? line[DEXCOM_COL_GLUCOSE_INDEX].trim() : "";
            	
        		if (!timestamp.isEmpty() && !glucose.isEmpty() && NumberUtils.isNumeric(glucose)) {
                    BgDataDto dto = new BgDataDto();
                    dto.setTimestamp(LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern(DEXCOM_DATE_PATTERN)));
                    dto.setGlucoseValue(new BigDecimal(glucose));
                    result.add(dto);
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
    		log.error("## Dexcom csv data parsing failed : ", e.getMessage());
    		throw new CustomException(ResultCdEnum.E108.getValue());
        } catch (CsvValidationException e) {
            throw new CustomException(ResultCdEnum.E108.getValue());
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        } 

        return result;
    }
    
    
    private List<BgDataDto> parseLibre(MultipartFile file) {
    	
    	List<BgDataDto> result = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder
                (new InputStreamReader(file.getInputStream()))
                .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
                .build()
        ) {
        	reader.readNext(); //첫줄은 'Patient Name' 값이라 건너뛰기
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new CustomException(ResultCdEnum.E103.getValue());
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
            	
            	String type = line.length > LIBRE_COL_GLUCOSE_TYPE_INDEX ? line[LIBRE_COL_GLUCOSE_TYPE_INDEX].trim() : "";
            	String timestamp = line.length > LIBRE_COL_TIMESTAMP_INDEX ? line[LIBRE_COL_TIMESTAMP_INDEX].trim() : "";
            	String glucose = ""; 
            	if(type.equals("0")) { //0 : HISTORIC_GLUCOSE, 1: SCAN_GLUCOSE
            		glucose = line.length > LIBRE_COL_HISTORIC_GLUCOSE_INDEX ? line[LIBRE_COL_HISTORIC_GLUCOSE_INDEX].trim() : "";
            	}else if(type.equals("1")){
            		//측정시간이 같으면서 type이 다른 경우, DB에 유니크 제한 조건에 걸리므로 주석처리함
            		//glucose = line.length > LIBRE_COL_SCAN_GLUCOSE_INDEX ? line[LIBRE_COL_SCAN_GLUCOSE_INDEX].trim() : "";
            	}
            	
        		if (!timestamp.isEmpty() && !glucose.isEmpty() && NumberUtils.isNumeric(glucose)) {
                    BgDataDto dto = new BgDataDto();
                    dto.setTimestamp(LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern(LIBRE_DATE_PATTERN)));
                    dto.setGlucoseValue(new BigDecimal(glucose));
                    result.add(dto);
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
    		log.error("## Libre csv data parsing failed : ", e.getMessage());
    		throw new CustomException(ResultCdEnum.E108.getValue());
        } catch (CsvValidationException e) {
            throw new CustomException(ResultCdEnum.E108.getValue());
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        } 

        return result;
	}
    

    // Medtronic 파일에서 디바이스 정보 추출
    private String extractDeviceForMedtronic(MultipartFile file) {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream()))
                .withSkipLines(MEDTRONIC_SKIP_HEADER_LINES)
                .build()
        ) {
            String[] headers = reader.readNext();

            if (headers == null) {
                throw new CustomException(ResultCdEnum.E103.getValue());
            }

            int deviceIdx = -1;

            for (int i = 0; i < headers.length; i++) {
                if (MEDTRONIC_COL_RAW_DEVICE_TYPE.equals(headers[i].trim())) {
                    deviceIdx = i;
                    break;
                }
            }

            if (deviceIdx == -1) {
                throw new CustomException(ResultCdEnum.E104.getValue());
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                String deviceName = line.length > deviceIdx ? line[deviceIdx].trim() : "";

                if (!deviceName.isEmpty()) {
                    return deviceName;
                }
            }
            throw new CustomException(ResultCdEnum.E106.getValue());
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        } catch (CsvValidationException e) {
            throw new CustomException(ResultCdEnum.E108.getValue());
        }
    }

    // DEXCOM 파일에서 Device 정보 추출
    private String extractDeviceForDexcom(MultipartFile file) {
        try (CSVReader reader = new CSVReaderBuilder
                (new InputStreamReader(file.getInputStream()))
                .build()
        ) {
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new CustomException(ResultCdEnum.E103.getValue());
            }

            int deviceIdx = DEXCOM_COL_DEVICE_INFO_INDEX;

            String[] line;
            while ((line = reader.readNext()) != null) {
                String deviceName = line.length > deviceIdx ? line[deviceIdx].trim() : "";

                if (!deviceName.isEmpty()) {
                    return deviceName;
                }
            }
            throw new CustomException(ResultCdEnum.E106.getValue());
        } catch (IOException e) {
            throw new CustomException(ResultCdEnum.E107.getValue());
        } catch (CsvValidationException e) {
            throw new CustomException(ResultCdEnum.E108.getValue());
        }
    }
}
