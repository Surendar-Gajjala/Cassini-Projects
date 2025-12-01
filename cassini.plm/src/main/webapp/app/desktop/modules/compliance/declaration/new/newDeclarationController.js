define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/directives/pgcObjectTypeDirective',
        'app/shared/services/core/declarationService',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/pgcObjectTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/classificationService'

    ],
    function (module) {

        module.controller('NewDeclarationController', NewDeclarationController);

        function NewDeclarationController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, DeclarationService, LoginService, ClassificationService,
                                          SupplierService, ObjectTypeAttributeService, QualityTypeService, AutonumberService, PGCObjectTypeService, AttributeAttachmentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");


            vm.selectedDeclarationType = null;
            vm.attributes = [];
            vm.validattributes = [];


            vm.newDeclaration = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                supplier: null,
                contact: null
            };


            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            function onSelectType(declarationType) {
                if (declarationType != null && declarationType != undefined) {
                    vm.selectedDeclarationType = declarationType;
                    vm.newDeclaration.type = declarationType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedDeclarationType != null) {
                    var source = vm.selectedDeclarationType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newDeclaration.number = data;
                            loadDeclarationTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            $scope.selectSupplier = parsed.html($translate.instant("SELECT_SUPPLIER")).html();
            $scope.selectContact = parsed.html($translate.instant("SELECT_CONTACT")).html();
            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();
            var supplierValidation = parsed.html($translate.instant("SELECT_SUPPLIER")).html();
            var contactValidation = parsed.html($translate.instant("SELECT_CONTACT")).html();
            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();


            function validate() {
                var valid = true;
                if (vm.newDeclaration.type == null || vm.newDeclaration.type == undefined ||
                    vm.newDeclaration.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newDeclaration.number == null || vm.newDeclaration.number == undefined ||
                    vm.newDeclaration.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newDeclaration.name == null || vm.newDeclaration.name == undefined ||
                    vm.newDeclaration.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }
                else if (vm.newDeclaration.supplier == null || vm.newDeclaration.supplier == undefined ||
                    vm.newDeclaration.supplier == "") {
                    $rootScope.showWarningMessage(supplierValidation);
                    valid = false;
                }
                else if (vm.newDeclaration.contact == null || vm.newDeclaration.contact == undefined ||
                    vm.newDeclaration.contact == "") {
                    $rootScope.showWarningMessage(contactValidation);
                    valid = false;
                }
                else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.customAttributes.length > 0 && !validateCustomAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            var insertedSuccefully = parsed.html($translate.instant("DECLARATION_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate()) {
                    vm.imageAttributes = [];
                    vm.attachmentAttributes = [];
                    vm.images = new Hashtable();
                    vm.attachments = new Hashtable();
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.attachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.attachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });

                    vm.customImageAttributes = [];
                    vm.customAttachmentAttributes = [];
                    vm.customImages = new Hashtable();
                    vm.customAttachments = new Hashtable();
                    angular.forEach(vm.customAttributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.customImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.customImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.customAttachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.customAttachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });

                    vm.newDeclaration.pgcObjectAttributes = vm.attributes;
                    vm.newDeclaration.objectAttributes = vm.customAttributes;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    DeclarationService.createDeclaration(vm.newDeclaration).then(
                        function (data) {
                            vm.newDeclaration = data;
                            saveCustomAttributes().then(
                                function (att) {
                                    saveAttributes().then(
                                        function (att) {
                                            vm.selectedDeclarationType = null;
                                            vm.attributes = [];
                                            vm.customAttributes = [];
                                            $scope.callback(vm.newDeclaration);
                                            vm.newDeclaration = {
                                                id: null,
                                                type: null,
                                                number: null,
                                                name: null,
                                                description: null,
                                                supplier: null,
                                                contact: null
                                            };
                                            loadCustomAttributeProperties();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(insertedSuccefully);
                                        }
                                    )
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }


            function createNewDeclaration() {
                create().then(
                    function () {
                        vm.newDeclaration = {
                            id: null,
                            type: null,
                            number: null,
                            name: null,
                            description: null,
                            supplier: null,
                            contact: null
                        };
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("PGCOBJECTTYPE", vm.newDeclaration.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    );
                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("PGCOBJECTTYPE", vm.newDeclaration.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }


            function loadDeclarationTypeAttributes() {
                vm.attributes = [];
                PGCObjectTypeService.getPgcObjectAttributesWithHierarchy(vm.newDeclaration.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newDeclaration.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                integerValue: null,
                                stringValue: null,
                                doubleValue: null,
                                imageValue: null,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            vm.attributes.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.attributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function validateCustomAttributes() {
                var valid = true;
                angular.forEach(vm.customAttributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.customImageAttributes.length > 0 || vm.customAttachmentAttributes.length > 0) {
                    angular.forEach(vm.customImageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("PGCOBJECT", vm.newDeclaration.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("PGCOBJECT", vm.newDeclaration.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadSuppliers() {
                SupplierService.getApprovedSuppliers().then(
                    function (data) {
                        vm.suppliers = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.loadContacts = loadContacts;
            function loadContacts(id) {
                SupplierService.getSupplierActiveContacts(id).then(
                    function (data) {
                        vm.contacts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCustomAttributeProperties() {
                vm.customAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("PGCOBJECT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newDeclaration.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            vm.customAttributes.push(att);

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $rootScope.hideBusyIndicator();
                //loadPersons();
                loadSuppliers();
                loadCustomAttributeProperties();
                $rootScope.$on('app.declaration.new', createNewDeclaration);
                //}
            })();
        }
    }
);