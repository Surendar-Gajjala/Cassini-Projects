define(
    [
        'app/desktop/desktop.app',
        'jquery-ui',
        'app/shared/services/core/classificationService'
    ],

    function (module) {
        module.directive('reqWfTree', ['$compile', '$timeout', 'ClassificationService', '$translate',
            function ($compile, $timeout, ClassificationService, $translate) {
                return {
                    templateUrl: 'app/desktop/modules/classification/workflowDirective/workflowSpecificationDirective.jsp',
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
                            if (data.attributes.nodeType == "SPECIFICATIONTYPE") {
                                var reqType = data.attributes.specType;
                            } else {
                                var reqType = data.attributes.refType;
                            }
                            $scope.onSelectType(reqType);
                            window.$("body").trigger("click");
                        }

                        var specificationTitle = $translate.instant("SPECIFICATION");
                        var requirementsTitle = $translate.instant("REQUIREMENTS");

                        function initWfRequirementsTree() {
                            classificationTree = $('#reqWfTree').tree({
                                data: [
                                    {
                                        id: nodeId,
                                        text: requirementsTitle,
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


                        function loadWfRequirementsClassification() {
                            ClassificationService.getClassificationTree('REQUIREMENTTYPE').then(
                                function (data) {
                                    if (data.length > 0) {
                                    }
                                    var nodes = [];
                                    angular.forEach(data, function (type) {
                                        if (languageKey == 'de') {
                                            type.name = 'Anforderung';
                                        }
                                        var node = makeReqNode(type);
                                        if (type.name == 'Requirement' || type.name == 'Anforderung') {
                                            type.editable = false;
                                        }
                                        else {
                                            type.editable = true;
                                        }

                                        if (type.children != null && type.children != undefined && type.children.length > 0) {
                                            node.state = "closed";
                                            visitChildren(node, type.children);
                                        }

                                        nodes.push(node);
                                    });

                                    //requirementRoot.children = nodes;

                                    ClassificationService.getClassificationTree('SPECIFICATIONTYPE').then(
                                        function (data) {
                                            angular.forEach(data, function (type) {
                                                if (languageKey == 'de') {
                                                    type.name = 'Spezifikation';
                                                }
                                                var node = makeSpecNode(type);

                                                if (type.name == 'Specification' || type.name == 'Spezifikation') {
                                                    type.editable = false;
                                                }
                                                else {
                                                    type.editable = true;
                                                }

                                                if (type.children != null && type.children != undefined && type.children.length > 0) {
                                                    node.state = "closed";
                                                    visitChildren(node, type.children);
                                                }

                                                nodes.push(node);
                                            });


                                            classificationTree.tree('append', {
                                                parent: rootNode.target,
                                                data: nodes
                                            });
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                         }
                                    )
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }


                        /*  function loadSpecificationTree() {
                         ClassificationService.getClassificationTree('SPECIFICATIONTYPE').then(
                         function (data) {
                         var nodes = [];
                         angular.forEach(data, function (reqType) {

                         if (languageKey == 'de') {
                         reqType.name = 'Spezifikation';
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
                         }
                         )
                         }*/


                        function visitChildren(parent, reqTypes) {
                            var nodes = [];
                            angular.forEach(reqTypes, function (reqType) {
                                if (parent.attributes.nodeType == 'REQUIREMENTTYPE') {
                                    var node = makeReqNode(reqType);
                                }
                                else if (parent.attributes.nodeType == 'SPECIFICATIONTYPE') {
                                    var node = makeSpecNode(reqType);
                                }
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

                        function makeReqNode(type) {
                            return {
                                id: ++nodeId,
                                text: type.name,
                                iconCls: 'req-node',
                                attributes: {
                                    refType: type,
                                    nodeType: 'REQUIREMENTTYPE'
                                }
                            };
                        }

                        function makeSpecNode(type) {
                            return {
                                id: ++nodeId,
                                text: type.name,
                                iconCls: 'spec-node',
                                attributes: {
                                    specType: type,
                                    nodeType: 'SPECIFICATIONTYPE'
                                }
                            };
                        }

                        (function () {
                            initWfRequirementsTree();
                            loadWfRequirementsClassification();
                        })();
                    }
                };
            }]);
    }
);
