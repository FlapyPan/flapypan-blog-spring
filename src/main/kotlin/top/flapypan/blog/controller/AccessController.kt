package top.flapypan.blog.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.flapypan.blog.service.AccessService

/**
 * 访问量相关接口
 */
@RestController
@RequestMapping("/api/access")
class AccessController(
    private val accessService: AccessService
) {

    /**
     * 获取基本访问量信息
     */
    @GetMapping
    fun getBaseInfo() = accessService.baseInfo()
}
