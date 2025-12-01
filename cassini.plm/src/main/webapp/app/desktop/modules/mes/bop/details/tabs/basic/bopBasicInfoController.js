define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/bopService',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/basicAttributeDetailsDirectiveController',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('BOPBasicInfoController', BOPBasicInfoController);

        function BOPBasicInfoController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $cookies, CommonService,
                                        $translate, BOPService) {
            var vm = this;
            vm.loading = true;
            vm.bopId = $stateParams.bopId;
            vm.bop = null;
            $scope.name = null;
            vm.persons = [];
            var parsed = angular.element("<div></div>");
            var updatedSuccessMsg = parsed.html($translate.instant("BOP_UPDATED_MESSAGE")).html();
            // var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var bopNameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();

            $rootScope.loadBopBasicDetails = loadBopBasicDetails;
            function loadBopBasicDetails() {
                vm.loading = true;
                if (vm.bopId != null && vm.bopId != undefined) {
                    BOPService.getBOPRevision(vm.bopId).then(
                        function (data) {
                            vm.bopRevision = data;
                            $rootScope.bopRevision = data;
                            BOPService.getBOP(vm.bopRevision.master).then(
                                function (data) {
                                    vm.bop = data;
                                    $scope.name = vm.bop.name;
                                    $rootScope.bop = data;
                                    CommonService.getMultiplePersonReferences([vm.bop], ['createdBy', 'modifiedBy']);
                                    $timeout(function () {
                                        $scope.$broadcast('app.attributes.tabActivated', {});
                                    }, 1000);
                                    vm.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    );
                }
            }

            vm.updateBOP = updateBOP;
            function updateBOP() {
                if (validate()) {
                    $rootScope.showBusyIndicator();
                    BOPService.updateBOP(vm.bop).then(
                        function (data) {
                            vm.bop = data;
                            $scope.name = vm.bop.name;
                            $rootScope.bop = data;
                            CommonService.getMultiplePersonReferences([vm.bop], ['createdBy', 'modifiedBy']);
                            $rootScope.showSuccessMessage(updatedSuccessMsg);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.bop.name = $scope.name;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else{
                    loadBopBasicDetails();
                }


            }

            function validate() {
                var valid = true;
                if (vm.bop.name == null || vm.bop.name == undefined ||
                    vm.bop.name == "") {
                    $rootScope.showWarningMessage(bopNameValidation);
                    valid = false;
                }



                return valid;
            }

            vm.showMBOM = showMBOM;
            function showMBOM() {
                $state.go('app.mes.mbom.details', {mbomId: vm.bopRevision.mbomRevision, tab: 'details.basic'});
            }


            (function () {
                $scope.$on('app.bop.tabActivated', function (event, data) {
                    if (data.tabId == 'details.basic') {
                        loadBopBasicDetails();
                    }
                });

            })();

        }
    }
);