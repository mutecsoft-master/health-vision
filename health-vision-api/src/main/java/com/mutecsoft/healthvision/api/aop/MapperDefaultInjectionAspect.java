package com.mutecsoft.healthvision.api.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mutecsoft.healthvision.api.util.UserUtil;
import com.mutecsoft.healthvision.common.annotation.LoginId;
import com.mutecsoft.healthvision.common.dto.UserDto.UserInfo;
import com.mutecsoft.healthvision.common.model.BaseModel;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class MapperDefaultInjectionAspect {
	
	private final UserUtil userUtil;

	@Around("execution(* com.mutecsoft.healthvision.common.mapper.*Mapper.*(..))") //* 뒤에 공백 필수
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();

        // @LoginId 애너테이션 확인
        if(((MethodSignature) signature).getMethod().getAnnotation(LoginId.class) == null || (args == null || args.length <= 0)) {
            return joinPoint.proceed();
        }

        UserInfo userInfo = userUtil.getUserInfo();
        Long userId = userInfo.getUserId();

        // 파라미터가 CommonVO일 경우 처리
        for(Object obj : args) {
            if(obj == null) continue;
            if(obj instanceof BaseModel) {
            	BaseModel vo = (BaseModel) obj;
                if(vo != null) {
                    vo.setRegId(userId);
                    vo.setUpdId(userId);
                    obj = vo;
                }
            } else if(obj instanceof List) { // 파라미터가 List일 경우 처리
                @SuppressWarnings("unchecked")
                List<BaseModel> list = ((List<BaseModel>) obj);
                if(list != null) {
                    list.forEach(r -> {
                        r.setRegId(userId);
                        r.setUpdId(userId);
                    });
                    obj = list;
                }
            }
        }

        return joinPoint.proceed(); // 메서드 실행
    }
}
