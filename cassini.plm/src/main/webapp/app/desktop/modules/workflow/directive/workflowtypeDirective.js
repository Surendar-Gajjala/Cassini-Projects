define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/workflowService'
    ],

    function (module) {
        module.directive('workflowTypeTree', ['$compile', '$timeout', '$rootScope', 'WorkflowService', function ($compile, $timeout, $rootScope, WorkflowService) {
            return {
                templateUrl: 'app/desktop/modules/workflow/directive/workflowtypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var classificationTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = classificationTree.tree('getData', node.target);
                        var workflowType = data.attributes.workflowType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(workflowType, $scope.obj);
                        } else {
                            $scope.onSelectType(workflowType);
                        }

                        window.$("body").trigger("click");
                    }

                    function initWorkflowTree() {
                        var treeId = 'workflowTypeTree_' + Math.random().toString(36).slice(2);
                        $(elm[0].children[1].children[0]).prop('id', treeId);
                        classificationTree = $(document.getElementById(treeId)).tree({
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
                        WorkflowService.getWorkflowTypeTree().then(
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
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
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
                            iconCls: 'wk-node',
                            attributes: {
                                workflowType: workflowType
                            }
                        };
                    }

                    (function () {
                        initWorkflowTree();
                        loadWorkflowTree();
                    })();
                }
            };
        }]);
    }
);
