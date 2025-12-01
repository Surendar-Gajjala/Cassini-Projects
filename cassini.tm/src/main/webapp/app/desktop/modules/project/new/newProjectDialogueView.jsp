<div class="view-container">
    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">New Project</h3>
    </div>

    <div class="modal-body" style="padding: 20px; max-height: 500px;">
        <form class="form-horizontal">
            <div ng-show="!newProjectDialogueVm.valid">
                <span style="margin-left: 150px" class="text-danger text-center">{{newProjectDialogueVm.nameError}}</span>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Name: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" name="Name" placeholder="Name"
                           ng-model="newProjectDialogueVm.newProject.name">
                </div>
            </div>


            <div ng-show="!newProjectDialogueVm.valid">
                <span style="margin-left: 150px" class="text-danger text-center">{{newProjectDialogueVm.descriptionError}}</span>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">Description: </label>

                <div class="col-sm-7">
                     <textarea rows="4" cols="50" class="form-control" name="Description" placeholder="Description"
                               ng-model="newProjectDialogueVm.newProject.description"
                               style="resize: none;">
                    </textarea>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" ng-click="newProjectDialogueVm.cancel()">Cancel</button>
            <button type="button" class="btn btn-sm btn-success" ng-click="newProjectDialogueVm.create()">Create</button>
        </div>
    </div>
</div>
