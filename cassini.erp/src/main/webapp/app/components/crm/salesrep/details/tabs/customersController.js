define(['app/app.modules',
        'app/components/crm/salesrep/salesRepFactory',
        'app/shared/directives/tableDirectives',
        'app/components/crm/customer/shared/customersMapViewController'
    ],
    function (app) {
        app.controller('SalesRepCustomersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', 'salesRepFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, salesRepFactory) {
                    $scope.setActiveFlag(0);

                    $scope.mapViewTemplate = 'app/components/crm/customer/shared/customersMapView.jsp';

                    $scope.viewType = 'grid';
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

                    $scope.emptyPagedResults = {
                        content: [],
                        last: false,
                        totalPages: 0,
                        totalElements: 0,
                        size: 10,
                        number: 0,
                        sort: null,
                        first: false,
                        numberOfElements: 0
                    };

                    $scope.customers = $scope.emptyPagedResults;

                    $scope.emptyFilters = {
                        name: null,
                        region: null,
                        contactPerson: null,
                        contactPhone: null
                    };

                    $scope.filters = angular.copy($scope.emptyFilters);

                    $scope.resetFilters = function() {
                        $scope.filters = angular.copy($scope.emptyFilters);
                        $scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };

                    $scope.applyFilters = function() {
                        $scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };


                    $scope.sortColumn =  function (col) {
                        if($scope.pageable.sort.label == col) {
                            if($scope.pageable.sort.order == 'asc') {
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

                        if(col == "name") {
                            $scope.pageable.sort.field = "name";
                        }
                        else if(col == "region") {
                            $scope.pageable.sort.field = "salesRegion.name";
                        }
                        else if(col == "contactPerson") {
                            $scope.pageable.sort.field = "contactPerson.firstName";
                        }

                        //$scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };

                    $scope.pageChanged = function() {
                        $scope.loading = true;
                        if($scope.viewType == 'grid') {
                            $scope.customers.content = [];
                        }
                        $scope.loadCustomers();
                    };

                    $scope.setViewType = function(view) {
                        $scope.viewType = view;
                    };

                    $scope.loadCustomers = function() {
                        $scope.spinner.active = true;
                        if($scope.viewType == 'grid') {
                            $scope.customers.content = [];
                        }
                        salesRepFactory.getSalesRepCustomers($scope.salesRep.id, $scope.filters, $scope.pageable).then (
                            function(data) {
                                $scope.customers = data;
                                $scope.loading = false;
                                $scope.spinner.active = false;
                                $rootScope.$broadcast('customersLoaded', $scope.customers);
                            }
                        )
                    };

                    $scope.getContactPhone = function(customer) {
                        var phone = null;
                        if(customer.contactPerson != null) {
                            phone = customer.contactPerson.phoneMobile;

                            if(phone == null) {
                                phone = customer.contactPerson.phoneOffice;
                            }
                        }

                        if(phone == null) {
                            phone = customer.officePhone;
                        }

                        return phone;
                    };

                    $scope.$on("salesRepLoaded", function() {
                        $scope.loadCustomers();
                    });

                    (function() {
                        $scope.loadCustomers();
                    })();

                }
            ]
        );
    }
);