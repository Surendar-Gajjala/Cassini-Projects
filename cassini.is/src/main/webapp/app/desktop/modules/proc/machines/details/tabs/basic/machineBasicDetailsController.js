define(['app/desktop/modules/proc/proc.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MachineBasicDetailsController', MachineBasicDetailsController);

        function MachineBasicDetailsController($scope, $rootScope, $stateParams, $timeout, $state, $cookies, $sce, DialogService, AttributeAttachmentService, ObjectAttributeService, ItemService, CommonService) {
            var vm = this;

            vm.machineId = $stateParams.machineId;
            vm.machine = null;
            vm.updateMachine = updateMachine;
            $rootScope.showObjectValues = showObjectValues;

            function loadMachine() {
                vm.loading = true;
                ItemService.getMachineItem(vm.machineId).then(
                    function (data) {
                        vm.machine = data;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Machine Details : " + vm.machine.itemName;
                        $rootScope.viewInfo.description = "Number: {0}".format(vm.machine.itemNumber);
                        CommonService.getPersonReferences([vm.machine], 'createdBy');
                        CommonService.getPersonReferences([vm.machine], 'modifiedBy');
                    }
                )
            }

            function validate() {
                var flag = true;
                if (vm.machine.itemName == null || vm.machine.itemName == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Machine Name cannot be empty");
                    loadMachine();
                }
                else if (vm.machine.units == null || vm.machine.units == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Machine units cannot be empty");
                    loadMachine();
                }
                return flag;
            }

            function updateMachine() {
                if (validate()) {
                    ItemService.updateMachine(vm.machine).then(
                        function (data) {
                            loadMachine();
                            $rootScope.showSuccessMessage("Machine updated successfully");
                        }
                    )
                }
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
                    loadMachine();
                }
            })();
        }
    });