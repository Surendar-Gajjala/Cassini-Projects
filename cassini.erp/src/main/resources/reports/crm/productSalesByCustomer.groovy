package reports.crm

String jpql = "SELECT DISTINCT(o.customer) FROM ERPCustomerOrder o";

if(_dateRange != null) {
    String[] arr = _dateRange.split("-");
    String from = arr[0].trim();
    String to = arr[1].trim();

    jpql += " WHERE o.orderedDate BETWEEN '" + from + "' AND '" + to + "'";
}
def customers = _entityManager.createQuery(jpql).getResultList();

def result = [];

for(customer in customers) {
    jpql = "SELECT SUM(o.orderTotal) " +
            "FROM ERPCustomerOrder o WHERE o.customer.id = " + customer.id + " AND o.orderType = 'PRODUCT'";

    if(_dateRange != null) {
        String[] arr = _dateRange.split("-");
        String from = arr[0].trim();
        String to = arr[1].trim();

        jpql += " AND o.orderedDate BETWEEN '" + from + "' AND '" + to + "'";
    }

    def orders = _entityManager.createQuery(jpql).getResultList();
    if(orders.size > 0 && orders[0] != null) {
        def map = [customer: customer.name, orders: orders[0]];
        result.add(map);
    }
}

result.sort{it.orders};
result.reverse(true);

return result;
