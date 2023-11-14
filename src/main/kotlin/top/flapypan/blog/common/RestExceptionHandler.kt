package top.flapypan.blog.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局异常处理类
 */
@RestControllerAdvice
class RestExceptionHandler {

    val log by LoggerDelegate()

    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(e: AuthenticationException?): String? {
        return "请登录"
    }

    /**
     * Spring Validation 异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): String? {
        val objectError = e.bindingResult.allErrors[0]
        val message = objectError.defaultMessage
        log.warn("字段错误 $message")
        return message
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(RestException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleRestException(e: RestException): ResponseEntity<String?> {
        log.error("业务错误", e)
        return ResponseEntity.status(e.code).body(e.message)
    }

}
