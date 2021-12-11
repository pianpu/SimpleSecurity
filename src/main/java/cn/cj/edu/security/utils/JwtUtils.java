package cn.cj.edu.security.utils;

//import cn.cj.edu.security.config.SimpleSecurityDataSource;
import cn.cj.edu.security.config.SecurityDataSource;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

@Component
public class JwtUtils {


    Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // @Autowired
    // UserService userService;
    @Autowired
    SecurityDataSource userService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 生成Token
     * @param username
     * @return
     */
    public static String getToken(String username){
        JwtBuilder jwtBuilder = Jwts.builder()
                // .setClaims(map)
                .setSubject(username)
                .setExpiration(new Date(new Date().getTime() + 1800000))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "123456");
        return jwtBuilder.compact();
    }

    /**
     * 校验token是否正确
     * @param token
     * @return
     */
    public static boolean checkToken(String token){
       try{
           Jwts.parser()
                   .setSigningKey("123456")
                   .parseClaimsJws(token)
                   .getBody();
           return true;
       }catch (Exception e){
           return false;
       }
    }


    /**
     * 校验token是否正确 且 判断数据库是否有包含该用户
     * @param token
     * @return
     */
    public boolean checkTokenIsUser(String token){
        try{
            Jwts.parser()
                    .setSigningKey("123456")
                    .parseClaimsJws(token)
                    .getBody();
            // 从数据库校验该用户名是否存在
            String username = getUsername(token);
            Object user = userService.findUserByUserName(username);
            if (null == user){
                return false;
            }
            return true;
        }catch (Exception e){
            logger.error("e=>" + e);
            return false;
        }
    }

    /**
     * 校验token是否过期
     * @param token
     * @return
     */
    public boolean checkTokenExpired(String token){
        try{
            Jwts.parser()
                    .setSigningKey("123456")
                    .parseClaimsJws(token)
                    .getBody();
            // 从redis中获取是否存在
            if (!isExist(token)){
                return false;
            }
            return true;
        }catch (Exception e){
            logger.error("e=>" + e);
            return false;
        }
    }

    /**
     * 检测token是否还存在缓存之中
     * @param token
     * @return
     */
    public boolean isExist(String token){
        String username = getUsername(token);
        String key = "user:" + username;
        boolean b = redisUtil.hasKey(key);
        if (!b){
            // redis 缓存中无该key。直接返回假。让其无效授权
            return false;
        }
        List<Object> objects = redisUtil.lGet(key, 0, (int) redisUtil.lGetListSize(key));
        List<Object> collect = objects.stream().filter(redisToken -> token.equals(redisToken)).collect(Collectors.toList());
        if (collect.size() == 0){
            return false;
        }
        return true;

    }


    public static String getUsername(String token){
        Claims claims;
        try{
            claims= Jwts
                    .parser()
                    .setSigningKey("123456")
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }catch (Exception e){
            return "";
        }
    }

}
