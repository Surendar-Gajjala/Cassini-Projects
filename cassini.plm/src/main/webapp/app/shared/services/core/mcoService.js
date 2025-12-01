define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('MCOService', MCOService);

        function MCOService(httpFactory) {
            return {

                createMCO: createMCO,
                getMCO: getMCO,
                updateMCO: updateMCO,
                deleteMCO: deleteMCO,
                getAllMCOs: getAllMCOs,
                getMcos: getMcos,
                attachMcoWorkflow: attachMcoWorkflow,
                deleteMcoWorkflow: deleteMcoWorkflow,

                createMcoItem: createMcoItem,
                updateMcoItem: updateMcoItem,
                createMcoItems: createMcoItems,
                createMcoMbom: createMcoMbom,
                updateMcoMbom: updateMcoMbom,
                createMcoMboms: createMcoMboms,
                deleteMcoProductAffectedItem: deleteMcoProductAffectedItem,
                getProductAffectedItems: getProductAffectedItems,
                getAffectedItems: getAffectedItems,
                getFilteredItems: getFilteredItems,
                createMcoRelatedItems: createMcoRelatedItems,
                getMcoRelatedItems: getMcoRelatedItems,
                deleteMcoAffectedItem: deleteMcoAffectedItem,
                deleteMcoRelatedItem: deleteMcoRelatedItem,
                createMcoRelatedItem: createMcoRelatedItem,
                getMcoDetailsCount: getMcoDetailsCount,
                getAmlParts: getAmlParts,
                updateMcoRelatedItem: updateMcoRelatedItem,
                getAllManufacturerMCOs: getAllManufacturerMCOs,
                getAllItemMCOs: getAllItemMCOs,
                getItemMco: getItemMco,
                updateItemMco: updateItemMco,
                getMaterialMco: getMaterialMco,
                updateMaterialMco: updateMaterialMco,
                getChangeAnalysts: getChangeAnalysts,
                getStatus: getStatus
            };

            function createMCO(mcr) {
                var url = "api/cms/mcos";
                return httpFactory.post(url, mcr)
            }

            function getMCO(id) {
                var url = "api/cms/mcos/" + id;
                return httpFactory.get(url)
            }

            function updateMCO(mcr) {
                var url = "api/cms/mcos/" + mcr.id;
                return httpFactory.put(url, mcr);
            }

            function deleteMCO(mcr) {
                var url = "api/cms/mcos/" + mcr;
                return httpFactory.delete(url);
            }

            function attachMcoWorkflow(dcrId, wfId) {
                var url = "api/cms/mcos/" + dcrId + "/attachWorkflow/" + wfId;
                return httpFactory.get(url);
            }

            function deleteMcoWorkflow(dcrId) {
                var url = 'api/cms/mcos/' + dcrId + "/workflow/delete";
                return httpFactory.delete(url);
            }


            function getAllMCOs(pageable, filters) {
                var url = "api/cms/mcos/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&mcoNumber={0}&mcoType={1}&title={2}&searchQuery={3}".
                    format(filters.mcoNumber, filters.mcoType, filters.title, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMcos(pageable,filters) {

                var url = "api/cms/mcos?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                
                url += "&searchQuery={0}&type={1}".
                    format(filters.searchQuery,filters.type);
                return httpFactory.get(url);
            }


            function getChangeAnalysts(type) {
                var url = "api/cms/mcos/changeAnalysts/" + type;
                return httpFactory.get(url);
            }

            function getStatus(type) {
                var url = "api/cms/mcos/status/" + type ;
                return httpFactory.get(url);
            }

            function getAllItemMCOs(pageable, filters) {
                var url = "api/cms/mcos/item/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&mcoNumber={0}&mcoType={1}&title={2}&searchQuery={3}&changeAnalyst={4}&status={5}".
                    format(filters.mcoNumber, filters.mcoType, filters.title, filters.searchQuery, filters.changeAnalyst, filters.status);
                return httpFactory.get(url);
            }

            function getAllManufacturerMCOs(pageable, filters) {
                var url = "api/cms/mcos/manufacturer/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&mcoNumber={0}&mcoType={1}&title={2}&searchQuery={3}&changeAnalyst={4}&status={5}".
                    format(filters.mcoNumber, filters.mcoType, filters.title, filters.searchQuery, filters.changeAnalyst, filters.status);
                return httpFactory.get(url);
            }

            function createMcoItem(dcrId, dcrItem) {
                var url = "api/cms/mcos/" + dcrId + "/affectedItem";
                return httpFactory.post(url, dcrItem)
            }

            function createMcoItems(dcrId, dcrItem) {
                var url = "api/cms/mcos/" + dcrId + "/affectedItem/multiple";
                return httpFactory.post(url, dcrItem)
            }

            function createMcoMbom(mcoId, mcoMbom) {
                var url = "api/cms/mcos/" + mcoId + "/productAffectedItem";
                return httpFactory.post(url, mcoMbom)
            }
            
            function createMcoMboms(mcoId, mcoMbom) {
                var url = "api/cms/mcos/" + mcoId + "/productAffectedItem/multiple";
                return httpFactory.post(url, mcoMbom)
            }

            function updateMcoMbom(mcoId, mcoMbom) {
                var url = "api/cms/mcos/" + mcoId + "/productAffectedItem/" + mcoMbom.id;
                return httpFactory.put(url, mcoMbom)
            }

            function deleteMcoProductAffectedItem(mco, id) {
                var url = "api/cms/mcos/" + mco + "/productAffectedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function getProductAffectedItems(mco) {
                var url = "api/cms/mcos/productAffectedItems/" + mco;
                return httpFactory.get(url);
            }

            function updateMcoItem(dcrId, dcrItem) {
                var url = "api/cms/mcos/" + dcrId + "/affectedItem/" + dcrItem.id;
                return httpFactory.put(url, dcrItem)
            }

            function getAffectedItems(mcr) {
                var url = "api/cms/mcos/affectedItems/" + mcr;
                return httpFactory.get(url);
            }

            function getFilteredItems(pageable, filters) {
                var url = "api/cms/mcos/filteredItems?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&itemNumber={0}&itemName={1}&itemType={2}&mcr={3}&dco={4}".
                    format(filters.itemNumber, filters.itemName, filters.itemType, filters.mcr, filters.dco);
                return httpFactory.get(url);
            }

            function createMcoRelatedItems(mcr, items) {
                var url = "api/cms/mcos/" + mcr + "/relatedItems/multiple";
                return httpFactory.post(url, items);
            }

            function createMcoRelatedItem(mcr, item) {
                var url = "api/cms/mcos/" + mcr + "/relatedItems";
                return httpFactory.post(url, item);
            }

            function updateMcoRelatedItem(mcr, item) {
                var url = "api/cms/mcos/" + mcr + "/relatedItems/" + item.id;
                return httpFactory.put(url, item);
            }

            function getMcoRelatedItems(mcr) {
                var url = "api/cms/mcos/relatedItems/" + mcr;
                return httpFactory.get(url);
            }

            function deleteMcoAffectedItem(mcr, id) {
                var url = "api/cms/mcos/" + mcr + "/affectedItem/delete/" + id;
                return httpFactory.delete(url)
            }

            function deleteMcoRelatedItem(mcr, id) {
                var url = "api/cms/mcos/" + mcr + "/relatedItem/" + id;
                return httpFactory.delete(url)
            }

            function getMcoDetailsCount(id) {
                var url = "api/cms/mcos/" + id + "/details/count";
                return httpFactory.get(url)
            }

            function getAmlParts(id) {
                var url = "api/cms/mcos/" + id + "/aml/parts";
                return httpFactory.get(url)
            }

            function getItemMco(mcoId) {
                var url = "api/cms/mcos/items/" + mcoId;
                return httpFactory.get(url);
            }

            function updateItemMco(id, mco) {
                var url = "api/cms/mcos/items/" + id;
                return httpFactory.put(url, mco);
            }

            function getMaterialMco(mcoId) {
                var url = "api/cms/mcos/materials/" + mcoId;
                return httpFactory.get(url);
            }

            function updateMaterialMco(id, mco) {
                var url = "api/cms/mcos/materials/" + id;
                return httpFactory.put(url, mco);
            }
        }
    }
);