package reports.crm

String jpql = "SELECT NEW com.cassinisys.erp.scripting.dto.KeyAndNumber(o.status, SUM(o.orderTotal)) " +
        "FROM ERPCustomerOrder o";

if(_dateRange != null) {
    String[] arr = _dateRange.split("-");
    String from = arr[0].trim();
    String to = arr[1].trim();

    jpql += " WHERE o.orderedDate BETWEEN '" + from + "' AND '" + to + "'";
}

jpql += " GROUP BY o.status ORDER BY SUM(o.orderTotal) DESC";

return _entityManager.createQuery(jpql).getResultList();