package top.flapypan.blog.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import top.flapypan.blog.common.RestResult
import top.flapypan.blog.common.restErr
import java.nio.charset.StandardCharsets


/**
 * security 配置
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
class SecurityConfig {

    /**
     * 密码加解密器
     */
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    /**
     * 提供认证管理器的 Bean
     */
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.getAuthenticationManager()

    /**
     * 用户信息服务
     */
    @Bean
    fun userDetailsService(
        @Value("\${blog.username}")
        username: String,
        @Value("\${blog.password}")
        password: String,
    ): UserDetailsService = InMemoryUserDetailsManager(
        // 管理员
        User.builder().username(username).password(password).build()
    )

    @Bean
    fun filterChainBasic(http: HttpSecurity): SecurityFilterChain {
        http {
            // 关闭 csrf
            csrf { disable() }
            // 使用 form 登录
            formLogin {
                loginProcessingUrl = "/auth/login"
                authenticationSuccessHandler =
                    AuthenticationSuccessHandler { _, response, _ ->
                        writeResponse(response, "登录成功".restErr(HttpStatus.OK.value()))
                    }
                authenticationFailureHandler = AuthenticationFailureHandler { _, response, _ ->
                    writeResponse(response, "登陆失败".restErr(HttpStatus.UNAUTHORIZED.value()))
                }
            }
            // 记住我
            rememberMe {
                tokenValiditySeconds = 3600 * 24 * 7 // 7 天
            }
            logout {
                logoutUrl = "/auth/logout"
                logoutSuccessHandler = LogoutSuccessHandler { _, response, _ ->
                    writeResponse(response, "退出登录".restErr(HttpStatus.OK.value()))
                }
            }
            // 关闭匿名功能
            anonymous { disable() }
            authorizeHttpRequests {
                // 忽略所有 GET 请求
                authorize(HttpMethod.GET, "/**", permitAll)
                // 忽略图片访问、登录接口
                authorize("/static/**", permitAll)
                authorize("/auth/**", permitAll)
                // 其他请求需要登录
                authorize(anyRequest, authenticated)
            }
            // 错误处理
            exceptionHandling {
                authenticationEntryPoint = AuthenticationEntryPoint { _, response, _ ->
                    writeResponse(response, "请登录".restErr(HttpStatus.UNAUTHORIZED.value()))
                }
                accessDeniedHandler = AccessDeniedHandler { _, response, _ ->
                    writeResponse(response, "请登录".restErr(HttpStatus.UNAUTHORIZED.value()))
                }
            }
        }
        return http.build()
    }

    /**
     * 将错误信息写入响应体
     */
    private fun writeResponse(response: HttpServletResponse, result: RestResult<String?>) = with(response) {
        // 设置 http 状态码
        status = HttpStatus.OK.value()
        // 设置 utf8 编码
        characterEncoding = StandardCharsets.UTF_8.name()
        // 类型 json
        contentType = MediaType.APPLICATION_JSON_VALUE
        writer.write(ObjectMapper().writeValueAsString(result))
    }

}
