define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('suppliersFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                        getSuppliers: function (pageable) {
                            var url = "api/production/suppliers/pagable?page={0}&size={1}&sort={2}:{3}".
                                format(pageable.page - 1, pageable.size, pageable.sort.field, pageable.sort.order);
                            return httpFactory.get(url);
                        },
                        getAllSuppliers: function () {
                            var url = "api/production/suppliers";
                            return httpFactory.get(url);
                        },
                        getSupplier: function (supplierId) {
                            var url = "api/production/suppliers/" + supplierId;
                            return httpFactory.get(url);
                        },

                        updateSupplier: function (supplier) {
                            var url = "api/production/suppliers/" + supplier.id;
                            return httpFactory.put(url, supplier);
                        },

                        createSupplier: function (supplier) {
                            var url = "api/production/suppliers";
                            return httpFactory.post(url, supplier);
                        },
                        validateSupplier: function (supplierName, officePhone) {
                            var url = "api/production/suppliers/" + supplierName + "/" + officePhone;
                            return httpFactory.get(url);
                        },
                        deleteSupplier: function (supplierId) {
                            var url = "api/production/suppliers/" + supplierId;
                            return httpFactory.delete(url, material);
                        },
                        getSuppliersByIds: getSuppliersByIds,
                        getSupplierNameReferences: function (objects, property) {
                            var ids = [];
                            angular.forEach(objects, function (object) {
                                if (object[property] != null && ids.indexOf(object[property]) == -1) {
                                    ids.push(object[property]);
                                }
                            });

                            if (ids.length > 0) {
                                getSuppliersByIds(ids).then(
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

                    function getSuppliersByIds(ids) {
                        var url = "api/production/suppliers/multiple/[" + ids + "]";
                        return httpFactory.get(url);
                    }


                }
            ]
        )
        ;
    }
)
;