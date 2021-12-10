package cn.cj.edu.security.config;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;

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

}
