define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/reqDocumentService',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/shared/services/core/reqDocumentService'
    ],

    function (module) {
        module.controller('ReqDocumentsSelectionController', ReqDocumentsSelectionController);

        function ReqDocumentsSelectionController($scope, $rootScope, $timeout, $translate, $stateParams, $state,
                                                 ProjectService, ActivityService, ItemService, ReqDocumentService) {

            var vm = this;

            var projectId = $scope.data.selectedProjectId;
            var project = $scope.data.selectedProject;
            vm.selectedItems = [];
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.reqDocuments.last != true) {
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadReqDocuments();
                }
            }

            function previousPage() {
                if (vm.reqDocuments.first != true) {
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadReqDocuments();
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
                number: null,
                type: '',
                name: null,
                description: null,
                searchQuery: null,
                project: '',
                owner: '',
                reqType: null
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
                loadReqDocuments();
                vm.clear = true;
            }

            function clearFilter() {
                vm.filters = {
                    number: null,
                    type: '',
                    name: null,
                    description: null,
                    searchQuery: null,
                    project: '',
                    owner: '',
                    reqType: null
                };
                vm.selectedType = null;
                vm.selectedItems = [];
                vm.pageable.page = 0;
                loadReqDocuments();
                vm.clear = false;
            }

            vm.selectAllCheck = false;

            function selectAll() {
                vm.selectedItems = [];
                if (vm.selectAllCheck == false) {
                    angular.forEach(vm.reqDocuments.content, function (item) {
                        item.selected = false;
                    })
                    vm.selectedItems = [];

                } else {
                    vm.error = "";
                    angular.forEach(vm.reqDocuments.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                    if (vm.selectedItems.length == vm.reqDocuments.content.length) {
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
                    if (vm.selectedItems.length == vm.reqDocuments.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            var parsed = angular.element("<div></div>");
            var atLeastReqDocValidation = parsed.html($translate.instant("ATLEAST_ONE_REQUIREMENT_DOCUMENT")).html();
            var reqDocAdded = parsed.html($translate.instant("REQ_DOC_ADDED")).html();
            vm.projectReqDocs = [];
            function create() {
                if (vm.selectedItems.length == 0) {
                    $rootScope.showWarningMessage(atLeastReqDocValidation);
                } else {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.projectReqDocs = [];
                    angular.forEach(vm.selectedItems, function (item) {
                        vm.projectReqDoc = {
                            id: null,
                            project: project,
                            reqDocument: null
                        };
                        vm.projectReqDoc.reqDocument = item.latestRevisionObject;
                        vm.projectReqDocs.push(vm.projectReqDoc);
                        if (vm.selectedItems.length == vm.projectReqDocs.length) {
                            ProjectService.createProjectReqDocuments(projectId, vm.projectReqDocs).then(
                                function (data) {
                                    vm.selectedItems = [];
                                    vm.projectReqDocs = [];
                                    $scope.callback();
                                    $rootScope.loadProjectCounts();
                                    $rootScope.showSuccessMessage(reqDocAdded);
                                    $rootScope.hideSidePanel();
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

            function loadReqDocuments() {
                vm.filters.project = projectId;
                ReqDocumentService.getAllReqDocuments(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.reqDocuments = data;
                        vm.loading = false;
                        ReqDocumentService.getReqDocLatestRevisionReferences(vm.reqDocuments.content, 'latestRevision');
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                $rootScope.$on('app.project.reqDocument.add', create);
                loadReqDocuments();
            })();
        }
    }
);