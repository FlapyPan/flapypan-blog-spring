package top.flapypan.blog.controller

import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.flapypan.blog.service.TagService
import top.flapypan.blog.vo.TagSaveRequest

/**
 * 标签相关接口
 */
@RestController
@RequestMapping("/api/tag")
class TagController(
    private val tagService: TagService
) {

    /**
     * 获取所有标签
     */
    @GetMapping
    fun all() = tagService.getAll()

    /**
     * 通过标签名获取标签信息
     */
    @Validated
    @GetMapping("/{name}")
    fun getByName(@PathVariable name: String) = tagService.findByName(name)

    /**
     * 添加标签
     */
    @PostMapping
    fun add(@RequestBody @Validated addRequest: TagSaveRequest) =
        tagService.add(addRequest)

    /**
     * 修改标签
     */
    @PutMapping
    fun update(@RequestBody @Validated updateRequest: TagSaveRequest) =
        tagService.update(updateRequest)

    /**
     * 删除标签
     */
    @Validated
    @DeleteMapping("/{id}")
    fun delete(@PathVariable @Positive id: Int) = tagService.delete(id)

    /**
     * 获取标签下的文章列表
     */
    @GetMapping("/{tag}/article")
    fun listArticle(@PathVariable tag: String, pageable: Pageable) =
        tagService.getArticleByTag(tag, pageable)
}
