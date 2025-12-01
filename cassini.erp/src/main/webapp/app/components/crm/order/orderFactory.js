define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('orderFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        createOrder: function (order) {
                            var url = "api/crm/customerorders";
                            return httpFactory.post(url, order);
                        },
                        updateOrder: function (order) {
                            var url = "api/crm/customerorders/" + order.id;
                            return httpFactory.put(url, order);
                        },
                        getOrders: function (filters, pageable) {
                            var url = "api/crm/customerorders";

                            url += "?orderNumber={0}&customer={1}&region={2}&salesRep={3}&orderTotal={4}&status={5}&orderedDate={6}:{7}&invoiceNumber={8}&trackingNumber={9}&orderType={10}&customerId={11}&poNumber={12}&deliveryDate={13}:{14}&shipTo={15}".
                                format(filters.orderNumber, filters.customer, filters.region, filters.salesRep,
                                    filters.orderTotal, filters.status, filters.orderedDate.startDate, filters.orderedDate.endDate,
                                        filters.invoiceNumber, filters.trackingNumber, filters.orderType, filters.cusomterId, filters.poNumber, filters.deliveryDate.startDate, filters.deliveryDate.endDate,filters.shipTo);

                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);

                            return httpFactory.get(url);
                        },
                        searchOrders: function (filters, pageable) {
                            var url = "api/crm/customerorders/search";

                            url += "?orderNumber={0}&customer={1}&region={2}&salesRep={3}&orderTotal={4}&status={5}&orderedDate={6}:{7}&invoiceNumber={8}&trackingNumber={9}&orderType={10}&searchQuery={11}&deliveryDate={12}:{13}".
                                format(filters.orderNumber, filters.customer, filters.region, filters.salesRep,
                                filters.orderTotal, filters.status, filters.orderedDate.startDate, filters.orderedDate.endDate,
                                filters.invoiceNumber, filters.trackingNumber, filters.orderType, filters.searchQuery, filters.deliveryDate.startDate, filters.deliveryDate.endDate);

                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getOrder: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId;
                            return httpFactory.get(url);
                        },
                        getOrderItems: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/items";
                            return httpFactory.get(url);
                        },
                        getOrderHistory: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/history";
                            return httpFactory.get(url);
                        },
                        approveOrder: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/approve";
                            return httpFactory.get(url);
                        },
                        cancelOrder: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/cancel";
                            return httpFactory.get(url);
                        },
                        verifyOrder: function (orderId, verification) {
                            var url = "api/crm/customerorders/" + orderId + "/verify";
                            return httpFactory.post(url, verification);
                        },
                        getVerifications: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/verifications";
                            return httpFactory.get(url);
                        },
                        getNewOrders: function () {
                            var url = "api/crm/customerorders/new";
                            return httpFactory.get(url);
                        },
                        approveAllNewOrders: function () {
                            var url = "api/crm/customerorders/new/approveall";
                            return httpFactory.get(url);
                        },
                        dispatchOrder: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/dispatch";
                            return httpFactory.get(url);
                        },
                        processOrder: function (orderId) {
                            var url = "api/crm/customerorders/" + orderId + "/process";
                            return httpFactory.get(url);
                        },
                        getOrdersForShipments: function(shipmentIds) {
                            var url = "api/crm/customerorders/shipments/[" + shipmentIds + "]";
                            return httpFactory.get(url);
                        },
                        deleteOrderItem: function (itemId) {
                            var url = "api/crm/customerorders/item/" + itemId;
                            return httpFactory.delete(url);
                        },
                        getLateApproveOrders: function(pageable){
                            var url = "api/crm/customerorders/lateapprovedorders";
                            url += "?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        lateShippedOrders: function(pageable){
                        var url = "api/crm/customerorders/lateshippedorders";
                        url += "?page={0}&size={1}&sort={2}:{3}".
                            format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                        return httpFactory.get(url);
                        },
                        getLateProcessedOrders: function(pageable){
                            var url = "api/crm/customerorders/lateprocessedorders";
                            url += "?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getOrderVerifications: function(criteria, pageable) {
                            var url = "api/crm/customerorders/verifications";
                            url += "?orderNumber={0}&poNumber={1}&customer={2}&assignedTo={3}&status={4}".
                                        format(criteria.orderNumber, criteria.poNumber, criteria.customer, criteria.assignedTo, criteria.status);
                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },

                        updateInvoiceNumber:function(shipment){
                            var url = "api/crm/customerorders/shipment/" + shipment.id ;
                            return httpFactory.put(url, shipment);
                        },
                    }
                }
            ]
        );
    }
);