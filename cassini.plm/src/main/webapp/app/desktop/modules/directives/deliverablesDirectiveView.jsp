<div>
    <style scoped>
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

        .active-deliverableTab {
            background-color: #2a6fa8 !important;
            color: white !important;
            font-weight: 600;
        }

        .tab-content {
            padding: 0px !important;
        }

        .round {
            width: 300px;
            min-width: 11%;
            border-radius: 3px;
            padding: 4px 5px 7px 119px;
            top: 0;
            left: 0;
            z-index: 5;
            border: 1px #e6e8ed solid;
            background-color: rgb(241, 243, 244);
            outline: none;
        }

        .round > input:focus {
            border: 1px solid #ddd !important;
            box-shadow: none;
        }

        #lists {

        }

        .round > input:focus {
            border: 1px solid #ddd !important;
            box-shadow: none;
        }

        .fa-search {
            position: relative;
            margin-top: 11px;
            color: grey;
            opacity: 0.5;
            margin-right: -28px !important;
        }

        i.fa-times-circle {
            margin-left: -45px !important;
            color: gray;
            cursor: pointer;
        }

        .responsive-table .dropdown-content {
            margin-left: -85px !important;
        }

        table {
            table-layout: auto;
        }

        .disabledll {
            cursor: not-allowed;

        }

    </style>


    <%--    <div style="text-align: center;margin-top: 7px;">
            <form>
                <i class="fa fa-search"></i>
                <input autocomplete="off" list="lists" name="list"
                       oninput="angular.element(this).scope().switchType(this.value)" ng-model="selected"
                       class="round">
                <i class="fa fa-times-circle" title="{{searchTitle}}"
                   ng-show="selected.length > 0"
                   ng-click="selected = '';switchType(selected)"></i>
            </form>
            <datalist id="lists">
                <option data-value="Items"><span translate>ITEMS_ALL_TITLE</span></option>
                &lt;%&ndash;<option data-value="Terminology"><span translate>TERMINOLOGY</span></option>&ndash;%&gt;
                &lt;%&ndash;<option data-value="Specifications"><span translate>SPECIFICATIONS</span></option>&ndash;%&gt;
                <option data-value="Requirement"><span translate>REQUIREMENT</span></option>
            </datalist>
        </div>--%>


    <div class="responsive-table" style="padding: 10px;overflow-y: auto"
         ng-show="showItemDeliverable == true">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                      ng-if="(hasPermission || (loginPersonDetails.person.id == projectInfo.projectManager && external.external== true))"
                      ng-hide="projectPercentComplete == 100">
                        <i class="la la-plus dropdown-toggle" style="padding-left: 3px;"
                           ng-if="objectType == 'PROJECT' && !deliverablesAdded" ng-click="newDeliverable()"
                           title=" {{ 'ADD_DELIVERABLE' | translate }} "></i>
                        <i class="la la-plus dropdown-toggle" style="padding-left: 3px;"
                           ng-if="(objectType == 'ACTIVITY' || objectType == 'TASK') && !deliverablesAdded"
                           ng-click="addDeliverable()" title=" {{ 'ADD_DELIVERABLE' | translate }} "></i>
                        <i class="la la-plus dropdown-toggle" style="padding-left: 3px;" ng-if="deliverablesAdded"
                           uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-if="objectType == 'PROJECT'" ng-click="newDeliverable()" translate>ADD_DELIVERABLE</a>
                                <a href="" ng-if="objectType == 'ACTIVITY' || objectType == 'TASK'"
                                   ng-click="addDeliverable()" translate>ADD_DELIVERABLE</a>
                            </li>
                            <li ng-if="deliverablesAdded">
                                <a href=""
                                   ng-click="pasteDeliverablesFromClipboard()"
                                   translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                </span>
                </th>
                <th style="width: 100px;" translate>CONTEXT</th>
                <th class="col-width-200" translate>CONTEXT_NAME</th>
                <th style="width: 150px;" translate>OWNERS</th>
                <th style="width: 150px;" translate="ITEM_ALL_ITEM_NUMBER"></th>
                <th style="width: 150px;" translate="ITEM_ALL_ITEM_TYPE"></th>
                <th class="col-width-250" translate="ITEM_ALL_ITEM_NAME"></th>
                <th class="col-width-250" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 150px;" translate="ITEM_ALL_REVISION"></th>
                <th style="width: 150px;" translate="ITEM_ALL_LIFECYCLE"></th>
                <th style="width: 150px;" translate="DELIVERY_STATUS"></th>
                <th style="width: 150px;" translate="ITEM_ALL_RELEASED_DATE"></th>
                <th class="actions-col sticky-actions-col text-center" ng-if="hasPermission" style="right: -10px;"
                    translate="ACTIONS"></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="loading == false && itemDeliverables.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Deliverables.png" alt="" class="image">

                        <div class="message" translate>NO_DELIVERABLES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>

            <tr ng-if="loading == false && itemDeliverables.length > 0"
                ng-repeat="deliverable in itemDeliverables">
                <td style="width: 50px;display: flex">
                    <div class="ckbox ckbox-default" style="display: inline-block;">
                        <input id="deliverable{{$index}}" name="deliverableSelected" type="checkbox" ng-value="true"
                               ng-model="deliverable.selected" ng-change="selectItem(deliverable)">
                        <label for="deliverable{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                    <i class="fa fa-th nav-icon-font" title="{{'ITEM' | translate}}" style="margin-top: 4px;"></i>
                </td>
                <td style="width: 100px;">
                <span ng-if="deliverable.objectType == 'PROJECT' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showItem(deliverable.item)">
                    <span style="cursor:pointer;font-size:12px;background: #00b5a4;padding: 3px 5px;color: white;border-radius: 3px;">PROJECT</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECT' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #00b5a4;padding: 3px 5px;color: white;border-radius: 3px;">PROJECT</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTACTIVITY' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                 <span ng-if="deliverable.objectType == 'PROJECTACTIVITY' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                </td>
                <td class="col-width-200">
                    {{deliverable.contextName}}
                </td>
                <td style="width: 150px;">
                    {{deliverable.owner}}
                </td>
                <td style="width: 150px;">
                    <a href="" ng-click="showItem(deliverable.item)"
                       ng-if="!loginPersonDetails.external"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="deliverable.item.itemNumber | highlightText: freeTextQuery"></span></a>
                    <span ng-if="loginPersonDetails.external"
                          ng-bind-html="deliverable.item.itemNumber | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-bind-html="deliverable.item.itemType.name | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250">
                    <span ng-bind-html="deliverable.item.itemName | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250" title="{{deliverable.item.description}}"><span
                        ng-bind-html="deliverable.item.description | highlightText: freeTextQuery"></span>
                </td>
                <td style="text-align: center;width: 50px;">{{deliverable.revision.revision}}</td>
                <td>
                    <item-status item="deliverable.revision"></item-status>
                </td>
                <td>
                    <delivery-status delivery="deliverable"></delivery-status>
                </td>
                <td style="text-align: center">
                    <span ng-if="deliverable.revision.releasedDate == null">{{'--'}}</span>
                    <span>{{deliverable.revision.releasedDate}}</span>
                </td>
                <td class="actions-col sticky-actions-col text-center" ng-if="hasPermission" style="right: -10px;">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle
                           ng-click="resizeDropdown(deliverable)"></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                id="deliverable-dropdown-{{deliverable.id}}"
                                style="z-index: 9999 !important;">
                                <li title="{{projectPercentComplete == 100 || deliverable.deliverableStatus == 'FINISHED' ? cannotDeleteFinishedDeliverable : '' }}">
                                    <a ng-class="{'disabled':projectPercentComplete == 100 || deliverable.deliverableStatus == 'FINISHED'}"
                                       ng-if="hasPermission"
                                       ng-show="deliverable.objectType == deliverableType"
                                       ng-click="deleteDeliverable(deliverable)" translate>DELETE_DELIVERABLE</a>
                                </li>
                                <li ng-if="hasPermission
                                        && deliverable.deliverableStatus == 'PENDING'"
                                    ng-show="deliverable.objectType == deliverableType"
                                    ng-class="{'disabled':taskPercentage == 100}"
                                    ng-click="finishDeliverable(deliverable)">
                                    <a translate>FINISH_DELIVERABLE</a>
                                </li>
                                <li title="{{deleteContextMsg}} ( {{deliverable.objectType}} )"
                                    style="cursor: pointer"
                                    ng-class="{'disabledl':deliverable.objectType != deliverableType}"
                                    ng-if="hasPermission"
                                    ng-show="deliverable.objectType != deliverableType">
                                    <a translate>DELETE_DELIVERABLE</a>
                                </li>
                                <plugin-table-actions context="object.deliverable"
                                                      object-value="deliverable"></plugin-table-actions>
                            </ul>
                     </span>
                </td>

            </tr>

            </tbody>
        </table>
    </div>


    <div class="responsive-table" style="padding: 10px;overflow-y: auto"
         ng-show="showGlossaryDeliverable == true">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                      ng-if="(hasPermission || (loginPersonDetails.person.id == projectInfo.projectManager && external.external== true))"
                      ng-hide="projectPercentComplete == 100">
                        <i class="la la-plus dropdown-toggle"
                           style="padding-left: 3px;text-align: left !important;" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-if="objectType == 'PROJECT'" ng-click="newDeliverable()" translate>ADD_DELIVERABLE</a>
                                <a href="" ng-if="objectType == 'ACTIVITY' || objectType == 'TASK'"
                                   ng-click="addDeliverable()" translate>ADD_DELIVERABLE</a>
                            </li>
                            <li ng-if="deliverablesAdded">
                                <a href=""
                                   ng-click="pasteDeliverablesFromClipboard()"
                                   translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                </span>
                </th>
                <th style="width: 100px;" translate>CONTEXT</th>
                <th style="width: 150px;" translate="NUMBER"></th>
                <th style="width: 150px;" translate="TYPE"></th>
                <th class="col-width-250" translate="NAME"></th>
                <th class="col-width-250" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 75px;" translate="ITEM_ALL_REVISION"></th>
                <th style="width: 100px;" translate="ITEM_ALL_LIFECYCLE"></th>
                <th style="width: 150px;" translate="ITEM_ALL_RELEASED_DATE"></th>
                <th style="width: 70px;" ng-if="hasPermission" translate="ACTIONS"></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="loading == false && glossaries.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Deliverables.png" alt="" class="image">

                        <div class="message" translate>NO_DELIVERABLES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>

            <tr ng-if="loading == false && glossaries.length > 0"
                ng-repeat="deliverable in glossaries">
                <td style="width: 50px;display: flex">
                    <div class="ckbox ckbox-default" style="display: inline-block;">
                        <input id="glossaryDeliverable{{$index}}" name="deliverableSelected" type="checkbox"
                               ng-value="true"
                               ng-model="deliverable.selected" ng-change="selectGlossary(deliverable)">
                        <label for="glossaryDeliverable{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                    <i class="fa fa-book" title="Terminology" style="margin-top: 4px;"></i></td>
                <td>
                <span ng-if="deliverable.objectType == 'PROJECT'">
                    --
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTACTIVITY' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                 <span ng-if="deliverable.objectType == 'PROJECTACTIVITY' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showTask(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                </td>
                <td>
                    <a href=""
                       ng-click="openGlossaryDetails(deliverable.glossary)"
                       ng-if="!loginPersonDetails.external"
                       title="{{glossaryOpenTitle}}">
                        <span>{{deliverable.glossary.number}}</span>
                    </a>
                    <span ng-if="loginPersonDetails.external">{{deliverable.glossary.number}}</span>
                </td>
                <td>Terminology</td>
                <td class="col-width-250">{{deliverable.glossary.defaultDetail.name}}</td>
                <td class="col-width-250">{{deliverable.glossary.defaultDetail.description}}</td>
                <td style="text-align: center; width: 50px;">{{deliverable.glossary.revision}}</td>
                <td>
                    <item-status item="deliverable.glossary"></item-status>
                </td>
                <td style="text-align: center">
                    <span ng-if="deliverable.glossary.releasedDate != null">{{deliverable.glossary.releasedDate}}</span>
                    <span ng-if="deliverable.glossary.releasedDate == null">{{'--'}}</span>
                </td>
                <td class="text-center" ng-if="hasPermission">
                <span class="row-menu" uib-dropdown style="min-width: 50px">
                    <span dropdown-append-to-body> </span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="deliverable.objectType == deliverableType"
                                    ng-class="{'disabled':projectPercentComplete == 100}"
                                    ng-click="deleteGlossaryDeliverable(deliverable.glossary)">
                                    <a href="" translate>DELETE_DELIVERABLE</a>
                                </li>
                                <li title="{{deleteContextMsg}} ( {{deliverable.objectType}} )"
                                    style="cursor: pointer"
                                    ng-class="{'disabledl':deliverable.objectType != deliverableType}"
                                    ng-if="hasPermission"
                                    ng-show="deliverable.objectType != deliverableType">
                                    <a translate>DELETE_DELIVERABLE</a>
                                </li>
                                <plugin-table-actions context="object.deliverable"
                                                      object-value="deliverable"></plugin-table-actions>
                            </ul>
                </span>
                </td>

            </tr>
            </tbody>
        </table>
    </div>


    <%-- Specification And Requirement table--%>

    <div class="responsive-table" style="padding: 10px;" ng-show="specification == true">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                      ng-if="(hasPermission || (loginPersonDetails.person.id == projectInfo.projectManager && external.external== true))"
                      ng-hide="projectPercentComplete == 100">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           style="text-align: left !important;padding-left: 3px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-if="objectType == 'PROJECT'" ng-click="newDeliverable()" translate>ADD_DELIVERABLE</a>
                                <a href="" ng-if="objectType == 'ACTIVITY' || objectType == 'TASK'"
                                   ng-click="addDeliverable()" translate>ADD_DELIVERABLE</a>
                            </li>
                            <li ng-if="deliverablesAdded">
                                <a href=""
                                   ng-click="pasteDeliverablesFromClipboard()"
                                   translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                </span>
                </th>
                <th style="width: 100px;" translate>CONTEXT</th>
                <th translate="NUMBER"></th>
                <th translate="TYPE"></th>
                <th class="col-width-200" translate="NAME"></th>
                <th class="col-width-250" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 75px;" translate="ITEM_ALL_REVISION"></th>
                <th style="width: 100px;" translate="ITEM_ALL_LIFECYCLE"></th>
                <th style="width: 150px;" translate="ITEM_ALL_RELEASED_DATE"></th>
                <th style="width: 70px;" ng-if="hasPermission" translate="ACTIONS"></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="loading == false && specDeliverables.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Deliverables.png" alt="" class="image">

                        <div class="message" translate>NO_DELIVERABLES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>

            <tr ng-if="loading == false && specDeliverables.length > 0"
                ng-repeat="deliverable in specDeliverables">
                <td style="width: 50px;display: flex;">
                    <div class="ckbox ckbox-default" style="display: inline-block;">
                        <input id="specDeliverable{{$index}}" name="deliverableSelected" type="checkbox" ng-value="true"
                               ng-model="deliverable.selected"
                               ng-change="selectSpecification(deliverable)">
                        <label for="specDeliverable{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                    <i class="fa fa-th nav-icon-font" title="{{'SPECIFICATION' | translate}}"
                       style="margin-top: 4px;"></i>
                </td>
                <td>
                <span ng-if="deliverable.objectType == 'PROJECT'">
                    --
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTACTIVITY'"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK'"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                </td>
                <td>
                    <a href="" ng-click="showSpecificationDetails(deliverable)"
                       ng-if="!loginPersonDetails.external"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="deliverable.specification.objectNumber | highlightText: freeTextQuery"></span></a>
                    <span ng-if="loginPersonDetails.external"
                          ng-bind-html="deliverable.specification.objectNumber | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250">
                    <span ng-bind-html="deliverable.specification.type.name | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-200">
                    <span ng-bind-html="deliverable.specification.name | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250">
                    <span ng-bind-html="deliverable.specification.description | highlightText: freeTextQuery"></span>
                </td>
                <td style="text-align: center;width: 50px;">{{deliverable.specification.revision}}</td>
                <td>
                    <span class="badge badge-success" style="padding: 5px 10px;font-size: 12px;">{{deliverable.specification.lifecyclePhase.phase}}</span>
                </td>
                <td style="text-align: center">
                    <span ng-if="deliverable.specification.releasedDate == null">{{'--'}}</span>
                    <span>{{deliverable.specification.releasedDate}}</span>
                </td>
                <td class="text-center" ng-if="hasPermission">
                <span class="row-menu" uib-dropdown style="min-width: 50px">
                    <span dropdown-append-to-body> </span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-class="{'disabled':projectPercentComplete == 100}"
                                    ng-if="hasPermission"
                                    ng-hide="deliverable.objectType != deliverableType"
                                    ng-click="deleteSpecificationDeliverable(deliverable)">
                                    <a href="" translate>DELETE_DELIVERABLE</a>
                                </li>
                                <li title="{{deleteContextMsg}} ( {{deliverable.objectType}} )"
                                    style="cursor: pointer"
                                    ng-class="{'disabledl':deliverable.objectType != deliverableType}"
                                    ng-if="hasPermission"
                                    ng-show="deliverable.objectType != deliverableType">
                                    <a translate>DELETE_DELIVERABLE</a>
                                </li>
                                <plugin-table-actions context="object.deliverable"
                                                      object-value="deliverable"></plugin-table-actions>
                            </ul>
                </span>
                </td>


            </tr>
            </tbody>
        </table>
    </div>


    <div class="responsive-table" style="padding: 10px;" ng-show="requirement == true">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                      ng-if="(hasPermission || (loginPersonDetails.person.id == projectInfo.projectManager && external.external== true))"
                      ng-hide="projectPercentComplete == 100">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           style="text-align: left !important;padding-left: 3px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-if="objectType == 'PROJECT'" ng-click="newDeliverable()" translate>ADD_DELIVERABLE</a>
                                <a href="" ng-if="objectType == 'ACTIVITY' || objectType == 'TASK'"
                                   ng-click="addDeliverable()" translate>ADD_DELIVERABLE</a>
                            </li>
                            <li ng-if="deliverablesAdded">
                                <a href=""
                                   ng-click="pasteDeliverablesFromClipboard()"
                                   translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                </span>
                </th>
                <th translate>CONTEXT</th>
                <th translate="NUMBER"></th>
                <th translate="TYPE"></th>
                <th class="col-width-200" translate="NAME"></th>
                <th class="col-width-250" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 75px;" translate="VERSION"></th>
                <th style="width: 75px;" translate="STATUS"></th>
                <th style="width: 70px;" ng-if="hasPermission" translate="ACTIONS"></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="loading == false && reqDeliverables.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Deliverables.png" alt="" class="image">

                        <div class="message" translate>NO_DELIVERABLES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>

            <tr ng-if="loading == false && reqDeliverables.length > 0 "
                ng-repeat="deliverable in reqDeliverables">
                <td>
                    <div class="ckbox ckbox-default" style="display: inline-block;">
                        <input id="reqDeliverable{{$index}}" name="deliverableSelected" type="checkbox" ng-value="true"
                               ng-model="deliverable.selected"
                               ng-change="selectRequirement(deliverable)">
                        <label for="reqDeliverable{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                    <i class="fa fa-th nav-icon-font" title="Requirement" style="margin-top: 4px;"></i>
                </td>
                <td>
                <span ng-if="deliverable.objectType == 'PROJECT'">
                    --
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTACTIVITY' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                 <span ng-if="deliverable.objectType == 'PROJECTACTIVITY' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #337ab7;padding: 3px 5px;color: white;border-radius: 3px;">ACTIVITY</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK' && !loginPersonDetails.external"
                      title="Click to show Details"
                      ng-click="showActivity(deliverable)">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                <span ng-if="deliverable.objectType == 'PROJECTTASK' && loginPersonDetails.external">
                    <span style="cursor:pointer;font-size:12px;background: #449d44;padding: 3px 5px;color: white;border-radius: 3px;">TASK</span>
                </span>
                </td>
                <td>
                    <a href="" ng-click="showRequirementDetails(deliverable)"
                       ng-if="!loginPersonDetails.external"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="deliverable.requirement.objectNumber | highlightText: freeTextQuery"></span></a>
                    <span ng-if="loginPersonDetails.external"
                          ng-bind-html="deliverable.requirement.objectNumber | highlightText: freeTextQuery"></span>
                </td>
                <td>
                    <span ng-bind-html="deliverable.requirement.type.name | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-200">
                    <span ng-bind-html="deliverable.requirement.name | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250">
                    <span ng-bind-html="deliverable.requirement.description | highlightText: freeTextQuery"></span>
                </td>
                <td style="text-align: center;width: 50px;">
                    <span ng-if="deliverable.requirement.version == 0">-</span>
                    <span ng-if="deliverable.requirement.version > 0"> {{deliverable.requirement.version}}</span>
                </td>
                <td style="text-align: center">
                <span><task-status
                        task="deliverable.requirement"></task-status></span>
                </td>
                <td class="text-center" ng-if="hasPermission">
                <span class="row-menu" uib-dropdown style="min-width: 50px">
                    <span dropdown-append-to-body> </span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-class="{'disabled':projectPercentComplete == 100}"
                                    ng-if="hasPermission"
                                    ng-hide="deliverable.objectType != deliverableType"
                                    ng-click="deleteRequirementDeliverable(deliverable)">
                                    <a href="" translate>DELETE_DELIVERABLE</a>
                                </li>
                                <li title="{{deleteContextMsg}} ( {{deliverable.objectType}} )"
                                    style="cursor: pointer"
                                    ng-class="{'disabledl':deliverable.objectType != deliverableType}"
                                    ng-if="hasPermission"
                                    ng-show="deliverable.objectType != deliverableType">
                                    <a translate>DELETE_DELIVERABLE</a>
                                </li>
                                <plugin-table-actions context="object.deliverable"
                                                      object-value="deliverable"></plugin-table-actions>
                            </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>