define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('consignmentFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getConsignments: function(criteria, pageable) {
                            var dfd = $q.defer(),
                                url = "api/crm/consignments";

                                url += "?number={0}&status={1}&shipper={2}&shippedDate={3}:{4}&orderNumber={5}&value={6}&consignee={7}&invoiceNumber={8}&poNumber={9}&confirmationNumber={10}&vehicle={11}&driver={12}&through={13}&shippedTo={14}".
                                            format(criteria.number, criteria.status, criteria.shipper,
                                                    criteria.shippedDate.startDate, criteria.shippedDate.endDate,
                                                        criteria.orderNumber, criteria.value, criteria.consignee,
                                                            criteria.invoiceNumber, criteria.poNumber, criteria.confirmationNumber,
                                                                criteria.vehicle, criteria.driver, criteria.through,criteria.shippedTo);
                                url += "&page={0}&size={1}&sort={2}:{3}".
                                            format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);

                            return httpFactory.get(url);
                        },
                        createConsignment: function(consignment) {
                            var dfd = $q.defer(),
                                url = "api/crm/consignments";
                            return httpFactory.post(url, consignment);
                        },
                        updateConsignment: function(consignment) {
                            var dfd = $q.defer(),
                                url = "api/crm/consignments/" + consignment.id;
                            return httpFactory.put(url, consignment);
                        },
                        getConsignment: function(id) {
                            var dfd = $q.defer(),
                                url = "api/crm/consignments/" + id;
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        )
    }
);