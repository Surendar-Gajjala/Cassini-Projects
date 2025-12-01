<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding-right: 38px">
        <div class="row" style="margin-right: 48px;height: 500px">
            <br>

            <form class="form-horizontal">

                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>NAME</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newWbsMilestoneVm.wbsMileStone.name">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">
                        <span translate>DESCRIPTION</span>: </label>

                    <div class="col-sm-7">
                <textarea name="description" rows="3" class="form-control" style="resize: none"
                          ng-model="newWbsMilestoneVm.wbsMileStone.description"></textarea>
                    </div>
                </div>
                <div class="form-group" ng-if='newWbsMilestoneVm.mode == "Edit" || newWbsMilestoneVm.mode == "New"'>
                    <label class="col-sm-4 control-label">
                        <span translate>ASSIGNED_TO</span>
                        <span class="asterisk">*</span>: </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="newWbsMilestoneVm.wbsMileStone.assignedTo" theme="bootstrap"
                                   style="width:100%"
                                   ng-disabled="newWbsMilestoneVm.wbsMileStone.status != 'PENDING'">
                            <ui-select-match placeholder="Select">{{$select.selected.firstName}}</ui-select-match>
                            <ui-select-choices
                                    repeat="person in newWbsMilestoneVm.persons | filter: $select.firstName.search">
                                <div ng-bind="person.firstName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <div class="form-group" ng-if='newWbsMilestoneVm.mode == "Edit" || newWbsMilestoneVm.mode == "New"'>
                    <label class="col-sm-4 control-label">
                        <span translate>PLANNED_FINISH_DATE</span>
                        <span class="asterisk">*</span> :</label>

                    <div class="col-sm-7">
                        <div class="input-group">
                            <input type="text" id="actualStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                   autocomplete="off"
                                   ng-disabled="newWbsMilestoneVm.wbsMileStone.status != 'PENDING'"
                                   ng-model="newWbsMilestoneVm.wbsMileStone.plannedFinishDate"
                                   start-finish-date-picker>
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
                                   ng-model="newWbsMilestoneVm.wbsMileStone.actualFinishDate" date-picker>
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                        </div>
                    </div>
                </div>--%>
            </form>
            <br>
        </div>
    </div>
</div>

