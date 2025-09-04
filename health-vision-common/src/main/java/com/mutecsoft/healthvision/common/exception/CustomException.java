package com.mutecsoft.healthvision.common.exception;

public class CustomException extends RuntimeException {
	
	private final String errorCd;
	private final Object[] args;
	
	public CustomException() {
		super();
		this.errorCd = null;
		this.args = null;
	}
	
    public CustomException(String errorCd) {
        super();
        this.errorCd = errorCd;
		this.args = null;
    }
    
    public CustomException(String errorCd, String message) {
        super(message);
        this.errorCd = errorCd;
		this.args = null;
    }

    public String getErrorCd() {
    	return errorCd;
    }

	public Object[] getArgs() {
		return args;
	}
}
