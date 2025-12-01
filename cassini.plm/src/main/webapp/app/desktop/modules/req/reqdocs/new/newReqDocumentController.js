define(
    [
        'app/desktop/modules/req/req.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/pmObjectTypeService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/reqDocTemplateService',
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

        module.controller('NewReqDocumentController', NewReqDocumentController);

        function NewReqDocumentController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService, LoginService, DCOService,
                                          AutonumberService, PMObjectTypeService, ReqDocumentService, QualityTypeService, ReqDocTemplateService, ObjectTypeAttributeService, ClassificationService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var reqNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var reqTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var reqNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var reqPersonValidation = parsed.html($translate.instant("REQ_OWNER_VALIDATION")).html();
            var reqCreatedMsg = parsed.html($translate.instant("REQ_DOCUMENT_CREATED_MSG")).html();
            $scope.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE")).html();

            $scope.personDetails = $rootScope.loginPersonDetails;

            vm.onSelectType = onSelectType;

            vm.newReqDocument = {
                id: null,
                type: null,
                name: null,
                number: null,
                description: null,
                template: null,
                person: null,
                personObject: $scope.personDetails.person,
                documentReviewer: false,
                requirementReviewer: false,
                workflowDefinition: null
            };


            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.newReqDocument.type = objectType;
                    vm.type = objectType;
                    vm.newReqDocument.workflowDefinition = null;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newReqDocument.type != null && vm.newReqDocument.type.autoNumberSource != null) {
                    var source = vm.newReqDocument.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newReqDocument.number = data;
                            loadWorkflows();
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
                    vm.newReqDocument.type = vm.type;
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
                    angular.forEach(vm.reqDocAttributes, function (attribute) {
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
                    vm.newReqDocument.requirementObjectAttributes = vm.attributes;
                    vm.newReqDocument.objectAttributes = vm.reqDocAttributes;
                    vm.newReqDocument.person = vm.newReqDocument.personObject.id;
                    if (vm.newReqDocument.workflowDefinition != null) {
                        vm.newReqDocument.workflowDefId = vm.newReqDocument.workflowDefinition;
                    }
                    ReqDocumentService.createReqDocument(vm.newReqDocument).then(
                        function (data) {
                            vm.newReqDocument = data;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newReqDocument);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(reqCreatedMsg);
                                            vm.newReqDocument = {
                                                id: null,
                                                type: null,
                                                number: null,
                                                name: null,
                                                description: null,
                                                person: null,
                                                documentReviewer: false,
                                                requirementReviewer: false
                                            };
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

            function validate() {
                var valid = true;
                if (vm.newReqDocument.type == null || vm.newReqDocument.type == undefined ||
                    vm.newReqDocument.type == "") {
                    $rootScope.showErrorMessage(reqTypeValidation);
                    valid = false;
                }
                else if (vm.newReqDocument.name == null || vm.newReqDocument.name == undefined ||
                    vm.newReqDocument.name == "") {
                    $rootScope.showErrorMessage(reqNameValidation);
                    valid = false;
                }
                else if (vm.newReqDocument.number == null || vm.newReqDocument.number == undefined ||
                    vm.newReqDocument.number == "") {
                    $rootScope.showErrorMessage(reqNumberValidation);
                    valid = false;
                }
                else if (vm.newReqDocument.personObject == null || vm.newReqDocument.personObject == undefined ||
                    vm.newReqDocument.personObject == "") {
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
                            ClassificationService.updateAttributeImageValue("REQUIREMENTDOCUMENT", vm.newReqDocument.latestRevision, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
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
                            ClassificationService.updateAttributeAttachmentValues("REQUIREMENTDOCUMENT", vm.newReqDocument.latestRevision, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeImageValue("REQUIREMENTDOCUMENT", vm.newReqDocument.master.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeAttachmentValues("REQUIREMENTDOCUMENT", vm.newReqDocument.master.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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

            function loadReqDocumentTemplates() {
                ReqDocTemplateService.getAllReqDocumentTemplates().then(
                    function (data) {
                        vm.templates = data;
                    }
                )
            }


            /*------------------ To get Custom Properties Names  -------------------*/

            function loadObjectAttributeDefs() {
                vm.reqDocAttributes = [];
                vm.reqReqProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("REQUIREMENTDOCUMENT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newReqDocument.id,
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
                            vm.reqDocAttributes.push(att);

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkflows() {
                vm.workflows = [];
                ReqDocumentService.getReqDocWorkflows(vm.type.id, 'REQUIREMENT DOCUMENTS').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            (function () {
                loadPersons();
                loadReqDocumentTemplates();
                loadObjectAttributeDefs();
                $rootScope.$on('app.req.doc.new', create);
            })();
        }
    }
)
;