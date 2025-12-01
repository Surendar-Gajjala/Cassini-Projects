<div>
    <style scoped>
        .item-details > div.row.master-att:last-child,
        .item-details > div.row.revision-att:last-child {
            border-bottom: 0 !important;
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
    <div style="padding: 30px;">
        <div class="item-details" style="padding: 30px">
            <div class="thumbnail-container">
                <div>
                    <a ng-if="mfrPartsBasicVm.manufacturePart.thumbnail != null && mfrPartsBasicVm.manufacturePart.thumbnail != ''"
                       href=""
                       ng-click="mfrPartsBasicVm.showImage(mfrPartsBasicVm.manufacturePart)"
                       title="{{mfrPartsBasicVm.titleImage}}">
                        <img class="medium-image" ng-src="{{mfrPartsBasicVm.manufacturePart.imagePath}}">
                    </a>

                    <div ng-if="mfrPartsBasicVm.manufacturePart.thumbnail == null"
                         ng-class="{'cursor-override': !hasPermission('manufacturerpart','edit')}"
                         title="{{hasPermission('manufacturerpart','edit') ? '' : noPermission}}"
                         class="no-thumbnail">
                        <a href=""
                           ng-class="{'permission-text-disabled': !hasPermission('manufacturerpart','edit')}"
                           onclick="$('#imageFile').click()"
                           title="{{addImage}}">
                            <span translate>ADD_THUMBNAIL</span>
                        </a>
                        <input style="display: none"
                               id="imageFile"
                               type="file" ng-file-model="mfrPartsBasicVm.thumbnail" accept="image/*"
                               onchange="angular.element(this).scope().saveImage(this.files[0])">
                    </div>
                    <div id="item-thumbnail-basic{{mfrPartsBasicVm.mfrPartId}}" class="item-thumbnail modal">
                        <div class="item-thumbnail-content">
                            <div class="thumbnail-content" style="display: flex;width: 100%;">
                                <div class="thumbnail-view" id="thumbnail-view-basic{{mfrPartsBasicVm.mfrPartId}}">
                                    <div id="thumbnail-image-basic{{mfrPartsBasicVm.mfrPartId}}"
                                         style="display: table-cell;vertical-align: middle;text-align: center;">
                                        <img ng-src="{{mfrPartsBasicVm.manufacturePart.imagePath}}"
                                             style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{mfrPartsBasicVm.mfrPartId}}"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div ng-if="mfrPartsBasicVm.manufacturePart.thumbnail != null && mfrPartsBasicVm.manufacturePart.thumbnail != '' && (!loginPersonDetails.external || (loginPersonDetails.external && sharedPermission == 'WRITE'))"
                    ng-class="{'cursor-override': !hasPermission('manufacturerpart','edit')}"
                     title="{{hasPermission('manufacturerpart','edit') ? '' : noPermission}}"
                     style="padding: 10px;background: #fff;border: 1px solid lightgrey;position: absolute;left: -1px;right: -1px;bottom: -1px">
                    <a href="" ng-class="{'permission-text-disabled': !hasPermission('manufacturerpart','edit')}"
                       onclick="$('#imageUpdateFile').click()"
                       title="{{updateImageTitle}}">
                        <i class="fa fa-upload" style="font-size: 16px;color: #636e7b"></i>
                    </a>
                    <a href="" title="{{removeImageTitle}}" ng-click="mfrPartsBasicVm.deleteImage()"
                       style="float: right;">
                        <i class="fa fa-trash" style="font-size: 16px;color: #636e7b;"></i>
                    </a>
                    <input style="display: none"
                           id="imageUpdateFile"
                           type="file" ng-file-model="mfrPartsBasicVm.thumbnail" accept="image/*"
                           onchange="angular.element(this).scope().saveImage(this.files[0])">
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>PART_NUMBER</span> :
                </div>
                <div class="value col-xs-8 col-sm-9"
                     ng-class="{'cursor-override': !hasPermission('manufacturerpart','edit')}"
                     title="{{hasPermission('manufacturerpart','edit') || external.external == true ? '' : noPermission}}">
              <span ng-if="external.external == false">
                <a href="#" ng-class="{'permission-text-disabled': !hasPermission('manufacturerpart','edit')}"
                   onaftersave="mfrPartsBasicVm.updateManufacturerPart()"
                   editable-text="mfrPartsBasicVm.manufacturePart.partNumber" translate>
                    {{mfrPartsBasicVm.manufacturePart.partNumber || 'ADD_PART_NUMBER'}}</a>
              </span>
            <span ng-if="external.external == true">
                <a ng-if="sharedPermission == 'WRITE'" href="#"
                   onaftersave="mfrPartsBasicVm.updateManufacturerPart()"
                   editable-text="mfrPartsBasicVm.manufacturePart.partNumber">{{mfrPartsBasicVm.manufacturePart.partNumber}}</a>

                   <div ng-if="sharedPermission == 'READ' || sharedPermission == undefined">
                     <span ng-bind-html="mfrPartsBasicVm.manufacturePart.partNumber"></span>
                 </div>
            </span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>MANUFACTURER_PART_TYPE </span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{mfrPartsBasicVm.manufacturePart.mfrPartType.name}}</span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>PART_NAME</span> :
                </div>
                <div class="value col-xs-4 col-sm-5"
                     ng-class="{'cursor-override': !hasPermission('manufacturerpart','edit')}"
                     title="{{hasPermission('manufacturerpart','edit') || external.external == true ? '' : noPermission}}">
                    <span ng-if="external.external == false">
                    <a href="#" ng-class="{'permission-text-disabled': !hasPermission('manufacturerpart','edit')}"
                       onaftersave="mfrPartsBasicVm.updateManufacturerPart()"
                       editable-text="mfrPartsBasicVm.manufacturePart.partName" translate>
                        {{mfrPartsBasicVm.manufacturePart.partName || 'ADD_PART_NAME'}}</a>
                    </span>
                     <span ng-if="external.external == true">
                    <a href="#" ng-if="sharedPermission == 'WRITE'"
                       onaftersave="mfrPartsBasicVm.updateManufacturerPart()"
                       editable-text="mfrPartsBasicVm.manufacturePart.partName" translate>
                        {{mfrPartsBasicVm.manufacturePart.partName || 'ADD_PART_NAME'}}</a>
                        <span ng-if="sharedPermission == 'READ' || sharedPermission == undefined">{{mfrPartsBasicVm.manufacturePart.partName}}</span>
                    </span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>DESCRIPTION</span> :
                </div>
                <div class="value col-xs-5 col-sm-5"
                     ng-class="{'cursor-override': !hasPermission('manufacturerpart','edit')}"
                     title="{{hasPermission('manufacturerpart','edit') || external.external == true ? '' : noPermission}}">
                     <span ng-if="external.external == false">
                    <a href="#" ng-class="{'permission-text-disabled': !hasPermission('manufacturerpart','edit')}"
                       onaftersave="mfrPartsBasicVm.updateManufacturerPart()"
                       editable-textarea="mfrPartsBasicVm.manufacturePart.description">
                        <span ng-bind-html="(mfrPartsBasicVm.manufacturePart.description) || 'CLICK_TO_ADD_DESCRIPTION' | translate"
                              title="{{mfrPartsBasicVm.manufacturePart.description}}"></span>

                    </a>
                         </span>
                    <span ng-if="external.external == true">
                        <a href="#" ng-if="sharedPermission == 'WRITE'"
                           onaftersave="mfrPartsBasicVm.updateManufacturerPart()"
                           editable-textarea="mfrPartsBasicVm.manufacturePart.description">
                        <span ng-bind-html="(mfrPartsBasicVm.manufacturePart.description) || 'CLICK_TO_ADD_DESCRIPTION' | translate"
                              title="{{mfrPartsBasicVm.manufacturePart.description}}"></span>

                        </a>
                        <span ng-if="sharedPermission == 'READ' || sharedPermission == undefined">{{mfrPartsBasicVm.manufacturePart.description}}</span>
                    </span>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>SERIALIZED</span> :
                </div>
                <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('manufacturePart','edit')}"
                     title="{{hasPermission('manufacturePart','edit') ? '' : noPermission}}">
                    <span ng-if="mfrPartsBasicVm.manufacturePart.serialized && !mfrPartsBasicVm.editSerialized"
                          class="label label-outline bg-light-success" translate>YES</span>
                    <span ng-if="!mfrPartsBasicVm.manufacturePart.serialized && !mfrPartsBasicVm.editSerialized"
                          class="label label-outline bg-light-danger" translate>NO</span>
                    <a href="" ng-if="!mfrPartsBasicVm.editSerialized" class="fa fa-pencil row-edit-btn"
                       ng-class="{'permission-text-disabled': !hasPermission('manufacturePart','edit')}"
                       ng-click="mfrPartsBasicVm.changeSerialized()"
                       title="{{mfrPartsBasicVm.assets.length > 0 ? assetAlreadyAdded : ''}}">
                    </a>
        
                    <div style="display: flex;" ng-if="mfrPartsBasicVm.editSerialized">
                        <ui-select ng-model="mfrPartsBasicVm.manufacturePart.serialized" theme="bootstrap"
                                   style="width:200px">
                            <ui-select-match placeholder="Select">{{$select.selected.label}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="serialized.value as serialized in mfrPartsBasicVm.serialized | filter: $select.search">
                                <div>{{serialized.label}}</div>
                            </ui-select-choices>
                        </ui-select>
                        <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                                ng-click="mfrPartsBasicVm.updateManufacturerPart()">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                                ng-click="mfrPartsBasicVm.cancelSerialized()">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>LIFE_CYCLE_PHASE</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <item-status item="mfrPartsBasicVm.manufacturePart"></item-status>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span style="font-size: 14px" translate>WORKFLOW_STATUS</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <workflow-status-settings workflow="mfrPartsBasicVm.manufacturePart"></workflow-status-settings>
                    <!-- <span>{{mfrPartsBasicVm.manufacturePart.workflowStatus}}</span> -->
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>CREATED_BY</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{mfrPartsBasicVm.manufacturePart.createdByObject.fullName}}</span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>CREATED_DATE</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{mfrPartsBasicVm.manufacturePart.createdDate}}</span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>MODIFIED_BY</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{mfrPartsBasicVm.manufacturePart.modifiedByObject.fullName}}</span>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span translate>MODIFIED_DATE</span> :
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <span>{{mfrPartsBasicVm.manufacturePart.modifiedDate}}</span>
                </div>
            </div>

            <basic-attribute-details-view object-type="MANUFACTURERPART"
                                          quality-type="MANUFACTURERPART"
                                          has-permission="hasPermission('manufacturer','edit')"
                                          object-id="mfrPartsBasicVm.mfrPartId"></basic-attribute-details-view>
            <object-attribute-details-view object-type="MANUFACTURERPARTTYPE"
                                           has-permission="hasPermission('manufacturer','edit')"
                                           object-type-id="mfrPart.mfrPartType.id" show-attributes="true"
                                           object-id="mfrPartsBasicVm.mfrPartId"></object-attribute-details-view>
        </div>
    </div>
</div>