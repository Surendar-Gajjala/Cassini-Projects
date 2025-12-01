define(
    [

        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/shared/services/core/templateActivityService'
    ],
    function (module) {
        module.controller('NewTemplateActivityTaskController', NewTemplateActivityTaskController);

        function NewTemplateActivityTaskController($scope, $stateParams, $q, $rootScope, $translate, $timeout, TemplateActivityService) {

            var vm = this;

            var activityId = $stateParams.activityId;

            vm.newTask = {
                id: null,
                activity: activityId,
                name: null,
                description: null
            };

            var parsed = angular.element("<div></div>");
            var createdSuccessfullyMsg = parsed.html($translate.instant("TASK_CREATED_MESSAGE")).html();
            var nameValidationMsg = parsed.html($translate.instant("ENTER_TASK_NAME")).html();

            function createTask() {
                if (validate()) {
                    TemplateActivityService.createTemplateTask(vm.newTask).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            vm.newTask = {
                                id: null,
                                activity: activityId,
                                name: null,
                                description: null
                            };
                            $rootScope.showSuccessMessage(createdSuccessfullyMsg);
                            $scope.callback(data);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

            }

            function validate() {
                var valid = true;
                if (vm.newTask.name == null || vm.newTask.name == "") {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidationMsg);
                }

                return valid;
            }

            (function () {
                $scope.$on('app.project.templateActivity.task.new', createTask);
            })();
        }
    }
);