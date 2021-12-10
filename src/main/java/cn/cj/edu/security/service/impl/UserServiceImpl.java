package cn.cj.edu.security.service.impl;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;
import cn.cj.edu.security.mapper.UserMapper;
import cn.cj.edu.security.service.UserService;
import cn.cj.edu.security.utils.LoginUtil;
import cn.cj.edu.security.utils.RedisUtil;
import cn.cj.edu.security.utils.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    LoginUtil loginUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Result findUserByUsernameAndPassword(Object user) {
        SimpleUser user2 = (SimpleUser)user;
        SimpleUser userByUsernameAndPassword = userMapper.findUserByUsernameAndPassword((SimpleUser)user);
        if (userByUsernameAndPassword == null){
            logger.warn("账号密码错误," +  user);
            return Result.fail("账号密码错误");
        }
        // 生成用户唯一凭证，且账号有效期由yml所配置
        Map<String, Object> map = loginUtil.addLoginData(user2.getUsername());
        if ((boolean) map.get("flag")){
            return Result.success("登录成功,登录凭证:" + map.get("token"));
        }


        return Result.fail("登录失败，请稍后尝试");
    }

    @Override
    public Integer addUser(SimpleUser user) {
        return userMapper.addUser(user);
    }

    @Override
    public SimpleUser findUserByUserName(String username) {
        return userMapper.findUserUserName(username);
    }

    @Override
    public List<SimpleRole> findUserRoleByUserName(String username) {
        List<SimpleRole> list = userMapper.findUserRoleByUserName(username);
        return list;
    }

    @Override
    public List<SimplePermission> findUserPermissionByUserName(String username) {
        return userMapper.findUserPermissionByUserName(username);
    }

//    @Override
//    public boolean checkUserPermission(String username, String value) {
//        // 先从redis 缓存中拿数据
//        boolean flag = false;
//        String key = "permission:" + username;
//        List<Object> redisList = redisUtil.lGet(key, 0, (int) redisUtil.lGetListSize(key));
//        List<Object> rdCollect = redisList.stream().filter(name -> name.equals(value)).collect(Collectors.toList());
//        if (rdCollect.size() > 0 ){
//            logger.info("本次操作从Redis");
//            return true;
//        }
//        // redis 无缓存数据再从数据库拿数据，且同步到redis中
//        List<Permission> list = findUserPermissionByUserName(username);
//        List<Permission> dbCollect = list.stream().filter(permission -> permission.getName().equals(value)).collect(Collectors.toList());
//        flag = dbCollect.size()>0?true:false;
//        if (flag){
//            // 清空redis
//            redisUtil.lRemoveAll(key);
//            // 同步数据到redis中
//            for (Permission permission : list) {
//                redisUtil.lSet(key,permission.getName());
//            }
//        }
//        logger.info("本次操作从Mysql");
//        return flag;
//    }
//
//    @Override
//    public boolean checkUserRole(String username, String value) {
//        // 先从redis 缓存中拿数据
//        boolean flag = false;
//        String key = "role:" + username;
//        List<Object> redisList = redisUtil.lGet(key, 0, (int) redisUtil.lGetListSize(key));
//        List<Object> rdCollect = redisList.stream().filter(name -> name.equals(value)).collect(Collectors.toList());
//        if (rdCollect.size() > 0 ){
//            logger.info("本次操作从Redis");
//            return true;
//        }
//        // redis 无缓存数据再从数据库拿数据，且同步到redis中
//        List<Role> list = findUserRoleByUserName(username);
//        List<Role> dbCollect = list.stream().filter(role -> role.getName().equals(value)).collect(Collectors.toList());
//        flag = dbCollect.size()>0?true:false;
//        if (flag){
//            // 清空redis
//            redisUtil.lRemoveAll(key);
//            // 同步数据到redis中
//            for (Role role : list) {
//                redisUtil.lSet(key,role.getName());
//            }
//        }
//        logger.info("本次操作从Mysql");
//        return flag;
//    }
}
