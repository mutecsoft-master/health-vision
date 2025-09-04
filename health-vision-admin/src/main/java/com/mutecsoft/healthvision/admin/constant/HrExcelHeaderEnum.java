package com.mutecsoft.healthvision.admin.constant;

//데이터관리 - 심박 Excel Header
public enum HrExcelHeaderEnum implements HealthExcelHeaderMeta {
	
	VALUE("value", "value", null),
    RECORD_DT("recordDt", "record dt", null),
    ;
	
	private final String key;
    private final String header;
    private final Integer width;

    HrExcelHeaderEnum(String key, String header, Integer width) {
        this.key = key;
        this.header = header;
        this.width = width;
    }

    public String getKey() { return key; }
    public String getHeader() { return header; }
    public Integer getWidth() { return width; }

}
