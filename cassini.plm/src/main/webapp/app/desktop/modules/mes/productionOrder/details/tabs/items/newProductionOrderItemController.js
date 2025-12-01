define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/productionOrderService',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/bopService'
    ],
    function (module) {

        module.controller('NewProductionOrderItemController', NewProductionOrderItemController);

        function NewProductionOrderItemController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                                  BOPService, MBOMService, ProductionOrderService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var pleaseSelectMbom = parsed.html($translate.instant("PLEASE_SELECT_MBOM")).html();
            var pleaseSelectMbomRevision = parsed.html($translate.instant("PLEASE_SELECT_MBOM_REVISION")).html();
            var pleaseSelectBop = parsed.html($translate.instant("PLEASE_SELECT_BOP")).html();
            var pleaseSelectBopRevision = parsed.html($translate.instant("PLEASE_SELECT_BOP_REVISION")).html();
            var pleaseEnterQtyProduced = parsed.html($translate.instant("PLEASE_ENTER_QTY_PRODUCE")).html();

            vm.productionOrderItem = {
                id: null,
                productionOrder: $stateParams.productionOrderId,
                mbomRevision: null,
                bopRevision: null,
                quantityProduced: null
            };

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProductionOrderService.createProductionOrderItem(vm.productionOrderItem.productionOrder, vm.productionOrderItem).then(
                        function (data) {
                            vm.productionOrderItem = data;
                            $scope.callback(vm.productionOrderItem);
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage("Item created successfully");
                            vm.productionOrderItem = {
                                id: null,
                                productionOrder: $stateParams.productionOrderId,
                                mbomRevision: null,
                                bopRevision: null,
                                quantityProduced: null
                            };
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.selectedMbom = null;
            vm.selectedBop = null;
            function validate() {
                var valid = true;
                if (vm.selectedMbom == null || vm.selectedMbom == "" || vm.selectedMbom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectMbom);
                } else if (vm.productionOrderItem.mbomRevision == null || vm.productionOrderItem.mbomRevision == "" || vm.productionOrderItem.mbomRevision == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectMbomRevision);
                } else if (vm.selectedBop == null || vm.selectedBop == "" || vm.selectedBop == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectBop);
                } else if (vm.productionOrderItem.bopRevision == null || vm.productionOrderItem.bopRevision == "" || vm.productionOrderItem.bopRevision == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseSelectBopRevision);
                } else if (vm.productionOrderItem.quantityProduced == null || vm.productionOrderItem.quantityProduced == "" || vm.productionOrderItem.quantityProduced == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterQtyProduced);
                } else if (vm.productionOrderItem.quantityProduced != null && vm.productionOrderItem.quantityProduced == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterQtyProduced);
                }

                return valid;
            }

            function loadReleasedMboms() {
                MBOMService.getReleasedMBOMs().then(
                    function (data) {
                        vm.mboms = data;
                    }
                )
            }

            vm.onSelectMbom = onSelectMbom;
            function onSelectMbom(mbom) {
                vm.selectedMbom = mbom;
                vm.selectedBop = null;
                vm.productionOrderItem.mbomRevision = null;
                vm.productionOrderItem.bopRevision = null;
                if (mbom.mbomRevisions.length == 1) {
                    vm.productionOrderItem.mbomRevision = mbom.mbomRevisions[0].id;
                    onSelectMbomRevision(mbom.mbomRevisions[0]);
                }
            }

            vm.onSelectMbomRevision = onSelectMbomRevision;
            function onSelectMbomRevision(mbomRevision) {
                vm.selectedBop = null;
                vm.productionOrderItem.bopRevision = null;
                MBOMService.getMBOMReleasedBOPs(mbomRevision.id).then(
                    function (data) {
                        vm.bops = data;
                        if (vm.bops.length == 1) {
                            vm.selectedBop = vm.bops[0];
                            vm.productionOrderItem.bopRevision = vm.selectedBop.bopRevisions[0].id
                        }
                    }
                )
            }

            vm.onSelectBop = onSelectBop;
            function onSelectBop(bop) {
                vm.selectedBop = bop;
                vm.productionOrderItem.bopRevision = null;
                if (bop.bopRevisions.length > 0) {
                    vm.productionOrderItem.bopRevision = bop.bopRevisions[0].id;
                }
            }


            (function () {
                loadReleasedMboms();
                $rootScope.$on('app.productionOrder.items.add', create);
            })();
        }
    }
);