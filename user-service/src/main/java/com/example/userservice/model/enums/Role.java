package com.example.userservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    Permissions.ADMIN_READ,
                    Permissions.ADMIN_CREATE,
                    Permissions.ADMIN_UPDATE,
                    Permissions.ADMIN_DELETE,

                    Permissions.USER_READ,
                    Permissions.USER_CREATE,
                    Permissions.USER_UPDATE,
                    Permissions.USER_DELETE
            )
    ),
    USER(
            Set.of(
                    Permissions.USER_READ,
                    Permissions.USER_CREATE,
                    Permissions.USER_UPDATE,
                    Permissions.USER_DELETE
            )
    );
    @Getter
    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
