define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('receiveTypeTree', ['$compile', '$timeout', 'ClassificationService', function ($compile, $timeout, ClassificationService) {
            return {
                templateUrl: 'app/desktop/modules/stores/details/tabs/receives/directive/receiveTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var classificationTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = classificationTree.tree('getData', node.target);
                        var itemType = data.attributes.itemType;
                        $scope.onSelectType(itemType);
                        window.$("body").trigger("click");
                    }

                    function initReceiveTypeTree() {
                        classificationTree = $('#receiveTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'ReceiveType',
                                    iconCls: 'material-node',
                                    attributes: {
                                        itemType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadReceiveTypeTree() {
                        ClassificationService.getClassificationTree('MATERIALRECEIVETYPE').then(
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
                        )
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
                            iconCls: 'material-node',
                            attributes: {
                                itemType: itemType
                            }
                        };
                    }

                    initReceiveTypeTree();
                    loadReceiveTypeTree();
                }
            };
        }]);
    }
);
