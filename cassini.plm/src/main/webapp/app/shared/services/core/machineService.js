define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MachineService', MachineService);

        function MachineService(httpFactory) {
            return {
                createMachine: createMachine,
                updateMachine: updateMachine,
                getMachine: getMachine,
                deleteMachine: deleteMachine,
                getAllMachines: getAllMachines,
                getMultipleMachines: getMultipleMachines,
                saveMachineAttributes: saveMachineAttributes,
                uploadImageAttribute: uploadImageAttribute,
                uploadImage: uploadImage
            };

            function createMachine(machine) {
                var url = "api/mes/machines";
                return httpFactory.post(url, machine)
            }

            function updateMachine(machine) {
                var url = "api/mes/machines/" + machine.id;
                return httpFactory.put(url, machine);
            }

            function getMachine(id) {
                var url = "api/mes/machines/" + id;
                return httpFactory.get(url)
            }

            function deleteMachine(machine) {
                var url = "api/mes/machines/" + machine;
                return httpFactory.delete(url);
            }

            function getAllMachines(pageable, filters) {
                var url = "api/mes/machines/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}&workOrder={4}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery, filters.workOrder);
                return httpFactory.get(url);
            }

            function getMultipleMachines(machineIds) {
                var url = "api/mes/machines/multiple/[" + machineIds + "]";
                return httpFactory.get(url);
            }

            function saveMachineAttributes(machineId, attributes) {
                var url = "api/mes/machines/" + machineId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mes/machines/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function uploadImage(machineId, file) {
                var url = "api/mes/machines/" + machineId + "/image";
                return httpFactory.upload(url, file);
            }

        }
    }
);