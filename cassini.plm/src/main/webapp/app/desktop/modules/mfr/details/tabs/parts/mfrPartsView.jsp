<div class="view-container" fitcontent>
    <style>
        .added-column {
            text-align: left;
            width: 150px;
        }

        .added-column i {
            display: none;
            cursor: pointer;
            margin-left: 5px;
        }

        .added-column:hover i {
            display: inline-block;
        }

        .attributeTooltip {
            position: relative;
            display: inline-block;
        }

        .attributeTooltip .attributeTooltiptext {
            visibility: hidden;
            width: 200px;
            background-color: #7BB7EB;
            color: #141f9f;
            text-align: left;
            border-radius: 6px;
            padding: 5px 0;
            position: absolute;
            z-index: 1;
            top: -5px;
            bottom: auto;
            right: 100%;
            opacity: 0;
            transition: opacity 1s;
        }

        .attributeTooltip .attributeTooltiptext::after {
            content: "";
            position: absolute;
            top: 25%;
            left: 102%;
            margin-left: -5px;
            border-width: 5px;
            border-style: solid;
            border-color: transparent transparent transparent #7BB7EB;
        }

        .attributeTooltip:hover .attributeTooltiptext {
            visibility: visible;
            opacity: 1;
        }

        /* The Close Button */
        .img-model .closeImage11 {
            position: absolute;
            top: 50px;
            right: 50px;

            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage11:hover,
        .img-model .closeImage11:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .responsive-table .dropdown-content {
            margin-left: 30px !important;
        }


    </style>
    <div class='responsive-table' style="padding: 10px">
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;"
                    ng-if="(hasPermission('manufacturer','edit') && external.external == false) || (external.external == true && sharedPermission == 'WRITE')">
                    <i class="la la-plus" ng-click="mfrPartsVm.addMfrParts()"
                       style="padding-left: 3px;cursor: pointer;" title="{{mfrPartsVm.crateParts}}"></i>
                </th>
                <th style="width: 200px;" translate>PART_NUMBER</th>
                <th style="width: 200px;" translate>MANUFACTURER_PART_TYPE</th>
                <th class="col-width-250" translate>PART_NAME</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th translate>LIFECYCLE</th>
                <th style="width: 200px;" translate>STATUS</th>
                <th style="width: 100px;" translate>SERIALIZED</th>
                <th style="width: 100px;text-align: center;" translate="THUMBNAIL"></th>
                <th class='added-column' style="width: 200px;"
                    ng-repeat="mfrPartAttribute in mfrPartsVm.selectedMfrPartAttributes">
                    {{mfrPartAttribute.name}}
                    <i class="fa fa-times-circle"
                       ng-click="mfrPartsVm.removeAttribute(mfrPartAttribute)"
                       title="Remove this column"></i>
                </th>
                <th style="text-align: center" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mfrPartsVm.loading == true">
                <td colspan="10"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5">
                    <span translate>LOADING_MFR_PARTS</span>
                </td>
            </tr>
            <tr ng-if="mfrPartsVm.loading == false && mfrPartsVm.mfrparts.length == 0">
                <td colspan="10" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message">{{ 'NO_MFR_PARTS' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-if="mfrPartsVm.mfrparts.length > 0"
                ng-repeat="mfrpart in mfrPartsVm.mfrparts">
                <td ng-if="(hasPermission('manufacturer','edit') && external.external == false) || (external.external == true && sharedPermission == 'WRITE')"></td>
                <td>
                    <a href="" ng-if="!loginPersonDetails.external" ng-click="mfrPartsVm.showMfrpartDetails(mfrpart)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"><span
                            ng-bind-html="mfrpart.partNumber | highlightText: freeTextQuery"></span></a>

                    <span ng-if="loginPersonDetails.external">
                        {{mfrpart.partNumber}}
                    </span>
                </td>
                <td><span ng-bind-html="mfrpart.mfrPartType.name | highlightText: freeTextQuery"></span></td>
                <td class="col-width-250"><span ng-bind-html="mfrpart.partName | highlightText: freeTextQuery"></span>
                </td>
                <td class="description-column">
                     <span title="{{mfrpart.description}}"><span
                             ng-bind-html="mfrpart.description  | highlightText: freeTextQuery"></span>
                     </span>
                </td>
                <td>
                    <item-status item="mfrpart"></item-status>
                </td>
                <td>{{mfrpart.status}}</td>
                <td class="col-width-150">
                            <span ng-if="mfrpart.serialized == true"
                                  class="label label-outline bg-light-success"
                                  translate>YES</span>
                            <span ng-if="mfrpart.serialized == false"
                                  class="label label-outline bg-light-danger"
                                  translate>NO</span>
                </td>
                <td style="width: 100px;text-align: center">
                    <div>
                        <a ng-if="mfrpart.thumbnail != null && mfrpart.thumbnail != ''"
                           href="" ng-click="mfrPartsVm.showImage(mfrpart)"
                           title="{{clickToShowLargeImage}}">
                            <img ng-src="{{mfrpart.imagePath}}"
                                 style="height: 20px;width: 30px;">
                        </a>
                        <a ng-if="mfrpart.thumbnail == null && mfrpart.thumbnail != ''" href="">
                            <img src="app/assets/images/cassini-logo-greyscale.png" title="No image" alt=""
                                 class="no-image-preview" style="height: 20px;width: 30px;">
                        </a>

                        <div id="item-thumbnail{{mfrpart.id}}" class="item-thumbnail modal">
                            <div class="item-thumbnail-content">
                                <div class="thumbnail-content" style="display: flex;width: 100%;">
                                    <div class="thumbnail-view" id="thumbnail-view{{mfrpart.id}}">
                                        <div id="thumbnail-image{{mfrpart.id}}"
                                             style="display: table-cell;vertical-align: middle;text-align: center;">
                                            <img ng-src="{{mfrpart.imagePath}}"
                                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{mfrpart.id}}"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>

                <td class="added-column"
                    ng-repeat="objectAttribute in mfrPartsVm.selectedMfrPartAttributes">
                    <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                                      && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                       ng-init="attrName = objectAttribute.id">
                        <a href=""
                           ng-if="objectAttribute.refType == 'ITEM'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="mfrPartsVm.showAttributeDetails(mfrpart[attrName])">{{mfrpart[attrName].itemNumber}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'ITEMREVISION'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="mfrPartsVm.showAttributeDetails(mfrpart[attrName])">
                            {{mfrpart[attrName].itemMaster+" "+mfrpart[attrName].revision+" "+
                            mfrpart[attrName].lifeCyclePhase.phase}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'CHANGE'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="mfrPartsVm.showAttributeDetails(mfrpart[attrName])">{{mfrpart[attrName].ecoNumber}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'MANUFACTURER'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="mfrPartsVm.showAttributeDetails(mfrpart[attrName])">{{mfrpart[attrName].name}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="mfrPartsVm.showAttributeDetails(mfrpart[attrName])">{{mfrpart[attrName].partNumber}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'WORKFLOW'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="mfrPartsVm.showAttributeDetails(mfrpart[attrName])">{{mfrpart[attrName].name}}
                        </a>
                        <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
                            {{mfrpart[attrName].firstName}}
                        </a>
                    </p>

                    <div class="attributeTooltip"
                         ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                         ng-init="attrName = objectAttribute.id">
                        <p>
                            <a ng-if="mfrpart[attrName].length > 0" href="">
                                {{mfrpart[attrName].length}} Attachments
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="attachment in mfrpart[attrName]">
                                    <a href="" ng-click="mfrPartsVm.openAttachment(attachment)"
                                       title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                       style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                        {{attachment.name}}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>


                    <div class="attributeTooltip"
                         ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'&&
                             objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == true"
                         ng-init="attrName = objectAttribute.id">
                        <p>
                            <a ng-if="mfrpart[attrName].length > 0" href="">
                                {{mfrpart[attrName].length}} listOfValue
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="listValue in mfrpart[attrName]">
                                    <a href=""
                                       style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                        {{listValue}}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="attributeTooltip"
                         ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'&&
                             objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple != true"
                         ng-init="attrName = objectAttribute.id">
                        <p>
                            <a href="">
                                {{mfrpart[attrName]}}
                            </a>
                        </p>
                    </div>

                    <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                         ng-init="attrName = objectAttribute.id">
                        <a href="" ng-click="mfrPartsVm.showThumbnailImage(objectAttribute)"
                           title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                            <img ng-if="mfrpart[attrName] != null"
                                 ng-src="{{mfrpart[attrName]}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </a>

                        <div id="myModal2" class="img-model modal">
                            <span class="closeImage1">&times;</span>
                            <img class="modal-content" id="img03">
                        </div>
                        <div id="mfrpart-thumbnail{{objectAttribute.id}}" class="mfrpart-thumbnail modal">
                            <div class="mfrpart-thumbnail-content">
                                <div class="thumbnail-content" style="display: flex;width: 100%;">
                                    <div class="thumbnail-view" id="thumbnail-view{{objectAttribute.id}}">
                                        <div id="thumbnail-image{{objectAttribute.id}}"
                                             style="display: table-cell;vertical-align: middle;text-align: center;">
                                            <img ng-src="{{mfrpart[attrName]}}"
                                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{objectAttribute.id}}"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'&& objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'LONGTEXT' && objectAttribute.dataType != 'HYPERLINK'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT' && objectAttribute.dataType != 'TEXT' && objectAttribute.dataType != 'BOOLEAN'"
                       ng-init="attrName = objectAttribute.id">
                        {{mfrpart[attrName]}}
                    </p>

                    <div ng-if="objectAttribute.dataType == 'BOOLEAN'"
                         ng-init="attrName = objectAttribute.id">
                        <p ng-if="mfrpart[attrName] == true">{{mfrpart[attrName]}}</p>

                        <p ng-if="mfrpart[attrName] == false || mfrpart[attrName] == null">false</p>
                    </div>

                    <p ng-if="objectAttribute.dataType == 'LONGTEXT'"
                       ng-init="attrName = objectAttribute.id" title="{{mfrpart[attrName]}}"
                       style="height: 30px;width: 40px;margin-bottom: -7px;">
                        {{mfrpart[attrName]}}
                    </p>

                    <p ng-if="objectAttribute.dataType == 'TEXT'"
                       ng-init="attrName = objectAttribute.id" title="{{mfrpart[attrName]}}"
                       style="height: 30px;width: 50px;margin-bottom: -7px;">
                        {{mfrpart[attrName]}}
                    </p>

                    <p ng-if="objectAttribute.dataType == 'HYPERLINK'"
                       ng-init="attrName = objectAttribute.id">
                        <a href=""
                           ng-click="showHyperLink(mfrpart[attrName])">{{mfrpart[attrName]}}</a>
                    </p>


                        <span ng-init="currencyType = objectAttribute.id+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="mfrpart[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.id"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{mfrpart[attrName]}}
                        </span>

                    <%--  Show Rich text content in modal --%>


                           <span ng-init="attrName = objectAttribute.id"
                                 ng-if="objectAttribute.dataType == 'RICHTEXT'">
                               <a href=""
                                  ng-if="mfrpart[attrName] != null && mfrpart[attrName] != undefined && mfrpart[attrName] != ''"
                                  data-toggle="modal"
                                  ng-click="showRichTextSidePanel(mfrpart[attrName],objectAttribute,mfrpart)">Click to
                                   show
                                   RichText</a>
                        </span>


                    <p ng-if="objectAttribute.name == 'Modified By'">{{mfrpart.modifiedByObject.firstName}}</p>

                    <p ng-if="objectAttribute.name == 'Created By'">{{mfrpart.createdByObject.firstName}}</p>

                    <%-- <p ng-if="objectAttribute.name == 'modifiedDate'">{{mfrpart.modifiedDate}}</p>--%>

                    <p ng-if="objectAttribute.name == 'Created Date'">{{mfrpart.createdDate}}</p>

                </td>
                <td class="text-center">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <comments-button object-type="MANUFACTURERPART" object-id="mfrpart.id">
                                    </comments-button>
                                </li>
                                <li ng-if="hasPermission('manufacturer','delete') || (external.external == true && sharedPermission == 'WRITE')"
                                    ng-click="mfrPartsVm.deleteMfrpart(mfrpart)"><a translate>DELETE_PART</a>
                                </li>
                                <plugin-table-actions context="manufacturer.part"
                                                      object-value="mfrpart"></plugin-table-actions>
                            </ul>
                        </span>

                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>