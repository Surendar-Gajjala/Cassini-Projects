define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('WorkCenterService', WorkCenterService);

        function WorkCenterService(httpFactory) {
            return {
                createWorkCenter: createWorkCenter,
                getWorkCenter: getWorkCenter,
                updateWorkCenter: updateWorkCenter,
                deleteWorkCenter: deleteWorkCenter,
                getAllWorkCenters: getAllWorkCenters,
                getWorkCenters: getWorkCenters,
                getMultipleWorkCenters: getMultipleWorkCenters,
                updateMultipleAssemblyLineWorkCenters: updateMultipleAssemblyLineWorkCenters,
                createWorkCenterOperations: createWorkCenterOperations,
                getAllWorkCenterOperations: getAllWorkCenterOperations,
                deleteworkcenterOperation: deleteworkcenterOperation
            };

            function createWorkCenter(workcenter) {
                var url = "api/mes/workcenters";
                return httpFactory.post(url, workcenter)
            }

            function getWorkCenter(id) {
                var url = "api/mes/workcenters/" + id;
                return httpFactory.get(url)
            }

            function updateWorkCenter(workcenter) {
                var url = "api/mes/workcenters/" + workcenter.id;
                return httpFactory.put(url, workcenter);
            }

            function deleteWorkCenter(workcenter) {
                var url = "api/mes/workcenters/" + workcenter;
                return httpFactory.delete(url);
            }

            function getAllWorkCenters(pageable, filters) {
                var url = "api/mes/workcenters/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&assemblyLine={3}&searchQuery={4}".
                    format(filters.number, filters.type, filters.name, filters.assemblyLine, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleWorkCenters(workcenterIds) {
                var url = "api/mes/workcenters/multiple/[" + workcenterIds + "]";
                return httpFactory.get(url);
            }

            function getWorkCenters() {
                var url = "api/mes/workcenters";
                return httpFactory.get(url);
            }

            function updateMultipleAssemblyLineWorkCenters(assemblyLindId, workCenters) {
                var url = "api/mes/workcenters/multiple/" + assemblyLindId;
                return httpFactory.put(url, workCenters);
            }

            function createWorkCenterOperations(workcenterId, operations) {
                var url = "api/mes/workcenters/" + workcenterId + "/operations/multiple";
                return httpFactory.post(url, operations);
            }

            function getAllWorkCenterOperations(workcenterId) {
                var url = "api/mes/workcenters/" + workcenterId + "/operations";
                return httpFactory.get(url);
            }
            function deleteworkcenterOperation(id) {
                var url = "api/mes/workcenters/operations/" + id;
                return httpFactory.delete(url);
            }

        }
    }
);