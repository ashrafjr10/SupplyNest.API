package SupplyNest.Business.Service.models;

import SupplyNest.Common.constants.RegexPatterns;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "business_address")
public class BusinessAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID businessAddressId;

    @NotBlank(message = "Address line 1 is required")
    @Pattern(regexp = RegexPatterns.REGEX_ADDRESS, message = "wrong address format")
    @Size(max = 250, min = 3, message = "Address should be between 3 and 250 characters")
    private String addressLine1;

    @Pattern(regexp = RegexPatterns.REGEX_ADDRESS, message = "wrong address format")
    @Size(max = 250, min = 3, message = "Address should be between 3 and 250 characters")
    private String addressLine2;

//    @NotBlank(message = "Address is required")
//    @Pattern(regexp = RegexPatterns.REGEX_ADDRESS, message = "wrong address format")
//    @Size(max = 250, min = 3, message = "Address should be between 3 and 250 characters")
//    private String address;

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
