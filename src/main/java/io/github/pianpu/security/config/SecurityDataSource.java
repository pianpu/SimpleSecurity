package io.github.pianpu.security.config;

import io.github.pianpu.security.entity.SimplePermission;
import io.github.pianpu.security.entity.SimpleRole;
import io.github.pianpu.security.entity.SimpleUser;

import java.util.List;



public interface SecurityDataSource {


    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 结果
     */
    SimpleUser findUserByUserName(String username);

    /**
     * 查询该用户用于什么角色
     * @param username 用户名
     * @return 结果
     */
    List<SimpleRole> findUserRoleByUserName(String username);

    /**
     * 查询该用户用于什么权限
     * @param username 用户名
     * @return 结果
     */
    List<SimplePermission> findUserPermissionByUserName(String username);

    /**
     * 处理Token为空
     * @return 结果
     */
    Object handleTokenIsNull();


    /**
     * 错误的Token凭证
     * @return 结果
     */
    Object handleTokenIsError();

    /**
     * Token已过期
     * @return 结果
     */
    Object handleTokenExpired();

    /**
     * 当前用户已不存在数据库。可能被删除了。导致该token无效
     * @return 结果
     */
    Object handleTokenUserIsNull();

    /**
     * 无权限处理
     * @return 结果
     */
    Object handleNonePermission();

    /**
     * 无角色处理
     * @return 结果
     */
    Object handleNoneRole();

    /**
     * 登录异常
     * @return 结果
     */
    Object handleLoginError();


}
