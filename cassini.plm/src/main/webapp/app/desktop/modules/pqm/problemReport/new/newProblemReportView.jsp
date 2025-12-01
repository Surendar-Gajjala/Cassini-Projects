<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REPORTER_TYPE</span>: </label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">

                            <div class="switch-toggle switch-candy">
                                <input id="prTypeC" name="view" type="radio" checked
                                       ng-click="newProblemReportVm.selectReporterType('customer', $event)">
                                <label for="prTypeC" onclick="" translate>CUSTOMER</label>

                                <input id="prTypeI" name="view" type="radio"
                                       ng-click="newProblemReportVm.selectReporterType('internal', $event)">
                                <label for="prTypeI" onclick="" translate="">INTERNAL</label>
                                <input id="prTypeS" name="view" type="radio"
                                       ng-click="newProblemReportVm.selectReporterType('supplier', $event)">
                                <label for="prTypeS" onclick="" translate="">SUPPLIER</label>
                                <a href=""></a>
                            </div>
                            <%--
                            <div class="form-check"
                                 style="border: 1px solid #ddd;padding:8px 8px 3px 8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-left: 30px; margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="reporterTypes" id="reporterType1"
                                           ng-click="newProblemReportVm.selectReporterType('customer', $event)"
                                           checked><span style="padding: 2px;margin-left: 5px;"
                                                         translate>CUSTOMER</span>
                                </label>
                                <label class="form-check-label" style="margin-left: 30px; margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="reporterTypes" id="reporterType3"
                                           ng-click="newProblemReportVm.selectReporterType('internal', $event)"><span
                                        style="padding: 2px;margin-left: 5px;" translate>INTERNAL</span>
                                </label>
                            </div>
                            --%>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REPORTED_BY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-if="newProblemReportVm.newProblemReport.reporterType == 'CUSTOMER'"
                                       ng-model="newProblemReportVm.newProblemReport.reportedBy" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectCustomerTitle}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="customer.id as customer in newProblemReportVm.customers | filter: $select.search">
                                    <div ng-bind="customer.name"></div>
                                </ui-select-choices>
                            </ui-select>

                            <ui-select ng-if="newProblemReportVm.newProblemReport.reporterType == 'SUPPLIER'"
                                       ng-model="newProblemReportVm.newProblemReport.reportedBy" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Supplier">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="supplier.id as supplier in newProblemReportVm.suppliers | filter: $select.search">
                                    <div ng-bind="supplier.name"></div>
                                </ui-select-choices>
                            </ui-select>

                            <ui-select ng-if="newProblemReportVm.newProblemReport.reporterType == 'INTERNAL'"
                                       ng-model="newProblemReportVm.newProblemReport.reportedBy" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectPersonTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newProblemReportVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newProblemReportVm.newProblemReport.reportedBy == 0">

                        <label class="col-sm-4 control-label">
                            <span translate>OTHER_REPORTED</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{enterOtherReported}}"
                                   ng-model="newProblemReportVm.newProblemReport.otherReported">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROBLEM_REPORT_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <quality-type-tree on-select-type="newProblemReportVm.onSelectType"
                                                               quality-type="PROBLEMREPORTTYPE"></quality-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newProblemReportVm.newProblemReport.prType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newProblemReportVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newProblemReportVm.newProblemReport.prNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRODUCT</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.product" theme="bootstrap"
                                       style="width:100%" on-select="newProblemReportVm.onSelectProduct($item)"
                                       ng-disabled="newProblemReportVm.newProblemReport.prType == null">
                                <ui-select-match placeholder="{{selectProductTitle}}">
                                    {{$select.selected.itemName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="item.latestReleasedRevision as item in newProblemReportVm.productItems | filter: $select.search">
                                    <div>{{item.itemName}} - {{item.latestReleasedRevisionObject.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>INSPECTION</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.inspection" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newProblemReportVm.newProblemReport.product == null">
                                <ui-select-match placeholder="{{selectInspectionTitle}}">
                                    {{$select.selected.inspectionNumber}} - {{$select.selected.inspectionPlan}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="inspection.id as inspection in newProblemReportVm.inspections | filter: $select.search">
                                    <div>{{inspection.inspectionNumber}} - {{inspection.inspectionPlan}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROBLEM</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea type="text" class="form-control" name="title" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_PROBLEM_PLACEHOLDER' | translate}}"
                                      ng-model="newProblemReportVm.newProblemReport.problem"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newProblemReportVm.newProblemReport.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>STEPS_TO_REPRODUCE</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_STEPS_TO_REPRODUCE' | translate}}"
                                      ng-model="newProblemReportVm.newProblemReport.stepsToReproduce"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>QUALITY_ANALYST</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.qualityAnalyst"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectQualityAnalystTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newProblemReportVm.qualityAnalysts | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newProblemReportVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DEFECT_TYPE</span>

                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.failureType" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newProblemReportVm.newProblemReport.prType == null">
                                <ui-select-match placeholder="{{selectDefectTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newProblemReportVm.newProblemReport.prType.failureTypes.values | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SEVERITY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.severity" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newProblemReportVm.newProblemReport.prType == null">
                                <ui-select-match placeholder="{{selectSeverityTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newProblemReportVm.newProblemReport.prType.severities.values | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DISPOSITION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProblemReportVm.newProblemReport.disposition" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newProblemReportVm.newProblemReport.prType == null">
                                <ui-select-match placeholder="{{selectDispositionTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newProblemReportVm.newProblemReport.prType.dispositions.values | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newProblemReportVm.prRequiredProperties.length > 0"
                                     attributes="newProblemReportVm.prRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newProblemReportVm.prProperties.length > 0"
                                     attributes="newProblemReportVm.prProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newProblemReportVm.attributes.length > 0"
                                     attributes="newProblemReportVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
