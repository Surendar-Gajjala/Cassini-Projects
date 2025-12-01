<style scoped>
    select {
        display: inline-block;
        width: auto;
    }

    /* The Close Button */
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeimage1 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage1:hover,
    .img-model .closeimage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .datetimepicker .table-condensed {
        width: 300px !important;
        font-size: 14px;
    }
</style>
<div ng-if="definitionDetailsVm.type == null && showDetailsTitleMessage == true" style="padding: 10px">
    <span>Click on Definition Type to show details</span>
</div>
<div ng-if="definitionDetailsVm.type == null && showDetailsTitleMessage == false">
    <span>Click on Definition Tree to create types</span>
</div>
<div ng-if="definitionDetailsVm.type != null">
    <div class="typeInfo" style="top: 0px !important;">
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                <form class="form-horizontal">
                    <div class="form-group" style="padding-top: 20px;">
                        <label class="col-sm-4 control-label"><span>Name</span><span class="asterisk"> *</span> :
                        </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-model="definitionDetailsVm.type.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span>Description</span> :
                        </label>

                        <div class="col-sm-8">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  ng-model="definitionDetailsVm.type.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group" ng-if="definitionDetailsVm.type.objectType == 'TESTCASE'">
                        <label class="col-sm-4 control-label"><span>Execution Type</span> : </label>

                        <div class="col-sm-8" style="text-align: left;margin: 0;padding: 5px 2px;">
                            <input type="radio" name="executionType" value="PROGRAM"
                                   ng-model="definitionDetailsVm.executionTypeValue"
                                   ng-click="definitionDetailsVm.selectProgram()"
                                   style="width: 25px;"> PROGRAM
                            <input type="radio" name="executionType"
                                   ng-model="definitionDetailsVm.executionTypeValue" value="SCRIPT"
                                   ng-click="definitionDetailsVm.selectScript()"
                                   style="width: 25px;"> SCRIPT
                        </div>
                    </div>

                    <div ng-show="definitionDetailsVm.type.objectType == 'TESTCASE' && definitionDetailsVm.executionType == 'PROGRAM'">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span>Program</span><span class="asterisk"> *</span> :
                            </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="title"
                                       ng-model="definitionDetailsVm.programExecution.program">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span>Params</span> : </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="title"
                                       ng-model="definitionDetailsVm.programExecution.params">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span>Working Directory</span> : </label>

                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="title"
                                       ng-model="definitionDetailsVm.programExecution.workingDir">
                            </div>
                        </div>
                    </div>

                    <div ng-show="definitionDetailsVm.type.objectType == 'TESTCASE' && definitionDetailsVm.executionType == 'SCRIPT'">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span>Language</span><span class="asterisk"> *</span>
                                : </label>

                            <div class="col-sm-8">
                                <ui-select ng-model="definitionDetailsVm.type.scriptExecution.scriptLanguage"
                                           on-select="onSelected($item)"
                                           theme="bootstrap"
                                           style="width:100%;">
                                    <ui-select-match placeholder="Select Language">{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="language in definitionDetailsVm.languageTypes">
                                        <div ng-bind="language"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span>Script</span><span class="asterisk"> *</span> :
                            </label>

                            <div class="col-sm-8" style="padding-top: 10px;">
                                <div class="" ng-hide="definitionDetailsVm.clickToHideLink == false"
                                     ng-if="definitionDetailsVm.linkActivate == true && definitionDetailsVm.selectedItem == 'GROOVY'"
                                     style="cursor: pointer">
                                    <a ng-click="clickToOpenSidePanel()">Click to go Groovy script editor</a>
                                </div>
                                <div class="" ng-hide="definitionDetailsVm.clickToHideLink == false"
                                     ng-if="definitionDetailsVm.linkActivate == true && definitionDetailsVm.selectedItem == 'PYTHON'"
                                     style="cursor: pointer">
                                    <a ng-if="definitionDetailsVm.type.objectType != null"
                                       ng-click="clickToOpenSidePanel()">Click
                                        to go Python script editor</a>
                                </div>


                                <div class="" ng-hide="definitionDetailsVm.clickToHideLink1 == false"
                                     ng-if="definitionDetailsVm.linkActivate1 == true && definitionDetailsVm.selectedItem == 'GROOVY'"
                                     style="cursor: pointer">
                                    <a ng-click="clickToOpenSidePanelToEdit()">Click to edit Groovy script </a>
                                </div>
                                <div class="" ng-hide="definitionDetailsVm.clickToHideLink1 == false"
                                     ng-if="definitionDetailsVm.linkActivate1 == true && definitionDetailsVm.selectedItem == 'PYTHON'"
                                     style="cursor: pointer">
                                    <a ng-click="clickToOpenSidePanelToEdit()">Click to edit Python script </a>
                                </div>
                            </div>
                        </div>
                    </div>

                </form>
            </div>
        </div>
        <br>
        <br>

        <div class="row" style="margin: 5px !important;"
             ng-show="definitionDetailsVm.type.objectType == 'TESTCASE' && definitionDetailsVm.type.id != null">

            <div class="col-md-6 panel panel-primary">
                <div class="panel-heading" style="padding: 10px;color: white;font-size: 22px;"><span>Input
                    Params</span>

                </div>
                <div class="panel-body" style="padding: 5px;">

                    <div class="responsive-table">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Data Type</th>
                                <th style="width: 70px;">
                                    <i class="fa fa-plus-circle" title="Click to add new"
                                       ng-click="definitionDetailsVm.addNewInputParam()"
                                       style="font-size:18px;cursor:pointer;"></i><input type="file" id="file"
                                                                                         value="file"
                                                                                         style="display: none"
                                                                                         onchange="angular.element(this).scope().importInputParams()"/>
                                    <label for="file"> <i class="fa fa-upload" aria-hidden="true"
                                                          title="Import input params"
                                                          ng-if="definitionDetailsVm.type.objectType == 'TESTCASE' && definitionDetailsVm.importAndExportFlag == true"
                                                          style="cursor:pointer;font-size: 18px"></i></label>
                                </th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="definitionDetailsVm.testCaseInputParams.length == 0">
                                <td colspan="7"><span>No Input Params</span></td>
                            </tr>
                            <tr ng-repeat="param in definitionDetailsVm.testCaseInputParams">
                                <td>
                                    <span ng-if="param.editMode == false">{{param.name}}</span>
                                    <input ng-if="param.editMode == true" type="text" class="form-control input-sm"
                                           ng-model="param.name">
                                </td>
                                <td>
                                    <span ng-if="param.editMode == false">{{param.description}}</span>
                                    <input ng-if="param.editMode == true" type="text" class="form-control input-sm"
                                           ng-model="param.description">
                                </td>
                                <td style="width: 120px">
                                    <span ng-if="param.editMode == false">{{param.dataType}}</span>
                                    <select ng-if="param.editMode == true" class="form-control input-sm"
                                            ng-model="param.dataType"
                                            ng-options="dataType for dataType in definitionDetailsVm.dataTypes">
                                    </select>
                                </td>
                                <td>
                                    <i class="fa fa-check-circle" title="Click to Save" ng-if="param.editMode == true"
                                       ng-click="definitionDetailsVm.saveInputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Cancel Changes"
                                       ng-if="param.editMode == true && param.isNew == false"
                                       ng-click="definitionDetailsVm.onCancelInputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-edit" title="Click to Edit" ng-if="param.editMode == false"
                                       ng-click="definitionDetailsVm.editInputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Remove"
                                       ng-if="param.editMode == true && param.isNew == true"
                                       ng-click="definitionDetailsVm.deleteInputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Delete"
                                       ng-if="param.editMode == false && param.isNew == false"
                                       ng-click="definitionDetailsVm.deleteInputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>

            <div class="col-md-6 panel panel-primary">
                <div class="panel-heading" style="padding: 10px;color: white;font-size: 22px;">Output
                    Params

                </div>
                <div class="panel-body" style="padding: 5px;">

                    <div class="responsive-table">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Date Type</th>
                                <th style="width: 70px;">
                                    <i class="fa fa-plus-circle" title="Click to add new"
                                       ng-click="definitionDetailsVm.addNewOutputParam()"
                                       style="font-size:20px;cursor:pointer;"></i> <input type="file" id="file1"
                                                                                          value="file1"
                                                                                          style="display: none"
                                                                                          onchange="angular.element(this).scope().importOutPutParams()"/>
                                    <label for="file1"> <i class="fa fa-upload" aria-hidden="true"
                                                           title="Import output params"
                                                           ng-if="definitionDetailsVm.type.objectType == 'TESTCASE' && definitionDetailsVm.importAndExportFlag1 == true"
                                                           style="cursor:pointer;font-size: 18px"></i></label>
                                </th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-if="definitionDetailsVm.testCaseOutputParams.length == 0">
                                <td colspan="7"><span>No Output Params</span></td>
                            </tr>
                            <tr ng-repeat="param in definitionDetailsVm.testCaseOutputParams">
                                <td style="width: 200px">
                                    <span ng-if="param.editMode == false">{{param.name}}</span>
                                    <input ng-if="param.editMode == true" type="text" class="form-control input-sm"
                                           ng-model="param.name">
                                </td>
                                <td>
                                    <span ng-if="param.editMode == false">{{param.description}}</span>
                                    <input ng-if="param.editMode == true" type="text" class="form-control input-sm"
                                           ng-model="param.description">
                                </td>
                                <td style="width: 120px">
                                    <span ng-if="param.editMode == false">{{param.dataType}}</span>
                                    <select ng-if="param.editMode == true" class="form-control input-sm"
                                            ng-model="param.dataType"
                                            ng-options="dataType for dataType in definitionDetailsVm.dataTypes">
                                    </select>
                                </td>

                                <td>
                                    <i class="fa fa-check-circle" title="Click to Save" ng-if="param.editMode == true"
                                       ng-click="definitionDetailsVm.saveOutputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Cancel Changes"
                                       ng-if="param.editMode == true && param.isNew == false"
                                       ng-click="definitionDetailsVm.onCancelOutputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-edit" title="Click to Edit" ng-if="param.editMode == false"
                                       ng-click="definitionDetailsVm.editOutputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Remove"
                                       ng-if="param.editMode == true && param.isNew == true"
                                       ng-click="definitionDetailsVm.deleteOutputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                    <i class="fa fa-times-circle" title="Click to Delete"
                                       ng-if="param.editMode == false && param.isNew == false"
                                       ng-click="definitionDetailsVm.deleteOutputParam(param)"
                                       style="font-size:18px;cursor:pointer;"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>


        <div ng-if="definitionDetailsVm.type.objectType == 'TESTSCENARIO' && definitionDetailsVm.type.id != null">
            <style>
                .table-condensed thead .picker-switch, .table-condensed thead .prev, .table-condensed thead .next {
                    display: none !important;
                }
            </style>

            <div class="col-md-12 panel panel-primary" style="margin-top: 5%;">
                <div class="panel-heading" style="padding: 10px;color: white;font-size: 22px;">
                    Test Run History
                </div>

                <div class="panel-body">
                    <div class="responsive-table" style="padding: 10px;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Test Run Id</th>
                                <th>Run Configuration</th>
                                <th>Status</th>
                                <th>Start Time</th>
                                <th>Finish Time</th>
                                <th>Total</th>
                                <th>Passed</th>
                                <th>Failed</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="definitionDetailsVm.loading == true">
                                <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">Loading RunHistory...
                            </span>
                                </td>
                            </tr>

                            <tr ng-if="definitionDetailsVm.history.length == 0">
                                <td colspan="10" style="padding-left: 11px;">No RunHistory</td>
                            </tr>
                            <tr ng-repeat="runHistory in definitionDetailsVm.history">
                                <td>{{runHistory.id}}</td>
                                <td>{{runHistory.testRunConfiguration.name}}</td>
                                <td>{{runHistory.status}}</td>
                                <td>{{runHistory.startTime}}</td>
                                <td>{{runHistory.finishTime}}</td>
                                <td>{{runHistory.total}}</td>
                                <td>{{runHistory.passed}}</td>
                                <td>{{runHistory.failed}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="(definitionDetailsVm.type.objectType == 'TESTSCENARIO' || definitionDetailsVm.type.objectType == 'TESTPLAN' || definitionDetailsVm.type.objectType == 'TESTSUITE' || definitionDetailsVm.type.objectType == 'TESTCASE') && definitionDetailsVm.objectAttributes.length>0"
             class="col-md-12 panel panel-primary"
             style="margin-top: 5%;">
            <div ng-if="definitionDetailsVm.type.objectType == 'TESTSCENARIO'" class="panel-heading"
                 style="padding: 10px;color: white;font-size: 22px;">
                Test Scenario Attributes
            </div>
            <div ng-if="definitionDetailsVm.type.objectType == 'TESTPLAN'" class="panel-heading"
                 style="padding: 10px;color: white;font-size: 22px;">
                Test Plan Attributes
            </div>
            <div ng-if="definitionDetailsVm.type.objectType == 'TESTSUITE'" class="panel-heading"
                 style="padding: 10px;color: white;font-size: 22px;">
                Test Suite Attributes
            </div>
            <div ng-if="definitionDetailsVm.type.objectType == 'TESTCASE'" class="panel-heading"
                 style="padding: 10px;color: white;font-size: 22px;">
                Test Case Attributes
            </div>

            <div class="panel-body">
                <div class="responsive-table">
                    <table class="table table-striped" id="inputParamValues">
                        <thead>
                        <tr>
                            <th style="width: 40%">Name</th>
                            <th style="width: 40%">Value</th>
                            <th style="width: 10%">Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-if="definitionDetailsVm.objectAttributes.length == 0">
                            <td colspan="7"><span>No Attributes</span></td>
                        </tr>
                        <tr ng-repeat="attribute in definitionDetailsVm.objectAttributes track by $index">
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

                                    <label ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'BOOLEAN'"
                                           class="radio-inline"
                                           ng-repeat="choice in [true, false]">
                                        <input type="radio" name="change_{{attribute.attributeDef.id}}"
                                               ng-value="choice"
                                               ng-model="attribute.value.booleanValue">{{choice}}
                                    </label>
                                </div>
                                <%------  Image Attribute  --------%>

                                <div ng-if="attribute.attributeDef.dataType == 'IMAGE'">
                                              <span>
                                               <a href="" ng-click="definitionDetailsVm.showImageProperty(attribute)"
                                                  title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}">
                                                   <img ng-if="attribute.value.imageValue != null"
                                                        ng-src="{{attribute.value.itemImagePath}}"
                                                        style="height: 30px;width: 30px;margin-bottom: 5px;">
                                               </a>
                                                 </span>
                                    <%-- To show large image --%>
                                    <div id="myModal2" class="img-model modal">
                                        <span class="closeimage1">&times;</span>
                                        <img class="modal-content" id="img03">
                                    </div>
                                    <div class="inline">
                                        <input class="browse-control"
                                               ng-if="attribute.value.imageValue == null && attribute.changeImage == false"
                                               name="file"
                                               id="attributeImageFile1"
                                               type="file" ng-file-model="attribute.newImageValue"
                                               style="width: 250px;">

                                        <input class="browse-control"
                                               ng-if="attribute.changeImage == true"
                                               name="file"
                                               id="attributeImageFile"
                                               type="file" ng-file-model="attribute.newImageValue"
                                               style="width: 250px;">
                                    </div>
                                </div>


                                <%------  Boolean Attribute  ------%>

                                <div ng-if="attribute.attributeDef.dataType == 'BOOLEAN'">
                                <span>
                                <a ng-if="attribute.editMode == false"
                                   href="" ng-click="changeAttribute(attribute)">
                                    {{attribute.value.booleanValue}}
                                </a>
                                </span>

                                </div>

                            </td>
                            <td>
                                <i class="fa fa-check-circle" title="Click to Save"
                                   ng-if="attribute.editMode == true && attribute.attributeDef.dataType != 'IMAGE'"
                                   ng-click="definitionDetailsVm.saveScenarioProperties(attribute)"
                                   style="font-size:20px;cursor:pointer;"></i>
                                <i class="fa fa-check-circle" title="Click to Save"
                                   ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'IMAGE'"
                                   ng-click="definitionDetailsVm.saveImage(attribute)"
                                   style="font-size:20px;cursor:pointer;"></i>
                                <i class="fa fa-edit" title="Click to Edit"
                                   ng-if="attribute.editMode == false && attribute.attributeDef.dataType != 'IMAGE'"
                                   ng-click="definitionDetailsVm.editScenarioProperties(attribute)"
                                   style="font-size:18px;cursor:pointer;"></i>
                                <i class="fa fa-edit" title="Click to Edit"
                                   ng-if="attribute.editMode == false && attribute.attributeDef.dataType == 'IMAGE'"
                                   ng-click="definitionDetailsVm.editScenarioImageProperties(attribute)"
                                   style="font-size:18px;cursor:pointer;"></i>
                                <i class="fa fa-times-circle" title="Cancel Changes"
                                   ng-if="attribute.editMode == true && attribute.isNew == false && attribute.attributeDef.dataType != 'IMAGE'"
                                   ng-click="definitionDetailsVm.cancelChanges(attribute)"
                                   style="font-size:20px;cursor:pointer;"></i>
                                <i class="fa fa-times-circle" title="Cancel Changes"
                                   ng-if="attribute.editMode == true && attribute.isNew == false && attribute.attributeDef.dataType == 'IMAGE'"
                                   ng-click="definitionDetailsVm.cancelImageChanges(attribute)"
                                   style="font-size:20px;cursor:pointer;"></i>
                                <i class="fa fa-times-circle" title="Delete attribute value"
                                   ng-if="attribute.editMode == false"
                                   ng-click="definitionDetailsVm.deleteAttribute(attribute)"
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