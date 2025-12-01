define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/templateActivityService'
    ],

    function (module) {
        module.controller('EditWbsActivityController', EditWbsActivityController);

        function EditWbsActivityController($scope, $rootScope, $timeout, $translate, $stateParams, $state, ProjectService,
                                           ItemService, CommonService, ActivityService, TemplateActivityService, LoginService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var activityCreatedMessage = parsed.html($translate.instant("ACTIVITY_CREATED_MESSAGE")).html();
            var activityUpdatedMessage = parsed.html($translate.instant("ACTIVITY_UPDATED_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var assignedToValidation = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var plannedStartDateValidation = parsed.html($translate.instant("PLANNED_START_DATE_VALIDATION")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var startDateValidation = parsed.html($translate.instant("START_DATE_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();
            var plannedStartShouldBeOnBeforePfd = parsed.html($translate.instant("PLANNED_START_DATE_SHOULD_BE_ON_BEFORE_PFD")).html();
            var plannedStartShouldBeAfterPsd = parsed.html($translate.instant("PLANNED_START_DATE_SHOULD_BE_ON_AFTER_PSD")).html();
            var plannedFinishShouldBeAfterPsd = parsed.html($translate.instant("PLANNED_FINISH_DATE_SHOULD_BE_ON_AFTER_PSD")).html();
            var plannedFinishShouldBeOnBeforePfd = parsed.html($translate.instant("PLANNED_FINISH_DATE_SHOULD_BE_ON_BEFORE_PFD")).html();

            vm.lastSelectedPerson = null;

            function editActivity() {
                if (validate()) {
                    if (vm.mode == "External") {
                        vm.newActivity.assignedTo = vm.newActivity.assignedTo;
                    }
                    if (vm.mode == "New" || vm.mode == "Edit") {
                        vm.newActivity.assignedTo = vm.newActivity.assignedTo.id;
                    }
                    ActivityService.updateActivity(vm.newActivity).then(
                        function (data) {
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage(activityUpdatedMessage);
                        }, function (error) {
                            vm.newActivity.name = vm.activity.name;
                            vm.newActivity.assignedTo = vm.activity.person;
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newActivity.name == null || vm.newActivity.name == "" || vm.newActivity.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.newActivity.assignedTo == null || vm.newActivity.assignedTo == "") {
                    valid = false;
                    $rootScope.showWarningMessage(assignedToValidation)
                } else if (vm.newActivity.plannedStartDate == null || vm.newActivity.plannedStartDate == "") {
                    valid = false;
                    $rootScope.showWarningMessage(plannedStartDateValidation)
                } else if (vm.newActivity.plannedStartDate != null && vm.newActivity.plannedStartDate != "") {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var projectFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var projectStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.newActivity.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                    var val2 = plannedStartDate.isAfter(projectFinishDate);
                    var validStartDate = plannedStartDate.isBefore(projectStartDate);
                    /*if (!val1) {
                     valid = false;
                     $rootScope.showWarningMessage(startDateValidation);
                     } else*/
                    if (vm.newActivity.plannedFinishDate == null || vm.newActivity.plannedFinishDate == "" || vm.newActivity.plannedFinishDate == undefined) {
                        valid = false;
                        $rootScope.showWarningMessage(plannedFinishDateValidation);
                    }

                    if (val2) {
                        valid = false;
                        $rootScope.showWarningMessage(plannedStartShouldBeOnBeforePfd + " : " + vm.project.plannedFinishDate);
                    }

                    if (validStartDate) {
                        valid = false;
                        $rootScope.showWarningMessage(plannedStartShouldBeAfterPsd + " : " + vm.project.plannedStartDate);
                    }
                }

                if (vm.newActivity.plannedStartDate != null && vm.newActivity.plannedFinishDate != null && vm.newActivity.plannedStartDate != "" && vm.newActivity.plannedFinishDate != "") {
                    var plannedFinishDate = moment(vm.newActivity.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.newActivity.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var projectPlannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                    var val3 = plannedFinishDate.isAfter(projectPlannedFinishDate);
                    var startDateValid = plannedFinishDate.isBefore(projectPlannedStartDate);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }

                    if (val3) {
                        valid = false;
                        $rootScope.showWarningMessage(plannedFinishShouldBeOnBeforePfd + " :" + vm.project.plannedFinishDate);
                    }

                    if (startDateValid) {
                        valid = false;
                        $rootScope.showWarningMessage(plannedFinishShouldBeAfterPsd + " : " + vm.project.plannedStartDate);
                    }
                }

                return valid;
            }

            function editTemplateActivity() {
                if (validateTemplateActivity()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    TemplateActivityService.updateTemplateActivity(vm.newActivity).then(
                        function (data) {
                            vm.activity = data;
                            $scope.callback();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateTemplateActivity() {
                var valid = true;
                if (vm.newActivity.name == null || vm.newActivity.name == "" || vm.newActivity.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            vm.persons = [];
            function loadProjectPersons() {
                vm.persons = [];
                ProjectService.getAllProjectMembers(vm.project.id).then(
                    function (data) {
                        vm.persons = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadProject() {
                ProjectService.getWbsproject(vm.activity.wbs).then(
                    function (data) {
                        vm.project = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                vm.wbs = $scope.data.activityWbsData;
                vm.mode = $scope.data.activityMode;
                vm.activity = $scope.data.activityData;
                vm.project = $scope.data.projectData;
                vm.templateId = $scope.data.templateData;

                if (vm.project == null) {
                    loadProject();
                }

                /*---------- For Project Activity -------------*/

                if (vm.mode == "Edit" && vm.activity != null) {
                    vm.newActivity = {
                        id: vm.activity.id,
                        name: vm.activity.name,
                        description: vm.activity.description,
                        wbs: vm.activity.parent,
                        assignedTo: vm.activity.person,
                        sequenceNumber: vm.activity.sequenceNumber,
                        predecessors: vm.activity.predecessors,
                        plannedStartDate: vm.activity.plannedStartDate,
                        plannedFinishDate: vm.activity.plannedFinishDate,
                        actualStartDate: vm.activity.actualStartDate,
                        actualFinishDate: vm.activity.actualFinishDate,
                        status: vm.activity.status,
                        percentComplete: 0,
                        createdDate: vm.activity.createdDate,
                        createdBy: vm.activity.createdBy
                    };
                }

                if (vm.mode == "External" && vm.activity != null) {
                    vm.newActivity = {
                        id: vm.activity.id,
                        name: vm.activity.name,
                        description: vm.activity.description,
                        wbs: vm.activity.wbs,
                        assignedTo: vm.activity.assignedTo,
                        sequenceNumber: vm.activity.sequenceNumber,
                        predecessors: vm.activity.predecessors,
                        plannedStartDate: vm.activity.plannedStartDate,
                        plannedFinishDate: vm.activity.plannedFinishDate,
                        actualStartDate: vm.activity.actualStartDate,
                        actualFinishDate: vm.activity.actualFinishDate,
                        status: vm.activity.status,
                        percentComplete: 0,
                        createdDate: vm.activity.createdDate,
                        createdBy: vm.activity.createdBy
                    };
                }

                $rootScope.$on('app.project.plan.activity.edit', editActivity);

                /*------------- For Template Activity -----------------*/

                if (vm.mode == "TemplateActivityEdit" && vm.activity != null) {
                    vm.newActivity = {
                        id: vm.activity.id,
                        name: vm.activity.name,
                        description: vm.activity.description,
                        wbs: vm.activity.wbs,
                        createdDate: vm.activity.createdDate,
                        createdBy: vm.activity.createdBy
                    };
                }

                $rootScope.$on('app.template.activity.edit', editTemplateActivity);
                loadProjectPersons();
            })();
        }
    }
);