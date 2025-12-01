<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-disabled="newWbsActivityVm.activity.percentComplete > 0"
                                   ng-model="newWbsActivityVm.newActivity.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="newWbsActivityVm.newActivity.description"></textarea>
                        </div>
                    </div>
                    <div ng-if='newWbsActivityVm.mode == "Edit" || newWbsActivityVm.mode == "New"'>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>ASSIGNED_TO</span>
                                <span class="asterisk">*</span>: </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="newWbsActivityVm.newActivity.assignedTo" theme="bootstrap"
                                           style="width:100%"
                                           ng-disabled="newWbsActivityVm.activity.status != 'PENDING' && (newWbsActivityVm.mode == 'Edit' || newWbsActivityVm.mode == 'TemplateActivityEdit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.firstName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="person in newWbsActivityVm.persons | filter: $select.firstName.search">
                                        <div ng-bind="person.firstName"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>PLANNED_START_DATE</span>
                                <span class="asterisk">*</span>:</label>

                            <div class="col-sm-7">
                                <div class="input-group">
                                    <input type="text" id="plannedStartDate" class="form-control" autocomplete="off"
                                           placeholder="dd/mm/yyyy"
                                           ng-disabled="newWbsActivityVm.activity.status != 'PENDING' && (newWbsActivityVm.mode == 'Edit' || newWbsActivityVm.mode == 'TemplateActivityEdit')"
                                           ng-model="newWbsActivityVm.newActivity.plannedStartDate" date-picker-edit>
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>PLANNED_FINISH_DATE</span>
                                <span class="asterisk">*</span>:</label>

                            <div class="col-sm-7">
                                <div class="input-group">
                                    <input type="text" id="plannedFinishDate" class="form-control" autocomplete="off"
                                           placeholder="dd/mm/yyyy"
                                           ng-disabled="newWbsActivityVm.activity.status != 'PENDING' && (newWbsActivityVm.mode == 'Edit' || newWbsActivityVm.mode == 'TemplateActivityEdit')"
                                           ng-model="newWbsActivityVm.newActivity.plannedFinishDate"
                                           date-picker-edit>
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                </div>
                            </div>
                        </div>

                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newWbsActivityVm.activityRequiredProperties"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newWbsActivityVm.activityProperties"></attributes-view>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

