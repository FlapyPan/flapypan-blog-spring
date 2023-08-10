package top.flapypan.blog.vo

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.springframework.util.StringUtils
import top.flapypan.blog.entity.Article
import top.flapypan.blog.entity.Tag


/**
 * 添加文章的请求
 */
data class ArticleAddRequest(

    @field:Size(min = 2, max = 32, message = "标题长度应在2-32之间")
    val title: String,

    @field:Size(min = 3, max = 64, message = "路径长度应在3-64之间")
    @field:Pattern(regexp = "^[a-z0-9:@._-]+$", message = "路径只允许小写字母,数字,冒号,@,英文点,下划线,分隔符")
    val path: String,

    val cover: String = "",

    @field:NotBlank(message = "文章内容不能为空")
    val content: String,

    val tagNames: List<String> = mutableListOf()

) {

    /**
     * 创建新的实体类
     */
    inline fun createEntity(after: (Article.() -> Unit) = {}) = Article().also {
        it.title = title
        // 没有指定路径则使用标题
        it.path = if (StringUtils.hasText(path)) path else title
        it.cover = cover
        it.content = content
        it.after()
    }

}

/**
 * 更新文章的请求
 */
data class ArticleUpdateRequest(

    @field:Positive
    val id: Long,

    @field:Size(min = 2, max = 32, message = "标题长度应在2-32之间")
    val title: String,

    @field:Size(min = 3, max = 64, message = "路径长度应在3-64之间")
    @field:Pattern(
        regexp = "^[a-z0-9:@._-]+$",
        message = "路径只允许小写字母,数字,冒号,@,英文点,下划线,分隔符"
    )
    val path: String,

    val cover: String = "",

    @field:NotBlank(message = "文章内容不能为空")
    val content: String,

    val tagNames: List<String> = mutableListOf()

) {

    /**
     * 复制属性值到已有实体
     */
    inline fun copyToEntity(entity: Article, after: (Article.() -> Unit) = {}) {
        if (title.isNotBlank()) {
            entity.title = title
        }
        if (path.isNotBlank()) {
            entity.path = path
        }
        if (cover.isNotBlank()) {
            entity.cover = cover
        }
        if (content.isNotBlank()) {
            entity.content = content
        }
        entity.after()
    }
}

/**
 * 标签添加请求
 */
data class TagAddRequest(
    @Size(min = 2, max = 16, message = "标签名称应在2-16之间")
    val name: String
) {
    /**
     * 创建新的实体类
     */
    fun createEntity() = Tag().also {
        it.name = name
    }

}

/**
 * 标签更新请求
 */
data class TagUpdateRequest(

    @field:Positive(message = "无效的id")
    val id: Long,

    @field:Size(min = 2, max = 16, message = "标签名称应在2-16之间")
    val name: String

) {
    /**
     * 复制属性值到已有实体类
     */
    fun copyToEntity(entity: Tag) {
        if (name.isNotBlank()) {
            entity.name = name
        }
    }

}
