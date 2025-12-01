define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MaintenancePlanService', MaintenancePlanService);

        function MaintenancePlanService(httpFactory) {
            return {
                createMaintenancePlan: createMaintenancePlan,
                updateMaintenancePlan: updateMaintenancePlan,
                getMaintenancePlan: getMaintenancePlan,
                deleteMaintenancePlan: deleteMaintenancePlan,
                getMaintenancePlans: getMaintenancePlans,
                getAllMaintenancePlans: getAllMaintenancePlans,
                getMultipleMaintenancePlans: getMultipleMaintenancePlans,

                getMaintenancePlanOperations: getMaintenancePlanOperations,
                createMaintenancePlanOperation: createMaintenancePlanOperation,
                updateMaintenancePlanOperation: updateMaintenancePlanOperation,
                deleteMaintenancePlanOperation: deleteMaintenancePlanOperation,
                getMaintenancePlansByAsset: getMaintenancePlansByAsset,
                getPlanTabCounts: getPlanTabCounts,
                createMultipleMaintenancePlanOperations: createMultipleMaintenancePlanOperations
            };

            function createMaintenancePlan(maintenancePlan) {
                var url = "api/mro/maintenanceplans";
                return httpFactory.post(url, maintenancePlan)
            }


            function updateMaintenancePlan(maintenancePlan) {
                var url = "api/mro/maintenanceplans/" + maintenancePlan.id;
                return httpFactory.put(url, maintenancePlan);
            }

            function getMaintenancePlan(id) {
                var url = "api/mro/maintenanceplans/" + id;
                return httpFactory.get(url)
            }

            function deleteMaintenancePlan(maintenancePlan) {
                var url = "api/mro/maintenanceplans/" + maintenancePlan;
                return httpFactory.delete(url);
            }

            function getMaintenancePlans() {
                var url = "api/mro/maintenanceplans";
                return httpFactory.get(url);
            }

            function getAllMaintenancePlans(pageable, filters) {
                var url = "api/mro/maintenanceplans/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleMaintenancePlans(maintenancePlanIds) {
                var url = "api/mro/maintenanceplans/multiple/[" + maintenancePlanIds + "]";
                return httpFactory.get(url);
            }

            function getMaintenancePlanOperations(planId) {
                var url = "api/mro/maintenanceplans/" + planId + "/operations";
                return httpFactory.get(url);
            }

            function createMaintenancePlanOperation(planId, operation) {
                var url = "api/mro/maintenanceplans/" + planId + "/operations";
                return httpFactory.post(url, operation);
            }

            function createMultipleMaintenancePlanOperations(planId, operation) {
                var url = "api/mro/maintenanceplans/" + planId + "/operations/multiple";
                return httpFactory.post(url, operation);
            }

            function updateMaintenancePlanOperation(planId, operation) {
                var url = "api/mro/maintenanceplans/" + planId + "/operations/" + operation.id;
                return httpFactory.put(url, operation);
            }

            function deleteMaintenancePlanOperation(planId, operationId) {
                var url = "api/mro/maintenanceplans/" + planId + "/operations/" + operationId;
                return httpFactory.delete(url);
            }

            function getMaintenancePlansByAsset(assetId) {
                var url = "api/mro/maintenanceplans/asset/" + assetId;
                return httpFactory.get(url);
            }

            function getPlanTabCounts(planId) {
                var url = "api/mro/maintenanceplans/" + planId + "/count";
                return httpFactory.get(url);
            }
        }
    }
);