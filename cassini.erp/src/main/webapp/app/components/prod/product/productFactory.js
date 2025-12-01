define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('productFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getProductsClassification: function () {
                            var url = "api/production/productcategories";
                            return httpFactory.get(url);
                        },
                        searchProducts: function (category, pageable) {
                            var url = "api/production/products/category/{0}?page={1}&size={2}&sort={3}:{4}".
                                format(category, pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        freeTextSearch: function (terms, pageable) {
                            var url = "api/production/products/search?name={0}&page={1}&size={2}&sort={3}:{4}".
                                format(terms, pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getProducts: function (filters, pageable) {
                            var url = "api/production/products?sku={0}&name={1}&category={2}".format(filters.sku, filters.name, filters.category);
                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getProduct: function (productId) {
                            var url = "api/production/products/" + productId;
                            return httpFactory.get(url);
                        },
                        getProductBySku: function (sku) {
                            var url = "api/production/products/sku/" + sku;
                            return httpFactory.get(url);
                        },
                        updateProduct: function (product) {
                            var url = "api/production/products/" + product.id;
                            return httpFactory.put(url, product);
                        },
                        getProductsInventory: function (productIds) {
                            var dfd = $q.defer(),
                                url = "api/production/productinventory?products=" + productIds;
                            return httpFactory.get(url);
                        },

                        getLowInventory: function () {
                            url = "api/production/productinventory/lowinventory";
                            return httpFactory.get(url);
                        },
                        addInvInventory: function (product) {
                            var dfd = $q.defer(),
                                url = "api/production/productinventory/stockin?product=" + product.id + "&quantity=" + product.inventory.newInventory;
                            return httpFactory.get(url);
                        },

                        issueInvInventory: function (product) {
                            var dfd = $q.defer(),
                                url = "api/production/productinventory/stockout?product=" + product.id + "&quantity=" + product.inventory.issueInv;
                            return httpFactory.get(url);
                        },

                        saveProductInventory: function (inventory) {
                            var url = "api/production/productinventory";
                            return httpFactory.post(url, inventory);
                        },
                        getProductInventoryHistory: function (productId, pageable) {
                            var url = "api/production/productinventory/{0}/history?page={1}&size={2}&sort={3}:{4}".
                                format(productId, pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getProductInventoryHistoryByStockType: function (productId, pageable, criteria) {
                            var url = "api/production/productinventory/{0}/history/stocktype?page={1}&size={2}&sort={3}:{4}".
                                format(productId, pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);

                            url += "&stockType={0}&product={1}".
                                format(criteria.stockType, criteria.product);

                            return httpFactory.get(url);
                        },
                        getProductTypes: function () {
                            var url = "api/production/producttypes";
                            return httpFactory.get(url);
                        },
                        createProduct: function (product) {
                            var url = "api/production/products";
                            return httpFactory.post(url, product);
                        },
                        createProductInventory: function (inventory) {
                            var url = "api/production/productinventory";
                            return httpFactory.post(url, inventory);
                        },
                        createProductCategory: function (productCategory) {
                            var url = "api/production/productcategories";
                            return httpFactory.post(url, productCategory);
                        },

                        updateRestockLevel:function(productId, inventory){
                            var url = "api/production/productinventory/productRestock/"+ productId;
                            return httpFactory.put(url, inventory);
                        }
                    }
                }
            ]
        );
    }
);