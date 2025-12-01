define(
    [
        'app/desktop/modules/req/req.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/pmObjectTypeService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/dcoService',
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
        module.controller('NewRequirementController', NewRequirementController);

        function NewRequirementController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application, $translate, ClassificationService,
                                          PMObjectTypeService, ReqDocumentService, QualityTypeService, RequirementService, CommentsService, DCOService, AutonumberService, LoginService, ObjectTypeAttributeService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var reqNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var reqTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var reqNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqDescriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var reqPersonValidation = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var reqCreatedMsg = parsed.html($translate.instant("REQUIREMENT_CREATE_MSG")).html();
            $scope.selectPerson = parsed.html($translate.instant("SELECT_PERSONS")).html();

            vm.parent = $scope.data.parent;
            vm.onSelectType = onSelectType;
            vm.newReqVersion = {
                id: null,
                document: null,
                assignedTo: null,
                priority: 'LOW',
                plannedFinishDate: null,
                workflowDefinition: null,
                master: {
                    id: null,
                    type: null,
                    name: null,
                    number: null,
                    description: null,
                    document: null,
                    parent: null,
                    priority: 'LOW'
                }
            };

            vm.prioritys = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];

            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newReqVersion.master.type = objectType;
                    vm.type = objectType;
                    vm.newReqVersion.workflowDefinition = null;
                    autoNumber();
                    loadWorkflows();
                }
            }

            function autoNumber() {
                if (vm.newReqVersion.master.type != null && vm.newReqVersion.master.type.autoNumberSource != null) {
                    var source = vm.newReqVersion.master.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newReqVersion.master.number = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newReqVersion.type = vm.type;
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
                    angular.forEach(vm.requirementAttributes, function (attribute) {
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
                    vm.newReqVersion.requirementObjectAttributes = vm.attributes;
                    vm.newReqVersion.objectAttributes = vm.requirementAttributes;
                    vm.newReqVersion.master.requirementDocumentRevision = $scope.data.reqDoc;
                    vm.newReqVersion.requirementDocumentRevision = $scope.data.reqDoc;
                    if (vm.newReqVersion.workflowDefinition != null) {
                        vm.newReqVersion.workflowDefId = vm.newReqVersion.workflowDefinition;
                    }
                    if (vm.parent != null) {
                        vm.newReqVersion.master.parent = vm.parent.master.id;
                        vm.newReqVersion.parent = vm.parent.id;
                    }
                    RequirementService.createRequirement(vm.newReqVersion).then(
                        function (data) {
                            vm.newReqVersion = data;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newReqVersion);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(reqCreatedMsg);
                                            vm.newReqVersion = {
                                                id: null,
                                                assignedTo: null,
                                                master: {
                                                    id: null,
                                                    type: null,
                                                    name: null,
                                                    number: null,
                                                    description: null,
                                                    document: null,
                                                    parent: null,
                                                    priority: 'LOW',
                                                    plannedFinishDate: null
                                                }
                                            };
                                        }
                                    )
                                })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newReqVersion.master.type == null || vm.newReqVersion.master.type == undefined ||
                    vm.newReqVersion.master.type == "") {
                    $rootScope.showErrorMessage(reqTypeValidation);
                    valid = false;
                }
                else if (vm.newReqVersion.master.name == null || vm.newReqVersion.master.name == undefined ||
                    vm.newReqVersion.master.name == "") {
                    $rootScope.showErrorMessage(reqNameValidation);
                    valid = false;
                }
                else if (vm.newReqVersion.master.description == null || vm.newReqVersion.master.description == undefined ||
                    vm.newReqVersion.master.description == "") {
                    $rootScope.showErrorMessage(reqDescriptionValidation);
                    valid = false;
                }
                else if (vm.newReqVersion.master.number == null || vm.newReqVersion.master.number == undefined ||
                    vm.newReqVersion.master.number == "") {
                    $rootScope.showErrorMessage(reqNumberValidation);
                    valid = false;
                }
                else if (vm.newReqVersion.assignedTo == null || vm.newReqVersion.assignedTo == undefined ||
                    vm.newReqVersion.assignedTo == "") {
                    $rootScope.showErrorMessage(reqPersonValidation);
                    valid = false;
                }
                else if (vm.attributes.length > 0 && !validateAttributes()) {
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

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("REQUIREMENT", vm.newReqVersion.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeAttachmentValues("REQUIREMENT", vm.newReqVersion.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeImageValue("REQUIREMENT", vm.newReqVersion.master.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeAttachmentValues("REQUIREMENT", vm.newReqVersion.master.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadAttributeDefs() {
                vm.attributes = [];
                PMObjectTypeService.getReqObjectAttributesWithHierarchy(vm.type.id).then(
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
                        $rootScope.hideBusyIndicator();
                    }
                )
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
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /*------------------ To get Custom Properties Names  -------------------*/

            function loadObjectAttributeDefs() {
                vm.requirementAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("REQUIREMENT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newReqVersion.master.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mlistValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                measurementUnit: null,
                                attachmentValues: []
                            };
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
                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.requirementAttributes.push(att);

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkflows() {
                vm.workflows = [];
                RequirementService.getReqWorkflows(vm.type.id, 'REQUIREMENT').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadPersons();
                loadObjectAttributeDefs();
                $rootScope.$on('app.requirement.new', create);
            })();
        }
    }
);