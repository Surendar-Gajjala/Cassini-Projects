<style>

    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }
</style>

<div ng-if="customerBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_CUSTOMER_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="customerBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href=""
               e-style="width:250px" onaftersave="customerBasicVm.updateCustomer()"
               editable-text="customerBasicVm.customer.name">
                <span ng-bind-html="customerBasicVm.customer.name || 'EDIT_NAME' | translate"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PHONE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href=""
               e-style="width:250px" onaftersave="customerBasicVm.updateCustomer()"
               editable-text="customerBasicVm.customer.phone">
                <span ng-bind-html="customerBasicVm.customer.phone || 'ADD_PHONE_NUMBER' | translate"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EMAIL</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href=""
               e-style="width:250px" onaftersave="customerBasicVm.updateCustomer()"
               editable-text="customerBasicVm.customer.email">
                <span ng-bind-html="customerBasicVm.customer.email || 'ADD_NAME' | translate"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>ADDRESS</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href=""
               onaftersave="customerBasicVm.updateCustomer()"
               editable-textarea="customerBasicVm.customer.address">
                <span ng-bind-html="customerBasicVm.customer.address || 'ADD_ADDRESS' | translate"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NOTES</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href=""
               onaftersave="customerBasicVm.updateCustomer()"
               editable-textarea="customerBasicVm.customer.notes"><span ng-bind-html="(customerBasicVm.customer.notes ) || 'ADD_NOTES' |
                translate" title="{{customerBasicVm.customer.notes}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CONTACT_PERSON</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <span>{{customerBasicVm.customer.person.fullName}}</span>
        </div>
    </div>
    <div class="row" ng-if="customerBasicVm.customer.contactPersonObject.phoneMobile != null">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PHONE_NUMBER</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <span>{{customerBasicVm.customer.person.phoneMobile}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EMAIL</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <span>{{customerBasicVm.customer.person.email}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customerBasicVm.customer.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customerBasicVm.customer.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customerBasicVm.customer.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{customerBasicVm.customer.modifiedDate}}</span>
        </div>
    </div>
</div>
