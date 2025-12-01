define(
    [
        'app/app.modules',
        'app/components/reporting/reportFactory'
    ],
    function ($app) {
        $app.controller('ProductVsSampleSalesController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', '$filter',
                'reportFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies, $filter,
                        reportFactory) {

                    var reportInProgress = false;

                    var orderBy = $filter('orderBy');
                    $scope.orders = [];

                    $scope.spinner = {
                        active: true
                    };

                    $scope.totals = {
                        product: 0,
                        sample: 0
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
                            $scope.spinner.active = true;

                            $scope.orders = [];
                            $scope.totals.product = 0;
                            $scope.totals.sample = 0;
                            $scope.totals.percent = 0;

                            var report = reportFactory.getReportById('reports.crm.product-vs-sample-sales-by-customer');
                            if(report != null) {
                                var dr = getDateRange();

                                reportInProgress = true;
                                reportFactory.executeReport(report, dr).then(
                                    function(data) {
                                        if(data != "" && data.length > 0) {
                                            $scope.orders = data;
                                            calculateDiscount();
                                        }
                                        $scope.spinner.active = false;
                                        reportInProgress = false;
                                    },
                                    function(error) {
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

                    function calculateDiscount() {
                        angular.forEach($scope.orders, function(order) {
                            order.sample = Math.round(0.3*order.sample);

                            $scope.totals.product += order.product;
                            $scope.totals.sample += order.sample;

                            if(order.product >= order.sample) {
                                order.percent = Math.round(order.sample/order.product*100);
                            }
                            else {
                                order.percent = Math.round(order.sample/order.product*100);
                            }

                            $scope.totals.percent = Math.round($scope.totals.sample/$scope.totals.product*100);
                        });
                    }

                    $scope.showCustomerDetails = function(id) {
                        $state.go('app.crm.customer', {customerId: id})
                    };

                    $scope.order = function(predicate) {
                        $scope.predicate = predicate;
                        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                        $scope.orders = orderBy($scope.orders, predicate, $scope.reverse);
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