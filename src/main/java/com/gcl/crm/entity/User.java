package com.gcl.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(name = "APP_USER_UK", columnNames = "User_Name")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id", nullable = false)
    private Long userId;

    @Column(name = "User_Name", length = 36, nullable = false)
    private String userName;

    @Column(name = "Encryted_Password", nullable = false)
    private String encrytedPassword;

    @Column(name = "Enabled", length = 1, nullable = false)
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name ="employee_id")
    private Employee employee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> userRoles;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notification> notifications;
}
