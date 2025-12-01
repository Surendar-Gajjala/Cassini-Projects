define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/sparePartsService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('SparePartBasicInfoController', SparePartBasicInfoController);

        function SparePartBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, ECOService, CommonService,
                                              SparePartService, $translate, LoginService) {
            var vm = this;
            vm.loading = true;
            vm.sparePartId = $stateParams.sparePartId;
            vm.sparePart = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            vm.updateSparePart = updateSparePart;

            function loadSparePartBasicDetails() {
                vm.loading = true;
                if (vm.sparePartId != null && vm.sparePartId != undefined) {
                    SparePartService.getSparePart(vm.sparePartId).then(
                        function (data) {
                            vm.sparePart = data;
                            $rootScope.sparePart = vm.sparePart;
                            $scope.name = vm.sparePart.name;
                            CommonService.getPersonReferences([vm.sparePart], 'modifiedBy');
                            CommonService.getPersonReferences([vm.sparePart], 'createdBy');
                            if (vm.sparePart.description != null && vm.sparePart.description != undefined) {
                                vm.sparePart.descriptionHtml = $sce.trustAsHtml(vm.sparePart.description.replace(/(?:\ r\n|\r|\n)/g, '<br>'));
                            }
                            $timeout(function () {
                                $scope.$broadcast('app.attributes.tabActivated', {});
                            }, 1000);
                            vm.editStatus = false;
                            vm.loading = false;
                            $rootScope.viewInfo.title = $translate.instant("SPAREPART_DETAILS");
                            $rootScope.viewInfo.description = vm.sparePart.number + " , " + vm.sparePart.name;
                            $scope.$evalAsync();
                        }, function (error) {
                              $rootScope.showErrorMessage(error.message);
                              $rootScope.hideBusyIndicator();
                         }
                    )
                }
            }


            var itemNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var sparePartUpdated = parsed.html($translate.instant("SPAREPART_UPDATED_MESSAGE")).html();


            function validateSparePart() {
                var valid = true;
                if (vm.sparePart.name == null || vm.sparePart.name == ""
                    || vm.sparePart.name == undefined) {
                    valid = false;
                    vm.sparePart.name = $scope.name;
                    $rootScope.showWarningMessage(itemNameValidation);

                }


                return valid;
            }

            function updateSparePart() {
                if (validateSparePart()) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SparePartService.updateSparePart(vm.sparePart).then(
                        function (data) {
                            loadSparePartBasicDetails();
                            vm.editMaintenance = false;
                            vm.editStatus = false;
                            $rootScope.showSuccessMessage(sparePartUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            (function () {
                $scope.$on('app.sparePart.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadSparePartBasicDetails();
                    }
                });

            })();

        }
    }
);