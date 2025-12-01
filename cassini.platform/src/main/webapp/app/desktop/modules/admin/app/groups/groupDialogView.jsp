<div class="modal-header">

    <h3>New Group</h3>
</div>

<div class="alert alert-danger" ng-hide="groupDialogVm.valid" ng-cloak>
    {{ groupDialogVm.error }}
</div>

<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <form>
        <div class="row">
            <div class="col-md-8">
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" class="form-control" id="name" placeholder="Name" ng-model="groupDialogVm.personGroup.name">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-8">
                <div class="form-group">
                    <label for="description">Description:</label>
                    <input type="text" class="form-control" id="description" placeholder="Description" ng-model="groupDialogVm.personGroup.description">
                </div>
            </div>
        </div>
    </form>
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-md-8">
        </div>
        <div class="modal-buttons" class="col-md-8">
            <button type="button" class="btn btn-sm btn-default"  ng-click="groupDialogVm.cancel()">Cancel</button>

            <button type="button" class="btn btn-sm btn-success"  ng-click="groupDialogVm.create()">Create</button>

        </div>
    </div>
</div>