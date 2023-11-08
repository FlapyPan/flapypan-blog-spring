package top.flapypan.blog.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.flapypan.blog.common.LoggerDelegate
import top.flapypan.blog.entity.Setting
import top.flapypan.blog.repository.SettingRepository

/**
 * 设置相关服务
 */
@Service
class SettingService(
    private val repository: SettingRepository
) {

    private val log by LoggerDelegate()

    companion object {
        // 默认设置
        private val DEFAULT_SETTINGS = mapOf(
            "siteTitle" to "FlapyPan's Blog",
            "favicon" to "/avatar.webp",
            "avatar" to "/avatar.webp",
            "banner" to "/banner.webp",
            "name" to "FlapyPan",
            "email" to "flapypan@gmail.com",
            "info" to "个人博客",
            "pageSize" to "12",
        )
    }

    /**
     * 初始化默认设置
     */
    fun initDefaultSettings() {
        if (repository.count() >= DEFAULT_SETTINGS.size) return
        synchronized(SettingService::class) {
            // 获取已经有的key
            val allKey = repository.findAllKey()
            // 求差集
            val keysToAdd = DEFAULT_SETTINGS.keys - allKey
            // 生成需要插入的值
            val settingsToAdd = keysToAdd.map { key -> Setting(key, DEFAULT_SETTINGS[key]) }
            val size = repository.saveAllAndFlush(settingsToAdd).size
            log.info("添加了 $size 项默认设置")
        }
    }

    /**
     * 获取设置
     */
    fun getSettingsMap(): Map<String, String?> {
        initDefaultSettings()
        return repository.findAll()
            .associateBy(Setting::key, Setting::value)
    }

    /**
     * 保存设置
     */
    @Transactional
    fun saveSettingsMap(settingsMap: Map<String, String?>) {
        // 获取所有设置
        val settingsWillSaved = repository.findAll()
            // 过滤掉无需存储的t和值相等的
            .filter { it.key in settingsMap && settingsMap[it.key] != it.value }
            // 设置新值
            .onEach { it.value = settingsMap[it.key] }
        repository.saveAllAndFlush(settingsWillSaved)
    }

}
