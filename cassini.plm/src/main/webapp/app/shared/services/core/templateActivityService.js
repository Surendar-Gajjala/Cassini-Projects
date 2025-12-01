define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TemplateActivityService', TemplateActivityService);

        function TemplateActivityService(httpFactory) {
            return {
                createTemplateActivity: createTemplateActivity,
                updateTemplateActivity: updateTemplateActivity,
                deleteTemplateActivity: deleteTemplateActivity,
                getTemplateActivity: getTemplateActivity,
                getTemplateActivities: getTemplateActivities,
                getActivityByNameAndWbs: getActivityByNameAndWbs,
                createTemplateTask: createTemplateTask,
                getTemplateActivityTasks: getTemplateActivityTasks,
                updateTask: updateTask,
                deleteTemplateActivityTask: deleteTemplateActivityTask,
                createActivites: createActivites,
                createActivityTasks: createActivityTasks,
                getTemplateTask: getTemplateTask,
                attachTaskTempWorkflow:attachTaskTempWorkflow

            };

            function createActivityTasks(activityId, taskList) {
                var url = "api/plm/templates/wbs/activities/" + activityId + "/taskList";
                return httpFactory.post(url, taskList);
            }

            function createActivites(activityList) {
                var url = "api/plm/templates/wbs/activities/activityList";
                return httpFactory.post(url, activityList);
            }


            function createTemplateActivity(activity) {
                var url = "api/plm/templates/wbs/activities";
                return httpFactory.post(url, activity);
            }

            function updateTemplateActivity(activity) {
                var url = "api/plm/templates/wbs/activities/" + activity.id;
                return httpFactory.put(url, activity);
            }

            function deleteTemplateActivity(activityId) {
                var url = "api/plm/templates/wbs/activities/" + activityId;
                return httpFactory.delete(url);
            }

            function getTemplateActivity(activityId) {
                var url = "api/plm/templates/wbs/activities/" + activityId;
                return httpFactory.get(url);
            }

            function getTemplateActivities() {
                var url = "api/plm/templates/wbs/activities";
                return httpFactory.get(url);
            }

            function getActivityByNameAndWbs(name, wbs) {
                var url = "api/plm/templates/wbs/activities/" + wbs + "/byName?activityName=" + name;
                return httpFactory.get(url);
            }

            function createTemplateTask(Task) {
                var url = "api/plm/templates/wbs/activities/task";
                return httpFactory.post(url, Task);
            }

            function getTemplateTask(taskId) {
                var url = "api/plm/templates/wbs/activities/task/" + taskId;
                return httpFactory.get(url);
            }

            function getTemplateActivityTasks(activityId) {
                var url = "api/plm/templates/wbs/activities/" + activityId + "/tasks";
                return httpFactory.get(url);
            }

            function updateTask(task) {
                var url = "api/plm/templates/wbs/activities/task/" + task.id;
                return httpFactory.put(url, task);
            }

            function deleteTemplateActivityTask(taskId) {
                var url = "api/plm/templates/wbs/activities/deleteTask/" + taskId;
                return httpFactory.delete(url);
            }
            function attachTaskTempWorkflow(templateId, wfId) {
                var url = "api/plm/templates/wbs/activities/task/" + templateId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }
        }
    }
);