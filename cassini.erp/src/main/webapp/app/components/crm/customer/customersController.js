define(['app/app.modules',
        'app/components/crm/customer/customerFactory',
        'app/shared/constants/listValues',
        'app/shared/directives/tableDirectives',
        'app/components/crm/customer/shared/customersMapViewController',
        'app/components/crm/customer/search/geoSearchController',
        'app/components/crm/customer/new/newCustomerController'
    ],
    function ($app) {
        $app.controller('CustomersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'customerFactory', 'uiGmapGoogleMapApi', 'ERPLists',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                          customerFactory, GoogleMapApi, ERPLists) {

                    $rootScope.iconClass = "fa flaticon-office";
                    $rootScope.viewTitle = "Customers";

                    $rootScope.showGeoSearch = false;
                    $scope.customerTypes = ERPLists.customerTypes;
                    $scope.templates = {
                        mapViewTemplate: 'app/components/crm/customer/shared/customersMapView.jsp',
                        geoSearch: 'app/components/crm/customer/search/geoSearchView.jsp'
                    };

                    $rootScope.viewType = 'grid';
                    $scope.spinner = {
                        active: false
                    };

                    $scope.loading = true;
                    $scope.pageable = {
                        page: 1,
                        size: 15,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };


                    $scope.pagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: Infinity,
                        size: $scope.pageable.size,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };


                    $scope.customers = $scope.pagedResults;
                    $scope.customerTypes = ERPLists.customerTypes;

                    $scope.emptyFilters = {
                        name: null,
                        customerType: null,
                        region: null,
                        salesRep: null,
                        contactPerson: null,
                        contactPhone: null,
                        blacklisted: false
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);


                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('customers-view-tb');
                    });

                    $rootScope.newCustomer = function () {
                        $state.go('app.crm.newcustomer');
                    };

                    $rootScope.geoSearch = function () {
                        $rootScope.showGeoSearch = !$rootScope.showGeoSearch;
                    };

                    $rootScope.setViewType = function (view) {
                        $rootScope.viewType = view;
                    };

                    $scope.sortColumn = function (col) {
                        if ($scope.pageable.sort.label == col) {
                            if ($scope.pageable.sort.order == 'asc') {
                                $scope.pageable.sort.order = 'desc';
                            }
                            else {
                                $scope.pageable.sort.order = 'asc';
                            }
                        }
                        else {
                            $scope.pageable.sort.label = col
                            $scope.pageable.sort.order = 'asc';
                        }

                        if (col == "name") {
                            $scope.pageable.sort.field = "name";
                        }
                        else if (col == "region") {
                            $scope.pageable.sort.field = "salesRegion.name";
                        }
                        else if (col == "contactPerson") {
                            $scope.pageable.sort.field = "contactPerson.firstName";
                        }

                        //$scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };

                    $scope.pageChanged = function () {
                        $scope.loading = true;
                        var pagenum = $scope.pageable.page;
                        if ($scope.viewType == 'grid') {
                            $scope.customers.content = [];
                        }
                        $scope.loadCustomers();
                        $state.go("app.crm.customers", {page: pagenum});
                    };

                    $scope.resetFilters = function () {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };

                    $scope.applyFilters = function () {
                        $scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };

                    $scope.getContactPhone = function (customer) {
                        var phone = null;
                        if (customer != null && customer.contactPerson != null) {
                            phone = customer.contactPerson.phoneMobile;

                            if (phone == null) {
                                phone = customer.contactPerson.phoneOffice;
                            }
                        }

                        if (customer != null && phone == null) {
                            phone = customer.officePhone;
                        }

                        return phone;
                    };

                    $scope.loadCustomers = function () {
                        $scope.loading = true;
                        $scope.spinner.active = true;
                        if ($scope.viewType == 'grid') {
                            $scope.customers.content = [];
                        }
                        customerFactory.getCustomers($scope.filters, $scope.pageable).then(
                            function (data) {
                                $scope.customers = data;
                                $scope.loading = false;
                                $scope.spinner.active = false;
                                $rootScope.$broadcast('customersLoaded', $scope.customers);
                            }
                        );
                    };

                    $scope.blacklistCustomer = function (customer) {
                        customerFactory.updateCustomer(customer).then(
                            function (data) {
                                if (customer.blacklisted == true) {
                                    $rootScope.showSuccessMessage("Customer has been blacklisted");
                                }
                                else {
                                    $rootScope.showSuccessMessage("Customer has been removed from blacklist");
                                }
                            }
                        )
                    };
                    (function () {
                        if ($stateParams.page != null && $stateParams.page != undefined) {
                            $scope.pageable.page = $stateParams.page;
                        }
                        $scope.loadCustomers();
                    })();
                }
            ]
        );
    }
);