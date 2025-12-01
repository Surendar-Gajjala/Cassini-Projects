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
<div ng-if="instrumentBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
             <span translate>LOADING_INSTRUMENT_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="instrumentBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="instrumentBasicVm.instrument.image != null && instrumentBasicVm.instrument.image != ''" href=""
               ng-click="instrumentBasicVm.showImage(instrumentBasicVm.instrument)"
               title="{{instrumentBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{instrumentBasicVm.instrument.imagePath}}">
            </a>

            <div ng-if="instrumentBasicVm.instrument.image == null"
                 ng-class="{'cursor-override': !hasPermission('instrument','edit')}"
                 title="{{hasPermission('instrument','edit') ? '' : noPermission}}"
                 class="no-thumbnail">
                <a href="" ng-class="{'permission-text-disabled': !hasPermission('instrument','edit')}"
                   onclick="$('#imageFile').click()"
                   title="{{addImage}}">
                    <span translate>ADD_IMAGE</span>
                </a>
                <input style="display: none"
                       id="imageFile"
                       type="file" ng-file-model="instrumentBasicVm.image" accept="image/*"
                       onchange="angular.element(this).scope().saveImage(this.files[0])">
            </div>
            <div id="item-thumbnail-basic{{instrumentBasicVm.instrumentId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{instrumentBasicVm.instrumentId}}">
                            <div id="thumbnail-image-basic{{instrumentBasicVm.instrumentId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{instrumentBasicVm.instrument.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{instrumentBasicVm.instrumentId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="instrumentBasicVm.instrument.image != null && instrumentBasicVm.instrument.image != ''"
             ng-class="{'cursor-override': !hasPermission('instrument','edit')}"
             title="{{hasPermission('instrument','edit') ? '' : noPermission}}"
             style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px;">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('instrument','edit')}"
               onclick="$('#imageUpdateFile').click()"
               title="{{updateImageTitle}}">
                <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
            </a>
            <a href="" title="{{removeImageTitle}}" ng-click="instrumentBasicVm.deleteImage()"
               style="float: right;">
                <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
            </a>
            <input style="display: none"
                   id="imageUpdateFile"
                   type="file" ng-file-model="instrumentBasicVm.image" accept="image/*"
                   onchange="angular.element(this).scope().saveImage(this.files[0])">
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>INSTRUMENT_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{instrumentBasicVm.instrument.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>INSTRUMENT_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{instrumentBasicVm.instrument.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('instrument','edit')}"
             title="{{hasPermission('instrument','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px" ng-class="{'permission-text-disabled': !hasPermission('instrument','edit')}"
               onaftersave="instrumentBasicVm.updateInstrument()"
               editable-text="instrumentBasicVm.instrument.name">
                <span ng-bind-html="instrumentBasicVm.instrument.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('instrument','edit')}"
             title="{{hasPermission('instrument','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('instrument','edit')}"
               onaftersave="instrumentBasicVm.updateInstrument()"
               editable-textarea="instrumentBasicVm.instrument.description"><span ng-bind-html="(instrumentBasicVm.instrument.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{instrumentBasicVm.instrument.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('instrument','edit')}"
             title="{{hasPermission('instrument','edit') ? '' : noPermission}}">
            <span ng-if="instrumentBasicVm.instrument.active == true && !instrumentBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="instrumentBasicVm.instrument.active == false && !instrumentBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!instrumentBasicVm.editStatus"
               ng-class="{'permission-text-disabled': !hasPermission('instrument','edit')}"
               ng-click="instrumentBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="instrumentBasicVm.editStatus">
                <ui-select ng-model="instrumentBasicVm.instrument.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in instrumentBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="instrumentBasicVm.updateInstrument()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="instrumentBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('instrument','edit')}"
             title="{{hasPermission('instrument','edit') ? '' : noPermission}}">
            <span ng-if="instrumentBasicVm.instrument.requiresMaintenance && !instrumentBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!instrumentBasicVm.instrument.requiresMaintenance && !instrumentBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!instrumentBasicVm.editMaintenance"
               ng-click="instrumentBasicVm.changeMaintenance()"
               ng-class="{'permission-text-disabled': !hasPermission('instrument','edit')}"
               title="{{instrumentBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="instrumentBasicVm.editMaintenance">
                <ui-select ng-model="instrumentBasicVm.instrument.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in instrumentBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="instrumentBasicVm.updateInstrument()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="instrumentBasicVm.cancelMaintenance()">
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
            <span>{{instrumentBasicVm.instrument.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{instrumentBasicVm.instrument.createDateDe}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{instrumentBasicVm.instrument.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{instrumentBasicVm.instrument.modifiedDate}}</span>
        </div>
    </div>
    <div ng-if="instrumentBasicVm.instrument.manufacturerData != null">
        <mes-mfr-data update-type="'update'" has-permission="true"
                      update-manufacturer="instrumentBasicVm.updateInstrument"
                      manufacturer-data="instrumentBasicVm.instrument.manufacturerData"></mes-mfr-data>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{instrument.objectType}}"
                                   has-permission="true"
                                   object-type-id="instrument.type.id"
                                   object-id="instrument.id"></object-attribute-details-view>
</div>
