define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/mesObjectTypeDirective'
    ],
    function (module) {

        module.controller('NewMaintenanceAndRepairController', NewMaintenanceAndRepairController);

        function NewMaintenanceAndRepairController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.onSelectType = onSelectType;
            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {

                }
            }


            (function () {
                $rootScope.$on('app.maintenanceAndRepair.new');
            })();
        }
    }
);