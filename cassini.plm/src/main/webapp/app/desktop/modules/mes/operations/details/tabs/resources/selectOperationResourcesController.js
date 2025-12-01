define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/materialService',
        'app/shared/services/core/manpowerService',
        'app/shared/services/core/operationService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/desktop/modules/directives/materialTypeDirective',
        'app/desktop/modules/directives/manpowerTypeDirective',
        'app/desktop/modules/directives/machinesTypeDirective',
        'app/desktop/modules/directives/equipmentsTypeDirective',
        'app/desktop/modules/directives/instrumentsTypeDirective',
        'app/desktop/modules/directives/toolsTypeDirective',
        'app/desktop/modules/directives/mesResourceTypeDirective'
    ],
    function (module) {
        module.controller('SelectOperationResourcesController', SelectOperationResourcesController);

        function SelectOperationResourcesController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies,
                                                    $translate, MaterialService, ManpowerService, OperationService) {
            var vm = this;

            vm.loading = true;
            vm.selectedTypes = ['MACHINES', 'EQUIPMENTS', 'INSTRUMENTS', 'TOOLS', 'JIGS_FIXTURES', 'MATERIALS', 'MANPOWER'];
            vm.selectedManpowers = [];

            vm.operationResource = {
                id: null,
                operation: $stateParams.operationId,
                resource: null,
                resourceType: null,
                quantity: null,
                description: null
            };

            vm.onSelectType = onSelectType;
            function onSelectType(type) {
                if (type != null && type != undefined) {
                    vm.selectedOperationType = type;
                    vm.operationResource.resourceType = type.id;
                }
            }


            function createOperationResource() {
                create().then(function () {
                    vm.operationResource = {
                        id: null,
                        operation: $stateParams.operationId,
                        resource: null,
                        resourceType: null,
                        quantity: null,
                        description: null
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }


            function validate() {
                var valid = true;
                if (vm.operationResource.resource == null || vm.operationResource.resource == undefined ||
                    vm.operationResource.resource == "") {
                    $rootScope.showWarningMessage("Please select resource");
                    valid = false;
                }

                else if (vm.operationResource.resourceType == null || vm.operationResource.resourceType == undefined ||
                    vm.operationResource.resourceType == "") {
                    $rootScope.showWarningMessage("Please select resource type");
                    valid = false;
                }
                else if (vm.operationResource.quantity == null || vm.operationResource.quantity == undefined ||
                    vm.operationResource.quantity == "") {
                    $rootScope.showWarningMessage("Please enter quantity");
                    valid = false;
                } else if (vm.operationResource.quantity != null && vm.operationResource.quantity < 0) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter +ve number for quantity");
                }

                return valid;
            }


            function create() {
                var dfd = $q.defer();
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    OperationService.createOperationResources(vm.operationResource).then(
                        function (data) {
                            vm.operationResource = {
                                id: null,
                                operation: $stateParams.operationId,
                                resource: null,
                                resourceType: null,
                                quantity: null,
                                description: null
                            };
                            vm.selectedOperationType = null,
                                $scope.callback();
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("Resources added successfully");

                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }


            (function () {

                $rootScope.$on('app.select.operation.resource', createOperationResource);

            })();
        }
    });