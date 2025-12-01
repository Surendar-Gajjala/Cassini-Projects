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
<div ng-if="equipmentBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_EQUIPMENT_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="equipmentBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="equipmentBasicVm.equipment.image != null && equipmentBasicVm.equipment.image != ''" href=""
               ng-click="equipmentBasicVm.showImage(equipmentBasicVm.equipment)"
               title="{{equipmentBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{equipmentBasicVm.equipment.imagePath}}">
            </a>

            <div ng-if="equipmentBasicVm.equipment.image == null"
                 ng-class="{'cursor-override': !hasPermission('equipment','edit')}"
                 title="{{hasPermission('equipment','edit') ? '' : noPermission}}"
                 class="no-thumbnail">
                <a href="" ng-class="{'permission-text-disabled': !hasPermission('equipment','edit')}"
                   onclick="$('#imageFile').click()"
                   title="{{addImage}}">
                    <span translate>ADD_IMAGE</span>
                </a>
                <input style="display: none"
                       id="imageFile"
                       type="file" ng-file-model="equipmentBasicVm.image" accept="image/*"
                       onchange="angular.element(this).scope().saveImage(this.files[0])">
            </div>
            <div id="item-thumbnail-basic{{equipmentBasicVm.equipmentId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{equipmentBasicVm.equipmentId}}">
                            <div id="thumbnail-image-basic{{equipmentBasicVm.equipmentId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{equipmentBasicVm.equipment.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{equipmentBasicVm.equipmentId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="equipmentBasicVm.equipment.image != null && equipmentBasicVm.equipment.image != ''"
             ng-class="{'cursor-override': !hasPermission('equipment','edit')}"
             title="{{hasPermission('equipment','edit') ? '' : noPermission}}"
             style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px;">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('equipment','edit')}"
               onclick="$('#imageUpdateFile').click()"
               title="{{updateImageTitle}}">
                <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
            </a>
            <a href="" title="{{removeImageTitle}}" ng-click="equipmentBasicVm.deleteImage()"
               style="float: right;">
                <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
            </a>
            <input style="display: none"
                   id="imageUpdateFile"
                   type="file" ng-file-model="equipmentBasicVm.image" accept="image/*"
                   onchange="angular.element(this).scope().saveImage(this.files[0])">
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EQUIPMENT_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{equipmentBasicVm.equipment.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>EQUIPMENT_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{equipmentBasicVm.equipment.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('equipment','edit')}"
             title="{{hasPermission('equipment','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('equipment','edit')}"
               onaftersave="equipmentBasicVm.updateEquipment()"
               editable-text="equipmentBasicVm.equipment.name">
                <span ng-bind-html="equipmentBasicVm.equipment.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('equipment','edit')}"
             title="{{hasPermission('equipment','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('equipment','edit')}"
               onaftersave="equipmentBasicVm.updateEquipment()"
               editable-textarea="equipmentBasicVm.equipment.description"><span ng-bind-html="(equipmentBasicVm.equipment.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{equipmentBasicVm.equipment.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('equipment','edit')}"
             title="{{hasPermission('equipment','edit') ? '' : noPermission}}">
            <span ng-if="equipmentBasicVm.equipment.active == true && !equipmentBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="equipmentBasicVm.equipment.active == false && !equipmentBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!equipmentBasicVm.editStatus"
               ng-class="{'permission-text-disabled': !hasPermission('equipment','edit')}"
               ng-click="equipmentBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="equipmentBasicVm.editStatus">
                <ui-select ng-model="equipmentBasicVm.equipment.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in equipmentBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="equipmentBasicVm.updateEquipment()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="equipmentBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('equipment','edit')}"
             title="{{hasPermission('equipment','edit') ? '' : noPermission}}">
            <span ng-if="equipmentBasicVm.equipment.requiresMaintenance && !equipmentBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!equipmentBasicVm.equipment.requiresMaintenance && !equipmentBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" ng-if="!equipmentBasicVm.editMaintenance" class="fa fa-pencil row-edit-btn"
               ng-class="{'permission-text-disabled': !hasPermission('equipment','edit')}"
               ng-click="equipmentBasicVm.changeMaintenance()"
               title="{{equipmentBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="equipmentBasicVm.editMaintenance">
                <ui-select ng-model="equipmentBasicVm.equipment.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in equipmentBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="equipmentBasicVm.updateEquipment()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="equipmentBasicVm.cancelMaintenance()">
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
            <span>{{equipmentBasicVm.equipment.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{equipmentBasicVm.equipment.createDateDe}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{equipmentBasicVm.equipment.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{equipmentBasicVm.equipment.modifiedDate}}</span>
        </div>
    </div>
    <div ng-if="equipmentBasicVm.equipment.manufacturerData != null">
        <mes-mfr-data update-type="'update'" has-permission="true"
                      update-manufacturer="equipmentBasicVm.updateEquipment"
                      manufacturer-data="equipmentBasicVm.equipment.manufacturerData"></mes-mfr-data>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{equipment.objectType}}"
                                   has-permission="true"
                                   object-type-id="equipment.type.id"
                                   object-id="equipment.id"></object-attribute-details-view>
</div>
