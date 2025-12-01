/**
 * Created by Nageshreddy on 03-12-2018.
 */
define(
    [
        'app/desktop/modules/planning/planning.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('PlanningMainController', PlanningMainController);

        function PlanningMainController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                        DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-edit";
            $rootScope.viewInfo.title = "Planning";

            var vm = this;

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                });
            })();
        }
    }
);