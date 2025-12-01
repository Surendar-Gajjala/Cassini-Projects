define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('manufacturerTree', ['$compile', '$timeout', 'ClassificationService', function ($compile, $timeout, ClassificationService) {
            return {
                templateUrl: 'app/desktop/modules/mfr/directive/manufacturerDirective.jsp',
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
                        var mfrType = data.attributes.mfrType;
                        $scope.onSelectType(mfrType);
                        window.$("body").trigger("click");
                    }

                    function initManufacturerTree() {
                        classificationTree = $('#manufacturerTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Manufacturer',
                                    iconCls: 'classification-root',
                                    attributes: {
                                        mfrType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    function loadManufacturerTree() {
                        ClassificationService.getClassificationTree('MANUFACTURERTYPE').then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (mfrType) {
                                    var node = makeNode(mfrType);

                                    if (mfrType.children != null && mfrType.children != undefined && mfrType.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, mfrType.children);
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

                    function visitChildren(parent, mfrTypes) {
                        var nodes = [];
                        angular.forEach(mfrTypes, function (mfrType) {
                            var node = makeNode(mfrType);

                            if (mfrType.children != null && mfrType.children != undefined && mfrType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, mfrType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(mfrType) {
                        return {
                            id: ++nodeId,
                            text: mfrType.name,
                            iconCls: 'mfr-node',
                            attributes: {
                                mfrType: mfrType
                            }
                        };
                    }

                    initManufacturerTree();
                    loadManufacturerTree();
                }
            };
        }]);
    }
);
