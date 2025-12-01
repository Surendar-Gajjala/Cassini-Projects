define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('MfrPartTimelineHistoryController', MfrPartTimelineHistoryController);

        function MfrPartTimelineHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, $uibModal, $translate,
                                                  CommonService) {
            var vm = this;

            vm.loading = true;
            vm.mfId = $stateParams.manufacturePartId;

            (function () {
                $scope.$on('app.mfrPart.tabactivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);