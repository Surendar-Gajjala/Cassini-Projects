define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('ProcurementService', ProcurementService);

        function ProcurementService(httpFactory) {
            return {
                createSupplier: createSupplier,
                updateSupplier: updateSupplier,
                deleteSupplier: deleteSupplier,
                getSupplier: getSupplier,
                getSuppliers: getSuppliers,
                getAllSuppliers: getAllSuppliers,
                getSupplierByCode: getSupplierByCode,

                createManufacturer: createManufacturer,
                updateManufacturer: updateManufacturer,
                deleteManufacturer: deleteManufacturer,
                getManufacturer: getManufacturer,
                getManufacturers: getManufacturers,
                getAllManufactures: getAllManufactures,
                getFilterManufactures: getFilterManufactures,
                getFilterSuppliers: getFilterSuppliers,
                getManufacturerByCode: getManufacturerByCode,
                deleteUpdates: deleteUpdates,
                updateUpdates: updateUpdates,
                getUpdatesByPerson: getUpdatesByPerson,
                updateMessage: updateMessage
            };

            function getFilterManufactures(pageable, filters) {
                var url = "api/drdo/procurement/manufacturers/filter?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(filters.searchText);
                return httpFactory.get(url);
            }

            function getFilterSuppliers(pageable, filters) {
                var url = "api/drdo/procurement/suppliers/filter?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".format(filters.searchText);
                return httpFactory.get(url);
            }

            function createSupplier(supplier) {
                var url = "api/drdo/procurement/suppliers";
                return httpFactory.post(url, supplier);
            }

            function updateSupplier(supplier) {
                var url = "api/drdo/procurement/suppliers/" + supplier.id;
                return httpFactory.put(url, supplier);
            }

            function deleteSupplier(supplierId) {
                var url = "api/drdo/procurement/suppliers/" + supplierId;
                return httpFactory.delete(url);
            }

            function getSupplier(supplierId) {
                var url = "api/drdo/procurement/suppliers/" + supplierId;
                return httpFactory.get(url);
            }

            function getSuppliers() {
                var url = "api/drdo/procurement/suppliers";
                return httpFactory.get(url);
            }

            function getSupplierByCode(code) {
                var url = "api/drdo/procurement/suppliers/byCode/" + code;
                return httpFactory.get(url);
            }

            function getAllSuppliers(pageable) {
                var url = "api/drdo/procurement/suppliers/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function createManufacturer(manufacturer) {
                var url = "api/drdo/procurement/manufacturers";
                return httpFactory.post(url, manufacturer);
            }

            function updateManufacturer(manufacturer) {
                var url = "api/drdo/procurement/manufacturers/" + manufacturer.id;
                return httpFactory.put(url, manufacturer);
            }

            function deleteManufacturer(manufacturerId) {
                var url = "api/drdo/procurement/manufacturers/" + manufacturerId;
                return httpFactory.delete(url);
            }

            function getManufacturer(manufacturerId) {
                var url = "api/drdo/procurement/manufacturers/" + manufacturerId;
                return httpFactory.get(url);
            }

            function getManufacturers() {
                var url = "api/drdo/procurement/manufacturers";
                return httpFactory.get(url);
            }

            function getAllManufactures(pageable) {
                var url = "api/drdo/procurement/manufacturers/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getManufacturerByCode(code) {
                var url = "api/drdo/procurement/manufacturers/byCode/" + code;
                return httpFactory.get(url);
            }

            function deleteUpdates(personId) {
                var url = "api/drdo/procurement/updates/" + personId + "/delete";
                return httpFactory.delete(url);
            }

            function updateUpdates(personId) {
                var url = "api/drdo/procurement/updates/" + personId + "/update";
                return httpFactory.get(url);
            }

            function getUpdatesByPerson(personId) {
                var url = "api/drdo/procurement/updates/" + personId;
                return httpFactory.get(url);
            }

            function updateMessage(update) {
                var url = "api/drdo/procurement/updates/" + update.id;
                return httpFactory.put(url, update);
            }
        }
    }
)
;