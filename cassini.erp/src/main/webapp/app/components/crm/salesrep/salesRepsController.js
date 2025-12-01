define(['app/app.modules',
        'app/components/crm/salesrep/salesRepFactory',
        'app/components/hrm/employee/dialog/employeeSelectionController'
    ],
    function($app) {
        $app.controller('SalesRepsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$modal',
                'salesRepFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, $modal,
                         salesRepFactory) {

                    $rootScope.iconClass = "fa flaticon-businessman277";
                    $rootScope.viewTitle = "Sales Reps";

                    $scope.emptyPagedResults = {
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
                    $scope.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            label: "firstName",
                            field: "firstName",
                            order: "asc"
                        }
                    };

                    $scope.salesReps = $scope.emptyPagedResults;

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('salesreps-view-tb');
                    });

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.salesReps.content = [];
                        $scope.loadSalesReps();
                    };

                    $scope.loadSalesReps = function() {
                        salesRepFactory.getSalesReps($scope.pageable).then (
                            function(data) {
                                $scope.salesReps = data;
                            }
                        )
                    };

                    $rootScope.addSalesRep = function() {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/hrm/employee/dialog/employeeSelectionDialog.jsp',
                            controller: 'EmployeeSelectionController',
                            size: 'lg',
                            resolve: {
                                selectedEmployee: function () {
                                    return null;
                                },
                                "dialogTitle": "Select an employee as a sales rep"
                            }
                        });

                        modalInstance.result.then(
                            function (selectedEmployee) {
                                salesRepFactory.getSalesRep(selectedEmployee.id).then(
                                    function(data) {
                                        if(data == null || data == "") {
                                            salesRepFactory.createSalesRep(selectedEmployee).then(
                                                function(data) {
                                                    $scope.salesReps.content.unshift(data);
                                                }
                                            );
                                        }
                                        else if(data.id == selectedEmployee.id) {
                                            $rootScope.showErrorMessage(selectedEmployee.firstName + " is already a sales rep");
                                        }
                                    }
                                )
                            }
                        );
                    };

                    $scope.showDetails = function(salesRep) {
                        $state.go('app.crm.salesrep.customers', { salesRepId: salesRep.id });
                    };

                    (function() {
                        $scope.loadSalesReps();
                    })();
                }
            ]
        );
    }
);