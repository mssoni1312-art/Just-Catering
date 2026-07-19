package com.justcatering.superadmin.security;

import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.entity.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security {@link UserDetails} adapter over the domain {@link User}.
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final boolean accountNonLocked;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Set<String> roles;
    private final Set<String> permissions;

    /**
     * Creates a principal from a domain user.
     *
     * @param user domain user with roles loaded
     */
    public UserPrincipal(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPasswordHash();
        this.enabled = user.isAuthenticatable();
        this.accountNonLocked = !user.isAccountLocked();

        Set<Role> activeRoles = user.getActiveRoles();
        this.roles = activeRoles.stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());

        this.permissions = activeRoles.stream()
                .flatMap(role -> role.getActivePermissions().stream())
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        Set<GrantedAuthority> granted = new HashSet<>();
        this.roles.forEach(role -> granted.add(new SimpleGrantedAuthority("ROLE_" + role)));
        this.permissions.forEach(permission -> granted.add(new SimpleGrantedAuthority(permission)));
        this.authorities = granted;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
