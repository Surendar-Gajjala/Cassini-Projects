define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('employeeAttendanceFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getEmployeeAttendace: function (param) {
                            var url = "api/hrm/employeeattendace/"  + param.empid + '/' + param.month + '/' + param.year;
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        )
    }
);