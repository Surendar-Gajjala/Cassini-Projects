define(
    [
        'app/desktop/modules/template/template.module',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/templateWbsService',
        'app/shared/services/core/templateActivityService',
        'app/shared/services/core/templateMilestoneService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/template/details/plan/templateGanttEditor'
    ],
    function (module) {
        module.controller('TemplatePlanController', TemplatePlanController);

        function TemplatePlanController($scope, $rootScope, $stateParams, $state, $translate, ProjectTemplateService, DialogService, TemplateWbsService,
                                        TemplateGanttEditor, TemplateActivityService, TemplateMilestoneService, $window, $timeout) {

            var vm = this;

            vm.loading = true;
            vm.templateWbsList = [];
            vm.selectTemplateWbs = selectTemplateWbs;
            vm.toggleNode = toggleNode;
            vm.deleteTemplateWbs = deleteTemplateWbs;

            var lastSelectedWbs = null;
            var templateId = $stateParams.templateId;
            var activityId = null;
            var taskSeqid = 1;
            var phaseId = null;
            var seqId = 1;
            var ganttId = null;
            var templateGantt = TemplateGanttEditor.getGanttInstance();
            $rootScope.ids = [];
            $rootScope.ganttType = 'template';
            $rootScope.ganttPersons = [];

            var demo_tasks = {
                data: [],
                persons: [],
                width: {}
            };

            var parsed = angular.element("<div></div>");
            var deleteDialogTitle = parsed.html($translate.instant("DELETE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var DeletedMessage = parsed.html($translate.instant("PROJECT_DELETED_MESSAGE")).html();

            vm.clickToEditTitle = parsed.html($translate.instant("CLICK_TO_EDIT")).html();
            vm.clickToFinishTitle = parsed.html($translate.instant("CLICK_TO_FINISH")).html();
            vm.clickToDeleteTitle = parsed.html($translate.instant("CLICK_TO_DELETE")).html();
            vm.clickToSelectWbsTitle = parsed.html($translate.instant("CLICK_TO_SELECT_WBS")).html();
            vm.activityTitle = parsed.html($translate.instant("ACTIVITY")).html();
            vm.milestoneTitle = parsed.html($translate.instant("MILESTONE")).html();
            var wbsUpdatedMessage = parsed.html($translate.instant("WBS_UPDATED_MESSAGE")).html();
            var wbsCreatedMessage = parsed.html($translate.instant("WBS_CREATED_MESSAGE")).html();
            var phaseCreatedMessage = parsed.html($translate.instant("CREATED_PHASE")).html();
            vm.createWbs = parsed.html($translate.instant("CREATE_WBS")).html();
            var updateWbs = parsed.html($translate.instant("UPDATE_WBS")).html();
            var editWbsTitle = parsed.html($translate.instant("EDIT_WBS")).html();
            var createActivity = parsed.html($translate.instant("CREATE_ACTIVITY")).html();
            var updateActivity = parsed.html($translate.instant("UPDATE_ACTIVITY")).html();
            var editActivityTitle = parsed.html($translate.instant("EDIT_ACTIVITY")).html();
            var createMilestone = parsed.html($translate.instant("CREATE_MILESTONE")).html();
            var updateMilestone = parsed.html($translate.instant("UPDATE_MILESTONE")).html();
            var editMilestone = parsed.html($translate.instant("EDIT_MILESTONE")).html();
            var newWBS = parsed.html($translate.instant("NEW_WBS")).html();
            var newActivity = parsed.html($translate.instant("NEW_ACTIVITY")).html();
            var newMilestone = parsed.html($translate.instant("NEW_MILESTONE")).html();
            var wbsDeletedMessage = parsed.html($translate.instant("WBS_DELETED_MESSAGE")).html();
            var phaseDeletedMessage = parsed.html($translate.instant("PHASE_DELETED_MESSAGE")).html();
            var activityDeletedMessage = parsed.html($translate.instant("ACTIVITY_DELETED_MESSAGE")).html();
            var milestoneDeletedMessage = parsed.html($translate.instant("MILESTONE_DELETED_MESSAGE")).html();
            var activityUpdatedMessage = parsed.html($translate.instant("ACTIVITY_UPDATED_MESSAGE")).html();
            var milestoneUpdatedMessage = parsed.html($translate.instant("MILESTONE_UPDATED_MESSAGE")).html();
            var activityCreatedMessage = parsed.html($translate.instant("ACTIVITY_CREATED_MESSAGE")).html();
            var milestoneCreatedMessage = parsed.html($translate.instant("MILESTONE_CREATED_MESSAGE")).html();
            var createTitle = parsed.html($translate.instant("CREATE")).html();
            var updateTitle = parsed.html($translate.instant("UPDATE")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();

            $rootScope.showTemplateActivityAndMilestone = false;

            vm.editTemplateWbs = editTemplateWbs;

            $rootScope.showTemplateWbsButton = true;
            $rootScope.addTemplateWbs = addTemplateWbs;
            $rootScope.addTemplateActivity = addTemplateActivity;
            $rootScope.addTemplateMilestone = addTemplateMilestone;

            function loadTemplatePlan() {
                vm.templateWbsList = [];
                ProjectTemplateService.getProjectTemplateWbs(templateId).then(
                    function (data) {
                        $rootScope.showBusyIndicator();
                        vm.templateWbsList = [];
                        vm.templatePlanWbs = data;
                        gantt.config.start_date = new Date(new Date().setDate(new Date().getDate() - 1));
                        gantt.config.end_date = new Date(new Date().setDate(new Date().getDate() + 1));
                        populateTemplateWbs(data);
                        demo_tasks.data = [];
                        loadGanttData();
                        $rootScope.templateGanttUpdated = false;
                        if (templateGantt.$container) {
                            TemplateGanttEditor.clearAll();
                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var phaseNameValidation = parsed.html($translate.instant("ENTER_PHASE_NAME")).html();
            var duplicatePhaseNameValidation = parsed.html($translate.instant("PHASE_NAME_VALIDATION")).html();

            function validatePhase(phase) {
                if (phase.text == null || phase.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(phaseNameValidation);
                } else if (vm.phaseNames.indexOf(phase.text) > -1) {
                    vm.valid = false;
                    $rootScope.showWarningMessage(phase.text + " : " + duplicatePhaseNameValidation);
                } else {
                    vm.phaseNames.push(phase.text);
                }
                return vm.valid;
            }

            var duplicateActivityNameValidation = parsed.html($translate.instant("ACTIVITY_NAME_VALIDATION")).html();
            var activityNameValidation = parsed.html($translate.instant("ENTER_ACTIVITY_NAME")).html();
            var activityParentId = null;

            function validateActivity(activity) {
                if (activity.text == null || activity.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(activityNameValidation);
                }
                if (activityParentId == null) {
                    activityParentId = activity.parent;
                    vm.activityNames.push(activity.text);
                } else {
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
                return vm.valid;
            }

            var taskNameValidation = parsed.html($translate.instant("ENTER_TASK_NAME")).html();
            var duplicateMilestoneNameValidation = parsed.html($translate.instant("MILESTONE_NAME_VALIDATION")).html();
            var milestoneNameValidation = parsed.html($translate.instant("ENTER_MILESTONE_NAME")).html();
            var projectPlanSaved = parsed.html($translate.instant("TEMPLATE_CREATE_MESSAGE")).html();
            var projectPlanUpdated = parsed.html($translate.instant("TEMPLATE_UPDATE_MESSAGE")).html();
            vm.saveTemplate = parsed.html($translate.instant("SAVE_TEMPLATE")).html();
            vm.expandAll = parsed.html($translate.instant("EXPAND_ALL")).html();
            vm.collapseAll = parsed.html($translate.instant("COLLAPSE_ALL")).html();
            var milestoneParentId = null;

            function validateMilestone(milestone) {
                if (milestone.text == null || milestone.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(milestoneNameValidation);
                }
                if (milestoneParentId == null) {
                    milestoneParentId = milestone.parent;
                    vm.milestoneNames.push(milestone.text);
                } else {
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
                return vm.valid;
            }

            function validateTask(task) {
                if (task.text == null || task.text == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(taskNameValidation);
                }
                if (activityId == null) {
                    activityId = task.parent;
                    vm.taskNames.push(task.text);
                } else {
                    if (activityId == task.parent) {
                        taskSeqid++;
                        if (vm.taskNames.indexOf(task.text) > -1) {
                            vm.valid = false;
                            $rootScope.showWarningMessage(task.text + " : " + taskNameValidation);
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
                return vm.valid;
            }

            function loadGanttData() {
                angular.forEach(vm.templateWbsList, function (wbs) {
                    wbs.text = wbs.name;
                    wbs.start_date = new Date();
                    wbs.end_date = new Date();
                    if (wbs.objectType == "TEMPLATEPHASE") wbs.object_type = "Phase";
                    if (wbs.objectType == "TEMPLATEACTIVITY") {
                        wbs.object_type = "Activity";
                        wbs.assignedTo = wbs.person != null ? wbs.person.id : null;
                        wbs.parent = wbs.parent == null ? 0 : wbs.parent;
                    }
                    if (wbs.objectType == "TEMPLATEMILESTONE") {
                        wbs.object_type = "Milestone";
                        wbs.assignedTo = wbs.person != null ? wbs.person.id : null;
                        wbs.parent = wbs.parent == null ? 0 : wbs.parent;
                        wbs.type = templateGantt.config.types.milestone;
                    }
                    if (wbs.objectType == "TEMPLATETASK") {
                        wbs.object_type = "Task";
                        wbs.parent = wbs.activity;
                    }
                    demo_tasks.data.push(wbs);
                });
                initGantt();
            }


            $scope.$on('app.template.createSaveGantt', function () {
                saveTemplateGantt();
            });

            $rootScope.saveTemplateGantt = saveTemplateGantt;
            function saveTemplateGantt() {
                vm.wbsList = [];
                vm.activityList = [];
                vm.milestoneList = [];
                vm.taskList = [];
                vm.valid = true;
                var phaseId = 0;
                vm.phaseNames = [];
                vm.activityNames = [];
                vm.milestoneNames = [];
                vm.taskNames = [];
                $rootScope.ids.length = 0;
                vm.ganttList = TemplateGanttEditor.saveGantt();
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
                if (vm.valid) createTemplateWbs();
            }

            function createTemplateWbs() {
                $rootScope.showBusyIndicator();
                TemplateWbsService.createWBSElements(templateId, vm.wbsList).then(
                    function (data) {
                        var ids = null;
                        vm.resultWbsList = data;
                        angular.forEach(vm.resultWbsList, function (wbs) {
                            if (wbs.ganttId != null) {
                                ids = {"cassiniId": wbs.id, "ganttId": wbs.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.activityList.length > 0) createTemplateActivity();
                        if (vm.milestoneList.length > 0) createTemplateMilestone();
                        if (vm.activityList.length == 0 && vm.milestoneList.length == 0) {
                            if (vm.templateWbsList.length == 0) {
                                $rootScope.showSuccessMessage(projectPlanSaved);
                            } else {
                                $rootScope.showSuccessMessage(projectPlanUpdated);
                            }
                            $rootScope.hideBusyIndicator();
                            loadTemplatePlan();
                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.templateGanttUpdated = false;
                    }
                );
            }

            function createTemplateActivity() {
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
                TemplateActivityService.createActivites(vm.activityList).then(
                    function (data) {
                        var ids = null;
                        vm.resultActivityList = data;
                        angular.forEach(vm.resultActivityList, function (activity) {
                            if (activity.ganttId != null) {
                                ids = {"cassiniId": activity.id, "ganttId": activity.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.taskList.length > 0) createTemplateTask();
                        if (vm.milestoneList.length == 0 && vm.taskList.length == 0) {
                            if (vm.templateWbsList.length == 0) {
                                $rootScope.showSuccessMessage(projectPlanSaved);
                            } else {
                                $rootScope.showSuccessMessage(projectPlanUpdated);
                            }
                            $rootScope.hideBusyIndicator();
                            loadTemplatePlan();

                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.templateGanttUpdated = false;
                    }
                )
            }

            function createTemplateMilestone() {
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
                TemplateMilestoneService.createMilestones(vm.milestoneList).then(
                    function (data) {
                        var ids = null;
                        vm.resultMilestoneList = data;
                        angular.forEach(vm.resultMilestoneList, function (milestone) {
                            if (milestone.ganttId != null) {
                                ids = {"cassiniId": milestone.id, "ganttId": milestone.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.templateWbsList.length == 0) {
                            $rootScope.showSuccessMessage(projectPlanSaved);
                        } else {
                            $rootScope.showSuccessMessage(projectPlanUpdated);
                        }
                        $rootScope.hideBusyIndicator();
                        if (vm.taskList.length == 0) loadTemplatePlan();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function createTemplateTask() {
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
                TemplateActivityService.createActivityTasks(activityId, vm.taskList).then(
                    function (data) {
                        var ids = null;
                        vm.resultTaskList = data;
                        angular.forEach(vm.resultTaskList, function (task) {
                            if (task.ganttId != null) {
                                ids = {"cassiniId": task.id, "ganttId": task.ganttId};
                                $rootScope.ids.push(ids);
                            }
                        });
                        if (vm.templateWbsList.length == 0) {
                            $rootScope.showSuccessMessage(projectPlanSaved);
                            $rootScope.hideBusyIndicator();
                        } else {
                            $rootScope.showSuccessMessage(projectPlanUpdated);
                            $rootScope.hideBusyIndicator();
                        }
                        loadTemplatePlan();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                )
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


            function newWbs(wbs, seqId) {
                var newWbs = {
                    id: wbs.id,
                    name: wbs.text,
                    parent: null,
                    template: templateId,
                    description: wbs.description != null ? wbs.description : null,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: seqId
                };
                return newWbs;
            }

            function newWbsActivity(wbs, seqId) {
                var newWbsActivity = {
                    id: wbs.id,
                    name: wbs.text,
                    description: wbs.description,
                    assignedTo: wbs.assignedTo,
                    wbs: wbs.parent != null ? parseInt(wbs.parent) : null,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: seqId,
                    workflow: wbs.workflow
                };
                return newWbsActivity;
            }

            function newWbsMilestone(wbs, seqId) {
                var newWbsMilestone = {
                    id: wbs.id,
                    name: wbs.text,
                    description: wbs.description != undefined ? wbs.description : null,
                    assignedTo: wbs.assignedTo,
                    wbs: wbs.parent != null ? parseInt(wbs.parent) : null,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: seqId
                };
                return newWbsMilestone;
            }

            function newActivityTask(wbs) {
                var newActivityTask = {
                    id: wbs.id,
                    name: wbs.text,
                    description: wbs.description,
                    required: false,
                    activity: wbs.parent != null ? parseInt(wbs.parent) : null,
                    assignedTo: wbs.assignedTo,
                    ganttId: ganttId != null ? ganttId : null,
                    sequenceNumber: taskSeqid,
                    workflow: wbs.workflow
                };
                return newActivityTask;
            }


            function initGantt() {
                $timeout(function () {
                    TemplateGanttEditor.initEditor('template_gantt_here', demo_tasks, $scope, $rootScope);
                    TemplateGanttEditor.expandAll();
                    $rootScope.hideBusyIndicator();
                }, 1000)
            }

            $scope.$on('app.template.ganttUpdated', templateGanttUpdated);
            function templateGanttUpdated() {
                $rootScope.templateGanttUpdated = true;
            }

            function populateTemplateWbs(data) {
                angular.forEach(data, function (item) {
                    item.newName = item.name;
                    item.newDescription = item.description;
                    item.isNew = false;
                    item.editMode = false;
                    if (lastSelectedWbs != null) {
                        if (lastSelectedWbs.id == item.id) {
                            item.selected = true;
                        }
                    }
                    if (item.objectType == "TEMPLATEPHASE") {
                        if (item.children.length > 0) item.hasChildren = true;
                        else item.hasChildren = false;
                    }
                    vm.templateWbsList.push(item);
                    if (item.templateActivityTasks.length > 0) {
                        angular.forEach(item.templateActivityTasks, function (task) {
                            vm.templateWbsList.push(task);
                        })
                    }
                    populateTemplateWbs(item.children);
                    /*  if (item.objectType == "TEMPLATEACTIVITY") {
                     populateTemplateWbs(item.templateTaskList);
                     }
                     populateTemplateWbs(item.templateActivities);
                     populateTemplateWbs(item.templateMilestones);*/

                });
                vm.loading = false;
            }

            function addTemplateWbs() {
                var options = {
                    title: newWBS,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/newWbsView.jsp',
                    controller: 'NewWbsController as newWbsVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/newWbsController',
                    width: 550,
                    data: {
                        wbsElementData: null,
                        wbsMode: 'TemplateWbsNew'
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.template.wbs.new'}
                    ],
                    callback: function () {
                        loadTemplatePlan();
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(phaseCreatedMessage);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editTemplateWbs(templateWbs) {
                var options = null;
                if (templateWbs.objectType == "TEMPLATEPHASE") {
                    options = {
                        title: editWbsTitle,
                        showMask: true,
                        template: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/editWbsView.jsp',
                        controller: 'EditWbsController as editWbsVm',
                        resolve: 'app/desktop/modules/pm/project/details/tabs/plan/wbs/editWbsController',
                        width: 550,
                        data: {
                            wbsElementData: templateWbs,
                            wbsMode: 'TemplateWbsEdit'
                        },
                        buttons: [
                            {text: updateTitle, broadcast: 'app.template.wbs.edit'}
                        ],
                        callback: function () {
                            loadTemplatePlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(wbsUpdatedMessage);
                        }
                    };

                    $rootScope.showSidePanel(options);
                } else if (templateWbs.objectType == "TEMPLATEACTIVITY") {
                    options = {
                        title: editActivityTitle,
                        showMask: true,
                        template: 'app/desktop/modules/pm/project/details/tabs/plan/activity/newWbsActivityView.jsp',
                        controller: 'NewWbsActivityController as newWbsActivityVm',
                        resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activity/newWbsActivityController',
                        width: 550,
                        data: {
                            activityData: templateWbs,
                            activityMode: 'TemplateActivityEdit'
                        },
                        buttons: [
                            {text: updateTitle, broadcast: 'app.template.activity.edit'}
                        ],
                        callback: function () {
                            loadTemplatePlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(activityUpdatedMessage);
                        }
                    };

                    $rootScope.showSidePanel(options);
                } else if (templateWbs.objectType == "TEMPLATEMILESTONE") {
                    options = {
                        title: editMilestone,
                        showMask: true,
                        template: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/editWbsMilestoneView.jsp',
                        controller: 'EditWbsMilestoneController as editWbsMilestoneVm',
                        resolve: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/editWbsMilestoneController',
                        width: 550,
                        data: {
                            milestoneData: templateWbs,
                            milestoneMode: 'TemplateMilestoneEdit'
                        },
                        buttons: [
                            {text: updateTitle, broadcast: 'app.template.milestone.edit'}
                        ],
                        callback: function () {
                            loadTemplatePlan();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(milestoneUpdatedMessage);
                        }
                    };

                    $rootScope.showSidePanel(options);
                }

            }

            function addTemplateActivity(wbs) {
                var options = {
                    title: newActivity,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/activity/newWbsActivityView.jsp',
                    controller: 'NewWbsActivityController as newWbsActivityVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/activity/newWbsActivityController',
                    width: 550,
                    data: {
                        activityWbsData: wbs,
                        activityMode: 'TemplateActivityNew',
                        templateData: templateId
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.template.activity.new'}
                    ],
                    callback: function () {
                        loadTemplatePlan();
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(activityCreatedMessage);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function addTemplateMilestone(wbs) {
                var options = {
                    title: newMilestone,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/newWbsMilestoneView.jsp',
                    controller: 'NewWbsMilestoneController as newWbsMilestoneVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/plan/milestone/newWbsMilestoneController',
                    width: 550,
                    data: {
                        milestoneWbsData: wbs,
                        milestoneMode: 'TemplateMilestoneNew',
                        projectData: templateId
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.template.milestone.new'}
                    ],
                    callback: function () {
                        loadTemplatePlan();
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(milestoneCreatedMessage);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function selectTemplateWbs(wbs) {
                wbs.selected = !wbs.selected;
                if (wbs.selected == true) {
                    lastSelectedWbs = wbs;
                    $rootScope.showTemplateWbsButton = false;
                    $rootScope.showTemplateActivityAndMilestone = true;
                }
                else {
                    wbs.selected = false;
                    lastSelectedWbs = null;
                    $rootScope.showTemplateWbsButton = true;
                    $rootScope.showTemplateActivityAndMilestone = false;
                }
                angular.forEach(vm.templateWbsList, function (templateWbs) {
                    if (wbs.id != templateWbs.id) {
                        templateWbs.selected = false;
                    }
                })
            }

            function toggleNode(wbs) {
                wbs.expanded = !wbs.expanded;
                var index = vm.templateWbsList.indexOf(wbs);
                if (wbs.expanded == false) {
                    removeChildren(wbs);
                } else {
                    ProjectTemplateService.getTemplateWbsChildren(templateId, wbs.id).then(
                        function (data) {
                            wbs.children = [];
                            wbs.templateActivities = data.templateActivities;
                            wbs.templateMilestones = data.templateMilestones;
                            angular.forEach(data.templateActivities, function (wbsChild) {
                                wbs.children.push(wbsChild);
                            });

                            angular.forEach(data.templateMilestones, function (wbsChild) {
                                wbs.children.push(wbsChild);
                            });

                            angular.forEach(wbs.children, function (wbsChild) {
                                index = index + 1;
                                vm.templateWbsList.splice(index, 0, wbsChild);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeChildren(wbs) {
                if (wbs.templateActivities != null && wbs.templateActivities != undefined) {
                    angular.forEach(wbs.templateActivities, function (item) {
                        var index = vm.templateWbsList.indexOf(item);
                        vm.templateWbsList.splice(index, 1);
                    });
                    wbs.expanded = false;
                }

                if (wbs.templateMilestones != null && wbs.templateMilestones != undefined) {
                    angular.forEach(wbs.templateMilestones, function (item) {
                        var index = vm.templateWbsList.indexOf(item);
                        vm.templateWbsList.splice(index, 1);
                    });
                    wbs.expanded = false;
                }

            }

            $scope.$on('app.template.deleteWbs', deleteWbsElement);

            function deleteWbsElement(event, args) {
                var id = args.Id;
                angular.forEach(vm.templateWbsList, function (val) {
                    if (val.id == id) {
                        deleteTemplateWbs(val);
                    }
                })
            }

            var taskDialogTitle = parsed.html($translate.instant("TASK_DIALOG_TITLE")).html();
            var taskDialogMessage = parsed.html($translate.instant("TASK_DIALOG_MESSAGE")).html();
            var taskDeletedMessage = parsed.html($translate.instant("TASK_DELETED_MESSAGE")).html();

            function deleteTemplateWbs(templateWbs) {
                var options = null;
                if (templateWbs.objectType == 'TEMPLATEPHASE') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " PHASE " + " [ " + templateWbs.name + " ] " + " ?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            TemplateWbsService.deleteTemplateWbs(templateWbs.id).then(
                                function (data) {
                                    var index = vm.templateWbsList.indexOf(templateWbs);
                                    vm.templateWbsList.splice(index, 1);
                                    $rootScope.showSuccessMessage(phaseDeletedMessage);
                                    loadTemplatePlan();
                                    $rootScope.showTemplateWbsButton = true;
                                    $rootScope.showTemplateActivityAndMilestone = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    })

                } else if (templateWbs.objectType == 'TEMPLATEACTIVITY') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + templateWbs.name + " ] " + vm.activityTitle + " ?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            TemplateActivityService.deleteTemplateActivity(templateWbs.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(activityDeletedMessage);
                                    loadTemplatePlan();
                                    $rootScope.showTemplateWbsButton = true;
                                    $rootScope.showTemplateActivityAndMilestone = false;
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    })

                } else if (templateWbs.objectType == 'TEMPLATEMILESTONE') {
                    options = {
                        title: deleteDialogTitle,
                        message: deleteDialogMessage + " [ " + templateWbs.name + " ] " + vm.milestoneTitle + " ?",
                        okButtonClass: 'btn-danger'
                    };

                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            TemplateMilestoneService.deleteTemplateMilestone(templateWbs.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(milestoneDeletedMessage);
                                    loadTemplatePlan();
                                    $rootScope.showTemplateWbsButton = true;
                                    $rootScope.showTemplateActivityAndMilestone = false;
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    })

                } else if (templateWbs.objectType == 'TEMPLATETASK') {
                    var options = {
                        title: taskDialogTitle,
                        message: taskDialogMessage + " [ " + templateWbs.name + " ] " + "?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            TemplateActivityService.deleteTemplateActivityTask(templateWbs.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(taskDeletedMessage);
                                    loadTemplatePlan();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    });

                }
            }

            /*vm.openActivityDetails = openActivityDetails;
             function openActivityDetails(wbs) {
             $window.localStorage.setItem("lastSelectedTemplateTab", JSON.stringify("details.plan"));
             $state.go('app.templates.activity.details', {activityId: wbs.id})
             }*/

            vm.persons = [];
            function loadPersons() {
                ProjectTemplateService.getAllProjectTemplateMembers(templateId).then(
                    function (data) {
                        demo_tasks.persons = [];
                        angular.forEach(data, function (person) {
                            demo_tasks.persons.push({
                                key: person.id,
                                label: person.fullName,
                                active: person.active
                            });
                            $rootScope.ganttPersons.push(person.fullName);
                        });
                        vm.persons = demo_tasks.persons;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var templateSaveValidation = parsed.html($translate.instant("TEMPLATE_SAVE_VALIDATION")).html();
            $scope.$on('app.projecttemplate.openActivityDetails', openActivityDetails);
            vm.openActivityDetails = openActivityDetails;
            function openActivityDetails(event, args) {
                var activityId = args.activityId;
                if (JSON.stringify(activityId).length > 10) {
                    $rootScope.showWarningMessage(templateSaveValidation);
                } else {
                    $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify(vm.projectPlanTabId));
                    $state.go('app.templates.activity.details', {
                        activityId: activityId,
                        tab: 'details.basic'
                    });
                }
            }

            $scope.$on('app.projecttemplate.openTaskDetails', openTaskDetails);
            vm.openTaskDetails = openTaskDetails;
            function openTaskDetails(event, args) {
                var taskId = args.taskId;
                if (JSON.stringify(taskId).length > 10) {
                    $rootScope.showWarningMessage(templateSaveValidation);
                } else {
                    $state.go('app.templates.task.details', {
                        taskId: taskId,
                        tab: 'details.basic'
                    });
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.template.tabActivated', function (event, data) {
                    if (data.tabId == 'details.plan') {
                        $rootScope.templateGanttUpdated = false;
                        loadPersons();
                        loadTemplatePlan();
                    }
                })
                //}
            })();
        }
    }
)
;