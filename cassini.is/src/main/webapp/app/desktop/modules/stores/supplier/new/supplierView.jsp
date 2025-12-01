<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row">
            <div>
                <form class="form-horizontal">

                    <div class="form-group">

                        <label class="col-sm-4 control-label">Name : <span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   ng-model="supplierVm.supplier.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"> Contact Person :</label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="contactPerson"
                                   ng-model="supplierVm.supplier.contactPerson">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">Contact Number :</label>

                        <div class="col-sm-7" ng-form name="mobile">
                            <div class="input-group mb15" style="width: 100%;margin-bottom: 0px;">
                                <input type="text"
                                       style="width: 20%;border-radius: 3px 0px 0px 3px;padding: 10px;"
                                       class="form-control" <%--value="+91"--%> readonly/>
                                <input type="text" class="form-control"
                                       ng-pattern="/^\+?\d{10}$/" <%--id="phoneNumber"--%>
                                       style="width: 80%;"
                                       maxlength="10"
                                       ng-model="supplierVm.supplier.contactPhone"
                                       name="contactPhone" <%--placeholder="Enter Valid Phone Number"--%>/>
                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description :</label>

                        <div class="col-sm-7">
                           <textarea name="description" rows="5" class="form-control"
                                     ng-model="supplierVm.supplier.description"></textarea>
                        </div>
                    </div>

                    <br>
                    <h4 ng-if="supplierVm.newSupplierAttributes.length  > 0 || supplierVm.attributes.length > 0"
                        class="section-title" style="color: black;">Attributes
                    </h4>
                    <br>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="supplierVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="supplierVm.attributes"></attributes-view>
                            <br>
                            <br>
                        </form>
                    </div>

                </form>

            </div>
        </div>
    </div>
</div>
