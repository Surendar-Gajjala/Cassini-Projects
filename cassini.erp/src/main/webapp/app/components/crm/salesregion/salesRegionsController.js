define(['app/app.modules',
        'app/components/crm/salesregion/salesRegionFactory',
        'app/components/common/commonFactory',
        'app/components/crm/salesrep/salesRepFactory',
        'app/shared/directives/tableDirectives'
    ],
    function ($app) {
        $app.controller('SalesRegionsController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'salesRegionFactory', 'commonFactory', 'salesRepFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          salesRegionFactory, commonFactory, salesRepFactory) {

                    $rootScope.iconClass = "fa flaticon-global42";
                    $rootScope.viewTitle = "Sales Regions";

                    $scope.pageable = {
                        page: 1,
                        size: 10
                    };
                    $scope.criteria = {
                        name: null,
                        district: null,
                        state: null,
                        country: null,
                        salesRep: null
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

                    $scope.pagedResults = $scope.emptyPagedResults;

                    $scope.newSalesRegion = {
                        editMode: true,
                        showValues: false,
                        name: "",
                        district: "",
                        country: {},
                        state: {},
                        countryAndStates: {}
                    };
                    $scope.countriesAndStates = [];
                    $scope.countries = [];
                    $scope.states = [];
                    $scope.loading = true;
                    $scope.salesReps = [];
                    $scope.showFilters = true;
                    $scope.filterButtonText = "Show Filters";

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('salesregions-view-tb');
                    });

                    $scope.pageChanged = function () {
                        $scope.loadSalesRegions();
                    };

                    $scope.acceptChanges = function (salesRegion) {
                        salesRegion.editMode = false;

                        $timeout(function () {
                            salesRegion.showValues = true;
                        }, 500);

                        $scope.saveSalesRegion(salesRegion);
                    };

                    $scope.showEditMode = function (salesRegion) {
                        salesRegion.editMode = true;
                        salesRegion.showValues = false;
                    };

                    $scope.removeItem = function ($index) {
                        $scope.pagedResults.content.splice($index, 1);
                    }

                    $scope.hideEditMode = function (salesRegion) {
                        salesRegion.editMode = false;

                        $timeout(function () {
                            salesRegion.showValues = true;
                        }, 500);
                    };

                    $rootScope.addSalesRegion = function () {
                        var region = angular.copy($scope.newSalesRegion);
                        if ($scope.countriesAndStates.length > 0) {
                            var item = $scope.countriesAndStates[0];
                            region.country = item.country;
                            region.state = item.states[0];
                            region.countryAndStates = item;
                        }

                        $scope.pagedResults.content.unshift(region);
                    };

                    $scope.saveSalesRegion = function (salesRegion) {
                        salesRegionFactory.saveSalesRegion(salesRegion).then(
                            function (data) {
                                salesRegion = data;
                            },
                            function (error) {
                                console.error(error);
                            }
                        );
                    };

                    commonFactory.getCountriesAndStates().then(
                        function (data) {
                            $scope.countriesAndStates = data;
                            angular.forEach($scope.countriesAndStates, function (item) {
                                $scope.countries.push(item.country);
                            });
                            $scope.states = $scope.countriesAndStates[0].states;
                            $scope.loadSalesRegions();
                        }
                    );

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
                            }
                        );
                    }

                    $scope.applyCriteria = function () {
                        $scope.pageable.page = 1;
                        $scope.loadSalesRegions();
                    };


                    $scope.resetCriteria = function () {
                        $scope.criteria = {
                            name: null,
                            district: null,
                            state: null,
                            country: null,
                            salesRep: null
                        };

                        $scope.pageable.page = 1;

                        $scope.loadSalesRegions();
                    };


                    $scope.loadSalesRegions = function () {
                        salesRegionFactory.getSalesRegions(getCriteria(), $scope.pageable).then(
                            function (data) {
                                angular.forEach(data.content, function (region) {
                                    region.editMode = false;
                                    region.showValues = true;

                                    setCountryAndStates(region);
                                });
                                $scope.pagedResults = data;
                                $scope.loading = false;
                            }
                        );
                    };


                    function setCountryAndStates(region) {
                        angular.forEach($scope.countriesAndStates, function (item) {
                            if (item.country.id == region.country.id) {
                                region.country = item.country;
                                region.countryAndStates = item;

                                angular.forEach(item.states, function (state) {
                                    if (region.state.id == state.id) {
                                        region.state = state;
                                    }
                                });
                            }
                        });
                    }

                    function getCriteria() {
                        var criteria = {
                            name: "",
                            district: "",
                            state: "",
                            country: "",
                            salesRep: ""
                        };

                        if ($scope.criteria.name != null) {
                            criteria.name = $scope.criteria.name;
                        }

                        if ($scope.criteria.district != null) {
                            criteria.district = $scope.criteria.district;
                        }

                        if ($scope.criteria.state != null) {
                            criteria.state = $scope.criteria.state.name;
                        }

                        if ($scope.criteria.country != null) {
                            criteria.country = $scope.criteria.country.name;
                        }

                        if ($scope.criteria.salesRep != null) {
                            criteria.salesRep = $scope.criteria.salesRep.firstName;
                        }

                        return criteria;

                    }


                    loadSalesReps();

                }
            ]
        );
    }
);