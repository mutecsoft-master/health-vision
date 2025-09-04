package com.mutecsoft.healthvision.admin.constant;

//데이터관리 - 혈당 Excel Header
public enum BgExcelHeaderEnum implements HealthExcelHeaderMeta {
	
	VALUE("value", "value", null),
    UNIT("unit", "unit", null),
    RECORD_DT("recordDt", "record dt", null),
    ;
	
	private final String key;
    private final String header;
    private final Integer width;

    BgExcelHeaderEnum(String key, String header, Integer width) {
        this.key = key;
        this.header = header;
        this.width = width;
    }

    public String getKey() { return key; }
    public String getHeader() { return header; }
    public Integer getWidth() { return width; }

}
