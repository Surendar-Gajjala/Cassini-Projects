<div class="view-container">
  <div class="view-toolbar">
    <h3 style="margin-top: 5px;">Select Persons</h3>
  </div>

  <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
    <table class="table table-striped">
      <tbody>
      <tr>
        <th>Actions</th>
        <th>Name</th>
        <th>PhoneNumber</th>
        <th>Email</th>
      </tr>
      <tr ng-repeat="person in empDialogueVm.persons">

        <td> <input type="checkbox" ng-click="empDialogueVm.selectCheckBox(person)"></td>
        <td style="vertical-align: middle;">
          {{person.firstName}}
        </td>

        <td style="vertical-align: middle;">
          {{person.phoneMobile}}
        </td>

        <td style="vertical-align: middle;">
          {{person.email}}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</div>
<div class="modal-footer">
  <div class="row">
    <div class="col-md-6">
    </div>
    <div class="modal-buttons" class="col-md-6">
      <button type="button" class="btn btn-sm btn-default"
              ng-click="empDialogueVm.cancel()">Cancel
      </button>
      <button type="button" class="btn btn-sm btn-success"
              ng-click="empDialogueVm.onOk()">Select
      </button>
    </div>
  </div>
</div>
</div>
