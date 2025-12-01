<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-info" ui-sref="app.person.all">Back</button>
    </div>
    <br>
    <br>
    <div class="view-content">
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <div class="panel panel-default" style="border: 1px solid #d7d7d7;">
                    <div class="panel-heading">
                        <h4 class="panel-title">Add New Person</h4>
                        <h5 class="text-danger" style="text-align: center;" ng-show="newPersonVm.errorMessage != null">
                            {{ newPersonVm.errorMessage}}</h5>
                    </div>
                    <div class="panel-body panel-body-nopadding">
                        <!-- BASIC WIZARD -->
                        <div id="basicWizard" class="basic-wizard">

                            <ul class="nav nav-tabs nav-justified">
                                <li class="active"><a data-toggle="tab" href="#home">Personal Info</a></li>
                                <li><a data-toggle="tab" href="#menu1">Emergency Contact</a></li>
                                <li><a data-toggle="tab" href="#menu2">Other Info</a></li>
                            </ul>

                            <div class="tab-content">

                                <div id="home" class="tab-pane fade in active" style="border: 1px solid #d7d7d7;">
                                    <div class="col-md-8 col-md-offset-2">
                                        <form>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">First Name</label>
                                                    <input type="text" ng-model="newPersonVm.person.firstName"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">MiddleName</label>
                                                    <input type="text" ng-model="newPersonVm.person.middleName"
                                                           class="form-control">
                                                </div>
                                            </div>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Last Name</label>
                                                    <input type="text" ng-model="newPersonVm.person.lastName"
                                                           class="form-control">
                                                </div>
                                            </div>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Login</label>
                                                    <input type="text" ng-model="newPersonVm.login.loginName"
                                                           class="form-control">
                                                </div>
                                            </div>
                                            <%--<div class="mb15">
                                             <div class="form-group">
                                              <label class="control-label">Job Title</label>
                                             <input type="text" ng-model="newPersonVm.person.title"  class="form-control">
                                               </div>
                                              </div>--%>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Person Role</label>
                                                    <ui-select ng-model="newPersonVm.otherInfo.role" theme="bootstrap">
                                                        <ui-select-match placeholder="Select">{{$select.selected}}
                                                        </ui-select-match>
                                                        <ui-select-choices
                                                                repeat="role in newPersonVm.personRoles| filter: $select.search">
                                                            <div ng-bind="role | highlight: $select.role.search"></div>
                                                        </ui-select-choices>
                                                    </ui-select>
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Department</label>
                                                    <ui-select ng-model="newPersonVm.otherInfo.departmentObject"
                                                               theme="bootstrap">
                                                        <ui-select-match placeholder="Select">
                                                            {{$select.selected.name}}
                                                        </ui-select-match>
                                                        <ui-select-choices
                                                                repeat="dept in newPersonVm.departments | filter: $select.search">
                                                            <div ng-bind="dept.name | highlight: $select.dept.search"></div>
                                                        </ui-select-choices>
                                                    </ui-select>
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Designation</label>
                                                    <input type="text" ng-model="newPersonVm.otherInfo.designation"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Devision</label>
                                                    <input type="text" ng-model="newPersonVm.otherInfo.devision"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Phone-Office</label>
                                                    <input type="text" ng-model="newPersonVm.person.phoneOffice"
                                                           class="form-control">
                                                </div>
                                            </div>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Phone-Mobile</label>
                                                    <input type="text" ng-model="newPersonVm.person.phoneMobile"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Email</label>
                                                    <input type="text" ng-model="newPersonVm.person.email"
                                                           class="form-control">
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                </div>

                                <div id="menu1" class="tab-pane fade" style="border: 1px solid #d7d7d7;">
                                    <div class="col-md-8 col-md-offset-2">
                                        <form>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">First Name</label>
                                                    <input type="text" ng-model="newPersonVm.emergencyContact.firstName"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Last Name</label>
                                                    <input type="text" ng-model="newPersonVm.emergencyContact.lastName"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Phone number</label>
                                                    <input type="text"
                                                           ng-model="newPersonVm.emergencyContact.phoneMobile"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Relationship</label>
                                                    <input type="text" ng-model="newPersonVm.emergencyContact.relation"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div id="menu2" class="tab-pane fade" style="border: 1px solid #d7d7d7;">
                                    <div class="col-md-8 col-md-offset-2">
                                        <form>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Parent Unit</label>
                                                    <input type="text" ng-model="newPersonVm.otherInfo.parentUnit"
                                                           class="form-control">
                                                </div>
                                            </div>
                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Controlling Officer</label>
                                                    <input type="text"
                                                           ng-model="newPersonVm.otherInfo.controllingOfficer"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Controlling Officer Contact</label>
                                                    <input type="text"
                                                           ng-model="newPersonVm.otherInfo.controllingOfficerContact"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Blood Group</label>
                                                    <input type="text" ng-model="newPersonVm.otherInfo.bloodGroup"
                                                           class="form-control">
                                                </div>
                                            </div>

                                            <div class="mb15">
                                                <div class="form-group">
                                                    <label class="control-label">Medical Problems</label>
                                                    <input type="text" ng-model="newPersonVm.otherInfo.medicalProblems"
                                                           class="form-control">
                                                </div>
                                            </div>
                                            <hr>
                                            <button class="btn btn-sm btn-primary pull-right" style="margin: 10px;"
                                                    ng-disabled="newPersonVm.createButton"
                                                    ng-click="newPersonVm.createPersonInfo();">Submit
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
