package top.flapypan.blog.controller

import org.springframework.web.bind.annotation.*
import top.flapypan.blog.entity.Link
import top.flapypan.blog.service.LinkService

/**
 * 固定链接相关接口
 */
@RestController
@RequestMapping("/api/link")
class LinkController(
    private val linkService: LinkService
) {

    /**
     * 获取所有固定链接
     */
    @GetMapping
    fun all() = linkService.getAll()

    /**
     * 修改固定链接
     */
    @PostMapping
    fun save(@RequestBody links: List<Link>) = linkService.save(links)
}
