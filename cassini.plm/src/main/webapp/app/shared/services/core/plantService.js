define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('PlantService', PlantService);

        function PlantService(httpFactory) {
            return {
                createPlant: createPlant,
                updatePlant: updatePlant,
                getPlant: getPlant,
                deletePlant: deletePlant,
                getPlants: getPlants,
                getAllPlants: getAllPlants,
                getMultiplePlants: getMultiplePlants,
                uploadImageAttribute: uploadImageAttribute,
                savePlantAttributes: savePlantAttributes
            };

            function createPlant(plant) {
                var url = "api/mes/plants";
                return httpFactory.post(url, plant)
            }


            function updatePlant(plant) {
                var url = "api/mes/plants/" + plant.id;
                return httpFactory.put(url, plant);
            }

            function getPlant(id) {
                var url = "api/mes/plants/" + id;
                return httpFactory.get(url)
            }

            function deletePlant(plant) {
                var url = "api/mes/plants/" + plant;
                return httpFactory.delete(url);
            }


            function getPlants() {
                var url = "api/mes/plants";
                return httpFactory.get(url);
            }

            function getAllPlants(pageable, filters) {
                var url = "api/mes/plants/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultiplePlants(plantIds) {
                var url = "api/mes/plants/multiple/[" + plantIds + "]";
                return httpFactory.get(url);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mes/plants/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }


            function savePlantAttributes(attributes) {
                var url = "api/mes/plants/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAllPlantsByPage(pageable) {
                var url = "api/mes/plants/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


        }
    }
);