<div class="view-container" fitcontent>

    <style scoped>
        .split-left-pane {
            width: 15% !important;
            overflow-x: auto !important;
            height: 88% !important;
        }

        .split-pane.fixed-left {
            position: absolute !important;
            top: 0 !important;
            height: 113% !important;
            overflow: hidden !important;
            z-index: 1 !important;
        }

        .tree-node {
            white-space: nowrap !important;
            cursor: pointer !important;
            width: fit-content !important;
        }

        .requirements-node {
            background: transparent url("app/assets/images/requirements.png") no-repeat !important;
            height: 16px;
        }

        .my-search {
            border: 0;
            outline: 0;
            background: transparent;
            box-shadow: 0px 0px 0px #d3d3d3;
            border-bottom: 1px solid #d3d3d3;
            margin: 5px 0px;
            width: 210px;
            text-align: center;
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky !important;
            position: sticky !important;
            top: -10px !important;
            z-index: 4 !important;
        }

        .search-form {
            border-radius: 3px;
        }

        .tab-content .tab-pane {
            overflow: hidden !important;
        }
    </style>
    <style>
        .added-column {
            text-align: left;
            width: 350px;
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
            background-color: #7BB7EB;
            color: #141f9f;
            text-align: left;
            border-radius: 6px;
            padding: 5px 0;
            position: absolute;
            z-index: 1;
            top: -5px;
            bottom: auto;
            opacity: 0;
            transition: opacity 1s;
            margin-left: -25px !important;
            word-break: break-all !important;

        }

        .attributeTooltip .attributeTooltiptext::after {
            content: "";
            position: absolute;
            top: 25%;
            left: 102%;
            border-width: 5px;
            border-style: solid;
            border-color: transparent transparent transparent #7BB7EB;
        }

        .attributeTooltip:hover .attributeTooltiptext {
            position: relative !important;
            visibility: visible;
            opacity: 1;
        }

        /* The Close Button */
        .img-model .closeImage,
        .img-model .closeImage1 {
            position: absolute;
            top: 50px;
            right: 50px;

            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage:hover,
        .img-model .closeImage:focus,
        .img-model .closeImage1:hover,
        .img-model .closeImage1:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .img-model .closeImage1 {
            position: absolute;
            top: 50px;
            right: 50px;

            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage1:hover,
        .img-model .closeImage1:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .notification1 {
            text-decoration: none;
            padding: 9px 0;
            position: relative;
            display: inline-block;
            border-radius: 2px;
        }

        .notification1 .badge12 {
            position: absolute;
            top: -4px;
            right: -16px;
            padding: 0px 7px;
            border-radius: 50%;
            background-color: orange;
            color: white;
        }

        .search-box > input {
            height: 30px;
            border-radius: 15px;
            padding-left: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            width: 300px;
            box-shadow: none;
        }

        .search-box > input:hover {
            background-color: rgb(229, 231, 234);
        }

        .search-box > input:focus {
            box-shadow: none;
        }

        .search-box i.fa-search {
            position: relative;
            margin-top: 11px;
            color: grey;
            opacity: 0.5;
            margin-right: -28px !important;
        }

        .search-box i.fa-times-circle {
            margin-left: -27px;
            color: gray;
            cursor: pointer;
        }

        .view-content .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 48px !important;
            height: 10%;
            width: 98%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .view-content .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .view-content .table-footer > div h5 {
            margin: 0;
        }

        .view-content .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .view-content .table-footer > div > div > i {
            font-size: 16px;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        p.limit.ng-binding.ng-scope.tl {
            min-width: 200px !important;
            max-width: 200px;;
            margin-bottom: 12px;
            word-wrap: break-word;
            overflow: visible;
            white-space: normal;
            height: auto; /* just added this line */
        }

        th.added-column.ng-binding.ng-scope {
            min-width: 200px !important;
            max-width: 200px;;
            margin-bottom: 12px;
            word-wrap: break-word;
            overflow: visible;
            white-space: normal;
        }


    </style>

    <div class="view-content  no-padding" style="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="margin-top: -25px"
                ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'">
                <li><a tabindex="-1" id="addType" href="" ng-click="specElementsVm.createNewRequirement()">
                    <span translate="NEW_REQUIREMENT"></span></a></li>
                <%--<li><a tabindex="-1" id="deleteType" href="" ng-click="specElementsVm.deleteType()">
                    <span translate>DELETE_TYPE</span></a>
                </li>--%>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div style="height: 40px;">
                        <input type="search" class="form-control input-sm search-form"
                               placeholder={{searchTitle}}
                               ng-model="specElementsVm.searchValue" ng-change="specElementsVm.searchTree()">
                    </div>
                    <ul id="classificationTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;overflow: auto">

                <div class="responsive-table" style="width: 98% !important; height: 74% !important;">
                    <table class='table table-striped'>
                        <thead>


                        <div class="search-box" style="text-align: center">
                            <i class="fa fa-search"></i>
                            <input type="search"
                                   class="form-control input-sm req-search-form"
                                   onfocus="this.setSelectionRange(0, this.value.length)"
                                   ng-model="specElementsVm.reqSearchFilter.searchQuery"
                                   ng-model-options="{ debounce: 500 }"
                                   ng-change="specElementsVm.freeTextSearch(specElementsVm.reqMode)">
                            <i class="fa fa-times-circle" title="{{clearTitleSearch}}"
                               ng-show="specElementsVm.reqSearchFilter.searchQuery.length > 0"
                               ng-click="specElementsVm.reqSearchFilter.searchQuery = '';specElementsVm.freeTextSearch()"></i>
                        </div>


                        <tr>
                            <th style="width: 50px;" translate>ITEM_ALL_ACTIONS</th>
                            <th class="" translate>NUMBER</th>
                            <th class="" translate>NAME</th>
                            <th class="" translate>DESCRIPTION</th>
                            <th class="" translate>VERSION</th>
                            <th class="" translate>STATUS</th>
                            <th class="" translate>ASSIGNED_TO
                            </th>
                            <th class="" translate>
                                PLANNED_FINISH_DATE
                            </th>
                            <th class='added-column'
                                style="word-wrap: break-word;white-space: normal;min-width: 200px;"
                                ng-repeat="selectedAttribute in specElementsVm.selectedAttribute">

                <span>{{selectedAttribute.name}}
                    <i class="fa fa-times-circle"
                       ng-click="specElementsVm.removeReqAttribute(selectedAttribute)"
                       title="Remove this column"></i>
                </span>


                            </th>
                        </tr>
                        </thead>


                        <tr id="search" ng-if="specElementsVm.searchAttributes.length > 0">
                            <td>
                                <div class="btn-group btn-group-xs" style="display: inline-flex !important;">

                                    <button title="{{searchTitle}}" type="button" class="btn btn-xs btn-success"
                                            ng-click="specElementsVm.freeTextSearch(specElementsVm.reqMode)">
                                        <i class="fa fa-search"></i>
                                    </button>

                                    <button title="{{clearTitle}}" type="button" class="btn btn-xs btn-default"
                                            ng-click="specElementsVm.clearFilter()">
                                        <i class="fa fa-times"></i>
                                    </button>
                                </div>
                            </td>

                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td class="added-column" ng-repeat="objectAttribute in specElementsVm.searchAttributes">
                                <div class="planned-finish-date-col"
                                     ng-if="objectAttribute.objectTypeAttribute.dataType == 'DATE'"
                                     style="text-align: center; width: 190px;">
                                    <div class="input-group">

                                        <input date-picker-edit class="form-control date-picker" type="text"
                                               style="width: 134px !important;"
                                               placeholder="Date"
                                               ng-click="specElementsVm.search(objectAttribute)"
                                               ng-model="objectAttribute.date"/>

                                    </div>
                                </div>

                       <span class="" style="text-align: center; width: 100px;"
                             ng-if="objectAttribute.objectTypeAttribute.dataType == 'TEXT'">
                         <input type="text" class="form-control" name="Name"
                                style="width: 90px !important;"
                                ng-change="specElementsVm.search(objectAttribute)"
                                placeholder="Text"
                                ng-model="objectAttribute.text"
                                ng-model-options="{ debounce: 500 }">
                      </span>

                   <span class="" style="text-align: center; width: 100px;"
                         ng-if="objectAttribute.objectTypeAttribute.dataType == 'LONGTEXT'">
                       <input type="text" class="form-control" name="Name"
                              style="width: 90px !important;"
                              ng-change="specElementsVm.search(objectAttribute)"
                              placeholder="Long Text"
                              ng-model="objectAttribute.longText"
                              ng-model-options="{ debounce: 500 }">
                        </span>

                       <span class="" style="text-align: center; width: 100px;"
                             ng-if="objectAttribute.objectTypeAttribute.dataType == 'INTEGER'">
                         <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                                ng-change="specElementsVm.search(objectAttribute)"
                                placeholder="Integer"
                                ng-model="objectAttribute.integer"
                                ng-model-options="{ debounce: 500 }">
                        </span>

                       <span class="" style="text-align: center; width: 100px;"
                             ng-if="objectAttribute.objectTypeAttribute.dataType == 'CURRENCY'">
                       <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                              ng-change="specElementsVm.search(objectAttribute)"
                              placeholder="Currency Value"
                              ng-model="objectAttribute.currency"
                              ng-model-options="{ debounce: 500 }">
                    </span>
                   <span class="" style="text-align: center; width: 100px;"
                         ng-if="objectAttribute.objectTypeAttribute.dataType == 'DOUBLE'">
                     <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                            ng-change="specElementsVm.attributeBoolean(objectAttribute)"
                            placeholder="Double Value"
                            ng-model="objectAttribute.aDouble"
                            ng-model-options="{ debounce: 500 }">
                        </span>

                 <span style="width: 150px;text-align:center;"
                       ng-if="objectAttribute.objectTypeAttribute.dataType == 'BOOLEAN'">
                      <ui-select ng-model="objectAttribute.aBoolean" style="width: 85px !important;"
                                 on-select="specElementsVm.attributeBoolean(objectAttribute)"
                                 theme="bootstrap"
                                 style="width:100%">
                          <ui-select-match allow-clear="false"
                                           placeholder="Boolean">


                              <a class="btn btn-xs btn-link pull-right"
                                 ng-click="clear($event, $select)"><i
                                      class="glyphicon glyphicon-remove a"></i></a>

                              {{$select.selected}}
                          </ui-select-match>
                          <ui-select-choices
                                  repeat="boolean in specElementsVm.booleanValue | filter: $select.search">
                              <div ng-bind="boolean | highlight: $select.boolean.search"></div>
                          </ui-select-choices>
                      </ui-select>
               </span>


              <span style="text-align: center; width: 100px !important;"
                    ng-if="objectAttribute.objectTypeAttribute.dataType == 'LIST' && objectAttribute.objectTypeAttribute.listMultiple == false">
                <ui-select ng-model="objectAttribute.list"
                           ng-change="specElementsVm.search(objectAttribute)" theme="bootstrap"

                           style="width:100%">
                    <ui-select-match placeholder="Select">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="lovValue in specElementsVm.lovValues | filter: $select.search">
                        <div ng-bind="lovValue"></div>
                    </ui-select-choices>
                </ui-select>

                </span>
                <span style="text-align: center; width: 100px !important;"
                      ng-if="objectAttribute.objectTypeAttribute.dataType == 'LIST' && objectAttribute.objectTypeAttribute.listMultiple == true">
                       <ui-select multiple ng-model="objectAttribute.mListvalue" theme="bootstrap"
                                  on-select="specElementsVm.mListValueSelect(objectAttribute,$item)"
                                  on-remove="specElementsVm.removeMlistValue(objectAttribute,$item)"
                                  checkboxes="true"
                                  close-on-select="false" title="Choose a List" remove-selected="true">
                           <ui-select-match placeholder="Select listValues...">{{$item}}</ui-select-match>
                           <ui-select-choices repeat="lovValue in specElementsVm.lovValues | filter: $select.search">
                               <div ng-bind="lovValue"></div>
                           </ui-select-choices>
                       </ui-select>
                 </span>

                <span class="" style="text-align: center; width: 100px;"
                      ng-if="objectAttribute.objectTypeAttribute.dataType == 'TIME'">
                <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                       ng-click="specElementsVm.attributeBoolean(objectAttribute)"
                       placeholder="Time Value"
                       ng-model="objectAttribute.time"
                       ng-model-options="{ debounce: 500 }" time-picker>
                 </span>
                            </td>
                        </tr>


                        <tbody>
                        <tr ng-hide="specElementsVm.selectedSpecElements.content == 0"
                            ng-repeat="item in specElementsVm.selectedSpecElements.content track by $index">

                            <td class="text-center">
                                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                     <i class="fa fa-ellipsis-v dropdown-toggle" uib-dropdown-toggle></i>
                                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                            style="z-index: 9999 !important;height: 119px !important;overflow-y: auto !important; margin: -35px 39px 0 0 !important;">
                                            <li ng-click="specElementsVm.showEditHistory(item)">
                                                <a href="" translate>REQUIREMENT_EDIT_TITLE</a>
                                            </li>
                                            <li ng-click="specElementsVm.showRevisionHistory(item.requirement)">
                                                <a href="" translate>REQUIREMENT_SHOW_VERSION_HISTORY</a>
                                            </li>
                                            <li ng-if="(hasPermission('admin','all') || hasPermission('pgcspecification','edit') || specPermission.editPermission == true)"
                                                ng-hide="item.requirement.finalAcceptEdit || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' ||  selectedSpecification.lifecyclePhase.phaseType == 'PRELIMINARY' || item.requirement.status == 'FINISHED'"
                                                ng-click="specElementsVm.editRequirement(item)">
                                                <a href="" translate>EDIT_REQUIREMENT</a>
                                            </li>
                                            <li ng-if="(hasPermission('admin','all') || hasPermission('pgcspecification','delete') || specPermission.deletePermission == true)"
                                                ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                                                ng-click="deleteSpecElement(item)">
                                                <a href="" translate>DELETE_REQUIREMENT</a>
                                            </li>

                                            <li ng-click="specElementsVm.compareVersion(item.requirement)">
                                                <a href="" translate>COMPARE</a>
                                            </li>

                                        </ul>
                                </span>
                            </td>


                            <td>
                                <span>
                                     <a href="" class="notification1"
                                        ng-click="specElementsVm.showRequirementDetails(item.requirement)">

                                         <span ng-bind-html="item.requirement.objectNumber | highlightText: specElementsVm.searchText"></span>
                                         <span class="badge12" ng-if="item.reqEditsLength > 0"
                                               title="{{'NO_OF_REQ_EDITS' | translate}}">{{item.reqEditsLength}}</span>
                                     </a>
                                </span>
                            </td>

                            <td>
                                  <span style="white-space: normal;word-wrap: break-word;">
                                 <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                                           webkit-column-count: 1;-moz-column-count: 1;column-count: 1;margin-top: -7px;display: inline-flex !important;">
                               <span style="max-height: 250px !important;display:inline-table !important;overflow:auto !important;"
                                     ng-bind-html="item.seqNumber"></span> &nbsp;&nbsp; <span
                                         style="max-height: 250px !important;display:block;overflow:auto !important;"
                                         ng-bind-html="item.requirement.name | highlightText: specElementsVm.searchText"></span>
                                 </div>
                             </span>

                            </td>

                            <td>

                                <span style="white-space: normal;word-wrap: break-word;">
                                 <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                                           webkit-column-count: 1;-moz-column-count: 1;column-count: 1;margin-top: 9px;">
                                <span style="max-height: 250px !important;display:block;overflow:auto !important;"
                                      ng-bind-html="item.requirement.description"></span>
                                 </div>
                             </span>
                            </td>


                            <td>
                                 <span ng-if="item.requirement.version > 0"
                                       style="width: 85px !important;">{{item.requirement.version}}</span>
                                <span ng-if="item.requirement.version == 0" style="width: 85px !important;">-</span>
                            </td>
                            <td>
                                <task-status task="item.requirement"></task-status>
                            </td>
                            <td>
                                <span ng-bind-html="item.requirement.assignedTo.fullName | highlightText: specElementsVm.searchText"></span>
                            </td>
                            <td>
                                {{item.requirement.plannedFinishDate}}
                            </td>

                            <td class="added-column"

                                ng-repeat="objectAttribute in specElementsVm.selectedAttribute">
                                <p class="limit"
                                   ng-class="{'tl': objectAttribute.dataType == 'TEXT' || objectAttribute.dataType == 'LONGTEXT'}"
                                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                                   ng-init="attrName = objectAttribute.id">
                                    <a href=""
                                       ng-if="objectAttribute.refType == 'ITEM'"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                       ng-click="specElementsVm.showAttributeDetails(item[attrName])">{{item[attrName].itemNumber}}
                                    </a>
                                    <a href=""
                                       ng-if="objectAttribute.refType == 'ITEMREVISION'"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                       ng-click="specElementsVm.showAttributeDetails(item[attrName])">
                                        {{item[attrName].itemMaster+" "+item[attrName].revision+" "+
                                        item[attrName].lifeCyclePhase.phase}}
                                    </a>
                                    <a href=""
                                       ng-if="objectAttribute.refType == 'CHANGE'"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                       ng-click="specElementsVm.showAttributeDetails(item[attrName])">{{item[attrName].ecoNumber}}
                                    </a>
                                    <a href=""
                                       ng-if="objectAttribute.refType == 'MANUFACTURER'"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                       ng-click="specElementsVm.showAttributeDetails(item[attrName])">{{item[attrName].name}}
                                    </a>
                                    <a href=""
                                       ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                       ng-click="specElementsVm.showAttributeDetails(item[attrName])">{{item[attrName].partNumber}}
                                    </a>
                                    <a href=""
                                       ng-if="objectAttribute.refType == 'WORKFLOW'"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                       ng-click="specElementsVm.showAttributeDetails(item[attrName])">{{item[attrName].name}}
                                    </a>
                                    <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
                                        {{item.requirement.assignedTo[attrName].firstName}}
                                    </a>
                                </p>

                                <div class="attributeTooltip"
                                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                                     ng-init="attrName = objectAttribute.id">
                                    <p>
                                        <a ng-if="item[attrName].length > 0" href="">
                                            {{item[attrName].length}} Attachments
                                        </a>
                                    </p>

                                    <div class="attributeTooltiptext">
                                        <ul>
                                            <li ng-repeat="attachment in item[attrName]">
                                                <a href="" ng-click="specElementsVm.openAttachment(attachment)"
                                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                                    {{attachment.name}}
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>

                                <div class="limit"
                                     ng-class="{'tl': objectAttribute.dataType == 'TEXT' || objectAttribute.dataType == 'LONGTEXT'}"
                                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE' && objectAttribute.dataType != 'RICHTEXT'"
                                     ng-init="attrName = objectAttribute.id">
                                    <a href="" ng-click="specElementsVm.showImage(item[attrName])"
                                       title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                                        <img ng-if="item[attrName] != null"
                                             ng-src="{{item[attrName]}}"
                                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                                    </a>

                                    <div id="myModal2" class="img-model modal">
                                        <span class="closeImage1">&times;</span>
                                        <img class="modal-content" id="img03">
                                    </div>
                                </div>

                                <p class="limit"
                                   ng-class="{'tl': objectAttribute.dataType == 'LONGTEXT'}"
                                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT' && objectAttribute.dataType != 'TEXT' && objectAttribute.dataType != 'LIST'"
                                   ng-init="attrName = objectAttribute.id">
                                    {{item[attrName]}}
                                </p>


                                <div class="limit">
                                 <span ng-if="objectAttribute.dataType == 'LIST' && (objectAttribute.listMultiple == true || objectAttribute.listMultiple == false)"
                                       ng-init="attrName = objectAttribute.id">
                                     <p ng-if="item[attrName] == null" ng-hide="item[attrName] != null">
                                         {{objectAttribute.defaultListValue}}</p>

                                     <p ng-if="item[attrName] != null">
                                         {{item[attrName]}}</p>
                                 </span>

                                 <span ng-if="objectAttribute.dataType == 'TEXT'"
                                       ng-init="attrName = objectAttribute.id">
                                     <p ng-if="item[attrName] == null && objectAttribute.defaultTextValue != null">
                                         {{objectAttribute.defaultTextValue}}</p>

                                     <p ng-if="item[attrName] != null">
                                         {{item[attrName]}}</p>
                                 </span>
                                </div>

                                <%--  Show Rich text content in modal --%>
                     <span style="height: 30px;width: 40px;margin-bottom: 5px;" ng-init="attrName = objectAttribute.id"
                           ng-if="objectAttribute.dataType == 'RICHTEXT'">
                          <a href=""
                             ng-if="item[attrName] != null && item[attrName] != undefined && item[attrName] != ''"
                             data-toggle="modal"
                             ng-click="showRichTextSidePanel(item[attrName],objectAttribute,item.requirement)">Click to
                              show
                              RichText</a>
                     </span>


                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="item[currencyType]"
                                >
                        </span>

                 <span ng-init="attrName = objectAttribute.id"
                       ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{item[attrName]}}
                        </span>

                                <p ng-if="objectAttribute.name == 'ModifiedBy'">
                                    {{item.modifiedByObject.firstName}}</p>

                                <p ng-if="objectAttribute.name == 'CreatedBy'">
                                    {{item.createdByObject.firstName}}</p>

                                <p ng-if="objectAttribute.name == 'CreatedDate'">{{item.requirement.createdDate}}</p>

                                <p ng-if="objectAttribute.name == 'ModifiedDate'">{{item.requirement.modifiedDate}}</p>

                            </td>


                        </tr>
                        </tbody>
                    </table>

                </div>


                <div class="table-footer" ng-if="specElementsVm.selectedSpecElements.content.length > 0">
                    <div>
                        <div>
                            <h5><span translate>DISPLAYING</span>{{specElementsVm.selectedSpecElements.numberOfElements}}
                                of
                                {{specElementsVm.selectedSpecElements.totalElements}}</h5>
                        </div>

                        <div class="text-right">
                            <span class="mr10"><span translate>PAGE</span> {{specElementsVm.selectedSpecElements.totalElements != 0 ? specElementsVm.selectedSpecElements.number+1:0}} of {{specElementsVm.selectedSpecElements.totalPages}}</span>
                            <a href="" ng-click="specElementsVm.previousPage()"
                               ng-class="{'disabled': specElementsVm.selectedSpecElements.first}"><i
                                    class="fa fa-arrow-circle-left mr10"></i></a>
                            <a href="" ng-click="specElementsVm.nextPage()"
                               ng-class="{'disabled': specElementsVm.selectedSpecElements.last}"><i
                                    class="fa fa-arrow-circle-right"></i></a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
