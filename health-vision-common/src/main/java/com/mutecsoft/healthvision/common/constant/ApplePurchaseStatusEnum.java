package com.mutecsoft.healthvision.common.constant;

/*구매 상태(apple)
  Apple 소모성 구매는 구매 완료에 대한 알림을 보내주지 않음.
*/
public enum ApplePurchaseStatusEnum {
	//0. Purchased 1. Canceled
	PURCHASED("0", "Purchased"), //완료
	CANCELED("1", "Canceled"); //취소
	
	private final String value;
	private final String desc;
	
	ApplePurchaseStatusEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static ApplePurchaseStatusEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (ApplePurchaseStatusEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
