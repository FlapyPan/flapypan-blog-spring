package top.flapypan.blog.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.flapypan.blog.common.RestException
import top.flapypan.blog.repository.ArticleRepository
import top.flapypan.blog.repository.TagRepository
import top.flapypan.blog.vo.ArticleAddRequest
import top.flapypan.blog.vo.ArticleGroupByYear
import top.flapypan.blog.vo.ArticleUpdateRequest

/**
 * 文章相关服务
 */
@Service
class ArticleService(
    private val repository: ArticleRepository,
    private val tagRepository: TagRepository
) {

    companion object {
        private val pathRegex = Regex("^[a-z0-9:@._-]+$")
    }

    /**
     * 获取文章分页
     */
    fun getPage(pageable: Pageable) = repository.findAll(pageable)

    /**
     * 根据年份获取文章，按照创建日期倒序
     */
    fun groupByYear() =
        repository.findYears().map { year ->
            ArticleGroupByYear(
                year,
                repository.findByYear(year)
            )
        }

    /**
     * 模糊查询文章
     */
    fun searchByKeyword(keyword: String, pageable: Pageable) =
        repository.findAllByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(keyword, keyword, pageable)

    /**
     * 通过路径查询文章
     */
    fun getByPath(path: String) =
        repository.findFirstByPath(path) ?: throw RestException(HttpStatus.NOT_FOUND.value(), "不存在的文章")

    /**
     * 添加文章
     */
    @Transactional
    fun add(addRequest: ArticleAddRequest) =
        // 转换为实体
        addRequest.createEntity {
            // 获取 tag
            tags = tagRepository.findAllByNameIn(addRequest.tagNames)
        }.let {
            // 保存
            repository.save(it).path
        }

    /**
     * 修改文章
     */
    @Transactional
    fun update(updateRequest: ArticleUpdateRequest): String {
        // 检查 path 有效性
        if (!pathRegex.matches(updateRequest.path)) {
            throw RestException(HttpStatus.BAD_REQUEST.value(), "path 格式无效")
        }
        // 查找文章
        val article = repository.findByIdOrNull(updateRequest.id)
            ?: throw RestException(HttpStatus.NOT_FOUND.value(), "不存在的文章")
        // 复制属性到实体
        updateRequest.copyToEntity(article) {
            // 获取 tag
            tags = tagRepository.findAllByNameIn(updateRequest.tagNames)
        }
        return repository.save(article).path
    }

    /**
     * 删除文章
     */
    @Transactional
    fun delete(id: Long) = repository.deleteById(id)

}
