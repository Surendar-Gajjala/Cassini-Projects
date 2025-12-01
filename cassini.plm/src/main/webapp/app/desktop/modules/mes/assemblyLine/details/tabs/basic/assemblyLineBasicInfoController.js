define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('AssemblyLineBasicInfoController', AssemblyLineBasicInfoController);

        function AssemblyLineBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                                 MESObjectTypeService, AssemblyLineService, MeasurementService, $translate, DialogService) {
            var vm = this;
            vm.loading = true;
            vm.assemblyLineId = $stateParams.assemblyLineId;

            var parsed = angular.element("<div></div>");
            var assemblyLineUpdateMsg = parsed.html($translate.instant("ASSEMBLYLINE_UPDATE_MSG")).html();
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();

            vm.updateAssemblyLine = updateAssemblyLine;

            function updateAssemblyLine() {
                if (validate()) {
                    AssemblyLineService.updateAssemblyLine(vm.assemblyLine).then(
                        function (data) {
                            vm.assemblyLine = data;
                            loadAssemblyLine();
                            vm.loading = false;
                            CommonService.getPersonReferences([vm.assemblyLine], 'createdBy');
                            CommonService.getPersonReferences([vm.assemblyLine], 'modifiedBy');
                            $rootScope.showSuccessMessage(assemblyLineUpdateMsg)
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.assemblyLine.name == null || vm.assemblyLine.name == "" || vm.assemblyLine.name == undefined) {
                    valid = false;
                    vm.assemblyLine.name = $scope.name;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            function loadAssemblyLine() {
                AssemblyLineService.getAssemblyLine(vm.assemblyLineId).then(
                    function (data) {
                        vm.assemblyLine = data;
                        $scope.name = vm.assemblyLine.name;
                        vm.loading = false;
                        $rootScope.viewInfo.description = vm.assemblyLine.number + " , " + vm.assemblyLine.name;
                        $rootScope.viewInfo.title = $translate.instant("MATERIAL_DETAILS");
                        CommonService.getPersonReferences([vm.assemblyLine], 'createdBy');
                        CommonService.getPersonReferences([vm.assemblyLine], 'modifiedBy');
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $scope.$on('app.assemblyLine.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadAssemblyLine();
                    }
                });

            })();

        }
    }
);