define(
    [
        'app/desktop/modules/dispatch/dispatch.module',
        'app/shared/services/core/dispatchService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('AllDispatchController', AllDispatchController);

        function AllDispatchController($scope, $rootScope, $window, $timeout, $application, $state, $stateParams, $cookies,
                                       DispatchService, CommonService) {

            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-sign-out";
            $rootScope.viewInfo.title = "Dispatch";

            var vm = this;

            vm.loading = true;

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
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

            vm.dispatchFilter = {
                notification: false,
                status: null,
                searchQuery: "",
                fromDate: null,
                toDate: null,
                month: null
            };

            vm.dispatches = angular.copy(pagedResults);

            vm.newDispatch = newDispatch;
            vm.showDispatchDetails = showDispatchDetails;
            vm.addFilter = addFilter;
            vm.getFilterResults = getFilterResults;
            vm.cancelFilter = cancelFilter;
            vm.errorMessage = null;
            vm.clearDateFilter = clearDateFilter;
            vm.freeTextSearch = freeTextSearch;
            vm.getMonthResults = getMonthResults;
            vm.filterMode = false;

            function clearDateFilter() {
                vm.errorMessage = null;
                vm.dispatchFilter.fromDate = null;
                vm.dispatchFilter.toDate = null;
                vm.dispatchFilter.month = null;
                vm.filterMode = false;
                loadDispatches();
            }

            function cancelFilter() {
                var modal = document.getElementById("add-filter");
                modal.style.display = "none";
            }

            function getFilterResults() {
                vm.errorMessage = null;
                if ((vm.dispatchFilter.fromDate != null && vm.dispatchFilter.fromDate != "" && vm.dispatchFilter.fromDate != undefined) &&
                    (vm.dispatchFilter.toDate != null && vm.dispatchFilter.toDate != "" && vm.dispatchFilter.toDate != undefined)) {
                    vm.filterMode = true;
                    vm.dispatchFilter.month = null;
                    loadDispatches();
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.errorMessage = "Please select From Date and To Date";
                }
            }

            function getMonthResults() {
                vm.errorMessage = null;
                if (vm.dispatchFilter.month != null && vm.dispatchFilter.month != "" && vm.dispatchFilter.month != undefined) {
                    vm.dispatchFilter.fromDate = null;
                    vm.dispatchFilter.toDate = null;
                    loadDispatches();
                    vm.filterMode = true;
                    var modal = document.getElementById("add-filter");
                    modal.style.display = "none";
                } else {
                    vm.filterMode = false;
                    vm.errorMessage = "Please select Month";
                }
            }

            function addFilter() {
                vm.errorMessage = null;
                vm.dispatchFilter.fromDate = null;
                vm.dispatchFilter.toDate = null;
                var modal = document.getElementById("add-filter");
                modal.style.display = "block";
            }

            function loadDispatches() {
                DispatchService.getAllDispatches(vm.pageable, vm.dispatchFilter).then(
                    function (data) {
                        vm.dispatches = data;
                        CommonService.getPersonReferences(vm.dispatches.content, 'createdBy');
                        vm.loading = false;
                    }
                )
            }

            function newDispatch() {
                var options = {
                    title: "New Dispatch",
                    template: 'app/desktop/modules/dispatch/new/newDispatchView.jsp',
                    controller: 'NewDispatchController as newDispatchVm',
                    resolve: 'app/desktop/modules/dispatch/new/newDispatchController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.dispatch.new'}
                    ],
                    callback: function (result) {
                        loadDispatches();
                    }
                };

                $rootScope.showSidePanel(options);

            }


            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    vm.dispatchFilter.searchQuery = freeText;
                    loadDispatches();
                } else {
                    resetPage();
                    loadDispatches();
                    $scope.freeTextQuery = null;
                    vm.dispatchFilter.searchQuery = null;
                }
            }

            function showDispatchDetails(dispatch) {
                var button = null;
                if (dispatch.status == "NEW" && $rootScope.hasPermission('permission.dispatch.edit')) {
                    button = {text: "Update", broadcast: 'app.dispatch.details'};
                } else {
                    button = {text: "Close", broadcast: 'app.dispatch.close'}
                }

                var options = {
                    title: dispatch.number + " Details",
                    template: 'app/desktop/modules/dispatch/details/dispatchDetailsView.jsp',
                    controller: 'DispatchDetailsController as dispatchDetailsVm',
                    resolve: 'app/desktop/modules/dispatch/details/dispatchDetailsController',
                    width: 700,
                    showMask: true,
                    data: {
                        dispatchDetails: dispatch
                    },
                    buttons: [
                        button
                    ],
                    callback: function (result) {
                        loadDispatches();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadDispatches();
                });
            })();
        }
    }
);