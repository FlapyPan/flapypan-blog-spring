package top.flapypan.blog.controller

import jakarta.validation.constraints.Positive
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.flapypan.blog.common.restOk
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
    fun getBaseInfo() = accessService.baseInfo().restOk()

    /**
     * 获取指定文章的访问量
     */
    @GetMapping("/article/{id}")
    fun getByArticleId(@PathVariable id: @Positive Long) = accessService.countByArticleId(id).restOk()
}
