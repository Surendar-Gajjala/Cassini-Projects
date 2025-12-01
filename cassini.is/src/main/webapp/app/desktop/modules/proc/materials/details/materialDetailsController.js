define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/proc/materials/details/tabs/basic/materialBasicDetailsController',
        'app/desktop/modules/proc/materials/details/tabs/attributes/materialAttributesController',
        'app/desktop/modules/proc/materials/details/tabs/inventory/materialInventoryController',
        'app/desktop/modules/proc/materials/details/tabs/stockMovement/materialStockMovementController'
    ],
    function (module) {
        module.controller('MaterialDetailsController', MaterialDetailsController);

        function MaterialDetailsController($scope, $rootScope, $timeout, $state, $stateParams, ItemService, CommonService, $cookies) {

            $rootScope.viewInfo.icon = "fa fa-th";
            $rootScope.viewInfo.title = "Material Details";

            var vm = this;

            vm.materialId = $stateParams.materialId;
            vm.prjId = $stateParams.projectId;
            vm.material = null;
            vm.editMaterial = editMaterial;
            vm.back = back;
            vm.materialDetailsTabActivated = materialDetailsTabActivated;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/proc/materials/details/tabs/basic/materialBasicDetailsView.jsp',
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'MaterialType Attributes',
                    template: 'app/desktop/modules/proc/materials/details/tabs/attributes/materialAttributesView.jsp',
                    active: false,
                    activated: false
                },
                inventory: {
                    id: 'details.inventory',
                    heading: 'Inventory',
                    template: 'app/desktop/modules/proc/materials/details/tabs/inventory/materialInventoryView.jsp',
                    active: false,
                    activated: false
                },
                stockMovement: {
                    id: 'details.stockMovement',
                    heading: 'Inventory history',
                    template: 'app/desktop/modules/proc/materials/details/tabs/stockMovement/materialStockMovementView.jsp',
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

            function materialDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.material.tabactivated', {tabId: tabId});

                    if (tab.id == "details.basic") {
                        loadMaterial();
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

            function loadMaterial() {
                vm.loading = true;
                ItemService.getMaterialItem(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        vm.loading = false;
                        CommonService.getPersonReferences([vm.material], 'createdBy');
                        CommonService.getPersonReferences([vm.material], 'modifiedBy');
                    }
                )
            }

            function editMaterial() {
                $state.go('app.proc.material.edit', {materialId: vm.materialId});
            }

            (function () {

            })();
        }
    }
);