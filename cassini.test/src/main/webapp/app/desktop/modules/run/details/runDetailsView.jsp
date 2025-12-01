<style scoped>
    .configuration-node {
        background: transparent url("app/assets/images/configuration.png") no-repeat !important;
        height: 16px;
    }

    .scenario-node {
        background: transparent url("app/assets/images/scenario (2).png") no-repeat !important;
        height: 16px;
    }

    .plan-node {
        background: transparent url("app/assets/images/sketch.png") no-repeat !important;
        height: 16px;
    }

    .suite-node {
        background: transparent url("app/assets/images/settings.png") no-repeat !important;
        height: 16px;
    }

    .case-node {
        background: transparent url("app/assets/images/file-case.png") no-repeat !important;
        height: 16px;
    }

    .toggleColor1 {
        color: #A8A2A2
    }

    .toggleColor2 {
        color: white;
    }

    .my-search {
        border: 0;
        outline: 0;
        background: transparent;
        box-shadow: 0px 0px 0px #d3d3d3;
        border-bottom: 1px solid #d3d3d3;
        margin: 5px 0px;
        width: 210px;
        text-align: center;
    }

    .search-form {
        border-radius: 30px;
    }
</style>

<div class="view-container" fitcontent>


    <div class="view-content no-padding" style="padding: 10px;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">

                <div style="height: 40px;">
                    <input type="search" class="form-control input-sm search-form" placeholder="Search"
                           ng-model="runDetailsVm.searchValue" ng-change="runDetailsVm.searchTree()">
                </div>


                <div id="classificationContainer1" class="classification-pane" data-toggle="context"
                     data-target="#context-menu1">
                    <ul id="runCase1" class="easyui-tree">
                    </ul>
                </div>


                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <ul id="runCase" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;overflow: auto">
                <br/>


                <div>
                    <div style="display: none;" id="itemFiles">
                        <div style="font-style: italic; text-align: center;margin-bottom: 20px;">
                            Drag and drop files on to the table or <a href="" ng-click="runDetailsVm.selectFile()">(Click
                            here to add files)</a>
                        </div>
                        <div class="responsive-table">
                            <table class="table table-striped highlight-row">
                                <thead>
                                <tr>
                                    <th>File Name</th>
                                    <th>File Size</th>
                                    <th style="text-align: center">Version</th>
                                    <th>Created By</th>
                                    <th>Created Date</th>
                                    <th>Modified Date</th>
                                    <th>Actions</th>

                                </tr>
                                </thead>
                                <tbody>

                                <tr ng-show="runDetailsVm.showFileDropzone == true" class="dropzone-row">
                                    <td colspan="14">
                                        <div style="height: 300px; border: slategray dashed 2px; overflow-y: auto">
                                            <div class="table table-striped table-bordered" class="files previews"
                                                 id="previews">
                                                <div id="template" class="file-row">
                                                    <!-- This is used as the file preview template -->
                                                    <div style="width:10px">
                                                        <span class="preview"><img data-dz-thumbnail/></span>
                                                    </div>
                                                    <div>
                                                        <p class="name" data-dz-name></p>
                                                        <strong class="error text-danger" data-dz-errormessage></strong>
                                                    </div>
                                                    <div>
                                                        <p class="size" data-dz-size></p>

                                                        <div class="progress progress-striped active" role="progressbar"
                                                             aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"
                                                             style="margin-bottom: 0">
                                                            <div class="progress-bar progress-bar-success"
                                                                 style="width:0%;"
                                                                 data-dz-uploadprogress></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>


                                <tr ng-if="runDetailsVm.loading == true">
                                    <td colspan="11"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading
                                        Files...
                                    </td>
                                </tr>
                                <tr ng-if="runDetailsVm.files.length == 0">
                                    <td colspan="11">No Files</td>
                                </tr>
                                <tr ng-if="runDetailsVm.files.length > 0"
                                    ng-repeat="file in runDetailsVm.files">
                                    <td><a href="" ng-click="runDetailsVm.downloadFile(file)">{{file.name}}</a></td>
                                    <td>{{runDetailsVm.fileSizeToString(file.size)}}</td>
                                    <td style="text-align: center">{{file.version}}</td>
                                    <td>{{file.createdByObject.fullName}}</td>
                                    <td>{{file.createdDate}}</td>
                                    <td>{{file.modifiedDate}}</td>
                                    <td>

                                        <button title="Delete this file" class="btn btn-xs btn-danger"
                                                ng-click="runDetailsVm.deleteFile(file)">
                                            <i class="fa fa-trash"></i>
                                        </button>

                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div ng-show="runDetailsVm.showDropzone == true">
                        <%@include file="tabs/files/dropzoneComponent.jsp" %>
                    </div>
                </div>

                <div class="row" id="testCaseDetailsDiv" style="display: none;">
                    <div class="view-content no-padding" style="" id="scrollcontent">
                        <div class="row row-eq-height" style="margin: 0;">
                            <div class="col-sm-12" style="padding: 0px;">
                                <div class="item-details-tabs">
                                    <uib-tabset active="runDetailsVm.active">
                                        <uib-tab heading="{{runDetailsVm.tabs.basic.heading}}"
                                                 active="runDetailsVm.tabs.basic.active"
                                                 select="runDetailsVm.itemDetailsTabActivated(runDetailsVm.tabs.basic.id)">
                                            <div ng-include="runDetailsVm.tabs.basic.template"
                                                 ng-controller="RunCaseBasicInfoController as runCaseBasicVm"></div>
                                        </uib-tab>

                                        <uib-tab heading="{{runDetailsVm.tabs.params.heading}}"
                                                 active="runDetailsVm.tabs.params.active"
                                                 select="runDetailsVm.itemDetailsTabActivated(runDetailsVm.tabs.params.id)">
                                            <div ng-include="runDetailsVm.tabs.params.template"
                                                 ng-controller="RunCaseParamsController as runCaseParamVm"></div>
                                        </uib-tab>
                                        <uib-tab heading="{{runDetailsVm.tabs.logs.heading}}"
                                                 active="runDetailsVm.tabs.logs.active"
                                                 select="runDetailsVm.runCaseDetailsTabActivated(runDetailsVm.tabs.logs.id)">
                                            <div ng-include="runDetailsVm.tabs.logs.template"
                                                 ng-controller="RunCaseLogFileController as runCaseLogVm"></div>
                                        </uib-tab>
                                        <uib-tab heading="{{runDetailsVm.tabs.files.heading}}"
                                                 active="runDetailsVm.tabs.files.active"
                                                 select="runDetailsVm.runCaseDetailsTabActivated(runDetailsVm.tabs.files.id)">
                                            <div ng-include="runDetailsVm.tabs.files.template"
                                                 ng-controller="RunCaseFilesController as runCaseFilesVm"></div>
                                        </uib-tab>
                                    </uib-tabset>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>


                <div class="row" id="runScenarioDetailsDiv" style="display: none;">
                    <div class="panel-heading"
                         style="font-size: 16px;font-weight: bold;padding: 5px;">Run Scenario Details
                    </div>
                    <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                        <form class="form-horizontal">
                            <div class="form-group" style="padding-top: 20px;">
                                <label class="col-sm-4 control-label"><span style="color: black">Name</span> :
                                </label>

                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="title"
                                           ng-model="runScenario.name" readonly>

                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label"><span style="color: black">Description</span> :
                                </label>

                                <div class="col-sm-8">
                                   <textarea name="description" rows="4" class="form-control" style="resize: none"
                                             ng-model="runScenario.description" readonly></textarea>
                                </div>


                            </div>
                        </form>
                    </div>
                </div>
                <div class="row" id="runPlanDetailsDiv" style="display: none;">
                    <div class="panel-heading"
                         style="font-size: 16px;font-weight: bold;padding: 5px;">Run Plan Details
                    </div>
                    <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                        <form class="form-horizontal">
                            <div class="form-group" style="padding-top: 20px;">
                                <label class="col-sm-4 control-label"><span style="color: black">Name</span> :
                                </label>

                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="title" ng-model="runPlan.name"
                                           readonly>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label"><span style="color: black">Description</span> :
                                </label>

                                <div class="col-sm-8">
                                    <textarea name="description" class="form-control" rows="4"
                                              style="resize: none" ng-model="runPlan.description" readonly></textarea>
                                </div>
                            </div>


                        </form>
                    </div>
                </div>

                <div class="row" id="runSuiteDetailsDiv" style="display: none;">
                    <div class="panel-heading"
                         style="font-size: 16px;font-weight: bold;padding: 5px;">Run Suite Details
                    </div>
                    <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                        <form class="form-horizontal">
                            <div class="form-group" style="padding-top: 20px;">
                                <label class="col-sm-4 control-label"><span style="color: black">Name</span> :
                                </label>

                                <div class="col-sm-8">
                                    <input type="text" class="form-control" name="title" ng-model="runSuite.name"
                                           readonly>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4 control-label"><span style="color: black">Description</span> :
                                </label>

                                <div class="col-sm-8">
                                    <textarea class="form-control" rows="4" name="title" style="resize: none"
                                              ng-model="runSuite.description" readonly></textarea>
                                </div>
                            </div>


                        </form>
                    </div>
                </div>

                <div id="attributes"
                     ng-if="(runDetailsVm.typeObject.objectType == 'RUNSCENARIO' || runDetailsVm.typeObject.objectType == 'RUNPLAN' || runDetailsVm.typeObject.objectType == 'RUNSUITE') && runDetailsVm.runAttributes.length >0"
                     class="col-md-12 panel panel-primary"
                     style="margin-top: 5%;">
                    <div ng-if="runDetailsVm.typeObject.objectType == 'RUNSCENARIO'" class="panel-heading"
                         style="padding: 10px;color: white;font-size: 22px;">
                        Run Scenario Attributes
                    </div>
                    <div ng-if="runDetailsVm.typeObject.objectType == 'RUNPLAN'" class="panel-heading"
                         style="padding: 10px;color: white;font-size: 22px;">
                        Run Plan Attributes
                    </div>
                    <div ng-if="runDetailsVm.typeObject.objectType == 'RUNSUITE'" class="panel-heading"
                         style="padding: 10px;color: white;font-size: 22px;">
                        Run Suite Attributes
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
                                <tr ng-if="runDetailsVm.runAttributes.length == 0">
                                    <td colspan="7"><span>No Attributes</span></td>
                                </tr>
                                <tr ng-repeat="attribute in runDetailsVm.runAttributes track by $index">
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
                                                   type="number" class="form-control input-sm"
                                                   ng-model="attribute.value.integerValue">
                                            <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'DOUBLE'"
                                                   class="form-control input-sm"
                                                   ng-model="attribute.value.doubleValue">
                                            <input ng-if="attribute.editMode == true && attribute.attributeDef.dataType == 'DATE'"
                                                   date-time-picker
                                                   type="text" class="form-control input-sm"
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
                                           ng-click="runDetailsVm.saveRunProperties(attribute)"
                                           style="font-size:20px;cursor:pointer;"></i>
                                        <i class="fa fa-edit" title="Click to Edit" ng-if="attribute.editMode == false"
                                           ng-click="runDetailsVm.editRunProperties(attribute)"
                                           style="font-size:18px;cursor:pointer;"></i>
                                        <i class="fa fa-times-circle" title="Cancel Changes"
                                           ng-if="attribute.editMode == true && attribute.isNew == false"
                                           ng-click="runDetailsVm.cancelChanges(attribute)"
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
</div>
