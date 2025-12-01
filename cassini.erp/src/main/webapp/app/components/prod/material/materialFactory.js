define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('materialFactory',
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

                        getMaterialss: function (filters, pageable) {
                            var url = "api/production/materials?sku={0}&name={1}&category={2}".format(filters.sku, filters.name, filters.category);
                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
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
                        getMaterialSuppliersByMaterial: function (materialId) {
                            var url = "api/production/materials/materialSuppliers/byMaterial/" + materialId;
                            return httpFactory.get(url);
                        },
                        getMaterialSuppliersBySupplier: function (supp) {
                            var url = "api/production/materials/materialSuppliers/bySupplier/" + supp;
                            return httpFactory.get(url);
                        },
                        deleteMaterialSupplier: function (ms) {
                            var url = "api/production/materials/materialSuppliers/" + ms.materialId + "/" + ms.supplierId;
                            return httpFactory.delete(url);
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
                        createMaterialSuppliers: function (materialSuppliers) {
                            var url = "api/production/materials/materialSuppliers";
                            return httpFactory.post(url, materialSuppliers);
                        },
                        updateMaterialCategory: function (materialCategory) {
                            var url = "api/production/materialcategories/" + materialCategory.id;
                            return httpFactory.put(url, materialCategory);
                        },

                        createMaterialCategory: function (materialCategory) {
                            var url = "api/production/materialcategories";
                            return httpFactory.post(url, materialCategory);
                        },

                        getMaterialsInventory: function (materialIds) {
                            var dfd = $q.defer(),
                                url = "api/production/materialinventory?materials=" + materialIds;
                            return httpFactory.get(url);
                        },

                        addInvInventory: function (material) {
                            var dfd = $q.defer(),
                                url = "api/production/materialinventory/stockin?material=" + material.id + "&quantity=" + material.inventory.newInventory;
                            return httpFactory.post(url, material);
                        },


                        issueInvInventory: function (material) {
                            var dfd = $q.defer(),
                                url = "api/production/materialinventory/stockout?material=" + material.id + "&quantity=" + material.inventory.issueInv;
                            return httpFactory.get(url);
                        },

                        saveMaterialInventory: function (inventory) {
                            var url = "api/production/materialinventory";
                            return httpFactory.post(url, inventory);
                        },
                        getMaterialInventoryHistory: function (productId, pageable) {
                            var url = "api/production/materialinventory/{0}/history?page={1}&size={2}".
                                format(productId, pageable.page - 1, pageable.size);
                            return httpFactory.get(url);
                        },
                        getMaterialsByIds: getMaterialsByIds,
                        createMaterialInventory: function (inventory) {
                            var url = "api/production/materialinventory";
                            return httpFactory.post(url, inventory);
                        },
                        getMaterialIssueReport: function (filters, pageable) {
                            var url = "api/production/materialinventory/issueReport";
                            url += "?timestamp={0}&sku={1}&name={2}&issuedQty={3}&consumeQty={4}&remainingQty={5}".
                                format(filters.timestamp, filters.sku, filters.name, filters.issuedQty,
                                filters.consumeQty, filters.remainingQty);

                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        updateMaterialDailyReport: function (dailyReport) {
                            var url = "api/production/materialinventory/issueReport";
                            return httpFactory.put(url, dailyReport);
                        },
                        getMaterialNameReferences: function (objects, property) {
                            var ids = [];
                            angular.forEach(objects, function (object) {
                                if (object[property] != null && ids.indexOf(object[property]) == -1) {
                                    ids.push(object[property]);
                                }
                            });

                            if (ids.length > 0) {
                                getMaterialsByIds(ids).then(
                                    function (data) {
                                        var map = new Hashtable();

                                        angular.forEach(data, function (item) {
                                            map.put(item.id, item);
                                        });
                                        angular.forEach(objects, function (object) {
                                            if (object[property] != null) {
                                                var item = map.get(object[property]);
                                                if (item != null) {
                                                    object[property + "Object"] = item;
                                                }
                                            }
                                        });
                                    }
                                );
                            }
                        }


                    }

                    function getMaterialsByIds(ids) {
                        var url = "api/production/materials/multiple/[" + ids + "]";
                        return httpFactory.get(url);
                    }
                }
            ]
        );
    }
);