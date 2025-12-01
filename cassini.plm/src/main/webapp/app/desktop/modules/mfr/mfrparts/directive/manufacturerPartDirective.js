define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('manufacturerPartTree', ['$compile', '$timeout', 'ClassificationService', function ($compile, $timeout, ClassificationService) {
            return {
                templateUrl: 'app/desktop/modules/mfr/mfrparts/directive/manufacturerPartDirective.jsp',
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
                        var mfrPartType = data.attributes.mfrPartType;
                        $scope.onSelectType(mfrPartType);
                        window.$("body").trigger("click");
                    }

                    function initManufacturerPartTree() {
                        classificationTree = $('#manufacturerPartTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'ManufacturerPart',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        mfrPartType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadManufacturerPartTree() {
                        ClassificationService.getClassificationTree('MANUFACTURERPARTTYPE').then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (mfrPartType) {
                                    var node = makeNode(mfrPartType);

                                    if (mfrPartType.children != null && mfrPartType.children != undefined && mfrPartType.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, mfrPartType.children);
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

                    function visitChildren(parent, mfrPartTypes) {
                        var nodes = [];
                        angular.forEach(mfrPartTypes, function (mfrPartType) {
                            var node = makeNode(mfrPartType);

                            if (mfrPartType.children != null && mfrPartType.children != undefined && mfrPartType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, mfrPartType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(mfrPartType) {
                        return {
                            id: ++nodeId,
                            text: mfrPartType.name,
                            iconCls: 'mfrpart-node',
                            attributes: {
                                mfrPartType: mfrPartType
                            }
                        };
                    }

                    initManufacturerPartTree();
                    loadManufacturerPartTree();
                }
            };
        }]);
    }
);
