define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/inspectionService'
    ],
    function (module) {
        module.controller('ItemInspectionController', ItemInspectionController);

        function ItemInspectionController($scope, $rootScope, $timeout, $window, $state, $stateParams, $translate, $cookies, InspectionService) {
            var vm = this;

            vm.itemId = $stateParams.itemId;
            vm.items = [];

            function loadItemInspections() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                InspectionService.getItemInspections(vm.itemId).then(
                    function (data) {
                        vm.inspections = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            vm.showInspection = showInspection;
            function showInspection(inspection) {
                $window.localStorage.setItem("lastSelectedItemTab", JSON.stringify("details.inspection"));
                $state.go('app.pqm.inspection.details', {inspectionId: inspection.id});
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {
                    if (data.tabId == 'details.inspection') {
                        if ($rootScope.selectedMasterItemId != null) {
                            $stateParams.itemId = $rootScope.selectedMasterItemId;
                            vm.itemId = $stateParams.itemId;
                        }
                        loadItemInspections();
                    }
                });
            })();
        }
    }
)
;