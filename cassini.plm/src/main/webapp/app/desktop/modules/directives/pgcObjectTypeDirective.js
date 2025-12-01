define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/pgcObjectTypeService'
    ],

    function (module) {
        module.directive('complianceTypeTree', ['$compile', '$timeout', 'PGCObjectTypeService', function ($compile, $timeout, PGCObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/pgcObjectTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'objectType': "@"
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var manufacturingTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = manufacturingTree.tree('getData', node.target);
                        var objectType = data.attributes.pgcType;
                        $scope.onSelectType(objectType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        manufacturingTree = $('#complianceTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Compliance',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        objectType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = manufacturingTree.tree('find', 0);
                    }

                    function loadTree() {
                        var promise = null;
                        if ($scope.objectType == "PGCSUBSTANCETYPE") {
                            promise = PGCObjectTypeService.getSubstanceTypeTree();
                        } else if ($scope.objectType == "PGCSUBSTANCEGROUPTYPE") {
                            promise = PGCObjectTypeService.getSubstanceGroupTypeTree();
                        } else if ($scope.objectType == "PGCSPECIFICATIONTYPE") {
                            promise = PGCObjectTypeService.getSpecificationTypeTree();
                        } else if ($scope.objectType == "PGCDECLARATIONTYPE") {
                            promise = PGCObjectTypeService.getDeclarationTypeTree();
                        }
                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (changeType) {
                                        var node = null;
                                        if ($scope.objectType == "PGCSUBSTANCETYPE") {
                                            node = makeSubstanceNode(changeType);
                                        } else if ($scope.objectType == "PGCSUBSTANCEGROUPTYPE") {
                                            node = makeSubstanceGroupNode(changeType);
                                        } else if ($scope.objectType == "PGCSPECIFICATIONTYPE") {
                                            node = makeSpecificationNode(changeType);
                                        } else if ($scope.objectType == "PGCDECLARATIONTYPE") {
                                            node = makeDeclarationNode(changeType);
                                        }

                                        if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, changeType.childrens);
                                        }

                                        nodes.push(node);
                                    });

                                    manufacturingTree.tree('append', {
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
                            if ($scope.objectType == "PGCSUBSTANCETYPE") {
                                node = makeSubstanceNode(changeType);
                            } else if ($scope.objectType == "PGCSUBSTANCEGROUPTYPE") {
                                node = makeSubstanceGroupNode(changeType);
                            } else if ($scope.objectType == "PGCSPECIFICATIONTYPE") {
                                node = makeSpecificationNode(changeType);
                            } else if ($scope.objectType == "PGCDECLARATIONTYPE") {
                                node = makeDeclarationNode(changeType);
                            }

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

                    function makeSubstanceNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'substance-node',
                            attributes: {
                                pgcType: type
                            }
                        };
                    }

                    function makeSubstanceGroupNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'subGroup-node',
                            attributes: {
                                pgcType: type
                            }
                        };
                    }

                    function makeSpecificationNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'specification-node',
                            attributes: {
                                pgcType: type
                            }
                        };
                    }

                    function makeDeclarationNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'declaration-node',
                            attributes: {
                                pgcType: type
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
