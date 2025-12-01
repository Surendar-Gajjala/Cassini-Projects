/**
 * Created by vhk009 on 8/12/2015.
 */
define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('employeeDetailsFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        employeeDetails: function (employeeId) {
                            var url = "api/hrm/employees/" + employeeId;
                            return httpFactory.get(url);
                        },
                        updateEmployeeDetails: function (employee) {
                            var url = "api/hrm/employees/" + employee.id;
                            return httpFactory.put(url,employee);
                        },
                        attendanceDetails: function (employeeId,month,year) {
                            var url = "api/hrm/employeeattendace/" + employeeId + "/" + month + "/" + year;
                            return httpFactory.get(url);
                        },
                        departmentDetails: function (depId) {
                            var url = "api/hrm/departments/" + depId;
                            return httpFactory.get(url);
                        },
                        timeOfRequest: function (timeOffData) {
                            var url = "api/hrm/employeetimeoff";
                            return httpFactory.post(url,timeOffData);
                        },
                        empTimeOfRequest: function (employeeId) {
                            var url = "api/hrm/employeetimeoff/"+employeeId;
                            return httpFactory.get(url);
                        },
                        timeOffByMonthAndYear: function (empId,month,year) {
                            var url = "api/hrm/employeetimeoff/byMonth/"+ empId + "/" + month + "/" + year;
                            return httpFactory.get(url);
                        },

                        timeOffCount: function (startDate,endDate) {
                            var url = "api/hrm/employeetimeoff/timeoffcount/" + startDate + "/" + endDate;
                            return httpFactory.get(url);
                        },
                        loanRequest: function (loanData) {
                            var url = "api/hrm/employeeloans";
                            return httpFactory.post(url,loanData);
                        },
                        timeOffTypes: function () {
                            var url = "api/hrm/timeofftype";
                            return httpFactory.get(url);
                        },
                        /*loanTypes: function () {
                            var url = "api/hrm/loantypes";
                            return httpFactory.get(url);
                        },*/
                        /*loanDetails: function(employeeId) {
                            var url = "api/hrm/employeeloans/employee/" + employeeId;
                            return httpFactory.get(url);
                        },*/
                        employeePayDetails: function (empId,year) {
                            var urlArr =  [];
                            urlArr.push("api/hrm/allowancetypes");
                            urlArr.push("api/hrm/deductiontypes");
                            urlArr.push("api/hrm/payroll/profilepay/"+ empId +"/" + year);

                            return httpFactory.all(urlArr);
                        }
                    }
                }
            ]
        );
    }
);