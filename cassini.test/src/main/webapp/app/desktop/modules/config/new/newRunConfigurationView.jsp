<div style="position: relative;">
    <div style="padding: 20px;">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span>Name</span>
                    <span class="asterisk">*</span> : </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" name="title"
                           ng-model="newRunConfigurationVm.newRunConfig.name">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span>Description</span><span class="asterisk"> *</span> : </label>

                <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newRunConfigurationVm.newRunConfig.description"></textarea>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span>Scenario</span><span class="asterisk"> *</span> : </label>

                <div class="col-sm-7">
                    <ui-select ng-model="newRunConfigurationVm.newRunConfig.scenario"
                               theme="bootstrap"
                               style="width:100%;">
                        <ui-select-match placeholder="Select Scenario">{{$select.selected.name}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="scenario in newRunConfigurationVm.scenarios">
                            <div ng-bind="scenario.name"></div>
                        </ui-select-choices>
                    </ui-select>
                </div>
            </div>
        </form>
    </div>
</div>
