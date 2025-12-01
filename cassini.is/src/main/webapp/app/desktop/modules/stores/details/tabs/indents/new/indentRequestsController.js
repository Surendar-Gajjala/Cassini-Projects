define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/requisitionService'

    ],
    function (module) {
        module.controller('IndentRequestsController', IndentRequestsController);

        function IndentRequestsController($scope, $rootScope, $timeout, $stateParams, $cookies, RequisitionService) {

            var vm = this;
            vm.loading = false;
            vm.projectRequisitions = [];
            vm.selectedRequests = [];
            vm.projectRequisitionChanged = projectRequisitionChanged;
            vm.addToIndentItems = addToIndentItems;
            var requisitionMap = new Hashtable;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function addToIndentItems(requisition, requisitionItem) {
                requisitionItem.id = null;
                requisitionItem.isNew = true;
                requisitionItem.editMode = true;
                requisitionItem.showEditButton = false;
                $scope.callback(requisition, requisitionItem, requisitionMap);
            }

            function projectRequisitionChanged(requisition) {
                vm.requisitionObject = null;
                vm.requisitionObject = requisition;
            }

            function loadProjectRequisitions() {
                RequisitionService.getPageableRequisitionsByProject($rootScope.storeId, $scope.data.projectObj.id, pageable).then(
                    function (data) {
                        vm.projectRequisitions = data.content;
                        angular.forEach(vm.projectRequisitions, function (projectRequisition) {
                            var reqItems = $scope.data.requestItemsMap.get(projectRequisition.requisitionNumber);
                            if (reqItems != null) {
                                angular.forEach(reqItems, function (reqItem) {
                                    var reqItemIndex = projectRequisition.customRequisitionItems.findIndex(item = > item.materialItem.id == reqItem.materialItem.id
                                    )
                                    ;
                                    if (reqItemIndex != -1) {
                                        projectRequisition.customRequisitionItems.splice(reqItemIndex, 1);
                                    }
                                });
                            }
                            requisitionMap.put(projectRequisition.requisitionNumber, projectRequisition);
                        });
                    });
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjectRequisitions();
                }
            })();
        }
    }
);