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
        width: 378px;
        max-height: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .col-sm-5 .editable-textarea {
        width: 450px;
    }
</style>
<div ng-if="machineBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
      <span translate>LOADING_MACHINE_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="machineBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="machineBasicVm.machine.image != null && machineBasicVm.machine.image != ''" href=""
               ng-click="machineBasicVm.showImage(machineBasicVm.machine)"
               title="{{machineBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{machineBasicVm.machine.imagePath}}">
            </a>

            <div ng-if="machineBasicVm.machine.image == null"
                 ng-class="{'cursor-override': !hasPermission('machine','edit')}"
                 title="{{hasPermission('machine','edit') ? '' : noPermission}}"
                 class="no-thumbnail">
                <a href="" ng-class="{'permission-text-disabled': !hasPermission('machine','edit')}"
                   onclick="$('#imageFile').click()"
                   title="{{addImage}}">
                    <span translate>ADD_IMAGE</span>
                </a>
                <input style="display: none"
                       id="imageFile"
                       type="file" ng-file-model="machineBasicVm.image" accept="image/*"
                       onchange="angular.element(this).scope().saveImage(this.files[0])">
            </div>
            <div id="item-thumbnail-basic{{machineBasicVm.machineId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{machineBasicVm.machineId}}">
                            <div id="thumbnail-image-basic{{machineBasicVm.machineId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{machineBasicVm.machine.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{machineBasicVm.machineId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="machineBasicVm.machine.image != null && machineBasicVm.machine.image != ''"
             ng-class="{'cursor-override': !hasPermission('machine','edit')}"
             title="{{hasPermission('machine','edit') ? '' : noPermission}}"
             style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('machine','edit')}"
               onclick="$('#imageUpdateFile').click()"
               title="{{updateImageTitle}}">
                <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
            </a>
            <a href="" title="{{removeImageTitle}}" ng-click="machineBasicVm.deleteImage()"
               style="float: right;">
                <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
            </a>
            <input style="display: none"
                   id="imageUpdateFile"
                   type="file" ng-file-model="machineBasicVm.image" accept="image/*"
                   onchange="angular.element(this).scope().saveImage(this.files[0])">
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MACHINE_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{machineBasicVm.machine.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MACHINE_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{machineBasicVm.machine.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>WORKCENTER</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-click="machineBasicVm.showWorkCenter(machineBasicVm.machine)"
               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                <span ng-bind-html="machine.workCenterName"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('machine','edit')}"
             title="{{hasPermission('machine','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('machine','edit')}"
               onaftersave="machineBasicVm.updateMachine()"
               editable-text="machineBasicVm.machine.name">
                <span ng-bind-html="machineBasicVm.machine.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('machine','edit')}"
             title="{{hasPermission('machine','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('machine','edit')}"
               onaftersave="machineBasicVm.updateMachine()"
               editable-textarea="machineBasicVm.machine.description">
                <span ng-bind-html="(machineBasicVm.machine.description) || 'CLICK_TO_ENTER_DESCRIPTION' | translate"
                      title="{{machineBasicVm.machine.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('machine','edit')}"
             title="{{hasPermission('machine','edit') ? '' : noPermission}}">
            <span ng-if="machineBasicVm.machine.active == true && !machineBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="machineBasicVm.machine.active == false && !machineBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!machineBasicVm.editStatus"
               ng-class="{'permission-text-disabled': !hasPermission('machine','edit')}"
               ng-click="machineBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="machineBasicVm.editStatus">
                <ui-select ng-model="machineBasicVm.machine.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in machineBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="machineBasicVm.updateMachine()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="machineBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('machine','edit')}"
             title="{{hasPermission('machine','edit') ? '' : noPermission}}">
            <span ng-if="machineBasicVm.machine.requiresMaintenance && !machineBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!machineBasicVm.machine.requiresMaintenance && !machineBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" ng-if="!machineBasicVm.editMaintenance" class="fa fa-pencil row-edit-btn"
               ng-class="{'permission-text-disabled': !hasPermission('machine','edit')}"
               ng-click="machineBasicVm.changeMaintenance()"
               title="{{machineBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="machineBasicVm.editMaintenance">
                <ui-select ng-model="machineBasicVm.machine.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in machineBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="machineBasicVm.updateMachine()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="machineBasicVm.cancelMaintenance()">
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
            <span>{{machineBasicVm.machine.createdByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{machineBasicVm.machine.createDateDe}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{machineBasicVm.machine.modifiedByObject.fullName}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{machineBasicVm.machine.modifiedDate}}</span>
        </div>
    </div>

    <div ng-if="machineBasicVm.machine.manufacturerData != null">
        <mes-mfr-data update-type="'update'" has-permission="true" update-manufacturer="machineBasicVm.updateMachine"
                      manufacturer-data="machineBasicVm.machine.manufacturerData"></mes-mfr-data>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{machine.objectType}}"
                                   has-permission="true"
                                   object-type-id="machine.type.id"
                                   object-id="machine.id"></object-attribute-details-view>
</div>
