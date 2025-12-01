define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('AssetService', AssetService);

        function AssetService(httpFactory) {
            return {
                createAsset: createAsset,
                updateAsset: updateAsset,
                getAsset: getAsset,
                getAssets: getAssets,
                getAllAssets: getAllAssets,
                deleteAsset: deleteAsset,
                getMultipleAssets: getMultipleAssets,
                getResourcesByType: getResourcesByType,
                saveAssetAttributes: saveAssetAttributes,
                getAssetsByType: getAssetsByType,
                getAssetSpareParts: getAssetSpareParts,
                createMultipleAssetSpareParts: createMultipleAssetSpareParts,
                deleteAssetSparePart: deleteAssetSparePart,
                getAssetTabCounts: getAssetTabCounts,
                getAssetWorkOrders: getAssetWorkOrders,
                deleteAssetMeter: deleteAssetMeter,
                getAssetsByResource: getAssetsByResource
            };

            function createAsset(asset) {
                var url = "api/mro/assets";
                return httpFactory.post(url, asset)
            }

            function updateAsset(asset) {
                var url = "api/mro/assets/" + asset.id;
                return httpFactory.put(url, asset);
            }

            function getAsset(id) {
                var url = "api/mro/assets/" + id;
                return httpFactory.get(url)
            }

            function deleteAsset(asset) {
                var url = "api/mro/assets/" + asset;
                return httpFactory.delete(url);
            }

            function getAssets() {
                var url = "api/mro/assets";
                return httpFactory.get(url);
            }

            function getAllAssets(pageable, filters) {
                var url = "api/mro/assets/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleAssets(assetIds) {
                var url = "api/mro/asset/multiple/[" + assetIds + "]";
                return httpFactory.get(url);
            }

            function getAssetsByType(typeId) {
                var url = "api/mro/assets/type/" + typeId;
                return httpFactory.get(url)
            }

            function getResourcesByType(type, id) {
                var url = "api/mro/assets/resources/" + type + "/" + id;
                return httpFactory.get(url)
            }

            function saveAssetAttributes(attributes) {
                var url = "api/mro/assets/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAssetSpareParts(assetId) {
                var url = "api/mro/assets/" + assetId + "/parts";
                return httpFactory.get(url);
            }


            function createMultipleAssetSpareParts(assetId, spareParts) {
                var url = "api/mro/assets/" + assetId + "/parts/multiple";
                return httpFactory.post(url, spareParts);
            }

            function deleteAssetSparePart(assetId, sparePartId) {
                var url = "api/mro/assets/" + assetId + "/parts/" + sparePartId;
                return httpFactory.delete(url);
            }

            function getAssetTabCounts(assetId) {
                var url = "api/mro/assets/" + assetId + "/count";
                return httpFactory.get(url);
            }

            function getAssetWorkOrders(assetId) {
                var url = "api/mro/assets/" + assetId + "/workorders";
                return httpFactory.get(url);
            }

            function deleteAssetMeter(assetId, meterId) {
                var url = "api/mro/assets/delete/asset/" + assetId + "/meter/" + meterId;
                return httpFactory.delete(url);
            }

            function getAssetsByResource(resourceId) {
                var url = "api/mro/assets/resources/" + resourceId;
                return httpFactory.get(url);
            }
        }
    }
);