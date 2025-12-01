define(
    [
        'app/desktop/modules/main/main.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/plantService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsController',
        'app/shared/services/core/mesObjectTypeService'
    ],
    function (module) {
        module.controller('RichTextSidePanelController', RichTextSidePanelController);

        function RichTextSidePanelController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $sce,PlantService,MESObjectTypeService,
                                             ObjectAttributeService, ItemService, MfrService, MfrPartsService, ECOService, SpecificationsService) {

            var vm = this;

            vm.richTextObjId = angular.copy($scope.data.selectedRichTextObj);
            vm.objectType = $scope.data.selectedObjectAttribute.objectType;
            if (vm.objectType == "ITEMREVISION") {
                vm.objectId = $scope.data.selectedProject.latestRevision;
            } else {
                vm.objectId = $scope.data.selectedProject.id;
            }
            vm.attributeDefId = $scope.data.selectedObjectAttribute;
            vm.editRichText = editRichText;
            vm.cancelRichText = cancelRichText;
            vm.richTextShowFalg = false;
            vm.saveRichText = saveRichText;
            function editRichText() {
                vm.oldValue = angular.copy(vm.richTextObj);
                vm.richTextShowFalg = true;
                $timeout(function () {
                    $('.note-current-fontname').text('Arial');
                }, 1000);

            }

            function cancelRichText() {
                vm.richTextObj = angular.copy(vm.oldValue);
                vm.richTextShowFalg = false;
            }

            function saveRichText() {
                if (vm.objectType == "SPECIFICATIONTYPE" || vm.objectType == "REQUIREMENTTYPE") {
                    SpecificationsService.updateSpecAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else if (vm.objectType == "ITEMTYPE") {
                    ItemService.updateItemAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else if (vm.objectType == "CHANGETYPE") {
                    ECOService.updateEcoAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else if (vm.objectType == "MANUFACTURERTYPE") {
                    MfrService.updateMfrAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else if (vm.objectType == "MANUFACTURERPARTTYPE") {
                    MfrPartsService.updateMfrPartAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else if (vm.objectType == "PLANTTYPE" || vm.objectType == "WORKCENTERTYPE" || vm.objectType == "MACHINETYPE" || vm.objectType == "EQUIPMENTTYPE" || vm.objectType == "INSTRUMENTTYPE" || vm.objectType == "TOOLTYPE" ||
                         vm.objectType == "JIGFIXTURETYPE" ||vm.objectType == "MATERIALTYPE" ||vm.objectType == "MANPOWERTYPE" || vm.objectType == "SHIFTSTYPE" || vm.objectType == "OPERATIONTYPE" ) {
                    MESObjectTypeService.updateMESObjectAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                else if (vm.objectType == "ASSETTYPE"  || vm.objectType == "METERTYPE"  || vm.objectType == "SPAREPARTTYPE" ||  vm.objectType == "WORKREQUESTTYPE" || vm.objectType == "WORKORDERTYPE" ) {
                    MESObjectTypeService.updateMROObjectAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    ObjectAttributeService.updateObjectAttribute(vm.objectId, vm.richTextObj).then(
                        function (data) {
                            vm.richTextObj = data;
                            vm.richTextShowFalg = false;
                            $rootScope.showSuccessMessage($rootScope.attributeUpdateMessage);

                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadRichText() {
                ObjectAttributeService.getObjectAttributeByIdAndDef(vm.objectId, vm.attributeDefId.id).then(
                    function (data) {
                        vm.richTextObj = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            (function () {
                loadRichText()
            })();
        }
    }
)
;
