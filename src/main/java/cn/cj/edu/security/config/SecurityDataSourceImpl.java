package cn.cj.edu.security.config;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;
import cn.cj.edu.security.mapper.UserMapper;
import cn.cj.edu.security.utils.LoginUtil;
import cn.cj.edu.security.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;



//    @Override
//    public Object findUserByUsernameAndPassword(Object user) {
//        User baseUser = (User) user;
//        if (userMapper.findUserByUsernameAndPassword(baseUser) == null){
//            logger.warn("账号密码错误," +  user);
//            return Result.fail("账号密码错误");
//        }
//        // 生成用户唯一凭证，且账号有效期由yml所配置
//        Map<String, Object> map = loginUtil.addLoginData(baseUser.getUsername());
//        if ((boolean) map.get("flag")){
//            return Result.success("登录成功,登录凭证:" + map.get("token"));
//        }
//        return Result.fail("登录失败，请稍后尝试");
//    }



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



}
