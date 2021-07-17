package com.gcl.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Diary implements Comparable<Diary> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(length = 1000)
    private String content;

    private Date createdDate;

    private Long maker;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_id")
    private Potential potential;

    @Override
    public int compareTo(Diary o) {
        if (this.createdDate.after(o.createdDate))
            return -1;
        if (this.createdDate.before(o.createdDate))
            return 1;
        return 0;
    }
}
