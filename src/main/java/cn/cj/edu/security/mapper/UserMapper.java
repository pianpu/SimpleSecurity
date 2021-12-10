package cn.cj.edu.security.mapper;

import cn.cj.edu.security.entity.SimplePermission;
import cn.cj.edu.security.entity.SimpleRole;
import cn.cj.edu.security.entity.SimpleUser;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("userMapper")
public interface UserMapper {
    Integer addUser(SimpleUser user);
    SimpleUser findUserByUsernameAndPassword(SimpleUser user);
    SimpleUser findUserUserName(String username);
    List<SimpleRole> findUserRoleByUserName(String username);
    List<SimplePermission> findUserPermissionByUserName(String username);
}
