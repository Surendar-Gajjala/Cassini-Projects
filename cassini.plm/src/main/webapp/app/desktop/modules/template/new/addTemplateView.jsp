<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;height: 300px;">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SELECT_TEMPLATE</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="addTemplateVm.selectedTemplate" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{selectTemplate}}">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="template in projectTemplates">
                                    <div ng-bind-html="trustAsHtml((template.name))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>


                    <div class="form-group" ng-if="addTemplateVm.selectedTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_TEAM</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox"
                                   ng-click="addTemplateVm.selectTeam(addTemplateVm.project)"
                                   ng-model="addTemplateVm.project.team">
                        </div>
                    </div>

                    <div class="form-group" ng-if="addTemplateVm.selectedTemplate != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_ASSIGNED_TO</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer"
                             ng-class="{'disabled': addTemplateVm.project.team == false}">
                            <input type="checkbox" ng-model="addTemplateVm.project.assignedTo">
                        </div>
                    </div>

                </form>
                <br>
            </div>
        </div>
    </div>
</div>

