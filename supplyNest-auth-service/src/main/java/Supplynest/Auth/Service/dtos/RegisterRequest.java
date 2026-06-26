package Supplynest.Auth.Service.dtos;

import SupplyNest.Common.constants.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class RegisterRequest {

    @NotBlank(message = "first name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "first name should contain only alphabets and spaces")
    private String firstName;

    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "Last name should contain only alphabets and spaces")
    private String lastName;

    @Pattern(regexp = RegexPatterns.REGEX_EMAIL, message = "Email should be in valid format")
    private String email;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = RegexPatterns.REGEX_PHONE, message = "Mobile Number should be in valid format")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = RegexPatterns.REGEX_PASSWORD, message = "Password should be in valid format")
    private String password;

    @NotNull(message = "Role Id is required")
    private UUID roleId;

    private CreateBusinessGroupRequestDTO createBusinessGroupRequestDTO;
}
