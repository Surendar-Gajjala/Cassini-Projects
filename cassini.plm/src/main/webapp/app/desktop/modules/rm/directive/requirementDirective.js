define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('requirementTree', ['$compile', '$timeout', 'ClassificationService', '$translate', function ($compile, $timeout, ClassificationService, $translate) {
            return {
                templateUrl: 'app/desktop/modules/rm/directive/requirementDirective.jsp',
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
                        var reqType = data.attributes.reqType;
                        $scope.onSelectType(reqType);
                        window.$("body").trigger("click");
                    }

                    var requirementTitle = $translate.instant("REQUIREMENT_TITLE");

                    function initRequirementTree() {
                        classificationTree = $('#requirementTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: requirementTitle,
                                    iconCls: 'classification-root',
                                    attributes: {
                                        reqType: null
                                    },
                                    children: []
                                }
                            ],
                            onSelect: onSelectType
                        });

                        rootNode = classificationTree.tree('find', 0);
                    }

                    var languageKey = $translate.storage().get($translate.storageKey());

                    function loadRequirementTree() {
                        ClassificationService.getClassificationTree('REQUIREMENTTYPE').then(
                            function (data) {
                                var nodes = [];
                                angular.forEach(data, function (reqType) {

                                    if (languageKey == 'de') {
                                        reqType.name = 'Anforderung';
                                    }
                                    var node = makeNode(reqType);

                                    if (reqType.children != null && reqType.children != undefined && reqType.children.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, reqType.children);
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

                    function visitChildren(parent, reqTypes) {
                        var nodes = [];
                        angular.forEach(reqTypes, function (reqType) {
                            var node = makeNode(reqType);

                            if (reqType.children != null && reqType.children != undefined && reqType.children.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, reqType.children);
                            }

                            nodes.push(node);
                        });

                        if (nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeNode(reqType) {
                        return {
                            id: ++nodeId,
                            text: reqType.name,
                            iconCls: 'req-node',
                            attributes: {
                                reqType: reqType
                            }
                        };
                    }

                    initRequirementTree();
                    loadRequirementTree();
                }
            };
        }]);
    }
);
