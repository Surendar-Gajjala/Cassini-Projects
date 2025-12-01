/**
 * Created by swapna on 1/4/18.
 */

define(['app/desktop/modules/pm/pm.module'

    ],
    function (module) {
        module.controller('PmMainController', PmMainController);

        function PmMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {
            //$rootScope.viewInfo.icon =
            $rootScope.viewInfo.title = "Projects";

            var vm = this;

            (function () {

            })();
        }
    }
);