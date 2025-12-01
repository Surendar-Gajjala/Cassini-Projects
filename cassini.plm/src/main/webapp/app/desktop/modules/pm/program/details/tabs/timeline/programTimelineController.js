define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ProgramTimelineController', ProgramTimelineController);

        function ProgramTimelineController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate) {
            var vm = this;

            vm.loading = true;
            vm.programId = $stateParams.programId;

            (function () {
                $scope.$on('app.program.tabactivated', function (event, data) {

                    if (data.tabId == 'details.timeline') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);