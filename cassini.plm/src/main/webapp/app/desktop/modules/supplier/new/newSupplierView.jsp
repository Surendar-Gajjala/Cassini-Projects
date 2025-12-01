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
                            <input type="text" class="form-control" name="title"
                                   ng-model="newSupplierVm.newSupplier.name">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newSupplierVm.newSupplier.phone">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ADDRESS</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="newSupplierVm.newSupplier.address"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newSupplierVm.newSupplier.email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES_TITLE</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="newSupplierVm.newSupplier.notes"></textarea>
                        </div>
                    </div>
                    <h4 class="section-title" style="" translate>CONTACT_PERSON</h4>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>FIRST_NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control"
                                   type="text" ng-model="newSupplierVm.newSupplier.person.firstName"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>LAST_NAME</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control"
                                   type="text" ng-model="newSupplierVm.newSupplier.person.lastName"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control"
                                   type="text" ng-model="newSupplierVm.newSupplier.person.phoneMobile"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input class="form-control"
                                   type="text" ng-model="newSupplierVm.newSupplier.person.email"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
