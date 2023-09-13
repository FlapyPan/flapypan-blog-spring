package top.flapypan.blog.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

@PublishedApi
internal object ClientInfoContext {
    @PublishedApi
    internal data class ClientInfo(val referrer: String, val ua: String, val ip: String)

    @JvmStatic
    private val clientInfoThreadLocal = ThreadLocal<ClientInfo>()

    internal fun set(info: ClientInfo) = clientInfoThreadLocal.set(info)

    fun get(): ClientInfo = clientInfoThreadLocal.get()

    internal fun remove() = clientInfoThreadLocal.remove()
}

class ClientInfoInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // 请求开始前，记录客户端信息
        val referrer = request.getHeader("Referer") ?: ""
        val ua = request.getHeader("User-Agent") ?: ""
        val ip = getClientIp(request)
        ClientInfoContext.set(ClientInfoContext.ClientInfo(referrer, ua, ip))
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        // 请求完毕，清空 ThreadLocal 防止 OOM
        ClientInfoContext.remove()
    }

    private val ipHeaders = arrayOf(
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR",
    )

    private fun getClientIp(request: HttpServletRequest): String {
        for (header in ipHeaders) {
            val ipAddress = request.getHeader(header)
            if (!ipAddress.isNullOrEmpty() && !"unknown".equals(ipAddress, ignoreCase = true)) {
                return ipAddress
            }
        }
        return request.remoteAddr ?: ""
    }


}
