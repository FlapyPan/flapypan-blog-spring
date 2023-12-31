package top.flapypan.blog.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import top.flapypan.blog.config.ClientInfo
import top.flapypan.blog.service.AccessService
import top.flapypan.blog.service.ArticleService
import top.flapypan.blog.vo.ArticleDetailInfo
import top.flapypan.blog.vo.ArticleInfo
import top.flapypan.blog.vo.ArticleSaveRequest

/**
 * 文章相关接口
 */
@RestController
@RequestMapping("/api/article")
class ArticleController(
    private val articleService: ArticleService,
    private val accessService: AccessService
) {

    /**
     * 获取分页文章信息
     */
    @GetMapping
    fun getPage(keyword: String?, pageable: Pageable) =
        (keyword?.takeIf(String::isNotBlank)?.let {
            articleService.searchByKeyword(it, pageable)
        } ?: articleService.getPage(pageable)).map(::ArticleInfo)

    /**
     * 获取所有文章信息，并根据年份分组
     */
    @GetMapping("/group-by/year")
    fun getGroupByYear() = articleService.groupByYear()

    /**
     * 通过路径获取文章内容
     */
    @Validated
    @GetMapping("/{path}")
    fun getByPath(
        @PathVariable @Pattern(regexp = "^[a-z0-9:@._-]+$") path: String,
        @Autowired request: HttpServletRequest,
    ): ArticleDetailInfo? {
        val article = articleService.getByPath(path)
        accessService.access(article, ClientInfo(request))
        val pre = articleService.getPre(article.id)
        val next = articleService.getNext(article.id)
        val accessCount = accessService.countByArticleId(article.id)
        return ArticleDetailInfo(article, pre, next, accessCount)
    }

    /**
     * 添加文章
     */
    @PostMapping
    fun add(@RequestBody @Validated articleSaveRequest: ArticleSaveRequest) =
        articleService.add(articleSaveRequest)

    /**
     * 修改文章
     */
    @PutMapping
    fun update(@RequestBody @Validated articleSaveRequest: ArticleSaveRequest) =
        articleService.update(articleSaveRequest)

    /**
     * 删除文章
     */
    @Validated
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: @Positive Int) =
        articleService.delete(id)
}
