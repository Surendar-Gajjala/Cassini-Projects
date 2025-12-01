<div>
    <style scoped>
        .open > .dropdown-toggle.btn {

        }
    </style>

    <div style="position: relative;" class="req">
        <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
            <div class="row" style="margin: 0;">
                <div>
                    <br>

                    <form class="form-horizontal">


                        <div class=form-group>
                            <label class="col-sm-4 control-label">
                                <span translate>REQUIREMENT_TYPE</span>
                                <span class="asterisk">*</span>:
                            </label>

                            <div class="col-sm-7" style="padding-left: 5px;padding-right: 0;">
                                <div class="input-group mb15">
                                    <div class="input-group-btn" uib-dropdown>
                                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                type="button">
                                            <span translate>SELECT</span> <span class="caret"
                                                                                style="margin-left: 4px;"></span>
                                        </button>
                                        <div class="dropdown-menu" role="menu">
                                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                <requirement-tree
                                                        on-select-type="newReqVm.onSelectType"></requirement-tree>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="newReqVm.newSpecRequirement.requirement.type.name" readonly>

                                </div>
                            </div>
                        </div>
                        <div class=form-group>
                            <label class="col-sm-4 control-label">
                                <span translate>NAME</span>
                                <span class="asterisk">*</span>:
                            </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       placeholder="{{'ENTER_REQUIREMENT_NAME' | translate}}"
                                       ng-model="newReqVm.newSpecRequirement.requirement.name">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>DESCRIPTION</span>
                                <span class="asterisk">*</span>:
                            </label>

                            <div class="col-sm-7">
                                <%-- <textarea class="form-control" rows="6" style="resize: none"
                                           ng-model="newReqVm.newSpecRequirement.requirement.description"></textarea>--%>

                                <summernote
                                        ng-model="newReqVm.newSpecRequirement.requirement.description"></summernote>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>ASSIGNED_TO</span>
                                : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="newReqVm.newSpecRequirement.requirement.assignedTo"
                                           theme="bootstrap"
                                           style="width:100%">
                                    <ui-select-match placeholder="Select">{{$select.selected.firstName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="person in newReqVm.persons | filter: $select.firstName.search">
                                        <div ng-bind="person.fullName"></div>
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
                                           ng-model="newReqVm.newSpecRequirement.requirement.plannedFinishDate"
                                           date-picker-edit>
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>WORKFLOW</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="newReqVm.newSpecRequirement.requirement.workflowDefinition"
                                           theme="bootstrap"
                                           style="width:100%">
                                    <ui-select-match placeholder=Select>{{$select.selected.name}} [ Revision :
                                        {{$select.selected.revision}} ]
                                    </ui-select-match>
                                    <ui-select-choices repeat="workflow in newReqVm.workflows | filter: $select.search">
                                        <div>{{workflow.name}} [ Revision : {{workflow.revision}} ]</div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newReqVm.requirementRequiredProperties"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newReqVm.requirementProperties"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         ng-if="newReqVm.newSpecRequirement.requirement.type != null"
                                         attributes="newReqVm.requiredAttributes"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         ng-if="newReqVm.newSpecRequirement.requirement.type != null"
                                         attributes="newReqVm.attributes"></attributes-view>
                    </form>

                </div>
                <br><br>
            </div>
        </div>
    </div>
</div>
