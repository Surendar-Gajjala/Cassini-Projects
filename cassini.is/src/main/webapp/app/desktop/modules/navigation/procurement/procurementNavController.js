define(['app/desktop/desktop.app'
    ],
    function (module) {
        module.controller('ProcurementNavController', ProcurementNavController);

        function ProcurementNavController($scope, $rootScope, $stateParams, $timeout, $state, $cookies) {
            var vm = this;
            vm.navItems = [];

            var tabs = [
                {
                    id: "procurement.classification",
                    label: "Classification",
                    state: "app.proc.classification",
                    active: true
                },
                {id: "procurement.material", label: "Material", state: "app.proc.materials.all", active: false},
                {id: "procurement.machine", label: "Machine", state: "app.proc.machines.all", active: false},
                {id: "procurement.manpower", label: "Manpower", state: "app.proc.manpower.all", active: false}];

            var lastSelectedItem = null;
            vm.activateProjectTab = activateProjectTab;
            vm.activeProject = {
                name: "Procurement"
            };

            $rootScope.$on('app.activate.procurement', function (event, args) {
                vm.activeProject = args.project;
            });

            function activateProjectTab(id) {
                var item = getTabById(id);
                if (item != null) {
                    lastSelectedItem.active = false;
                    item.active = true;
                    lastSelectedItem = item;
                    $rootScope.tab = lastSelectedItem;

                    $timeout(function () {
                        $('.project-headerbar').trigger('click');
                    }, 100);

                    if (item.id == 'project.documents') {
                        $state.go(item.state, {type: 'documents'});
                    }
                    else if (item.id == 'project.drawings') {
                        $state.go(item.state, {type: 'drawings'});
                    }
                    else {
                        $state.go(item.state);
                    }
                }
            }

            function getTabById(id) {
                var found = null;

                angular.forEach(vm.navItems, function (item) {
                    if (item.id == id) {
                        found = item;
                    }
                });

                return found;
            }

            function masterDataTabs() {
                angular.forEach(tabs, function (tab) {
                    if (tab.id == 'procurement.classification') {
                        //if($rootScope.hasPermission('permission.masterdata.classification') || $rootScope.hasPermission('permission.masterdata.editClassification') || $rootScope.hasPermission('permission.masterdata.addType') || $rootScope.hasPermission('permission.masterdata.deleteType')) {
                        if (($stateParams.masterDataMode != null && $stateParams.masterDataMode != undefined) || $rootScope.mode == "Inventory") {
                            tab.active = false;
                        }

                        vm.navItems.push(tab);
                        //}
                    }
                    if (tab.id == 'procurement.material') {
                        if ($rootScope.hasPermission('permission.materials.view') || $rootScope.hasPermission('permission.materials.newMaterial') || $rootScope.hasPermission('permission.materials.edit') || $rootScope.hasPermission('permission.materials.delete') || $rootScope.hasPermission('permission.materials.addAttributes')) {

                            if ($stateParams.masterDataMode != null && $stateParams.masterDataMode != undefined) {
                                tab.active = true;
                            }
                            vm.navItems.push(tab);
                        }
                    }
                    if (tab.id == 'procurement.machine') {
                        if ($rootScope.hasPermission('permission.machines.view') || $rootScope.hasPermission('permission.machines.newMachine') || $rootScope.hasPermission('permission.machines.edit') || $rootScope.hasPermission('permission.machines.delete')) {
                            vm.navItems.push(tab);
                        }
                    }
                    if (tab.id == 'procurement.manpower') {
                        if ($rootScope.hasPermission('permission.manpower.view') || $rootScope.hasPermission('permission.manpower.newManpower') || $rootScope.hasPermission('permission.manpower.edit') || $rootScope.hasPermission('permission.manpower.delete')) {
                            vm.navItems.push(tab);
                        }
                    }
                });
                if ($stateParams.masterDataMode != null && $stateParams.masterDataMode != undefined) {
                    lastSelectedItem = vm.navItems[1];
                    lastSelectedItem.active = true;
                }
                if ($rootScope.mode != "Inventory") {
                    lastSelectedItem = vm.navItems[0];
                    lastSelectedItem.active = true;
                }
                if ($rootScope.mode == "Inventory") {
                    $rootScope.mode = null;
                    angular.forEach(vm.navItems, function (item) {
                        if (item.id == $rootScope.tab.id) {
                            item.active = true;
                            lastSelectedItem = item;
                        }
                    })
                } else {
                    lastSelectedItem = vm.navItems[0];
                    lastSelectedItem.active = true;
                }

            }

            (function () {
                if ($application.homeLoaded == true) {
                    masterDataTabs();
                    if ($stateParams.mode == 'materialTab' && $stateParams.materialId != null) {
                        activateProjectTab('procurement.material');
                        $state.go('app.proc.materials.details', {
                            materialId: $stateParams.materialId,
                            mode: 'null'
                        });
                    }
                }
            })();
        }
    }
)
;