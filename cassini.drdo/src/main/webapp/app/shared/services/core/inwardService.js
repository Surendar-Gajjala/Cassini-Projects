define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('InwardService', InwardService);

        function InwardService(httpFactory) {
            return {
                createInward: createInward,
                updateInward: updateInward,
                deleteInward: deleteInward,
                getInward: getInward,
                getInwards: getInwards,
                getAllInwards: getAllInwards,
                getAllInwardsByPermission: getAllInwardsByPermission,

                getInwardsByBom: getInwardsByBom,
                getInwardReport: getInwardReport,

                createInwardItems: createInwardItems,
                createInwardItem: createInwardItem,
                getInwardItems: getInwardItems,
                deleteInwardItem: deleteInwardItem,
                updateInwardData: updateInwardData,
                updateInwardStatus: updateInwardStatus,
                getExpiredItems: getExpiredItems,

                acceptItem: acceptItem,
                provisionalAcceptItem: provisionalAcceptItem,
                acceptItemInstance: acceptItemInstance,
                provAcceptItemInstance: provAcceptItemInstance,
                returnInwardItemInstance: returnInwardItemInstance,
                reviewInwardItemInstance: reviewInwardItemInstance,
                updateInwardItemInstance: updateInwardItemInstance,
                getReturnInstanceByInstance: getReturnInstanceByInstance,
                updateReturnedInwardItemInstance: updateReturnedInwardItemInstance,
                acceptItemInstanceLater: acceptItemInstanceLater,

                allocateStorageToItem: allocateStorageToItem,
                allocateStorageToInstance: allocateStorageToInstance,
                verifyStoragePart: verifyStoragePart,
                saveMultipleLotAttributes: saveMultipleLotAttributes,
                uploadMultipleLotImageAttribute: uploadMultipleLotImageAttribute,
                uploadMultipleLotAttachment: uploadMultipleLotAttachment,

                saveInstanceAttribute: saveInstanceAttribute,
                saveInstanceImage: saveInstanceImage,
                saveInstanceAttachment: saveInstanceAttachment,

                createItemLot: createItemLot,
                searchGatePass: searchGatePass,
                createGatePass: createGatePass,
                updateGatePass: updateGatePass,
                deleteGatePass: deleteGatePass,

                getAttributesByItemIdAndAttributeId: getAttributesByItemIdAndAttributeId,
                saveSelectedInstanceAttributes: saveSelectedInstanceAttributes,
                getItemStatusHistory: getItemStatusHistory,
                saveAttributeToAllInstances: saveAttributeToAllInstances,
                deleteInstanceAttributeAttachment: deleteInstanceAttributeAttachment,
                saveInstanceAttributes: saveInstanceAttributes,

                getAllReturnItems: getAllReturnItems,
                getAllFailedItems: getAllFailedItems,
                getUpnDetails: getUpnDetails,
                getLotUpnDetails: getLotUpnDetails,
                getIssuedLotUpnDetails: getIssuedLotUpnDetails,
                getAllGatePasses: getAllGatePasses,
                getAllInwardItems: getAllInwardItems,
                checkInwardsWithSupplier: checkInwardsWithSupplier,
                searchItemInstances: searchItemInstances,
                allocateStorageToReturnedItemInstance: allocateStorageToReturnedItemInstance,
                getReturnInstanceByInstanceAndMfr: getReturnInstanceByInstanceAndMfr,
                getGatePassDetails: getGatePassDetails,
                generateNextLotNumber: generateNextLotNumber,
                deleteGeneratedLotNumber: deleteGeneratedLotNumber,
                getToExpireItems: getToExpireItems,
                generateRootCardNumber: generateRootCardNumber,
                getInwardReportBySystem: getInwardReportBySystem,
                updateInwardItemInstanceAfterSubmit: updateInwardItemInstanceAfterSubmit
            };

            function createGatePass(gatePassNumber, gatePassDate, file) {
                var url = "api/drdo/inwards/gatePass?gatePassNumber=" + gatePassNumber + "&gatePassDate=" + gatePassDate;
                return httpFactory.upload(url, file);
            }

            function updateGatePass(gatePass) {
                var url = "api/drdo/inwards/gatePass/" + gatePass.id;
                return httpFactory.put(url, gatePass);
            }

            function deleteGatePass(gatePass) {
                var url = "api/drdo/inwards/gatePass/" + gatePass.id;
                return httpFactory.delete(url);
            }

            function searchGatePass(filter) {
                var url = "api/drdo/inwards/gatePass/search?searchQuery={0}".format(filter.searchQuery);
                return httpFactory.get(url);
            }

            function createInward(inward) {
                var url = "api/drdo/inwards";
                return httpFactory.post(url, inward);
            }

            function updateInward(inward) {
                var url = "api/drdo/inwards/" + inward.id;
                return httpFactory.put(url, inward);
            }

            function updateInwardData(inward) {
                var url = "api/drdo/inwards/" + inward.id + "/update";
                return httpFactory.put(url, inward);
            }

            function deleteInward(inwardId) {
                var url = "api/drdo/inwards/" + inwardId;
                return httpFactory.delete(url);
            }

            function getInward(inwardId) {
                var url = "api/drdo/inwards/" + inwardId;
                return httpFactory.get(url);
            }

            function getInwards() {
                var url = "api/drdo/inwards";
                return httpFactory.get(url);
            }

            function getInwardsByBom(bomId) {
                var url = "api/drdo/inwards/bom/" + bomId;
                return httpFactory.get(url);
            }

            function getInwardReport(inwardId) {
                var url = "api/drdo/inwards/" + inwardId + "/report";
                return httpFactory.get(url);
            }

            function getAllInwards(pageable, filter) {
                var url = "api/drdo/inwards/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&status={1}&notification={2}&adminPermission={3}&storeApprove={4}&ssqagApprove={5}&bdlApprove={6}&casApprove={7}"
                    .format(filter.searchQuery, filter.status, filter.notification, filter.adminPermission, filter.storeApprove, filter.ssqagApprove, filter.bdlApprove, filter.casApprove);
                return httpFactory.get(url);
            }

            function getAllInwardItems(pageable, filter) {
                var url = "api/drdo/inwards/items/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&status={1}&notification={2}&adminPermission={3}&storeApprove={4}&ssqagApprove={5}&bdlApprove={6}&casApprove={7}"
                    .format(filter.searchQuery, filter.status, filter.notification, filter.adminPermission, filter.storeApprove, filter.ssqagApprove, filter.bdlApprove, filter.casApprove);
                return httpFactory.get(url);
            }

            function getAllGatePasses(pageable, filter) {
                var url = "api/drdo/inwards/gatePass/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&gatePassNumber={1}&gatePassName={2}&finish={3}&month={4}"
                    .format(filter.searchQuery, filter.gatePassNumber, filter.gatePassName, filter.finish, filter.month);
                url += "&fromDate={0}&toDate={1}"
                    .format(filter.fromDate, filter.toDate);
                return httpFactory.get(url);
            }

            function getAllInwardsByPermission(pageable, filter) {
                var url = "api/drdo/inwards/allInwards?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&status={1}&notification={2}&adminPermission={3}&storeApprove={4}&ssqagApprove={5}&bdlApprove={6}&casApprove={7}&gatePassPermission={8}&finishedPage={9}&gatePassView={10}&finish={11}&&month={12}"
                    .format(filter.searchQuery, filter.status, filter.notification, filter.adminPermission, filter.storeApprove, filter.ssqagApprove, filter.bdlApprove,
                    filter.casApprove, filter.gatePassPermission, filter.finishedPage, filter.gatePassView, filter.finish, filter.month);
                url += "&fromDate={0}&toDate={1}"
                    .format(filter.fromDate, filter.toDate);
                return httpFactory.get(url);
            }

            function getGatePassDetails(gatePassId) {
                return httpFactory.get("api/drdo/inwards/gatePass/" + gatePassId + "/details");
            }

            function updateInwardStatus(inward) {
                var url = "api/drdo/inwards/" + inward.id + "/update/status";
                return httpFactory.put(url, inward);
            }

            function createInwardItems(inwardId, inwardItems) {
                var url = "api/drdo/inwards/" + inwardId + "/items";
                return httpFactory.post(url, inwardItems);
            }

            function createInwardItem(inwardId, inwardItem) {
                var url = "api/drdo/inwards/" + inwardId + "/item";
                return httpFactory.post(url, inwardItem);
            }

            function deleteInwardItem(inwardId, inwardItem, store) {
                var url = "api/drdo/inwards/" + inwardId + "/item/" + inwardItem.id + "?store=" + store;
                return httpFactory.delete(url);
            }

            function getInwardItems(inward) {
                var url = "api/drdo/inwards/" + inward + "/items";
                return httpFactory.get(url);
            }

            function updateInwardItemInstance(inward, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/itemInstance/" + itemInstance.id;
                return httpFactory.put(url, itemInstance);
            }

            function updateInwardItemInstanceAfterSubmit(inward, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/itemInstance/" + itemInstance.id + "/save";
                return httpFactory.put(url, itemInstance);
            }

            function updateReturnedInwardItemInstance(inward, itemInstance, updateId) {
                var url = "api/drdo/inwards/" + inward + "/itemInstance/" + itemInstance.id + "/update/" + updateId;
                return httpFactory.put(url, itemInstance);
            }

            function acceptItem(inward, inwardItem) {
                var url = "api/drdo/inwards/" + inward + "/acceptItem/" + inwardItem.id;
                return httpFactory.put(url, inwardItem);
            }

            function provisionalAcceptItem(inward, inwardItem, reason) {
                var url = "api/drdo/inwards/" + inward + "/provisionalAcceptItem/" + inwardItem.id + "?provReason=" + reason;
                return httpFactory.put(url, inwardItem);
            }

            function acceptItemInstance(inward, inwardItem, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/" + inwardItem + "/acceptItemInstance";
                return httpFactory.put(url, itemInstance);
            }

            function acceptItemInstanceLater(inward, inwardItem, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/" + inwardItem + "/acceptItemInstance/later";
                return httpFactory.put(url, itemInstance);
            }

            function provAcceptItemInstance(inward, inwardItem, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/" + inwardItem + "/provAcceptItemInstance";
                return httpFactory.put(url, itemInstance);
            }

            function returnInwardItemInstance(inward, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/returnItemInstance";
                return httpFactory.put(url, itemInstance);
            }

            function getReturnInstanceByInstance(upnNumber, oemNumber) {
                var url = "api/drdo/inwards/returnItemInstance?upnNumber=" + upnNumber + "&serialNumber=" + oemNumber;
                return httpFactory.get(url);
            }

            function getReturnInstanceByInstanceAndMfr(instanceId, upnNumber, oemNumber, mfr) {
                var url = "api/drdo/inwards/returnItemInstance/" + instanceId + "?upnNumber=" + upnNumber + "&serialNumber=" + oemNumber + "&mfr=" + mfr;
                return httpFactory.get(url);
            }

            function reviewInwardItemInstance(inward, itemInstance) {
                var url = "api/drdo/inwards/" + inward + "/reviewItemInstance";
                return httpFactory.put(url, itemInstance);
            }

            function createItemLot(inward, inwardItem) {
                var url = "api/drdo/inwards/" + inward + "/item/lot";
                return httpFactory.post(url, inwardItem);
            }

            function allocateStorageToItem(inwardId, inwardItem) {
                var url = "api/drdo/inwards/" + inwardId + "/allocate/all";
                return httpFactory.put(url, inwardItem);
            }

            function allocateStorageToInstance(inwardId, inwardItemInstance) {
                var url = "api/drdo/inwards/" + inwardId + "/allocate";
                return httpFactory.put(url, inwardItemInstance);
            }

            function allocateStorageToReturnedItemInstance(inwardId, inwardItemInstance) {
                var url = "api/drdo/inwards/" + inwardId + "/allocate/returnStorage";
                return httpFactory.put(url, inwardItemInstance);
            }

            function verifyStoragePart(inwardId, inwardInstanceId, storageName, upnNumber) {
                var url = "api/drdo/inwards/" + inwardId + "/item/" + inwardInstanceId + "/verify/" + storageName + "/" + upnNumber;
                return httpFactory.get(url);
            }

            function saveMultipleLotAttributes(objectIds, attributes) {
                var url = "api/drdo/inwards/[" + objectIds + "]/lot/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function uploadMultipleLotImageAttribute(objectIds, attributeId, file) {
                var url = "api/drdo/inwards/multipleLot/uploadImage/[" + objectIds + "]/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function uploadMultipleLotAttachment(objectIds, attributeId, files) {
                var url = "api/drdo/inwards/multipleLot/attachment/[" + objectIds + "]/" + attributeId;
                return httpFactory.uploadMultiple(url, files);
            }

            function getAttributesByItemIdAndAttributeId(itemIds, attributeIds) {
                var url = "api/drdo/inwards/instances/objectAttributes";
                return httpFactory.post(url, [itemIds, attributeIds]);
            }

            function saveInstanceAttachment(instanceId, attributeId, files) {
                var url = "api/drdo/inwards/" + instanceId + "/saveAttachments/" + attributeId;
                return httpFactory.uploadMultiple(url, files);
            }

            function saveInstanceImage(instanceId, attributeId, file) {
                var url = "api/drdo/inwards/" + instanceId + "/saveImage/" + attributeId;
                return httpFactory.upload(url, file);
            }

            function saveInstanceAttribute(instanceId, objectAttribute) {
                var url = "api/drdo/inwards/" + instanceId + "/saveAttribute";
                return httpFactory.post(url, objectAttribute);
            }

            function saveInstanceAttributes(instanceId, objectAttributes) {
                var url = "api/drdo/inwards/" + instanceId + "/saveAttributes/multiple";
                return httpFactory.put(url, objectAttributes);
            }

            function saveSelectedInstanceAttributes(instanceId, selectedInstances) {
                var url = "api/drdo/inwards/" + instanceId + "/details/apply";
                return httpFactory.post(url, selectedInstances);
            }

            function saveAttributeToAllInstances(instanceId, attribute) {
                var url = "api/drdo/inwards/" + instanceId + "/details/update/apply";
                return httpFactory.post(url, attribute);
            }

            function deleteInstanceAttributeAttachment(instanceIds, attributeDef, attachmentId) {
                var url = "api/drdo/inwards/" + attributeDef + "/delete/attachment/" + attachmentId;
                return httpFactory.put(url, instanceIds);
            }

            function getItemStatusHistory(instanceId) {
                var url = "api/drdo/inwards/" + instanceId + "/item/status/history";
                return httpFactory.get(url);
            }

            function getUpnDetails(instanceId) {
                var url = "api/drdo/inwards/" + instanceId + "/item/upnDetails";
                return httpFactory.get(url);
            }

            function getLotUpnDetails(instanceId) {
                var url = "api/drdo/inwards/" + instanceId + "/item/lot/upnDetails";
                return httpFactory.get(url);
            }

            function getIssuedLotUpnDetails(instanceId) {
                var url = "api/drdo/inwards/" + instanceId + "/item/lot/issued/upnDetails";
                return httpFactory.get(url);
            }

            function getAllReturnItems(pageable) {
                var url = "api/drdo/inwards/returnItems/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllFailedItems(pageable) {
                var url = "api/drdo/inwards/failureItems/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function checkInwardsWithSupplier(supplier) {
                var url = "api/drdo/inwards/check/" + supplier;
                return httpFactory.get(url);
            }

            function getExpiredItems(pageable) {
                var url = "api/drdo/inwards/expiredItems/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getToExpireItems(pageable) {
                var url = "api/drdo/inwards/toExpire/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function searchItemInstances(pageable, filter) {
                var url = "api/drdo/inwards/itemInstances/search?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(filter.searchQuery);
                return httpFactory.get(url);
            }

            function generateNextLotNumber(itemInstanceId) {
                var url = "api/drdo/inwards/" + itemInstanceId + "/next/lotNumber";
                return httpFactory.get(url);
            }

            function deleteGeneratedLotNumber(itemInstanceId) {
                var url = "api/drdo/inwards/" + itemInstanceId + "/delete/lotNumber";
                return httpFactory.get(url);
            }

            function generateRootCardNumber(itemInstanceId) {
                var url = "api/drdo/inwards/" + itemInstanceId + "/generate/rootCardNumber";
                return httpFactory.get(url);
            }

            function getInwardReportBySystem(systemId, searchText, pageable) {
                var url = "api/drdo/inwards/bom/" + systemId + "/report?searchText=" + searchText + "&page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }
        }
    }
);