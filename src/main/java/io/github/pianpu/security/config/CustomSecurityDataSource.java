package io.github.pianpu.security.config;

/**
 * 重写接口，提供权限数据源
 * 打包的时候需要注释掉。由用户自行重写。
 */
//@Component
public class CustomSecurityDataSource extends DefaultSecurityDataSourceImpl {
    @Override
    public Object handleTokenIsNull() {
        return "token 为空，我是重写者";
    }
}
