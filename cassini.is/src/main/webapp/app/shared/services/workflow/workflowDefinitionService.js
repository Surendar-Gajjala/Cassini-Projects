define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('WorkflowDefinitionService', WorkflowDefinitionService);

        function WorkflowDefinitionService($q, httpFactory) {
            return {
                createWorkflowDefinition: createWorkflowDefinition,
                updateWorkflowDefinition: updateWorkflowDefinition,
                getWorkflowDefinition: getWorkflowDefinition,
                getAllWorkflowDefinitions: getAllWorkflowDefinitions,
                getWorkflowDefs: getWorkflowDefs,
                freeTextSearch: freeTextSearch,
                deleteWorkflow: deleteWorkflow,
                getWorkflow: getWorkflow

            };

            function createWorkflowDefinition(workflowDefinition) {
                var url = "api/is/workflows/definitions";
                return httpFactory.post(url, workflowDefinition);
            }

            function updateWorkflowDefinition(workflowDefinition) {
                var url = "api/is/workflows/definitions/" + workflowDefinition.id;
                return httpFactory.put(url, workflowDefinition);
            }

            function getWorkflowDefinition(definitionId) {
                var url = "api/is/workflows/definitions/" + definitionId;
                return httpFactory.get(url);
            }

            function getAllWorkflowDefinitions(pageable) {
                var url = "api/is/workflows/definitions?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getWorkflowDefs() {
                var url = "api/is/workflows/definitions/all";
                return httpFactory.get(url);
            }

            function freeTextSearch(pageable, freetext) {
                var url = "api/is/workflows/definitions/freetextsearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);

                url += "&searchQuery={0}".
                    format(freetext);
                return httpFactory.get(url);
            }

            function deleteWorkflow(workflowId) {
                var url = "api/is/workflows/definitions/workflow/" + workflowId;
                return httpFactory.delete(url);
            }

            function getWorkflow(taskId) {
                var url = "api/is/workflows/instances/" + taskId;
                return httpFactory.get(url);
            }
        }
    }
);