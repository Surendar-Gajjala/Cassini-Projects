<style>

    .ui-select-bootstrap .ui-select-choices-row > span:hover, .ui-select-bootstrap .ui-select-choices-row > span:focus {
        text-decoration: none !important;
        color: #1d2939 !important;
        /*background-color: #2a6fa8   #e6e6e6  #ffffff   #eeeeee #BFBFBF #f5f5f5   #BFBFBF #fff   #1d2939;    ; !important;*/
        background-color: #BFBFBF !important;

    }

</style>


<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Type :<span class="asterisk">*</span> </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newIssueVm.newIssue.type" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Type">{{$select.selected.label}}</ui-select-match>
                                <ui-select-choices
                                        repeat="type in newIssueVm.types | filter: $select.label.search |orderBy: 'label'">
                                    <div ng-bind-html="trustAsHtml((type.label | highlight: $select.label.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Title :<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title" ng-model="newIssueVm.newIssue.title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Priority :<span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newIssueVm.newIssue.priority" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Priority">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="priority in newIssueVm.priorities | filter: $select.search |orderBy: 'priority'">
                                    <div ng-bind-html="trustAsHtml((priority | highlight: $select.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Assigned To : <span class="asterisk">*</span></label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newIssueVm.newIssue.assignedTo" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Person">{{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newIssueVm.persons | filter: $select.fullName.search |orderBy: 'fullName'">
                                    <div ng-bind-html="trustAsHtml((person.fullName | highlight: $select.fullName.search))"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description : </label>

                        <div class="col-sm-8">
                <textarea name="description" rows="5" class="form-control" style="resize: none"
                          ng-model="newIssueVm.newIssue.description"></textarea>
                        </div>

                        <h4 ng-if="newIssueVm.newProblemAttributes.length > 0" class="section-title"
                            style="color: black;">
                            Attributes</h4>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newIssueVm.newProblemAttributes"></attributes-view>

                    </div>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
</div>
