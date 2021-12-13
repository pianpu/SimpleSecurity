package io.github.pianpu.security.config;

import io.github.pianpu.security.entity.SimplePermission;
import io.github.pianpu.security.entity.SimpleRole;
import io.github.pianpu.security.entity.SimpleUser;

import io.github.pianpu.security.utils.LoginUtil;
import io.github.pianpu.security.utils.RedisUtil;
import io.github.pianpu.security.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;



public class DefaultSecurityDataSourceImpl implements SecurityDataSource{
    Logger logger = LoggerFactory.getLogger(DefaultSecurityDataSourceImpl.class);

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
