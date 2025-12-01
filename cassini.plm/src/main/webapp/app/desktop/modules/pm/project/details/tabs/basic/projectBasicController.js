define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'

    ],
    function (module) {
        module.controller('ProjectBasicController', ProjectBasicController);

        function ProjectBasicController($scope, $translate, $rootScope, $timeout, $sce, $state, $stateParams, $cookies, ProjectService, CommonService,
                                        LoginService, ProgramService) {

            $rootScope.viewInfo.title = "Projects";

            var vm = this;
            vm.projectId = $stateParams.projectId;
            vm.project = null;
            vm.back = back;
            vm.persons = [];
            vm.updateProject = updateProject;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            $rootScope.external = $rootScope.loginPersonDetails;
            vm.newProjectManager = null;

            vm.cancelChanges = cancelChanges;

            function cancelChanges(project) {
                vm.project.editMode = false;
            }

            var parsed = angular.element("<div></div>");
            var updatedSuccessfullyMsg = parsed.html($translate.instant("UPDATED_SUCCESS_MESSAGE")).html();
            var NameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
            var plannedStartDateValidation = parsed.html($translate.instant("PLANNED_START_DATE_VALIDATION")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var startDateValidation = parsed.html($translate.instant("START_DATE_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();
            var plannedStartDateBeforeValidation = parsed.html($translate.instant("PLANNED_STARTBEFORE_MSG")).html();
            var plannedEndDateAfterValidation = parsed.html($translate.instant("PLANNED_ENDAFTER_MSG")).html();
            vm.clickToUpdateDes = parsed.html($translate.instant("CLICK_TO_UPDATE_DESCRIPTION")).html();
            vm.nameTooltip = parsed.html($translate.instant("CLICK_TO_UPDATE_NAME")).html();
            vm.clickToUpdatePerson = parsed.html($translate.instant("CLICK_TO_UPDATE_PERSON")).html();

            function loadProject() {
                vm.loading = true;
                ProjectService.getProject(vm.projectId).then(
                    function (data) {
                        vm.project = data;
                        $rootScope.projectInfo = vm.project;
                        vm.project.editMode = false;
                        vm.finishDateFlag = false;
                        $rootScope.projectId = vm.project.id;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                        if (vm.project.program != null && vm.project.program != "") {
                            ProgramService.getProgram(vm.project.program).then(
                                function (data) {
                                    vm.program = data;
                                }
                            )
                        }
                        CommonService.getMultiplePersonReferences([vm.project], ['createdBy', 'modifiedBy', 'projectManager']);
                        $rootScope.viewInfo.title = vm.project.name;
                        vm.projectName = $rootScope.viewInfo.title;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function back() {
                window.history.back();
            }

            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && !login.external) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.changeFinishDate = changeFinishDate;
            vm.cancelFinishDate = cancelFinishDate;
            function changeFinishDate() {
                vm.project.editMode = true;
            }

            function cancelFinishDate() {
                vm.project.editMode = false;
                loadProject();
            }

            vm.finishDateFlag = false;
            vm.changeFinishDatee = changeFinishDatee;
            vm.cancelFinishDatee = cancelFinishDatee;
            function changeFinishDatee() {
                vm.finishDateFlag = true;
            }

            function cancelFinishDatee() {
                vm.finishDateFlag = false;
                loadProject();
            }

            function validate() {
                vm.valid = true;
                if (vm.newProjectManager != null) {
                    vm.project.projectManager = vm.newProjectManager.id;
                }

                if (vm.project.name == null || vm.project.name == undefined || vm.project.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(NameValidation);
                }

                else if (vm.project.plannedStartDate == null ||
                    vm.project.plannedStartDate == undefined || vm.project.plannedStartDate == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage(plannedStartDateValidation);
                }
                else if (vm.project.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var minDate = moment($rootScope.projectInfo.minDate, $rootScope.applicationDateSelectFormat);
                    var maxDate = moment($rootScope.projectInfo.maxDate, $rootScope.applicationDateSelectFormat);
                    /*var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                     if (!val1) {
                     vm.valid = false;
                     $rootScope.showWarningMessage(startDateValidation);
                     }*/
                    /*if (vm.project.plannedFinishDate == null || vm.project.plannedFinishDate == undefined || vm.project.plannedFinishDate == "") {
                     vm.valid = false;

                     $rootScope.showWarningMessage(plannedFinishDateValidation);
                     }*/
                }

                if (vm.project.plannedStartDate != null && vm.project.plannedFinishDate != null && vm.project.plannedStartDate != "" && vm.project.plannedFinishDate != "") {
                    var plannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                    if (!val) {
                        vm.valid = false;
                        $rootScope.showWarningMessage(finishDateValidation);
                    }
                    if (plannedStartDate.isAfter(minDate)) {
                        vm.valid = false;
                        $rootScope.showWarningMessage(plannedStartDateBeforeValidation + $rootScope.projectInfo.minDate);
                    }
                    if (plannedFinishDate.isBefore(maxDate)) {
                        vm.valid = false;
                        $rootScope.showWarningMessage(plannedEndDateAfterValidation + $rootScope.projectInfo.maxDate);
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

            function updateProject() {
                if (validate() == false) {
                    loadProject();
                }
                else if (validate() == true) {
                    vm.project.projectManager = vm.project.projectManagerObject.id;
                    if (vm.project.plannedStartDate == "") {
                        vm.project.plannedStartDate = null;
                    }
                    if (vm.project.plannedFinishDate == "") {
                        vm.project.plannedFinishDate = null;
                    }
                    $rootScope.showBusyIndicator();
                    ProjectService.updateProject(vm.project).then(
                        function (data) {
                            loadProject();
                            $rootScope.loadProject();
                            $rootScope.viewInfo.title = vm.project.name;
                            $rootScope.viewInfo.description = "Manager: " + vm.project.projectManagerObject.firstName;
                            $rootScope.showSuccessMessage(vm.project.name + " : " + updatedSuccessfullyMsg);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            vm.project.name = vm.projectName;
                            loadProject();
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadProject();
                    }
                });
                loadProject();
                loadPersons();
            })();
        }
    }
);
