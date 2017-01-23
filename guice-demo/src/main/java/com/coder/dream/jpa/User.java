package com.coder.dream.jpa;

import javax.persistence.*;

/**
 * Created by konghang on 2017/1/23.
 */
@Entity
@Table(name = "t_user")
public class User {

    private Long id;

    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
