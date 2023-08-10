package top.flapypan.blog.common

import org.springframework.http.HttpStatus

private val OK_STATUS = HttpStatus.OK.value()

/**
 * 统一返回结构
 */
data class RestResult<T>(
    val code: Int,
    val data: T,
    val success: Boolean = (code == OK_STATUS)
)

/**
 * 扩展方法，用于包装返回类型
 */
fun <T> T?.restOk() = RestResult(OK_STATUS, this)

/**
 * 扩展方法，用于包装返回类型
 */
fun <T> T?.restErr(code: Int = HttpStatus.INTERNAL_SERVER_ERROR.value()) = RestResult(code, this)
