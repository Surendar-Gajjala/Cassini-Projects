define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/supplierService'
    ],
    function (module) {
        module.controller('MESObjectTypeBasicController', MESObjectTypeBasicController);
        function MESObjectTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                              MESObjectTypeService, SupplierService, CommonService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            $rootScope.loadBasicInfo = loadBasicInfo;
            function loadBasicInfo() {
                if ($rootScope.selectedProductionResType != null) {
                    MESObjectTypeService.getMesObjectTypeByType($rootScope.selectedProductionResType.id, $rootScope.selectedProductionResType.objectType).then(
                        function (data) {
                            $rootScope.resourceType = data;
                            loadMesObjectTypeObjects();
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadMesObjectTypeObjects() {
                var promise = null;
                if ($rootScope.selectedProductionResType.objectType == "SUPPLIERTYPE") {
                    promise = SupplierService.getObjectsCountByType($rootScope.selectedProductionResType.id);
                } else {
                    promise = MESObjectTypeService.getObjectsCountByType($rootScope.selectedProductionResType.id, $rootScope.selectedProductionResType.objectType);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            if (data > 0) {
                                $rootScope.mesTypeObjectsExist = true;
                            } else {
                                $rootScope.mesTypeObjectsExist = false;
                            }
                            $scope.$off('app.mesObjectType.selected', loadBasicInfo);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var toolTypeUpdateMessage = parsed.html($translate.instant("TOOL_TYPE_UPDATE_MESSAGE")).html();
            var materialTypeUpdateMessage = parsed.html($translate.instant("MATERIAL_TYPE_UPDATE_MESSAGE")).html();
            var jigsFixTypeUpdateMessage = parsed.html($translate.instant("JIGS_FIXTURE_TYPE_UPDATE_MESSAGE")).html();
            var lifecycleHasNoValues = parsed.html($translate.instant("LIFECYCLE_HAS_NO_VALUES")).html();
            var typeValidation = parsed.html($translate.instant("TYPE_UPDATE_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            vm.onSave = onSave;
            function onSave() {
                if (validate()) {
                    if ($rootScope.selectedProductionResType != null) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($rootScope.selectedProductionResType.objectType == "TOOLTYPE") {
                            MESObjectTypeService.updateToolType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage(toolTypeUpdateMessage);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "MATERIALTYPE") {
                            MESObjectTypeService.updateMaterialType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.showSuccessMessage(materialTypeUpdateMessage);
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "JIGFIXTURETYPE") {
                            MESObjectTypeService.updateJigsFixtureType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage(jigsFixTypeUpdateMessage);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "PLANTTYPE") {
                            MESObjectTypeService.updatePlantType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "ASSEMBLYLINETYPE") {
                            MESObjectTypeService.updateAssemblyLineType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "MACHINETYPE") {
                            MESObjectTypeService.updateMachineType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "WORKCENTERTYPE") {
                            MESObjectTypeService.updateWorkCenterType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "OPERATIONTYPE") {
                            MESObjectTypeService.updateOperationType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "PRODUCTIONORDERTYPE") {
                            MESObjectTypeService.updateProductionOrderType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "MBOMTYPE") {
                            MESObjectTypeService.updateMBOMType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "BOPTYPE") {
                            MESObjectTypeService.updateBOPType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }  else if ($rootScope.selectedProductionResType.objectType == "MANPOWERTYPE") {
                            MESObjectTypeService.updateManpowerType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "EQUIPMENTTYPE") {
                            MESObjectTypeService.updateEquipmentType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "INSTRUMENTTYPE") {
                            MESObjectTypeService.updateInstrumentType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "SPAREPARTTYPE") {
                            MESObjectTypeService.updateSparePartType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "WORKREQUESTTYPE") {
                            MESObjectTypeService.updateWorkRequestType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "WORKORDERTYPE") {
                            MESObjectTypeService.updateWorkOrderType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "ASSETTYPE") {
                            MESObjectTypeService.updateAssetType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "METERTYPE") {
                            MESObjectTypeService.updateMeterType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.selectedProductionResType.objectType == "SUPPLIERTYPE") {
                            MESObjectTypeService.updateSupplierType($rootScope.resourceType).then(
                                function (data) {
                                    loadBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedResourceTypeNode,
                                        nodeName: $rootScope.resourceType.name
                                    });
                                    $rootScope.showSuccessMessage($rootScope.resourceType.name + " " + typeValidation);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            $rootScope.hideBusyIndicator();
                        }
                    }
                }
            }

            vm.onSelectLifecycle = onSelectLifecycle;
            function onSelectLifecycle(lifecycle) {
                if (lifecycle.phases.length == 0) {
                    $rootScope.resourceType.lifecycle = null;
                    $rootScope.showWarningMessage(lifecycle.name + " : " + lifecycleHasNoValues);
                }
            }

            function validate() {
                var valid = true;
                if ($rootScope.resourceType.name == "" || $rootScope.resourceType.name == "" || $rootScope.resourceType.name == undefined) {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                }

                return valid;
            }

            (function () {
                $scope.$on('app.objectType.tabActivated', function (event, data) {
                    $rootScope.loadBasicInfo();
                })
            })();
        }
    }
);