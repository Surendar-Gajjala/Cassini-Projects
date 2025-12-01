/**
 * Created by swapna on 19/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/roadChallanService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'

    ],
    function (module) {
        module.controller('RoadChallanBasicController', RoadChallanBasicController);

        function RoadChallanBasicController($scope, $rootScope, $window, $state, $stateParams, RoadChallanService) {
            var vm = this;

            vm.roadChallan = null;
            vm.updateRoadChallan = updateRoadChallan;
            vm.loading = false;

            function loadRoadChallan() {
                vm.loading = true;
                RoadChallanService.getRoadChallan($rootScope.storeId, $stateParams.roadchallanId).then(
                    function (data) {
                        vm.loading = false;
                        vm.roadChallan = data;
                        $rootScope.viewInfo.title = "RoadChallan : " + vm.roadChallan.chalanNumber;
                    }
                )
            }

            function updateRoadChallan() {
                RoadChallanService.updateRoadChallan($rootScope.storeId, vm.roadChallan).then(
                    function (data) {
                        vm.roadChallan = data;
                        $rootScope.showSuccessMessage(vm.roadChallan.chalanNumber + ": updated successfully");
                    }
                )
            }

            function back() {
                $window.history.back();
            }

            (function () {
                loadRoadChallan();
            })();
        }

    }
)
;