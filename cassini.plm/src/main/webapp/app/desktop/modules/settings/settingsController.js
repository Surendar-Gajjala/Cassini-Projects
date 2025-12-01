define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/autonumber/autonumbersController',
        'app/desktop/modules/settings/lovs/lovsController',
        'app/desktop/modules/settings/lifecycles/lifecyclesController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/preferences/preferencesController',
        'app/desktop/modules/settings/relationships/relationshipsController',
        'app/desktop/modules/settings/templates/templatesController',
        'app/desktop/modules/settings/github/githubController',
        'app/desktop/modules/settings/plugins/pluginsController',
        'app/desktop/modules/settings/measurement/measurementsController',
        'app/desktop/modules/settings/profiles/profilesController',
        'app/desktop/modules/settings/miscellaneous/miscellaneousController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('SettingsController', SettingsController);

        function SettingsController($q, $scope, $rootScope, $timeout, $state, DialogService, ItemTypeService, AutonumberService, $stateParams, $cookies,
                                    CommonService, $translate) {

            $rootScope.viewInfo.icon = "fa fa-wrench";
            $rootScope.viewInfo.title = $translate.instant("SETTINGS_TITLE");
            $rootScope.viewInfo.showDetails = false;
            var parsed = angular.element("<div></div>");

            var vm = this;

            vm.tabId = $stateParams.tab;

            var tabs = new Hashtable();

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
                if (nodeType == 'PREFERENCES') {
                    $('#settingsPane').css('overflow', 'hidden');
                } else {
                    $('#settingsPane').css('overflow', 'auto');
                }
                vm.selectedNode = nodeType;
                $state.transitionTo('app.settings', {tab: nodeType.toLowerCase()}, {notify: false});
                $scope.$evalAsync();
            }

            var autoNumbers = $translate.instant("AUTO_NUMBERS");
            var lifeCycles = $translate.instant("LIFE_CYCLES");
            var listOfValues = $translate.instant("LIST_OF_VALUES");
            var customProperties = $translate.instant("CUSTOM_PROPERTIES");
            var preferencesTitle = $translate.instant("PREFERENCES");
            var relationshipsTitle = $translate.instant("RELATIONSHIPS");
            var templatesTitle = $translate.instant("EMAIL_TEMPLATES");
            var githubTitle = $translate.instant("GITHUB");
            var pluginsTitle = $translate.instant("PLUGINS");
            var measurmentsTitle = $translate.instant("MEASUREMENTS");
            var profilesTitle = parsed.html($translate.instant("APP_PROFILES")).html();
            var miscellaneousTitle = parsed.html($translate.instant("MISCELLANEOUS")).html();
            var deleteTitle = parsed.html($translate.instant("AUTONUMBER_TITLE_DELETE")).html();
            var deleteTitleMessage = parsed.html($translate.instant("AUTONUMBER_TITLE_MES_DELETE")).html();
            var deleteMessage = parsed.html($translate.instant("AUTONUMBER_DELETE_MESSAGE")).html();

            function deleteAutoNumber(auto) {
                ItemTypeService.getAutoNumberUsedCount(auto.id, auto.name).then(
                    function (data) {
                        if (data == 0) {
                            ItemTypeService.findItemByAutoNumId(auto.id).then(
                                  function (data) {
                                    var options = {
                                        title: deleteTitle,
                                        message: deleteTitleMessage,
                                        okButtonClass: 'btn-danger'
                                    };
                                    DialogService.confirm(options, function (yes) {
                                        if (yes == true) {
                                            AutonumberService.deleteAutonumber(auto.id).then(
                                                function (data) {
                                                    var index = $rootScope.autonumbers.content.indexOf(auto);
                                                    $rootScope.autonumbers.content.splice(index, 1);
                                                    $rootScope.showSuccessMessage(deleteMessage);
                                                    $rootScope.loadAutoNumbers();
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
                        } else{
                            $rootScope.showErrorMessage("This NumberSource "+"("+auto.name+")"+" is already used. We can't delete");
                        }
                    }
                )
            }

            function loadTree() {
                var nodes = [];
                if ($rootScope.isProfileMenu('settings.number-sources')) nodes.push(makeNode(autoNumbers, 'autonumbers-node', 'AUTONUMBERS'));
                if ($rootScope.isProfileMenu('settings.lifecycles')) nodes.push(makeNode(lifeCycles, 'lifecycles-node', 'LIFECYCLES'));
                if ($rootScope.isProfileMenu('settings.lovs')) nodes.push(makeNode(listOfValues, 'lovs-node', 'LOVS'));
                if ($rootScope.isProfileMenu('settings.relationships')) nodes.push(makeNode(relationshipsTitle, 'relationships-node', 'RELATIONSHIPS'));
                //if ($rootScope.isProfileMenu('settings.preferences')) nodes.push(makeNode(preferencesTitle, 'preferences-node', 'PREFERENCES'));
                if ($rootScope.isProfileMenu('settings.email-temp')) nodes.push(makeNode(templatesTitle, 'templates-node', 'EMAIL_TEMPLATES'));
                if ($rootScope.isProfileMenu('settings.qties-measurement')) nodes.push(makeNode(measurmentsTitle, 'measurements-node', 'MEASUREMENTS'));
                if ($rootScope.isProfileMenu('settings.git-hub')) nodes.push(makeNode(githubTitle, 'github-node', 'GITHUB'));
                if ($rootScope.isProfileMenu('settings.profiles')) nodes.push(makeNode(profilesTitle, 'profiles-node', 'PROFILES'));
                if ($rootScope.isProfileMenu('settings.preferences')) nodes.push(makeNode(miscellaneousTitle, 'miscellaneous-node', 'MISCELLANEOUS'));
                if ($rootScope.isProfileMenu('settings.plugins')) nodes.push(makeNode(pluginsTitle, 'plugins-node', 'PLUGINS'));


                settingsTree.tree('append', {
                    parent: rootNode.target,
                    data: nodes
                });

                var select = 'AUTONUMBERS';
                if (vm.tabId !== undefined && vm.tabId != null && tabs.get(vm.tabId.toUpperCase()) != null) {
                    select = vm.tabId.toUpperCase();
                }
                var nodeId = tabs.get(select);

                var selectNode = settingsTree.tree('find', nodeId);
                if (selectNode != null) {
                    selectNode.target.className = "tree-node tree-node-hover tree-node-selected";
                    $timeout(function () {
                        onSelectType(selectNode)
                    }, 300)
                }

            }

            function makeNode(name, iconCls, nodeType) {
                var id = ++nodeId;
                tabs.put(nodeType, id);

                return {
                    id: id,
                    text: name,
                    iconCls: iconCls,
                    attributes: {
                        nodeType: nodeType
                    }
                };
            }

            function onLoad() {
                //$rootScope.hideBusyIndicator();
            }

            vm.newAutonumber = newAutonumber;
            function newAutonumber() {
                $scope.$broadcast("settings.new.autonumber", {});
            }

            vm.newRelationship = newRelationship;
            function newRelationship() {
                $scope.$broadcast("settings.new.relationship", {});
            }

            vm.newLifecycle = newLifecycle;
            function newLifecycle() {
                $scope.$broadcast("settings.new.lifecycle", {});
            }

            vm.newLov = newLov;
            function newLov() {
                $scope.$broadcast("settings.new.lov", {});
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    $scope.$on("app.autoNumber.delete", function (evnt, args) {
                        deleteAutoNumber(args);
                    });

                    $timeout(function () {
                        initSettingsTree();
                    }, 1000);
                });
            })();
        }
    }
);