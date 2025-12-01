<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

    </style>
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;padding-bottom: 150px;">
            <div>
                <h4 class="section-title" style="" translate>BASIC_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NCR_TYPE</span>
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
                                            <quality-type-tree on-select-type="newNcrVm.onSelectType"
                                                               quality-type="NCRTYPE"></quality-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newNcrVm.newNcr.ncrType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NCR_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newNcrVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newNcrVm.newNcr.ncrNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NCR_TITLE' | translate}}"
                                   ng-model="newNcrVm.newNcr.title"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>INSPECTION</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newNcrVm.newNcr.inspection" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectInspectionTitle}}">
                                    {{$select.selected.inspectionPlan}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="inspection.id as inspection in newNcrVm.inspections | filter: $select.search">
                                    <div ng-bind="inspection.inspectionPlan"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newNcrVm.newNcr.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>QUALITY_ANALYST</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newNcrVm.newNcr.qualityAnalyst" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectQualityAdminTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newNcrVm.qualityAnalysts | filter: $select.search">
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
                            <ui-select ng-model="newNcrVm.newNcr.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflowTitle}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newNcrVm.workflows | filter: $select.search">
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
                            <ui-select ng-model="newNcrVm.newNcr.failureType" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newNcrVm.newNcr.ncrType == null">
                                <ui-select-match placeholder="{{selectDefectTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newNcrVm.newNcr.ncrType.failureTypes.values | filter: $select.search">
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
                            <ui-select ng-model="newNcrVm.newNcr.severity" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newNcrVm.newNcr.ncrType == null">
                                <ui-select-match placeholder="{{selectSeverityTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newNcrVm.newNcr.ncrType.severities.values | filter: $select.search">
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
                            <ui-select ng-model="newNcrVm.newNcr.disposition" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newNcrVm.newNcr.ncrType == null">
                                <ui-select-match placeholder="{{selectDispositionTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newNcrVm.newNcr.ncrType.dispositions.values | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newNcrVm.ncrRequiredProperties.length > 0"
                                     attributes="newNcrVm.ncrRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newNcrVm.ncrProperties.length > 0"
                                     attributes="newNcrVm.ncrProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newNcrVm.attributes.length > 0"
                                     attributes="newNcrVm.attributes"></attributes-view>
                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
