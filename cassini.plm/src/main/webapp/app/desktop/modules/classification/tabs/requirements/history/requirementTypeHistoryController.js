define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/ecoService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'
    ],
    function (module) {
        module.controller('RequirementTypeHistoryController', RequirementTypeHistoryController);

        function RequirementTypeHistoryController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                                ECOService, CommonService, $translate, ItemTypeService, AutonumberService, ClassificationService, $window,
                                              LovService, MeasurementService) {

            (function () {
                $scope.$on('app.rmType.tabactivated', function (event, data) {
                    if (data.tabId == 'details.history') {
                        $scope.$broadcast('app.object.timeline', {});

                    }
                })
            })();

        }
    });