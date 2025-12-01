define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/desktop/modules/directives/timelineDirective'
    ],
    function (module) {
        module.controller('MFRTimelineHistoryController', MFRTimelineHistoryController);

        function MFRTimelineHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, $uibModal, $translate,
                                              CommonService) {
            var vm = this;

            vm.loading = true;
            vm.mfrId = $stateParams.manufacturerId;

            (function () {
                $scope.$on('app.mfr.tabactivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);