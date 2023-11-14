package top.flapypan.blog.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.flapypan.blog.common.RestException
import top.flapypan.blog.entity.Tag
import top.flapypan.blog.repository.ArticleRepository
import top.flapypan.blog.repository.TagRepository
import top.flapypan.blog.vo.ArticleInfo
import top.flapypan.blog.vo.TagSaveRequest

/**
 * 标签相关服务
 */
@Service
class TagService(
    private val repository: TagRepository,
    private val articleRepository: ArticleRepository
) {

    /**
     * 获取所有标签
     */
    fun getAll(): List<Tag> = repository.findAll()

    /**
     * 通过标签名查询
     */
    fun findByName(name: String) =
        repository.findByName(name) ?: throw RestException(HttpStatus.NOT_FOUND.value(), "不存在的标签")

    /**
     * 添加标签
     */
    @Transactional
    fun add(request: TagSaveRequest): String {
        if (repository.existsByNameIgnoreCase(request.name)) {
            throw RestException(HttpStatus.BAD_REQUEST.value(), "标签已经存在")
        }
        // 构建新实体
        val tag = request.createEntity()
        // 保存
        return repository.save(tag).name
    }

    /**
     * 修改标签
     */
    @Transactional
    fun update(request: TagSaveRequest): String {
        val tag = repository.findByIdOrNull(request.id)
            ?: throw RestException(HttpStatus.NOT_FOUND.value(), "不存在的标签")
        // 复制属性到已有实体
        request.copyToEntity(tag)
        // 保存
        return repository.save(tag).name
    }

    /**
     * 删除标签
     */
    @Transactional
    fun delete(id: Int) {
        if (articleRepository.countByTagsId(id) > 0) {
            throw RestException(HttpStatus.BAD_REQUEST.value(), "当前标签下存在文章")
        }
        repository.deleteById(id)
    }

    /**
     * 通过标签名获取文章列表
     */
    fun getArticleByTag(tagName: String, pageable: Pageable): Page<ArticleInfo> {
        repository.findByName(tagName) ?: RestException(HttpStatus.NOT_FOUND.value(), "不存在的标签")
        return articleRepository
            .findAllByTagsName(tagName, pageable)
            .map(::ArticleInfo)
    }
}
