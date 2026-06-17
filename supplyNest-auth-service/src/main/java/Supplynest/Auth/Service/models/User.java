package Supplynest.Auth.Service.models;

import Supplynest.Auth.Service.constants.AppConstants;
import Supplynest.Auth.Service.constants.RegexPatterns;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    @Pattern(regexp = RegexPatterns.REGEX_PASSWORD, message = AppConstants.VALIDATION_PASSWORD)
    @JsonIgnore
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    private UUID businessId;

    private Boolean enabled;

    private Boolean accountNonLocked;

    private Boolean emailVerified;

    private Boolean mobileVerified;

    private String refreshToken;
}
