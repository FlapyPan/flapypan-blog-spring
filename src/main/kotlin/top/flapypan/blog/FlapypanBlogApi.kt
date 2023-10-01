package top.flapypan.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import top.flapypan.blog.config.securityBeans

@SpringBootApplication
class FlapypanBlogApi

fun main(args: Array<String>) {
    // 启动参数为 hash <pwd> 时生成输出加密过的密码
    if (args.size > 1 && "hash" == args[0]) {
        println(BCryptPasswordEncoder().encode(args[1]))
        return
    }
    // 启动 springboot
    runApplication<FlapypanBlogApi>(*args) {
        // 添加 security 相关的 bean
        addInitializers(securityBeans)
    }
}
