define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/ecoService'
    ],

    function (module) {
        module.directive('workflowChange', ['$compile', '$timeout', '$rootScope', 'ECOService', function ($compile, $timeout, $rootScope, ECOService) {
            return {
                templateUrl: 'app/desktop/modules/classification/workflowDirective/workflowChangeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var changeTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = changeTree.tree('getData', node.target);
                        var changeType = data.attributes.changeType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(changeType, $scope.obj);
                        } else {
                            $scope.onSelectType(changeType);
                        }
                        window.$("body").trigger("click");
                    }

                    function initChangeTree() {
                        changeTree = $('#workflowChange').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Change',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        changeType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = changeTree.tree('find', 0);
                    }

                    function loadChangeTree() {
                        ECOService.getChangeTypeTree().then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (changeType) {
                                    var node = makeNode(changeType);

                                    if (changeType.children != null && changeType.children != undefined && changeType.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, changeType.children);
                                    }

                                    nodes.push(node);
                                });

                                changeTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    function visitChildren(parent, changeTypes) {
                        var nodes = [];
                        angular.forEach(changeTypes, function (changeType) {
                            var node = makeNode(changeType);

                            if (changeType.children != null && changeType.children != undefined && changeType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, changeType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(changeType) {
                        return {
                            id: ++nodeId,
                            text: changeType.name,
                            iconCls: 'change-node',
                            attributes: {
                                changeType: changeType
                            }
                        };
                    }

                    initChangeTree();
                    loadChangeTree();
                }
            };
        }]);
    }
);
