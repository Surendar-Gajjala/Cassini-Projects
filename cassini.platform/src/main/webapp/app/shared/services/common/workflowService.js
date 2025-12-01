define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('WorkflowService', WorkflowService);

        function WorkflowService(httpFactory) {
            return {
                getWorkflows: getWorkflows,
                getWorkflow: getWorkflow,
                createWorkflow: createWorkflow,
                updateWorkflow: updateWorkflow,
                deleteWorkflow: deleteWorkflow,

                getActivities: getActivities,
                getActivity: getActivity,
                createActivity: createActivity,
                updateActivity: updateActivity,
                deleteActivity: deleteActivity,

                getActions: getActions,
                getAction: getAction,
                createAction: createAction,
                updateAction: updateAction,
                deleteAction: deleteAction,
                performAction: performAction,

                getTasks: getTasks,
                getTask: getTask,
                createTask: createTask,
                updateTask: updateTask,
                deleteTask: deleteTask
            };

            //Workflows
            function getWorkflows() {
                var url = 'api/wfm/workflows';
                return httpFactory.get(url);
            }

            function getWorkflow(wfId) {
                var url = 'api/wfm/workflows/' + wfId;
                return httpFactory.get(url);
            }

            function createWorkflow(wf) {
                var url = 'api/wfm/workflows';
                return httpFactory.post(url, wf);
            }

            function updateWorkflow(wf) {
                var url = 'api/wfm/workflows/' + wf.id;
                return httpFactory.put(url, wf);
            }

            function deleteWorkflow(wfId) {
                var url = 'api/wfm/workflows/' + wfId;
                return httpFactory.delete(url);
            }


            // Activities
            function getActivities(wfId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities';
                return httpFactory.get(url);
            }

            function getActivity(wfId, activityId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId;
                return httpFactory.get(url);
            }

            function createActivity(wfId, activity) {
                var url = 'api/wfm/workflows/' + wfId + '/activities';
                return httpFactory.post(url, activity);
            }

            function updateActivity(wfId, activity) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activity.id;
                return httpFactory.put(url, activity);
            }

            function deleteActivity(wfId, activityId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId;
                return httpFactory.delete(url);
            }


            //Actions
            function getActions(wfId, activityId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/actions';
                return httpFactory.get(url);
            }

            function getAction(wfId, activityId, actionId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/actions/' + actionId;
                return httpFactory.get(url);
            }

            function createAction(wfId, activityId, action) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/actions';
                return httpFactory.post(url, action);
            }

            function updateAction(wfId, activityId, action) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/actions/' + action.id;
                return httpFactory.put(url, action);
            }

            function deleteAction(wfId, activityId, actionId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/actions/' + actionId;
                return httpFactory.delete(url);
            }

            function performAction(wfId, activityId, actionId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/actions/' + actionId + "/perform";
                return httpFactory.get(url);
            }


            // Tasks
            function getTasks(wfId, activityId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/tasks';
                return httpFactory.get(url);
            }

            function getTask(wfId, activityId, taskId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/tasks/' + taskId;
                return httpFactory.get(url);
            }

            function createTask(wfId, activityId, task) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/tasks';
                return httpFactory.post(url, task);
            }

            function updateTask(wfId, activityId, task) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/tasks/' + task.id;
                return httpFactory.put(url, task);
            }

            function deleteTask(wfId, activityId, taskId) {
                var url = 'api/wfm/workflows/' + wfId + '/activities/' + activityId + '/tasks/' + taskId;
                return httpFactory.delete(url);
            }
        }
    }
);