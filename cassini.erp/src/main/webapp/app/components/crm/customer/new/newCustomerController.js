define(['app/app.modules',
        'app/components/common/commonFactory',
        'app/components/crm/customer/customerFactory',
        'app/components/crm/salesrep/salesRepFactory',
        'app/components/crm/salesregion/salesRegionFactory'
    ],
    function ($app) {
        $app.controller('NewCustomerController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies',
                'commonFactory', 'customerFactory', 'salesRepFactory', 'salesRegionFactory', 'uiGmapGoogleMapApi',

                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                          commonFactory, customerFactory, salesRepFactory, salesRegionFactory, GoogleMapApi) {

                    $rootScope.viewTitle = "New Customer";
                    $rootScope.customerCreated = false;

                    $scope.error = {
                        hasError: false,
                        errorMessage: ""
                    };

                    $rootScope.createAnother = false;
                    $scope.salesRegion = {
                        name: null,
                        district: null,
                        state: null,
                        country: commonFactory.getCountryAndStatesMapByCountry("India").country
                    };
                    $scope.showNewRegion = false;
                    $scope.newSalesRegion = angular.copy($scope.salesRegion);

                    $scope.customerTypes = [];
                    $scope.regions = [];
                    $scope.salesReps = [];
                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;

                    $scope.emptyCustomer = {
                        name: "",
                        customerType: null,
                        officePhone: "",
                        officeFax: "",
                        officeEmail: "",
                        customerAddresses: [
                            {
                                addressType: commonFactory.getAddressTypeByName("Office"),
                                addressText: null,
                                city: null,
                                state: null,
                                pincode: null,
                                country: commonFactory.getCountryAndStatesMapByCountry("India").country
                            }
                        ],
                        salesRegion: null,
                        salesRep: null,
                        contactPerson: {
                            personType: commonFactory.getPersonTypeByName("Customer"),
                            firstName: null,
                            phoneMobile: "",
                            email: ""
                        },
                        geoLocation: {
                            latitude: null,
                            longitude: null
                        }
                    };

                    $scope.customer = angular.copy($scope.emptyCustomer);

                    $scope.map = {
                        show: true,
                        control: {},
                        version: "unknown",
                        showTraffic: true,
                        showBicycling: false,
                        showWeather: false,
                        showHeat: false,
                        center: {
                            latitude: 17.8760325,
                            longitude: 79.2727298
                        },
                        options: {
                            streetViewControl: false,
                            panControl: false,
                            maxZoom: 20,
                            minZoom: 3,
                            mapTypeControl: false
                        },
                        zoom: 6,
                        dragging: false,
                        bounds: {},
                        events: {}
                    };

                    $scope.marker = {
                        id: 1,
                        showWindow: false
                    };

                    GoogleMapApi.then(function (maps) {
                        $scope.api = maps;
                    });

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('newcustomer-view-tb')
                    });

                    $scope.createNewSalesRegion = function () {
                        $scope.error.hasError = false;
                        var name = $scope.newSalesRegion.name;
                        var state = $scope.newSalesRegion.state;
                        if (name != null && state != null && isRegionExists(name) == false) {
                            salesRegionFactory.saveSalesRegion($scope.newSalesRegion).then(
                                function (data) {
                                    var first = $scope.regions[0];
                                    $scope.regions.splice(0, 1);
                                    $scope.regions.unshift(data);
                                    $scope.regions.unshift(first);

                                    $scope.customer.salesRegion = data;
                                    $scope.showNewRegion = false;
                                }
                            )
                        }

                    };

                    function isRegionExists(name) {
                        var exists = false;
                        angular.forEach($scope.regions, function (region) {
                            if (region.createNew != undefined && region.createNew != true &&
                                region.name != null && region.name == name) {
                                exists = true;

                                $scope.error.hasError = true;
                                $scope.error.errorMessage = "Region '" + name + "' already exists";
                            }
                        });

                        return exists;
                    }


                    $scope.cancelNewSalesRegion = function () {
                        $scope.showNewRegion = false;
                    };

                    $rootScope.createNewCustomer = function () {
                        $scope.error.hasError = false;
                        if (validateCustomerData()) {
                            cleanupCustomerData();

                            customerFactory.createCustomer($scope.customer).then(
                                function (data) {
                                    $scope.customer = data;
                                    if ($scope.customer.geoLocation != null) {
                                        addMarker();
                                    }
                                    $rootScope.showSuccessMessage("Customer {0} is created successfully".format($scope.customer.name));
                                    $rootScope.createAnother = true;
                                    $rootScope.customerCreated = true;

                                    var source = $stateParams.source;
                                    if (source != null && source != undefined) {
                                        if (source == 'salesrep.reports') {
                                            var salesRep = $rootScope.lastSalesRep;
                                            $rootScope.lastNewCustomer = data;
                                            $state.go('app.crm.salesrep.fieldreports', {
                                                salesRepId: salesRep.id,
                                                mode: 'create'
                                            })
                                        } else if (source == 'shopping.cart') {
                                            $rootScope.lastNewCustomer = data;
                                            $state.go('app.crm.orders.cart', {mode: 'create'})
                                        }

                                    }
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else {
                            $scope.error.hasError = true;
                        }
                    };

                    $rootScope.createAnotherCustomer = function () {
                        $scope.customer = angular.copy($scope.emptyCustomer);
                        $rootScope.closeNotification();
                        $rootScope.createAnother = false;
                        $rootScope.customerCreated = false;
                    };

                    function addMarker() {
                        $scope.marker = {
                            id: $scope.customer.id,
                            latitude: $scope.customer.geoLocation.latitude,
                            longitude: $scope.customer.geoLocation.longitude
                        };

                        var bounds = new $scope.api.LatLngBounds();
                        var latlang = new $scope.api.LatLng($scope.marker.latitude, $scope.marker.longitude);
                        bounds.extend(latlang);

                        $scope.map.center = {
                            latitude: bounds.getCenter().lat(),
                            longitude: bounds.getCenter().lng()
                        };
                        var origCenter = {latitude: $scope.map.center.latitude, longitude: $scope.map.center.longitude};
                        $scope.map.zoom = 16;
                        $scope.map.control.refresh(origCenter);
                    }

                    function cleanupCustomerData() {
                        if ($scope.customer.customerAddresses[0].addressText == null ||
                            $scope.customer.customerAddresses[0].addressText == "") {
                            $scope.customer.customerAddresses = [];
                        }

                        if ($scope.customer.contactPerson.firstName == null ||
                            $scope.customer.contactPerson.firstName == "") {
                            $scope.customer.contactPerson = null;
                        }

                        if ($scope.customer.geoLocation.latitude == null || $scope.customer.geoLocation.latitude == "" ||
                            $scope.customer.geoLocation.longitude == null || $scope.customer.geoLocation.longitude == "") {
                            $scope.customer.geoLocation = null;
                        }
                    }

                    function loadCustomerTypes() {
                        customerFactory.getCustomerTypes().then(
                            function (data) {
                                $scope.customerTypes = data;
                            }
                        )
                    }

                    $rootScope.showAllCustomers = function () {
                        $state.go('app.crm.customers');
                    };

                    function loadRegions() {
                        var pageable = {
                            page: 1,
                            size: 10000000,
                            sort: {
                                field: "name",
                                order: "ASC"
                            }
                        };
                        customerFactory.getSalesRegions(pageable).then(
                            function (data) {
                                $scope.regions = data.content;
                                $scope.regions.unshift({
                                    createNew: true,
                                    name: "New Region..."
                                });
                            }
                        )
                    }

                    $scope.onSelectRegion = function (item, region) {
                        $scope.validateCustomernameWithRegion($scope.customer);
                        if (region.name != null && region != null && region.state != null) {
                            $scope.showNewRegion = false;
                            var selectedState = null;
                            angular.forEach($scope.states, function (state) {
                                if (region.state.name == state.name) {
                                    selectedState = state;
                                }
                            });

                            if (selectedState != null) {
                                $scope.customer.customerAddresses[0].state = selectedState;
                            }
                        }
                        else if (region.createNew != undefined &&
                            region.createNew != null &&
                            region.createNew == true) {
                            $scope.showNewRegion = true;
                        }


                    };

                    function loadSalesReps() {
                        var pageable = {
                            page: 1,
                            size: 1000,
                            sort: {
                                field: 'firstName',
                                order: 'ASC'
                            }
                        };
                        salesRepFactory.getSalesReps(pageable).then(
                            function (data) {
                                $scope.salesReps = data.content;
                                if ($stateParams.salesRep != null && $scope.salesRep != undefined) {
                                    angular.forEach($scope.salesReps, function (salesRep) {
                                        if (salesRep.id == $stateParams.salesRep) {
                                            $scope.customer.salesRep = salesRep;
                                        }
                                    });
                                }
                            }
                        )
                    }

                    $scope.validateCustmerName = function () {

                        var iChars = "!#%^$*+=[]{}|<>?";
                        var value = $scope.customer.name;
                        var valid = true;
                        for (var i = 0; i < value.length; i++) {
                            if (iChars.indexOf(value[i]) != -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                            $rootScope.showErrorMessage("Customer name does't have any special characters such as(!#%^$*+=[]{}|<>?)");
                            return valid;
                        }
                        else {
                            return valid;
                        }
                    };

                    $scope.validateEmail = function (emailId) {
                        atpos = emailId.indexOf("@");
                        dotpos = emailId.lastIndexOf(".");
                        var valid = true;
                        if (emailId != null && emailId != undefined && emailId != "") {
                            if (atpos < 1 || ( dotpos - atpos < 2 )) {
                                $rootScope.showErrorMessage("Please Enter Valid Email Address");
                                valid = false;
                            }
                            else {
                                valid = true;
                            }
                        }
                        return valid;
                    }

                    $scope.validateCustomernameWithRegion = function (customer) {
                        var valid = false;
                        customerFactory.getCustomerByCustomernameandRegion(customer.name,
                            customer.salesRegion.name).then(
                            function (data) {
                                valid = data;
                                if (valid) {
                                    $rootScope.showErrorMessage(customer.name + " is already created in " + customer.salesRegion.name);

                                }
                            }
                        )
                        return valid;
                    };

                    $scope.validatePhoneNumber = function (phoneNumber) {
                        var nChars = "0123456789 +-_";
                        var value = phoneNumber;
                        var valid = true;
                        for (var i = 0; i < value.length; i++) {
                            if (nChars.indexOf(value[i]) == -1) {
                                valid = false;
                                break;
                            }
                        }
                        if (!valid) {
                            $rootScope.showErrorMessage("Please Enter valid Phone Number");
                            return valid;
                        }
                        else {
                            return valid;
                        }
                    }

                    function validateCustomerData() {
                        var valid = true;
                        if ($scope.customer.name == null || $scope.customer.name == "") {
                            $scope.error.errorMessage = "Customer name cannot be empty";
                            valid = false;
                        }
                        else if ($scope.customer.customerType == null) {
                            $scope.error.errorMessage = "Customer type cannot be empty";
                            valid = false;
                        }
                        else if ($scope.customer.salesRegion == null) {
                            $scope.error.errorMessage = "Region cannot be empty";
                            valid = false;
                        }
                        else if ($scope.customer.customerAddresses[0].addressText != null &&
                            $scope.customer.customerAddresses[0].addressText != "") {

                            if ($scope.customer.customerAddresses[0].city == null ||
                                $scope.customer.customerAddresses[0].city == "") {
                                $scope.error.errorMessage = "Address was specified but no city was entered";
                                valid = false;
                            }
                            else if ($scope.customer.customerAddresses[0].state == null) {
                                $scope.error.errorMessage = "Address was specified but no state was selected";
                                valid = false;
                            }

                            if ($scope.customer.salesRegion.state != null &&
                                $scope.customer.customerAddresses[0].state != null &&
                                $scope.customer.salesRegion.state.name != $scope.customer.customerAddresses[0].state.name) {

                                $scope.error.errorMessage = "Selected region does not belong to the selected state";
                                valid = false;
                            }
                        }
                        if (!$scope.validateCustmerName()) {
                            $scope.error.errorMessage = "Customer name does't have special characters such as(!#%^$*+=[]{}|<>?)";
                            valid = false;
                        }
                        if (!$scope.validatePhoneNumber($scope.customer.officePhone)) {
                            $scope.error.errorMessage = "Please Enter Valid office Phone Number";
                            valid = false;
                        }
                        if (!$scope.validatePhoneNumber($scope.customer.contactPerson.phoneMobile)) {
                            $scope.error.errorMessage = "Please Enter Valid contact person Phone Number";
                            valid = false;
                        }
                        if (!$scope.validateEmail($scope.customer.officeEmail)) {
                            $scope.error.errorMessage = "Please Enter Valid office Email Address";
                            valid = false;
                        }
                        if (!$scope.validateEmail($scope.customer.contactPerson.email)) {
                            $scope.error.errorMessage = "Please Enter Valid contact person Email Address";
                            valid = false;
                        }
                        return valid;
                    }

                    (function () {
                        loadCustomerTypes();
                        loadRegions();
                        loadSalesReps();

                        if ($stateParams.salesRep != null && $stateParams.salesRep != undefined) {
                            $scope.salesRep = $stateParams.salesRep;
                        }
                    })();

                }
            ]
        )
        ;
    }
);