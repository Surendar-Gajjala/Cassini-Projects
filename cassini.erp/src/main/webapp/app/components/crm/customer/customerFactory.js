define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('customerFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getCustomerTypes: function () {
                            var url = "api/crm/customers/types";
                            return httpFactory.get(url);
                        },
                        saveCustomerType: function (customerType) {
                            var url = "api/crm/customers/types";
                            return httpFactory.post(url, customerType);
                        },
                        getSalesRegions: function (page) {
                            var url = "api/crm/customers/salesregions?page={0}&size={1}&sort={2}:{3}".
                                format(page.page - 1, page.size, page.sort.field, page.sort.order);
                            return httpFactory.get(url);
                        },
                        saveSalesRegion: function (salesRegion) {
                            var url = "api/crm/customers/salesregions";
                            return httpFactory.post(url, salesRegion);
                        },
                        getCustomers: function (criteria, page) {
                            var url = "api/crm/customers";

                            url += "?name={0}&region={1}&salesRep={2}&contactPerson={3}&contactPhone={4}&state={5}&blacklisted={6}&customerType={7}".
                                format(criteria.name, criteria.region, criteria.salesRep,
                                criteria.contactPerson, criteria.contactPhone, criteria.state, criteria.blacklisted, criteria.customerType);
                            url += "&page={0}&size={1}&sort={2}:{3}".
                                format(page.page - 1, page.size, page.sort.field, page.sort.order);
                            return httpFactory.get(url);
                        },
                        searchCustomers: function (criteria, page) {
                            var url = "api/crm/customers/search";
                            url += "?page={0}&size={1}&sort={2}:{3}&searchQuery={4}".
                                format(page.page - 1, page.size, page.sort.field, page.sort.order, criteria.searchQuery);
                            return httpFactory.get(url);
                        },
                        getCustomer: function (customerId) {
                            var url = "api/crm/customers/" + customerId;
                            return httpFactory.get(url);
                        },
                        getCustomerOrders: function (customerId) {
                            var url = "api/crm/customers/" + customerId + "/orders";
                            return httpFactory.get(url);
                        },
                        getCustomerReturns: function (customerId) {
                            var url = "api/crm/customers/" + customerId + "/returns";
                            return httpFactory.get(url);
                        },
                        getCustomerReports: function (customerId) {
                            var url = "api/crm/customers/" + customerId + "/reports";
                            return httpFactory.get(url);
                        },
                        createCustomer: function (customer) {
                            var url = "api/crm/customers";
                            return httpFactory.post(url, customer);
                        },
                        updateCustomer: function (customer) {
                            var url = "api/crm/customers/" + customer.id;
                            return httpFactory.put(url, customer);
                        },
                        getCustomerByCustomernameandRegion: function (name,salesRegion) {
                            var url = "api/crm/customers/name/region?name={0}&salesRegion={1}".
                                format(name, salesRegion);
                            return httpFactory.get(url);
                        },
                        radiusSearch: function (latitude, longitude, radius) {
                            var url = "api/crm/customers/geosearch/radius?latitude={0}&longitude={1}&radius={2}".
                                format(latitude, longitude, radius);
                            return httpFactory.get(url);
                        },
                        boxSearch: function (ne, sw) {
                            var url = "api/crm/customers/geosearch/box?neLatitude={0}&neLongitude={1}&swLatitude={2}&swLongitude={3}".
                                format(ne.lat(), ne.lng(), sw.lat(), sw.lng());
                            return httpFactory.get(url);
                        },
                        deleteOrderItem: function (id) {
                            var url = "api/crm/customers/" + id;
                            httpFactory.delete(url)
                        }
                    }
                }
            ]
        );
    }
);