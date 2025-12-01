/**
 * Created by swapna on 1/16/18.
 */

define(['app/desktop/modules/pm/pm.module',
        'app/desktop/modules/directives/deliverablesDirective'

    ],
    function (module) {
        module.controller('DeliverablesController', DeliverablesController);

        function DeliverablesController($scope, $stateParams) {

            var vm = this;
            vm.projectId = $stateParams.projectId;

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.deliverables') {
                        $scope.$broadcast('app.object.deliverables', {});
                    }
                });
            })();
        }
    }
);