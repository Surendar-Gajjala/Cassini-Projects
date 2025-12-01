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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .scroll-style-1::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
        border-radius: 10px;
        background-color: #F5F5F5;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

</style>

<style scoped>

    #itemFolder {
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

    #mfrFolder .tree-file + span {
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    .itemfolders-panel .context-menu {
        z-index: 9999 !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MANUFACTURERS_TITLE</span>

        <button class="btn btn-sm new-button" ng-if="hasPermission('manufacturer','create') " id="newManufacturer"
                ng-click="allMfrsVm.showNewManufacture()" title="{{'NEW_MANUFACTURER' | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW_MANUFACTURER' | translate}}</span>
        </button>

        <div class="btn-group">
            <div class="btn-group" ng-if="hasPermission('manufacturer','edit')" uib-dropdown>
                <button uib-dropdown-toggle class="btn btn-sm btn-warning dropdown-toggle" type="button"
                        title="{{allMfrsVm.mfrFolderTitle}}">
                    <span><i class="fa fa-folder-o" style="" aria-hidden="true"></i></span>
                    <span class="caret"></span>
                </button>

                <div class="dropdown-menu" role="menu"
                     style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important;">
                    <div>
                        <div class="home-widget-panel1 itemfolders-panel" style="height: 100%">

                            <div id="folderContextMenu" class="context-menu dropdown clearfix"
                                 style="position: fixed;display:none; z-index: 9999">
                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                                    <li id="addFoldermfr"><a tabindex="-1" href=""
                                                             ng-click="allMfrsVm.addFolder($event)"
                                                             translate>ADD_FOLDER</a>
                                    </li>
                                    <li id="deleteFoldermfr"><a tabindex="-1" href=""
                                                                ng-click="allMfrsVm.deleteFolder($event)" translate>
                                        DELETE_FOLDER</a></li>

                                </ul>
                            </div>
                            <%-- <div class="home-widget-panel-header">
                                <span class="panel-header-title">Folders</span>
                            </div>--%>
                            <div class="home-widget-panel-body1">
                                <div id="classificationContainer" class="tree-pane" data-toggle="context"
                                     data-target="#context-menu">
                                    <ul id="mfrFolder" class="easyui-tree">
                                    </ul>
                                </div>

                            </div>
                        </div>
                        <%--<folder-tree on-select-folder="onSelectFolder"></folder-tree>--%>
                    </div>
                </div>
                <%--<div class="dropdown-menu" role="menu">
                    <div>
                        <folder-tree on-select-folder="onSelectFolder"></folder-tree>
                    </div>
                </div>--%>
            </div>
            <button class="btn btn-sm btn-primary" ng-if="hasPermission('manufacturer','create')"
                    ng-click="allMfrsVm.createParts()" title="{{allMfrsVm.mfrPartTitle}}">
                <i class="fa fa-cubes"></i>
            </button>
        </div>
        <div class="btn-group" style="margin-left: 10px;">
            <button class="btn btn-sm btn-maroon"
                    ng-click="allMfrsVm.showTypeAttributes()" title="{{allMfrsVm.mfrAttributesTitle}}">
                <i class="fa fa-newspaper-o"></i>
            </button>
            <button class="btn btn-sm btn-lightblue" ng-click="showMfrSavedSearches()"
                    title="{{allMfrsVm.savedSearchItems}}">
                <i class="fa fa-save"></i>
            </button>
            <button ng-show="showSearch == true" class="btn btn-sm btn-darkblue" title="{{allMfrsVm.saveSearchMfrs}}"
                    ng-click="allMfrsVm.showSaveSearch()">
                <i class="fa fa-clipboard"></i>
            </button>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
            <%--<button ng-show="allMfrsVm.showShareButton == true && hasPermission('manufacturer','edit')"
                    class="btn btn-sm btn-default"
                    style="border-radius:4px;"
                    ng-click="allMfrsVm.shareSelectedMfrs()" title="{{allMfrsVm.shareItem}}">
                <i class="fa fa-share-alt" style=""></i>
            </button>--%>
        </div>
        <%--<p class="blink"
           ng-show="searchModeType == true"
           style="color: #0390fd;margin-top: -25px;margin-left: 700px; font-size: 16px;" translate>ALL_VIEW_ALERT</p>--%>
        <free-text-search on-clear="allMfrsVm.resetPage" search-term="allMfrsVm.searchText"
                          on-search="allMfrsVm.freeTextSearch"
                          filter-search="allMfrsVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;height: 230px;">
        <div class="responsive-table scroll-style-1" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 40px">
                        <div ng-if="allMfrsVm.manufacturers.content.length > 1" class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="manufacturer{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="allMfrsVm.flag" ng-click="allMfrsVm.selectAll()">
                            <label for="manufacturer{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th class="col-width-250" translate>MANUFACTURER_NAME</th>
                    <!-- <th style="width: 200px;" translate>MANUFACTURER_TYPE</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="allMfrsVm.selectedMfrType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{allMfrsVm.selectedMfrType.name}})
                                <i class="fa fa-times-circle" ng-click="allMfrsVm.clearTypeSelection()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>
                            <div class="dropdown" uib-dropdown style="display: inline-block"
                                 ng-if="allMfrsVm.search.searchType != 'advanced'">
                                <span uib-dropdown-toggle><span translate>MANUFACTURER_TYPE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>

                                <div class="dropdown-menu" role="menu"
                                     style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important; min-height: 300px !important;margin-top:5px;font-weight: normal;">
                                            <workflow-Manufacturer 
                                                                 on-select-type="allMfrsVm.onSelectType">
                                            </workflow-Manufacturer>                 
                                </div>
                            </div>
                    </th>


                    <th class="description-column" translate>DESCRIPTION</th>
                    <th translate>LIFECYCLE</th>
                    <th style="width: 200px;" translate>PHONE_NUMBER</th>
                    <th style="width: 200px;" translate>CONTACT_PERSON</th>
                    <%--     <th style="width: 200px;" ng-repeat="mfrAttribute in allMfrsVm.selectedMfrAttributes">
                             {{mfrAttribute.name}}
                         </th>--%>
                    <th class='added-column'
                        style="width: 150px;"
                        ng-repeat="selectedAttributes in allMfrsVm.selectedAttributes">
                        {{selectedAttributes.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allMfrsVm.removeAttribute(selectedAttributes)"
                           title="Remove this column"></i>
                    </th>
                    <th style="width: 50px;">Actions</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="allMfrsVm.loading == true">
                    <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_MFRS</span>
                    </span>
                    </td>
                </tr>
                <tr ng-if="allMfrsVm.loading == false && allMfrsVm.manufacturers.content.length == 0">
                    <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Manufacturers.png" alt="" class="image">

                            <div class="message" translate>NO_MFRS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="manufacturer in allMfrsVm.manufacturers.content">
                    <td style="width: 40px;" ng-if="allMfrsVm.manufacturers.content.length > 0">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="manufacturer{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="manufacturer.selected" ng-click="allMfrsVm.selectCheck(manufacturer)">
                            <label for="manufacturer{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td class="col-width-250">
                        <a href=""
                           ng-click="allMfrsVm.showMfrDetails(manufacturer)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="manufacturer.name | highlightText: freeTextQuery"></span>
                        </a>
                        <%--<span>--%>
                        <%--<span ng-bind-html="manufacturer.name | highlightText: freeTextQuery"></span>--%>
                        <%--</span>--%>
                    </td>
                    <td>
                        <span ng-bind-html="manufacturer.mfrType.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td title="{{manufacturer.description}}" class="description-column">
                            <span ng-bind-html="manufacturer.description  | highlightText: freeTextQuery"></span>
                        </span>
                        <input ng-if="manufacturer.editMode == true" type=" text" class="form-control input-sm"
                               ng-model="manufacturer.description">
                    </td>
                    <td>
                        <item-status item="manufacturer"></item-status>
                    </td>
                    <td title="{{manufacturer.phoneNumber}}"  style="width: 200px;">
                        <span ng-bind-html="manufacturer.phoneNumber | highlightText: freeTextQuery"></span>
                        <input ng-if="manufacturer.editMode == true" type=" text" class="form-control input-sm"
                               ng-model="manufacturer.phoneNumber">
                    </td>
                    <td title="{{manufacturer.contactPerson}}" style="width: 200px;">
                        <span ng-bind-html="manufacturer.contactPerson | highlightText: freeTextQuery"></span>
                        <input ng-if="manufacturer.editMode == true" type=" text" class="form-control input-sm"
                               ng-model="manufacturer.contactPerson">
                    </td>

                    <td class="added-column"
                        ng-repeat="mfrAttribute in allMfrsVm.selectedAttributes">
                        <all-view-attributes object="manufacturer"
                                             object-attribute="mfrAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="btn-group" ng-if="manufacturer.editMode == true" style="margin: -1px">
                            <i title="Save"
                               ng-click="allMfrsVm.applyChanges(manufacturer)"
                               class="la la-check">
                            </i>
                            <i title="{{cancelChangesTitle}}"
                               ng-click="allMfrsVm.cancelChanges(manufacturer)"
                               class="la la-times">
                            </i>
                        </span>
                        <span ng-if="manufacturer.editMode != true" class="row-menu" uib-dropdown
                              dropdown-append-to-body
                              style="right: 17px;">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">

                                <li ng-if="manufacturer.editMode == false && hasPermission('manufacturer','edit')"
                                    ng-click="allMfrsVm.editMfr(manufacturer)"><a translate>EDIT</a>
                                </li>
                                <tags-button object-type="'MANUFACTURER'" object="manufacturer.id"
                                             tags-count="manufacturer.tags.length"></tags-button>
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(manufacturer.id,'MANUFACTURER')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <plugin-table-actions context="manufacturer.all"
                                                      object-value="manufacturer"></plugin-table-actions>
                            </ul>
                     </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allMfrsVm.manufacturers" pageable="allMfrsVm.pageable"
                          previous-page="allMfrsVm.previousPage"
                          next-page="allMfrsVm.nextPage" page-size="allMfrsVm.pageSize"></table-footer>
        </div>
    </div>

</div>