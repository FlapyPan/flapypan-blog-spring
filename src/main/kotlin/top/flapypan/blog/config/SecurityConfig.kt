package top.flapypan.blog.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.support.beans
import org.springframework.core.env.get
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import java.nio.charset.StandardCharsets

/**
 * security 配置
 */
val securityBeans = beans {

    // 密码加解密器
    bean<PasswordEncoder> { BCryptPasswordEncoder() }

    // 用户信息服务
    bean<UserDetailsService> {
        val username: String? = env["blog.username"]
        val password: String? = env["blog.password"]
        InMemoryUserDetailsManager(
            // 管理员
            User.builder()
                .username(username)
                .password(password)
                .build()
        )
    }

    // security 主配置
    bean<SecurityFilterChain> {
        val http: HttpSecurity = ref()
        http {
            // 关闭 csrf
            csrf { disable() }
            // 使用 form 登录
            formLogin {
                loginProcessingUrl = "/api/auth/login"
                authenticationSuccessHandler =
                    AuthenticationSuccessHandler { _, response, _ ->
                        writeResponse(response, HttpStatus.OK, "welcome")
                    }
                authenticationFailureHandler = AuthenticationFailureHandler { _, response, e ->
                    writeResponse(response, HttpStatus.UNAUTHORIZED, e.message)
                }
            }
            // 记住我
            rememberMe {
                tokenValiditySeconds = 3600 * 24 * 7 // 7 天
            }
            logout {
                logoutUrl = "/api/auth/logout"
                logoutSuccessHandler = LogoutSuccessHandler { _, response, _ ->
                    writeResponse(response, HttpStatus.OK, "bye")
                }
            }
            // 关闭匿名功能
            anonymous { disable() }
            authorizeHttpRequests {
                // 忽略所有 GET 请求
                authorize(HttpMethod.GET, "/**", permitAll)
                // 忽略图片访问、登录接口
                authorize("/static/**", permitAll)
                authorize("/api/auth/**", permitAll)
                // 其他请求需要登录
                authorize(anyRequest, authenticated)
            }
            // 错误处理
            exceptionHandling {
                authenticationEntryPoint = AuthenticationEntryPoint { _, response, _ ->
                    writeResponse(response, HttpStatus.UNAUTHORIZED, "请登录")
                }
                accessDeniedHandler = AccessDeniedHandler { _, response, _ ->
                    writeResponse(response, HttpStatus.UNAUTHORIZED, "请登录")
                }
            }
        }
        http.build()
    }
}

private fun writeResponse(response: HttpServletResponse, httpStatus: HttpStatus, result: String?) =
    with(response) {
        // 设置 http 状态码
        status = httpStatus.value()
        // 设置 utf8 编码
        characterEncoding = StandardCharsets.UTF_8.name()
        writer.write(result ?: "")
    }
