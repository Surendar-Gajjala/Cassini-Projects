<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 200px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUESTER_TYPE</span>: </label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">

                            <div class="switch-toggle switch-candy">
                                <input id="internal" name="view" type="radio" checked
                                       ng-click="newEcrVm.selectRequestedType('internal', $event)">
                                <label for="internal" onclick="" translate="">INTERNAL</label>
                                <input id="customer" name="view" type="radio"
                                       ng-click="newEcrVm.selectRequestedType('customer', $event)">
                                <label for="customer" onclick="" translate>CUSTOMER</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REQUESTED_BY</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcrVm.newEcr.requestedBy" theme="bootstrap"
                                       style="width:100%" ng-if="newEcrVm.newEcr.requesterType == 'INTERNAL'">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newEcrVm.persons | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                            <ui-select ng-if="newEcrVm.newEcr.requesterType == 'CUSTOMER'"
                                       ng-model="newEcrVm.newEcr.requestedBy" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectCustomerTitle}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="customer.id as customer in newEcrVm.customers | filter: $select.search">
                                    <div ng-bind="customer.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newEcrVm.newEcr.requestedBy == 0">

                        <label class="col-sm-4 control-label">
                            <span translate>OTHER_REQUESTED</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{enterOtherRequested}}"
                                   ng-model="newEcrVm.newEcr.otherRequested">
                        </div>
                    </div>

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
                                                    on-select-type="newEcrVm.onSelectType"
                                                    change-type="ECR"></change-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newEcrVm.selectedEcrType.name" readonly>


                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>ECR_NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newEcrVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newEcrVm.newEcr.crNumber">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>QCR</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcrVm.newEcr.qcr" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.title}}</ui-select-match>
                                <ui-select-choices
                                        repeat="qcr in newEcrVm.qcrs | filter: $select.search">
                                    <div ng-bind="qcr.title"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_ECR_TITLE' | translate}}" ng-model="newEcrVm.newEcr.title">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION_OF_CHANGE</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterDescriptionofChange}}"
                                      ng-model="newEcrVm.newEcr.descriptionOfChange"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_REASON</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcrVm.newEcr.changeReasonType" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="reason in newEcrVm.selectedEcrType.changeReasonTypes.values | filter: $select.search">
                                    <div ng-bind="reason"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>REASON_FOR_CHANGE</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterReasonForChange}}"
                                      ng-model="newEcrVm.newEcr.reasonForChange"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROPOSED_CHANGES</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{enterPraposedChanges}}"
                                      ng-model="newEcrVm.newEcr.proposedChanges"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>IMPACT_ANALYSIS</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_IMPACT_ANALYSIS' | translate}}"
                                      ng-model="newEcrVm.newEcr.impactAnalysis"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ORIGINATOR</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcrVm.newEcr.originator" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newEcrVm.originators | filter: $select.search">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CHANGE_ANALYST</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcrVm.newEcr.changeAnalyst" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newEcrVm.changeAnalysts | filter: $select.search | orderBy:'firstName'">
                                    <div ng-bind-html="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>URGENCY</span>
                            <span class="asterisk">*</span> :</label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newEcrVm.newEcr.urgency" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{select}}">{{$select.selected}}</ui-select-match>
                                <ui-select-choices repeat="urgency in newEcrVm.urgencys | filter: $select.search">
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
                            <ui-select ng-model="newEcrVm.newEcr.workflow" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflowTitle}}">
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newEcrVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newEcrVm.ecrProperties.length > 0"
                                     attributes="newEcrVm.ecrProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newEcrVm.attributes.length > 0"
                                     attributes="newEcrVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
