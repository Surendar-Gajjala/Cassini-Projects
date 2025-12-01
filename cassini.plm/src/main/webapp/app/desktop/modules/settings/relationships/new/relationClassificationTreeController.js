define(
    [
        'app/desktop/desktop.app',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('RelationClassificationTreeController', RelationClassificationTreeController);

        function RelationClassificationTreeController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                                      ItemTypeService) {

            var vm = this;

            var nodeId = 0;
            var relationClassificationTree = null;
            var rootNode = null;

            function onSelectType(node) {
                var data = relationClassificationTree.tree('getData', node.target);
                var itemType = data.attributes.itemType;
                window.$("body").trigger("click");
            }

            function initClassificationTree() {
                relationClassificationTree = $('#relationClassificationTree').tree({
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

                rootNode = relationClassificationTree.tree('find', 0);
            }

            function loadClassificationTree() {
                ItemTypeService.getItemTypeTree().then(
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

                        relationClassificationTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
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
                $scope.$on('$viewContentLoaded', function () {
                    initClassificationTree();
                    loadClassificationTree();
                });
            })();
        }
    }
);