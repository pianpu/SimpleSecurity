package cn.cj.edu.security.service;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;
import cn.cj.edu.security.utils.Result;

import java.util.List;

public interface UserService {
    /**
     * 登录
     * @param user
     * @return
     */
    Result findUserByUsernameAndPassword(SimpleUser user);






}
