define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('OperationService', OperationService);

        function OperationService(httpFactory) {
            return {
                createOperation: createOperation,
                updateOperation: updateOperation,
                getOperation: getOperation,
                deleteOperation: deleteOperation,
                getAllOperations: getAllOperations,
                getMultipleOperations: getMultipleOperations,
                saveOperationAttributes: saveOperationAttributes,
                deleteOperationResource: deleteOperationResource,
                createOperationResources: createOperationResources,
                getAllOperationResources: getAllOperationResources,
                updateOperationResource: updateOperationResource,
                getOperationResourcesByGroup: getOperationResourcesByGroup
            };

            function createOperation(operation) {
                var url = "api/mes/operations";
                return httpFactory.post(url, operation)
            }


            function updateOperation(operation) {
                var url = "api/mes/operations/" + operation.id;
                return httpFactory.put(url, operation);
            }

            function getOperation(id) {
                var url = "api/mes/operations/" + id;
                return httpFactory.get(url)
            }

            function deleteOperation(operation) {
                var url = "api/mes/operations/" + operation;
                return httpFactory.delete(url);
            }

            function getAllOperations(pageable, filters) {
                var url = "api/mes/operations/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}&bop={4}&bopPlan={5}&workCenter={6}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery, filters.bop, filters.bopPlan, filters.workCenter);
                return httpFactory.get(url);
            }

            function getMultipleOperations(operationIds) {
                var url = "api/mes/operations/multiple/[" + operationIds + "]";
                return httpFactory.get(url);
            }

            function saveOperationAttributes(id, attributes) {
                var url = "api/mes/operations/" + id + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function createOperationResources(resource) {
                var url = "api/mes/operations/resources";
                return httpFactory.post(url, resource)
            }

            function getAllOperationResources(id) {
                var url = "api/mes/operations/" + id + "/resources";
                return httpFactory.get(url)
            }

            function getOperationResourcesByGroup(id, planId) {
                var url = "api/mes/operations/" + id + "/resources/" + planId + "/list";
                return httpFactory.get(url)
            }

            function deleteOperationResource(id, resource) {
                var url = "api/mes/operations/" + id + "/resource/" + resource;
                return httpFactory.delete(url);
            }

            function updateOperationResource(resource) {
                var url = "api/mes/operations/resources/" + resource.id;
                return httpFactory.put(url, resource);
            }

        }
    }
);