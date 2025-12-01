define(
    [
        'app/phone/modules/accomm/accomm.module',
        'app/shared/services/accommodationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('AssignBedController', AssignBedController);

        function AssignBedController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $mdDialog,
                                     AccommodationService, CommonService, suite) {
            var vm = this;

            $scope.beds = [];
            $scope.persons = [];

            $scope.selectedBed = null;
            $scope.selectedPerson = null;


            $scope.onCancel = function() {
                $mdDialog.cancel();
            };

            $scope.onOk = function() {
                if($scope.selectedBed != null && $scope.selectedPerson != null) {
                    $scope.selectedBed.assignedTo = $scope.selectedPerson.id;
                    AccommodationService.updateBed(suite.accommodation, suite.suitId, $scope.selectedBed).then (
                        function(bed) {
                            $mdDialog.hide(bed);
                        }
                    )
                }
            };

            function loadBeds() {
                AccommodationService.getSuiteBedsAvailable(suite.suitId).then(
                    function(data) {
                        $scope.beds = data;
                    }
                )
            }

            function loadPersons() {
                AccommodationService.getOccupiedBeds().then(
                    function (beds) {
                        var map = new Hashtable();
                        angular.forEach(beds, function(bed) {
                            map.put(bed.assignedTo, bed);
                        });

                        CommonService.getAllPersonsByPersonType(1).then(
                            function(persons) {
                                angular.forEach(persons, function(person) {
                                    if(map.get(person.id) == null && person.firstName != "Administrator") {
                                        $scope.persons.push(person);
                                    }
                                });

                                $rootScope.sortPersons($scope.persons);
                            }
                        )
                    }
                );
            }


            (function () {
                loadBeds();
                loadPersons();
            })();
        }
    }
);