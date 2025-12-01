define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/pmObjectTypeService'
    ],

    function (module) {
        module.directive('reqManagementTypeTree', ['$compile', '$timeout', '$rootScope', 'PMObjectTypeService', function ($compile, $timeout, $rootScope, PMObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/reqObjectTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'objectType': "@",
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
                        if ($scope.objectType == "REQUIREMENTDOCUMENTTYPE") {
                            $scope.textValue = 'Requirements Document'
                        } else if ($scope.objectType == "REQUIREMENTTYPE") {
                            $scope.textValue = 'Requirement'
                        } else  {
                            $scope.textValue = 'Project Management'
                        }
                        projectManagementTree = $('#reqManagementTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: $scope.textValue,
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
                        var promise = null;
                        if ($scope.objectType == "REQUIREMENTDOCUMENTTYPE") {
                            promise = PMObjectTypeService.getReqDocTypeTree();
                        } else if ($scope.objectType == "REQUIREMENTTYPE") {
                            promise = PMObjectTypeService.getReqTypeTree();
                        } else if ($scope.objectType == "PROJECT" || $scope.objectType == "PROGRAM") {
                            // promise = PMObjectTypeService.getPmTypeTree();
                           promise = PMObjectTypeService.getProjectTypeTree($scope.objectType);
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (changeType) {
                                        var node = null;
                                        if ($scope.objectType == "REQUIREMENTDOCUMENTTYPE") {
                                            node = makePMReqDocNode(changeType);
                                        } else if ($scope.objectType == "REQUIREMENTTYPE") {
                                            node = makePMReqNode(changeType);
                                        } else if ($scope.objectType == "PROJECT" || $scope.objectType == "PROGRAM") {
                                            node = makeProjectNode(changeType);
                                        }

                                        if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, changeType.childrens);
                                        }
                                        if (changeType.children != null && changeType.children != undefined && changeType.children.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, changeType.children);
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
                            )
                        }
                    }

                    function visitChildren(parent, changeTypes) {
                        var nodes = [];
                        angular.forEach(changeTypes, function (changeType) {
                            var node = null;
                            if ($scope.objectType == "REQUIREMENTDOCUMENTTYPE") {
                                node = makePMReqDocNode(changeType);
                            } else if ($scope.objectType == "REQUIREMENTTYPE") {
                                node = makePMReqNode(changeType);
                            } else if ($scope.objectType == "PROJECT" || $scope.objectType == "PROGRAM") {
                                node = makeProjectNode(changeType);
                            }

                            if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, changeType.childrens);
                            }
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

                    function makeProjectNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'project-node',
                            attributes: {
                                pmType: type
                            }
                        };
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

                    function makePMReqDocNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'spec-node',
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
