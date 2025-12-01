define(['app/app.modules',
        'app/components/crm/customer/customerFactory'
    ],
    function($app) {
        $app.controller('SelectCustomerDialogController',
            [
                '$scope', '$modalInstance', 'customerFactory', 'salesRepFilter',

                function($scope, $modalInstance, customerFactory, salesRepFilter) {

                    $scope.salesRepFilter = salesRepFilter;

                    $scope.loading = true;
                    $scope.emptyResults = {
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

                    $scope.customers = $scope.emptyResults;

                    $scope.pageable = {
                        page: 1,
                        size: 20,
                        sort: {
                            label: "name",
                            field: "name",
                            order: "asc"
                        }
                    };

                    $scope.filters = {
                        name: "",
                        region: "",
                        salesRep: "",
                        blacklisted: false
                    };

                    $scope.filterButtonText = "Show Filters";
                    $scope.selectedCustomer = null;

                    $scope.applyCriteria = function() {
                        $scope.pageable.page = 1;
                        $scope.loadCustomers();
                    };

                    $scope.selectCustomer = function(customer) {
                        $scope.selectedCustomer = customer;
                    };

                    $scope.resetCriteria = function() {
                        $scope.filters = {
                            name: "",
                            region: "",
                            salesRep: "",
                            customerType: "",
                            blacklisted: false
                        };

                        $scope.pageable.page = 1;

                        $scope.loadCustomers();
                    };

                    $scope.ok = function() {
                        $modalInstance.close($scope.selectedCustomer);
                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.isCustomerSelected = function() {
                        angular.forEach($scope.customers.content, function(customer) {
                            if(customer.selected == true) {
                                $scope.selectedCustomer = customer;
                            }
                        });
                    };

                    $scope.loadCustomers = function () {
                        if($scope.salesRepFilter != null) {
                            $scope.filters.salesRep = $scope.salesRepFilter.firstName;
                        }

                        $scope.selectedCustomer = null;
                        customerFactory.getCustomers($scope.filters, $scope.pageable).then (
                            function(data) {
                                $scope.loading = false;
                                $scope.customers = data;
                            }
                        );
                    };

                    (function() {
                        $scope.loadCustomers();
                    })();

                }
            ]
        );
    }
);