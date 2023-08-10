package top.flapypan.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class FlapypanBlogApiApplication

fun main(args: Array<String>) {
    // 启动参数为 hash <pwd> 时生成输出加密过的密码
    if (args.size > 1 && "hash" == args[0]) {
        println(BCryptPasswordEncoder().encode(args[1]))
        return
    }
    runApplication<FlapypanBlogApiApplication>(*args)
}
