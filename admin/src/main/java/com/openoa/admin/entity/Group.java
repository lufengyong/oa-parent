package com.openoa.admin.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Setter
@Getter(value = AccessLevel.PUBLIC)
@Entity
@Table(name = "oauth_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "name")
    private String name;
    @Column(name = "alias")
    private String alias;
}
