<style>

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

    .description {
        word-wrap: break-word;
        width: 375px;
        white-space: normal !important;
        text-align: left;
    }

    .name {
        word-wrap: break-word;
        width: 250px;
        white-space: normal !important;
        text-align: left;
    }

    .notes {

        word-wrap: break-word;
        width: 275px;
        white-space: normal !important;
        text-align: left;
    }

    .inlineTable {
        display: inline-block;
    }

    .notification1 {
        text-decoration: none;
        padding: 15px 26px;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .notification1 .badge1 {
        position: absolute;
        top: -7px;
        left: 0px;
        padding: 0px 7px;
        border-radius: 50%;
        background-color: orange;
        color: white;
    }
</style>

<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <%-- <div class="row">
             <p class="col-sm-11"
                ng-show="searchModeType == true"
                style="color: #0390fd;font-size: 16px;text-align: center;" translate>
                 ALL_VIEW_ALERT</p>
         </div>--%>
        <thead>
        <tr>
            <th style="width: 10px"><i class="la la-plus"
                                       style="cursor: pointer !important;"
                                       ng-if="hasPermission('terminology','edit')"
                                       title="{{glossaryEntryItemVm.addEntriesTitle}}"
                                       ng-disabled='selectedGlossary.lifeCyclePhase.phaseType == "RELEASED"'
                                       ng-click="addGlossaryEntries()"></i>
            </th>
            <th translate>NAME</th>
            <th class="description" translate>DESCRIPTION</th>
            <th style="width: 50px; text-align: center" translate>VERSION</th>
            <th class="notes" translate>NOTES</th>
            <th class='added-column'
                style="width: 150px;"
                ng-repeat="selectedAttribute in selectedAttributes">
                {{selectedAttribute.name}}
                <i class="fa fa-times-circle"
                   ng-click="removeAttribute(selectedAttribute)"
                   title="Remove this column"></i>
            </th>
            <th style="width: 150px; text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="glossaryEntryItemVm.loading == true">
            <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                                <span translate>LOADING_ENTRY_ITEMS</span>
                            </span>
            </td>
        </tr>

        <tr ng-if="glossaryEntryItemVm.loading == false && glossaryEntryItems.length == 0">
            <td colspan="25" translate>NO_ENTRY_ITEMS</td>
        </tr>

        <tr ng-repeat="entryItem in glossaryEntryItems">
            <td></td>
            <td class="name"><span
                    ng-bind-html="entryItem.entry.defaultDetail.name | highlightText: entrySearchTerm"></span>
            </td>
            <td class="description">
                <span ng-bind-html="entryItem.entry.defaultDetail.description | highlightText: entrySearchTerm"></span>
            </td>
            <td style="width: 50px;text-align: center">
                <span ng-if="entryItem.entry.version > 0">{{entryItem.entry.version}}</span>
                <span ng-if="entryItem.entry.version == 0">-</span>
            </td>
            <td class="notes">
                <span ng-if="entryItem.entry.defaultDetail.notes != 'null'"
                      ng-bind-html="entryItem.entry.defaultDetail.notes | highlightText: entrySearchTerm"></span>
            </td>


            <td class="added-column"
                ng-repeat="objectAttribute in selectedAttributes">
                <p ng-if="objectAttribute.id != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'TEXT'
                                                && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.id">
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEM'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="entryItemsVm.showAttributeDetails(entryItem[attrName])">{{entryItem[attrName].itemNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEMREVISION'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="entryItemsVm.showAttributeDetails(entryItem[attrName])">
                        {{entryItem[attrName].itemMaster+" "+entryItem[attrName].revision+" "+
                        entryItem[attrName].lifeCyclePhase.phase}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'CHANGE'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="entryItemsVm.showAttributeDetails(entryItem[attrName])">{{entryItem[attrName].ecoNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURER'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="entryItemsVm.showAttributeDetails(entryItem[attrName])">{{entryItem[attrName].name}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="entryItemsVm.showAttributeDetails(entryItem[attrName])">{{entryItem[attrName].partNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'WORKFLOW'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="entryItemsVm.showAttributeDetails(entryItem[attrName])">{{entryItem[attrName].name}}
                    </a>
                    <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
                        {{entryItem[attrName].person.firstName}}
                    </a>
                </p>

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                                     && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                     ng-init="attrName = objectAttribute.id">
                    <p>
                        <a ng-if="entryItem[attrName].length > 0" href="">
                            {{entryItem[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in entryItem[attrName]">
                                <a href="" ng-click="openAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                    {{attachment.name}}
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>


                <%--             <div class="attributeTooltip"
                                  ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType == 'LIST'
                                          && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                                  ng-init="attrName = objectAttribute.id">
                                 <p>
                                     <a ng-if="entryItem[attrName].length > 0" href="">
                                         {{entryItem[attrName].length}} listOfValue
                                     </a>
                                 </p>

                                 <div class="attributeTooltiptext">
                                     <ul>
                                         <li ng-repeat="listOfValue in entryItem[attrName]">
                                             <a href=""
                                                style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                                 {{listOfValue}}
                                             </a>
                                         </li>
                                     </ul>
                                 </div>
                             </div>--%>

                <div>
                                            <span ng-if="objectAttribute.dataType == 'LIST' && (objectAttribute.listMultiple == true || objectAttribute.listMultiple == false)"
                                                  ng-init="attrName = objectAttribute.id">
                                                <p ng-if="entryItem[attrName] == null"
                                                   ng-hide="entryItem[attrName] != null">
                                                    {{objectAttribute.defaultListValue}}</p>

                                                <p ng-if="entryItem[attrName] != null">
                                                    {{entryItem[attrName]}}</p>
                                            </span>

                                            <span ng-if="objectAttribute.dataType == 'TEXT'"
                                                  ng-init="attrName = objectAttribute.id">
                                                <p ng-if="entryItem[attrName] == null"
                                                   ng-hide="entryItem[attrName] != null">
                                                    {{objectAttribute.defaultTextValue}}</p>

                                                <p ng-if="entryItem[attrName] != null">
                                                    {{entryItem[attrName]}}</p>
                                            </span>
                </div>

                <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                                 && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                     ng-init="attrName = objectAttribute.id">
                    <a href="" ng-click="showImage(entryItem[attrName])"
                       title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                        <img ng-if="entryItem[attrName] != null"
                             ng-src="{{entryItem[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="myModal2" class="img-model modal">
                        <span class="closeImage1">&times;</span>
                        <img class="modal-content" id="img03">
                    </div>
                </div>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'TEXT'
                                                && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'LIST'
                                                && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.id">
                    {{entryItem[attrName]}}
                </p>


                <%--  Show Rich text content in modal --%>




                  <span ng-init="attrName = objectAttribute.id"
                        ng-if="objectAttribute.dataType == 'RICHTEXT'">
                               <a href=""
                                  ng-if="entryItem[attrName] != null && entryItem[attrName] != undefined && entryItem[attrName] != ''"
                                  data-toggle="modal"
                                  ng-click="showRichTextSidePanel(entryItem[attrName],objectAttribute,entryItem.entry)"
                                  translate>CLICK_TO_SHOW_RICHTEXT</a>
                  </span>

                <span ng-init="currencyType = objectAttribute.name+'type'"
                      ng-if="objectAttribute.dataType == 'CURRENCY'"
                      ng-bind-html="entryItem[currencyType]">
                                                </span>
                                                <span ng-init="attrName = objectAttribute.id"
                                                      ng-if="objectAttribute.dataType == 'CURRENCY'">
                                                    {{entryItem[attrName]}}
                                                </span>

                <p ng-if="objectAttribute.name == 'modifiedBy'">{{entryItem.entry.modifiedByObject.firstName}}</p>

                <p ng-if="objectAttribute.name == 'createdBy'">{{entryItem.entry.createdByObject.firstName}}</p>

                <p ng-if="objectAttribute.name == 'createdDate'">{{entryItem.entry.createdDate}}</p>

                <p ng-if="objectAttribute.name == 'modifiedDate'">{{entryItem.entry.modifiedDate}}</p>

            </td>


            <td class="text-center">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                     <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                         style="z-index: 9999 !important;">
                         <li ng-click="glossaryEntryItemVm.showEditHistory(entryItem.entry)">
                             <a class="notification1" ng-show="entryItem.entry.entryEdits.length > 0">{{glossaryEntryItemVm.entryEditHistoryTitle}}
                                 <%--<span class="badge1">{{entryItem.entry.entryEdits.length}}</span>--%>
                             </a>
                         </li>
                         <li ng-click="glossaryEntryItemVm.showVersionHistory(entryItem.entry)">
                             <a href="" translate>ENTRY_VERSION_HISTORY</a>
                         </li>
                         <li ng-if=" hasPermission('glossary','edit') || selectGlossaryPermission.editPermission == true"
                             ng-show='selectedGlossary.lifeCyclePhase.phaseType != "RELEASED"'
                             ng-click="glossaryEntryItemVm.editEntry(entryItem.entry)">
                             <a href="" translate>EDIT_ENTRY</a>
                         </li>
                         <li ng-if="hasPermission('glossary','delete') || selectGlossaryPermission.editPermission == true"
                             ng-show='selectedGlossary.lifeCyclePhase.phaseType != "RELEASED"'
                             ng-click="glossaryEntryItemVm.deleteEntryItem(entryItem)">
                             <a href="" translate>DELETE_ENTRY</a>
                         </li>
                     </ul>
                </span>
            </td>

        </tr>
        </tbody>
    </table>
</div>