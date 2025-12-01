define(
    [
        'app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService'
    ],
    function (module) {
        module.controller('Supplier4mChangeController', Supplier4mChangeController);

        function Supplier4mChangeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window, DialogService,
                                            $translate, SupplierService, CustomObjectService, CommonService, CustomObjectTypeService) {
            var vm = this;

            vm.loading = true;
            vm.supplierId = $stateParams.supplierId;
            vm.customObjects = [];
            vm.showObjectDetails = showObjectDetails;
            vm.customObjectType = null;
            var parsed = angular.element("<div></div>");
            var createButton = parsed.html($translate.instant("CREATE")).html();

            function load4mChangeSuppliers() {
                vm.loading = true;
                CustomObjectTypeService.getCustomObjectByName("4MChange-Supplier").then(
                    function (data) {
                        if (data.customObjectType != null && data.customObjectType != "") {
                            vm.customObjectType = data.customObjectType;
                            vm.typeId = data.customObjectType.id;
                            CustomObjectService.getCustomObjectsByTypeAndSupplier(vm.typeId, vm.supplierId).then(
                                function (data) {
                                    vm.customObjects = data;
                                    CommonService.getMultiplePersonReferences(vm.customObjects, ['createdBy', 'modifiedBy']);
                                    vm.loading = false;
                                }
                            );
                        } else {
                            vm.loading = false;
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.createCustomObject = createCustomObject;
            function createCustomObject() {
                var options = {
                    title: 'New ' + vm.customObjectType.name,
                    template: 'app/desktop/modules/customObject/new/newCustomObjectView.jsp',
                    controller: 'NewCustomObjectController as newCustomObjectsVm',
                    resolve: 'app/desktop/modules/customObject/new/newCustomObjectController',
                    width: 650,
                    showMask: true,
                    data: {
                        selectType: vm.customObjectType.id,
                        customeObjectTypeName: vm.customObjectType.name,
                        creationType: "Supplier",
                        supplierData: $rootScope.supplier,
                        customObjectTypeData: vm.customObjectType
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.customObject.new'}
                    ],
                    callback: function () {
                        load4mChangeSuppliers();
                        $rootScope.loadSupplierFileCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function showObjectDetails(object) {
                $window.localStorage.setItem("shared-permission", $rootScope.sharedPermission);
                $state.go('app.customobjects.details', {
                    customId: object.id,
                    permission: $rootScope.sharedPermission,
                    tab: 'details.basic'
                });
            }

            (function () {
                $scope.$on('app.supplier.tabActivated', function (event, data) {
                    if (data.tabId == 'details.4mChangeSupplier') {
                        load4mChangeSuppliers();
                    }
                });
            })();
        }
    }
);