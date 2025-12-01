define(
    [
        'app/desktop/modules/change/change.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('MCOTimeLineController', MCOTimeLineController);

        function MCOTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate, CommentsService) {
            var vm = this;

            vm.mcoId = $stateParams.mcoId;

            (function () {
                //if ($application.homeLoaded == true) {
                    $scope.$on('app.mco.tabActivated', function (event, args) {
                        if (args.tabId == 'details.timeLine') {
                            $scope.$broadcast('app.object.timeline', {});
                        }
                    });
                //}
            })();
        }
    }
);