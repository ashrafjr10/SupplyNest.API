package SupplyNest.Common;

import SupplyNest.Common.dtos.RoleResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {
    private UUID userId;
    private RoleResponseDTO role;
    private String phoneOrEmail;
    private String businessCode;
    private String businessGroupCode;
    private String userType;
}
