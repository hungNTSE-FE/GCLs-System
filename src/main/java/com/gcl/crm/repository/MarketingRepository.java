package com.gcl.crm.repository;

import com.gcl.crm.dto.KPIMktGroup;
import com.gcl.crm.entity.TMP_KPI_EMPLOYEE;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusEvaluationForm;
import com.gcl.crm.form.KPIEmployeeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Repository
public class MarketingRepository {

    @Autowired
    EntityManager entityManager;

    public List<CustomerStatusForm> getCustomerStatusList(String startDate, String endDate) {
        String sql = "select marketing_name\n" +
                "     , sum(level_0) as level_0\n" +
                "     , sum(level_1) as level_1\n" +
                "     , sum(level_2) as level_2\n" +
                "     , sum(level_3) as level_3\n" +
                "     , sum(level_4) as level_4\n" +
                "     , sum(level_5) as level_5\n" +
                "     , sum(level_6) as level_6\n" +
                "     , sum(level_7) as level_7\n" +
                "     , sum(total) as total\n" +
                "from (\n" +
                "         select mkt_group.id   as                          mkt_id\n" +
                "              , mkt_group.name as                          marketing_name\n" +
                "              , count(if(level_name = 'Level 0', 1, null)) level_0\n" +
                "              , count(if(level_name = 'Level 1', 1, null)) level_1\n" +
                "              , count(if(level_name = 'Level 2', 1, null)) level_2\n" +
                "              , count(if(level_name = 'Level 3', 1, null)) level_3\n" +
                "              , count(if(level_name = 'Level 4', 1, null)) level_4\n" +
                "              , count(if(level_name = 'Level 5', 1, null)) level_5\n" +
                "              , count(if(level_name = 'Level 6', 1, null)) level_6\n" +
                "              , count(if(level_name = 'Level 7', 1, null)) level_7\n" +
                "              , count(1)       as                          total\n" +
                "         from marketing_group mkt_group\n" +
                "                  inner join customer_distribution cus_dis on mkt_group.id = cus_dis.mkt_id\n" +
                "                  inner join customer cus on cus.customer_id = cus_dis.customer_id\n" +
                "                  inner join level lv on cus.level_id = lv.level_id\n" +
                "         where cus_dis.date_distribution between\n" +
                "                   str_to_date(:start_date, '%d/%m/%Y') and str_to_date(:end_date, '%d/%m/%Y')\n" +
                "         group by mkt_group.id\n" +
                "         union\n" +
                "         select mkt_group.id   as                          mkt_id\n" +
                "              , mkt_group.name as                          marketing_name\n" +
                "              , count(if(level_name = 'Level 0', 1, null)) level_0\n" +
                "              , count(if(level_name = 'Level 1', 1, null)) level_1\n" +
                "              , count(if(level_name = 'Level 2', 1, null)) level_2\n" +
                "              , count(if(level_name = 'Level 3', 1, null)) level_3\n" +
                "              , count(if(level_name = 'Level 4', 1, null)) level_4\n" +
                "              , count(if(level_name = 'Level 5', 1, null)) level_5\n" +
                "              , count(if(level_name = 'Level 6', 1, null)) level_6\n" +
                "              , count(if(level_name = 'Level 7', 1, null)) level_7\n" +
                "              , count(1)       as                          total\n" +
                "         from marketing_group mkt_group\n" +
                "                  inner join customer_distribution cus_dis on mkt_group.id = cus_dis.mkt_id\n" +
                "                  inner join potential po on po.id = cus_dis.potential_id\n" +
                "                  inner join level lv on po.level_id = lv.level_id\n" +
                "         where cus_dis.date_distribution between\n" +
                "                   str_to_date(:start_date, '%d/%m/%Y') and str_to_date(:end_date, '%d/%m/%Y')\n" +
                "         and cus_dis.customer_id is null\n" +
                "         group by mkt_group.id\n" +
                "     ) tmp\n" +
                "group by mkt_id";
        Query query = entityManager.createNativeQuery(sql, "getCustomerStatusListMapping");
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);
        List<CustomerStatusForm> customerStatusFormList = query.getResultList();
        return customerStatusFormList;
    }

    public List<CustomerStatusEvaluationForm> getCustomerStatusEvaluationList(String startDate, String endDate) {
        String sql = "select mkt_group.name      as                     marketing_name\n" +
                "     , count(if(level_name = 'Level 6', 1, null)) level_6\n" +
                "     , count(if(level_name = 'Level 7', 1, null)) level_7\n" +
                "     , coalesce(sum(th.LOT), 0) as                     LOT\n" +
                "from marketing_group mkt_group\n" +
                "         inner join customer_distribution cus_dis on mkt_group.id = cus_dis.mkt_id\n" +
                "         inner join customer cus on cus.customer_id = cus_dis.customer_id\n" +
                "         inner join level lv on cus.level_id = lv.level_id\n" +
                "         left join trading_account ta on cus.account_number = ta.account_number\n" +
                "         left join (\n" +
                "                    select sum(lot) as LOT, account_number\n" +
                "                    from transaction_history\n" +
                "                    group by account_number\n" +
                "                ) th on ta.account_number = th.account_number\n" +
                "where cus_dis.date_distribution between\n" +
                "          str_to_date(:start_date, '%d/%m/%Y') and str_to_date(:end_date, '%d/%m/%Y')\n" +
                "and cus.account_number is not null\n" +
                "group by mkt_id";
        Query query = entityManager.createNativeQuery(sql, "getCustomerStatusReportListMapping");
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);
        return query.getResultList();
    }

    public List<KPIMktGroup> getKPIMktGroup(String fromDate, String toDate) {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("PROC_KPI_EVALUATION");
        storedProcedureQuery.registerStoredProcedureParameter("START_DATE", String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter("END_DATE", String.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("START_DATE", fromDate);
        storedProcedureQuery.setParameter("END_DATE", toDate);
        storedProcedureQuery.execute();

        String sql = "select\n" +
                    "MKT_GROUP_ID\n" +
                    ", MKT_GROUP_NAME\n" +
                    ", SUM_POT_DATA\n" +
                    ", SUM_LOT\n" +
                    ", KPI\n" +
                    ", NUM_LEVEL_6\n" +
                    ", NUM_LEVEL_7\n" +
                    "FROM tmp_kpi_employee\n" +
                    "ORDER BY KPI desc";
        Query query = entityManager.createNativeQuery(sql, "employeeKPIEvaluation");
        return query.getResultList();
    }

}
