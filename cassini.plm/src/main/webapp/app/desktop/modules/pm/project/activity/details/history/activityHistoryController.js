define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/directives/timelineDirective'

    ],
    function (module) {
        module.controller('ActivityHistoryController', ActivityHistoryController);

        function ActivityHistoryController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, $uibModal, $translate,
                                          CommonService) {
            var vm = this;

            vm.loading = true;
            vm.activityId = $stateParams.activityId;

            (function () {
                $scope.$on('app.activity.tabActivated', function (event, data) {

                    if (data.tabId == 'details.activityHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();
        }
    }
);