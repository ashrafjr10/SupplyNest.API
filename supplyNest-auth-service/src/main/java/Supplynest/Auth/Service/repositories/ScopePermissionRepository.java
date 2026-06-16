package Supplynest.Auth.Service.repositories;

import Supplynest.Auth.Service.enums.modelEnums;
import Supplynest.Auth.Service.models.ScopePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScopePermissionRepository extends JpaRepository<ScopePermission, UUID> {
    boolean existsByScope(String key);

    boolean existsByScopeAndScopeType(String key, modelEnums.ScopeType value);
}
