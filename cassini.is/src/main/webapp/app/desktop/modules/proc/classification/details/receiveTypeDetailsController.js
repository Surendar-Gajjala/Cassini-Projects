/**
 * Created by swapna on 20/06/19.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('ReceiveTypeDetailsController', ReceiveTypeDetailsController);

        function ReceiveTypeDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ItemService, ItemTypeService,
                                              ClassificationService, AutonumberService, CommonService, LovService, DialogService) {

            var vm = this;
            vm.valid = true;
            vm.error = "";
            vm.receiveType = null;
            vm.autoNumbers = [];
            vm.lovs = [];
            vm.onSave = onSave;
            vm.addAttribute = addAttribute;
            vm.applyChanges = applyChanges;
            vm.editAttribute = editAttribute;
            vm.cancelChanges = cancelChanges;
            vm.deleteAttribute = deleteAttribute;
            vm.attributesShow = false;
            vm.refTypes = ['MACHINETYPE', 'MACHINETYPE', 'MANPOWERTYPE'];
            var attributeMap = new Hashtable();
            var name = null;
            var nodeId = null;

            vm.dataTypes = [
                'TEXT',
                'INTEGER',
                'DOUBLE',
                'DATE',
                'BOOLEAN',
                'LIST',
                'TIME',
                'TIMESTAMP',
                'CURRENCY',
                //'OBJECT',
                'IMAGE',
                'ATTACHMENT'
            ];
            var newAttribute = {
                id: null,
                name: null,
                description: null,
                dataType: 'STRING',
                required: false,
                objectType: 'MATERIALRECEIVETYPE',
                editMode: true,
                showValues: false,
                refType: null,
                lov: null
            };

            function receiveTypeSelected(event, args) {
                vm.receiveType = args.typeObject;
                name = args.typeObject.name;
                nodeId = args.nodeId;
                if (vm.receiveType != null && vm.receiveType != undefined) {
                    if (vm.receiveType.id == undefined || vm.receiveType.id == null) {
                        vm.attributesShow = false;
                        $scope.$apply();
                    } else {
                        vm.attributesShow = true;
                        $scope.$apply();
                    }
                    vm.receiveType.attributes = [];
                    loadAttributes();
                    loadAutoNumbers();
                    loadAllLovs();
                } else {
                    $scope.$apply();
                }
            }

            function loadAttributes() {
                attributeMap = new Hashtable();
                if (vm.receiveType.id != null || vm.receiveType.id != undefined) {
                    ItemTypeService.getReceiveTypeAttributesWithHierarchy(vm.receiveType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                attribute.showValues = true;
                                attribute.editMode = false;
                                attributeMap.put(attribute.name, attribute);
                            });
                            vm.receiveType.attributes = data;
                        }
                    );
                }
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (vm.receiveType.receiveNumberSource == null && item.name == "Default Stock Receive Number Source") {
                                vm.receiveType.receiveNumberSource = item;
                                if (vm.receiveType.id != undefined) {
                                    ClassificationService.updateType('MATERIALRECEIVETYPE', vm.receiveType).then(
                                        function (data) {
                                        }
                                    );
                                }

                            }
                        });
                    }
                )
            }

            function loadAllLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovs = data;
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function onSave() {
                if (vm.receiveType != null && validate()) {

                    if (vm.receiveType.id == null || vm.receiveType.id == undefined) {

                        ClassificationService.createType('MATERIALRECEIVETYPE', vm.receiveType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.showSuccessMessage(data.name + " : created successfully");
                                    vm.attributesShow = true;
                                    vm.receiveType.id = data.id;
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.receiveType.name
                                    });
                                }
                                else if (data == null) {
                                    $rootScope.showErrorMessage(vm.receiveType.name + " : already exists");
                                    vm.receiveType.name = name;
                                }
                            },
                            function (error) {
                                $rootScope.showErrorMessage(vm.receiveType.name + " : already exists");
                                vm.receiveType.name = name;
                            }
                        )
                    }
                    else {
                        ClassificationService.updateType('MATERIALRECEIVETYPE', vm.receiveType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.showSuccessMessage(data.name + " : updated successfully");
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.receiveType.name
                                    });
                                }
                                else if (data == null) {
                                    $rootScope.showErrorMessage(vm.receiveType.name + " : already exists");
                                    vm.receiveType.name = name;
                                }
                            },
                            function (error) {
                                $rootScope.showErrorMessage(vm.receiveType.name + " : already exists");
                                vm.receiveType.name = name;
                            }
                        )

                    }
                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.receiveType.name == null || vm.receiveType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Receive Type Name cannot be empty");
                }

                return vm.valid;
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                att.itemType = vm.receiveType.id;
                vm.receiveType.attributes.unshift(att);
            }

            function applyChanges(attribute) {
                var promise = null;

                if (attribute.id == null || attribute.id == undefined) {
                    if (validateAttribute(attribute)) {
                        if (attributeMap.get(attribute.newName) != null) {
                            vm.error = "{0} Name already exists".format(attribute.newName);
                            vm.receiveType.attributes.splice(vm.receiveType.attributes.indexOf(attribute), 1);
                        }
                        else {
                            attribute.name = attribute.newName;
                            attribute.description = attribute.newDescription;
                            attribute.dataType = attribute.newDataType;
                            attribute.refType = attribute.newRefType;
                            attribute.lov = attribute.newLov;
                            promise = ClassificationService.createTypeAttribute('MATERIALRECEIVETYPE', vm.receiveType.id, attribute);
                            $rootScope.showSuccessMessage("Attribute created successfully");
                        }
                    }

                }
                else if (validateAttribute(attribute)) {
                    if (attributeMap.get(attribute.newName) == null || (attributeMap.get(attribute.newName) != null && attributeMap.get(attribute.newName).id == attribute.id)) {
                        attribute.name = attribute.newName;
                        attribute.description = attribute.newDescription;
                        attribute.dataType = attribute.newDataType;
                        attribute.refType = attribute.newRefType;
                        attribute.lov = attribute.newLov;
                        promise = ClassificationService.updateTypeAttribute('MATERIALRECEIVETYPE', vm.receiveType.id, attribute);
                        $rootScope.showSuccessMessage("Attribute updated successfully");
                    }
                    else {
                        vm.error = "{0} Name already exists".format(attribute.newName);
                    }
                }

                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.error = "";
                            attribute.id = data.id;
                            attributeMap.put(attribute.name, attribute);
                            attribute.editMode = false;
                            $timeout(function () {
                                attribute.showValues = true;
                            }, 500);
                        }
                    )
                }
                else if (promise == null) {
                    $timeout(function () {
                        vm.error = "";
                        attribute.editMode = true;
                        attribute.showValues = false;
                    }, 2000);
                }
            }

            function validateAttribute(attribute) {
                var valid = true;
                if (attribute.newName == null || attribute.newName == "" || attribute.newName == undefined) {
                    vm.error = "Please enter Name";
                    valid = false;
                } else if (attribute.newDataType == null || attribute.newDataType == "" || attribute.newDataType == undefined) {
                    vm.error = "Please select Data Type";
                    valid = false;
                } else if (attribute.newDataType == 'LIST' &&
                    (attribute.newLov == null || attribute.newLov == "" || attribute.newLov == undefined)) {
                    vm.error = "Please select a List of value";
                    valid = false;

                } else if (attribute.newDataType == 'OBJECT' &&
                    (attribute.newRefType == null || attribute.newRefType == "" || attribute.newRefType == undefined)) {
                    vm.error = "Please select a Reference Type";
                    valid = false;
                }

                return valid;
            }

            function editAttribute(attribute) {
                attribute.showValues = false;
                attribute.newName = attribute.name;
                attribute.newDescription = attribute.description;
                attribute.newDataType = attribute.dataType;
                attribute.newRefType = attribute.refType;
                attribute.newLov = attribute.lov;
                $timeout(function () {
                    attribute.editMode = true;
                }, 500);
            }

            function cancelChanges(attribute) {
                if (attribute.id == null || attribute.id == undefined) {
                    vm.receiveType.attributes.splice(vm.receiveType.attributes.indexOf(attribute), 1);
                }
                else {
                    attribute.newName = attribute.name;
                    attribute.newDescription = attribute.description;
                    attribute.newDataType = attribute.dataType;
                    attribute.editMode = false;
                    loadAttributes();
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }
                vm.error = "";
            }

            function deleteAttribute(attribute) {

                ItemService.findReceiveTypeItemsByAttribute(attribute.id).then(
                    function (data) {
                        if (data.length == 0) {
                            var options = {
                                title: 'Delete Attribute',
                                message: 'Are you sure you want to delete this Attribute?',
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    if (attribute.id != null && attribute.id != undefined) {
                                        ClassificationService.deleteTypeAttribute('MATERIALRECEIVETYPE', vm.receiveType.id, attribute.id).then(
                                            function (data) {
                                                vm.receiveType.attributes.splice(vm.receiveType.attributes.indexOf(attribute), 1);
                                                $rootScope.showSuccessMessage("Attribute deleted successfully");
                                            }
                                        )
                                    }
                                }
                            });
                        }
                        else {
                            $rootScope.showErrorMessage("This Attribute is in use! We cannot delete this Attribute");

                        }
                    }
                );

            }

            (function () {
                $scope.$on('app.receiveType.selected', receiveTypeSelected);
                $scope.$on('app.receiveType.save', onSave);
            })();
        }
    }
);