/**
 * Created by swapna on 21/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module'
    ],
    function (module) {
        module.controller('ContractMainController', ContractMainController);

        function ContractMainController($scope, $rootScope, $timeout, $state, $cookies) {

            $rootScope.setViewType('APP');
            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);