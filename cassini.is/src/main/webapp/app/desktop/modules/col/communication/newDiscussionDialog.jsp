<div class="modal-header">
    <h3>New Discussion Group</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label">Name: </label>

            <div class="col-sm-8">
                <input type="text" class="form-control" name="type" ng-model="newDiscussionVm.name">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">Description: </label>

            <div class="col-sm-8">
           <textarea name="description" rows="10" class="form-control" style="resize: none"
                     ng-model="newDiscussionVm.description"></textarea>
            </div>
        </div>
    </form>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default"
                    ng-click="newDiscussionVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="newDiscussionVm.create()">Create
            </button>
        </div>
    </div>
</div>