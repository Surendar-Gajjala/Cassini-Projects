define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/testTreeService'
    ],

    function (module) {
        module.directive('testTree', ['$compile', '$timeout', 'TestTreeService', function ($compile, $timeout, TestTreeService) {
            return {
                templateUrl: 'app/desktop/modules/directive/testTreeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '='
                },


                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var testTree = null;
                    var rootNode = null;


                    function onSelectType(node) {
                        var data = testTree.tree('getData', node.target);
                        var type = data.attributes.type;
                        $scope.onSelectType(type);
                        window.$("body").trigger("click");
                    }


                    function initClassificationTree() {
                        testTree = $('#testTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Test',
                                    iconCls: '',
                                    object:null,
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = testTree.tree('find', 0);
                    }

                    function loadClassificationTree() {
                        TestTreeService.getTestTree().then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (type) {
                                    var node = makeNode(type);

                                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, type.children);
                                    }

                                    nodes.push(node);
                                });

                                testTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }
                        )
                    }

                    function visitChildren(parent, types) {
                        var nodes = [];
                        angular.forEach(types, function (type) {
                            var node = makeNode(type);

                            if (type.children != null && type.children != undefined && type.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, type.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'classification-root',
                            object:type
                        };
                    }

                    initClassificationTree();
                    loadClassificationTree();
                }
            };
        }]);
    }
);
