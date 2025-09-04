package com.mutecsoft.healthvision.common.exception;

import com.mutecsoft.healthvision.common.exception.CustomException;

public class AuthenticationFailedException extends CustomException {
	
	public AuthenticationFailedException() {
		super();
	}
	
    public AuthenticationFailedException(String message) {
        super(message);
    }
	
}
