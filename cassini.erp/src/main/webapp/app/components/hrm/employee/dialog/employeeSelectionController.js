define(['app/app.modules',
        'app/components/hrm/employee/employeeFactory'
    ],
    function ($app) {
        $app.controller('EmployeeSelectionController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                '$modalInstance', 'employeeFactory', 'selectedEmployee', 'dialogTitle',
                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          $modalInstance, employeeFactory, selectedEmployee, dialogTitle) {

                    $scope.dialogTitle = (dialogTitle != undefined || dialogTitle != null) ? dialogTitle : "Select Employee";
                    $scope.selectedEmployee = selectedEmployee;
                    $scope.loading = true;
                    $scope.emptyResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 10,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.employees = $scope.emptyResults;

                    $scope.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            label: "firstName",
                            field: "firstName",
                            order: "asc"
                        }
                    };

                    $scope.filters = {
                        firstName: "",
                        lastName: "",
                        jobTitle: "",
                        department: "",
                        manager: ""
                    };

                    $scope.filterButtonText = "Show Filters";

                    $scope.applyCriteria = function() {
                        $scope.pageable.page = 1;
                        $scope.loadEmployees();
                    };

                    $scope.selectEmployee = function(employee) {
                        $scope.selectedEmployee = employee;
                    };

                    $scope.resetCriteria = function() {
                        $scope.filters = {
                            firstName: "",
                            lastName: "",
                            jobTitle: "",
                            department: ""
                        };

                        $scope.pageable.page = 1;

                        $scope.loadCustomers();
                    };

                    $scope.ok = function() {
                        $modalInstance.close($scope.selectedEmployee);
                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.isEmployeeSelected = function() {
                        angular.forEach($scope.employees.content, function(employee) {
                            if(employee.selected == true) {
                                $scope.selectedEmployee = employee;
                            }
                        });
                    };

                    $scope.loadEmployees = function () {
                        employeeFactory.searchEmployees($scope.filters, $scope.pageable).then (
                            function(data) {
                                $scope.loading = false;
                                $scope.employees = data;
                            }
                        );
                    };

                    (function() {
                        $scope.loadEmployees();
                    })();

                }
            ]
        );
    }
);