package io.github.pianpu.security.annotation;

import java.lang.annotation.*;

/**
 * 判断调用者是否具有特定权限
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasRole {

    /**
     * 权限标识符 单个
     *
     * @return 结果
     */
    String value() default "";

    /**
     * 权限标识符 单个
     *
     * @return 结果
     */
    String hasOne() default "";


    /**
     * 权限标识符 多个
     *
     * @return 结果
     */
    String[] hasMuch() default {};

    /**
     * 有很多个，但包含一个
     *
     * @return 结果
     */
    String[] hasContainsOne() default {};

}
