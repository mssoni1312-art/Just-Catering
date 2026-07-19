package com.justcatering.superadmin.config;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.entity.UserRole;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.repository.RoleRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.repository.UserRoleEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bootstraps the default Super Admin account on application startup when missing.
 * <p>
 * Credentials are configurable via {@code app.bootstrap.super-admin.*}.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.super-admin.email:admin@justcatering.com}")
    private String email;

    @Value("${app.bootstrap.super-admin.password:Admin@12345}")
    private String password;

    @Value("${app.bootstrap.super-admin.first-name:Super}")
    private String firstName;

    @Value("${app.bootstrap.super-admin.last-name:Admin}")
    private String lastName;

    @Value("${app.bootstrap.super-admin.enabled:true}")
    private boolean enabled;

    /**
     * Creates the Super Admin user when it does not already exist.
     *
     * @param args application arguments
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        if (userRepository.existsByEmailIgnoreCaseAndDeletedFalse(email)) {
            log.debug("Super Admin already exists: {}", email);
            return;
        }

        Role superAdminRole = roleRepository.findByCodeAndDeletedFalse(AppConstants.ROLE_SUPER_ADMIN)
                .orElseThrow(() -> new IllegalStateException(
                        "SUPER_ADMIN role missing. Ensure Flyway migrations have run."
                ));

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email.toLowerCase())
                .passwordHash(passwordEncoder.encode(password))
                .companyName("Just Catering")
                .emailVerified(Boolean.TRUE)
                .failedLoginAttempts(0)
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        user = userRepository.save(user);

        UserRole userRole = UserRole.builder()
                .user(user)
                .role(superAdminRole)
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();
        userRoleEntityRepository.save(userRole);

        log.info("Bootstrapped Super Admin account: {}", email);
    }
}
