package top.flapypan.blog.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.flapypan.blog.entity.Link
import top.flapypan.blog.repository.LinkRepository

/**
 * 固定链接相关服务
 */
@Service
class LinkService(
    private val repository: LinkRepository
) {

    /**
     * 获取所有固定链接
     */
    fun getAll(): List<Link> = repository.findAll()

    /**
     * 保存固定链接
     */
    @Transactional
    fun save(links: List<Link>): List<Link> {
        repository.deleteAll()
        return repository.saveAll(links)
    }
}
