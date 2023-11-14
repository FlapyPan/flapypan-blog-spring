package top.flapypan.blog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import top.flapypan.blog.entity.Tag

@Repository
interface TagRepository : JpaRepository<Tag, Int> {

    /**
     * 查询名称是否存在
     */
    fun existsByNameIgnoreCase(name: String): Boolean

    /**
     * 根据名称列表获取标签
     */
    fun findAllByNameIn(names: Collection<String>): List<Tag>

    /**
     * 通过标签名查询
     */
    fun findByName(name: String): Tag?

}
