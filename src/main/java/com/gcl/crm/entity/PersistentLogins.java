package com.gcl.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class PersistentLogins {

    @Id
    private String username;
}