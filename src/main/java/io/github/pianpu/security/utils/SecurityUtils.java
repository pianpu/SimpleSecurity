package io.github.pianpu.security.utils;

import io.github.pianpu.security.config.SecurityDataSource;

import io.github.pianpu.security.exception.TokenIsErrorException;
import io.github.pianpu.security.exception.TokenIsExpiredException;
import io.github.pianpu.security.exception.TokenIsNullException;
import io.github.pianpu.security.exception.TokenUserIsNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class SecurityUtils {


    Logger logger = LoggerFactory.getLogger(SecurityUtils.class);


    @Autowired(required = false)
    SecurityDataSource securityDataSource;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 获取当前操作用户
     * @return
     */
    public String getUser(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequest= (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequest.getRequest();
        String token = request.getHeader("token");
        try{
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
        }catch (Exception e){
            return null;
        }
        String username = jwtUtils.getUsername(token);
        return username;
    }
}
