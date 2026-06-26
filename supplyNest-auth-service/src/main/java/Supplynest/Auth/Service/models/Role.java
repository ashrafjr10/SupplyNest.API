package Supplynest.Auth.Service.models;

import SupplyNest.Common.constants.RegexPatterns;
import Supplynest.Auth.Service.enums.modelEnums;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @NotBlank(message = "name is required")
    @Pattern(regexp = RegexPatterns.REGEX_LETTERS_AND_SPACES, message = "name should contain only alphabets and spaces")
    @Size(max = 50, min = 3, message = "name should be between 3 and 50 characters")
    private String name;

    @Pattern(regexp = RegexPatterns.REGEX_ONLY_LETTERS, message = "description must contain only letters")
    @Size(max = 300, min = 3, message = "description must be between 3 and 300 characters")
    private String description;

    @NotNull(message = "role type is required")
    @Enumerated(EnumType.STRING)
    private modelEnums.RoleTypes roleTypes;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<RoleCrudPermission> crudPermissions = new HashSet<>();

    public void addPermission(RoleCrudPermission permission) {
        permission.setRole(this);
        this.crudPermissions.add(permission);
    }
}
