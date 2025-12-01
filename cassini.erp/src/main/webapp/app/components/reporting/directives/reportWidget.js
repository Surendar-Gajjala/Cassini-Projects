define(['app/app.modules', 'morris', 'app/components/reporting/reportFactory'],
    function (app) {
        app.directive('reportWidget',
            ['$rootScope', '$compile', '$timeout', 'reportFactory',
                function ($rootScope, $compile, $timeout, reportFactory) {
                    return {
                        restrict: 'E',
                        templateUrl: 'app/components/reporting/directives/reportWidget.jsp',
                        scope: {
                            report: '@',
                            barColors: '=',
                            labels: '='
                        },
                        link: function ($scope, element, attrs) {
                            $scope.chart = {
                                title: "Report Title",
                                data: []
                            };

                            $scope.morris = null;

                            $scope.spinner = {
                                active: true
                            };

                            $("#mainPanel").resize(function() {
                                if($scope.morris != null && $scope.chart.data.length > 0) {
                                    $scope.morris.redraw();
                                }
                            });

                            $scope.xkey = 'key';
                            $scope.ykeys = ['number'];

                            $scope.loadReport = function() {
                                $scope.spinner.active = true;
                                var report = reportFactory.getReportById($scope.report);
                                if(report != null) {
                                    reportFactory.executeReport(report).then(
                                        function(data) {
                                            if(data != "" && data.length > 0) {
                                                $scope.chart.title = report.name;
                                                $scope.chart.data = data;
                                                console.log("Report widget {0} loaded".format($scope.report));
                                                $scope.drawChart();
                                            }
                                            else {
                                                $scope.spinner.active = false;
                                            }
                                        }
                                    )
                                }
                            };

                            $scope.drawChart = function() {
                                $timeout(function() {
                                    var Morris = window.Morris;
                                    $scope.morris = Morris.Bar({
                                        element: $(element).find('.widget-chart'),
                                        data: $scope.chart.data,
                                        xkey: $scope.xkey,
                                        ykeys: $scope.ykeys,
                                        labels: $scope.labels,
                                        barSize: 100,
                                        hideHover: true,
                                        barColors: $scope.barColors
                                    });

                                    $(element).find('svg').mouseout(function() {
                                        $(element).find('.morris-hover').hide();
                                    });

                                    $scope.spinner.active = false;
                                }, 1000);
                            };

                            if(reportFactory.areReportsLoaded() == true) {
                                $scope.loadReport();
                            }

                            $rootScope.$on("reportsLoaded", function() {
                                $scope.loadReport();
                            });
                        }
                    };
                }
            ]
        );
    }
);
