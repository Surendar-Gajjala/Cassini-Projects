define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('QcrService', QcrService);

        function QcrService(httpFactory) {
            return {
                createQcr: createQcr,
                updateQcr: updateQcr,
                deleteQcr: deleteQcr,
                getQcr: getQcr,
                getAllQcrs: getAllQcrs,
                getMultipleQcrs: getMultipleQcrs,
                getQcrFiles: getQcrFiles,
                createQcrAttribute: createQcrAttribute,
                updateQcrAttribute: updateQcrAttribute,
                createPrProblemSources: createPrProblemSources,
                createNcrProblemSources: createNcrProblemSources,
                getNcrProblemSources: getNcrProblemSources,
                getPrProblemSources: getPrProblemSources,
                createQcrProblemItems: createQcrProblemItems,
                createQcrProblemItem: createQcrProblemItem,
                createQcrProblemMaterials: createQcrProblemMaterials,
                createQcrProblemMaterial: createQcrProblemMaterial,
                getQcrProblemItems: getQcrProblemItems,
                getQcrProblemMaterials: getQcrProblemMaterials,
                deletePrProblemSource: deletePrProblemSource,
                deleteNcrProblemSource: deleteNcrProblemSource,
                deleteQcrProblemItem: deleteQcrProblemItem,
                deleteQcrProblemMaterial: deleteQcrProblemMaterial,
                getQcrRelatedItems: getQcrRelatedItems,
                getQcrRelatedMaterials: getQcrRelatedMaterials,
                deleteQcrRelatedItem: deleteQcrRelatedItem,
                deleteQcrRelatedMaterial: deleteQcrRelatedMaterial,
                createQcrRelatedItems: createQcrRelatedItems,
                createQcrRelatedMaterials: createQcrRelatedMaterials,
                getQcrDetailsCount: getQcrDetailsCount,
                getReleasedByQcrFor: getReleasedByQcrFor,
                getAllQCRCaPa: getAllQCRCaPa,
                createQCRCaPa: createQCRCaPa,
                updateQCRCaPa: updateQCRCaPa,
                deleteQCRCaPa: deleteQCRCaPa,
                updateQCRCaPaAudit: updateQCRCaPaAudit,
                updateQcrProblemItem: updateQcrProblemItem,
                updateQcrProblemMaterial: updateQcrProblemMaterial
            };

            function createQcr(plan) {
                var url = "api/pqm/qcrs";
                return httpFactory.post(url, plan);
            }

            function updateQcr(id, plan) {
                var url = "api/pqm/qcrs/" + id;
                return httpFactory.put(url, plan);
            }

            function deleteQcr(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId;
                return httpFactory.delete(url);
            }

            function getQcr(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId;
                return httpFactory.get(url);
            }

            function getReleasedByQcrFor(qcrFor) {
                var url = "api/pqm/qcrs/released/" + qcrFor;
                return httpFactory.get(url);
            }

            function getAllQcrs(pageable, filters) {
                var url = "api/pqm/qcrs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&qcrNumber={0}&qcrType={1}&title={2}&searchQuery={3}".
                    format(filters.qcrNumber, filters.qcrType, filters.title, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleQcrs(planIds) {
                var url = "api/pqm/qcrs/multiple/[" + planIds + "]";
                return httpFactory.get(url);
            }

            function getQcrFiles(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/files";
                return httpFactory.get(url);
            }

            function createQcrAttribute(ncrId, attribute) {
                var url = "api/pqm/qcrs/" + ncrId + "/attributes";
                return httpFactory.post(url, attribute);
            }

            function updateQcrAttribute(ncrId, attribute) {
                var url = "api/pqm/qcrs/" + ncrId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function createPrProblemSources(qcrId, sources) {
                var url = "api/pqm/qcrs/" + qcrId + "/sources/pr";
                return httpFactory.post(url, sources);
            }

            function deletePrProblemSource(qcrId, sourceId) {
                var url = "api/pqm/qcrs/" + qcrId + "/sources/pr/" + sourceId;
                return httpFactory.delete(url);
            }

            function createNcrProblemSources(qcrId, sources) {
                var url = "api/pqm/qcrs/" + qcrId + "/sources/ncr";
                return httpFactory.post(url, sources);
            }

            function deleteNcrProblemSource(qcrId, sourceId) {
                var url = "api/pqm/qcrs/" + qcrId + "/sources/ncr/" + sourceId;
                return httpFactory.delete(url);
            }

            function getNcrProblemSources(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/sources/ncr";
                return httpFactory.get(url);
            }

            function getPrProblemSources(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/sources/pr";
                return httpFactory.get(url);
            }


            function createQcrProblemItems(qcrId, items) {
                var url = "api/pqm/qcrs/" + qcrId + "/items/multiple";
                return httpFactory.post(url, items);
            }

            function createQcrProblemItem(qcrId, item) {
                var url = "api/pqm/qcrs/" + qcrId + "/items";
                return httpFactory.post(url, item);
            }

            function updateQcrProblemItem(qcrId, item) {
                var url = "api/pqm/qcrs/" + qcrId + "/items/" + item.id;
                return httpFactory.put(url, item);
            }

            function deleteQcrProblemItem(qcrId, itemId) {
                var url = "api/pqm/qcrs/" + qcrId + "/items/" + itemId;
                return httpFactory.delete(url);
            }

            function createQcrProblemMaterials(qcrId, materials) {
                var url = "api/pqm/qcrs/" + qcrId + "/materials/multiple";
                return httpFactory.post(url, materials);
            }

            function createQcrProblemMaterial(qcrId, material) {
                var url = "api/pqm/qcrs/" + qcrId + "/materials";
                return httpFactory.post(url, material);
            }

            function updateQcrProblemMaterial(qcrId, material) {
                var url = "api/pqm/qcrs/" + qcrId + "/materials/" + material.id;
                return httpFactory.put(url, material);
            }

            function deleteQcrProblemMaterial(qcrId, materialId) {
                var url = "api/pqm/qcrs/" + qcrId + "/materials/" + materialId;
                return httpFactory.delete(url);
            }

            function getQcrProblemItems(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/items";
                return httpFactory.get(url);
            }

            function getQcrProblemMaterials(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/materials";
                return httpFactory.get(url);
            }

            function getQcrRelatedItems(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/relatedItems";
                return httpFactory.get(url);
            }

            function getQcrRelatedMaterials(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/relatedMaterials";
                return httpFactory.get(url);
            }

            function deleteQcrRelatedItem(qcrId, itemId) {
                var url = "api/pqm/qcrs/" + qcrId + "/relatedItems/" + itemId;
                return httpFactory.delete(url);
            }

            function deleteQcrRelatedMaterial(qcrId, materialId) {
                var url = "api/pqm/qcrs/" + qcrId + "/relatedMaterials/" + materialId;
                return httpFactory.delete(url);
            }

            function createQcrRelatedItems(qcrId, materials) {
                var url = "api/pqm/qcrs/" + qcrId + "/relatedItems";
                return httpFactory.post(url, materials);
            }

            function createQcrRelatedMaterials(qcrId, materials) {
                var url = "api/pqm/qcrs/" + qcrId + "/relatedMaterials";
                return httpFactory.post(url, materials);
            }

            function getQcrDetailsCount(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/details/count";
                return httpFactory.get(url);
            }

            function getAllQCRCaPa(qcrId) {
                var url = "api/pqm/qcrs/" + qcrId + "/capa";
                return httpFactory.get(url);
            }

            function createQCRCaPa(qcrId, capa) {
                var url = "api/pqm/qcrs/" + qcrId + "/capa";
                return httpFactory.post(url, capa);
            }

            function updateQCRCaPa(qcrId, capa) {
                var url = "api/pqm/qcrs/" + qcrId + "/capa/" + capa.id;
                return httpFactory.put(url, capa);
            }

            function updateQCRCaPaAudit(qcrId, capa) {
                var url = "api/pqm/qcrs/" + qcrId + "/capa/" + capa.id + "/audit";
                return httpFactory.put(url, capa);
            }

            function deleteQCRCaPa(qcrId, capaId) {
                var url = "api/pqm/qcrs/" + qcrId + "/capa/" + capaId;
                return httpFactory.delete(url);
            }
        }
    }
);