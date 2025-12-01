<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name <span class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="type" placeholder="Enter Contractor Name"
                                   ng-model="newContractorVm.newContractor.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Contact Person:<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <ui-select class="required-field" ng-model="newContractorVm.newContractor.contact"
                                       theme="bootstrap">
                                <ui-select-match placeholder="Select Person">{{$select.selected.person.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="login in newContractorVm.logins | filter: $select.search |orderBy: 'fullName'">
                                    <div ng-bind="login.person.fullName | highlight: $select.person.fullName.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newContractorVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newContractorVm.attributes"></attributes-view>
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

