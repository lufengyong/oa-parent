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
    @JoinTable(name = "oauth_object_permission", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @WhereJoinTable(clause = "object_type=2")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Permission> authorities;
}
