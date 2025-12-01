define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/salesrep/salesRepFactory'
    ],
    function($app) {
        $app.controller('CustomerReportsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies', '$sce',
                'customerFactory', 'salesRepFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $sce,
                         customerFactory, salesRepFactory) {

                    $scope.$parent.$parent.$parent.reportsScope = $scope;

                    $scope.loading = true;
                    $scope.fieldReports = [];
                    $scope.showNew = false;
                    $scope.newFieldReport = {
                        showNotes: true,
                        salesRep: null,
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


                    $scope.loadFieldReports = function() {
                        customerFactory.getCustomerReports($scope.customer.id, $scope.pageable).then (
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
                            salesRep: $scope.customer.salesRep,
                            customer: $scope.customer,
                            notes: null
                        };
                        $scope.showNew = true;
                    };

                    $scope.cancel = function() {
                        $scope.showNew = false;
                    };

                    $scope.createNew = function() {
                        if(validateFieldReport() == true) {
                            salesRepFactory.createSalesRepFieldReport($scope.newFieldReport).then (
                                function(data) {
                                    var fieldReport = data;
                                    fieldReport.notes = $sce.trustAsHtml(fieldReport.notes);
                                    fieldReport.showNotes = true;
                                    $scope.fieldReports.unshift(fieldReport);
                                    $scope.showNew = false;
                                },
                                function(error) {
                                    console.error(error);
                                }
                            )
                        }
                    };

                    function validateFieldReport() {
                        var valid = true;
                        if($scope.newFieldReport.notes == null ||
                            $scope.newFieldReport.notes == "") {
                            $rootScope.showErrorMessage("Field report notes cannot be empty");
                            valid = false;
                        }

                        return valid;
                    }

                    $scope.$parent.$on("customerLoaded", function() {
                        $scope.loadFieldReports();
                        $scope.newFieldReport.customer = $scope.customer;
                        $scope.newFieldReport.salesRep = $scope.customer.salesRep;
                    });

                    (function() {
                        $rootScope.$on('customerLoaded', function() {
                            $scope.criteria = {
                                customer: $scope.customer.id
                            };
                            $scope.loadFieldReports();
                        });
                    })();

                }
            ]
        );
    }
);