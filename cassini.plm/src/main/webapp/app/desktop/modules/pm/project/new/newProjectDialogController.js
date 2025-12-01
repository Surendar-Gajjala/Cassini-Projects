define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
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
        'app/shared/services/core/programService',
        'app/desktop/modules/directives/pmObjectTypeDirective'
    ],
    function (module) {
        module.controller('NewProjectController', NewProjectController);

        function NewProjectController($scope, $rootScope, $timeout, $sce, $state, $translate, $cookies, $q, ItemService,
                                      ProjectService, CommonService, ProjectTemplateService, LoginService, ObjectAttributeService, AttachmentService,
                                      AttributeAttachmentService, ObjectTypeAttributeService, ProgramService) {
            var vm = this;

            vm.project = {
                name: null,
                type: null,
                description: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                projectManager: $rootScope.loginPersonDetails.person.id,
                workflowDefinition: null,
                team: false,
                assignedTo: false,
                program: null,
                programProject: null,
                copyFolders: false,
                allFolders: true,
                allWorkflows: true
            };
            vm.create = create;
            vm.creating = false;
            vm.valid = true;
            vm.error = "";
            vm.persons = [];
            vm.selectedTemplate = null;
            vm.projectCreationType = $scope.data.projectCreationFrom;

            var parsed = angular.element("<div></div>");
            var createdSuccessfullyMsg = parsed.html($translate.instant("CREATED_SUCCESSFULLY")).html();
            var NameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
            var typeValidation = parsed.html($translate.instant("PROJECT_TYPE_VALIDATION")).html();
            var NameExists = parsed.html($translate.instant("PROJECT_NAME_EXISTS")).html();
            var DescriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var validation = parsed.html($translate.instant("CANNOT_BE_EMPTY")).html();
            var plannedSDate = parsed.html($translate.instant("PLANNED_START_DATE")).html();
            var plannedFDate = parsed.html($translate.instant("PLANNED_FINISH_DATE")).html();
            var manager = parsed.html($translate.instant("PROJECT_MANAGER")).html();
            var selectManager = parsed.html($translate.instant("SELECT_MANAGER")).html();
            var plannedStartDateValidation = parsed.html($translate.instant("PLANNED_START_DATE_VALIDATION")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var startDateValidation = parsed.html($translate.instant("START_DATE_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();
            vm.select = parsed.html($translate.instant("SELECT")).html();
            vm.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE_TITLE")).html();
            var itemWorkflowValidation = parsed.html($translate.instant("ITEM_WORKFLOW_VALIDATION")).html();
            $scope.selectTemplate = parsed.html($translate.instant("SELECT_TEMPLATE")).html();
            $scope.selectWorkflow = parsed.html($translate.instant("SELECT_WORKFLOW")).html();

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
                }
            };

            var projects = [];

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function loadPersons() {
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

            vm.selectCopyWorkflows = selectCopyWorkflows;
            function selectCopyWorkflows() {
                if (vm.project.copyWorkflows) {
                    vm.project.workflowDefinition = null;
                }
            }

            function validate() {
                vm.valid = true;

                if (vm.project.type == null || vm.project.type == undefined || vm.project.type == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(typeValidation);
                }
                else if (vm.project.name == null || vm.project.name == undefined || vm.project.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(NameValidation);
                }
                else if (vm.project.projectManager == null || vm.project.projectManager == undefined || vm.project.projectManager == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(manager + " " + validation);
                }
                else if (vm.project.plannedStartDate == null ||
                    vm.project.plannedStartDate == undefined || vm.project.plannedStartDate == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(plannedStartDateValidation);
                } else if (vm.project.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);

                    if (vm.project.plannedFinishDate == null || vm.project.plannedFinishDate == undefined || vm.project.plannedFinishDate == "") {
                        vm.valid = false;
                        $rootScope.showWarningMessage(plannedFinishDateValidation);
                    }
                }
                if (vm.project.plannedStartDate != null && vm.project.plannedFinishDate != null && vm.project.plannedStartDate != "" && vm.project.plannedFinishDate != "") {
                    var plannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                    if (!val) {
                        vm.valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }
                }
                if (vm.project.plannedStartDate != null && vm.project.plannedStartDate != "" && vm.project.plannedStartDate != undefined) {
                    if ($rootScope.dateValidation(vm.project.plannedStartDate)) {
                        vm.project.plannedStartDate = $rootScope.convertDate(vm.project.plannedStartDate);
                    } else {
                        vm.valid = false;
                    }
                }
                if (vm.project.plannedFinishDate != null && vm.project.plannedFinishDate != "" && vm.project.plannedFinishDate != undefined) {
                    if ($rootScope.dateValidation(vm.project.plannedFinishDate)) {
                        vm.project.plannedFinishDate = $rootScope.convertDate(vm.project.plannedFinishDate);
                    } else {
                        vm.valid = false;
                    }
                }
                return vm.valid;
            }

            function create() {
                if (validate()) {
                    if (validateRequiredProperties()) {
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        vm.creating = true;
                        if (vm.project.workflowDefinition != null) {
                            vm.project.workflowDefId = vm.project.workflowDefinition.id;
                        }
                        if (vm.selectedTemplate == null) {
                            ProjectService.createProject(vm.project).then(
                                function (data) {
                                    vm.project.id = data.id;
                                    saveObjectAttributes().then(
                                        function (object) {
                                            vm.project = {
                                                name: null,
                                                description: null,
                                                plannedStartDate: null,
                                                plannedFinishDate: null,
                                                projectManager: null,
                                                workflowDefinition: null
                                            };

                                            vm.attributes = [];
                                            $rootScope.hideSidePanel('right');
                                            $scope.callback(data);
                                            vm.creating = false;
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(createdSuccessfullyMsg);

                                        }
                                    )

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            ProjectService.createProjectWithTemplate(vm.project, vm.selectedTemplate.id).then(
                                function (data) {
                                    vm.project.id = data.id;
                                    saveObjectAttributes().then(
                                        function (object) {
                                            vm.project = {
                                                name: null,
                                                description: null,
                                                plannedStartDate: null,
                                                plannedFinishDate: null,
                                                projectManager: null,
                                                team: false,
                                                assignedTo: false
                                            };

                                            vm.attributes = [];
                                            $rootScope.hideSidePanel('right');
                                            $scope.callback(data);
                                            vm.creating = false;
                                            vm.selectedTemplate = null;
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(createdSuccessfullyMsg);

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
            }

            vm.onSelectType = onSelectType;
            function onSelectType(projectType) {
                vm.attributes = [];
                if (projectType != null && projectType != undefined) {
                    vm.project.projectType = projectType;
                    vm.project.type = projectType;
                    ProjectService.getProjectAttributesWithHierarchy(vm.project.projectType.id).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.project.id,
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
                        attribute.id.objectId = vm.project.id;

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
                        ObjectAttributeService.saveItemObjectAttributes(vm.project.id, objectAttrs).then(
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
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROJECT', attachmentFile).then(
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
                                    $rootScope.showWarningMessage(validateAttribute + attribute.attributeDef.name);
                                }
                            }
                        }
                    })
                }

                return valid;
            }

            function loadTemplates() {
                ProjectTemplateService.getProjectTemplatesByProgramNull().then(
                    function (data) {
                        vm.templates = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWorkflows() {
                ProjectService.getWorkflows('PROJECTS').then(
                    function (data) {
                        vm.wfs = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadPrograms() {
                ProgramService.getPrograms().then(
                    function (data) {
                        vm.programs = data;
                    }
                )
            }

            vm.selectTeam = selectTeam;
            function selectTeam(project) {
                if (project.team == false) {
                    project.assignedTo = false;
                }
            }

            vm.selectFolder = selectFolder;
            function selectFolder(value) {
                if (value == "All") {
                    vm.project.allFolders = true;
                    vm.project.projectFolders = false;
                    vm.project.activityFolders = false;
                    vm.project.taskFolders = false;
                } else if (value == "Project") {
                    vm.project.allFolders = false;
                    vm.project.projectFolders = true;
                    vm.project.activityFolders = false;
                    vm.project.taskFolders = false;
                } else if (value == "Activity") {
                    vm.project.allFolders = false;
                    vm.project.projectFolders = false;
                    vm.project.activityFolders = true;
                    vm.project.taskFolders = false;
                } else {
                    vm.project.allFolders = false;
                    vm.project.projectFolders = false;
                    vm.project.activityFolders = false;
                    vm.project.taskFolders = true;
                }
            }

            vm.selectWorkflow = selectWorkflow;
            function selectWorkflow(value) {
                if (value == "All") {
                    vm.project.allWorkflows = true;
                    vm.project.projectWorkflows = false;
                    vm.project.activityWorkflows = false;
                    vm.project.taskWorkflows = false;
                } else if (value == "Project") {
                    vm.project.allWorkflows = false;
                    vm.project.projectWorkflows = true;
                    vm.project.activityWorkflows = false;
                    vm.project.taskWorkflows = false;
                }
                else if (value == "Activity") {
                    vm.project.allWorkflows = false;
                    vm.project.projectWorkflows = false;
                    vm.project.activityWorkflows = true;
                    vm.project.taskWorkflows = false;
                } else {
                    vm.project.allWorkflows = false;
                    vm.project.projectWorkflows = false;
                    vm.project.activityWorkflows = false;
                    vm.project.taskWorkflows = true;
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadPersons();
                loadTemplates();
                loadWorkflows();
                loadPrograms();
                if ($scope.data.projectCreationFrom == "PROGRAM") {
                    vm.project.program = $scope.data.selectedProgram.id;
                    vm.project.programProject = $scope.data.selectedProgramGroup.id;
                }
                $rootScope.$on('app.project.new', create);
                //}
            })();
        }
    }
);