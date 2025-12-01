define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/pmObjectTypeService'
    ],

    function (module) {
        module.directive('requirementWfTree', ['$compile', '$timeout', '$rootScope', 'PMObjectTypeService', '$translate', function ($compile, $timeout, $rootScope, PMObjectTypeService, $translate) {
            return {
                templateUrl: 'app/desktop/modules/classification/workflowDirective/workflowRequirementsDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var projectManagementTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = projectManagementTree.tree('getData', node.target);
                        var objectType = data.attributes.pmType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(objectType, $scope.obj);
                        } else {
                            $scope.onSelectType(objectType);
                        }
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        projectManagementTree = $('#requirementWfTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Requirements',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        objectType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = projectManagementTree.tree('find', 0);
                    }

                    function loadTree() {
                        PMObjectTypeService.getReqTypeTree().then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (changeType) {
                                    var node = null;
                                    node = makePMReqNode(changeType);
                                    if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, changeType.childrens);
                                    }

                                    nodes.push(node);
                                });

                                projectManagementTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }

                    function visitChildren(parent, changeTypes) {
                        var nodes = [];
                        angular.forEach(changeTypes, function (changeType) {
                            var node = null;
                            node = makePMReqNode(changeType);

                            if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, changeType.childrens);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }


                    function makePMReqNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'req-node',
                            attributes: {
                                pmType: type
                            }
                        };
                    }

                    initTree();
                    loadTree();
                }
            };
        }]);
    }
);
