package com.camicompany.auth_service.config;

import com.camicompany.auth_service.model.Permission;
import com.camicompany.auth_service.model.Role;
import com.camicompany.auth_service.model.UserApp;
import com.camicompany.auth_service.reposiroty.IPermissionRepository;
import com.camicompany.auth_service.reposiroty.IRoleRepository;
import com.camicompany.auth_service.reposiroty.IUserAppRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;


    @Bean
    CommandLineRunner initData(
            IUserAppRepository userRepo,
            IRoleRepository roleRepo,
            IPermissionRepository permRepo,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

                //Permissions
            createPermissionIfNotExists(permRepo, "CREATE");
            createPermissionIfNotExists(permRepo, "READ");
            createPermissionIfNotExists(permRepo, "UPDATE");
            createPermissionIfNotExists(permRepo, "DELETE");

            Permission create = permRepo.findByPermissionName("CREATE").orElseThrow();
            Permission read = permRepo.findByPermissionName("READ").orElseThrow();
            Permission update = permRepo.findByPermissionName("UPDATE").orElseThrow();
            Permission delete = permRepo.findByPermissionName("DELETE").orElseThrow();

            // ROLES
            createRoleIfNotExists(roleRepo, "ADMIN", Set.of(create, read, update, delete));
            createRoleIfNotExists(roleRepo, "USER", Set.of(read, update, create));

            // Create user ADMIN if not exist
            if (!userRepo.existsByUsername(adminUsername)) {

                Role adminRole = roleRepo.findByRoleName("ADMIN").orElseThrow();

                UserApp admin = new UserApp();
                admin.setUsername(adminUsername);
                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );
                admin.setEnabled(true);
                admin.setAccountNotExpired(true);
                admin.setCredentialsNotExpired(true);
                admin.setAccountNotLocked(true);
                admin.setRole(adminRole);

                userRepo.save(admin);
                System.out.println("✅ Usuario ADMIN creado correctamente");
            }
        };
    }

    //Helper method to create permissions
    private void createPermissionIfNotExists(
            IPermissionRepository permRepo,
            String name) {
        if (!permRepo.existsByPermissionName(name)) {
            permRepo.save(new Permission(null, name));
        }
    }

    //Helper method to create roles
    private void createRoleIfNotExists(
            IRoleRepository roleRepo,
            String roleName,
            Set<Permission> permissionsList) {
        if (!roleRepo.existsByRoleName(roleName)) {
            Role role = new Role();
            role.setRoleName(roleName);
            role.setPermissionsList(permissionsList);
            roleRepo.save(role);
        }
    }
}
