define(
    [
        'app/desktop/modules/run/run.module',
        'split-pane',
        'jquery.easyui',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/testRunService',
        'app/desktop/modules/directive/testTreeDirective',
        'app/shared/services/testCaseService',
        'app/shared/services/runConfigurationService',
        'app/desktop/modules/run/details/tabs/basic/runCaseBasicController',
        'app/desktop/modules/run/details/tabs/params/runCaseParamsController',
        'app/desktop/modules/run/details/tabs/files/runCaseFilesController',
        'app/desktop/modules/run/details/tabs/params/runCaseParamsController',
        'app/desktop/modules/run/details/tabs/log/runCaseLogFileController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('RunDetailsController', RunDetailsController);

        function RunDetailsController($scope, $rootScope, $timeout, $stateParams, CommonService, $state, $cookies, DialogService, TestRunService, RunConfigurationService,
                                      ObjectAttributeService, ObjectTypeAttributeService) {

            $rootScope.viewInfo.icon = "fa fa-play";
            $rootScope.viewInfo.title = "Test Run Details";

            var vm = this;
            vm.back = back;
            vm.runCaseDetailsTabActivated = runCaseDetailsTabActivated;
            $rootScope.loadRunCaseFiles = loadRunCaseFiles;
            $rootScope.saveRunCaseProperties = saveRunCaseProperties;
            $rootScope.cancelRunCaseChanges = cancelRunCaseChanges;
            vm.saveRunProperties = saveRunProperties;
            vm.editRunProperties = editRunProperties;
            vm.cancelChanges = cancelChanges;
            vm.showDropzone = false;
            vm.runFile = false;
            vm.showFile = false;
            vm.downloadFile = downloadFile;
            vm.deleteFile = deleteFile;
            vm.selectFile = selectFile;
            vm.fileSizeToString = fileSizeToString;
            vm.deleteTestRun = deleteTestRun;
            vm.searchTree = searchTree;
            vm.searchValue = null;
            $rootScope.viewInfo.total = null;
            $rootScope.viewInfo.passed = null;
            $rootScope.viewInfo.failed = null;
            $rootScope.viewInfo.testrunDate = null;
            $rootScope.viewInfo.testRunConfigurationName = null;
            $rootScope.viewInfo.runScenario = null;
            $rootScope.runCaseAttributes = [];
            $rootScope.saveTimeProperty = saveTimeProperty;
            $rootScope.runCase = null;
            vm.testCaseDetails = null;
            $rootScope.runScenario = null;
            $rootScope.runPlan = null;
            $rootScope.runSuite = null;
            $rootScope.log = null;
            var nodeId = 0;
            var testRunTree = null;
            var testFileTree = null;
            var rootNode = null;
            var fileNode = null;
            vm.scenName = null;
            var testRunId = $stateParams.testRunId;
            if ($application.homeLoaded == false) {
                return;
            }

            function back() {
                $state.go('app.run.all');

            }

            /* ------- Initialize TestRun Tree -----*/
            function initTestTree() {
                testRunTree = $('#runCase').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Test Run",
                            iconCls: 'classification-root',
                            object: null,
                            children: []
                        }
                    ],
                    onSelect: onSelectType,
                    onContextMenu: onContextMenu
                });

                rootNode = testRunTree.tree('find', 0);
                $(document).click(function () {
                    $("#contextMenu").hide();
                });
                loadTestRunScenario();
            }


            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/run/details/tabs/basic/runCaseBasicView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                },
                params: {
                    id: 'details.params',
                    heading: 'Params',
                    template: 'app/desktop/modules/run/details/tabs/params/runCaseParamsView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                logs: {
                    id: 'details.logs',
                    heading: 'Log',
                    template: 'app/desktop/modules/run/details/tabs/log/runCaseLogFileView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: 'Files',
                    template: 'app/desktop/modules/run/details/tabs/files/runCaseFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                }

            };


            /* -------  Load Test Run Tree ------*/
            function loadTestRunScenario() {
                TestRunService.getTestTreeBasedOnTestRunId(testRunId).then(
                    function (data) {
                        $rootScope.viewInfo.total = data.total;
                        $rootScope.viewInfo.passed = data.passed;
                        $rootScope.viewInfo.failed = data.failed;
                        vm.scenName = data.scenarioName;
                        $rootScope.viewInfo.testrunDate = data.startTime;
                        $rootScope.viewInfo.runScenario = data.runScenario.name;
                        $rootScope.viewInfo.testRunConfigurationName = data.testRunConfiguration.name;
                        if (data.runOutputLog != null) {
                            $rootScope.log = data.runOutputLog.log;
                        }

                        var nodes = [];

                        var treeNode = {
                            id: ++nodeId,
                            text: 'Files',
                            object: 'TESTFILE',
                            type: 'TESTFILE',
                            iconCls: '<i class="fa fa-file" style="font-size: 14px !important;color: #915608;"></i>'
                        };


                        vm.testcases = data.testRunConfiguration;
                        if (vm.testcases.objectType = "TESTRUNCONFIGURATION") {
                            var node = makeNode(vm.testcases);
                            vm.testcases.children = [];
                            vm.testcases.children.push(data.runScenario);

                            if (vm.testcases.children != null && vm.testcases.children != undefined && vm.testcases.children.length > 0) {

                                visitChildren(node, vm.testcases.children);
                            }
                            nodes.push(node);
                        }

                        nodes.push(treeNode);
                        testRunTree.tree('append', {
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
                    } else if (type.objectType == 'RUNSCENARIO') {
                        var node = makeScenarioNode(type);
                    } else if (type.objectType == 'RUNPLAN') {
                        var node = makeTestNode(type);
                    } else if (type.objectType == 'RUNSUITE') {
                        var node = makeSuiteNode(type);
                    } else if (type.objectType == 'RUNCASE') {
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
                        text: type.name + '[' + vm.scenName + ']',
                        object: type,
                        type: type.type,
                        iconCls: 'configuration-node'
                    };

                return treeNode;
            }

            function makeScenarioNode(type) {
                if (type.objectType = "RUNSCENARIO")
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
                vm.selectedRunNode = node;
                if (node.type == 'TESTFILE') {
                    $("#itemFiles").show();
                    $("#runScenarioDetailsDiv").hide();
                    $("#runPlanDetailsDiv").hide();
                    $("#runSuiteDetailsDiv").hide();
                    $("#testCaseDetailsDiv").hide();
                    $("#attributes").hide();
                    vm.showFile = true;
                    vm.showDropzone = false;
                } else {
                    vm.showFile = false;
                    vm.showFileDropzone = false;
                    var data = testRunTree.tree('getData', node.target);
                    vm.typeObject = data.object;
                    if (vm.typeObject.objectType == 'RUNSCENARIO') {
                        getScenario(vm.typeObject.id);
                    }
                    if (vm.typeObject.objectType == 'RUNPLAN') {
                        getRunPlan(vm.typeObject.id);
                    }
                    if (vm.typeObject.objectType == 'RUNSUITE') {
                        getRunSuite(vm.typeObject.id);
                    }
                    if (vm.typeObject.objectType == 'RUNCASE') {
                        getRunCase(vm.typeObject.id);
                    }
                }
            }

            /* ------ Get Scenario Details -----*/
            function getScenario(scenarioId) {
                $("#itemFiles").hide();
                $("#runScenarioDetailsDiv").show();
                $("#runPlanDetailsDiv").hide();
                $("#runSuiteDetailsDiv").hide();
                $("#testCaseDetailsDiv").hide();
                TestRunService.getRunScenario(scenarioId).then(
                    function (data) {
                        $rootScope.runScenario = data;
                        vm.selectedId = $rootScope.runScenario.id;
                        vm.selectedType = $rootScope.runScenario.objectType;
                        loadRunScenarioAttributes();

                    }
                )

            }

            /* ------ Get Run Plan Details -----*/
            function getRunPlan(runPlanId) {
                $("#itemFiles").hide();
                $("#runScenarioDetailsDiv").hide();
                $("#runPlanDetailsDiv").show();
                $("#runSuiteDetailsDiv").hide();
                $("#testCaseDetailsDiv").hide();
                TestRunService.getRunPlan(runPlanId).then(
                    function (data) {
                        $rootScope.runPlan = data;
                        vm.selectedId = $rootScope.runPlan.id;
                        vm.selectedType = $rootScope.runPlan.objectType;
                        loadRunScenarioAttributes();
                    }
                )
            }

            /* ------ Get Run Suite Details -----*/
            function getRunSuite(runSuiteId) {
                $("#itemFiles").hide();
                $("#testCaseDetailsDiv").hide();
                $("#runScenarioDetailsDiv").hide();
                $("#runPlanDetailsDiv").hide();
                $("#runSuiteDetailsDiv").show();
                TestRunService.getRunSuite(runSuiteId).then(
                    function (data) {
                        $rootScope.runSuite = data;
                        vm.selectedId = $rootScope.runSuite.id;
                        vm.selectedType = $rootScope.runSuite.objectType;
                        loadRunScenarioAttributes();
                    }
                )
            }

            /* ------ Get Run Case Details -----*/
            function getRunCase(runCase) {
                $("#itemFiles").hide();
                $("#runScenarioDetailsDiv").hide();
                $("#runPlanDetailsDiv").hide();
                $("#runSuiteDetailsDiv").hide();
                $("#testCaseDetailsDiv").show();
                $rootScope.runCaseAttributes = [];
                $rootScope.inputParams = [];
                $rootScope.outputParams = [];
                TestRunService.getRunCaseDetails(runCase).then(
                    function (data) {
                        $rootScope.runCase = data.runCase;
                        $rootScope.selectedRunCaseId = data.runCase.id;
                        $scope.$broadcast('app.run.tabactivated', {tabId: 'details.files'});
                        angular.forEach(data.runOutPutParams, function (outParam) {
                            if (outParam.description.length > 10) {
                                var desc = outParam.description.substring(0, 7);
                                outParam.id = desc + '...';
                                $rootScope.outputParams.push(outParam);
                            }

                            else {
                                var desc = outParam.description;
                                outParam.id = desc;
                                $rootScope.outputParams.push(outParam);
                            }

                        })

                        angular.forEach(data.runInputParams, function (inputParam) {
                            if (inputParam.description.length > 10) {
                                var desc = inputParam.description.substring(0, 7);
                                inputParam.id = desc + '...';
                                $rootScope.inputParams.push(inputParam);
                            }

                            else {
                                var desc = inputParam.description;
                                inputParam.id = desc;
                                $rootScope.inputParams.push(inputParam);
                            }

                        })
                        loadRunCaseAttributes();
                        loadRunCaseFiles();
                        vm.showFile = false;
                        vm.runFile = true;
                    }
                )
            }

            /* ------ Load TestRun Files ------*/
            function loadRunCaseFiles() {
                TestRunService.getTestRunCaseFiles($rootScope.selectedRunCaseId).then(
                    function (data) {
                        $rootScope.runCaseFiles = data;
                        vm.loading = false;
                        CommonService.getPersonReferences($rootScope.runCaseFiles, 'createdBy');
                        CommonService.getPersonReferences($rootScope.runCaseFiles, 'modifiedBy');
                    }
                )
            }

            /* ------ Load RunCase Attributes ------*/
            function loadRunCaseAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("RUNCASE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: $rootScope.runCase.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: $rootScope.runCase.id,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    dateValue: null,
                                    booleanValue: null,
                                    timeValue: null,
                                    timestampValue: null
                                },
                                editMode: false,
                                showTimestamp: false,
                                showTimeAttribute: false
                            };
                            $rootScope.runCaseAttributes.push(att);
                        });

                        loadObjectProperties();
                    });
            }

            /* ---- Load RunCase ObjectProperies ------*/
            function loadObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes($rootScope.runCase.id).then(
                    function (data) {
                        var map = new Hashtable();
                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach($rootScope.runCaseAttributes, function (attribute) {
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.doubleValue = value.doubleValue;
                                attribute.value.dateValue = value.dateValue;
                                attribute.value.timeValue = value.timeValue;
                                attribute.value.timestampValue = value.timestampValue;
                            }
                        });
                    }
                )
            }


            /* ------  File Delete -----*/
            function deleteFile(file) {
                var options = {
                    title: 'Delete File',
                    message: 'Are you sure you want to delete this file?',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TestRunService.deleteFile(testRunId, file.id).then(
                            function (data) {
                                var index = vm.files.indexOf(file);
                                vm.files.splice(index, 1);
                                $rootScope.showSuccessMessage("File deleted successfully");
                            }
                        )
                    }
                });
            }

            function onSelectFileType(node) {
                var data = testFileTree.tree('getData', node.target);
                vm.showFile = true;
                vm.runFile = false;

            }

            function handleDragEnter(e) {
                this.classList.add('drag-over');
                vm.showFileDropzone = true;
            }

            function handleDragLeave(e) {
                this.classList.remove('drag-over');
                vm.showFileDropzone = false;
            }

            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            var fileDropZone = null;

            function initFileDrop() {
                $("#itemFilesTable").on('dragover', function () {
                    vm.showFileDropzone = true;
                    $scope.$apply();
                });
                $("#itemFilesTable").on('dragleave', function () {
                    vm.showFileDropzone = false;
                    $scope.$apply();
                });
                $("#itemFilesTable").on('drop', function () {

                });

            }

            /* ------ Initialize to File Table DropZone -----*/
            function initFilesTableDropzone() {
                var previewNode = $("#template");
                var previewTemplate = previewNode.parent().html();
                previewNode.remove();
                var dropZone = new Dropzone(document.querySelector('#itemFiles'), { // Make the whole body a dropzone
                    url: "api/test/run/" + testRunId + "/files", // Set the url
                    thumbnailWidth: 80,
                    thumbnailHeight: 80,
                    parallelUploads: 20,
                    maxFilesize: 1000,
                    previewTemplate: previewTemplate,
                    autoQueue: true, // Make sure the files aren't queued until manually added
                    previewsContainer: "#previews"
                });

                dropZone.on("queuecomplete", function (progress) {
                    $("#itemFilesTableContainer").removeClass('drag-over');
                    vm.showFileDropzone = false;
                    dropZone.removeAllFiles(true);
                    $scope.$apply();
                    $rootScope.showSuccessMessage("File(s) added successfully");
                    loadFiles();
                });

                $("#itemFilesTableContainer").on('dragover', handleDragEnter);
                $("#itemFilesTableContainer").on('dragleave', handleDragLeave);
                $("#itemFilesTableContainer").on('drop', handleDragLeave);
            }

            /* ------- Load Files -----*/
            function loadFiles() {
                TestRunService.getRunCaseFiles(testRunId).then(
                    function (data) {
                        vm.files = data;
                        vm.loading = false;
                        $rootScope.searchModeType = false;
                        CommonService.getPersonReferences(vm.files, 'createdBy');
                        CommonService.getPersonReferences(vm.files, 'modifiedBy');
                    }
                );

            }

            /* ------- Convert fileSize to String ------*/
            function fileSizeToString(bytes) {
                if (bytes == 0) {
                    return "0.00 B";
                }
                var e = Math.floor(Math.log(bytes) / Math.log(1024));
                return (bytes / Math.pow(1024, e)).toFixed(2) + ' ' + ' KMGTP'.charAt(e) + 'B';
            }

            /* ------ Click to enter file -----*/
            function selectFile() {
                $('#itemFiles')[0].click();
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                vm.selectedNode = testRunTree.tree('getSelected');
                if (vm.selectedNode.object.objectType == "TESTRUNCONFIGURATION") {
                    vm.menu1 = $("#contextMenu");
                    vm.menu1.css({
                        left: e.pageX,
                        top: e.pageY
                    });
                    vm.menu1.show();
                    //showHideDeleteOption(node, '#deleteRunConfiguration')
                }
            }

            /* ----- Delete Test Run -----*/
            function deleteTestRun() {
                vm.selectedNode = testRunTree.tree('getSelected');
                if (vm.selectedNode.object.objectType == "TESTRUNCONFIGURATION") {
                    options = {
                        title: 'Delete TestRun',
                        message: 'All related items should be deleted. Please confirm to delete.',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            var data = testRunTree.tree('getData', vm.selectedNode.target);
                            if (data != null && data.object != null) {
                                TestRunService.deleteTestRun(testRunId).then(
                                    function (data) {
                                        testRunTree.tree('remove', vm.selectedNode.target);
                                        $rootScope.showSuccessMessage(vm.selectedNode.object.name + " :" + "TestRun deleted successfully");
                                        // $rootScope.$broadcast('app.definition.update', {object: null});
                                    }
                                )
                            }
                        }
                    });
                }

            }


            function searchTree() {
                if (vm.searchValue != null) {
                    $('#runCase').tree('expandAll');
                }
                $('#runCase').tree('doFilter', vm.searchValue);
            }

            /* ----- Download File ----*/
            function downloadFile(file) {
                var url = "{0}//{1}/api/test/run/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    file.testRun, file.id);
                launchUrl(url);
            }

            /* ------ Activate to Tab OnLoad ------*/
            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            /* ------  RunCase Details Tab Activated ----*/
            function runCaseDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.run.tabactivated', {tabId: tabId});

                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            /* ------ Get Yab By Id ------*/
            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }


            /* ------ Load RunCase Attributes -----*/
            function loadRunScenarioAttributes() {
                vm.runAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.selectedType).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.selectedId,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.selectedId,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    dateValue: null
                                },
                                editMode: true,
                                isNew: true
                            };
                            vm.runAttributes.push(att);
                        });

                        loadRunScenarioObjectProperties();
                    });
            }


            /* ------- Validation For RunCase Attributes -----*/
            function validateAttributes(attribute) {
                var valid = true;
                if (attribute.attributeDef.dataType == "STRING" && (attribute.value.stringValue == null || attribute.value.stringValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + attribute.attributeDef.name);
                } else if (attribute.attributeDef.dataType == "INTEGER" && (attribute.value.integerValue == null || attribute.value.integerValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + attribute.attributeDef.name);
                } else if (attribute.attributeDef.dataType == "DOUBLE" && (attribute.value.doubleValue == null || attribute.value.doubleValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + attribute.attributeDef.name);
                } else if (attribute.attributeDef.dataType == "DATE" && (attribute.value.dateValue == null || attribute.value.dateValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select date for " + attribute.attributeDef.name);
                }

                return valid;
            }

            /* ------- Save Run Case Properties -----*/
            function saveRunProperties(attribute) {
                if (attribute.attributeDef.objectType == vm.selectedType) {
                    if (validateAttributes(attribute)) {
                        ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Attribute saved successfully");
                                attribute.editMode = false;
                                loadRunScenarioObjectProperties();
                            }
                        )
                    }
                }
            }

            function saveRunCaseProperties(attribute) {
                if (attribute.attributeDef.objectType == "RUNCASE") {
                    if (validateAttributes(attribute)) {
                        ObjectAttributeService.createObjectAttribute($rootScope.runCase.id, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Attribute saved successfully");
                                attribute.editMode = false;
                                loadObjectProperties();
                            }
                        )
                    }
                }
            }

            /* ------- Cancel RunCase Changes After Edit -----*/
            function cancelRunCaseChanges(attribute) {
                attribute.value.stringValue = $rootScope.stringValue;
                attribute.value.integerValue = $rootScope.integerValue;
                attribute.value.dateValue = $rootScope.dateValue;
                attribute.value.doubleValue = $rootScope.doubleValue;
                attribute.editMode = false;
            }

            function loadRunScenarioObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(vm.selectedId).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.runAttributes, function (attribute) {
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.doubleValue = value.doubleValue;
                                attribute.value.dateValue = value.dateValue;
                                attribute.editMode = false;
                            }
                        });
                    }
                )
            }

            /* ------- Edit RunCase properties ------*/
            function editRunProperties(attribute) {
                attribute.stringValue = attribute.value.stringValue;
                attribute.integerValue = attribute.value.integerValue;
                attribute.dateValue = attribute.value.dateValue;
                attribute.doubleValue = attribute.value.doubleValue;
                attribute.editMode = true;
                attribute.isNew = false;
            }

            function cancelChanges(attribute) {
                attribute.value.stringValue = attribute.stringValue;
                attribute.value.integerValue = attribute.integerValue;
                attribute.value.dateValue = attribute.dateValue;
                attribute.value.doubleValue = attribute.doubleValue;
                attribute.editMode = false;
            }


            function saveTimeProperty(attribute) {
                if (attribute.attributeDef.objectType == "RUNCASE") {
                    if (attribute.value.timeValue != null) {
                        attribute.value.timeValue = moment(attribute.value.timeValue).format("HH:mm:ss");
                        ObjectAttributeService.updateObjectAttribute($rootScope.runCase.id, attribute.value).then(
                            function (data) {
                                attribute.showTimeAttribute = false;
                                $rootScope.showSuccessMessage("save time");
                                loadObjectProperties();
                            }
                        )
                    } else if (attribute.value.timestampValue != null) {
                        attribute.value.timestampValue = moment(attribute.value.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                        ObjectAttributeService.updateObjectAttribute($rootScope.runCase.id, attribute.value).then(
                            function (data) {
                                attribute.showTimestamp = false;
                                $rootScope.showSuccessMessage("save time stamp");
                                loadObjectProperties();
                            }
                        )

                    }
                }
            }


            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    initFilesTableDropzone();
                    initTestTree();
                    loadFiles();
                })
            })();
        }
    }
);
