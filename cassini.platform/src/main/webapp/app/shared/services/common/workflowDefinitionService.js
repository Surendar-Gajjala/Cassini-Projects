define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('WorkflowDefinitionService', WorkflowDefinitionService);

        function WorkflowDefinitionService(httpFactory) {
            return {
                getWorkflowDefinitions: getWorkflowDefinitions,
                getWorkflowDefinition: getWorkflowDefinition,
                createWorkflowDefinition: createWorkflowDefinition,
                updateWorkflowDefinition: updateWorkflowDefinition,
                deleteWorkflowDefinition: deleteWorkflowDefinition,

                getActivityDefinitions: getActivityDefinitions,
                getActivityDefinition: getActivityDefinition,
                createActivityDefinition: createActivityDefinition,
                updateActivityDefinition: updateActivityDefinition,
                deleteActivityDefinition: deleteActivityDefinition,

                getActionDefinitions: getActionDefinitions,
                getActionDefinition: getActionDefinition,
                createActionDefinition: createActionDefinition,
                updateActionDefinition: updateActionDefinition,
                deleteActionDefinition: deleteActionDefinition,

                getTaskDefinitions: getTaskDefinitions,
                getTaskDefinition: getTaskDefinition,
                createTaskDefinition: createTaskDefinition,
                updateTaskDefinition: updateTaskDefinition,
                deleteTaskDefinition: deleteTaskDefinition
            };

            function getWorkflowDefinitions() {
                var url = 'api/wfm/workflowdefinitions';
                return httpFactory.get(url);
            }

            function getWorkflowDefinition(wfDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId;
                return httpFactory.get(url);
            }

            function createWorkflowDefinition(wfDef) {
                var url = 'api/wfm/workflowdefinitions';
                return httpFactory.post(url, wfDef);
            }

            function updateWorkflowDefinition(wfDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfDef.id;
                return httpFactory.put(url, wfDef);
            }

            function deleteWorkflowDefinition(wfDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId;
                return httpFactory.delete(url);
            }

            function getActivityDefinitions(wfDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities';
                return httpFactory.get(url);
            }

            function getActivityDefinition(wfDefId, activityDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId;
                return httpFactory.get(url);
            }

            function createActivityDefinition(wfDefId, activityDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities';
                return httpFactory.post(url, activityDef);
            }

            function updateActivityDefinition(wfDefId, activityDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDef.id;
                return httpFactory.put(url, activityDef);
            }

            function deleteActivityDefinition(wfDefId, activityDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId;
                return httpFactory.delete(url);
            }


            function getActionDefinitions(wfDefId, activityDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId + '/actions';
                return httpFactory.get(url);
            }

            function getActionDefinition(wfDefId, activityDefId, actionDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId + '/actions/' + actionDefId;
                return httpFactory.get(url);
            }

            function createActionDefinition(wfDefId, activityDefId, actionDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId + '/actions';
                return httpFactory.post(url, actionDef);
            }

            function updateActionDefinition(wfDefId, activityDefId, actionDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId + '/actions/' + actionDef.id;
                return httpFactory.put(url, actionDef);
            }

            function deleteActionDefinition(wfDefId, activityDefId, actionDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfDefId + '/activities/' + activityDefId + '/actions/' + actionDefId;
                return httpFactory.delete(url);
            }

            // Tasks
            function getTaskDefinitions(wfId, activityDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfId + '/activities/' + activityDefId + '/tasks';
                return httpFactory.get(url);
            }

            function getTaskDefinition(wfId, activityDefId, taskDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfId + '/activities/' + activityDefId + '/tasks/' + taskDefId;
                return httpFactory.get(url);
            }

            function createTaskDefinition(wfId, activityDefId, taskDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfId + '/activities/' + activityDefId + '/tasks';
                return httpFactory.post(url, taskDef);
            }

            function updateTaskDefinition(wfId, activityDefId, taskDef) {
                var url = 'api/wfm/workflowdefinitions/' + wfId + '/activities/' + activityDefId + '/tasks/' + taskDef.id;
                return httpFactory.put(url, taskDef);
            }

            function deleteTaskDefinition(wfId, activityId, taskDefId) {
                var url = 'api/wfm/workflowdefinitions/' + wfId + '/activities/' + activityId + '/tasks/' + taskDefId;
                return httpFactory.delete(url);
            }
        }
    }
);