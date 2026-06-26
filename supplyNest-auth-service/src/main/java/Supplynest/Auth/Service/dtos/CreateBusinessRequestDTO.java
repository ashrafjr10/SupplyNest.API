package Supplynest.Auth.Service.dtos;

import SupplyNest.Common.constants.RegexPatterns;
import Supplynest.Auth.Service.enums.modelEnums;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBusinessRequestDTO {
    @NotBlank(message = "Business Name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Business Name should contain only alphabets and spaces")
    @Size(max = 60, min = 3, message = "Business Name should be between 3 and 60 characters")
    private String businessName;

    @NotBlank(message = "Business Code is required")
    private String businessCode;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.BusinessType type;

    @Pattern(regexp = RegexPatterns.REGEX_GSTIN, message = "GST number should be in valid format")
    private String gstNumber;

    @NotBlank(message = "Address line 1 is required")
    @Pattern(regexp = RegexPatterns.REGEX_ADDRESS, message = "wrong address format")
    @Size(max = 250, min = 3, message = "Address should be between 3 and 250 characters")
    private String addressLine1;

    @Pattern(regexp = RegexPatterns.REGEX_ADDRESS, message = "wrong address format")
    @Size(max = 250, min = 3, message = "Address should be between 3 and 250 characters")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "City should contain only alphabets and spaces")
    @Size(max = 50, min = 3, message = "City should be between 3 and 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "State should contain only alphabets and spaces")
    private String state;

    @NotBlank(message = "Pin Code is required")
    @Pattern(regexp = RegexPatterns.REGEX_ONLY_NUMBERS, message = "Pin Code should contain only numbers")
    @Size(max = 6, min = 6, message = "Pin Code should be 6 digits")
    private String pinCode;
}
