define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/pgcObjectTypeDirective',
        'app/shared/services/core/pgcObjectTypeService',
        'app/shared/services/core/specificationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewSpecificationController', NewSpecificationController);

        function NewSpecificationController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService,
                                            AutonumberService, PGCObjectTypeService, SpecificationService, ObjectTypeAttributeService, ClassificationService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var specNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var specTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var specNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var specCreatedMsg = parsed.html($translate.instant("SPECIFICATION_CREATE_MSG")).html();

            vm.onSelectType = onSelectType;
            vm.create = create;
            vm.newSpecification = {
                id: null,
                type: null,
                name: null,
                number: null,
                description: null,
                active: true
            };


            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newSpecification.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newSpecification.type != null && vm.newSpecification.type.autoNumberSource != null) {
                    var source = vm.newSpecification.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newSpecification.number = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newSpecification.type = vm.type;
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

                    vm.newSpecification.pgcObjectAttributes = vm.attributes;
                    vm.newSpecification.objectAttributes = vm.customAttributes;
                    SpecificationService.createSpecification(vm.newSpecification).then(
                        function (data) {
                            vm.newSpecification = data;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            vm.type = null;
                                            vm.attributes = [];
                                            vm.customAttributes = [];
                                            $scope.callback(vm.newSpecification);
                                            vm.newSpecification = {
                                                id: null,
                                                type: null,
                                                number: null,
                                                name: null,
                                                description: null,
                                                active: true
                                            };
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(specCreatedMsg);
                                            loadCustomAttributeProperties();
                                        }
                                    )
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function createSpecificationObject() {
                create().then(
                    function () {
                        vm.newSpecification = {
                            id: null,
                            type: null,
                            number: null,
                            name: null,
                            description: null,
                            active: true
                        };
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function validate() {
                var valid = true;
                if (vm.newSpecification.type == null || vm.newSpecification.type == undefined ||
                    vm.newSpecification.type == "") {
                    $rootScope.showErrorMessage(specTypeValidation);
                    valid = false;
                }
                else if (vm.newSpecification.name == null || vm.newSpecification.name == undefined ||
                    vm.newSpecification.name == "") {
                    $rootScope.showErrorMessage(specNameValidation);
                    valid = false;
                }
                else if (vm.newSpecification.number == null || vm.newSpecification.number == undefined ||
                    vm.newSpecification.number == "") {
                    $rootScope.showErrorMessage(specNumberValidation);
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

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("PGCOBJECTTYPE", vm.newSpecification.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeAttachmentValues("PGCOBJECTTYPE", vm.newSpecification.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeImageValue("PGCOBJECT", vm.newSpecification.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("PGCOBJECT", vm.newSpecification.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadAttributeDefs() {
                vm.attributes = [];
                PGCObjectTypeService.getPgcObjectAttributesWithHierarchy(vm.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.type.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
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
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            vm.attributes.push(att);
                        });
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
                                    objectId: vm.newSpecification.id,
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
                loadCustomAttributeProperties();
                $rootScope.$on('app.specification.new', createSpecificationObject);
            })();
        }
    }
)
;
