package com.gcl.crm.entity;

import javax.persistence.*;

@Entity
@Table(name = "User_Role",uniqueConstraints = {
        @UniqueConstraint(name = "USER_ROLE_UK", columnNames = {"User_Id", "Role_Id"})
})
public class UserRole {
    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id", nullable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Role_Id", nullable = false)
    private AppRole appRole;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppRole getAppRole() {
        return appRole;
    }

    public void setAppRole(AppRole appRole) {
        this.appRole = appRole;
    }
}
