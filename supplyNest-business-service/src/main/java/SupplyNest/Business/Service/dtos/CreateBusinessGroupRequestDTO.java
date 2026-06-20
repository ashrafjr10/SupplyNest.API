package SupplyNest.Business.Service.dtos;

import SupplyNest.Business.Service.constants.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class CreateBusinessGroupRequestDTO {

    @NotBlank(message = "first name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "first name should contain only alphabets and spaces")
    private String firstName;

    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Last name should contain only alphabets and spaces")
    private String lastName;

    @NotBlank(message = "Business Group Name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Business Group Name should contain only alphabets and spaces")
    @Size(min = 3, max = 100, message = "Business Group Name should be between 3 and 100 characters")
    private String businessGroupName;

    @Pattern(regexp = RegexPatterns.REGEX_EMAIL, message = "Email should be in valid format")
    private String email;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = RegexPatterns.REGEX_PHONE, message = "Mobile Number should be in valid format")
    private String mobileNumber;

    @NotNull(message = "user is required")
    private UUID userId;

    private List<CreateBusinessRequestDTO> businesses;
}
