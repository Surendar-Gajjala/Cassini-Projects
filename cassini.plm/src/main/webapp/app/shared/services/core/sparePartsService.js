define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('SparePartService', SparePartService);

        function SparePartService(httpFactory) {
            return {
                createSparePart: createSparePart,
                updateSparePart: updateSparePart,
                getSparePart: getSparePart,
                deleteSparePart: deleteSparePart,
                getSpareParts: getSpareParts,
                getAllSpareParts: getAllSpareParts,
                getMultipleSpareParts: getMultipleSpareParts,
                uploadImageAttribute: uploadImageAttribute,
                saveSparePartAttributes: saveSparePartAttributes,
                getObjectAttributesWithHierarchy: getObjectAttributesWithHierarchy
            };

            function createSparePart(sparepart) {
                var url = "api/mro/spareparts";
                return httpFactory.post(url, sparepart)
            }


            function updateSparePart(sparepart) {
                var url = "api/mro/spareparts/" + sparepart.id;
                return httpFactory.put(url, sparepart);
            }

            function getSparePart(id) {
                var url = "api/mro/spareparts/" + id;
                return httpFactory.get(url)
            }

            function deleteSparePart(sparepart) {
                var url = "api/mro/spareparts/" + sparepart;
                return httpFactory.delete(url);
            }


            function getSpareParts() {
                var url = "api/mro/spareparts";
                return httpFactory.get(url);
            }

            function getAllSpareParts(pageable, filters) {
                var url = "api/mro/spareparts/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&number={1}&name={2}&partType={3}&workOrder={4}&asset={5}".
                    format(filters.searchQuery, filters.number, filters.name, filters.partType, filters.workOrder, filters.asset);
                return httpFactory.get(url);
            }

            function getMultipleSpareParts(sparepartIds) {
                var url = "api/mro/spareparts/multiple/[" + sparepartIds + "]";
                return httpFactory.get(url);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mro/spareparts/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }


            function saveSparePartAttributes(attributes) {
                var url = "api/mro/spareparts/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAllSparePartsByPage(pageable) {
                var url = "api/mro/spareparts/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getObjectAttributesWithHierarchy(typeId) {
                var url = "api/mro/objecttypes/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }


        }
    }
);