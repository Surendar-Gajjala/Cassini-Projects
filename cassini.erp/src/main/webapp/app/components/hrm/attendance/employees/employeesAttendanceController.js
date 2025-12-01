define(['app/app.modules', 'app/components/hrm/attendance/employees/employeesAttendanceFactory'],
    function($app) {
        $app.controller('EmployeesAttendanceController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams','$cookies', 'employeesAttendanceFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, employeesAttendanceFactory) {

                    $rootScope.iconClass = "fa flaticon-finance-and-business4";
                    $rootScope.viewTitle = "Attendance";

                    $scope.attendance = [];
                    $scope.loading = true;

                    var init = function() {
                        var param = {};
                            param.month = $stateParams.month;
                            param.year = $stateParams.year;

                        employeesAttendanceFactory.getEmployeesAttendace(param).then (
                            function(response) {
                                $scope.attendance = response;
                                $scope.loading = false;
                            },

                            function (error) {
                                $scope.loading = true;
                            }
                        );
                    };

                    $scope.employeeDetails = function(emp) {
                        $state.go('app.hrm.attendance.employee', {'empid':emp.empNumber,'month':$stateParams.month, 'year': $stateParams.year});
                    };

                    $scope.navigate = function() {
                        $state.go('app.hrm.attendance.import');
                    };

                    init();
                }
            ]
        );
    }
);




