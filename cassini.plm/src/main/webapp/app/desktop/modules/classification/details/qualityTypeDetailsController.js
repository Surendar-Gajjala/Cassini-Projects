define(
    ['app/desktop/modules/classification/classification.module',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/tabs/quality/basic/qualityTypeBasicController',
        'app/desktop/modules/classification/tabs/quality/history/qualityTypeHistoryController',
        'app/desktop/modules/classification/tabs/quality/attributes/qualityTypeAttributesController',
        'app/shared/services/core/qualityTypeService'
    ],
    function (module) {
        module.controller('QualityTypeDetailsController', QualityTypeDetailsController);

        function QualityTypeDetailsController($scope, $rootScope, $timeout, $window, $state, $stateParams, $cookies, $translate,
                                              ClassificationService, ItemTypeService, AutonumberService,
                                              CommonService, DialogService, LovService, QualityTypeService) {
            var vm = this;


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: "Basic",
                    template: 'app/desktop/modules/classification/tabs/quality/basic/qualityTypeBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/classification/tabs/quality/attributes/qualityTypeAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                history: {
                    id: 'details.history',
                    heading: 'History',
                    template: 'app/desktop/modules/classification/tabs/quality/history/qualityTypeHistoryView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            vm.qualityDetailsTabActivated = qualityDetailsTabActivated;
            function qualityDetailsTabActivated(tabId) {

                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    $rootScope.selectedTab = tab;
                    tab.activated = true;
                    $scope.$broadcast('app.qualityType.tabactivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function resizeAttributesView() {
                var height = $('.view-content').outerHeight();
                $('.tab-content').height(height - 70);
                $('.tab-pane').height(height - 70);
                var tabPane = $('.tab-pane').outerHeight();
                var attributeButtons = $('#action-buttons').outerHeight();
                $("#attributes-table").height(tabPane - (attributeButtons));

            }

            function qualityTypeDetailsSelected(event, args) {
                $rootScope.selectedQualityType = args.typeObject;
                qualityDetailsTabActivated(vm.tabs.basic.id);
            }

            function loadBasicInfo() {
                if ($rootScope.selectedQualityType != null && $rootScope.selectedQualityType != undefined && $rootScope.selectedQualityType.id != undefined && $rootScope.selectedQualityType.qualityType != undefined) {
                    QualityTypeService.getQualityTypeByType($rootScope.selectedQualityType.id, $rootScope.selectedQualityType.qualityType).then(
                        function (data) {
                            $rootScope.qualityType = data;
                            loadQualityTypeObjects();
                            $timeout(function () {
                                resizeAttributesView();
                            }, 500);
                            $scope.$evalAsync();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            $rootScope.qualityTypeObjectsExist = false;
            function loadQualityTypeObjects() {
                if ($rootScope.selectedQualityType != null && $rootScope.selectedQualityType != undefined && $rootScope.selectedQualityType.id != undefined && $rootScope.selectedQualityType.qualityType != undefined) {
                    QualityTypeService.getObjectsByType($rootScope.selectedQualityType.id, $rootScope.selectedQualityType.qualityType).then(
                        function (data) {
                            if (data > 0) {
                                $rootScope.qualityTypeObjectsExist = true;
                            } else {
                                $rootScope.qualityTypeObjectsExist = false;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.qualityType.selected', qualityTypeDetailsSelected);
                $timeout(function () {
                    loadBasicInfo();
                }, 500)
                //}
            })();
        }
    }
);
