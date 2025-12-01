define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('productionFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                        getProductionOrders: function(pageable) {
                            var url = "api/production/orders/pagable?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        );
    }
);