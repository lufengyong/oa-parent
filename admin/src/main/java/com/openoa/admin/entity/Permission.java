package com.openoa.admin.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_permission")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "scope")
    private String scope;
    @Column(name = "authority")
    private String authority;
    @Column(name = "alias")
    private String alias;
    @Override
    public String getAuthority() {
        return this.scope + "." + this.authority;
    }
}
