package Supplynest.Auth.Service.utils;

import Supplynest.Auth.Service.enums.modelEnums;
import Supplynest.Auth.Service.models.Role;
import Supplynest.Auth.Service.models.RoleCrudPermission;
import Supplynest.Auth.Service.models.ScopePermission;
import Supplynest.Auth.Service.repositories.RoleRepository;
import Supplynest.Auth.Service.repositories.ScopePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SystemRoleInitializer {

    private static final Logger logger = LoggerFactory.getLogger(SystemRoleInitializer.class);
    private final RoleRepository roleRepository;
    private final ScopePermissionRepository scopePermissionRepository;

//    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role superAdmin = roleRepository
                .findByName("SUPER_ADMIN")
                .orElseGet(this::createSuperAdminRole);

        syncSuperAdminPermissions(superAdmin);
    }

    private Role createSuperAdminRole() {
        Role role = new Role();
        role.setName("SUPER_ADMIN");
        role.setRoleTypes(modelEnums.RoleTypes.SUPER_ADMIN);
        role.setDescription("System generated role with full access");
        role.setCreatedBy("SYSTEM");
        role.prePersist();

        roleRepository.save(role);
        logger.info("SUPER_ADMIN role created");

        return role;
    }

    private void syncSuperAdminPermissions(Role role) {

        // Enum = source of truth
        Set<String> enumOperations = Arrays.stream(modelEnums.CrudOperation.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        // Existing permissions mapped by scope
        Map<String, RoleCrudPermission> existingPermissions =
                role.getCrudPermissions()
                        .stream()
                        .collect(Collectors.toMap(
                                RoleCrudPermission::getScope,
                                p -> p
                        ));

        List<ScopePermission> crudPermissionScopes = scopePermissionRepository.findAll();
        if (crudPermissionScopes.isEmpty())
            crudPermissionScopes = createCrudPermissionScopes();

        for (ScopePermission scopeEnum : crudPermissionScopes) {

            String scope = scopeEnum.getScope();

            RoleCrudPermission permission =
                    existingPermissions.get(scope);

            if (permission == null) {
                // New scope added in enum → create once
                permission = new RoleCrudPermission();
                permission.setScope(scope);
                permission.setOperations(new HashSet<>(enumOperations));
                role.addPermission(permission);

                logger.info("Added new permission scope {}", scope);
                continue;
            }

            // 🔁 SYNC OPERATIONS IN-PLACE

            // Add new operations
            for (String op : enumOperations) {
                if (!permission.getOperations().contains(op)) {
                    permission.getOperations().add(op);
                    logger.info("Added operation {} to scope {}", op, scope);
                }
            }

            // Remove deleted operations
            permission.getOperations().removeIf(op -> !enumOperations.contains(op));
        }

        // Remove permissions whose scope no longer exists in enum
        Set<String> enumScopes = crudPermissionScopes.stream()
                .map(ScopePermission::getScope)
                .collect(Collectors.toSet());

        role.getCrudPermissions()
                .removeIf(p -> !enumScopes.contains(p.getScope()));

        roleRepository.save(role);
        logger.info("SUPER_ADMIN permissions synced incrementally");
    }

    private List<ScopePermission> createCrudPermissionScopes(){
        Map<String, modelEnums.ScopeType> scope = new HashMap<>();
        scope.put("SUBSCRIPTION", modelEnums.ScopeType.PLATFORM);
        scope.put("USER", modelEnums.ScopeType.PLATFORM);
        scope.put("Role", modelEnums.ScopeType.PLATFORM);

        scope.put("User Role", modelEnums.ScopeType.USER);
        scope.put("Business", modelEnums.ScopeType.USER);
        scope.put("BusinessGroup", modelEnums.ScopeType.USER);
        scope.put("BusinessAddress", modelEnums.ScopeType.USER);
        scope.put("SubscribeBusiness", modelEnums.ScopeType.USER);
        scope.put("Order", modelEnums.ScopeType.USER);
        scope.put("Profile", modelEnums.ScopeType.USER);
        scope.put("Settings", modelEnums.ScopeType.USER);
        scope.put("Product", modelEnums.ScopeType.USER);
        scope.put("Inventory", modelEnums.ScopeType.USER);

        List<ScopePermission> scopePermissionList = new ArrayList<>();

        for (Map.Entry<String, modelEnums.ScopeType> entry : scope.entrySet()) {

            if (!scopePermissionRepository.existsByScopeAndScopeType(entry.getKey(), entry.getValue())) {
                ScopePermission scopePermission = new ScopePermission();
                scopePermission.setScope(entry.getKey());
                scopePermission.setScopeType(entry.getValue());

                scopePermissionList.add(scopePermission);
            }
        }

        return scopePermissionRepository.saveAll(scopePermissionList);
    }
}
