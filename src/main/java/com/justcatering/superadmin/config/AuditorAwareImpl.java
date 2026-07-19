package com.justcatering.superadmin.config;

import com.justcatering.superadmin.security.UserPrincipal;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Resolves the current auditor (user id) for JPA auditing.
 */
@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<Long> {

    /**
     * Returns the current authenticated user id when available.
     *
     * @return optional user id
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return Optional.empty();
        }
        return Optional.ofNullable(principal.getId());
    }
}
