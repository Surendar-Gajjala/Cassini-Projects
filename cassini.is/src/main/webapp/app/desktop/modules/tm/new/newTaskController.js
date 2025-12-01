define(['app/desktop/modules/tm/tm.module',
        'dropzone',
        'split-pane',
        'jquery.easyui',
        'app/desktop/modules/tm/new/wbsSelectDialogController',
        'app/desktop/directives/wbsTreeDirective',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/projectSiteService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/storeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/workflow/workflowDefinitionService'
    ],
    function (module) {
        module.controller('NewTaskController', NewTaskController);

        function NewTaskController($scope, $rootScope, $uibModal, $q, $timeout, $state, $stateParams, $cookies,
                                   TaskService, ProjectSiteService, CommonService, ProjectService, StoreService,
                                   ItemService, ObjectAttributeService, AttributeAttachmentService, WorkflowDefinitionService) {
            var vm = this;

            vm.valid = true;
            vm.hasError = false;
            var isProjectTask = $scope.data.projectTask;

            vm.person = null;
            vm.wbs = null;
            vm.sites = null;
            var sitesMap = new Hashtable();
            vm.task = null;
            vm.requiredAttributes = [];
            vm.site = {
                name: null,
                description: "",
                project: $stateParams.projectId
            };
            var site = {
                name: "New Site",
                description: "",
                project: $stateParams.projectId
            };

            vm.persons = [];
            vm.projectPersons = [];

            vm.createTask = createTask;
            vm.cancelTask = cancelTask;
            vm.onSelectWbs = onSelectWbs;
            vm.newSite = newSite;
            vm.existingSite = existingSite;
            vm.changeMode = changeMode;

            function existingSite(mode) {
                vm.mode = 'EXIST';
            }

            function newSite(mode) {
                vm.mode = 'NEW';
            }

            vm.newTask = {
                name: null,
                description: null,
                project: $stateParams.projectId,
                plannedStartDate: null,
                plannedFinishDate: null,
                percentComplete: 0,
                wbsItem: null,
                wbsItemObject: null,
                site: null,
                siteObject: null,
                person: null,
                personObject: null,
                unitOfWork: null,
                totalUnits: 0,
                tempTotalUnits: null,
                inspectedBy: null,
                inspectionPerson: null,
                inspectedOn: null,
                inspectionResult: null,
                inspectionRemarks: null,
                subContract: false,
                workflowDefinition: null
            };

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
                }
            };

            var tasks = [];
            var taskMap = new Hashtable();

            function changeMode() {
                if (vm.newTask.siteObject.name == "New Site" && (vm.newTask.siteObject.id == null || vm.newTask.siteObject.id == undefined)) {
                    vm.mode = 'NEW';
                }
                else {
                    vm.mode = 'EXIST';
                }
            }

            function validateTaskFields() {
                vm.valid = true;
                if (vm.newTask.name == null || vm.newTask.name == undefined || vm.newTask.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Task Name cannot be empty");
                } else if (vm.newTask.siteObject == null || vm.newTask.siteObject == undefined || vm.newTask.siteObject == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Site cannot be empty");
                } else if (vm.newTask.siteObject != null) {
                    if (vm.mode == 'NEW') {
                        if (vm.site.name == null || vm.site.name == undefined || vm.site.name == "") {
                            vm.valid = false;
                            $rootScope.showWarningMessage("Site Name cannot be empty");
                        } else if (vm.site.name != null) {
                            var task = taskMap.get(vm.newTask.name);
                            if (vm.site.id != null) {
                                if (task != null && task.site == vm.site.id) {
                                    vm.valid = false;
                                    $rootScope.showWarningMessage("Task {0} Name already exists for this Site {1}".format(vm.newTask.name, vm.site.name));
                                } else {
                                    vm.valid = false;
                                }
                            }
                        }
                    } else if (vm.mode != 'NEW') {
                        var task = taskMap.get(vm.newTask.name);
                        if (vm.newTask.siteObject.id != null) {
                            if (task != null && task.site == vm.newTask.siteObject.id) {
                                vm.valid = false;
                                $rootScope.showWarningMessage("Task {0} Name already exists for this Site {1}".format(vm.newTask.name, vm.newTask.siteObject.name));
                            }
                            /* else {
                             vm.valid = false;
                             }*/
                        }
                    } else if (vm.newTask.wbsItemObject == null || vm.newTask.wbsItemObject == undefined || vm.newTask.wbsItemObject == "") {
                        vm.valid = false;
                        $rootScope.showWarningMessage("WBS Item cannot be empty");
                    } else if (vm.newTask.wbsItemObject != null) {
                        /* if (vm.newTask.subContract == false && (vm.newTask.personObject == null || vm.newTask.personObject == undefined || vm.newTask.personObject == "")) {
                         vm.valid = false;
                         $rootScope.showWarningMessage("Please select Assigned To");*/
                    } else if (vm.newTask.personObject == null || vm.newTask.subContract) {
                        if (vm.newTask.unitOfWork == null || vm.newTask.unitOfWork == undefined || vm.newTask.unitOfWork == "") {
                            vm.valid = false;
                            $rootScope.showWarningMessage("Unit of work cannot be empty");
                        } else if (vm.newTask.unitOfWork != null || vm.newTask.subContract) {
                            if (vm.newTask.tempTotalUnits == null || vm.newTask.tempTotalUnits == undefined || vm.newTask.tempTotalUnits == "") {
                                vm.valid = false;
                                $rootScope.showWarningMessage("Total Units cannot be empty");
                            } else if (vm.newTask.tempTotalUnits != null) {
                                if (vm.newTask.plannedStartDate == null || vm.newTask.plannedStartDate == undefined || vm.newTask.plannedStartDate == "") {
                                    vm.valid = false;
                                    $rootScope.showWarningMessage("Planned Start Date cannot be empty");
                                } else if (vm.newTask.plannedStartDate != null) {
                                    var plannedStartDate = moment(vm.newTask.plannedStartDate, 'DD/MM/YYYY');
                                    var wbsPlannedStartDate = moment(vm.wbs.plannedStartDate, 'DD/MM/YYYY');
                                    if (plannedStartDate.isBefore(wbsPlannedStartDate)) {
                                        vm.valid = false;
                                        $rootScope.showWarningMessage("Planned Start Date should be in b/w WBS Planned Start & Finished Date's");
                                    } else {
                                        if (vm.newTask.plannedFinishDate == null || vm.newTask.plannedFinishDate == undefined || vm.newTask.plannedFinishDate == "") {
                                            vm.valid = false;
                                            $rootScope.showWarningMessage("Planned Finish Date cannot be empty");
                                        } else if (vm.newTask.plannedFinishDate != null) {
                                            var plannedFinishDate = moment(vm.newTask.plannedFinishDate, 'DD/MM/YYYY');
                                            var plannedStartDate = moment(vm.newTask.plannedStartDate, 'DD/MM/YYYY');
                                            var wbsPlannedFinishDate = moment(vm.wbs.plannedFinishDate, 'DD/MM/YYYY');
                                            var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                                            if (!val) {
                                                vm.valid = false;
                                                $rootScope.showWarningMessage("Planned Finish Date should be after Planned Start Date");
                                            } else if (plannedFinishDate.isAfter(wbsPlannedFinishDate)) {
                                                vm.valid = false;
                                                $rootScope.showWarningMessage("Planned Finish Date should not exceed WBS Planned Finish Date");
                                            } else {
                                                if (vm.mode == 'NEW') {
                                                    vm.newTask.site = vm.site.id;
                                                } else {
                                                    vm.newTask.site = vm.newTask.siteObject.id;
                                                }
                                                vm.newTask.wbsItem = vm.newTask.wbsItemObject;
                                                if (vm.newTask.personObject != null) {
                                                    vm.newTask.person = vm.newTask.personObject.id;
                                                }
                                                if (vm.newTask.inspectionPerson != null) {
                                                    vm.newTask.inspectedBy = vm.newTask.inspectionPerson.id;
                                                }
                                                vm.newTask.totalUnits = vm.newTask.tempTotalUnits;
                                                vm.valid = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /*}*/
                }
                return vm.valid;
            }

            function validationTask() {
                vm.valid = true;
                if (vm.newTask.name == null || vm.newTask.name == undefined || vm.newTask.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Task Name cannot be empty");
                } else if (vm.newTask.siteObject == null || vm.newTask.siteObject == undefined || vm.newTask.siteObject == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Site cannot be empty");
                } else if (vm.newTask.siteObject != null && !validataTaskSite()) {
                    vm.valid = false;
                } else if (vm.newTask.wbsItemObject == null || vm.newTask.wbsItemObject == undefined || vm.newTask.wbsItemObject == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("WBS Item cannot be empty");
                } else if (vm.newTask.unitOfWork == null || vm.newTask.unitOfWork == undefined || vm.newTask.unitOfWork == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Unit of work cannot be empty");
                } else if (vm.newTask.tempTotalUnits == null || vm.newTask.tempTotalUnits == undefined || vm.newTask.tempTotalUnits == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Total Units cannot be empty");
                } else if (vm.newTask.plannedStartDate == null || vm.newTask.plannedStartDate == undefined || vm.newTask.plannedStartDate == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Planned Start Date cannot be empty");
                } else if (vm.newTask.plannedStartDate != null) {
                    var plannedStartDate = moment(vm.newTask.plannedStartDate, 'DD/MM/YYYY');
                    var wbsPlannedStartDate = moment(vm.wbs.plannedStartDate, 'DD/MM/YYYY');
                    var taskWbsPlannedFinishDate = moment(vm.wbs.plannedFinishDate, 'DD/MM/YYYY');
                    if (plannedStartDate.isBefore(wbsPlannedStartDate)) {
                        vm.valid = false;
                        $rootScope.showWarningMessage("Planned Start Date should be in b/w WBS Planned Start & Finished Date's");
                    } else if (plannedStartDate.isAfter(taskWbsPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showWarningMessage("Planned Start Date should be before WBS Planned Finished Date");
                    } else if (vm.newTask.plannedFinishDate == null || vm.newTask.plannedFinishDate == undefined || vm.newTask.plannedFinishDate == "") {
                        vm.valid = false;
                        $rootScope.showWarningMessage("Planned Finish Date cannot be empty");
                    } else if (vm.newTask.plannedFinishDate != null) {
                        var plannedFinishDate = moment(vm.newTask.plannedFinishDate, 'DD/MM/YYYY');
                        var plannedStartDate = moment(vm.newTask.plannedStartDate, 'DD/MM/YYYY');
                        var wbsPlannedFinishDate = moment(vm.wbs.plannedFinishDate, 'DD/MM/YYYY');
                        var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                        if (!val) {
                            vm.valid = false;
                            $rootScope.showWarningMessage("Planned Start Date should be before Planned Finish Date");
                        } else if (plannedFinishDate.isAfter(wbsPlannedFinishDate)) {
                            vm.valid = false;
                            $rootScope.showWarningMessage("Planned Finish Date should not exceed WBS Planned Finish Date");
                        }
                    }
                }

                return vm.valid;
            }

            function validataTaskSite() {
                var valid = true;
                if (vm.mode == 'NEW') {
                    if (vm.site.name == null || vm.site.name == undefined || vm.site.name == "") {
                        valid = false;
                        $rootScope.showWarningMessage("Site Name cannot be empty");
                    } else if (vm.site.name != null) {
                        var task = taskMap.get(vm.newTask.name);
                        if (vm.site.id != null) {
                            if (task != null && task.site == vm.site.id) {
                                valid = false;
                                $rootScope.showWarningMessage("Task {0} Name already exists for this Site {1}".format(vm.newTask.name, vm.site.name));
                            } else {
                                valid = false;
                            }
                        }
                    }
                } else if (vm.mode != 'NEW') {
                    var task = taskMap.get(vm.newTask.name);
                    if (vm.newTask.siteObject.id != null) {
                        if (task != null && task.site == vm.newTask.siteObject.id) {
                            valid = false;
                            $rootScope.showWarningMessage("Task {0} Name already exists for this Site {1}".format(vm.newTask.name, vm.newTask.siteObject.name));
                        }
                    }
                }

                /*   if (vm.site.name == null || vm.site.name == undefined || vm.site.name == "") {
                 valid = false;
                 $rootScope.showWarningMessage("Site name cannot be empty");
                 }
                 if (vm.site.name != null) {
                 var task = taskMap.get(vm.newTask.name);
                 if (vm.site.id != null) {
                 if (task != null && task.site == vm.site.id) {
                 valid = false;
                 $rootScope.showWarningMessage("Task {0} Name already exists for Site {1}".format(vm.newTask.name, vm.site.name));
                 }
                 }
                 }*/

                return valid;
            }

            function validate() {
                vm.hasError = false;
                vm.valid = true;
                if (vm.newTask.name == null || vm.newTask.name == undefined || vm.newTask.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Task Name cannot be empty");
                } else if (vm.newTask.name != null) {
                    if (vm.newTask.siteObject == null || vm.newTask.siteObject == undefined || vm.newTask.siteObject == "") {
                        vm.valid = false;
                        $rootScope.showWarningMessage("Site cannot be empty");
                    } else if (vm.newTask.siteObject != null) {
                        if (vm.mode == 'NEW') {
                            if (vm.site.name == null || vm.site.name == undefined || vm.site.name == "") {
                                vm.valid = false;
                                $rootScope.showWarningMessage("Site Name cannot be empty");
                            } else if (vm.site.name != null) {
                                var task = taskMap.get(vm.newTask.name);
                                if (vm.site.id != null) {
                                    if (task != null && task.site == vm.site.id) {
                                        vm.valid = false;
                                        $rootScope.showWarningMessage("Task {0} Name already exists for this Site {1}".format(vm.newTask.name, vm.site.name));
                                    } else if (validateTaskFields()) {
                                        vm.valid = true;
                                    } else {
                                        vm.valid = false;
                                    }
                                }
                            }
                        } else {
                            var task = taskMap.get(vm.newTask.name);
                            if (vm.newTask.siteObject.id != null) {
                                if (task != null && task.site == vm.site.id) {
                                    vm.valid = false;
                                    $rootScope.showWarningMessage("Task {0} Name already exists for this Site {1}".format(vm.newTask.name, vm.site.name));
                                } else if (validateTaskFields()) {
                                    vm.valid = true;
                                } else {
                                    vm.valid = false;
                                }
                            }
                        }
                    }
                }
                return vm.valid;
            }

            function validateSite() {
                vm.valid = true;

                if (vm.site.name === null || vm.site.name == undefined || vm.site.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Site Name cannot be empty");
                }
                else if (sitesMap.get(vm.site.name) != null) {
                    vm.valid = false;
                    $rootScope.showWarningMessage("{0} Name already exists".format(vm.site.name));
                }
                return vm.valid;
            }

            function cancelTask() {
                vm.newTask = null;
                $state.go('app.pm.project.tasks');
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
                            $rootScope.showWarningMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newTaskAttributes != null && vm.newTaskAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newTaskAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showWarningMessage(attribute.attributeDef.name + ": attribute is required");
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

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TASK', attachmentFile).then(
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
                if (vm.newTaskAttributes.length > 0) {
                    angular.forEach(vm.newTaskAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newTask.id, vm.newTaskAttributes).then(
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
                        }, function (error) {
                            defered.reject();
                        }
                    )
                } else {
                    defered.resolve();
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

                if (vm.newTaskAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newTaskAttributes, function (attribute) {
                        attribute.id.objectId = vm.newTask.id;
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
                                    if (attrCount == vm.newTaskAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (success) {
                                                vm.newTaskAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (erro) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newTaskAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newTaskAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            function create() {
                $rootScope.closeNotification();
                if (validationTask()) {
                    if (vm.newTask.subContract == true) {
                        vm.newTask.person = null;
                    }
                    attributesValidate().then(
                        function (success) {
                            if (vm.mode == 'NEW') {
                                createSite().then(
                                    function (siteCreated) {
                                        createTask();
                                    }
                                );
                            }
                            else {
                                createTask();
                            }
                        }, function (error) {

                        });
                }
            }

            function createTask() {
                if (vm.newTask.workflowDefinition != null) {
                    var workflowDefinition = vm.newTask.workflowDefinition;
                }
                if (vm.newTask.site == null) {
                    vm.newTask.site = vm.newTask.siteObject.id;
                }
                if (vm.newTask.wbsItem == null) {
                    vm.newTask.wbsItem = vm.newTask.wbsItemObject;
                }
                if (vm.newTask.personObject != null) {
                    vm.newTask.person = vm.newTask.personObject.id;
                }
                if (vm.newTask.inspectionPerson != null) {
                    vm.newTask.inspectedBy = vm.newTask.inspectionPerson.id;
                }
                if (vm.newTask.totalUnits != null) {
                    vm.newTask.totalUnits = vm.newTask.tempTotalUnits;
                }
                TaskService.createProjectTask($stateParams.projectId, vm.newTask).then(
                    function (data) {
                        vm.newTask = data;
                        if (workflowDefinition != null) {
                            TaskService.attachWorkflow($stateParams.projectId, vm.newTask.id, workflowDefinition.id).then(
                                function (data) {
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.showSuccessMessage("Task (" + vm.newTask.name + ") created successfully");
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.taskId = vm.newTask.id;
                                            $rootScope.hideSidePanel();
                                            $rootScope.loadProject();
                                            if (isProjectTask) {
                                                $rootScope.$broadcast("app.project.tasks");
                                            }
                                            else {
                                                $rootScope.$broadcast("app.project.problem.update", {task: vm.newTask});
                                            }
                                        }, function (error) {

                                        });
                                });
                        } else {
                            intializationForAttributesSave().then(
                                function (success) {
                                    $rootScope.showSuccessMessage("Task (" + vm.newTask.name + ") created successfully");
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.taskId = vm.newTask.id;
                                    $rootScope.hideSidePanel();
                                    $rootScope.loadProject();
                                    if (isProjectTask) {
                                        $rootScope.$broadcast("app.project.tasks");
                                    }
                                    else {
                                        $rootScope.$broadcast("app.project.problem.update", {task: vm.newTask});
                                    }
                                }, function (error) {

                                });
                        }
                    }, function (error) {

                    });
            }

            function onSelectWbs(node) {
                var data = node.attributes.wbs.children;
                if (data.length == 0 || data.length == null || data.length == "") {
                    $rootScope.closeNotification();
                    vm.wbs = node.attributes.wbs;
                    vm.newTask.plannedStartDate = vm.wbs.plannedStartDate;
                    vm.newTask.plannedFinishDate = vm.wbs.plannedFinishDate;
                    vm.newTask.wbsItemObject = vm.wbs.id;
                    window.$("body").trigger("click");
                }
                else if (data.length > 0 && data.length != null && data.length != "") {
                    $rootScope.showWarningMessage("Please click children node");
                }
            }

            function loadSites() {
                vm.sites = [];
                vm.sites.push(site);
                ProjectSiteService.getPagedSitesByProject(vm.newTask.project, vm.pageable).then(
                    function (data) {
                        angular.forEach(data.content, function (site) {
                            vm.sites.push(site);
                            sitesMap.put(site.name, site);
                        })
                    }
                )
            }

            function loadProjectPersons() {
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        angular.forEach(vm.projectPersons, function (obj) {
                            loadPersonsById(obj.person);

                        })
                    }
                )
            }

            function loadPersonsById(person) {
                vm.loading = false;
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                    }
                )
            }

            function loadTasks() {
                TaskService.getListProjectTasks($stateParams.projectId).then(
                    function (data) {
                        tasks = data;
                        angular.forEach(tasks, function (task) {
                            taskMap.put(task.name, task);
                        })
                    }
                )
            }

            function loadObjectAttributeDefs() {
                vm.newTaskAttributes = [];
                ItemService.getAllTypeAttributes("TASK").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newTask.id,
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
                            vm.newTaskAttributes.push(att);
                        });
                    }
                );
            }

            function createSite() {
                var defered = $q.defer();
                if (validateSite()) {
                    ProjectSiteService.createSite(vm.site).then(
                        function (data) {
                            vm.site = data;
                            vm.newTask.site = vm.site.id;
                            defered.resolve();
                        },
                        function (error) {
                            defered.reject();
                        }
                    );
                }
                return defered.promise;
            }

            function loadWorkflows() {
                vm.wfs = [];
                WorkflowDefinitionService.getWorkflowDefs().then(
                    function (data) {
                        vm.workflows = data;
                        /* angular.forEach(vm.workflows, function (workflow) {
                         var changes = workflow.assignableTo;
                         if (changes.indexOf('ECO') != -1) {
                         vm.wfs.push(workflow);
                         }
                         }
                         )*/
                    }
                );
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjectPersons();
                    loadTasks();
                    loadSites();
                    loadWorkflows();
                    loadObjectAttributeDefs();
                    $rootScope.$on('app.task.new', function () {
                        create();
                    });
                }
            })();
        }
    }
)
;