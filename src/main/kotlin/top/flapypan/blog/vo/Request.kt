package top.flapypan.blog.vo

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.beans.BeanUtils
import top.flapypan.blog.entity.Article
import top.flapypan.blog.entity.Tag

/**
 * 保存实体的请求
 */
abstract class SaveRequest<Entity : Any> {

    companion object {
        private val DEFAULT_COPY_IGNORE_PROPERTIES = emptyArray<String>()
    }

    /**
     * 获取属性复制忽略列表，覆盖此方法即可自定义忽略列表
     */
    protected open fun getIgnoreProperties() = DEFAULT_COPY_IGNORE_PROPERTIES

    /**
     * 复制属性
     */
    protected open fun copyPropertiesTo(entity: Entity) {
        // 使用 Spring 自带的复制工具
        BeanUtils.copyProperties(this, entity, *getIgnoreProperties())
    }

    /**
     * 获取新实体，由继承者提供具体实现
     */
    protected abstract fun newEntity(): Entity

    /**
     * 构建新的实体，创建之后执行[after]
     */
    fun createEntity(after: (Entity.() -> Unit)? = null): Entity {
        // 构建新实体
        val entity = newEntity()
        // 复制属性，并执行后续方法
        return copyToEntity(entity, after)
    }

    /**
     * 复制值到已有的[entity]，复制之后执行[after]
     */
    fun copyToEntity(entity: Entity, after: (Entity.() -> Unit)? = null): Entity {
        // 复制属性
        copyPropertiesTo(entity)
        // 执行后续方法
        after?.let { entity.it() }
        return entity
    }

}


/**
 * 保存文章的请求
 */
data class ArticleSaveRequest(
    val id: Long = 0L,

    @field:Size(min = 2, max = 32, message = "标题长度应在2-32之间")
    val title: String,

    @field:Size(min = 3, max = 64, message = "路径长度应在3-64之间")
    @field:Pattern(regexp = "^[a-z0-9:@._-]+$", message = "路径只允许小写字母,数字,冒号,@,英文点,下划线,分隔符")
    val path: String,

    val cover: String = "",

    @field:NotBlank(message = "文章内容不能为空")
    val content: String,

    val tagNames: List<String> = mutableListOf()

) : SaveRequest<Article>() {
    companion object {
        private val ignoreProperties = arrayOf("tagNames")
    }

    override fun getIgnoreProperties() = ignoreProperties

    override fun newEntity() = Article()
}


/**
 * 标签保存请求
 */
data class TagSaveRequest(
    val id: Long = 0L,

    @field:Size(min = 2, max = 16, message = "标签名称应在2-16之间")
    val name: String
) : SaveRequest<Tag>() {
    companion object {
        private val ignoreProperties = arrayOf("tagNames")
    }

    override fun getIgnoreProperties() = ignoreProperties

    override fun newEntity() = Tag()
}
