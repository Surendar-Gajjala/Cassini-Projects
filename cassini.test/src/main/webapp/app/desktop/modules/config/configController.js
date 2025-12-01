define(
    [
        'app/desktop/modules/config/config.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/config/details/configuarationDetailsController',
        'app/desktop/modules/directive/testTreeDirective',
        'app/shared/services/runConfigurationService'
    ],
    function (module) {
        module.controller('ConfigController', ConfigController);

        function ConfigController($scope, $rootScope, $timeout, $state, $cookies, DialogService, RunConfigurationService) {

            $rootScope.viewInfo.icon = "fa fa-sliders";
            $rootScope.viewInfo.title = "Run Configuration";

            var vm = this;

            if ($application.homeLoaded == false) {
                return;
            }

            var nodeId = 0;
            var runConfigTree = null;
            var rootNode = null;
            vm.deleteConfiguration = deleteConfiguration;
            vm.searchTree = searchTree;
            vm.searchValue = null;
            vm.collapseAll = collapseAll;
            vm.expandAll = expandAll;
            vm.createRunConfig = createRunConfig;

            /* ------ Initialize Run Configuration tree ------*/
            function initTestTree() {
                runConfigTree = $('#runConfigTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Run Configurations",
                            iconCls: 'classification-root',
                            object: null,
                            children: []
                        }
                    ],
                    onSelect: onSelectType,
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit
                });

                rootNode = runConfigTree.tree('find', 0);
                $(document).click(function () {
                    $("#contextMenu").hide();
                    $("#runContextMenu").hide();
                });


                loadRCConfigScenarios();
            }

            /* ------- Load Run Configuration Scenarios ------*/
            function loadRCConfigScenarios() {
                RunConfigurationService.getRunConfigTree().then(
                    function (data) {
                        var nodes = [];
                        if (data.length > 0) {
                            $rootScope.selectedNode = true;
                        } else {
                            $rootScope.selectedNode = false;
                        }
                        angular.forEach(data, function (type) {
                            if (type.objectType = "TESTRUNCONFIGURATION") {
                                var node = makeNode(type);
                                type.children = [];
                                type.children.push(type.scenario);

                                if (type.children != null && type.children != undefined && type.children.length > 0) {

                                    visitChildren(node, type.children);
                                }
                                nodes.push(node);
                            }
                        });

                        runConfigTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function visitChildren(parent, types) {
                parent.state = "closed";
                var nodes = [];
                angular.forEach(types, function (type) {
                    if (type.objectType == "TESTRUNCONFIGURATION") {
                        var node = makeNode(type);
                    } else if (type.objectType == 'TESTSCENARIO') {
                        var node = makeScenarioNode(type);
                    } else if (type.objectType == 'TESTPLAN') {
                        var node = makeTestNode(type);
                    } else if (type.objectType == 'TESTSUITE') {
                        var node = makeSuiteNode(type);
                    } else if (type.objectType == 'TESTCASE') {
                        var node = makeCaseNode(type);
                    }
                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }


            function makeNode(type) {
                if (type.objectType = "TESTRUNCONFIGURATION")
                    var treeNode = {
                        id: ++nodeId,
                        text: type.name + '[' + type.scenario.name + ']',
                        object: type,
                        type: type.type,
                        iconCls: 'configuration-node'
                    };

                return treeNode;
            }

            function makeScenarioNode(type) {
                if (type.objectType = "TESTSCENARIO")
                    var treeNode = {
                        id: ++nodeId,
                        text: type.name,
                        object: type,
                        type: type.type,
                        iconCls: 'scenario-node'
                    };

                return treeNode;
            }

            function makeTestNode(type) {
                var treeNode = {
                    id: ++nodeId,
                    text: type.name,
                    object: type,
                    type: type.type,
                    iconCls: 'plan-node'
                };

                return treeNode;
            }

            function makeSuiteNode(type) {
                var treeNode = {
                    id: ++nodeId,
                    text: type.name,
                    object: type,
                    type: type.type,
                    iconCls: 'suite-node'
                };

                return treeNode;
            }

            function makeCaseNode(type) {
                var treeNode = {
                    id: ++nodeId,
                    text: type.name,
                    object: type,
                    type: type.type,
                    iconCls: 'case-node'
                };

                return treeNode;
            }

            function onSelectType(node) {
                vm.selectedNode = runConfigTree.tree('getData', node.target);
                var typeObject = vm.selectedNode.object;
                if (vm.selectedNode.id == 0) {
                    if (vm.selectedNode.children.length > 0) {
                        $rootScope.selectedNode = true;
                    } else {
                        $rootScope.selectedNode = false;
                    }
                }
                $scope.$apply();

                $scope.$broadcast('app.runConfiguration.details', {typeObject: typeObject, nodeId: node.id});
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                vm.selectedNode = runConfigTree.tree('getSelected');
                if (vm.selectedNode.object == null) {
                    vm.menu1 = $("#contextMenu");
                    vm.menu1.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu1.show();
                }
                if (vm.selectedNode.object.objectType == 'TESTRUNCONFIGURATION') {
                    vm.menu2 = $("#runContextMenu");
                    vm.menu2.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu2.show();
                }
            }

            function showHideDeleteOption(node, type) {
                if (node.object.children.length == 0) {
                    $(type).show();
                } else {
                    $(type).hide();
                }
            }

            /* ----- Delete Run Configuration -----*/
            function deleteConfiguration() {
                vm.selectedNode = runConfigTree.tree('getSelected');
                if (vm.selectedNode.object.objectType == "TESTRUNCONFIGURATION") {
                    RunConfigurationService.getConfigurationRuns(vm.selectedNode.object.id).then(
                        function (data) {
                            if (data.length > 0) {
                                options = {
                                    title: 'Delete RunConfiguration',
                                    message: 'All related Test Runs should be deleted. Please confirm to delete.',
                                    okButtonClass: 'btn-danger'
                                };
                            } else {
                                options = {
                                    title: 'Delete RunConfiguration',
                                    message: 'Please confirm to this Run Configuration.',
                                    okButtonClass: 'btn-danger'
                                };
                            }
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    var data = runConfigTree.tree('getData', vm.selectedNode.target);
                                    if (data != null && data.object != null) {
                                        RunConfigurationService.deleteRunConfig(data.object.id).then(
                                            function (data) {
                                                runConfigTree.tree('remove', vm.selectedNode.target);
                                                $rootScope.showSuccessMessage(vm.selectedNode.object.name + " :" + "Configuration deleted successfully");
                                                $rootScope.$broadcast('app.runConfiguration.details', {object: null});
                                            }
                                        )
                                    }
                                }
                            });

                        }
                    )

                }
            }

            function onAfterEdit(node) {
                var parent = runConfigTree.tree('getParent', node.target);
                var parentData = runConfigTree.tree('getData', parent.target);
                var data = runConfigTree.tree('getData', node.target);
                if (data.object == null) {
                    data.object = {}
                }

                if (parentData.id != 0 && parentData.object != undefined) {
                    data.object.parent = parentData.object.id;
                }

                data.object.name = node.text;
                data.object.objectType = node.objectType;
                $scope.$broadcast('app.runConfiguration.details', {typeObject: data.object, nodeId: nodeId});
                runConfigTree.tree('select', node.target);
            }

            /* ------- Create Run Configuration ------*/
            function createRunConfig() {
                vm.selectedNode = runConfigTree.tree('getSelected');
                var options = {
                    title: "New Run Configuration",
                    template: 'app/desktop/modules/config/new/newRunConfigurationView.jsp',
                    controller: 'NewRunConfigurationController as newRunConfigurationVm',
                    resolve: 'app/desktop/modules/config/new/newRunConfigurationController',
                    width: 600,
                    side: "left",
                    showMask: true,
                    buttons: [
                        {text: "Create", broadcast: 'app.runConfiguration.new'}
                    ],
                    callback: function (data) {
                        var nodeid = ++nodeId;

                        var nodes = [];
                        var node = makeNode(data);
                        data.children = [];
                        data.children.push(data.scenario);

                        if (data.children != null && data.children != undefined && data.children.length > 0) {

                            visitChildren(node, data.children);
                        }
                        nodes.push(node);
                        runConfigTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                };

                $rootScope.showSidePanel(options);

            }

            vm.onSave = onSave;

            function onSave() {
                $scope.$broadcast('app.runConfiguration.save');
            }

            function update(event, args) {
                var node = runConfigTree.tree('find', args.nodeId);
                if (node) {
                    runConfigTree.tree('update', {
                        target: node.target,
                        text: args.nodeName.name,
                        object: args.nodeName
                    });
                }
            }


            function searchTree() {
                if (vm.searchValue != null) {
                    $('#runConfigTree').tree('expandAll');
                }
                $('#runConfigTree').tree('doFilter', vm.searchValue);
            }

            function collapseAll() {
                var node = $('#runConfigTree').tree('getSelected');
                if (node) {
                    $('#runConfigTree').tree('collapseAll', node.target);
                }
                else {
                    $('#runConfigTree').tree('collapseAll');
                }
            }

            function expandAll() {
                var node = $('#runConfigTree').tree('getSelected');
                if (node) {
                    $('#runConfigTree').tree('expandAll', node.target);
                }
                else {
                    $('#runConfigTree').tree('expandAll');
                }
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    initTestTree();
                    $scope.$on("app.runConfiguration.update", update);
                })
            })();
        }
    }
);