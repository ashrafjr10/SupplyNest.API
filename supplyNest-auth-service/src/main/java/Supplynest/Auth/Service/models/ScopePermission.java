package Supplynest.Auth.Service.models;

import SupplyNest.Common.constants.RegexPatterns;
import Supplynest.Auth.Service.enums.modelEnums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "scope_permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScopePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID scopePermissionId;

    @NotBlank(message = "scope is required")
    @Column(unique = true)
    private String scope;

    @Size(max = 300, min = 3, message = "description must be between 3 and 300 characters")
    @Pattern(regexp = RegexPatterns.REGEX_ONLY_LETTERS, message = "description must contain only letters")
    private String description;

    @NotNull(message = "scope type is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.ScopeType scopeType;
}
