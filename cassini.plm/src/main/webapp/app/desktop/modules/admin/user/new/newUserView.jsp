<div style="position: relative;">
    <style scoped>
        .switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 34px;
        }

        .switch input {
            opacity: 0;
            width: 0;
            height: 0;
        }

        .switch .slider {
            position: absolute;
            cursor: pointer;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #ccc;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch .slider:before {
            position: absolute;
            content: "";
            height: 26px;
            width: 26px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch input:checked + .slider {
            background-color: #2196F3;
        }

        .switch input:focus + .slider {
            box-shadow: 0 0 1px #2196F3;
        }

        .switch input:checked + .slider:before {
            -webkit-transform: translateX(26px);
            -ms-transform: translateX(26px);
            transform: translateX(26px);
        }

        /* Rounded sliders */
        .switch .slider.round {
            border-radius: 34px;
        }

        .switch .slider.round:before {
            border-radius: 50%;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group" ng-if="newUserVm.personWithoutLoginCount > 0">
                        <label class="col-sm-4 control-label">
                            <span translate>LOGIN_FOR_EXISTING_PERSON</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="newPerson" switch="none" checked=""
                                   ng-model="newUserVm.newLogin.existPerson" ng-change="newUserVm.onChangeLoginMode()">
                            <label for="newPerson" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group"
                         ng-show="newUserVm.newLogin.existPerson">
                        <label class="col-sm-4 control-label">
                            <span translate>PERSON_TYPE</span>: </label>

                        <div class="col-sm-7" style="margin-top: 3px;margin-left: -2px;">
                            <div class="switch-toggle switch-candy">
                                <input id="customer" name="personType" type="radio" checked
                                       ng-click="newUserVm.selectPersonType('Customers', $event)">
                                <label for="customer" onclick="" translate>CUSTOMER</label>

                                <input id="supplierContact" name="personType" type="radio"
                                       ng-click="newUserVm.selectPersonType('SupplierContacts', $event)">
                                <label for="supplierContact" onclick="" translate>SUPPLIER</label>

                                <input id="manpowerContact" name="personType" type="radio"
                                       ng-click="newUserVm.selectPersonType('ManpowerContacts', $event)">
                                <label for="manpowerContact" onclick="" translate>MANPOWER</label>
                                <a href=""></a>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" ng-show="newUserVm.newLogin.existPerson">
                        <label class="col-sm-4 control-label">
                            <span ng-if="newUserVm.selectedPersonType == 'Customers'" translate>CUSTOMER_CONTACT</span>
                            <span ng-if="newUserVm.selectedPersonType == 'SupplierContacts'"
                                  translate>SUPPLIER_CONTACT</span>
                            <span ng-if="newUserVm.selectedPersonType == 'ManpowerContacts'" translate>MANPOWER</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newUserVm.selectedPerson" theme="bootstrap" id="selectCustomer"
                                       style="width:100%" on-select="newUserVm.changePerson($item)"
                                       ng-show="newUserVm.selectedPersonType == 'Customers'">
                                <ui-select-match placeholder="{{selectCustomerTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newUserVm.customers | filter: $select.search | orderBy:'fullName'">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                            <ui-select ng-model="newUserVm.selectedPerson" theme="bootstrap" id="selectSupplierContact"
                                       style="width:100%" on-select="newUserVm.changePerson($item)"
                                       ng-show="newUserVm.selectedPersonType == 'SupplierContacts'">
                                <ui-select-match placeholder="{{selectSupplierContactTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newUserVm.supplierContacts | filter: $select.search | orderBy:'fullName'">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                            <ui-select ng-model="newUserVm.selectedPerson" theme="bootstrap" id="selectManpowerContact"
                                       style="width:100%" on-select="newUserVm.changePerson($item)"
                                       ng-show="newUserVm.selectedPersonType == 'ManpowerContacts'">
                                <ui-select-match placeholder="{{selectManpowerContactTitle}}">
                                    {{$select.selected.fullName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="person in newUserVm.manpowerContacts | filter: $select.search | orderBy:'fullName'">
                                    <div ng-bind="person.fullName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>FIRST_NAME</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="firstName"
                                   placeholder="{{'FIRST_NAME_TITLE' | translate}}"
                                   ng-model="newUserVm.newPerson.firstName"
                                   ng-change="newUserVm.setUserName()"
                                   ng-disabled="newUserVm.newLogin.existPerson">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>LAST_NAME</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="lastName"
                                   placeholder="{{'LAST_NAME_TITLE' | translate}}"
                                   ng-model="newUserVm.newPerson.lastName"
                                   ng-change="newUserVm.setUserName()"
                                   ng-disabled="newUserVm.newLogin.existPerson">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>USERNAME</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">

                            <input type="text" class="form-control" name="userName"
                                   placeholder="{{'USER_NAME_TITLE' | translate}}"
                                   ng-model="newUserVm.newLogin.loginName "
                                   ng-change="newUserVm.newLogin.loginName = (newUserVm.newLogin.loginName | lowercase)">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DEFAULT_ROLE</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newUserVm.newPerson.defaultGroup" theme="bootstrap"
                                       style="width:100%" on-select="newUserVm.onSelectGroup($item)">
                                <ui-select-match placeholder="{{'SELECT_ROLE' | translate}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="group.groupId as group in newUserVm.groups | startsWith: $select.search | orderBy:'name'">
                                    <div ng-bind-html="group.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="phoneNumber"
                                   placeholder="{{'PHONE_NUMBER_TITLE' | translate}}"
                                   ng-model="newUserVm.newPerson.phoneMobile" valid-number
                                   pattern="[0-9]*" ng-disabled="newUserVm.newLogin.existPerson">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span>
                            <span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="email"
                                   placeholder="{{'EMAIL' | translate}}"
                                   ng-model="newUserVm.newPerson.email" ng-disabled="newUserVm.newLogin.existPerson">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>IS_ADMIN</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isAdmin" switch="none" checked=""
                                   ng-model="newUserVm.newLogin.isSuperUser"
                                   ng-disabled="newUserVm.newLogin.external">
                            <label for="isAdmin" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>IS_ACTIVE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isActive" switch="none" checked=""
                                   ng-model="newUserVm.newLogin.isActive" ng-change="newUserVm.userActivation()">
                            <label for="isActive" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>EXTERNAL</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isExternal" switch="none" checked=""
                                   ng-model="newUserVm.newLogin.external"
                                   ng-disabled="true">
                            <label for="isExternal" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>
