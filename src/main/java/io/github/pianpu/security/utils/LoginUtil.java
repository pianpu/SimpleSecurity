package io.github.pianpu.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginUtil {

    Logger logger = LoggerFactory.getLogger(LoginUtil.class);


    @Autowired
    RedisUtil redisUtil;

    @Value("${pianpu.login-config.maxLoginCount:3}")
    private Integer maxLoginCount;

    @Value("${pianpu.login-config.validPeriod:3600}")
    private Integer validPeriod;


    /**
     * 存储登录数据
     * @param username
     * @return
     */
    public Map<String, Object> addLoginData(String username) {
        JwtUtils jwtUtils = new JwtUtils();
        Map<String, Object> map = new HashMap<>();
        String key = "user:" + username;
        String value = jwtUtils.getToken(username);
        if (redisUtil.hasKey(key)) {

            if (redisUtil.lGetListSize(key) - maxLoginCount >=2) {
                redisUtil.lRemoveAll(key);
            }
            // redisList列表个数超过系统所系统则踢出最先记录的token
            if (redisUtil.lGetListSize(key) >= maxLoginCount) {
                logger.warn("username:" + username + "登录频繁");
                String token = (String) redisUtil.lGetIndex(key, 0); // 获取上一次的临时登录凭证
                long l = redisUtil.lRemove(key, 0, token); // 删除一个
                map.put("flag", redisUtil.lSet(key,value,validPeriod));
                map.put("token", value);
                return map;
            }
        }
        map.put("flag", redisUtil.lSet(key, value,validPeriod));
        map.put("token", value);
        return map;

    }

}