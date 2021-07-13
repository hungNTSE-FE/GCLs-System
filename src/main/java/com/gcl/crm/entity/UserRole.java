package com.gcl.crm.entity;

import com.gcl.crm.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "User_Role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    private Date createdDate;

    private Long creator;

    private Date lastModified;

    private Long lastModifier;

    @Column(name = "active")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

}