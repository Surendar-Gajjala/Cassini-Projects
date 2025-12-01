define(
    [
        'app/desktop/modules/template/templateActivity/templateActivity.module',
        'app/shared/services/core/templateActivityService'
    ],
    function (module) {
        module.controller('TemplateActivityTaskController', TemplateActivityTaskController);

        function TemplateActivityTaskController($scope, $stateParams, $rootScope, $translate, $timeout, $state, CommonService,
                                                DialogService, TemplateActivityService) {

            var vm = this;
            var activityId = $stateParams.activityId;
            var parsed = angular.element("<div></div>");

            $rootScope.addActivityTasks = addActivityTasks;
            vm.editTask = editTask;
            vm.deleteTask = deleteTask;
            vm.cancelChanges = cancelChanges;
            vm.updateTask = updateTask;

            var newTask = parsed.html($translate.instant("NEW_TASK")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            vm.addTask = parsed.html($translate.instant("ADD_TASK")).html();

            function addActivityTasks() {
                var options = {
                    title: newTask,
                    template: 'app/desktop/modules/template/templateActivity/details/task/newTemplateActivityTaskView.jsp',
                    controller: 'NewTemplateActivityTaskController as newTemplateActivityTaskVm',
                    resolve: 'app/desktop/modules/template/templateActivity/details/task/newTemplateActivityTaskController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.project.templateActivity.task.new'}
                    ],
                    callback: function (data) {
                        loadActivityTasks();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            vm.activityTasks = [];
            function loadActivityTasks() {
                TemplateActivityService.getTemplateActivityTasks(activityId).then(
                    function (data) {
                        vm.activityTasks = data;
                        angular.forEach(vm.activityTasks, function (task) {
                            task.editMode = false;
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function updateTask(task) {
                TemplateActivityService.updateTask(task).then(
                    function (data) {
                        task.editMode = false;
                        loadActivityTasks();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function editTask(task) {
                task.newName = task.name;
                task.newDescription = task.description;
                task.editMode = true;
            }

            function cancelChanges(task) {
                task.name = task.newName;
                task.description = task.newDescription;
                task.editMode = false;
            }

            var taskDialogTitle = parsed.html($translate.instant("TASK_DIALOG_TITLE")).html();
            var taskDialogMessage = parsed.html($translate.instant("TASK_DIALOG_MESSAGE")).html();
            var taskDeletedMessage = parsed.html($translate.instant("TASK_DELETED_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();

            function deleteTask(task) {
                var options = {
                    title: taskDialogTitle,
                    message: taskDialogMessage + " [ " + task.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TemplateActivityService.deleteTemplateActivityTask(task).then(
                            function (data) {
                                var index = vm.activityTasks.indexOf(task);
                                vm.activityTasks.splice(index, 1);
                                $rootScope.showSuccessMessage(taskDeletedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            (function () {
                $scope.$on('app.template.activity.tabActivated', function (event, data) {
                    if (data.tabId == 'details.tasks') {
                        loadActivityTasks();
                    }

                })
            })();
        }
    }
)
;