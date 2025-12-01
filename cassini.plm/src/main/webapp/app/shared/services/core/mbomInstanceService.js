define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MBOMInstanceService', MBOMInstanceService);

        function MBOMInstanceService(httpFactory) {
            return {
                createMBOMInstance: createMBOMInstance,
                updateMBOMInstance: updateMBOMInstance,
                getMBOMInstance: getMBOMInstance,
                deleteMBOMInstance: deleteMBOMInstance,
                getMBOMInstances: getMBOMInstances,
                getMbomInstanceTabCounts: getMbomInstanceTabCounts,
                getMBOMInstanceItems: getMBOMInstanceItems,
                getMBOMInstanceItemChildren: getMBOMInstanceItemChildren,
                getMBOMInstanceOperations: getMBOMInstanceOperations,
                getMbomInstanceOperation:getMbomInstanceOperation,
                getMbomInstanceOperationInstructions: getMbomInstanceOperationInstructions,
                getMbomInstanceOperationItems: getMbomInstanceOperationItems,
                getMbomInstanceOperationResources: getMbomInstanceOperationResources,
                updateMbomInstanceOperationResource: updateMbomInstanceOperationResource,
                deleteMbomInstanceOperationResource: deleteMbomInstanceOperationResource,
                getMbomInstanceOperationCounts: getMbomInstanceOperationCounts,

            };

            function getMbomInstanceOperation(operationId) {
                var url = "api/mes/mbominstances/operations/" + operationId;
                return httpFactory.get(url);
            }

            function getMbomInstanceOperationInstructions(operationId) {
                var url = "api/mes/mbominstances/operations/" + operationId + "/instructions";
                return httpFactory.get(url);
            }

            function getMbomInstanceOperationItems(operationId) {
                var url = "api/mes/mbominstances/operations/" + operationId + "/parts";
                return httpFactory.get(url);
            }

            function getMbomInstanceOperationResources(operationId) {
                var url = "api/mes/mbominstances/operations/" + operationId + "/resources";
                return httpFactory.get(url);
            }
            function updateMbomInstanceOperationResource(operationId, resource) {
                var url = "api/mes/mbominstances/operations/" + operationId + "/resources/" + resource.id;
                return httpFactory.put(url, resource);
            }
            function deleteMbomInstanceOperationResource(operationId, resourceId) {
                var url = "api/mes/mbominstances/operations/" + operationId + "/resources/" + resourceId;
                return httpFactory.delete(url);
            }
            function createMBOMInstance(mbom) {
                var url = "api/mes/mbominstances";
                return httpFactory.post(url, mbom)
            }

            function updateMBOMInstance(mbom) {
                var url = "api/mes/mbominstances/" + mbom.id;
                return httpFactory.put(url, mbom);
            }

            function getMBOMInstance(id) {
                var url = "api/mes/mbominstances/" + id;
                return httpFactory.get(url)
            }

            function getMbomInstanceTabCounts(id) {
                var url = "api/mes/mbominstances/" + id + "/counts";
                return httpFactory.get(url)
            }

            function deleteMBOMInstance(mbom) {
                var url = "api/mes/mbominstances/" + mbom;
                return httpFactory.delete(url);
            }

            function getMBOMInstances() {
                var url = "api/mes/mbominstances";
                return httpFactory.get(url);
            }

            function getMBOMInstanceItems(id, hierarchy) {
                var url = "api/mes/mbominstances/" + id + "/items?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getMBOMInstanceOperations(id) {
                var url = "api/mes/mbominstances/" + id + "/operations";
                return httpFactory.get(url);
            }

            function getMBOMInstanceItemChildren(id, itemId) {
                var url = "api/mes/mbominstances/" + id + "/items/" + itemId + "/children";
                return httpFactory.get(url);
            }

            function getMbomInstanceOperationCounts(routeId) {
                var url = "api/mes/mbominstances/operations/" + routeId + "/counts";
                return httpFactory.get(url)
            }
        }
    }
);