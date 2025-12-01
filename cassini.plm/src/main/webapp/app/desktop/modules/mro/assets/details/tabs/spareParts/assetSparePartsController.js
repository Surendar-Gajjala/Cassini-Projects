define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AssetSparePartsController', AssetSparePartsController);

        function AssetSparePartsController($scope, $rootScope, $sce, $timeout, $state, $stateParams, DialogService, AssetService) {
            var vm = this;
            vm.assetId = $stateParams.assetId;

            vm.addSpareParts = addSpareParts;
            vm.deletePart = deletePart;
            vm.showSparePart = showSparePart;

            vm.selectedSpareParts = [];


            var emptySparePart = {
                id: null,
                asset: vm.assetId,
                sparePart: null
            };
            vm.assetSpareParts = [];


            function loadAssetSpareParts() {
                vm.assetSpareParts = [];
                AssetService.getAssetSpareParts(vm.assetId).then(
                    function (data) {
                        vm.assetSpareParts = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function addSpareParts() {
                var options = {
                    title: "Add Spare Parts",
                    template: 'app/desktop/modules/mro/workOrder/details/tabs/sparePart/selectSparePartView.jsp',
                    controller: 'SelectSparePartController as selectSparePartsVm',
                    resolve: 'app/desktop/modules/mro/workOrder/details/tabs/sparePart/selectSparePartController',
                    width: 700,
                    showMask: true,
                    data: {
                        selectedAssetId: vm.assetId,
                        mode:"ASSET"
                    },
                    buttons: [
                        {text: "Add", broadcast: 'app.workorder.spareparts.add'}
                    ],
                    callback: function (result) {
                        angular.forEach(result, function (sparePart) {
                            var part = angular.copy(emptySparePart);
                            part.sparePart = sparePart;
                            vm.selectedSpareParts.push(part);
                        });
                        saveParts(vm.selectedSpareParts);

                    }
                };

                $rootScope.showSidePanel(options);
            }

            function saveParts() {
                AssetService.createMultipleAssetSpareParts(vm.assetId, vm.selectedSpareParts).then(
                    function (data) {
                        loadAssetSpareParts();
                        $rootScope.loadAssetTabCounts();
                        vm.selectedSpareParts = [];
                        $rootScope.showSuccessMessage("Spare Parts added successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }


            function deletePart(part) {
                var options = {
                    title: "Remove Spare Part",
                    message: "Are you sure you want to delete spare part?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            $rootScope.showBusyIndicator($('.view-container'));
                            AssetService.deleteAssetSparePart(vm.assetId, part.id).then(
                                function (data) {
                                    loadAssetSpareParts();
                                    $rootScope.loadAssetTabCounts();
                                    $rootScope.showSuccessMessage("Spare part removed successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                )
            }


            function showSparePart(part) {
                $state.go('app.mro.sparePart.details', {sparePartId: part.sparePart.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.asset.tabActivated', function (event, data) {
                    if (data.tabId == 'details.spareParts') {
                        loadAssetSpareParts();
                    }
                })
            })();
        }
    }
);
