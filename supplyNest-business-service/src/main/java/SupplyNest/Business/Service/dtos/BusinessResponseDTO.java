package SupplyNest.Business.Service.dtos;

import SupplyNest.Business.Service.enums.modelEnums;
import SupplyNest.Business.Service.models.BusinessGroup;
import SupplyNest.Common.constants.RegexPatterns;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class BusinessResponseDTO {
    private UUID businessId;
    private String businessName;
    private String businessCode;
    private modelEnums.BusinessType type;
    private String gstNumber;
    private modelEnums.BusinessStatus status;
    private BusinessAddressResponseDTO businessAddress;
}
