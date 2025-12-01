define(['app/app.modules', 'morris', 'app/components/reporting/reportFactory'],
    function (app) {
        app.directive('pieChart',
            ['$rootScope', '$compile', '$timeout', 'reportFactory',
                function ($rootScope, $compile, $timeout, reportFactory) {
                    return {
                        restrict: 'E',
                        templateUrl: 'app/components/reporting/directives/pieChart.jsp',
                        scope: {
                            report: '='
                        },
                        link: function ($scope, element, attrs) {
                            $scope.chart = {
                                title: "",
                                data: []
                            };

                            $scope.colors = [
                                '#D9534F',
                                '#1CAF9A',
                                '#F0AD4E',
                                '#428BCA',
                                '#5BC0DE'
                            ];

                            $scope.spinner = {
                                active: true
                            };

                            $(window).resize(function() {

                            });


                            $scope.loadReport = function() {
                                if($scope.report != null) {
                                    reportFactory.executeReport($scope.report).then(
                                        function(data) {
                                            if(data != "" && data.length > 0) {
                                                $scope.chart.title = $scope.report.name;
                                                $scope.chart.data = data;

                                                fixChartData();
                                                $scope.drawChart();
                                            }
                                            else {
                                                $scope.spinner.active = false;
                                            }
                                        }
                                    )
                                }
                            };

                            $scope.$watch('report', function(newValue, oldValue) {
                                $scope.loadReport();
                            });

                            function fixChartData() {
                                for(var i=0; i<$scope.chart.data.length; i++) {
                                    var series = $scope.chart.data[i];
                                    series.label = series.key + " (" + series.number + ")";
                                    series.data = series.number;
                                    series.color = $scope.colors[i];
                                }
                            }

                            $scope.drawChart = function() {
                                $timeout(function() {
                                    $(element).find('.morris-hover').remove();
                                    $.plot('.report-view', $scope.chart.data, {
                                        series: {
                                            pie: {
                                                show: true,
                                                radius: 1,
                                                label: {
                                                    show: true,
                                                    radius: 2/3,
                                                    formatter: labelFormatter,
                                                    threshold: 0.1
                                                }
                                            }
                                        },
                                        grid: {
                                            hoverable: true,
                                            clickable: true
                                        }
                                    });

                                    function labelFormatter(label, series) {
                                        return "<div style='font-size:8pt; text-align:center; padding:2px; color:white;'>" + label + "<br/>" + Math.round(series.percent) + "%</div>";
                                    }

                                    $scope.spinner.active = false;
                                }, 200);
                            };

                            (function() {
                                $scope.loadReport();
                            })();
                        }
                    };
                }
            ]
        );
    }
);
