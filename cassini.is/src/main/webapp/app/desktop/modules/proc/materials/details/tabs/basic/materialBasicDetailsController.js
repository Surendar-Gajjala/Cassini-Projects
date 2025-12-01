define(['app/desktop/modules/proc/proc.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MaterialBasicDetailsController', MaterialBasicDetailsController);

        function MaterialBasicDetailsController($scope, $rootScope, $stateParams, ItemService, CommonService) {
            var vm = this;

            vm.materialId = $stateParams.materialId;
            vm.material = null;
            vm.updateMaterial = updateMaterial;
            vm.showObjectValues = showObjectValues;

            function updateMaterial() {
                if (validate()) {
                    ItemService.updateMaterial(vm.material).then(
                        function (data) {
                            loadMaterial();
                            $rootScope.showSuccessMessage("Material updated successfully");
                        }
                    )
                }
            }

            function validate() {
                var flag = true;
                if (vm.material.itemName == null || vm.material.itemName == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Material Name cannot be empty");
                    loadMaterial();
                }
                else if (vm.material.units == null || vm.material.units == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Material Units cannot be empty");
                    loadMaterial();
                }
                return flag;
            }

            function loadMaterial() {
                vm.loading = true;
                ItemService.getMaterialItem(vm.materialId).then(
                    function (data) {
                        vm.material = data;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Material Details : " + vm.material.itemName;
                        $rootScope.viewInfo.description = "Number: {0}".format(vm.material.itemNumber);
                        CommonService.getPersonReferences([vm.material], 'createdBy');
                        CommonService.getPersonReferences([vm.material], 'modifiedBy');
                    }
                )
            }

            vm.previousValue = null;
            function showObjectValues(attribute) {
                var options = null;
                attribute.editMode = true;
                vm.previousValue = attribute.value.refValue;
                if (attribute.attributeDef.refType == 'MATERIALTYPE') {
                    options = {
                        title: 'Select Material',
                        template: 'app/desktop/modules/select/materialSelectionView.jsp',
                        controller: 'MaterialSelectionController as materialSelectVm',
                        resolve: 'app/desktop/modules/select/materialSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.material'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.itemNumber + ' : Material added successfully');
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'MACHINETYPE') {
                    options = {
                        title: 'Select Machine',
                        template: 'app/desktop/modules/select/machineSelectionView.jsp',
                        controller: 'MachineSelectionController as machineSelectVm',
                        resolve: 'app/desktop/modules/select/machineSelectionController.js',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.machine'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.itemNumber + ' : Machine added successfully');
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
                else if (attribute.attributeDef.refType == 'MANPOWERTYPE') {
                    options = {
                        title: 'Select Manpower',
                        template: 'app/desktop/modules/select/manpowerSelectionView.jsp',
                        controller: 'ManpowerSelectionController as manpowerSelectVm',
                        resolve: 'app/desktop/modules/select/manpowerSelectionController',
                        width: 600,
                        side: 'left',
                        showMask: true,
                        buttons: [
                            {text: 'Select', broadcast: 'app.select.manpower'}
                        ],
                        callback: function (result) {
                            attribute.refValue = attribute.value.refValue;
                            attribute.value.refValue = result;
                            if (attribute.value.refValue != null) {
                                $rootScope.showInfoMessage(result.itemNumber + ' : Manpower added successfully');
                            }
                        }
                    };
                    $rootScope.showSidePanel(options);

                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadMaterial();
                }
            })();
        }
    });