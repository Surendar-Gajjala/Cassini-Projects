define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('payrollFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        payRollDetails: function (year) {
                            var url = "api/hrm/payroll/year/" + year;
                            return httpFactory.get(url);
                        },
                        newPayroll: function(month) {
                            var url = "api/hrm/payroll/new/"+month;
                            return httpFactory.get(url);
                        },
                        //attendanceHelper: function() {
                        //    var url = "api/common/attendencehelper";
                        //    return httpFactory.get(url);
                        //},
                        employeePayDetails: function () {
                            var urlArr =  [];
                            urlArr.push("api/hrm/employees/emppay");
                            urlArr.push("api/hrm/allowancetypes");
                            urlArr.push("api/hrm/deductiontypes");

                            return httpFactory.all(urlArr);
                        },
                        updateEmployeePay: function (payroll) {
                            var url = "api/hrm/employees/emppay";
                            return httpFactory.put(url,payroll);
                        },
                        payRollByMonth: function(month, year){
                            var url = "api/hrm/payroll/year/" + year + "/month/" + month;
                            return httpFactory.get(url);
                        },
                        runNewPayRoll: function (payroll) {
                            var url = "api/hrm/payroll";
                            return httpFactory.post(url,payroll);
                        }

                    }
                }
            ]
        );
    }
);