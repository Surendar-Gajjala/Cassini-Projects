define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('shipmentFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {

                    return {
                        getShipments: function(criteria, pageable) {
                            var dfd = $q.defer(),
                                url = "api/crm/shipments";
                            url += "?invoiceNumber={0}&status={1}&orderNumber={2}&customer={3}&poNumber={4}&date={5}:{6}&shipTo={7}".
                                format(criteria.invoiceNumber, criteria.status, criteria.orderNumber,
                                criteria.customer, criteria.poNumber,
                                criteria.createdDate.startDate, criteria.createdDate.endDate, criteria.shipTo);

                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);

                            return httpFactory.get(url);
                        },
                        getConsignmentsForShipments: function(shipments) {
                            var dfd = $q.defer(),
                                url = "api/crm/consignments/shipments/" + shipments;
                            return httpFactory.get(url);
                        },

                        searchShipments: function(filters, pageable) {
                            var dfd = $q.defer(),
                                url = "api/crm/shipments/search";
                            url += "?page={0}&size={1}&sort={2}:{3}&searchQuery={4}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order, filters.searchQuery);
                            return httpFactory.get(url);
                        },

                        getConsignmentsByOrderId: function(orderId) {
                            var dfd = $q.defer(),
                                url = "api/crm/consignments/shipments/" + orderId;
                            return httpFactory.get(url);
                        },

                        getOrderShipments: function (orderId) {
                            var dfd = $q.defer(),
                                url = "api/crm/shipments/{0}".format(orderId);
                            return httpFactory.get(url);
                        },
                        updateShipment: function(shipment) {
                            var url = "api/crm/shipments/" + shipment.id;
                            return httpFactory.put(url, shipment)
                        },

                        cancelShipment: function (shipmentId) {
                            var url = "api/crm/shipments/" + shipmentId + "/cancel";
                            return httpFactory.get(url);
                        },

                        deleteShipmentItem: function(shipmentId, itemId) {
                            var url = "api/crm/shipments/" + shipmentId + "/details/" + itemId;
                            return httpFactory.delete(url);
                        },

                        deleteShipmentFromConsignment: function(shipmentId) {
                            var url = "api/crm/consignments/shipments/" + shipmentId;
                            return httpFactory.delete(url);
                        },

                        cancelShipment: function (shipmentId) {
                            var url = "api/crm/shipments/"+ shipmentId +"/cancel";
                            return httpFactory.get(url);
                        },

                        getShipmentDetails: function (shipmentId) {
                            var dfd = $q.defer(),
                                url = "api/crm/shipments/{0}/details".format(shipmentId);
                            return httpFactory.get(url);
                        },
                        getShipmentByInvoiceNumber: function (invoiceNumber) {
                            var dfd = $q.defer(),
                                url = "api/crm/shipments/invoice/{0}".format(invoiceNumber);
                            return httpFactory.get(url);
                        },
                        processShipment: function (orderId, ship, shipment) {
                            var dfd = $q.defer(),
                                url = "api/crm/shipments/{0}?ship={1}".format(orderId, ship);
                            return httpFactory.post(url, shipment);
                        },
                        getShippers: function() {
                            var dfd = $q.defer,
                                url = "api/crm/shipments/shippers";
                            return httpFactory.get(url);
                        },
                        createShipper: function(shipper) {
                            var dfd = $q.defer,
                                url = "api/crm/shipments/shippers";
                            return httpFactory.post(url, shipper);
                        },
                        updateShipper: function(shipper) {
                            var dfd = $q.defer,
                                url = "api/crm/shipments/shippers/" + shipper.id;
                            return httpFactory.put(url, shipper);
                        },
                        getShipmentsByIds: function(shipmentIds) {
                            var url = "api/crm/shipments/[" + shipmentIds + "]";
                            return httpFactory.get(url);
                        }
                    }

                }
            ]
        )
    }
);