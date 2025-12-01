<style>
    .datetimepicker .table-condensed {
        width: 300px !important;
        font-size: 14px;
    }
</style>
<div ng-if="configurationDetailsVm.type == null && selectedNode == true" style="padding: 10px">
    <span>Click on Run Configuration Type to show details</span>
</div>
<div ng-if="configurationDetailsVm.type == null && selectedNode == false" style="padding: 10px">
    <span>Click on Run Configuration Tree to create runTypes</span>
</div>
<div ng-if="configurationDetailsVm.type != null">
    <div class="typeInfo" style="top: 0px !important;">
        <%--<h4 class="section-title">Basic Info</h4>--%>


        <div class="row" ng-if="configurationDetailsVm.type.objectType != 'TESTCASE'">
            <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                <form class="form-horizontal">
                    <div class="form-group" style="padding-top: 20px;">
                        <label class="col-sm-4 control-label"><span>Name</span><span class="asterisk"> *</span> :
                        </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-model="configurationDetailsVm.type.name" >

                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span>Description</span> :
                        </label>

                        <div class="col-sm-8">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  ng-model="configurationDetailsVm.type.description" ></textarea>
                        </div>
                    </div>

                    <div class="form-group" ng-if="configurationDetailsVm.type.objectType == 'TESTRUNCONFIGURATION'">
                        <label class="col-sm-4 control-label">
                            <span>Scenario</span><span class="asterisk"> *</span> : </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-model="configurationDetailsVm.type.scenario.name" readonly/>
                        </div>
                    </div>
                </form>
            </div>
        </div>


        <div class="row" style="margin: 5px !important;"
             ng-show="configurationDetailsVm.type.objectType == 'TESTCASE' && configurationDetailsVm.type.id != null">
            <div class="col-md-6 panel panel-primary">
                <div class="panel-heading" style="padding: 10px;color: white;text-align: center;font-size: 22px;">
                    Input Param Values
                </div>
                <div class="panel-body" style="padding: 5px;">


                    <div class="responsive-table">
                        <table class="table table-striped" id="inputParamValues">
                            <thead>
                            <tr>
                                <th style="width: 40%">Name</th>
                                <th style="width: 40%">Value</th>
                                <th style="width: 10%"><i class="fa fa-download"
                                                          style="font-size: 18px;cursor: pointer;"
                                                          ng-click="exportinputParams()"
                                                          ng-if="configurationDetailsVm.importAndExportFlag == true && configurationDetailsVm.type.objectType == 'TESTCASE'"
                                                          title="Export input param values"
                                                          aria-hidden="true"></i>
                                    <input type="file" id="file" value="file" style="display: none"
                                           onchange="angular.element(this).scope().importInputParamValues()"/>
                                    <label for="file"> <i class="fa fa-upload" aria-hidden="true"
                                                          title="Import input param values"
                                                          ng-if="configurationDetailsVm.importAndExportFlag == true && configurationDetailsVm.type.objectType == 'TESTCASE'"
                                                          style="cursor:pointer;font-size: 18px"></i></label>
                                </th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="configurationDetailsVm.testCaseInputParamValues.length == 0">
                                <td colspan="7"><span>No Input Param Values</span></td>
                            </tr>
                            <tr ng-repeat="param in configurationDetailsVm.testCaseInputParamValues track by $index">
                                <td>
                                    <span>{{param.inputParamData.name}}</span>
                                </td>
                                <td>
                                    <span ng-if="param.editMode == false && param.inputParamData.dataType == 'STRING'">{{param.stringValue}}</span>
                                    <span ng-if="param.editMode == false && param.inputParamData.dataType == 'INTEGER'">{{param.integerValue}}</span>
                                    <span ng-if="param.editMode == false && param.inputParamData.dataType == 'DOUBLE'">{{param.doubleValue}}</span>
                                    <span ng-if="param.editMode == false && param.inputParamData.dataType == 'DATE'">{{param.dateValue}}</span>
                                    <span ng-if="param.editMode == false && param.inputParamData.dataType == 'TIME'">{{param.timeValue}}</span>
                                    <span ng-if="param.editMode == false && param.inputParamData.dataType == 'TIMESTAMP'">{{param.timestampValue}}</span>

                                    <input ng-if="param.editMode == true && param.inputParamData.dataType == 'STRING'"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.stringValue">
                                    <input ng-if="param.editMode == true && param.inputParamData.dataType == 'INTEGER'"
                                           type="number" class="form-control input-sm"
                                           ng-model="param.integerValue">
                                    <input ng-if="param.editMode == true && param.inputParamData.dataType == 'DOUBLE'"
                                           type="text" class="form-control input-sm" valid-number
                                           ng-model="param.doubleValue">
                                    <input ng-if="param.editMode == true && param.inputParamData.dataType == 'DATE'"
                                           date-picker style="background: white"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.dateValue">
                                    <input ng-if="param.editMode == true && param.inputParamData.dataType == 'TIME'"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.timeValue">
                                    <input ng-if="param.editMode == true && param.inputParamData.dataType == 'TIMESTAMP'"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.timestampValue">
                                </td>
                                <td>
                                    <i class="fa fa-check-circle" title="Click to Save" ng-if="param.editMode == true"
                                       ng-click="configurationDetailsVm.saveInputParamValue(param)"
                                       style="font-size:20px;cursor:pointer;"></i>
                                    <i class="fa fa-edit" title="Click to Edit" ng-if="param.editMode == false"
                                       ng-click="configurationDetailsVm.editInputParamValue(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Cancel Changes"
                                       ng-if="param.editMode == true && param.isNew == false"
                                       ng-click="configurationDetailsVm.onCancelInputParamValue(param)"
                                       style="font-size:20px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Delete"
                                       ng-if="param.editMode == false"
                                       ng-click="configurationDetailsVm.deleteInputParamValue(param)"
                                       style="font-size:20px;cursor:pointer;"></i>
                                </td>
                            </tr>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-6 panel panel-primary">
                <div class="panel-heading" style="padding: 10px;color: white;text-align: center;font-size: 22px;">
                    Expected Output Param Values

                </div>
                <div class="panel-body" style="padding: 5px;">


                    <div class="responsive-table">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th style="width: 40%">Name</th>
                                <th style="width: 40%">Value</th>
                                <th style="width: 10%"><i class="fa fa-download"
                                                          style="font-size: 18px;cursor: pointer;"
                                                          ng-click="exportOutParams()"
                                                          ng-if="configurationDetailsVm.importAndExportFlag1 == true && configurationDetailsVm.type.objectType == 'TESTCASE'"
                                                          title="Export Expected output param values"
                                                          aria-hidden="true"></i>
                                    <input type="file" id="file1" value="file1" style="display: none"
                                           onchange="angular.element(this).scope().importOutPutParamValues()"/>
                                    <label for="file1"> <i class="fa fa-upload" aria-hidden="true"
                                                           title="Import output param values"
                                                           ng-if="configurationDetailsVm.importAndExportFlag1 == true && configurationDetailsVm.type.objectType == 'TESTCASE'"
                                                           style="cursor:pointer;font-size: 18px"></i></label></th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="configurationDetailsVm.testCaseOutputExpectedParamValues.length == 0">
                                <td colspan="7"><span>No Output Param Values</span></td>
                            </tr>
                            <tr ng-repeat="param in configurationDetailsVm.testCaseOutputExpectedParamValues">
                                <td style="width: 200px">
                                    <span>{{param.outputParamData.name}}</span>
                                </td>
                                <td>
                                    <span ng-if="param.editMode == false && param.outputParamData.dataType == 'STRING'">{{param.stringValue}}</span>
                                    <span ng-if="param.editMode == false && param.outputParamData.dataType == 'INTEGER'">{{param.integerValue}}</span>
                                    <span ng-if="param.editMode == false && param.outputParamData.dataType == 'DOUBLE'">{{param.doubleValue}}</span>
                                    <span ng-if="param.editMode == false && param.outputParamData.dataType == 'DATE'">{{param.dateValue}}</span>
                                    <span ng-if="param.editMode == false && param.outputParamData.dataType == 'TIME'">{{param.timeValue}}</span>
                                    <span ng-if="param.editMode == false && param.outputParamData.dataType == 'TIMESTAMP'">{{param.timestampValue}}</span>

                                    <input ng-if="param.editMode == true && param.outputParamData.dataType == 'STRING'"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.stringValue">
                                    <input ng-if="param.editMode == true && param.outputParamData.dataType == 'INTEGER'"
                                           type="number" class="form-control input-sm"
                                           ng-model="param.integerValue">
                                    <input ng-if="param.editMode == true && param.outputParamData.dataType == 'DOUBLE'"
                                           type="text" class="form-control input-sm" valid-number
                                           ng-model="param.doubleValue">
                                    <input ng-if="param.editMode == true && param.outputParamData.dataType == 'DATE'"
                                           date-picker style="background: white;"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.dateValue">
                                    <input ng-if="param.editMode == true && param.outputParamData.dataType == 'TIME'"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.timeValue">
                                    <input ng-if="param.editMode == true && param.outputParamData.dataType == 'TIMESTAMP'"
                                           type="text" class="form-control input-sm"
                                           ng-model="param.timestampValue">

                                </td>
                                <td>
                                    <i class="fa fa-check-circle" title="Click to Save" ng-if="param.editMode == true"
                                       ng-click="configurationDetailsVm.saveOutputParamValue(param)"
                                       style="font-size:20px;cursor:pointer;"></i>
                                    <i class="fa fa-edit" title="Click to Edit" ng-if="param.editMode == false"
                                       ng-click="configurationDetailsVm.editOutputParamValue(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Delete"
                                       ng-if="param.editMode == true && param.isNew == false"
                                       ng-click="configurationDetailsVm.onCancelOutputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Remove"
                                       ng-if="param.editMode == false"
                                       ng-click="configurationDetailsVm.deleteOutputParamValue(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="configurationDetailsVm.type.objectType == 'TESTRUNCONFIGURATION'">
            <style>
                .table-condensed thead .picker-switch, .table-condensed thead .prev, .table-condensed thead .next {
                    display: none !important;
                }
            </style>
            <div class="col-md-12 panel panel-primary" style="margin-top: 5%;">
                <div class="panel-heading" style="padding: 10px;color: white;font-size: 22px;">
                    Run Schedule
                </div>
                <div class="panel-body">
                    <form class="form-horizontal">
                        <div class="col-md-6">
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Sunday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.sunday">

                                </div>
                            </div>
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Monday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.monday">

                                </div>
                            </div>
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Tuesday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.tuesday">
                                </div>
                            </div>
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Wednesday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.wednesday">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Thursday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.thursday">
                                </div>
                            </div>
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Friday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.friday">
                                </div>
                            </div>
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span>Saturday</span> :
                                </label>

                                <div class="col-sm-6">
                                    <input type="text" class="form-control" placeholder="Select Time" time-picker
                                           ng-model="configurationDetailsVm.runSchedule.saturday">
                                </div>
                            </div>
                            <div class="form-group" style="padding-top: 10px;">
                                <label class="col-sm-4 control-label"><span></span>
                                </label>

                                <div class="col-sm-6">
                                    <button class="btn btn-sm btn-success" title="Click to Save Run Schedule"
                                            ng-click="configurationDetailsVm.saveRunSchedule()">Save
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

            </div>


            <div class="col-md-12 panel panel-primary" style="margin-top: 5%;">
                <div class="panel-heading" style="padding: 10px;color: white;;font-size: 22px;">
                    Test Run History
                </div>
                <div class="panel-body">

                    <div class="responsive-table">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>Test Run Id</th>
                                <th>Date</th>
                                <th>Status</th>
                                <th>Total</th>
                                <th>Passed</th>
                                <th>Failed</th>
                            </tr>
                            </thead>

                            <tbody>

                            <tr ng-if="configurationDetailsVm.history.length == 0">
                                <td colspan="6"><span>No Test Runs</span></td>
                            </tr>
                            <tr ng-if="configurationDetailsVm.history.length >= 0"
                                ng-repeat="hist in configurationDetailsVm.history">
                                <td>
                                    <span>{{hist.id}}</span>
                                </td>
                                <td>
                                    <span>{{hist.startTime}}</span>
                                </td>
                                <td>
                                    <span>{{hist.status}}</span>
                                </td>

                                <td>
                                    <span>{{hist.total}}</span>
                                </td>

                                <td>
                                    <span>{{hist.passed}}</span>
                                </td>

                                <td>
                                    <span>{{hist.failed}}</span>
                                </td>


                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>


            <div ng-if="configurationDetailsVm.type.objectType == 'TESTRUNCONFIGURATION' && configurationDetailsVm.runConfigAttributes.length >0"
                 class="col-md-12 panel panel-primary"
                 style="margin-top: 5%;">
                <div class="panel-heading" style="padding: 10px;color: white;font-size: 22px;">
                    Test Case Attributes
                </div>

                <div class="panel-body">

                    <div class="responsive-table">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th style="width: 40%">Name</th>
                                <th style="width: 40%">Value</th>
                                <th style="width: 10%">Actions</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="configurationDetailsVm.runConfigAttributes.length == 0">
                                <td colspan="7"><span>No Attributes</span></td>
                            </tr>
                            <tr ng-repeat="attribute in configurationDetailsVm.runConfigAttributes track by $index">
                                <td>
                                    <span>{{attribute.attributeDef.name}}</span>
                                </td>
                                <td>
                                    <span ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'STRING'">{{attribute.value.stringValue}}</span>
                                    <span ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'INTEGER'">{{attribute.value.integerValue}}</span>
                                    <span ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'DOUBLE'">{{attribute.value.doubleValue}}</span>
                                    <span ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'DATE'">{{attribute.value.dateValue}}</span>
                                    <span ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'TIME'">{{attribute.value.timeValue}}</span>
                                    <span ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'TIMESTAMP'">{{attribute.value.timestampValue}}</span>

                                    <div style="width: 300px;">
                                        <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'STRING'"
                                               type="text" class="form-control input-sm"
                                               ng-model="attribute.value.stringValue">
                                        <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'INTEGER'"
                                               type="text" class="form-control input-sm" numbers-only
                                               ng-model="attribute.value.integerValue">
                                        <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'DOUBLE'"
                                               class="form-control input-sm" type="text" valid-number
                                               ng-model="attribute.value.doubleValue">
                                        <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'DATE'"
                                               id="my_txtbox"
                                               date-picker
                                               type="text" class="form-control input-sm" style="background: white;"
                                               ng-model="attribute.value.dateValue">
                                        <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'TIME'"
                                               type="text" class="form-control input-sm" time-picker
                                               ng-model="attribute.value.timeValue">


                                        <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'TIMESTAMP'"
                                               type="text" class="form-control input-sm"
                                               time-pickers
                                               ng-model="attribute.value.timestampValue">
                                    </div>
                                </td>
                                <td>
                                    <i class="fa fa-check-circle" title="Click to Save"
                                       ng-if="attribute.editMode == true"
                                       ng-click="configurationDetailsVm.saveRunConfigProperties(attribute)"
                                       style="font-size:20px;cursor:pointer;"></i>
                                    <i class="fa fa-edit" title="Click to Edit" ng-if="attribute.editMode == false"
                                       ng-click="configurationDetailsVm.editRunConfigProperties(attribute)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Cancel Changes"
                                       ng-if="attribute.editMode == true && attribute.isNew == false"
                                       ng-click="configurationDetailsVm.cancelChanges(attribute)"
                                       style="font-size:20px;cursor:pointer;"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>