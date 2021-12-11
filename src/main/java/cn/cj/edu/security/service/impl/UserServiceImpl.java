package cn.cj.edu.security.service.impl;


import cn.cj.edu.security.entity.SimpleUser;

import cn.cj.edu.security.service.UserService;
import cn.cj.edu.security.utils.LoginUtil;
import cn.cj.edu.security.utils.RedisUtil;
import cn.cj.edu.security.utils.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    LoginUtil loginUtil;


    @Autowired
    RedisUtil redisUtil;

    @Override
    public Result findUserByUsernameAndPassword(SimpleUser user) {
        // 生成用户唯一凭证，且账号有效期由yml所配置
        Map<String, Object> map = loginUtil.addLoginData(user.getUsername());
        if ((boolean) map.get("flag")){
            return Result.success("登录成功,登录凭证:" + map.get("token"));
        }
        return Result.fail("登录失败，请稍后尝试");
    }
}
