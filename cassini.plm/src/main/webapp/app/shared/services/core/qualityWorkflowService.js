define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('QualityWorkflowService', QualityWorkflowService);

        function QualityWorkflowService($q, httpFactory) {
            return {
                attachObjectWorkflow: attachObjectWorkflow,
                deleteObjectWorkflow: deleteObjectWorkflow

            };


            function attachObjectWorkflow(id, wfId, objectType) {
                var url = "api/plm/objects/" + id + "/workflows/" + wfId + "/attachWorkflow/" + objectType;
                return httpFactory.get(url);
            }

            function deleteObjectWorkflow(id) {
                var url = 'api/plm/objects/' + id + "/workflows/delete";
                return httpFactory.delete(url);
            }
        }
    }
);