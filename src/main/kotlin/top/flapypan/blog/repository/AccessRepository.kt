package top.flapypan.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import top.flapypan.blog.entity.Access
import java.time.LocalDateTime

@Repository
interface AccessRepository : JpaRepository<Access, Long> {
    fun countByCreateDateBetween(createDateStart: LocalDateTime, createDateEnd: LocalDateTime): Long

    fun countByArticleId(id: Long): Long

}
