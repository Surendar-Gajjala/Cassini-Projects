define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',

    ],
    function (module) {
        module.controller('ManpowerRelatedItemController', ManpowerRelatedItemController);

        function ManpowerRelatedItemController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,
                                                 MESObjectTypeService) {
            var vm = this;
            vm.loading = true;



            (function () {
                $scope.$on('app.manpower.tabActivated', function (event, args) {
                    if (args.tabId == 'details.relatedItem') {

                    }
                });
            })();
        }
    }
);