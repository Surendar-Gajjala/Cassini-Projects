define(
    [
        'app/desktop/modules/mes/mes.module'
    ],
    function (module) {
        module.controller('MESMasterDataHomeController', MESMasterDataHomeController);

        function MESMasterDataHomeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            var vm = this;
            $rootScope.viewInfo.showDetails = false;
            vm.selectedMasterData = null;

            vm.selectMasterData = selectMasterData;
            function selectMasterData(type) {
                vm.selectedMasterData = type;
            }

            function navigateMasterView(name) {
                if (name == "app.mes.masterData.plant.all" || name == "app.mes.masterData.plant.details") {
                    vm.selectedMasterData = "plants";
                } else if (name == "app.mes.masterData.assemblyline.all" || name == "app.mes.masterData.assemblyline.details") {
                    vm.selectedMasterData = "assemblyLine";
                } else if (name == "app.mes.masterData.workcenter.all" || name == "app.mes.masterData.workcenter.details") {
                    vm.selectedMasterData = "workCenter";
                } else if (name == "app.mes.masterData.tool.all" || name == "app.mes.masterData.tool.details") {
                    vm.selectedMasterData = "tools";
                } else if (name == "app.mes.masterData.machine.all" || name == "app.mes.masterData.machine.details") {
                    vm.selectedMasterData = "machine";
                } else if (name == "app.mes.masterData.operation.all" || name == "app.mes.masterData.operation.details") {
                    vm.selectedMasterData = "operation";
                } else if (name == "app.mes.masterData.material.all" || name == "app.mes.masterData.material.details") {
                    vm.selectedMasterData = "material";
                } else if (name == "app.mes.masterData.shift.all" || name == "app.mes.masterData.shift.details") {
                    vm.selectedMasterData = "shift";
                } else if (name == "app.mes.masterData.jigsAndFixtures.all" || name == "app.mes.masterData.jigsAndFixtures.details") {
                    vm.selectedMasterData = "jigFixtures";
                } else if (name == "app.mes.masterData.manpower.all" || name == "app.mes.masterData.manpower.details") {
                    vm.selectedMasterData = "manpower";
                } else if (name == "app.mes.masterData.maintenanceAndRepair.all" || name == "app.mes.masterData.maintenanceAndRepair.details") {
                    vm.selectedMasterData = "plants";
                } else if (name == "app.mes.masterData.equipment.all" || name == "app.mes.masterData.equipment.details") {
                    vm.selectedMasterData = "equipments";
                } else if (name == "app.mes.masterData.instrument.all" || name == "app.mes.masterData.instrument.details") {
                    vm.selectedMasterData = "instruments";
                } else if (name == "app.mes.masterData") {
                    vm.selectedMasterData = "plants";
                    $state.go("app.mes.masterData.plant.all")
                }
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $rootScope.$on('$stateChangeStart',
                        function (event, toState, toParams, fromState, fromParams) {
                            navigateMasterView(toState.name);
                        }
                    );
                    navigateMasterView($state.$current.name);
                })
            })();
        }
    }
);