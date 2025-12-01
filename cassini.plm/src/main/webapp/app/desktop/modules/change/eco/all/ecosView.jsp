<div class="view-container" fitcontent>
    <style>
        #td {
            word-wrap: break-word;
            width: 300px;
            white-space: normal;
            text-align: left;
        }

        /*.added-column {
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
        }*/

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

        .view-content {
            position: relative;
        }

        .view-content .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 40px;
            top: 0;
            overflow: auto;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }

        .view-content .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0;
            height: 40px;
            width: 100%;
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

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
            /*background-color: #fff;*/
        }

        .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
            background-color: #d6e1e0;
        }

        .center {
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 5%;
            width: 200px;
        }

        .no-conversations .no-conversations-message {
            font-size: 20px;
            font-weight: 300 !important;
            text-align: center;
        }

    </style>

    <style scoped>

        #ecoFolder {
            padding-top: 10px;
        }

        .folder-item {
            background: transparent url("app/assets/images/gear_wheel.png") no-repeat !important;
            height: 16px;
        }

        .folder-change {
            background: transparent url("app/assets/images/admin_edit.gif") no-repeat !important;
            height: 16px;
        }

        .folder-mfr {
            background: transparent url("app/assets/images/mfr.png") no-repeat !important;
            height: 16px;
        }

        .items-loading {
            background: transparent url("app/assets/bower_components/cassini-platform/images/loaders/loader2.gif") no-repeat !important;
            height: 16px;
        }

        #ecoFolder .tree-file + span {
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        .ecoFolders-panel .context-menu {
            z-index: 9999 !important;
        }

    </style>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>ECOs</span>

        <button class="btn btn-sm new-button" ng-click="ecosVm.showNewECO()" id="newButton"
                ng-if="(hasPermission('change','eco','create') || hasPermission('change','create'))"
                title="{{'ECO_ALL_NEW_ECO' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'ECO' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-default" title="{{searchItemType}}" id="searchIcon"
                    style="" ng-click="ecosVm.ecosSearch()">
                <i class="fa fa-search" style=""></i>
            </button>
            <div class="btn-group" uib-dropdown>
                <button uib-dropdown-toggle class="btn btn-sm btn-warning dropdown-toggle" type="button"
                        id="dropdownButton"
                        ng-if="hasPermission('change','eco','edit')"
                        title="{{addItemsFolder}}">
                    <span><i class="fa fa-folder-o" style="" aria-hidden="true"></i></span>
                    <span class="caret"></span>
                </button>
                <div class="dropdown-menu" role="menu"
                     style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important;">
                    <div>

                        <div class="home-widget-panel1 ecoFolders-panel" style="height: 100%">

                            <div id="folderContextMenu" class="context-menu dropdown clearfix"
                                 style="position: fixed;display:none; z-index: 9999">
                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                                    <li id="addFoldereco"><a tabindex="-1" href=""
                                                             ng-click="ecosVm.addFolder($event)"
                                                             translate>ADD_FOLDER</a>
                                    </li>
                                    <li id="deleteFoldereco"><a tabindex="-1" href=""
                                                                ng-click="ecosVm.deleteFolder($event)" translate>
                                        DELETE_FOLDER</a></li>

                                </ul>
                            </div>
                            <%-- <div class="home-widget-panel-header">
                                <span class="panel-header-title">Folders</span>
                            </div>--%>
                            <div class="home-widget-panel-body1">
                                <div id="classificationContainer" class="tree-pane" data-toggle="context"
                                     data-target="#context-menu">
                                    <ul id="ecoFolder" class="easyui-tree">
                                    </ul>
                                </div>

                            </div>
                        </div>
                        <%-- <folder-tree on-select-folder="onSelectFolder"></folder-tree>--%>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-group" style="margin-left: 10px;">
            <button class="btn btn-sm btn-maroon" ng-click="ecosVm.showTypeAttributes()" id="attributes"
                    title="{{ecosVm.ecoAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-lightblue" ng-click="showEcoSavedSearches()" id="savedSearch"
                    title="{{ecosVm.savedSearchItems}}">
                <i class="fa fa-save" style=""></i>
            </button>
            <button ng-show="showSearch == true" class="btn btn-sm btn-darkblue"
                    ng-click="ecosVm.showSaveSearch()" title="{{ecosVm.saveSearchEcos}}">
                <i class="fa fa-clipboard" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferedPage"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="ecosVm.resetPage" search-term="ecosVm.searchText"
                          on-search="ecosVm.freeTextSearch" filter-search="ecosVm.filterSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="padding: 10px;height: 230px;">
        <div class="responsive-table scroll-style-1" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <%--   <div class="row">
                       <p class="col-sm-11"
                          ng-show="searchModeType == true"
                          style="color: #0390fd;font-size: 16px;text-align: center;" translate>
                           ALL_VIEW_ALERT</p>
                   </div>--%>
                <thead>
                <tr id="theadRow">
                    <th style="width: 30px">
                        <div ng-if="ecosVm.ecos.content.length > 1" class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="change{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="ecosVm.changeSelected" ng-click="ecosVm.selectAll()">
                            <label for="change{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 100px;" translate>ECO_NUMBER</th>
                    <!-- <th class="col-width-150" translate>ECO_TYPE</th> -->
                    <th>
                        <span ng-if="ecosVm.selectedEcoType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{ecosVm.selectedEcoType.name}})
                            <i class="fa fa-times-circle" ng-click="ecosVm.clearTypeSelection()"
                               title="{{removeTitle}}"></i>
                      </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>ECO_TYPE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>

                            <div class="dropdown-menu" role="menu">
                                <div
                                        style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                    <change-object-type-tree
                                            on-select-type="ecosVm.onSelectType"
                                            change-type="ECO"></change-object-type-tree>
                                </div>
                            </div>
                        </div>
                    </th>
                    <th class="col-width-200" translate>TITLE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                    <th></th>

                    <th style="width: 150px;z-index: auto !important;">
                            <span ng-if="selectedStatus != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                        ({{selectedStatus}})
                                        <i class="fa fa-times-circle" ng-click="clearStatus()"
                                           title="{{removeTitle}}"></i>
                                </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                                    <span uib-dropdown-toggle><span translate>STATUS</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="status in statuses"
                                    ng-click="onSelectStatus(status)" style="text-transform: uppercase;"><a
                                        href="">{{status}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px;" translate>CREATED_DATE</th>
                    <th style="width: 150px;" translate>RELEASED_REJECTED_DATE</th>
                    <th style="width: 150px;" translate>WORKFLOW</th>
                    <!-- <th class="col-width-150" translate>OWNER</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedPerson != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedPerson.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearChangeAnalyst()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>CHANGE_ANALYST</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;right:auto;margin-top:5px;">
                                <li ng-repeat=" person in changeAnalysts"
                                    ng-click="onSelectChangeAnalyst(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in ecosVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="ecosVm.removeAttribute(selectedAttribute)"
                           title={{ecosVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate="ITEM_ALL_ACTIONS"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="ecosVm.loading == true">
                    <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ECO</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="ecosVm.loading == false && ecosVm.ecos.content.length == 0">
                    <%-- <td colspan="12" translate="">NO_ECOS</td>--%>

                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/ECO.png" alt="" class="image">

                            <div class="message" translate>NO_ECOS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>

                </tr>

                <tr ng-repeat="change in ecosVm.ecos.content">
                    <td ng-if="ecosVm.ecos.content.length > 0">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="change{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="change.selected" ng-click="ecosVm.toggleSelection(change)">
                            <label for="change{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td style="width: 150px;">
                        <a href=""
                           ng-click="ecosVm.showEcoDetails(change)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="change.ecoNumber | highlightText: freeTextQuery"></span>
                        </a>

                    </td>

                    <td class="col-width-150">
                        <span>{{change.ecoTypeObject.name}}</span>
                    </td>
                    <td class="col-width-200">
                        <span ng-if="change.showValues == true"><span
                                ng-bind-html="change.title  | highlightText: freeTextQuery"></span>
                        </span>
                        <input ng-if="change.editMode == true" type=" text" class="form-control input-sm"
                               ng-model="change.newTitle">
                    </td>
                    <td class="col-width-250">
                        <span ng-if="change.showValues == true" title="{{change.description}}"><span
                                ng-bind-html="change.description | highlightText: freeTextQuery"></span>
                        </span>
                        <input ng-if="change.editMode == true" type=" text" class="form-control input-sm"
                               ng-model="change.newDescription">
                    </td>
                    <td class="col-width-250">
                        <span ng-if="change.showValues == true" title="{{change.reasonForChange}}"><span
                                ng-bind-html="change.reasonForChange | highlightText: freeTextQuery"></span>
                        </span>
                        <input ng-if="change.editMode == true" type=" text" class="form-control input-sm"
                               ng-model="change.newReasonForChange">
                    </td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='change.startWorkflow && !change.finishWorkflow && !change.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='change.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='change.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td style="width: 150px;">
                        <workflow-status-settings workflow="change"></workflow-status-settings>
                        <span class="label label-warning" ng-if="change.onHold">HOLD</span>
                    </td>
                    <td style="width: 150px;">
                        <span>{{change.createdDate}}</span>
                    </td>
                    <td style="width: 150px;">
                        <span>{{change.releasedDate}}</span>
                    </td>
                    <td style="width: 150px;">
                        <span>{{change.workflowObject.name}}</span>
                    </td>
                    <td class="col-width-150">{{change.ecoOwnerObject.fullName}}</td>

                    <td class="added-column"
                        ng-repeat="objectAttribute in ecosVm.selectedAttributes">
                        <all-view-attributes object="change" object-attribute="objectAttribute"></all-view-attributes>
                    </td>

                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="btn-group"
                              ng-if="change.editMode == true"
                              style="margin: -1px">
                    <button class="btn btn-xs btn-success" type="button" title="{{saveItemTitle}}"
                            ng-click="ecosVm.applyChanges(change)">
                        <i class="fa fa-check"></i>
                    </button>

                     <button class="btn btn-xs btn-danger" type="button"
                             title="{{cancelChangesTitle}}"
                             ng-click="ecosVm.cancelChanges(change)">
                         <i class="fa fa-times"></i>
                     </button>
                </span>

                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                           ng-hide="change.editMode == true">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <%--<li ng-if="change.editMode == true"
                                    ng-click="ecosVm.applyChanges(change)">
                                    <a translate>SAVE</a></li>

                                <li ng-if="change.editMode == true"
                                    ng-click="ecosVm.cancelChanges(change)"><a translate>CANCEL</a>
                                </li>--%>

                                <li ng-class="{'disabled': change.released == true}"
                                    ng-if="change.showValues == true && hasPermission('change','eco','edit')"
                                    ng-click="ecosVm.editECO(change)"><a translate>EDIT_ECO</a>
                                </li>
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(change.id,'ECO')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'CHANGE'" object="change.id"
                                             tags-count="change.tags.length"></tags-button>
                                <li title="{{change.released || change.cancelled ? cannotDeleteApprovedEco:''}}">
                                    <a ng-class="{'disabled': change.released == true || change.cancelled}"
                                       ng-if="change.showValues == true && (hasPermission('change','eco','delete') || hasPermission('change','delete'))"
                                       ng-click="ecosVm.deleteECO(change)" translate>DELETE_ECO_TITLE</a></li>
                                <plugin-table-actions context="eco.all" object-value="change"></plugin-table-actions>
                            </ul>
                         </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="ecosVm.ecos" pageable="ecosVm.pageable"
                          previous-page="ecosVm.previousPage" next-page="ecosVm.nextPage"
                          page-size="ecosVm.pageSize"></table-footer>
        </div>
    </div>
</div>