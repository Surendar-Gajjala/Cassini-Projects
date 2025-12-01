define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/qualityTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {

        module.controller('NewMCOController', NewMCOController);

        function NewMCOController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, AutonumberService,
                                  LoginService, ObjectTypeAttributeService, ECOService, DCOService, QcrService, QualityTypeService, WorkflowDefinitionService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var mcoCreated = parsed.html($translate.instant("MCO_CREATED_MSG")).html();
            var enterTitle = $translate.instant("ENTER_TITLE");
            var selectWorkflow = $translate.instant("WORKFLOW_SELECT_VALID");
            var selectChangeAnalyst = $translate.instant("SELECT_CHANGE_ANALYST");
            var selectMCOType = $translate.instant("SELECT_CHANGE_TYPE");
            var enterMcoNumber = $translate.instant("ENTER_MCO_NUMBER");
            $scope.mcoType = $scope.data.mcoType;
            $scope.enterReasonForChange = parsed.html($translate.instant("ENTER_REASON_FOR_CHANGE")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW_TITLE")).html();
            var selectRevisionCreationType = $translate.instant("SELECT_REVISION_CREATION_TYPE");
            $scope.selectWorkflowActivityTitle = $translate.instant("S_WORKFLOW_ACTIVITY");
            var selectWorkflowActivity = $translate.instant("SELECT_WORKFLOW_ACTIVITY");

            vm.onSelectType = onSelectType;
            vm.newMco = {
                id: null,
                mcoType: null,
                mcoNumber: null,
                qcr: null,
                title: null,
                description: null,
                reasonForChange: null,
                status: "NONE",
                changeAnalyst: null,
                workflow: null,
                changeClass: null,
                revisionCreationType: "WORKFLOW_START",
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newMco.mcoType = itemType;
                    vm.newMco.changeClass = itemType
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newMco.mcoType != null && vm.newMco.mcoType.autoNumberSource != null) {
                    var source = vm.newMco.mcoType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newMco.mcoNumber = data;
                            loadAttributeDefs();
                            loadWorkflows();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadQCRs() {
                QcrService.getReleasedByQcrFor("NCR").then(
                    function (data) {
                        vm.qcrs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkflows() {
                ECOService.getWorkflows(vm.newMco.mcoType.id, 'CHANGES').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                ECOService.getEcoAttributesWithHierarchy(vm.newMco.mcoType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMco.id,
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

            function create() {
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
                    angular.forEach(vm.mcoProperties, function (attribute) {
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
                    vm.newMco.type = $scope.mcoType;
                    var changeAttributeDto = {
                        mco: vm.newMco,
                        changeAttributes: vm.attributes,
                        objectAttributes: vm.mcoProperties
                    }
                    if ($scope.mcoType == 'ITEMMCO') changeAttributeDto.itemMco = vm.newMco;
                    if ($scope.mcoType == 'OEMPARTMCO') changeAttributeDto.manufacturerMCO = vm.newMco;
                    DCOService.createDCOObject('MCO', changeAttributeDto).then(
                        function (data) {
                            vm.newMco = data.mco;
                            saveCustomAttributes().then(
                                function (attr) {
                                    saveAttributes().then(
                                        function (attributes) {
                                            $scope.callback(vm.newMco);
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(mcoCreated);
                                            vm.newMco = {
                                                id: null,
                                                mcoType: null,
                                                mcoNumber: null,
                                                title: null,
                                                description: null,
                                                reasonForChange: null,
                                                status: "NONE",
                                                changeAnalyst: null,
                                                workflow: null
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
                if (vm.newMco.mcoType == null || vm.newMco.mcoType == undefined || vm.newMco.mcoType == "") {
                    $rootScope.showWarningMessage(selectMCOType);
                    valid = false;
                } else if (vm.newMco.mcoNumber == null || vm.newMco.mcoNumber == undefined || vm.newMco.mcoNumber == "") {
                    $rootScope.showWarningMessage(enterMcoNumber);
                    valid = false;
                } else if (vm.newMco.title == null || vm.newMco.title == undefined || vm.newMco.title == "") {
                    $rootScope.showWarningMessage(enterTitle);
                    valid = false;
                } else if (vm.newMco.changeAnalyst == null || vm.newMco.changeAnalyst == "" || vm.newMco.changeAnalyst == undefined) {
                    $rootScope.showWarningMessage(selectChangeAnalyst);
                    valid = false;
                } else if (vm.newMco.workflow == null || vm.newMco.workflow == "" || vm.newMco.workflow == undefined) {
                    $rootScope.showWarningMessage(selectWorkflow);
                    valid = false;
                } else if ($scope.mcoType == 'ITEMMCO' && (vm.newMco.revisionCreationType == null || vm.newMco.revisionCreationType == undefined || vm.newMco.revisionCreationType == "")) {
                    $rootScope.showWarningMessage(selectRevisionCreationType);
                    valid = false;
                } else if ($scope.mcoType == 'ITEMMCO' && vm.newMco.revisionCreationType == "ACTIVITY_COMPLETION" && (vm.newMco.workflowStatus == null || vm.newMco.workflowStatus == undefined ||
                    vm.newMco.workflowStatus == "")) {
                    $rootScope.showWarningMessage(selectWorkflowActivity);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (vm.mcoProperties.length > 0 && !validateCustomAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            vm.selectRevisionCreationRule = selectRevisionCreationRule;
            function selectRevisionCreationRule(value) {
                if (value == 'workflowStart') {
                    vm.newMco.revisionCreationType = "WORKFLOW_START";
                    vm.newMco.workflowStatus = null;
                }
                if (value == 'activityCompletion') {
                    vm.newMco.workflowStatus = null;
                    vm.newMco.revisionCreationType = "ACTIVITY_COMPLETION";
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    getNormalWorkflowStatuses();
                }
                $scope.$evalAsync();

            }

            vm.workflowStatuses = [];
            function getNormalWorkflowStatuses() {
                WorkflowDefinitionService.getNormalWorkflowStatuses(vm.newMco.workflow).then(
                    function (data) {
                        vm.workflowStatuses = data;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
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
                angular.forEach(vm.mcoProperties, function (attribute) {
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
                            DCOService.updateAttributeImageValue("MCOTYPE", vm.newMco.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            DCOService.updateAttributeAttachmentValues("MCOTYPE", vm.newMco.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
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

            function saveCustomAttributes() {
                var defered = $q.defer();
                if (vm.customImageAttributes.length > 0 || vm.customAttachmentAttributes.length > 0) {
                    angular.forEach(vm.customImageAttributes, function (imgAtt) {
                            DCOService.updateAttributeImageValue("MCO", vm.newMco.id, imgAtt.id.attributeDef, vm.customImages.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    )

                    angular.forEach(vm.customAttachmentAttributes, function (imgAtt) {
                            DCOService.updateAttributeAttachmentValues("MCO", vm.newMco.id, imgAtt.id.attributeDef, vm.customAttachments.get(imgAtt.id.attributeDef)).then(
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
                vm.changeAnalysts = [];
                var preference = $application.defaultValuesPreferences.get("DEFAULT_CHANGE_ANALYST_ROLE");
                if (preference != null && preference.defaultValueName != null) {
                    var groupName = preference.defaultValueName;
                    var permission = "permission.change.mco.all";
                    QualityTypeService.getPersonByGroupNameAndPermission(groupName, permission).then(
                        function (data) {
                            vm.changeAnalysts = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadMcrCustomProperties() {
                vm.mcoProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CHANGE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMco.id,
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
                            vm.mcoProperties.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onSelectWorkflow = onSelectWorkflow;
            function onSelectWorkflow(workflow) {
                vm.newMco.workflowStatus = null;
                getNormalWorkflowStatuses();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPersons();
                loadMcrCustomProperties();
                loadQCRs();
                $rootScope.$on('app.mcos.new', create);
                //}
            })();
        }
    }
)
;