define(
    [
        'app/desktop/modules/config/config.module',
        'app/shared/services/testTreeService',
        'app/shared/services/testCaseService',
        'app/shared/services/runConfigurationService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ConfigurationDetailsController', ConfigurationDetailsController);

        function ConfigurationDetailsController($scope, $rootScope, $timeout, $state, CommonService, DialogService, $cookies, TestTreeService, TestCaseService, ObjectTypeAttributeService, ObjectAttributeService,
                                                RunConfigurationService) {

            var vm = this;
            if ($application.homeLoaded == false) {
                return;
            }

            var nodeId = null;
            vm.type = null;
            vm.testCaseInputParamValues = [];
            vm.testCaseOutputExpectedParamValues = [];
            vm.saveInputParamValue = saveInputParamValue;
            vm.editInputParamValue = editInputParamValue;
            vm.deleteInputParamValue = deleteInputParamValue;
            vm.saveOutputParamValue = saveOutputParamValue;
            vm.editOutputParamValue = editOutputParamValue;
            vm.deleteOutputParamValue = deleteOutputParamValue;
            vm.saveRunConfigProperties = saveRunConfigProperties;
            vm.editRunConfigProperties = editRunConfigProperties;
            vm.onCancelOutputParam = onCancelOutputParam;
            vm.onCancelInputParamValue = onCancelInputParamValue;
            vm.cancelChanges = cancelChanges;
            vm.saveRunSchedule = saveRunSchedule;
            $scope.importInputParamValues = importInputParamValues;
            $scope.importOutPutParamValues = importInputParamValues;
            $rootScope.exportOutParams = exportOutParams;
            $rootScope.exportinputParams = exportinputParams;
            vm.importAndExportFlag = false;
            vm.importAndExportFlag1 = false;

            vm.runSchedule = {
                id: null,
                runConfig: null,
                sunday: null,
                monday: null,
                tuesday: null,
                wednesday: null,
                thursday: null,
                friday: null,
                saturday: null
            };

            /* --------- Method to select on Run Configuration -----------*/
            function runConfigurationSelected(event, args) {
                vm.testCaseInputParamValues = [];
                vm.testCaseOutputExpectedParamValues = [];
                vm.type = args.typeObject;
                vm.runConfig = vm.type;
                nodeId = args.nodeId;
                if (vm.type != null && vm.type != undefined) {
                    if (vm.type.id != null && vm.type.id != undefined) {
                        if (vm.type.objectType == "TESTCASE") {
                            vm.importAndExportFlag = true;
                            vm.importAndExportFlag1 = true;
                            vm.testCaseId = vm.type.id;
                            vm.runConfigId = vm.type.runConfiguration;
                            loadTestCaseInputAndOutputParams();
                        }

                        if (vm.type.objectType == "TESTRUNCONFIGURATION") {
                            RunConfigurationService.getRunScheduleByRunConfig(vm.type.id).then(
                                function (data) {
                                    if (data != "") {
                                        vm.runSchedule = data;
                                    } else {
                                        vm.runSchedule = {
                                            id: null,
                                            runConfig: null,
                                            sunday: null,
                                            monday: null,
                                            tuesday: null,
                                            wednesday: null,
                                            thursday: null,
                                            friday: null,
                                            saturday: null
                                        };

                                    }
                                }
                            )
                        }

                    }
                    loadTestCaseHistory();
                    loadRunConfigAttributes();
                }
                $scope.$apply();
            }


            /* --------- Load testCase input and outputParams ------------*/
            function loadTestCaseInputAndOutputParams() {
                vm.testCaseInputParams = [];
                vm.testCaseOutputExpectedParams = [];
                vm.testCaseInputParamValues = [];
                vm.testCaseOutputExpectedParamValues = [];
                TestCaseService.getInputOutputValuesByConfigAndTestCase(vm.type).then(
                    function (data) {
                        vm.testCaseInputParams = data.testInputParams;
                        vm.testCaseOutputExpectedParams = data.testOutputParams;
                        angular.forEach(vm.testCaseInputParams, function (inputParam) {
                            var testCaseInputParamValue = {
                                id: null,
                                config: vm.type.runConfiguration,
                                testCase: vm.type.id,
                                inputParam: inputParam.id,
                                inputParamData: inputParam,
                                stringValue: null,
                                integerValue: null,
                                doubleValue: null,
                                dateValue: null,
                                timeValue: null,
                                timestampValue: null,
                                booleanValue: null,
                                editMode: true,
                                isNew: true
                            };
                            if (inputParam.rcInputParamValue != null) {
                                testCaseInputParamValue.id = inputParam.rcInputParamValue.id;
                                if (inputParam.dataType == "STRING") {
                                    testCaseInputParamValue.stringValue = inputParam.rcInputParamValue.stringValue;
                                    testCaseInputParamValue.editMode = false;
                                    testCaseInputParamValue.isNew = false;
                                } else if (inputParam.dataType == "INTEGER") {
                                    testCaseInputParamValue.integerValue = inputParam.rcInputParamValue.integerValue;
                                    testCaseInputParamValue.editMode = false;
                                    testCaseInputParamValue.isNew = false;
                                } else if (inputParam.dataType == "DOUBLE") {
                                    testCaseInputParamValue.doubleValue = inputParam.rcInputParamValue.doubleValue;
                                    testCaseInputParamValue.editMode = false;
                                    testCaseInputParamValue.isNew = false;
                                } else if (inputParam.dataType == "DATE") {
                                    testCaseInputParamValue.dateValue = inputParam.rcInputParamValue.dateValue;
                                    testCaseInputParamValue.editMode = false;
                                    testCaseInputParamValue.isNew = false;
                                } else if (inputParam.dataType == "TIME") {
                                    testCaseInputParamValue.timeValue = inputParam.rcInputParamValue.timeValue;
                                    testCaseInputParamValue.editMode = false;
                                    testCaseInputParamValue.isNew = false;
                                } else if (inputParam.dataType == "TIMESTAMP") {
                                    testCaseInputParamValue.timestampValue = inputParam.rcInputParamValue.timestampValue;
                                    testCaseInputParamValue.editMode = false;
                                    testCaseInputParamValue.isNew = false;
                                }
                            }
                            vm.testCaseInputParamValues.push(testCaseInputParamValue);
                        })

                        angular.forEach(vm.testCaseOutputExpectedParams, function (outputParam) {
                            var testCaseOutputParamValue = {
                                id: null,
                                config: vm.type.runConfiguration,
                                testCase: vm.type.id,
                                outputParam: outputParam.id,
                                outputParamData: outputParam,
                                stringValue: null,
                                integerValue: null,
                                doubleValue: null,
                                dateValue: null,
                                timeValue: null,
                                timestampValue: null,
                                booleanValue: null,
                                editMode: true,
                                isNew: true
                            };
                            if (outputParam.outPutParamExpectedValue != null) {
                                testCaseOutputParamValue.id = outputParam.outPutParamExpectedValue.id;
                                if (outputParam.dataType == "STRING") {
                                    testCaseOutputParamValue.stringValue = outputParam.outPutParamExpectedValue.stringValue;
                                    testCaseOutputParamValue.editMode = false;
                                    testCaseOutputParamValue.isNew = false;
                                } else if (outputParam.dataType == "INTEGER") {
                                    testCaseOutputParamValue.integerValue = outputParam.outPutParamExpectedValue.integerValue;
                                    testCaseOutputParamValue.editMode = false;
                                    testCaseOutputParamValue.isNew = false;
                                } else if (outputParam.dataType == "DOUBLE") {
                                    testCaseOutputParamValue.doubleValue = outputParam.outPutParamExpectedValue.doubleValue;
                                    testCaseOutputParamValue.editMode = false;
                                    testCaseOutputParamValue.isNew = false;
                                } else if (outputParam.dataType == "DATE") {
                                    testCaseOutputParamValue.dateValue = outputParam.outPutParamExpectedValue.dateValue;
                                    testCaseOutputParamValue.editMode = false;
                                    testCaseOutputParamValue.isNew = false;
                                } else if (outputParam.dataType == "TIME") {
                                    testCaseOutputParamValue.timeValue = outputParam.outPutParamExpectedValue.timeValue;
                                    testCaseOutputParamValue.editMode = false;
                                    testCaseOutputParamValue.isNew = false;
                                } else if (outputParam.dataType == "TIMESTAMP") {
                                    testCaseOutputParamValue.timestampValue = outputParam.outPutParamExpectedValue.timestampValue;
                                    testCaseOutputParamValue.editMode = false;
                                    testCaseOutputParamValue.isNew = false;
                                }
                            }
                            vm.testCaseOutputExpectedParamValues.push(testCaseOutputParamValue);
                        })
                    }
                )
            }

            /* ------ Save and update Run Configuration ------*/
            function onSave() {
                if (vm.type.objectType == "TESTRUNCONFIGURATION") {
                    if (vm.type.id == null || vm.type == undefined) {
                        TestTreeService.createRunConfiguration(vm.type).then(
                            function (data) {
                                vm.type = data;
                                $rootScope.showSuccessMessage("Run configuration created successfully");
                                $rootScope.$broadcast("app.runConfiguration.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.type
                                })
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        RunConfigurationService.updateRunConfiguration(vm.type).then(
                            function (data) {
                                vm.type = data;
                                $rootScope.showSuccessMessage("Run configuration updated successfully");
                                $rootScope.$broadcast("app.runConfiguration.update", {
                                    nodeId: nodeId,
                                    nodeName: vm.type
                                })
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                }
            }


            /* ------ Save inputParam Values ------*/
            function saveInputParamValue(inputParamValue) {
                if (validateInputParamValues(inputParamValue)) {
                    if (inputParamValue.id == null || inputParamValue.id == undefined) {
                        TestCaseService.createRCInputParamValue(inputParamValue).then(
                            function (data) {
                                inputParamValue.editMode = false;
                                inputParamValue.id = data.id;
                                $rootScope.showSuccessMessage("Input param value save successfully");
                                loadTestCaseInputAndOutputParams();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        TestCaseService.updateRCInputParamValue(inputParamValue).then(
                            function (data) {
                                inputParamValue.editMode = false;
                                inputParamValue.id = data.id;
                                $rootScope.showSuccessMessage("Input param value save successfully");
                                loadTestCaseInputAndOutputParams();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }

            }

            /*------- Load testCase history ------*/
            vm.history = null;
            function loadTestCaseHistory() {
                RunConfigurationService.getTestCaseHistory(vm.type.id).then(
                    function (data) {
                        vm.history = data;
                    }
                )
            }

            /*------- Cancel outputParam after edit -------*/
            function onCancelOutputParam(outputParamValue) {
                outputParamValue.stringValue = outputParamValue.oldStringValue;
                outputParamValue.integerValue = outputParamValue.oldIntegerValue;
                outputParamValue.doubleValue = outputParamValue.oldDoubleValue;
                outputParamValue.dateValue = outputParamValue.oldDateValue;
                outputParamValue.timeValue = outputParamValue.oldTimeValue;
                outputParamValue.timestampValue = outputParamValue.oldTimestampValue;
                outputParamValue.editMode = false;
            }

            /*-------  Cancel inputParam after edit ------*/
            function onCancelInputParamValue(inputParamValue) {
                inputParamValue.stringValue = inputParamValue.oldStringValue;
                inputParamValue.integerValue = inputParamValue.oldIntegerValue;
                inputParamValue.doubleValue = inputParamValue.oldDoubleValue;
                inputParamValue.dateValue = inputParamValue.oldDateValue;
                inputParamValue.timeValue = inputParamValue.oldTimeValue;
                inputParamValue.timestampValue = inputParamValue.oldTimestampValue;
                inputParamValue.editMode = false;
            }

            /* ------- Edit inputParamValue --------*/
            function editInputParamValue(inputParamValue) {
                inputParamValue.editMode = true;
                inputParamValue.isNew = false;
                inputParamValue.oldStringValue = inputParamValue.stringValue;
                inputParamValue.oldIntegerValue = inputParamValue.integerValue;
                inputParamValue.oldDoubleValue = inputParamValue.doubleValue;
                inputParamValue.oldDateValue = inputParamValue.dateValue;
                inputParamValue.oldTimeValue = inputParamValue.timeValue;
                inputParamValue.oldTimestampValue = inputParamValue.timestampValue;
            }

            /* ------  Delete InputParam Value  -------*/
            function deleteInputParamValue(inputParamValue) {
                var options = {
                    title: 'Delete Input param value',
                    message: 'Please confirm to delete this Input param value.',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TestCaseService.deleteRCInputParamValue(inputParamValue).then(
                            function (data) {
                                inputParamValue.id = null;
                                inputParamValue.stringValue = null;
                                inputParamValue.integerValue = null;
                                inputParamValue.doubleValue = null;
                                inputParamValue.dateValue = null;
                                inputParamValue.timeValue = null;
                                inputParamValue.timestampValue = null;
                                inputParamValue.editMode = true;
                                inputParamValue.isNew = true;
                                $rootScope.showSuccessMessage("Value deleted successfully");
                            }
                        )
                    }
                });
            }

            function cancelChanges(paramValue) {
                paramValue.stringValue = paramValue.oldStringValue;
                paramValue.integerValue = paramValue.oldIntegerValue;
                paramValue.doubleValue = paramValue.oldDoubleValue;
                paramValue.dateValue = paramValue.oldDateValue;
                paramValue.timeValue = paramValue.oldTimeValue;
                paramValue.timestampValue = paramValue.oldTimestampValue;
                paramValue.editMode = false;
            }

            /* ------- Save OutputParamValue -------*/
            function saveOutputParamValue(expectedOutputValue) {
                if (validateExpectedOutputValues(expectedOutputValue)) {
                    if (expectedOutputValue.id == null || expectedOutputValue.id == undefined) {
                        TestCaseService.createExpectedOutputValue(expectedOutputValue).then(
                            function (data) {
                                expectedOutputValue.editMode = false;
                                expectedOutputValue.id = data.id;
                                $rootScope.showSuccessMessage("Expected Output value saved successfully");
                                loadTestCaseInputAndOutputParams();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        TestCaseService.updateExpectedOutputValue(expectedOutputValue).then(
                            function (data) {
                                expectedOutputValue.editMode = false;
                                expectedOutputValue.id = data.id;
                                $rootScope.showSuccessMessage("Expected Output value saved successfully");
                                loadTestCaseInputAndOutputParams();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            /*------- Edit outputParam Value -------*/
            function editOutputParamValue(expectedOutputValue) {
                expectedOutputValue.editMode = true;
                expectedOutputValue.isNew = false;
                expectedOutputValue.oldStringValue = expectedOutputValue.stringValue;
                expectedOutputValue.oldIntegerValue = expectedOutputValue.integerValue;
                expectedOutputValue.oldDoubleValue = expectedOutputValue.doubleValue;
                expectedOutputValue.oldDateValue = expectedOutputValue.dateValue;
                expectedOutputValue.oldTimeValue = expectedOutputValue.timeValue;
                expectedOutputValue.oldTimestampValue = expectedOutputValue.timestampValue;
            }

            /* -------Delete OutPutParam Value -------*/
            function deleteOutputParamValue(expectedOutputValue) {
                var options = {
                    title: 'Delete output param value',
                    message: 'Please confirm to delete this expected output param value.',
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        TestCaseService.deleteExpectedOutputValue(expectedOutputValue).then(
                            function (data) {
                                expectedOutputValue.id = null;
                                expectedOutputValue.stringValue = null;
                                expectedOutputValue.integerValue = null;
                                expectedOutputValue.doubleValue = null;
                                expectedOutputValue.dateValue = null;
                                expectedOutputValue.timeValue = null;
                                expectedOutputValue.timestampValue = null;
                                expectedOutputValue.editMode = true;
                                expectedOutputValue.isNew = true;
                                $rootScope.showSuccessMessage("Value deleted successfully");
                            }
                        )
                    }
                });
            }


            /* ------ Validations for inputParam vaues ------*/
            function validateInputParamValues(inputParamValue) {
                var valid = true;
                if (inputParamValue.inputParamData.dataType == "STRING" && (inputParamValue.stringValue == null || inputParamValue.stringValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + inputParamValue.inputParamData.name);
                } else if (inputParamValue.inputParamData.dataType == "INTEGER" && (inputParamValue.integerValue == null || inputParamValue.integerValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + inputParamValue.inputParamData.name);
                } else if (inputParamValue.inputParamData.dataType == "DOUBLE" && (inputParamValue.doubleValue == null || inputParamValue.doubleValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + inputParamValue.inputParamData.name);
                } else if (inputParamValue.inputParamData.dataType == "DATE" && (inputParamValue.dateValue == null || inputParamValue.dateValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select date for " + inputParamValue.inputParamData.name);
                } else if (inputParamValue.inputParamData.dataType == "TIME" && (inputParamValue.timeValue == null || inputParamValue.timeValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + inputParamValue.inputParamData.name);
                } else if (inputParamValue.inputParamData.dataType == "TIMESTAMP" && (inputParamValue.timestampValue == null || inputParamValue.timestampValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + inputParamValue.inputParamData.name);
                } else if (inputParamValue.inputParamData.dataType == "BOOLEAN" && (inputParamValue.booleanValue == null || inputParamValue.booleanValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select value for " + inputParamValue.inputParamData.name);
                }

                return valid;
            }

            /*------ Validations for Expected outputParam values ------*/
            function validateExpectedOutputValues(expectedOutputValue) {
                var valid = true;
                if (expectedOutputValue.outputParamData.dataType == "STRING" && (expectedOutputValue.stringValue == null || expectedOutputValue.stringValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + expectedOutputValue.outputParamData.name);
                } else if (expectedOutputValue.outputParamData.dataType == "INTEGER" && (expectedOutputValue.integerValue == null || expectedOutputValue.integerValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + expectedOutputValue.outputParamData.name);
                } else if (expectedOutputValue.outputParamData.dataType == "DOUBLE" && (expectedOutputValue.doubleValue == null || expectedOutputValue.doubleValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + expectedOutputValue.outputParamData.name);
                } else if (expectedOutputValue.outputParamData.dataType == "DATE" && (expectedOutputValue.dateValue == null || expectedOutputValue.dateValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select date for " + expectedOutputValue.outputParamData.name);
                } else if (expectedOutputValue.outputParamData.dataType == "TIME" && (expectedOutputValue.timeValue == null || expectedOutputValue.timeValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + expectedOutputValue.outputParamData.name);
                } else if (expectedOutputValue.outputParamData.dataType == "TIMESTAMP" && (expectedOutputValue.timestampValue == null || expectedOutputValue.timestampValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please enter value for " + expectedOutputValue.outputParamData.name);
                } else if (expectedOutputValue.outputParamData.dataType == "BOOLEAN" && (expectedOutputValue.booleanValue == null || expectedOutputValue.booleanValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select value for " + expectedOutputValue.outputParamData.name);
                }

                return valid;
            }

            /* ------- Save Run schedue on Run Configuration ------*/
            function saveRunSchedule() {
                vm.runSchedule.runConfig = vm.type;
                if (vm.runSchedule.id == null || vm.runSchedule.id == undefined) {
                    RunConfigurationService.createTestRunSchedule(vm.runSchedule).then(
                        function (data) {
                            vm.runSchedule = data;
                            $rootScope.showSuccessMessage("Run schedule saved successfully");
                        }
                    )
                } else {
                    if (vm.runSchedule.sunday == "") {
                        vm.runSchedule.sunday = null;
                    }
                    if (vm.runSchedule.monday == "") {
                        vm.runSchedule.monday = null;
                    }
                    if (vm.runSchedule.tuesday == "") {
                        vm.runSchedule.tuesday = null;
                    }
                    if (vm.runSchedule.wednesday == "") {
                        vm.runSchedule.wednesday = null;
                    }
                    if (vm.runSchedule.thursday == null) {
                        vm.runSchedule.thursday = null;
                    }
                    if (vm.runSchedule.friday == "") {
                        vm.runSchedule.friday = null;
                    }
                    if (vm.runSchedule.saturday == "") {
                        vm.runSchedule.saturday = null;
                    }


                    RunConfigurationService.updateTestRunSchedule(vm.runSchedule).then(
                        function (data) {
                            vm.runSchedule = data;
                            $rootScope.showSuccessMessage("Run schedule saved successfully");
                        }
                    )
                }
            }

            /* ------ Import input param values from excel -----*/
            function importInputParamValues() {
                var file = document.getElementById("file");
                if (vm.importFile = file.files[0]) {
                    $rootScope.showBusyIndicator();
                    TestCaseService.importInputParamsValues(vm.importFile, vm.testCaseId, vm.runConfigId).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            loadTestCaseInputAndOutputParams();
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

            /* ------ Import outputParam values from excel ------*/
            $scope.importOutPutParamValues = function () {
                var file1 = document.getElementById("file1");
                if (vm.importFile1 = file1.files[0]) {
                    $rootScope.showBusyIndicator();
                    TestCaseService.importOutPutParamsValues(vm.importFile1, vm.testCaseId, vm.runConfigId).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            loadTestCaseInputAndOutputParams();
                            document.getElementById("file1").value = "";
                            $rootScope.showSuccessMessage("Imported successfully");


                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            document.getElementById("file1").value = "";
                        }
                    )
                }
            };


            /*  ------ Initialize columns for input and outputParam Values ------*/
            var initColumns = {
                "Name": {
                    "columnName": "Name",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Value": {
                    "columnName": "Value",
                    "columnValue": null,
                    "columnType": "string"
                }
            };

            var initColumns1 = {
                "Name": {
                    "columnName": "Name",
                    "columnValue": null,
                    "columnType": "string"
                },
                "Value": {
                    "columnName": "Value",
                    "columnValue": null,
                    "columnType": "string"
                }
            };

            var inputParamHeaders = ["Name", "Value"];
            var inputParamHeaders1 = ["Name", "Value"];

            /* ------ Export Expected OutputParamValues ------*/
            function exportOutParams() {
                var exportRows = [];
                var empty = null;
                $rootScope.showBusyIndicator();
                for (var i = 0; i < vm.testCaseOutputExpectedParamValues.length; i++) {
                    var exportRwDetails = [];
                    var emptyColumns = angular.copy(initColumns1);
                    angular.forEach(inputParamHeaders1, function (header) {
                        empty = emptyColumns[header];
                        if (empty != undefined) {
                            var inputParamRow = vm.testCaseOutputExpectedParamValues[i];
                            if (empty.columnName == "Name" || empty.columnName == "Value") {
                                if (empty.columnName == "Name") {
                                    empty.columnValue = inputParamRow.outputParamData.name;
                                }
                                if (empty.columnName == "Value") {
                                    if (inputParamRow.outputParamData.dataType == 'INTEGER') {
                                        if (inputParamRow.outputParamData.outPutParamExpectedValue != null) {
                                            empty.columnValue = inputParamRow.outputParamData.outPutParamExpectedValue.integerValue;
                                        }
                                        else {
                                            empty.columnValue = '';
                                        }

                                    }
                                    if (inputParamRow.outputParamData.dataType == 'STRING') {
                                        if (inputParamRow.outputParamData.outPutParamExpectedValue != null) {
                                            empty.columnValue = inputParamRow.outputParamData.outPutParamExpectedValue.stringValue;
                                        } else {
                                            empty.columnValue = '';
                                        }

                                    }
                                    if (inputParamRow.outputParamData.dataType == 'DATE') {
                                        if (inputParamRow.outputParamData.outPutParamExpectedValue != null) {
                                            empty.columnValue = inputParamRow.outputParamData.outPutParamExpectedValue.dateValue;
                                        } else {
                                            empty.columnValue = '';
                                        }

                                    }
                                    if (inputParamRow.outputParamData.dataType == 'DOUBLE') {
                                        if (inputParamRow.outputParamData.outPutParamExpectedValue != null) {
                                            empty.columnValue = inputParamRow.outputParamData.outPutParamExpectedValue.doubleValue;
                                        } else {
                                            empty.columnValue = '';
                                        }

                                    }


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
                    "fileName": 'ExpectedOutput Param',
                    "headers": angular.copy(inputParamHeaders1)
                };

                CommonService.exportReport("EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                        $rootScope.hideBusyIndicator();

                    }
                )

            }


            /* -----   Export InputParam Values  ------*/
            function exportinputParams() {
                var exportRows = [];
                var empty = null;
                $rootScope.showBusyIndicator();
                for (var i = 0; i < vm.testCaseInputParamValues.length; i++) {
                    var exportRwDetails = [];
                    var emptyColumns = angular.copy(initColumns);
                    angular.forEach(inputParamHeaders, function (header) {
                        empty = emptyColumns[header];
                        if (empty != undefined) {
                            var inputParamRow = vm.testCaseInputParamValues[i];
                            if (empty.columnName == "Name" || empty.columnName == "Value") {
                                if (empty.columnName == "Name") {
                                    empty.columnValue = inputParamRow.inputParamData.name;
                                }
                                if (empty.columnName == "Value") {

                                    if (inputParamRow.inputParamData.dataType == 'INTEGER') {
                                        if (inputParamRow.inputParamData.rcInputParamValue != null) {
                                            empty.columnValue = inputParamRow.inputParamData.rcInputParamValue.integerValue;
                                        }
                                        else {
                                            empty.columnValue = '';
                                        }

                                    }
                                    if (inputParamRow.inputParamData.dataType == 'STRING') {
                                        if (inputParamRow.inputParamData.rcInputParamValue != null) {
                                            empty.columnValue = inputParamRow.inputParamData.rcInputParamValue.stringValue;
                                        } else {
                                            empty.columnValue = '';
                                        }

                                    }
                                    if (inputParamRow.inputParamData.dataType == 'DATE') {
                                        if (inputParamRow.inputParamData.rcInputParamValue != null) {
                                            empty.columnValue = inputParamRow.inputParamData.rcInputParamValue.dateValue;
                                        } else {
                                            empty.columnValue = '';
                                        }

                                    }
                                    if (inputParamRow.inputParamData.dataType == 'DOUBLE') {
                                        if (inputParamRow.inputParamData.rcInputParamValue != null) {
                                            empty.columnValue = inputParamRow.inputParamData.rcInputParamValue.doubleValue;
                                        } else {
                                            empty.columnValue = '';
                                        }

                                    }
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
                    "fileName": 'InputParam',
                    "headers": angular.copy(inputParamHeaders)
                };

                CommonService.exportReport("EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                        url += data + "/download";
                        window.open(url, '_self');
                        $rootScope.hideBusyIndicator();

                    }
                )
            }

            /* ------- Load Run Configuration Aattributes ------*/
            function loadRunConfigAttributes() {
                vm.runConfigAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("TESTRUNCONFIGURATION").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.runConfig.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: vm.runConfig.id,
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
                            vm.runConfigAttributes.push(att);
                        });

                        loadRunConfigObjectProperties();
                    });
            }

            /* -------- Load RunConfiguration Attribute properties ------*/
            function loadRunConfigObjectProperties() {
                ObjectAttributeService.getAllObjectAttributes(vm.runConfig.id).then(
                    function (data) {
                        var map = new Hashtable();
                        angular.forEach(data, function (attribute) {
                            map.put(attribute.id.attributeDef, attribute);
                        });
                        angular.forEach(vm.runConfigAttributes, function (attribute) {
                            var value = map.get(attribute.attributeDef.id);
                            if (value != null) {
                                attribute.editMode = false;
                                attribute.value.stringValue = value.stringValue;
                                attribute.value.integerValue = value.integerValue;
                                attribute.value.doubleValue = value.doubleValue;
                                attribute.value.dateValue = value.dateValue;
                            }
                        });
                    }
                )
            }

            /* ------- Validation for Run Configuration Attributes ------*/
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

            /* ----- Save Run Configuration properties  -----*/
            function saveRunConfigProperties(attribute) {
                if (attribute.attributeDef.objectType == 'TESTRUNCONFIGURATION') {
                    if (validateAttributes(attribute)) {
                        ObjectAttributeService.createObjectAttribute(vm.runId, attribute.value).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Configuration attribute saved successfully");
                                attribute.editMode = false;
                                loadRunConfigObjectProperties();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            /* ------- Edit Run Cnfiguration properties ------*/
            function editRunConfigProperties(attribute) {
                attribute.stringValue = attribute.value.stringValue;
                attribute.integerValue = attribute.value.integerValue;
                attribute.dateValue = attribute.value.dateValue;
                attribute.doubleValue = attribute.value.doubleValue;
                attribute.editMode = true;
                attribute.isNew = false;

            }

            /* ------ After Edit Cancel changes for Run Configuration Properties ------*/
            function cancelChanges(attribute) {
                attribute.value.stringValue = attribute.stringValue;
                attribute.value.integerValue = attribute.integerValue;
                attribute.value.dateValue = attribute.dateValue;
                attribute.value.doubleValue = attribute.doubleValue;
                attribute.editMode = false;

            }

            (function () {
                $scope.$on('app.runConfiguration.details', runConfigurationSelected);
                $scope.$on('app.runConfiguration.save', onSave);
            })();

        }
    }
)
;