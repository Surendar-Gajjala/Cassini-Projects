define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('StorageService', StorageService);

        function StorageService(httpFactory) {
            return {
                getStorageTree: getStorageTree,
                createStorage: createStorage,
                updateStorage: updateStorage,
                deleteStorage: deleteStorage,
                getStorage: getStorage,
                getStorageDetails: getStorageDetails,

                getStorageTypes: getStorageTypes,
                getWarehouseTypeByName: getWarehouseTypeByName,
                getStockRoomByName: getStockRoomByName,
                getStockRoomByParentAndName: getStockRoomByParentAndName,
                getAreaByParentAndName: getAreaByParentAndName,
                getRackByParentAndName: getRackByParentAndName,
                getShelfByParentAndName: getShelfByParentAndName,
                getBinByParentAndName: getBinByParentAndName,
                saveStorageItemTypes: saveStorageItemTypes,
                getStorageClassificationTree: getStorageClassificationTree,
                deleteTypeFromStorage: deleteTypeFromStorage,
                getStoreItems: getStoreItems,
                saveStorageParts: saveStorageParts,
                deleteStorageItem: deleteStorageItem
            };


            function getStorageTree() {
                var url = "api/drdo/storage/tree";
                return httpFactory.get(url);
            }

            function createStorage(storage) {
                var url = "api/drdo/storage";
                return httpFactory.post(url, storage);
            }

            function updateStorage(storage) {
                var url = "api/drdo/storage/" + storage.id;
                return httpFactory.put(url, storage);
            }

            function getStorage(storage) {
                var url = "api/drdo/storage/" + storage;
                return httpFactory.get(url);
            }

            function getStorageDetails(storage) {
                var url = "api/drdo/storage/" + storage + "/storageDetails";
                return httpFactory.get(url);
            }

            function deleteStorage(storageId) {
                var url = "api/drdo/storage/" + storageId;
                return httpFactory.delete(url);
            }

            function getWarehouseTypeByName(name) {
                var url = "api/drdo/storage/warehouseByName/" + name;
                return httpFactory.get(url);
            }

            function getStockRoomByName(name) {
                var url = "api/drdo/storage/stockroomByName/" + name;
                return httpFactory.get(url);
            }

            function getStockRoomByParentAndName(parent, name) {
                var url = "api/drdo/storage/stockroomByParentAndName/" + parent + "/" + name;
                return httpFactory.get(url);
            }

            function getAreaByParentAndName(parent, name) {
                var url = "api/drdo/storage/areaByParentAndName/" + parent + "/" + name;
                return httpFactory.get(url);
            }

            function getRackByParentAndName(parent, name) {
                var url = "api/drdo/storage/rackByParentAndName/" + parent + "/" + name;
                return httpFactory.get(url);
            }

            function getShelfByParentAndName(parent, name) {
                var url = "api/drdo/storage/shelfByParentAndName/" + parent + "/" + name;
                return httpFactory.get(url);
            }

            function getBinByParentAndName(parent, name) {
                var url = "api/drdo/storage/binByParentAndName/" + parent + "/" + name;
                return httpFactory.get(url);
            }

            function saveStorageItemTypes(storageId, itemTypes) {
                var url = "api/drdo/storage/saveStorageItemTypes/" + storageId;
                return httpFactory.post(url, itemTypes);
            }

            function getStorageClassificationTree(storageId) {
                var url = "api/drdo/storage/classificationTree/" + storageId;
                return httpFactory.get(url);
            }

            function deleteTypeFromStorage(storageId, itemTypeId) {
                var url = "api/drdo/storage/" + storageId + "/deleteType/" + itemTypeId;
                return httpFactory.delete(url);
            }

            function getStorageTypes(storageId) {
                var url = "api/drdo/storage/" + storageId + "/storageTypes";
                return httpFactory.get(url);
            }

            function getStoreItems(storageId) {
                var url = "api/drdo/storage/" + storageId + "/items";
                return httpFactory.get(url);
            }

            function saveStorageParts(storageId, storageParts) {
                var url = "api/drdo/storage/" + storageId + "/storageParts";
                return httpFactory.post(url, storageParts);
            }

            function deleteStorageItem(storageItemId) {
                var url = "api/drdo/storage/storageItem/" + storageItemId;
                return httpFactory.delete(url);
            }
        }
    }
);