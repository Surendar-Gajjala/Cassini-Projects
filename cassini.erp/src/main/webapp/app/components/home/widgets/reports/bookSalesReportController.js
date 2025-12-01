define(
    [
        'app/app.modules',
        'app/components/reporting/reportFactory'
    ],
    function ($app) {
        $app.controller('BookSalesReportController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$filter',
                'reportFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $filter,
                        reportFactory) {

                    var reportInProgress = false;
                    var orderBy = $filter('orderBy');
                    $scope.reportData = [];

                    $scope.spinner = {
                        active: true
                    };

                    $scope.aggregate = {
                        product: 0,
                        sample: 0,
                        percent: 0
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
                        loadReport();
                    });

                    function loadReport() {
                        if(!reportInProgress) {
                            var report = reportFactory.getReportById('reports.crm.book-sales-report');
                            if (report != null) {
                                var dr = getDateRange();

                                reportInProgress = true;
                                $scope.reportData = [];

                                $scope.aggregate.product = 0;
                                $scope.aggregate.sample = 0;
                                $scope.aggregate.percent = 0;

                                reportFactory.executeReport(report, dr).then(
                                    function (data) {
                                        if (data != "" && data.length > 0) {
                                            $scope.reportData = data;
                                            calculatePercent();
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
                            dr.startDate = moment().subtract(1, 'year').format('MM/DD/YYYY');
                            dr.endDate = moment().format('MM/DD/YYYY');
                        }

                        return dr;
                    }

                    function calculatePercent() {
                        angular.forEach($scope.reportData, function(row) {
                            $scope.aggregate.product += row.product;
                            $scope.aggregate.sample += row.sample;


                            if(row.product >= row.sample) {
                                row.percent = Math.round(row.sample/row.product*100);
                            }
                            else {
                                row.percent = Math.round(row.sample/row.product*100);
                            }
                        });

                        $scope.aggregate.percent = Math.round($scope.aggregate.sample/$scope.aggregate.product*100);
                    }


                    $scope.order = function(predicate) {
                        $scope.predicate = predicate;
                        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                        $scope.reportData = orderBy($scope.reportData, predicate, $scope.reverse);
                    };


                    (function() {
                        $timeout(function() {
                            loadReport();
                        }, 5000);
                    })();

                }
            ]
        );
    }
);