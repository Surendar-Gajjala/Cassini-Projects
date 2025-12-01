define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('businessUnitFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getBusinessUnits: function() {
                            var url = "api/common/businessunits";
                            return httpFactory.get(url);
                        },
                        getBusinessUnit: function(id) {
                            var url = "api/common/businessunits/" + id;
                            return httpFactory.get(url);
                        },
                        createBusinessUnit: function(bu) {
                            var url = "api/common/businessunits";
                            return httpFactory.post(url, bu);
                        },
                        updateBusinessUnit: function(bu) {
                            var url = "api/common/businessunits/" + bu.id;
                            return httpFactory.put(url, bu);
                        },
                        deleteBusinessUnit: function(id) {
                            var url = "api/common/businessunits/" + id;
                            return httpFactory.delete(url);
                        }
                    }
                }
            ]
        );
    }
);