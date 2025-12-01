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
<div ng-if="specificationBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_SPECIFICATION_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="specificationBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{specificationBasicVm.specification.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{specificationBasicVm.specification.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('pgcspecification','edit')}"
             title="{{hasPermission('pgcspecification','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('pgcspecification','edit')}"
               onaftersave="specificationBasicVm.updateSpecification()"
               editable-text="specificationBasicVm.specification.name">
                <span ng-bind-html="specificationBasicVm.specification.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('pgcspecification','edit')}"
             title="{{hasPermission('pgcspecification','edit') ? '' : noPermission}}">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('pgcspecification','edit')}"
               onaftersave="specificationBasicVm.updateSpecification()"
               editable-textarea="specificationBasicVm.specification.description">
                <span ng-bind-html="(specificationBasicVm.specification.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{specificationBasicVm.specification.description}}"></span>
            </a>
        </div>

    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span ng-if="specificationBasicVm.specification.active == true && !specificationBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="specificationBasicVm.specification.active == false && !specificationBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!specificationBasicVm.editStatus"
               ng-click="specificationBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="specificationBasicVm.editStatus">
                <ui-select ng-model="specificationBasicVm.specification.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in specificationBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="specificationBasicVm.updateSpecification()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="specificationBasicVm.cancelStatus()">
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
            <span>{{specificationBasicVm.specification.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{specificationBasicVm.specification.createDateDe}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{specificationBasicVm.specification.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{specificationBasicVm.specification.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="PGCOBJECTTYPE" show-attributes="true"
                                    actual-object-type="{{specification.objectType}}"
                                    has-permission="true"
                                   object-type-id="specification.type.id"
                                   object-id="specification.id"></object-attribute-details-view>
</div>
