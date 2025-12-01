define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/proc/machines/details/tabs/basic/machineBasicDetailsController',
        'app/desktop/modules/proc/machines/details/tabs/attributes/machineAttributesController',
        'app/desktop/modules/proc/machines/details/tabs/inventory/machineInventoryController',
        'app/desktop/modules/proc/machines/details/tabs/stockMovement/machineStockMovementController'
    ],
    function (module) {
        module.controller('MachineDetailsController', MachineDetailsController);

        function MachineDetailsController($scope, $rootScope, $timeout, $state, $stateParams, ItemService, CommonService, $cookies) {

            $rootScope.viewInfo.icon = "fa fa-truck";
            $rootScope.viewInfo.title = "Machine Details";

            var vm = this;

            vm.machineId = $stateParams.machineId;
            vm.prjId = $stateParams.projectId;
            vm.machine = null;
            vm.editMachine = editMachine;
            vm.back = back;
            vm.machineDetailsTabActivated = machineDetailsTabActivated;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/proc/machines/details/tabs/basic/machineBasicDetailsView.jsp',
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Machine Type Attributes',
                    template: 'app/desktop/modules/proc/machines/details/tabs/attributes/machineAttributesView.jsp',
                    active: false,
                    activated: false
                }
            };

            function back() {
                window.history.back();
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function machineDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.machine.tabactivated', {tabId: tabId});

                    if (tab.id == "details.basic") {
                        loadMachine();
                    }
                }
                if (tab != null) {
                    activateTab(tab);
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

            function loadMachine() {
                vm.loading = true;
                ItemService.getMachineItem(vm.machineId).then(
                    function (data) {
                        vm.machine = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.machine], 'createdBy');
                        CommonService.getPersonReferences([vm.machine], 'modifiedBy');
                    }
                )
            }

            function editMachine() {
                $state.go('app.proc.machines.edit', {machineId: vm.machineId});
            }

            (function () {

            })();
        }
    }
);