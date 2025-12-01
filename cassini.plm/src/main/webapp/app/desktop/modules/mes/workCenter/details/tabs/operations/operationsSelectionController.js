define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/operationService',
        'app/shared/services/core/workCenterService'
    ],

    function (module) {
        module.controller('OperationsSelectionController', OperationsSelectionController);

        function OperationsSelectionController($scope, $rootScope, $timeout, $translate, $stateParams, $state,WorkCenterService, OperationService) {

            var vm = this;

            vm.selectedItems = [];
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.workCenterId = $stateParams.workcenterId;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.reqDocuments.last != true) {
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadOperations();
                }
            }

            function previousPage() {
                if (vm.reqDocuments.first != true) {
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadOperations();
                }
            }

            vm.pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                searchQuery: null,
                number: null,
                type: '',
                name: null,
                bop: '',
                bopPlan: '',
                workCenter: ''
            };

            function onSelectType(reqType) {
                if (reqType != null && reqType != undefined) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.filters.reqType = reqType;
                    vm.filters.type = reqType.id;
                    vm.selectedItems = [];
                    searchItems();
                }
            }

            vm.searchItems = searchItems;

            function searchItems() {
                vm.pageable.page = 0;
                loadOperations();
                vm.clear = true;
            }

            function clearFilter() {
                vm.filters = {
                    searchQuery: null,
                    number: null,
                    type: '',
                    name: null,
                    bop: '',
                    bopPlan: '',
                    workCenter: ''
                };
                vm.selectedType = null;
                vm.selectedItems = [];
                vm.pageable.page = 0;
                loadOperations();
                vm.clear = false;
            }

            vm.selectAllCheck = false;

            function selectAll() {
                vm.selectedItems = [];
                if (vm.selectAllCheck == false) {
                    angular.forEach(vm.operations.content, function (item) {
                        item.selected = false;
                    })
                    vm.selectedItems = [];

                } else {
                    vm.error = "";
                    angular.forEach(vm.operations.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                    if (vm.selectedItems.length == vm.operations.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                        vm.selectAllCheck = false;
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                    if (vm.selectedItems.length == vm.operations.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            var parsed = angular.element("<div></div>");
            var atLeastReqDocValidation = parsed.html($translate.instant("ATLEAST_ONE_REQUIREMENT_DOCUMENT")).html();
            var operationsAdded = parsed.html($translate.instant("OPERTATIONS_ADDED")).html();
            vm.projectReqDocs = [];
            
            function create() {
               WorkCenterService.createWorkCenterOperations(vm.workCenterId, vm.selectedItems).then(
                function(data){
                    $scope.callback(data);
                    $rootScope.showSuccessMessage(operationsAdded);
                    $rootScope.hideSidePanel();
                    $rootScope.hideBusyIndicator();
                    

                })
            }

            function loadOperations() {
                vm.filters.workCenter = vm.workCenterId;
                OperationService.getAllOperations(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.operations = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }
            
            (function () {
                $rootScope.$on('app.workcenter.operation.add', create);
                loadOperations();
            })();
        }
    }
);