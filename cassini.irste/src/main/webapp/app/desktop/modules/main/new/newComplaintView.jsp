<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 08-11-2018
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label">Email :<span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <input type="email" class="form-control" name="title" <%--ng-blur="newCompVm.isPersonNew()"--%>
                               ng-model="newCompVm.person.email" autofocus>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Complainant Type:<span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <ui-select class="required-field" ng-model="newCompVm.person.personType"
                                   theme="bootstrap">
                            <ui-select-match placeholder="Select Designation">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="personType in newCompVm.personTypes | filter: $select.search">
                                <div ng-bind="personType.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">First Name :<span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newCompVm.person.firstName">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">Last Name :</label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="title"
                               ng-model="newCompVm.person.lastName">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label"> Phone :<span class="asterisk">*</span></label>

                    <div class="col-sm-7" ng-form name="mobile">
                        <div class="input-group mb15" style="width: 100%;margin-bottom: 0px;">
                            <input type="text"
                                   style="width: 20%;border-radius: 3px 0px 0px 3px;padding: 10px;"
                                   class="form-control" value="+91" readonly/>
                            <input type="text" class="form-control" ng-pattern="/^\+?\d{10}$/" id="phoneNumber"
                                   style="width: 80%;"
                                   maxlength="10"
                                   ng-model="newCompVm.person.phoneMobile"
                                   name="mobileNumber" placeholder="Enter Valid Phone Number"/>
                        </div>
                    </div>
                </div>

                <br>

                <h4 class="section-title" style="color: black;">Complaint Details</h4>

                <div class="form-group">

                    <label class="col-sm-4 control-label">Number : <span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <div class="input-group mb15">
                            <div class="input-group-btn">
                            </div>
                            <input type="text" class="form-control" name="title"
                                   ng-model="newCompVm.complaintNumber" readonly>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Group Location:<span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <ui-select class="required-field" ng-model="newCompVm.newComplaint.group"
                                   theme="bootstrap">
                            <ui-select-match placeholder="Select Group Location">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices repeat="group in newCompVm.groupLocations | filter: $select.name.search">
                                <div ng-bind="group.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Location:<span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <ui-select class="required-field" ng-model="newCompVm.newComplaint.location"
                                   theme="bootstrap" ng-disabled="newCompVm.newComplaint.group == null">
                            <ui-select-match placeholder="Select Location">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="location in newCompVm.newComplaint.group.locations | filter: $select.name.search">
                                <div ng-bind="location.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Utility:<span class="asterisk">*</span></label>

                    <div class="col-sm-7">
                        <ui-select class="required-field" ng-model="newCompVm.newComplaint.utility"
                                   theme="bootstrap" ng-disabled="newCompVm.newComplaint.location == null">
                            <ui-select-match placeholder="Select Utility">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="utility in newCompVm.newComplaint.location.utilities | filter: $select.search">
                                <div ng-bind="utility | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label">Details :<span class="asterisk">*</span> </label>

                    <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newCompVm.newComplaint.details"></textarea>
                    </div>
                </div>
            </form>
            <br><br>
        </div>
    </div>
</div>
</div>
