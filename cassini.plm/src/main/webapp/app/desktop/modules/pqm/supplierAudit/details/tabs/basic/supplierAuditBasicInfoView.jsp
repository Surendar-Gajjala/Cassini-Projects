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
<div ng-if="supplierAuditBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_SUPPLIER_AUDIT_BASIC_DETAILS</span>
    </span>
    <br/>
</div>


<div class="item-details" style="padding: 30px">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SUPPLIER_AUDIT_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierAuditBasicVm.supplierAudit.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SUPPLIER_AUDIT_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierAuditBasicVm.supplierAudit.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SUPPLIER_AUDIT_NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('supplieraudit','edit') || supplierAudit.status.phaseType == 'RELEASED'}"
             title="{{hasPermission('supplieraudit','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('supplieraudit','edit') || supplierAudit.status.phaseType == 'RELEASED'}"
               onaftersave="supplierAuditBasicVm.updateSupplierAudit()"
               editable-text="supplierAuditBasicVm.supplierAudit.name">
                <span ng-bind-html="supplierAuditBasicVm.supplierAudit.name"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('supplieraudit','edit') || supplierAudit.status.phaseType == 'RELEASED'}"
             title="{{hasPermission('supplieraudit','edit') ? '' : noPermission}}">

            <a href=""
               ng-class="{'permission-text-disabled': !hasPermission('supplieraudit','edit') || supplierAudit.status.phaseType == 'RELEASED'}"
               onaftersave="supplierAuditBasicVm.updateSupplierAudit()"
               editable-textarea="supplierAuditBasicVm.supplierAudit.description"><span ng-bind-html="(supplierAuditBasicVm.supplierAudit.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{supplierAuditBasicVm.supplierAudit.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span style="font-size: 14px" translate>ASSIGNED_TO</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" style="line-height: 20px"
             ng-class="{'cursor-override': !hasPermission('supplieraudit','edit') || supplierAudit.status.phaseType == 'RELEASED'}">
            <a href="#" e-style="width: 50%"
               ng-class="{'permission-text-disabled': !hasPermission('supplieraudit','edit') || supplierAudit.status.phaseType == 'RELEASED'}"
               onaftersave="supplierAuditBasicVm.updateSupplierAudit()"
               editable-select="supplierAuditBasicVm.supplierAudit.assignedToObject"
               title="{{supplierAuditBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in supplierAuditBasicVm.persons track by person.id">
                {{supplierAuditBasicVm.supplierAudit.assignedToObject.fullName}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <ppap-status object="supplierAuditBasicVm.supplierAudit"></ppap-status>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>PLANNED_YEAR</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a ng-if="supplierAuditBasicVm.editPlannedYear == false && supplierAuditBasicVm.supplierAudit.status.phaseType == 'PRELIMINARY'"
               href="" e-style="width:250px;"
               ng-click="supplierAuditBasicVm.editPlannedYear = true">
                <span ng-bind-html="supplierAuditBasicVm.supplierAudit.plannedYear || 'CLICK_TO_SET_PLANNED_YEAR' | translate"></span>
            </a>
            <span ng-if="supplierAuditBasicVm.editPlannedYear == false && supplierAuditBasicVm.supplierAudit.status.phaseType != 'PRELIMINARY'"
                  ng-bind-html="supplierAuditBasicVm.supplierAudit.plannedYear || 'CLICK_TO_SET_EFFECTIVE_FROM' | translate"></span>

            <div ng-if="supplierAuditBasicVm.editPlannedYear" style="display: flex;">
                <input class="form-control" year-picker placeholder="{{'SELECT_PLANNED_YEAR' | translate}}"
                       type="text" ng-model="supplierAuditBasicVm.supplierAudit.plannedYear" style="width: 200px;"/>
                <i class="fa fa-times" ng-if="supplierAuditBasicVm.supplierAudit.plannedYear != null"
                   style="position: absolute;margin-top: 10px;margin-left: 185px;cursor: pointer;"
                   ng-click="supplierAuditBasicVm.supplierAudit.plannedYear = null"></i>
                <button class="btn btn-sm btn-primary"
                        ng-click="supplierAuditBasicVm.updateSupplierAudit()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default"
                        ng-click="supplierAuditBasicVm.editPlannedYear = false;supplierAuditBasicVm.supplierAudit.plannedYear = supplierAuditBasicVm.plannedYear">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierAuditBasicVm.supplierAudit.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierAuditBasicVm.supplierAudit.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierAuditBasicVm.supplierAudit.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{supplierAuditBasicVm.supplierAudit.modifiedDate}}</span>
        </div>
    </div>
    <basic-attribute-details-view object-type="QUALITY"
                                  quality-type="SUPPLIERAUDIT"
                                  has-permission="hasPermission('supplieraudit','edit') && supplierAudit.status.phaseType != 'REJECTED'"
                                  object-id="supplierAudit.id"></basic-attribute-details-view>

    <object-attribute-details-view object-type="SUPPLIERAUDITTYPE" show-attributes="true"
                                   has-permission="hasPermission('supplieraudit','edit') && supplierAudit.status.phaseType != 'REJECTED'"
                                   object-type-id="supplierAudit.type.id"
                                   object-id="supplierAudit.id"></object-attribute-details-view>
</div>