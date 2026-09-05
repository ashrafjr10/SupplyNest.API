package Supplynest.Auth.Service.models;

import SupplyNest.Common.constants.RegexPatterns;
import Supplynest.Auth.Service.enums.modelEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Business extends BaseEntity{
    private UUID businessId;
    private String businessName;
    private String businessCode;
    private modelEnums.BusinessType type;
    private String gstNumber;
    private modelEnums.BusinessStatus status;
}
