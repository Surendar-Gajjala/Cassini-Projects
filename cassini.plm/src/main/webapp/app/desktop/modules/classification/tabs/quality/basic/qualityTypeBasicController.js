define(
    [
        'app/desktop/modules/classification/classification.module',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/itemClassTreeDirective',
        'app/desktop/modules/mfr/mfrparts/directive/manufacturerPartDirective'
    ],
    function (module) {
        module.controller('QualityTypeBasicController', QualityTypeBasicController);
        function QualityTypeBasicController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                            QualityTypeService, ItemTypeService, ItemService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.name = null;

            $rootScope.loadQualityBasicInfo = loadQualityBasicInfo;
            function loadQualityBasicInfo() {
                if ($rootScope.selectedQualityType != null && $rootScope.selectedQualityType != undefined && $rootScope.selectedQualityType.id != undefined && $rootScope.selectedQualityType.qualityType != undefined) {
                    QualityTypeService.getQualityTypeByType($rootScope.selectedQualityType.id, $rootScope.selectedQualityType.qualityType).then(
                        function (data) {
                            $rootScope.qualityType = data;
                            console.log($rootScope.qualityType);
                            vm.name = data.name;
                            loadQualityTypeObjects();
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadQualityTypeObjects() {
                if ($rootScope.qualityType != null && $rootScope.qualityType != undefined && $rootScope.qualityType.id != undefined && $rootScope.qualityType.qualityType != undefined) {
                    QualityTypeService.getObjectsByType($rootScope.qualityType.id, $rootScope.qualityType.qualityType).then(
                        function (data) {
                            if (data > 0) {
                                $rootScope.qualityTypeObjectsExist = true;
                            } else {
                                $rootScope.qualityTypeObjectsExist = false;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.onSelectType = onSelectType;
            function onSelectType(productType) {
                $rootScope.qualityType.productType = productType;
            }

            vm.onSelectPartType = onSelectPartType;
            function onSelectPartType(partType) {
                $rootScope.qualityType.partType = partType;
            }

            vm.save = save;
            function save() {
                if (validate()) {
                    if ($rootScope.qualityType != null) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($rootScope.qualityType.qualityType == "PRODUCTINSPECTIONPLANTYPE") {
                            QualityTypeService.updateProductInspectionPlanType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.showSuccessMessage("Inspection plan type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.qualityType.qualityType == "MATERIALINSPECTIONPLANTYPE") {
                            QualityTypeService.updateMaterialInspectionPlanType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.showSuccessMessage("Inspection plan type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.qualityType.qualityType == "PRTYPE") {
                            QualityTypeService.updatePrType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.showSuccessMessage("Problem Report type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    // $rootScope.loadQualityBasicInfo();
                                    $rootScope.qualityType.name = vm.name;
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.qualityType.qualityType == "NCRTYPE") {
                            QualityTypeService.updateNcrType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.showSuccessMessage("NCR type updated successfully");
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.qualityType.qualityType == "QCRTYPE") {
                            QualityTypeService.updateQcrType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.showSuccessMessage("QCR type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.qualityType.qualityType == "SUPPLIERAUDITTYPE") {
                            QualityTypeService.updateSupplierAuditType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.showSuccessMessage("Supplier audit type updated successfully");
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if ($rootScope.qualityType.qualityType == "PPAPTYPE") {
                            QualityTypeService.updatePpapType($rootScope.qualityType).then(
                                function (data) {
                                    $rootScope.loadQualityBasicInfo();
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: $rootScope.selectedQualityTypeNode,
                                        nodeName: $rootScope.qualityType.name
                                    });
                                    $rootScope.showSuccessMessage("PPAP type updated successfully");
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

            function validate() {
                var valid = true;
                if ($rootScope.qualityType.name == "" || $rootScope.qualityType.name == "" || $rootScope.qualityType.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Name cannot be empty")
                }

                return valid;
            }

            (function () {
                $scope.$on('app.qualityType.tabactivated', function (event, data) {
                    $rootScope.loadQualityBasicInfo();
                })
            })();
        }
    }
);