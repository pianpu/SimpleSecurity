package io.github.pianpu.security.config;

import io.github.pianpu.security.annotation.SecurityUser;
import io.github.pianpu.security.exception.TokenIsErrorException;
import io.github.pianpu.security.exception.TokenIsExpiredException;
import io.github.pianpu.security.exception.TokenIsNullException;
import io.github.pianpu.security.exception.TokenUserIsNullException;
import io.github.pianpu.security.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



/**
 * 自定义注解获取登录参数
 */

@Component

public class UserResolver implements HandlerMethodArgumentResolver {

    Logger logger = LoggerFactory.getLogger(UserResolver.class);

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SecurityUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("token");
        if (token==null || token.length() <0){
            // 处理Token 为空状态
            throw new TokenIsNullException();
        }else if (!jwtUtils.checkToken(token)){
            // 处理Token 校验不通过状态
            throw new TokenIsErrorException();
        } else if (!jwtUtils.checkTokenIsUser(token)){
            // 处理Token 挂靠用户不存在状态
            throw new TokenUserIsNullException();
        } else if (!jwtUtils.checkTokenExpired(token)){
            // 处理Token 过期不通过状态
            throw new TokenIsExpiredException();
        }
        String username = jwtUtils.getUsername(token);
        return username;
    }
}
