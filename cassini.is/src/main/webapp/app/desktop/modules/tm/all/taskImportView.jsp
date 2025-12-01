<style>
    #rightSidePanelContent {
        overflow: hidden !important;
    }
</style>
<div style="position: relative;">
    <div style="padding: 20px; height: 500px;">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Project :<span class="asterisk">*</span> </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="taskImportVm.projectClone.project" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Project">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="project in taskImportVm.projects | filter: $select.name.search">
                                    <div ng-bind-html="trustAsHtml((project.name | highlight: $select.name.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

