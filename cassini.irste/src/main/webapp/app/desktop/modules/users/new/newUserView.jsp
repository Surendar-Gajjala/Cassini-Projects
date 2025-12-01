<style>
    .ui-select-match .close {
        font-size: 25px !important;

    }

    .ui-select-bootstrap {
        height: 38px !important;

    }
</style>

<div style="position: relative;">
    <div class="row" style="margin: 0;padding: 20px;">
        <div>
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-4 control-label classificationlables">First Name <span
                            class="asterisk">*</span> :
                    </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="name" placeholder="Enter First Name"
                               ng-model="newUserVm.newUser.firstName">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label classificationlables">Last Name:
                    </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="name" placeholder="Enter Last Name"
                               ng-model="newUserVm.newUser.lastName"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label classificationlables">Designation<span
                            class="asterisk">*</span> :
                    </label>

                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="name" placeholder="Enter Designation"
                               ng-model="newUserVm.newUser.designation"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label classificationlables">Email<span
                            class="asterisk">* :
                        </span>
                    </label>

                    <div class="col-sm-7" ng-form name="myForm">
                        <input type="text" class="form-control" ng-pattern="/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/"
                               ng-model="newUserVm.newUser.email"
                               ng-change="newUserVm.removeErrorMessage()"
                               name="email" placeholder="Enter Valid Email Address"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label classificationlables">Phone Number
                        <span class="asterisk">*</span> :
                    </label>

                    <div class="col-sm-7" ng-form name="mobile">
                        <div class="input-group mb15" style="width: 100%;margin-bottom: 0px;">
                            <input type="text"
                                   style="width: 20%;border-radius: 3px 0px 0px 3px;padding: 10px;"
                                   class="form-control" value="+91" readonly/>
                            <input type="text" class="form-control" ng-pattern="/^\+?\d{10}$/" id="phoneNumber"
                                   style="width: 80%;"
                                   maxlength="10" ng-change="newUserVm.removeErrorMessage()"
                                   ng-model="newUserVm.newUser.phoneMobile"
                                   name="mobileNumber" placeholder="Enter Valid Phone Number"/>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-4 control-label classificationlables">Utilities <span class="asterisk">*</span>:
                    </label>

                    <div class="col-sm-7" style="margin-top: 5px !important;">
                        <ui-select multiple ng-model="newUserVm.newUser.utilities" theme="bootstrap" <%--rows="5"--%>
                                   style="height: auto !important;"
                                   close-on-select="false" title="Choose a Utilities" remove-selected="true">
                            <ui-select-match placeholder="Select Utilities...">{{$item}}</ui-select-match>
                            <ui-select-choices repeat="utility in newUserVm.utilities">
                                <div ng-bind="utility"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>