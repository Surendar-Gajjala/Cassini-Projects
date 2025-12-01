define(
    ['app/desktop/modules/proc/proc.module',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('MaterialTypeDetailsController', MaterialTypeDetailsController);

        function MaterialTypeDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                               ClassificationService, AutonumberService,
                                               CommonService, DialogService, LovService, ItemService, ItemTypeService) {

            var vm = this;

            vm.valid = true;
            vm.materialType = null;
            vm.autoNumbers = [];
            vm.lovs = [];
            vm.addAttribute = addAttribute;
            vm.deleteAttribute = deleteAttribute;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.editAttribute = editAttribute;
            vm.attributesShow = false;
            vm.onSave = onSave;
            vm.error = "";
            vm.refTypes = ['MATERIALTYPE', 'MACHINETYPE', 'MANPOWERTYPE'];
            var attributeMap = new Hashtable();
            var materialName = null;
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
                dataType: 'TEXT',
                revisionSpecific: false,
                changeControlled: false,
                required: false,
                objectType: 'MATERIALTYPE',
                editMode: true,
                showValues: false,
                refType: null,
                lov: null
            };

            function materialTypeSelected(event, args) {
                vm.materialType = args.typeObject;
                materialName = args.typeObject.name;
                nodeId = args.nodeId;
                if (vm.materialType != null && vm.materialType != undefined) {
                    if (vm.materialType.id == undefined || vm.materialType.id == null) {
                        vm.attributesShow = false;
                        $scope.$apply();
                    } else {
                        vm.attributesShow = true;
                        $scope.$apply();
                    }
                    vm.materialType.attributes = [];
                    loadAttributes();
                    loadAutoNumbers();
                    loadAllLovs();
                } else {
                    $scope.$apply();
                }
            }

            function loadAttributes() {
                if (vm.materialType.id != null || vm.materialType.id != undefined) {
                    ItemTypeService.getMaterialAttributesWithHierarchy(vm.materialType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                attribute.showValues = true;
                                attribute.editMode = false;
                                attributeMap.put(attribute.name, attribute);
                            });
                            vm.materialType.attributes = data;
                        }
                    );
                }
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (vm.materialType.materialNumberSource == null && item.name == "Default Material Item Number Source") {
                                vm.materialType.materialNumberSource = item;
                            }
                        });
                    }
                )
            }

            function validate() {
                vm.valid = true;

                $rootScope.closeNotification();

                if (vm.materialType.name == null || vm.materialType.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Material Type Name cannot be empty");
                }

                return vm.valid;
            }

            function onSave() {
                if (vm.materialType != null && validate()) {

                    if (vm.materialType.id == null || vm.materialType.id == undefined) {

                        ClassificationService.createType('MATERIALTYPE', vm.materialType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.showSuccessMessage(data.name + " : created successfully");
                                    vm.attributesShow = true;
                                    vm.materialType.id = data.id;
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeName: vm.materialType.name,
                                        nodeId: nodeId
                                    });
                                }

                                /*else if (data == null) {
                                 $rootScope.showErrorMessage(vm.materialType.name + " : already exists" );
                                 $rootScope.$broadcast("app.classification.update", {
                                 nodeName: vm.materialType.name
                                 });
                                 }*/
                            }/*,
                             function (error) {
                             $rootScope.showErrorMessage(vm.materialType.name + " : already exists" );
                             $rootScope.$broadcast("app.classification.update", {
                             nodeName: vm.materialType.name
                             });
                             }*/
                        )
                    }
                    else {
                        ClassificationService.updateType('MATERIALTYPE', vm.materialType).then(
                            function (data) {
                                if (data != null) {
                                    $rootScope.showSuccessMessage(data.name + " : updated successfully");
                                    $rootScope.$broadcast("app.classification.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.materialType.name
                                    });
                                }
                                /*else if (attributeMap.get(vm.materialType.name) != null) {
                                 $rootScope.showErrorMessage(vm.materialType.name + " : already exists" );
                                 //vm.materialType.name = materialName;
                                 loadAttributes();
                                 }*/
                            }/*,
                             function (error) {
                             $rootScope.showErrorMessage(vm.materialType.name + " : already exists" );
                             vm.materialType.name = materialName;
                             }*/
                        )

                    }
                }
            }

            function addAttribute() {
                var att = angular.copy(newAttribute);
                att.itemType = vm.materialType.id;
                vm.materialType.attributes.unshift(att);
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

            //  vm.hide=false;
            function applyChanges(attribute) {
                var promise = null;

                if (attribute.id == null || attribute.id == undefined) {
                    if (validateAttribute(attribute)) {
                        if (attributeMap.get(attribute.newName) != null) {
                            vm.error = "{0} Name already exists".format(attribute.newName);
                            vm.materialType.attributes.splice(vm.materialType.attributes.indexOf(attribute), 1);
                        } else {
                            attribute.name = attribute.newName;
                            attribute.description = attribute.newDescription;
                            attribute.dataType = attribute.newDataType;
                            attribute.refType = attribute.newRefType;
                            attribute.lov = attribute.newLov;
                            promise = ClassificationService.createTypeAttribute('MATERIALTYPE', vm.materialType.id, attribute);
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
                        promise = ClassificationService.updateTypeAttribute('MATERIALTYPE', vm.materialType.id, attribute);
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
                            attribute.editMode = false;
                            attributeMap.put(attribute.name, attribute);
                            $timeout(function () {
                                attribute.showValues = true;
                            }, 500);
                        }
                    )
                }
                else if (promise == null) {
                    // vm.error = "";
                    // attribute.editMode = true;
                    //attribute.showValues = false;
                    $timeout(function () {
                        vm.error = "";
                        attribute.editMode = true;
                        attribute.showValues = false;
                        //loadAttributes();
                        //vm.materialType.attributes.splice(vm.materialType.attributes.indexOf(attribute), 1);
                    }, 2000);

                }
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
                    vm.materialType.attributes.splice(vm.materialType.attributes.indexOf(attribute), 1);
                }
                else {
                    attribute.newName = attribute.name;
                    attribute.newDescription = attribute.description;
                    attribute.newDataType = attribute.dataType;
                    attribute.newRefType = attribute.refType;
                    attribute.newLov = attribute.lov;
                    attribute.editMode = false;
                    loadAttributes();
                    $timeout(function () {
                        attribute.showValues = true;
                    }, 500);
                }
                vm.error = "";
            }

            function deleteAttribute(attribute) {
                ItemService.findMaterialItemsByAttribute(attribute.id).then(
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
                                        ClassificationService.deleteTypeAttribute('MATERIALTYPE', vm.materialType.id, attribute.id).then(
                                            function (data) {
                                                vm.materialType.attributes.splice(vm.materialType.attributes.indexOf(attribute), 1);
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
                $scope.$on('app.materialType.selected', materialTypeSelected);
                $scope.$on('app.materialType.save', onSave);
            })();
        }
    }
);