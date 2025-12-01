<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <h4 class="section-title" style="" translate>CUSTOMER_INFO</h4>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="{{'ENTER_CUSTOMER_NAME' | translate}}"
                                   ng-model="newCustomerVm.newCustomer.name" autofocus>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"  placeholder="{{'ENTER_PHONE_NUMBER' | translate}}"
                                   ng-model="newCustomerVm.newCustomer.phone"
                                   valid-number pattern="[0-9]*">
                        </div>
                    </div>

                    <div class=" form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ADDRESS</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"  placeholder="{{'ENTER_ADDRESS' | translate}}"
                                      ng-model="newCustomerVm.newCustomer.address"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="{{'ENTER_EMAIL' | translate}}"
                                   ng-model="newCustomerVm.newCustomer.email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES_TITLE</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none" placeholder="{{'ENTER_NOTES' | translate}}"
                                      ng-model="newCustomerVm.newCustomer.notes"></textarea>
                        </div>
                    </div>
                    <h4 class="section-title" style="" translate>CONTACT_PERSON</h4>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>FIRST_NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control"  placeholder="{{'ENTER_FIRST_NAME' | translate}}"
                                   type="text" ng-model="newCustomerVm.newCustomer.person.firstName"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>LAST_NAME</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{'ENTER_LAST_NAME' | translate}}"
                                   type="text" ng-model="newCustomerVm.newCustomer.person.lastName"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{'ENTER_PHONE_NUMBER' | translate}}"
                                   type="text" ng-model="newCustomerVm.newCustomer.person.phoneMobile"
                                   valid-number pattern="[0-9]*"/>
                        </div>
                    </div>
                    <div class=" form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control" placeholder="{{'ENTER_EMAIL' | translate}}"
                                   type="text" ng-model="newCustomerVm.newCustomer.person.email"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
