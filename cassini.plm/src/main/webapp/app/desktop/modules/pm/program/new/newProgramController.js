define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programService',
        'app/shared/services/core/programTemplateService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/projectTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/projectService',
        'app/desktop/modules/directives/pmObjectTypeDirective'
    ],
    function (module) {
        module.controller('NewProgramController', NewProgramController);

        function NewProgramController($scope, $rootScope, $timeout, $sce, $state, $translate, $cookies, $q, ItemService,
                                      ProgramService, CommonService, ProjectTemplateService, LoginService, ObjectAttributeService, AttachmentService,
                                      AttributeAttachmentService, ProjectService, ObjectTypeAttributeService, ProgramTemplateService) {
            var vm = this;

            vm.program = {
                id: null,
                name: null,
                type: null,
                description: null,
                programManager: null,
                workflowDefinition: null,
                resources: false,
                team: false,
                assignedTo: false,
                copyFolders: false,
                allFolders: true,
                allWorkflows: true,
                copyWorkflows: false
            };
            vm.create = create;
            vm.creating = false;
            vm.valid = true;
            vm.error = "";
            vm.persons = [];
            vm.selectedTemplate = null;
            var parsed = angular.element("<div></div>");
            var createdSuccessfullyMsg = parsed.html($translate.instant("PROGRAM_CREATED_MSG")).html();
            var NameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
            var typeValidation = parsed.html($translate.instant("PROGRAM_TYPE_VALIDATION")).html();
            var validation = parsed.html($translate.instant("CANNOT_BE_EMPTY")).html();
            var manager = parsed.html($translate.instant("PROGRAM_MANAGER")).html();
            vm.select = parsed.html($translate.instant("SELECT")).html();
            vm.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE_TITLE")).html();
            $scope.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            function loadPersons() {
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            vm.persons.push(login.person);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectTeam = selectTeam;
            function selectTeam(program) {
                if (program.team == false) {
                    program.assignedTo = false;
                }
            }

            vm.selectCopyWorkflows = selectCopyWorkflows;
            function selectCopyWorkflows() {
                if (vm.program.copyWorkflows) {
                    vm.program.workflowDefinition = null;
                }
            }


            function validate() {
                vm.valid = true;

                if (vm.program.type == null || vm.program.type == undefined || vm.program.type == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(typeValidation);
                } else if (vm.program.name == null || vm.program.name == undefined || vm.program.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(NameValidation);
                }
                else if (vm.program.programManager == null || vm.program.programManager == undefined || vm.program.programManager == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(manager + " " + validation);
                }
                return vm.valid;
            }

            function create() {
                if (validate()) {
                    if (validateRequiredProperties()) {
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        vm.creating = true;
                        if (vm.program.workflowDefinition != null) {
                            vm.program.workflowDefId = vm.program.workflowDefinition.id;
                        }
                        ProgramService.createProgram(vm.program).then(
                            function (data) {
                                vm.program.id = data.id;
                                saveObjectAttributes().then(
                                    function (object) {
                                        vm.program = {
                                            id: null,
                                            name: null,
                                            description: null,
                                            programManager: null,
                                            workflowDefinition: null,
                                            resources: false,
                                            team: false,
                                            assignedTo: false,
                                            copyFolders: false,
                                            allFolders: true
                                        };

                                        vm.attributes = [];
                                        $timeout(function () {
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage(createdSuccessfullyMsg);
                                            $scope.callback(data);
                                            vm.creating = false;
                                            $rootScope.hideBusyIndicator();
                                        }, 1000)


                                    }
                                )

                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }


            vm.onSelectType = onSelectType;
            function onSelectType(programType) {
                vm.attributes = [];
                if (programType != null && programType != undefined) {
                    vm.program.programType = programType;
                    vm.program.type = programType;
                    ProjectService.getProjectAttributesWithHierarchy(vm.program.programType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.program.id,
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
                                if (attribute.dataType == "RICHTEXT") {
                                    $timeout(function () {
                                        $(".note-editor").show();
                                        $('.note-current-fontname').text('Arial');
                                    }, 1000);

                                }
                                vm.attributes.push(att);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                    loadWorkflows();
                }
            }

            /*------Save ObjectAttributes ------*/
            function saveObjectAttributes() {
                var defered = $q.defer();
                var propertyImages = new Hashtable();
                vm.propertyImageAttributes = [];
                var objectAttrs = vm.attributes;
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.program.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addSpecPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.program.id, objectAttrs).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function addSpecPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROGRAM', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                });
                return propertyAttachmentIds;
            }

            var validateAttribute = parsed.html($translate.instant("PLEASE_ENTER")).html();

            /*-------Validation for Required Properties -----------------*/
            function validateRequiredProperties() {
                var valid = true;

                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (valid) {
                            if (attribute.attributeDef.required == true) {
                                if ($rootScope.checkAttribute(attribute)) {
                                    valid = true;
                                } else {
                                    valid = false;
                                    $rootScope.showWarningMessage(validateAttribute + " " + attribute.attributeDef.name);
                                }
                            }
                        }
                    })
                }

                return valid;
            }

            function loadWorkflows() {
                ProjectService.getWorkflows('PROGRAM').then(
                    function (data) {
                        vm.wfs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadProgramTemplates() {
                ProgramTemplateService.getProgramTemplates().then(
                    function (data) {
                        vm.programTemplates = data;
                    }
                )
            }

            vm.selectFolder = selectFolder;
            function selectFolder(value) {
                if (value == "All") {
                    vm.program.allFolders = true;
                    vm.program.programFolders = false;
                    vm.program.projectFolders = false;
                    vm.program.activityFolders = false;
                    vm.program.taskFolders = false;
                } else if (value == "Program") {
                    vm.program.allFolders = false;
                    vm.program.programFolders = true;
                    vm.program.projectFolders = false;
                    vm.program.activityFolders = false;
                    vm.program.taskFolders = false;
                } else if (value == "Project") {
                    vm.program.allFolders = false;
                    vm.program.programFolders = false;
                    vm.program.projectFolders = true;
                    vm.program.activityFolders = false;
                    vm.program.taskFolders = false;
                } else if (value == "Activity") {
                    vm.program.allFolders = false;
                    vm.program.programFolders = false;
                    vm.program.projectFolders = false;
                    vm.program.activityFolders = true;
                    vm.program.taskFolders = false;
                } else {
                    vm.program.allFolders = false;
                    vm.program.programFolders = false;
                    vm.program.projectFolders = false;
                    vm.program.activityFolders = false;
                    vm.program.taskFolders = true;
                }
            }

            vm.selectWorkflow = selectWorkflow;
            function selectWorkflow(value) {
                if (value == "All") {
                    vm.program.allWorkflows = true;
                    vm.program.programWorkflows = false;
                    vm.program.projectWorkflows = false;
                    vm.program.activityWorkflows = false;
                    vm.program.taskWorkflows = false;
                } else if (value == "Program") {
                    vm.program.allWorkflows = false;
                    vm.program.programWorkflows = true;
                    vm.program.projectWorkflows = false;
                    vm.program.activityWorkflows = false;
                    vm.program.taskWorkflows = false;
                } else if (value == "Project") {
                    vm.program.allWorkflows = false;
                    vm.program.programWorkflows = false;
                    vm.program.projectWorkflows = true;
                    vm.program.activityWorkflows = false;
                    vm.program.taskWorkflows = false;
                 } else if (value == "Activity") {
                    vm.program.allWorkflows = false;
                    vm.program.programWorkflows = false;
                    vm.program.projectWorkflows = false;
                    vm.program.activityWorkflows = true;
                    vm.program.taskWorkflows = false;
                } else {
                    vm.program.allWorkflows = false;
                    vm.program.programWorkflows = false;
                    vm.program.projectWorkflows = false;
                    vm.program.activityWorkflows = false;
                    vm.program.taskWorkflows = true;
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadProgramTemplates();
                loadPersons();
                loadWorkflows();
                $rootScope.$on('app.program.new', create);
                //}
            })();
        }
    }
)
;