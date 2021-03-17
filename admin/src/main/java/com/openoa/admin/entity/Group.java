package com.openoa.admin.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_group", uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "name"})})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "alias", nullable = false)
    private String alias;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "oauth_group_permission", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Permission> authorities;
}
