define(
    [
        'app/app.modules',
        'morris',
        'app/components/reporting/reportFactory'
    ],
    function (module) {
        module.controller('DailySalesReportController', DailySalesReportController);

        function DailySalesReportController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                reportFactory) {

            var reportInProgress = false;
            $scope.spinner = {
                active: true
            };

            $scope.reportData = [];

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
                loadReport();
            });

            function loadReport() {
                if(!reportInProgress) {
                    var report = reportFactory.getReportById('reports.crm.daily-sales-report');
                    if (report != null) {
                        reportInProgress = true;
                        $scope.reportData = [];
                        $scope.spinner.active = true;

                        var dr = getDateRange()

                        reportFactory.executeReport(report, dr).then(
                            function (data) {
                                if (data != "" && data.length > 0) {
                                    $scope.reportData = data;
                                    angular.forEach($scope.reportData, function (row) {
                                        row.product = Math.round(row.product);
                                        row.sample = Math.round(0.3 * row.sample);
                                    });
                                    drawChart();
                                }
                                $scope.spinner.active = false;
                                reportInProgress = false;
                            },
                            function (error) {
                                $scope.spinner.active = false;
                                reportInProgress = false;
                            }
                        )
                    }
                    else {
                        $scope.spinner.active = false;
                        reportInProgress = false;
                    }
                }
            }

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
                    dr.startDate = moment().subtract(1, 'month').format('MM/DD/YYYY');
                    dr.endDate = moment().format('MM/DD/YYYY');
                }

                return dr;
            }

            function drawChart() {
                Morris.Bar({
                    element: 'dailySalesReport',
                    data: $scope.reportData,
                    xkey: 'day',
                    ykeys: ['product', 'sample'],
                    labels: ['Product Sales', 'Sample Sales']
                });
            }

            (function () {
                $timeout(function() {
                    loadReport();
                }, 5000);
            })();
        }
    }
);