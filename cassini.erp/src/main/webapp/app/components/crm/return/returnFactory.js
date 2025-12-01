define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('returnFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        createReturn: function (order) {
                            var dfd = $q.defer(),
                                url = "api/crm/customerReturns";
                            return httpFactory.post(url, order);
                        },
                        getReturns: function (filters, pageable) {
                            var dfd = $q.defer(),
                                url = "api/crm/customerReturns";

                            url += "?returnDate={0}&customer={1}&region={2}&district={3}&salesRep={4}&reason={5}&status={6}".
                                format(filters.returnDate, filters.customer,
                                filters.region, filters.district, filters.salesRep, filters.reason, filters.status);

                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);

                            return httpFactory.get(url);
                        },
                        approveAllNewReturns: function () {
                            var url = "api/crm/customerReturns/new/approveAll";
                            return httpFactory.get(url);
                        },
                        approveReturn: function (returnId) {
                            var url = "api/crm/customerReturns/" + returnId + "/approve";
                            return httpFactory.get(url);
                        },
                        cancelOrder: function (returnId) {
                            var url = "api/crm/customerReturns/" + returnId + "/cancel";
                            return httpFactory.get(url);
                        },
                        getReturn: function (returnId) {
                            var dfd = $q.defer(),
                                url = "api/crm/customerReturns/" + returnId;
                            return httpFactory.get(url);
                        },
                        getReturnItems: function (returnId) {
                            var url = "api/crm/customerReturns/" + returnId + "/details";
                            return httpFactory.get(url);
                        },
                    }
                }
            ]
        );
    }
);