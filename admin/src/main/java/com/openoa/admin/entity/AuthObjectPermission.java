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
@Table(name = "oauth_object_permission")
public class AuthObjectPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "object_id")
    private Integer objectId;
    @Column(name = "object_type")
    private Integer objectType;
    @Column(name = "permission_id")
    private String permissionId;
}
