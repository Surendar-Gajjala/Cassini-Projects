<div style="position: relative;padding-bottom: 150px;">
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
                            <span translate>QCR_TYPE</span>
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
                                            <quality-type-tree on-select-type="newQcrVm.onSelectType"
                                                               quality-type="QCRTYPE"></quality-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newQcrVm.newQcr.qcrType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>QCR_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newQcrVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newQcrVm.newQcr.qcrNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>QCR_FOR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newQcrVm.newQcr.qcrFor" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="newQcrVm.newQcr.qcrType == null">
                                <ui-select-match placeholder="{{selectQCRForTitle}}">
                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in newQcrVm.qcrForTypes | filter: $select.search">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_QCR_TITLE' | translate}}"
                                   ng-model="newQcrVm.newQcr.title"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newQcrVm.newQcr.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>QUALITY_ADMINISTRATOR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newQcrVm.newQcr.qualityAdministrator" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectQualityAdminTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newQcrVm.qualityAnalysts | filter: $select.search | orderBy:'fullName'">
                                    <div ng-bind-html="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newQcrVm.newQcr.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflowTitle}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newQcrVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newQcrVm.qcrRequiredProperties.length > 0"
                                     attributes="newQcrVm.qcrRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newQcrVm.qcrProperties.length > 0"
                                     attributes="newQcrVm.qcrProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newQcrVm.attributes.length > 0"
                                     attributes="newQcrVm.attributes"></attributes-view>
                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
