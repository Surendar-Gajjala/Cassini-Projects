<div class="view-container">
    <div class="view-toolbar">
        <h3 style="margin-top: 5px;">New Accommodation</h3>
    </div>

    <div class="modal-body" style="padding: 20px; max-height: 500px;">
        <form class="form-horizontal">

            <div class="form-group">
                <label class="col-sm-3 control-label">Name: </label>

                <div class="col-sm-7">
                    <input type="text" class="form-control" name="Name" placeholder="Name"
                           ng-model="newAccommodationVm.newAccommodation.name">
                </div>
            </div>

            <p class="text-danger text-center" style="margin-top: 15px; margin-bottom: 0px"
               ng-show="newAccommodationVm.errorMessage.name != null" ng-cloak>
                {{ newAccommodationVm.errorMessage.name}}
            </p>

            <div class="form-group">
                <label class="col-sm-3 control-label">Description: </label>

                <div class="col-sm-7">
                     <textarea rows="4" cols="50" class="form-control" name="Description" placeholder="Description"
                               ng-model="newAccommodationVm.newAccommodation.description"
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
            <button type="button" class="btn btn-sm btn-default" ng-click="newAccommodationVm.cancel()">Cancel</button>
            <button type="button" class="btn btn-sm btn-success" ng-click="newAccommodationVm.create()">Create</button>
        </div>
    </div>
</div>
