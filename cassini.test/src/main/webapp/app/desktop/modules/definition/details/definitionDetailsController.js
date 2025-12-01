define(
    [
        'app/desktop/modules/definition/definition.module',
        'app/shared/services/testTreeService',
        'app/shared/services/testCaseService',
        'app/shared/services/runConfigurationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService'
    ],
    function (module) {
        module.controller('DefinitionDetailsController', DefinitionDetailsController);

        function DefinitionDetailsController($scope, $rootScope, $timeout, $state, $cookies, $translate, AttributeAttachmentService, ObjectAttributeService, TestTreeService, RunConfigurationService, ObjectTypeAttributeService, TestCaseService, DialogService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var parsed = angular.element("<div></div>");

            var vm = this;
            var nodeId = null;
            vm.type = null;
            vm.testCaseOutputParams = [];
            vm.testCaseInputParams = [];
            vm.executionType = null;
            vm.scriptLanguageType = null;
            vm.scenarioAttributes = [];
            vm.saveScenarioProperties = saveScenarioProperties;
            vm.editScenarioProperties = editScenarioProperties;
            vm.cancelChanges = cancelChanges;
            vm.onSave = onSave;
            vm.selectProgram = selectProgram;
            vm.selectScript = selectScript;
            vm.addNewInputParam = addNewInputParam;
            vm.addNewOutputParam = addNewOutputParam;
            vm.saveInputParam = saveInputParam;
            vm.editInputParam = editInputParam;
            vm.editOutputParam = editOutputParam;
            vm.deleteInputParam = deleteInputParam;
            vm.saveOutputParam = saveOutputParam;
            vm.deleteOutputParam = deleteOutputParam;
            vm.onCancelOutputParam = onCancelOutputParam;
            vm.onCancelInputParam = onCancelInputParam;
            $scope.importOutPutParams = importOutPutParams;
            $scope.importInputParams = importInputParams;
            vm.deleteAttribute = deleteAttribute;
            vm.linkActivate = false;
            vm.selectedItem = null;
            vm.clickToHideLink = true;
            vm.clickToHideLink1 = false;
            vm.editScript = null;
            vm.editScriptPhython = null;
            vm.importAndExportFlag = false;
            vm.importAndExportFlag1 = false;
            vm.scriptAdded = null;
            $rootScope.allTestCasesWithScenarios = [];

            vm.programExecution = {
                id: null,
                program: null,
                params: null,
                workingDir: null
            };

            vm.scriptExecution = {
                id: null,
                scriptLanguage: null,
                script: null
            };

            vm.languageTypes = [
                'GROOVY',
                'PYTHON'
            ];

            var inputParam = {
                id: null,
                testCase: null,
                name: null,
                description: null,
                dataType: null,
                editMode: true,
                isNew: true
            };

            var outputParam = {
                id: null,
                testCase: null,
                name: null,
                description: null,
                dataType: null,
                editMode: true,
                isNew: true
            };

            vm.dataTypes = [
                'STRING',
                'INTEGER',
                'DOUBLE',
                'DATE'
            ];

            /* ----- SidePanel for Create Groovy script Or Python -----*/
            $scope.clickToOpenSidePanel = function () {
                if (vm.selectedItem != null) {
                    if (vm.selectedItem == "GROOVY") {
                        var options = {
                            title: 'Groovy',
                            template: 'app/desktop/modules/definition/new/newScriptView.jsp',
                            controller: 'NewScriptController as newScriptVm',
                            resolve: 'app/desktop/modules/definition/new/newScriptController',
                            width: 1000,
                            showMask: true,
                            data: {
                                selectedScript: vm.selectedItem
                            },
                            buttons: [
                                {text: 'Create', broadcast: 'app.definitions.newScript'}
                            ],
                            callback: function (result) {
                                vm.scriptAdded = result;
                                vm.editScript = result;
                                vm.linkActivate = false;
                                vm.linkActivate1 = true;
                                vm.clickToHideLink1 = true;
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }
                    if (vm.selectedItem == "PYTHON") {
                        var options = {
                            title: 'Python',
                            template: 'app/desktop/modules/definition/new/newScriptView.jsp',
                            controller: 'NewScriptController as newScriptVm',
                            resolve: 'app/desktop/modules/definition/new/newScriptController',
                            width: 1000,
                            showMask: true,
                            data: {
                                selectedScript: vm.selectedItem
                            },
                            buttons: [
                                {text: 'Create', broadcast: 'app.definitions.newScript'}
                            ],
                            callback: function (result) {
                                vm.scriptAdded = result;
                                vm.editScriptPhython = result;
                                vm.linkActivate = false;
                                vm.linkActivate1 = true;
                                vm.clickToHideLink1 = true;
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }


                }
            }

            /* ------ Side panel to update Groovy Or Python script -----*/
            $scope.clickToOpenSidePanelToEdit = function () {
                if (vm.selectedItem != null) {
                    if (vm.selectedItem == "GROOVY") {
                        var options = {
                            title: 'Groovy Edit',
                            template: 'app/desktop/modules/definition/new/newScriptView.jsp',
                            controller: 'NewScriptController as newScriptVm',
                            resolve: 'app/desktop/modules/definition/new/newScriptController',
                            width: 1000,
                            showMask: true,
                            data: {
                                selectedScript: vm.selectedItem,
                                getScript: vm.editScript
                            },
                            buttons: [
                                {text: 'Update', broadcast: 'app.definitions.newScript'}
                            ],
                            callback: function (result) {
                                vm.scriptAdded = result;
                                vm.editScript = result;
                                vm.linkActivate = false;
                                vm.linkActivate1 = true;
                                vm.clickToHideLink1 = true;
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }
                    if (vm.selectedItem == "PYTHON") {
                        var options = {
                            title: 'Python Edit',
                            template: 'app/desktop/modules/definition/new/newScriptView.jsp',
                            controller: 'NewScriptController as newScriptVm',
                            resolve: 'app/desktop/modules/definition/new/newScriptController',
                            width: 1000,
                            showMask: true,
                            data: {
                                selectedScript: vm.selectedItem,
                                getScriptPython: vm.editScriptPhython
                            },
                            buttons: [
                                {text: 'Update', broadcast: 'app.definitions.newScript'}
                            ],
                            callback: function (result) {
                                vm.scriptAdded = result;
                                vm.editScriptPhython = result;
                                vm.linkActivate = false;
                                vm.linkActivate1 = true;
                                vm.clickToHideLink1 = true;
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }


                }
            }

            /*------ Language dropdown Select method -------*/
            $scope.onSelected = function (select) {
                vm.linkActivate = true;
                vm.linkActivate1 = false;
                vm.clickToHideLink = true;
                vm.clickToHideLink1 = false;
                vm.selectedItem = select;
            }

            /* ------ Load Input And OutPut Params ------*/
            function loadInputAndOutPutParams() {
                vm.testCaseOutputParams = [];
                vm.testCaseInputParams = [];
                TestCaseService.getInputAndOutputParamsByTestCase(vm.type).then(
                    function (data) {
                        vm.type = data;
                        vm.executionTypeValue = vm.type.executionType;
                        vm.executionType = vm.type.executionType;
                        if (vm.executionType == "PROGRAM") {
                            vm.programExecution = vm.type.programExecution;
                            vm.scriptExecution = {
                                id: null,
                                scriptLanguage: null,
                                script: null
                            };
                            vm.scriptLanguageType = null;
                        } else {
                            if (vm.type.scriptExecution != undefined) {
                                if (vm.type.scriptExecution.scriptLanguage == 'GROOVY') {
                                    vm.editScript = vm.type.scriptExecution.script;
                                    vm.linkActivate1 = true;
                                    vm.clickToHideLink = true;
                                    vm.selectedItem = vm.type.scriptExecution.scriptLanguage;
                                }
                                if (vm.type.scriptExecution.scriptLanguage == 'PYTHON') {
                                    vm.linkActivate1 = true;
                                    vm.clickToHideLink = true;
                                    vm.selectedItem = vm.type.scriptExecution.scriptLanguage;
                                    vm.editScriptPhython = vm.type.scriptExecution.script;

                                }
                            }
                            if (vm.type.scriptExecution != null) {
                                vm.scriptExecution = vm.type.scriptExecution;
                            }

                            if (vm.scriptExecution.scriptLanguage != null) {
                                vm.scriptLanguageType = vm.scriptExecution.scriptLanguage;
                            }


                            vm.programExecution = {
                                id: null,
                                program: null,
                                params: null,
                                workingDir: null
                            };
                        }

                        vm.testCaseInputParams = data.testInputParams;
                        angular.forEach(vm.testCaseInputParams, function (inputParam) {
                            inputParam.editMode = false;
                            inputParam.isNew = false;
                        });
                        vm.testCaseOutputParams = data.testOutputParams;
                        angular.forEach(vm.testCaseOutputParams, function (outputParam) {
                            outputParam.editMode = false;
                            outputParam.isNew = false;
                        })
                    }
                )
            }

            /* ----- On Definition Node selected ----*/
            function definitionTypeSelected(event, args) {
                vm.type = args.typeObject;
                vm.linkActivate = false;
                nodeId = args.nodeId;
                $rootScope.allTestCasesWithScenarios = [];
                if (vm.type != null && vm.type.id != undefined && vm.type.objectType == "TESTSCENARIO") {
                    TestTreeService.getScenario(vm.type).then(
                        function (data) {
                            vm.scenario = data;
                            vm.selecteType = vm.type.objectType;
                            vm.selectedId = vm.scenario.id;
                            $rootScope.allTestCasesWithScenarios = data;
                            loadTestCaseHistory();
                            loadObjectAttributes();
                        }
                    )
                }
                if (vm.type != null && vm.type.id != undefined && vm.type.objectType == "TESTPLAN") {
                    TestTreeService.getTestPlan(vm.type).then(
                        function (data) {
                            vm.plan = data;
                            vm.selecteType = vm.type.objectType;
                            vm.selectedId = vm.plan.id;
                            loadObjectAttributes();
                        }
                    )
                }
                if (vm.type != null && vm.type.id != undefined && vm.type.objectType == "TESTSUITE") {
                    TestTreeService.getTestSuite(vm.type).then(
                        function (data) {
                            vm.suite = data;
                            vm.selecteType = vm.type.objectType;
                            vm.selectedId = vm.suite.id;
                            loadObjectAttributes();
                        }
                    )
                }
                if (vm.type != null && vm.type != undefined) {
                    if (vm.type.id != null && vm.type.id != undefined) {
                        if (vm.type.objectType == "TESTCASE") {
                            vm.importAndExportFlag = true;
                            vm.importAndExportFlag1 = true;
                            vm.testCaseId = vm.type.id;
                            vm.linkActivate1 = true;
                            TestCaseService.getInputAndOutputParamsByTestCase(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    vm.selecteType = vm.type.objectType;
                                    vm.selectedId = vm.type.id;
                                    vm.executionTypeValue = vm.type.executionType;
                                    vm.executionType = vm.type.executionType;
                                    if (vm.executionType == "PROGRAM") {
                                        if (vm.type.programExecution != null) {
                                            vm.programExecution = vm.type.programExecution;
                                            vm.scriptExecution = {
                                                id: null,
                                                scriptLanguage: null,
                                                script: null
                                            };
                                        }
                                        vm.scriptLanguageType = null;
                                    } else {
                                        if (vm.type.scriptExecution != undefined) {
                                            if (vm.type.scriptExecution.scriptLanguage == 'GROOVY') {
                                                vm.editScript = vm.type.scriptExecution.script;
                                                vm.linkActivate1 = true;
                                                vm.clickToHideLink1 = true;
                                                vm.scriptAdded = vm.type.scriptExecution.script;
                                                vm.selectedItem = vm.type.scriptExecution.scriptLanguage;
                                            }
                                            if (vm.type.scriptExecution.scriptLanguage == 'PYTHON') {
                                                vm.linkActivate1 = true;
                                                vm.clickToHideLink1 = true;
                                                vm.scriptAdded = vm.type.scriptExecution.script;
                                                vm.selectedItem = vm.type.scriptExecution.scriptLanguage;
                                                vm.editScriptPhython = vm.type.scriptExecution.script;

                                            }
                                        }
                                        if (vm.type.scriptExecution != null) {
                                            vm.scriptExecution = vm.type.scriptExecution;
                                            vm.scriptLanguageType = vm.scriptExecution.scriptLanguage;
                                            vm.programExecution = {
                                                id: null,
                                                program: null,
                                                params: null,
                                                workingDir: null
                                            };
                                        }

                                    }

                                    vm.testCaseInputParams = data.testInputParams;
                                    angular.forEach(vm.testCaseInputParams, function (inputParam) {
                                        inputParam.editMode = false;
                                        inputParam.isNew = false;
                                    });
                                    vm.testCaseOutputParams = data.testOutputParams;
                                    angular.forEach(vm.testCaseOutputParams, function (outputParam) {
                                        outputParam.editMode = false;
                                        outputParam.isNew = false;
                                    })
                                    loadObjectAttributes();
                                }
                            )
                        }
                    } else {
                        if (vm.type.objectType == "TESTCASE") {
                            vm.executionTypeValue = null;
                            vm.executionType = null;
                            vm.testCaseOutputParams = [];
                            vm.testCaseInputParams = [];
                            vm.programExecution = {
                                id: null,
                                program: null,
                                params: null,
                                workingDir: null
                            };

                            vm.scriptExecution = {
                                id: null,
                                scriptLanguage: null,
                                script: null
                            };
                        }
                    }
                }
            }

            /* -----Save InputParam -----*/
            function saveInputParam(inputParam) {
                if (validateParams(inputParam)) {
                    inputParam.testCase = vm.type.id;
                    if (inputParam.id == null || inputParam.id == undefined) {
                        TestCaseService.createTestInputParam(inputParam).then(
                            function (data) {
                                inputParam.editMode = false;
                                inputParam.isNew = false;
                                inputParam.id = data.id;
                                $rootScope.showSuccessMessage("Input param created successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        TestCaseService.updateTestInputParam(inputParam).then(
                            function (data) {
                                inputParam.editMode = false;
                                inputParam.isNew = false;
                                inputParam.id = data.id;
                                $rootScope.showSuccessMessage("Input param updated successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            /* ------ Save OutputParam -----*/
            function saveOutputParam(outputParam) {
                if (validateParams(outputParam)) {
                    outputParam.testCase = vm.type.id;
                    if (outputParam.id == null || outputParam.id == undefined) {
                        TestCaseService.createTestOutputParam(outputParam).then(
                            function (data) {
                                outputParam.editMode = false;
                                outputParam.isNew = false;
                                outputParam.id = data.id;
                                $rootScope.showSuccessMessage("Output param created successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        TestCaseService.updateTestOutputParam(outputParam).then(
                            function (data) {
                                outputParam.editMode = false;
                                outputParam.isNew = false;
                                outputParam.id = data.id;
                                $rootScope.showSuccessMessage("Input param updated successfully");
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            /* ------ Load TestCase History -----*/
            function loadTestCaseHistory() {
                TestCaseService.getTestRunCaseHistory(vm.type.id).then(
                    function (data) {
                        if (data != null) {
                            vm.history = data;
                        }
                    }
                )
            }

            /* ------- Validation for Inpur and Output Parama -----*/
            function validateParams(param) {
                var valid = true;
                if (param.name == null || param.name == "" || param.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter Name");
                } else if (param.dataType == null || param.dataType == "" || param.dataType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Data Type");
                }

                return valid;
            }

            /*------ After edit Cancel Output Param Changes ------*/
            function onCancelOutputParam(outputParam) {
                outputParam.name = outputParam.newName;
                outputParam.description = outputParam.newDescription;
                outputParam.editMode = false;
            }

            /*------ After edit Cancel Input Param Changes ------*/
            function onCancelInputParam(inputParam) {
                inputParam.name = inputParam.newName;
                inputParam.description = inputParam.newDescription;
                inputParam.editMode = false;
            }

            /* ------ Edit Input Param -----*/
            function editInputParam(inputParam) {
                inputParam.newName = inputParam.name;
                inputParam.newDescription = inputParam.description;
                inputParam.editMode = true;
            }

            /* ------ Edit Output Param -----*/
            function editOutputParam(outputParam) {
                outputParam.newName = outputParam.name;
                outputParam.newDescription = outputParam.description;
                outputParam.editMode = true;
            }

            /* ------ Delete Input Param -----*/
            function deleteInputParam(inputParam) {
                if (inputParam.id == null || inputParam.id == undefined) {
                    var index = vm.testCaseInputParams.indexOf(inputParam);
                    vm.testCaseInputParams.splice(index, 1);
                } else {
                    var options = {
                        title: 'Delete Input param',
                        message: 'Please confirm to delete this Input param.',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            TestCaseService.deleteTestInputParam(inputParam).then(
                                function (data) {
                                    var index = vm.testCaseInputParams.indexOf(inputParam);
                                    vm.testCaseInputParams.splice(index, 1);
                                    $rootScope.showSuccessMessage("Input param deleted successfully");
                                }
                            )
                        }
                    });
                }
            }

            /* ------ Delete Output Param -----*/
            function deleteOutputParam(outputParam) {
                if (outputParam.id == null || outputParam.id == undefined) {
                    var index = vm.testCaseOutputParams.indexOf(outputParam);
                    vm.testCaseOutputParams.splice(index, 1);
                } else {
                    var options = {
                        title: 'Delete Output param',
                        message: 'Please confirm to delete this Output param.',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            TestCaseService.deleteTestOutputParam(outputParam).then(
                                function (data) {
                                    var index = vm.testCaseOutputParams.indexOf(outputParam);
                                    vm.testCaseOutputParams.splice(index, 1);
                                    $rootScope.showSuccessMessage("Output param deleted successfully");
                                }
                            )
                        }
                    });
                }
            }

            /* ------ Method to call Select only Script Radio Button ----*/
            function selectScript() {
                vm.executionType = "SCRIPT";
                if (vm.type != null && vm.type.objectType == "TESTCASE") {
                    if (vm.type.id != null && vm.type.scriptExecution != null) {
                        if (vm.type.scriptExecution.scriptLanguage != null) {
                            vm.linkActivate1 = true;
                            vm.clickToHideLink1 = true;
                            vm.clickToHideLink = true;
                        }

                    }
                    else {
                        vm.scriptAdded = null;
                        vm.clickToHideLink = false;
                        vm.clickToHideLink1 = false;

                    }

                }

            }

            /* ------- Method to call only on select Program Radio Button -----*/
            function selectProgram() {
                vm.executionType = "PROGRAM";
                if (vm.type.id != null && vm.type.programExecution != null) {
                    vm.programExecution = vm.type.programExecution;
                }
                else {
                    vm.programExecution = {
                        id: null,
                        program: null,
                        params: null,
                        workingDir: null
                    };
                }


            }

            /*------ Add New Input Param ------*/
            function addNewInputParam() {
                var newInputParam = angular.copy(inputParam);
                vm.testCaseInputParams.push(newInputParam)
            }

            /* -------Add new Output Param ------*/
            function addNewOutputParam() {
                var newOutputParam = angular.copy(outputParam);
                vm.testCaseOutputParams.push(newOutputParam)
            }

            /*  ------- Save Scenario,Pplan,Suite and TestCase ------*/
            function onSave() {
                if (validate()) {
                    if (vm.type != null && vm.type.objectType == "TESTSCENARIO") {
                        if (vm.type.id == null || vm.type == undefined) {
                            TestTreeService.createScenario(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    vm.scenario = vm.type;
                                    $rootScope.showSuccessMessage("Scenario created successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                    loadObjectAttributes();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            TestTreeService.updateScenario(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    $rootScope.showSuccessMessage("Scenario updated successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }

                    } else if (vm.type != null && vm.type.objectType == "TESTPLAN") {
                        if (vm.type.id == null || vm.type == undefined) {
                            vm.type.scenario = vm.type.parent;
                            TestTreeService.createTestPlan(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    vm.plan = vm.type;
                                    $rootScope.showSuccessMessage("Plan created successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            TestTreeService.updateTestPlan(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    $rootScope.showSuccessMessage("Plan updated successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }

                    } else if (vm.type != null && vm.type.objectType == "TESTSUITE") {
                        if (vm.type.id == null || vm.type == undefined) {
                            vm.type.plan = vm.type.parent;
                            TestTreeService.createTestSuite(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    $rootScope.showSuccessMessage("Suite created successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            TestTreeService.updateTestSuite(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    $rootScope.showSuccessMessage("Suite updated successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }

                    } else if (vm.type != null && vm.type.objectType == "TESTCASE") {
                        if (vm.type.id == null || vm.type == undefined) {
                            vm.scriptExecution.script = vm.scriptAdded;
                            vm.type.suite = vm.type.parent;
                            vm.type.executionType = vm.executionType;
                            vm.type.programExecution = vm.programExecution;
                            vm.type.scriptExecution = vm.scriptExecution;
                            vm.type.scriptExecution.scriptLanguage = vm.selectedItem;
                            TestTreeService.createTestCase(vm.type).then(
                                function (data) {
                                    vm.type = data;
                                    $rootScope.showSuccessMessage("Test case created successfully");
                                    $rootScope.$broadcast("app.definition.update", {
                                        nodeId: nodeId,
                                        nodeName: vm.type
                                    })
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            if (vm.executionTypeValue == vm.executionType) {
                                if (vm.executionType == "SCRIPT") {
                                    if (vm.scriptExecution.scriptLanguage == vm.scriptLanguageType) {
                                        updateTestCase();
                                    } else {
                                        var options = {
                                            title: "Warning",
                                            message: "Please confirm to change Script Language " + vm.scriptLanguageType + " to " + vm.scriptExecution.scriptLanguage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                updateTestCase();
                                            } else {
                                                if (vm.scriptLanguageType == "GROOVY") {
                                                    vm.linkActivate1 = true;
                                                    vm.selectedItem = "GROOVY";
                                                    vm.editScript = vm.scriptExecution.script;
                                                } else {
                                                    vm.linkActivate1 = true;
                                                    vm.selectedItem = "PYTHON";
                                                    vm.editScriptPhython = vm.scriptExecution.script;
                                                }

                                                vm.scriptExecution.scriptLanguage = vm.scriptLanguageType;
                                            }
                                        });
                                    }

                                } else {
                                    updateTestCase();
                                }

                            } else {
                                var options = {
                                    title: "Warning",
                                    message: "Please confirm to change Execution Type " + vm.executionType + " to " + vm.executionTypeValue,
                                    okButtonClass: 'btn-danger'
                                };

                                DialogService.confirm(options, function (yes) {
                                    if (yes == true) {
                                        vm.type.executionType = vm.executionTypeValue;
                                        vm.type.programExecution = vm.programExecution;
                                        vm.type.scriptExecution = vm.scriptExecution;
                                        updateTestCase();
                                    }
                                });
                            }
                        }

                    }
                }
            }

            /* ------- Update TestCase ------*/
            function updateTestCase() {
                vm.type.executionType = vm.executionType;
                vm.type.programExecution = vm.programExecution;
                vm.type.scriptExecution = vm.scriptExecution;
                vm.scriptExecution.script = vm.scriptAdded;
                vm.scriptExecution = vm.type.scriptExecution;
                vm.type.scriptExecution.scriptLanguage = vm.selectedItem;
                vm.scriptLanguageType = vm.type.scriptExecution.scriptLanguage;
                TestTreeService.updateTestCase(vm.type).then(
                    function (data) {
                        vm.type = data;
                        vm.executionTypeValue = vm.type.executionType;
                        vm.executionType = vm.type.executionType;
                        if (vm.executionType == "PROGRAM") {
                            loadInputAndOutPutParams();
                            vm.programExecution = vm.type.programExecution;
                            vm.programExecution = {
                                id: null,
                                scriptLanguage: null,
                                script: null
                            };
                        } else {
                            loadInputAndOutPutParams();
                            vm.scriptExecution = {
                                id: null,
                                program: null,
                                params: null,
                                workingDir: null
                            };
                        }
                        $rootScope.showSuccessMessage("Test case updated successfully");
                        $rootScope.$broadcast("app.definition.update", {
                            nodeId: nodeId,
                            nodeName: vm.type
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /* ------ Validation For TestCase ------*/
            function validate() {
                var valid = true;
                vm.type.programExecution = vm.programExecution;
                if (vm.type != null && vm.type.objectType != "TESTCASE") {
                    if (vm.type.name == null || vm.type.name == "" || vm.type.name == undefined) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter name");
                    }
                } else if (vm.type != null && vm.type.objectType == "TESTCASE") {
                    if (vm.type.name == null || vm.type.name == "" || vm.type.name == undefined) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter name");
                    } else if (vm.executionTypeValue == null || vm.executionTypeValue == "" || vm.executionTypeValue == undefined) {
                        valid = false;
                        $rootScope.showWarningMessage("Please select execution type");
                    } else if (vm.executionTypeValue == "PROGRAM" && (vm.programExecution.program == null || vm.programExecution.program == "")) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter program");
                    } else if (vm.executionTypeValue == "SCRIPT" && (vm.type.scriptExecution == null || vm.type.scriptExecution == "")) {
                        valid = false;
                        $rootScope.showWarningMessage("Please select script language");
                    } else if (vm.executionTypeValue == "SCRIPT" && (vm.scriptAdded == null || vm.scriptAdded == "")) {
                        valid = false;
                        $rootScope.showWarningMessage("Please enter script");
                    }
                }

                return valid;
            }

            /*-------- Import InputParams -------*/
            function importInputParams() {
                var file = document.getElementById("file");
                if (vm.importFile = file.files[0]) {
                    $rootScope.showBusyIndicator();
                    TestCaseService.importInputParams(vm.importFile, vm.testCaseId).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            loadInputAndOutPutParams();
                            document.getElementById("file").value = "";
                            $rootScope.showSuccessMessage("Imported successfully");


                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            document.getElementById("file").value = "";
                        }
                    )
                }

            }

            /*-------- Import OutPutParams ------*/
            function importOutPutParams() {
                var file1 = document.getElementById("file1");
                if (vm.importFile1 = file1.files[0]) {
                    $rootScope.showBusyIndicator();
                    TestCaseService.importOutputParams(vm.importFile1, vm.testCaseId).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            loadInputAndOutPutParams();
                            document.getElementById("file1").value = "";
                            $rootScope.showSuccessMessage("Imported successfully");


                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            document.getElementById("file1").value = "";
                        }
                    )
                }

            }

            /* --------Load Scenario Attributes ------*/
            function loadObjectAttributes() {
                vm.objectAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType(vm.selecteType).then(
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
                                    booleanValue: null,
                                    dateValue: null,
                                    imageValue: null,
                                    attachmentValues: []
                                },
                                editMode: true,
                                isNew: true,
                                imageValue: null,
                                newImageValue: null,
                                changeImage: false
                            };
                            vm.objectAttributes.push(att);
                        });

                        loadObjectProperties();
                    });
            }

            /* ------ Validation For Attributes ------*/
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

            /* ------- Save Scenario properties ------*/
            function saveScenarioProperties(attribute) {
                if (attribute.attributeDef.objectType == 'TESTSCENARIO') {
                    if (validateAttributes(attribute)) {
                        if (attribute.value.timestampValue != null) {
                            attribute.value.timestampValue = moment(attribute.value.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    attribute.editMode = false;
                                    $rootScope.showSuccessMessage("Scenario attribute saved successfully");
                                    loadObjectProperties();
                                }
                            )
                        } else {
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Scenario attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
                if (attribute.attributeDef.objectType == 'TESTPLAN') {
                    if (validateAttributes(attribute)) {
                        if (attribute.value.timestampValue != null) {
                            attribute.value.timestampValue = moment(attribute.value.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Plan attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Plan attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
                if (attribute.attributeDef.objectType == 'TESTSUITE') {
                    if (validateAttributes(attribute)) {
                        if (attribute.value.timestampValue != null) {
                            attribute.value.timestampValue = moment(attribute.value.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Suite attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Suite attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
                if (attribute.attributeDef.objectType == 'TESTCASE') {
                    if (validateAttributes(attribute)) {
                        if (attribute.value.timestampValue != null) {
                            attribute.value.timestampValue = moment(attribute.value.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Test case attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            ObjectAttributeService.createObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    $rootScope.showSuccessMessage("Test case attribute saved successfully");
                                    attribute.editMode = false;
                                    loadObjectProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                }
            }

            /* ----- Load ObjectAttributeProperties -----*/
            function loadObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(vm.selectedId).then(
                    function (data) {
                        var map = new Hashtable();

                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });

                        angular.forEach(vm.objectAttributes, function (attribute) {
                            var attachmentIds = [];
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                attribute.editMode = false;
                                if (value.attachmentValues.length > 0) {
                                    angular.forEach(value.attachmentValues, function (attachment) {
                                        attachmentIds.push(attachment);
                                    });
                                    AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                        function (data) {
                                            vm.itemPropertyAttachments = data;
                                            attribute.value.attachmentValues = vm.itemPropertyAttachments;
                                        }
                                    )
                                }
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.doubleValue = value.doubleValue;
                                attribute.value.dateValue = value.dateValue;
                                attribute.value.booleanValue = value.booleanValue;
                                attribute.value.timeValue = value.timeValue;
                                attribute.value.timestampValue = value.timestampValue;
                                attribute.value.imageValue = value.imageValue;
                                attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                            }
                        });
                    }
                )
            }

            /* ------ Edit Scenario properties ------*/
            function editScenarioProperties(attribute) {
                attribute.stringValue = attribute.value.stringValue;
                attribute.integerValue = attribute.value.integerValue;
                attribute.dateValue = attribute.value.dateValue;
                attribute.doubleValue = attribute.value.doubleValue;
                attribute.booleanValue = attribute.value.booleanValue;
                attribute.timeValue = attribute.value.timeValue;
                attribute.timestampValue = attribute.value.timestampValue;
                attribute.editMode = true;
                attribute.isNew = false;

            }

            /* ------ After Edit Cancel changes ------*/
            function cancelChanges(attribute) {
                attribute.value.stringValue = attribute.stringValue;
                attribute.value.integerValue = attribute.integerValue;
                attribute.value.dateValue = attribute.dateValue;
                attribute.value.doubleValue = attribute.doubleValue;
                attribute.value.booleanValue = attribute.booleanValue;
                attribute.value.timeValue = attribute.timeValue;
                attribute.value.timestampValue = attribute.timestampValue;
                attribute.editMode = false;
            }

            function deleteAttribute(attribute) {
                var options = {
                    title: 'Delete Attribute value',
                    message: 'Please confirm to delete this attribute value.',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ObjectAttributeService.deleteObjectAttribute(attribute.id.objectId, attribute).then(
                            function (data) {
                                loadObjectProperties();
                                $rootScope.showSuccessMessage("attribute value deleted successfully");
                            }
                        )
                    }
                });

            }

            vm.change = change;
            function change(attribute) {
                attribute.changeImage = true;
                var attributeImageFile = document.getElementById("attributeImageFile");
                if (attributeImageFile != null && attributeImageFile != undefined) {
                    document.getElementById("attributeImageFile").value = "";
                }
                var attributeRevisionImageFile = document.getElementById("attributeRevisionImageFile");
                if (attributeRevisionImageFile != null && attributeRevisionImageFile != undefined) {
                    document.getElementById("attributeRevisionImageFile").value = "";
                }
            }


            function validateImageAttributes(attribute) {
                var valid = true;
                if (attribute.newImageValue != null) {
                    var fup = document.getElementById('attributeImageFile1');
                    var fileName = fup.value;
                    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                        return true;
                    } else {
                        $rootScope.showWarningMessage("Upload Images only");
                        fup.focus();
                        valid = false;
                    }
                    return valid;
                }
            }


            /*--- To save Image property ---*/
            vm.saveImage = saveImage;
            function saveImage(attribute) {
                if (validateImageAttributes(attribute)) {
                    if (attribute.newImageValue != null) {
                        $rootScope.showBusyIndicator($(".view-content"));
                        attribute.imageValue = attribute.newImageValue;
                        if (attribute.attributeDef.objectType == 'TESTSCENARIO' || attribute.attributeDef.objectType == 'TESTPLAN' || attribute.attributeDef.objectType == 'TESTSUITE' || attribute.attributeDef.objectType == 'TESTCASE') {
                            ObjectAttributeService.updateObjectAttribute(vm.selectedId, attribute.value).then(
                                function (data) {
                                    ObjectAttributeService.uploadObjectAttributeImage(attribute.id.objectId, attribute.id.attributeDef, attribute.imageValue).then(
                                        function (data) {
                                            attribute.changeImage = false;
                                            attribute.newImageValue = null;
                                            attribute.value.itemImagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                            $rootScope.showSuccessMessage("image added successfully");
                                            loadObjectProperties();
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }


            /*---- To show Large Image of item image property -----*/
            vm.showImageProperty = showImageProperty;
            function showImageProperty(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById("img03");

                modal.style.display = "block";
                modalImg.src = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeimage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.editScenarioImageProperties = editScenarioImageProperties;

            function editScenarioImageProperties(attribute) {
                attribute.changeImage = true;
                attribute.editMode = true;
                attribute.isNew = false;
                var attributeImageFile = document.getElementById("attributeImageFile");
                if (attributeImageFile != null && attributeImageFile != undefined) {
                    document.getElementById("attributeImageFile").value = "";
                }
            }

            vm.cancelImageChanges = cancelImageChanges;
            function cancelImageChanges(attribute) {
                attribute.editMode = false;
                attribute.changeImage = false;


            }


            (function () {
                $scope.$on('app.definition.details', definitionTypeSelected);
                $scope.$on('app.definition.save', onSave);

            })();

        }

    });