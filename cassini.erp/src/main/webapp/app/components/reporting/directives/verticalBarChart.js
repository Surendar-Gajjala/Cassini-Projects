define(['app/app.modules', 'morris', 'app/components/reporting/reportFactory'],
    function (app) {
        app.directive('verticalBarChart',
            ['$rootScope', '$compile', '$timeout', 'reportFactory',
                function ($rootScope, $compile, $timeout, reportFactory) {
                    return {
                        restrict: 'E',
                        templateUrl: 'app/components/reporting/directives/verticalBarChart.jsp',
                        scope: {
                            report: '='
                        },
                        link: function ($scope, element, attrs) {
                            $scope.chart = {
                                title: "",
                                data: []
                            };
                            $scope.loading = true;

                            $scope.morris = null;

                            $scope.spinner = {
                                active: true
                            };

                            $scope.filters = {
                                orderedDate: {
                                    startDate: null,
                                    endDate: null
                                }
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

                            $scope.$watch('filters.orderedDate', function (date) {
                                $scope.loadReport();
                            });

                            $(window).resize(function() {
                                if($scope.morris != null && $scope.chart.data.length > 0) {
                                    $scope.morris.redraw();
                                }
                            });

                            $scope.xkey = 'key';
                            $scope.ykeys = ['number'];

                            $scope.loadReport = function() {
                                if($scope.report != null) {
                                    $scope.loading = true;
                                    var dr = getDateRange();

                                    $scope.chart.data = [];

                                    reportFactory.executeReport($scope.report, dr).then(
                                        function(data) {
                                            $scope.chart.data = [];
                                            $scope.chart.title = $scope.report.name;
                                            if(data != "" && data.length > 0) {
                                                $scope.chart.data = data;
                                                $scope.drawChart();
                                            }
                                            else {
                                                $scope.spinner.active = false;
                                            }
                                            $scope.loading = false;
                                        },
                                        function(error) {
                                            $scope.loading = false;
                                        }
                                    )
                                }
                            };

                            function getDateRange() {
                                var dr = null;
                                if($scope.filters.orderedDate.startDate != null &&
                                    $scope.filters.orderedDate.startDate != null) {
                                    dr = {
                                        startDate: null,
                                        endDate: null
                                    };

                                    var d = $scope.filters.orderedDate.startDate;
                                    dr.startDate = moment(d).format('MM/DD/YYYY');

                                    d = $scope.filters.orderedDate.endDate;
                                    dr.endDate = moment(d).format('MM/DD/YYYY');
                                }
                                else {
                                    dr = {
                                        startDate: null,
                                        endDate: null
                                    };
                                    dr.startDate = moment().subtract(1, 'year').format('MM/DD/YYYY');
                                    dr.endDate = moment().format('MM/DD/YYYY');
                                }

                                return dr;
                            }

                            $scope.$watch('report', function(newValue, oldValue) {
                                $scope.loadReport();
                            });

                            $scope.drawChart = function() {
                                $timeout(function() {
                                    $(element).find('.morris-hover').remove();
                                    $scope.morris = Morris.Bar({
                                        element: $(element).find('.report-view'),
                                        data: $scope.chart.data,
                                        xkey: $scope.xkey,
                                        ykeys: $scope.ykeys,
                                        labels: $scope.report.properties.labels,
                                        barSize: 100,
                                        hideHover: true,
                                        barColors: $scope.report.properties.barColors
                                    });

                                    $(element).find('svg').mouseout(function() {
                                        $(element).find('.morris-hover').hide();
                                    });

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
