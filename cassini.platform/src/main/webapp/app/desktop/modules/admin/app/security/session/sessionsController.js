define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/tableFilter/tableDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/sessionService'],
    function(module){

        module.controller('SessionsController',SessionsController);

        function SessionsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                 SessionService) {
            var vm = this;

            vm.sortColumn = sortColumn;
            vm.loadSessions = loadSessions;
            vm.applyFilters = applyFilters;
            vm.pageChanged = pageChanged;
            vm.resetFilters = resetFilters;

            vm.dateRangeOptions = {
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

            vm.loading = true;
            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "loginTime",
                    field: "loginTime",
                    order: "desc"
                }
            };

            vm.pagedResults = {
                content: [],
                last: false,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: false,
                numberOfElements: 0
            };

            vm.sessions = vm.pagedResults;
            vm.pageChanged = pageChanged;


            vm.emptyFilters = {
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

            vm.filters = angular.copy(vm.emptyFilters);

            $scope.$watch('filters.loginTime', function(date){
                applyFilters();
            });

            $scope.$watch('filters.logoutTime', function(date){
                applyFilters();
            });

            function sortColumn(col) {
                if(vm.pageable.sort.label == col) {
                    if(vm.pageable.sort.order == 'asc') {
                        vm.pageable.sort.order = 'desc';
                    }
                    else {
                        vm.pageable.sort.order = 'asc';
                    }
                }
                else {
                    vm.pageable.sort.label = col
                    vm.pageable.sort.order = 'asc';
                }

                if(col == "login") {
                    vm.pageable.sort.field = "login.loginName";
                }
                else if(col == "user") {
                    vm.pageable.sort.field = "login.person.firstName";
                }
                else if(col == "loginTime") {
                    vm.pageable.sort.field = "loginTime";
                }
                else if(col == "logoutTime") {
                    vm.pageable.sort.field = "logoutTime";
                }

                //$scope.pageable.page = 1;
                vm.loadSessions();
            };

            function pageChanged() {
                vm.loading = true;
                vm.sessions.content = [];
                vm.loadSessions();
            };

            function resetFilters() {
                vm.filters = angular.copy(vm.emptyFilters);
                vm.pageable.page = 1;
                vm.loadSessions();
            };

            function applyFilters() {
                vm.pageable.page = 1;
                vm.loadSessions();
            };

            function loadSessions() {
                vm.loading = true;

                if(vm.filters.loginTime != null &&
                    vm.filters.loginTime.startDate != null) {
                    var d = vm.filters.loginTime.startDate;
                    vm.filters.loginTime.startDate = moment(d).format('DD/MM/YYYY');
                }
                if(vm.filters.loginTime != null &&
                    vm.filters.loginTime.endDate != null) {
                    var d = vm.filters.loginTime.endDate;
                    vm.filters.loginTime.endDate = moment(d).format('DD/MM/YYYY');
                }

                if(vm.filters.logoutTime != null &&
                    vm.filters.logoutTime.startDate != null) {
                    var d = vm.filters.logoutTime.startDate;
                    vm.filters.logoutTime.startDate = moment(d).format('DD/MM/YYYY');
                }
                if(vm.filters.logoutTime != null &&
                    vm.filters.logoutTime.endDate != null) {
                    var d = vm.filters.logoutTime.endDate;
                    vm.filters.logoutTime.endDate = moment(d).format('DD/MM/YYYY');
                }

                SessionService.getSessions(vm.filters, vm.pageable).then (
                    function(data) {
                        vm.sessions = data;
                        vm.loading = false;
                    },

                    function(error) {
                        console.error(error);
                    }
                );
            };

            (function() {
                vm.loadSessions();
            })();
        }
    }
)