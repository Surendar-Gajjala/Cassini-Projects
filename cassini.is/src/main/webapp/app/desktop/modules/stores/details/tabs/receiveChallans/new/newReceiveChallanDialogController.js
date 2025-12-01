/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module'

    ],
    function (module) {
        module.controller('NewReceiveChallanController', NewReceiveChallanController);

        function NewReceiveChallanController($scope, $rootScope, $window, $state) {
            var vm = this;

            function back() {
                $window.history.back();
            }

            (function () {

            })();
        }
    }
)
;