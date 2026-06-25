package Supplynest.Auth.Service.security;

import SupplyNest.Common.CurrentUser;
import Supplynest.Auth.Service.constants.RequestAttributes;
import Supplynest.Auth.Service.dtos.UserDO;
import Supplynest.Auth.Service.models.User;
import Supplynest.Auth.Service.repositories.UserRepository;
import Supplynest.Auth.Service.utils.JwtUtils;
import Supplynest.Auth.Service.utils.RoleFormatterForUI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleFormatterForUI roleFormatterForUI;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization Header");
            return false;
        }

        String token = authHeader.substring(7);

        if (!jwtUtils.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return false;
        }

        CurrentUser currentUser = jwtUtils.getUserDataFromToken(token);
        request.setAttribute(RequestAttributes.CURRENT_USER, currentUser);

        return true;
    }
}
