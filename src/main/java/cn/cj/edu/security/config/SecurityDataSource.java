package cn.cj.edu.security.config;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;
import org.springframework.stereotype.Component;

import java.util.List;



public interface SecurityDataSource {


    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    SimpleUser findUserByUserName(String username);

    /**
     * 查询该用户用于什么角色
     * @param username
     * @return
     */
    List<SimpleRole> findUserRoleByUserName(String username);

    /**
     * 查询该用户用于什么权限
     * @param username
     * @return
     */
    List<SimplePermission> findUserPermissionByUserName(String username);

    /**
     * 处理Token为空
     * @return
     */
    Object handleTokenIsNull();


    /**
     * 错误的Token凭证
     * @return
     */
    Object handleTokenIsError();

    /**
     * Token已过期
     * @return
     */
    Object handleTokenExpired();

    /**
     * 当前用户已不存在数据库。可能被删除了。导致该token无效
     * @return
     */
    Object handleTokenUserIsNull();

    /**
     * 无权限处理
     * @return
     */
    Object handleNonePermission();

    /**
     * 无角色处理
     * @return
     */
    Object handleNoneRole();

}
