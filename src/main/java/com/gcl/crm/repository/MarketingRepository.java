package com.gcl.crm.repository;

import com.gcl.crm.dto.KPIMktGroup;
import com.gcl.crm.dto.SummaryCustomerManagement;
import com.gcl.crm.dto.SummaryMKTReport;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusEvaluationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
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

    public void createTempMKTKPITable(String startDate, String endDate) {
        String sql = "CREATE TEMPORARY TABLE TMP_MKT_KPI\n" +
                "select mkt_group.name      as                     marketing_name\n" +
                "                     , count(if(level_name = 'Level 6', 1, null)) level_6\n" +
                "                     , count(if(level_name = 'Level 7', 1, null)) level_7\n" +
                "                     , coalesce(sum(th.LOT), 0) as                     LOT\n" +
                "                     , mkt_group.id\n" +
                "                from marketing_group mkt_group\n" +
                "                         inner join customer_distribution cus_dis on mkt_group.id = cus_dis.mkt_id\n" +
                "                         inner join customer cus on cus.customer_id = cus_dis.customer_id\n" +
                "                         inner join level lv on cus.level_id = lv.level_id\n" +
                "                         left join trading_account ta on cus.account_number = ta.account_number\n" +
                "                         left join (\n" +
                "                                    select sum(lot) as LOT, account_number\n" +
                "                                    from transaction_history\n" +
                "                                    group by account_number\n" +
                "                                ) th on ta.account_number = th.account_number\n" +
                "                where cus_dis.date_distribution between\n" +
                "                          str_to_date(:start_date, '%d/%m/%Y') and str_to_date(:end_date, '%d/%m/%Y')\n" +
                "                and cus.account_number is not null\n" +
                "                group by mkt_group.id";

        entityManager.createNativeQuery("DROP TEMPORARY TABLE IF EXISTS TMP_MKT_KPI").executeUpdate();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);
        query.executeUpdate();

        entityManager.createNativeQuery("DROP TEMPORARY TABLE IF EXISTS TMP_MKT_KPI_1").executeUpdate();
        entityManager.createNativeQuery("CREATE TEMPORARY TABLE TMP_MKT_KPI_1 SELECT * FROM TMP_MKT_KPI").executeUpdate();
    }

    public SummaryMKTReport getMaxRegisteredAccountMKT() {
        String sql = "select marketing_name as name, level_6 as value\n" +
                "from TMP_MKT_KPI\n" +
                "where level_6 = (select MAX(level_6) from TMP_MKT_KPI_1)\n" +
                "limit 1";
        List result = entityManager.createNativeQuery(sql, "getSummaryMKTReport").getResultList();

        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

    public SummaryMKTReport getMaxTopUpAccountMKT() {
        String sql = "select marketing_name as name, level_7 as value\n" +
                "from TMP_MKT_KPI\n" +
                "where level_7 = (select MAX(level_7) from TMP_MKT_KPI_1)\n" +
                "limit 1";
        List result = entityManager.createNativeQuery(sql, "getSummaryMKTReport").getResultList();
        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

    public SummaryMKTReport getMaxLOTMKT() {
        String sql = "select marketing_name as name, lot as value\n" +
                "from TMP_MKT_KPI\n" +
                "where lot = (select MAX(lot) from TMP_MKT_KPI_1)\n" +
                "limit 1";
        List result = entityManager.createNativeQuery(sql, "getSummaryMKTReport").getResultList();
        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

    public List<SummaryCustomerManagement> getSummaryCustomerManagement(String startDate, String endDate) {
        String sql = "select\n" +
                "  CONCAT('Th??ng ', MONTH(cus_dis.date_distribution), '/', YEAR(cus_dis.date_distribution)) as MONTH_RANGE\n" +
                "                     , count(if(level_name = 'Level 6', 1, null)) level_6\n" +
                "                     , count(if(level_name = 'Level 7', 1, null)) level_7\n" +
                "                     , coalesce(sum(th.LOT), 0) as                     LOT\n" +
                "                         from customer_distribution cus_dis\n" +
                "                         inner join customer cus on cus.customer_id = cus_dis.customer_id\n" +
                "                         inner join level lv on cus.level_id = lv.level_id\n" +
                "                         left join trading_account ta on cus.account_number = ta.account_number\n" +
                "                         left join (\n" +
                "                                    select sum(lot) as LOT, account_number\n" +
                "                                    from transaction_history\n" +
                "                                    group by account_number\n" +
                "                                ) th on ta.account_number = th.account_number\n" +
                "                where cus_dis.date_distribution between\n" +
                "                          str_to_date(:start_date, '%d/%m/%Y') and str_to_date(:end_date, '%d/%m/%Y')\n" +
                "                and cus.account_number is not null\n" +
                "group by YEAR(cus_dis.date_distribution), MONTH(cus_dis.date_distribution)";

        Query query = entityManager.createNativeQuery(sql, "getSummaryCustomerManagement");
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);
        return query.getResultList();
    }

    public void createTempSourceKPI(String startDate, String endDate) {
        entityManager.createNativeQuery("DROP TEMPORARY TABLE IF EXISTS TMP_SOURCE_KPI").executeUpdate();

        String sql = "CREATE TEMPORARY TABLE TMP_SOURCE_KPI\n" +
                "select s.source_name\n" +
                "                     , count(if(level_name = 'Level 6', 1, null)) level_6\n" +
                "                     , count(if(level_name = 'Level 7', 1, null)) level_7\n" +
                "                     , coalesce(sum(th.LOT), 0) as                     LOT\n" +
                "                from marketing_group mkt_group\n" +
                "                         inner join customer_distribution cus_dis on mkt_group.id = cus_dis.mkt_id\n" +
                "                         inner join customer cus on cus.customer_id = cus_dis.customer_id\n" +
                "                         inner join level lv on cus.level_id = lv.level_id\n" +
                "                         inner join source s on cus.source_id = s.source_id\n" +
                "                         left join trading_account ta on cus.account_number = ta.account_number\n" +
                "                         left join (\n" +
                "                                    select sum(lot) as LOT, account_number\n" +
                "                                    from transaction_history\n" +
                "                                    group by account_number\n" +
                "                                ) th on ta.account_number = th.account_number\n" +
                "                where cus_dis.date_distribution between\n" +
                "                          str_to_date(:start_date, '%d/%m/%Y') and str_to_date(:end_date, '%d/%m/%Y')\n" +
                "                and cus.account_number is not null\n" +
                "                group by cus.source_id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start_date", startDate);
        query.setParameter("end_date", endDate);
        query.executeUpdate();

        entityManager.createNativeQuery("DROP TEMPORARY TABLE IF EXISTS TMP_SOURCE_KPI_1").executeUpdate();
        entityManager.createNativeQuery("CREATE TEMPORARY TABLE TMP_SOURCE_KPI_1 SELECT * FROM TMP_SOURCE_KPI").executeUpdate();
    }

    public SummaryMKTReport getMaxRegisteredAccountSource() {
        String sql = "select source_name as name\n" +
                "     , level_6 as value\n" +
                "from TMP_SOURCE_KPI\n" +
                "where level_6 = (select MAX(level_6) from TMP_SOURCE_KPI_1)\n" +
                "limit 1";
        List result = entityManager.createNativeQuery(sql, "getSummaryMKTReport").getResultList();
        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

    public SummaryMKTReport getMaxTopUpAccountSource() {
        String sql = "select source_name as name\n" +
                "     , level_7 as value\n" +
                "from TMP_SOURCE_KPI\n" +
                "where level_7 = (select MAX(level_7) from TMP_SOURCE_KPI_1)\n" +
                "limit 1";
        List result = entityManager.createNativeQuery(sql, "getSummaryMKTReport").getResultList();
        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

    public SummaryMKTReport getMaxLOTSource() {
        String sql = "select source_name as name\n" +
                "     , lot as value\n" +
                "from TMP_SOURCE_KPI\n" +
                "where lot = (select MAX(lot) from TMP_SOURCE_KPI_1)\n" +
                "limit 1";
        List result = entityManager.createNativeQuery(sql, "getSummaryMKTReport").getResultList();
        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

    public SummaryMKTReport getBrokerByUserId(Integer cusId) {
        String sql = "select mg.name as name, mg.code as value\n" +
                "from customer_distribution\n" +
                "inner join marketing_group mg on customer_distribution.mkt_id = mg.id\n" +
                "where customer_distribution.customer_id = :cus_id";
        Query query = entityManager.createNativeQuery(sql, "getSummaryMKTReport");
        query.setParameter("cus_id", cusId);
        List result = query.getResultList();
        return !CollectionUtils.isEmpty(result) ? (SummaryMKTReport) result.get(0) : null;
    }

}
