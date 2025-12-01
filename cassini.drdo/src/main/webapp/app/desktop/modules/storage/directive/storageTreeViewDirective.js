define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/storageService'
    ],

    function (module) {
        module.directive('storagesTree', ['$compile', '$timeout', 'StorageService',
            function ($compile, $timeout, StorageService) {
                return {
                    templateUrl: 'app/desktop/modules/storage/directive/storageTreeViewDirective.jsp',
                    restrict: 'E',
                    replace: false,
                    scope: {
                        'onSelectType': '='
                    },

                    link: function ($scope, elm, attr) {

                        var nodeId = 0;
                        var storagesTree = null;
                        var rootNode = null;

                        function onSelectType(node) {
                            var data = storagesTree.tree('getData', node.target);
                            var itemType = data.object;
                            $scope.onSelectType(itemType);
                            window.$("body").trigger("click");
                        }

                        function initStorageTree() {
                            storagesTree = $('#storagesTree').tree({
                                data: [
                                    {
                                        id: nodeId,
                                        text: 'Storage',
                                        iconCls: 'classification-root',
                                        attributes: {
                                            itemType: null
                                        },
                                        children: []
                                    }
                                ],
                                onSelect: onSelectType
                            });
                            rootNode = storagesTree.tree('find', 0);
                        }

                        function loadStorageTree() {
                            StorageService.getStorageTree().then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (object) {
                                        var node = makeNode(object);

                                        if (object.children != null && object.children != undefined && object.children.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, object.children);
                                        }

                                        nodes.push(node);
                                    });

                                    storagesTree.tree('append', {
                                        parent: rootNode.target,
                                        data: nodes
                                    });
                                }
                            );
                        }

                        function visitChildren(parent, itemTypes) {
                            var nodes = [];
                            parent.state = 'open';
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

                        function makeNode(object) {
                            var treeNode = {
                                id: ++nodeId,
                                text: object.name,
                                object: object,
                                type: object.type,
                                iconCls: null
                            };

                            if (object.type == 'WAREHOUSE') {
                                if (object.returned) {
                                    treeNode.iconCls = 'wh-ret-node';
                                } else if (object.onHold) {
                                    treeNode.iconCls = 'wh-on-node';
                                } else {
                                    treeNode.iconCls = 'wh-node';
                                }
                            } else if (object.type == 'STOCKROOM') {
                                treeNode.iconCls = 'str-node';
                            } else if (object.type == 'SHELF') {
                                treeNode.iconCls = 'shelf-node;'
                            } else if (object.type == 'BIN') {
                                treeNode.iconCls = 'bin-node';
                            } else if (object.type == 'RACK') {
                                treeNode.iconCls = 'rack-node';
                            } else if (object.type == 'AREA') {
                                treeNode.iconCls = 'area-node';
                            } else {
                                treeNode.iconCls = 'bom-node';
                            }

                            return treeNode;
                        }

                        initStorageTree();
                        loadStorageTree();
                    }
                };
            }]);
    }
);
