package top.flapypan.blog.common

/**
 * 自定义业务异常
 */
class RestException(val code: Int, message: String?) : RuntimeException(message)
