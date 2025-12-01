define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function (app) {
        app.factory('navFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getNavigation: function () {
                            var dfd = $q.defer(),
                                url = "app/components/main/navigation.json";
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        );
    }
);