package top.flapypan.blog.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import top.flapypan.blog.common.RestException
import top.flapypan.blog.service.UploadService

/**
 * 上传相关接口
 */
@RestController
@RequestMapping("/api/upload")
class UploadController(
    private val uploadService: UploadService
) {

    /**
     * 上传图片
     */
    @PostMapping
    fun add(@RequestParam("file") multipartFile: MultipartFile): String? {
        // 限制为图片
        if (multipartFile.contentType?.startsWith("image/") != true) {
            throw RestException(HttpStatus.BAD_REQUEST.value(), "错误的文件类型")
        }
        return uploadService.upload(multipartFile)
    }
}
