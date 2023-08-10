package top.flapypan.blog.common

import jakarta.servlet.ServletException
import org.springframework.http.HttpStatus
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
     * sa token 异常处理
     */
    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleAuthenticationException(e: AuthenticationException?): RestResult<String?> {
        return "请登录".restErr(HttpStatus.UNAUTHORIZED.value())
    }

    /**
     * Spring Validation 异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): RestResult<String?> {
        val objectError = e.bindingResult.allErrors[0]
        val message = objectError.defaultMessage
        log.warn("字段错误 $message")
        return message.restErr(HttpStatus.BAD_REQUEST.value())
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(RestException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleRestException(e: RestException): RestResult<String?> {
        log.error("业务错误", e)
        return e.message.restErr(e.code)
    }

    @ExceptionHandler(ServletException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleServletException(e: ServletException): RestResult<String?> {
        return e.message.restErr(HttpStatus.BAD_REQUEST.value())
    }

    /**
     * 其他异常处理
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleException(e: Exception?): RestResult<String?> {
        log.error("服务器内部错误", e)
        return "服务器内部错误".restErr()
    }

}
