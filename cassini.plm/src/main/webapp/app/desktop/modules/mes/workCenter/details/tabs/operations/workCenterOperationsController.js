define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/workCenterService'

    ],
    function (module) {
        module.controller('WorkCenterOperationsController', WorkCenterOperationsController);

        function WorkCenterOperationsController($scope, $rootScope, $timeout, $state, $stateParams, $translate, $cookies, $window,DialogService,
                                               WorkCenterService) {
            var vm = this;
            vm.loading = false;
            vm.workCenterId = $stateParams.workcenterId;
            vm.addOperations = addOperations;
            var parsed = angular.element("<div></div>");
            function addOperations() {
                var options = {
                    title: "Add Operations",
                    showMask: true,
                    template: 'app/desktop/modules/mes/workCenter/details/tabs/operations/operationsSelectionView.jsp',
                    controller: 'OperationsSelectionController as operationsSelectionVm',
                    resolve: 'app/desktop/modules/mes/workCenter/details/tabs/operations/operationsSelectionController',
                    width: 700,
                    buttons: [
                        {text: 'Add', broadcast: 'app.workcenter.operation.add'}
                    ],
                    callback: function () {
                        loadWorkCenterOperations();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteOperationTitle = parsed.html($translate.instant("DELETE_WORKCENTER_OPERATION")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_WORKCENTER_TYPE_DIALOG_MESSAGE")).html();
            var OperationDeleteMsg = parsed.html($translate.instant("OPERATION_WORKCENTER_DELETE_MSG")).html();


            function loadWorkCenterOperations(){
                WorkCenterService.getAllWorkCenterOperations(vm.workCenterId).then(
                    function(data){
                    vm.workcenterOperations = data;
                    angular.forEach(vm.workcenterOperations, function (operation) {
                        operation.modifiedDatede = null;
                        if (operation.operationObject.modifiedDate != null) {
                            operation.operationObject.modifiedDatede = moment(operation.operationObject.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        }
                    });
                    }
                )
            }
vm.removeOperation = removeOperation;
            function removeOperation(operation) {
                var options = {
                    title: deleteOperationTitle,
                    message: deleteDialogMessage + " [ " + operation.operationObject.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (operation.id != null && operation.id != undefined) {
                            WorkCenterService.deleteworkcenterOperation(operation.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(OperationDeleteMsg);
                                    loadWorkCenterOperations();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }


            (function () {
                $scope.$on('app.workcenter.tabActivated', function (event, data) {
                    if (data.tabId == 'details.operations') {
                        loadWorkCenterOperations();
                    }
                });
            })();
        }
    }
);