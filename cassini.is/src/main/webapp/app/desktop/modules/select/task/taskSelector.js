/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/tm/taskService'
    ],

    function (module) {
        module.factory('TaskSelector', TaskSelector);

        function TaskSelector(TaskService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling task selector dialog");
                var options = {
                    title: 'Select Task',
                    template: 'app/desktop/modules/select/task/taskSelectionView.jsp',
                    controller: 'TaskSelectionController as taskSelectVm',
                    resolve: 'app/desktop/modules/select/task/taskSelectionController',
                    width: 600,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.task.selected'}
                    ],
                    callback: function (selectedTask) {
                        callback(selectedTask, selectedTask.name);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                var taskId = [id];
                TaskService.getTasksByIds(null, taskId).then(
                    function (data) {
                        attribute.refValueString = data[0].name;
                        attribute[attributeName] = data[0].name;
                    }
                );
            }
        }
    }
);