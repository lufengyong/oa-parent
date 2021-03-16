package com.openoa.admin.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_permission", uniqueConstraints = {@UniqueConstraint(columnNames = {"scope", "authority"})})
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "scope", nullable = false)
    private String scope;
    @Column(name = "authority", nullable = false)
    private String authority;
    @Column(name = "alias", nullable = false)
    private String alias;

    @Override
    public String getAuthority() {
        return this.scope + "." + this.authority;
    }
}
