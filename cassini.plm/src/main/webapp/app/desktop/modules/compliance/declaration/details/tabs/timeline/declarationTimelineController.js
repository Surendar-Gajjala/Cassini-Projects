define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('DeclarationTimeLineController', DeclarationTimeLineController);

        function DeclarationTimeLineController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, $application,
                                       $translate) {
            var vm = this;
            vm.declarationId = $stateParams.declarationId;

            var parsed = angular.element("<div></div>");


            (function () {

                $scope.$on('app.declaration.tabActivated', function (event, data) {
                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }
                });

            })();
        }
    }
);