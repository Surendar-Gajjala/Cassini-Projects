<div class="modal-header">

  <h3>New Login</h3>
</div>

<div class="alert alert-danger" ng-hide="loginDialogVm.valid" ng-cloak>
  {{ loginDialogVm.error }}
</div>


<div class="modal-body" style="padding: 20px; max-height: 500px;">
  <form>
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="fName">Firstname:</label>
          <input type="text" class="form-control" id="fName" placeholder="Name" ng-model="loginDialogVm.person.firstName">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="lName">Lastname:</label>
          <input type="text" class="form-control" id="lName" placeholder="Lastname" ng-model="loginDialogVm.person.lastName">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="Username">Username:</label>
          <input type="text" class="form-control" id="Username" placeholder="LoginName" ng-model="loginDialogVm.login.loginName">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="pwd">password:</label>
          <input type="password" class="form-control" id="pwd" placeholder="Password" ng-model="loginDialogVm.login.password">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label for="mobile">Mobile no:</label>
          <input type="tel" class="form-control" id="mobile" placeholder="Mobile no" ng-model="loginDialogVm.person.phoneMobile">
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label for="mail">Email:</label>
          <input type="email" class="form-control" id="mail" placeholder="email" ng-model="loginDialogVm.person.email">
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="form-group">
          <label for="address">Address:</label>
          <textarea class="form-control" rows="3" style="resize:none;" id="address"></textarea>
        </div>
      </div>
    </div>
  </form>
</div>
<div class="modal-footer">
  <div class="row">
    <div class="col-md-6">
    </div>
    <div class="modal-buttons" class="col-md-6">
      <button type="button" class="btn btn-sm btn-default"  ng-click="loginDialogVm.cancel()">Cancel</button>

      <button type="button" class="btn btn-sm btn-success"  ng-click="loginDialogVm.create()">Create</button>

    </div>
  </div>
</div>