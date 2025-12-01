<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <change-type-tree
                                                    on-select-type="newDcrVm.onSelectType"
                                                    change-type="DCR"></change-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newDcrVm.type.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>DCR_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newDcrVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newDcrVm.newDCR.crNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_DCR_TITLE' | translate}}"
                                   ng-model="newDcrVm.newDCR.title">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION_OF_CHANGE</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="" style="resize: none"
                                      placeholder="{{enterDescriptionofChange}}"
                                      ng-model="newDcrVm.newDCR.descriptionOfChange"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_REASON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcrVm.newDCR.changeReasonType" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="reason in newDcrVm.selectedDcrType.changeReasonTypes.values | filter: $select.search">
                                    <div ng-bind="reason"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_CHANGE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterReasonForChange}}"
                                      ng-model="newDcrVm.newDCR.reasonForChange"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROPOSED_CHANGES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterPraposedChanges}}"
                                      ng-model="newDcrVm.newDCR.proposedChanges"></textarea>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ORIGINATOR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcrVm.newDCR.originatorObject" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices repeat="person in newDcrVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUESTED_BY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcrVm.newDCR.requestedByObject" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices repeat="person in newDcrVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>OWNER</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcrVm.newDCR.dcrOwnerObject" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newDcrVm.changeAnalysts | filter: $select.search | orderBy:'firstName'">
                                    <div ng-bind-html="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>URGENCY</span>
                            <span class="asterisk">*</span>:</label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcrVm.newDCR.urgency" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices repeat="urgency in newDcrVm.urgencys | filter: $select.search">
                                    <div ng-bind="urgency"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newDcrVm.newDCR.workflow"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newDcrVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newDcrVm.dcrRequiredProperties.length > 0"
                                     attributes="newDcrVm.dcrRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newDcrVm.dcrProperties.length > 0"
                                     attributes="newDcrVm.dcrProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newDcrVm.attributes.length > 0"
                                     attributes="newDcrVm.attributes"></attributes-view>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>
