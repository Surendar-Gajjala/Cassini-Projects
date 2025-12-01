define(['app/desktop/modules/shift/shift.module',
        'app/desktop/modules/shift/employee/selectEmpDialogueController',
        'app/desktop/modules/shift/details/tabs/morningShift/morningShiftController',
        'app/desktop/modules/shift/details/tabs/eveningShift/eveningShiftController',
        'app/desktop/modules/shift/details/tabs/nightShift/nightShiftController',
        'app/desktop/modules/shift/new/newShiftController',
        'app/shared/services/shiftService'

    ],
    function (module) {
        module.controller('AllShiftsController', AllShiftsController);

        function AllShiftsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModal, ShiftService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = "Shifts";

            vm.shiftDetailsTabActivated = shiftDetailsTabActivated;
            vm.tabLoaded = tabLoaded;
            vm.createShift = createShift;
            vm.loading = true;

            function loadShifts() {
                ShiftService.getAllShifts().then(
                    function (data) {
                        vm.shifts = data;
                    });
                vm.loading = false;
            }

            function createShift() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/shift/new/newShiftView.jsp',
                    controller: 'NewShiftController as newShiftVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        ShiftService.createShift(result).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Shift Created Successfully ");
                                loadShifts();
                            }
                        )

                    }
                );
            }

            vm.tabs = {
                morningShift: {
                    id: 'details.morningShift',
                    heading: 'Morning Shift',
                    template: 'app/desktop/modules/shift/details/tabs/morningShift/morningShiftView.jsp',
                    active: true
                },
                eveningShift: {
                    id: 'details.eveningShift',
                    heading: 'Evening Shift',
                    template: 'app/desktop/modules/shift/details/tabs/eveningShift/eveningShiftView.jsp',
                    active: false
                },
                nightShift: {
                    id: 'details.nightShift',
                    heading: 'Night Shift',
                    template: 'app/desktop/modules/shift/details/tabs/nightShift/nightShiftView.jsp',
                    active: false
                }

            };

            function shiftDetailsTabActivated(tabId) {
                $scope.$broadcast('app.shift.tabactivated', {tabId: tabId})

                var tab = getTabById(tabId);
                if (tab != null) {
                    activateTab(tab);
                }

            }

            function tabLoaded(tabId) {
                $scope.$broadcast('app.shift.tabloaded', {tabId: tabId});
            }


            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            (function () {
                if ($application.homeLoaded == true) {
                }
            })();
        }
    }
);