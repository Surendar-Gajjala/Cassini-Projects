define(
    [
        'app/phone/modules/accomm/accomm.module',
        'app/shared/services/accommodationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('AssignedBedsController', AssignBedController);

        function AssignBedController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $mdDialog,
                                     AccommodationService, CommonService, suite) {
            var vm = this;

            $scope.loading = true;
            $scope.assignedBeds = [];

            $scope.onCancel = function() {
                $mdDialog.cancel();
            };

            $scope.onOk = function() {
                $mdDialog.cancel();
            };


            function loadBedAssignments() {
                AccommodationService.getSuiteBedsOccupied(suite.suitId).then (
                    function(beds) {
                        CommonService.getPersonReferences(beds, 'assignedTo');
                        $scope.assignedBeds = beds;
                        $scope.loading = false;
                    }
                )
            }

            (function () {
                loadBedAssignments();
            })();
        }
    }
);