<style>
    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        z-index: 10 !important;
    }

    .medium-image {
        max-height: 378px;
        width: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .col-sm-5 .editable-textarea {
        width: 450px;
    }
</style>
<div ng-if="ppapBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_PPAP_BASIC_DETAILS</span>
    </span>
    <br/>
</div>
<div class="row row-eq-height">
    <div class="col-sm-12">
        <div class="item-details" style="padding: 30px">

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>PPAP_NUMBER</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{ppapBasicVm.ppap.number}}</span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>PPAP_TYPE</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{ppapBasicVm.ppap.objectType}}</span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>PPAP_NAME</span> :
                </div>
                <div class="value col-xs-4 col-sm-5"
                     ng-class="{'cursor-override': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED'}"
                     title="{{hasPermission('ppap','edit') ? '' : noPermission}}">
                    <a href="" e-style="width:250px"
                       ng-class="{'permission-text-disabled': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED'}"
                       onaftersave="ppapBasicVm.updatePpap()"
                       editable-text="ppapBasicVm.ppap.name">
                        <span ng-bind-html="ppapBasicVm.ppap.name"></span>
                    </a>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>DESCRIPTION</span> :
                </div>
                <div class="value col-xs-4 col-sm-5"
                     ng-class="{'cursor-override': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED'}"
                     title="{{hasPermission('ppap','edit') ? '' : noPermission}}">

                    <a href=""
                       ng-class="{'permission-text-disabled': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED'}"
                       onaftersave="ppapBasicVm.updatePpap()"
                       editable-textarea="ppapBasicVm.ppap.description"><span ng-bind-html="(ppapBasicVm.ppap.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{ppapBasicVm.ppap.description}}"></span>
                    </a>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>SUPPLIER</span> :
                </div>
                <div class="value col-xs-8 col-sm-9"
                     ng-class="{'cursor-override': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED'}"
                     title="{{hasPermission('ppap','edit') ? '' : noPermission}}">
                    <a href="" e-style="width: 250px"
                       ng-class="{'permission-text-disabled': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED' || ppapCountNumber > 0}"
                       ng-if="hasPermission('admin','all')"
                       onaftersave="ppapBasicVm.updateSupplier(ppapBasicVm.ppap.supplier)"
                       editable-select="ppapBasicVm.ppap.supplier"
                       title="Select Supplier"
                       e-ng-options="supplier.id as supplier.name for supplier in ppapBasicVm.suppliers | orderBy:'name' track by supplier.id">
                        {{ppapBasicVm.ppap.supplierName}}
                    </a>
                    <span ng-if="!hasPermission('admin','all')">{{ppapBasicVm.ppap.supplierName}}</span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>SUPPLIER_PART</span> :
                </div>
                <div class="value col-xs-8 col-sm-9"
                     ng-class="{'cursor-override': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED'}"
                     title="{{hasPermission('ppap','edit') ? '' : noPermission}}">
                    <a href="#" e-style="width: 250px"
                       ng-class="{'permission-text-disabled': !hasPermission('ppap','edit') || ppap.status.phaseType == 'RELEASED' || ppapCountNumber > 0}"
                       ng-if="hasPermission('admin','all') "
                       onaftersave="ppapBasicVm.updatePpap()"
                       editable-select="ppapBasicVm.ppap.supplierPart"
                       title="Edit SupplierPart"
                       e-ng-options="part.id as part.manufacturerPart.partName for part in ppapBasicVm.supplierParts | orderBy:'partName' track by part.id">
                        {{ppapBasicVm.ppap.mfrPart.partName}}
                    </a>
            <span ng-if="ppapBasicVm.ppap.mfrPart.partName == null">
                <a href="#" e-style="width: 250px"
                   ng-if="hasPermission('admin','all')  "
                   onaftersave="ppapBasicVm.updatePpap()"
                   editable-select="ppapBasicVm.ppap.supplierPart"
                   title="Edit SupplierPart"
                   e-ng-options="part.id as part.manufacturerPart.partName for part in ppapBasicVm.supplierParts | orderBy:'partName' track by part.id">
                    Select Supplier Part
                </a>
            </span>
                    <span ng-if="!hasPermission('admin','all')">{{ppapBasicVm.ppap.mfrPart.partName}}</span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>STATUS</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <ppap-status object="ppapBasicVm.ppap"></ppap-status>
                </div>
            </div>


            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>CREATED_BY</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{ppapBasicVm.ppap.createdByObject.fullName}}</span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>CREATED_DATE</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{ppapBasicVm.ppap.createdDate}}</span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>MODIFIED_BY</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{ppapBasicVm.ppap.modifiedByObject.fullName}}</span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>MODIFIED_DATE</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{ppapBasicVm.ppap.modifiedDate}}</span>
                </div>
            </div>
            <basic-attribute-details-view object-type="QUALITY"
                                          quality-type="PPAP"
                                          has-permission="hasPermission('ppap','edit') && ppap.status.phaseType != 'REJECTED'"
                                          object-id="ppap.id"></basic-attribute-details-view>

            <object-attribute-details-view object-type="PPAPTYPE" show-attributes="true"
                                           has-permission="hasPermission('ppap','edit') && ppap.status.phaseType != 'REJECTED'"
                                           object-type-id="ppap.type.id"
                                           object-id="ppap.id"></object-attribute-details-view>
        </div>
    </div>
</div>