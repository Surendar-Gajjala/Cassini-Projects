<div class="modal-header">
    <h3>New Suite</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <form class="form-horizontal">

        <div class="form-group">
            <label class="col-sm-3 control-label">Name: </label>

            <div class="col-sm-7">
                <input type="text" class="form-control" name="Name" placeholder="Name"
                       ng-model="newSuitVm.newSuit.name">
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-3 control-label">Description: </label>

            <div class="col-sm-7">
                     <textarea rows="4" cols="50" class="form-control" name="Description" placeholder="Description"
                               ng-model="newSuitVm.newSuit.description"
                               style="resize: none;">
                    </textarea>
            </div>
        </div>
    </form>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">

        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default" ng-click="newSuitVm.cancel()">Cancel</button>
            <button type="button" class="btn btn-sm btn-success" ng-click="newSuitVm.create()">Create</button>
        </div>
    </div>
</div>