define(['app/desktop/modules/accommodation/accommodation.module',
        'app/desktop/modules/accommodation/new/newAccommodationController',
        'app/desktop/modules/accommodation/new/newSuitController',
        'app/desktop/modules/accommodation/new/newBedController',
        'app/shared/services/accommodationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/accommodation/all/assignedBedsController',
        'app/desktop/modules/accommodation/all/assignBedController'
    ],
    function (module) {
        module.controller('AccommodationController', AccommodationController);

        function AccommodationController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, AccommodationService, DialogService, CommonService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-office42";
            $rootScope.viewInfo.title = "Accommodation";

            vm.accommadation = null;

            vm.loading = true;
            vm.totalSuites = 0;
            vm.totalBeds = 0;
            vm.createAccommodation = createAccommodation;
            vm.createSuit = createSuit;
            vm.createBed = createBed;
            vm.deleteBed = deleteBed;
            vm.deleteAccSuit = deleteAccSuit;
            vm.deleteAccommodation = deleteAccommodation;
            vm.accommodations = null;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.showAccommodationDetails = showAccommodationDetails;
            vm.showSuitDetails = showSuitDetails;
            vm.loading = true;
            vm.showAccSuits = showAccSuits;
            vm.showAccSuitBeds = showAccSuitBeds;
            vm.hideAccSuitBeds = hideAccSuitBeds;
            vm.hideAccSuits = hideAccSuits;
            vm.showBedAssignments = showBedAssignments;
            vm.assignBed = assignBed;
            vm.filters = {
                searchQuery: null
            }

            var pageable = {
                page: 0,
                size: 20
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.accommodations = angular.copy(pagedResults);

            function showAccSuitBeds(suit) {
                suit.showBeds = true;
            }

            function hideAccSuitBeds(suit) {
                suit.showBeds = false;
            }


            function showBedAssignments(suite, count) {
                if(count == 0) {
                    $rootScope.showSuccessMessage("No beds are assigned in this suite.");
                } else {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/desktop/modules/accommodation/all/assignedBedsDialog.jsp',
                        controller: 'AssignedBedsController as assignedBedsVm',
                        size: 'md',
                        resolve: {
                            suite: suite
                        }
                    });
                    modalInstance.result.then(
                        function (result) {

                        }
                    );
                }
            }


            function assignBed(suite, count) {
                if(count == 0) {
                    $rootScope.showSuccessMessage("There are no beds available in this suite.");

                }
                else {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/desktop/modules/accommodation/all/assignBedDialog.jsp',
                        controller: 'AssignBedController as assignBedVm',
                        size: 'md',
                        resolve: {
                            suite: suite
                        }
                    });
                    modalInstance.result.then(
                        function (bed) {
                            if (bed != null && bed != undefined) {
                                suite.occupied = suite.occupied + 1;
                                suite.available = suite.available - 1;
                            }
                        }
                    );
                }
            }

            function showAccSuits(accommodation) {
                accommodation.showSuites = true;
                if(accommodation.showSuites == true /*&& accommodation.suitesLoaded == false*/) {
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
                                        })
                                    }
                                )
                            });
                        }
                    )
                }
            }

            function hideAccSuits(accommodation) {
                accommodation.showSuites = false;
            }

            function loadAccommodations() {
                vm.totalBeds = 0;
                vm.totalSuites = 0;
                AccommodationService.getAllAccommodations(pageable).then(
                    function (data) {
                        vm.accommodations = data.content;
                        angular.forEach(vm.accommodations, function (accommodation) {
                            accommodation.suites = null;
                            accommodation.showSuites = false;
                            getSuitByAccomedationId(accommodation);
                        })
                    });
                vm.loading = false;
            }

            function deleteAccSuit(suit, accommodation) {
                var options = {
                    title: 'Delete Suite',
                    message: 'Are you sure you want to delete this Suite?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        AccommodationService.deleteSuit(suit.accommodation, suit.suitId).then(
                            function (data) {
                                var index = accommodation.suites.indexOf(suit);
                                accommodation.suites.splice(index, 1);
                                vm.totalSuites--;
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                loadAccommodations();
                            }
                        )
                    }
                });
            }

            function deleteAccommodation(accommodation) {
                var options = {
                    title: 'Delete Accommodation',
                    message: 'Are you sure you want to delete this Accommodation?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        AccommodationService.deleteAccommodation(accommodation.id).then(
                            function (data) {
                                var index = vm.accommodations.indexOf(accommodation);
                                vm.accommodations.splice(index, 1);
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                                loadAccommodations();
                            }
                        )
                    }
                });
            }

            function deleteBed(bed, suite) {
                var options = {
                    title: 'Delete Bed',
                    message: 'Are you sure you want to delete this Bed?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        AccommodationService.deleteBed(bed.suite, bed.bedId).then(
                            function (data) {
                                var index = suite.beds.indexOf(bed);
                                suite.beds.splice(index, 1);
                                vm.totalBeds--;
                                $rootScope.showErrorMessage(" Deleted Successfully!");
                            }
                        )
                    }
                });
            }

            function createAccommodation() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/accommodation/new/newAccommodationView.jsp',
                    controller: 'NewAccommodationController as newAccommodationVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        AccommodationService.createAccommodation(result).then(
                            function (data) {
                                vm.accommadation = data;
                                $rootScope.showSuccessMessage("Accommodation Created Successfully ");
                                loadAccommodations();
                            }
                        )
                    }
                );
            }

            function getSuitByAccomedationId(accommodation) {
                AccommodationService.getSuitsByAccommodation(accommodation.id).then(
                    function (data) {
                        accommodation.suites = data;
                        vm.totalSuites += accommodation.suites.length;
                        angular.forEach(accommodation.suites, function (suit) {
                            suit.beds = [];
                            getbedsBySuit(suit);
                        })
                    });
            }

            function getbedsBySuit(suit) {
                AccommodationService.getBedsBySuit(suit.accommodation, suit.suitId).then(
                    function (data) {
                        suit.beds = data;
                        vm.totalBeds += suit.beds.length;
                        CommonService.getPersonReferences(suit.beds, 'assignedTo');
                    });
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    AccommodationService.freeTextSearch(vm.filters, pageable).then(
                        function (data) {
                            vm.accommodations = data.content;
                            angular.forEach(vm.accommodations, function (accommodation) {
                                accommodation.suites = null;
                            })
                        }
                    )
                } else {
                    resetPage();
                    loadAccommodations();
                }
            }

            function resetPage() {
                pageable.page = 0;
            }

            function nextPage() {
                if (vm.accommodations.last != true) {
                    pageable.page++;

                }
            }

            function previousPage() {
                if (vm.accommodations.first != true) {
                    pageable.page--;
                }
            }

            function createSuit(acc) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/accommodation/new/newSuitView.jsp',
                    controller: 'NewSuitController as newSuitVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        AccommodationService.createSuit(acc.id, result).then(
                            function (data) {
                                var suit = data;
                                acc.suites.unshift(suit);
                                vm.totalSuites++;
                                $rootScope.showSuccessMessage("Suite Created Successfully ");
                            }
                        )
                    }
                );
            }

            function createBed(acc, suite) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/desktop/modules/accommodation/new/newBedView.jsp',
                    controller: 'NewBedController as newBedVm',
                    size: 'md'
                });
                modalInstance.result.then(
                    function (result) {
                        result.assignedTo = result.assignedTo.id;
                        AccommodationService.createBed(acc.id, suite.suitId, result).then(
                            function (data) {
                                var bed = data;
                                CommonService.getPersonReferences([bed], 'assignedTo');
                                suite.beds.unshift(bed);
                                vm.totalBeds++;
                                $rootScope.showSuccessMessage("Bed Created Successfully ");
                            }
                        )
                    }
                );
            }

            function showAccommodationDetails(accommodation) {
                $state.go('app.accommodation.details', {accommodationId: accommodation.id});
            }

            function showSuitDetails(suit) {
                $state.go('app.accommodation.details.suitDetails', {suitId: suit.id});
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadAccommodations();
                }
            })();
        }
    }
);
