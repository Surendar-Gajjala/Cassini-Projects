define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.directive('manufacturingTypeTree', ['$compile', '$timeout', '$translate', 'MESObjectTypeService', function ($compile, $timeout, $translate, MESObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/mesObjectTypeDirective.jsp',
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
                    var parsed = angular.element("<div></div>");
                    var manufacturingTitle = parsed.html($translate.instant("MANUFACTURING")).html();
                    var maintenanceAndRepairsTitle = parsed.html($translate.instant("MAINTENANCEANDREPAIRS")).html();

                    function onSelectType(node) {
                        var data = manufacturingTree.tree('getData', node.target);
                        var objectType = data.attributes.mesType;
                        $scope.onSelectType(objectType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        var treeNodeTitle = "";
                        if ($scope.objectType == "ASSETTYPE" || $scope.objectType == "METERTYPE" || $scope.objectType == "WORKREQUESTTYPE"
                            || $scope.objectType == "WORKORDERTYPE" || $scope.objectType == "REPAIRWORKORDERTYPE" || $scope.objectType == "SPAREPARTTYPE") {
                            treeNodeTitle = maintenanceAndRepairsTitle;
                        } else {
                            treeNodeTitle = manufacturingTitle;
                        }
                        manufacturingTree = $('#manufacturingTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: treeNodeTitle,
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
                        if ($scope.objectType == "MANPOWERTYPE") {
                            promise = MESObjectTypeService.getManpowerTypeTree();
                        } else if ($scope.objectType == "MATERIALTYPE") {
                            promise = MESObjectTypeService.getMaterialTypeTree();
                        } else if ($scope.objectType == "TOOLTYPE") {
                            promise = MESObjectTypeService.getToolTypeTree();
                        } else if ($scope.objectType == "JIFSFIXTURETYPE") {
                            promise = MESObjectTypeService.getJigsFixtureTypeTree();
                        } else if ($scope.objectType == "PLANTTYPE") {
                            promise = MESObjectTypeService.getPlantTypeTree();
                        }else if ($scope.objectType == "MBOMTYPE") {
                            promise = MESObjectTypeService.getMBOMTypeTree();
                        }else if ($scope.objectType == "BOPTYPE") {
                            promise = MESObjectTypeService.getBOPTypeTree();
                        } else if ($scope.objectType == "PRODUCTIONORDERTYPE") {
                            promise = MESObjectTypeService.getProductionOrderTypeTree();
                        } else if ($scope.objectType == "MACHINETYPE") {
                            promise = MESObjectTypeService.getMachineTypeTree();
                        } else if ($scope.objectType == "OPERATIONTYPE") {
                            promise = MESObjectTypeService.getOperationTypeTree();
                        } else if ($scope.objectType == "WORKCENTERTYPE") {
                            promise = MESObjectTypeService.getWorkCenterTypeTree();
                        } else if ($scope.objectType == "EQUIPMENTTYPE") {
                            promise = MESObjectTypeService.getEquipmentTypeTree();
                        } else if ($scope.objectType == "INSTRUMENTTYPE") {
                            promise = MESObjectTypeService.getInstrumentTypeTree();
                        } else if ($scope.objectType == "SPAREPARTTYPE") {
                            promise = MESObjectTypeService.getSparePartTypeTree();
                        } else if ($scope.objectType == "WORKREQUESTTYPE") {
                            promise = MESObjectTypeService.getWorkRequestTypeTree();
                        } else if ($scope.objectType == "WORKORDERTYPE") {
                            promise = MESObjectTypeService.getWorkOrderTypeTree();
                        } else if ($scope.objectType == "REPAIRWORKORDERTYPE") {
                            promise = MESObjectTypeService.getRepairWorkOrderTypeTree();
                        } else if ($scope.objectType == "ASSETTYPE") {
                            promise = MESObjectTypeService.getAssetTypeTree();
                        } else if ($scope.objectType == "METERTYPE") {
                            promise = MESObjectTypeService.getMeterTypeTree();
                        } else if ($scope.objectType == "ASSEMBLYLINETYPE") {
                            promise = MESObjectTypeService.getAssemblyLineTypeTree();
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (changeType) {
                                        var node = null;
                                        if ($scope.objectType == "MANPOWERTYPE") {
                                            node = makeManPowerNode(changeType);
                                        } else if ($scope.objectType == "MATERIALTYPE") {
                                            node = makeMaterialNode(changeType);
                                        } else if ($scope.objectType == "TOOLTYPE") {
                                            node = makeToolNode(changeType);
                                        } else if ($scope.objectType == "JIFSFIXTURETYPE") {
                                            node = makeJigsFixtureNode(changeType);
                                        } else if ($scope.objectType == "PLANTTYPE") {
                                            node = makePlantNode(changeType);
                                        } else if ($scope.objectType == "MBOMTYPE") {
                                            node = makeMBOMNode(changeType);
                                        } else if ($scope.objectType == "BOPTYPE") {
                                            node = makeBOPNode(changeType);
                                        } else if ($scope.objectType == "PRODUCTIONORDERTYPE") {
                                            node = makeProductionOrderNode(changeType);
                                        } else if ($scope.objectType == "MACHINETYPE") {
                                            node = makeMachineNode(changeType);
                                        } else if ($scope.objectType == "OPERATIONTYPE") {
                                            node = makeOperationNode(changeType);
                                        }
                                        else if ($scope.objectType == "WORKCENTERTYPE") {
                                            node = makeWorkCenterNode(changeType);
                                        } else if ($scope.objectType == "EQUIPMENTTYPE") {
                                            node = makeEquipmentNode(changeType);
                                        } else if ($scope.objectType == "INSTRUMENTTYPE") {
                                            node = makeInstrumentNode(changeType);
                                        } else if ($scope.objectType == "SPAREPARTTYPE") {
                                            node = makeSparePartNode(changeType);
                                        } else if ($scope.objectType == "WORKREQUESTTYPE") {
                                            node = makeWorkRequestNode(changeType);
                                        } else if ($scope.objectType == "WORKORDERTYPE" || $scope.objectType == "REPAIRWORKORDERTYPE") {
                                            node = makeWorkOrderNode(changeType);
                                        } else if ($scope.objectType == "ASSETTYPE") {
                                            node = makeAssetNode(changeType);
                                        } else if ($scope.objectType == "METERTYPE") {
                                            node = makeMeterNode(changeType);
                                        } else if ($scope.objectType == "ASSEMBLYLINETYPE") {
                                            node = makeAssemblyLineNode(changeType);
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
                                      $rootScope.hideBusyIndicator();
                                 }
                            )
                        }
                    }

                    function visitChildren(parent, changeTypes) {
                        var nodes = [];
                        angular.forEach(changeTypes, function (changeType) {
                            var node = null;
                            if ($scope.objectType == "MANPOWERTYPE") {
                                node = makeManPowerNode(changeType);
                            } else if ($scope.objectType == "MATERIALTYPE") {
                                node = makeMaterialNode(changeType);
                            } else if ($scope.objectType == "TOOLTYPE") {
                                node = makeToolNode(changeType);
                            } else if ($scope.objectType == "JIFSFIXTURETYPE") {
                                node = makeJigsFixtureNode(changeType);
                            } else if ($scope.objectType == "PLANTTYPE") {
                                node = makePlantNode(changeType);
                            } else if ($scope.objectType == "MBOMTYPE") {
                                node = makeMBOMNode(changeType);
                            } else if ($scope.objectType == "BOPTYPE") {
                                node = makeBOPNode(changeType);
                            } else if ($scope.objectType == "PRODUCTIONORDERTYPE") {
                                node = makeProductionOrderNode(changeType);
                            } else if ($scope.objectType == "MACHINETYPE") {
                                node = makeMachineNode(changeType);
                            } else if ($scope.objectType == "OPERATIONTYPE") {
                                node = makeOperationNode(changeType);
                            } else if ($scope.objectType == "WORKCENTERTYPE") {
                                node = makeWorkCenterNode(changeType);
                            } else if ($scope.objectType == "EQUIPMENTTYPE") {
                                node = makeEquipmentNode(changeType);
                            } else if ($scope.objectType == "INSTRUMENTTYPE") {
                                node = makeInstrumentNode(changeType);
                            } else if ($scope.objectType == "SPAREPARTTYPE") {
                                node = makeSparePartNode(changeType);
                            } else if ($scope.objectType == "WORKREQUESTTYPE") {
                                node = makeWorkRequestNode(changeType);
                            } else if ($scope.objectType == "WORKORDERTYPE" || $scope.objectType == "REPAIRWORKORDERTYPE") {
                                node = makeWorkOrderNode(changeType);
                            } else if ($scope.objectType == "ASSETTYPE") {
                                node = makeAssetNode(changeType);
                            } else if ($scope.objectType == "METERTYPE") {
                                node = makeMeterNode(changeType);
                            } else if ($scope.objectType == "ASSEMBLYLINETYPE") {
                                node = makeAssemblyLineNode(changeType);
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

                    function makeOperationNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'operation-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makePlantNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'plant-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }
                    function makeMBOMNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'mbom-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeBOPNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'bop-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeAssemblyLineNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'assemblyLine-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeAssetNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'asset-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeMeterNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'meter-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeMaterialNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'material-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeJigsFixtureNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'jigs-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeToolNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'tool-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeProductionOrderNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'production-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeMachineNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'machine-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeManPowerNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'manpower-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeWorkCenterNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'workCenter-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeEquipmentNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'equipment-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeInstrumentNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'instrument-node',
                            attributes: {
                                mesType: type
                            }
                        };
                    }

                    function makeSparePartNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'sparepart-node',
                            attributes: {
                                mesType: type
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
