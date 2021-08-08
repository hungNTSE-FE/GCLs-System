package com.gcl.crm.entity;

import com.gcl.crm.dto.KPIMktGroup;
import com.gcl.crm.dto.SummaryCustomerManagement;
import com.gcl.crm.dto.SummaryMKTReport;
import com.gcl.crm.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@SqlResultSetMapping(
        name = "employeeKPIEvaluation",
        classes = @ConstructorResult(
                targetClass = KPIMktGroup.class
                , columns = {
                @ColumnResult(name = "MKT_GROUP_ID", type = Long.class),
                @ColumnResult(name = "MKT_GROUP_NAME", type = String.class),
                @ColumnResult(name = "SUM_POT_DATA", type = Integer.class),
                @ColumnResult(name = "SUM_LOT", type = Integer.class),
                @ColumnResult(name = "KPI", type = Double.class),
                @ColumnResult(name = "NUM_LEVEL_6", type = Integer.class),
                @ColumnResult(name = "NUM_LEVEL_7", type = Integer.class),
        }
        )
)
@SqlResultSetMapping(
        name = "getSummaryMKTReport",
        classes = @ConstructorResult(
                targetClass = SummaryMKTReport.class
                , columns = {
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "value", type = Integer.class),
        }
        )
)
@SqlResultSetMapping(
        name = "getSummaryCustomerManagement",
        classes = @ConstructorResult(
                targetClass = SummaryCustomerManagement.class
                , columns = {
                @ColumnResult(name = "MONTH_RANGE", type = String.class),
                @ColumnResult(name = "level_6", type = Integer.class),
                @ColumnResult(name = "level_7", type = Integer.class),
                @ColumnResult(name = "LOT", type = Integer.class),
        }
        )
)
public class MarketingGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String note;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marketingGroup")
    private List<Employee> employees;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marketingGroup")
    private List<CustomerDistribution> customerDistributionList;

    private Date createDate;

    // Id of last employee create group
    private Long maker;

    private Date lastModified;

    // Id of last employee made changes to group
    private Long lastModifier;
}
