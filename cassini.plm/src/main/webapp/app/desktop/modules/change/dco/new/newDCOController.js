define(
    [
        'app/desktop/modules/change/change.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/qualityTypeService'
    ],
    function (module) {

        module.controller('NewDCOController', NewDCOController);

        function NewDCOController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, CommonService, DCOService,
                                  AutonumberService, ECOService, LoginService, WorkflowDefinitionService, ObjectTypeAttributeService, QualityTypeService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var titleValidation = parsed.html($translate.instant("TITLE_VALIDATION")).html();
            var workflowValidation = parsed.html($translate.instant("WORKFLOW_VALIDATION")).html();
            var reasonforChange = parsed.html($translate.instant("REASON_FOR_CHANGE_VALIDATION")).html();
            var selectChangeAnalyst = parsed.html($translate.instant("SELECT_CHANGE_ANALYST")).html();
            var dcoNumberValidation = parsed.html($translate.instant("DCO_NUMBER_VALIDATION")).html();
            var dcoTypeValidation = parsed.html($translate.instant("DCO_TYPE_VALIDATION")).html();
            var dcoCreatedMsg = parsed.html($translate.instant("DCO_CREATED_MSG")).html();
            $scope.selectWorkflowActivityTitle = $translate.instant("S_WORKFLOW_ACTIVITY");
            var selectWorkflowActivity = $translate.instant("SELECT_WORKFLOW_ACTIVITY");
            var selectRevisionCreationType = $translate.instant("SELECT_REVISION_CREATION_TYPE");
            $scope.enterReasonForChange = parsed.html($translate.instant("ENTER_REASON_FOR_CHANGE")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            vm.onSelectType = onSelectType;
            vm.newDCO = {
                id: null,
                dcoType: null,
                dcoNumber: null,
                title: null,
                description: null,
                reasonForChange: null,
                changeAnalyst: null,
                workflow: null,
                revisionCreationType: "WORKFLOW_START",
                workflowStatus: null,
                workflowDefinition: null,
                dcoOwnerObject: null,
                status: 'NONE',
                changeClass: null
            };

            function onSelectType(changeType) {
                if (changeType != null && changeType != undefined) {
                    vm.newDCO.dcoType = changeType;
                    vm.newDCO.changeClass = changeType;
                    vm.type = changeType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newDCO.dcoType != null && vm.newDCO.dcoType.autoNumberSource != null) {
                    var source = vm.newDCO.dcoType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newDCO.dcoNumber = data;
                            loadWorkflows();
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadWorkflows() {
                ECOService.getWorkflows(vm.newDCO.dcoType.id, 'CHANGES').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.changeAnalysts = [];
            function loadPersons() {
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                var groupName = preference.defaultValueName;
                var permission = "permission.dco.all";
                QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                    function (data) {
                        vm.changeAnalysts = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function create() {
                if (validate()) {
                    vm.newDCO.dcoType = vm.type.id;
                    vm.newDCO.changeAnalyst = vm.newDCO.dcoOwnerObject.id;
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
                    angular.forEach(vm.dcoRequiredProperties, function (reqatt) {
                        vm.dcoProperties.push(reqatt);
                    })
                    angular.forEach(vm.dcoProperties, function (attribute) {
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
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    var changeAttributeDto = {
                        plmdco: vm.newDCO,
                        changeAttributes: vm.attributes,
                        objectAttributes: vm.dcoProperties
                    }
                    DCOService.createDCOObject("DCO", changeAttributeDto).then(
                        function (data) {
                            vm.newDCO = data.plmdco;
                            saveCustomAttributes().then(
                                function (atts) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newDCO);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(dcoCreatedMsg);
                                            vm.newDCO = {
                                                id: null,
                                                dcoType: null,
                                                dcoNumber: null,
                                                title: null,
                                                description: null,
                                                reasonForChange: null
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
                if (vm.newDCO.dcoType == null || vm.newDCO.dcoType == undefined ||
                    vm.newDCO.dcoType == "") {
                    $rootScope.showWarningMessage(dcoTypeValidation);
                    valid = false;
                }
                else if (vm.newDCO.dcoNumber == null || vm.newDCO.dcoNumber == undefined ||
                    vm.newDCO.dcoNumber == "") {
                    $rootScope.showWarningMessage(dcoNumberValidation);
                    valid = false;
                }

                else if (vm.newDCO.title == null || vm.newDCO.title == undefined ||
                    vm.newDCO.title == "") {
                    $rootScope.showWarningMessage(titleValidation);
                    valid = false;
                }
                else if (vm.newDCO.reasonForChange == null || vm.newDCO.reasonForChange == undefined ||
                    vm.newDCO.reasonForChange == "") {
                    $rootScope.showWarningMessage(reasonforChange);
                    valid = false;
                }
                else if (vm.newDCO.dcoOwnerObject == null || vm.newDCO.dcoOwnerObject == undefined ||
                    vm.newDCO.dcoOwnerObject == "") {
                    $rootScope.showWarningMessage(selectChangeAnalyst);
                    valid = false;
                }
                else if (vm.newDCO.workflow == null || vm.newDCO.workflow == undefined ||
                    vm.newDCO.workflow == "") {
                    $rootScope.showWarningMessage(workflowValidation);
                    valid = false;
                } else if (vm.newDCO.revisionCreationType == null || vm.newDCO.revisionCreationType == undefined ||
                    vm.newDCO.revisionCreationType == "") {
                    $rootScope.showWarningMessage(selectRevisionCreationType);
                    valid = false;
                } else if (vm.newDCO.revisionCreationType == null || vm.newDCO.revisionCreationType == undefined ||
                    vm.newDCO.revisionCreationType == "") {
                    $rootScope.showWarningMessage(selectRevisionCreationType);
                    valid = false;
                } else if (vm.newDCO.revisionCreationType == "ACTIVITY_COMPLETION" && (vm.newDCO.workflowStatus == null || vm.newDCO.workflowStatus == undefined ||
                    vm.newDCO.workflowStatus == "")) {
                    $rootScope.showWarningMessage(selectWorkflowActivity);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.dcoRequiredProperties.length > 0 && !validateCustomAttributes()) {
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
                angular.forEach(vm.dcoRequiredProperties, function (attribute) {
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
                            DCOService.updateAttributeImageValue("DCOTYPE", vm.newDCO.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeAttachmentValues("DCOTYPE", vm.newDCO.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeImageValue("DCO", vm.newDCO.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
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
                            DCOService.updateAttributeAttachmentValues("DCO", vm.newDCO.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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
                ECOService.getEcoAttributesWithHierarchy(vm.type.id).then(
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

            function loadDcoCustomProperties() {
                vm.dcoProperties = [];
                vm.dcoRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CHANGE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newDCO.id,
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
                            if (attribute.required == false) {
                                vm.dcoProperties.push(att);
                            } else {
                                vm.dcoRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.selectRevisionCreationRule = selectRevisionCreationRule;
            function selectRevisionCreationRule(value) {
                if (value == 'workflowStart') {
                    vm.newDCO.revisionCreationType = "WORKFLOW_START";
                    vm.newDCO.workflowStatus = null;
                }
                if (value == 'activityCompletion') {
                    vm.newDCO.workflowStatus = null;
                    vm.newDCO.revisionCreationType = "ACTIVITY_COMPLETION";
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    getNormalWorkflowStatuses();
                }
                $scope.$evalAsync();

            }

            vm.workflowStatuses = [];
            function getNormalWorkflowStatuses() {
                WorkflowDefinitionService.getNormalWorkflowStatuses(vm.newDCO.workflow).then(
                    function (data) {
                        vm.workflowStatuses = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPersons();
                loadDcoCustomProperties();
                $rootScope.$on('app.dco.new', create);
                //}
            })();
        }
    }
)
;