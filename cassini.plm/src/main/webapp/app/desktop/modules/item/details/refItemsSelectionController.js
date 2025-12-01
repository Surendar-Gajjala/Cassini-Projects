/**
 * Created by annap on 01/06/2016.
 */
define(['app/desktop/modules/item/item.module'
    ],
    function (module) {
        module.controller('RefItemsSelectionController', RefItemsSelectionController);

        function RefItemsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModalInstance) {
            var vm = this;
            vm.cancel = cancel;

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {

            })();
        }
    }
);