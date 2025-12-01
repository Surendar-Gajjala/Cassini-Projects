define(
    [
        'app/desktop/modules/home/home.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/requestService'
    ],
    function (module) {
        module.controller('RequestWidgetController', RequestWidgetController);

        function RequestWidgetController($scope, $rootScope, $timeout, $state, $cookies,
                                         CommonService, RequestService) {

            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;
            vm.loading = true;
            vm.listStatus = ["BDL_REQUEST", "BDL_MANAGER", "CAS", "REJECTED"];
            vm.showRequest = showRequest;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.resetPage = resetPage;
            vm.clear = false;
            vm.applyFilters = applyFilters;
            vm.clearFilter = clearFilter;
            vm.emptyFilters = {
                status: null,
                searchQuery: null,
                completed: null,
                itemRefs: [],
                finalStatus: false
            };

            vm.filters = angular.copy(vm.emptyFilters);

            function applyFilters() {
                vm.pageable.page = 0;
                loadRequests();
            }

            function clearFilter() {
                vm.pageable.page = 0;
                loadRequests();
                vm.clear = false;
            }

            vm.pageable = {
                page: 0,
                size: 6,
                sort: {
                    field: "createdDate",
                    order: "DESC"
                }
            };
            vm.pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.reqs = angular.copy(vm.pagedResults);


            function showRequest(req) {
                $state.go('app.requests.details', {requestId: req.id, mode: 'home'});
            }

            function resetPage() {
                vm.pageable.page = 0;
            }

            function loadRequests() {
                RequestService.getAllRequests(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.loading = false;
                        vm.reqs = data;
                    }
                )
            }

            function nextPage() {
                vm.pageable.page++;
                loadRequests();
            }

            function previousPage() {
                vm.pageable.page--;
                loadRequests();
            }

            (function () {
                loadRequests();
            })();
        }
    }
);