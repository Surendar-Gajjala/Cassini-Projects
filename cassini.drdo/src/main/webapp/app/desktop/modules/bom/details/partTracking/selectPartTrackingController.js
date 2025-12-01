/**
 * Created by Nageshreddy on 02-01-2018.
 */
define([
        'app/desktop/modules/bom/bom.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/shared/services/core/listService'
    ],
    function (module) {
        module.controller('SelectPartTrackingController', SelectPartTrackingController);

        function SelectPartTrackingController($scope, $rootScope, LovService, ListService) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;
            vm.partTrackings = null;
            vm.selectedPartTracking = null;

            function loadLovs() {
                ListService.getLists().then(
                    function (data) {
                        vm.partTrackings = data;
                    }
                )
            }

            function assignPartTracking() {
                if (vm.selectedPartTracking != null) {
                    $scope.callback(vm.selectedPartTracking);
                    $rootScope.hideSidePanel('left');
                } else {
                    $rootScope.showWarningMessage("Please select one Part Tracking Process");
                }
            }

            (function () {
                loadLovs();
                $rootScope.$on('select.partTracking', assignPartTracking);
            })();

        }
    }
);
