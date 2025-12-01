<div class="row">
    <br>
    <br>

    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <div class="panel-btns">
                    <a href="" class="panel-close" data-ng-click="close();">&times;</a>
                </div>
                <h4 class="panel-title">Add New Employee</h4>
            </div>
            <div class="panel-body panel-body-nopadding">
                <!-- BASIC WIZARD -->
                <div id="basicWizard" class="basic-wizard">
                    <ul class="nav nav-pills nav-justified">
                        <li ng-class="{active: currentTab == tabList.personDetails}"><a href=""
                                                                                        data-ng-click="updateTab(tabList.personDetails,personDetails,employeeDetails,addressDetails);"><span>Step 1:</span>
                            Person Details</a></li>
                        <li ng-class="{active: currentTab == tabList.employeeDetails}"><a href=""
                                                                                          data-ng-click="updateTab(tabList.employeeDetails,personDetails,employeeDetails,addressDetails);"><span>Step 2:</span>
                            Employee Details</a></li>
                        <li ng-class="{active: currentTab == tabList.addressDetails}"><a href=""
                                                                                         data-ng-click="updateTab(tabList.addressDetails,personDetails,employeeDetails,addressDetails);"><span>Step 3:</span>
                            Address Information</a></li>
                    </ul>
                    <div class="tab-content styled-panel" style="margin: 25px">
                        <div class="tab-pane" ng-class="{active: currentTab == tabList.personDetails}" id="tab1">
                            <div class="row">
                                <div class="col-sm-8 col-sm-offset-2">
                                    <form class="form" name="personDetails" novalidate angular-validator>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Employee Number</label>

                                                <input type="text" name="employeeNumber" ng-model="employee.employeeNumber"
                                                       class="form-control" required-message="constants.REQUIRED"
                                                       ng-blur="validateEmpNum(employee.employeeNumber)" validate-on="dirty" required>
                                            </div>
                                        </div>

                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Firstname</label>

                                                <input type="text" name="firstname" ng-model="employee.firstName"
                                                       class="form-control" required-message="constants.REQUIRED"
                                                      ng-blur="validateName(employee.firstName)" validate-on="dirty" required>

                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Middlename</label>
                                                <input type="text" name="middlename" ng-model="employee.middleName"
                                                       ng-blur="validateName(employee.middleName)"    class="form-control">
                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Lastname</label>
                                                <input type="text" name="lastname" ng-model="employee.lastName"
                                                       class="form-control" required-message="constants.REQUIRED"
                                                       ng-blur="validateName(employee.lastName)" validate-on="dirty" required>
                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Email</label>
                                                <input type="email" name="email" ng-model="employee.email"
                                                       class="form-control" invalid-message="constants.INVALIDEMAIL"
                                                       required-message="constants.REQUIRED" validate-on="dirty"
                                                       required>
                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Upload Profile Pic</label>

                                                <div class="input-group">
                                                    <input type="text" class="form-control" placeholder="">
                                                    <span class="input-group-addon"><i class="fa fa-upload"></i></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Phone- Mobile</label>
                                                <input type="text" name="phonemobile" ng-model="employee.phoneMobile"
                                                       class="form-control" validate-on="dirty"
                                                      ng-blur="validatePhoneNumber(employee.phoneMobile)" required-message="constants.REQUIRED" required>
                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Phone- Office</label>
                                                <input type="text" name="phoneoffice" ng-model="employee.phoneOffice"
                                                       ng-blur="validatePhoneNumber(employee.phoneOffice)" class="form-control">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" ng-class="{active: currentTab == tabList.employeeDetails}" id="tab2">
                            <div class="row">
                                <div class="col-sm-8 col-sm-offset-2">
                                    <form class="form" name="employeeDetails" novalidate angular-validator>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Job Title</label>
                                                <input type="text" name="jobtitle" ng-model="employee.jobTitle"
                                                       ng-blur="validateJobTitle()"
                                                       class="form-control" required-message="constants.REQUIRED"
                                                       validate-on="dirty" required>
                                            </div>
                                        </div>
                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Department</label>
                                                <ui-select ng-model="employee.department" theme="bootstrap"
                                                           style="width: 100%;" title="Choose a Department">
                                                    <ui-select-match placeholder="Choose a Department">
                                                        {{$select.selected.name}}
                                                    </ui-select-match>
                                                    <ui-select-choices
                                                            repeat="department in departments | filter: $select.search">
                                                        <div ng-bind-html="department.name | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>
                                                <input type="text" name="departmentdummy"
                                                       ng-model="employee.department.id" class="hide form-control"
                                                       required-message="constants.REQUIRED"
                                                       validator="departmentValidator(employee.department)"
                                                       validate-on="dirty"
                                                       required>
                                            </div>
                                        </div>

                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Manager</label>
                                                <ui-select ng-model="employee.manager" theme="bootstrap"
                                                           style="width: 100%;" title="Choose a Manager">
                                                    <ui-select-match placeholder="Choose Manager">
                                                        {{$select.selected.firstName}}
                                                    </ui-select-match>
                                                    <ui-select-choices
                                                            repeat="employee in employees.content | filter: $select.search">
                                                        <div ng-bind-html="employee.firstName | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>

                                                <!--
                                                <input type="text" name="managerdummy" ng-model="employee.manager.id" class="hide form-control"
                                                       required-message="constants.REQUIRED"
                                                       validator = "managerValidator(employee.manager)"
                                                       validate-on="dirty"
                                                       required>
                                                -->
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" ng-class="{active: currentTab == tabList.addressDetails}" id="tab3">
                            <div class="row">
                                <div class="col-sm-8 col-sm-offset-2">
                                    <form class="form" name="addressDetails" novalidate angular-validator>
                                        <div class="mb15">

                                            <div class="form-group">
                                                <label class="control-label">Street Address</label>
                                                <textarea class="form-control" rows="3"
                                                          ng-model="employee.addresses[0].addressText"
                                                          style="resize:none;" name="streetaddress"
                                                          required-message="constants.REQUIRED"
                                                          validate-on="dirty"
                                                          required></textarea>
                                            </div>
                                        </div>

                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">City</label>
                                                <input type="text" name="city" ng-model="employee.addresses[0].city"
                                                       ng-blur="validateCityName()"
                                                       class="form-control" required-message="constants.REQUIRED"
                                                       validate-on="dirty" required>
                                            </div>
                                        </div>

                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">State</label>
                                                <ui-select ng-model="employee.addresses[0].state"
                                                           data-ng-change="stateChange();" theme="bootstrap"
                                                           style="width: 100%;" title="Choose a State">
                                                    <ui-select-match placeholder="Choose a State">
                                                        {{$select.selected.name}}
                                                    </ui-select-match>
                                                    <ui-select-choices
                                                            repeat="state in allStates | filter: $select.search">
                                                        <div ng-bind-html="state.name | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>
                                                <input type="text" name="statedummy"
                                                       ng-model="employee.addresses[0].state.id"
                                                       class="hide form-control"
                                                       required-message="constants.REQUIRED"
                                                       validator="stateValidator(employee.addresses[0].state)"
                                                       validate-on="dirty"
                                                       required>
                                            </div>
                                        </div>


                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">Country</label>
                                                <ui-select ng-model="employee.addresses[0].country" theme="bootstrap"
                                                           style="width: 100%;" title="Choose a Country">
                                                    <ui-select-match placeholder="Choose a Country">
                                                        {{$select.selected.name}}
                                                    </ui-select-match>
                                                    <ui-select-choices
                                                            repeat="country in countries | filter: $select.search">
                                                        <div ng-bind-html="country.name | highlight: $select.search"></div>
                                                    </ui-select-choices>
                                                </ui-select>
                                                <input type="text" name="countrydummy"
                                                       ng-model="employee.addresses[0].country.id"
                                                       class="hide form-control"
                                                       required-message="constants.REQUIRED"
                                                       validator="countryValidator(employee.department)"
                                                       validate-on="dirty"
                                                       required>
                                            </div>
                                        </div>

                                        <div class="mb15">
                                            <div class="form-group">
                                                <label class="control-label">PinCode</label>
                                                <input type="text" name="" class="form-control"
                                                       ng-model="employee.addresses[0].pincode">
                                            </div>
                                        </div>


                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- tab-content -->
                    <ul class="pager wizard">
                        <li class="previous" ng-if="currentTab!==tabList.personDetails"><a href=""
                                                                                           data-ng-click="previous('PREVIOUS');">Previous</a>
                        </li>
                        <li class="next" ng-if="currentTab!==tabList.addressDetails"><a href=""
                                                                                        data-ng-click="navigateTab('NEXT',personDetails,employeeDetails,addressDetails);">Next</a>
                        </li>
                        <li class="next" ng-if="currentTab==tabList.addressDetails"><a href=""
                                                                                       data-ng-click="navigateTab('SUBMIT',personDetails,employeeDetails,addressDetails);">Submit</a>
                        </li>
                    </ul>
                </div>
                <!-- #basicWizard -->
            </div>
            <!-- panel-body -->
        </div>
        <!-- panel -->
    </div>
</div>
