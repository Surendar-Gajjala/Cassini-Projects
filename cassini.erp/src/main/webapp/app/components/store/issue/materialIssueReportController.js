define(['app/app.modules',
        'app/components/prod/materialpo/materialsPOFactory',
        'app/components/prod/material/materialFactory',
        'app/shared/directives/tableDirectives',
        'app/shared/directives/commonDirectives',
        'app/components/store/inventory/material/materialInventoryHistoryController'
    ],
    function ($app) {
        $app.controller('MaterialIssueReportController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'materialFactory', 'materialsPOFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          materialFactory, materialsPOFactory) {

                    $scope.addInvMode = false;
                    $scope.issueInvMode = false;

                    $rootScope.iconClass = "fa fa-shopping-cart";
                    $rootScope.viewTitle = "Issue Report";

                    $scope.selectedMaterial = null;

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 0,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };
                    $scope.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            label: "timestamp",
                            field: "timestamp",
                            order: "desc"
                        }
                    };

                    $scope.historyRow = {
                        showHistory: false
                    };

                    $scope.materials = $scope.emptyPagedResults;

                    $scope.emptyFilters = {
                        timestamp: {
                            startDate: null,
                            endDate: null
                        },
                        sku: null,
                        name: null,
                        issuedQty: null,
                        consumeQty: null,
                        remainingQty: null
                    };

                    $scope.dateRangeOptions = {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                            'Year to Date': [
                                moment().startOf('year').startOf('month').startOf('hour').startOf('minute').startOf('second'),
                                moment()
                            ],
                            'Last Year': [
                                moment().subtract(1, 'year').startOf('year'),
                                moment().subtract(1, 'year').endOf('year')
                            ]
                        }
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);

                    var today = new Date();
                    var dd = today.getDate();
                    var mm = today.getMonth() + 1; //January is 0!

                    var yyyy = today.getFullYear();
                    if (dd < 10) {
                        dd = '0' + dd;
                    }
                    if (mm < 10) {
                        mm = '0' + mm;
                    }
                    $scope.date = dd + '/' + mm + '/' + yyyy;

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        $scope.materials.content = [];
                        $scope.loadMaterialIssueReport();
                    };


                    $scope.acceptConsumeChanges = function (material) {
                        if ($scope.updateConsumeQty(material)) {
                            material.addConsume = false;
                        }
                    };

                    $scope.addConsumeQty = function (material) {
                        material.addConsume = true;
                        material.newConsumeQty = 0;
                    };

                    $scope.hideAddConsume = function (material) {
                        material.addConsume = false;
                        material.newConsumeQty = 0;
                    };

                    $scope.updateConsumeQty = function (material) {
                        if (material.consumeQty == undefined ||
                            isNaN(material.consumeQty) ||
                            material.consumeQty < 0) {
                            $rootScope.showErrorMessage("Quantity has to be positive whole number");

                            return false;
                        }
                        else {
                            material.remainingQty = material.remainingQty - material.newConsumeQty;
                            material.consumeQty = material.consumeQty + material.newConsumeQty;
                            materialFactory.updateMaterialDailyReport(material).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Updated successfully!");
                                }
                            );
                            return true;
                        }
                    };

                    $scope.validateConsumeQuantity = function (material) {
                        $scope.okButton = true;
                        if (material.remainingQty < material.newConsumeQty) {
                            $rootScope.showErrorMessage("Enter quantity is more than remaining quantity");
                            $scope.okButton = false;
                        } else {
                            $rootScope.closeNotification();
                            $scope.okButton = true;
                        }
                    };

                    $scope.loadMaterialIssueReport = function () {
                        if ($scope.filters.timestamp != null &&
                            $scope.filters.timestamp.startDate != null) {
                            var d = $scope.filters.timestamp.startDate;
                            $scope.filters.timestamp.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if ($scope.filters.timestamp != null &&
                            $scope.filters.timestamp.endDate != null) {
                            var d = $scope.filters.timestamp.endDate;
                            $scope.filters.timestamp.endDate = moment(d).format('DD/MM/YYYY');
                        }
                        $scope.loading = true;
                        $scope.okButton = true;
                        materialFactory.getMaterialIssueReport($scope.filters, $scope.pageable).then(
                            function (data) {
                                $scope.materials = data;
                                materialFactory.getMaterialNameReferences($scope.materials.content, 'material');
                                $scope.loading = false;
                                angular.forEach($scope.materials.content, function (material) {
                                    material.addConsume = false;
                                    material.newConsumeQty = 0;
                                })
                            }
                        )
                    };

                    $scope.resetFilters = function () {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        $scope.loadMaterialIssueReport();
                    };

                    $scope.applyFilters = function () {
                        $scope.pageable.page = 1;
                        $scope.loadMaterialIssueReport();
                    };

                    (function () {
                        $scope.loadMaterialIssueReport();
                    })();
                }
            ]
        )
        ;
    }
)
;