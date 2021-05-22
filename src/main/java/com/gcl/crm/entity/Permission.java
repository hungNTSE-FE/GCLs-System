package com.gcl.crm.entity;

import com.gcl.crm.enums.PermissionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_name", nullable = false)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private PermissionStatus status;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<Department> departments;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<Employee> employee;
}
