package reports.crm

String jpql = "SELECT DISTINCT(o.status) FROM ERPCustomerOrder o";
def statuses = _entityManager.createQuery(jpql).getResultList();

def result = [];

for(st in statuses) {
    jpql = "SELECT NEW com.cassinisys.erp.scripting.dto.KeyAndNumber(o.orderType, COUNT(o)) " +
            "FROM ERPCustomerOrder o WHERE o.status = '" + st + "'";

    jpql += " GROUP BY o.orderType ORDER BY COUNT(o) DESC";

    def c = _entityManager.createQuery(jpql).getResultList();
    def map = [status: st, counts: c];
    result.add(map);
}

return result;
