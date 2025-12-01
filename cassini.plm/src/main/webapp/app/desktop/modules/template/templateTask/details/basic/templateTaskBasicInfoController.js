define(
    [
        'app/desktop/modules/template/templateTask/templateTask.module',
        'app/shared/services/core/templateActivityService'
    ],
    function (module) {
        module.controller('TemplateTaskBasicInfoController', TemplateTaskBasicInfoController);

        function TemplateTaskBasicInfoController($scope, $stateParams, $rootScope, $timeout, $sce, $state, $translate,
                                                 CommonService, TemplateActivityService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            var taskId = $stateParams.taskId;
            vm.updateTask = updateTask;

            function loadTemplateTask() {
                TemplateActivityService.getTemplateTask(taskId).then(
                    function (data) {
                        vm.task = data;
                        vm.taskName = vm.task.name;
                        $rootScope.viewInfo.title = vm.task.name;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var updateTemplateTask = parsed.html($translate.instant("TASK_UPDATE_MSG")).html();
            var activityNameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();

            function updateTask(task) {
                if (task.name == null || task.name == "") {
                    $rootScope.showWarningMessage(activityNameValidation);
                    vm.task.name = vm.taskName;
                } else {
                    $rootScope.showBusyIndicator();
                    TemplateActivityService.updateTask(task).then(
                        function (data) {
                            vm.task = data;
                            $rootScope.viewInfo.title = vm.task.name;
                            $rootScope.showSuccessMessage(updateTemplateTask);
                            $rootScope.hideBusyIndicator();

                        },
                        function (error) {
                            vm.task.name = vm.taskName;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            (function () {
                loadTemplateTask();
            })();
        }
    }
);