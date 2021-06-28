package com.gcl.crm.entity;

import com.gcl.crm.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date startDate;

    private Date endDate;

    private Long createBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "performer_id")
    private Employee performer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_id")
    private Potential potential;

    private Date createDate;

    private Status status;
}
