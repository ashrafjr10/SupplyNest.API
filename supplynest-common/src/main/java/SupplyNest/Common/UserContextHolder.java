package SupplyNest.Common;

import SupplyNest.Common.dtos.RoleResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserContextHolder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CurrentUser getCurrentUser(HttpServletRequest request) {

        String roleJson = request.getHeader("X-Role");
        RoleResponseDTO role;
        try {
            role = objectMapper.readValue(roleJson, RoleResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize role from header", e);
        }

        return CurrentUser.builder()
                .userId(UUID.fromString(request.getHeader("X-User-Id")))
                .role(role)
                .businessCode(request.getHeader("X-Business-Code"))
                .businessGroupCode(request.getHeader("X-Business-Group-Code"))
                .userType(request.getHeader("X-User-Type"))
                .build();
    }
}