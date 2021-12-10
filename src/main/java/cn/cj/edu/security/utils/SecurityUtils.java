package cn.cj.edu.security.utils;

import cn.cj.edu.security.config.SecurityDataSource;

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


    @Autowired
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

        if (token==null || token.length() <0){
            logger.error("当前Token为空，无法获取操作者用户名");
            return "";
//            throw new RuntimeException("请先登录...");
        }else if (!jwtUtils.checkTokenP(token)){
            logger.error("当前Token过期或伪造，无法获取操作者用户名");
//            throw new RuntimeException("Token过期...");
            return "";
        }
        String username = jwtUtils.getUsername(token);
        return username;
    }
}
