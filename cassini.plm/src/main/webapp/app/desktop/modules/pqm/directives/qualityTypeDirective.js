define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/qualityTypeService'
    ],

    function (module) {
        module.directive('qualityTypeTree', ['$compile', '$timeout', 'QualityTypeService', function ($compile, $timeout, QualityTypeService) {
            return {
                templateUrl: 'app/desktop/modules/pqm/directives/qualityTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'qualityType': "@"
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var changeTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = changeTree.tree('getData', node.target);
                        var planType = data.attributes.planType;
                        $scope.onSelectType(planType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        changeTree = $('#qualityTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Quality',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        planType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = changeTree.tree('find', 0);
                    }

                    function loadTree() {
                        var promise = null;
                        if ($scope.qualityType == "PRODUCTINSPECTIONPLANTYPE") {
                            promise = QualityTypeService.getProductInspectionPlanTypeTree();
                        } else if ($scope.qualityType == "MATERIALINSPECTIONPLANTYPE") {
                            promise = QualityTypeService.getMaterialInspectionPlanTypeTree();
                        } else if ($scope.qualityType == "PROBLEMREPORTTYPE") {
                            promise = QualityTypeService.getPrTypeTree();
                        } else if ($scope.qualityType == "NCRTYPE") {
                            promise = QualityTypeService.getNcrTypeTree();
                        } else if ($scope.qualityType == "QCRTYPE") {
                            promise = QualityTypeService.getQcrTypeTree();
                        } else if ($scope.qualityType == "PPAPTYPE") {
                            promise = QualityTypeService.getPpapTypeTree();
                        }else if ($scope.qualityType == "SUPPLIERAUDITTYPE") {
                            promise = QualityTypeService.getSupplierAuditTypeTree();
                        }  

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (planType) {
                                        var node = makeNode(planType);

                                        if (planType.childrens != null && planType.childrens != undefined && planType.childrens.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, planType.childrens);
                                        }

                                        nodes.push(node);
                                    });

                                    changeTree.tree('append', {
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

                    function visitChildren(parent, planTypes) {
                        var nodes = [];
                        angular.forEach(planTypes, function (planType) {
                            var node = makeNode(planType);

                            if (planType.childrens != null && planType.childrens != undefined && planType.childrens.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, planType.childrens);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(planType) {
                        return {
                            id: ++nodeId,
                            text: planType.name,
                            iconCls: 'quality-node',
                            attributes: {
                                planType: planType
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
