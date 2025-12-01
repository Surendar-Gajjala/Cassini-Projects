define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/pmObjectTypeService'
    ],

    function (module) {
        module.directive('projectTypeTree', ['$compile', '$timeout', '$rootScope', 'PMObjectTypeService', function ($compile, $timeout, $rootScope, PMObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/projectTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'objectType': "@",
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var projectTypeTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = projectTypeTree.tree('getData', node.target);
                        var objectType = data.attributes.pmType;
                        $scope.onSelectType(objectType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        $scope.textValue = 'Project Management'
                        projectTypeTree = $('#projectTypeTree').tree({
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

                        rootNode = projectTypeTree.tree('find', 0);
                    }

                    function loadTree() {
                        var promise = null;
                        if ($scope.objectType == "PROJECT" || $scope.objectType == "PROGRAM") {
                            promise = PMObjectTypeService.getProjectTypeTree($scope.objectType);
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (changeType) {
                                        var node = null;
                                        if ($scope.objectType == "PROJECT" || $scope.objectType == "PROGRAM") {
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

                                    projectTypeTree.tree('append', {
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
                            if ($scope.objectType == "PROJECT" || $scope.objectType == "PROGRAM") {
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


                    initTree();
                    loadTree();
                }
            };
        }]);
    }
);
