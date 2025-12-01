define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/milestoneService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/recentlyVisitedService',
        'app/desktop/modules/pm/project/details/tabs/plan/ganttEditor',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService',
        'app/shared/services/core/workflowService'
    ],
    function (module) {
        module.controller('ProjectPlanController', ProjectPlanController);

        function ProjectPlanController($scope, $rootScope, $state, $timeout, $sce, $window, $stateParams, $translate, ProjectService, GanttEditor,
                                       ActivityService, CommonService, MilestoneService, DialogService, ItemService, ObjectTypeAttributeService, WorkflowService,
                                       AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, PreferenceService) {

            var vm = this;
            var projectId = $stateParams.projectId;
            vm.wbsItems = [];
            vm.loading = true;
            var lastSelectedWbs = null;
            var maxIndex = 0;
            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.taskComplete = false;
            $rootScope.ganttType = 'plan';

            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.zoomed = false;
            var linkId = null;
            var links = [];
            var project = null;
            var wbsMap = new Hashtable();
            var creatingWbs = false;
            var activityId = null;
            var taskSeqid = 1;
            var phaseId = null;
            var seqId = 1;
            $rootScope.ganttPersons = [];

            var gantt_data = {
                data: [],
                links: [],
                persons: [],
                projectStartDate: null,
                projectFinishDate: null,
                width: {},
                workingDays: null,
                holidays: [],
                hasPermission: false
            };

            var default_width = {
                text: 200,
                object_type: 100,
                assignedTo: 100,
                duration: 80,
                start_date: 100,
                end_date: 100,
                actual_start_date: 125,
                actual_end_date: 125,
                grid: 800,
                showGantt: false
            };

            var width_settings = {};

            //$rootScope.addWbs = addWbs;
            //$rootScope.addActivity = addActivity;
            //$rootScope.addMilestone = addMilestone;
            $rootScope.createDuplicateWbs = createDuplicateWbs;
            $rootScope.createNewTemplate = createNewTemplate;
            $rootScope.addNewTemplate = addNewTemplate;
            $rootScope.showWbsButton = true;
            $rootScope.showActivityAndMilestone = false;
            $rootScope.showCreateDuplicateWbs = false;

            $rootScope.saveGantt = saveGantt;
            $rootScope.ids = [];
            vm.ganttChanges = false;

            $rootScope.showProjectActivityFiles = showProjectActivityFiles;
            function showProjectActivityFiles(activity) {
                var id = parseInt(activity);
                $state.go('app.pm.project.activity.details', {activityId: id, tab: 'details.files'});
            }

            $rootScope.onSearchProjectPlan = onSearchProjectPlan;
            function onSearchProjectPlan(searchProjectName) {
                $rootScope.searchProjectName = searchProjectName;
                gantt.refreshData();
            }

            $rootScope.showProjectTaskFiles = showProjectTaskFiles;
            function showProjectTaskFiles(task) {
                var obj = task.split(",");
                var id = parseInt(obj[0]);
                var parentId = parseInt(obj[1]);
                $state.go('app.pm.project.activity.task.details', {
                    activityId: parentId,
                    taskId: id,
                    tab: 'details.files'
                });
            }

            function loadGanttData() {
                angular.forEach(vm.wbsItems, function (wbs) {
                    wbs.start_date = wbs.plannedStartDate == null ? "" : wbs.plannedStartDate;
                    var startDate = moment(wbs.start_date, $rootScope.applicationDateSelectFormat);
                    var projectStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    if (wbs.start_date != vm.project.plannedStartDate) {
                        if (projectStartDate.isAfter(startDate)) wbs.start_date = vm.project.plannedStartDate;
                    }
                    wbs.duration = wbs.duration == null ? null : wbs.duration;
                    if (wbs.duration == null) wbs.end_date = wbs.plannedFinishDate == null ? "" : wbs.plannedFinishDate;
                    var finishDate = moment(wbs.end_date, $rootScope.applicationDateSelectFormat);
                    var projectFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    if (wbs.end_date != vm.project.plannedFinishDate && wbs.end_date != null) {
                        if (finishDate.isBefore(projectFinishDate)) wbs.end_date = vm.project.plannedFinishDate;
                    }
                    wbs.actual_start_date = wbs.actualStartDate == null ? "" : wbs.actualStartDate;
                    wbs.actual_end_date = wbs.actualFinishDate == null ? "" : wbs.actualFinishDate;
                    if (wbs.plannedStartDate == "" || wbs.plannedFinishDate == "" || wbs.plannedStartDate == null || wbs.plannedFinishDate == null) {
                        wbs.unscheduled = true;
                    } else {
                        wbs.unscheduled = false;
                    }
                    wbs.created_date = wbs.createdDate;
                    wbs.created_by = wbs.createdBy;
                    wbs.text = wbs.name;
                    wbs.progress = wbs.percentComplete / 100;
                    wbs.parent = wbs.parent == null ? 0 : wbs.parent;
                    if (wbs.objectType != "PROJECTTASK") wbs.assignedTo = wbs.person != null ? wbs.person.id : null;
                    if (wbs.objectType == "PROJECTPHASEELEMENT") wbs.object_type = "Phase";
                    if (wbs.objectType == "PROJECTACTIVITY") {
                        wbs.object_type = "Activity";
                        wbs.assignedTo = wbs.assignedTo != null ? wbs.assignedTo : null;
                        if (wbs.activityTasks.length == 0) {
                            var today = moment(new Date());
                            var todayStr = today.format($rootScope.applicationDateSelectFormat);
                            var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                            var finishDate = moment(wbs.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                            if (todayDate.isAfter(finishDate)) wbs.over_due = "true";
                            else wbs.over_due = "false";
                        }
                    }
                    if (wbs.objectType == "PROJECTMILESTONE") {
                        wbs.object_type = "Milestone";
                        wbs.start_date = wbs.plannedFinishDate == null ? wbs.start_date : wbs.plannedFinishDate;
                        var startDate = moment(wbs.start_date, $rootScope.applicationDateSelectFormat);
                        var plannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                        if (plannedStartDate.isAfter(startDate)) wbs.start_date = vm.project.plannedStartDate;
                        wbs.type = gantt.config.types.milestone;
                    }
                    if (wbs.objectType == "PROJECTTASK") {
                        wbs.object_type = "Task";
                        wbs.parent = wbs.activity;
                        wbs.assignedTo = wbs.assignedTo != null ? wbs.assignedTo : null;
                        var today = moment(new Date());
                        var todayStr = today.format($rootScope.applicationDateSelectFormat);
                        var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                        var finishDate = moment(wbs.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                        if (todayDate.isAfter(finishDate)) wbs.over_due = "true";
                        else wbs.over_due = "false";
                    }
                    gantt_data.data.push(wbs);
                });
            }

            var ganttId = null;

            $scope.$on('app.project.createSaveGantt', function () {
                saveGantt();
            });

            $scope.$on('app.project.hideGantt', function () {
                $timeout(function () {
                    $rootScope.showBusyIndicator();
                    $rootScope.showGantt = false;
                    width_settings.showGantt = $rootScope.showGantt;
                    $window.localStorage.setItem("widthSettings", JSON.stringify(width_settings));
                    gantt_data.width.showGantt = $rootScope.showGantt;
                    initGantt();
                }, 200);
            });

            $scope.$on('app.project.showGantt', function () {
                $timeout(function () {
                    $rootScope.showBusyIndicator();
                    $rootScope.showGantt = true;
                    width_settings.showGantt = $rootScope.showGantt;
                    $window.localStorage.setItem("widthSettings", JSON.stringify(width_settings));
                    gantt_data.width.showGantt = $rootScope.showGantt;
                    initGantt();
                }, 200);
            });

            function saveGantt() {
                vm.wbsList = [];
                vm.activityList = [];
                vm.milestoneList = [];
                vm.taskList = [];
                vm.valid = true;
                vm.ganttChanges = true;
                var phaseId = 0;
                vm.phaseNames = [];
                vm.activityNames = [];
                vm.milestoneNames = [];
                vm.taskNames = [];
                vm.message = projectPlanSaved;
                $rootScope.ids.length = 0;
                vm.ganttList = GanttEditor.saveGantt();
                angular.forEach(vm.ganttList.data, function (val) {
                    if (vm.valid) {
                        if (val.object_type == "" || val.object_type == null || val.object_type == undefined) {
                            $rootScope.showWarningMessage("Please select type for " + val.text);
                            vm.valid = false;
                        } else {
                            if (JSON.stringify(val.id).length > 10) {
                                ganttId = parseInt(val.id);
                                val.id = null;
                            } else {
                                ganttId = null;
                            }
                            if (val.object_type == "Phase") {
                                phaseId += 1;
                                if (validatePhase(val)) vm.wbsList.push(newWbs(val, phaseId));
                            }
                            if (val.object_type != "Phase" && val.object_type != "Task") {
                                generateSeqNumbers(val);
                                if (val.object_type == "Activity") {
                                    if (validateActivity(val)) vm.activityList.push(newWbsActivity(val, seqId));
                                }
                                if (val.object_type == "Milestone") {
                                    if (validateMilestone(val)) vm.milestoneList.push(newWbsMilestone(val, seqId));
                                }
                            }
                            if (val.object_type == "Task") {
                                if (validateTask(val)) vm.taskList.push(newActivityTask(val));
                            }
                        }
                    }
                });
                if (vm.valid) createWbs();
                //$rootScope.loadProjectPercentageComplete();
            }

            function generateSeqNumbers(val) {
                if (phaseId == null) {
                    phaseId = val.parent;
                }
                else {
                    if (phaseId == val.parent) {
                        seqId++;
                    } else {
                        seqId = 1;
                        phaseId = val.parent;
                    }
                }
            }


            var phaseNameValidation = parsed.html($translate.instant("ENTER_PHASE_NAME")).html();
            var duplicatePhaseNameValidation = parsed.html($translate.instant("PHASE_NAME_VALIDATION")).html();

            function validatePhase(phase) {
                if (phase.text == null || phase.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(phaseNameValidation);
                }
                else if (vm.phaseNames.indexOf(phase.text) > -1) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(phase.text + " : " + duplicatePhaseNameValidation);
                } else {
                    vm.phaseNames.push(phase.text);
                }
                return vm.valid;
            }


            var duplicateActivityNameValidation = parsed.html($translate.instant("ACTIVITY_NAME_VALIDATION")).html();
            var activityNameValidation = parsed.html($translate.instant("ENTER_ACTIVITY_NAME")).html();
            var addPersonValidation = parsed.html($translate.instant("PERSON_ADD_FOR")).html();
            var activityParentId = null;

            function validateActivity(activity) {
                if (activity.text == null || activity.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(activityNameValidation);
                } else if (activity.assignedTo == null || activity.assignedTo == " " || activity.assignedTo == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(addPersonValidation + " : " + activity.text);
                } else if (dateValidation(activity)) {
                    if (activityParentId == null) {
                        activityParentId = activity.parent;
                        vm.activityNames.push(activity.text);
                    }
                    else {
                        if (activityParentId == activity.parent) {
                            if (vm.activityNames.indexOf(activity.text) > -1) {
                                vm.valid = false;
                                $rootScope.showWarningMessage(activity.text + " : " + duplicateActivityNameValidation);
                            } else {
                                vm.activityNames.push(activity.text);
                            }
                        } else {
                            activityParentId = activity.parent;
                            vm.activityNames.length = 0;
                            vm.activityNames.push(activity.text);
                        }
                    }
                } else {
                    vm.valid = false;
                }
                return vm.valid;
            }

            var duplicateMilestoneNameValidation = parsed.html($translate.instant("MILESTONE_NAME_VALIDATION")).html();
            var milestoneNameValidation = parsed.html($translate.instant("ENTER_MILESTONE_NAME")).html();
            var projectPlanSaved = parsed.html($translate.instant("PROJECT_PLAN_SAVED")).html();
            var milestoneParentId = null;

            function validateMilestone(milestone) {
                if (milestone.text == null || milestone.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(milestoneNameValidation);
                } else if (milestone.assignedTo == null || milestone.assignedTo == " " || milestone.assignedTo == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(addPersonValidation + " : " + milestone.text);
                } else if (dateValidation(milestone)) {
                    if (milestoneParentId == null) {
                        milestoneParentId = milestone.parent;
                        vm.milestoneNames.push(milestone.text);
                    }
                    else {
                        if (milestoneParentId == milestone.parent) {
                            if (vm.milestoneNames.indexOf(milestone.text) > -1) {
                                vm.valid = false;
                                $rootScope.showWarningMessage(milestone.text + " : " + duplicateMilestoneNameValidation);
                            } else {
                                vm.milestoneNames.push(milestone.text);
                            }
                        } else {
                            milestoneParentId = milestone.parent;
                            vm.milestoneNames.length = 0;
                            vm.milestoneNames.push(milestone.text);
                        }
                    }
                } else {
                    vm.valid = false;
                }
                return vm.valid;
            }


            var duplicateTaskNameValidation = parsed.html($translate.instant("TASK_NAME_VALIDATION")).html();
            var taskNameValidation = parsed.html($translate.instant("ENTER_TASK_NAME")).html();

            function validateTask(task) {
                if (task.text == null || task.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(taskNameValidation);
                } else if (task.assignedTo == null || task.assignedTo == " " || task.assignedTo == undefined) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(addPersonValidation + " : " + task.text);
                } else if (dateValidation(task)) {
                    if (activityId == null) {
                        activityId = task.parent;
                        vm.taskNames.push(task.text);
                    }
                    else {
                        if (activityId == task.parent) {
                            taskSeqid++;
                            if (vm.taskNames.indexOf(task.text) > -1) {
                                vm.valid = false;
                                $rootScope.showWarningMessage(task.text + " : " + duplicateTaskNameValidation);
                            } else {
                                vm.taskNames.push(task.text);
                            }
                        } else {
                            taskSeqid = 1;
                            activityId = task.parent;
                            vm.taskNames.length = 0;
                            vm.taskNames.push(task.text);
                        }
                    }
                } else {
                    vm.valid = false;
                }
                return vm.valid;
            }


            function dateValidation(val) {
                var projectPlannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                if (val.start_date != null) {
                    var plannedStartDate = moment(val.start_date, $rootScope.applicationDateSelectFormat);
                    if (plannedStartDate.isBefore(projectPlannedStartDate) || plannedStartDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Start Date should be in b/w project Planned Start & Finish Date for task : " + val.name);
                    }
                }
                if (val.end_date != null) {
                    var plannedFinishDate = moment(val.end_date, $rootScope.applicationDateSelectFormat);
                    if (plannedFinishDate.isBefore(projectPlannedStartDate) || plannedFinishDate.isAfter(projectPlannedFinishDate)) {
                        vm.valid = false;
                        $rootScope.showErrorMessage("Planned Finish Date should be in b/w project Planned Start & Finish Date for task : " + val.name);
                    }
                }
                return vm.valid;
            }

            function newWbs(wbs, seqId) {
                var newWbs = {
                    id: wbs.id,
                    name: wbs.text,
                    description: wbs.description != null ? wbs.description : null,
                    plannedStartDate: wbs.start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    plannedFinishDate: wbs.end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    createdDate: wbs.created_date,
                    createdBy: wbs.created_by,
                    parent: null,
                    project: vm.project,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: seqId
                };
                return newWbs;
            }

            function newWbsActivity(wbs, seqId) {
                var percent = wbs.progress * 100;
                var status = null;
                if (percent == 0) status = "PENDING";
                else if (percent > 0 && percent < 100) status = "INPROGRESS";
                else if (percent == 100) status = "FINISHED";
                var newWbsActivity = {
                    id: wbs.id,
                    name: wbs.text,
                    status: status,
                    description: wbs.description,
                    duration: wbs.duration,
                    percentComplete: wbs.progress * 100,
                    workflow: wbs.workflow,
                    actualStartDate: wbs.actual_start_date != "" ? wbs.actual_start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider) : null,
                    actualFinishDate: wbs.actual_end_date != "" ? wbs.actual_end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider) : null,
                    plannedStartDate: wbs.start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    plannedFinishDate: wbs.end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    assignedTo: wbs.assignedTo != "" ? parseInt(wbs.assignedTo) : null,
                    createdDate: wbs.created_date,
                    createdBy: wbs.created_by,
                    wbs: wbs.parent != null ? parseInt(wbs.parent) : null,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: seqId
                };
                return newWbsActivity;
            }

            function newWbsMilestone(wbs, seqId) {
                var newWbsMilestone = {
                    id: wbs.id,
                    name: wbs.text,
                    description: wbs.description,
                    percentComplete: wbs.progress * 100,
                    plannedStartDate: wbs.start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    plannedFinishDate: wbs.end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    actualFinishDate: wbs.actual_end_date != "" ? wbs.actual_end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider) : null,
                    assignedTo: wbs.assignedTo != "" ? parseInt(wbs.assignedTo) : null,
                    status: wbs.status == undefined ? "PENDING" : wbs.status,
                    createdDate: wbs.created_date,
                    createdBy: wbs.created_by,
                    predecessors: [],
                    wbs: wbs.parent != null ? parseInt(wbs.parent) : null,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: seqId
                };
                return newWbsMilestone;
            }

            function newActivityTask(wbs) {
                var percent = wbs.progress * 100;
                var status = null;
                if (percent == 0) status = "PENDING";
                else if (percent > 0 && percent < 100) status = "INPROGRESS";
                else if (percent == 100) status = "FINISHED";
                var newActivityTask = {
                    id: wbs.id,
                    name: wbs.text,
                    status: status,
                    duration: wbs.duration,
                    description: wbs.description,
                    plannedStartDate: wbs.start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    plannedFinishDate: wbs.end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider),
                    actualFinishDate: wbs.actual_end_date != "" ? wbs.actual_end_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider) : null,
                    actualStartDate: wbs.actual_start_date != "" ? wbs.actual_start_date.slice(0, 10).split("-").join($rootScope.applicationDateSelectFormatDivider) : null,
                    percentComplete: wbs.progress * 100,
                    createdDate: wbs.created_date,
                    createdBy: wbs.created_by,
                    required: false,
                    assignedTo: wbs.assignedTo != "" ? parseInt(wbs.assignedTo) : null,
                    activity: wbs.parent != null ? parseInt(wbs.parent) : null,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: taskSeqid,
                    workflow: wbs.workflow
                };
                return newActivityTask;
            }

            function createWbs() {
                $rootScope.showBusyIndicator();
                ProjectService.createWBSElements(projectId, vm.wbsList).then(
                    function (data) {
                        var ids = null;
                        vm.resultWbsList = data;
                        angular.forEach(vm.resultWbsList, function (wbs) {
                            if (wbs.ganttId != null) {
                                ids = {"cassiniId": wbs.id, "ganttId": wbs.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.activityList.length > 0) createActivity();
                        if (vm.milestoneList.length > 0) createMilestone();
                        if (vm.activityList.length == 0 && vm.milestoneList.length == 0) {
                            $rootScope.hideBusyIndicator();
                            updateSequenceNumbers();
                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.ganttUpdated = true;
                    }
                );
            }

            function createActivity() {
                angular.forEach(vm.activityList, function (activity) {
                    angular.forEach($rootScope.ids, function (id) {
                        if (activity.wbs == parseInt(id.ganttId)) {
                            activity.wbs = parseInt(id.cassiniId);
                        }
                        else if (activity.wbs == parseInt(id.cassiniId)) {
                            activity.wbs = parseInt(id.cassiniId);
                        }
                    });
                });
                ActivityService.createActivites(projectId, vm.activityList).then(
                    function (data) {
                        var ids = null;
                        vm.resultActivityList = data;
                        angular.forEach(vm.resultActivityList, function (activity) {
                            if (activity.ganttId != null) {
                                ids = {"cassiniId": activity.id, "ganttId": activity.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.taskList.length > 0) createTask();
                        else if (vm.ganttList.links.length > 0) createLinks();
                        if (vm.milestoneList.length == 0 && vm.taskList.length == 0) {
                            $rootScope.hideBusyIndicator();
                            updateSequenceNumbers();
                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.ganttUpdated = true;
                    }
                )
            }

            function createMilestone() {
                var wbsId = null;
                angular.forEach(vm.milestoneList, function (milestone) {
                    angular.forEach($rootScope.ids, function (id) {
                        if (milestone.wbs == parseInt(id.ganttId)) {
                            milestone.wbs = parseInt(id.cassiniId);
                        }
                        else if (milestone.wbs == parseInt(id.cassiniId)) {
                            milestone.wbs = parseInt(id.cassiniId);
                        }
                    });
                    wbsId = milestone.wbs;
                });
                MilestoneService.createMilestones(projectId, wbsId, vm.milestoneList).then(
                    function (data) {
                        var ids = null;
                        vm.resultMilestoneList = data;
                        angular.forEach(vm.resultMilestoneList, function (milestone) {
                            if (milestone.ganttId != null) {
                                ids = {"cassiniId": milestone.id, "ganttId": milestone.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.taskList.length == 0) createLinks();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.ganttUpdated = true;
                    }
                )
            }

            function createTask() {
                var activityId = null;
                angular.forEach(vm.taskList, function (task) {
                    angular.forEach($rootScope.ids, function (id) {
                        if (task.activity == parseInt(id.ganttId)) {
                            task.activity = parseInt(id.cassiniId);
                        }
                        else if (task.activity == parseInt(id.cassiniId)) {
                            task.activity = parseInt(id.cassiniId);
                        }
                    });
                    activityId = task.activity;
                });
                ActivityService.createActivityTasks(projectId, activityId, vm.taskList).then(
                    function (data) {
                        var ids = null;
                        vm.resultTaskList = data;
                        angular.forEach(vm.resultTaskList, function (task) {
                            if (task.ganttId != null) {
                                ids = {"cassiniId": task.id, "ganttId": task.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.ganttList.links.length > 0) $rootScope.ganttUpdated = false;
                        createLinks();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.ganttUpdated = true;
                    }
                )
            }

            function createLinks() {
                vm.links = [];
                var links = vm.ganttList.links;
                if (links.length > 0) {
                    angular.forEach(links, function (link) {
                        var sourceId = parseInt(link.source);
                        var targetId = parseInt(link.target);
                        if ($rootScope.ids != null) {
                            angular.forEach($rootScope.ids, function (id) {
                                if (sourceId == id.ganttId) link.source = id.cassiniId;
                                if (targetId == id.ganttId) link.target = id.cassiniId;
                            })
                        }
                    });
                    angular.forEach(links, function (link) {
                        var sourceId = parseInt(link.source);
                        var targetId = parseInt(link.target);
                        var linkExists = false;
                        if (vm.links.length > 0) {
                            angular.forEach(vm.links, function (link1) {
                                if (sourceId == link1.source && targetId == link1.target && !linkExists) {
                                    linkExists = true;
                                }
                            });
                            if (!linkExists) vm.links.push(link);
                        } else {
                            vm.links.push(link);
                        }
                    });
                }
                ProjectService.createLinks(projectId, JSON.stringify(vm.links)).then(
                    function (data) {
                        updateSequenceNumbers();
                    },
                    function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.ganttUpdated = true;
                    }
                )
            }

            vm.selectWbs = selectWbs;
            //vm.editWbs = editWbs;
            vm.toggleNode = toggleNode;
            vm.deleteWbs = deleteWbs;
            vm.finishMilestone = finishMilestone;

            vm.newWbs = {
                id: null,
                name: null,
                description: null,
                parent: null,
                project: vm.project
            };

            var parsed = angular.element("<div></div>");

            vm.clickToEditTitle = parsed.html($translate.instant("CLICK_TO_EDIT")).html();
            vm.clickToFinishTitle = parsed.html($translate.instant("CLICK_TO_FINISH")).html();
            vm.clickToDeleteTitle = parsed.html($translate.instant("CLICK_TO_DELETE")).html();
            vm.clickToSelectWbsTitle = parsed.html($translate.instant("CLICK_TO_SELECT_WBS")).html();
            vm.activityTitle = parsed.html($translate.instant("ACTIVITY")).html();
            vm.milestoneTitle = parsed.html($translate.instant("MILESTONE")).html();
            var wbsUpdatedMessage = parsed.html($translate.instant("WBS_UPDATED_MESSAGE")).html();
            var wbsCreatedMessage = parsed.html($translate.instant("WBS_CREATED_MESSAGE")).html();
            vm.createWbs = parsed.html($translate.instant("CREATE_WBS")).html();
            vm.addActivity = parsed.html($translate.instant("ADD_ACTIVITYS")).html();
            var updateWbs = parsed.html($translate.instant("UPDATE_WBS")).html();
            var editWbsTitle = parsed.html($translate.instant("EDIT_WBS")).html();
            //var createActivity = parsed.html($translate.instant("CREATE_ACTIVITY")).html();
            var updateActivity = parsed.html($translate.instant("UPDATE_ACTIVITY")).html();
            var editActivityTitle = parsed.html($translate.instant("EDIT_ACTIVITY")).html();
            //var createMilestone = parsed.html($translate.instant("CREATE_MILESTONE")).html();
            var updateMilestone = parsed.html($translate.instant("UPDATE_MILESTONE")).html();
            var editMilestone = parsed.html($translate.instant("EDIT_MILESTONE")).html();
            var newWBS = parsed.html($translate.instant("NEW_WBS")).html();
            var newActivity = parsed.html($translate.instant("NEW_ACTIVITY")).html();
            var newMilestone = parsed.html($translate.instant("NEW_MILESTONE")).html();
            var wbsDeletedMessage = parsed.html($translate.instant("WBS_DELETED_MESSAGE")).html();
            var phaseDeletedMessage = parsed.html($translate.instant("PHASE_DELETED_MESSAGE")).html();
            var activityDeletedMessage = parsed.html($translate.instant("ACTIVITY_DELETED_MESSAGE")).html();
            var milestoneDeletedMessage = parsed.html($translate.instant("MILESTONE_DELETED_MESSAGE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var DeletedMessage = parsed.html($translate.instant("PROJECT_DELETED_MESSAGE")).html();
            var createTemplate = parsed.html($translate.instant("CREATE_TEMPLATE")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            var add = parsed.html($translate.instant("ADD")).html();
            var templateCreatedMessage = parsed.html($translate.instant("TEMPLATE_CREATE_MESSAGE")).html();
            var templateAddedMessage = parsed.html($translate.instant("TEMPLATE_ADDED_MESSAGE")).html();
            var newTemplate = parsed.html($translate.instant("NEW_TEMPLATE")).html();
            var addTemplate = parsed.html($translate.instant("ADD_TEMPLATE")).html();
            var milestoneUpdateMsg = parsed.html($translate.instant("MILESTONE_UPDATED_MESSAGE")).html();
            var copyWbsMessage = parsed.html($translate.instant("COPY_WBS_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var sequenceUpdateMessage = parsed.html($translate.instant("SEQUENCE_UPDATED_MESSAGE")).html();
            var taskDeletedMessage = parsed.html($translate.instant("TASK_DELETED_MESSAGE")).html();
            vm.showTasks = parsed.html($translate.instant("CLICK_TO_SHOW_TASKS")).html();
            vm.Tasks = parsed.html($translate.instant("TASKS")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();

            function selectWbs(wbs) {
                wbs.selected = !wbs.selected;
                if (wbs.selected == true) {
                    lastSelectedWbs = wbs;
                    if (wbs.children == 0) {

                    }
                    $rootScope.showCreateDuplicateWbs = true;
                    $rootScope.showWbsButton = false;
                    $rootScope.showActivityAndMilestone = true;
                }
                else {
                    wbs.selected = false;
                    lastSelectedWbs = null;
                    $rootScope.showWbsButton = true;
                    $rootScope.showActivityAndMilestone = false;
                    $rootScope.showCreateDuplicateWbs = false;
                }

                angular.forEach(vm.wbsItems, function (wbsItem) {
                    if (wbs.id != wbsItem.id) {
                        wbsItem.selected = false;
                    }
                })
            }

            function loadProjectPlan() {
                vm.wbsItems = [];
                ProjectService.getProjectWbsStructure(projectId).then(
                    function (data) {
                        vm.projectWbs = data;
                        $rootScope.ganttData = data;
                        //populateBomItems(data);
                        populateChildren(data);
                        gantt_data.data = [];
                        loadGanttData();
                        initGantt();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateChildren(data) {
                angular.forEach(data, function (item) {
                    item.newName = item.name;
                    item.newDescription = item.description;
                    item.isNew = false;
                    item.editMode = false;
                    if (item.percentComplete != undefined) {
                        if (item.percentComplete < 100 && item.percentComplete > 0) {
                            item.percentComplete = parseInt(item.percentComplete);
                        }
                    }

                    if (lastSelectedWbs != null) {
                        if (lastSelectedWbs.id == item.id) {
                            item.selected = true;
                        }
                    }
                    if (item.objectType == "PROJECTPHASEELEMENT") {
                        if (item.children.length > 0) item.hasChildren = true;
                        else item.hasChildren = false;
                    }
                    vm.wbsItems.push(item);
                    if (item.activityTasks.length > 0) {
                        angular.forEach(item.activityTasks, function (task) {
                            vm.wbsItems.push(task);
                        })
                    }
                    populateChildren(item.children);
                    CommonService.getPersonReferences(item.children, 'modifiedBy');
                    CommonService.getPersonReferences(item.children, 'createdBy');
                    loadActivityAttributeValues();
                });
            }


            function populateBomItems(data) {
                angular.forEach(data, function (item) {
                    item.newName = item.name;
                    item.newDescription = item.description;
                    item.isNew = false;
                    item.editMode = false;
                    if (item.percentComplete != undefined) {
                        if (item.percentComplete < 100 && item.percentComplete > 0) {
                            item.percentComplete = parseInt(item.percentComplete);
                        }
                    }

                    if (lastSelectedWbs != null) {
                        if (lastSelectedWbs.id == item.id) {
                            item.selected = true;
                        }
                    }
                    vm.wbsItems.push(item);
                    populateBomItems(item.activities);
                    populateBomItems(item.milestones);
                    CommonService.getPersonReferences(item.activities, 'modifiedBy');
                    CommonService.getPersonReferences(item.activities, 'createdBy');
                    loadActivityAttributeValues();
                });
            }

            function loadProject() {
                ProjectService.getProject(projectId).then(
                    function (data) {
                        vm.project = data;
                        if (!$rootScope.loginPersonDetails.external) {
                            gantt_data.hasPermission = $rootScope.hasPermission('project', 'create') || $rootScope.hasPermission('project', 'edit') ||
                                $rootScope.loginPersonDetails.isAdmin || $rootScope.loginPersonDetails.person.id == vm.project.projectManager;
                        } else {
                            gantt_data.hasPermission = $rootScope.sharedProjectPermission == 'WRITE' && ($rootScope.hasPermission('project', 'create') || $rootScope.hasPermission('project', 'edit'));
                        }
                        gantt_data.projectView = $rootScope.hasPermission('project', 'view') || $rootScope.loginPersonDetails.isAdmin || $rootScope.loginPersonDetails.person.id == vm.project.projectManager;
                        gantt_data.hasDelete = $rootScope.hasPermission('project', 'delete') || $rootScope.loginPersonDetails.isAdmin || $rootScope.loginPersonDetails.person.id == vm.project.projectManager;
                        gantt_data.projectManager = vm.project.projectManager;
                        gantt_data.projectStartDate = vm.project.plannedStartDate;
                        gantt_data.projectFinishDate = vm.project.plannedFinishDate;
                        var widthSetting = JSON.parse($window.localStorage.getItem("widthSettings"));
                        if (widthSetting != null) {
                            gantt_data.width.showGantt = widthSetting.showGantt != false;
                        }
                        else {
                            gantt_data.width.showGantt = $rootScope.showGantt;
                        }
                        if (vm.project.plannedStartDate != null && vm.project.plannedStartDate != "" && vm.project.plannedFinishDate != null && vm.project.plannedFinishDate != "") {
                            loadProjectPlan();
                        } else {
                            $rootScope.showWarningMessage("Please add project planned start and finish dates");
                            $rootScope.navigateProjectTab("details.basic");
                            $rootScope.hideBusyIndicator();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.persons = [];
            function loadPersons() {
                ProjectService.getAllProjectMembers(projectId).then(
                    function (data) {
                        gantt_data.persons = [];
                        angular.forEach(data, function (person) {
                            gantt_data.persons.push({key: person.id, label: person.fullName, active: person.active});
                            $rootScope.ganttPersons.push(person.fullName);
                        });
                        vm.persons = gantt_data.persons;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function getIndexTopInsertNewChild(lastSelected) {
                var index = 0;

                if (lastSelected.children != undefined && lastSelected.children != null) {
                    index = lastSelected.children.length;
                    angular.forEach(lastSelected.children, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            function toggleNode(wbs) {
                wbs.expanded = !wbs.expanded;
                var index = vm.wbsItems.indexOf(wbs);
                if (wbs.expanded == false) {
                    removeChildren(wbs);
                } else {
                    ProjectService.getWbsChildren(projectId, wbs.id).then(
                        function (data) {
                            wbs.children = [];
                            wbs.children = data;

                            angular.forEach(wbs.children, function (wbsChild) {
                                index = index + 1;
                                vm.wbsItems.splice(index, 0, wbsChild);
                                loadActivityAttributeValues();
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeChildren(wbs) {
                if (wbs.children != null && wbs.children != undefined) {
                    angular.forEach(wbs.children, function (item) {
                        var index = vm.wbsItems.indexOf(item);
                        vm.wbsItems.splice(index, 1);
                    });
                    wbs.expanded = false;
                }

            }

            $scope.$on('app.project.ganttUpdated', ganttUpdated);
            function ganttUpdated() {
                $rootScope.ganttUpdated = true;
            }

            $scope.$on('app.project.saveWidths', saveWidths);
            function saveWidths(event, args) {
                var columnName = args.columnName;
                var newWidth = args.newWidth;
                if (columnName == "text") width_settings.text = newWidth;
                if (columnName == "object_type") width_settings.object_type = newWidth;
                if (columnName == "assignedTo") width_settings.assignedTo = newWidth;
                if (columnName == "duration") width_settings.duration = newWidth;
                if (columnName == "start_date") width_settings.start_date = newWidth;
                if (columnName == "end_date") width_settings.end_date = newWidth;
                if (columnName == "actual_start_date") width_settings.actual_start_date = newWidth;
                if (columnName == "actual_end_date") width_settings.actual_end_date = newWidth;
                if (columnName == "grid") width_settings.grid = newWidth;
                $window.localStorage.setItem("widthSettings", JSON.stringify(width_settings));
            }

            function loadWidths() {
                vm.width = {};
                var widthSetting = JSON.parse($window.localStorage.getItem("widthSettings"));
                if (widthSetting != null) {
                    vm.width.text = widthSetting.text != null ? widthSetting.text : default_width.text;
                    vm.width.object_type = widthSetting.object_type != null ? widthSetting.object_type : default_width.object_type;
                    vm.width.assignedTo = widthSetting.assignedTo != null ? widthSetting.assignedTo : default_width.assignedTo;
                    vm.width.duration = widthSetting.duration != null ? widthSetting.duration : default_width.duration;
                    vm.width.start_date = widthSetting.start_date != null ? widthSetting.start_date : default_width.start_date;
                    vm.width.end_date = widthSetting.end_date != null ? widthSetting.end_date : default_width.end_date;
                    vm.width.actual_start_date = widthSetting.actual_start_date != null ? widthSetting.actual_start_date : default_width.actual_start_date;
                    vm.width.actual_end_date = widthSetting.actual_end_date != null ? widthSetting.actual_end_date : default_width.actual_end_date;
                    vm.width.grid = widthSetting.grid != null ? widthSetting.grid : default_width.grid;
                    gantt_data.width = vm.width;
                } else {
                    gantt_data.width = default_width;
                }
            }


            $scope.$on('app.project.deleteWbs', deleteWbsElement);
            var options = null;

            function deleteWbsElement(event, args) {
                var id = args.wbsId;
                var name = args.name;
                options = {
                    title: deleteDialogTitle,
                    message: deleteDialogMessage + " [ " + name + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deleteWBSElement(projectId, id).then(
                            function (data) {
                                vm.ganttChanges = true;
                                vm.message = phaseDeletedMessage;
                                $rootScope.showWbsButton = true;
                                $rootScope.showActivityAndMilestone = false;
                                $rootScope.showCreateDuplicateWbs = false;
                                updateSequenceNumbers();
                            }, function (error) {
                                updateSequenceNumbers();
                            }
                        )
                    }
                })
            }

            var activityDeleteValidationMessage = parsed.html($translate.instant("ACTIVITY_CANNOT_DELETE")).html();
            var finishedActivityDeleteValidationMessage = parsed.html($translate.instant("FINISHED_ACTIVITY_CANNOT_DELETE")).html();
            var startedActivityDeleteValidationMessage = parsed.html($translate.instant("STARTED_ACTIVITY_CANNOT_DELETE")).html();
            $scope.$on('app.project.deleteActivity', deleteActivity);
            function deleteActivity(event, args) {
                var id = args.activityId;
                var name = args.name;
                var status = args.status;
                if (status == 'PENDING') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + name + " ] " + vm.activityTitle + " ?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ActivityService.deleteActivity(id).then(
                                function (data) {
                                    vm.ganttChanges = true;
                                    vm.message = activityDeletedMessage;
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                    updateSequenceNumbers();
                                }, function (error) {
                                    updateSequenceNumbers();
                                }
                            )
                        }
                    });
                } else if (status == 'FINISHED') {
                    $rootScope.showWarningMessage(finishedActivityDeleteValidationMessage);
                } else {
                    $rootScope.showWarningMessage(startedActivityDeleteValidationMessage);
                }
            }

            var milestoneDeleteValidationMessage = parsed.html($translate.instant("MILESTONE_CANNOT_DELETE")).html();
            $scope.$on('app.project.deleteMilestone', deleteMilestone);
            function deleteMilestone(event, args) {
                var id = args.milestoneId;
                var wbsId = args.parentId;
                var name = args.name;
                var status = args.status;
                if (status == 'PENDING') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + name + " ] " + vm.milestoneTitle + "?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            MilestoneService.deleteMilestoneById(wbsId, id).then(
                                function (data) {
                                    vm.ganttChanges = true;
                                    vm.message = milestoneDeletedMessage;
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                    updateSequenceNumbers();
                                    $scope.$off('app.project.deleteMilestone', deleteMilestone);
                                }, function (error) {
                                    updateSequenceNumbers();
                                    $scope.$off('app.project.deleteMilestone', deleteMilestone);
                                }
                            )
                        }
                    })
                } else {
                    $rootScope.showWarningMessage(milestoneDeleteValidationMessage);
                }

            }

            var taskDialogTitle = parsed.html($translate.instant("TASK_DIALOG_TITLE")).html();
            var taskDialogMessage = parsed.html($translate.instant("TASK_DIALOG_MESSAGE")).html();
            var taskDeleteValidationMessage = parsed.html($translate.instant("TASK_CANNOT_DELETE")).html();
            var finishedtaskDeleteValidationMessage = parsed.html($translate.instant("FINISHED_TASK_CAN_NOT_DELETE")).html();
            var pendingtaskDeleteValidationMessage = parsed.html($translate.instant("STARTED_TASK_CAN_NOT_DELETE")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE")).html();
            $scope.$on('app.project.deleteTask', deleteTask);
            function deleteTask(event, args) {
                var id = args.taskId;
                var activityId = args.parentId;
                var name = args.name;
                var status = args.status;
                if (status == 'PENDING') {
                    var options = {
                        title: taskDialogTitle,
                        message: taskDialogMessage + " [ " + name + " ]" + itemDelete + "?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ActivityService.deleteActivityTask(activityId, id).then(
                                function (data) {
                                    vm.ganttChanges = true;
                                    vm.message = taskDeletedMessage;
                                    updateSequenceNumbers();
                                }, function (error) {
                                    updateSequenceNumbers();
                                }
                            )
                        }
                    });
                } else if (status == 'FINISHED') {
                    $rootScope.showWarningMessage(finishedtaskDeleteValidationMessage);
                }
                else {
                    $rootScope.showWarningMessage(pendingtaskDeleteValidationMessage);
                }

            }

            function deleteWbs(wbs) {
                var options = null;
                if (wbs.objectType == 'PROJECTPHASEELEMENT') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + wbs.name + " ] " + itemDelete + "?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ProjectService.deleteWBSElement(projectId, wbs.id).then(
                                function (data) {
                                    var index = vm.wbsItems.indexOf(wbs);
                                    vm.wbsItems.splice(index, 1);
                                    $rootScope.showSuccessMessage(wbsDeletedMessage);
                                    loadProjectPlan();
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                } else if (wbs.objectType == 'PROJECTACTIVITY') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + wbs.name + " ] " + vm.activityTitle + " ?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ActivityService.deleteActivity(wbs.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(activityDeletedMessage);
                                    loadProjectPlan();
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                } else if (wbs.objectType == 'PROJECTMILESTONE') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + wbs.name + " ] " + vm.milestoneTitle + "?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            MilestoneService.deleteMilestone(wbs.parent, wbs).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(milestoneDeletedMessage);
                                    loadProjectPlan();
                                    $rootScope.showWbsButton = true;
                                    $rootScope.showActivityAndMilestone = false;
                                    $rootScope.showCreateDuplicateWbs = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                }

            }

            var addMember = parsed.html($translate.instant("ADD_MEMBERS_TO_PROJECT")).html();
            $scope.$on('app.project.addMembers', addMembers);
            function addMembers() {
                $rootScope.ganttUpdated = false;
                $rootScope.showWarningMessage(addMember);
            }

            var linkDelete = parsed.html($translate.instant("LINK_DELETED")).html();
            $scope.$on('app.project.linkDeleted', linkDeleted);
            function linkDeleted(event, args) {
                var message = linkDelete + ":" + args.source + " - " + args.target;
                $rootScope.showWarningMessage(message);
            }

            var taskUpdateMsg = parsed.html($translate.instant("TASK_UPDATE_MSG")).html();
            var activityUpdateMsg = parsed.html($translate.instant("ACTIVITY_UPDATE_MSG")).html();
            var assignedToMsg = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var addPlannedStartFinishDates = parsed.html($translate.instant("ADD_PLANNED_START_FINISH_DATES")).html();
            var addPlannedFinishDates = parsed.html($translate.instant("ADD_PLANNED_FINISH_DATES")).html();
            var assignedOrPmMsg = parsed.html($translate.instant("ASSIGNED_OR_PM_VALIDATION")).html();
            var taskWorkflowComplete = parsed.html($translate.instant("TASK_WORKFLOW_COMPLETE")).html();
            var projectSaveValidation = parsed.html($translate.instant("PROJECT_SAVE_VALIDATION")).html();
            vm.select = parsed.html($translate.instant("SELECT")).html();
            $scope.$on('app.project.finishActivityTask', finishActivityTask);
            $rootScope.finishActivityTask = finishActivityTask;
            vm.finishActivityTask = finishActivityTask;
            function finishActivityTask(event, args) {
                var taskId = args.taskId;
                var activityId = args.activityId;
                var task = null;
                var finishTask = true;
                angular.forEach(vm.wbsItems, function (item) {
                    if (item.id == taskId && finishTask) {
                        task = item;
                        if (task.assignedTo == null) {
                            $rootScope.showWarningMessage(assignedToMsg);
                            finishTask = false;
                        } else if (task.plannedStartDate == null || task.plannedStartDate == "" || task.plannedFinishDate == null || task.plannedFinishDate == "") {
                            $rootScope.showWarningMessage(addPlannedStartFinishDates.format(task.name));
                            finishTask = false;
                        } else if (task.assignedTo != $rootScope.loginPersonDetails.person.id && $rootScope.loginPersonDetails.person.id != vm.project.projectManager) {
                            $rootScope.showWarningMessage(assignedOrPmMsg);
                            finishTask = false;
                        } else if (task.workflow != null && !task.finishedWorkflow) {
                            $rootScope.showWarningMessage(taskWorkflowComplete);
                            finishTask = false;
                        }
                        else {
                            task.percentComplete = 100;
                            task.status = "FINISHED";
                        }
                    }
                });
                if (finishTask) {
                    if (task.objectType == "PROJECTACTIVITY") {
                        task.wbs = activityId;
                        ActivityService.updateActivity(task).then(
                            function (data) {
                                vm.activity = data;
                                vm.ganttChanges = true;
                                vm.message = activityUpdateMsg;
                                if (args.fromTaskDetails == null) updateSequenceNumbers();
                                $rootScope.loadProjectPercentageComplete();
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                            }
                        )
                    } else {
                        ActivityService.updateActivityTask(activityId, task).then(
                            function (data) {
                                vm.activityTask = data;
                                vm.ganttChanges = true;
                                vm.message = taskUpdateMsg;
                                if (args.fromTaskDetails == null) updateSequenceNumbers();
                                $rootScope.loadProjectPercentageComplete();
                            }, function (error) {
                                $rootScope.showWarningMessage(error.message);
                            }
                        )
                    }
                }
            }

            $scope.$on('app.project.finishMilestone', finishMilestone);
            function finishMilestone(event, args) {
                var milestoneId = args.milestoneId;
                var phaseId = args.phaseId;
                var milestone = null;
                var finishMilestone = true;
                angular.forEach(vm.wbsItems, function (item) {
                    if (item.id == milestoneId && finishMilestone) {
                        milestone = item;
                        if (milestone.assignedTo == null) {
                            $rootScope.showWarningMessage(assignedToMsg);
                            finishMilestone = false;
                        } else if (milestone.plannedFinishDate == null || milestone.plannedFinishDate == "") {
                            $rootScope.showWarningMessage(addPlannedFinishDates.format(item.name));
                            finishMilestone = false;
                        } else if (milestone.assignedTo != $rootScope.loginPersonDetails.person.id && $rootScope.loginPersonDetails.person.id != vm.project.projectManager) {
                            $rootScope.showWarningMessage(assignedOrPmMsg);
                            finishMilestone = false;
                        }
                        else {
                            milestone.wbs = phaseId;
                            milestone.status = "FINISHED";
                        }
                    }
                });
                if (finishMilestone) {
                    MilestoneService.finishWbsMilestone(phaseId, milestone).then(
                        function (data) {
                            vm.milestone = data;
                            updateSequenceNumbers();
                            vm.ganttChanges = true;
                            vm.message = milestoneUpdateMsg;
                            $rootScope.loadProjectPercentageComplete();
                            $scope.$off('app.project.finishMilestone', finishMilestone);
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            $scope.$off('app.project.finishMilestone', finishMilestone);
                        }
                    )
                }
            }

            $scope.$on('app.project.openActivityDetails', openActivityDetails);
            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(event, args) {
                var wbsId = args.activityId;
                var permission = args.permission;
                if (JSON.stringify(wbsId).length > 10) {
                    $rootScope.showWarningMessage(projectSaveValidation);
                } else {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify(vm.projectPlanTabId));
                    $state.go('app.pm.project.activity.details', {
                        activityId: wbsId,
                        tab: 'details.basic',
                        permission: permission
                    });
                }
            }

            $scope.$on('app.project.openTaskDetails', openTaskDetails);
            vm.openTaskDetails = openTaskDetails;
            function openTaskDetails(event, args) {
                var taskId = args.taskId;
                var activityId = args.activityId;
                var permission = args.permission;
                if (JSON.stringify(taskId).length > 10) {
                    $rootScope.showWarningMessage(projectSaveValidation);
                } else {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify(vm.projectPlanTabId));
                    $state.go('app.pm.project.activity.task.details', {
                        activityId: activityId,
                        taskId: taskId,
                        tab: 'details.basic',
                        permission: permission
                    });
                }
            }

            vm.activityPopover = {
                templateUrl: 'app/desktop/modules/pm/project/details/tabs/plan/activityPopoverTemplate.jsp'
            };

            function createNewTemplate() {
                var options = {
                    title: newTemplate,
                    template: 'app/desktop/modules/template/new/newTemplateView.jsp',
                    controller: 'NewTemplateController as newTemplateVm',
                    resolve: 'app/desktop/modules/template/new/newTemplateController',
                    width: 600,
                    showMask: true,
                    data: {
                        projectPlan: vm.project
                    },
                    buttons: [
                        {text: create, broadcast: 'app.project.template.new'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(templateCreatedMessage);
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function addNewTemplate() {
                var options = {
                    title: addTemplate,
                    template: 'app/desktop/modules/template/new/addTemplateView.jsp',
                    controller: 'AddTemplateController as addTemplateVm',
                    resolve: 'app/desktop/modules/template/new/addTemplateController',
                    width: 600,
                    showMask: true,
                    data: {
                        projectPlan: vm.project
                    },
                    buttons: [
                        {text: add, broadcast: 'app.project.template.add'}
                    ],
                    callback: function (data) {
                        loadPersons();
                        updateSequenceNumbers();
                        $rootScope.showSuccessMessage(templateAddedMessage);
                        $rootScope.hideSidePanel();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function createDuplicateWbs() {
                ProjectService.copyWbs(projectId, lastSelectedWbs.id).then(
                    function (data) {
                        lastSelectedWbs = null;
                        $rootScope.showWbsButton = true;
                        $rootScope.showCreateDuplicateWbs = false;
                        $rootScope.showActivityAndMilestone = false;
                        loadProjectPlan();
                        $rootScope.showSuccessMessage(copyWbsMessage);
                    },
                    function (error) {
                        $rootScope.showWarningMessage(error.message);
                    }
                )
            }

            vm.selectedAttributes = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            vm.itemIds = [];
            vm.attributeIds = [];
            var currencyMap = new Hashtable();
            $rootScope.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTypeAttributes() {
                var selectedAttributes = angular.copy(vm.selectedAttributes);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/activityTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activityTypeAttributesController',
                    controller: 'ActivityTypeAttributesController as activityTypeAttributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: selectedAttributes
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("allActivityattributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadProjectPlan();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("allActivityattributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allActivityattributes"));
                    //JSON.parse($window.localStorage.getItem("requirements"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal23');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            $('#myModalHorizontal').on('hidden', function () {
                $(this).data('modal').$element.removeData();
            });

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);

            }

            /*    Show Modal dialogue for RichText*/
            vm.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data;
                mymodal.modal('show');
            }

            function loadActivityAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.wbsItems, function (wbs) {
                    if (wbs.objectType == "PROJECTACTIVITY" || wbs.objectType == "PROJECTMILESTONE") {
                        if (wbs.plannedStartDate) {
                            wbs.plannedStartDatede = moment(wbs.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (wbs.plannedFinishDate) {
                            wbs.plannedFinishDatede = moment(wbs.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (wbs.actualStartDate) {
                            wbs.actualStartDatede = moment(wbs.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        if (wbs.actualFinishDate) {
                            wbs.actualFinishDatede = moment(wbs.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        }
                        vm.itemIds.push(wbs.id);
                    }
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.wbsItems, function (item) {

                                if (item.objectType == "PROJECTACTIVITY") {
                                    var attributes = [];

                                    var itemAttributes = vm.selectedObjectAttributes[item.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
                                    angular.forEach(attributes, function (attribute) {
                                        var selectatt = map.get(attribute.id.attributeDef);
                                        if (selectatt != null) {
                                            var attributeName = selectatt.id;
                                            if (selectatt.dataType == 'TEXT') {
                                                item[attributeName] = attribute.stringValue;
                                            } else if (selectatt.dataType == 'LONGTEXT') {
                                                item[attributeName] = attribute.longTextValue;
                                            } else if (selectatt.dataType == 'RICHTEXT') {
                                                item[attributeName] = attribute;
                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'TIME') {
                                                item[attributeName] = attribute.timeValue;
                                            } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                                item[attributeName] = attribute.listValue;
                                            } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                                item[attributeName] = attribute.mlistValue;
                                            } else if (selectatt.dataType == 'TIMESTAMP') {
                                                item[attributeName] = attribute.timestampValue;

                                            } else if (selectatt.dataType == 'HYPERLINK') {
                                                item[attributeName] = attribute.hyperLinkValue;

                                            } else if (selectatt.dataType == 'CURRENCY') {
                                                item[attributeName] = attribute.currencyValue;
                                                if (attribute.currencyType != null) {
                                                    item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                                }
                                            } else if (selectatt.dataType == 'ATTACHMENT') {
                                                var revisionAttachmentIds = [];
                                                if (attribute.attachmentValues.length > 0) {
                                                    angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                        revisionAttachmentIds.push(attachmentId);
                                                    });
                                                    AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                        function (data) {
                                                            vm.revisionAttachments = data;
                                                            item[attributeName] = vm.revisionAttachments;
                                                        }
                                                    )
                                                }
                                            } else if (selectatt.dataType == 'IMAGE') {
                                                if (attribute.imageValue != null) {
                                                    item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                }
                                            } else if (selectatt.dataType == 'OBJECT') {
                                                if (selectatt.refType != null) {
                                                    if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                        ItemService.getItem(attribute.refValue).then(
                                                            function (itemValue) {
                                                                item[attributeName] = itemValue;
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                        ItemService.getRevisionId(attribute.refValue).then(
                                                            function (revisionValue) {
                                                                item[attributeName] = revisionValue;
                                                                ItemService.getItem(revisionValue.itemMaster).then(
                                                                    function (data) {
                                                                        item[attributeName].itemMaster = data.itemNumber;
                                                                    }
                                                                )
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                        ECOService.getECO(attribute.refValue).then(
                                                            function (changeValue) {
                                                                item[attributeName] = changeValue;
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                        WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                            function (workflowValue) {
                                                                item[attributeName] = workflowValue;
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                        MfrService.getManufacturer(attribute.refValue).then(
                                                            function (mfrValue) {
                                                                item[attributeName] = mfrValue;
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                        MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                            function (mfrPartValue) {
                                                                item[attributeName] = mfrPartValue;
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                        CommonService.getPerson(attribute.refValue).then(
                                                            function (person) {
                                                                item[attributeName] = person;
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    })

                                }
                            })

                        }
                    );
                }
            }

            function initGantt() {
                gantt_data.projectPercentage = $rootScope.projectPercentComplete;
                $timeout(function () {
                    GanttEditor.initEditor('gantt_here', gantt_data, $scope, $rootScope);
                    expandAll();
                    $rootScope.hideBusyIndicator();
                    $timeout(function () {
                        if (vm.ganttChanges) {
                            $rootScope.showSuccessMessage(vm.message);
                            vm.ganttChanges = false;
                        }
                    }, 200);
                }, 1000);
            }

            function expandAll() {
                gantt.eachTask(function (task) {
                    task.$open = true;
                });
                //gantt.render();
            }

            vm.updateWbsSeq = updateWbsSeq;
            function updateWbsSeq(actualRow, targetRow) {
                $rootScope.showBusyIndicator($('.view-content'));
                if (actualRow.objectType == "PROJECTPHASEELEMENT") {
                    ProjectService.updateWbsItemSeq(actualRow.id, targetRow.id).then(
                        function (data) {
                            loadProjectPlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(sequenceUpdateMessage);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    ActivityService.updateWbsChildrenSeq(actualRow.id, targetRow.id).then(
                        function (data) {
                            loadProjectPlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(sequenceUpdateMessage);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadLinks() {
                ProjectService.getLinks(projectId).then(
                    function (data) {
                        if (data.dependency != null || data.dependency != undefined) {
                            gantt_data.links = (JSON.parse(data.dependency));
                        }
                        loadWorkingDays();
                        loadHolidays();
                    }, function (error) {

                    }
                )
            }

            function loadWorkingDays() {
                var key = "APPLICATION.WORKING_DAYS";
                PreferenceService.getPreferenceByKey(key).then(
                    function (data) {
                        if (data != "") gantt_data.workingDays = JSON.parse(data.integerValue);
                    }
                )
            }

            function loadHolidays() {
                var key = "APPLICATION.HOLIDAY_LIST";
                PreferenceService.getPreferenceByKey(key).then(
                    function (data) {
                        if (data != "") gantt_data.holidays = JSON.parse(data.jsonValue);
                    }
                )
            }

            function updateSequenceNumbers() {
                ProjectService.updateProjectSequenceNumbers(projectId).then(
                    function (data) {
                        $rootScope.showBusyIndicator();
                        $rootScope.loadProjectCounts();
                        loadProject();
                        loadWidths();
                        loadLinks();
                        addContextMenu();
                        unSelectTasks();
                        $rootScope.loadProjectPercentageComplete();
                        $rootScope.ganttUpdated = false;
                        vm.assignedTo = null;
                        GanttEditor.clearAll();
                    }
                )
            }

            function addContextMenu() {
                // enables context menu
                var $contextMenu = $("#contextMenu");
                $("#gantt_here").on("contextmenu", function (e) {
                    var tasks = gantt.getSelectedTasks();
                    if (tasks.length > 0) {
                        $contextMenu.css({
                            display: "block",
                            left: e.pageX,
                            top: e.pageY - 210
                        });
                    }
                    return false;
                });

                // disables context menu
                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
                $("#gantt_here").on("click", function (e) {
                    $contextMenu.hide();
                });
            }

            vm.updateAssignTo = updateAssignTo;
            function updateAssignTo() {
                angular.forEach(gantt.getSelectedTasks(), function (taskId) {
                    var task = gantt.getTask(taskId);
                    task.assignedTo = vm.assignedTo.key;
                });
                gantt.refreshData();
            }

            vm.openAssignedTo = openAssignedTo;
            function openAssignedTo() {
                $('#assignedToModal').modal('show');
            }

            function unSelectTasks() {
                angular.forEach(gantt.getSelectedTasks(), function (taskId) {
                    gantt.unselectTask(taskId);
                });
            }

            vm.closeAssignedToModel = closeAssignedToModel;
            function closeAssignedToModel() {
                $('#assignedToModal').modal('hide');
                unSelectTasks();
            }


            vm.sharedProjectObject = {
                id: null,
                name: null,
                objectType: null
            };
            var share = $translate.instant("SHARE");
            var saveBeforeSharing = $translate.instant("SAVE_BEFORE_SHARING");
            vm.shareSelectedItems = shareSelectedItems;
            function shareSelectedItems() {
                vm.selectedItemIds = gantt.getSelectedTasks();
                vm.selectedItems = [];
                var flag = true;
                angular.forEach(vm.selectedItemIds, function (id) {
                    if (id.length > 10 && flag) {
                        $rootScope.showWarningMessage(saveBeforeSharing);
                        flag = false;
                    } else {
                        var sharedProjectObject = angular.copy(vm.sharedProjectObject);
                        var task = gantt.getTask(id);
                        if (task.objectType == 'PROJECTACTIVITY' || task.objectType == 'PROJECTTASK') {
                            sharedProjectObject.id = id;
                            sharedProjectObject.name = task.name;
                            sharedProjectObject.objectType = task.objectType;
                            vm.selectedItems.push(sharedProjectObject);
                        }
                    }
                });
                if (flag) {
                    var options = {
                        title: share,
                        template: 'app/desktop/modules/shared/share/shareView.jsp',
                        controller: 'ShareController as shareVm',
                        resolve: 'app/desktop/modules/shared/share/shareController',
                        width: 600,
                        showMask: true,
                        data: {
                            selectedShareItems: vm.selectedItems,
                            itemsSharedType: 'projectMultipleSelection',
                            objectType: "PROJECT"
                        },
                        buttons: [
                            {text: share, broadcast: 'app.shareSelectedItems.item'}
                        ],
                        callback: function (data) {
                            angular.forEach(vm.selecteditems, function (selectedItem) {
                                selectedItem.selected = false;
                            });
                            vm.showShareButton = false;
                            vm.selecteditems = [];
                            vm.flag = false;
                            unSelectTasks();
                        }
                    };
                    $rootScope.showSidePanel(options);
                }
            }

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.plan') {
                        vm.projectPlanTabId = data.tabId;
                        $rootScope.ganttUpdated = false;
                        loadPersons();

                        if (validateJSON()) {
                            $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify('details.basic'));
                            var setAttributes = JSON.parse($window.localStorage.getItem("allActivityattributes"));
                        }
                        else {
                            var setAttributes = null;
                        }

                        if (setAttributes != null && setAttributes != undefined) {
                            angular.forEach(setAttributes, function (setAtt) {
                                if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                                    vm.objectIds.push(setAtt.id);
                                }
                            });
                            ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                                function (data) {
                                    if (data.length == 0) {
                                        setAttributes = null;
                                        $window.localStorage.setItem("allActivityattributes", "");
                                        vm.selectedAttributes = setAttributes
                                    } else {
                                        vm.selectedAttributes = setAttributes;
                                    }
                                    updateSequenceNumbers();
                                }
                            )
                        }
                        else {
                            updateSequenceNumbers();
                        }

                    }
                });
            })();
        }
    }
)
;