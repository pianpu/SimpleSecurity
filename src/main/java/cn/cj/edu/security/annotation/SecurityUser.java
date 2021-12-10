package cn.cj.edu.security.annotation;

import java.lang.annotation.*;

/**
 * 从方法参数获取登录实体
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SecurityUser {


}
