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
<div ng-if="jigsAndFixturesBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
         <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_JIGSANDFIXTURE_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="jigsAndFixturesBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="jigsAndFixturesBasicVm.jigsFixture.image != null && jigsAndFixturesBasicVm.jigsFixture.image != ''"
               href=""
               ng-click="jigsAndFixturesBasicVm.showImage(jigsAndFixturesBasicVm.jigsFixture)"
               title="{{jigsAndFixturesBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{jigsAndFixturesBasicVm.jigsFixture.imagePath}}">
            </a>

            <div ng-if="jigsAndFixturesBasicVm.jigsFixture.image == null"
                 ng-class="{'cursor-override': !hasPermission('jigfixture','edit')}"
                 title="{{hasPermission('jigfixture','edit') ? '' : noPermission}}"
                 class="no-thumbnail">
                <a href="" ng-class="{'permission-text-disabled': !hasPermission('jigfixture','edit')}"
                   onclick="$('#imageFile').click()"
                   title="{{addImage}}">
                    <span translate>ADD_IMAGE</span>
                </a>
                <input style="display: none"
                       id="imageFile"
                       type="file" ng-file-model="jigsAndFixturesBasicVm.image" accept="image/*"
                       onchange="angular.element(this).scope().saveImage(this.files[0])">
            </div>
            <div id="item-thumbnail-basic{{jigsAndFixturesBasicVm.jigsFixId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{jigsAndFixturesBasicVm.jigsFixId}}">
                            <div id="thumbnail-image-basic{{jigsAndFixturesBasicVm.jigsFixId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{jigsAndFixturesBasicVm.jigsFixture.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{jigsAndFixturesBasicVm.jigsFixId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="jigsAndFixturesBasicVm.jigsFixture.image != null && jigsAndFixturesBasicVm.jigsFixture.image != ''"
             ng-class="{'cursor-override': !hasPermission('jigfixture','edit')}"
             title="{{hasPermission('jigfixture','edit') ? '' : noPermission}}"
             style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px;">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('jigfixture','edit')}"
               onclick="$('#imageUpdateFile').click()"
               title="{{updateImageTitle}}">
                <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
            </a>
            <a href="" title="{{removeImageTitle}}" ng-click="jigsAndFixturesBasicVm.deleteImage()"
               style="float: right;">
                <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
            </a>
            <input style="display: none"
                   id="imageUpdateFile"
                   type="file" ng-file-model="jigsAndFixturesBasicVm.image" accept="image/*"
                   onchange="angular.element(this).scope().saveImage(this.files[0])">
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{jigsAndFixturesBasicVm.jigsFixture.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{jigsAndFixturesBasicVm.jigsFixture.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('jigfixture','edit')}"
             title="{{hasPermission('jigfixture','edit') ? '' : noPermission}}">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('jigfixture','edit')}"
               e-style="width:250px"
               onaftersave="jigsAndFixturesBasicVm.updateJigsFixture()"
               editable-text="jigsAndFixturesBasicVm.jigsFixture.name"
                    ><span ng-bind-html="jigsAndFixturesBasicVm.jigsFixture.name || 'ADD_NAME' |
                translate"></span></a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('jigfixture','edit')}"
             title="{{hasPermission('jigfixture','edit') ? '' : noPermission}}">

            <a href="" ng-class="{'permission-text-disabled': !hasPermission('jigfixture','edit')}"
               onaftersave="jigsAndFixturesBasicVm.updateJigsFixture()"
               editable-textarea="jigsAndFixturesBasicVm.jigsFixture.description"><span ng-bind-html="(jigsAndFixturesBasicVm.jigsFixture.description) || 'ADD_DESCRIPTION' |
                translate" title="{{jigsAndFixturesBasicVm.jigsFixture.description}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('jigfixture','edit')}"
             title="{{hasPermission('jigfixture','edit') ? '' : noPermission}}">
            <span ng-if="jigsAndFixturesBasicVm.jigsFixture.active == true && !jigsAndFixturesBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="jigsAndFixturesBasicVm.jigsFixture.active == false && !jigsAndFixturesBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!jigsAndFixturesBasicVm.editStatus"
               ng-class="{'permission-text-disabled': !hasPermission('jigfixture','edit')}"
               ng-click="jigsAndFixturesBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="jigsAndFixturesBasicVm.editStatus">
                <ui-select ng-model="jigsAndFixturesBasicVm.jigsFixture.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in jigsAndFixturesBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="jigsAndFixturesBasicVm.updateJigsFixture()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="jigsAndFixturesBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('jigfixture','edit')}"
             title="{{hasPermission('jigfixture','edit') ? '' : noPermission}}">
            <span ng-if="jigsAndFixturesBasicVm.jigsFixture.requiresMaintenance == true && !jigsAndFixturesBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="jigsAndFixturesBasicVm.jigsFixture.requiresMaintenance == false && !jigsAndFixturesBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" ng-if="!jigsAndFixturesBasicVm.editMaintenance" class="fa fa-pencil row-edit-btn"
               ng-class="{'permission-text-disabled': !hasPermission('jigfixture','edit')}"
               ng-click="jigsAndFixturesBasicVm.changeMaintenance()"
               title="{{jigsAndFixturesBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="jigsAndFixturesBasicVm.editMaintenance">
                <ui-select ng-model="jigsAndFixturesBasicVm.jigsFixture.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in jigsAndFixturesBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="jigsAndFixturesBasicVm.updateJigsFixture()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="jigsAndFixturesBasicVm.cancelMaintenance()">
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
            <span>{{jigsAndFixturesBasicVm.jigsFixture.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{jigsAndFixturesBasicVm.jigsFixture.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{jigsAndFixturesBasicVm.jigsFixture.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{jigsAndFixturesBasicVm.jigsFixture.modifiedDate}}</span>
        </div>
    </div>
    <div ng-if="jigsAndFixturesBasicVm.jigsFixture.manufacturerData != null">
        <mes-mfr-data update-type="'update'" has-permission="true"
                      update-manufacturer="jigsAndFixturesBasicVm.updateJigsFixture"
                      manufacturer-data="jigsAndFixturesBasicVm.jigsFixture.manufacturerData"></mes-mfr-data>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{jigFixture.objectType}}"
                                   has-permission="true"
                                   object-type-id="jigFixture.type.id"
                                   object-id="jigFixture.id"></object-attribute-details-view>
</div>
