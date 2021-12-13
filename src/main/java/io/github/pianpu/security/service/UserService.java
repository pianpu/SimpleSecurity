package io.github.pianpu.security.service;

import io.github.pianpu.security.entity.SimpleUser;
import io.github.pianpu.security.utils.Result;

public interface UserService {
    /**
     * 登录
     * @param user
     * @return
     */
    Result findUserByUsernameAndPassword(SimpleUser user);






}
