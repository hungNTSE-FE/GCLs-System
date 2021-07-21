package com.gcl.crm.repository;

import com.gcl.crm.form.TradingAccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TradingAccountRepository2 {

    @Autowired
    EntityManager entityManager;

    String sql = "SELECT cs.phone_number, cs.email, ta.account_number, ta.account_name, ta.broker_code, ta.broker_name\n" +
            "FROM customer cs\n" +
            "INNER JOIN trading_account ta on cs.account_number = ta.account_number\n" +
            "WHERE MONTH(ta.create_date) = MONTH(STR_TO_DATE(:date, '%Y-%m-%d'))\n" +
            "  AND YEAR(ta.create_date) = YEAR(STR_TO_DATE(:date, '%Y-%m-%d'))";

    public List<TradingAccountForm> getTradingAccountByMonth(String date) {
        Query query = entityManager.createNativeQuery(sql, "getTradingAccountByMonth");
        query.setParameter("date", date);
        return query.getResultList();
    }
}
