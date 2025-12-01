package reports.hrm

String jpql = "SELECT NEW com.cassinisys.erp.scripting.dto.KeyAndNumber(e.department.name, COUNT(e)) " +
        "FROM ERPEmployee e GROUP BY e.department.name ORDER BY COUNT(e) DESC";

return _entityManager.createQuery(jpql).getResultList();