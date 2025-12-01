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
        module.controller('EditWbsController', EditWbsController);

        function EditWbsController($scope, $rootScope, $timeout, $translate, $stateParams, $state, ProjectService,
                                   ItemService, MilestoneService, CommonService, ProjectTemplateService, TemplateWbsService) {

            var vm = this;
            vm.projectId = $stateParams.projectId;
            vm.templateId = $stateParams.templateId;

            var parsed = angular.element("<div></div>");
            var wbsNameValidation = parsed.html($translate.instant("WBS_NAME_VALIDATION")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var wbsNameAlreadyExist = parsed.html($translate.instant("WBS_NAME_ALREADY_EXIST")).html();

            function editWbs() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProjectService.updateWBSElement(vm.projectId, vm.newWbs).then(
                        function (data) {
                            vm.wbs = data;
                            $scope.callback();
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
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

            function editTemplateWbsNew() {
                if (validate()) {

                    TemplateWbsService.getTemplateWbsByName(vm.newWbs.template, vm.newWbs.name).then(
                        function (templateWbs) {

                            if (templateWbs != null && templateWbs != "") {
                                if (templateWbs.name == vm.presentWbsName) {
                                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                                    TemplateWbsService.createTemplateWbs(vm.newWbs).then(
                                        function (data) {
                                            $scope.callback();
                                            $rootScope.hideSidePanel();
                                        }, function (error) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showWarningMessage(error.message);
                                        }
                                    )
                                } else {
                                    $rootScope.showWarningMessage(wbsNameAlreadyExist);
                                    $rootScope.hideBusyIndicator();
                                }
                            } else {
                                $rootScope.showBusyIndicator($('#rightSidePanel'));
                                TemplateWbsService.createTemplateWbs(vm.newWbs).then(
                                    function (data) {
                                        $scope.callback();
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.hideSidePanel();
                                    }, function (error) {
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showWarningMessage(error.message);
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
                        }
                    )
                }
            }

            (function () {
                vm.wbsData = $scope.data.wbsElementData;
                vm.mode = $scope.data.wbsMode;
                if (vm.mode == 'Edit' && vm.wbsData != null) {
                    vm.newWbs = {
                        id: vm.wbsData.id,
                        name: vm.wbsData.name,
                        description: vm.wbsData.description,
                        parent: vm.wbsData.parent,
                        project: vm.wbsData.project,
                        createdDate: vm.wbsData.createdDate,
                        sequenceNumber: vm.wbsData.sequenceNumber
                    };
                    if (vm.newWbs.name.indexOf("Copy") > -1) {
                        vm.disableName = true;
                    } else {
                        vm.disableName = false;
                    }
                }

                if (vm.mode == 'TemplateWbsEdit' && vm.wbsData != null) {
                    vm.presentWbsName = null;
                    vm.newWbs = {
                        id: vm.wbsData.id,
                        name: vm.wbsData.name,
                        description: vm.wbsData.description,
                        template: vm.wbsData.template,
                        createdDate: vm.wbsData.createdDate
                    };
                    vm.presentWbsName = vm.wbsData.name;
                }
                $rootScope.$on('app.project.plan.wbs.edit', editWbs);
                $rootScope.$on('app.template.wbs.edit', editTemplateWbsNew);
                loadProject();
                loadTemplate();
            })();
        }
    }
);