define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('attendanceFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                    }
                }
            ]
        );
    }
);