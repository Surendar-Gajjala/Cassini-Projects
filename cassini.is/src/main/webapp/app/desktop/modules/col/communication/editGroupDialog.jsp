<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label">Name: <span class="asterisk">*</span></label>

            <div class="col-sm-8">
                <input type="text" class="form-control" name="type" ng-model="editDialogVm.name">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">Description: <span class="asterisk">*</span></label>

            <div class="col-sm-8">
           <textarea name="description" rows="10" class="form-control" style="resize: none"
                     ng-model="editDialogVm.description"></textarea>
            </div>
        </div>
        <div class="form-group" ng-show="editDialogVm.mode == 'EDIT'">
            <label class="col-sm-3 control-label">Users: </label>

            <div class="col-sm-8">
                <ui-select multiple ng-model="editDialogVm.selectedUsers" theme="bootstrap" style="width:100%"
                           on-remove="editDialogVm.removeUser($item)"
                           on-select="editDialogVm.selectUser($item)">
                    <ui-select-match placeholder="Select Group Members">{{$item.fullName}}</ui-select-match>
                    <ui-select-choices repeat="user in editDialogVm.userList | filter: $select.search">
                        <div ng-bind="user.fullName"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
        </div>
    </form>
</div>
<div class="modal-footer">
    <div id="appSidePanelButtonsPanel" class='buttons-panel' style="display: none">
        <button class="btn btn-sm btn-success pull-right"
                ng-click="editDialogVm.create()">create
        </button>
    </div>

</div>
