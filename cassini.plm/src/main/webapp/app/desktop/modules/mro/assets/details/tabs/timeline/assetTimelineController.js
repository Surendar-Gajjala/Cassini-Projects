define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('AssetTimeLineController', AssetTimeLineController);

        function AssetTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.assetId = $stateParams.assetId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.asset.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);