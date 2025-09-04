package com.mutecsoft.healthvision.admin.constant;

//데이터관리 - 식이 Excel Header
public enum FoodDiaryExcelHeaderEnum implements HealthExcelHeaderMeta {

	FOOD_NAME("foodNm", "food name", null),
	CALORIES("calories", "calories", null),
	FOOD_DESC("foodDesc", "food desc", null),
	MEAL_TYPE_NM("mealTypeNm", "meal type", null),
	MEAL_DT("mealDt", "meal dt", null),
    ;
	
	private final String key;
    private final String header;
    private final Integer width;

    FoodDiaryExcelHeaderEnum(String key, String header, Integer width) {
        this.key = key;
        this.header = header;
        this.width = width;
    }

    public String getKey() { return key; }
    public String getHeader() { return header; }
    public Integer getWidth() { return width; }

}
