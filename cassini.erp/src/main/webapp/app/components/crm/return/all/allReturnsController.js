define(['app/app.modules',
        'app/components/crm/return/returnFactory',
        'app/shared/directives/tableDirectives'
    ],
    function($app) {
        $app.controller('AllReturnsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'returnFactory',

                function($scope, $rootScope, $timeout, $interval, $state, $cookies, returnFactory) {

                    $rootScope.iconClass = "fa flaticon-refresh2";
                    $rootScope.viewTitle = "Returns";

                    $scope.dateRangeOptions = {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf('month')],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                            'Yea to Date': [
                                moment().startOf('year').startOf('month').startOf('hour').startOf('minute').startOf('second'),
                                moment()
                            ],
                            'Last Year': [
                                moment().subtract(1, 'year').startOf('year'),
                                moment().subtract(1, 'year').endOf('year')
                            ]
                        }
                    };

                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {
                            label: "returnDate",
                            field: "returnDate",
                            order: "desc"
                        }
                    };
                    $scope.pagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.returns = $scope.pagedResults;

                    $scope.emptyFilters = {
                        id: null,
                        returnDate: {
                            startDate: null,
                            endDate: null
                        },
                        customer: null,
                        region: null,
                        district: null,
                        salesRep: null,
                        reason: null
                    };
                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.$on('$viewContentLoaded', function(){
                        $rootScope.setToolbarTemplate('returns-view-tb');
                    });

                    $rootScope.newReturn = function() {
                        $state.go('app.crm.returns.new');
                    };

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.returns.content = [];
                        loadReturns();
                    };

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        loadReturns();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        loadReturns();
                    };

                    $scope.sortColumn =  function (label, field) {
                        if($scope.pageable.sort.label == label) {
                            if($scope.pageable.sort.order == 'asc') {
                                $scope.pageable.sort.order = 'desc';
                            }
                            else {
                                $scope.pageable.sort.order = 'asc';
                            }
                        }
                        else {
                            $scope.pageable.sort.label = label;
                            $scope.pageable.sort.order = 'asc';
                        }

                        $scope.pageable.sort.field = field;

                        //$scope.pageable.page = 1;
                        loadReturns();
                    };

                    $scope.$watch('filters.returnDate', function(date){
                        $scope.applyFilters();
                    });

                    function loadReturns() {
                        if($scope.filters.returnDate != null &&
                            $scope.filters.returnDate.startDate != null) {
                            var d = $scope.filters.returnDate.startDate;
                            $scope.filters.returnDate.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if($scope.filters.returnDate != null &&
                            $scope.filters.returnDate.endDate != null) {
                            var d = $scope.filters.returnDate.endDate;
                            $scope.filters.returnDate.endDate = moment(d).format('DD/MM/YYYY');
                        }
                        returnFactory.getReturns($scope.filters, $scope.pageable).then(
                            function(data) {
                                $scope.returns = data;
                                $scope.loading = false;
                            }
                        )
                    }

                    $scope.openReturnDetails = function(ret) {
                        $state.go('app.crm.returns.details', {returnId: ret.id});
                    };

                    $scope.hasStringFilter = function(value) {
                        if(value != null && value != '') {
                            return "hasFilter";
                        }
                    };

                    (function() {
                        loadReturns();
                    })();
                }
            ]
        );
    }
);