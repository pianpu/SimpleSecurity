package cn.cj.edu.security;

import cn.cj.edu.security.service.UserService;
import cn.cj.edu.security.utils.JwtUtils;
import cn.cj.edu.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SecurityApplicationTests {

    @Value("${pianpu.login-config.validPeriod}")
    private Integer validPeriod;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;
    // 存储信息到redis
    // @Test
    // public void test001(){
    //     System.out.println(validPeriod);
    //     String uuid = UUID.randomUUID().toString();
    //     boolean ddd = redisUtil.hset("login",uuid,"登录信息",validPeriod);
    //     System.out.println(ddd);
    // }
    //
    //
    // @Test
    // public void test002(){
    //     // 登录凭证
    //     String uuid = UUID.randomUUID().toString();
    //
    //     boolean b = redisUtil.lSet("user:id:1", uuid);
    //     long l = redisUtil.lGetListSize("user:id:1");
    //     System.out.println(l);
    //     System.out.println(b);
    // }
    //
    // @Test
    // public void test003(){
    //     long l = redisUtil.lGetListSize("user:id:1");
    //     List<Object> objects = redisUtil.lGet("user:id:1", 0, (int) l);
    //     for (Object object : objects) {
    //         System.out.println(object.toString());
    //     }
    // }
    //
    // @Test
    // public void test004(){
    //     // redisUtil.lget
    //     // long l = redisUtil.lGetListSize("user:id:1");
    //     // System.out.println(l);
    // }
    //
    //
    // @Test
    // public void Test005(){
    //     String admin = jwtUtils.getToken("admin");
    //     System.out.println(admin);
    //     boolean b = jwtUtils.checkToken(admin);
    //     System.out.println(b);
    // }
    //
    // @Test
    // public void Test006(){
    //     List<Permission> list = userService.findUserPermissionByUserName("admin");
    //     list.stream().forEach(permission -> System.out.println(permission.getName()));
    // }
    //
    // @Test
    // public void Test007(){
    //    for(int i =0;i<100000;i++){
    //        System.out.println("执行第" + i + "条");
    //        String s = UUID.randomUUID().toString();
    //        userService.addUser(new User(null,s ,s));
    //    }
    // }
    //
    //
    // @Test
    // void contextLoads() {
    //     User user = new User();
    //     user.setUsername("root");
    //     user.setPassword("root2");
    //
    //     Result result = userService.findUserByUsernameAndPassword(user);
    //     System.out.println(result);
    //     System.out.println("DDD");
    // }

}
