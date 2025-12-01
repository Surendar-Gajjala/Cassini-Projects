define(
    [
        'app/desktop/modules/accommodation/accommodation.module',
        'app/shared/services/accommodationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('AssignedBedsController', AssignBedController);

        function AssignBedController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance,
                                     AccommodationService, CommonService, suite) {
            var vm = this;

            $scope.loading = true;
            $scope.assignedBeds = [];

            $scope.onCancel = function() {
                $uibModalInstance.dismiss('cancel');
            };

            $scope.onOk = function() {
                $uibModalInstance.dismiss('cancel');
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