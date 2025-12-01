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
                                   ng-model="newActivityTaskVm.newTask.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="newActivityTaskVm.newTask.description">

                            </textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSIGNED_TO</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newActivityTaskVm.newTask.assignedTo" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select">{{$select.selected.firstName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newActivityTaskVm.persons | filter: $select.firstName.search">
                                    <div ng-bind="person.firstName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newActivityTaskVm.taskRequiredProperties"></attributes-view>

                    <attributes-view show-objects="selectObjectValues"
                                     attributes="newActivityTaskVm.taskProperties"></attributes-view>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

