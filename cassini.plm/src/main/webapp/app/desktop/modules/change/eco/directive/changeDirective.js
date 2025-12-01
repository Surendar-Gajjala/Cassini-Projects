define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('changeTree', ['$compile', '$timeout', 'ClassificationService','$rootScope', function ($compile, $timeout, ClassificationService, $rootScope) {
            return {
                templateUrl: 'app/desktop/modules/change/eco/directive/changeDirective.jsp',
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
                        var changeType = data.attributes.changeType;
                        $scope.onSelectType(changeType);
                        window.$("body").trigger("click");
                    }

                    function initChangeTree() {
                        classificationTree = $('#changeTree').tree({
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

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadChangeTree() {
                        ClassificationService.getClassificationTree('CHANGETYPE').then(
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

                                classificationTree.tree('append', {
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
