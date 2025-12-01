define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService'
    ],

    function (module) {
        module.directive('customTypeTree', ['$compile', '$timeout', '$rootScope', 'CustomObjectService', 'CustomObjectTypeService', function ($compile, $timeout, $rootScope, CustomObjectService, CustomObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/customObjectTypeDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '=',
                    'customType': "@",
                    'typeId': "=",
                    'obj': '='
                },

                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var customTree = null;
                    var rootNode = null;

                    function onSelectType(node) {
                        var data = customTree.tree('getData', node.target);
                        var customType = data.attributes.customType;
                        if ($scope.obj != null) {
                            $scope.onSelectType(customType, $scope.obj);
                        } else {
                            $scope.onSelectType(customType);
                        }
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        customTree = $('#customTypeTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Custom',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        customType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = customTree.tree('find', 0);
                    }

                    function loadTree() {
                        if ($scope.typeId != null) {
                            CustomObjectTypeService.getCustomObjectTypeTree($scope.typeId).then(
                                function (data) {
                                    var nodes = [];
                                    var node = makeNode(data);

                                    if (data.children != null && data.children != undefined && data.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, data.children);
                                    }

                                    nodes.push(node);

                                    customTree.tree('append', {
                                        parent: rootNode.target,
                                        data: nodes
                                    });
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            CustomObjectTypeService.getCustomObjectTypeClassificationTree().then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (customType) {
                                        var node = makeNode(customType);

                                        if (customType.children != null && customType.children != undefined && customType.children.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, customType.children);
                                        }

                                        nodes.push(node);
                                    });

                                    customTree.tree('append', {
                                        parent: rootNode.target,
                                        data: nodes
                                    });
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function visitChildren(parent, customTypes) {
                        var nodes = [];
                        angular.forEach(customTypes, function (customType) {
                            var node = makeNode(customType);

                            if (customType.children != null && customType.children != undefined && customType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, customType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(customType) {
                        return {
                            id: ++nodeId,
                            text: customType.name,
                            iconCls: 'custom-node',
                            attributes: {
                                customType: customType
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
