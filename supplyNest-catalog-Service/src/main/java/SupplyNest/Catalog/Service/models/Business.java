package SupplyNest.Catalog.Service.models;

import SupplyNest.Catalog.Service.enums.modelEnums;
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
public class Business {
    private UUID businessId;

    @NotBlank(message = "Business Name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Business Name should contain only alphabets and spaces")
    @Size(max = 60, min = 3, message = "Business Name should be between 3 and 60 characters")
    private String businessName;

    @NotBlank(message = "Business Code is required")
    private String businessCode;

    private String logo;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.BusinessType type;

    @Pattern(regexp = RegexPatterns.REGEX_GSTIN, message = "GST number should be in valid format")
    private String gstNumber;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.BusinessStatus status;

    private BusinessGroup businessGroup;
}
