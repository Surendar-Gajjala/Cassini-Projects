define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/bomService'
    ],

    function (module) {
        module.directive('bomTree', ['$compile', '$timeout', 'BomService', function ($compile, $timeout, BomService) {
            return {
                templateUrl: 'app/desktop/modules/bom/directive/bomTreeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var bomMainTree = null;
                    var rootNode = null;


                    function onSelectType(node) {
                        var data = bomMainTree.tree('getData', node.target);
                        var item = data.attributes.item;
                        $scope.onSelectType(item);
                        window.$("body").trigger("click");
                    }

                    function initBomTree() {
                        bomMainTree = $('#bomTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'BOM',
                                    iconCls: 'bom-root',
                                    attributes: {
                                        item: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = bomMainTree.tree('find', 0);

                        $(document).click(function () {
                            $("#contextMenu").hide();
                        });

                        loadBomTree();
                    }

                    var nodes = [];

                    function loadBomTree() {
                        BomService.getBomTree().then(
                            function (data) {
                                nodes = [];
                                angular.forEach(data, function (item) {
                                    var node = makeNode(item);
                                    if (item.children != null && item.children != undefined && item.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, item.children);
                                    }
                                    nodes.push(node);
                                });

                                bomMainTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    function visitChildren(parent, items) {
                        var nodes = [];
                        angular.forEach(items, function (item) {
                            var node = makeInstanceNode(item);
                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeInstanceNode(item) {
                        return {
                            id: ++nodeId,
                            text: item.item.instanceName,
                            iconCls: 'bom-inst',
                            attributes: {
                                item: item
                            }
                        };
                    }

                    function makeNode(item) {
                        return {
                            id: ++nodeId,
                            text: item.item.itemMaster.itemName,
                            iconCls: 'bom-node',
                            attributes: {
                                item: item
                            }
                        };
                    }

                    (function () {
                        initBomTree();
                    })()
                }
            };
        }]);
    }
);
