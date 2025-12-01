define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/varianceService'
    ],

    function (module) {
        module.directive('changeObjectTypeTree', ['$compile', '$timeout', 'ECOService', 'VarianceService', function ($compile, $timeout, ECOService, VarianceService) {
            return {
                templateUrl: 'app/desktop/modules/directives/changeObjectTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'changeType': "@"
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var changeTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = changeTree.tree('getData', node.target);
                        var changeType = data.attributes.changeType;
                        $scope.onSelectType(changeType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        changeTree = $('#changeObjectTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Change',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        changeType: null
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
                        if ($scope.changeType == "ECR") {
                            promise = ECOService.getECRTypeTree();
                        } else if ($scope.changeType == "MCO") {
                            promise = ECOService.getMCOTypeTree();
                        }  else if ($scope.changeType == "ITEMMCO") {
                            promise = ECOService.getItemMCOTypeTree();
                        }  else if ($scope.changeType == "OEMPARTMCO") {
                            promise = ECOService.getManufacturerMCOTypeTree();
                        }  else if ($scope.changeType == "DCR") {
                            promise = ECOService.getDCRTypeTree();
                        } else if ($scope.changeType == "DCO") {
                            promise = ECOService.getDCOTypeTree();
                        }  else if ($scope.changeType == "ECO") {
                            promise = ECOService.getECOTypeTree();
                        } if ($scope.changeType == "Deviation") {
                            promise = VarianceService.getDeviationTypeTree();
                        } else if ($scope.changeType == "Waiver") {
                            promise = VarianceService.getWaiverTypeTree();
                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (changeType) {
                                        var node = makeNode(changeType);
                                        

                                        if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, changeType.childrens);
                                        }

                                        nodes.push(node);
                                    });

                                    changeTree.tree('append', {
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
                            var node = makeNode(changeType);

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

                    function makeNode(changeType) {
                        return {
                            id: ++nodeId,
                            text: changeType.name,
                            iconCls: 'change-node',
                            attributes: {
                                changeType: changeType
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
