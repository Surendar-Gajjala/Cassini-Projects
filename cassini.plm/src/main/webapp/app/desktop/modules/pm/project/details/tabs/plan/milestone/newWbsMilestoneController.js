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
        module.controller('NewWbsMilestoneController', NewWbsMilestoneController);

        function NewWbsMilestoneController($scope, $rootScope, $timeout, $translate, $stateParams, $state, ProjectService,
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

            function createMilestone() {
                if (validate()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.lastSelectedPerson = vm.wbsMileStone.assignedTo;
                    vm.wbsMileStone.assignedTo = vm.wbsMileStone.assignedTo.id;
                    MilestoneService.createMilestone(vm.wbs.id, vm.wbsMileStone).then(
                        function (data) {
                            vm.milestone = data;
                            vm.wbsMileStone = {
                                id: null,
                                name: null,
                                description: null,
                                wbs: vm.wbs.id,
                                assignedTo: null,
                                sequenceNumber: null,
                                predecessors: [],
                                plannedFinishDate: null,
                                actualFinishDate: null,
                                status: 'PENDING'
                            };
                            $scope.callback();
                            $rootScope.showSuccessMessage(milestoneCreatedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.wbsMileStone.assignedTo = vm.lastSelectedPerson;
                            $rootScope.hideBusyIndicator();
                            $rootScope.showWarningMessage(error.message);
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

            function createTemplateWbsMilestone() {
                if (validateTemplateMilestone()) {
                    TemplateMilestoneService.getMilestoneByNameAndWbs(vm.wbsMileStone.name, vm.wbsMileStone.wbs).then(
                        function (milestoneName) {
                            if (milestoneName != null && milestoneName != "") {
                                $rootScope.showWarningMessage(vm.wbsMileStone.name + " : Milestone already exist on Wbs");
                            } else {
                                TemplateMilestoneService.createTemplateMilestone(vm.wbsMileStone).then(
                                    function (data) {
                                        $scope.callback();
                                        vm.wbsMileStone = {
                                            id: null,
                                            name: null,
                                            description: null,
                                            wbs: vm.wbs.id
                                        };
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
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
                ProjectService.getAllProjectMembers(vm.project.id).then(
                    function (data) {
                        vm.persons = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                vm.wbs = $scope.data.milestoneWbsData;
                vm.milestone = $scope.data.milestoneData;
                vm.mode = $scope.data.milestoneMode;
                vm.project = $scope.data.projectData;
                if (vm.mode == 'New') {
                    vm.wbsMileStone = {
                        id: null,
                        name: null,
                        description: null,
                        wbs: vm.wbs.id,
                        assignedTo: null,
                        sequenceNumber: null,
                        predecessors: [],
                        plannedFinishDate: null,
                        actualFinishDate: null,
                        status: 'PENDING'
                    };
                }
                $rootScope.$on('app.project.plan.milestone.new', createMilestone);

                /*---------------- For Template Milestone ---------------------*/

                if (vm.mode == "TemplateMilestoneNew") {
                    vm.wbsMileStone = {
                        id: null,
                        name: null,
                        description: null,
                        wbs: vm.wbs.id
                    };
                }

                $rootScope.$on('app.template.milestone.new', createTemplateWbsMilestone);
                loadProjectPersons();
            })();
        }
    }
);