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
                                        <span translate>SELECT</span> <span class="caret"
                                                                            style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <project-management-type-tree
                                                    on-select-type="newReqVm.onSelectType"
                                                    object-type="REQUIREMENTTYPE"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newReqVm.type.name" readonly>

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
                                            ng-click="newReqVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newReqVm.newReqVersion.master.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newReqVm.newReqVersion.master.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newReqVm.newReqVersion.master.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRIORITY</span>:
                        </label>

                        <div class="col-sm-7">
                            <div class="switch-toggle switch-candy">
                                <input id="priorityL" name="priority" type="radio" value="LOW" checked
                                       ng-model="newReqVm.newReqVersion.priority">
                                <label for="priorityL">LOW</label>

                                <input id="priorityM" name="priority" type="radio" value="MEDIUM"
                                       ng-model="newReqVm.newReqVersion.priority">
                                <label for="priorityM">MEDIUM</label>

                                <input id="priorityH" name="priority" type="radio" value="HIGH"
                                       ng-model="newReqVm.newReqVersion.priority">
                                <label for="priorityH">HIGH</label>

                                <input id="priorityC" name="priority" type="radio" value="CRITICAL"
                                       ng-model="newReqVm.newReqVersion.priority">
                                <label for="priorityC">CRITICAL</label>
                                <a></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSIGNED_TO</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newReqVm.newReqVersion.assignedTo" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectPerson}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newReqVm.persons | filter: $select.search">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANNED_FINISH_DATE</span>
                            :</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="plannedStartDate" class="form-control"
                                       placeholder="dd/mm/yyyy"
                                       ng-model="newReqVm.newReqVersion.plannedFinishDate"
                                       start-finish-date-picker>
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WORKFLOW</span>
                            : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newReqVm.newReqVersion.workflowDefinition"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder={{selectWorkflowTitle}}>
                                    {{$select.selected.name}} [ Revision : {{$select.selected.revision}} ]
                                </ui-select-match>
                                <ui-select-choices style="height: 120px"
                                                   repeat="workflow.id as workflow in newReqVm.workflows | filter: $select.search">
                                    <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newReqVm.requirementAttributes.length > 0"
                                     attributes="newReqVm.requirementAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newReqVm.attributes.length > 0"
                                     attributes="newReqVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>