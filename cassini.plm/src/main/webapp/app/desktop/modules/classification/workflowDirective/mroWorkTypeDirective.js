define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.directive('mroTypeTree', ['$compile', '$timeout', '$translate', 'MESObjectTypeService', function ($compile, $timeout, $translate, MESObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/classification/workflowDirective/mroWorkflowTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var mroTypeTree = null;
                    var rootNode = null;
                    var parsed = angular.element("<div></div>");
                    var maintenanceAndRepairsTitle = parsed.html($translate.instant("MAINTENANCEANDREPAIRS")).html();

                    function onSelectType(node) {
                        var data = mroTypeTree.tree('getData', node.target);
                        var objectType = data.attributes.mesType;
                        $scope.onSelectType(objectType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        mroTypeTree = $('#mroTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: maintenanceAndRepairsTitle,
                                    iconCls: 'classification-root',
                                    attributes: {
                                        objectType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = mroTypeTree.tree('find', 0);
                    }

                    function loadTree() {

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
                        MESObjectTypeService.getAllMROObjectTypeTree().then(
                            function (data) {
                                var workOrderTypes = data.workOrderTypes;
                                var nodes = [];
                                var node = null;
                                angular.forEach(workOrderTypes, function (type) {
                                    node = makeWorkOrderNode(type);
                                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                                        node.state = "closed";
                                        visitMesObjectsTypeChildren(node, type.childrens);
                                    }
                                    nodes.push(node);
                                });
                                workOrderRoot.children = nodes;
                                mroTypeTree.tree('append', {
                                    parent: rootNode.target,
                                    data: workOrderRoot
                                });
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            });


                    }

                    function visitMesObjectsTypeChildren(parent, changeTypes) {
                        var nodes = [];
                        angular.forEach(changeTypes, function (changeType) {
                            var node = null;
                            if (parent.attributes.nodeType == "WORKORDERTYPE") {
                                node = makeWorkOrderNode(changeType);
                            }
                            if (changeType.childrens != null && changeType.childrens != undefined && changeType.childrens.length > 0) {
                                node.state = 'closed';
                                visitMesObjectsTypeChildren(node, changeType.childrens);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
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
