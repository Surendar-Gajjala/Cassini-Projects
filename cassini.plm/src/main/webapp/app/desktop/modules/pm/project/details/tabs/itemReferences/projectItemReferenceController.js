define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/referenceItemsDirectiveController'
    ],

    function (module) {
        module.controller('ProjectItemReferenceController', ProjectItemReferenceController);

        function ProjectItemReferenceController($scope, $rootScope, $timeout, $translate, $window, $stateParams, $state, ProjectService,
                                                DialogService) {

            var vm = this;
            vm.projectId = $stateParams.projectId;

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.itemReferences') {
                        $scope.$broadcast('app.project.referenceItems', {});
                    }
                })
            })();
        }
    }
);