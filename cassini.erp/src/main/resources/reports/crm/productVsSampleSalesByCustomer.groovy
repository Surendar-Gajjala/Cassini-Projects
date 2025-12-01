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
    jpql = "SELECT NEW com.cassinisys.erp.scripting.dto.KeyAndNumber(s.order.orderType, SUM(s.invoiceAmount)) " +
            "FROM ERPCustomerOrderShipment s WHERE s.order.customer.id = " + customer.id;

    if(_dateRange != null) {
        String[] arr = _dateRange.split("-");
        String from = arr[0].trim();
        String to = arr[1].trim();

        jpql += " AND s.order.orderedDate BETWEEN '" + from + "' AND '" + to + "'";
    }

    jpql += " GROUP BY s.order.orderType";


    def orders = _entityManager.createQuery(jpql).getResultList();
    if(orders.size > 0) {
        def sample = 0;
        def product = 0;
        for(type in orders) {
            if(type.key == "PRODUCT") {
                product = type.number;
            }
            else if(type.key == "SAMPLE") {
                sample = type.number;
            }
        }
        def map = [customer: customer.name, customerId: customer.id, product: product, sample: sample];
        result.add(map);
    }
}

result.sort{-it.product};

return result;
