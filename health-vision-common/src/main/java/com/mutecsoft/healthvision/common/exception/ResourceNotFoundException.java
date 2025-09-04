package com.mutecsoft.healthvision.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends CustomException {
	
	private final String resourceType;
	private final String key;
	
	public ResourceNotFoundException(String resourceType) {
		this.resourceType = resourceType;
		this.key = "";
	}
	
	public ResourceNotFoundException(String resourceType, String key) {
        this.resourceType = resourceType;
		this.key = key;
    }
	
	public ResourceNotFoundException(String resourceType, String key, String message) {
		super(message);
        this.resourceType = resourceType;
		this.key = key;
    }
}
