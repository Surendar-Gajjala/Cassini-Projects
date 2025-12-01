<div class="modal-header">
  <h3>New Bed</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">
  <form class="form-horizontal">

    <div class="form-group">
      <label class="col-sm-3 control-label">Name: </label>

      <div class="col-sm-7">
        <input type="text" class="form-control" name="Name" placeholder="Name"
               ng-model="newBedVm.newBed.name">
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-3 control-label">Assigned To:</label>

      <div class="col-sm-7">
        <ui-select ng-model="newBedVm.newBed.assignedTo" theme="bootstrap">
          <ui-select-match placeholder="Select">{{$select.selected.firstName}}</ui-select-match>
          <ui-select-choices repeat="person in newBedVm.persons | filter: $select.search">
            <div ng-bind="person.firstName | highlight: $select.search"></div>
          </ui-select-choices>
        </ui-select>
      </div>
    </div>

    <div class="form-group">
      <label class="col-sm-3 control-label">Description: </label>

      <div class="col-sm-7">
                     <textarea rows="4" cols="50" class="form-control" name="Description" placeholder="Description"
                               ng-model="newBedVm.newBed.description"
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
      <button type="button" class="btn btn-sm btn-default" ng-click="newBedVm.cancel()">Cancel</button>
      <button type="button" class="btn btn-sm btn-success" ng-click="newBedVm.create()">Create</button>
    </div>
  </div>
</div>
