package cn.cj.edu.security.config;



import cn.cj.edu.security.annotation.HasRole;
import cn.cj.edu.security.exception.TokenIsErrorException;
import cn.cj.edu.security.exception.TokenIsExpiredException;
import cn.cj.edu.security.exception.TokenIsNullException;
import cn.cj.edu.security.exception.TokenUserIsNullException;
import cn.cj.edu.security.service.UserService;
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

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class HasRoleConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SecurityDataSource securityDataSource;

    @Autowired
    SimpleCheckRoleAndPermission simpleCheckRoleAndPermission;


    @Pointcut("@annotation(cn.cj.edu.security.annotation.HasRole)")
    public void hasRole(){}

    @Around("@annotation(hasRole)")
    public Object Interceptor(ProceedingJoinPoint pjp, HasRole hasRole){
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
        String username = jwtUtils.getUsername(token);
        boolean flag = false;
        // 这里要处理注解有多个的方法的使用
        // 有一个权限标识符
       if (hasRole.value() !=null && !"".equals(hasRole.value())){
           flag = hasOne(username,hasRole.value());
       }
        // 有一个权限标识符
        if (hasRole.hasOne() !=null && !"".equals(hasRole.hasOne())){
            flag = hasOne(username,hasRole.value());
        }
        // 有多个权限标识符
        if (hasRole.hasMuch() !=null && hasRole.hasMuch().length != 0){
            flag = hasMuch(username,hasRole.hasMuch());
        }
        // 至少包含一个  (多个权限标识符肿包含一个 就给通过)
        if (hasRole.hasContainsOne() !=null && hasRole.hasContainsOne().length != 0){
            flag = hasContainsOne(username,hasRole.hasContainsOne());
        }



        if (!flag){
            return securityDataSource.handleNoneRole();
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
        return  simpleCheckRoleAndPermission.checkUserRole(username, value);
    }

    /**
     * 处理多个权限标识符是否都有
     * @param username
     * @param values
     * @return
     */
    private boolean hasMuch(String username,String[] values){
        for (String value : values) {
            if (!simpleCheckRoleAndPermission.checkUserRole(username,value)){
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
            if (simpleCheckRoleAndPermission.checkUserRole(username,value)){
                return true;
            }
        }
        return false;
    }



}
