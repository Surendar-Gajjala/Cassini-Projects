/**
 * Created by Nageshreddy on 19-08-2018.
 */
define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('StoreFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                        getAllStores: function () {
                            var url = "api/stores";
                            return httpFactory.get(url);
                        },
                        getStore: function (storeId) {
                            var url = "api/stores/" + storeId;
                            return httpFactory.get(url);
                        },
                        createStore: function (store) {
                            var url = "api/stores";
                            return httpFactory.post(url, store);
                        },
                        updateStore: function (store) {
                            var url = "api/stores/" + store.id;
                            return httpFactory.put(url, store);
                        },
                        deleteStore: function (storeId) {
                            var url = "api/stores/"+storeId;
                            return httpFactory.delete(url);
                        },
                        getStores: function(pageable){
                            var url = "api/stores/pageable";
                            url += "?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        }
                    }

                }
            ]
        );
    }
);