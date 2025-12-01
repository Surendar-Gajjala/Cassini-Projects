/**
 * Created by SRAVAN on 7/30/2018.
 */
define(['app/desktop/modules/definition/definition.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/desktop/modules/definition/details/definitionDetailsController',
        'app/desktop/modules/directive/testTreeDirective',
        'app/shared/services/testTreeService'


    ],

    function (module) {

        module.controller('DefinitionMainController', DefinitionMainController);

        function DefinitionMainController($scope, $rootScope, $timeout, $interval, $state, CommonService, $cookies, DialogService, $application, TestTreeService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-ellipsis-v";
            $rootScope.viewInfo.title = "Test Definition";

            var nodeId = 0;
            var testTree = null;
            var rootNode = null;
            vm.onSave = onSave;
            vm.deleteType = deleteType;
            $scope.importScenario = importScenario;
            vm.allTestCasesWithScenarios = [];
            vm.testCasesForExport = [];
            vm.createType = createType;
            vm.searchTree = searchTree;
            vm.searchValue = null;
            vm.collapseAll = collapseAll;
            vm.expandAll = expandAll;
            $rootScope.exportTestCases = exportTestCases;

            /* ------ Initialize Definition tree ----*/
            function initTestTree() {
                testTree = $('#testTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Test Definition",
                            iconCls: 'classification-root',
                            object: null,
                            children: []
                        }
                    ],
                    onSelect: onSelectType,
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit
                });

                rootNode = testTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                    $("#planContextMenu").hide();
                    $("#suiteContextMenu").hide();
                    $("#caseContextMenu").hide();
                    $("#delTcContextMenu").hide();
                });


                loadTestTree();
            }

            function onAfterEdit(node) {
                var promise = null;
                var deleted = false;
                var parent = testTree.tree('getParent', node.target);
                var parentData = testTree.tree('getData', parent.target);
                var data = testTree.tree('getData', node.target);
                if (data.object == null) {
                    data.object = {}
                }

                vm.selectedType = data.type;
                if (parentData.id != 0 && parentData.object != undefined) {
                    data.object.parent = parentData.object.id;
                }

                data.object.name = node.text;
                data.object.objectType = node.objectType;
                $scope.$broadcast('app.definition.details', {typeObject: data.object, nodeId: nodeId});
                testTree.tree('select', node.target);
            }


            function onContextMenu(e, node) {
                if (node.object == null) {
                    e.preventDefault();
                    vm.selectedNode = testTree.tree('getSelected');
                    if (vm.selectedNode.object == null) {
                        vm.menu1 = $("#contextMenu");
                        vm.menu1.css({
                            left: e.pageX,
                            top: e.pageY
                        });
                        vm.menu1.show();
                        if (vm.menu2 != undefined) {
                            vm.menu2.contextmenu = null;
                            vm.menu2.hide();
                        } else if (vm.menu3 != undefined) {
                            vm.menu3.contextmenu = null;
                            vm.menu3.hide();
                        } else if (vm.menu4 != undefined) {
                            vm.menu4.contextmenu = null;
                            vm.menu4.hide();
                        }
                    }
                }


                else if (node.object.id != undefined && node.object.id != null && node.object.id != "") {
                    e.preventDefault();
                    vm.selectedNode = testTree.tree('getSelected');
                    if (vm.selectedNode.object == null) {
                        vm.menu1 = $("#contextMenu");
                        vm.menu1.css({
                            left: e.pageX,
                            top: e.pageY
                        });
                        vm.menu1.show();
                        if (vm.menu2 != undefined) {
                            vm.menu2.contextmenu = null;
                            vm.menu2.hide();
                        } else if (vm.menu3 != undefined) {
                            vm.menu3.contextmenu = null;
                            vm.menu3.hide();
                        } else if (vm.menu4 != undefined) {
                            vm.menu4.contextmenu = null;
                            vm.menu4.hide();
                        }
                    }

                    if (vm.selectedNode.object != null && vm.selectedNode.object.objectType == 'TESTSCENARIO') {
                        vm.testCasesForExport = [];
                        vm.ScenarioName = $rootScope.allTestCasesWithScenarios.name;
                        angular.forEach($rootScope.allTestCasesWithScenarios.testCases, function (caseLoop) {
                            caseLoop.executionType = vm.ScenarioName;
                            var index = vm.testCasesForExport.indexOf(caseLoop);
                            if (index == -1) {
                                vm.testCasesForExport.push(caseLoop);
                            }


                        })

                        vm.menu2 = $("#planContextMenu");
                        vm.menu2.css({
                            left: e.pageX,
                            top: e.pageY
                        });
                        vm.menu2.show();
                        if (vm.menu3 != undefined) {
                            vm.menu3.contextmenu = null;
                            vm.menu3.hide();
                        } else if (vm.menu4 != undefined) {
                            vm.menu4.contextmenu = null;
                            vm.menu4.hide();
                        }
                        vm.menu2.on("click", "a", function () {
                            vm.menu2.hide();
                        });
                    }

                    if (vm.selectedNode.object != null && vm.selectedNode.object.objectType == 'TESTPLAN') {

                        vm.menu3 = $("#suiteContextMenu");
                        vm.menu3.css({
                            left: e.pageX,
                            top: e.pageY
                        });
                        vm.menu3.show();
                        if (vm.menu2 != undefined) {
                            vm.menu2.contextmenu = null;
                            vm.menu2.hide();
                        } else if (vm.menu4 != undefined) {
                            vm.menu4.contextmenu = null;
                            vm.menu4.hide();
                        }
                        vm.menu3.on("click", "a", function () {
                            vm.menu3.hide();
                        });
                    }

                    if (vm.selectedNode.object != null && vm.selectedNode.object.objectType == 'TESTSUITE') {

                        vm.menu4 = $("#caseContextMenu");
                        vm.menu4.css({
                            left: e.pageX,
                            top: e.pageY
                        });
                        vm.menu4.show();
                        if (vm.menu2 != undefined) {
                            vm.menu2.contextmenu = null;
                            vm.menu2.hide();
                        } else if (vm.menu3 != undefined) {
                            vm.menu3.contextmenu = null;
                            vm.menu3.hide();
                        }
                        vm.menu4.on("click", "a", function () {
                            vm.menu4.hide();
                        });
                    }

                    if (vm.selectedNode.object != null && vm.selectedNode.object.objectType == 'TESTCASE') {
                        vm.menu5 = $("#delTcContextMenu");
                        vm.menu5.css({
                            left: e.pageX,
                            top: e.pageY
                        });
                        vm.menu5.show();
                        if (vm.menu2 != undefined) {
                            vm.menu2.contextmenu = null;
                            vm.menu2.hide();
                        } else if (vm.menu3 != undefined) {
                            vm.menu3.contextmenu = null;
                            vm.menu3.hide();
                        }
                        vm.menu5.on("click", "a", function () {
                            vm.menu5.hide();
                        });
                    }
                }


            }

            function showHideDeleteOption(node, type) {
                if (node.object.children.length == 0) {
                    $(type).show();
                } else {
                    $(type).hide();
                }
            }

            /* ----- Create Scenario,Plan,Suite and Test -----*/
            function createType() {
                vm.selectedNode = testTree.tree('getSelected');
                var objectType = null;
                var objectText = null;
                var icon = null;

                if (vm.selectedNode.object == null) {
                    objectType = "TESTSCENARIO";
                    objectText = "New Scenario";
                    icon = 'scenario-node';
                } else if (vm.selectedNode.object.objectType == "TESTSCENARIO") {
                    objectType = "TESTPLAN";
                    objectText = "New Plan";
                    icon = 'plan-node';
                } else if (vm.selectedNode.object.objectType == "TESTPLAN") {
                    objectType = "TESTSUITE";
                    objectText = "New Suite";
                    icon = 'suite-node';
                } else if (vm.selectedNode.object.objectType == "TESTSUITE") {
                    objectType = "TESTCASE";
                    objectText = "New TestCase";
                    icon = 'case-node';
                }

                var nodeid = ++nodeId;

                testTree.tree('append', {
                    parent: vm.selectedNode.target,
                    data: [
                        {
                            id: nodeid,
                            iconCls: icon,
                            text: objectText,
                            objectType: objectType,
                            object: null
                        }
                    ]
                });
                if (vm.selectedNode.children.length != null) {
                    var newNode = testTree.tree('find', nodeid);
                    testTree.tree('expandTo', newNode.target);
                }

                var newNode = testTree.tree('find', nodeid);
                if (newNode != null) {
                    testTree.tree('beginEdit', newNode.target);

                    $timeout(function () {
                        $('.tree-editor').focus().select();
                    }, 1);
                }

            }

            /* ----- Delete Scenario,Plan,Suite and Test -----*/
            function deleteType() {
                vm.selectedNode = testTree.tree('getSelected');
                if (vm.selectedNode.object.objectType == "TESTSCENARIO") {
                    TestTreeService.getPlanByScenario(vm.selectedNode.object.id).then(
                        function (data) {
                            if (data.length > 0) {
                                options = {
                                    title: 'Delete Scenario',
                                    message: 'All related Items should be deleted. Please confirm to delete this Scenario.',
                                    okButtonClass: 'btn-danger'
                                };
                            } else {
                                options = {
                                    title: 'Delete Scenario',
                                    message: 'Please confirm to this Scenario.',
                                    okButtonClass: 'btn-danger'
                                };
                            }
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    var data = testTree.tree('getData', vm.selectedNode.target);
                                    if (data != null && data.object != null) {
                                        TestTreeService.deleteScenario(data.object.id).then(
                                            function (data) {
                                                testTree.tree('remove', vm.selectedNode.target);
                                                $rootScope.showSuccessMessage(vm.selectedNode.object.name + " :" + "Scenario deleted successfully");
                                                $rootScope.$broadcast('app.definition.details', {object: null});
                                            }
                                        )
                                    }
                                }
                            });
                        }
                    )
                } else if (vm.selectedNode.object.objectType == "TESTPLAN") {
                    TestTreeService.getSuiteByPlan(vm.selectedNode.object.id).then(
                        function (data) {
                            if (data.length > 0) {
                                options = {
                                    title: 'Delete Plan',
                                    message: 'All related Items should be deleted. Please confirm to delete this Plan.',
                                    okButtonClass: 'btn-danger'
                                };
                            } else {
                                options = {
                                    title: 'Delete Plan',
                                    message: 'Please confirm to this Plan.',
                                    okButtonClass: 'btn-danger'
                                };
                            }
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    var data = testTree.tree('getData', vm.selectedNode.target);
                                    if (data != null && data.object != null) {
                                        TestTreeService.deletePlan(data.object.id).then(
                                            function (data) {
                                                testTree.tree('remove', vm.selectedNode.target);
                                                $rootScope.showSuccessMessage(vm.selectedNode.object.name + " : " + "Plan deleted successfully");
                                                $rootScope.$broadcast('app.definition.details', {object: null});
                                            }
                                        )
                                    }
                                }
                            });
                        });

                } else if (vm.selectedNode.object.objectType == "TESTSUITE") {
                    TestTreeService.getCaseBySuite(vm.selectedNode.object.id).then(
                        function (data) {
                            if (data.length > 0) {
                                options = {
                                    title: 'Delete Suite',
                                    message: 'All related Items should be deleted. Please confirm to delete this suite.',
                                    okButtonClass: 'btn-danger'
                                };
                            } else {
                                options = {
                                    title: 'Delete Suite',
                                    message: 'Please confirm to this Suite.',
                                    okButtonClass: 'btn-danger'
                                };
                            }
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    var data = testTree.tree('getData', vm.selectedNode.target);
                                    if (data != null && data.object != null) {
                                        TestTreeService.deleteSuite(data.object.id).then(
                                            function (data) {
                                                testTree.tree('remove', vm.selectedNode.target);
                                                $rootScope.showSuccessMessage(vm.selectedNode.object.name + " : " + "Suite deleted successfully");
                                                $rootScope.$broadcast('app.definition.details', {object: null});
                                            }
                                        )
                                    }
                                }
                            });
                        });
                } else if (vm.selectedNode.object.objectType == "TESTCASE") {
                    options = {
                        title: 'Delete Test Case',
                        message: 'Please confirm to delete this Test case.',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            var data = testTree.tree('getData', vm.selectedNode.target);
                            if (data != null && data.object != null) {
                                TestTreeService.deleteTestCase(data.object.id).then(
                                    function (data) {
                                        testTree.tree('remove', vm.selectedNode.target);
                                        $rootScope.showSuccessMessage(vm.selectedNode.object.name + " : " + " Test Case deleted successfully");
                                        $rootScope.$broadcast('app.definition.details', {object: null});
                                    }
                                )
                            }
                        }
                    });
                }

            }

            /*------ Load Definition tree ------*/
            function loadTestTree() {
                TestTreeService.getTestTree().then(
                    function (data) {
                        var nodes = [];
                        if (data.length > 0) {
                            $rootScope.showDetailsTitleMessage = true;
                        } else {
                            $rootScope.showDetailsTitleMessage = false;
                        }
                        angular.forEach(data, function (type) {
                            if (type.objectType = 'TESTSCENARIO') {
                                var node = makeNode(type);
                                if (type.children != null && type.children != undefined && type.children.length > 0) {
                                    visitChildren(node, type.children);
                                }
                                nodes.push(node);
                            }
                        });

                        testTree.tree('append', {
                            parent: (rootNode ? rootNode.target : null),
                            data: nodes
                        });
                    }
                )
            }

            /*------- Import Scenario ------*/
            function importScenario() {
                var file1 = document.getElementById("scenarioFile");
                if (vm.importFile1 = file1.files[0]) {
                    $rootScope.showBusyIndicator();
                    TestTreeService.importScenario(vm.importFile1).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            document.getElementById("scenarioFile").value = "";
                            nodeId = 0;
                            initTestTree();
                            $rootScope.showSuccessMessage("Imported successfully");

                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            document.getElementById("scenarioFile").value = "";
                        }
                    )
                }

            }

            function visitChildren(parent, types) {
                parent.state = "closed";
                var nodes = [];
                angular.forEach(types, function (type) {
                    if (type.objectType == 'TESTSCENARIO') {
                        var node = makeNode(type);
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
                if (type.objectType = "TESTSCENARIO")
                    var treeNode = {
                        id: ++nodeId,
                        text: type.name,
                        object: type,
                        iconCls: 'scenario-node'
                    };

                return treeNode;
            }

            function makeTestNode(type) {
                var treeNode = {
                    id: ++nodeId,
                    text: type.name,
                    object: type,
                    iconCls: 'plan-node'
                };

                return treeNode;
            }

            function makeSuiteNode(type) {
                var treeNode = {
                    id: ++nodeId,
                    text: type.name,
                    object: type,
                    iconCls: 'suite-node'
                };

                return treeNode;
            }

            function makeCaseNode(type) {
                var treeNode = {
                    id: ++nodeId,
                    text: type.name,
                    object: type,
                    iconCls: 'case-node'
                };

                return treeNode;
            }

            function update(event, args) {
                var node = testTree.tree('find', args.nodeId);
                if (node) {
                    testTree.tree('update', {
                        target: node.target,
                        text: args.nodeName.name,
                        object: args.nodeName
                    });
                }
            }

            function onSelectType(node) {
                vm.selectedNode = testTree.tree('getData', node.target);
                var typeObject = vm.selectedNode.object;
                if (vm.selectedNode.id == 0) {
                    if (vm.selectedNode.children.length > 0) {
                        $rootScope.showDetailsTitleMessage = true;
                    } else {
                        $rootScope.showDetailsTitleMessage = false;
                    }
                }
                $scope.$apply();
                $scope.$broadcast('app.definition.details', {typeObject: typeObject, nodeId: node.id});
            }

            function onSave() {
                $scope.$broadcast('app.definition.save');
            }


            function searchTree() {
                if (vm.searchValue != null) {
                    $('#testTree').tree('expandAll');
                }
                $('#testTree').tree('doFilter', vm.searchValue);
            }


            function collapseAll() {
                var node = $('#testTree').tree('getSelected');
                if (node) {
                    $('#testTree').tree('collapseAll', node.target);
                }
                else {
                    $('#testTree').tree('collapseAll');
                }
            }

            function expandAll() {
                var node = $('#testTree').tree('getSelected');
                if (node) {
                    $('#testTree').tree('expandAll', node.target);
                }
                else {
                    $('#testTree').tree('expandAll');
                }
            }

            /* ------ Initializion columns for TestCases -----*/
            var initColumns = {
                "ScenarioName": {
                    "columnName": "ScenarioName",
                    "columnValue": null,
                    "columnType": "string"
                },
                "TestCaseId": {
                    "columnName": "TestCaseId",
                    "columnValue": null,
                    "columnType": "int"
                }
                ,
                "TestCaseDescription": {
                    "columnName": "TestCaseDescription",
                    "columnValue": null,
                    "columnType": "string"
                },
                "RunConfiguration": {
                    "columnName": "RunConfiguration",
                    "columnValue": null,
                    "columnType": "string"
                }
            };

            var inputParamHeaders = ["ScenarioName", "TestCaseId", "TestCaseDescription", "RunConfiguration"];

            /*------- Export TestCases ------*/
            function exportTestCases() {
                var exportRows = [];
                var empty = null;
                for (var i = 0; i < vm.testCasesForExport.length; i++) {
                    var exportRwDetails = [];
                    var emptyColumns = angular.copy(initColumns);
                    angular.forEach(inputParamHeaders, function (header) {
                        empty = emptyColumns[header];
                        if (empty != undefined) {
                            var inputParamRow = vm.testCasesForExport[i];
                            if (empty.columnName == "ScenarioName" || empty.columnName == "TestCaseId" || empty.columnName == "TestCaseDescription" || empty.columnName == "RunConfiguration") {
                                if (empty.columnName == "ScenarioName") {
                                    empty.columnValue = inputParamRow.executionType;
                                }
                                if (empty.columnName == "TestCaseId") {

                                    empty.columnValue = inputParamRow.id;


                                }
                                if (empty.columnName == "TestCaseDescription") {
                                    empty.columnValue = inputParamRow.description;
                                }

                                if (empty.columnName == "RunConfiguration") {
                                    empty.columnValue = null;
                                }
                            }

                            exportRwDetails.push(empty);
                        }
                    });
                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                }

                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": 'TestCasesExport',
                    "headers": angular.copy(inputParamHeaders)
                };

                CommonService.exportReport("EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');

                    }
                )
            }


            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    initTestTree();
                    $scope.$on("app.definition.update", update);
                })
            })();
        }

    }
)
;