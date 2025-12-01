package reports.crm

import com.cassinisys.erp.model.crm.OrderType;


String jpql = "SELECT invoiceItem.product.name, invoiceItem.shipment.order.orderType, SUM(invoiceItem.quantityShipped) " +
        "FROM ERPCustomerOrderShipmentDetails invoiceItem ";
if(_dateRange != null) {
    String[] arr = _dateRange.split("-");
    String from = arr[0].trim();
    String to = arr[1].trim();

    jpql += "WHERE invoiceItem.shipment.createdDate BETWEEN '" + from + "' AND '" + to + "'";
}
jpql += "GROUP BY invoiceItem.product.name, invoiceItem.shipment.order.orderType";


def results = _entityManager.createQuery(jpql).getResultList();

def map = [:];

for(result in results) {
    def book = result[0];
    def orderType = result[1];
    def quantity = result[2];
    def typeMap = [:];

    typeMap[OrderType.PRODUCT] = 0;
    typeMap[OrderType.SAMPLE] = 0;

    if(map[book] == null) {
        typeMap[orderType] = quantity;
        map[book] = typeMap;
    }
    else {
        def t = map[book];
        if(t[orderType] == null) {
            t[orderType] = quantity;
        }
        else {
            t[orderType] += quantity;
        }
    }
}

def list = [];
map.each { key, value ->
    def row = [:];
    row['book'] = key;
    row['product'] = value[OrderType.PRODUCT];
    row['sample'] = value[OrderType.SAMPLE];
    list.add(row);
}


return list;