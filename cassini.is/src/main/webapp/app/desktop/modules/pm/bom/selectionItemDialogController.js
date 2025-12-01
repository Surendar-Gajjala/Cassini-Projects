define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/itemService',
        "app/desktop/modules/proc/classification/directive/classificationTreeDirective",
        "app/desktop/modules/proc/classification/directive/classificationTreeController",
        'app/shared/services/core/classificationService'
    ],
    function (module) {
        module.controller('SelectionItemDialogController', SelectionItemDialogController);

        function SelectionItemDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ClassificationService, ItemService) {

            $rootScope.viewInfo.icon = "fa flaticon-deadlines";
            $rootScope.viewInfo.title = "BOQ";

            var vm = this;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.items = angular.copy(pagedResults);
            var machineItems = $scope.data.machineItems;
            var materialItems = $scope.data.materialItems;
            var selectedItems = [];

            vm.loading = true;
            vm.itemType = null;
            vm.hasError = false;
            vm.selectedAll = false;

            vm.checkAll = checkAll;
            vm.create = create;
            vm.onSelectType = onSelectType;
            vm.selectCheck = selectCheck;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;

            function selectCheck(item) {
                var flag = true;
                if (item.selected == false) {
                    vm.selectedAll = false;
                    var index = selectedItems.indexOf(item);
                    selectedItems.splice(index, 1);
                }
                else {
                    angular.forEach(selectedItems, function (selectedItem) {
                        if (selectedItem.id == item.id) {
                            flag = false;
                            var index = selectedItems.indexOf(item);
                            selectedItems.splice(index, 1);
                        }
                    });
                    if (flag) {
                        selectedItems.push(item);
                    }
                }
                if (selectedItems.length == vm.items.content.length) {
                    vm.selectedAll = true;
                }
                else {
                    vm.selectedAll = false;
                }
            }

            function checkAll() {
                if (vm.selectedAll) {
                    vm.selectedAll = true;
                } else {
                    vm.selectedAll = false;
                }
                angular.forEach(vm.items.content, function (item) {
                    item.selected = vm.selectedAll;
                })
            }

            function create() {
                var items = [];
                var bomItems = $scope.data.bomItems;
                angular.forEach(vm.items.content, function (item) {
                    if (item.selected) {
                        items.push(item);
                    }
                });
                if (items.length > 0) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(items);
                }
                else {
                    $rootScope.showErrorMessage("Please add atleast one item");
                }
            }

            function nextPage() {
                if (!vm.items.last) {
                    vm.pageable.page++;
                    if (vm.itemType.attributes.typeObject.objectType == "MATERIALTYPE") {
                        loadMaterialItems(vm.itemType);
                    } else if (vm.itemType.attributes.typeObject.objectType == "MACHINETYPE") {
                        loadMachineItems(vm.itemType);
                    }
                }

            }

            function previousPage() {
                if (!vm.items.first) {
                    vm.pageable.page--;
                    if (vm.itemType.attributes.typeObject.objectType == "MATERIALTYPE") {
                        loadMaterialItems(vm.itemType);
                    } else if (vm.itemType.attributes.typeObject.objectType == "MACHINETYPE") {
                        loadMachineItems(vm.itemType);
                    }
                }
            }

            function initClassificationTree() {
                $rootScope.flag = true;
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Classification',
                            iconCls: 'classification-root',
                            attributes: {
                                typeObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType
                });

                rootNode = classificationTree.tree('find', 0);
            }

            function onSelectType(itemType) {
                vm.selectedAll = false;
                if (itemType != null && itemType != undefined) {
                    vm.selectItem = true;
                    vm.itemType = itemType;
                    if (itemType.attributes.nodeType == "MATERIALTYPE") {
                        $rootScope.$broadcast("app.selectionItems.title", {title: "Select Material Items"});
                        loadMaterialItems(itemType);
                    } else if (itemType.attributes.nodeType == "MACHINETYPE") {
                        $rootScope.$broadcast("app.selectionItems.title", {title: "Select Machine Items"});
                        loadMachineItems(itemType);
                    }

                    $(".dropdown-toggle").click(function (e) {
                        e.stopPropagation();
                        $(".dropdown-menu").hide();
                        $(this).next().show();
                    });

                }
            }

            function loadMaterialItems(itemType) {
                vm.items = angular.copy(pagedResults);
                ItemService.getMaterialItemsByType(itemType.attributes.typeObject.id, vm.pageable).then(
                    function (data) {
                        vm.items = data;
                        angular.forEach(materialItems, function (materialItem) {
                            angular.forEach(vm.items.content, function (item) {
                                if (materialItem.itemNumber == item.itemNumber) {
                                    var index = vm.items.content.indexOf(item);
                                    vm.items.content.splice(index, 1);
                                    vm.items.numberOfElements--;
                                }
                            });

                        });
                        vm.loading = false;
                        $(".dropdown-menu").hide();
                        $(this).next().show();
                    }
                )
            }

            function loadMachineItems(itemType) {
                vm.items = angular.copy(pagedResults);
                ItemService.getMachineItemsByType(itemType.attributes.typeObject.id, vm.pageable).then(
                    function (data) {
                        vm.items = data;
                        vm.loading = false;
                        angular.forEach(machineItems, function (machineItem) {
                            angular.forEach(vm.items.content, function (bomItem) {
                                if (machineItem.itemNumber == bomItem.itemNumber) {
                                    var index = vm.items.content.indexOf(bomItem);
                                    vm.items.content.splice(index, 1);
                                    vm.items.numberOfElements--;
                                }
                            });

                        });
                        $(".dropdown-menu").hide();
                        $(this).next().show();
                    }
                )
            }

            function loadMaterialClassification() {
                var materialRoot = {
                    id: ++nodeId,
                    text: 'Material',
                    iconCls: 'material-node',
                    attributes: {
                        root: true,
                        nodeType: 'MATERIALTYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MATERIALTYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            materialRoot.state = 'closed'
                        }
                        var nodes = [];
                        angular.forEach(data, function (materialType) {
                            var node = makeMaterialNode(materialType);

                            if (materialType.children != null && materialType.children != undefined && materialType.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, materialType.children);
                            }

                            nodes.push(node);
                        });

                        materialRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: materialRoot
                        });

                        loadMachineClassification();
                    }
                )
            }

            function loadMachineClassification() {
                var machineRoot = {
                    id: ++nodeId,
                    text: 'Machine',
                    iconCls: 'machine-node',
                    attributes: {
                        root: true,
                        nodeType: 'MACHINETYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MACHINETYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            machineRoot.state = 'closed'
                        }
                        var nodes = [];
                        angular.forEach(data, function (type) {
                            var node = makeMachineNode(type);

                            if (type.children != null && type.children != undefined && type.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, type.children);
                            }

                            nodes.push(node);
                        });

                        machineRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: machineRoot
                        });
                    }
                )
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    var node = makeMaterialNode(itemType);

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, itemType.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeMaterialNode(materialType) {
                return {
                    id: ++nodeId,
                    text: materialType.name,
                    iconCls: 'material-node',
                    attributes: {
                        typeObject: materialType,
                        nodeType: 'MATERIALTYPE'
                    }
                };
            }

            function makeMachineNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'machine-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MACHINETYPE'
                    }
                };
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.bom.new', create);
                    $timeout(function () {
                        initClassificationTree();
                        loadMaterialClassification();
                    }, 1500);
                    $(".dropdown-toggle").click(function (e) {
                        e.stopPropagation();
                        $(".dropdown-menu").hide();
                        $(this).next().show();
                    });

                    var rightHeight = $("#rightSidePanel").outerHeight();
                    $('#rightSidePanelContent').height(rightHeight - 100);

                    var leftHeight = $("#leftSidePanel").outerHeight();
                    $('#leftSidePanelContent').height(leftHeight - 100);
                }
            })();
        }
    }
);