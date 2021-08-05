package com.gcl.crm.repository;

import com.gcl.crm.dto.SourceEvaluationDto;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.form.PotentialSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class PotentialRepository2 {

    @Autowired
    private EntityManager entityManager;

    public List<PotentialSearchForm> getListPotentialToShare(List<Long> listSelectedId) {
        String sql = "select p1.id as potentialID, coalesce(p1.name, '') as name, coalesce (p1.phone_number, '') as phone, " +
                "coalesce (p1.email,'') as email, coalesce (p1.date, '') as time, coalesce (s.source_name, '') as source_name\n" +
                "from potential p1\n" +
                "inner join\n" +
                "source s on p1.source_id = s.source_id\n" +
                "where not exists(\n" +
                "    select 1\n" +
                "    from customer_distribution p2\n" +
                "    where p1.id = p2.potential_id\n" +
                "    )\n";
        if (!CollectionUtils.isEmpty(listSelectedId)) {
            sql = sql + " and p1.id in :list_selected_id";
        }
        Query query = entityManager.createNativeQuery(sql, "getPotentailFormToShare");

        if (!CollectionUtils.isEmpty(listSelectedId)) {
            query.setParameter("list_selected_id", listSelectedId);
        }
        return query.getResultList();
    }

    public Potential getReferenceById(Long id) {
        return entityManager.getReference(Potential.class, id);
    }

    public List<Potential> getListPotentialOfSale(Long mkt_id) {
        String sql = "select potential.*\n" +
                "from potential\n" +
                "inner join (\n" +
                "    select potential_id\n" +
                "    from customer_distribution\n" +
                "    where mkt_id = :mkt_id\n" +
                "    ) tmp\n" +
                "on tmp.potential_id = potential.id";

        Query query = entityManager.createNativeQuery(sql, Potential.class);
        query.setParameter("mkt_id", mkt_id);
        return query.getResultList();
    }

    public void ratingPotential() {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("PROC_RATING_POTENTIAL");
        storedProcedureQuery.execute();
    }

    public List<SourceEvaluationDto> getSourceEvaluation(String start_date, String end_date) {
        String sql = "select s.source_name,\n" +
                "       count(1) as num_of_potential,\n" +
                "       round((count(1) / poten_1.sum_of_source) * 100, 2) as source_percent,\n" +
                "       poten_1.sum_of_source\n" +
                "from potential\n" +
                "    inner join source s on potential.source_id = s.source_id\n" +
                "         cross join (\n" +
                "    select count(1) as sum_of_source\n" +
                "    from potential\n" +
                "    where str_to_date(date, '%d/%m/%Y') between STR_TO_DATE(:start_date, '%d/%m/%Y') and STR_TO_DATE(:end_date, '%d/%m/%Y')\n" +
                ") poten_1\n" +
                "where str_to_date(date, '%d/%m/%Y') between STR_TO_DATE(:start_date, '%d/%m/%Y') and STR_TO_DATE(:end_date, '%d/%m/%Y')\n" +
                "group by potential.source_id;";
        Query query = entityManager.createNativeQuery(sql, "getSourceEvaluation");
        query.setParameter("start_date", start_date);
        query.setParameter("end_date", end_date);
        return query.getResultList();
    }
}
