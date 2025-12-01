define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('timeoffsFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        
                        allTimeoffs: function (pageable) {
                            var url = "api/hrm/employeetimeoff/pageable?page={0}&size={1}".
                                format(pageable.page-1, pageable.size);
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        );
    }
);