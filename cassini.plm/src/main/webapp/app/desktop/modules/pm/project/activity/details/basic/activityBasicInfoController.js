define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ActivityBasicInfoController', ActivityBasicInfoController);

        function ActivityBasicInfoController($scope, $stateParams, $rootScope, $timeout, $sce,$window, $state, $translate, ActivityService, CommonService, ProjectService) {

            var vm = this;
            var activityId = $stateParams.activityId;
            var wbsId = $stateParams.wbsId;
            var project = $rootScope.projectId;
            vm.updateActivity = updateActivity;
            vm.persons = [];
            vm.newProjectManager = null;
            $rootScope.selectedWbsActivity = null;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var activityUpdateMessage = parsed.html($translate.instant("ACTIVITY_UPDATED_MESSAGE")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var startDateValidation = parsed.html($translate.instant("START_DATE_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();

            var activityStartBeforeProject = parsed.html($translate.instant("ACTIVITY_PLANSTARTBEFORE_MSG")).html();
            var activityStartAfterProject = parsed.html($translate.instant("ACTIVITY_PLANSTARTAFTER_MSG")).html();
            var activityPlanFinishAfterPlanStart = parsed.html($translate.instant("ACTIVITY_PLANFINISHAFTER_PLANSTART_MSG")).html();
            var activityPlanStartBeforePlanFinish = parsed.html($translate.instant("ACTIVITY_PLANSTARTBEFORE_PLANFINISH_MSG")).html();
            var activityFinishBeforeProject = parsed.html($translate.instant("ACTIVITY_PLANFINISHBEFOREMSG")).html();
            var activityFinishAfterProject = parsed.html($translate.instant("ACTIVITY_PLANFINISHAFTERMSG")).html();
            var activityExistOnWbs = parsed.html($translate.instant("ACTIVITY_NAME_EXIST_WBS")).html();

            vm.editPlannedStartDate = editPlannedStartDate;
            vm.editPlannedFinishDate = editPlannedFinishDate;

            vm.plannedStartDateEditMode = false;
            vm.plannedFinishDateEditMode = false;
            function editPlannedStartDate(activity) {
                vm.plannedStartDateEditMode = true;
                $rootScope.plannedStartDate = activity.plannedStartDate;
            }

            function editPlannedFinishDate(activity) {
                vm.plannedFinishDateEditMode = true;
                $rootScope.plannedFinishDate = activity.plannedFinishDate;
            }

            vm.chancelPlannedStartDate = chancelPlannedStartDate;
            function chancelPlannedStartDate(activity) {
                vm.plannedStartDateEditMode = false;
                activity.plannedStartDate = $rootScope.plannedStartDate;
            }

            vm.chancelPlannedFinishDate = chancelPlannedFinishDate;
            function chancelPlannedFinishDate(activity) {
                vm.plannedFinishDateEditMode = false;
                activity.plannedFinishDate = $rootScope.plannedFinishDate;
            }

            function loadActivity() {
                ActivityService.getActivity(activityId).then(
                    function (data) {
                        vm.activity = data;
                        if (vm.activity.plannedStartDate)
                            vm.activity.plannedStartDatede = moment(vm.activity.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        if (vm.activity.plannedFinishDate)
                            vm.activity.plannedFinishDatede = moment(vm.activity.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        if (vm.activity.actualStartDate)
                            vm.activity.actualStartDatede = moment(vm.activity.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        if (vm.activity.actualFinishDate)
                            vm.activity.actualStartDatede = moment(vm.activity.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                        $rootScope.selectedWbsActivity = data;
                        $rootScope.viewInfo.title = vm.activity.name;
                        CommonService.getPerson(vm.activity.createdBy).then(
                            function (data) {
                                vm.activity.createdByObject = data;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                        loadWbs();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateActivity(activity) {
                if (validate(activity)) {
                    activity.assignedTo = activity.person.id;
                    ActivityService.updateActivity(activity).then(
                        function (data) {
                            vm.activity.name = data.name;
                            vm.activity.description = data.description;
                            vm.plannedStartDateEditMode = false;
                            vm.plannedFinishDateEditMode = false;
                            loadActivity();
                            $rootScope.showSuccessMessage(activityUpdateMessage)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            function validate(activity) {
                var valid = true;
                if (activity.name == null || activity.name == "" || activity.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                    loadActivity();
                }

                else if (vm.project != null && vm.project.program != null) {
                    $window.localStorage.setItem("project_open_from", vm.project.program);
                    if (vm.project.plannedStartDate == null || vm.project.plannedStartDate == "" || vm.project.plannedFinishDate == null || vm.project.plannedFinishDate == "") {
                        $rootScope.showWarningMessage("Please add project planned start and finish dates before edit task details");
                        valid = false;
                        $timeout(function () {
                            $state.go('app.pm.project.details', {
                                projectId: vm.project.id,
                                tab: 'details.basic'
                            });
                        }, 1500)
                    }
                }
                /*else if (activity.person == null || activity.person == "") {
                 valid = false;
                 $rootScope.showWarningMessage(assignedToValidation)
                 }
                 else if (activity.plannedStartDate == null || activity.plannedStartDate == "") {
                 valid = false;
                 $rootScope.showWarningMessage(plannedStartDateValidation)
                 }*/
                else if (activity.plannedStartDate != null && activity.plannedStartDate != "") {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var projectFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var projectStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(activity.plannedStartDate, $rootScope.applicationDateSelectFormat);

                    if ($rootScope.dateValidation(activity.plannedStartDate)) {
                        activity.plannedStartDate = $rootScope.convertDate(activity.plannedStartDate);
                        var plannedStartDate = moment(activity.plannedStartDate, $rootScope.applicationDateSelectFormat);

                        var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                        var val2 = plannedStartDate.isAfter(projectFinishDate);
                        var validStartDate = plannedStartDate.isBefore(projectStartDate);
                        if (activity.plannedFinishDate == null || activity.plannedFinishDate == "" || activity.plannedFinishDate == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(plannedFinishDateValidation);
                        }

                        if (val2) {
                            valid = false;
                            $rootScope.showWarningMessage(activityStartBeforeProject + " : " + vm.project.plannedFinishDate);
                        }

                        if (validStartDate) {
                            valid = false;
                            $rootScope.showWarningMessage(activityStartAfterProject + " : " + vm.project.plannedStartDate);
                        }
                    } else {
                        valid = false;
                    }

                }

                if (activity.plannedStartDate != null && activity.plannedFinishDate != null && activity.plannedStartDate != "" && activity.plannedFinishDate != "" && valid == true) {
                    if ($rootScope.dateValidation(activity.plannedFinishDate)) {
                        activity.plannedFinishDate = $rootScope.convertDate(activity.plannedFinishDate);
                        var plannedFinishDate = moment(activity.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                        var plannedStartDate = moment(activity.plannedStartDate, $rootScope.applicationDateSelectFormat);
                        var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                        var projectPlannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                        var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                        var val3 = plannedFinishDate.isAfter(projectPlannedFinishDate);
                        var startDateValid = plannedFinishDate.isBefore(projectPlannedStartDate);
                        if (!val && vm.plannedStartDateEditMode) {
                            valid = false;
                            $rootScope.showWarningMessage(activityPlanStartBeforePlanFinish);
                        }
                        if (!val && vm.plannedFinishDateEditMode) {
                            valid = false;
                            $rootScope.showWarningMessage(activityPlanFinishAfterPlanStart);
                        }

                        if (val3) {
                            valid = false;
                            $rootScope.showWarningMessage(activityFinishBeforeProject + " :" + vm.project.plannedFinishDate);
                        }

                        if (startDateValid) {
                            valid = false;
                            $rootScope.showWarningMessage(activityFinishAfterProject + " :" + vm.project.plannedStartDate);
                        }
                    } else {
                        valid = false;
                    }
                }

                /*if (valid == true) {
                 activity.assignedTo = activity.person.id;
                 }*/
                return valid;
            }

            function loadPersons() {
                vm.persons = [];
                ProjectService.getAllProjectMembers(vm.project.id).then(
                    function (data) {
                        vm.persons = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadWbs() {
                ProjectService.getWBSElement(vm.activity.wbs, vm.activity.wbs).then(
                    function (data) {
                        vm.project = data.project;
                        $rootScope.projectInfo = vm.project;
                        CommonService.getPersonReferences([vm.project], 'createdBy');
                        CommonService.getPersonReferences([vm.project], 'projectManager');
                        loadPersons();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadActivity();
            })();
        }
    }
);