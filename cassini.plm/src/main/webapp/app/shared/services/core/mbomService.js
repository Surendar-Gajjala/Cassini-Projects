define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MBOMService', MBOMService);

        function MBOMService(httpFactory) {
            return {
                createMBOM: createMBOM,
                updateMBOM: updateMBOM,
                getMBOM: getMBOM,
                deleteMBOM: deleteMBOM,
                getMBOMs: getMBOMs,
                getAllMBOMs: getAllMBOMs,
                getMultipleMBOMs: getMultipleMBOMs,
                uploadImageAttribute: uploadImageAttribute,
                saveMBOMAttributes: saveMBOMAttributes,
                getMESTypeWorkflows: getMESTypeWorkflows,
                getMBOMRevision: getMBOMRevision,
                createMBOMItem: createMBOMItem,
                updateMBOMItem: updateMBOMItem,
                getMBOMItem: getMBOMItem,
                deleteMBOMItem: deleteMBOMItem,
                getMBOMItems: getMBOMItems,
                getMBOMChanges: getMBOMChanges,
                getMBOMItemChildren: getMBOMItemChildren,
                getReleasedBom: getReleasedBom,
                getValidateReleasedBom: getValidateReleasedBom,
                attachMBOMWorkflow: attachMBOMWorkflow,
                getFilteredMboms: getFilteredMboms,
                getMBOMRevisionHistory: getMBOMRevisionHistory,
                getReleasedMBOMs: getReleasedMBOMs,
                getMBOMReleasedBOPs: getMBOMReleasedBOPs,
                createMultipleMBOMItems: createMultipleMBOMItems,
                getMbomTabCounts: getMbomTabCounts,
                getMBOMWhereUsed: getMBOMWhereUsed
            };

            function createMBOM(mbom) {
                var url = "api/mes/mboms";
                return httpFactory.post(url, mbom)
            }

            function updateMBOM(mbom) {
                var url = "api/mes/mboms/" + mbom.id;
                return httpFactory.put(url, mbom);
            }

            function getMBOM(id) {
                var url = "api/mes/mboms/" + id;
                return httpFactory.get(url)
            }

            function getMBOMRevision(id) {
                var url = "api/mes/mboms/revisions/" + id;
                return httpFactory.get(url)
            }

            function deleteMBOM(mbom) {
                var url = "api/mes/mboms/" + mbom;
                return httpFactory.delete(url);
            }


            function getMBOMs() {
                var url = "api/mes/mboms";
                return httpFactory.get(url);
            }

            function getMBOMRevisionHistory(id) {
                var url = "api/mes/mboms/" + id + "/revisions/history";
                return httpFactory.get(url);
            }

            function getAllMBOMs(pageable, filters) {
                var url = "api/mes/mboms/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&number={1}&name={2}&type={3}&mco={4}".
                    format(filters.searchQuery, filters.number, filters.name, filters.type, filters.mco);
                return httpFactory.get(url);
            }

            function getFilteredMboms(pageable, mbomsFilters) {
                var url = "api/mes/mboms/all?page={0}&size={1}&sort={2}:{3}".format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&mbomNumber={0}&mbomName={1}".format(mbomsFilters.mbomNumber, mbomsFilters.mbomName);
                return httpFactory.get(url);
            }

            function getMultipleMBOMs(mbomIds) {
                var url = "api/mes/mboms/multiple/[" + mbomIds + "]";
                return httpFactory.get(url);
            }

            function createMBOMItem(id, item) {
                var url = "api/mes/mboms/" + id + "/items";
                return httpFactory.post(url, item)
            }

            function createMultipleMBOMItems(id, item) {
                var url = "api/mes/mboms/" + id + "/items/multiple";
                return httpFactory.post(url, item)
            }

            function updateMBOMItem(id, item) {
                var url = "api/mes/mboms/" + id + "/items/" + item.id;
                return httpFactory.put(url, item);
            }

            function getMBOMItem(id, itemId) {
                var url = "api/mes/mboms/" + id + "/items/" + itemId;
                return httpFactory.get(url)
            }

            function deleteMBOMItem(id, itemId) {
                var url = "api/mes/mboms/" + id + "/items/" + itemId;
                return httpFactory.delete(url);
            }

            function getMBOMItems(id, hierarchy, bop) {
                var url = "api/mes/mboms/" + id + "/items?hierarchy=" + hierarchy + "&bop=" + bop;
                return httpFactory.get(url);
            }

            function getReleasedMBOMs() {
                var url = "api/mes/mboms/released";
                return httpFactory.get(url);
            }

            function getMBOMReleasedBOPs(mbomRevision) {
                var url = "api/mes/mboms/revisions/" + mbomRevision + "/bops/released";
                return httpFactory.get(url);
            }

            function getMBOMChanges(id) {
                var url = "api/mes/mboms/" + id + "/changes";
                return httpFactory.get(url);
            }

            function getMBOMWhereUsed(id) {
                var url = "api/mes/mboms/" + id + "/whereused";
                return httpFactory.get(url);
            }

            function getMbomTabCounts(id) {
                var url = "api/mes/mboms/" + id + "/counts";
                return httpFactory.get(url);
            }

            function getMBOMItemChildren(id, itemId) {
                var url = "api/mes/mboms/" + id + "/items/" + itemId + "/children";
                return httpFactory.get(url);
            }

            function getReleasedBom(id, itemId, hierarchy) {
                var url = "api/mes/mboms/" + id + "/item/" + itemId + "/bom/released?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getValidateReleasedBom(id, itemId, hierarchy) {
                var url = "api/mes/mboms/" + id + "/item/" + itemId + "/bom/released/validate?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mes/mboms/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function saveMBOMAttributes(attributes) {
                var url = "api/mes/mboms/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAllMBOMsByPage(pageable) {
                var url = "api/mes/mboms/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getMESTypeWorkflows(typeId, type) {
                var url = "api/mes/mboms/" + typeId + "/" + type + "/workflows";
                return httpFactory.get(url);
            }

            function attachMBOMWorkflow(id, wfId) {
                var url = "api/mes/mboms/revisions/" + id + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }
        }
    }
);