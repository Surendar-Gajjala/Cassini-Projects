define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/desktop/modules/settings/properties/propertiesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('SettingsController', SettingsController);

        function SettingsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                    CommonService, $translate) {

            $rootScope.viewInfo.icon = "fa fa-wrench";
            $rootScope.viewInfo.title = $translate.instant("SETTINGS_TITLE");

            var vm = this;

            var nodeId = 0;
            var settingsTree = null;
            var rootNode = null;
            vm.selectedNode = null;
            vm.onLoad = onLoad;

            var settingsTitle = $translate.instant("SETTINGS_TITLE");

            function initSettingsTree() {
                settingsTree = $('#settingsTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: settingsTitle,
                            iconCls: 'settings-node',
                            attributes: {
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType
                });

                rootNode = settingsTree.tree('find', 0);

                loadTree();
            }

            function onSelectType(node) {
                var data = settingsTree.tree('getData', node.target);
                var nodeType = data.attributes.nodeType;
                if (nodeType != 'ROOT' && nodeType != vm.selectedNode) {
                    $rootScope.showBusyIndicator($('#settingsPane'));
                }
                vm.selectedNode = nodeType;
                $scope.$apply();
            }


            function loadTree() {
                var nodes = [];
                nodes.push(makeNode("System Attributes", 'properties-node', 'PROPERTIES'));
                settingsTree.tree('append', {
                    parent: rootNode.target,
                    data: nodes
                });
            }

            function makeNode(name, iconCls, nodeType) {
                return {
                    id: ++nodeId,
                    text: name,
                    iconCls: iconCls,
                    attributes: {
                        nodeType: nodeType
                    }
                };
            }

            function onLoad() {
                $rootScope.hideBusyIndicator();
            }


            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();

                    initSettingsTree();
                });
            })();
        }
    }
);