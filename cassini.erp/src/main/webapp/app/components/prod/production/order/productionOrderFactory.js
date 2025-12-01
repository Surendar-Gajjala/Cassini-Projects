define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('productionOrderFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                        getProductionOrders: function(pageable) {
                            var url = "api/production/orders/pagable?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getAllProductionOrders: function() {
                            var url = "api/production/orders";
                            return httpFactory.get(url);
                        },
                        getProductionOrderDetails: function(orderId) {
                            var url = "api/production/orders/"+orderId +"/details";
                            return httpFactory.get(url);
                        },
                        getProductionOrder: function(orderId) {
                            var url = "api/production/orders/" + orderId;
                            return httpFactory.get(url);
                        },

                        updateProductionOrder: function(order) {
                            var url = "api/production/orders/" + order.id;
                            return httpFactory.put(url, order);
                        },

                        createProductionOrder: function(order) {
                            var url = "api/production/orders";
                            return httpFactory.post(url, order);
                        },
                        deleteProductionOrder: function(orderId){
                            var url = "api/production/orders/"+orderId;
                            return httpFactory.delete(url, material);

                        },
                        getCustomBomItems: function(bomItems) {
                            var url = "api/production/boms/custom";
                            return httpFactory.post(url,bomItems);
                        }

                    }
                }
            ]
        );
    }
);