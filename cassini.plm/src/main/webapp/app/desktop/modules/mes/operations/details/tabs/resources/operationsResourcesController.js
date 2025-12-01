define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/operationService'

    ],
    function (module) {
        module.controller('OperationResourcesController', OperationResourcesController);

        function OperationResourcesController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window, DialogService,OperationService) {
            var vm = this;
            vm.loading = false;
            vm.addOperationResources = addOperationResources;
            vm.operationId = $stateParams.operationId;
            
            var parsed = angular.element("<div></div>");
            function addOperationResources() {
                var options = {
                    title: "Resources",
                    template: 'app/desktop/modules/mes/operations/details/tabs/resources/selectOperationResourcesView.jsp',
                    controller: 'SelectOperationResourcesController as selectOperationResourcesVm',
                    resolve: 'app/desktop/modules/mes/operations/details/tabs/resources/selectOperationResourcesController',
                    width: 650,
                    showMask: true,
                    data: {
                        type: null
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.select.operation.resource'}
                    ],
                    callback: function () {
                    loadOperationResources();   
                
                    }
                };

                $rootScope.showSidePanel(options);
            }


            var deleteResourceDialogTitle = parsed.html($translate.instant("DELETE_OPERATION_RESOURCE")).html();
            var deleteResourceDialogMessage = parsed.html($translate.instant("DELETE_OPERATION_RESOURCE_TITLE_MSG")).html();
            var deleteResourceMessage = parsed.html($translate.instant("OPERATION_RESOURCE_DELETE_MSG")).html();


            vm.deleteResource = deleteResource;
            function deleteResource(resource) {
                var options = {
                    title: deleteResourceDialogTitle,
                    message: deleteResourceDialogMessage + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        OperationService.deleteOperationResource(vm.operationId, resource.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(deleteResourceMessage);
                                loadOperationResources();
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                    }
                });
            }
            function loadOperationResources(){
                OperationService.getAllOperationResources(vm.operationId).then(
                    function (data) {
                        vm.opreationResources = data;
                        angular.forEach(vm.opreationResources, function(operation){
                            operation.editMode = false;
                        })
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message)
                    }
                )
            }

            vm.editResource = editResource;

            function editResource(resource){
                $scope.qty = resource.quantity;
                $scope.description = resource.description;
                resource.editMode = true;
            }

            vm.onCancel = onCancel;

            function onCancel(resource){
                resource.quantity = $scope.qty;
                resource.description = $scope.description;
                resource.editMode = false;
            }

            vm.saveResource = saveResource;
            function saveResource(operationResource){
                if (operationResource.quantity != null && operationResource.quantity < 0) {
                    $rootScope.showWarningMessage("Please enter +ve number for quantity");
                }else{
                    OperationService.updateOperationResource(operationResource).then(
                        function(data){
                            operationResource.editMode = false;
                            operationResource = data;
                            $rootScope.showSuccessMessage("Resource updated successfully");
    
                    })
                }
            }


            (function () {
                $scope.$on('app.operation.tabActivated', function (event, data) {
                    if (data.tabId == 'details.resources') {
                        loadOperationResources();
                    }
                    
                });
            })();
        }
    }
);