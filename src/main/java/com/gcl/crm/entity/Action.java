package com.gcl.crm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Long id;

    @Column(name = "action_name", nullable = false)
    private String name;
}
