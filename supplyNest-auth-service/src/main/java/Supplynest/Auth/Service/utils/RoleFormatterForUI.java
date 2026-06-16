package Supplynest.Auth.Service.utils;

import Supplynest.Auth.Service.dtos.RoleCrudePermissionResponseDTO;
import Supplynest.Auth.Service.dtos.RoleResponseDTO;
import Supplynest.Auth.Service.models.Role;
import Supplynest.Auth.Service.models.RoleCrudPermission;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleFormatterForUI {
    public RoleResponseDTO formatRole(Role role) {
        if(role == null) return null;

        Set<RoleCrudePermissionResponseDTO> permissionResponseDTOS =
                role.getCrudPermissions()
                        .stream()
                        .map(this::mapPermission)
                        .collect(Collectors.toSet());

        return RoleResponseDTO.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .type(role.getRoleTypes().name())
                .description(role.getDescription())
                .crudPermissions(permissionResponseDTOS)
                .build();
    }

    // Helper methods

    private RoleCrudePermissionResponseDTO mapPermission(RoleCrudPermission permission) {
        return RoleCrudePermissionResponseDTO.builder()
                .scope(permission.getScope())
                .operations(permission.getOperations())
                .build();
    }
}
