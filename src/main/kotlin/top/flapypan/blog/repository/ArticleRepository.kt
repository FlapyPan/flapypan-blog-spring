package top.flapypan.blog.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import top.flapypan.blog.entity.Article

@Repository
interface ArticleRepository : JpaRepository<Article, Long> {

    /**
     * 通过路径获取文章
     */
    fun findFirstByPath(path: String): Article?

    /**
     * 查询所有文章的年份
     */
    @Query("SELECT DISTINCT YEAR(a.createDate) FROM Article a ORDER BY YEAR(a.createDate) DESC")
    fun findYears(): List<Int>

    /**
     * 根据年份查询文章
     */
    @Query(
        """
        SELECT a
            FROM Article a
            WHERE YEAR(a.createDate) = :year
            ORDER BY a.createDate DESC
        """
    )
    fun findByYear(year: Int): List<Article>

    /**
     * 模糊查询标题和标签名
     */
    fun findAllByTitleContainingIgnoreCaseOrTagsNameContainingIgnoreCase(
        title: String,
        tagName: String,
        pageable: Pageable
    ): Page<Article>

    /**
     * 通过标签名获取文章分页
     */
    fun findAllByTagsName(tagName: String, pageable: Pageable): Page<Article>

    /**
     * 通过标签名获取文章数量
     */
    fun countByTagsId(tagId: Long): Long

}
