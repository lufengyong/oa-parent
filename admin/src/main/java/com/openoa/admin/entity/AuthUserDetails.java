package com.openoa.admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.WhereJoinTable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_user")
public class AuthUserDetails implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String username;
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "account_non_expired")
    private boolean accountNonExpired;
    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;
    @Column(name = "account_non_locked")
    private boolean accountNonLocked;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "oauth_object_permission",joinColumns = @JoinColumn(name = "object_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @WhereJoinTable(clause = "object_type='USER'")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Permission> authorities;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "oauth_user_group",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "group_id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Group> groups;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "oauth_user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Permission> allAuthorities = new ArrayList<>();
        allAuthorities.addAll(this.authorities);
        this.groups.forEach(group -> {
            allAuthorities.addAll(group.getAuthorities());
        });
        this.roles.forEach(role -> {
            allAuthorities.addAll(role.getAuthorities());
        });
        return allAuthorities.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean hasGroup(String groupMame)
    {
        return this.groups.stream().anyMatch(group -> group.getName() == groupMame);
    }

    public boolean hasRole(String roleMame)
    {
        return this.roles.stream().anyMatch(role -> role.getName() == roleMame);
    }

    public boolean hasPermission(String permissionFullMame)
    {
        return this.authorities.stream().anyMatch(permission -> permission.getAuthority() == permissionFullMame);
    }

}
