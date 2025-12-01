define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.directive('manufacturingWorkflowTypeTree', ['$compile', '$timeout', '$rootScope', 'MESObjectTypeService', function ($compile, $timeout, $rootScope, MESObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/mesWorkflowTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var manufacturingTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = manufacturingTree.tree('getData', node.target);
                        var objectType = data.attributes.nodeType;

                        if (objectType != null && objectType != "MASTERDATANODE") {
                            if (objectType == "PRODUCTIONORDERTYPE") {
                                $scope.onSelectType(data.attributes.mesType);
                            }
                            else {
                                $scope.onSelectType(data.attributes.typeObject);
                            }

                        }

                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        manufacturingTree = $('#manufacturingWorkflowTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Manufacturing',
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
                        MESObjectTypeService.getAllObjectTypeTree().then(
                            function (data) {
                                var mesObjectTypesDto = data;
                                loadMESManufacturingClassification(mesObjectTypesDto);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    function loadMESManufacturingClassification(data) {
                        // var masterDataRoot = {
                        //     id: ++nodeId,
                        //     text: 'MasterData',
                        //     iconCls: 'masterData-node',
                        //     attributes: {
                        //         root: true,
                        //         nodeType: 'MASTERDATANODE'
                        //     },
                        //     children: []
                        // };
                        if (data != null) {
                            rootNode.state = 'closed'
                        }
                        // var plantTypes = data.plantTypes;
                        // var assemblyLineTypes = data.assemblyLineTypes;
                        // var workCenterTypes = data.workCenterTypes;
                        // var machineTypes = data.machineTypes;
                        // var equipmentTypes = data.equipmentTypes;
                        // var instrumentTypes = data.instrumentTypes;
                        // var toolTypes = data.toolTypes;
                        // var jigsFixtureTypes = data.jigsFixtureTypes;

                        // var materialTypes = data.materialTypes;
                        // var manpowerTypes = data.manpowerTypes;
                        // var operationTypes = data.operationTypes;
                        var productionOrderTypes = data.productionOrderTypes;
                        // var mbomTypes = data.mbomTypes;
                        var bopTypes = data.bopTypes;

                        var nodes = [];
                        var node = null;
                        // angular.forEach(plantTypes, function (type) {
                        //     node = makePlantNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(assemblyLineTypes, function (type) {
                        //     node = makeAssemblyLineNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(workCenterTypes, function (type) {
                        //     node = makeWorkCenterNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(machineTypes, function (type) {
                        //     node = makeMachineNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(equipmentTypes, function (type) {
                        //     node = makeEquipmentNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(instrumentTypes, function (type) {
                        //     node = makeInstrumentNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(toolTypes, function (type) {
                        //     node = makeToolNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(jigsFixtureTypes, function (type) {
                        //     node = makeJigsFixtureNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(materialTypes, function (type) {
                        //     node = makeMaterialNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(manpowerTypes, function (type) {
                        //     node = makeManPowerNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // angular.forEach(operationTypes, function (type) {
                        //     node = makeOperationNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     nodes.push(node);
                        // });
                        // masterDataRoot.children = nodes;
                        // rootNode.children.push(masterDataRoot);
                        // angular.forEach(mbomTypes, function (type) {
                        //     node = makeMbomNode(type);
                        //     if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        //         node.state = "closed";
                        //         visitMesObjectsTypeChildren(node, type.childrens);
                        //     }
                        //     rootNode.children.push(node);
                        // });
                        angular.forEach(bopTypes, function (type) {
                            node = makeBopNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            rootNode.children.push(node);
                        });
                        angular.forEach(productionOrderTypes, function (type) {
                            node = makeProductionOrderNode(type);
                            if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                node.state = "closed";
                                visitMesObjectsTypeChildren(node, type.childrens);
                            }
                            rootNode.children.push(node);
                        });
                        //  masterDataRoot.state = 'closed';
                        manufacturingTree.tree('append', {
                            parent: rootNode.target,
                            data: rootNode.children
                        });
                    }


                    function visitMesObjectsTypeChildren(parent, itemTypes) {
                        var nodes = [];
                        angular.forEach(itemTypes, function (itemType) {
                                // if (parent.attributes.nodeType == 'PLANTTYPE') {
                                //     var node = makePlantNode(itemType);
                                // }
                                // if (parent.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                                //     var node = makeAssemblyLineNode(itemType);
                                // }
                                // else if (parent.attributes.nodeType == 'WORKCENTERTYPE') {
                                //     var node = makeWorkCenterNode(itemType);
                                // }
                                // else if (parent.attributes.nodeType == 'MACHINETYPE') {
                                //     var node = makeMachineNode(itemType);
                                // }
                                // else if (parent.attributes.nodeType == 'EQUIPMENTTYPE') {
                                //     var node = makeEquipmentNode(itemType);
                                // }
                                // else if (parent.attributes.nodeType == 'INSTRUMENTTYPE') {
                                //     var node = makeInstrumentNode(itemType);
                                // }
                                // else if (parent.attributes.nodeType == 'JIGFIXTURETYPE') {
                                //     var node = makeJigsFixtureNode(itemType);

                                // } else if (parent.attributes.nodeType == 'TOOLTYPE') {
                                //     var node = makeToolNode(itemType);
                                // } else if (parent.attributes.nodeType == 'MANPOWERTYPE') {
                                //     var node = makeManPowerNode(itemType);
                                // } else if (parent.attributes.nodeType == 'OPERATIONTYPE') {
                                //     var node = makeOperationNode(itemType);
                                // }
                                // if (parent.attributes.nodeType == 'MBOMTYPE') {
                                //     var node = makeMbomNode(itemType);
                                // }
                                if (parent.attributes.nodeType == 'BOPTYPE') {
                                    var node = makeBopNode(itemType);
                                } else if (parent.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                                    var node = makeProductionOrderNode(itemType);
                                    // } else if (parent.attributes.nodeType == 'MATERIALTYPE') {
                                    //     var node = makeMaterialNode(itemType);
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


                    // function makePlantNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'plant-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'PLANTTYPE'
                    //         }
                    //     };
                    // }


                    // function makeJigsFixtureNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'jigs-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'JIGFIXTURETYPE'
                    //         }
                    //     };
                    // }

                    // function makeToolNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'tool-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'TOOLTYPE'
                    //         }
                    //     };
                    // }


                    // function makeMachineNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'machine-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'MACHINETYPE'
                    //         }
                    //     };
                    // }


                    // function makeWorkCenterNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'workCenter-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'WORKCENTERTYPE'
                    //         }
                    //     };
                    // }

                    // function makeEquipmentNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'equipment-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'EQUIPMENTTYPE'
                    //         }
                    //     };
                    // }

                    // function makeInstrumentNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'instrument-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'INSTRUMENTTYPE'
                    //         }
                    //     };
                    // }

                    // function makeAssemblyLineNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'assemblyLine-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'ASSEMBLYLINETYPE'
                    //         }
                    //     };
                    // }


                    // function makeMaterialNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'material-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'MATERIALTYPE'
                    //         }
                    //     };
                    // }

                    function makeProductionOrderNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'production-node',
                            attributes: {
                                mesType: type,
                                nodeType: 'PRODUCTIONORDERTYPE'
                            }
                        };
                    }

                    // function makeManPowerNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'manpower-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'MANPOWERTYPE'
                    //         }
                    //     };
                    // }

                    // function makeOperationNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'operation-node',
                    //         attributes: {
                    //             mesType: type,
                    //             nodeType: 'OPERATIONTYPE'
                    //         }
                    //     };
                    // }

                    // function makeMbomNode(type) {
                    //     return {
                    //         id: ++nodeId,
                    //         text: type.name,
                    //         iconCls: 'classification-root',
                    //         attributes: {
                    //             typeObject: type,
                    //             nodeType: 'MBOMTYPE'
                    //         }
                    //     };
                    // }
                    function makeBopNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'classification-root',
                            attributes: {
                                typeObject: type,
                                nodeType: 'BOPTYPE'
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
