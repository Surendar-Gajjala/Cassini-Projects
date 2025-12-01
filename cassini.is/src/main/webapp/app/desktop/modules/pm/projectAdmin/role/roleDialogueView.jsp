<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
                       ng-show="roleDialogueVm.errorMessage.name != null" ng-cloak>
                        {{ roleDialogueVm.errorMessage.name}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Name <span class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="type"
                                   ng-model="roleDialogueVm.projectRole.role">
                        </div>
                    </div>
                    <p title=" {{ roleDialogueVm.errorMessage.description}}" class="text-danger text-center"
                       style="margin-top: 15px; margin-bottom: 0px"
                       ng-show="roleDialogueVm.errorMessage.description != null" ng-cloak>
                        {{roleDialogueVm.errorMessage | limitTo: 15}}{{roleDialogueVm.errorMessage.length > 15 ? '...' :
                        ''}}
                    </p>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description : </label>

                        <div class="col-sm-8">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  ng-model="roleDialogueVm.projectRole.description"></textarea>
                        </div>
                    </div>
                </form>
                <br>
                <br>
            </div>
        </div>
    </div>
    <div id="appSidePanelButtonsPanel" class='buttons-panel' style="display: none">
        <button class="btn btn-sm btn-success pull-right"
                ng-click="roleDialogueVm.create()">Create
        </button>
    </div>
</div>

