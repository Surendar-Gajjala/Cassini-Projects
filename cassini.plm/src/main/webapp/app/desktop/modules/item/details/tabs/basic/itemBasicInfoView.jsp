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

    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
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

    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        z-index: 10 !important;
    }

    .small-thumbnail-container {
        border: 1px solid #ddd;
        height: 150px;
        width: 200px;
        position: absolute;
        background-color: #fff;
        z-index: 10 !important;
        top: 10px;
        right: 10px;
    }

    .thumbnail-container .thumbnail-panel {

    }

    .medium-image {
        max-height: 378px;
        width: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .small-image {
        height: 148px;
        width: 198px;
    }

    .anchorDisable {
        pointer-events: none !important;
        cursor: default !important;
    }


</style>

<div ng-if="itemBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_ITEM_DETAILS</span>
    </span>
    <br/>
</div>
<div ng-if="itemBasicVm.loading == false">
    <style scoped>
        .editable-textarea {
            width: 400px;
            height: 120px;
        }

        .popover-content {
            height: 110px !important;
        }
    </style>
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding-top:30px;">
                <div class="thumbnail-container"
                     ng-class="{'small-thumbnail-container':selectedMasterItemId != null && selectedMasterItemId != ''}">
                    <div>
                        <a ng-if="itemBasicVm.item.thumbnail != null && itemBasicVm.item.thumbnail != ''" href=""
                           ng-click="itemBasicVm.showThumbnailImage(item)"
                           title="{{itemBasicVm.titleImage}}">
                            <img ng-src="{{itemBasicVm.item.thumbnailImage}}"
                                 ng-class="{'medium-image':selectedMasterItemId == null || selectedMasterItemId == '','small-image':selectedMasterItemId != null && selectedMasterItemId != ''}">
                        </a>
                        <%--<a ng-if="(itemBasicVm.item.thumbnail == null || itemBasicVm.item.thumbnail == '') && itemBasicVm.item.itemImageObj != null && itemBasicVm.item.itemImageObj.thumbnail != null"
                           href="">
                            <img src="data:image/png;base64,{{itemBasicVm.item.itemImageObj.thumbnail}}"
                                 ng-click="showAutoDeskFile(item.itemImageObj)"/>
                        </a>--%>

                        <div ng-class="{'no-thumbnail': itemBasicVm.item.thumbnail == null && itemBasicVm.item.itemImageObj == null && (selectedMasterItemId == null || selectedMasterItemId == ''),
                                         'no-small-thumbnail':itemBasicVm.item.thumbnail == null && itemBasicVm.item.itemImageObj == null && (selectedMasterItemId != null && selectedMasterItemId != '')}"
                             ng-if="itemBasicVm.item.thumbnail == null && itemBasicVm.item.itemImageObj == null"
                             style="">
                            <a href=""
                               ng-if="itemBasicVm.addImage == false"
                               onclick="$('#thumbnailFile').click()"
                               title="{{addImage}}">
                                    <span ng-if="editItemPermission && !itemRevision.released && !itemRevision.rejected"
                                          ng-hide="itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id && !adminPermission"
                                          translate>ADD_THUMBNAIL</span>
                            </a>
                            <input style="display: none"
                                   id="thumbnailFile"
                                   type="file" ng-file-model="itemBasicVm.thumbnail" accept="image/*"
                                   onchange="angular.element(this).scope().saveThumbnail(this.files[0])">
                        </div>

                        <%--<div id="myModal21" class="img-model modal">
                            <span class="closeimage">&times;</span>
                            <img class="modal-content" id="img012">
                        </div>--%>
                        <div id="item-thumbnail-basic{{itemBasicVm.itemId}}" class="item-thumbnail modal">
                            <div class="item-thumbnail-content">
                                <div class="thumbnail-content" style="display: flex;width: 100%;">
                                    <div class="thumbnail-view" id="thumbnail-view-basic{{itemBasicVm.itemId}}">
                                        <div id="thumbnail-image-basic{{itemBasicVm.itemId}}"
                                             style="display: table-cell;vertical-align: middle;text-align: center;">
                                            <img ng-src="{{itemBasicVm.item.thumbnailImage}}"
                                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{itemBasicVm.itemId}}"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div ng-if="itemBasicVm.item.thumbnail != null && itemBasicVm.item.thumbnail != ''"
                         ng-hide="!editItemPermission || itemRevision.released || (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id && !adminPermission)"
                         style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px;">
                        <a href=""
                           onclick="$('#thumbnailUpdateFile').click()"
                           title="{{updateThumnailTitle}}">
                            <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
                        </a>
                        <a href="" title="{{removeThumnailTitle}}" ng-click="itemBasicVm.deleteItemThumbnail()"
                           style="float: right;">
                            <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
                        </a>
                        <input style="display: none"
                               id="thumbnailUpdateFile"
                               type="file" ng-file-model="itemBasicVm.thumbnail" accept="image/*"
                               onchange="angular.element(this).scope().saveThumbnail(this.files[0])">
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ITEM_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.itemType.name}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CONFIGURATION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span class="label label-outline bg-light-primary"
                              ng-if="itemBasicVm.item.configurable == false && itemBasicVm.item.configured == false  && !itemBasicVm.editConfiguration"
                              translate>NORMAL</span>
                        <span class="label label-outline bg-light-danger"
                              ng-if="itemBasicVm.item.configurable == true && itemBasicVm.item.configured == false && !itemBasicVm.editConfiguration"
                              translate>CONFIGURABLE</span>
                         <span class="label label-outline bg-light-danger"
                               ng-if="itemBasicVm.item.configurable == false && itemBasicVm.item.configured == true && !itemBasicVm.editConfiguration "
                               translate>CONFIGURED</span>

                        <a href="" class="fa fa-pencil row-edit-btn"
                           ng-if="editItemPermission && itemVm.itemDetails.configuredItems == 0 && itemVm.bomConfigs.length == 0 && !itemBasicVm.editConfiguration && !itemBasicVm.item.configured && itemBasicVm.item.lifeCyclePhase.phaseType == 'PRELIMINARY' && itemBasicVm.itemRevision.revision == '-'"
                           ng-click="itemBasicVm.changeConfiguration()" title="{{EDIT | translate}}"></a>


                        <div style="display: flex;" ng-if="itemBasicVm.editConfiguration">
                            <div>
                                <ui-select ng-model="itemBasicVm.Configuration" theme="bootstrap"
                                           style="width:200px" ng-if="itemBasicVm.editConfiguration">
                                    <ui-select-match placeholder=Select>{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="configurationType in itemBasicVm.configurationTypes | filter: $select.search">
                                        <div ng-bind="configurationType"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                            <div>
                                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                                        ng-click="itemBasicVm.updateItem()">
                                    <i class="fa fa-check"></i>
                                </button>
                                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                                        ng-click="itemBasicVm.cancelConfiguration()">
                                    <i class="fa fa-times"></i>
                                </button>
                            </div>

                        </div>
                    </div>
                </div>


                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ITEM_NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.itemNumber}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ITEM_CLASS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <item-class item="itemBasicVm.item.itemType.itemClass"></item-class>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>ITEM_NAME</span> :
                    </div>
                    <div class="value col-xs-5 col-sm-5" ng-if="external.external == false">
                        <a ng-if="!adminPermission && editItemPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-text="itemBasicVm.item.itemName">{{itemBasicVm.item.itemName}}
                        </a>
                        <a ng-if="adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-text="itemBasicVm.item.itemName">{{itemBasicVm.item.itemName}}
                        </a>
                        <a ng-if="adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-text="itemBasicVm.item.itemName">{{itemBasicVm.item.itemName}}
                        </a>
                        <a ng-if="editItemPermission && itemBasicVm.item.lockObject == false"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-text="itemBasicVm.item.itemName">{{itemBasicVm.item.itemName}}
                        </a>
                        <span ng-if="!adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id) && editItemPermission"
                              ng-hide="itemRevision.released || itemRevision.rejected">
                               {{itemBasicVm.item.itemName}}
                           </span>

                        <div ng-if="(!adminPermission && !editItemPermission) || itemRevision.released || itemRevision.rejected">
                            {{itemBasicVm.item.itemName}}
                        </div>
                    </div>
                    <div class="value col-xs-5 col-sm-5" ng-if="external.external == true">
                        <a ng-if="external.external == true && sharedPermission == 'WRITE'"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-text="itemBasicVm.item.itemName">{{itemBasicVm.item.itemName}}</a>

                        <div ng-if="(external.external == true && sharedPermission == 'READ') || itemRevision.released">
                            {{itemBasicVm.item.itemName}}
                        </div>

                        <div ng-if="sharedPermission == null && external.external == true">
                            {{itemBasicVm.item.itemName}}
                        </div>
                    </div>
                </div>
                <div class="row" ng-if="itemBasicVm.itemRevision.designObject != null">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>Design Object</span> :
                    </div>
                    <div class="value col-xs-5 col-sm-5">
                        <a href=""
                           ui-sref="app.pdm.assemblies.details({assemblyId: itemBasicVm.itemRevision.designObject})"
                           ng-if="itemBasicVm.itemRevision.designObjectRef.objectType === 'PDM_ASSEMBLY' && (hasPermission('pdm_assembly','view') || hasPermission('pdmobject','all'))">{{itemBasicVm.itemRevision.designObjectRef.name}}</a>
                        <span ng-if="itemBasicVm.itemRevision.designObjectRef.objectType === 'PDM_ASSEMBLY' && !(hasPermission('pdm_assembly','view') || hasPermission('pdmobject','all'))">{{itemBasicVm.itemRevision.designObjectRef.name}}</span>
                        <a href=""
                           ui-sref="app.pdm.parts.details({partId: itemBasicVm.itemRevision.designObject})"
                           ng-if="itemBasicVm.itemRevision.designObjectRef.objectType === 'PDM_PART' && (hasPermission('pdm_part','view') || hasPermission('pdmobject','all'))">{{itemBasicVm.itemRevision.designObjectRef.name}}</a>
                        <span ng-if="itemBasicVm.itemRevision.designObjectRef.objectType === 'PDM_PART' && !(hasPermission('pdm_part','view') || hasPermission('pdmobject','all'))">{{itemBasicVm.itemRevision.designObjectRef.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>
                    <div class="value col-xs-5 col-sm-5" ng-if="external.external == false">
                        <a ng-if="!adminPermission && editItemPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href=""
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-textarea="itemBasicVm.item.description"><span
                                ng-bind-html="itemBasicVm.item.descriptionHtml ||'CLICK_TO_ENTER_DESCRIPTION' | translate"></span>
                        </a>
                        <a ng-if="adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id)"
                           href=""
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-textarea="itemBasicVm.item.description">
                            <span ng-bind-html="itemBasicVm.item.descriptionHtml ||'CLICK_TO_ENTER_DESCRIPTION' | translate"></span>
                        </a>
                        <a ng-if="adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href=""
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-textarea="itemBasicVm.item.description"><span
                                ng-bind-html="itemBasicVm.item.descriptionHtml ||'CLICK_TO_ENTER_DESCRIPTION' | translate"></span>
                        </a>
                        <a ng-if="editItemPermission && itemBasicVm.item.lockObject == false"
                           href=""
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-textarea="itemBasicVm.item.description"><span
                                ng-bind-html="(itemBasicVm.item.description ) ||'CLICK_TO_ENTER_DESCRIPTION' | translate"
                                title="{{itemBasicVm.item.description}}"></span>
                        </a>
                        <span ng-if="!adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id) && editItemPermission"
                              ng-hide="itemRevision.released || itemRevision.rejected">
                            <span ng-bind-html="itemBasicVm.item.descriptionHtml"></span>
                        </span>

                        <div ng-if="(!adminPermission && !editItemPermission) || itemRevision.released || itemRevision.rejected">
                            <span ng-bind-html="itemBasicVm.item.descriptionHtml"></span>
                        </div>
                    </div>
                    <div class="value col-xs-5 col-sm-5" ng-if="external.external == true">
                        <a ng-if="sharedPermission == 'WRITE' && external.external == true"
                           href=""
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           onaftersave="itemBasicVm.updateItem()"
                           editable-textarea="itemBasicVm.item.description"><span
                                ng-bind-html="itemBasicVm.item.description ||'CLICK_TO_ENTER_DESCRIPTION' | translate"></span></a>

                        <div ng-if="(external.external == true && sharedPermission == 'READ') || itemRevision.released || itemRevision.rejected">
                            <span ng-bind-html="itemBasicVm.item.description"></span>
                        </div>

                        <div ng-if="external.external == true && sharedPermission == null">
                            <span ng-bind-html="itemBasicVm.item.description"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REVISION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.itemRevision.revision}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>LIFE_CYCLE_PHASE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <item-status item="itemBasicVm.itemRevision"></item-status>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MAKE_BUY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span class="label label-outline bg-light-primary"
                              ng-if="itemBasicVm.item.makeOrBuy == 'MAKE' && !itemBasicVm.editMakeOrBuy">{{item.makeOrBuy}}</span>
                        <span class="label label-outline bg-light-danger"
                              ng-if="!itemBasicVm.editMakeOrBuy && itemBasicVm.item.makeOrBuy == 'BUY'">{{item.makeOrBuy}}</span>

                        <span title="{{ itemMfrParts.length >0 ? ('MAKE_OR_BUY_VALIDATION_HAS_MANUFACTURING_PARTS'|translate): ''}}">
                            <a href="" class="fa fa-pencil row-edit-btn"
                               ng-class="{anchorDisable: itemMfrParts.length >0}"
                               ng-if="itemBasicVm.item.itemType.itemClass != 'PRODUCT' && itemBasicVm.item.itemType.itemClass != 'DOCUMENT' && !itemRevision.released &&
                                  !itemRevision.rejected && !itemBasicVm.editMakeOrBuy && editItemPermission"
                               ng-hide="!adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id)"
                               ng-click="itemBasicVm.changeMakeOrBuy()" title="{{EDIT | translate}}"></a>
                        </span>


                        <div style="display: flex;" ng-if="itemBasicVm.editMakeOrBuy">
                            <div>
                                <ui-select ng-model="itemBasicVm.item.makeOrBuy" theme="bootstrap"
                                           style="width:200px" ng-if="itemBasicVm.editMakeOrBuy">
                                    <ui-select-match placeholder=Select>{{$select.selected}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="makeOrBuyType in itemBasicVm.makeOrBuyTypes | filter: $select.search">
                                        <div ng-bind="makeOrBuyType"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                            <div>
                                <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                                        ng-click="itemBasicVm.updateItem()">
                                    <i class="fa fa-check"></i>
                                </button>
                                <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                                        ng-click="itemBasicVm.cancelMakeOrBuy()">
                                    <i class="fa fa-times"></i>
                                </button>
                            </div>


                        </div>
                    </div>
                </div>
                <div class="row"
                     ng-if="itemBasicVm.item.itemType.itemClass == 'PART' && itemBasicVm.item.makeOrBuy == 'BUY'">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REQUIRES_COMPLIANCE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-if="itemBasicVm.item.requireCompliance && !itemBasicVm.editRequireCompliance"
                              class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="!itemBasicVm.item.requireCompliance && !itemBasicVm.editRequireCompliance"
                              class="label label-outline bg-light-danger"
                              translate>NO</span>
                        <a href="" class="fa fa-pencil row-edit-btn"
                           ng-if="editItemPermission && !itemBasicVm.editRequireCompliance && !itemRevision.released &&
                                  !itemRevision.rejected && itemBasicVm.itemRevision.revision == '-'"
                           ng-hide="!adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id)"
                           ng-click="itemBasicVm.changeRequireMaintenance()" title="{{EDIT | translate}}"></a>

                        <div ng-if="itemBasicVm.editRequireCompliance" style="display: flex;">
                            <label class="switch-light switch-candy" onclick=""
                                   style="min-width:150px;max-width: 150px;cursor: pointer">
                                <input type="checkbox" ng-model="itemBasicVm.item.requireCompliance">
                                    <span>
                                        <span translate>NO</span>
                                        <span translate>YES</span>
                                        <a href=""></a>
                                    </span>
                            </label>
                            <button class="btn btn-sm btn-primary" style="margin-left: 5px;"
                                    title="{{SAVE | translate}}"
                                    ng-click="itemBasicVm.updateItem()">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                                    ng-click="itemBasicVm.cancelRequireCompliance()">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="row" ng-show="itemBasicVm.itemRevision.released">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>RELEASED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-if="itemBasicVm.itemRevision.releasedDate == null">{{'--'}}</span>
                        <span>{{itemBasicVm.itemRevision.releasedDate}}</span>
                    </div>
                </div>
                <div class="row" ng-show="itemBasicVm.itemRevision.rejected">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>REJECTED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span ng-if="itemBasicVm.itemRevision.releasedDate == null">{{'--'}}</span>
                        <span>{{itemBasicVm.itemRevision.releasedDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>UNITS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.units}}</span>
                    </div>
                </div>

                <div class="row" ng-if="itemBasicVm.item.itemType.softwareType">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>GitHub Repository</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ng-if="editItemPermission && itemBasicVm.item.lockObject == false"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released"
                           onaftersave="itemBasicVm.setGitHubItemRepo($data)"
                           editable-select="itemBasicVm.githubItemRepo"
                           e-ng-options="r as r.name for r in itemBasicVm.githubRepos">
                            {{itemBasicVm.githubItemRepo.repository.name || 'Click to select'}}
                        </a>

                        <div ng-if="itemRevision.released">
                            <span>{{itemBasicVm.githubItemRepo.repository.name}}</span>
                        </div>


                    </div>
                </div>

                <div class="row" ng-if="itemBasicVm.item.itemType.softwareType">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span>GitHub Release Tag</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ng-if="editItemPermission && itemBasicVm.item.lockObject == false"
                           href="" e-style="width:250px"
                           ng-hide="itemRevision.released"
                           onaftersave="itemBasicVm.setGitHubItemRelease($data)"
                           editable-select="itemBasicVm.itemRevisionGitHubRelease"
                           e-ng-options="r as r.releaseName for r in itemBasicVm.githubRepoReleases">
                            {{itemBasicVm.itemRevisionGitHubRelease.releaseName || 'Click to select'}}
                        </a>

                        <div ng-if="itemRevision.released">
                            <span>{{itemBasicVm.itemRevisionGitHubRelease.releaseName}}</span>
                        </div>

                    </div>
                </div>

                <div class="row" ng-if="itemBasicVm.item.lockObject">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>LOCKED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.lockedBy.fullName}}</span>
                    </div>
                </div>

                <div class="row" ng-if="itemBasicVm.item.lockObject">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>LOCKED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.lockedDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>EFFECTIVE_FROM</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ng-if="!itemBasicVm.editEffectiveFrom && !adminPermission && editItemPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveFrom = true;itemBasicVm.effectiveFromDate = itemBasicVm.itemRevision.effectiveFrom">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveFrom || 'CLICK_TO_SET_EFFECTIVE_FROM' | translate"></span>
                        </a>
                        <a ng-if="!itemBasicVm.editEffectiveFrom && adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveFrom = true;itemBasicVm.effectiveFromDate = itemBasicVm.itemRevision.effectiveFrom">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveFrom || 'CLICK_TO_SET_EFFECTIVE_FROM' | translate"></span>
                        </a>
                        <a ng-if="!itemBasicVm.editEffectiveFrom && adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveFrom = true;itemBasicVm.effectiveFromDate = itemBasicVm.itemRevision.effectiveFrom">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveFrom || 'CLICK_TO_SET_EFFECTIVE_FROM' | translate"></span>
                        </a>
                        <a ng-if="!itemBasicVm.editEffectiveFrom && editItemPermission && itemBasicVm.item.lockObject == false"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveFrom = true;itemBasicVm.effectiveFromDate = itemBasicVm.itemRevision.effectiveFrom">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveFrom || 'CLICK_TO_SET_EFFECTIVE_FROM' | translate"></span>
                        </a>

                        <span ng-if="!adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id) && editItemPermission"
                              ng-hide="itemRevision.released || itemRevision.rejected">
                               {{itemBasicVm.itemRevision.effectiveFrom}}
                        </span>

                        <div ng-if="itemRevision.released || itemRevision.rejected">
                            {{itemBasicVm.itemRevision.effectiveFrom}}
                        </div>

                        <div ng-if="itemBasicVm.editEffectiveFrom" style="display: flex;">
                            <input class="form-control" date-picker placeholder="{{effectiveFromPlaceholder}}"
                                   min-date='{{itemBasicVm.itemRevision.createdDate}}'
                                   type="text" ng-model="itemBasicVm.itemRevision.effectiveFrom" style="width: 200px;">
                            <i class="fa fa-times" ng-if="itemBasicVm.itemRevision.effectiveFrom != null"
                               style="position: absolute;margin-top: 10px;margin-left: 185px;cursor: pointer;"
                               ng-click="itemBasicVm.itemRevision.effectiveFrom = null"></i>
                            <button class="btn btn-sm btn-primary"
                                    ng-click="itemBasicVm.updateEffectiveFrom()">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-default"
                                    ng-click="itemBasicVm.editEffectiveFrom = false;itemBasicVm.itemRevision.effectiveFrom = itemBasicVm.effectiveFromDate">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>
                </div>


                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>EFFECTIVE_TO</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a ng-if="!itemBasicVm.editEffectiveTo && !adminPermission && editItemPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveTo = true;itemBasicVm.effectiveToDate = itemBasicVm.itemRevision.effectiveTo">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveTo || 'CLICK_TO_SET_EFFECTIVE_TO' | translate"></span>
                        </a>
                        <a ng-if="!itemBasicVm.editEffectiveTo && adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveTo = true;itemBasicVm.effectiveToDate = itemBasicVm.itemRevision.effectiveTo">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveTo || 'CLICK_TO_SET_EFFECTIVE_TO' | translate"></span>
                        </a>
                        <a ng-if="!itemBasicVm.editEffectiveTo && adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id == itemBasicVm.item.lockedBy.id)"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveTo = true;itemBasicVm.effectiveToDate = itemBasicVm.itemRevision.effectiveTo">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveTo || 'CLICK_TO_SET_EFFECTIVE_TO' | translate"></span>
                        </a>
                        <a ng-if="!itemBasicVm.editEffectiveTo && editItemPermission && itemBasicVm.item.lockObject == false"
                           href="" e-style="width:250px;"
                           ng-hide="itemRevision.released || itemRevision.rejected"
                           ng-click="itemBasicVm.editEffectiveTo = true;itemBasicVm.effectiveToDate = itemBasicVm.itemRevision.effectiveTo">
                            <span ng-bind-html="itemBasicVm.itemRevision.effectiveTo || 'CLICK_TO_SET_EFFECTIVE_TO' | translate"></span>
                        </a>

                        <span ng-if="!itemBasicVm.editEffectiveTo && !adminPermission && (itemBasicVm.item.lockObject && loginPersonDetails.person.id != itemBasicVm.item.lockedBy.id) && editItemPermission"
                              ng-hide="itemRevision.released || itemRevision.rejected">
                               {{itemBasicVm.itemRevision.effectiveTo}}
                        </span>

                        <div ng-if="!itemBasicVm.editEffectiveTo && (itemRevision.released || itemRevision.rejected)">
                            {{itemBasicVm.itemRevision.effectiveTo}}
                        </div>

                        <div ng-if="itemBasicVm.editEffectiveTo" style="display: flex;">
                            <input class="form-control" date-picker placeholder="{{effectiveToPlaceholder}}"
                                   min-date='{{itemBasicVm.itemRevision.createdDate}}'
                                   type="text" ng-model="itemBasicVm.itemRevision.effectiveTo" style="width: 200px;">
                            <i class="fa fa-times" ng-if="itemBasicVm.itemRevision.effectiveTo != null"
                               style="position: absolute;margin-top: 10px;margin-left: 185px;cursor: pointer;"
                               ng-click="itemBasicVm.itemRevision.effectiveTo = null"></i>
                            <button class="btn btn-sm btn-primary"
                                    ng-click="itemBasicVm.updateEffectiveTo()">
                                <i class="fa fa-check"></i>
                            </button>
                            <button class="btn btn-sm btn-default"
                                    ng-click="itemBasicVm.editEffectiveTo = false;itemBasicVm.itemRevision.effectiveTo = itemBasicVm.effectiveToDate">
                                <i class="fa fa-times"></i>
                            </button>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.createdDate}}</span>
                        <%--<span ng-if="currentLang == 'de'">{{itemBasicVm.item.createdDatede}}</span>--%>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.item.createdByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.itemRevision.modifiedDate}}</span>
                        <%--<span ng-if="currentLang == 'de'">{{itemBasicVm.item.modifiedDatede}}</span>--%>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{itemBasicVm.itemRevision.modifiedByPerson.fullName}}</span>
                    </div>
                </div>


                <basic-attribute-details-view object-type="ITEM"
                                              quality-type="ITEM"
                                              has-permission="editItemPermission && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                                              object-id="item.id"></basic-attribute-details-view>
                <basic-attribute-details-view object-type="ITEMREVISION" quality-type="ITEMREVISION"
                                              has-permission="editItemPermission && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                                              object-id="itemRevision.id"></basic-attribute-details-view>

            </div>
            <%--<object-attribute-details-view object-type="ITEMTYPE"
                                           has-permission="editItemPermission && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                                           object-type-id="item.itemType.id" show-attributes="true"
                                           object-id="item.id"></object-attribute-details-view>--%>
            <object-attribute-details-view object-type="ITEMTYPE"
                                           has-permission="editItemPermission && !itemRevision.released && !itemRevision.rejected && lockedObjectPermission"
                                           object-type-id="item.itemType.id" show-attributes="true"
                                           object-id="itemRevision.id"
                                           has-released="itemRevision.released"
                                           master-id="item.id"></object-attribute-details-view>
        </div>
    </div>
</div>