define([
    "app/desktop/desktop.app",
    "jquery-ui",
    "app/shared/services/core/mesObjectTypeService",
], function (module) {
    module.directive("mbomTypeTree", [
        "$compile",
        "$timeout",
        "$translate",
        "$rootScope",
        "MESObjectTypeService",
        function ($compile, $timeout, $translate,$rootScope, MESObjectTypeService) {
            return {
                templateUrl:
                    "app/desktop/modules/classification/workflowDirective/mbomWorkflowTypeDirective.jsp",
                restrict: "E",
                replace: false,
                scope: {
                    onSelectType: "=",
                },

                link: function ($scope, elm, attr) {
                    var nodeId = 0;
                    var mbomTypeTree = null;
                    var rootNode = null;
                    var parsed = angular.element("<div></div>");
                    var manufacturingTitle = parsed
                        .html($translate.instant("MANUFACTURING"))
                        .html();

                    function onSelectType(node) {
                        var data = mbomTypeTree.tree("getData", node.target);
                        var objectType = data.attributes.typeObject;
                        $scope.onSelectType(objectType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        mbomTypeTree = $("#mbomTypeTree").tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Manufacturing',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        objectType: null

                                    },
                                    children: [],
                                },
                            ],
                            onSelect: onSelectType,
                        });

                        rootNode = mbomTypeTree.tree("find", 0);
                    }
                    function loadTree() {
                       
                        MESObjectTypeService.getAllObjectTypeTree().then(
                            function (data) {

                                var mbomTypes = data.mbomTypes;
                                var nodes = [];
                                var node = null;
                                angular.forEach(mbomTypes, function (type) {
                                    node = makeMbomNode(type);
                                    if (
                                        type.childrens != null &&
                                        type.childrens != undefined &&
                                        type.childrens.length > 0
                                    ) {
                                        node.state = "closed";
                                        visitMesObjectsTypeChildren(node, type.childrens);
                                    }
                                    
                                    nodes.push(node);
                                });
                                mbomTypeTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }

                    function visitMesObjectsTypeChildren(parent, itemTypes) {
                        var nodes = [];
                        angular.forEach(itemTypes, function (itemType) {
                        
                             if (parent.attributes.nodeType == 'MBOMTYPE') {
                                var node = makeMbomNode(itemType);
                               
                            }
                            if (itemType.childrens != null && itemType.childrens != undefined && itemType.childrens.length > 0) {
                                node.state = 'closed';
                                visitMesObjectsTypeChildren(node, itemType.childrens);
                            }
        
                            nodes.push(node);
                        });
                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }
                   
                    function makeMbomNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: "mbom-node",
                            attributes: {
                                typeObject: type,
                                nodeType: "MBOMTYPE",
                            },
                        };
                    }
                    initTree();
                    loadTree();
                },
            };
        },
    ]);
});
