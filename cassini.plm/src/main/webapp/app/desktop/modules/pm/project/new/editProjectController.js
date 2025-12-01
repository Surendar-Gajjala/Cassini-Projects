define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/projectTemplateService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/modules/directives/pmObjectTypeDirective'
    ],
    function (module) {
        module.controller('EditProjectController', EditProjectController);

        function EditProjectController($scope, $rootScope, $timeout, $sce, $state, $translate, $cookies,
                                       ProjectService, CommonService, ProjectTemplateService, LoginService) {
            var vm = this;

            vm.project = {
                name: null,
                description: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                projectManager: null,
                actualStartDate: null,
                actualFinishDate: null,
                type: null
            };

            vm.selectedProjectManager = null;
            vm.creating = false;
            vm.valid = true;
            vm.error = "";
            vm.persons = [];
            vm.selectedTemplate = null;
            var parsed = angular.element("<div></div>");
            var updatedSuccessfullyMsg = parsed.html($translate.instant("UPDATED_SUCCESS_MESSAGE")).html();
            var NameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
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

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
                }
            };

            var projects = [];
            var projectMap = new Hashtable();

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            vm.onSelectType = onSelectType;
            function onSelectType(projectType) {
                vm.attributes = [];
                if (projectType != null && projectType != undefined) {
                    vm.project.type = projectType;
                }
            }

            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true/* && login.external == false*/) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjectDetails() {
                ProjectService.getProject(vm.projectData.id).then(
                    function (data) {
                        vm.project = data;
                        vm.project.projectManager = vm.projectData.projectManagerObject;
                    }
                )
            }

            function loadProjects() {
                ProjectService.getProjects(pageable).then(
                    function (data) {
                        projects = data;
                        angular.forEach(projects.content, function (project) {
                            projectMap.put(project.name, project);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validate() {
                vm.valid = true;

                if (vm.project.name == null || vm.project.name == undefined || vm.project.name == "") {
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
                }
                else if (vm.project.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var plannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                    /*if (!val1) {
                     vm.valid = false;
                     $rootScope.showWarningMessage(startDateValidation);
                     }*/
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
                if (vm.project.projectManager != null && vm.valid == true) {
                    vm.selectedProjectManager = vm.project.projectManager;
                    vm.project.projectManager = vm.project.projectManager.id;
                }

                return vm.valid;
            }

            function update() {
                if (validate() == true) {
                    vm.creating = true;
                    ProjectService.updateProject(vm.project).then(
                        function (data) {
                            $rootScope.hideSidePanel('right');
                            $scope.callback(data);
                            vm.creating = false;
                            $rootScope.showSuccessMessage(vm.project.name + " " + updatedSuccessfullyMsg);
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            vm.creating = false;
                            vm.project.projectManager = vm.selectedProjectManager;
                        }
                    )

                }
            }

            function loadTemplates() {
                ProjectTemplateService.getProjectTemplatesByProgramNull().then(
                    function (data) {
                        vm.templates = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                vm.projectData = $scope.data.projectDetails;
                /*vm.project = {
                    id: vm.projectData.id,
                    name: vm.projectData.name,
                 type: vm.projectData.type,
                    description: vm.projectData.description,
                    plannedStartDate: vm.projectData.plannedStartDate,
                    plannedFinishDate: vm.projectData.plannedFinishDate,
                    projectManager: vm.projectData.projectManagerObject,
                    createdDate: vm.projectData.createdDate,
                    actualStartDate: vm.projectData.actualStartDate,
                    actualFinishDate: vm.projectData.actualFinishDate,
                    makeConversationPrivate: vm.projectData.makeConversationPrivate
                 };*/
                loadProjectDetails();
                loadPersons();
                //loadProjects();
                /*loadTemplates();*/
                $rootScope.$on('app.project.edit', update);
                //}
            })();
        }
    }
);