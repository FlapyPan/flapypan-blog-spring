package top.flapypan.blog.service

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import top.flapypan.blog.config.ClientInfo
import top.flapypan.blog.entity.Access
import top.flapypan.blog.entity.Article
import top.flapypan.blog.repository.AccessRepository
import top.flapypan.blog.vo.AccessBaseInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class AccessService(
    private val repository: AccessRepository
) {

    /**
     * 添加一条新的访问数据
     */
    @Async
    fun access(accessArticle: Article, clientInfo: ClientInfo) {
        val access = Access().apply {
            article = accessArticle
            referrer = clientInfo.referrer
            ua = clientInfo.ua
            ip = clientInfo.ip
        }
        repository.save(access)
    }

    fun baseInfo(): AccessBaseInfo {
        // 获取总访问量
        val all = repository.count()
        // 获取今日访问量
        val today = repository.countByCreateDateBetween(
            // 获取今天的开始时间（00:00:00）
            LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
            // 获取当前时间
            LocalDateTime.now(),
        )
        return AccessBaseInfo(all, today)
    }

    fun countByArticleId(id: Int) = repository.countByArticleId(id)
}
