package reports.crm

String jpql = "SELECT NEW com.cassinisys.erp.scripting.dto.KeyAndNumber(o.customer.salesRep.firstName, SUM(o.orderTotal)) " +
        "FROM ERPCustomerOrder o";

if(_dateRange != null) {
    String[] arr = _dateRange.split("-");
    String from = arr[0].trim();
    String to = arr[1].trim();

    jpql += " WHERE o.orderedDate BETWEEN '" + from + "' AND '" + to + "'";
}

jpql += " GROUP BY o.customer.salesRep.firstName ORDER BY SUM(o.orderTotal) DESC";

return _entityManager.createQuery(jpql).setMaxResults(10).getResultList();