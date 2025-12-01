/**
 * Created by anuko on 25-09-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (module) {
        module.factory('SupplierService', SupplierService);

        function SupplierService(httpFactory) {
            return {
                createSupplier: createSupplier,
                updateSupplier: updateSupplier,
                getSuppliers: getSuppliers,
                getSupplier: getSupplier,
                deleteSupplier: deleteSupplier,
                freeTextSearch: freeTextSearch,
                getSuppliersByIds: getSuppliersByIds,
                getSupplierReferences: getSupplierReferences
            };

            function createSupplier(supplier) {
                var url = "api/suppliers/";
                return httpFactory.post(url, supplier);
            }

            function getSuppliers(pageable) {
                var url = "api/suppliers?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }


            function updateSupplier(supplier) {
                var url = "api/suppliers/" + supplier.id;
                return httpFactory.put(url, supplier);
            }

            function getSupplier(supplierId) {
                var url = "api/suppliers/" + supplierId;
                return httpFactory.get(url);
            }

            function deleteSupplier(supplierId) {
                var url = "api/suppliers/" + supplierId;
                return httpFactory.delete(url);
            }

            function freeTextSearch(pageable, searchQuery) {
                var url = "api/suppliers/freesearch?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(searchQuery /*, criteria.name, criteria.description*/)
                return httpFactory.get(url);
            }

            function getSuppliersByIds(supplierIds) {
                var url = "api/suppliers/multiple/[" + supplierIds + "]";
                return httpFactory.get(url);
            }

            function getSupplierReferences(objects, property) {
                var supplierIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && supplierIds.indexOf(object[property]) == -1) {
                        supplierIds.push(object[property]);
                    }
                });

                if (supplierIds.length > 0) {
                    getSuppliersByIds(supplierIds).then(
                        function (suppliers) {
                            var map = new Hashtable();
                            angular.forEach(suppliers, function (supplier) {
                                map.put(supplier.id, supplier);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var supplier = map.get(object[property]);
                                    if (supplier != null) {
                                        object[property + "Object"] = supplier;
                                    }
                                }
                            });
                        }
                    );
                }
            }

        }
    }
);