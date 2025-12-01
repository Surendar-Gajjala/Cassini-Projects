define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('EquipmentService', EquipmentService);

        function EquipmentService(httpFactory) {
            return {
                createEquipment: createEquipment,
                updateEquipment: updateEquipment,
                getEquipment: getEquipment,
                getAllEquipments: getAllEquipments,
                deleteEquipment: deleteEquipment,
                getMultipleEquipments: getMultipleEquipments,
                uploadImage: uploadImage,
                saveEquipmentAttributes:saveEquipmentAttributes
            };

            function createEquipment(equipment) {
                var url = "api/mes/equipments";
                return httpFactory.post(url, equipment)
            }

            function updateEquipment(equipment) {
                var url = "api/mes/equipments/" + equipment.id;
                return httpFactory.put(url, equipment);
            }

            function getEquipment(id) {
                var url = "api/mes/equipments/" + id;
                return httpFactory.get(url)
            }

            function deleteEquipment(equipment) {
                var url = "api/mes/equipments/" + equipment;
                return httpFactory.delete(url);
            }

            function getAllEquipments(pageable, filters) {
                var url = "api/mes/equipments/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}&workOrder={4}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery,filters.workOrder);
                return httpFactory.get(url);
            }

            function getMultipleEquipments(equipmentIds) {
                var url = "api/mes/equipment/multiple/[" + equipmentIds + "]";
                return httpFactory.get(url);
            }

            function uploadImage(instrumentId, file) {
                var url = "api/mes/equipments/" + instrumentId + "/image";
                return httpFactory.upload(url, file);
            }
            function saveEquipmentAttributes(id ,attributes){
                var url = "api/mes/equipments/" + id + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }
        }
    }
);