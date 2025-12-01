define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('employeesAttendanceFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getEmployeesAttendace: function (param) {
                            var url = "api/hrm/employeeattendace/" + param.month + '/' + param.year;
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        )
    }
);