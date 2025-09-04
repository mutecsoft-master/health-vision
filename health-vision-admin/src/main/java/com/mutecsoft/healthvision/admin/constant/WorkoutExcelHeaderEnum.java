package com.mutecsoft.healthvision.admin.constant;

//데이터관리 - 운동 Excel Header
public enum WorkoutExcelHeaderEnum implements HealthExcelHeaderMeta {

	ACTIVITY_TYPE("activityType", "activity type", null),
	DURATION("duration", "duration", null),
	START_DT("startDt", "start dt", null),
	END_DT("endDt", "end dt", null),
	BURNED_CALORIES("burnedCalories", "burned calories", null),
    ;
	
	private final String key;
    private final String header;
    private final Integer width;

    WorkoutExcelHeaderEnum(String key, String header, Integer width) {
        this.key = key;
        this.header = header;
        this.width = width;
    }

    public String getKey() { return key; }
    public String getHeader() { return header; }
    public Integer getWidth() { return width; }

}