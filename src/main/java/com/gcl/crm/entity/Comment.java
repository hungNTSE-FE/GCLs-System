package com.gcl.crm.entity;

import com.gcl.crm.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "potential_id")
    private Potential potential;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "poster_id")
    private Employee poster;

    private Date date;

    private Status status;
}
