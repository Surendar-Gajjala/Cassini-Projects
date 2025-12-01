<div>
    <style scoped>
        #td {
            word-wrap: break-word;
            width: 300px;
            white-space: normal;
            text-align: left;
        }

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

        .product-image {
            width: 100%;
            height: 300px;
        }

        .view-content {
            position: relative;
        }

        .notification1 {
            text-decoration: none;
            position: relative;
            display: inline-block;
            border-radius: 2px;
        }

        .notification1 .badge1 {
            position: absolute;
            top: -6px;
            left: -10px;
            padding: 2px 6px;
            border-radius: 50%;
            background-color: grey;
            color: white;
            font-size: 14px;
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
            bottom: 0px !important;
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

        .popover-title {
            font-size: 14px;
            font-weight: 600;
            text-align: center;
            line-height: 25px;
        }

        .popover {
            max-width: 500px;
            width: 500px;
        }

        .popover-content {
            max-height: 220px;
            overflow-y: auto;
        }

        .popover table {
            width: 497px;
            max-width: 100% !important;
        }

        .popover.bottom > .arrow::after {
            border-bottom-color: #f7f7f7;
        }

        table {
            table-layout: fixed;
        }

        /*table.highlight-row tbody tr:hover td a, table.highlight-row tbody tr:hover td i {

        }*/

        .responsive-table.dropdown-content a:hover, .responsive-table.dropdown-content i:hover {
            color: white !important;
        }

        .select-item td {
            background-color: #ddd !important;
            /*color: white !important;*/
        }

        .split-pane-component .responsive-table {
            padding: 0;
            position: absolute;
            bottom: 40px;
            top: 0;
            overflow: auto;
        }

        .split-pane-component .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            /*background-color: #fff;*/
        }

        .split-pane-component .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .split-pane-component .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .split-pane-component .table-footer > div h5 {
            margin: 0;
        }

        .split-pane-component .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .split-pane-component .table-footer > div > div > i {
            font-size: 16px;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .tab-content {
            padding: 0px !important;
        }

        .tab-content .tab-pane {
            overflow: auto !important;
        }

        .tab-pane {
            position: relative;
        }

        .tab-content .tab-pane .responsive-table {
            height: 100%;
            position: absolute;
            overflow: auto !important;
            padding: 5px;
        }

        .JCLRgrip {
            width: 13px;
            margin-left: -5px !important;
            position: absolute !important;
            height: 18px !important;
            cursor: col-resize !important;
            border-left: 4px solid #a0a0a0;
            background-repeat: no-repeat;
            margin-bottom: 0;
            margin-top: 10px;
        }

        .JCLRgrip:before {
            /*content: "\f7a5";
            font-family: FontAwesome;*/
            left: -5px;
            position: absolute;
            top: 0;
        }

        .JCLRdisabledGrip {
            display: none !important;
        }

        .grip:hover {
            background-position: 20px 0px;
            cursor: e-resize !important;

        }

        .grip:active {
            background-position: 20px 0px;
            cursor: e-resize !important;

        }

        .dragging .grip {
            background-position: 40px 0px;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .itemNumber-column {
            /*display: run-in;*/
            word-wrap: break-word;
            min-width: 200px;
            width: 200px !important;
            white-space: normal !important;
            text-align: left;
        }

        .itemName-column {
            /*display: run-in;*/
            word-wrap: break-word;
            min-width: 200px;
            width: 200px !important;
            white-space: normal !important;
            text-align: left;
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

        #freeTextSearchDirective ::-webkit-input-placeholder {
            /**/
            font-weight: bold;
        }

        #freeTextSearchDirective :-moz-placeholder {
            /**/
            font-weight: bold;
        }

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

        #itemFolder .tree-file + span {
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
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{itemsTitle}}</span>

            <button class="btn btn-sm new-button" ng-click="itemsVm.showNewItem()"
                    title="{{'CLICK_HERE_FOR_NEWITEM' | translate}}"
                    ng-if="createItemPermission">
                <i class="las la-plus" aria-hidden="true"></i>
                <span>{{'NEW' | translate}} {{itemsVm.newButtonText}}</span>
            </button>

            <div class="btn-group">
                <button class="btn btn-sm btn-info" title="{{itemsVm.searchItemType}}"
                        ng-click="itemsVm.itemsSearch()">
                    <i class="fa fa-search" style=""></i>
                </button>

                <button class="btn btn-sm btn-lightblue" title="{{itemsVm.savedSearchItems}}"
                        ng-click="showItemSavedSearches()">
                    <i class="fa fa-save" style=""></i>
                </button>
                <button ng-show="showSearch == true" title="{{itemsVm.saveSearchItems}}" class="btn btn-sm btn-darkblue"
                        ng-click="itemsVm.showSaveSearch()">
                    <i class="fa fa-clipboard" style=""></i>
                </button>

                <%--<div class="btn-group" ng-show="itemsVm.itemView" uib-dropdown auto-close="outsideClick">
                    <button uib-dropdown-toggle class="btn btn-sm btn-warning dropdown-toggle" type="button"
                            title="Show classification">
                        <span><i class="las la-project-diagram" style="position: absolute;top: 9px;"></i></span>
                        <span ng-if="itemsVm.selectedItemType != null" style="color: #1258de;margin-left: 22px;">{{itemsVm.selectedItemType.name}}</span>
                        <span class="caret" ng-if="itemsVm.selectedItemType != null"></span>
                        <span class="caret" style="margin-left: 22px;" ng-if="itemsVm.selectedItemType == null"></span>
                    </button>

                    <div class="dropdown-menu" role="menu"
                         style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important; min-height: 300px !important;">
                        <div>
                            <div class="home-widget-panel1 itemfolders-panel" style="height: 100%">
                                <div style="line-height: 30px;"><a href="" ng-click="itemsVm.clearTypeSelection()">Clear
                                    Selection</a></div>
                                <classification-tree tree-id="classificationTreeDropdown"
                                                     ng-if="itemsVm.itemsMode == null || itemsVm.itemsMode == '' || itemsVm.itemsMode == undefined"
                                                     on-select-type="itemsVm.onSelectType">
                                </classification-tree>

                                <item-class-tree
                                        tree-id="itemClassTreeDropdown"
                                        item-class="{{itemsVm.itemsMode}}"
                                        ng-if="itemsVm.itemsMode != null && itemsVm.itemsMode != '' && itemsVm.itemsMode != undefined"
                                        on-select-type="itemsVm.onSelectType">
                                </item-class-tree>
                            </div>
                        </div>
                    </div>

                </div>--%>

                <div class="btn-group" ng-show="itemsVm.itemView" uib-dropdown auto-close="outsideClick">
                    <button uib-dropdown-toggle class="btn btn-sm btn-warning dropdown-toggle" type="button"
                            title="{{itemsVm.addItemsFolder}}"
                            ng-if="editItemPermission">
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
                                        <li id="addFolderItem"><a tabindex="-1" href=""
                                                                  ng-click="itemsVm.addFolder($event)"
                                                                  translate>ADD_FOLDER</a>
                                        </li>
                                        <li id="deleteFolderItem"><a tabindex="-1" href=""
                                                                     ng-click="itemsVm.deleteFolder($event)" translate>
                                            DELETE_FOLDER</a></li>

                                    </ul>
                                </div>
                                <div class="home-widget-panel-body1">
                                    <div id="classificationContainer" class="tree-pane" data-toggle="context"
                                         data-target="#context-menu">
                                        <ul id="itemFolder" class="easyui-tree">
                                        </ul>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <div class="btn-group" style="margin-left: 10px;"
                 ng-if="editItemPermission">
                <button ng-show="itemsVm.itemView" title="{{itemsVm.showItemsAttributes}}" class="btn btn-sm btn-maroon"
                        ng-click="itemsVm.showTypeAttributes()">
                    <i class="fa fa-newspaper-o" style=""></i>
                </button>
            </div>
            <div class="btn-group" ng-if="editItemPermission">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false" title="{{itemsVm.itemsExport}}">
                    <span class="mr5"><i class="fa fa-upload" style=""></i></span>
                    <%--<span class="mr5" translate>ALL_VIEW_EXPORT</span>--%><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="itemsVm.exportItems('csv')"><a href="">CSV</a></li>
                </ul>
            </div>

            <button class="btn btn-sm btn-default"
                    style="" title="{{itemsVm.itemMainView}}"
                    ng-if="itemsVm.itemMasterView" ng-click="itemsVm.showItemView()">
                <i class="fa fa-table" style="margin-top:-6px;"></i>
            </button>

            <button class="btn btn-sm btn-default"
                    style="" title="{{itemsVm.itemMastersView}}"
                    ng-if="itemsVm.itemView" ng-click="itemsVm.showItemMasterView()">
                <i class="fa fa-th-list" style=""></i>
            </button>

            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>

            <button ng-show="itemsVm.showShareButton == true && editItemPermission"
                    class="btn btn-sm btn-default"
                    style="border-radius:4px;"
                    ng-click="itemsVm.shareSelectedItems()" title="{{itemsVm.shareItem}}">
                <i class="las la-share" style=""></i>
            </button>

            <button class="btn btn-sm btn-default"
                    ng-if="itemsVm.showCopyToClipBoard && itemsVm.clipBoardItems.length == 0"
                    ng-click="itemsVm.copyToClipBoard()" title="{{ 'COPY_ITEMS_CLIPBOARD_TITLE' | translate}}">
                <i class="fa fa-copy" style=""></i>
            </button>
            <div class="btn-group" ng-if="itemsVm.showCopyToClipBoard && itemsVm.clipBoardItems.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy"></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li ng-click="itemsVm.clearAndCopyToClipBoard()"><a href="" translate>CLEAR_AND_ADD_ITEMS</a></li>
                    <li ng-click="itemsVm.copyToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_ITEMS</span>
                        ({{itemsVm.clipBoardItems.length}})</a></li>
                </ul>
            </div>
            <button class="btn btn-default"
                    ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                    ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                <i class="fa fa-copy" style=""></i>
            </button>
            <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-copy" style=""></span><span class="caret"></span>
                </button>
                <ul class="dropdown-menu" style="left: auto;right: -100px;">
                    <li ng-click="clearAndCopyObjectFilesToClipBoard()">
                        <a href="" translate="">CLEAR_AND_ADD_FILES</a></li>
                    <li ng-click="copyObjectFilesToClipBoard()"><a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                        ({{clipBoardObjectFiles.length}})</a></li>
                </ul>
            </div>

            <!-- Custom actions and action groups -->
            <div class="btn-group" ng-if="itemsVm.itemMasterView">
                <div class="btn-group" ng-repeat="group in itemsVm.customActionGroups">
                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false" title="{{group.tooltip}}">
                        <span class="fa {{group.icon}}" style=""></span>
                        <span class="caret" style="margin-left: 3px;margin-top: -3px;"></span>
                    </button>
                    <ul class="dropdown-menu" style="left: auto;right: 0;">
                        <li ng-repeat="action in group.actions"
                            ng-click="itemsVm.performCustomAction(action)"
                            title="{{action.tooltip}}">
                            <a href="">{{action.label}}</a>
                        </li>
                    </ul>
                </div>

                <button class="btn btn-sm btn-default"
                        title="{{action.tooltip}}"
                        ng-click="itemsVm.performCustomAction(action)"
                        ng-repeat="action in itemsVm.customActions">
                    <i class="fa {{action.icon}}" style=""></i>
                </button>
            </div>

            <free-text-search on-clear="itemsVm.resetPage" search-term="itemsVm.searchText"
                              on-search="itemsVm.freeTextSearchItems"
                              filter-search="itemsVm.filterSearch"></free-text-search>
        </div>
        <div class="view-content no-padding" style="padding: 10px;" ng-show="itemsVm.itemView">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row" style="width: 100% !important;margin-bottom: 15px">
                    <thead>
                    <tr>
                        <th style="width: 30px">
                            <div ng-if="itemsVm.items.content.length > 1" class="ckbox ckbox-default"
                                 style="display: inline-block;">
                                <input id="items{{$index}}" name="itemSelected" type="checkbox"
                                       ng-model="itemsVm.flag" ng-click="itemsVm.selectAll()">
                                <label for="items{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </th>
                        <th style="width:1px;white-space:nowrap;"></th>
                        <th style="width: 150px;z-index: auto !important;">
                        <span ng-click="itemsVm.changeSorting('itemNumber')" style="cursor: pointer"
                              translate="ITEM_ALL_ITEM_NUMBER"></span>

                            <div class="btn-group" style="height:100%; width: auto;margin-bottom: 0px;"
                                 ng-if="itemsVm.pageable.sort.field == 'itemNumber'">
                                <i class="fa fa-sort-amount-asc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'ASC'"
                                   ng-click="itemsVm.changeSorting('itemNumber')"></i>

                                <i class="fa fa-sort-amount-desc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'DESC'"
                                   ng-click="itemsVm.changeSorting('itemNumber')"></i>
                            </div>
                        </th>
                        <th style="width: 150px;z-index: auto !important;"
                            ng-if="itemsVm.itemsMode == '' || itemsVm.itemsMode == null">
                        <span ng-click="itemsVm.changeSorting('itemNumber')" style="cursor: pointer"
                              translate="ITEM_CLASS"></span>
                        </th>
                        <th style="width: 150px;z-index: auto !important;">
                            <span ng-if="itemsVm.selectedItemType != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                    ({{itemsVm.selectedItemType.name}})
                                    <i class="fa fa-times-circle" ng-click="itemsVm.clearTypeSelection()"
                                       title="{{removeTitle}}"></i>
                            </span>
                            <br>
                            <span ng-click="itemsVm.changeSorting('itemType.name')" style="cursor: pointer"
                                  ng-if="itemsVm.search.searchType == 'advanced'"
                                  translate="ITEM_ALL_ITEM_TYPE">
                            </span>

                            <div class="dropdown" uib-dropdown style="display: inline-block"
                                 ng-if="itemsVm.search.searchType != 'advanced'">
                                <span uib-dropdown-toggle><span translate>ITEM_ALL_ITEM_TYPE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>

                                <div class="dropdown-menu" role="menu"
                                     style="height: auto !important;max-height: 500px !important;overflow-x: hidden !important;min-width: 250px !important; min-height: 300px !important;margin-top:5px;font-weight: normal;">
                                    <div>
                                        <div class="home-widget-panel1 itemfolders-panel" style="height: 100%">
                                            <classification-tree tree-id="classificationTreeDropdown"
                                                                 ng-if="itemsVm.itemsMode == null || itemsVm.itemsMode == '' || itemsVm.itemsMode == undefined"
                                                                 on-select-type="itemsVm.onSelectType">
                                            </classification-tree>

                                            <item-class-tree
                                                    tree-id="itemClassTreeDropdown"
                                                    item-class="{{itemsVm.itemsMode}}"
                                                    ng-if="itemsVm.itemsMode != null && itemsVm.itemsMode != '' && itemsVm.itemsMode != undefined"
                                                    on-select-type="itemsVm.onSelectType">
                                            </item-class-tree>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="btn-group" style="height:100%; width: auto;margin-bottom: 0px;"
                                 ng-if="itemsVm.pageable.sort.field == 'itemType.name'">
                                <i class="fa fa-sort-amount-asc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'ASC'"
                                   ng-click="itemsVm.changeSorting('itemType.name')"></i>

                                <i class="fa fa-sort-amount-desc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'DESC'"
                                   ng-click="itemsVm.changeSorting('itemType.name')"></i>
                            </div>
                        </th>
                        <th class="col-width-200" style="z-index: auto !important;">
                        <span ng-click="itemsVm.changeSorting('itemName')" style="cursor: pointer"
                              translate="ITEM_ALL_ITEM_NAME"></span>

                            <div class="btn-group" style="height:100%; width: auto;margin-bottom: 0px;"
                                 ng-if="itemsVm.pageable.sort.field == 'itemName'">
                                <i class="fa fa-sort-amount-asc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'ASC'"
                                   ng-click="itemsVm.changeSorting('itemName')"></i>

                                <i class="fa fa-sort-amount-desc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'DESC'"
                                   ng-click="itemsVm.changeSorting('itemName')"></i>
                            </div>
                        </th>
                        <th class="col-width-250" style="z-index: auto !important;">
                        <span ng-click="itemsVm.changeSorting('description')" style="cursor: pointer"
                              translate="DESCRIPTION"></span>

                            <div class="btn-group" style="height:100%; width: auto;margin-bottom: 0px;"
                                 ng-if="itemsVm.pageable.sort.field == 'description'">
                                <i class="fa fa-sort-amount-asc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'ASC'"
                                   ng-click="itemsVm.changeSorting('description')"></i>

                                <i class="fa fa-sort-amount-desc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'DESC'"
                                   ng-click="itemsVm.changeSorting('description')"></i>
                            </div>
                        </th>
                        <th style="width: 150px; text-align: center;z-index: auto !important;">
                            <span ng-if="itemsVm.search.searchType == 'advanced'" translate>ITEM_ALL_REVISION</span>
                            <span ng-if="itemsVm.selectedRevision != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;text-transform: uppercase;">
                                    ({{itemsVm.selectedRevision}})
                                    <i class="fa fa-times-circle" ng-click="itemsVm.clearRevision()"
                                       title="{{removeTitle}}"></i>
                            </span>
                            <br>

                            <div class="dropdown" uib-dropdown style="display: inline-block"
                                 ng-if="itemsVm.search.searchType != 'advanced'">
                                <span uib-dropdown-toggle><span translate>ITEM_ALL_REVISION</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                                <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                    style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                    <li ng-repeat="revision in itemsVm.revisions"
                                        ng-click="itemsVm.onSelectRevision(revision)"><a
                                            href="">{{revision}}</a>
                                    </li>
                                </ul>
                            </div>
                        </th>
                        <th style="width: 150px;z-index: auto !important;">
                            <span ng-if="itemsVm.search.searchType == 'advanced'" translate>ITEM_ALL_LIFECYCLE</span>
                            <span ng-if="itemsVm.selectedPhase != null"
                                  style="font-weight:normal;font-size: 13px;cursor: pointer !important;text-transform: uppercase;">
                                    ({{itemsVm.selectedPhase}})
                                    <i class="fa fa-times-circle" ng-click="itemsVm.clearPhase()"
                                       title="{{removeTitle}}"></i>
                            </span>
                            <br>

                            <div class="dropdown" uib-dropdown style="display: inline-block"
                                 ng-if="itemsVm.search.searchType != 'advanced'">
                                <span uib-dropdown-toggle><span translate>ITEM_ALL_LIFECYCLE</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                                <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                    style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                    <li ng-repeat="phase in itemsVm.lifecyclePhases"
                                        ng-click="itemsVm.onSelectPhase(phase)" style="text-transform: uppercase;"><a
                                            href="">{{phase}}</a>
                                    </li>
                                </ul>
                            </div>
                        </th>
                        <th style="width: 150px;z-index: auto !important;" translate="MAKE_BUY"></th>
                        <th style="width: 100px;z-index: auto !important;">
                        <span ng-click="itemsVm.changeSorting('units')" style="cursor: pointer"
                              translate="ITEM_ALL_UNITS"></span>

                            <div class="btn-group" style="height:100%; width: auto;margin-bottom: 0px;"
                                 ng-if="itemsVm.pageable.sort.field == 'units'">
                                <i class="fa fa-sort-amount-asc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'ASC'"
                                   ng-click="itemsVm.changeSorting('units')"></i>

                                <i class="fa fa-sort-amount-desc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'DESC'"
                                   ng-click="itemsVm.changeSorting('units')"></i>
                            </div>
                        </th>
                        <th style="width: 150px;z-index: auto !important;" translate="RELEASED_REJECTED_DATE"></th>
                        <th style="width: 155px;z-index: auto !important;">
                        <span ng-click="itemsVm.changeSorting('modifiedDate')" style="cursor: pointer"
                              translate="MODIFIED_DATE"></span>

                            <div class="btn-group" style="height:100%; width: auto;margin-bottom: 0px;"
                                 ng-if="itemsVm.pageable.sort.field == 'modifiedDate'">
                                <i class="fa fa-sort-amount-asc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'ASC'"
                                   ng-click="itemsVm.changeSorting('modifiedDate')"></i>

                                <i class="fa fa-sort-amount-desc" style="font-size:15px;cursor: pointer !important;"
                                   ng-if="itemsVm.pageable.sort.order == 'DESC'"
                                   ng-click="itemsVm.changeSorting('modifiedDate')"></i>
                            </div>
                        </th>
                        <%--<th style="width: auto" translate>EFFECTIVE_FROM</th>
                        <th style="width: auto" translate>EFFECTIVE_TO</th>--%>
                        <th style="width: 100px;text-align: center;" translate="THUMBNAIL"></th>
                        <th class='added-column'
                            style="width: 150px;z-index: auto !important;"
                            ng-repeat="selectedAttribute in itemsVm.selectedAttributes">
                            {{selectedAttribute.name}}
                            <i class="fa fa-times-circle"
                               ng-click="itemsVm.removeAttribute(selectedAttribute)"
                               title={{itemsVm.RemoveColumnTitle}}></i>
                        </th>
                        <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                            translate="ITEM_ALL_ACTIONS"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="itemsVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="itemsVm.loading == false && itemsVm.items.content.length == 0">
                        <%-- <td colspan="25">{{ 'NO' | translate}} {{itemsTitle}}</td>--%>
                        <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                            <div class="no-data">
                                <img ng-src="{{itemsVm.image}}" alt="" class="image">

                                <div class="message">{{ 'NO' | translate}} {{itemsTitle}}</div>
                                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                    NO_PERMISSION_MESSAGE
                                </div>
                            </div>
                        </td>

                    </tr>

                    <tr ng-repeat="item in itemsVm.items.content">
                        <td style="width: 30px;" ng-if="itemsVm.items.content.length > 0">
                            <div class="ckbox ckbox-default" style="display: inline-block;">
                                <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-model="item.selected" ng-click="itemsVm.toggleSelection(item)">
                                <label for="item{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </td>
                        <td style="width:1px !important;white-space: nowrap;text-align: left;">
                            <all-view-icons object="item"></all-view-icons>
                        </td>
                        <td style="width: 150px">
                            <a href="" ng-click="itemsVm.showItem(item)"
                               title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                            </a>
                            <%--<span ng-if="!itemsVm.viewItemPermission">
                                <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                            </span>--%>
                        </td>
                        <td style="width:150px;" ng-if="itemsVm.itemsMode == '' || itemsVm.itemsMode == null">
                            <item-class item="item.itemClass"></item-class>
                        </td>
                        <td class="column-width-150" title="{{item.itemType.name}}"><span
                                ng-bind-html="item.itemTypeName  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-200" title="{{item.itemName}}"><span
                                ng-bind-html="item.itemName  | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="col-width-250" title="{{item.description}}"><span
                                ng-bind-html="item.description | highlightText: freeTextQuery"></span>
                        </td>
                        <td style="width: 150px;text-align: center;">
                            <a href="" ng-click="itemsVm.showItemRevisionHistory(item)"
                               title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                                {{item.revision}}
                            </a>
                        </td>
                        <td style="width: 150px">
                            <item-status item="item"></item-status>
                        </td>
                        <td style="width: 150px;text-align: center;">
                            <span class="label label-outline bg-light-primary"
                                  ng-if="item.makeOrBuy == 'MAKE'">{{item.makeOrBuy}}</span>
                            <span class="label label-outline bg-light-danger"
                                  ng-if="item.makeOrBuy == 'BUY'">{{item.makeOrBuy}}</span>
                        </td>
                        <td style="width: 100px">{{item.units}}</td>
                        <td style="width: 150px;text-align: center">

                            <span ng-if="item.releasedDate == null">{{'-'}}</span>
                            <span title="{{item.released ? releasedDate : rejectedDate}}">{{item.releasedDate}}</span>
                        </td>
                        <td style="width: 155px">
                            {{item.modifiedDate}}
                        </td>
                        <%--<td style="width: auto">{{item.latestRevisionObject.effectiveFrom}}</td>
                        <td style="width: auto">{{item.latestRevisionObject.effectiveTo}}</td>--%>
                        <td style="width: 100px;text-align: center">
                            <div>
                                <a ng-if="item.hasThumbnail"
                                   href="" ng-click="itemsVm.showThumbnailImage(item)"
                                   title="{{clickToShowLargeImage}}">
                                    <img ng-src="{{item.thumbnailImage}}"
                                         style="height: 20px;width: 30px;">
                                </a>
                                <%--<a ng-if="!item.hasThumbnail && item.itemImageObj != null && item.itemImageObj.thumbnail != null"
                                   href="">
                                    <img src="data:image/png;base64,{{item.itemImageObj.thumbnail}}"
                                         style="height: 20px;width: 30px;"
                                         ng-click="showAutoDeskFile(item.itemImageObj)"/>
                                </a>--%>
                                <a ng-if="!item.hasThumbnail && item.itemImageObj == null" href="">
                                    <img src="app/assets/images/cassini-logo-greyscale.png" title="No thumbnail" alt=""
                                         class="no-thumbnail-preview">
                                </a>

                                <div id="item-thumbnail{{item.id}}" class="item-thumbnail modal">
                                    <div class="item-thumbnail-content">
                                        <div class="thumbnail-content" style="display: flex;width: 100%;">
                                            <div class="thumbnail-view" id="thumbnail-view{{item.id}}">
                                                <div id="thumbnail-image{{item.id}}"
                                                     style="display: table-cell;vertical-align: middle;text-align: center;">
                                                    <img ng-src="{{item.thumbnailImage}}"
                                                         style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{item.id}}"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td class="added-column"
                            ng-repeat="objectAttribute in itemsVm.selectedAttributes">
                            <all-view-attributes object="item" object-attribute="objectAttribute"></all-view-attributes>
                        </td>
                        <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li title="{{!lockItemPermission ? noPermission : ''}}"
                                    ng-click="itemsVm.lockItem(item)">
                                    <a ng-class="{'disabled':(item.lockObject  && loginPersonDetails.person.id != item.lockedBy && !adminPermission) || !lockItemPermission}"
                                       ng-if="!item.lockObject" translate>ITEM_LOCK</a>
                                    <a ng-class="{'disabled':(item.lockObject  && loginPersonDetails.person.id != item.lockedBy && !adminPermission) || !lockItemPermission}"
                                       ng-if="item.lockObject" translate>ITEM_UNLOCK</a>
                                </li>
                                <li ng-click="itemsVm.saveAs(item)"
                                    title="{{!copyItemPermission ? noPermission : ''}}"
                                    ng-hide="item.configured">
                                    <a ng-class="{'disabled':(item.lockObject  && loginPersonDetails.person.id != item.lockedBy && !adminPermission) || !copyItemPermission}"
                                       href="" translate>COPY_ITEM</a>
                                </li>

                                <li title="{{!printItemPermission ? noPermission : ''}}">
                                    <a href="" ng-class="{'disabled':!printItemPermission}"
                                       ng-click="itemsVm.getPrintOptions(item)" translate>PREVIEW_AND_PRINT</a>
                                </li>

                                <tags-button object-type="'ITEM'" object="item.id"
                                             tags-count="item.tagsCount"></tags-button>

                                <li title="{{item.lifeCyclePhase.phaseType == 'RELEASED' || item.lifeCyclePhase.phaseType == 'OBSOLETE' || item.released == true || (item.lockObject  && loginPersonDetails.person.id != item.lockedBy && !adminPermission) ? cannotDeleteApprovedItem:''}}">
                                    <a href=""
                                       ng-class="{'disabled':item.lifeCyclePhase.phaseType == 'RELEASED' || item.lifeCyclePhase.phaseType == 'OBSOLETE' || item.released == true || !deleteItemPermission || (item.lockObject  && loginPersonDetails.person.id != item.lockedBy && !adminPermission)}"
                                       ng-click="itemsVm.deleteItem(item)" translate>DELETE_ITEM</a>
                                </li>
                                <plugin-table-actions object-value="item" context="item.all"></plugin-table-actions>
                            </ul>
                     </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                <table-footer objects="itemsVm.items" pageable="itemsVm.pageable"
                              previous-page="itemsVm.previousPage" next-page="itemsVm.nextPage"
                              page-size="itemsVm.pageSize"></table-footer>
            </div>
        </div>

        <div class="view-content no-padding" ng-show="itemsVm.itemMasterView">
            <div class="split-pane fixed-left">
                <div class="split-pane-component split-left-pane"
                     style="padding: 0;width: 500px;max-width: 500px;overflow: hidden">
                    <div class="responsive-table" style="padding-top: 5px;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th style="width: 30px">
                                    <div ng-if="itemsVm.items.content.length > 1" class="ckbox ckbox-default"
                                         style="display: inline-block;">
                                        <input id="item{{$index}}" name="itemSelected" type="checkbox"
                                               ng-model="itemsVm.flag" ng-click="itemsVm.selectAll()">
                                        <label for="item{{$index}}" class="item-selection-checkbox"></label>
                                    </div>
                                </th>
                                <th style="width: 60px;"></th>
                                <th style="width: 200px;" translate="ITEM_ALL_ITEM_NUMBER"></th>
                                <th style="width: 200px;" translate="ITEM_ALL_ITEM_NAME"></th>
                                <th style="width: 70px;text-align: center" translate="ITEM_ALL_REVISION"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in itemsVm.items.content"
                                ng-click="itemsVm.selectItem(item)"
                                ng-class="{'select-item':itemsVm.selectedItem.id == item.id}">
                                <td style="width: 30px;" ng-if="itemsVm.items.content.length > 0">
                                    <div class="ckbox ckbox-default" style="display: inline-block;">
                                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                               ng-model="item.selected" ng-click="itemsVm.toggleSelection(item)">
                                        <label for="item{{$index}}" class="item-selection-checkbox"></label>
                                    </div>
                                </td>

                                <td style="width:1px;white-space: nowrap;text-align: left;">
                                    <all-view-icons object="item"></all-view-icons>
                                </td>
                                <td class="itemNumber-column">
                                    <a href="" ng-if="viewItemPermission" ng-click="itemsVm.showItem(item, $event);"
                                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                        <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>
                                    </a>
                                </td>
                                <td class="itemName-column" ng-click="itemsVm.selectItem(item)">
                                    <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span></td>
                                <%--<td>{{item.itemType.name}}</td>--%>
                                <td ng-click="itemsVm.selectItem(item)" style="width: 70px;text-align: center">
                                    {{item.revision}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="table-footer">
                        <div>
                            <div>
                                <h5><span style="padding-right: 5px" translate>DISPLAYING</span>
                        <span ng-if="itemsVm.items.totalElements ==0">
                            {{(itemsVm.pageable.page*itemsVm.pageable.size)}}
                        </span>
                        <span ng-if="itemsVm.items.totalElements > 0">
                            {{(itemsVm.pageable.page*itemsVm.pageable.size)+1}}
                        </span>
                                    -
                                    <span ng-if="itemsVm.items.last ==false">{{((itemsVm.pageable.page+1)*itemsVm.pageable.size)}}</span>
                                    <span ng-if="itemsVm.items.last == true">{{itemsVm.items.totalElements}}</span>

                                    <span translate>OF</span>
                                    {{itemsVm.items.totalElements}} <span translate>AN</span></h5>
                            </div>

                            <div class="text-right">
                                <span class="mr10" translate>PAGE</span>{{itemsVm.items.totalElements != 0 ?
                                itemsVm.items.number+1:0}}
                                <span translate>OF</span>
                                {{itemsVm.items.totalPages}}
                                <a href="" ng-click="itemsVm.previousPage()"
                                   ng-class="{'disabled': itemsVm.items.first}"><i
                                        class="fa fa-arrow-circle-left mr10"></i></a>
                                <a href="" ng-click="itemsVm.nextPage()"
                                   ng-class="{'disabled': itemsVm.items.last}"><i
                                        class="fa fa-arrow-circle-right"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="split-pane-divider" style="left: 500px;">
                </div>
                <div class="split-pane-component split-right-pane noselect"
                     style="left:500px;padding: 0px;overflow-y: hidden;">
                    <div ng-show="itemsVm.selectedItem == null && itemsVm.items.content.length > 0"
                         style="padding: 15px;"
                         translate>SELECT_ITEM_DETAILS
                    </div>
                    <div class="row row-eq-height" style="margin: 0;" ng-show="itemsVm.selectedItem != null">
                        <div class="col-sm-12" style="padding: 10px;">
                            <div class="item-details-tabs">
                                <scrollable-tabset tooltip-left-placement="top" show-drop-down="false">
                                    <uib-tabset active="itemsVm.active">
                                        <uib-tab heading="{{itemsVm.tabs.basic.heading}}"
                                                 active="itemsVm.tabs.basic.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.basic.id)">
                                            <div ng-include="itemsVm.tabs.basic.template"
                                                 ng-controller="ItemBasicInfoController as itemBasicVm"></div>
                                        </uib-tab>

                                        <%--<uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                                 active="itemsVm.tabs.attributes.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.attributes.id)">
                                            <div ng-include="itemsVm.tabs.attributes.template"
                                                 ng-controller="ItemAttributesController as itemAttributesVm"></div>
                                        </uib-tab>--%>
                                        <uib-tab id="bom" ng-show="itemsVm.hasDisplayTab('bom')"
                                                 heading="{{itemsVm.tabs.bom.heading}}"
                                                 active="itemsVm.tabs.bom.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.bom.id)">
                                            <div ng-include="itemsVm.tabs.bom.template"
                                                 ng-controller="ItemBomController as itemBomVm"></div>
                                        </uib-tab>
                                        <uib-tab id="configured" ng-show="item.configurable"
                                                 heading="{{itemsVm.tabs.configured.heading}}"
                                                 active="itemsVm.tabs.configured.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.configured.id)">
                                            <div ng-include="itemsVm.tabs.configured.template"
                                                 ng-controller="ItemConfiguredController as itemConfiguredVm"></div>
                                        </uib-tab>
                                        <uib-tab id="whereUser" ng-show="itemsVm.hasDisplayTab('whereUsed')"
                                                 heading="{{itemsVm.tabs.whereUsed.heading}}"
                                                 active="itemsVm.tabs.whereUsed.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.whereUsed.id)">
                                            <div ng-include="itemsVm.tabs.whereUsed.template"
                                                 ng-controller="ItemWhereUsedController as itemWhereUsedVm"></div>
                                        </uib-tab>
                                        <uib-tab id="changesTab" ng-show="itemsVm.hasDisplayTab('changes')"
                                                 heading="{{itemsVm.tabs.changes.heading}}"
                                                 active="itemsVm.tabs.changes.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.changes.id)">
                                            <div ng-include="itemsVm.tabs.changes.template"
                                                 ng-controller="ItemChangesController as itemChangesVm"></div>
                                        </uib-tab>
                                        <uib-tab id="variance" ng-show="itemsVm.hasDisplayTab('variance')"
                                                 heading="{{itemsVm.tabs.variance.heading}}"
                                                 active="itemsVm.tabs.variance.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.variance.id)">
                                            <div ng-include="itemsVm.tabs.variance.template"
                                                 ng-controller="ItemVarianceController as itemVarianceVm"></div>
                                        </uib-tab>
                                        <uib-tab id="qualityTab" ng-show="itemsVm.hasDisplayTab('quality')"
                                                 heading="{{itemsVm.tabs.quality.heading}}"
                                                 active="itemsVm.tabs.quality.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.quality.id)">
                                            <div ng-include="itemsVm.tabs.quality.template"
                                                 ng-controller="ItemQualityController as itemQualityVm"></div>
                                        </uib-tab>
                                        <uib-tab id="files" ng-show="itemsVm.hasDisplayTab('files')"
                                                 heading="{{'DETAILS_TAB_FILES' | translate}}"
                                                 active="itemsVm.tabs.files.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.files.id)">
                                            <div ng-include="itemsVm.tabs.files.template"
                                                 ng-controller="ItemFilesController as itemFilesVm"></div>
                                        </uib-tab>
                                        <uib-tab id="mfr" ng-show="itemsVm.hasDisplayTab('mfrParts')"
                                                 heading="{{'ITEM_DETAILS_TAB_MANUFACTURER_PARTS' | translate}}"
                                                 active="itemsVm.tabs.mfr.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.mfr.id)">
                                            <div ng-include="itemsVm.tabs.mfr.template"
                                                 ng-controller="ItemMfrController as itemMfrVm"></div>
                                        </uib-tab>

                                        <uib-tab id="relatedItems" ng-show="itemsVm.hasDisplayTab('relatedItems')"
                                                 heading="{{itemsVm.tabs.relatedItems.heading}}"
                                                 active="itemsVm.tabs.relatedItems.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.relatedItems.id)">
                                            <div ng-include="itemsVm.tabs.relatedItems.template"
                                                 ng-controller="RelatedItemsController as relatedItemsVm"></div>
                                        </uib-tab>

                                        <uib-tab id="projectItem" ng-show="itemsVm.hasDisplayTab('projects')"
                                                 heading="{{itemsVm.tabs.projectItem.heading}}"
                                                 active="itemsVm.tabs.projectItem.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.projectItem.id)">
                                            <div ng-include="itemsVm.tabs.projectItem.template"
                                                 ng-controller="ProjectItemController as projectItemsVm"></div>
                                        </uib-tab>

                                        <uib-tab id="requirements-tab" ng-show="itemsVm.hasDisplayTab('requirements')"
                                                 heading="{{itemsVm.tabs.requirements.heading}}"
                                                 active="itemsVm.tabs.requirements.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.requirements.id)">
                                            <div ng-include="itemsVm.tabs.requirements.template"
                                                 ng-controller="ItemRequirementsController as itemRequirementsVm"></div>
                                        </uib-tab>
                                        <uib-tab id="itemHistory"
                                                 heading="{{itemsVm.tabs.itemTimelineHistory.heading}}"
                                                 active="itemsVm.tabs.itemTimelineHistory.active"
                                                 select="itemsVm.itemDetailsTabActivated(itemsVm.tabs.itemTimelineHistory.id)">
                                            <div ng-include="itemsVm.tabs.itemTimelineHistory.template"
                                                 ng-controller="ItemTimelineHistoryController as itemTimelineHistoryVm"></div>
                                        </uib-tab>
                                        <!-- Custom tabs -->
                                        <uib-tab ng-repeat="customTab in itemsVm.customTabs"
                                                 id="{{customTab.id}}"
                                                 class="custom-tab"
                                                 heading="{{customTab.heading}}"
                                                 active="customTab.active"
                                                 select="itemsVm.itemDetailsTabActivated(customTab.id)">
                                            <div ng-include="customTab.template"
                                                 dynamic-ctrl="customTab.controller"></div>
                                        </uib-tab>
                                    </uib-tabset>
                                </scrollable-tabset>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>