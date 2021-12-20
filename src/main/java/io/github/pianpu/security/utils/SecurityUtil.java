package io.github.pianpu.security.utils;

import io.github.pianpu.security.config.SecurityDataSource;

import io.github.pianpu.security.entity.SimplePermission;
import io.github.pianpu.security.entity.SimpleRole;
import io.github.pianpu.security.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ConditionalOnBean({SecurityDataSource.class})
@Component
public class SecurityUtil {


    Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    // 权限角色授权源
    private static SecurityDataSource securityDataSource;
    @Autowired(required = false)
    public void setSecurityDataSource(SecurityDataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

    // Jwt工具类
    private static JwtUtil jwtUtils;
    @Autowired
    public void setJwtUtils(JwtUtil jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    // Redis工具类
    private static RedisUtil redisUtil;
    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    // 最大登录数 默认一个客户端
    private static Integer maxLoginCount;
    @Value("${pianpu.login-config.maxLoginCount:1}")
    public void setMaxLoginCount(Integer maxLoginCount) {
        this.maxLoginCount = maxLoginCount;
    }

    // token有效期 默认为24小时
    private static Integer validPeriod;
    @Value("${pianpu.login-config.validPeriod:1440}")
    public void setValidPeriod(Integer validPeriod) {
        this.validPeriod = validPeriod;
    }

    /**
     * 获取当前会话用户名
     * @return 用户名
     */
    public static String getUser(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequest= (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequest.getRequest();
        String token = request.getHeader("token");
        try{
            if (token==null || token.length() <0){
                // 处理Token 为空状态
                throw new TokenIsNullException();
            }else if (!JwtUtil.checkToken(token)){
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
            // 以上被拦截到的都处理为NULL
            return null;
        }
        String username = jwtUtils.getUsername(token);
        return username;
    }

    /**
     * 是否具有该角色
     * @param role 角色代码
     * @return true/false
     */
    public static boolean hasRole(String role){
        String username = getUser();
        List<SimpleRole> userRoleByUserName = securityDataSource.findUserRoleByUserName(username);
        for (SimpleRole simpleRole : userRoleByUserName) {
            if (role.equals(simpleRole.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否具有该权限
     * @param permission 权限标识符
     * @return true/false
     */
    public boolean hasPermission(String permission){
        String username = getUser();
        List<SimplePermission> list = securityDataSource.findUserPermissionByUserName(username);
        for (SimplePermission p : list) {
            if (permission.equals(p.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * 登录
     * @param username 用户名
     * @return 结果
     */
    public static String login(String username) {
        String key = "user:" + username;
        String token = jwtUtils.getToken(username);
        if (redisUtil.hasKey(key)) {
            // 我能看懂，但我不知道如何解释
            if (redisUtil.lGetListSize(key) - maxLoginCount >=2) {
                redisUtil.lRemoveAll(key);
            }
            // redisList列表个数超过系统所系统则踢出最先记录的token
            if (redisUtil.lGetListSize(key) >= maxLoginCount) {
                String findOfToken = (String) redisUtil.lGetIndex(key, 0); // 获取上一次的临时登录凭证
                redisUtil.lRemove(key, 0, findOfToken); // 删除一个
            }
        }
        redisUtil.lSet(key, token, validPeriod,"login");
        return token;
    }

    /**
     * 获取当前会话Token
     * @return token
     */
    public static String getToken(){
        try{
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequest= (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequest.getRequest();
            String token = request.getHeader("token");
            return token;
        }catch (Exception e){
            return "";
        }
    }

    /**
     * 注销登录
     */
    public static void logout(){
        String key = "user:" + getUser();
        String token = getToken();
        redisUtil.lRemove(key, 0, token); // 删除一个
    }


    /**
     * 判断当前是否登录
     * @return true/false
     */
    public static boolean isLogin(){
        String user = getUser();
        if (null != user){
            return true;
        }
        return false;
    }



}
