package SupplyNest.Business.Service.models;

import SupplyNest.Common.constants.RegexPatterns;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {
    private UUID userId;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = RegexPatterns.REGEX_ONLY_LETTERS, message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = RegexPatterns.REGEX_ONLY_LETTERS, message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = RegexPatterns.REGEX_EMAIL, message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = RegexPatterns.REGEX_PHONE, message = "Invalid mobile number format")
    @Column(unique = true)
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @JsonIgnore
    private String password;

//    private Role role;

    private UUID businessId;

    private Boolean enabled;

    private Boolean accountNonLocked;

    private Boolean emailVerified;

    private Boolean mobileVerified;

    @Column(length = 2048)
    private String refreshToken;
}
