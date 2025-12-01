define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/itemTypeService'
    ],

    function (module) {
        module.directive('itemClassification', ['$compile', '$timeout', 'ItemTypeService', function ($compile, $timeout, ItemTypeService) {
            return {
                templateUrl: 'app/desktop/modules/classification/workflowDirective/itemClassificationDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'obj': "="
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var itemClassification = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = itemClassification.tree('getData', node.target);
                        var itemType = data.attributes.itemType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(itemType, $scope.obj);
                        } else {
                            $scope.onSelectType(itemType);
                        }
                        window.$("body").trigger("click");
                    }

                    function initClassificationTree() {
                        itemClassification = $('#itemClassification').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Classification',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        itemType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = itemClassification.tree('find', 0);
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

                                itemClassification.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
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
                            iconCls: 'itemtype-node',
                            attributes: {
                                itemType: itemType
                            }
                        };
                    }

                    initClassificationTree();
                    loadClassificationTree();
                }
            };
        }]);
    }
);
