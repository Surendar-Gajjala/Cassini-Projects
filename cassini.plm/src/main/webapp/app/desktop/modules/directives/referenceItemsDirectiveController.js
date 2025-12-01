define(
    [
        'app/desktop/desktop.app',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],

    function (module) {
        module.directive('referenceItemsView', ReferenceItemsController);

        function ReferenceItemsController($rootScope, $translate, $window, $state, ProjectService,
                                          $injector, ActivityService, DialogService) {
            return {
                templateUrl: 'app/desktop/modules/directives/referenceItemsView.jsp',
                restrict: 'E',
                scope: {
                    'objectType': '@',
                    'projectId': '=',
                    'taskId': '=',
                    'activityId': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {

                    $scope.loading = true;

                    $scope.addItems = addItems;
                    $scope.deleteItemReference = deleteItemReference;
                    $scope.showItem = showItem;
                    $scope.loginPersonDetails = $rootScope.loginPersonDetails;

                    var parsed = angular.element("<div></div>");
                    var addItemsTitle = parsed.html($translate.instant("ADD_ITEM")).html();
                    var itemsAddedMessage = parsed.html($translate.instant("ITEM_ADDED_MESSAGE")).html();
                    var yes = parsed.html($translate.instant("YES")).html();
                    var deleteReferenceItemTitle = parsed.html($translate.instant("DELETE_REFERENCE_ITEM_TITLE")).html();
                    var deleteReferenceItemMessage = parsed.html($translate.instant("DELETE_REFERENCE_ITEM_MESSAGE")).html();
                    var no = parsed.html($translate.instant("NO")).html();
                    var referenceItemDeleteMessage = parsed.html($translate.instant("REFERENCE_ITEM_DELETE_MESSAGE")).html();
                    $scope.clickToShowItem = parsed.html($translate.instant("CLICK_TO_SHOW_ITEM")).html();
                    var addButton = parsed.html($translate.instant("ADDBUTTON")).html();

                    function addItems() {
                        var options = {
                            title: addItemsTitle,
                            showMask: true,
                            template: 'app/desktop/modules/pm/project/details/tabs/itemReferences/selectItemReferencesView.jsp',
                            controller: 'SelectItemReferenceController as selectItemReferenceVm',
                            resolve: 'app/desktop/modules/pm/project/details/tabs/itemReferences/selectItemReferencesController',
                            width: 720,
                            data: {
                                projectIdData: $scope.projectId,
                                activityIdData: $scope.activityId,
                                taskIdData: $scope.taskId,
                                referenceItemMode: $scope.objectType
                            },
                            buttons: [
                                {text: addButton, broadcast: 'app.project.itemReferences'}
                            ],
                            callback: function (data) {
                                $rootScope.showSuccessMessage(itemsAddedMessage);
                                loadItemReferences();
                                $rootScope.loadProjectCounts();
                                $rootScope.hideSidePanel();
                            }
                        };

                        $rootScope.showSidePanel(options);

                    }

                    var itemsMap = new Hashtable();

                    function loadItemReferences() {
                        $rootScope.showBusyIndicator();
                        $scope.loading = true;
                        if ($scope.objectType == 'PROJECT') {
                            ProjectService.getItemsByProject($scope.projectId).then(
                                function (data) {
                                    $scope.itemReferences = data;
                                    $rootScope.loadProjectCounts();
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            ActivityService.getItemsByActivity($scope.activityId).then(
                                function (data) {
                                    $scope.itemReferences = data;
                                    $rootScope.loadActivityCount();
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        if ($scope.objectType == 'TASK') {
                            ActivityService.getItemsByTask($scope.activityId, $scope.taskId).then(
                                function (data) {
                                    $scope.itemReferences = data;
                                    $rootScope.loadTaskCount();
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function showItem(item) {
                        $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify($scope.referenceItemsTabId));
                        $state.go('app.items.details', {itemId: item.plmItem.latestRevision});
                    }

                    function deleteItemReference(itemReference) {
                        var options = {
                            title: deleteReferenceItemTitle,
                            message: deleteReferenceItemMessage + " [ " + itemReference.plmItem.itemName + " ] " + "?",
                            okButtonClass: 'btn-danger',
                            okButtonText: yes,
                            cancelButtonText: no
                        };
                        if ($scope.objectType == 'PROJECT') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ProjectService.deleteProjectItemReference($scope.projectId, itemReference).then(
                                        function (data) {
                                            loadItemReferences();
                                            $rootScope.loadProjectCounts();
                                            $rootScope.showSuccessMessage(referenceItemDeleteMessage);
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            });
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ActivityService.deleteActivityItemReference($scope.activityId, itemReference).then(
                                        function (data) {
                                            loadItemReferences();
                                            $rootScope.loadActivityCount();
                                            $rootScope.showSuccessMessage(referenceItemDeleteMessage);
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            });
                        }
                        if ($scope.objectType == 'TASK') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ActivityService.deleteTaskItemReference($scope.activityId, $scope.taskId, itemReference).then(
                                        function (data) {
                                            loadItemReferences();
                                            $rootScope.loadTaskCount();
                                            $rootScope.showSuccessMessage(referenceItemDeleteMessage);
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            });
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
                        $scope.$on('app.project.referenceItems', function (event, data) {
                            $scope.referenceItemsTabId = data.tabId;
                            loadItemReferences();
                        });
                    })();
                }
            }
        }
    }
);
