package top.flapypan.blog.controller

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.flapypan.blog.common.restOk
import top.flapypan.blog.service.ArticleService
import top.flapypan.blog.vo.ArticleAddRequest
import top.flapypan.blog.vo.ArticleInfo
import top.flapypan.blog.vo.ArticleUpdateRequest

/**
 * 文章相关接口
 */
@RestController
@RequestMapping("/article")
class ArticleController(
    private val articleService: ArticleService
) {

    /**
     * 获取分页文章信息
     */
    @GetMapping
    fun getPage(keyword: String?, pageable: Pageable) =
        (keyword?.takeIf(String::isNotBlank)?.let {
            articleService.searchByKeyword(it, pageable)
        } ?: articleService.getPage(pageable)).map(::ArticleInfo).restOk()

    /**
     * 获取所有文章信息，并根据年份分组
     */
    @GetMapping("/group-by/year")
    fun getGroupByYear() = articleService.groupByYear().restOk()

    /**
     * 通过路径获取文章内容
     */
    @Validated
    @GetMapping("/{path}")
    fun getByPath(@PathVariable @Pattern(regexp = "^[a-z0-9:@._-]+$") path: String) =
        articleService.getByPath(path).restOk()

    /**
     * 添加文章
     */
    @PostMapping
    fun add(@RequestBody @Validated articleAddRequest: ArticleAddRequest) =
        articleService.add(articleAddRequest).restOk()

    /**
     * 修改文章
     */
    @PutMapping
    fun update(@RequestBody @Validated articleUpdateRequest: ArticleUpdateRequest) =
        articleService.update(articleUpdateRequest).restOk()

    /**
     * 删除文章
     */
    @Validated
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: @Positive Long) =
        articleService.delete(id).restOk()
}
