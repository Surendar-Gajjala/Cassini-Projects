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
                                   ng-model="editWbsMilestoneVm.wbsMileStone.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                <textarea name="description" rows="3" class="form-control" style="resize: none"
                          ng-model="editWbsMilestoneVm.wbsMileStone.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group" ng-if='editWbsMilestoneVm.mode == "Edit"'>
                        <label class="col-sm-4 control-label">
                            <span translate>ASSIGNED_TO</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="editWbsMilestoneVm.wbsMileStone.assignedTo" theme="bootstrap"
                                       style="width:100%"
                                       ng-disabled="editWbsMilestoneVm.wbsMileStone.status != 'PENDING'">
                                <ui-select-match placeholder="Select">{{$select.selected.firstName}}</ui-select-match>
                                <ui-select-choices
                                        repeat="person in editWbsMilestoneVm.persons | filter: $select.firstName.search">
                                    <div ng-bind="person.firstName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group" ng-if='editWbsMilestoneVm.mode == "Edit"'>
                        <label class="col-sm-4 control-label">
                            <span translate>PLANNED_FINISH_DATE</span>
                            <span class="asterisk">*</span> :</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="actualStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                       ng-disabled="editWbsMilestoneVm.wbsMileStone.status != 'PENDING'"
                                       ng-model="editWbsMilestoneVm.wbsMileStone.plannedFinishDate" date-picker-edit>
                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                            </div>
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ACTUAL_FINISH_DATE</span> :</label>

                        <div class="col-sm-7">
                            <div class="input-group">
                                <input type="text" id="actualFinishDate" class="form-control" placeholder="dd/mm/yyyy"
                                       ng-model="editWbsMilestoneVm.wbsMileStone.actualFinishDate" date-picker>
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

