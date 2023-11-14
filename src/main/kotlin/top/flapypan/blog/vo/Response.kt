package top.flapypan.blog.vo

import top.flapypan.blog.entity.Article
import top.flapypan.blog.entity.Tag
import java.time.LocalDateTime

/**
 * 带额外信息的文章
 */
class ArticleDetailInfo(
    val id: Int,
    val title: String,
    val path: String,
    val cover: String?,
    val content: String,
    val tags: List<Tag>,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime,
    val pre: String?,
    val next: String?,
    val accessCount: Long,
) {

    /**
     * 通过实体类[article]构造新的[ArticleDetailInfo]
     */
    constructor(article: Article, pre: String?, next: String?, accessCount: Long) : this(
        id = article.id,
        title = article.title,
        path = article.path,
        cover = article.cover,
        content = article.content,
        tags = article.tags,
        createDate = article.createDate,
        updateDate = article.updateDate,
        pre, next, accessCount,
    )
}

/**
 * 文章信息，无文章内容
 */
data class ArticleInfo(
    val id: Int,
    val title: String,
    val path: String,
    val cover: String?,
    val tags: List<Tag>,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
) {

    /**
     * 通过实体类[article]构造新的[ArticleInfo]
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
 * 通过年份[year]分组的文章信息列表[list]
 */
data class ArticleGroupByYear(
    /**
     * 年份
     */
    val year: String,
    /**
     * 同一年份的文章信息列表
     */
    val list: List<ArticleInfo>
) {
    /**
     * 通过年份[year]和文章列表[list]构建
     */
    constructor(year: Int, list: List<Article>) : this(year.toString(), list.map(::ArticleInfo))
}

data class AccessBaseInfo(val all: Long, val today: Long)
