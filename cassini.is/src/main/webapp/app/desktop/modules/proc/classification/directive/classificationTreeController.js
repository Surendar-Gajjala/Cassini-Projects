define(
    [
        'app/desktop/desktop.app',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('ClassificationTreeController', ClassificationTreeController);

        function ClassificationTreeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                              ItemTypeService) {
            var vm = this;

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;

            function onSelectType(node) {
                var data = classificationTree.tree('getData', node.target);
                var itemType = data.attributes.itemType;
                window.$("body").trigger("click");
            }

            function initClassificationTree() {
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Classification',
                            iconCls: 'classification-root',
                            attributes: {
                                itemType: null
                            },
                            children: [],
                            onSelect: onSelectType
                        }
                    ]
                });

                rootNode = classificationTree.tree('find', 0);
            }

            function loadClassificationTree() {
                ItemTypeService.getClassificationTree().then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (itemType) {
                            var node = makeNode(itemType);

                            if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                                node.state = "closed";
                                visitFoders(node, itemType.children);
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

            function visitFoders(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    var node = makeNode(itemType);

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = 'closed';
                        visitFoders(node, itemType.children);
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

            (function () {
                if ($application.homeLoaded == true) {
                    $scope.$on('$viewContentLoaded', function () {
                        initClassificationTree();
                        loadClassificationTree();
                    });
                }
            })();
        }
    }
);