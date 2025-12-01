define(['app/desktop/modules/vaults/vault.module'
    ],
    function (module) {
        module.controller('VaultController', VaultController);

        function VaultController ($scope, $rootScope, $timeout, $interval, $state, $cookies) {

            if ($application.homeLoaded == false) {
                return;
            }

            (function() {

            })();
        }
    }
);