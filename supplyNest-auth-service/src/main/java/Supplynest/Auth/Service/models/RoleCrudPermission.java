package Supplynest.Auth.Service.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "role_crud_permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleCrudPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID crudRolePermissionId;

    @NotBlank(message = "Scope is required")
    private String scope;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable( name = "crud_operations", joinColumns = @JoinColumn(name = "crud_role_permission_id"))
    @Column(name = "operation", nullable = false)
    private Set<String> operations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
    private Role role;
}
