define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.directive('mroResourceTypeTree', ['$compile', '$timeout', '$rootScope', 'MESObjectTypeService', function ($compile, $timeout, $rootScope, MESObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/mroResourceTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'objectType': "@",
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var manufacturingTree = null;
                    var rootNode = null;


                    function onSelectType(node) {
                        var data = manufacturingTree.tree('getData', node.target);
                        var objectType = data.attributes.mesType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(objectType, $scope.obj);
                        } else {
                            $scope.onSelectType(objectType);
                        }
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        manufacturingTree = $('#mroResourceTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Maintenance',
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
                        MESObjectTypeService.getMROObjectsTypeTree().then(
                            function (data) {
                                var mroObjectTypesDto = data;
                                loadMROManufacturingClassification(mroObjectTypesDto);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    function loadMROManufacturingClassification(data) {

                        var workOrderRoot = {
                            id: ++nodeId,
                            text: 'Work Order',
                            iconCls: 'workOrder-node',
                            attributes: {
                                root: true,
                                nodeType: 'MROOBJECTTYPE'
                            },
                            children: []
                        };

                        if (data != null) {
                            rootNode.state = 'closed'
                        }
                        var assetTypes = data.assetTypes;
                        var meterTypes = data.meterTypes;
                        var mroSparePartTypes = data.mroSparePartTypes;
                        var workRequestTypes = data.workRequestTypes;
                        var workOrderTypes = data.workOrderTypes;

                        var nodes = [];
                        var node = null;
                        var workOrderNodes = [];
                        angular.forEach(assetTypes, function (type) {
                            node = makeAssetNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            nodes.push(node);
                        });

                        angular.forEach(meterTypes, function (type) {
                            node = makeMeterNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            nodes.push(node);
                        });

                        angular.forEach(mroSparePartTypes, function (type) {
                            node = makeSparePartNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            nodes.push(node);
                        });

                        angular.forEach(workRequestTypes, function (type) {
                            node = makeWorkRequestNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            nodes.push(node);
                        });

                        angular.forEach(workOrderTypes, function (type) {
                            node = makeWorkOrderNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            workOrderNodes.push(node);
                        });
                        if (workOrderNodes.length > 0) {
                            workOrderRoot.state = 'closed';
                        }
                        workOrderRoot.children = workOrderNodes;
                        nodes.push(workOrderRoot);
                        manufacturingTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }


                    function visitMesObjectsTypeChildren(parent, itemTypes) {
                        var nodes = [];
                        angular.forEach(itemTypes, function (itemType) {
                                if (parent.attributes.nodeType == "ASSETTYPE") {
                                    var node = makeAssetNode(itemType);
                                }
                                else if (parent.attributes.nodeType == "SPAREPARTTYPE") {
                                    var node = makeSparePartNode(itemType);
                                } else if (parent.attributes.nodeType == "WORKREQUESTTYPE") {
                                    var node = makeWorkRequestNode(itemType);
                                } else if (parent.attributes.nodeType == "WORKORDERTYPE") {
                                    var node = makeWorkOrderNode(itemType);
                                } else if (parent.attributes.nodeType == "METERTYPE") {
                                    var node = makeMeterNode(itemType);
                                }

                                if (itemType.childrens != null && itemType.childrens != undefined && itemType.childrens.length > 0) {
                                    node.state = 'closed';
                                    visitMesObjectsTypeChildren(node, itemType.childrens);
                                }

                                nodes.push(node);
                            }
                        )
                        ;
                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeAssetNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'asset-node',
                            attributes: {
                                mesType: type,
                                nodeType: 'ASSETTYPE'
                            }
                        };
                    }

                    function makeMeterNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'meter-node',
                            attributes: {
                                mesType: type,
                                nodeType: 'METERTYPE'
                            }
                        };
                    }

                    function makeSparePartNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'sparepart-node',
                            attributes: {
                                mesType: type,
                                nodeType: 'SPAREPARTTYPE'
                            }
                        };
                    }

                    function makeWorkRequestNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'workRequest-node',
                            attributes: {
                                mesType: type,
                                nodeType: 'WORKREQUESTTYPE'
                            }
                        };
                    }

                    function makeWorkOrderNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'workOrder-node',
                            attributes: {
                                mesType: type,
                                nodeType: 'WORKORDERTYPE'
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
