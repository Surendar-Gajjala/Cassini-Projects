define(
    ['app/desktop/modules/proc/proc.module',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('ManPowerTypeDetailsController', ManPowerTypeDetailsController);

        function ManPowerTypeDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ClassificationService,
                                               AutonumberService, CommonService, DialogService, LovService, ItemService, ItemTypeService) {

            var vm = this;
            vm.valid = true;
            vm.error = "";
            vm.manpowerType = null;
            vm.autoNumbers = [];
            vm.onSave = onSave;
            vm.addAttribute = addAttribute;
            vm.applyChanges = applyChanges;
            vm.editAttribute = editAttribute;
            vm.cancelChanges = cancelChanges;
            vm.deleteAttribute = deleteAttribute;
            vm.attributesShow = false;
            vm.refTypes = ['MATERIALTYPE', 'MACHINETYPE', 'MANPOWERTYPE'];
            var attributeMap = new Hashtable();
            var manpowerName = null;
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
                'OBJECT',
                'IMAGE',
                'ATTACHMENT'
            ];
            var newAttribute = {
                id: null,
                name: null,
                description: null,
                dataType: 'STRING',
                revisionSpecific: false,
                changeControlled: false,
                required: false,
                objectType: 'MANPOWERTYPE',
                editMode: true,
                showValues: false,
                refType: null,
                lov: null
            };

            function manpowerTypeSelected(event, args) {
                vm.manpowerType = args.typeObject;
                manpowerName = args.typeObject.name;
                nodeId = args.nodeId;
                if (vm.manpowerType != null && vm.manpowerType != undefined) {
                    if (vm.manpowerType.id == undefined || vm.manpowerType.id == null) {
                        vm.attributesShow = false;
                        $scope.$apply();
                    } else {
                        vm.attributesShow = true;
                        $scope.$apply();
                    }
                    vm.manpowerType.attributes = [];
                    loadAttributes();
                    loadAutoNumbers();
                    loadAllLovs();
                } else {
                    $scope.$apply();
                }
            }

            function loadAttributes() {
                if (vm.manpowerType.id != null || vm.manpowerType.id != undefined) {
                    ItemTypeService.getManpowerAttributesWithHierarchy(vm.manpowerType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                attribute.showValues = true;
                                attribute.editMode = false;
                                attributeMap.put(attribute.name, attribute);
                            });
                            vm.manpowerType.attributes = data;
                        }
                    );
                }
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (vm.manpowerType.manpowerNumberSource == null && item.name == "Default Manpower Item Number Source") {
                                vm.manpowerType.manpowerNumberSource = item;
                                if (vm.manpowerType.id != undefined) {
                                    ClassificationService.updateType('MANPOWERTYPE', vm.manpowerType).then(
                                        function (data) {

                                        }
                                    )
                                }
                            }
                        });
                    }
                )
            }

            function onSave() {
                if (vm.manpowerType != null && validate()) {

                    if (vm.manpowerType.id == null || vm.manpowerType.id == undefined) {

                        ClassificationService.createType('MANPOWERTYPE', vm.manpowerType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.showSuccessMessage(data.name + " : created successfully");
                                    vm.attributesShow = true;
                                    vm.manpowerType.id = data.id;
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.manpowerType.name
                                    });
                                }
                                /*else if (data == null) {
                                 $rootScope.showErrorMessage(vm.manpowerType.name + " : already exists");
                                 vm.manpowerType.name = manpowerName;
                                 }*/
                            }/*,
                             function (error) {
                             $rootScope.showErrorMessage(vm.manpowerType.name + " : already exists");
                             vm.manpowerType.name = manpowerName;
                             }*/
                        )
                    }
                    else {
                        ClassificationService.updateType('MANPOWERTYPE', vm.manpowerType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.showSuccessMessage(data.name + " : updated successfully");
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.manpowerType.name
                                    });
                                }
                                /*else if (data == null) {
                                 $rootScope.showErrorMessage(vm.manpowerType.name + " : already exists");
                                 vm.manpowerType.name = manpowerName;
                                 }*/
                            }/*,
                             function (error) {
                             $rootScope.showErrorMessage(vm.manpowerType.name + " : already exists");
                             vm.manpowerType.name = manpowerName;
                             }*/
                        )

                    }
                }
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.manpowerType.name == null || vm.manpowerType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Manpower Type Name cannot be empty");
                }

                return vm.valid;
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                att.itemType = vm.manpowerType.id;
                vm.manpowerType.attributes.unshift(att);
            }

            function applyChanges(attribute) {
                var promise = null;

                if (attribute.id == null || attribute.id == undefined) {
                    if (validateAttribute(attribute)) {
                        if (attributeMap.get(attribute.newName) != null) {
                            vm.error = "{0} Name already exists".format(attribute.newName);
                            vm.manpowerType.attributes.splice(vm.manpowerType.attributes.indexOf(attribute), 1);
                        } else {
                            attribute.name = attribute.newName;
                            attribute.description = attribute.newDescription;
                            attribute.dataType = attribute.newDataType;
                            attribute.refType = attribute.newRefType;
                            attribute.lov = attribute.newLov;
                            promise = ClassificationService.createTypeAttribute('MANPOWERTYPE', vm.manpowerType.id, attribute);
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
                        promise = ClassificationService.updateTypeAttribute('MANPOWERTYPE', vm.manpowerType.id, attribute);
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
                        /* loadAttributes();
                         vm.manpowerType.attributes.splice(vm.manpowerType.attributes.indexOf(attribute), 1);*/
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
                }
                else if (attribute.newDataType == 'LIST' &&
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
                $timeout(function () {
                    attribute.editMode = true;
                }, 500);
            }

            function cancelChanges(attribute) {
                if (attribute.id == null || attribute.id == undefined) {
                    vm.manpowerType.attributes.splice(vm.manpowerType.attributes.indexOf(attribute), 1);
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

                ItemService.findManpowerItemsByAttribute(attribute.id).then(
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
                                        ClassificationService.deleteTypeAttribute('MANPOWERTYPE', vm.manpowerType.id, attribute.id).then(
                                            function (data) {
                                                vm.manpowerType.attributes.splice(vm.manpowerType.attributes.indexOf(attribute), 1);
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

            (function () {
                $scope.$on('app.manpowerType.selected', manpowerTypeSelected);
                $scope.$on('app.manpowerType.save', onSave);
            })();
        }
    }
);