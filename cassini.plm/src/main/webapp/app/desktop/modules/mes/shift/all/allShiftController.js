define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/shiftService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllShiftController', AllShiftController);

        function AllShiftController($scope, $rootScope, $translate, $timeout, $state, $window,
                                    DialogService, $application, $stateParams, $cookies, $sce, ShiftService, CommonService, RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newShift = newShift;
            vm.shifts = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.deleteShift = deleteShift;
            vm.searchText = null;
            vm.filterSearch = null;


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

            vm.filters = {
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.shifts = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newShiftsHeading = parsed.html($translate.instant("NEW_SHIFT")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_SHIFT")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var plantDeletedSuccessMessage = parsed.html($translate.instant("SHIFT_DELETED_MESSAGE")).html();


            function newShift() {
                var options = {
                    title: newShiftsHeading,
                    template: 'app/desktop/modules/mes/shift/new/newShiftView.jsp',
                    controller: 'NewShiftController as newShiftVm',
                    resolve: 'app/desktop/modules/mes/shift/new/newShiftController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.shift.new'}
                    ],
                    callback: function (shift) {
                        $timeout(function () {
                            loadShifts();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.shifts.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadShifts();
                }
            }

            function previousPage() {
                if (vm.shifts.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadShifts();
                }
            }

            $scope.operationFilePopover = {
                templateUrl: 'app/desktop/modules/mes/shift/all/shiftFilePopoverTemplate.jsp'
            };

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadShifts();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadShifts();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.shifts = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadShifts();
            }

            function loadShifts() {
                vm.loading = true;
                ShiftService.getAllShifts(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.shifts = data;
                        /*angular.forEach(vm.shifts.content, function (shift) {
                         shift.modifiedDatede = null;
                         if (shift.modifiedDate != null) {
                         shift.modifiedDatede = moment(shift.modifiedDate, "DD/MM/YYYY, HH:mm:ss");
                         }
                         });*/
                        CommonService.getPersonReferences(vm.shifts.content, 'modifiedBy');
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function deleteShift(shift) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteDialogMessage + " [ " + shift.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ShiftService.deleteShift(shift.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(plantDeletedSuccessMessage);
                                loadShifts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showShift = showShift;
            function showShift(shift) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = shift.id;
                vm.recentlyVisited.objectType = shift.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.shift.details', {shiftId: shift.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.mes.masterData.shift.details', {shiftId: shift.id, tab: 'details.basic'});
                    }
                )
            }

            (function () {
                loadShifts()
            })();

        }
    }
);