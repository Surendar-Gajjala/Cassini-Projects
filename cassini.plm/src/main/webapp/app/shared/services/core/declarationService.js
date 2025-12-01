define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('DeclarationService', DeclarationService);

        function DeclarationService(httpFactory) {
            return {
                createDeclaration: createDeclaration,
                updateDeclaration: updateDeclaration,
                getDeclaration: getDeclaration,
                getAllDeclarations: getAllDeclarations,
                deleteDeclaration: deleteDeclaration,
                getMultipleDeclarations: getMultipleDeclarations,
                uploadImage: uploadImage,
                saveDeclarationAttributes: saveDeclarationAttributes,
                createDeclarationPart: createDeclarationPart,
                createDeclarationParts: createDeclarationParts,
                updateDeclarationPart: updateDeclarationPart,
                getAllDeclarationParts: getAllDeclarationParts,
                deleteDeclarationPart: deleteDeclarationPart,
                submitDeclaration: submitDeclaration,
                createPartSubstance: createPartSubstance,
                updatePartSubstance: updatePartSubstance,
                deletePartSubstance: deletePartSubstance,

                getDeclarationSpecifications: getDeclarationSpecifications,
                createDeclarationSpecification: createDeclarationSpecification,
                createMultipleDeclarationSpecification: createMultipleDeclarationSpecification,
                updateDeclarationSpecification: updateDeclarationSpecification,
                deleteDeclarationSpecification: deleteDeclarationSpecification,
                getDeclarationTabCount: getDeclarationTabCount,
                generateComplianceReport: generateComplianceReport
            };

            function createDeclaration(declaration) {
                var url = "api/pgc/declarations";
                return httpFactory.post(url, declaration)
            }

            function updateDeclaration(declaration) {
                var url = "api/pgc/declarations/" + declaration.id;
                return httpFactory.put(url, declaration);
            }

            function getDeclaration(id) {
                var url = "api/pgc/declarations/" + id;
                return httpFactory.get(url)
            }

            function getDeclarationTabCount(id) {
                var url = "api/pgc/declarations/" + id + "/count";
                return httpFactory.get(url)
            }

            function deleteDeclaration(declaration) {
                var url = "api/pgc/declarations/" + declaration;
                return httpFactory.delete(url);
            }

            function getAllDeclarations(pageable, filters) {
                var url = "api/pgc/declarations/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleDeclarations(declarationIds) {
                var url = "api/pgc/declaration/multiple/[" + declarationIds + "]";
                return httpFactory.get(url);
            }

            function uploadImage(instrumentId, file) {
                var url = "api/pgc/declarations/" + instrumentId + "/image";
                return httpFactory.upload(url, file);
            }

            function saveDeclarationAttributes(id, attributes) {
                var url = "api/pgc/declarations/" + id + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function createDeclarationPart(declarationId, part) {
                var url = "api/pgc/declarations/" + declarationId + "/parts";
                return httpFactory.post(url, part);
            }

            function createDeclarationParts(declarationId, parts) {
                var url = "api/pgc/declarations/" + declarationId + "/parts/multiple";
                return httpFactory.post(url, parts);
            }

            function updateDeclarationPart(declarationId, part) {
                var url = "api/pgc/declarations/" + declarationId + "/parts/" + part.id;
                return httpFactory.put(url, part);
            }

            function getAllDeclarationParts(declarationId) {
                var url = "api/pgc/declarations/" + declarationId + "/parts";
                return httpFactory.get(url);
            }

            function deleteDeclarationPart(declarationId, declPartId) {
                var url = "api/pgc/declarations/" + declarationId + "/parts/" + declPartId;
                return httpFactory.delete(url);
            }

            function createPartSubstance(partId, substance) {
                var url = "api/pgc/declarations/parts/" + partId + "/substances";
                return httpFactory.post(url, substance);
            }

            function updatePartSubstance(partId, substance) {
                var url = "api/pgc/declarations/parts/" + partId + "/substances/" + substance.id;
                return httpFactory.put(url, substance);
            }

            function deletePartSubstance(partId, substanceId) {
                var url = "api/pgc/declarations/parts/" + partId + "/substances/" + substanceId;
                return httpFactory.delete(url);
            }

            function getDeclarationSpecifications(declarationId) {
                var url = "api/pgc/declarations/" + declarationId + "/specifications";
                return httpFactory.get(url);
            }

            function createDeclarationSpecification(declarationId, substance) {
                var url = "api/pgc/declarations/" + declarationId + "/specifications";
                return httpFactory.post(url, substance);
            }

            function createMultipleDeclarationSpecification(declarationId, substance) {
                var url = "api/pgc/declarations/" + declarationId + "/specifications/multiple";
                return httpFactory.post(url, substance);
            }

            function updateDeclarationSpecification(declarationId, substance) {
                var url = "api/pgc/declarations/" + declarationId + "/specifications" + substance.id;
                return httpFactory.put(url, substance);
            }

            function deleteDeclarationSpecification(declarationId, substanceId) {
                var url = "api/pgc/declarations/" + declarationId + "/specifications/" + substanceId;
                return httpFactory.delete(url);
            }

            function submitDeclaration(declarationId) {
                var url = "api/pgc/declarations/" + declarationId + "/submit";
                return httpFactory.get(url);
            }

            function generateComplianceReport(declarationId) {
                var url = "api/pgc/declarations/" + declarationId + "/compliancereport";
                return httpFactory.get(url);
            }

        }
    }
);