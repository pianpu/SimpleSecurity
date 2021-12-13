package cn.cj.edu.security.config;

import cn.cj.edu.security.exception.*;
import cn.cj.edu.security.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
@ConditionalOnBean(SecurityDataSource.class)
public class GlobExceptionHandle {

    Logger logger = LoggerFactory.getLogger(GlobExceptionHandle.class);

    @Autowired(required = false)
    SecurityDataSource securityDataSource;

    /**
     * 处理Token 为空的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = TokenIsNullException.class)
    @ResponseBody
    public Object exceptionHandlerTokenIsNullException(TokenIsNullException e){
       return securityDataSource.handleTokenIsNull();
    }

    /**
     * 处理Token 签名错误的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = TokenIsErrorException.class)
    @ResponseBody
    public Object exceptionHandlerTokenIsErrorException(TokenIsErrorException e){
        return securityDataSource.handleTokenIsError();
    }

    /**
     * 处理Token 过期的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = TokenIsExpiredException.class)
    @ResponseBody
    public Object exceptionHandlerTokenIsExpiredException(TokenIsExpiredException e){
        return securityDataSource.handleTokenExpired();
    }

    /**
     * 处理Token 挂靠用户名不存在的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = TokenUserIsNullException.class)
    @ResponseBody
    public Object exceptionHandlerTokenUserIsNullException(TokenUserIsNullException e){
        return securityDataSource.handleTokenUserIsNull();
    }

    /**
     * 处理无权限
     * @param e
     * @return
     */
    @ExceptionHandler(value = NonePermissionException.class)
    @ResponseBody
    public Object exceptionHandlerNonePermissionException(NonePermissionException e){
        return securityDataSource.handleNonePermission();
    }

    /**
     * 处理无角色
     * @param e
     * @return
     */
    @ExceptionHandler(value = NoneRoleException.class)
    @ResponseBody
    public Object exceptionHandlerNoneRoleException(NoneRoleException e){
        return securityDataSource.handleNoneRole();
    }


}
