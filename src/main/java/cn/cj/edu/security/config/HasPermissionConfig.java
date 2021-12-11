package cn.cj.edu.security.config;


import cn.cj.edu.security.exception.TokenIsErrorException;
import cn.cj.edu.security.exception.TokenIsExpiredException;
import cn.cj.edu.security.exception.TokenIsNullException;
import cn.cj.edu.security.exception.TokenUserIsNullException;
import cn.cj.edu.security.utils.JwtUtils;
import cn.cj.edu.security.utils.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import cn.cj.edu.security.annotation.HasPermission;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class HasPermissionConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SimpleCheckRoleAndPermission simpleCheckRoleAndPermission;

    @Autowired
    SecurityDataSource securityDataSource;


    @Pointcut("@annotation(cn.cj.edu.security.annotation.HasPermission)")
    public void hasPermission(){}

    @Around("@annotation(hasPermission)")
    public Object Interceptor(ProceedingJoinPoint pjp,HasPermission hasPermission){
        Object result = null;
        // 获取请求参数
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequest= (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequest.getRequest();
        String token = request.getHeader("token");
        // 拦截暂未登录或已过期
        if (token == null){
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
        // 判断用户是否有具体权限
        String username = JwtUtils.getUsername(token);
        // boolean flag = userService.checkUserPermission(username, value);
        boolean flag = false;
        // 这里要处理注解有多个的方法的使用
        // 有一个权限标识符
       if (hasPermission.value() !=null && !"".equals(hasPermission.value())){
           flag = hasOne(username,hasPermission.value());
       }
        // 有一个权限标识符
        if (hasPermission.hasOne() !=null && !"".equals(hasPermission.hasOne())){
            flag = hasOne(username,hasPermission.value());
        }
        // 有多个权限标识符
        if (hasPermission.hasMuch() !=null && hasPermission.hasMuch().length != 0){
            flag = hasMuch(username,hasPermission.hasMuch());
        }
        // 至少包含一个  (多个权限标识符肿包含一个 就给通过)
        if (hasPermission.hasContainsOne() !=null && hasPermission.hasContainsOne().length != 0){
            flag = hasContainsOne(username,hasPermission.hasContainsOne());
        }

        if (!flag){
            return securityDataSource.handleNonePermission();
        }
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    /**
     * 处理默认、第一种方法
     * @param username
     * @param value
     * @return
     */
    private boolean hasOne(String username,String value){
        return  simpleCheckRoleAndPermission.checkUserPermission(username, value);
    }

    /**
     * 处理多个权限标识符是否都有
     * @param username
     * @param values
     * @return
     */
    private boolean hasMuch(String username,String[] values){
        for (String value : values) {
            if (!simpleCheckRoleAndPermission.checkUserPermission(username,value)){
                return false;
            }
        }
        return true;
    }


    /**
     * 至少包含一个
     * @param username
     * @param values
     * @return
     */
    private boolean hasContainsOne(String username,String[] values){
        for (String value : values) {
            if (simpleCheckRoleAndPermission.checkUserPermission(username,value)){
                return true;
            }
        }
        return false;
    }



}
