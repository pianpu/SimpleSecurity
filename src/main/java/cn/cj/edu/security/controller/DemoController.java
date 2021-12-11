package cn.cj.edu.security.controller;

import cn.cj.edu.security.annotation.HasPermission;
import cn.cj.edu.security.annotation.HasRole;
import cn.cj.edu.security.annotation.SecurityUser;

import cn.cj.edu.security.entity.SimpleUser;
import cn.cj.edu.security.service.UserService;
import cn.cj.edu.security.utils.RedisUtil;
import cn.cj.edu.security.utils.Result;
import cn.cj.edu.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    // @Autowired
    // UserService userService;

//    @Autowired
//    SecurityDataSource securityDataSource;

    @Autowired
    UserService userService;


    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SecurityUtils securityUtils;


    @GetMapping("/login")
    public Object login(@RequestParam("username") String username,@RequestParam("password") String password){
        return userService.findUserByUsernameAndPassword(new SimpleUser(null, username, password));
    }

    @HasRole(hasMuch = {"admin","user","ddasdas"})
    @HasPermission(hasMuch = {"xxxx"})
    @GetMapping("/")
    public Result index(){
        return Result.success("授权后可见");
    }

    @GetMapping("/ddd")
    public Result ddd(@SecurityUser String username){
        return Result.success(securityUtils.getUser());
    }
}
