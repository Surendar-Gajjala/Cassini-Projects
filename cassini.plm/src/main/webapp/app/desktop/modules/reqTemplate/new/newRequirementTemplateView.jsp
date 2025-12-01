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
                                                    on-select-type="newReqTemplateVm.onSelectType"
                                                    object-type="REQUIREMENTTYPE"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newReqTemplateVm.type.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newReqTemplateVm.newReqTemplate.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newReqTemplateVm.newReqTemplate.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PRIORITY</span>:
                        </label>

                        <div class="col-sm-7">
                            <div class="switch-toggle switch-candy">
                                <input id="priorityL" name="priority" type="radio" value="LOW" checked
                                       ng-model="newReqTemplateVm.newReqTemplate.priority">
                                <label for="priorityL">LOW</label>

                                <input id="priorityM" name="priority" type="radio" value="MEDIUM"
                                       ng-model="newReqTemplateVm.newReqTemplate.priority">
                                <label for="priorityM">MEDIUM</label>

                                <input id="priorityH" name="priority" type="radio" value="HIGH"
                                       ng-model="newReqTemplateVm.newReqTemplate.priority">
                                <label for="priorityH">HIGH</label>

                                <input id="priorityC" name="priority" type="radio" value="CRITICAL"
                                       ng-model="newReqTemplateVm.newReqTemplate.priority">
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
                            <ui-select ng-model="newReqTemplateVm.newReqTemplate.assignedTo" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectPerson}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person.id as person in newReqTemplateVm.persons | filter: $select.search">
                                    <div>{{person.fullName}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>