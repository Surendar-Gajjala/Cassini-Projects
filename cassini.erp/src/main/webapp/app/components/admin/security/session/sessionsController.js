define(['app/app.modules',
        'app/components/admin/security/session/sessionFactory',
        'app/shared/directives/tableDirectives'
    ],
    function (app) {
        app.controller('SessionsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', 'sessionFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, sessionFactory) {

                    $scope.setActiveFlag(2);

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
                        },
                        locale: {
                            format: 'DD/MM/YYYY'
                        }
                    };

                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 10,
                        sort: {
                            label: "loginTime",
                            field: "loginTime",
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


                    $scope.sessions = $scope.pagedResults;

                    $scope.emptyFilters = {
                        id: null,
                        login: null,
                        user: null,
                        ipAddress:null,
                        loginTime:  {
                            startDate: null,
                            endDate: null
                        },
                        logoutTime:  {
                            startDate: null,
                            endDate: null
                        }
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.$watch('filters.loginTime', function(date){
                        $scope.applyFilters();
                    });

                    $scope.$watch('filters.logoutTime', function(date){
                        $scope.applyFilters();
                    });

                    $scope.sortColumn =  function (col) {
                        if($scope.pageable.sort.label == col) {
                            if($scope.pageable.sort.order == 'asc') {
                                $scope.pageable.sort.order = 'desc';
                            }
                            else {
                                $scope.pageable.sort.order = 'asc';
                            }
                        }
                        else {
                            $scope.pageable.sort.label = col
                            $scope.pageable.sort.order = 'asc';
                        }

                        if(col == "login") {
                            $scope.pageable.sort.field = "login.loginName";
                        }
                        else if(col == "user") {
                            $scope.pageable.sort.field = "login.person.firstName";
                        }
                        else if(col == "loginTime") {
                            $scope.pageable.sort.field = "loginTime";
                        }
                        else if(col == "logoutTime") {
                            $scope.pageable.sort.field = "logoutTime";
                        }

                        //$scope.pageable.page = 1;
                        $scope.loadSessions();
                    };

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        $scope.sessions.content = [];
                        $scope.loadSessions();
                    };

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        $scope.loadSessions();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        $scope.loadSessions();
                    };

                    $scope.loadSessions = function() {
                        $scope.loading = true;

                        if($scope.filters.loginTime != null &&
                            $scope.filters.loginTime.startDate != null) {
                            var d = $scope.filters.loginTime.startDate;
                            $scope.filters.loginTime.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if($scope.filters.loginTime != null &&
                            $scope.filters.loginTime.endDate != null) {
                            var d = $scope.filters.loginTime.endDate;
                            $scope.filters.loginTime.endDate = moment(d).format('DD/MM/YYYY');
                        }

                        if($scope.filters.logoutTime != null &&
                            $scope.filters.logoutTime.startDate != null) {
                            var d = $scope.filters.logoutTime.startDate;
                            $scope.filters.logoutTime.startDate = moment(d).format('DD/MM/YYYY');
                        }
                        if($scope.filters.logoutTime != null &&
                            $scope.filters.logoutTime.endDate != null) {
                            var d = $scope.filters.logoutTime.endDate;
                            $scope.filters.logoutTime.endDate = moment(d).format('DD/MM/YYYY');
                        }

                        sessionFactory.getSessions($scope.filters, $scope.pageable).then (
                            function(data) {
                                $scope.sessions = data;
                                $scope.loading = false;
                            },

                            function(error) {
                                console.error(error);
                            }
                        );
                    };

                    (function() {
                        $scope.loadSessions();
                    })();
                }
            ]);
    });