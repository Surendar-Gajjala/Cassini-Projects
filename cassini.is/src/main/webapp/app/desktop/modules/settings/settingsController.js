define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/autonumber/autonumbersController',
        'app/desktop/modules/settings/lovs/lovsController',
        'app/desktop/modules/settings/properties/propertiesController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/preferences/preferencesController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('SettingsController', SettingsController);

        function SettingsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, ItemService,
                                    CommonService, AutonumberService) {
            $rootScope.setViewType('APP');
            $rootScope.viewInfo.icon = "fa fa-wrench";
            $rootScope.viewInfo.title = "Settings";

            var vm = this;

            var nodeId = 0;
            var settingsTree = null;
            var rootNode = null;
            vm.selectedNode = null;
            vm.onLoad = onLoad;

            function initSettingsTree() {
                settingsTree = $('#settingsTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: '<strong>Settings</strong>',
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
                if ($rootScope.hasPermission('permission.settings.autoNumbers') || $rootScope.hasPermission('permission.settings.editAutoNumbers') || $rootScope.hasPermission('permission.settings.addAutoNumbers')) {
                    nodes.push(makeNode('Auto Numbers', 'autonumbers-node', 'AUTONUMBERS'));
                }
                if ($rootScope.hasPermission('permission.settings.lovs') || $rootScope.hasPermission('permission.settings.editLovs') || $rootScope.hasPermission('permission.settings.addLovs')) {
                    nodes.push(makeNode('List of Values', 'lovs-node', 'LOVS'));
                }
                if ($rootScope.hasPermission('permission.settings.customProperties') || $rootScope.hasPermission('permission.settings.editCustomProperties') || $rootScope.hasPermission('permission.settings.addCustomProperties')) {
                    nodes.push(makeNode('Custom Properties', 'properties-node', 'PROPERTIES'));
                }
                nodes.push(makeNode('Preferences', 'preferences-node', 'PREFERENCES'));

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


            function deleteAutoNumber(auto) {
                ItemService.findItemByAutoNumId(auto.id).then(
                    function (data) {
                        var options = {
                            title: 'Delete AutoNumber',
                            message: 'Are you sure you want to delete this AutoNumber?',
                            okButtonClass: 'btn-danger',
                            okButtonText: "Yes",
                            cancelButtonText: "No"
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                AutonumberService.deleteAutonumber(auto.id).then(
                                    function (data) {
                                        var index = $rootScope.autonumbers.content.indexOf(auto);
                                        $rootScope.autonumbers.content.splice(index, 1);
                                        $rootScope.showSuccessMessage("AutoNumber deleted successfully");
                                    },
                                    function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                );
                            }
                        });

                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-left-pane').css({width: 300, "min-width": 300, "max-width": 300});
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane-divider').css({left: 300});
                    $('div.split-pane').splitPane();
                    $scope.$on("app.autoNumber.delete", function (evnt, args) {
                        deleteAutoNumber(args);
                    })
                    initSettingsTree();
                });
            })();
        }
    }
);