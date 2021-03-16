package com.openoa.admin.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_object_permission", uniqueConstraints = {@UniqueConstraint(columnNames = {"object_id", "object_type", "permission_id"})})
public class AuthObjectPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "object_id", nullable = false)
    private Integer objectId;
    @Column(name = "object_type", nullable = false)
    private Integer objectType;
    @Column(name = "permission_id", nullable = false)
    private Integer permissionId;
}
