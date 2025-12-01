define(['app/app.modules',
        'app/shared/directives/commonDirectives',
        'app/components/reporting/reportFactory',
        'app/components/reporting/directives/verticalBarChart',
        'app/components/reporting/directives/pieChart',
        'app/components/home/widgets/reports/bookSalesReportController.js',
        'app/components/home/widgets/reports/dailySalesReportController.js',
        'app/components/home/widgets/reports/productVsSampleSalesController.js'
    ],
    function ($app) {
        $app.controller('ReportsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'reportFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          reportFactory) {
                    $rootScope.iconClass = "fa fa-bar-chart-o";
                    $rootScope.viewTitle = "Reports";

                    $scope.reports = [];
                    $scope.selectedModule = null;
                    $scope.selectedReport = null;

                    function loadReports() {
                        reportFactory.getReports().then(
                            function(data) {
                                $scope.reports = data;
                                if(data.length > 0) {
                                    addBuiltinReports();

                                    angular.forEach($scope.reports, function(module) {
                                        angular.forEach(module.reports, function(report) {
                                            report.selected = false;
                                        })
                                    });
                                    $scope.selectedModule = data[0];
                                    $scope.selectedReport = $scope.selectedModule.reports[0];
                                    $scope.selectedReport.selected = true;
                                }
                            }
                        );
                    }

                    $scope.onSelectModule = function(item, model) {
                        if($scope.selectedReport != null) {
                            $scope.selectedReport.selected = false;
                        }

                        if($scope.selectedModule != model) {
                            $scope.selectedModule = model;
                            $scope.selectedReport = $scope.selectedModule.reports[0];
                            $scope.selectedReport.selected = true;
                        }
                    };

                    $scope.selectReport = function(report) {
                        if($scope.selectedReport != null) {
                            $scope.selectedReport.selected = false;
                        }

                        report.selected = true;
                        $scope.selectedReport = report;
                    };


                    function addBuiltinReports() {
                        var reportModule = null;
                        angular.forEach($scope.reports, function(rm) {
                            if(rm.id == 'reports.crm') {
                                reportModule = rm;
                            }
                        });

                        if(reportModule != null) {
                            reportModule.reports.unshift({
                                id: 'builtin.reports.book-sales',
                                name: "Book Sales Report",
                                description: "Book sales report",
                                viewType: 'custom',
                                template: 'app/components/home/widgets/reports/bookSalesReportView.jsp',
                                controller: 'BookSalesReportController'
                            });
                            reportModule.reports.unshift({
                                id: 'builtin.reports.product-vs-sample',
                                name: "Product vs Sample Sales",
                                description: "Product vs samples sales",
                                viewType: 'custom',
                                template: 'app/components/home/widgets/reports/productVsSampleSalesView.jsp',
                                controller: 'ProductVsSampleSalesController'
                            });
                            reportModule.reports.unshift({
                                id: 'builtin.reports.daily-sales',
                                name: "Daily Sales Report",
                                description: "Daily sales report",
                                viewType: 'custom',
                                template: 'app/components/home/widgets/reports/dailySalesReportView.jsp',
                                controller: 'DailySalesReportController'
                            });
                        }
                    }


                    (function() {
                        loadReports();
                    })();
                }
            ]
        );
    }
);