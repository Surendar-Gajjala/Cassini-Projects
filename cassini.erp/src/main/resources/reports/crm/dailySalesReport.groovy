package reports.crm

import com.cassinisys.erp.model.crm.OrderType;


String jpql = "SELECT invoice.createdDate, invoice.order.orderType, SUM(invoice.invoiceAmount)" +
        "FROM ERPCustomerOrderShipment invoice";
if(_dateRange != null) {
    String[] arr = _dateRange.split("-");
    String from = arr[0].trim();
    String to = arr[1].trim();

    jpql += " WHERE invoice.createdDate BETWEEN '" + from + "' AND '" + to + "'";
}
jpql += " GROUP BY invoice.createdDate, invoice.order.orderType ORDER BY invoice.createdDate DESC";

def results = _entityManager.createQuery(jpql).getResultList();

def map = [:];

for(result in results) {
    def dbDate = new Date(result[0].getTime());
    def date = dbDate.format('dd/MM/yyyy');
    def orderType = result[1];
    def orderAmt = result[2];
    def typeMap = [:];

    typeMap[OrderType.PRODUCT] = 0.0;
    typeMap[OrderType.SAMPLE] = 0.0;


    if(map[date] == null) {
        typeMap[orderType] = orderAmt;
        map[date] = typeMap;
    }
    else {
        def t = map[date];
        if(t[orderType] == null) {
            t[orderType] = orderAmt;
        }
        else {
            t[orderType] += orderAmt;
        }
    }
}

def list = [];
map.each { key, value ->
    def row = [:];
    row['day'] = key;
    row['product'] = value[OrderType.PRODUCT];
    row['sample'] = value[OrderType.SAMPLE];
    list.add(row);
}



return list;
