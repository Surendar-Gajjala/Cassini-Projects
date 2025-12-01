define(
    [
        'app/desktop/modules/dispatch/dispatch.module',
        'app/shared/services/core/dispatchService',
        'app/shared/services/core/bomService'
    ],
    function (module) {

        module.controller('NewDispatchController', NewDispatchController);

        function NewDispatchController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                       DispatchService, BomService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.dispatchItems = [];
            vm.selectAllCheck = false;

            vm.newDispatch = {
                id: null,
                number: null,
                bom: null,
                gatePassNumber: null,
                status: 'NEW',
                dispatchDate: null,
                type: null,
                itemInstances: []
            };
            vm.dispatchTypes = ['REJECTED', 'FAILURE', 'FABRICATION'];

            vm.onSelectBom = onSelectBom;
            vm.selectAll = selectAll;
            vm.selectItem = selectItem;
            vm.onSelectType = onSelectType;

            function createDispatch() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newDispatch.itemInstances = vm.dispatchItems;
                    DispatchService.createDispatch(vm.newDispatch).then(
                        function (data) {
                            vm.newDispatch = {
                                id: null,
                                number: null,
                                bom: null,
                                gatePassNumber: null,
                                status: 'NEW',
                                dispatchDate: null,
                                itemInstances: []
                            };
                            vm.dispatchItems = [];
                            $rootScope.showSuccessMessage("New Dispatch created successfully");
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                            $scope.callback(data);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

            }

            function validate() {
                var valid = true;

                if (vm.newDispatch.bom == null || vm.newDispatch.bom == "" || vm.newDispatch.bom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select BOM");
                } else if (vm.newDispatch.type == null || vm.newDispatch.type == "" || vm.newDispatch.type == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Dispatch Type");
                } else if (vm.newDispatch.dispatchDate == null || vm.newDispatch.dispatchDate == "" || vm.newDispatch.dispatchDate == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Dispatch Date");
                } else if (vm.newDispatch.dispatchDate != null && !validDispatchDate()) {
                    valid = false;
                } else if (vm.dispatchItems.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select at least one item to Dispatch");
                }

                return valid;
            }

            function validDispatchDate() {
                var valid = true;

                var today = moment(new Date());
                var todayStr = today.format('DD/MM/YYYY');
                var todayDate = moment(todayStr, 'DD/MM/YYYY');
                var dispatchDate = moment(vm.newDispatch.dispatchDate, 'DD/MM/YYYY');

                if (dispatchDate.isBefore(todayDate)) {
                    valid = false;
                    $rootScope.showWarningMessage("Dispatch Date should be on (or) after Today's Date");
                }

                return valid;
            }

            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        vm.boms = data;
                    }
                )
            }

            function onSelectType(type) {
                vm.dispatchItems = [];
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                DispatchService.getItemsToDispatch(vm.newDispatch.bom.id, type).then(
                    function (data) {
                        vm.itemsToDispatch = data;
                        angular.forEach(vm.itemsToDispatch, function (item) {
                            item.selected = false;
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function onSelectBom() {
                vm.dispatchItems = [];
                vm.newDispatch.type = null;
            }

            function selectAll() {
                vm.dispatchItems = [];
                if (vm.selectAllCheck) {
                    angular.forEach(vm.itemsToDispatch, function (item) {
                        item.selected = true;
                        vm.dispatchItems.push(item);
                    });
                } else {
                    vm.dispatchItems = [];
                    angular.forEach(vm.itemsToDispatch, function (item) {
                        item.selected = false;
                    });
                }
            }

            function selectItem(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectAllCheck = false;
                    var index = vm.dispatchItems.indexOf(item);
                    vm.dispatchItems.splice(index, 1);
                } else {
                    angular.forEach(vm.dispatchItems, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = vm.dispatchItems.indexOf(item);
                            vm.dispatchItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        vm.dispatchItems.push(item);
                    }

                    if (vm.dispatchItems.length == vm.itemsToDispatch.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadBoms();
                    $rootScope.$on('app.dispatch.new', createDispatch);
                }
            })();
        }
    }
)
;