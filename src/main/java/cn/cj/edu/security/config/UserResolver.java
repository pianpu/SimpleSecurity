package cn.cj.edu.security.config;

import cn.cj.edu.security.annotation.SecurityUser;
import cn.cj.edu.security.entity.SimpleUser;
import cn.cj.edu.security.exception.TokenIsErrorException;
import cn.cj.edu.security.exception.TokenIsExpiredException;
import cn.cj.edu.security.exception.TokenIsNullException;
import cn.cj.edu.security.exception.TokenUserIsNullException;
import cn.cj.edu.security.service.UserService;
import cn.cj.edu.security.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
