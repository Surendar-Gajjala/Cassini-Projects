define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/workOrderService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/shared/services/core/assetService',
        'app/shared/services/core/workRequestService',
        'app/shared/services/core/maintenancePlanService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('WorkOrderBasicInfoController', WorkOrderBasicInfoController);

        function WorkOrderBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                              WorkOrderService, $translate, LoginService, AssetService, WorkRequestService, MaintenancePlanService) {
            var vm = this;
            vm.loading = true;
            vm.workOrderId = $stateParams.workOrderId;
            vm.workOrder = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateWorkOrder = updateWorkOrder;
            $rootScope.loadWorkOrderBasicDetails = loadWorkOrderBasicDetails;
            function loadWorkOrderBasicDetails() {
                vm.loading = true;
                if (vm.workOrderId != null && vm.workOrderId != undefined) {
                    WorkOrderService.getWorkOrder(vm.workOrderId).then(
                        function (data) {
                            vm.workOrder = data;
                            $rootScope.workOrder = vm.workOrder;
                            $scope.name = vm.workOrder.name;
                            loadAsset();
                            loadWorkRequest();
                            loadMaintenancePlan();
                            CommonService.getMultiplePersonReferences([vm.workOrder], ['createdBy', 'modifiedBy', 'assignedTo']);
                            if (vm.workOrder.description != null && vm.workOrder.description != undefined) {
                                vm.workOrder.descriptionHtml = $sce.trustAsHtml(vm.workOrder.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.editStatus = false;
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("WORK_ORDER_DETAILS");
                            $rootScope.viewInfo.description = vm.workOrder.number + " , " + vm.workOrder.name;
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadAsset() {
                AssetService.getAsset(vm.workOrder.asset).then(
                    function (data) {
                        vm.workOrder.assetObject = data;
                        if (vm.workOrder.assetObject.resourceObject.objectType == 'MACHINE' && vm.workOrder.assetObject.resourceObject.image != null) {
                            vm.workOrder.assetObject.imagePath = "api/mes/machines/" + vm.workOrder.assetObject.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.workOrder.assetObject.resourceObject.objectType == 'TOOL' && vm.workOrder.assetObject.resourceObject.image != null) {
                            vm.workOrder.assetObject.imagePath = "api/mes/tools/" + vm.workOrder.assetObject.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.workOrder.assetObject.resourceObject.objectType == 'EQUIPMENT' && vm.workOrder.assetObject.resourceObject.image != null) {
                            vm.workOrder.assetObject.imagePath = "api/mes/equipments/" + vm.workOrder.assetObject.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.workOrder.assetObject.resourceObject.objectType == 'INSTRUMENT' && vm.workOrder.assetObject.resourceObject.image != null) {
                            vm.workOrder.assetObject.imagePath = "api/mes/instruments/" + vm.workOrder.assetObject.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.workOrder.assetObject.resourceObject.objectType == 'JIGFIXTURE' && vm.workOrder.assetObject.resourceObject.image != null) {
                            vm.workOrder.assetObject.imagePath = "api/mes/jigsfixs/" + vm.workOrder.assetObject.resourceObject.id + "/image/download?" + new Date().getTime();
                        } else if (vm.workOrder.assetObject.resourceObject.objectType == 'MATERIAL' && vm.workOrder.assetObject.resourceObject.image != null) {
                            vm.workOrder.assetObject.imagePath = "api/mes/materials/" + vm.workOrder.assetObject.resourceObject.id + "/image/download?" + new Date().getTime();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showImage = showImage;
            function showImage(asset) {
                var modal = document.getElementById('item-thumbnail-basic' + asset.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + asset.id);
                $("#thumbnail-image-basic" + asset.id).width($('#thumbnail-view-basic' + asset.id).outerWidth());
                $("#thumbnail-image-basic" + asset.id).height($('#thumbnail-view-basic' + asset.id).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            function loadWorkRequest() {
                if (vm.workOrder.request != null && vm.workOrder.request != "") {
                    WorkRequestService.getWorkRequest(vm.workOrder.request).then(
                        function (data) {
                            vm.workOrder.requestObject = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadMaintenancePlan() {
                if (vm.workOrder.plan != null && vm.workOrder.plan != "") {
                    MaintenancePlanService.getMaintenancePlan(vm.workOrder.plan).then(
                        function (data) {
                            vm.workOrder.planObject = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var workOrderUpdated = parsed.html($translate.instant("WORK_ORDER_UPDATED_MESSAGE")).html();


            function validateWorkOrder() {
                var valid = true;
                if (vm.workOrder.name == null || vm.workOrder.name == "" || vm.workOrder.name == undefined) {
                    valid = false;
                    vm.workOrder.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateWorkOrder() {
                if (validateWorkOrder()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    WorkOrderService.updateWorkOrder(vm.workOrder).then(
                        function (data) {
                            loadWorkOrderBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(workOrderUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.workOrder.name = $scope.name;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            (function () {
                $scope.$on('app.workOrder.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        $rootScope.loadWorkOrderBasicDetails();
                    }
                });

            })();

        }
    }
);