package top.flapypan.blog.config

import jakarta.servlet.http.HttpServletRequest

class ClientInfo(request: HttpServletRequest) {
    val referrer: String
    val ua: String
    val ip: String

    init {
        referrer = request.getHeader("Referer") ?: ""
        ua = request.getHeader("User-Agent") ?: ""
        ip = getClientIp(request)
    }

    companion object {
        private val ipHeaders = arrayOf(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
        )
    }

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
