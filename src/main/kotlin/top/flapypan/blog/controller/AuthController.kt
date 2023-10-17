package top.flapypan.blog.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.flapypan.blog.common.restOk

/**
 * 认证相关接口
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    /**
     * 检查登录状态
     */
    @GetMapping
    fun check() =
        (SecurityContextHolder.getContext().authentication?.isAuthenticated ?: false).restOk()

}
