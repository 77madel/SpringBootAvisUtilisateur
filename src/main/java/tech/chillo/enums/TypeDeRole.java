package tech.chillo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum TypeDeRole {
    UTILISATEUR(
            Set.of(TypePermission.UTILISATEUR_CREATE)
    ),

    MANAGER(
            Set.of(
                    TypePermission.MANAGER_CREATE,
                    TypePermission.MANAGER_READ,
                    TypePermission.MANAGER_UPDATE,
                    TypePermission.MANAGER_DELETE_AVIS
            )
    ),

    ADMINISTRATEUR(
            Set.of(
                    TypePermission.ADMINISTRATEUR_CREATE,
                    TypePermission.ADMINISTRATEUR_READ,
                    TypePermission.ADMINISTRATEUR_UPDATE,
                    TypePermission.ADMINISTRATEUR_DELETE,

                    TypePermission.MANAGER_CREATE,
                    TypePermission.MANAGER_READ,
                    TypePermission.MANAGER_UPDATE,
                    TypePermission.MANAGER_DELETE_AVIS
            )
    );


    @Getter
    Set<TypePermission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
       final List<SimpleGrantedAuthority> grantedAuthorities = this.getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.name())
        ).collect(Collectors.toList());

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
