define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/qualityTypeService'
    ],

    function (module) {
        module.directive('workflowQuality', ['$compile', '$timeout', 'QualityTypeService', function ($compile, $timeout, QualityTypeService) {
            return {
                templateUrl: 'app/desktop/modules/classification/workflowDirective/qualityWorkflowDirective.jsp',
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
                        var qualityType = data.attributes.qualityType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(qualityType, $scope.obj);
                        } else {
                            $scope.onSelectType(qualityType);
                        }
                        window.$("body").trigger("click");
                    }

                    function initQualityWfTree() {
                        classificationTree = $('#workflowQuality').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: "Quality",
                                    iconCls: 'classification-root',
                                    attributes: {
                                        qualityType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadQualityWfClassification() {
                        QualityTypeService.getQualityTypeTree().then(
                            function (data) {
                                if (data.length > 0) {
                                }
                                var nodes = [];
                                angular.forEach(data, function (type) {
                                    var node = makeQualityNode(type);

                                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, type.children);
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

                    function visitChildren(parent, qualityTypes) {
                        var nodes = [];
                        angular.forEach(qualityTypes, function (qualityType) {
                            var node = makeQualityNode(qualityType);

                            if (qualityType.children != null && qualityType.children != undefined && qualityType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, qualityType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeQualityNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'quality-node',
                            attributes: {
                                qualityType: type,
                                nodeType: "QUALITY_TYPE"
                            }
                        };
                    }

                    initQualityWfTree();
                    loadQualityWfClassification();
                }
            };
        }]);
    }
);
