define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('materialsPOFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                        getMaterialsClassification: function () {
                            var url = "api/production/materialcategories";
                            return httpFactory.get(url);
                        },
                        searchMaterials: function (category, pageable) {
                            var url = "api/production/materials/category/{0}?page={1}&size={2}&sort={3}:{4}".
                                format(category, pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        freeTextSearch: function (terms, pageable) {
                            var url = "api/production/materials/search?name={0}&page={1}&size={2}&sort={3}:{4}".
                                format(terms, pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },

                        getMaterials: function (pageable) {
                            var url = "api/production/materials/pagable?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getMaterial: function (materialId) {
                            var url = "api/production/materials/" + materialId;
                            return httpFactory.get(url);
                        },
                        getMaterialTypes: function () {
                            var url = "api/production/materialtypes";
                            return httpFactory.get(url);
                        },

                        getMaterialsBySku: function (sku) {
                            var url = "api/production/materials/sku/" + sku;
                            return httpFactory.get(url);
                        },
                        updateMaterial: function (material) {
                            var url = "api/production/materials/" + material.id;
                            return httpFactory.put(url, material);
                        },

                        createMaterial: function (material) {
                            var url = "api/production/materials";
                            return httpFactory.post(url, material);
                        },

                        updateMaterialCategory: function (materialCategory) {
                            var url = "api/production/materialcategories/" + materialCategory.id;
                            return httpFactory.put(url, materialCategory);
                        },

                        createMaterialCategory: function (materialCategory) {
                            var url = "api/production/materialcategories";
                            return httpFactory.post(url, materialCategory);
                        },

                        getMaterialPurchaseOrders: function (pageable) {
                            var url = "api/production/materialpurchaseorders/pagable?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },

                        createMaterialPO: function (materialPO) {
                            var url = "api/production/materialpurchaseorders";
                            return httpFactory.post(url, materialPO);
                        },

                        updateMaterialPO: function (materialPO) {
                            var url = "api/production/materialpurchaseorders/" + materialPO.id;
                            return httpFactory.put(url, materialPO);
                        },

                        getMaterialPurchaseOrder: function (materialPoId) {
                            var url = "api/production/materialpurchaseorders/" + materialPoId;
                            return httpFactory.get(url);
                        },

                        getMaterialPurchaseOrderDetailsByIssued: function (materialPoId, issued) {
                            var url = "api/production/materialpurchaseorders/" + materialPoId + "/" + issued + "/details";
                            return httpFactory.get(url);
                        },

                        getMaterialPODetailsByMaterialAndMaterialPurchaseOrder: function (material, materialPOId) {
                            var url = "api/production/materialpurchaseorders/byMaterial/" + material + "/" + materialPOId + "/details";
                            return httpFactory.get(url);
                        },

                        createMaterialPOs: function (materialPOs) {
                            var url = "api/production/materialpurchaseorders/multiple";
                            return httpFactory.post(url, materialPOs);
                        }
                    }
                }
            ]
        );
    }
);