define(
    [
        'app/phone/modules/accomm/accomm.module',
        'app/shared/services/accommodationService',
        'app/phone/modules/accomm/all/assignBedController',
        'app/phone/modules/accomm/all/assignedBedsController'
    ],
    function (module) {
        module.controller('AllAccommodationsController', AllAccommodationsController);

        function AllAccommodationsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $mdDialog,
                            AccommodationService) {

            $rootScope.viewName = "Accommodation";
            $rootScope.backgroundColor = "#607D8B";

            var vm = this;
            vm.counts = {
                accommodations: 0,
                suites: 0,
                beds: 0,
                occupiedBeds: 0,
                availableBeds: 0
            };
            vm.accommodations = [];
            vm.toggleAccommodation = toggleAccommodation;
            vm.assignBed = assignBed;
            vm.showBedAssignments = showBedAssignments;


            function loadCounts() {
                AccommodationService.getCounts().then (
                    function(data) {
                        angular.forEach(data, function(item) {
                            if(item.key == 'accommodations') {
                                vm.counts.accommodations = item.number;
                            }
                            else if(item.key == 'suites') {
                                vm.counts.suites = item.number;
                            }
                            else if(item.key == 'beds') {
                                vm.counts.beds = item.number;
                            }
                        });
                    }
                );

                AccommodationService.getBedCounts().then(
                    function(data) {
                        angular.forEach(data, function(item) {
                            if(item.key == 'occupied') {
                                vm.counts.occupiedBeds = item.number;
                            }
                            else if(item.key == 'available') {
                                vm.counts.availableBeds = item.number;
                            }
                        });
                    }
                );
            }

            function toggleAccommodation(accommodation) {
                accommodation.showSuites = !accommodation.showSuites;
                if(accommodation.showSuites == true && accommodation.suitesLoaded == false) {
                    accommodation.loading = true;
                    AccommodationService.getSuitsByAccommodation(accommodation.id).then(
                        function(data) {
                            accommodation.suites = data;
                            accommodation.suitesLoaded = true;
                            accommodation.loading = false;

                            angular.forEach(accommodation.suites, function(suite) {
                                suite.occupied = 0;
                                suite.available = 0;

                                AccommodationService.getSuiteCounts(suite.suitId).then(
                                    function(data) {
                                        angular.forEach(data, function(item) {
                                            if(item.key == 'occupied') {
                                                suite.occupied = item.number;
                                            }
                                            else if(item.key == 'available') {
                                                suite.available = item.number;
                                            }
                                        });
                                    }
                                )
                            });
                        }
                    )
                }
            }

            function loadAccommodations() {
                var pageable = {
                    page: 0,
                    size: 1000
                };

                AccommodationService.getAllAccommodations(pageable).then(
                    function(data) {
                        vm.accommodations = data.content;
                        angular.forEach(vm.accommodations, function(accomm) {
                            accomm.showSuites = false;
                            accomm.suites = [];
                            accomm.suitesLoaded = false;
                        })
                    }
                )
            }


            function assignBed(suite, count) {
                if(count == 0) {
                    $rootScope.showMessage("Assign Bed", "There are no beds available in this suite.");
                }
                else {
                    $mdDialog.show({
                        controller: 'AssignBedController',
                        templateUrl: 'app/phone/modules/accomm/all/assignBedDialog.jsp',
                        parent: angular.element(document.body),
                        targetEvent: null,
                        clickOutsideToClose: true,
                        locals: {
                            suite: suite
                        }
                    }).then(
                        function (bed) {
                            if (bed != null && bed != undefined) {
                                suite.occupied = suite.occupied + 1;
                                suite.available = suite.available - 1;
                            }
                        }
                    );
                }
            }

            function showBedAssignments(suite, count) {
                if(count == 0) {
                    $rootScope.showMessage("Assigned Beds", "No beds are assigned in this suite.");
                }
                else {
                    $mdDialog.show({
                        controller: 'AssignedBedsController',
                        templateUrl: 'app/phone/modules/accomm/all/assignedBedsDialog.jsp',
                        parent: angular.element(document.body),
                        targetEvent: null,
                        clickOutsideToClose: true,
                        locals: {
                            suite: suite
                        }
                    }).then(
                        function (result) {

                        }
                    );
                }
            }


            (function () {
                loadCounts();
                loadAccommodations();
            })();
        }
    }
);