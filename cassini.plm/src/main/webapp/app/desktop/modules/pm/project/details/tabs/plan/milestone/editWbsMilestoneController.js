define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/milestoneService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/templateMilestoneService'
    ],

    function (module) {
        module.controller('EditWbsMilestoneController', EditWbsMilestoneController);

        function EditWbsMilestoneController($scope, $rootScope, $timeout, $translate, $stateParams, $state, ProjectService,
                                            ItemService, MilestoneService, CommonService, TemplateMilestoneService, LoginService) {

            var vm = this;
            vm.persons = [];

            var parsed = angular.element("<div></div>");
            var milestoneCreatedMessage = parsed.html($translate.instant("MILESTONE_CREATED_MESSAGE")).html();
            var milestoneUpdatedMessage = parsed.html($translate.instant("MILESTONE_UPDATED_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var assignedToValidation = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("MILESTONE_FINISH_DATE_VALIDATION")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var beforePlannedFinishDateValidation = parsed.html($translate.instant("BEFORE_PLANNED_FINISH_DATE_VALID")).html();
            var afterPlannedStartDateValidation = parsed.html($translate.instant("AFTER_PLANNED_START_DATE_VALID")).html();

            vm.lastSelectedPerson = null;

            function editMilestone() {
                if (validate()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.wbsMileStone.assignedTo = vm.wbsMileStone.assignedTo.id;
                    MilestoneService.updateMilestone(vm.wbsMileStone.wbs, vm.wbsMileStone).then(
                        function (data) {
                            vm.milestone = data;
                            vm.wbsMileStone.assignedTo = vm.milestone.person;
                            $scope.callback();
                            $rootScope.hideSidePanel();
                            $rootScope.showSuccessMessage(milestoneUpdatedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.wbsMileStone.name = vm.milestone.name;
                            vm.wbsMileStone.assignedTo = vm.milestone.person;
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.wbsMileStone.name == null || vm.wbsMileStone.name == "" || vm.wbsMileStone.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.wbsMileStone.assignedTo == null || vm.wbsMileStone.assignedTo == "") {
                    valid = false;
                    $rootScope.showWarningMessage(assignedToValidation)
                } else if (vm.wbsMileStone.plannedFinishDate == null || vm.wbsMileStone.plannedFinishDate == "") {
                    valid = false;
                    $rootScope.showWarningMessage(plannedFinishDateValidation)
                } else if (vm.wbsMileStone.plannedFinishDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var plannedFinishDate = moment(vm.wbsMileStone.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var projectPlannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val1 = plannedFinishDate.isSame(todayDate) || plannedFinishDate.isAfter(todayDate);
                    var val2 = plannedFinishDate.isAfter(projectPlannedFinishDate);
                    var val3 = plannedFinishDate.isBefore(projectPlannedStartDate);
                    if (!val1) {
                        valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }

                    if (val2) {
                        valid = false;
                        $rootScope.showWarningMessage(beforePlannedFinishDateValidation + " : " + vm.project.plannedFinishDate);
                    }
                    if (val3) {
                        valid = false;
                        $rootScope.showWarningMessage(afterPlannedStartDateValidation + " : " + vm.project.plannedStartDate);
                    }

                }

                return valid;
            }

            function editTemplateMilestone() {
                if (validateTemplateMilestone()) {
                    TemplateMilestoneService.updateTemplateMilestone(vm.wbsMileStone).then(
                        function (data) {
                            $scope.callback();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateTemplateMilestone() {
                var valid = true;
                if (vm.wbsMileStone.name == null || vm.wbsMileStone.name == "" || vm.wbsMileStone.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            function loadProjectPersons() {
                vm.persons = [];
                if (vm.project != null) {
                    ProjectService.getAllProjectMembers(vm.project.id).then(
                        function (data) {
                            vm.persons = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                vm.wbs = $scope.data.milestoneWbsData;
                vm.milestone = $scope.data.milestoneData;
                vm.mode = $scope.data.milestoneMode;
                vm.project = $scope.data.projectData;
                if (vm.mode == 'Edit' && vm.milestone != null) {
                    vm.wbsMileStone = {
                        id: vm.milestone.id,
                        name: vm.milestone.name,
                        description: vm.milestone.description,
                        wbs: vm.milestone.parent,
                        assignedTo: vm.milestone.person,
                        sequenceNumber: vm.milestone.sequenceNumber,
                        predecessors: vm.milestone.predecessors,
                        plannedFinishDate: vm.milestone.plannedFinishDate,
                        actualFinishDate: vm.milestone.actualFinishDate,
                        status: vm.milestone.status
                    };
                }

                $rootScope.$on('app.project.plan.milestone.edit', editMilestone);

                /*---------------- For Template Milestone ---------------------*/

                if (vm.mode == "TemplateMilestoneEdit" && vm.milestone != null) {
                    vm.wbsMileStone = {
                        id: vm.milestone.id,
                        name: vm.milestone.name,
                        description: vm.milestone.description,
                        wbs: vm.milestone.wbs,
                        createdDate: vm.milestone.createdDate,
                        createdBy: vm.milestone.createdBy
                    };
                }
                $rootScope.$on('app.template.milestone.edit', editTemplateMilestone);
                loadProjectPersons();
            })();
        }
    }
);