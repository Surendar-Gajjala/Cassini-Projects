define(
    [
        'app/desktop/desktop.app',
        'app/shared/services/core/relatedItemService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],

    function (module) {
        module.directive('relatedItemsView', RelatedItemsController);

        function RelatedItemsController($rootScope, $compile, $sce, $timeout, $state, $translate, $application,
                                        $injector, RelatedItemService, DialogService) {
            return {
                templateUrl: 'app/desktop/modules/directives/relatedItems/relatedItemsView.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'objectId': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {

                    var parsed = angular.element("<div></div>");
                    $scope.loading = true;

                    var selectItems = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
                    $scope.addRelatedItemTitle = parsed.html($translate.instant("ADD_RELATED_ITEMS")).html();

                    $scope.showRelatedItems  = showRelatedItems;
                    $scope.items = [];
                    $scope.itemFlag = false;
                    function showRelatedItems() {
                        $scope.selectedItems = [];
                        var options = {
                            title: selectItems,
                            template: 'app/desktop/modules/change/dcr/details/tabs/relatedItem/dcrRelatedItemsSelectionView.jsp',
                            controller: 'DCRRelatedItemsSelectionController as relatedItemsVm',
                            resolve: 'app/desktop/modules/change/dcr/details/tabs/relatedItem/dcrRelatedItemsSelectionController',
                            width: 700,
                            showMask: true,
                            buttons: [
                                {text: $rootScope.add, broadcast: 'add.dcr.related.items'}
                            ],
                            data: {
                                mode: $scope.objectType,
                                selectedObject: $scope.objectId
                            },
                            callback: function (result) {
                                loadRelatedItems();
                                $rootScope.hideBusyIndicator();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    function loadRelatedItems() {
                        loadRelatedItemsPermission();
                        $rootScope.showBusyIndicator();
                        $scope.loading = true;
                        $scope.relatedItems = [];
                        RelatedItemService.getObjectRelatedItems($scope.objectId, $scope.objectType).then(
                            function (data) {
                                $scope.relatedItems = data;
                                $scope.loading = false;
                                loadItems();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                    }

                    $scope.noPermission = parsed.html($translate.instant('NO_PERMISSION_PERFORM')).html();
                    function loadRelatedItemsPermission() {
                        $scope.hasCreate = $rootScope.hasPermission('relateditem','create');
                        $scope.hasDelete = $rootScope.hasPermission('relateditem','delete');
                    }

                    function loadItems() {
                        if ($scope.objectType == "DCO") {
                            $rootScope.loadDCOCounts();
                        } else if ($scope.objectType == "DCR") {
                            $rootScope.loadDCRCounts();
                        } else if ($scope.objectType == "ECR") {
                            $rootScope.loadECRCounts();
                        } else if ($scope.objectType == "VARIANCE") {
                            $rootScope.loadVarianceCounts();
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            $rootScope.loadProblemReportDetails();
                        } else if ($scope.objectType == "QCR") {
                            $rootScope.loadQcrDetails();
                        } else if ($scope.objectType == "ITEMINSPECTION") {
                            $rootScope.loadInspectionDetails();
                        }
                    }

                    var removeItems = parsed.html($translate.instant("REMOVE_ITEM")).html();
                    var itemRemoveTitle = parsed.html($translate.instant("REMOVE_ITEM_TITLE_MSG")).html();
                    var itemRemoveMsg = parsed.html($translate.instant("REMOVE_ITEM_SUCCESS_MSG")).html();

                    $scope.deleteItem = deleteItem;

                    function deleteItem(item) {
                        var options = {
                            title: removeItems,
                            message: itemRemoveTitle,
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                RelatedItemService.deleteObjectRelatedItem(item.objectId, $scope.objectType).then(
                                    function (data) {
                                        var index = $scope.relatedItems.indexOf(item);
                                        $scope.relatedItems.splice(index, 1);
                                        $rootScope.showSuccessMessage(itemRemoveMsg);
                                        loadRelatedItems();
                                    }, function (error) {
                                          $rootScope.showErrorMessage(error.message);
                                          $rootScope.hideBusyIndicator();
                                     }
                                )
                            }
                        });

                    }

                    $scope.performCustomTableAction = performCustomTableAction;
                    function performCustomTableAction(action) {
                        var service = $injector.get(action.service);
                        if (service != null && service !== undefined) {
                            var method = service[action.method];
                            if (method != null && method !== undefined && typeof method === "function") {
                                if ($rootScope.pluginTableObjectRevision != null && $rootScope.pluginTableObjectRevision != undefined) method($rootScope.pluginTableObject, $rootScope.pluginTableObjectRevision);
                                else method($rootScope.pluginTableObject);
                            }
                        }
                    }

                    $scope.showItem = showItem;
                    function showItem(item) {
                        $state.go('app.items.details', {itemId: item.itemId});
                    }

                    (function () {
                        $scope.$on('app.relatedItem.tabActivated', function (event, data) {
                            loadRelatedItems();
                        });
                    })();
                }
            }
        }
    }
);
