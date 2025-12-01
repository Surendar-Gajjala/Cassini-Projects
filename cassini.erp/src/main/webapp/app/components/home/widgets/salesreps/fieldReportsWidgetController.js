define(['app/app.modules',
        'app/components/crm/salesrep/salesRepFactory'
    ],
    function($app) {
        $app.controller('FieldReportsWidgetController',
            [
                '$scope', '$rootScope', '$state', '$sce', 'salesRepFactory',

                function($scope, $rootScope, $state, $sce, salesRepFactory) {
                    $scope.selectedReport = null;
                    $scope.showReportDetails = false;
                    $scope.loading = true;

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

                    $scope.reports = $scope.emptyPagedResults;

                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "timestamp",
                            field: "timestamp",
                            order: "desc"
                        }
                    };

                    $scope.emptyCriteria = {
                        salesRep: null,
                        customer: null,
                        timestamp: null,
                        notes: null
                    };

                    $scope.criteria = angular.copy($scope.emptyCriteria);

                    $scope.viewReport = function (report) {
                        $scope.selectedReport = report;
                        $scope.showReportDetails = true;
                    };

                    $scope.closeReport = function() {
                        $scope.showReportDetails = false;
                        $scope.selectedReport = null;
                    };

                    $scope.loadReports = function() {
                        salesRepFactory.getAllFieldReports($scope.criteria, $scope.pageable).then(
                            function(data) {
                                angular.forEach(data.content, function(report) {
                                    report.notes = $sce.trustAsHtml(report.notes);
                                });
                                $scope.reports = data;
                                $scope.loading = false;
                            }
                        )
                    };


                    (function() {
                        $scope.loadReports();
                    })();
                }
            ]
        );
    }
);