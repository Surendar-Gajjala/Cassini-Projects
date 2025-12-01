define(['app/app.modules', 'dropzone', 'app/components/hrm/attendance/employee/employeeAttendanceFactory'],
    function($app) {
        $app.controller('EmployeeAttendanceController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies', 'employeeAttendanceFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, employeeAttendanceFactory) {

                    $rootScope.iconClass = "fa flaticon-finance-and-business4";

                    $scope.attendance = [];
                    $scope.loading = true;

                    var init = function() {
                        var param = {};
                        param.empid = $stateParams.empid;
                        param.month = $stateParams.month;
                        param.year = $stateParams.year;

                        employeeAttendanceFactory.getEmployeeAttendace(param).then (
                            function(response) {
                                $rootScope.viewTitle = response.empName + " " + "Attendance";
                                $scope.attendance = response.attendance;
                                $scope.loading = false;
                            },
                            function (error) {
                                $scope.loading = true;
                            }
                        );
                    };

                    $scope.navigate = function() {
                        $state.go('app.hrm.attendance.employees', {'month':$stateParams.month, 'year': $stateParams.year });
                    };

                    init();



                }
            ]
        );
    }
);




