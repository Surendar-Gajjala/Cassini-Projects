/**
 * Created by Hello on 10/29/2020.
 */
define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',

    ],
    function (module) {
        module.controller('MachineRelatedItemController', MachineRelatedItemController);

        function MachineRelatedItemController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,
                                                 MESObjectTypeService) {
            var vm = this;
            vm.loading = true;



            (function () {
                $scope.$on('app.material.tabActivated', function (event, args) {
                    if (args.tabId == 'details.whereUsed') {

                    }
                });
            })();
        }
    }
);