define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('CustomerSupplierService', CustomerSupplierService);

        function CustomerSupplierService(httpFactory) {
            return {
                createCustomer: createCustomer,
                updateCustomer: updateCustomer,
                deleteCustomer: deleteCustomer,
                getCustomer: getCustomer,
                getAllCustomers: getAllCustomers,
                getCustomers: getCustomers,
                getMultipleCustomers: getMultipleCustomers,
                getAllCustomerProblemReports: getAllCustomerProblemReports,
                getCustomerTabCount: getCustomerTabCount,

                createSupplier: createSupplier,
                updateSupplier: updateSupplier,
                deleteSupplier: deleteSupplier,
                getSupplier: getSupplier,
                getAllSuppliers: getAllSuppliers,
                getSuppliers: getSuppliers,
                getMultipleSuppliers: getMultipleSuppliers
            };

            function createCustomer(customer) {
                var url = "api/pqm/customers";
                return httpFactory.post(url, customer);
            }

            function updateCustomer(customer) {
                var url = "api/pqm/customers/" + customer.id;
                return httpFactory.put(url, customer);
            }

            function deleteCustomer(customerId) {
                var url = "api/pqm/customers/" + customerId;
                return httpFactory.delete(url);
            }

            function getCustomer(customerId) {
                var url = "api/pqm/customers/" + customerId;
                return httpFactory.get(url);
            }

            function getAllCustomers(pageable, filters) {
                var url = "api/pqm/customers/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getCustomers() {
                var url = "api/pqm/customers";
                return httpFactory.get(url);
            }

            function getMultipleCustomers(customerIds) {
                var url = "api/pqm/customers/multiple/[" + customerIds + "]";
                return httpFactory.get(url);
            }

            function createSupplier(supplier) {
                var url = "api/pqm/suppliers";
                return httpFactory.post(url, supplier);
            }

            function updateSupplier(supplier) {
                var url = "api/pqm/suppliers/" + supplier.id;
                return httpFactory.put(url, supplier);
            }

            function deleteSupplier(supplierId) {
                var url = "api/pqm/suppliers/" + supplierId;
                return httpFactory.delete(url);
            }

            function getSupplier(supplierId) {
                var url = "api/pqm/suppliers/" + supplierId;
                return httpFactory.get(url);
            }

            function getSuppliers() {
                var url = "api/pqm/suppliers";
                return httpFactory.get(url);
            }

            function getAllSuppliers(pageable, filters) {
                var url = "api/pqm/suppliers/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleSuppliers(planIds) {
                var url = "api/pqm/suppliers/multiple/[" + planIds + "]";
                return httpFactory.get(url);
            }

            function getAllCustomerProblemReports(customer) {
                var url = "api/pqm/customers/" + customer + "/problemreports";
                return httpFactory.get(url);
            }

            function getCustomerTabCount(id) {
                var url = "api/pqm/customers/" + id + "/count";
                return httpFactory.get(url)
            }
        }
    }
);