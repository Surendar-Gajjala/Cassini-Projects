define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.directive('supplierTree', ['$compile', '$timeout', 'ClassificationService', 'MESObjectTypeService', function ($compile, $timeout, ClassificationService, MESObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/mfr/supplier/directive/supplierDirective.jsp',
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
                        var supplierType = data.attributes.supplierType;
                        $scope.onSelectType(supplierType);
                        window.$("body").trigger("click");
                    }

                    function initSupplierTree() {
                        classificationTree = $('#supplierTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Supplier',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        supplierType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadSupplierTree() {
                        MESObjectTypeService.getSupplierTypeTree().then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (supplierType) {
                                    var node = makeNode(supplierType);

                                    if (supplierType.children != null && supplierType.children != undefined && supplierType.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, supplierType.children);
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

                    function visitChildren(parent, supplierTypes) {
                        var nodes = [];
                        angular.forEach(supplierTypes, function (supplierType) {
                            var node = makeNode(supplierType);

                            if (supplierType.children != null && supplierType.children != undefined && supplierType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, supplierType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(supplierType) {
                        return {
                            id: ++nodeId,
                            text: supplierType.name,
                            iconCls: 'supplier-node',
                            attributes: {
                                supplierType: supplierType
                            }
                        };
                    }

                    initSupplierTree();
                    loadSupplierTree();
                }
            };
        }]);
    }
);
