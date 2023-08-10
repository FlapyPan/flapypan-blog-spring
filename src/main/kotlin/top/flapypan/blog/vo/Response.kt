package top.flapypan.blog.vo

import top.flapypan.blog.entity.Article
import top.flapypan.blog.entity.Tag
import java.time.LocalDateTime

/**
 * 文章信息，无文章内容
 */
data class ArticleInfo(
    val id: Long,
    val title: String,
    val path: String,
    val cover: String?,
    val tags: List<Tag>,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
) {

    /**
     * 通过实体类构造
     */
    constructor(article: Article) : this(
        id = article.id,
        title = article.title,
        path = article.path,
        cover = article.cover,
        tags = article.tags,
        createDate = article.createDate,
        updateDate = article.updateDate
    )

}

/**
 * 通过年份分组的文章列表
 */
data class ArticleGroupByYear(
    val year: String,
    val list: List<ArticleInfo>
) {
    constructor(year: Int, list: List<Article>) : this(year.toString(), list.map(::ArticleInfo))
}
