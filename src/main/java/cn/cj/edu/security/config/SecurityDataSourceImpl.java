package cn.cj.edu.security.config;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;

import cn.cj.edu.security.utils.LoginUtil;
import cn.cj.edu.security.utils.RedisUtil;
import cn.cj.edu.security.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 重写接口，提供权限数据源
 * 打包的时候需要注释掉。由用户自行重写。
 */
//@Service
public class SecurityDataSourceImpl implements SecurityDataSource{
    Logger logger = LoggerFactory.getLogger(SecurityDataSourceImpl.class);

    @Autowired
    LoginUtil loginUtil;


    @Autowired
    RedisUtil redisUtil;


    @Override
    public SimpleUser findUserByUserName(String username) {
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.setUsername("dddd");
        return simpleUser;
    }

    @Override
    public List<SimpleRole> findUserRoleByUserName(String username) {
        List<SimpleRole> list = new ArrayList<>();
        list.add(new SimpleRole(null,"admin","admin"));
        list.add(new SimpleRole(null,"DDDASDA","DDDASDA"));
        return list;
    }

    @Override
    public List<SimplePermission> findUserPermissionByUserName(String username) {
        SimplePermission simplePermission = new SimplePermission();
        simplePermission.setName("ddd");
        List<SimplePermission> list = new ArrayList<>();
        list.add(new SimplePermission(null,"xxxx","dddd"));
        list.add(new SimplePermission(null,"2131313","dddd"));

        return list;
    }

    @Override
    public Object handleTokenIsNull() {
        return Result.fail("自定义实现接口，token不能为空");
    }

    @Override
    public Object handleTokenIsError() {
        return Result.fail("自定义实现接口，token签名异常无法登录");
    }

    @Override
    public Object handleTokenExpired() {
        return Result.fail("自定义实现接口，token已过期，请重写登录");
    }

    @Override
    public Object handleTokenUserIsNull() {
        return Result.fail("自定义实现接口，当前用户不存在");
    }

    @Override
    public Object handleNonePermission() {
        return Result.fail("自定义实现接口，无权限访问");
    }

    @Override
    public Object handleNoneRole() {
        return Result.fail("自定义实现接口，无角色访问");
    }
}
