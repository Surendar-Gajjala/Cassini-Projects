define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/mesObjectTypeService'
    ],

    function (module) {
        module.directive('instrumentsTypeTree', ['$compile', '$timeout', '$translate', 'MESObjectTypeService', function ($compile, $timeout, $translate, MESObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/instrumentsTypeDirective.jsp',
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

                    function onSelectType(node) {
                        var data = manufacturingTree.tree('getData', node.target);
                        var objectType = data.attributes.mesType;
                        $scope.onSelectType(objectType);
                        window.$("body").trigger("click");
                    }

                    function initTree() {
                        var treeNodeTitle = "";
                        
                        treeNodeTitle = manufacturingTitle;
                        
                        manufacturingTree = $('#instrumentsTypeTree').tree({
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
                        if ($scope.objectType == "INSTRUMENTSTYPE") {
                            promise = MESObjectTypeService.getInstrumentTypeTree();
                        } 

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    var nodes = [];
                                    angular.forEach(data, function (changeType) {
                                        var node = null;
                                         if ($scope.objectType == "INSTRUMENTSTYPE") {
                                            node = makeInstrumentsNode(changeType);
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
                             if ($scope.objectType == "INSTRUMENTSTYPE") {
                                node = makeInstrumentsNode(changeType);
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

                   

                    function makeInstrumentsNode(type) {
                        return {
                            id: ++nodeId,
                            text: type.name,
                            iconCls: 'instrument-node',
                            attributes: {
                                mesType: type
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
