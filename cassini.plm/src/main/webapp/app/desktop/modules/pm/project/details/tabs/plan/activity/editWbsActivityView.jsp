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
                                   ng-disabled="editWbsActivityVm.activity.percentComplete > 0"
                                   ng-model="editWbsActivityVm.newActivity.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="editWbsActivityVm.newActivity.description"></textarea>
                        </div>
                    </div>
                    <div ng-if='editWbsActivityVm.mode == "Edit" || editWbsActivityVm.mode == "New" || editWbsActivityVm.mode == "External"'>
                        <div class="form-group" ng-hide='editWbsActivityVm.mode == "External"'>
                            <label class="col-sm-4 control-label">
                                <span translate>ASSIGNED_TO</span>
                                <span class="asterisk">*</span>: </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="editWbsActivityVm.newActivity.assignedTo" theme="bootstrap"
                                           style="width:100%"
                                           ng-disabled="editWbsActivityVm.activity.status != 'PENDING' && (editWbsActivityVm.mode == 'Edit' || editWbsActivityVm.mode == 'TemplateActivityEdit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.firstName}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="person in editWbsActivityVm.persons | filter: $select.firstName.search">
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
                                    <input type="text" id="plannedStartDate" class="form-control"
                                           placeholder="dd/mm/yyyy"
                                           ng-disabled="editWbsActivityVm.activity.status != 'PENDING' && (editWbsActivityVm.mode == 'Edit' || editWbsActivityVm.mode == 'TemplateActivityEdit')"
                                           ng-model="editWbsActivityVm.newActivity.plannedStartDate" date-picker-edit>
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
                                    <input type="text" id="plannedFinishDate" class="form-control"
                                           placeholder="dd/mm/yyyy"
                                           ng-disabled="editWbsActivityVm.activity.status != 'PENDING' && (editWbsActivityVm.mode == 'Edit' || editWbsActivityVm.mode == 'TemplateActivityEdit')"
                                           ng-model="editWbsActivityVm.newActivity.plannedFinishDate" date-picker-edit>
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ACTUAL_START_DATE</span> :</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="actualStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                       ng-model="editWbsActivityVm.newActivity.actualStartDate" date-picker>
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ACTUAL_FINISH_DATE</span> :</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="actualFinishDate" class="form-control" placeholder="dd/mm/yyyy"
                                       ng-model="editWbsActivityVm.newActivity.actualFinishDate" date-picker>
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>
                    </div>--%>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

