define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/referenceItemsDirectiveController'
    ],

    function (module) {
        module.controller('ActivityReferenceItemsController', ActivityReferenceItemsController);

        function ActivityReferenceItemsController($scope, $rootScope, $timeout, $translate, $window, $stateParams, $state, ProjectService,
                                                  DialogService, ActivityService) {

            var vm = this;
            vm.activityId = $stateParams.activityId;

            (function () {
                $scope.$on('app.activity.tabActivated', function (event, data) {
                    if (data.tabId == 'details.itemReferences') {
                        $scope.$broadcast('app.project.referenceItems', {});
                    }
                })
            })();
        }
    }
);