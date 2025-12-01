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
<div ng-if="toolBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5" translate>
      <span translate>LOADING_TOOL_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="toolBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="toolBasicVm.tool.image != null && toolBasicVm.tool.image != ''" href=""
               ng-click="toolBasicVm.showImage(toolBasicVm.tool)"
               title="{{toolBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{toolBasicVm.tool.imagePath}}">
            </a>

            <div ng-if="toolBasicVm.tool.image == null" ng-class="{'cursor-override': !hasPermission('tool','edit')}"
                 title="{{hasPermission('tool','edit') ? '' : noPermission}}"
                 class="no-thumbnail">
                <a href=""
                   onclick="$('#imageFile').click()"
                   title="{{addImage}}">
                    <span translate>ADD_IMAGE</span>
                </a>
                <input style="display: none"
                       id="imageFile"
                       type="file" ng-file-model="toolBasicVm.image" accept="image/*"
                       onchange="angular.element(this).scope().saveImage(this.files[0])">
            </div>
            <div id="item-thumbnail-basic{{toolBasicVm.toolId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{toolBasicVm.toolId}}">
                            <div id="thumbnail-image-basic{{toolBasicVm.toolId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{toolBasicVm.tool.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{toolBasicVm.toolId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="toolBasicVm.tool.image != null && toolBasicVm.tool.image != ''"
             ng-class="{'cursor-override': !hasPermission('tool','edit')}"
             title="{{hasPermission('tool','edit') ? '' : noPermission}}"
             style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px;">
            <a href=""
               onclick="$('#imageUpdateFile').click()"
               title="{{updateImageTitle}}">
                <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
            </a>
            <a href="" title="{{removeImageTitle}}" ng-click="toolBasicVm.deleteImage()"
               style="float: right;">
                <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
            </a>
            <input style="display: none"
                   id="imageUpdateFile"
                   type="file" ng-file-model="toolBasicVm.image" accept="image/*"
                   onchange="angular.element(this).scope().saveImage(this.files[0])">
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{toolBasicVm.tool.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{toolBasicVm.tool.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('tool','edit')}"
             title="{{hasPermission('tool','edit') ? '' : noPermission}}">
            <a href="" onaftersave="toolBasicVm.updateTool()" ng-class="{'permission-text-disabled': !hasPermission('tool','edit')}"
               editable-text="toolBasicVm.tool.name">
                <span ng-bind-html="toolBasicVm.tool.name"
                      title="{{toolBasicVm.tool.name}}"></span>
            </a>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('tool','edit')}"
             title="{{hasPermission('tool','edit') ? '' : noPermission}}">
            <a href="" onaftersave="toolBasicVm.updateTool()" ng-class="{'permission-text-disabled': !hasPermission('tool','edit')}"
               editable-textarea="toolBasicVm.tool.description">
                <span ng-bind-html="(toolBasicVm.tool.description) || 'ADD_DESCRIPTION' | translate"
                      title="{{toolBasicVm.tool.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('tool','edit')}"
             title="{{hasPermission('tool','edit') ? '' : noPermission}}">
            <span ng-if="toolBasicVm.tool.active == true && !toolBasicVm.editStatus"
                  class="label label-outline bg-light-success" translate>C_ACTIVE</span>
            <span ng-if="toolBasicVm.tool.active == false && !toolBasicVm.editStatus"
                  class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!toolBasicVm.editStatus"
               ng-class="{'permission-text-disabled': !hasPermission('tool','edit')}"
               ng-click="toolBasicVm.changeStatus()" title="{{EDIT | translate}}"></a>

            <div style="display: flex;" ng-if="toolBasicVm.editStatus">
                <ui-select ng-model="toolBasicVm.tool.active" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="status.value as status in toolBasicVm.statuses | filter: $select.search">
                        <div>{{status.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="toolBasicVm.updateTool()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="toolBasicVm.cancelStatus()">
                    <i class="fa fa-times"></i>
                </button>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUIRESMAINTENANCE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('tool','edit')}"
             title="{{hasPermission('tool','edit') ? '' : noPermission}}">
            <span ng-if="toolBasicVm.tool.requiresMaintenance && !toolBasicVm.editMaintenance"
                  class="label label-outline bg-light-success" translate>YES</span>
            <span ng-if="!toolBasicVm.tool.requiresMaintenance && !toolBasicVm.editMaintenance"
                  class="label label-outline bg-light-danger" translate>NO</span>
            <a href="" class="fa fa-pencil row-edit-btn" ng-if="!toolBasicVm.editMaintenance"
               ng-class="{'permission-text-disabled': !hasPermission('tool','edit')}"
               ng-click="toolBasicVm.changeMaintenance()"
               title="{{toolBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
            </a>

            <div style="display: flex;" ng-if="toolBasicVm.editMaintenance">
                <ui-select ng-model="toolBasicVm.tool.requiresMaintenance" theme="bootstrap"
                           style="width:200px">
                    <ui-select-match placeholder="Select">{{$select.selected.label}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="maintenance.value as maintenance in toolBasicVm.maintenances | filter: $select.search">
                        <div>{{maintenance.label}}</div>
                    </ui-select-choices>
                </ui-select>
                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                        ng-click="toolBasicVm.updateTool()">
                    <i class="fa fa-check"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                        ng-click="toolBasicVm.cancelMaintenance()">
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
            <span>{{toolBasicVm.tool.createdByPerson.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{toolBasicVm.tool.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{toolBasicVm.tool.modifiedByPerson.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{toolBasicVm.tool.modifiedDate}}</span>
        </div>
    </div>

    <div ng-if="toolBasicVm.tool.manufacturerData != null">
        <mes-mfr-data update-type="'update'" has-permission="true"
                      update-manufacturer="toolBasicVm.updateTool"
                      manufacturer-data="toolBasicVm.tool.manufacturerData"></mes-mfr-data>
    </div>
    <%--<basic-attribute-details-view object-type="CHANGE"
                                  quality-type="CHANGE"
                                  has-permission="hasPermission('permission.dco.edit') && !dco.isReleased && dco.statusType != 'REJECTED'"
                                  object-id="dco.id"></basic-attribute-details-view>--%>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{tool.objectType}}"
                                   has-permission="true"
                                   object-type-id="tool.type.id"
                                   object-id="tool.id"></object-attribute-details-view>

</div>
