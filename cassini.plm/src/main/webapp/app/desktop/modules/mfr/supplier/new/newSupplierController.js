define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/mfr/supplier/directive/supplierDirective',
        'app/shared/services/core/supplierService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewSupplierController', NewSupplierController);

        function NewSupplierController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, LoginService,
                                       ObjectTypeAttributeService, AutonumberService, ItemTypeService, ClassificationService, SupplierService, AttributeAttachmentService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.persons = [];
            vm.selectedSupplierType = null;
            vm.attributes = [];
            vm.validattributes = [];
            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var supplierManagerValidation = parsed.html($translate.instant("SUPPLIER_MANAGER_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();
            var successMsg = parsed.html($translate.instant("SUPPLIER_CREATED_SUCCESS_MESSAGE")).html();
            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();

            vm.newSupplier = {
                id: null,
                supplierType: null,
                name: null,
                number: null,
                description: null,
                address: null,
                city: null,
                country: null,
                postalCode: null,
                phoneNumber: null,
                mobileNumber: null,
                faxNumber: null,
                email: null,
                webSite: null,
                supplierAttributes: [],
                customAttributes: []
            };
            vm.onSelectType = onSelectType;
            function onSelectType(supplierType) {
                if (supplierType != null && supplierType != undefined) {
                    vm.selectedSupplierType = supplierType;
                    vm.newSupplier.supplierType = supplierType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newSupplier.supplierType != null && vm.newSupplier.supplierType.autoNumberSource != null) {
                    var source = vm.newSupplier.supplierType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newSupplier.number = data;
                            loadSupplierTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            /*
             * Create Supplier
             * */

            function createNewSupplier() {
                create().then(function () {
                    vm.newSupplier = {
                        id: null,
                        supplierType: null,
                        name: null,
                        description: null,
                        address: null,
                        city: null,
                        country: null,
                        postalCode: null,
                        phoneNumber: null,
                        mobileNumber: null,
                        faxNumber: null,
                        email: null,
                        webSite: null
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })


            }

            var enterValidEmail = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();

            function validateEmail(email) {
                var valid = true;
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            /*
             * Validate Supplier Object
             * */
            function validate() {
                var valid = true;
                if (vm.newSupplier.supplierType == null || vm.newSupplier.supplierType == undefined ||
                    vm.newSupplier.supplierType == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newSupplier.name == null || vm.newSupplier.name == undefined ||
                    vm.newSupplier.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }
                else if (vm.newSupplier.email == null || vm.newSupplier.email == "" || vm.newSupplier.email == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation);
                }
                else if (!validateEmail(vm.newSupplier.email)) {
                    valid = false;
                    $rootScope.showWarningMessage(enterValidEmail);
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

            /*
             * Create Supplier
             *
             * */


            function create() {
                var dfd = $q.defer();
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
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
                    vm.newSupplier.supplierAttributes = vm.attributes;
                    vm.newSupplier.objectAttributes = vm.customAttributes;
                    SupplierService.createSupplier(vm.newSupplier).then(
                        function (data) {
                            vm.newSupplier = data;
                            saveCustomAttributes().then(
                                function (data) {
                                    saveAttributes().then(
                                        function (att) {
                                            vm.newSupplier = {
                                                id: null,
                                                supplierType: null,
                                                name: null,
                                                description: null,
                                                address: null,
                                                city: null,
                                                country: null,
                                                postalCode: null,
                                                phoneNumber: null,
                                                mobileNumber: null,
                                                faxNumber: null,
                                                email: null,
                                                webSite: null
                                            };
                                            vm.selectedSupplierType = null;
                                            vm.attributes = [];
                                            vm.customAttributes = [];
                                            $scope.callback();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(successMsg);
                                            loadSupplerAttributeDefs();
                                        }
                                    )
                                })
                        }
                        ,
                        function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("SUPPLIERTYPE", vm.newSupplier.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("SUPPLIERTYPE", vm.newSupplier.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.customImageAttributes.length > 0 || vm.customAttachmentAttributes.length > 0) {
                    angular.forEach(vm.customImageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("MFRSUPPLIER", vm.newSupplier.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("MFRSUPPLIER", vm.newSupplier.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadSupplierTypeAttributes() {
                vm.attributes = [];
                SupplierService.getSupplierObjectAttributesWithHierarchy(vm.newSupplier.supplierType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSupplier.id,
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
                            if (attribute.dataType == "LIST") {
                                att.listValue = attribute.defaultListValue;
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

            function loadSupplerAttributeDefs() {
                vm.customAttributes = [];
                ItemTypeService.getAllTypeAttributes("MFRSUPPLIER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSupplier.id,
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
                            vm.customAttributes.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadSupplerAttributeDefs();
                $rootScope.$on('app.supplier.new', createNewSupplier);
                //}
            })();
        }
    }
)
;