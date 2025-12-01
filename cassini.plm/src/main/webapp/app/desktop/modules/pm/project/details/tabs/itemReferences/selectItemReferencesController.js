define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController'
    ],

    function (module) {
        module.controller('SelectItemReferenceController', SelectItemReferenceController);

        function SelectItemReferenceController($scope, $rootScope, $timeout, $translate, $stateParams, $state, ProjectService, ActivityService, ItemService) {

            var vm = this;

            var projectId = $scope.data.projectIdData;
            var activityId = $scope.data.activityIdData;
            var taskId = $scope.data.taskIdData;
            vm.selectedItems = [];
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.items.last != true) {
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadItems();
                }
            }
            function previousPage() {
                if (vm.items.first != true) {
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    loadItems();
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
                itemType: null,
                itemNumber: null,
                itemName: null,
                typeName: null,
                description: null,
                latestRevision: null,
                status: null
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.selectedType = itemType;
                    vm.itemType = itemType;
                    vm.filters.itemType = itemType;
                    vm.filters.typeName = itemType.name;
                    vm.selectedItems = [];
                    loadItems();
                }
            }

            vm.searchItems = searchItems;
            function searchItems() {
                vm.pageable.page = 0;
                loadItems();
            }

            vm.loadItems = loadItems;
            function loadItems() {
                if (projectId != null && vm.mode == "PROJECT") {
                        ProjectService.searchItemReferences(projectId, vm.mode, vm.pageable, vm.filters).then(
                            function (data) {
                                vm.clear = true;
                                vm.items = data;
                                vm.selectedItems = [];
                                vm.selectAllCheck = false;
                                ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                            }, function (error) {
                                  $rootScope.showErrorMessage(error.message);
                                  $rootScope.hideBusyIndicator();
                             }
                        )
                }
                if (activityId != null && vm.mode == "ACTIVITY") {
                    ProjectService.searchItemReferences(activityId, vm.mode, vm.pageable, vm.filters).then(
                        function (data) {
                            vm.clear = true;
                            vm.items = data;
                            vm.selectedItems = [];
                            vm.selectAllCheck = false;
                            ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                if (taskId != null && vm.mode == "TASK") {
                    ProjectService.searchItemReferences(taskId, vm.mode, vm.pageable, vm.filters).then(
                        function (data) {
                            vm.clear = true;
                            vm.items = data;
                            vm.selectedItems = [];
                            vm.selectAllCheck = false;
                            ItemService.getLatestRevisionReferences(vm.items.content, 'latestRevision');
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function clearFilter() {
                vm.filters = {
                    itemType: null,
                    itemNumber: null,
                    itemName: null,
                    typeName: null,
                    latestRevision: null,
                    status: null
                };
                vm.selectedType = null;
                vm.selectedItems = [];
                vm.pageable.page = 0;
                vm.clear = false;
                loadItems();
                $('[name="itemSelected"]').prop("checked", false);
            }

            vm.selectAllCheck = false;

            function selectAll() {
                vm.selectedItems = [];
                if (vm.selectAllCheck == false) {
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = false;
                    })
                    vm.selectedItems = [];

                } else {
                    vm.error = "";
                    angular.forEach(vm.items.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                    if (vm.selectedItems.length == vm.items.content.length) {
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
                    if (vm.selectedItems.length == vm.items.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            var parsed = angular.element("<div></div>");
            var atLeastOneItemValidation = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
            var searchValidation = parsed.html($translate.instant("SEARCH_MESSAGE_VALIDATION")).html();
            vm.itemReferences = [];
            function create() {
                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(atLeastOneItemValidation);
                } else {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    if (vm.mode == "PROJECT") {
                        vm.itemReferences = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.itemReference = {
                                id: null,
                                project: vm.project,
                                item: null
                            };
                            vm.itemReference.item = item.latestRevisionObject;
                            vm.itemReferences.push(vm.itemReference);
                            if (vm.selectedItems.length == vm.itemReferences.length) {
                                ProjectService.saveMultipleItemReferences(projectId, vm.itemReferences).then(
                                    function (data) {
                                        vm.selectedItems = [];
                                        vm.itemReferences = [];
                                        $rootScope.hideBusyIndicator();
                                        $scope.callback();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    }
                    if (vm.mode == "ACTIVITY") {
                        vm.itemReferences = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.itemReference = {
                                id: null,
                                activity: vm.activityId,
                                item: null
                            };
                            vm.itemReference.item = item.latestRevisionObject;
                            vm.itemReferences.push(vm.itemReference);
                            if (vm.selectedItems.length == vm.itemReferences.length) {
                                ActivityService.saveActivityItemReferences(vm.activityId, vm.itemReferences).then(
                                    function (data) {
                                        vm.selectedItems = [];
                                        vm.itemReferences = [];
                                        $rootScope.hideBusyIndicator();
                                        $scope.callback();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    }

                    if (vm.mode == "TASK") {
                        vm.itemReferences = [];
                        angular.forEach(vm.selectedItems, function (item) {
                            vm.itemReference = {
                                id: null,
                                task: vm.taskId,
                                item: null
                            };
                            vm.itemReference.item = item.latestRevisionObject;
                            vm.itemReferences.push(vm.itemReference);
                            if (vm.selectedItems.length == vm.itemReferences.length) {
                                ActivityService.saveTaskItemReferences(vm.activityId, vm.taskId, vm.itemReferences).then(
                                    function (data) {
                                        vm.selectedItems = [];
                                        vm.itemReferences = [];
                                        $rootScope.hideBusyIndicator();
                                        $scope.callback();
                                    }
                                )
                            }
                        });
                    }
                }
            }

            function loadProject() {
                ProjectService.getProject(projectId).then(
                    function (data) {
                        vm.project = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.mode = $scope.data.referenceItemMode;
                    vm.activityId = $scope.data.activityIdData;
                    vm.taskId = $scope.data.taskIdData;
                    $rootScope.$on('app.project.itemReferences', create);
                    if (vm.mode == "PROJECT") {
                        loadProject();
                    }
                    loadItems();
                    vm.selectedItems = [];
                //}
            })();
        }
    }
);