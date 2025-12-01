<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SUPPLIER_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <supplier-tree
                                                    on-select-type="newSupplierVm.onSelectType"
                                                    object-type="SUPPLIERTYPE"></supplier-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newSupplierVm.selectedSupplierType.name" readonly>

                            </div>
                        </div>
                    </div>


                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newSupplierVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newSupplierVm.newSupplier.number">
                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}" ng-model="newSupplierVm.newSupplier.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newSupplierVm.newSupplier.description"></textarea>
                        </div>
                    </div>


                    <div class=" form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ADDRESS</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_ADDRESS' | translate}}"
                                      ng-model="newSupplierVm.newSupplier.address"></textarea>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>CITY</span>
                            : </label>

                        <div class="col-sm-7">
                            <input type="text" placeholder="{{'ENTER_CITY' | translate}}" class="form-control"
                                   name="title" ng-model="newSupplierVm.newSupplier.city">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>COUNTRY</span>
                            : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_COUNTRY' | translate}}"
                                   name="title" ng-model="newSupplierVm.newSupplier.country">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>POSTAL_CODE</span>
                            : </label>

                        <div class="col-sm-7">
                            <input type="text" placeholder="{{'ENTER_POSTAL_CODE' | translate}}" class="form-control"
                                   name="title"
                                   ng-model="newSupplierVm.newSupplier.postalCode">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PHONE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_PHONE_NUMBER' | translate}}"
                                   ng-model="newSupplierVm.newSupplier.phoneNumber"
                                    >
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MOBILE_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_MOBILE_NUMBER' | translate}}"
                                   ng-model="newSupplierVm.newSupplier.mobileNumber"
                                    >
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>FAX_NUMBER</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_FAX_NUMBER' | translate}}"
                                   ng-model="newSupplierVm.newSupplier.faxNumber"
                                    >
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>EMAIL</span> 
                            <span class="asterisk">*</span> : </label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_EMAIL' | translate}}"
                                   ng-model="newSupplierVm.newSupplier.email">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>WEBSITE</span>: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_WEBSIT' | translate}}"
                                   ng-model="newSupplierVm.newSupplier.webSite">


                        </div>
                    </div>

                    <attributes-view show-objects="selectObjectValues" ng-if="newSupplierVm.customAttributes.length > 0"
                                     attributes="newSupplierVm.customAttributes"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newSupplierVm.attributes.length > 0"
                                     attributes="newSupplierVm.attributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
