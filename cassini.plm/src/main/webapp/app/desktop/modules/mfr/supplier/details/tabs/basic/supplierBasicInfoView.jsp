<div ng-if="supplierBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_SUPPLIER_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="supplierBasicVm.loading == false">


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SUPPLIER_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierBasicVm.supplier.supplierType.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
                <span ng-bind-html="supplierBasicVm.supplier.number"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.name">
                <span ng-bind-html="supplierBasicVm.supplier.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-textarea="supplierBasicVm.supplier.description">
                <span ng-bind-html="(supplierBasicVm.supplier.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                      title="{{supplierBasicVm.supplier.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>LIFE_CYCLE_PHASE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <item-status item="supplierBasicVm.supplier"></item-status>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ADDRESS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-textarea="supplierBasicVm.supplier.address"><span ng-bind-html="(supplierBasicVm.supplier.address ) || 'CLICK_TO_ENTER_ADDRESS' |
                translate" title="{{supplierBasicVm.supplier.address}}"></span>
                {{supplierBasicVm.supplier.address.length > 100 ? '...' : ''}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CITY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.city"><span
                    ng-bind-html="(supplierBasicVm.supplier.city) || 'CLICK_TO_ENTER_CITY' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>COUNTRY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.country">
                <span ng-bind-html="(supplierBasicVm.supplier.country) || 'CLICK_TO_ENTER_COUNTRY' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>POSTAL_CODE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.postalCode">
                <span ng-bind-html="(supplierBasicVm.supplier.postalCode) || 'CLICK_TO_ENTER_POSTAL_CODE' |translate"></span>
            </a>

        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PHONE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">

            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.phoneNumber">
                <span ng-bind-html="(supplierBasicVm.supplier.phoneNumber) || 'CLICK_TO_ENTER_PHONE_NUMBER' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MOBILE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.mobileNumber">
                <span
                        ng-bind-html="(supplierBasicVm.supplier.mobileNumber) || 'CLICK_TO_ENTER_MOBILE_NUMBER' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>FAX_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.faxNumber">
                <span
                        ng-bind-html="(supplierBasicVm.supplier.faxNumber) || 'CLICK_TO_ENTER_FAX_ADDRESS' |translate"></span>

            </a>

        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EMAIL</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">

            <a ng-bind-html="(supplierBasicVm.supplier.email) || 'CLICK_TO_ENTER_EMAIL' |translate"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               ng-if="!supplierBasicVm.editEmail"
               ng-click="supplierBasicVm.supplierChangeEmail()"></a>


            <div style="display: flex;" ng-if="supplierBasicVm.editEmail">

                <input style="background-color:  rgb(234, 243, 251) !important;border: 1px solid  rgb(234, 243, 251) !important; padding: 8px 10px;font-size: 14px; width: 250px; "
                       class="form-control" type="text" ng-model="supplierBasicVm.supplier.email">

                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="supplierBasicVm.updateSupplier()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="supplierBasicVm.cancelSupplier()">
                    <i class="fa fa-times"></i>
                </button>
            </div>


        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WEBSITE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mfrsupplier','edit')}"
             title="{{hasPermission('mfrsupplier','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('mfrsupplier','edit')}"
               onaftersave="supplierBasicVm.updateSupplier()"
               editable-text="supplierBasicVm.supplier.webSite"><span
                    ng-bind-html="(supplierBasicVm.supplier.webSite) || 'CLICK_TO_ENTER_NOTES' |translate"></span>
            </a>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierBasicVm.supplier.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierBasicVm.supplier.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierBasicVm.supplier.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierBasicVm.supplier.modifiedDate}}</span>
        </div>
    </div>
    <basic-attribute-details-view object-type="MFRSUPPLIER"
                                  quality-type="MFRSUPPLIER"
                                  has-permission="true"
                                  object-id="supplier.id"></basic-attribute-details-view>

    <object-attribute-details-view object-type="SUPPLIERTYPE"
                                   has-permission="true" show-attributes="true"
                                   object-type-id="supplier.supplierType.id" show-attributes="true"
                                   object-id="supplier.id"></object-attribute-details-view>

</div>
