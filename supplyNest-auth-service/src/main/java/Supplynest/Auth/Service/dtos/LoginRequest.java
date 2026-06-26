package Supplynest.Auth.Service.dtos;

import SupplyNest.Common.constants.AppConstants;
import SupplyNest.Common.constants.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "Phone Number or Email or Username is required")
    private String phoneOrEmail;

    @Pattern(regexp = RegexPatterns.REGEX_PHONE +  "|" + RegexPatterns.REGEX_PASSWORD + "|" + RegexPatterns.REGEX_DATE, message = AppConstants.MESSAGE_INVALID_CREDENTIALS )
    private String password;
}
