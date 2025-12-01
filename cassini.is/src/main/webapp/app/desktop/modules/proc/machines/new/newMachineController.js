define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/proc/machines/directive/machineDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('NewMachineController', NewMachineController);

        function NewMachineController($scope, $q, $rootScope, $timeout, $state, ItemService, $stateParams, AttributeAttachmentService, ObjectAttributeService, $cookies,
                                      AutonumberService, ClassificationService, ItemTypeService) {

            var vm = this;
            vm.autoNumber = autoNumber;

            vm.newMachine = {
                itemType: null,
                itemNumber: null,
                itemName: null,
                description: null,
                units: "Each"
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
                }
            };

            vm.attributes = [];
            vm.attribute = null;
            vm.machine = null;

            vm.onSelectType = onSelectType;
            vm.createItem = createItem;
            vm.cancelItem = cancelItem;
            vm.autoNumber = autoNumber;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            var machines = [];
            var machineMap = new Hashtable();

            function anotherItem() {
                create().then(
                    function () {
                        vm.newMachine = {
                            itemType: null,
                            itemNumber: null,
                            itemName: null,
                            description: null,
                            units: "Each"
                        };
                        $rootScope.hideBusyIndicator();
                        $rootScope.hideSidePanel();
                        $rootScope.showSuccessMessage("Machine (" + vm.machine.itemNumber + ") created successfully");
                        //$scope.callback();
                        $rootScope.$broadcast('app.machines.all');
                    }
                );
            }

            function createItem() {
                create().then(
                    function () {
                        $rootScope.hideSidePanel();
                        $rootScope.$broadcast('app.machines.all');
                        //$scope.callback();
                    }
                );
            }

            function validate() {
                var valid = true;

                if (vm.newMachine.itemType == null || vm.newMachine.itemType == undefined || vm.newMachine.itemType == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Type cannot be empty');
                }
                else if (vm.newMachine.itemNumber == null || vm.newMachine.itemNumber == undefined || vm.newMachine.itemNumber == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Number cannot be empty');
                } else if (vm.newMachine.itemName == null || vm.newMachine.itemName == undefined || vm.newMachine.itemName == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Name cannot be empty');
                }
                else if (vm.newMachine.units == null || vm.newMachine.units == undefined || vm.newMachine.units == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Machine Units cannot be empty');
                }
                else if (machineMap.get(vm.newMachine.itemName) != null) {
                    valid = false;
                    $rootScope.showErrorMessage("{0} Name already exists".format(vm.newMachine.itemName));
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

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newMachineAttributes.length == 0) {
                    defered.resolve();
                } else {
                    angular.forEach(vm.newMachineAttributes, function (attribute) {
                        attribute.id.objectId = vm.machine.id;
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
                                    if (attrCount == vm.newMachineAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newMachineAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newMachineAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newMachineAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
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
                if (vm.newMachineAttributes != null && vm.newMachineAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newMachineAttributes);
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

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                if (validate()) {
                    attributesValidate().then(
                        function (success) {
                            ItemService.createMachineItem(vm.newMachine).then(
                                function (data) {
                                    vm.machine = data;
                                    vm.newMachine.id = data.id;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            defered.resolve();
                                        }, function (error) {
                                            defered.reject();
                                        });
                                }, function (error) {
                                    $rootScope.showErrorMessage("{0} Number already exists".format(vm.newMachine.itemNumber));
                                    defered.reject();
                                }
                            )
                        }, function (error) {
                            defered.reject();
                        });
                }
                return defered.promise;
            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MACHINE', attachmentFile).then(
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

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newMachineAttributes.length > 0) {
                    angular.forEach(vm.newMachineAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.machine.id, vm.newMachineAttributes).then(
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
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newMachine.itemType = null;
                    vm.newMachine.itemType = itemType;
                    autoNumber();
                }
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getMachineAttributesWithHierarchy(vm.newMachine.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMachine.id,
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
                )
            }

            function autoNumber() {
                if (vm.newMachine.itemType != null && vm.newMachine.itemType.machineNumberSource != null) {
                    var source = vm.newMachine.itemType.machineNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newMachine.itemNumber = data;
                            loadAttributeDefs();
                        }
                    )
                }
            }

            function loadObjectAttributeDefs() {
                vm.newMachineAttributes = [];
                ItemService.getAllTypeAttributes("MACHINE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMachine.id,
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
                            vm.newMachineAttributes.push(att);
                        });
                    });
            }

            function cancelItem() {
                vm.newMachine = null;
                $state.go('app.proc.machines.all');
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

            function loadMachines() {
                ItemService.getMachines(vm.pageable).then(
                    function (data) {
                        machines = data;
                        angular.forEach(machines.content, function (machine) {
                            machineMap.put(machine.itemName, machine);
                        })
                    });
                loadObjectAttributeDefs();
            }

            $scope.$on('app.machines.new', function () {
                anotherItem();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadMachines();
                }
            })();
        }
    }
)
;