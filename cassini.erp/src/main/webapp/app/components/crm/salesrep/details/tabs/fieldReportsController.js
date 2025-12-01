define(['app/app.modules',
        'app/shared/directives/ckeditor',
        'app/components/crm/salesrep/salesRepFactory',
        'app/components/crm/customer/selectCustomerDialogController'
    ],
    function (app) {
        app.controller('SalesRepFieldReportsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$sce',
                '$modal', 'salesRepFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $sce, $modal,
                          salesRepFactory) {
                    $scope.setActiveFlag(3);

                    $scope.loading = true;
                    $scope.fieldReports = [];
                    $scope.selectText = "Select";
                    $scope.showNew = false;
                    $scope.newFieldReport = {
                        showNotes: true,
                        salesRep: $scope.salesRep,
                        customer: null,
                        notes: null
                    };

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

                    $scope.pagedFieldReports = $scope.emptyPagedResults;

                    $scope.pageable = {
                        page: 1,
                        size: 5,
                        sort: {
                            label: "timestamp",
                            field: "timestamp",
                            order: "desc"
                        }
                    };

                    $rootScope.newCustomer = function() {
                        $rootScope.lastSalesRep = $scope.salesRep;
                        $state.go('app.crm.newcustomer', {source: 'salesrep.reports', salesRep: $scope.salesRep.id});
                    };

                    $scope.loadFieldReports = function() {
                        salesRepFactory.getSalesRepFieldReports($scope.salesRep.id, $scope.pageable).then (
                            function(data) {
                                angular.forEach(data.content, function(report) {
                                    report.notes = $sce.trustAsHtml(report.notes);
                                    report.showNotes = true;
                                    $scope.fieldReports.push(report);
                                });
                                $scope.pagedFieldReports = data;
                                $scope.loading = false;
                            }
                    )
                };

                    $scope.toggleNotes = function(report) {
                        report.showNotes = !report.showNotes;
                    };

                    $scope.loadMore = function() {
                        if($scope.pagedFieldReports.last == false) {
                            $scope.pageable.page = $scope.pageable.page + 1;
                            $scope.loadFieldReports();
                        }
                    };

                    $scope.addNew = function() {
                        $scope.newFieldReport = {
                            salesRep: $scope.salesRep,
                            customer: null,
                            notes: null
                        };
                        $scope.showNew = true;
                    };

                    $scope.cancel = function() {
                        $scope.showNew = false;
                    };

                    $scope.createNew = function() {
                        $scope.newFieldReport.salesRep = $scope.salesRep;
                        if(validateFieldReport() == true) {
                            salesRepFactory.createSalesRepFieldReport($scope.newFieldReport).then (
                                function(data) {
                                    var fieldReport = data;
                                    fieldReport.notes = $sce.trustAsHtml(fieldReport.notes);
                                    fieldReport.showNotes = true;
                                    //$scope.fieldReports.unshift(fieldReport);
                                    $scope.showNew = false;
                                    $scope.loadFieldReports();
                                },
                                function(error) {
                                    console.error(error);
                                }
                            )
                        }
                    };

                    function validateFieldReport() {
                        var valid = true;

                        if($scope.newFieldReport.customer == null) {
                            $rootScope.showErrorMessage("Field report must have a customer");
                            valid = false;
                        }
                        else if($scope.newFieldReport.notes == null ||
                            $scope.newFieldReport.notes == "") {
                            $rootScope.showErrorMessage("Field report notes cannot be empty");
                            valid = false;
                        }

                        return valid;
                    }

                    $scope.showCustomerSelectionDialog = function () {
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/crm/customer/selectCustomerDialog.jsp',
                            controller: 'SelectCustomerDialogController',
                            size: 'lg',
                            resolve: {
                                salesRepFilter: function () {
                                    return $scope.salesRep;
                                }
                            }
                        });

                        modalInstance.result.then(
                            function (selectedCustomer) {
                                $scope.newFieldReport.customer = selectedCustomer;
                                $scope.selectText = "Change";
                            },
                            function () {

                            }
                        );
                    };
                    (function() {
                        $scope.loadFieldReports();
                        var mode = $stateParams.mode;
                        if(mode != null && mode != undefined && mode == 'create') {
                            $scope.addNew();
                            $scope.newFieldReport.customer = $rootScope.lastNewCustomer;
                        }
                    })();
                }
            ]
        );
    }
);