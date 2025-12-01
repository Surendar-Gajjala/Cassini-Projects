define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/problemReportService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.directive('affectedItems', AffectedItemsDirective);

        function AffectedItemsDirective($rootScope, $translate, ECRService, DCRService, $injector, ProblemReportService, QcrService) {

            return {
                templateUrl: 'app/desktop/directives/affected-items/affectedItemsDirective.jsp',
                restrict: 'E',
                scope: {
                    objectType: "@",
                    objectId: "=",
                    hasPermission: "=",
                    deleteItem: "="
                },

                link: function ($scope, element, attrs) {
                    $scope.affectedItems = [];
                    $scope.selectedItems = [];
                    $scope.affectedItemIds = [];
                    var emptyItem = {
                        id: null,
                        ecr: null,
                        dcr: null,
                        problemReport: null,
                        qcr: null,
                        item: null,
                        notes: null
                    };

                    $scope.loading = true;
                    $scope.addAffectedItems = addAffectedItems;

                    var parsed = angular.element("<div></div>");
                    var selectItems = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
                    var addButton = parsed.html($translate.instant("ADD")).html();
                    var itemsAddedMsg = parsed.html($translate.instant("ITEMS_ADDED_SUCCESSFULLY")).html();
                    var itemsUpdateMsg = parsed.html($translate.instant("ITEM_UPDATE_MSG")).html();

                    function loadAffectedItems() {
                        $rootScope.showBusyIndicator();
                        var promise = null;
                        $scope.loading = true;
                        if ($scope.objectType == "ECR") {
                            promise = ECRService.getAffectedItems($scope.objectId);
                        } else if ($scope.objectType == "DCR") {
                            promise = DCRService.getAffectedItems($scope.objectId);
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            promise = ProblemReportService.getPrAffectedItems($scope.objectId);
                        } else if ($scope.objectType == "QCR") {
                            promise = QcrService.getQcrProblemItems($scope.objectId);
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    $scope.affectedItems = data;
                                    angular.forEach($scope.affectedItems, function (item) {
                                        item.editMode = false;
                                        item.isNew = false;
                                        if ($scope.objectType == "QCR" && item.problemReport != null) {
                                            item.disableDelete = true;
                                        }
                                    })
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function addAffectedItems() {
                        var options = {
                            title: selectItems,
                            template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                            controller: 'ItemsSelectionController as itemsSelectionVm',
                            resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                            width: 700,
                            showMask: true,
                            buttons: [
                                {text: addButton, broadcast: 'add.select.items'}
                            ],
                            data: {
                                mode: $scope.objectType,
                                selectedObjectId: $scope.objectId
                            },
                            callback: function (result) {
                                angular.forEach(result, function (item) {
                                    var affectedItem = angular.copy(emptyItem);
                                    affectedItem.itemNumber = item.itemNumber;
                                    affectedItem.itemName = item.itemName;
                                    affectedItem.description = item.description;
                                    affectedItem.itemType = item.itemType.name;
                                    if ($scope.objectType == "PROBLEMREPORT") {
                                        if ($scope.problemReport != null && $scope.problemReport.product != null && $scope.problemReport.product != "") {
                                            affectedItem.revision = item.asReleasedRevisionObject.revision;
                                            affectedItem.lifeCyclePhase = item.asReleasedRevisionObject.lifeCyclePhase;
                                            affectedItem.item = item.asReleasedRevision;
                                        } else {
                                            affectedItem.revision = item.latestReleasedRevisionObject.revision;
                                            affectedItem.lifeCyclePhase = item.latestReleasedRevisionObject.lifeCyclePhase;
                                            affectedItem.item = item.latestReleasedRevision;
                                        }
                                    } else {
                                        if (!item.latestRevisionObject.rejected) {
                                            affectedItem.revision = item.latestRevisionObject.revision;
                                            affectedItem.lifeCyclePhase = item.latestRevisionObject.lifeCyclePhase;
                                            affectedItem.item = item.latestRevision;
                                        } else if (item.latestReleasedRevisionObject != null && item.latestReleasedRevisionObject != "") {
                                            affectedItem.revision = item.latestReleasedRevisionObject.revision;
                                            affectedItem.lifeCyclePhase = item.latestReleasedRevisionObject.lifeCyclePhase;
                                            affectedItem.item = item.latestReleasedRevision;
                                        }
                                    }
                                    affectedItem.editMode = true;
                                    affectedItem.isNew = true;
                                    if ($scope.objectType == "ECR") {
                                        affectedItem.ecr = $scope.objectId;
                                        affectedItem.problemReportList = [];
                                        affectedItem.qcrItem = false;
                                    } else if ($scope.objectType == "DCR") {
                                        affectedItem.dcr = $scope.objectId;
                                    } else if ($scope.objectType == "PROBLEMREPORT") {
                                        affectedItem.problemReport = $scope.objectId;
                                    } else if ($scope.objectType == "QCR") {
                                        affectedItem.qcr = $scope.objectId;
                                    }
                                    $scope.affectedItems.unshift(affectedItem);
                                    $scope.selectedItems.unshift(affectedItem);
                                })
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.save = save;
                    $scope.onCancel = onCancel;
                    $scope.cancelChanges = cancelChanges;
                    $scope.saveAll = saveAll;
                    $scope.updateItem = updateItem;
                    $scope.removeAll = removeAll;
                    $scope.deleteAffectedItem = deleteAffectedItem;
                    $scope.editItem = editItem;

                    function save(item) {
                        var promise = null;
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($scope.objectType == "ECR") {
                            item.ecr = $scope.objectId;
                            promise = ECRService.createEcrItem($scope.objectId, item);
                        } else if ($scope.objectType == "DCR") {
                            item.dcr = $scope.objectId;
                            promise = DCRService.createDcrItem($scope.objectId, item)
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            item.problemReport = $scope.objectId;
                            promise = ProblemReportService.createPrAffectedItem($scope.objectId, item);
                        } else if ($scope.objectType == "QCR") {
                            item.qcr = $scope.objectId;
                            promise = QcrService.createQcrProblemItem($scope.objectId, item);
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    item.id = data.id;
                                    item.editMode = false;
                                    item.isNew = false;
                                    loadDetailCounts();
                                    $scope.selectedItems.splice($scope.selectedItems.indexOf(item), 1);
                                    $rootScope.showSuccessMessage(itemsAddedMsg);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function loadDetailCounts() {
                        if ($scope.objectType == "ECR") {
                            $rootScope.loadECRCounts();
                        } else if ($scope.objectType == "DCR") {
                            $rootScope.loadDCRCounts();
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            $rootScope.loadProblemReportDetails();
                        } else if ($scope.objectType == "QCR") {
                            $rootScope.loadQcrDetails();
                        }
                    }

                    function onCancel(item) {
                        $scope.affectedItems.splice($scope.affectedItems.indexOf(item), 1);
                        $scope.selectedItems.splice($scope.selectedItems.indexOf(item), 1);
                    }

                    function cancelChanges(item) {
                        item.editMode = false;
                        item.notes = item.oldNotes;
                    }

                    function saveAll() {
                        var promise = null;
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($scope.objectType == "ECR") {
                            promise = ECRService.createEcrItems($scope.objectId, $scope.selectedItems);
                        } else if ($scope.objectType == "DCR") {
                            promise = DCRService.createDcrItems($scope.objectId, $scope.selectedItems);
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            promise = ProblemReportService.createPrAffectedItems($scope.objectId, $scope.selectedItems);
                        } else if ($scope.objectType == "QCR") {
                            promise = QcrService.createQcrProblemItems($scope.objectId, $scope.selectedItems);
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    $scope.selectedItems = [];
                                    loadDetailCounts();
                                    loadAffectedItems();
                                    $rootScope.showSuccessMessage(itemsAddedMsg);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function removeAll() {
                        angular.forEach($scope.selectedItems, function (item) {
                            $scope.affectedItems.splice($scope.affectedItems.indexOf(item), 1);
                        })
                        $scope.selectedItems = [];
                    }

                    function updateItem(item) {
                        var promise = null;
                        $rootScope.showBusyIndicator($('.view-container'));
                        if ($scope.objectType == "ECR") {
                            promise = ECRService.updateEcrItem($scope.objectId, item);
                        } else if ($scope.objectType == "DCR") {
                            promise = DCRService.createDcrItem($scope.objectId, item);
                        } else if ($scope.objectType == "PROBLEMREPORT") {
                            promise = ProblemReportService.updatePrAffectedItem($scope.objectId, item);
                        } else if ($scope.objectType == "QCR") {
                            promise = QcrService.updateQcrProblemItem($scope.objectId, item);
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    item.id = data.id;
                                    item.editMode = false;
                                    item.isNew = false;
                                    $rootScope.showSuccessMessage(itemsUpdateMsg);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function editItem(item) {
                        item.editMode = true;
                        item.isNew = false;
                        item.oldNotes = item.notes;
                    }

                    function deleteAffectedItem(item) {
                        $scope.deleteItem(item);
                    }

                    $scope.problemReport = null;
                    function loadPr() {
                        if ($scope.objectType == "PROBLEMREPORT") {
                            ProblemReportService.getProblemReport($scope.objectId).then(
                                function (data) {
                                    $scope.problemReport = data;
                                }
                            )
                        }
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

                    (function () {
                        $scope.$on('app.affectedItems.view', function (event, data) {
                            loadAffectedItems();
                            loadPr();
                        });
                    })();
                }
            }

        }
    }
);