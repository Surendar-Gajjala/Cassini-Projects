define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/supplier/supplierService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('NewSupplierController', NewSupplierController);

        function NewSupplierController($scope, $rootScope, $timeout, $interval, $state, $stateParams, SupplierService, CommonService,
                                       ItemService, ObjectAttributeService, ObjectTypeAttributeService, AttributeAttachmentService, $q) {

            $rootScope.viewInfo.icon = "fa fa flaticon-businessman276";
            $rootScope.viewInfo.title = "Suppliers";

            var vm = this;
            vm.create = create;
            var supplierMap = $scope.data.supplierMap;

            vm.requiredAttributes = [];
            vm.attributes = [];

            vm.persons = [];

            vm.supplier = {
                name: null,
                description: null,
                contactPerson: null,
                contactPhone: null
            };

            function validate() {
                vm.valid = true;
                if (vm.supplier.name === null || vm.supplier.name == undefined || vm.supplier.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Supplier Name cannot be empty");
                } else if (supplierMap.get(vm.supplier.name) != null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("{0} name already exists".format(vm.supplier.name));
                }
                return vm.valid;
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
            };

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'SUPPLIER', attachmentFile).then(
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
                if (vm.newSupplierAttributes.length > 0) {
                    angular.forEach(vm.newSupplierAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.supplier.id, vm.newSupplierAttributes).then(
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
                $rootScope.hideSidePanel('right');
                $rootScope.$broadcast("app.store.supplier.all");
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
                if (vm.newSupplierAttributes != null && vm.newSupplierAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newSupplierAttributes);
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
                if (vm.newSupplierAttributes.length == 0) {
                    $rootScope.hideBusyIndicator();
                    $rootScope.supplierId = vm.supplier.id;
                    //$state.go('app.store.supplierDetails', {supplierId: vm.supplier.id});
                    $rootScope.showSuccessMessage("Supplier (" + vm.supplier.name + ") created successfully");
                    $rootScope.hideSidePanel('right');
                    $rootScope.$broadcast("app.store.supplier.all");
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newSupplierAttributes, function (attribute) {
                        attribute.id.objectId = vm.supplier.id;
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
                                    if (attrCount == vm.newSupplierAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newSupplierAttributes = [];
                                                loadObjectAttributeDefs();
                                                $rootScope.showSuccessMessage("Supplier (" + vm.supplier.name + ") created successfully");
                                                $rootScope.hideSidePanel('right');
                                                defered.resolve();
                                                $rootScope.$broadcast("app.store.supplier.all");
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newSupplierAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newSupplierAttributes = [];
                                        loadObjectAttributeDefs();
                                        $rootScope.showSuccessMessage("Supplier (" + vm.supplier.name + ") created successfully");
                                        $rootScope.hideSidePanel('right');
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

            function create() {
                if (validate()) {
                    attributesValidate().then(
                        function (success) {
                            SupplierService.createSupplier(vm.supplier).then(
                                function (data) {
                                    if (validate()) {
                                        vm.supplier = data;
                                        $rootScope.supplierId = vm.supplier.id;
                                        supplierMap.put(vm.supplier.name, vm.supplier);
                                        intializationForAttributesSave()
                                    }
                                }, function (error) {

                                }
                            )
                        }, function (error) {

                        });
                }
            }

            function loadObjectAttributeDefs() {
                vm.newSupplierAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("SUPPLIER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.supplier.id,
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
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newSupplierAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            (function () {
                loadObjectAttributeDefs();
                $scope.$on("app.store.supplier.new", function () {
                    create();
                });
            })();
        }
    }
);