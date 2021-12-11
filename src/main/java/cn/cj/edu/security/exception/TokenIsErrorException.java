package cn.cj.edu.security.exception;

/**
 * token 为伪造的异常类
 * 通常为签名校验不通过
 */
public class TokenIsErrorException extends RuntimeException{
    public TokenIsErrorException() {
    }
}
