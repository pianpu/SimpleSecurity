package cn.cj.edu.security.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasPermission {

    /**
     * 权限标识符 单个
     * @return
     */
    String value() default "";

    /**
     * 权限标识符 单个
     * @return
     */
    String hasOne() default "";



    /**
     * 权限标识符 多个
     * @return
     */
    String[] hasMuch() default {};

    /**
     * 有很多个，但包含一个
     * @return
     */
    String[] hasContainsOne() default {};

}
