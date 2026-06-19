package Supplynest.Auth.Service.dtos;

import Supplynest.Auth.Service.constants.AppConstants;
import Supplynest.Auth.Service.constants.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Pattern(regexp = RegexPatterns.REGEX_ONLY_LETTERS, message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = RegexPatterns.REGEX_ONLY_LETTERS, message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = RegexPatterns.REGEX_EMAIL, message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = RegexPatterns.REGEX_PHONE, message = "Invalid mobile number format")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = RegexPatterns.REGEX_PASSWORD, message = AppConstants.VALIDATION_PASSWORD)
    private String password;

    @NotNull(message = "Role is required")
    private UUID roleId;
}
