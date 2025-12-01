<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Contractor:<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <ui-select class="required-field" ng-model="newWorkOrderVm.newWorkOrder.contractor"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Contractor">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="contractor in newWorkOrderVm.contractors | filter: $select.search |orderBy: 'name'">
                                    <div ng-bind="contractor.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Project:<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <ui-select class="required-field" ng-model="newWorkOrderVm.newWorkOrder.project"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="project in newWorkOrderVm.projects | filter: $select.search |orderBy: 'name'">
                                    <div ng-bind="project.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newWorkOrderVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newWorkOrderVm.attributes"></attributes-view>
                            <br>
                            <br>
                        </form>
                    </div>

                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>

