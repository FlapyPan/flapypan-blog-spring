package top.flapypan.blog.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.flapypan.blog.common.RestException
import top.flapypan.blog.repository.ArticleRepository
import top.flapypan.blog.repository.TagRepository
import top.flapypan.blog.vo.ArticleGroupByYear
import top.flapypan.blog.vo.ArticleSaveRequest

/**
 * 文章相关服务
 */
@Service
class ArticleService(
    private val repository: ArticleRepository,
    private val tagRepository: TagRepository
) {

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
     * 获取上一篇文章路径
     */
    fun getPre(id: Int) = repository.findPrePathById(id)

    /**
     * 获取下一篇文章路径
     */
    fun getNext(id: Int) = repository.findNextPathById(id)

    /**
     * 添加文章
     */
    @Transactional
    fun add(request: ArticleSaveRequest): String {
        // 构建新实体
        val article = request.createEntity {
            // 获取 tag
            tags = tagRepository.findAllByNameIn(request.tagNames)
        }
        // 保存
        return repository.save(article).path
    }


    /**
     * 修改文章
     */
    @Transactional
    fun update(updateRequest: ArticleSaveRequest): String {
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
    fun delete(id: Int) = repository.deleteById(id)

}
