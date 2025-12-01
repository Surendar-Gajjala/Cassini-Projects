define(
    [
        'app/desktop/modules/req/reqdocs/requirement/requirement.module',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/itemService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ReqItemsController', ReqItemsController);

        function ReqItemsController($scope, $rootScope, $timeout, $state, $stateParams, RequirementService,
                                    ItemService, $q, $translate, $application, DialogService) {
            var vm = this;
            vm.reqId = $stateParams.requirementId;
            var parsed = angular.element("<div></div>");
            var multipleItemSelectionTitle = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            var removeItemTitle = parsed.html($translate.instant("REMOVE_ITEM")).html();
            var removeDialogMsg = parsed.html($translate.instant("REMOVE_ITEM_DIALOG_MESSAGE")).html();
            var itemRemoved = parsed.html($translate.instant("ITEM_REMOVED")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            vm.deleteRequirementItem = deleteRequirementItem;


            function loadRequirementItems() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                RequirementService.getRequirementItems(vm.reqId).then(
                    function (data) {
                        vm.reqItems = data;
                        var masters = [];
                        angular.forEach(vm.reqItems, function (reqItem) {
                            masters.push(reqItem.item);
                        })
                        ItemService.getItemReferences(masters, 'itemMaster');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var emptyReqItem = {
                id: null,
                requirementVersion: null,
                item: null
            }

            vm.addItems = addItems;
            function addItems() {
                var options = {
                    title: multipleItemSelectionTitle,
                    template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                    controller: 'ItemsSelectionController as itemsSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'add.select.items'}
                    ],
                    data: {
                        mode: "requirements",
                        selectedItemFilterId: vm.reqId
                    },

                    callback: function (result) {
                        vm.addedItems = [];
                        angular.forEach(result, function (item) {
                            var reqItem = angular.copy(emptyReqItem);
                            reqItem.item = item.latestRevisionObject;
                            reqItem.requirementVersion = $rootScope.reqVersion;
                            reqItem.reqDoc = vm.reqId;
                            vm.addedItems.push(reqItem);
                        })
                        RequirementService.createRequirementItems(vm.reqId, vm.addedItems).then(
                            function (data) {
                                loadRequirementItems();
                                $rootScope.loadRequirementTabCounts();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function deleteRequirementItem(reqItem) {
                var options = {
                    title: removeItemTitle,
                    message: removeDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        RequirementService.deleteRequirementItem(vm.reqId, reqItem.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(itemRemoved);
                                loadRequirementItems();
                                $rootScope.loadRequirementTabCounts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showItem = showItem;
            function showItem(reqItem) {
                $state.go('app.items.details', {itemId: reqItem.item.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.req.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.items') {
                        loadRequirementItems();
                    }
                });
            })();
        }
    }
);

