define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('attendanceFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getCurrentMonthImportStatus: function(param) {
                            var url = "api/hrm/employeeattendace/"  + param.month + '/' + param.year + '/count';
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        )
    }
);