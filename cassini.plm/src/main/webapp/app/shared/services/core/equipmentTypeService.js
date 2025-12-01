define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('EquipmentTypeService', EquipmentTypeService);

        function EquipmentTypeService($q, httpFactory) {
            return {
                createEquipmentType: createEquipmentType,
                updateEquipmentType: updateEquipmentType,
                deleteEquipmentType: deleteEquipmentType,
                getEquipmentType: getEquipmentType,
                getAllEquipmentTypes: getAllEquipmentTypes,
                getMultipleEquipmentTypes: getMultipleEquipmentTypes

            };

            function createEquipmentType(equipmentType) {
                var url = "api/plm/equipmentTypes";
                return httpFactory.post(url, equipmentType);
            }

            function updateEquipmentType(equipmentType) {
                var url = "api/plm/equipmentTypes/" + equipmentType.id;
                return httpFactory.put(url, equipmentType);
            }

            function deleteEquipmentType(id) {
                var url = "api/plm/equipmentTypes/" + id;
                return httpFactory.delete(url);
            }

            function getEquipmentType(id) {
                var url = "api/plm/equipmentTypes/" + id;
                return httpFactory.get(url);
            }

            function getAllEquipmentTypes() {
                var url = "api/plm/equipmentTypes";
                return httpFactory.get(url);
            }

            function getMultipleEquipmentTypes(ids) {
                var url = "api/plm/equipmentTypes/multiple/[{ids}]";
                return httpFactory.get(url);
            }

        }
    }
);