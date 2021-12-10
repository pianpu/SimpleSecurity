package cn.cj.edu.security.config;

import cn.cj.edu.security.annotation.SecurityUser;
import cn.cj.edu.security.entity.SimpleUser;
import cn.cj.edu.security.service.UserService;
import cn.cj.edu.security.utils.JwtUtils;
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
        if (token == null || token.length() < 0) {
            logger.error("当前Token为空，无法获取操作者用户名");
            return "";
//            throw new RuntimeException("请先登录...");
        } else if (!jwtUtils.checkTokenP(token)) {
            logger.error("当前Token过期或伪造，无法获取操作者用户名");
            return "";
//            throw new RuntimeException("Token过期...");
        }
        String username = jwtUtils.getUsername(token);
        return username;
    }
}
