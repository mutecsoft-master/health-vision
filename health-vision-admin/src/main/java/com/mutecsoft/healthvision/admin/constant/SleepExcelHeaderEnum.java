package com.mutecsoft.healthvision.admin.constant;

//데이터관리 - 수면 Excel Header
public enum SleepExcelHeaderEnum implements HealthExcelHeaderMeta {
	
	CATEGORY("category", "category", null),
	START_DT("startDt", "start dt", null),
	END_DT("endDt", "end dt", null),
    ;
	
	private final String key;
    private final String header;
    private final Integer width;

    SleepExcelHeaderEnum(String key, String header, Integer width) {
        this.key = key;
        this.header = header;
        this.width = width;
    }

    public String getKey() { return key; }
    public String getHeader() { return header; }
    public Integer getWidth() { return width; }
}
