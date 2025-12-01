define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('SubstanceService', SubstanceService);

        function SubstanceService(httpFactory) {
            return {
                createSubstance: createSubstance,
                updateSubstance: updateSubstance,
                getSubstance: getSubstance,
                deleteSubstance: deleteSubstance,
                getAllSubstances: getAllSubstances,
                getMultipleSubstances: getMultipleSubstances
            };

            function createSubstance(material) {
                var url = "api/pgc/substances";
                return httpFactory.post(url, material)
            }

            function updateSubstance(material) {
                var url = "api/pgc/substances/" + material.id;
                return httpFactory.put(url, material);
            }

            function getSubstance(id) {
                var url = "api/pgc/substances/" + id;
                return httpFactory.get(url)
            }

            function deleteSubstance(material) {
                var url = "api/pgc/substances/" + material;
                return httpFactory.delete(url);
            }

            function getAllSubstances(pageable, filters) {
                var url = "api/pgc/substances/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&description={3}&casNumber={4}&searchQuery={5}&declarationPart={6}".
                    format(filters.number, filters.type, filters.name, filters.description, filters.casNumber, filters.searchQuery, filters.declarationPart);
                return httpFactory.get(url);
            }

            function getMultipleSubstances(materialIds) {
                var url = "api/pgc/substances/multiple/[" + materialIds + "]";
                return httpFactory.get(url);
            }
        }
    }
);