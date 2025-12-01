define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MaterialService', MaterialService);

        function MaterialService(httpFactory) {
            return {
                createMaterial: createMaterial,
                updateMaterial: updateMaterial,
                getMaterial: getMaterial,
                deleteMaterial: deleteMaterial,
                getAllMaterials: getAllMaterials,
                getMultipleMaterials: getMultipleMaterials,
                createObject: createObject,
                uploadImage: uploadImage,
                getAllFilteredMaterials: getAllFilteredMaterials
            };

            function createMaterial(material) {
                var url = "api/mes/materials";
                return httpFactory.post(url, material)
            }


            function updateMaterial(material) {
                var url = "api/mes/materials/" + material.id;
                return httpFactory.put(url, material);
            }

            function getMaterial(id) {
                var url = "api/mes/materials/" + id;
                return httpFactory.get(url)
            }

            function deleteMaterial(material) {
                var url = "api/mes/materials/" + material;
                return httpFactory.delete(url);
            }

            function getAllMaterials(pageable, filters) {
                var url = "api/mes/materials/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&description{3}&searchQuery={4}&workOrder={5}".
                    format(filters.number, filters.type, filters.name, filters.description, filters.searchQuery, filters.workOrder);
                return httpFactory.get(url);
            }

            function getAllFilteredMaterials(pageable, filters) {
                var url = "api/mes/materials/filtered?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&description{3}&searchQuery={4}&operation={5}".
                    format(filters.number, filters.type, filters.name, filters.description, filters.searchQuery, filters.operation);
                return httpFactory.get(url);
            }

            function getMultipleMaterials(materialIds) {
                var url = "api/mes/materials/multiple/[" + materialIds + "]";
                return httpFactory.get(url);
            }

            function createObject(objectType, dto) {
                var url = "api/mes/materials/" + objectType + "/object";
                return httpFactory.post(url, dto);
            }

            function uploadImage(machineId, file) {
                var url = "api/mes/materials/" + machineId + "/image";
                return httpFactory.upload(url, file);
            }
        }
    }
);