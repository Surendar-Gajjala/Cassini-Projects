define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MBOMBasicInfoController', MBOMBasicInfoController);

        function MBOMBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                         $translate, MBOMService) {
            var vm = this;

            vm.loading = true;
            vm.mbomId = $stateParams.mbomId;
            var parsed = angular.element("<div></div>");
            var updatedSuccessMsg = parsed.html($translate.instant("MBOM_UPDATED_MESSAGE")).html();
            var mbomNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();

            $rootScope.loadMBOMDetails = loadMBOMDetails;
            function loadMBOMDetails() {
                MBOMService.getMBOMRevision(vm.mbomId).then(
                    function (data) {
                        vm.mbomRevision = data;
                        $rootScope.mbomRevision = data;
                        MBOMService.getMBOM(vm.mbomRevision.master).then(
                            function (data) {
                                vm.mbom = data;
                                $scope.name = vm.mbom.name;
                                $rootScope.mbom = data;
                                CommonService.getPersonReferences([vm.mbom], 'createdBy');
                                CommonService.getPersonReferences([vm.mbom], 'modifiedBy');
                                $timeout(function () {
                                    $scope.$broadcast('app.attributes.tabActivated', {});
                                }, 1000);
                                vm.loading = false;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                )
            }

            vm.showItem = showItem;
            function showItem() {
                $state.go('app.items.details', {itemId: vm.mbomRevision.itemRevision, tab: 'details.basic'});
            }

            vm.updateMBOM = updateMBOM;
            function updateMBOM() {
                if (validate()) {
                    $rootScope.showBusyIndicator();
                    MBOMService.updateMBOM(vm.mbom).then(
                        function (data) {
                            vm.mbom = data;
                            $scope.name = vm.mbom.name;
                            $rootScope.mbom = data;
                            CommonService.getPersonReferences([vm.mbom], 'createdBy');
                            CommonService.getPersonReferences([vm.mbom], 'modifiedBy');
                            $rootScope.showSuccessMessage(updatedSuccessMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showSuccessMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMBOMDetails();
                }
            }

            vm.showMBOMRevisionHistory = showMBOMRevisionHistory;
            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");

            function showMBOMRevisionHistory() {
                var options = {
                    title: vm.mbom.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: vm.mbomRevision.master,
                        revisionHistoryType: "MBOM"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            function validate() {
                var valid = true;
                if (vm.mbom.name == null || vm.mbom.name == undefined ||
                    vm.mbom.name == "") {
                    $rootScope.showWarningMessage(mbomNameValidation);
                    valid = false;
                }

                return valid;
            }

            function resizeView() {
                $timeout(function () {
                    $('#mbom-basic-details').height($('.tab-pane').outerHeight());
                }, 500);
            }

            (function () {
                $scope.$on('app.mbom.tabActivated', function (event, data) {
                    resizeView();
                    loadMBOMDetails();
                });
            })();

        }
    }
);