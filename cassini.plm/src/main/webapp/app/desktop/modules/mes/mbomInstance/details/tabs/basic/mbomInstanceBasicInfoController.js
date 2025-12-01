define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('MBOMInstanceBasicInfoController', MBOMInstanceBasicInfoController);

        function MBOMInstanceBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                                 $translate, MBOMInstanceService) {
            var vm = this;

            vm.loading = true;
            vm.mbomInstanceId = $stateParams.mbomInstanceId;
            var parsed = angular.element("<div></div>");
            var updatedSuccessMsg = parsed.html($translate.instant("MBOM_UPDATED_MESSAGE")).html();
            var mbomNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();

            function loadMBOMInstanceDetails() {
                MBOMInstanceService.getMBOMInstance(vm.mbomInstanceId).then(
                    function (data) {
                        vm.mbomInstance = data;
                        $scope.name = vm.mbomInstance.name;
                        $rootScope.mbomInstance = data;
                        CommonService.getPersonReferences([vm.mbomInstance], 'createdBy');
                        CommonService.getPersonReferences([vm.mbomInstance], 'modifiedBy');
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

            vm.showItem = showItem;
            function showItem() {
                $state.go('app.items.details', {itemId: vm.mbomRevision.itemRevision, tab: 'details.basic'});
            }

            vm.updateMBOMInstance = updateMBOMInstance;
            function updateMBOMInstance() {
                if (validate()) {
                    $rootScope.showBusyIndicator();
                    MBOMInstanceService.updateMBOMInstance(vm.mbomInstance).then(
                        function (data) {
                            vm.mbomInstance = data;
                            $scope.name = vm.mbomInstance.name;
                            $rootScope.mbomInstance = data;
                            CommonService.getPersonReferences([vm.mbomInstance], 'createdBy');
                            CommonService.getPersonReferences([vm.mbomInstance], 'modifiedBy');
                            $rootScope.showSuccessMessage(updatedSuccessMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showSuccessMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMBOMInstanceDetails();
                }
            }

            function validate() {
                var valid = true;
                /*if (vm.mbomInstance.name == null || vm.mbomInstance.name == undefined ||
                 vm.mbomInstance.name == "") {
                 $rootScope.showWarningMessage(mbomNameValidation);
                 valid = false;
                 }*/

                return valid;
            }

            vm.showMBOM = showMBOM;
            function showMBOM() {
                $state.go('app.mes.mbom.details', {mbomId: vm.mbomInstance.mbomRevision, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.mbomInstance.tabActivated', function (event, data) {
                    loadMBOMInstanceDetails();
                });
            })();

        }
    }
);