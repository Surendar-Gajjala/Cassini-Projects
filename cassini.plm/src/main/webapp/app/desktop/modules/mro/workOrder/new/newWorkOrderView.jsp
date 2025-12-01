<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;"
                                             ng-if="newWorkOrderVm.workOrderMode == 'WORKORDER'">
                                            <manufacturing-type-tree
                                                    on-select-type="newWorkOrderVm.onSelectType"
                                                    object-type="WORKORDERTYPE"></manufacturing-type-tree>
                                        </div>
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;"
                                             ng-if="newWorkOrderVm.workOrderMode == 'WORKREQUEST'">
                                            <manufacturing-type-tree
                                                    on-select-type="newWorkOrderVm.onSelectType"
                                                    object-type="REPAIRWORKORDERTYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newWorkOrderVm.newWorkOrder.type.name" readonly>

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
                                            ng-click="newWorkOrderVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newWorkOrderVm.newWorkOrder.number">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSET</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkOrderVm.newWorkOrder.asset" theme="bootstrap"
                                       style="width:100%" on-select="newWorkOrderVm.onSelectAsset()"
                                       ng-if="newWorkOrderVm.workOrderMode == 'WORKORDER'">
                                <ui-select-match placeholder="{{selectAsset}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="asset.id as asset in newWorkOrderVm.assets | filter: $select.search">
                                    <div>{{asset.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                            <input type="text" class="form-control" name="title"
                                   ng-if="newWorkOrderVm.workOrderMode == 'WORKREQUEST'"
                                   ng-model="newWorkOrderVm.asset.name" readonly>
                        </div>
                    </div>
                    <div class="form-group" ng-if="newWorkOrderVm.newWorkOrder.type.type == 'REPAIR'">
                        <label class="col-sm-4 control-label">
                            <span translate>WORK_REQUEST</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkOrderVm.newWorkOrder.request" theme="bootstrap"
                                       style="width:100%" ng-if="newWorkOrderVm.workOrderMode == 'WORKORDER'">
                                <ui-select-match placeholder="{{'SELECT_WORK_REQUEST' | translate}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workRequest.id as workRequest in newWorkOrderVm.workRequests | filter: $select.search">
                                    <div>{{workRequest.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                            <input type="text" class="form-control" name="title"
                                   ng-if="newWorkOrderVm.workOrderMode == 'WORKREQUEST'"
                                   ng-model="newWorkOrderVm.workRequest.name" readonly>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-if="newWorkOrderVm.newWorkOrder.type.type == 'MAINTENANCE' && newWorkOrderVm.newWorkOrder.asset != null">
                        <label class="col-sm-4 control-label">
                            <span translate>MAINTENANCE_PLAN</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkOrderVm.newWorkOrder.plan" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{'SELECT_MAINTENANCE_PLAN' | translate}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="maintenancePlan.id as maintenancePlan in newWorkOrderVm.maintenancePlans | filter: $select.search">
                                    <div>{{maintenancePlan.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newWorkOrderVm.newWorkOrder.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newWorkOrderVm.newWorkOrder.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSIGNED_TO</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkOrderVm.newWorkOrder.assignedTo" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectAssignedTo}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newWorkOrderVm.persons | filter: $select.search | orderBy:'fullName'">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRIORITY</span> : </label>

                        <div class="col-sm-7">
                            <div class="switch-toggle switch-candy">
                                <input id="priorityL" name="priority" type="radio" value="LOW" checked
                                       ng-model="newWorkOrderVm.newWorkOrder.priority">
                                <label for="priorityL">LOW</label>

                                <input id="priorityM" name="priority" type="radio" value="MEDIUM"
                                       ng-model="newWorkOrderVm.newWorkOrder.priority">
                                <label for="priorityM">MEDIUM</label>

                                <input id="priorityH" name="priority" type="radio" value="HIGH"
                                       ng-model="newWorkOrderVm.newWorkOrder.priority">
                                <label for="priorityH">HIGH</label>

                                <input id="priorityC" name="priority" type="radio" value="CRITICAL"
                                       ng-model="newWorkOrderVm.newWorkOrder.priority">
                                <label for="priorityC">CRITICAL</label>
                                <a></a>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_NOTES' | translate}}"
                                      ng-model="newWorkOrderVm.newWorkOrder.notes"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newWorkOrderVm.newWorkOrder.workflow" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectWorkflow}}">{{$select.selected.name}} [
                                    Revision :
                                    {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="workflow.id as workflow in newWorkOrderVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues" ng-if="newWorkOrderVm.attributes.length > 0"
                                     attributes="newWorkOrderVm.attributes"></attributes-view>

                </form>
            </div>
        </div>
    </div>
</div>
