package com.openoa.admin.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "alias")
    private String alias;
}
