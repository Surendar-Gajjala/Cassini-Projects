define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('salesRepFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        createSalesRep: function(salesRep) {
                            var url = "api/crm/salesreps";
                            return httpFactory.post(url, salesRep);
                        },
                        getSalesReps: function(pageable) {
                            var url = "api/crm/salesreps";
                            url += "?page={0}&size={1}&sort={2}:{3}".format(pageable.page-1, pageable.size,
                                    pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getSalesRep: function(salesRepId) {
                            var url = "api/crm/salesreps/" + salesRepId;
                            return httpFactory.get(url);
                        },
                        getSalesRepCustomers: function(salesRepId, criteria, pageable) {
                            var url = "api/crm/salesreps/{0}/customers?page={1}&size={2}&sort={3}:{4}".
                                        format(salesRepId, pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);

                                url += "&name={0}&region={1}&contactPerson={2}&contactPhone={3}".
                                            format(criteria.name, criteria.region, criteria.contactPerson, criteria.contactPhone);

                            return httpFactory.get(url);
                        },
                        getSalesRepOrders: function(salesRepId, filters, pageable) {
                            var url = "api/crm/salesreps/{0}/orders?page={1}&size={2}&sort={3}:{4}".
                                    format(salesRepId, pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);

                                url += "&orderNumber={0}&customer={1}&region={2}&orderTotal={3}&status={4}&orderedDate={5}:{6}&orderType={7}".
                                    format(filters.orderNumber, filters.customer, filters.region, filters.orderTotal,
                                        filters.status, filters.orderedDate.startDate, filters.orderedDate.endDate,
                                            filters.orderType);

                            return httpFactory.get(url);
                        },
                        getSalesRepFieldReports: function(salesRep, pageable) {
                            var url = "api/crm/salesreps/{0}/fieldreports?page={1}&size={2}&sort={3}:{4}".
                                            format(salesRep, pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);

                            return httpFactory.get(url);
                        },
                        createSalesRepFieldReport: function(fieldReport) {
                            var url = "api/crm/salesrepfieldreports";

                            return httpFactory.post(url, fieldReport);
                        },
                        getAllFieldReports: function(criteria, pageable) {
                            var url = "api/crm/salesreps/fieldreports?salesRep={0}&customer={1}&timestamp={2}&notes={3}".
                                        format(criteria.salesRep, criteria.customer, criteria.timestamp, criteria.notes);
                                url += "&page={0}&size={1}&sort={2}:{3}".format(pageable.page-1, pageable.size,
                                            pageable.sort.field, pageable.sort.order);

                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        );
    }
);