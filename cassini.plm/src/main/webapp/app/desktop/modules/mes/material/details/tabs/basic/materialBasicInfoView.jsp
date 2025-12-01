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
<div ng-if="materialBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
          <span translate>LOADING_MATERIAL_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="materialBasicVm.loading == false">
    <div class="thumbnail-container">
        <div>
            <a ng-if="materialBasicVm.material.image != null && materialBasicVm.material.image != ''" href=""
               ng-click="materialBasicVm.showImage(materialBasicVm.material)"
               title="{{materialBasicVm.titleImage}}">
                <img class="medium-image" ng-src="{{materialBasicVm.material.imagePath}}">
            </a>

            <div ng-if="materialBasicVm.material.image == null"
                 ng-class="{'cursor-override': !hasPermission('material','edit')}"
                 title="{{hasPermission('material','edit') ? '' : noPermission}}"
                 class="no-thumbnail">
                <a href=""
                   onclick="$('#imageFile').click()"
                   title="{{addImage}}">
                    <span translate>ADD_IMAGE</span>
                </a>
                <input style="display: none"
                       id="imageFile"
                       type="file" ng-file-model="materialBasicVm.image" accept="image/*"
                       onchange="angular.element(this).scope().saveImage(this.files[0])">
            </div>
            <div id="item-thumbnail-basic{{materialBasicVm.materialId}}" class="item-thumbnail modal">
                <div class="item-thumbnail-content">
                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                        <div class="thumbnail-view" id="thumbnail-view-basic{{materialBasicVm.materialId}}">
                            <div id="thumbnail-image-basic{{materialBasicVm.materialId}}"
                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                <img ng-src="{{materialBasicVm.material.imagePath}}"
                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{materialBasicVm.materialId}}"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div ng-if="materialBasicVm.material.image != null && materialBasicVm.material.image != ''"
             ng-class="{'cursor-override': !hasPermission('material','edit')}"
             title="{{hasPermission('material','edit') ? '' : noPermission}}"
             style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px;">
            <a href=""
               onclick="$('#imageUpdateFile').click()"
               title="{{updateImageTitle}}">
                <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
            </a>
            <a href="" title="{{removeImageTitle}}" ng-click="materialBasicVm.deleteImage()"
               style="float: right;">
                <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
            </a>
            <input style="display: none"
                   id="imageUpdateFile"
                   type="file" ng-file-model="materialBasicVm.image" accept="image/*"
                   onchange="angular.element(this).scope().saveImage(this.files[0])">
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{materialBasicVm.material.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('material','edit')}"
             title="{{hasPermission('material','edit') ? '' : noPermission}}">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('material','edit')}"
               e-style="width:250px" onaftersave="materialBasicVm.updateMaterial()"
               editable-text="materialBasicVm.material.name">
                <span ng-bind-html="materialBasicVm.material.name || 'ADD_NAME' | translate"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>TYPE</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <span>{{materialBasicVm.material.type.name}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('material','edit')}"
             title="{{hasPermission('material','edit') ? '' : noPermission}}">
            <a href="" ng-class="{'permission-text-disabled': !hasPermission('material','edit')}"
               onaftersave="materialBasicVm.updateMaterial()"
               editable-textarea="materialBasicVm.material.description"><span ng-bind-html="(materialBasicVm.material.description) || 'ADD_DESCRIPTION' |
                translate" title="{{materialBasicVm.material.description}}"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MEASUREMENT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('material','edit')}"
             title="{{hasPermission('material','edit') ? '' : noPermission}}">
            <a href="#" e-style="width: 250px" ng-class="{'permission-text-disabled': !hasPermission('material','edit')}"
               onaftersave="materialBasicVm.updateMaterial()"
               editable-select="materialBasicVm.material.qomObject"
               title="{{ecrBasicVm.clickToUpdatePerson}}"
               e-ng-options="measurement as measurement.name for measurement in materialBasicVm.measurements track by measurement.id">
                {{materialBasicVm.material.qomObject.name}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MEASUREMENT_UNIT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('material','edit')}"
             title="{{hasPermission('material','edit') ? '' : noPermission}}">
            <a href="#" e-style="width: 250px" ng-class="{'permission-text-disabled': !hasPermission('material','edit')}"
               onaftersave="materialBasicVm.updateMaterial()"
               editable-select="materialBasicVm.material.uomObject"
               title="{{ecrBasicVm.clickToUpdatePerson}}"
               e-ng-options="unit as unit.name for unit in materialBasicVm.material.qomObject.measurementUnits track by unit.id">
                {{materialBasicVm.material.uomObject.name || 'ADD_MEASUREMENT_UNIT' | translate}}
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{materialBasicVm.material.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{materialBasicVm.material.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{materialBasicVm.material.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{materialBasicVm.material.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="MESOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{material.objectType}}"
                                   has-permission="true"
                                   object-type-id="material.type.id"
                                   object-id="material.id"></object-attribute-details-view>
</div>
