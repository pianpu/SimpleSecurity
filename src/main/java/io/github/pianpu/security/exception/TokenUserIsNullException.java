package io.github.pianpu.security.exception;

/**
 * token 挂靠用户名不存在的异常类
 * 实际业务场景的话: token已生成，但后续数据库删除了，token还存在的处理方式
 */
public class TokenUserIsNullException extends RuntimeException{
    public TokenUserIsNullException() {
    }
}
