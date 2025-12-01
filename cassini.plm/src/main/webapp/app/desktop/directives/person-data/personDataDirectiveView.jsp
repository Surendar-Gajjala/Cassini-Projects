<div class="form-group">
    <label class="col-sm-4 control-label">
        <span translate>FIRST_NAME</span>
        <span class="asterisk">*</span> :
    </label>

    <div class="col-sm-7">
        <input type="text" class="form-control" name="firstName"
               placeholder="{{'ENTER_FIRST_NAME' | translate}}"
               ng-model="person.firstName" ng-class="{disabled:disable}">
    </div>
</div>

<div class="form-group">
    <label class="col-sm-4 control-label">
        <span translate>LAST_NAME</span> :
    </label>

    <div class="col-sm-7">
        <input type="text" class="form-control" name="lastName"
               placeholder="{{'ENTER_LAST_NAME' | translate}}"
               ng-model="person.lastName" ng-class="{disabled:disable}">
    </div>
</div>

<div class="form-group">
    <label class="col-sm-4 control-label">
        <span translate>PHONE_NUMBER</span> :
    </label>

    <div class="col-sm-7">
        <input type="text" class="form-control" name="phoneNumber"
               placeholder="{{'ENTER_PHONE_NUMBER' | translate}}"
               ng-model="person.phoneMobile" ng-class="{disabled:disable}" valid-number
               pattern="[0-9]*">
    </div>
</div>
<div class="form-group" style="margin-bottom: 15px;">
    <label class="col-sm-4 control-label">
        <span translate>EMAIL</span>
        <span class="asterisk">*</span>:
    </label>

    <div class="col-sm-7">
        <input type="text" class="form-control" name="email"
               placeholder="{{'ENTER_EMAIL' | translate}}"
               ng-model="person.email" ng-class="{disabled:disable}">
    </div>
</div>