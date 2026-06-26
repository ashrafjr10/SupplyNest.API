package SupplyNest.Business.Service.dtos;

import SupplyNest.Business.Service.enums.modelEnums;
import SupplyNest.Common.constants.RegexPatterns;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
public class BusinessGroupResponseDTO {
    private UUID businessGroupId;
    private String firstName;
    private String lastName;
    private String businessGroupCode;
    private modelEnums.BusinessGroupStatus status;
    private String phoneNumber;
    private String email;
    private UUID userId;

    private List<BusinessResponseDTO> businesses;
}
