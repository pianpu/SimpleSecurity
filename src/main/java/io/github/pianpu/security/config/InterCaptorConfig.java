package io.github.pianpu.security.config;

import io.github.pianpu.security.entity.SimpleUser;
import io.github.pianpu.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@ConditionalOnBean(SecurityDataSource.class)
public class InterCaptorConfig implements WebMvcConfigurer {

    @Autowired
    UserResolver userResolver;

    @Autowired
    UserInterceptor userInterceptor;


    // /**
    //  * 拦截未登录的
    //  *
    //  * @param registry
    //  */
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     InterceptorRegistration registration = registry.addInterceptor(userInterceptor);
    //     registration.addPathPatterns("/**"); // 拦截全部
    //     registration.excludePathPatterns("/demo/login"); // 放行登录接口
    // }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userResolver);
    }
}

/**
 * 拦截全局
 */
@Component
class UserInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired(required = false)
    SecurityDataSource securityDataSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        String token = request.getHeader("token");
        if (token == null || token.length() < 0) {
            response.getWriter().write("请先登录...");
            return false;
        } else if (!jwtUtils.checkTokenIsUser(token)) {
            // 用户不存在
            response.getWriter().write("用户不存在...");
            return false;
        } else if (!jwtUtils.checkTokenExpired(token)) {
            // Token过期
            response.getWriter().write("Token过期...");
            return false;
        }
        String username = jwtUtils.getUsername(token);
        SimpleUser user = securityDataSource.findUserByUserName(username);
        if (user != null) {
            return true;
        }
        return false;
    }
}

/**
 * 自定义注解获取登录参数
 */
//@Component
//class UserResolver implements HandlerMethodArgumentResolver {
//
//
//    @Autowired
//    JwtUtils jwtUtils;
//    @Autowired
//    UserService userService;
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.hasParameterAnnotation(SecurityUser.class);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String token = webRequest.getHeader("token");
//        if (token == null || token.length() < 0) {
//            throw new RuntimeException("请先登录...");
//        } else if (!jwtUtils.checkTokenP(token)) {
//            throw new RuntimeException("Token过期...");
//        }
//        String username = jwtUtils.getUsername(token);
//        SimpleUser user = userService.findUserByUserName(username);
//        return user;
//    }
//}

