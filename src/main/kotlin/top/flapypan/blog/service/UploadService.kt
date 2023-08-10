package top.flapypan.blog.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import top.flapypan.blog.common.LoggerDelegate
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_")

/**
 * 上传相关服务
 */
@Service
class UploadService(
    @Value("\${blog.upload-path}")
    private val uploadPath: String
) {
    private val log by LoggerDelegate()

    /**
     * 上传文件
     */
    fun upload(multipartFile: MultipartFile): String {
        val now = LocalDateTime.now()
        // 获取时间
        val year = now.year.toString()
        val month = now.monthValue.toString()
        val day = now.dayOfMonth.toString()
        // 检查并创建父文件夹
        val fileUploadDirPath = Path.of(uploadPath, year, month, day)
        if (Files.notExists(fileUploadDirPath)) {
            Files.createDirectories(fileUploadDirPath)
        }
        // 获取后缀名
        val extension = StringUtils.getFilenameExtension(multipartFile.originalFilename) ?: "jpg"
        // 生成随机文件名
        val filename = "${now.format(DATE_TIME_FORMATTER)}${UUID.randomUUID()}.$extension"
        // 例子 .../upload/2023/6/1/2023-06-01_12-00-00_620be734-a06d-426b-bc09-fe0f48efb314.jpg
        val filePath = fileUploadDirPath.resolve(filename)
        log.info("上传文件: $filePath")
        // 保存文件
        multipartFile.transferTo(filePath)
        // 返回访问路径
        return "/static/$year/$month/$day/$filename"
    }

}
