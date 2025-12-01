define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService'
    ],

    function (module) {
        module.directive('userTree', ['$compile', '$timeout', 'PersonGroupService', function ($compile, $timeout, PersonGroupService) {
            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/groupTreeViewDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'onSelectType': '='
                },


                link: function ($scope, elm, attr) {

                    var nodeId = 0;
                    var rootNode = null;
                    var classificationMainTree = null;


                    function onDblClick(node) {
                    }

                    function onAfterEdit(node) {
                    }

                    function loadClassificationTree() {
                        PersonGroupService.getHierarchy().then(
                            function(data) {
                                var nodes = [];

                                angular.forEach(data, function(item) {
                                    item.type = "GROUP";
                                    var node = makeGroupNode(item);

                                    if(item.subGroups != null && item.subGroups != undefined && item.subGroups.length > 0) {
                                        node.state = "closed";
                                        visitChildren(node, item.subGroups);
                                    }

                                    nodes.push(node);
                                });

                                classificationMainTree.tree('append', {
                                    parent: rootNode.target,
                                    data: nodes
                                });
                            }
                        )
                    }

                    function visitChildren(parent, itemTypes) {
                        var nodes = [];
                        angular.forEach(itemTypes, function(itemType) {
                            itemType.type = "GROUP";
                            var node = makeGroupNode(itemType);

                            if(itemType.subGroups != null && itemType.subGroups != undefined && itemType.subGroups.length > 0) {
                                node.state = 'closed';
                                visitChildren(node, itemType.subGroups);
                            }

                            nodes.push(node);
                        });

                        if(nodes.length > 0) {
                            parent.children = nodes;
                        }
                    }

                    function makeGroupNode(item) {
                        return {
                            id: ++nodeId,
                            text: item.name,
                            iconCls: 'group-node',
                            attributes: {
                                item: item
                            }
                        };
                    }

                    function initClassificationTree() {
                        classificationMainTree = $('#userTree').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: 'Groups',
                                    iconCls: 'groups-node',
                                    attributes: {
                                        item: null
                                    },
                                    children: []
                                }
                            ],
                            onContextMenu: onContextMenu,
                            onDblClick: onDblClick,
                            onAfterEdit: onAfterEdit,
                            onSelect: onSelectType
                        });

                        rootNode = classificationMainTree.tree('find', 0);

                        $(document).click(function () {
                            $("#contextMenu").hide();
                        });
                    }

                    function onSelectType(node) {
                        var data =  classificationMainTree.tree('getData', node.target);
                        var item = data.attributes.item;
                        $scope.onSelectType(item);
                        window.$("body").trigger("click");
                    }

                    function onContextMenu(e, node) {
                        e.preventDefault();
                    }

                    initClassificationTree();
                    loadClassificationTree();
                }
            };
        }]);
    }
);
