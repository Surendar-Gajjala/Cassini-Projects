define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/productionOrderService',
        'app/shared/services/core/plantService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ProductionOrderBasicInfoController', ProductionOrderBasicInfoController);

        function ProductionOrderBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, PlantService, $cookies, CommonService, ProductionOrderService, $translate) {
            var vm = this;

            vm.loading = true;
            vm.productionOrderId = $stateParams.productionOrderId;
            vm.productionOrder = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateProductionOrder = updateProductionOrder;

            $rootScope.loadBasicProductionOrder = loadBasicProductionOrder;
            function loadBasicProductionOrder() {
                vm.loading = true;
                if (vm.productionOrderId != null && vm.productionOrderId != undefined) {
                    ProductionOrderService.getProductionOrder(vm.productionOrderId).then(
                        function (data) {
                            vm.productionOrder = data;
                            $rootScope.productionOrder = vm.productionOrder;
                            $scope.name = vm.productionOrder.name;
                            CommonService.getPersonReferences([vm.productionOrder], 'modifiedBy');
                            CommonService.getPersonReferences([vm.productionOrder], 'createdBy');
                            if (vm.productionOrder.description != null && vm.productionOrder.description != undefined) {
                                vm.productionOrder.descriptionHtml = $sce.trustAsHtml(vm.productionOrder.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            $rootScope.viewInfo.title = $translate.instant("PRODUCTION_ORDER_DETAILS");
                            $rootScope.viewInfo.description = vm.productionOrder.number + " , " + vm.productionOrder.name;
                            vm.loading = false;
                            $scope.$evalAsync();
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();

            function validateProductionOrder() {
                var valid = true;
                if (vm.productionOrder.name == null || vm.productionOrder.name == "" || vm.productionOrder.name == undefined) {
                    valid = false;
                    vm.productionOrder.name = $scope.typeName;
                    vm.productionOrder.plantName = data.plantName;
                    $rootScope.showWarningMessage(itemNameValidation);
                } else if (vm.productionOrder.plannedStartDate != null && vm.productionOrder.plannedFinishDate != null && vm.productionOrder.plannedStartDate != "" && vm.productionOrder.plannedFinishDate != "") {
                    var plannedFinishDate = moment(vm.productionOrder.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.productionOrder.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }
                }
                return valid;
            }

            var productionOrderUpdated = parsed.html($translate.instant("PRODUCTION_ORDER_UPDATED")).html();

            function updateProductionOrder() {
                if (validateProductionOrder()) {
                    ProductionOrderService.updateProductionOrder(vm.productionOrder).then(
                        function (data) {
                            vm.editStartDate = false;
                            vm.editFinishDate = false;
                            $rootScope.showSuccessMessage(productionOrderUpdated);
                            loadBasicProductionOrder();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadPlants() {
                PlantService.getPlants().then(
                    function (data) {
                        vm.plants = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showShift = showShift;
            function showShift() {
                $state.go('app.mes.masterData.shift.details', {
                    shiftId: vm.productionOrder.shift,
                    tab: 'details.basic'
                });
            }

            vm.editStartDate = false;
            vm.changeStartDate = changeStartDate;
            function changeStartDate() {
                vm.editStartDate = !vm.editStartDate;
            }

            vm.editFinishDate = false;
            vm.changeFinishDate = changeFinishDate;
            function changeFinishDate() {
                vm.editFinishDate = !vm.editFinishDate;
            }

            (function () {
                $scope.$on('app.productionOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadBasicProductionOrder();
                        loadPlants();
                    }
                });

            })();

        }
    }
);