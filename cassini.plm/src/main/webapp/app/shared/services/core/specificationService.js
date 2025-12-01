define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('SpecificationService', SpecificationService);

        function SpecificationService(httpFactory) {
            return {
                createSpecification: createSpecification,
                updateSpecification: updateSpecification,
                getSpecification: getSpecification,
                getAllSpecifications: getAllSpecifications,
                deleteSpecification: deleteSpecification,
                getMultipleSpecifications: getMultipleSpecifications,
                getSpecificationSubstances: getSpecificationSubstances,
                createSpecSubstance: createSpecSubstance,
                createSpecSubstances: createSpecSubstances,
                updateSpecSubstance: updateSpecSubstance,
                getAllSpecSubstances: getAllSpecSubstances,
                deleteSpecSubstance: deleteSpecSubstance,
                getSpecificationTabCount: getSpecificationTabCount
            };

            function createSpecification(specification) {
                var url = "api/pgc/specifications";
                return httpFactory.post(url, specification)
            }

            function updateSpecification(specification) {
                var url = "api/pgc/specifications/" + specification.id;
                return httpFactory.put(url, specification);
            }

            function getSpecification(id) {
                var url = "api/pgc/specifications/" + id;
                return httpFactory.get(url)
            }

            function getSpecificationTabCount(id) {
                var url = "api/pgc/specifications/" + id + "/count";
                return httpFactory.get(url)
            }

            function deleteSpecification(specification) {
                var url = "api/pgc/specifications/" + specification;
                return httpFactory.delete(url);
            }

            function getAllSpecifications(pageable, filters) {
                var url = "api/pgc/specifications/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&name={1}&number={2}&type={3}&declaration={4}&item={5}".
                    format(filters.searchQuery, filters.name, filters.number, filters.type, filters.declaration, filters.item);
                return httpFactory.get(url);
            }

            function getMultipleSpecifications(specificationIds) {
                var url = "api/pgc/specifications/multiple/[" + specificationIds + "]";
                return httpFactory.get(url);
            }

            function getSpecificationSubstances(filter, pageable) {
                var url = "api/pgc/specifications/allsubstances?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&name={1}&type={2}&description={3}&specification={4}"
                    .format(filter.number, filter.name, filter.type, filter.description, filter.specification);
                return httpFactory.get(url);
            }

            function createSpecSubstance(specId, substance) {
                var url = "api/pgc/specifications/" + specId + "/substance";
                return httpFactory.post(url, substance);
            }

            function createSpecSubstances(specId, substances) {
                var url = "api/pgc/specifications/" + specId + "/substance/multiple";
                return httpFactory.post(url, substances);
            }

            function updateSpecSubstance(specId, substance) {
                var url = "api/pgc/specifications/" + specId + "/substance/" + substance.id;
                return httpFactory.put(url, substance);
            }

            function getAllSpecSubstances(specId) {
                var url = "api/pgc/specifications/" + specId + "/specsubstances";
                return httpFactory.get(url);
            }

            function deleteSpecSubstance(specId, specSubId) {
                var url = "api/pgc/specifications/" + specId + "/specsubstance/" + specSubId;
                return httpFactory.delete(url);
            }
        }
    }
);