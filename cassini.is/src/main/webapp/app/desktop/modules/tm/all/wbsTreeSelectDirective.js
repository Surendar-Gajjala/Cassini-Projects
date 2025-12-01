define(
    [
        'app/desktop/modules/tm/tm.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/pm/project/wbsService'
    ],

    function (module) {
        module.directive('wbsTreeSelect', ['$compile', '$timeout', '$stateParams', 'WbsService', function ($compile, $timeout, $stateParams, WbsService) {
            return {
                templateUrl: 'app/desktop/modules/tm/all/wbsTreeSelectDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectWbs': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var wbsTree = null;
                    var rootNode = null;

                    function initWbsTree() {
                        wbsTree = $('#wbsTreeSelect').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Wbs',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        itemType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: $scope.onSelectWbs
                        });

                        rootNode = wbsTree.tree('find', 0);

                        $(document).click(function () {
                            $("#contextMenu").hide();
                        });
                    }

                    function loadWbsTree() {
                        WbsService.getWbsTree($stateParams.projectId).then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (wbs) {
                                    var node = makeNode(wbs);

                                    if (wbs.children != null && wbs.children != undefined && wbs.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, wbs.children);
                                    }

                                    nodes.push(node);
                                });
                                rootNode.state = "closed";
                                wbsTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }
                        );
                    }

                    function visitChildren(parent, wbss) {
                        var nodes = [];
                        angular.forEach(wbss, function (wbs) {
                            var node = makeNode(wbs);

                            if (wbs.children != null && wbs.children != undefined && wbs.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, wbs.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(wbs) {
                        return {
                            id: ++nodeId,
                            text: wbs.name,
                            iconCls: 'itemtype-node',
                            attributes: {
                                wbs: wbs
                            }
                        };
                    }

                    initWbsTree();
                    loadWbsTree();
                }
            };
        }]);
    }
);
