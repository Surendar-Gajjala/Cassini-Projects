define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('OperationTypeService', OperationTypeService);

        function OperationTypeService($q, httpFactory) {
            return {
                createOperationType: createOperationType,
                updateOperationType: updateOperationType,
                deleteOperationType: deleteOperationType,
                getOperationType: getOperationType,
                getAllOperationTypes: getAllOperationTypes,
                getMultipleOperationTypes: getMultipleOperationTypes

            };

            function createOperationType(operationType) {
                var url = "api/plm/operationTypes";
                return httpFactory.post(url, operationType);
            }

            function updateOperationType(operationType) {
                var url = "api/plm/operationTypes/" + operationType.id;
                return httpFactory.put(url, operationType);
            }

            function deleteOperationType(id) {
                var url = "api/plm/operationTypes" + id;
                return httpFactory.delete(url);
            }

            function getOperationType(id) {
                var url = "api/plm/operationTypes" + id;
                return httpFactory.get(url);
            }

            function getAllOperationTypes() {
                var url = "api/plm/operationTypes";
                return httpFactory.get(url);
            }

            function getMultipleOperationTypes(ids) {
                var url = "api/plm/operationTypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

        }
    }
);