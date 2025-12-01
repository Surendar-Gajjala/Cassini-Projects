define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/milestoneService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/templateWbsService'

    ],

    function (module) {
        module.controller('NewWbsController', NewWbsController);

        function NewWbsController($scope, $rootScope, $timeout, $translate, $stateParams, $state, ProjectService,
                                  ItemService, MilestoneService, CommonService, ProjectTemplateService, TemplateWbsService) {

            var vm = this;
            vm.projectId = $stateParams.projectId;
            vm.templateId = $stateParams.templateId;

            var parsed = angular.element("<div></div>");
            var wbsNameValidation = parsed.html($translate.instant("WBS_NAME_VALIDATION")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var wbsNameAlreadyExist = parsed.html($translate.instant("WBS_NAME_ALREADY_EXIST")).html();

            function createWbs() {
                if (validate()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    ProjectService.getParentWbsByName(vm.projectId, vm.newWbs.name).then(
                        function (wbsName) {
                            vm.existedWbsName = wbsName;
                            if (vm.existedWbsName != null && vm.existedWbsName != "" && vm.existedWbsName != undefined) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showWarningMessage(vm.newWbs.name + " : " + wbsNameValidation);
                            } else {
                                vm.newWbs.parent = null;
                                vm.newWbs.project = vm.project;
                                ProjectService.createWBSElement(vm.projectId, vm.newWbs).then(
                                    function (data) {
                                        $scope.callback();
                                        $rootScope.hideBusyIndicator();
                                        vm.newWbs = {
                                            id: null,
                                            name: null,
                                            description: null,
                                            parent: null,
                                            project: vm.project
                                        };
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                );
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function editWbs() {
                if (validate()) {
                    ProjectService.updateWBSElement(vm.projectId, vm.newWbs).then(
                        function (data) {
                            vm.wbs = data;
                            $scope.callback();
                        }, function (error) {
                            $rootScope.showWarningMessage(vm.newWbs.name + " : Wbs name already exist.")
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newWbs.name == null || vm.newWbs.name == "" || vm.newWbs.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            function createTemplateWbsNew() {
                if (validate()) {
                    vm.newWbs.template = vm.template.id;
                    TemplateWbsService.getTemplateWbsByName(vm.newWbs.template, vm.newWbs.name).then(
                        function (templateWbs) {
                            if (templateWbs != "" && templateWbs != null && templateWbs != undefined) {
                                $rootScope.showWarningMessage(wbsNameAlreadyExist);
                                $rootScope.hideBusyIndicator();
                            } else {
                                TemplateWbsService.createTemplateWbs(vm.newWbs).then(
                                    function (data) {
                                        $scope.callback();
                                        vm.newWbs = {
                                            id: null,
                                            name: null,
                                            description: null,
                                            template: null
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
                    );
                }
            }

            function editTemplateWbsNew() {
                if (validate()) {
                    TemplateWbsService.getTemplateWbsByName(vm.newWbs.template, vm.newWbs.name).then(
                        function (templateWbs) {

                            if (templateWbs != null && templateWbs != "") {
                                if (templateWbs.name == vm.presentWbsName) {
                                    TemplateWbsService.createTemplateWbs(vm.newWbs).then(
                                        function (data) {
                                            $scope.callback();
                                            $rootScope.hideSidePanel();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                } else {
                                    $rootScope.showWarningMessage(wbsNameAlreadyExist);
                                    $rootScope.hideBusyIndicator();
                                }
                            } else {
                                TemplateWbsService.createTemplateWbs(vm.newWbs).then(
                                    function (data) {
                                        $scope.callback();
                                        $rootScope.hideSidePanel();
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
                    );
                }
            }

            function loadProject() {
                if (vm.projectId != undefined) {
                    ProjectService.getProject(vm.projectId).then(
                        function (data) {
                            vm.project = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadTemplate() {
                if (vm.templateId != undefined) {
                    ProjectTemplateService.getProjectTemplate(vm.templateId).then(
                        function (data) {
                            vm.template = data;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                vm.wbsData = $scope.data.wbsElementData;
                vm.mode = $scope.data.wbsMode;
                if (vm.mode == 'New') {
                    vm.newWbs = {
                        id: null,
                        name: null,
                        description: null,
                        parent: null,
                        project: null
                    };
                }

                if (vm.mode == 'TemplateWbsNew') {
                    vm.newWbs = {
                        id: null,
                        name: null,
                        description: null,
                        template: null
                    };
                }
                $rootScope.$on('app.project.plan.wbs.new', createWbs);
                $rootScope.$on('app.template.wbs.new', createTemplateWbsNew);
                loadProject();
                loadTemplate();
            })();
        }
    }
);