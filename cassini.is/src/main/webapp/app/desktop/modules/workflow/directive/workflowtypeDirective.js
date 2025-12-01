define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('workflowTypeTree', ['$compile', '$timeout', 'ClassificationService', function ($compile, $timeout, ClassificationService) {
            return {
                templateUrl: 'app/desktop/modules/workflow/directive/workflowtypeDirective.jsp',
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
                        var workflowType = data.attributes.workflowType;
                        $scope.onSelectType(workflowType);
                        window.$("body").trigger("click");
                    }

                    function initWorkflowTree() {
                        classificationTree = $('#workflowTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Workflow',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        workflowType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadWorkflowTree() {
                        ClassificationService.getClassificationTree('WORKFLOWTYPE').then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (workflowType) {
                                    var node = makeNode(workflowType);

                                    if (workflowType.children != null && workflowType.children != undefined && workflowType.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, workflowType.children);
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

                    function visitChildren(parent, workflowTypes) {
                        var nodes = [];
                        angular.forEach(workflowTypes, function (workflowType) {
                            var node = makeNode(workflowType);

                            if (workflowType.children != null && workflowType.children != undefined && workflowType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, workflowType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(workflowType) {
                        return {
                            id: ++nodeId,
                            text: workflowType.name,
                            iconCls: 'workflow-node',
                            attributes: {
                                workflowType: workflowType
                            }
                        };
                    }

                    initWorkflowTree();
                    loadWorkflowTree();
                }
            };
        }]);
    }
);
