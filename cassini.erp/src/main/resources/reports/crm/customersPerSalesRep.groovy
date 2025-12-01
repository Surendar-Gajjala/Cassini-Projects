package reports.crm

String jpql = "SELECT NEW com.cassinisys.erp.scripting.dto.KeyAndNumber(c.salesRep.firstName, COUNT(c)) " +
        "FROM ERPCustomer c GROUP BY c.salesRep.firstName ORDER BY COUNT(c) DESC";

return _entityManager.createQuery(jpql).setMaxResults(10).getResultList();