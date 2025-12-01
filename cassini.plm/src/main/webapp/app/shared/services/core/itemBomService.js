define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ItemBomService', ItemBomService);

        function ItemBomService(httpFactory) {
            return {
                getItemBom: getItemBom,
                getBomItem: getBomItem,
                createItemBom: createItemBom,
                createBomItems: createBomItems,
                updateBomItem: updateBomItem,
                updateBomItems: updateBomItems,
                deleteBomItem: deleteBomItem,
                getWhereUsed: getWhereUsed,
                getBom: getBom,
                getBomList: getBomList,
                createBomItem: createBomItem,
                getWhereUsedItems: getWhereUsedItems,
                updateBomItemSeq: updateBomItemSeq,
                pasteClipBoardItemsToBomItem: pasteClipBoardItemsToBomItem,
                undoCopiedItems: undoCopiedItems,
                createBomRules: createBomRules,
                getBomModal: getBomModal,
                getBomInclusionRules: getBomInclusionRules,
                getBomItemToItemInclusions: getBomItemToItemInclusions,
                createBomConfiguration: createBomConfiguration,
                getBomConfigurations: getBomConfigurations,
                getBomConfigItemWithValues: getBomConfigItemWithValues,
                resolveSelectedBomConfig: resolveSelectedBomConfig,
                getItemBomConfigurations: getItemBomConfigurations,
                getBomConfigurationModal: getBomConfigurationModal,
                getConfiguredAttributeValues: getConfiguredAttributeValues,
                getItemBomInstances: getItemBomInstances,
                getBomConfigurationInclusions: getBomConfigurationInclusions,
                resolveBomItemInstance: resolveBomItemInstance,
                getAttributesExclusionRules: getAttributesExclusionRules,
                resolveItemBom: resolveItemBom,
                searchBomItems: searchBomItems,
                getItemBomRollUpReport: getItemBomRollUpReport,
                getBomRollupItemChildren: getBomRollupItemChildren,
                getItemBomWhereUsedReport: getItemBomWhereUsedReport,
                getItemBomWhereUsedReportByIds: getItemBomWhereUsedReportByIds,
                getBomConfigurationAttributeExclusions: getBomConfigurationAttributeExclusions,
                getAttributeValueUsedInConfigurations: getAttributeValueUsedInConfigurations,
                getAvailableItemBomConfigurations: getAvailableItemBomConfigurations,
                createItemInstances: createItemInstances,
                createAllInstance: createAllInstance,
                createAllCombination: createAllCombination,
                createBomConfigItemInclusionRules: createBomConfigItemInclusionRules,
                createBomNonConfigItemInclusionRules: createBomNonConfigItemInclusionRules,
                createBomConfigAttributeExclusionRules: createBomConfigAttributeExclusionRules,
                substituteBomItem: substituteBomItem,
                getBomComplianceReport: getBomComplianceReport,
                getAsReleasedItemBom: getAsReleasedItemBom,
                getTotalBom: getTotalBom,
                createNewItemBom: createNewItemBom,
                createMultipleBomItems: createMultipleBomItems,
                updateItemBom: updateItemBom
            };

            function getItemBom(itemId, hierarchy) {
                var url = 'api/plm/items/' + itemId + "/bom?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getTotalBom(itemId, hierarchy, bomRule) {
                var url = 'api/plm/items/' + itemId + "/bom/all?hierarchy=" + hierarchy + "&bomRule=" + bomRule;
                return httpFactory.get(url);
            }

            function getAsReleasedItemBom(itemId, problemReport) {
                var url = 'api/plm/items/' + itemId + "/bom/released?problemReport=" + problemReport;
                return httpFactory.get(url);
            }

            function searchBomItems(itemId, hierarchy, bomSearchFilters) {
                var url = 'api/plm/items/' + itemId + "/bom/search?hierarchy=" + hierarchy;
                url += "&searchQuery={0}&item={1}&fromDate={2}&toDate={3}".
                    format(bomSearchFilters.searchQuery, bomSearchFilters.item, bomSearchFilters.fromDate, bomSearchFilters.toDate);
                return httpFactory.get(url);
            }

            function getBomItem(itemId, bomItemId) {
                var url = 'api/plm/items/' + itemId + "/bom/" + bomItemId;
                return httpFactory.get(url);
            }

            function getBomList(itemId, bomItemId) {
                var url = "api/plm/items/" + itemId + "/bom/[" + bomItemId + "]";
                return httpFactory.get(url);
            }

            function getBom(itemId, bomItemId) {
                var url = 'api/plm/items/' + itemId + "/bom/item/" + bomItemId;
                return httpFactory.get(url);
            }

            function createBomRules(itemId, bomRules) {
                var url = 'api/plm/items/' + itemId + "/bom/bomRules";
                return httpFactory.post(url, bomRules);
            }

            function createItemBom(itemId, bomItem) {
                var url = 'api/plm/items/' + itemId + "/bom";
                return httpFactory.post(url, bomItem);
            }

            function createNewItemBom(itemId, bomItem) {
                var url = 'api/plm/items/' + itemId + "/bom/new";
                return httpFactory.post(url, bomItem);
            }

            function createBomItem(revisionId, bomItem) {
                var url = 'api/plm/items/' + revisionId + "/bom/bomItem";
                return httpFactory.post(url, bomItem);
            }

            function createBomItems(itemId, bomItems) {
                var url = 'api/plm/items/' + itemId + "/bom/multiple";
                return httpFactory.post(url, bomItems);
            }

            function createMultipleBomItems(itemId, bomItems) {
                var url = 'api/plm/items/' + itemId + "/bom/new/multiple";
                return httpFactory.post(url, bomItems);
            }

            function updateBomItem(itemId, bomItem) {
                bomItem.bomChildren = [];
                var url = 'api/plm/items/' + itemId + "/bom/" + bomItem.id;
                return httpFactory.put(url, bomItem);
            }

            function updateItemBom(itemId, bomItem) {
                bomItem.bomChildren = [];
                var url = 'api/plm/items/' + itemId + "/bom/" + bomItem.id + "/update";
                return httpFactory.put(url, bomItem);
            }

            function updateBomItems(itemId, bomItems) {
                var url = 'api/plm/items/' + itemId + "/bom/multiple";
                return httpFactory.put(url, bomItems);
            }

            function deleteBomItem(itemId, bomItemId) {
                var url = 'api/plm/items/' + itemId + "/bom/" + bomItemId;
                return httpFactory.delete(url);
            }

            function getWhereUsed(itemId, hierarchy) {
                var url = 'api/plm/items/' + itemId + "/whereused?hierarchy=" + hierarchy;
                return httpFactory.get(url);
            }

            function getWhereUsedItems(item) {
                var url = 'api/plm/items/' + item.id + "/whereused/items";
                return httpFactory.get(url);
            }

            function updateBomItemSeq(itemId, actualId, targetId) {
                var url = 'api/plm/items/' + itemId + "/bom/" + actualId + "/change/" + targetId;
                return httpFactory.get(url);
            }

            function pasteClipBoardItemsToBomItem(itemId, bomItems) {
                var url = 'api/plm/items/' + itemId + "/bom/paste";
                return httpFactory.put(url, bomItems);
            }

            function undoCopiedItems(itemId, bomItems) {
                var url = 'api/plm/items/' + itemId + "/bom/undo";
                return httpFactory.put(url, bomItems);
            }

            function getItemBomConfigurations(itemId) {
                var url = 'api/plm/items/' + itemId + "/bom/configurations";
                return httpFactory.get(url);
            }

            function getAvailableItemBomConfigurations(itemId) {
                var url = 'api/plm/items/' + itemId + "/bom/configurations/available";
                return httpFactory.get(url);
            }

            function getBomConfigurationModal(itemId, id) {
                var url = 'api/plm/items/' + itemId + "/bom/configurations/" + id + "/modal";
                return httpFactory.get(url);
            }

            function getBomModal(itemId) {
                var url = 'api/plm/items/' + itemId + "/bom/modal";
                return httpFactory.get(url);
            }

            function getBomInclusionRules(itemId, bomModal) {
                var url = 'api/plm/items/' + itemId + "/bom/inclusion/validate";
                return httpFactory.post(url, bomModal);
            }

            function getAttributesExclusionRules(itemId, bomModal) {
                var url = 'api/plm/items/' + itemId + "/bom/attributes/validate";
                return httpFactory.post(url, bomModal);
            }

            function getBomItemToItemInclusions(itemId, bomModal) {
                var url = 'api/plm/items/' + itemId + "/bom/itemToItemExclusion/validate";
                return httpFactory.post(url, bomModal);
            }

            function createBomConfiguration(itemId, bomConfig) {
                var url = 'api/plm/bomConfig/' + itemId + "/bomConfiguration";
                return httpFactory.post(url, bomConfig);
            }

            function getBomConfigurations(revisionId) {
                var url = 'api/plm/bomConfig/item/' + revisionId;
                return httpFactory.get(url);
            }

            function createItemInstances(itemId, itemInstance) {
                var url = "api/plm/bomConfig/items/" + itemId + "/createItemInstances";
                return httpFactory.post(url, itemInstance)
            }

            function createAllInstance(itemId, itemInstance) {
                var url = "api/plm/bomConfig/items/" + itemId + "/createAllInstance";
                return httpFactory.post(url, itemInstance)
            }

            function createAllCombination(itemId) {
                var url = "api/plm/bomConfig/items/" + itemId + "/getAllCombinations";
                return httpFactory.get(url)
            }

            function getBomConfigItemWithValues(bomConfigId) {
                var url = 'api/plm/bomConfig/resolve/' + bomConfigId;
                return httpFactory.get(url);
            }

            function resolveSelectedBomConfig() {
                var url = 'api/plm/bomConfig/resolve';
                return httpFactory.get(url);
            }

            function getConfiguredAttributeValues(itemId, configId) {
                var url = 'api/plm/items/' + itemId + "/bom/" + configId + "/configured/attribute/values";
                return httpFactory.get(url);
            }

            function getItemBomInstances(itemId) {
                var url = 'api/plm/items/' + itemId + "/bom/configured";
                return httpFactory.get(url);
            }

            function getBomConfigurationInclusions(revisionId) {
                var url = 'api/plm/bomConfig/inclusions/' + revisionId;
                return httpFactory.get(url);
            }

            function getBomConfigurationAttributeExclusions(revisionId) {
                var url = 'api/plm/bomConfig/attributeExclusion/' + revisionId;
                return httpFactory.get(url);
            }

            function resolveBomItemInstance(itemId, bomItem) {
                var url = 'api/plm/items/' + itemId + "/bom/" + bomItem + "/resolve";
                return httpFactory.get(url);
            }

            function resolveItemBom(itemId) {
                var url = 'api/plm/items/' + itemId + "/bom/resolve";
                return httpFactory.get(url);
            }

            function getItemBomRollUpReport(itemId, attributes) {
                var url = 'api/plm/items/' + itemId + "/bom/rollup/report";
                return httpFactory.post(url, attributes);
            }

            function getBomRollupItemChildren(itemId, attributeIds) {
                var url = 'api/plm/items/' + itemId + "/bom/[" + attributeIds + "]/rollup/report/children";
                return httpFactory.get(url);
            }

            function getItemBomWhereUsedReport(itemId) {
                var url = "api/plm/items/" + itemId + "/bom/whereUsedReport";
                return httpFactory.get(url);
            }

            function getBomComplianceReport(itemId) {
                var url = "api/plm/items/" + itemId + "/bom/compliancereport";
                return httpFactory.get(url);
            }

            function getItemBomWhereUsedReportByIds(itemId, ids) {
                var url = "api/plm/items/" + itemId + "/bom/whereUsedReport/search/[" + ids + "]";
                return httpFactory.get(url);
            }

            function getAttributeValueUsedInConfigurations(itemId, attributeId, value) {
                var url = "api/plm/items/" + itemId + "/bom/attribute/" + attributeId + "/value?value=" + value;
                return httpFactory.get(url);
            }

            function createBomConfigItemInclusionRules(itemId, combinations) {
                var url = "api/plm/items/" + itemId + "/bom/config/items/inclusions";
                return httpFactory.post(url, combinations);
            }

            function createBomNonConfigItemInclusionRules(itemId, combinations) {
                var url = "api/plm/items/" + itemId + "/bom/nonconfig/items/inclusions";
                return httpFactory.post(url, combinations);
            }

            function createBomConfigAttributeExclusionRules(itemId, combinations) {
                var url = "api/plm/items/" + itemId + "/bom/attributes/items/exclusions";
                return httpFactory.post(url, combinations);
            }

            function substituteBomItem(revisionId, substituteBomItem) {
                var url = "api/plm/items/" + revisionId + "/bom/substituteBomItem";
                return httpFactory.post(url, substituteBomItem);
            }
        }
    }
);