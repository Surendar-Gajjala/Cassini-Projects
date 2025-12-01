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
                        <label class="col-sm-4 control-label">Portfolio :<span class="asterisk">*</span> </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProjectCloneVm.projectClone.portfolio" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Portfolio">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="portfolio in newProjectCloneVm.portfolios | filter: $select.name.search">
                                    <div ng-bind-html="trustAsHtml((portfolio.name | highlight: $select.name.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newProjectCloneVm.projectClone.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="5" class="form-control" style="resize: none"
                                      ng-model="newProjectCloneVm.projectClone.description">
                            </textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Planned Start Date:<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedStartDate" class="form-control" placeholder="dd/mm/yyyy"
                                   ng-model="newProjectCloneVm.projectClone.plannedStartDate" start-finish-date-picker>
                        </div>


                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Planned Finish Date:<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" id="plannedFinishDate" class="form-control" placeholder="dd/mm/yyyy"
                                   ng-model="newProjectCloneVm.projectClone.plannedFinishDate" start-finish-date-picker>
                        </div>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

