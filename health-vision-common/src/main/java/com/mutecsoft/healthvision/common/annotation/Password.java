package com.mutecsoft.healthvision.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.mutecsoft.healthvision.common.config.PasswordConstraintsValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintsValidator.class)
public @interface Password {
	String message() default "V002";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
