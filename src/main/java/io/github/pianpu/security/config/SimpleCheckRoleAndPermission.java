package io.github.pianpu.security.config;

import io.github.pianpu.security.entity.SimplePermission;
import io.github.pianpu.security.entity.SimpleRole;

import io.github.pianpu.security.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 检查用户是否具有权限或角色身份
 * 优先从redis中获取，如redis不存在，则获取一次数据库，数据库也不存在的话，就直接判定为无角色或无权限、如存在则同步数据到redis
 */
@Component
public class SimpleCheckRoleAndPermission {

    @Autowired(required = false)
    SecurityDataSource securityDataSource;

    Logger logger = LoggerFactory.getLogger(SimpleCheckRoleAndPermission.class);



    @Autowired
    RedisUtil redisUtil;


    /**
     * 检查用户是否拥有该权限标识符
     * @param username 用户名
     * @param value 权限代码
     * @return true/false
     */
    public boolean checkUserPermission(String username, String value) {
        // 先从redis 缓存中拿数据
        boolean flag = false;
        String key = "permission:" + username;
        List<Object> redisList = redisUtil.lGet(key, 0, (int) redisUtil.lGetListSize(key));
        List<Object> rdCollect = redisList.stream().filter(name -> name.equals(value)).collect(Collectors.toList());
        if (rdCollect.size() > 0 ){
            logger.info("本次操作从Redis");
            return true;
        }
        // redis 无缓存数据再从数据库拿数据，且同步到redis中
        List<SimplePermission> list =  (List<SimplePermission>) (List)securityDataSource.findUserPermissionByUserName(username);
        List<SimplePermission> dbCollect = list.stream().filter(permission -> permission.getName().equals(value)).collect(Collectors.toList());
        flag = dbCollect.size()>0?true:false;
        if (flag){
            // 清空redis
            redisUtil.lRemoveAll(key);
            // 同步数据到redis中
            for (SimplePermission permission : list) {
                redisUtil.lSet(key,permission.getName());
            }
        }
        logger.info("本次操作从Mysql");
        return flag;
    }

    /**
     * 检测用户是否拥有该角色
     * @param username 用户名
     * @param value 角色代码
     * @return true/false
     */
    public boolean checkUserRole(String username, String value) {
        // 先从redis 缓存中拿数据
        boolean flag = false;
        String key = "role:" + username;
        List<Object> redisList = redisUtil.lGet(key, 0, (int) redisUtil.lGetListSize(key));
        List<Object> rdCollect = redisList.stream().filter(name -> name.equals(value)).collect(Collectors.toList());
        if (rdCollect.size() > 0 ){
            logger.info("本次操作从Redis");
            return true;
        }
        // redis 无缓存数据再从数据库拿数据，且同步到redis中
        List<SimpleRole> list = (List<SimpleRole>) securityDataSource.findUserRoleByUserName(username);
        List<SimpleRole> dbCollect = list.stream().filter(role -> role.getName().equals(value)).collect(Collectors.toList());

        flag = dbCollect.size()>0?true:false;
        if (flag){
            // 清空redis
            redisUtil.lRemoveAll(key);
            // 同步数据到redis中
            for (SimpleRole role : list) {
                redisUtil.lSet(key,role.getName());
            }
        }
        logger.info("本次操作从Mysql");
        return flag;
    }
}
