package com.mutecsoft.healthvision.common.config;

import java.util.Arrays;
import java.util.Collections;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.WhitespaceRule;

import com.mutecsoft.healthvision.common.annotation.Password;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		
		PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(
                        new LengthRule(8, 32), //비밀번호 길이가 8~32 사이여야 한다.
                        new MixedCaseRule(1), //적어도 하나의 대소문자가 있어야 한다. (커스텀 룰)
//                        new CharacterRule(EnglishCharacterData.UpperCase, 1), //적어도 하나의 대문자가 있어야 한다.
//                        new CharacterRule(EnglishCharacterData.LowerCase, 1), //적어도 하나의 소문자가 있어야 한다.
                        new CharacterRule(EnglishCharacterData.Digit, 1), //적어도 하나의 숫자가 있어야 한다.
                        new CharacterRule(EnglishCharacterData.Special, 1), //적어도 하나의 특수문자가 있어야 한다.
                        new WhitespaceRule() //공백문자는 허용하지 않는다.
                )
        );
		
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }else {
            //context.disableDefaultConstraintViolation();
        	//실패 메시지 나열
            //context.buildConstraintViolationWithTemplate(String.join(",", passwordValidator.getMessages(result)))
            //        .addConstraintViolation();
            return false;
        }
	}
	
	private static class MixedCaseRule implements Rule {
		private final int min;
		public MixedCaseRule(int min) {
		    this.min = min;
		}
		
		@Override
		public RuleResult validate(PasswordData passwordData) {
		    String password = passwordData.getPassword();
		    long count = password.chars()
		        .filter(ch -> Character.isUpperCase(ch) || Character.isLowerCase(ch))
		        .count();
		
		    RuleResult result = new RuleResult();
		    if (count >= min) {
		        result.setValid(true);
		    } else {
		        result.setValid(false);
		        //메시지 key (다국어 처리용)
		        //result.setDetails(Collections.singletonList(
		        //    new RuleResultDetail("INSUFFICIENT_MIXED_CASE", Collections.singletonMap("min", String.valueOf(min)))
		    	//));
		    }
		return result;
    }

    @Override
    public String toString() {
        return String.format("MixedCaseRule(%d)", min);
        }
    }
	

}
