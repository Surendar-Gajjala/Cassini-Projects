define(
    [
        'app/desktop/desktop.app',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/dcoService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],

    function (module) {
        module.directive('changeRequestView', ChangeRequestController);

        function ChangeRequestController($rootScope, $compile, $sce, $timeout, $state, $translate, $application,
                                         $injector, ECRService, DCOService, DialogService) {
            return {
                templateUrl: 'app/desktop/modules/directives/changeRequestItems/changeRequestView.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'objectId': '=',
                    'hasPermission': '=',
                    'canAddRequest': "=",
                    'canDeleteRequest': "="
                },
                link: function ($scope, $elem, attrs) {

                    var parsed = angular.element("<div></div>");
                    $scope.loading = true;
                    var selectItems = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
                    var deleteCRDialogTitle = parsed.html($translate.instant("DELETE_CHANGE_REQUEST_TITLE")).html();
                    var deleteCRDialogMessage = parsed.html($translate.instant("DELETE_CHANGE_REQUEST_MESSAGE")).html();
                    var deleteCRMessage = parsed.html($translate.instant("CHANGE_REQUEST_DELETED_MESSAGE")).html();
                    $scope.addChangeRequestTitle = parsed.html($translate.instant("ADD_CHANGE_REQUEST")).html();
                    $scope.cannotDeleteChangeRequest = parsed.html($translate.instant("CANNOT_DELETE_REVISED_CHANGE_REQUEST")).html();

                    $scope.addChangeRequests = addChangeRequests;
                    function addChangeRequests() {
                        var options = {
                            title: "Select Change Requests",
                            template: 'app/desktop/modules/directives/changeRequestItems/changeReqItemsSelectionView.jsp',
                            controller: 'ChangeReqItemsSelectionController as changeItemSelectionVm',
                            resolve: 'app/desktop/modules/directives/changeRequestItems/changeReqItemsSelectionController',
                            width: 800,
                            showMask: true,
                            buttons: [
                                {text: $rootScope.add, broadcast: 'add.change.request.items'}
                            ],
                            data: {
                                selectedObjectId: $scope.objectId,
                                selectedObjectType: $scope.objectType
                            },
                            callback: function (result) {
                                loadChangeRequestItems();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.requestItems = [];
                    function loadChangeRequestItems() {
                        $rootScope.showBusyIndicator();
                        $scope.loading = true;
                        if ($scope.objectType == 'ECO') {
                            ECRService.getECRChangeRequestItems($scope.objectId).then(
                                function (data) {
                                    $scope.requestItems = data;
                                    $scope.loading = false;
                                    $rootScope.loadECOCounts();
                                    $rootScope.hideBusyIndicator()
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                 }
                            )
                        } else if ($scope.objectType == 'DCO') {
                            DCOService.getDCOChangeRequestItems($scope.objectId).then(
                                function (data) {
                                    $scope.requestItems = data;
                                    $scope.loading = false;
                                    $rootScope.loadDCOCounts();
                                    $rootScope.hideBusyIndicator()
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    $scope.deleteItem = deleteItem;
                    function deleteItem(item) {
                        if ($scope.objectType == 'ECO') {
                            if ($rootScope.eco.revisionsCreated) {
                                $rootScope.showWarningMessage($scope.cannotDeleteChangeRequest);
                            } else {
                                var options = {
                                    title: deleteCRDialogTitle,
                                    message: deleteCRDialogMessage + " [ " + item.crNumber + " ] " + "?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        ECRService.deleteEcoChangeRequest($scope.objectId, item.id).then(
                                            function (data) {
                                                $rootScope.showSuccessMessage(deleteCRMessage);
                                                loadChangeRequestItems();
                                                $rootScope.loadECOCounts();
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                });
                            }
                        } else if ($scope.objectType == 'DCO') {
                            if ($rootScope.dco.revisionsCreated) {
                                $rootScope.showWarningMessage($scope.cannotDeleteChangeRequest);
                            } else {
                                var options = {
                                    title: deleteCRDialogTitle,
                                    message: deleteCRDialogMessage + " [ " + item.crNumber + " ] " + "?",
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        DCOService.deleteDcoChangeRequest($scope.objectId, item.dcodcr).then(
                                            function (data) {
                                                $rootScope.showSuccessMessage(deleteCRMessage);
                                                loadChangeRequestItems();
                                                $rootScope.loadDCOCounts();
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                });
                            }
                        }
                    }

                    $scope.showChangeRequest = showChangeRequest;
                    function showChangeRequest(item) {
                        if ($scope.objectType == 'ECO') {
                            $state.go('app.changes.ecr.details', {ecrId: item.changeRequest, tab: 'details.basic'});
                            $window.localStorage.setItem("lastSelectedEcoTab", JSON.stringify("details.changeRequests"));
                        } else if ($scope.objectType == 'DCO') {
                            $state.go('app.changes.dcr.details', {dcrId: item.id, tab: 'details.basic'});
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
                        $scope.$on('app.changeRequestItem.tabActivated', function (event, data) {
                            loadChangeRequestItems();
                        });
                    })();
                }
            }
        }
    }
);

