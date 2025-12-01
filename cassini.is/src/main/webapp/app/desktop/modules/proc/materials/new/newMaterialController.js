define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/itemTypeService',
        'app/desktop/modules/proc/materials/directive/materialDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('NewMaterialController', NewMaterialController);

        function NewMaterialController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies,
                                       AutonumberService, ItemService, ClassificationService, AttributeAttachmentService,
                                       ObjectAttributeService, ItemTypeService, LovService) {

            var vm = this;

            vm.newMaterial = {
                itemType: null,
                itemNumber: null,
                itemName: null,
                description: null,
                units: "Each"
            };

            var materials = [];
            var materialMap = new Hashtable();

            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate"
                }
            };

            vm.attributes = [];
            vm.attribute = null;
            vm.material = null;

            vm.onSelectType = onSelectType;
            vm.createItem = createItem;
            vm.cancelItem = cancelItem;
            vm.autoNumber = autoNumber;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            function anotherItem() {
                create().then(
                    function () {
                        vm.newMaterial = {
                            itemType: null,
                            itemNumber: null,
                            itemName: null,
                            description: null,
                            units: "Each"
                        };
                        $rootScope.hideBusyIndicator();
                        $rootScope.hideSidePanel();
                        $rootScope.showSuccessMessage("Material (" + vm.material.itemNumber + ") created successfully");
                        //$scope.callback();
                        $rootScope.$broadcast('app.materials.all');
                    }
                );
            }

            function createItem() {
                create().then(
                    function () {
                        $rootScope.hideSidePanel();
                        //$scope.callback();
                        $rootScope.$broadcast('app.materials.all');
                    }
                );
            }

            function validate() {
                var valid = true;

                if (vm.newMaterial.itemType == null || vm.newMaterial.itemType == undefined || vm.newMaterial.itemType == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Material Type cannot be empty');
                }
                else if (vm.newMaterial.itemNumber == null || vm.newMaterial.itemNumber == undefined || vm.newMaterial.itemNumber == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Material Number cannot be empty');
                }
                else if (vm.newMaterial.itemName == null || vm.newMaterial.itemName == undefined || vm.newMaterial.itemName == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Material Name cannot be empty');
                }
                else if (materialMap.get(vm.newMaterial.itemName) != null) {
                    valid = false;
                    $rootScope.showErrorMessage("{0} Name already exists".format(vm.newMaterial.itemName));
                }
                else if (vm.newMaterial.units == null || vm.newMaterial.units == undefined || vm.newMaterial.units == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Material Units cannot be empty');
                }

                return valid;
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newMaterialAttributes != null && vm.newMaterialAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newMaterialAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newMaterialAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newMaterialAttributes, function (attribute) {
                        attribute.id.objectId = vm.material.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.newMaterialAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newMaterialAttributes = [];
                                                // loadObjectAttributeDefs();
                                                defered.resolve();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newMaterialAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newMaterialAttributes = [];
                                        // loadObjectAttributeDefs();
                                        defered.resolve();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                if (validate()) {
                    attributesValidate().then(
                        function (success) {
                            ItemService.createMaterialItem(vm.newMaterial).then(
                                function (data) {
                                    vm.material = data;
                                    vm.newMaterial.id = data.id;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            defered.resolve();
                                            $rootScope.hideSidePanel();
                                            $rootScope.$broadcast('app.material.all');
                                        }, function (erro) {
                                            defered.reject();
                                        });

                                }, function (error) {
                                    defered.reject();
                                    $rootScope.showErrorMessage("{0} Number already exists".format(vm.newMaterial.itemNumber));
                                }
                            )
                        }, function (error) {

                        });
                }
                return defered.promise;
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newMaterial.itemType = null;
                    vm.newMaterial.itemType = itemType;
                    autoNumber();
                }
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getMaterialAttributesWithHierarchy(vm.newMaterial.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMaterial.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                integerValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }
                        });
                    }
                );

            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MATERIAL', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function loadMaterials() {
                ItemService.getMaterials(pageable).then(
                    function (data) {
                        materials = data;
                        angular.forEach(materials.content, function (material) {
                            materialMap.put(material.itemName, material);
                        });
                    });
                loadObjectAttributeDefs();
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newMaterialAttributes.length > 0) {
                    angular.forEach(vm.newMaterialAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.material.id, vm.newMaterialAttributes).then(
                        function (data) {
                            var secCount = 0;
                            if (vm.propertyImageAttributes.length > 0) {
                                angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                        function (data) {
                                        });
                                    secCount++;
                                    if (secCount == vm.propertyImageAttributes.length) {
                                        defered.resolve();
                                    }
                                });
                            } else {
                                defered.resolve();
                            }
                        },
                        function (error) {
                            defered.reject();
                        });
                } else {
                    defered.resolve();
                }
                $rootScope.$broadcast('app.material.all');
                return defered.promise;
            }

            function autoNumber() {
                if (vm.newMaterial.itemType != null && vm.newMaterial.itemType.materialNumberSource != null) {
                    var source = vm.newMaterial.itemType.materialNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newMaterial.itemNumber = data;
                            loadAttributeDefs();

                        }
                    )
                }
            }

            function loadObjectAttributeDefs() {
                vm.newMaterialAttributes = [];
                ItemService.getAllTypeAttributes("MATERIAL").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMaterial.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            vm.newMaterialAttributes.push(att);
                        });
                    }
                );
            }

            function cancelItem() {
                vm.newMaterial = null;
                $state.go('app.proc.materials.all');
            }

            function addToListValue(attribute) {
                if (attribute.listValue == undefined) {
                    attribute.listValue = [];
                }

                attribute.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            $scope.$on('app.items.new', function loadCreateMethod() {
                anotherItem();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadMaterials();
                }
            })();
        }
    }
)
;