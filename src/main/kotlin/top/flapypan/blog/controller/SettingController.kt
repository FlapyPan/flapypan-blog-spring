package top.flapypan.blog.controller

import org.springframework.web.bind.annotation.*
import top.flapypan.blog.common.restOk
import top.flapypan.blog.service.SettingService

/**
 * 设置相关接口
 */
@RestController
@RequestMapping("/api/setting")
class SettingController(
    private val settingService: SettingService
) {

    /**
     * 获取所有设置
     */
    @GetMapping
    fun getSettings() = settingService.getSettingsMap().restOk()

    /**
     * 保存设置
     */
    @PostMapping
    fun updateSettings(@RequestBody settingsMap: Map<String, String?>) =
        settingService.saveSettingsMap(settingsMap).restOk()
}
