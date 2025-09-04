package com.mutecsoft.healthvision.common.constant;

//구매 상태(google - purchaseState)
public enum GooglePurchaseStatusEnum {
	//0. Purchased 1. Canceled 2. Pending
	PURCHASED("0", "Purchased"), //완료
	CANCELED("1", "Canceled"), //취소
	PENDING("2", "Pending"); //대기
	
	private final String value;
	private final String desc;
	
	GooglePurchaseStatusEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static GooglePurchaseStatusEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (GooglePurchaseStatusEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
