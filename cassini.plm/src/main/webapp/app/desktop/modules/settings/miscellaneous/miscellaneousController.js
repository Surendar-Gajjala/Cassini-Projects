define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/shared/services/core/itemTypeService',
        'app/desktop/modules/settings/miscellaneous/cadSettings/cadSettingsController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/system/systemsettingsController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/application/applicationsettingsController'
    ],
    function (module) {
        module.controller('MiscellaneousController', MiscellaneousController);

        function MiscellaneousController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                         CommonService, MeasurementService, $translate, ItemTypeService) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.selectedMiscellaneous = "systemsettings";

            vm.selectMiscellaneous = selectMiscellaneous;
            function selectMiscellaneous(type) {
                $state.transitionTo('app.settings', {tab: "miscellaneous", tab1: type}, {notify: false});
                vm.selectedMiscellaneous = type;
            }

            (function () {
                selectMiscellaneous(vm.selectedMiscellaneous);
                if ($rootScope.loginPersonDetails.isSuperUser) {
                    $rootScope.isAdminGroup = true;
                }
                $rootScope.hideBusyIndicator();
            })();
        }
    }
)
;