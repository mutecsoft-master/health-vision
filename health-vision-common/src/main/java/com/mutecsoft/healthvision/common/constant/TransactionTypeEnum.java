package com.mutecsoft.healthvision.common.constant;

//결제 트랜잭션 타입
public enum TransactionTypeEnum {

	PAYMENT("PAYMENT", "Payment"), //결제
	REFUND("REFUND", "Refund"); //환불
	
	private final String value;
	private final String desc;
	
	TransactionTypeEnum(final String newValue, final String descVal) {
		value = newValue;
		desc = descVal;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescValue(){
		return desc;
	}
	
	public static TransactionTypeEnum fromValue(String val) {
		if (val == null || val.isEmpty())
			return null;
		else {
			for (TransactionTypeEnum e : values())
				if (e.getValue().equals(val))
					return e;
		}
		return null;
	}
	
}
