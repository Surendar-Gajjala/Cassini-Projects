define(['app/desktop/modules/classification/classification.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/itemTypeService',
        'app/desktop/modules/classification/details/itemTypeDetailsController',
        'app/shared/services/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'
    ],
    function (module) {
        module.controller('ClassificationController', ClassificationController);

        function ClassificationController($scope, $rootScope, $timeout, CommonService, ItemService, DialogService, $q, AutonumberService, ItemTypeService, $state, $cookies) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";
            $rootScope.viewInfo.title = "Classification";
            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            vm.autoNumbers = [];
            $rootScope.flag = false;
            vm.addItemType = addItemType;
            vm.deleteItemType = deleteItemType;
            vm.onSave = onSave;

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;

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

            function initClassificationTree() {
                $rootScope.flag = true;
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Classification',
                            iconCls: 'classification-root',
                            attributes: {
                                root: true,
                                itemType: null
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = classificationTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            function onDblClick(node) {
                var data = classificationTree.tree('getData', node.target);
                if (data.id != 0) {
                    classificationTree.tree('beginEdit', node.target);
                }

                $timeout(function () {
                    $('.tree-editor').focus().select();
                }, 1);
            }

            function onAfterEdit(node) {
                var data = classificationTree.tree('getData', node.target);
                if (data.attributes.itemType == null) {
                    data.attributes.itemType = {}
                }

                var parent = classificationTree.tree('getParent', node.target);
                var parentData = classificationTree.tree('getData', parent.target);

                if (parentData.id != 0) {
                    data.attributes.itemType.parentType = parentData.attributes.itemType.id;
                }

                data.attributes.itemType.name = node.text;


                var promise = null;
                if (data.attributes.itemType.id == undefined || data.attributes.itemType.id == null) {
                    promise = ItemTypeService.createItemType(data.attributes.itemType);
                }
                else {
                    promise = ItemTypeService.updateItemType(data.attributes.itemType);
                }
                promise.then(
                    function (itemType) {
                        data.attributes.itemType = angular.copy(itemType);
                        classificationTree.tree('update', {target: node.target, attributes: data.attributes});
                    }
                );
            }

            function onSelectType(node) {
                var data = classificationTree.tree('getData', node.target);
                var itemType = data.attributes.itemType;

                $scope.$broadcast('app.itemType.selected', {itemType: itemType});
                $scope.$apply();
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                var parent = classificationTree.tree('getData', node.target);
                $('#addType').show();
                $('#deleteType').show();
                $contextMenu.show();
                if (node.attributes.root == true) {
                    $('#addType').text('Add Type');
                    $('#deleteType').hide();
                }
                if (node.attributes.root == undefined && parent.id === 0) {
                    $contextMenu.hide();
                }
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }

            function addItemType() {
                var selectedNode = classificationTree.tree('getSelected');

                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    classificationTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'itemtype-node',
                                text: 'New Type',
                                attributes: {
                                    itemType: null
                                }
                            }
                        ]
                    });

                    var newNode = classificationTree.tree('find', nodeid);
                    if (newNode != null) {
                        //classificationTree.tree('select', newNode.target);
                        classificationTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }
            }

            function isExistsInItems(typeId) {
                var defered = $q.defer();
                ItemService.getItemsByType(typeId, vm.pageable).then(
                    function (data) {
                        vm.itemTypes = angular.copy(pagedResults);
                        vm.itemTypes = data.content;
                        if (vm.itemTypes.length != 0) {
                            defered.resolve(true);
                        } else {
                            defered.reject(false);
                        }
                    });

                return defered.promise;
            }

            function deleteItemType() {
                var selectedNode = classificationTree.tree('getSelected');
                if (selectedNode != null) {
                    var data = classificationTree.tree('getData', selectedNode.target);
                    if (data != null && data.attributes.itemType != null) {
                        isExistsInItems(data.attributes.itemType.id).then(
                            function (success) {
                                $rootScope.showErrorMessage("This item type is assigned to item(s) we can't delete");
                            },
                            function (failure) {
                                var options = {
                                    title: 'Delete Type',
                                    message: 'Are you sure you want to delete this type?',
                                    okButtonClass: 'btn-danger'
                                };
                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        ItemTypeService.deleteItemType(data.attributes.itemType.id).then(
                                            function (data) {
                                                classificationTree.tree('remove', selectedNode.target);
                                            });
                                    }
                                });
                            });
                    }
                }
            }


            function loadClassificationTree() {
                ItemTypeService.getClassificationTree().then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (itemType) {
                            var node = makeNode(itemType);

                            if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, itemType.children);
                            }

                            nodes.push(node);
                        });

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                );
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    var node = makeNode(itemType);

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

            function makeNode(itemType) {
                return {
                    id: ++nodeId,
                    text: itemType.name,
                    iconCls: 'itemtype-node',
                    attributes: {
                        itemType: itemType
                    }
                };
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#classificationTree').tree('expandAll');
                }
                $('#classificationTree').tree('doFilter', vm.searchValue);
            }

            function onSave() {
                $scope.$broadcast('app.itemtype.save');
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}});

                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();

                    initClassificationTree();
                    loadClassificationTree();
                }
            })();
        }
    }
);