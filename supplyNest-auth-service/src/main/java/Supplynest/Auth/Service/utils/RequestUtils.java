package Supplynest.Auth.Service.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getDevice(String userAgent) {
        if (userAgent == null) return "Unknown";
        return userAgent.contains("Mobile") ? "Mobile" : "Desktop";
    }

    public static String getBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";

        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";

        return "Unknown";
    }
}
