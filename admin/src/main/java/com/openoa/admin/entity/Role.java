package com.openoa.admin.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import java.util.List;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "alias", nullable = false)
    private String alias;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "oauth_object_permission", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @WhereJoinTable(clause = "object_type=1")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Permission> authorities;
}
