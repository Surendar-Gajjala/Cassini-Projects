define(
    [
        'app/desktop/modules/storage/storage.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/bomService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('StoragePartSelectionController', StoragePartSelectionController);

        function StoragePartSelectionController($scope, $rootScope, $timeout, $interval, $state, $cookies, BomService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            var bomStructure = null;
            var nodeId = 0;
            var rootNode = null;
            var selectedBom = $scope.data.bomDetails;
            var selectedStorage = $scope.data.storageDetails;
            vm.selectedBomType = selectedStorage.bom;
            //vm.selectedBomType = null;
            vm.searchText = null;

            function initBomStructureTree() {
                bomStructure = $('#bomStructure').tree({
                    data: [
                        {
                            id: nodeId,
                            text: selectedBom.item.itemMaster.itemName,
                            iconCls: 'bom-root',
                            type: null,
                            object: null,
                            children: []
                        }
                    ],
                    onSelect: onSelectType
                });

                rootNode = bomStructure.tree('find', 0);
            }

            vm.selectedType = null;
            vm.uniqueParts = [];
            vm.commonParts = [];
            function onSelectType(node) {
                vm.uniqueParts = [];
                vm.commonParts = [];
                vm.selectedType = bomStructure.tree('getData', node.target);
                vm.loading = true;
                vm.selectedBomType = vm.selectedType.object;
                if (vm.searchText == null) {
                    loadParts();
                } else {
                    searchParts();
                }
                $('#bomStructure').tree('expandAll', node.target);

            }

            function checkIfAlreadyAdded(part) {
                var found = false;

                angular.forEach($rootScope.selectedStorageParts, function (storagePart) {
                    if (part.uniqueCode == storagePart.uniqueCode) {
                        found = true;
                    }
                })

                return found;
            }

            function loadBomStructure() {
                $rootScope.showBusyIndicator($("#leftSidePanel"));
                BomService.getBomStructureByBom(selectedBom.id).then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (object) {
                            var node = makeNode(object);

                            if (object.children != null && object.children != undefined && object.children.length > 0) {

                                visitChildren(node, object.children);
                            }
                            nodes.push(node);
                        });

                        bomStructure.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function visitChildren(parent, objects) {
                var nodes = [];
                angular.forEach(objects, function (object) {
                    if (object.bomItemType != "PART") {
                        parent.state = "closed";
                        var node = makeNode(object);
                        if (object.children != null && object.children != undefined && object.children.length > 0) {
                            node.state = "closed";
                            visitChildren(node, object.children);
                        }
                        nodes.push(node);
                    } else {
                        parent.state = null;
                    }
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(object) {
                var treeNode = {
                    id: ++nodeId,
                    text: object.typeRef.name,
                    object: object,
                    type: object.type
                };

                return treeNode;
            }

            vm.addPartToStorage = addPartToStorage;
            function addPartToStorage(part) {
                $rootScope.addSelectPartToStorage(part);
                vm.searchResults.numberOfElements = vm.searchResults.numberOfElements - 1;
                vm.searchResults.totalElements = vm.searchResults.totalElements - 1;
                vm.searchResults.content.splice(vm.searchResults.content.indexOf(part), 1);
            }

            vm.addAllPartsToStorage = addAllPartsToStorage;
            function addAllPartsToStorage() {
                var count = 0;
                angular.forEach(vm.searchResults.content, function (result) {
                    count++;
                    $rootScope.addSelectPartToStorage(result);

                    if (count == vm.searchResults.content.length) {
                        vm.searchResults.content = [];
                        vm.searchResults.numberOfElements = 0;
                        vm.searchResults.totalElements = 0;
                    }
                })
            }

            function close() {
                $rootScope.hideSidePanel('left');
            }


            vm.locationsPopover = {
                templateUrl: 'app/desktop/modules/storage/details/selection/locationsPopover.jsp'
            };

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "createdDate",
                    order: "ASC"
                }
            };

            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            function resetPage() {
                loadParts();
            }

            function freeTextSearch(freeText) {
                if (freeText != null && freeText != "" && freeText != undefined) {
                    $scope.freeTextQuery = freeText;
                    searchParts();

                } else {
                    $scope.freeTextQuery = null;
                    resetPage();
                }
            }

            function loadParts() {
                vm.uniqueParts = [];
                vm.commonParts = [];
                if (vm.selectedBomType.objectType == "BOMITEM") {
                    $rootScope.showBusyIndicator($("#leftSidePanel"));
                    vm.loading = true;
                    BomService.getBomItemBySelectedType(vm.selectedBomType.id, selectedStorage.id).then(
                        function (data) {

                            angular.forEach(data.uniqueParts, function (uniquePart) {
                                if (!checkIfAlreadyAdded(uniquePart)) {
                                    vm.uniqueParts.push(uniquePart);
                                }
                            });

                            angular.forEach(data.commonParts, function (uniquePart) {
                                if (!checkIfAlreadyAdded(uniquePart)) {
                                    vm.commonParts.push(uniquePart);
                                }
                            });

                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.searchFilter = {
                searchQuery: null,
                bom: '',
                section: '',
                subsystem: '',
                unit: ''
            };

            function searchParts() {
                vm.uniqueParts = [];
                vm.commonParts = [];
                vm.loading = true;
                vm.searchFilter.bom = vm.selectedBomType.id;
                if (selectedStorage == null || selectedStorage == undefined) {
                    selectedStorage = 0;
                }
                $rootScope.showBusyIndicator($("#leftSidePanel"));
                BomService.searchBomItems(selectedStorage.id, pageable, vm.searchFilter).then(
                    function (data) {
                        vm.searchResults = data;
                        vm.showResults = true;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.showResults = false;
            vm.performSearch = performSearch;
            vm.resetSearch = resetSearch;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            function performSearch() {
                if (vm.searchFilter.searchQuery.length > 1) {
                    searchParts();
                }
            }

            function resetSearch() {
                pageable.page = 0;
                vm.searchFilter.searchQuery = null;
                vm.showResults = false;
            }

            function nextPage() {
                if (vm.searchResults.last != true) {
                    pageable.page++;
                    searchParts();
                }
            }

            function previousPage() {
                if (vm.searchResults.first != true) {
                    pageable.page--;
                    searchParts();
                }
            }

            function resize() {
                var content = $("#leftSidePanelContent").outerHeight();
                $("#storageParts-view").height(content);

                var search = $("#storageParts-search").outerHeight();
                var contentView = $("#storageParts-view").outerHeight();

                $("#storageParts-content").height(contentView - search);
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('app.storage.details.parts', close);
                    $timeout(function () {
                        $('div.split-right-pane').css({left: 300});
                        $('div.split-pane').splitPane();
                        resize();
                        //initBomStructureTree();
                        //loadBomStructure();
                    }, 1000)
                }
            })();
        }
    }
);