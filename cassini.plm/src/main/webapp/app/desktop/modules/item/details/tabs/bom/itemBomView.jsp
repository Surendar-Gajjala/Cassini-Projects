<div class="bom-class">
    <style scoped>
        table.highlight-row tbody tr:hover td .label-count {
            background-color: #859cad !important;
            color: #fff;
        }
    </style>
    <div style="padding-top: 20px;position: fixed !important;left: 0;width: 100%;text-align: center;z-index: 15">
        <div class="text-center" ng-class="{'master-class': masterDetailsEnable == true}">
            <div class="btn-group bom-configs-ui-select" style="width: 300px !important;margin-bottom: 3px;"
                 ng-class="{ 'bom-configs-selected': itemVm.selectedBomConfig != null }"
                 ng-if="itemVm.tabs.bom.active && checkBomAvailOrNot == true && itemVm.item.configurable && itemVm.bomConfigs.length > 0">
                <ui-select ng-model="itemVm.selectedBomConfig" theme="bootstrap"
                           on-select="openBomConfiguration($item)"
                           style="width:100%">
                    <ui-select-match allow-clear="true" placeholder="Select Bom Configuration" style="">
                        <span style="width: 94%;text-overflow: ellipsis;white-space: nowrap;font-weight: bold;padding: 0 2px;">{{$select.selected.name}}</span>
                    </ui-select-match>
                    <ui-select-choices
                            repeat="bomConfig in itemVm.bomConfigs | filter: $select.search">
                        <div ng-bind-html="bomConfig.name"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="revision-configs-panel">
                    <span class="mr10 text-primary" style="font-weight: bold"
                          translate>ITEM_TAB_BOM_CONFIGURATION</span>:&nbsp;

                <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
                    <input name="bomRule" value="bom.latest" id="latestRevision" type="radio" checked
                           ng-model="itemBomVm.selectedBomRule"
                           ng-change="itemBomVm.loadBom();itemBomVm.expandedAll = false" style="margin-top: -10px;">
                    <label for="latestRevision" translate>ITEM_TAB_BOM_LATEST_REVISION</label>
                </div>

                <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
                    <input name="bomRule" value="bom.latest.released" id="latestReleasedRevision" type="radio"
                           ng-model="itemBomVm.selectedBomRule"
                           ng-change="itemBomVm.loadBom();itemBomVm.expandedAll = false" style="margin-top: -10px;">
                    <label for="latestReleasedRevision" translate>ITEM_TAB_BOM_LATEST_RELEASED</label>
                </div>

                <div class="rdio rdio-primary" style="display: inline-block;">
                    <input name="bomRule" value="bom.as.released" id="asReleasedRevision" type="radio"
                           ng-model="itemBomVm.selectedBomRule"
                           ng-change="itemBomVm.loadBom();itemBomVm.expandedAll = false" style="margin-top: -10px;">
                    <label for="asReleasedRevision" translate>ITEM_TAB_BOM_AS_RELEASED</label>
                </div>
            </div>
        </div>

    </div>
    <style scoped>
        .master-class {
            margin: 0 -300px 0 185px !important;
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

        .bom-table > table > tbody > tr > td {
            padding: 5px !important;
        }

        .popover {
            min-width: 420px !important;
        }

        .responsive-table .dropdown-content {
            margin-left: -55px !important;
        }

        table {
            table-layout: auto !important;
        }

        .bom-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .bom-model .bomModel-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bom-model .bomRollup-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bomWhereUsed-content .ui-select-container .ui-select-match .btn {
            border: 0 !important;
            background-color: #d6d6d6;
            color: var(--cassini-font-color);
        }

        .ui-select-multiple.ui-select-bootstrap .ui-select-match .close {
            line-height: 21px !important;
        }

        .bom-model .bomWhereUsed-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bom-model .bomCompliance-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bom-model .configurationEditor-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .config-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .config-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close:before, .config-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .config-close:before {
            transform: rotate(45deg) !important;
        }

        .config-close:after {
            transform: rotate(-45deg) !important;
        }

        .itemType-column {
            word-wrap: break-word;
            min-width: 120px;
            width: 120px !important;
            white-space: normal !important;
            text-align: left;
        }

        .popover {
            min-width: 500px !important;
        }

        .popover-title {
            display: none;
        }

        .popover {
            max-width: 500px;
            width: 500px;
        }

        .popover-content {
            max-height: 220px;
            overflow-y: auto;
            padding: 10px 0;
        }

        .scrollable-table {
            overflow-y: auto !important;
            max-height: 500px !important;
        }

        .add-column {
            max-width: 20px !important;
            min-width: 20px !important;
            /*width: 20px !important;*/
        }

        .toggle-column {
            /*width: 40px !important;*/
            max-width: 40px !important;
        }

        .number-column {

        }

        .item-name-column {

        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -5px;
        }

        .whereUsed-height {
            width: 100%;
            position: relative;
            padding: 0 10px;
            overflow: auto;
        }

        .whereUsed-height table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
        }

        .compliance-height {
            width: 100%;
            position: relative;
            padding: 0 10px;
            overflow: auto;
        }

        .compliance-height table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
        }

        /*
        .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
            background-color: #fff;
        }

        .table-striped > tbody > tr:nth-child(2n) > td.add-column,
        .table-striped > tbody > tr:nth-child(2n) > td.toggle-column,
        .table-striped > tbody > tr:nth-child(2n) > td.col-width-200,
        .table-striped > tbody > tr:nth-child(2n) > td.number-column {
            background-color: #fff;
        }

        .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
            background-color: #d6e1e0;
        }
        */

        .bom-item-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .bom-item-model .bom-item-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bom-item-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .procedure-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .procedure-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
            height: 100%;
        }

        .bom-item-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .bom-item-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .bom-item-close:before, .bom-item-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .bom-item-close:before {
            transform: rotate(45deg) !important;
        }

        .bom-item-close:after {
            transform: rotate(-45deg) !important;
        }

        td div div .ui-select-bootstrap .ui-select-choices-row > span {
            white-space: normal !important;
        }

        .bom-class .responsive-table {
            height: auto !important;
            position: absolute !important;
            overflow: auto !important;
            padding: 5px !important;
            bottom: 0px !important;
            top: 0 !important;
            margin: 34px 0 0 0 !important;
        }
    </style>

    <div class='responsive-table' id="scrollcontent"
         infinite-scroll infinite-scroll-callback-fn="increaseLimit()" infinite-scroll-percentage="80">
        <style scoped>
            .pad-cell {
                padding-left: 30px;
            }

            tr.unresolved-bom-item {
                background: repeating-linear-gradient(135deg, transparent, transparent 10px, #e6e6e6 10px, #ececec 20px),
                linear-gradient(to bottom, #eee, #fff) !important;
            }

            tr.unresolved-bom-item td {
                background: transparent;
            }

            .actions-col {
                width: 100px;
                text-align: center;
            }

            .disabled1 {
                cursor: not-allowed !important;
            }

            .view-content .responsive-table {
                /*top: 60px !important;*/
            }

            table.highlight-row tbody tr:hover td span i {
                /*color: white !important;*/
            }

            .bold-item {
                font-weight: bold;
            }

            .bom-header {
                padding: 5px;
                text-align: center;
                border-bottom: 1px solid lightgrey;
                height: 50px;
            }

            .configuration-header {
                font-weight: bold;
                font-size: 22px;
                /*position: absolute;*/
                display: inline-block;
                /*left: 44%;*/
                margin-top: 7px;
            }

            #configuration-error {
                display: none;
                padding: 11px !important;
                margin-bottom: 0 !important;
                width: 100%;
                height: 50px;
                margin-top: -50px;
                float: left;
                position: relative;
            }

            .bom-content {
                padding: 10px;
                overflow: auto;
                min-width: 100%;
                width: auto;
            }

            .editor-configuration-content {
                padding: 0 10px;
                overflow: auto;
                min-width: 100%;
                width: auto;
            }

            .config-row {
                display: inline-flex;
                min-width: 100%;
                width: auto;
                padding: 5px;
            }

            .bom-headers {
                display: inline-flex;
                padding: 5px;
                min-width: 100%;
                width: auto
            }

            .bom-header-column {
                width: 200px;
                font-weight: bold;
            }

            .bom-value-column {
                width: 200px;
            }

            .bom-footer {
                border-bottom: 1px solid lightgrey;
                padding: 5px;
                text-align: center;
                height: 50px;
                width: 100%;
                display: flex;
            }

            .config-close-btn {
                position: absolute;
                right: 40px;
                top: 7px;
                width: 32px;
                height: 32px;
                opacity: 0.3;
            }

            .config-close-btn:hover {
                opacity: 0.6;
                border-radius: 50%;
                background-color: #ddd;
            }

            .config-close-btn:before, .config-close-btn:after {
                position: absolute;
                top: 9px;
                left: 15px;
                content: ' ';
                height: 15px;
                width: 2px;
                background-color: #333;
            }

            .config-close-btn:before {
                transform: rotate(45deg) !important;
            }

            .config-close-btn:after {
                transform: rotate(-45deg) !important;
            }

            .ckbox, .rdio {
                line-height: 17px;
            }

            #bom-footer .ui-select-choices {
                top: auto !important;
                bottom: 100% !important;
            }

            .responsive-table .fa-plus {
                opacity: 0.7;
            }

            .responsive-table .fa-plus:hover {
                opacity: 1.0;
            }

            .dateContainer {
                margin-bottom: 0 !important;
            }

            .dateContainer [type=text] {
                cursor: inherit;
                display: block;
                filter: alpha(opacity=0);
                opacity: 0;
                position: absolute;
                right: 0;
                text-align: right;
                top: 0;
                width: 100%;
            }

            .dateContainer [type="text"] {
                bottom: 0 !important;
                left: 0;
                top: auto;
            }

            .editor-content {
                overflow: hidden;
                min-width: 100%;
                width: auto;
                display: flex;
            }

            .editor-left {
                width: 300px;
                padding: 5px;
                border-right: 1px solid lightgrey;
                height: 100%;
                overflow-x: auto;
                min-width: 300px;;
            }

            .editor-right {
                height: 100%;
            }

            .revision-configs-panel {
                background-color: var(--cassini-form-controls-color);
                border: 1px solid var(--cassini-form-controls-color);
                display: inline;
                margin-left: auto;
                margin-right: auto;
                text-align: center;
                padding: 8px;
                border-radius: 5px;
            }

            .bom-configs-ui-select .ui-select-container .ui-select-match .ui-select-toggle .btn-link .glyphicon {
                color: #636e7b !important;
                font-size: 10px !important;
                top: 3px;
            }

            .bom-configs-ui-select .ui-select-container .btn.ui-select-toggle {
                padding-top: 3px !important;
                padding-bottom: 3px !important;
                height: 34px !important;
            }

            .bom-configs-ui-select.bom-configs-selected .ui-select-container .btn.ui-select-toggle .ui-select-match-text > span {
                margin-top: 2px !important;
            }

            .bom-configs-ui-select .ui-select-container input.ui-select-search {
                padding-top: 6px !important;
                padding-bottom: 6px !important;
            }

            .ml8 {
                margin-left: 8px;
            }

            #whereUsed-height svg {
                width: 100% !important;;
            }

        </style>
        <table class='table table-striped highlight-row' style="margin: 10px 0 0 0 !important;">
            <thead>
            <tr>
                <th class="add-col add-column sticky-col sticky-actions-col"
                    style="width:20px;white-space:nowrap;text-align: center;z-index: 12">
                    <span ng-hide="(itemBomVm.editItemPermission || sharedPermission == 'WRITE') && itemBomVm.copiedItems.length == 0 && itemBomVm.selectedBomRule == 'bom.latest'"
                          ng-if="itemBomVm.resolvedBom || item.configured || itemRevision.released || itemRevision.rejected
                        || (itemBomVm.item.lockObject  && loginPersonDetails.person.id != itemBomVm.item.lockedBy.id && !itemBomVm.adminPermission)"
                          style="width:40px;display: block !important;">

                    </span>
                    <i class="la la-plus" title="{{itemBomVm.AddItem}}"
                       style="cursor: pointer" ng-click="itemBomVm.onAddMultiple()"
                       ng-if="(itemBomVm.createBomPermission || sharedPermission == 'WRITE') && itemBomVm.copiedItems.length == 0 && itemBomVm.selectedBomRule == 'bom.latest'"
                       ng-hide="itemBomVm.resolvedBom || item.configured || itemRevision.released || itemRevision.rejected
                        || (itemBomVm.item.lockObject  && loginPersonDetails.person.id != itemBomVm.item.lockedBy.id && !itemBomVm.adminPermission)">
                    </i>
                <span uib-dropdown dropdown-append-to-body ng-if="itemBomVm.copiedItems.length > 0">
                    <i class="la la-plus dropdown-toggle" uib-dropdown-toggle title="{{itemBomVm.AddItem}}"
                       style="cursor: pointer"
                       ng-if="(itemBomVm.createBomPermission || sharedPermission == 'WRITE') && itemBomVm.selectedBomRule == 'bom.latest'"
                       ng-hide="itemBomVm.resolvedBom || item.configured || itemRevision.released || itemRevision.rejected
                        || (itemBomVm.item.lockObject  && loginPersonDetails.person.id != itemBomVm.item.lockedBy.id && !itemBomVm.adminPermission)"></i>
                    <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                        <li><a href ng-click="itemBomVm.onAddMultiple()" translate>SELECT_ITEMS</a>
                        </li>
                        <li>
                            <a href
                               ng-click="itemBomVm.pasteItemsFromClipboard(itemBomVm.item,'ITEM')"><span translate>PASTE_FROM_CLIPBOARD</span>
                                ({{itemBomVm.copiedItems.length}})</a>
                        </li>
                    </ul>
                </span>
                </th>
                <th class="toggle-column sticky-col sticky-actions-col"
                    style="white-space:nowrap;text-align: center;z-index: 12">
                <span class="level{{item.level}}"
                      ng-if="itemRevision.hasBom && itemBomVm.bomItems.length > 0 && showExpandAll">
                    <i class="mr10 fa" style="cursor: pointer; font-size: 16px;"
                       title="{{itemBomVm.expandedAll ? collapseAll : expandAll}}"
                       ng-class="{'fa-caret-right': itemBomVm.expandedAll == false, 'fa-caret-down': itemBomVm.expandedAll == true}"
                       ng-click="itemBomVm.expandAllBomItems(item)"></i>
                </span>
                </th>
                <th id="itemNumber-column" class="number-column sticky-col sticky-actions-col" style="z-index: 12"
                    translate>ITEM_NUMBER
                </th>
                <%--<th><span translate>SUBSTITUTE_ITEM</span></th>--%>
                <th><span translate>ITEM_TYPE</span></th>
                <th class="col-width-200 item-name-column sticky-col sticky-actions-col" style="z-index: 12;" translate>
                    ITEM_NAME
                </th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th style="text-align: center" translate>REVISION</th>
                <th style="text-align: center">
                    <span ng-if="itemBomVm.selectedPhase != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;text-transform: uppercase;">
                        {{itemBomVm.phaseCount}} ({{itemBomVm.selectedPhase}})
                        <i class="fa fa-times-circle" ng-click="itemBomVm.clearPhase()" title="{{removeTitle}}"></i>
                    </span>
                    <br>

                    <div class="dropdown" uib-dropdown style="display: inline-block">
                        <span uib-dropdown-toggle><span translate>LIFE_CYCLE_PHASE</span>
                            <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                        </span>
                        <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                            style="max-height:250px;overflow-y: auto;">
                            <li ng-repeat="phase in itemBomVm.lifecyclePhases"
                                ng-click="itemBomVm.onSelectPhase(phase)" style="text-transform: uppercase;"><a href="">{{phase}}</a>
                            </li>
                        </ul>
                    </div>
                </th>
                <th style="text-align: center" translate>MAKE_BUY</th>
                <th style="text-align: center" translate>MFR_PART</th>
                <th style="text-align: center" translate>QUANTITY</th>
                <th style="text-align: center" translate>UNITS</th>
                <th style="text-align: center" translate>THUMBNAIL</th>
                <th style="width: 150px;padding-right: 5px;text-align: center;">
                <span ng-if="bomSearch.fromDate != null"
                      style="font-weight:normal;font-size: 12px;cursor: pointer !important;">
                    ({{bomSearch.fromDate}})
                    <i class="fa fa-times-circle" ng-click="itemBomVm.clearFromDate()" title="{{removeTitle}}"></i>
                </span>
                    <br>
                    <span translate style="cursor: pointer !important;">EFFECTIVE_FROM</span>
                    <label class="dateContainer" ng-hide="itemBomVm.editModeItemsCount > 0">
                    <span class="fa fa-caret-down"
                          style="font-size: 16px;vertical-align: bottom;cursor: pointer !important;"></span>
                        <input type="text" ng-model="bomSearch.fromDate" date-picker
                               style="cursor: pointer !important;"/>
                    </label>
                </th>
                <th style="width: 150px;padding-right: 5px;text-align: center;">
                <span ng-if="bomSearch.toDate != null"
                      style="font-weight:normal;font-size: 12px;cursor: pointer !important;">
                    ({{bomSearch.toDate}})
                    <i class="fa fa-times-circle" title="{{removeTitle}}" ng-click="itemBomVm.clearToDate()"></i>
                </span>
                    <br>
                    <span translate style="cursor: pointer !important;">EFFECTIVE_TO</span>
                    <label class="dateContainer" ng-hide="itemBomVm.editModeItemsCount > 0">
                    <span class="fa fa-caret-down"
                          style="font-size: 16px;vertical-align: bottom;cursor: pointer !important;"></span>
                        <input type="text" ng-model="bomSearch.toDate" date-picker style="cursor: pointer !important;"/>
                    </label>
                </th>
                <th translate>REF_DES</th>
                <th translate>NOTES</th>
                <th class='added-column'
                    style="width: 150px;"
                    ng-repeat="selectedAttribute in itemBomVm.selectedBomAttributes">
                    {{selectedAttribute.name}}
                    <i class="fa fa-times-circle"
                       ng-click="itemBomVm.removeAttribute(selectedAttribute)"
                       title="{{removeThisColumn}}"></i>
                </th>
                <th ng-hide="external.external == true && sharedPermission == 'READ'"
                    class="actions-col sticky-col sticky-actions-col">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="itemBomVm.saveAddedItems()"
                       ng-if="itemBomVm.addedItemsToBom.length > 1 && itemBomVm.selectedBomAttributes.length == 0"
                       title="Save"
                       style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="itemBomVm.removeAddedItems()" title="Remove"
                       ng-if="itemBomVm.addedItemsToBom.length > 1 && itemBomVm.selectedBomAttributes.length == 0"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemBomVm.loading == true">
                <td colspan="100">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5"><span translate>LOADING_ITEMS</span>
                </td>
            </tr>
            <tr ng-if="itemBomVm.loading == false && itemBomVm.bomItems.length == 0">
                <td colspan="100">
                    <span ng-if="itemBomVm.selectedPhase == null" translate>NO_ITEMS</span>
                    <span ng-if="itemBomVm.selectedPhase != null">No <span style="text-transform: uppercase">{{itemBomVm.selectedPhase}}</span> items found</span>
                </td>
            </tr>

            <tr id="{{$index}}" ng-if="itemBomVm.loading == false"
                ng-repeat="item in itemBomVm.bomItems | limitTo:limit" ng-class="{'selected': item.selected,
                'unresolved-bom-item ': item.revision == '?'}"
                dragbomitem bom-items="itemBomVm.bomItems" update-bom-item-seq="itemBomVm.updateBomItemSeq">
                <td class="add-column sticky-col sticky-actions-col" style="z-index: 11;width: 20px;">
                    <span ng-if="itemBomVm.resolvedBom || itemBomVm.item.configured || item.configured || itemRevision.released || itemRevision.rejected || item.editMode
                            || (itemBomVm.item.lockObject  && loginPersonDetails.person.id != itemBomVm.item.lockedBy.id && !itemBomVm.adminPermission)"
                          ng-hide="(itemBomVm.createBomPermission || sharedPermission == 'WRITE') && itemBomVm.copiedItems.length == 0 && itemBomVm.selectedBomRule == 'bom.latest'"
                          style="display: block !important;">

                    </span>
                    <i class="la la-plus" title="{{itemBomVm.AddItem}}"
                       ng-click="itemBomVm.onAddMultiple(item)" style="cursor:pointer"
                       ng-if="(itemBomVm.createBomPermission || sharedPermission == 'WRITE') && itemBomVm.copiedItems.length == 0 && itemBomVm.selectedBomRule == 'bom.latest'"
                       ng-hide="itemBomVm.resolvedBom || itemBomVm.item.configured || item.configured || itemRevision.released || itemRevision.rejected || item.editMode
                            || (itemBomVm.item.lockObject  && loginPersonDetails.person.id != itemBomVm.item.lockedBy.id && !itemBomVm.adminPermission)"></i>
                <span uib-dropdown style="min-width: 50px" ng-if="itemBomVm.copiedItems.length > 0">
                    <span dropdown-append-to-body> </span>
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle title="{{itemBomVm.AddItem}}"
                           ng-if="(itemBomVm.createBomPermission || sharedPermission == 'WRITE') && itemBomVm.selectedBomRule == 'bom.latest'"
                           ng-hide="itemBomVm.resolvedBom || itemBomVm.item.configured || item.configured || itemRevision.released || itemRevision.rejected
                            || (itemBomVm.item.lockObject  && loginPersonDetails.person.id != itemBomVm.item.lockedBy.id && !itemBomVm.adminPermission)"></i>
                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-left" role="menu"
                            style="z-index: 9999 !important;">
                            <%--<li><a href ng-click="itemBomVm.onAddSingle(item)" translate>DETAILS_ADD_ITEMS.SINGLE</a>--%>
                            </li>
                                <li ng-if="itemBomVm.createBomPermission">
                                    <a href ng-click="itemBomVm.onAddMultiple(item)"
                                       translate>SELECT_ITEMS</a></li>
                            <li>
                                <a href
                                   ng-click="itemBomVm.pasteItemsFromClipboard(item,'BOM')">
                                    <span translate>PASTE_FROM_CLIPBOARD</span> ({{itemBomVm.copiedItems.length}})
                                </a>
                            </li>
                        </ul>
                </span>
                </td>
                <td class="toggle-column sticky-col sticky-actions-col"
                    style="white-space:nowrap;text-align: center;z-index: 11">
                    <a class="icon fa fa-paperclip"
                       title="Show item files"
                       ng-if="item.hasFiles && item.itemFiles.length > 0"
                       uib-popover-template="itemBomVm.bomItemFilePopover.templateUrl"
                       popover-append-to-body="true"
                       popover-popup-delay="50"
                       popover-placement="top-left"
                       popover-title="({{item.itemNumber}}) Files"
                       popover-trigger="'outsideClick'">
                    </a>
                    <i ng-if="item.configurable == true" title="Configurable Item" class="fa fa-cog"></i>
                    <i ng-if="item.configured == true" title="Configured Item" class="fa fa-cogs"></i>
                    <i ng-if="item.hasSubstitutes && item.hasAlternates" title="{{hasSubstituteParts}}"
                       class="fa fa-exchange" style="padding-left: 3px;padding-top: 5px;"></i>
                    <i ng-if="!item.hasSubstitutes && item.hasAlternates" title="{{hasAlternateParts}}"
                       class="fa fa-object-ungroup" style="padding-left: 3px;padding-top: 5px;"></i>
                </td>
                <td class="number-column sticky-col sticky-actions-col" style="z-index: 11">
                <span class="level{{item.level}}">
                    <i ng-if="item.hasBom && item.isNew == false && item.editMode == false" class="fa"
                       style="cursor: pointer;margin-right: 0;" title="{{itemBomVm.ExpandCollapse}}"
                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                       'fa-caret-down': item.expanded == true}" ng-click="itemBomVm.toggleBomItem(item)"></i>
                    <a ng-click="itemBomVm.showItemDetails(item)"
                       ng-if="item.id != undefined && item.isNew == false && item.latestRevision != null">
                        <span ng-class="{'ml8': item.hasBom == false}" style="">
                            <span ng-bind-html="item.itemNumber | highlightText: freeTextQuerys"></span>
                        </span>
                    </a>
                    <span ng-if="item.id == undefined || item.latestRevision == null">{{item.itemNumber}}</span>
                    <span class="label label-default label-count"
                          ng-if="item.count > 0 && item.hasBom && item.isNew == false && item.editMode == false"
                          title="{{itemBomVm.ExpandCollapse}}"
                          ng-click="itemBomVm.toggleBomItem(item)"
                          style="font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;"
                          ng-bind-html="item.count"></span>
                </span>
                    <%--<span ng-if="item.isNew == true">
                            <div class="level{{item.level}} input-group">
                                <div class="input-group-addon" ng-click="itemBomVm.itemSelection(item)">
                                    <i class="fa fa-search"></i>
                                </div>
                                <input style="width: 150px;margin: 0" type="text" class="form-control input-sm"
                                       name="itemNumber"
                                       ng-model="item.itemNumber" ng-blur="itemBomVm.findItem(item)" autofocus>
                            </div>
                    </span>--%>
                </td>

                <%--<td class="itemType-column">
                    <a ui-sref="app.items.details({itemId: item.substituteItem.latestRevision})"
                       ng-if="item.itemRevision.id != undefined && item.isNew == false">
                        <span ng-class="{'ml20': item.itemRevision.hasBom == false}">
                            <span ng-bind-html="item.substituteItem.itemNumber | highlightText: freeTextQuerys"></span>
                        </span>
                    </a>
                </td>--%>
                <td class="itemType-column">
                    <span ng-bind-html="item.itemTypeName | ellipsis:20:freeTextQuerys"></span>
                </td>
                <td class="col-width-200 item-name-column sticky-col sticky-actions-col" style="z-index: 11"
                    title="{{item.itemName}}">
                    <span ng-bind-html="item.itemName | ellipsis:35:freeTextQuerys"></span>
                </td>
                <td class="col-width-250" title="{{item.description}}">
                    <span ng-bind-html="item.description | ellipsis:35:freeTextQuerys"></span>
                </td>
                <td style="text-align: center;">
                    <span class='label label-danger' style="padding-top: 6px;"
                          ng-if="item.revision == '?'">
                        <i class="fa fa-question" style="font-size: 14px;margin-top: 5px;"></i>
                    </span>
                    <span ng-if="item.revision != '?'">
                        <a href="" ng-click="itemBomVm.showItemRevisionHistory(item)">{{item.revision}}</a></span>

                </td>
                <td style="text-align: center">
                <span class='label label-danger' style="padding-top: 6px;"
                      ng-if="item.lifeCyclePhase.phase == '?'">
                    <i class="fa fa-question" style="font-size: 14px;margin-top: 5px;"></i>
                </span>
                    <item-status ng-if="item.lifeCyclePhase.phase != '?'" item="item"></item-status>
                </td>
                <td style="text-align: center">
                    <span class="label label-outline bg-light-primary"
                          ng-if="item.makeOrBuy == 'MAKE'">{{item.makeOrBuy}}</span>
                    <span class="label label-outline bg-light-danger"
                          ng-if="item.makeOrBuy == 'BUY'">{{item.makeOrBuy}}</span>
                </td>
                <td>
                    <a ui-sref="app.mfr.mfrparts.details({mfrId: item.mfrId, manufacturePartId: item.mfrPartId})"
                       title="{{item.mfrPartName}}">
                        {{item.mfrPartNumber }}{{item.mfrPartNumber.length > 20 ? '...' : ''}}</a>
                </td>
                <td style="text-align: center">
                    <span ng-if="item.isNew != true && item.editMode != true">{{item.quantity}}</span>

                    <div style="display: flex;justify-content: center; width: 100%"
                         ng-if="item.isNew == true || item.editMode == true">
                        <input ng-pattern="/^[1-9]{1}[0-9]*$/" style="width: 70px;margin: 0;text-align: center"
                               type="number"
                               class="form-control input-sm" name="quantity"
                               ng-model="item.newQuantity">
                    </div>
                </td>
                <td style="text-align: center">{{item.units}}</td>
                <td style="width: 100px;text-align: center">
                    <div>
                        <a ng-if="item.hasThumbnail" href=""
                           ng-click="itemBomVm.showThumbnailImage(item)"
                           title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}">
                            <img ng-src="{{item.thumbnailImage}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </a>
                        <a ng-if="!item.hasThumbnail" href="">
                            <img src="app/assets/images/cassini-logo-greyscale.png" title="No thumbnail" alt=""
                                 class="no-thumbnail-preview">
                        </a>

                        <%--<div id="myModal23" class="img-model modal">
                            <span class="closeImage">&times;</span>
                            <img class="modal-content" id="img13">
                        </div>--%>
                        <div id="item-thumbnail{{item.id}}{{item.item}}" class="item-thumbnail modal">
                            <div class="item-thumbnail-content">
                                <div class="thumbnail-content" style="display: flex;width: 100%;">
                                    <div class="thumbnail-view" id="thumbnail-view{{item.id}}{{item.item}}">
                                        <div id="thumbnail-image{{item.id}}{{item.item}}"
                                             style="display: table-cell;vertical-align: middle;text-align: center;">
                                            <img ng-src="{{item.thumbnailImage}}"
                                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close{{item.id}}{{item.item}}"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
                <td class="text-center">
                <span ng-if="item.isNew != true && item.editMode != true">{{item.effectiveFrom}}
                </span>
                <span ng-if="item.isNew == true || item.editMode == true">
                    <input style="width: 150px;margin: 0;background-color: #fff;" type="text"
                           class="form-control input-sm" name="effectivefrom"
                           placeholder="{{effectiveFromPlaceholder}}" min-date='{{item.itemCreatedDate}}'
                           ng-model="item.effectiveFrom" date-picker/>
                    <i class="fa fa-times" ng-if="item.effectiveFrom != null"
                       style="position: absolute;margin-top: -26px;margin-left: 60px;cursor: pointer;"
                       ng-click="item.effectiveFrom = null"></i>
                </span>
                </td>
                <td class="text-center">
                <span ng-if="item.isNew != true && item.editMode != true">{{item.effectiveTo}}
                </span>
                <span ng-if="item.isNew == true || item.editMode == true">
                    <input style="width: 150px;margin: 0;background-color: #fff;" type="text"
                           class="form-control input-sm" name="effectiveto"
                           placeholder="{{effectiveToPlaceholder}}"
                           ng-model="item.effectiveTo" date-picker min-date='{{item.itemCreatedDate}}'/>
                    <i class="fa fa-times" ng-if="item.effectiveTo != null"
                       style="position: absolute;margin-top: -26px;margin-left: 60px;cursor: pointer;"
                       ng-click="item.effectiveTo = null"></i>
                </span>
                </td>
                <td>
                <span ng-if="item.isNew != true && item.editMode != true"
                      title="{{item.refdes}}">
                    <span ng-bind-html="item.refdes | ellipsis:20:freeTextQuerys"></span>
                    {{item.refdes.length > 22 ? '...' : ''}}
                </span>
                <span ng-if="item.isNew == true || item.editMode == true">
                   <form>
                       <input style="width: 150px;margin: 0" type="text" class="form-control input-sm" name="refdes"
                              ng-model="item.newRefdes">
                   </form>
                </span>
                </td>
                <td class="col-width-200">
                <span ng-if="item.isNew != true && item.editMode != true" title="{{item.notes}}">
                    {{item.notes}}
                </span>
                <span ng-if="item.isNew == true || item.editMode == true">
                   <form>
                       <input style="width: 150px;margin: 0" type="text" class="form-control input-sm" name="notes"
                              ng-model="item.newNotes">
                   </form>
                </span>
                </td>


                <td class="added-column" style="width: 150px;"
                    ng-repeat="objectAttribute in itemBomVm.selectedBomAttributes">
                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'TEXT'"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].stringValue}}
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'INTEGER'"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].integerValue}}
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'DOUBLE'"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].doubleValue}}
                    </p>

                    <div ng-if="item.editMode == false && objectAttribute.dataType == 'BOOLEAN'"
                         ng-init="attrName = objectAttribute.name">
                        <p ng-if="item[attrName].booleanValue == true">{{item[attrName].booleanValue}}</p>

                        <p ng-if="item[attrName].booleanValue == false">false</p>
                    </div>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'DATE'"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].dateValue}}
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'TIME'"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].timeValue}}
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'TIMESTAMP'"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].timestampValue}}
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'HYPERLINK'"
                       ng-init="attrName = objectAttribute.name">
                        <a href="" ng-click="showHyperLink(item[attrName].hyperLinkValue)">{{item[attrName].hyperLinkValue}}</a>
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'LONGTEXT'"
                       ng-init="attrName = objectAttribute.name" class="col-width-300">
                        <span ng-bind-html="item[attrName].longTextValue "
                              title="{{item[attrName].longTextValue}}"></span>
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'RICHTEXT'"
                       ng-init="attrName = objectAttribute.name">
                        <a href="" ng-if="item[attrName].richTextValue != null"
                           ng-click="showBomItemRichText(objectAttribute,item[attrName],item)">
                            Click to show value</a>
                    </p>

                    <p ng-if="item.editMode == false && objectAttribute.dataType == 'LIST' && !objectAttribute.listMultiple"
                       ng-init="attrName = objectAttribute.name">
                        {{item[attrName].listValue}}
                    </p>

                    <div class="attributeTooltip"
                         ng-if="item.editMode == false && objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == true"
                         ng-init="attrName = objectAttribute.name">
                        <p>
                            <a ng-if="item[attrName].mlistValue.length > 0" href="">
                                {{item[attrName].mlistValue.length}} Values
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="listValue in item[attrName].mlistValue">
                                    <a href=""
                                       style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                        {{listValue}}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div ng-if="item.editMode == false && objectAttribute.dataType == 'IMAGE'" style="width:100px;"
                         ng-init="attrName = objectAttribute.name">
                        <a href="" ng-click="itemBomVm.showAttributeImage(item)"
                           title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                            <img ng-if="item[attrName] != null"
                                 ng-src="{{item[attrName].imagePath}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </a>

                        <div id="item-thumbnail{{item.id}}{{item.id}}" class="item-thumbnail modal">
                            <div class="item-thumbnail-content">
                                <div class="thumbnail-content" style="display: flex;width: 100%;">
                                    <div class="thumbnail-view" id="thumbnail-view{{item.id}}{{item.id}}">
                                        <div id="thumbnail-image{{item.id}}{{item.id}}"
                                             style="display: table-cell;vertical-align: middle;text-align: center;">
                                            <img ng-src="{{item[attrName].imagePath}}"
                                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close{{item.id}}{{item.id}}"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%--<div id="myModal2" class="img-model modal">
                            <span class="closeImage">&times;</span>
                            <img class="modal-content" id="img03">
                        </div>--%>
                    </div>

                    <div class="attributeTooltip"
                         ng-if="item.editMode == false && objectAttribute.dataType == 'ATTACHMENT'"
                         ng-init="attrName = objectAttribute.name">
                        <p>
                            <a ng-if="item[attrName].length > 0" href="">
                                {{item[attrName].length}} Attachments
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="attachment in item[attrName]">
                                    <a href="" ng-click="itemBomVm.openAttachment(attachment)"
                                       title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                       style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                        {{attachment.name}}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div ng-if="item.editMode == false && objectAttribute.dataType == 'CURRENCY'">
                    <span ng-if="item[attrName].currencyValue != null"
                          ng-init="currencyType = objectAttribute.name+'type'"
                          ng-bind-html="item[currencyType]">
                        </span>
                    <span ng-init="attrName = objectAttribute.name">{{item[attrName].currencyValue}}
                    </span>
                    </div>

                    <div ng-if="item.editMode == false && objectAttribute.dataType == 'OBJECT'"
                         ng-init="attrName = objectAttribute.name">
                        <p ng-if="item[attrName].refValue.objectType == 'ITEM'">
                            {{item[attrName].refValue.itemNumber}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'ITEMREVISION'">
                            {{item[attrName].refValue.itemMaster + " :" + item[attrName].refValue.revision + " :" +
                            item[attrName].refValue.lifeCyclePhase.phase}}
                        </p>

                        <p ng-if="item[attrName].refValue.objectType == 'CHANGE'">
                            {{item[attrName].refValue.ecoNumber}}</p>

                        <div ng-if="item[attrName].refValue.objectType == 'CHANGE'" href="">
                                <span ng-if="item[attrName].refValue.changeType == 'ECO'"
                                      ng-bind-html="item[attrName].refValue.ecoNumber">
                                </span>
                                <span ng-if="item[attrName].refValue.changeType == 'DCO'"
                                      ng-bind-html="item[attrName].refValue.dcoNumber">
                                </span>
                                <span ng-if="item[attrName].refValue.changeType == 'ECR' || item[attrName].refValue.changeType == 'DCR'"
                                      ng-bind-html="item[attrName].refValue.crNumber">
                                </span>
                                <span ng-if="item[attrName].refValue.changeType == 'MCO'"
                                      ng-bind-html="item[attrName].refValue.mcoNumber">
                                </span>
                        </div>

                        <p ng-if="item[attrName].refValue.objectType == 'MANUFACTURER'">
                            {{item[attrName].refValue.name}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'MANUFACTURERPART'">
                            {{item[attrName].refValue.partNumber}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'PLMWORKFLOWDEFINITION'">
                            {{item[attrName].refValue.name}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'PERSON'">
                            {{item[attrName].refValue.firstName}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'MROASSET' || item[attrName].refValue.objectType == 'MROMETER' ||
                                item[attrName].refValue.objectType == 'MROSPAREPART' || item[attrName].refValue.objectType == 'MROWORKREQUEST' || item[attrName].refValue.objectType == 'MROWORKORDER'">
                            {{item[attrName].refValue.number}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'PLANT' || item[attrName].refValue.objectType == 'ASSEMBLYLINE' ||
                                item[attrName].refValue.objectType == 'WORKCENTER' || item[attrName].refValue.objectType == 'MACHINE' ||
                                item[attrName].refValue.objectType == 'JIGFIXTURE' || item[attrName].refValue.objectType == 'MANPOWER' ||
                                item[attrName].refValue.objectType == 'INSTRUMENT' || item[attrName].refValue.objectType == 'EQUIPMENT' ||
                                item[attrName].refValue.objectType == 'PRODUCTIONORDER' || item[attrName].refValue.objectType == 'OPERATION' ||
                                item[attrName].refValue.objectType == 'MATERIAL' || item[attrName].refValue.objectType == 'TOOL'">
                            {{item[attrName].refValue.number}}</p>

                        <p ng-if="item[attrName].refValue.objectType == 'REQUIREMENT' || item[attrName].refValue.objectType == 'REQUIREMENTDOCUMENT'
                        || item[attrName].refValue.objectType == 'CUSTOMOBJECT'">{{item[attrName].refValue.number}}
                        </p>

                    </div>

                    <div ng-if="item.editMode == true"
                         ng-init="attrName = objectAttribute.name" style="min-width: 150px;">
                        <input ng-if="objectAttribute.dataType == 'TEXT' && objectAttribute.editMode == true"
                               type="text" class="form-control input-sm"
                               ng-model="item[attrName].stringValue">

                        <p ng-if="objectAttribute.dataType == 'TEXT' && objectAttribute.editMode == false"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].stringValue}}
                        </p>
                        <input type="number"
                               ng-if="objectAttribute.dataType == 'INTEGER' && objectAttribute.editMode == true"
                               type="text"
                               class="form-control input-sm"
                               ng-model="item[attrName].integerValue">

                        <p ng-if="objectAttribute.dataType == 'INTEGER' && objectAttribute.editMode == false"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].integerValue}}
                        </p>
                        <input type="number" class="form-control" name="title" class="form-control input-sm"
                               ng-if="objectAttribute.dataType == 'DOUBLE' && objectAttribute.editMode == true"
                               ng-model="item[attrName].doubleValue">

                        <p ng-if="objectAttribute.dataType == 'DOUBLE' && objectAttribute.editMode == false"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].doubleValue}}
                        </p>
                        <label class="radio-inline" ng-repeat="choice in [true, false]"
                               ng-if="objectAttribute.dataType == 'BOOLEAN' && objectAttribute.editMode == true">
                            <input type="radio" name="change_{{objectAttribute.name}}" ng-value="choice"
                                   ng-model="item[attrName].booleanValue">{{choice}}
                        </label>

                        <p ng-if="objectAttribute.dataType == 'BOOLEAN' && objectAttribute.editMode == false && item[attrName].booleanValue == true">
                            {{item[attrName].booleanValue}}</p>

                        <p ng-if="objectAttribute.dataType == 'BOOLEAN' && objectAttribute.editMode == false && item[attrName].booleanValue == false">
                            false</p>
                        <textarea type="text" class="form-control" name="title"
                                  ng-if="objectAttribute.dataType == 'LONGTEXT' && objectAttribute.editMode == true"
                                  rows="3" ng-model="item[attrName].longTextValue"
                                  style="width:250px;resize: none;"></textarea>

                        <p ng-if="objectAttribute.dataType == 'LONGTEXT' && objectAttribute.editMode == false"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].longTextValue}}
                        </p>
                        <input type="text" class="form-control" name="title"
                               ng-if="objectAttribute.dataType == 'HYPERLINK' && objectAttribute.editMode == true"
                               ng-model="item[attrName].hyperLinkValue" style="width:250px;">

                        <p ng-if="objectAttribute.dataType == 'HYPERLINK' && objectAttribute.editMode == false"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].hyperLinkValue}}
                        </p>

                        <i class="fa fa-pencil" style="display: block"
                           ng-if="objectAttribute.dataType == 'RICHTEXT' && objectAttribute.editMode == true"
                           ng-click="showBomItemRichText(objectAttribute,item[attrName],item)"></i>

                        <p ng-if="objectAttribute.editMode == false && objectAttribute.dataType == 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            <a href="" ng-if="item[attrName].richTextValue != null"
                               ng-click="showBomItemRichText(objectAttribute,item[attrName],item)">
                                Click to show value</a>
                        </p>

                        <div class="input-group"
                             ng-if="objectAttribute.dataType == 'DATE' && objectAttribute.editMode == true"
                             style="width:200px;">
                            <input type="text" class="form-control" date-picker-edit
                                   ng-model="item[attrName].dateValue" style="width: 165px;"
                                   name="attDate" placeholder="dd/mm/yyyy">
                            <span class="input-group-addon" style="padding: 0;">
                                <i class="glyphicon glyphicon-calendar" style="display: block;width: 35px;"></i>
                            </span>
                        </div>

                        <p ng-if="item.editMode == false && objectAttribute.dataType == 'DATE'"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].dateValue}}
                        </p>

                        <div ng-if="objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == false && objectAttribute.editMode == true"
                             style="width: 220px;">
                            <ui-select ng-model="item[attrName].listValue" theme="bootstrap" style="width:200px">
                                <ui-select-match placeholder="Select">{{$select.selected}}</ui-select-match>
                                <ui-select-choices
                                        repeat="value in objectAttribute.lov.values">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                        <p ng-if="objectAttribute.editMode == false && objectAttribute.dataType == 'LIST' && !objectAttribute.listMultiple"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].listValue}}
                        </p>

                        <div ng-if="objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == true && objectAttribute.editMode == true"
                             style="width: 100%;">
                            <ui-select multiple ng-model="item[attrName].mlistValue" theme="bootstrap"
                                       close-on-select="false" title="Choose a List" remove-selected="true">
                                <ui-select-match placeholder="Select list...">{{$item}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="value in objectAttribute.lov.values track by value">
                                    <div ng-bind="value"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.editMode == false && objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == true"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="item[attrName].mlistValue.length > 0" href="">
                                    {{item[attrName].mlistValue.length}} Values
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="listValue in item[attrName].mlistValue">
                                        <a href=""
                                           style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{listValue}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <input type="time"
                               ng-if="objectAttribute.dataType == 'TIMESTAMP' && objectAttribute.editMode == true"
                               class="form-control input-sm"
                               ng-model="item[attrName].timestampValue">


                        <p ng-if="objectAttribute.editMode == false && objectAttribute.dataType == 'TIMESTAMP'"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].timestampValue}}
                        </p>

                        <input class="form-control" type="text" ng-model="item[attrName].timeValue" time-picker
                               style="width: 240px;"
                               ng-if="objectAttribute.dataType == 'TIME' && objectAttribute.editMode == true">

                        <p ng-if="objectAttribute.editMode == false && objectAttribute.dataType == 'TIME'"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName].timeValue}}
                        </p>


                        <div ng-if="objectAttribute.dataType == 'IMAGE' && objectAttribute.editMode == true">
                            <input class="browse-control" name="file"
                                   type="file" ng-file-model="item[attrName].imageFile"/>
                        </div>


                        <div ng-if="objectAttribute.editMode == false && objectAttribute.dataType == 'IMAGE'"
                             style="width:100px;"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="itemBomVm.showAttributeImage(item)"
                               title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                                <img ng-if="item[attrName] != null"
                                     ng-src="{{item[attrName].imagePath}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="item-thumbnail{{item.id}}{{item.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{item.id}}{{item.id}}">
                                            <div id="thumbnail-image{{item.id}}{{item.id}}"
                                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{item[attrName].imagePath}}"
                                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close{{item.id}}{{item.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.editMode == true">
                            <input class="browse-control" name="file" multiple="true" style="width: 150px;"
                                   type="file" ng-file-model="item[attrName].attachmentValues"/>

                        </div>
                        <div ng-if="item.editMode == true && objectAttribute.dataType == 'CURRENCY' && objectAttribute.editMode == true">
                            <div>
                                <select class="form-control" ng-model="item[attrName].currencyType"
                                        ng-init="item[attrName].currencyType = itemBomVm.currencies[0].id"
                                        ng-options="currency.id as currency.name for currency in itemBomVm.currencies"
                                        style="width:100px;height: 34px;">
                                </select>
                                <input type="text" class="form-control" name="title"
                                       style="width:100px;margin-top:-33px;margin-left:100px;"
                                       ng-model="item[attrName].currencyValue">
                            </div>
                        </div>

                        <div ng-if="objectAttribute.dataType == 'OBJECT'" ng-init="attrName = objectAttribute.name">
                            <i class="fa fa-pencil" style="display: block"
                               ng-if="item[attrName].objectType != null && objectAttribute.editMode == true"
                               type="button" style="width: 85px;height: 40px;"
                               title="{{'CLICK_TO_SELECT_TITLE' | translate}}"
                               ng-click="itemBomVm.showObjectViews(item,item[attrName])">
                            </i>

                            <p ng-if="item[attrName].refValue.objectType == 'CHANGE'">
                                {{item[attrName].refValue.ecoNumber}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'ITEM'">
                                {{item[attrName].refValue.itemNumber}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'ITEMREVISION'">
                                {{item[attrName].refValue.itemMaster + " :" + item[attrName].refValue.revision + " :" +
                                item[attrName].refValue.lifeCyclePhase.phase}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'MANUFACTURER'">
                                {{item[attrName].refValue.name}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'MANUFACTURERPART'">
                                {{item[attrName].refValue.partNumber}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'PLMWORKFLOWDEFINITION'">
                                {{item[attrName].refValue.name}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'PERSON'">
                                {{item[attrName].refValue.firstName}}
                            </p>

                            <p ng-if="item[attrName].refValue.objectType == 'CHANGE'">
                                {{item[attrName].refValue.ecoNumber}}</p>

                            <div ng-if="item[attrName].refValue.objectType == 'CHANGE'" href="">
                                <span ng-if="item[attrName].refValue.changeType == 'ECO'"
                                      ng-bind-html="item[attrName].refValue.ecoNumber">
                                </span>
                                <span ng-if="item[attrName].refValue.changeType == 'DCO'"
                                      ng-bind-html="item[attrName].refValue.dcoNumber">
                                </span>
                                <span ng-if="item[attrName].refValue.changeType == 'ECR' || item[attrName].refValue.changeType == 'DCR'"
                                      ng-bind-html="item[attrName].refValue.crNumber">
                                </span>
                                <span ng-if="item[attrName].refValue.changeType == 'MCO'"
                                      ng-bind-html="item[attrName].refValue.mcoNumber">
                                </span>
                            </div>

                            <p ng-if="item[attrName].refValue.objectType == 'MROASSET' || item[attrName].refValue.objectType == 'MROMETER' ||
                                item[attrName].refValue.objectType == 'MROSPAREPART' || item[attrName].refValue.objectType == 'MROWORKREQUEST' || item[attrName].refValue.objectType == 'MROWORKORDER'">
                                {{item[attrName].refValue.number}}</p>

                            <p ng-if="item[attrName].refValue.objectType == 'PLANT' || item[attrName].refValue.objectType == 'ASSEMBLYLINE' ||
                                item[attrName].refValue.objectType == 'WORKCENTER' || item[attrName].refValue.objectType == 'MACHINE' ||
                                item[attrName].refValue.objectType == 'JIGFIXTURE' || item[attrName].refValue.objectType == 'MANPOWER' ||
                                item[attrName].refValue.objectType == 'INSTRUMENT' || item[attrName].refValue.objectType == 'EQUIPMENT' ||
                                item[attrName].refValue.objectType == 'PRODUCTIONORDER' || item[attrName].refValue.objectType == 'OPERATION' ||
                                item[attrName].refValue.objectType == 'MATERIAL' || item[attrName].refValue.objectType == 'TOOL'">
                                {{item[attrName].refValue.number}}</p>

                            <p ng-if="item[attrName].refValue.objectType == 'REQUIREMENT' || item[attrName].refValue.objectType == 'REQUIREMENTDOCUMENT'
                             || item[attrName].refValue.objectType == 'CUSTOMOBJECT'">{{item[attrName].refValue.number}}
                            </p>

                        </div>
                    </div>
                </td>
                <td class="actions-col sticky-col sticky-actions-col">
                    <%--<span class="btn-group" class="btn-group"
                          ng-if="item.isNew == true || item.editMode == true"
                          style="margin: -1px">
                        <button class="btn btn-xs btn-success" type="button" title="{{saveItemTitle}}"
                                ng-click="itemBomVm.onOk(item)">
                            <i class="fa fa-check"></i>
                        </button>
                        <button class="btn btn-xs btn-danger" type="button" title="{{cancelChangesTitle}}"
                                ng-click="itemBomVm.onCancel(item)">
                            <i class="fa fa-times"></i>
                        </button>
                    </span>--%>

                    <span class="btn-group" class="btn-group"
                          ng-if="item.isNew == true || item.editMode == true"
                          style="margin: -1px">
                    <i type="button" title="{{saveItemTitle}}"
                       ng-click="itemBomVm.onOk(item)" class="la la-check">
                    </i>
                    <i type="button" title="{{cancelChangesTitle}}"
                       ng-click="itemBomVm.onCancel(item)"
                       class="la la-times">
                    </i>
                </span>

                <span ng-if="item.isNew != true && item.editMode != true  && itemBomVm.selectedBomRule == 'bom.latest'"
                      ng-hide="item.configured || itemBomVm.resolvedBom || itemBomVm.item.configured || (external.external == true && sharedPermission == 'READ')"
                      class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="(external.external == true && sharedPermission == 'WRITE') && item.editMode == false"
                            ng-click="itemBomVm.editItem(item)"
                            ng-class="{'disabled':(item.disableEdit) || itemRevision.released || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !itemBomVm.adminPermission)}">
                            <a href="" translate>EDIT_BOM_ITEM</a>
                        </li>

                        <li ng-if="(external.external == true && sharedPermission == 'WRITE') && item.editMode == false"
                            ng-click="itemBomVm.deleteItem(item)"
                            ng-class="{'disabled':(item.disableEdit) || itemRevision.released || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !itemBomVm.adminPermission)}">
                            <a href="" translate>DELETE_BOM_ITEM</a>
                        </li>

                        <%--<li ng-if="(external.external == true && sharedPermission == 'WRITE') && item.editMode == false && item.itemRevision.hasBom == false"
                            ng-click="itemBomVm.onSubstituteItem(item)"
                            ng-class="{'disabled':(item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !hasPermission('permission.admin.all'))}">
                            <a href="" translate>SUBSTITUTE_BOM_ITEM</a>
                        </li>--%>

                        <li ng-if="(external.external == false) && item.editMode == false"
                            ng-class="{'cursor-override': !itemBomVm.editBomPermission}"
                            title="{{itemBomVm.editBomPermission ? '' : noPermission}}">
                            <a href="" ng-click="itemBomVm.editItem(item)"
                               ng-class="{'disabled':((item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id)) || itemRevision.released || !itemBomVm.editBomPermission}"
                               translate>EDIT_BOM_ITEM</a>
                        </li>

                        <li ng-if="(external.external == false) && item.editMode == false"
                            ng-class="{'cursor-override': !itemBomVm.deleteBomPermission}"
                            title="{{itemBomVm.deleteBomPermission ? '' : noPermission}}">
                            <a href="" ng-click="itemBomVm.deleteItem(item)"
                               ng-class="{'disabled':((item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id)) || itemRevision.released || !itemBomVm.deleteBomPermission}"
                               translate>DELETE_BOM_ITEM</a>
                        </li>

                        <%--<li ng-if="(external.external == false && hasPermission('permission.items.edit')) && item.editMode == false && item.itemRevision.hasBom  == false"
                            ng-click="itemBomVm.onSubstituteItem(item)"
                            ng-class="{'disabled':(item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !hasPermission('permission.admin.all'))}">
                            <a href="" translate>SUBSTITUTE_BOM_ITEM</a>
                        </li>--%>
                        <li ng-if="(external.external == false && itemBomVm.editItemPermission) && item.editMode == false && item.itemClass == 'PART'"
                            ng-click="itemBomVm.addSubstituteParts(item)"
                            ng-class="{'disabled':(item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !itemBomVm.adminPermission)}">
                            <a href="" translate>ADD_SUBSTITUTE_PARTS</a>
                        </li>
                        <plugin-table-actions context="item.bom" object-value="item"></plugin-table-actions>
                    </ul>
                </span>
                <span ng-if="item.configurable && itemBomVm.item.configured"
                      ng-hide="itemLifeCycleStatus == 'RELEASED' || itemLifeCycleStatus == 'OBSOLETE' || (external.external == true && sharedPermission == 'READ')"
                      class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="(external.external == false && itemBomVm.editItemPermission) && item.configurable && itemBomVm.item.configured"
                            ng-click="itemBomVm.resolveBomItem(item)"
                            ng-class="{'disabled':(item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !itemBomVm.adminPermission)}">
                            <a href="" translate>RESOLVE_BOM_ITEM</a>
                        </li>
                        <li ng-if="(external.external == true && sharedPermission == 'WRITE')"
                            ng-click="itemBomVm.resolveBomItem(item)"
                            ng-class="{'disabled':(item.disableEdit) || (itemBomVm.itemRevision.itemMasterObject.lockObject
                                        && loginPersonDetails.person.id != itemBomVm.itemRevision.itemMasterObject.lockedBy.id && !itemBomVm.adminPermission)}">
                            <a href="" translate>RESOLVE_BOM_ITEM</a>
                        </li>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="bom-rollup" class="bom-model modal">
        <div class="bomRollup-content">
            <div class="bom-header">
                <div class="btn-group pull-left" style="margin: 2px 0 0 10px !important;">
                    <button type="button" class="btn btn-sm btn-primary dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="mr5" translate>EXPORT</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="itemBomVm.exportRollUpReport('CSV')"><a href="">CSV</a></li>
                        <li ng-click="itemBomVm.exportRollUpReport('EXCEL')"><a href="">Excel</a></li>
                        <li ng-click="itemBomVm.exportRollUpReport('HTML')"><a href="">Html</a></li>
                    </ul>
                </div>
                <span class="configuration-header">{{bomRollUpReportTitle}} ( {{item.itemName}} )</span>
                <a href="" ng-click="hideBomRollup()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="bom-content" style="padding: 0">
                <div class='responsive-table'
                     style="height: 100% !important;overflow:auto !important;width: 100% !important;position: relative !important;padding: 10px !important;margin: 0 !important;"
                     infinite-scroll infinite-scroll-callback-fn="increaseRollupLimit()"
                     infinite-scroll-percentage="80">
                    <table class='table table-striped highlight-row'>
                        <thead>
                        <tr>
                            <th class="r-item-number  sticky-col sticky-actions-col" style="z-index: 12" translate>
                                ITEM_NUMBER
                            </th>
                            <th class="r-item-type sticky-col sticky-actions-col" style="z-index: 12" translate>
                                ITEM_TYPE
                            </th>
                            <th class="r-item-name col-width-250 sticky-col sticky-actions-col" style="z-index: 12"
                                translate>ITEM_NAME
                            </th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="text-align: center" translate>REVISION</th>
                            <th style="" translate>LIFE_CYCLE_PHASE</th>
                            <th style="text-align: center" translate>QUANTITY</th>
                            <th style="text-align: center" translate>UNITS</th>
                            <th class="text-center"
                                ng-repeat="selectedAttribute in itemBomVm.selectedBomRollUpAttributes">
                                <span ng-if="$index%2 == 0" style="margin:0;padding: 0 5px;">
                                    {{unitTitle}} {{selectedAttribute.name}}
                                    <span ng-if="selectedAttribute.measurementUnit != null">( {{selectedAttribute.measurementUnit.symbol}} )</span>
                                </span>

                                <span ng-if="$index%2 != 0" style="margin: 0;">{{totalTitle}}
                                    {{selectedAttribute.name}}
                                    <span ng-if="selectedAttribute.measurementUnit != null">( {{selectedAttribute.measurementUnit.symbol}} )</span>
                                </span>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="reportItem in itemBomVm.bomRollupItems | limitTo:bollupLimit"
                            ng-class="{'bold-item':reportItem.bomItem.hasBom}">
                            <td class="r-item-number sticky-col sticky-actions-col" style="z-index: 11">
                                <span class="level{{reportItem.bomItem.level}}">
                                    <i class="mr5 fa" ng-if="reportItem.bomItem.hasBom" ng-hide="$index == 0"
                                    <%--ng-click="itemBomVm.toggleRollUpItem(reportItem)" title="{{itemBomVm.ExpandCollapse}}"--%>
                                       style="cursor: pointer;"
                                       ng-class="{'fa-caret-right': (reportItem.expanded == false || reportItem.expanded == null || reportItem.expanded == undefined),
                                                  'fa-caret-down': reportItem.expanded == true}"></i>
                                    <span>{{reportItem.bomItem.itemNumber}}</span>
                                </span>
                            </td>
                            <td class="r-item-type sticky-col sticky-actions-col" style="z-index: 11"><span>{{reportItem.bomItem.itemTypeName}}</span>
                            </td>
                            <td class="r-item-name col-width-250 sticky-col sticky-actions-col" style="z-index: 11">
                                <span>{{reportItem.bomItem.itemName}}</span></td>
                            <td class="col-width-250"><span>{{reportItem.bomItem.description}}</span></td>
                            <td class="text-center">
                                <span>{{reportItem.bomItem.revision}}</span>
                            </td>
                            <td>
                                <item-status item="reportItem.bomItem"></item-status>
                            </td>
                            <td style="text-align: center;"><span>{{reportItem.bomItem.quantity}}</span></td>
                            <td class="text-center"><span>{{reportItem.bomItem.units}}</span></td>
                            <td class="text-center" ng-repeat="attribute in reportItem.bomRollUpAttributes">
                                <span ng-if="$index%2 == 0" style="margin: 0;">
                                    <span ng-hide="reportItem.bomItem.item == item.id">
                                        <span ng-if="attribute.rollUpValue > 0 || attribute.approximated">
                                            <span ng-if="reportItem.bomItem.hasBom">{{attribute.rollUpValue}}</span>
                                            <span ng-if="!reportItem.bomItem.hasBom">{{attribute.actualValue}}</span> {{attribute.unitSymbol}}
                                            <span ng-if="reportItem.bomItem.hasBom && attribute.rollUpValue == 0 && attribute.approximated">~</span>
                                        </span>
                                    </span>
                                </span>

                                <span ng-if="$index%2 != 0" style="margin: 0;text-align: center;">
                                        <span ng-if="attribute.multipliedValue > 0 || attribute.approximated">
                                            <span ng-if="reportItem.bomItem.hasBom">{{attribute.multipliedValue}}</span>
                                            <span ng-if="!reportItem.bomItem.hasBom">{{attribute.actualMultipliedValue}}</span>{{attribute.unitSymbol}}
                                            <span ng-if="reportItem.bomItem.hasBom && attribute.multipliedValue == 0 && attribute.approximated">~</span>
                                        </span>
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div id="bom-whereUsedReport" class="bom-model modal">
        <style scoped>
            .bomWhereUsed-content .ui-select-multiple.ui-select-bootstrap .ui-select-match-item > span {

            }

            .bomWhereUsed-content .ui-select-multiple.ui-select-bootstrap input.ui-select-search {
                height: 30px;
            }

            .bomWhereUsed-content .ui-select-container .ui-select-match {
                left: 10px;
                position: absolute;
            }

            .bomWhereUsed-content .ui-select-container .ui-select-match .btn {
                /*height: 27px !important;*/
            }

            .bomWhereUsed-content .ui-select-container .ui-select-search {
                width: 100% !important;
                padding-left: 10px !important;
            }

            .bomWhereUsed-content i.clear-search {
                margin-left: -27px;
                color: gray;
                cursor: pointer;
                position: absolute;
                right: 22px;
                top: 11px;
            }

            .node {
                cursor: pointer;
            }

            .node circle {
                fill: #fff;
                stroke: steelblue;
                stroke-width: 1.5px;
            }

            text {
                word-wrap: break-word !important;
                min-width: 150px;
                width: 150px !important;
                white-space: normal !important;
                text-align: left;
            }

            .node text {
                font-size: 14px;
            }

            .link {
                fill: none;
                stroke: #ccc;
                stroke-width: 1.5px;
            }

            .found {
                fill: #ff4136;
                stroke: #ff4136;
            }

            ul.select2-results {
                max-height: 100px;
            }

            /*svg{
                width: 100%;
                height: 100%;
            }*/


        </style>
        <div class="bomWhereUsed-content">
            <style scoped>
                .ui-select-choices-group {
                    max-height: 500px;
                    overflow-y: auto;
                }

                .ui-select-container.open .ui-select-choices-group {
                    position: absolute;
                    width: calc(100% + 4px);
                    z-index: 99;
                    background-color: #fff;
                    left: -2px;
                    border: 1px solid #ddd;
                }

                .bomWhereUsed-content .ui-select-container .ui-select-match {
                    text-align: left;
                    display: block;
                }

                .bomWhereUsed-content .ui-select-container .ui-select-match {
                    position: relative;
                }
            </style>
            <div class="bom-header">
                <div class="btn-group pull-left" style="margin: 2px 0 0 10px !important;"
                     ng-if="itemBomVm.whereUsedTableView">
                    <button type="button" class="btn btn-sm btn-primary dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="mr5" translate>EXPORT</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li ng-click="itemBomVm.exportWhereUsedReport('CSV')"><a href="">CSV</a></li>
                        <li ng-click="itemBomVm.exportWhereUsedReport('EXCEL')"><a href="">Excel</a></li>
                        <li ng-click="itemBomVm.exportWhereUsedReport('HTML')"><a href="">Html</a></li>
                    </ul>
                </div>
                <span class="configuration-header">BOM Where Used Report ( {{item.itemName}} )</span>
                <a href="" ng-click="hideBomWhereUsedReport()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="bom-content" style="padding: 0;overflow: hidden;">
                <div id="item-selection" style="display: flex;width: 100%;border-bottom: 1px solid #ddd;">
                    <div class="form-group" style="width: 300px;margin-bottom: 0;text-align: center;">
                        <div class="col-sm-12" style="margin: 6px 0 0 0 !important;">

                            <div class="form-check" style="line-height: 30px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios1"
                                           ng-click="selectWhereUsedView('table')" checked>
                                    <span style="padding: 2px;margin-left: 5px;" translate>Table View</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios2"
                                           ng-click="selectWhereUsedView('graphical')"><span
                                        style="padding: 2px;margin-left: 5px;" translate>Graphical View</span>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" style="margin: 0;padding: 5px;width: calc(100% - 300px);">
                        <label class="col-sm-1 control-label" style="text-align: right;line-height: 32px;padding: 0">
                            <span translate>Select Item</span> : </label>

                        <div class="col-sm-11"
                             ng-class="{'no-where-used-selected': itemBomVm.selectedWhereUsedItems.length == 0}">
                            <ui-select multiple ng-model="itemBomVm.selectedWhereUsedItems" theme="bootstrap"
                                       on-select="onBomTreeSearch()"
                                       close-on-select="false" title="" remove-selected="true"
                                       on-remove="onRemoveSearch($item)">
                                <ui-select-match placeholder="Select Item">[{{$item.itemNumber}}] {{$item.itemName}}
                                    ({{$item.count}})
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="item in bomTreeSearchItems | orderBy:'itemName' | filter: $select.search">
                                    <div>[{{item.itemNumber}}] {{item.itemName}} ( {{item.count}} )</div>
                                </ui-select-choices>
                            </ui-select>
                            <i class="fa fa-times-circle clear-search"
                               ng-show="itemBomVm.selectedWhereUsedItems.length > 0"
                               ng-click="clearSelections()"></i>
                        </div>
                        <%--<div class="col-sm-2">
                            <button class="btn btn-xs btn-success"
                                    style="height: 35px;"
                                    ng-disabled="itemBomVm.selectedWhereUsedItems.length == 0"
                                    ng-click="searchWhereUsedReport()">Search
                            </button>
                        </div>--%>
                    </div>
                </div>
                <div id="whereUsed-height" class="whereUsed-height"
                     infinite-scroll infinite-scroll-callback-fn="increaseWhereUsedLimit()"
                     infinite-scroll-percentage="80">
                    <table class='table table-striped highlight-row' ng-show="itemBomVm.whereUsedTableView">
                        <thead>
                        <tr>
                            <th class="" translate>ITEM_NUMBER</th>
                            <th translate>ITEM_TYPE</th>
                            <th class="col-width-250" translate>ITEM_NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="text-align: center" translate>REVISION</th>
                            <th style="" translate>LIFE_CYCLE_PHASE</th>
                            <th style="text-align: center" translate>QUANTITY</th>
                            <th style="text-align: center" translate>UNITS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="bomItem in itemBomVm.bomWhereUsedItems | limitTo:whereUsedLimit"
                            ng-class="{'bold-item':hasSelectedItems(bomItem) == true}">
                            <td>
                                <span class="level{{bomItem.level}}">
                                    <i class="mr5 fa" ng-if="bomItem.hasBom" ng-hide="$index == 0"
                                    <%--ng-click="itemBomVm.toggleRollUpItem(reportItem)" title="{{itemBomVm.ExpandCollapse}}"--%>
                                       style="cursor: pointer;"
                                       ng-class="{'fa-caret-right': (bomItem.expanded == false || bomItem.expanded == null || bomItem.expanded == undefined),
                                                  'fa-caret-down': bomItem.expanded == true}"></i>
                                    <span>{{bomItem.itemNumber}}</span>
                                </span>
                            </td>
                            <td><span>{{bomItem.itemTypeName}}</span></td>
                            <td title="{{bomItem.itemName}}"><span>{{bomItem.itemName}}</span>
                            </td>
                            <td class="col-width-250" title="{{bomItem.description}}">
                                <span>{{bomItem.description}}</span>
                            </td>
                            <td class="text-center">
                                <span>{{bomItem.revision}}</span>
                            </td>
                            <td>
                                <item-status item="bomItem"></item-status>
                            </td>
                            <td style="text-align: center;"><span>{{bomItem.quantity}}</span></td>
                            <td class="text-center"><span>{{bomItem.units}}</span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div id="bom-complianceReport" class="bom-model modal">
        <style scoped>

        </style>
        <div class="bomCompliance-content">
            <div class="bom-header">
                <span class="configuration-header">BOM Compliance Report ( {{item.itemName}} )</span>
                <a href="" ng-click="hideBomComplianceReport()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="bom-content" style="padding: 0;overflow: hidden;">
                <div id="compliance-height" class="compliance-height">
                    <table class='table table-striped highlight-row'>
                        <thead>
                        <tr>
                            <th class="col-width-150" translate>ITEM_NUMBER</th>
                            <th class="col-width-200" translate>ITEM_NAME</th>
                            <th class="col-width-200" translate>MFR_PART</th>
                            <th class="col-width-150" translate>MANUFACTURER_NAME</th>
                            <th class="col-width-150" style="text-align: center !important;"
                                ng-repeat="specification in itemBomVm.itemSpecifications">
                                {{specification.name}}
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="bomItem in itemBomVm.bomComplianceItems">
                            <td class="col-width-150">
                                <span class="level{{bomItem.level}}">
                                    <i class="mr5 fa" ng-if="bomItem.hasBom" ng-hide="$index == 0"
                                       style="cursor: pointer;" ng-click="itemBomVm.toggleComplianceItem(bomItem)"
                                       ng-class="{'fa-caret-right': (bomItem.expanded == false || bomItem.expanded == null || bomItem.expanded == undefined),
                                                  'fa-caret-down': bomItem.expanded == true}"></i>
                                    <span>{{bomItem.itemNumber}}</span>
                                    <span class="label label-default label-count" ng-if="bomItem.children.length > 0"
                                          title=""
                                          style="font-size: 12px;background-color: #e4dddd;font-style: italic;"
                                          ng-bind-html="bomItem.children.length"></span>
                                </span>
                            </td>
                            <td class="col-width-200">
                                <span>{{bomItem.itemName}}</span>
                            </td>
                            <td class="col-width-200">
                                <span ng-if="!bomItem.hasBom">{{bomItem.partNumber}} - {{bomItem.partName}}</span>
                            </td>
                            <td class="col-width-150">
                                <span ng-if="!bomItem.hasBom">{{bomItem.mfrName}}</span>
                            </td>
                            <td class="col-width-150" style="text-align: center !important;"
                                ng-repeat="specification in bomItem.specifications">
                                <a class="icon fa fa-check" style="font-size: 16px;color: #1CAF9A"
                                   title="Compliant / Show substance values"
                                   ng-if="!bomItem.hasBom && bomItem.requireCompliance && specification.compliant"
                                   uib-popover-template="itemBomVm.substancePopover.templateUrl"
                                   popover-append-to-body="true"
                                   popover-popup-delay="50"
                                   popover-placement="top-right"
                                   popover-title="({{bomItem.partNumber}} - {{bomItem.partName}}) Substances"
                                   popover-trigger="'outsideClick'">
                                </a>
                                <%--<span ng-if="!bomItem.hasBom && bomItem.requireCompliance && specification.compliant">
                                    <i class="fa fa-check" title="Compliant" style="font-size: 16px;color: #1CAF9A"></i>
                                </span>--%>
                                <a class="icon fa fa-times" style="font-size: 16px;color: #d9534f"
                                   title="Non Compliant / Show substance values"
                                   ng-if="!bomItem.hasBom && bomItem.requireCompliance && !specification.compliant && !specification.exempt"
                                   uib-popover-template="itemBomVm.substancePopover.templateUrl"
                                   popover-append-to-body="true"
                                   popover-popup-delay="50"
                                   popover-placement="top-right"
                                   popover-title="({{bomItem.partNumber}} - {{bomItem.partName}}) Substances"
                                   popover-trigger="'outsideClick'">
                                </a>
                                <%--<span ng-if="!bomItem.hasBom && bomItem.requireCompliance && !specification.compliant">
                                    <i class="fa fa-times" style="font-size: 16px;color: #d9534f"
                                       title="Non Compliant"></i>
                                </span>--%>
                                <span ng-if="!bomItem.hasBom && bomItem.requireCompliance && !specification.compliant && specification.exempt">
                                    <%--<span class="label label-primary">EXEMPT</span>--%>
                                    <i class="fa fa-minus" style="font-size: 16px;" title="Exempt"></i>
                                </span>
                                <span ng-if="!bomItem.hasBom && !bomItem.requireCompliance">
                                    <%--<span class="label label-primary">EXEMPT</span>--%>
                                    <i class="fa fa-minus" style="font-size: 16px;" title="Exempt"></i>
                                </span>
                                <span ng-if="bomItem.hasBom && specification.compliant">
                                    <%--<span class="label label-success">COMPLIANT</span>--%>
                                <i class="fa fa-check" title="Compliant" style="font-size: 16px;color: #1CAF9A"></i>
                                </span>
                                <span ng-if="bomItem.hasBom && !specification.compliant">
                                    <%--<span class="label label-danger">NON COMPLIANT</span>--%>
                                <i class="fa fa-times" style="font-size: 16px;color: #d9534f"
                                   title="Non Compliant"></i>
                                </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>


    <div id="bom-item-richText" class="bom-item-model modal">
        <div class="bom-item-content">
            <div class="bom-item-header">
                <span class="procedure-header" translate>{{itemBomVm.bomItemAttribute.name}}</span>
                <a href="" ng-click="hideRichTextDialog()" class="bom-item-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="procedure-content" style="padding: 10px">
                <div ng-if="itemBomVm.attributeBomItem.editMode">
                    <summernote ng-model="itemBomVm.bomItemRichText.oldRichTextValue"></summernote>
                </div>
                <span ng-if="!itemBomVm.attributeBomItem.editMode && itemBomVm.bomItemRichText.encodedRichText != null"
                      ng-bind-html="itemBomVm.bomItemRichText.encodedRichText">
                </span>
            </div>
        </div>
    </div>

    <div id="configuration-editor" class="bom-model modal">
        <style scoped>
            .tree-node .tree-icon, .folders-pane .tree-node .tree-indent {
                vertical-align: middle;
            }

            .tree-node {
                height: 24px !important;
            }

            .tree-node .tree-title {
                height: 24px;
                line-height: 22px;
                font-size: 14px;
            }

            .tree-node-icon-bom-configeditor {
                background: transparent url("/app/assets/images/bom-config-editor.png") no-repeat !important;
            }

            .tree-node-icon-bomrules {
                background: transparent url("/app/assets/images/bom-inclusions.png") no-repeat !important;
            }

            .tree-node-icon-bomexcl {
                background: transparent url("/app/assets/images/bom-attribute-exclusions.png") no-repeat !important;
            }

            .tree-node-icon-bomconfigs {
                background: transparent url("/app/assets/images/bom-configurations.png") no-repeat !important;
            }

            .tree-node-icon-configatts {
                background: transparent url("/app/assets/images/config-atts.png") no-repeat !important;
            }

            .colorExclude {
                background-color: #f4b7c8;
            }

            .configurationEditor-content .lov-header > span {
                font-weight: bold;
                font-size: 16px;
                color: #337ab7 !important;
            }

            .configurationEditor-content .attribute-table .att-name {
                font-weight: bold;
                font-size: 16px;
                color: #337ab7 !important;
            }

            .configurationEditor-content .attribute-table .item-header.att-name {
                font-weight: bold;
                font-size: 18px;
                color: #1CAF9A !important;
            }

            .configurationEditor-content .editor-content .blue-header {
                font-size: 16px;
                color: #337ab7 !important;
            }

            .configurationEditor-content .bom-configuration .bom-footer input.bom-config-name,
            .configurationEditor-content .bom-configuration .bom-footer input.bom-config-description {
                padding: 5px 10px;
                height: auto;
                font-weight: 700 !important;
                font-size: 14px !important;
            }
        </style>
        <div class="configurationEditor-content">
            <div class="bom-header">
                <span class="configuration-header">{{bomConfigurationEditorTitle}}</span>
                <a href="" ng-click="hideConfigurationEditor()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div id="configuration-error" class="alert {{itemBomVm.notificationBackground}} animated">
                <span style="margin-right: 10px;"><i class="fa {{itemBomVm.notificationClass}}"></i></span>
                <a href="" class="config-close-btn" ng-click="itemBomVm.closeErrorNotification()"></a>
                {{itemBomVm.message}}
            </div>
            <div class="editor-content">
                <div class="editor-left">
                    <div id="contextMenu" class="context-menu dropdown clearfix"
                         style="position: fixed;display:none; z-index: 9999">
                        <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="margin-top: -25px">
                            <li><a tabindex="-1" id="addType" href="" ng-click="itemBomVm.newBomConfiguration()">
                                <span>{{newBomConfigurationTitle}}</span></a>
                            </li>
                        </ul>
                    </div>
                    <div id="classificationContainer" class="classification-pane" data-toggle="context"
                         data-target="#context-menu">
                        <ul id="configurationEditor" class="easyui-tree"></ul>
                    </div>
                </div>
                <div class="editor-right">
                    <div style="margin: 20px;" ng-if="itemBomVm.selectConfigEditorNode.nodeType == 'CONFIGURATIONS'">
                        <span>{{editorConfigurationsTitle}}</span>
                    </div>
                    <div style="height: 100%;" class="bom-configuration"
                         ng-if="itemBomVm.loading == false && (itemBomVm.selectConfigEditorNode.nodeType == 'CONFIGURATION' || itemBomVm.selectConfigEditorNode.nodeType == 'NEW_BOM_CONFIGURATION')">
                        <div class="bom-footer">
                            <div class="form-group" style="width: 40%;">
                                <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                                    <span translate>NAME</span>
                                    <span class="asterisk">*</span> : </label>

                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm bom-config-name"
                                           placeholder="{{enterName}}"
                                           ng-model="itemBomVm.bomConfiguration.name">
                                </div>
                            </div>
                            <div class="form-group" style="width: 40%;">
                                <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                                    <span translate>DESCRIPTION</span> : </label>

                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm bom-config-description"
                                           placeholder="{{enterDescription}}"
                                           ng-model="itemBomVm.bomConfiguration.description">
                                </div>
                            </div>
                            <div class="form-group" style="width: 10%;text-align: right;margin-top: 3px;">
                                <button class="btn btn-sm btn-success" style="width: 75px;"
                                        ng-click="itemBomVm.saveBomConfiguration()"
                                        translate>
                                    SAVE
                                </button>
                            </div>
                        </div>
                        <div class="editor-configuration-content">
                            <div class="bom-headers">
                                <div class="bom-header-column blue-header" translate>ITEM_NUMBER</div>
                                <div class="bom-header-column blue-header" translate>ITEM_NAME</div>
                                <div class="bom-header-column">
                                    <button class="btn btn-xs btn-warning" ng-if="itemBomVm.showClear"
                                            ng-click="itemBomVm.loadBomModal()" translate>CLEAR_ALL
                                    </button>
                                </div>
                            </div>
                            <div class="config-row" ng-repeat="modal in itemBomVm.modalBom">
                                <div class="bom-value-column" ng-class="{'bold-item':$index == 0}">
                                    <span style="vertical-align: sub;">
                                        <span class="level{{modal.level}}"></span>{{modal.item.itemNumber}}
                                    </span>
                                </div>
                                <div class="bom-value-column" ng-class="{'bold-item':$index == 0}"
                                     title="{{modal.item.itemName}}">
                                    <span style="vertical-align: sub;">{{modal.item.itemName  }}{{modal.item.itemName.length > 23 ? '...' : ''}}</span>
                                </div>
                                <div class="bom-value-column" ng-repeat="configAttribute in modal.attributes">
                                    <ui-select ng-model="configAttribute.listValue" theme="bootstrap"
                                               style="width:97%" on-select="itemBomVm.getBomValidationRules(modal)"
                                               title="{{configAttribute.itemAttribute.attribute.name}} ( {{configAttribute.listValue}} )">
                                        <ui-select-match
                                                placeholder="Select {{configAttribute.itemAttribute.attribute.name}}{{configAttribute.itemAttribute.attribute.name.length > 15 ? '..' : ''}}">
                                        <span style="width:auto;display: flex;">{{configAttribute.itemAttribute.attribute.name}} (
                                        <span style="width: auto;font-weight: bold;padding: 0 2px;">{{$select.selected}}</span> )</span>
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="value in configAttribute.itemAttribute.values">
                                            <div ng-bind-html="value"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div style="height: 100%;overflow: auto;"
                         ng-if="itemBomVm.loading == false && itemBomVm.selectConfigEditorNode.nodeType == 'EXCLUSION_RULES'">
                        <div style="text-align: center;" ng-if="itemBomVm.finalValues.length > 0">
                            <p class="text-success" style="line-height: 25px;height: 25px;">
                                {{itemBomVm.exclSaveMessage}}</p>
                        </div>
                        <p ng-if="itemBomVm.finalValues.length == 0" style="padding: 10px;" translate>
                            NO_CONFIGURABLE_ATTRIBUTES</p>

                        <div class="col-sm-12" style="display: inline !important;height: 100%;"
                             ng-if="itemBomVm.finalValues.length > 0">
                            <div class="row" style="height: 100%;">
                                <table class="attribute-table" style="margin: 15px auto !important;">
                                    <thead>
                                    <tr>
                                        <th rowspan="2" colspan="2"></th>
                                        <th class="att-name" ng-repeat="header in itemBomVm.nameHeaders"
                                            style=""
                                            colspan="{{header.numValues}}">
                                            {{header.name}}
                                        </th>
                                    </tr>
                                    <tr>
                                        <th class="vertical"
                                            ng-repeat="value in itemBomVm.finalValues track by $index">
                                            <div class="vertical" style=""
                                                 title="{{value.value}}">
                                                <span>{{value.value}}</span>

                                            </div>
                                        </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="val in itemBomVm.finalValues track by $index"
                                        ng-init="pair = itemBomVm.shouldAddAttributeRowHeader($index)">
                                        <td class="att-name" rowspan="{{pair[1]}}" ng-if="pair[1] > 0"
                                            style="font-weight: bold !important;">{{pair[0]}}
                                        </td>
                                        <td class="att-value" style="font-weight: bold;"
                                            title="{{val.value}}">
                                            {{val.value}}

                                        </td>
                                        <td ng-repeat="val1 in itemBomVm.finalValues track by $index"
                                            class="sameWidth" style="cursor: pointer;"
                                            ng-init="columnIndex = $index;title1 = val1.key+'('+val.value+'-'+val1.value+')'"
                                            ng-click="val.key == val1.key || itemBomVm.createExclusionObj(val,$index,val1,columnIndex)"
                                            ng-disabled="val.key == val1.key"
                                            ng-attr-title="{{ itemAttributesExclusions.indexOf(title1) == -1 ? title1 : 'Combination already in use we can not exclude'}}"
                                            ng-class="[itemBomVm.checkExclude(val,val1),checkBomAttributeInclusion(val,val1), {'myOwnBg': val.key == val1.key},{'unselectable': itemAttributesExclusions.indexOf(title1) != -1}]">
                                            <span ng-if="val.key != val1.key"
                                            <%--title="{{val1.key}}({{val.value}}-{{val1.value}})"--%>>
                                                {{val.value | limitTo:1}}{{val1.value | limitTo: 1}}</span>

                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div style="height: 100%"
                         ng-if="itemBomVm.loading == false && itemBomVm.selectConfigEditorNode.nodeType == 'INCLUSION_RULES'">
                        <div id="inclusion-buttons" style="display: inline-block;padding:10px;width: 100%">
                            <div class="col-md-3">
                                <button class="btn btn-xs btn-primary" ng-click="inclusionAll()"
                                        translate>
                                    ADD_ALL_INCLUSIONS
                                </button>
                                <button class="btn btn-xs btn-warning"
                                        ng-click="resetAllInclusions()"
                                        translate>
                                    CLEAR_ALL
                                </button>
                            </div>
                            <div class="col-md-9" style="max-height: 25px">
                                <p style="font-size: 15px;height: 25px;" class="text-success"
                                   ng-if="inclSaveMessage != '' && inclSaveMessage != null">
                                    {{inclSaveMessage}}</p>

                                <p style="font-size: 15px;height: 25px;" class="text-warning"
                                   ng-if="errorSaveMessage != '' && errorSaveMessage != null">
                                    {{errorSaveMessage}}</p>
                            </div>
                        </div>
                        <div id="inclusions-content" class="row bomRuleexclusionBody" style="margin-bottom: 20px;">
                            <div class="col-sm-12" style="display: inline !important;">
                                <div class="row">
                                    <div class="" id="container" style="padding-bottom: 30px;">
                                        <table class="attribute-table" style="margin: 0 auto !important;">
                                            <thead>
                                            <tr>
                                                <th colspan="3" rowspan="4"></th>

                                            </tr>
                                            <tr>
                                                <th class="item-header att-name"
                                                    ng-repeat="header1 in childrenItemHeaders"
                                                    style="text-align: center !important;"
                                                    colspan="{{header1.length}}" title="{{header1.name}}">
                                                    {{header1.name }}{{header1.name.length > 25 ? '...' :
                                                    ''}}
                                                </th>
                                            </tr>
                                            <tr>

                                                <th class="att-header att-name" ng-repeat="header in nameHeaders"
                                                    style=""
                                                    colspan="{{header.numValues}}" title="{{header.name}}">
                                                    {{header.name}}
                                                </th>
                                            </tr>
                                            <tr>
                                                <th class="vertical" ng-repeat="value in finalValuesForChildren">
                                                    <div class="vertical" style=""
                                                         title="{{value.value}}">
                                                        <span ng-if="value.configurable">{{value.value}}</span>
                                                    </div>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td class="item-header att-value" style="font-weight: bold;"
                                                    colspan="1" rowspan="{{finalValuesForParent.length+1}}"
                                                    title="{{parentItemObj.itemName}}">
                                                    {{parentItemObj.itemName}}
                                                </td>
                                            </tr>
                                            <tr ng-repeat="val in finalValuesForParent track by $index"
                                                ng-init="pair = shouldAddRowHeader($index)">
                                                <td class="att-header att-name" rowspan="{{pair[1]}}"
                                                    ng-if="pair[1] > 0"
                                                    style="font-weight: bold !important;"
                                                    title="{{pair[0]}}"> {{pair[0] |
                                                    limitTo: 10 }}{{pair[0].length > 10 ? '...' : ''}}
                                                <td class="att-value" style="font-weight: bold;"
                                                    title="{{val.value}}">
                                                    {{val.value}}
                                                </td>
                                                <td ng-repeat="val1 in finalValuesForChildren track by $index"
                                                    class="sameWidth"
                                                    style="cursor: pointer;"
                                                    ng-init="title1 = val.itemName+' '+val.key+'('+val.value+') - '+val1.itemName+' '+val1.key+'('+val1.value+')'"
                                                    ng-attr-title="{{ bomConfigCombinations.indexOf(title1) == -1 ? title1 : 'Combination already in use we can not un-check'}}"
                                                    ng-click="createBomInlcusionExclusionObj(val,val1)"
                                                    ng-class="[checkBomInclusion(val,val1), {'unselectable': bomConfigCombinations.indexOf(title1) != -1 || val1.itemName == null}]">
                                                    <span ng-if="val1.configurable && val1.itemName != null">
                                                        {{val.value| limitTo:1}}{{val1.value| limitTo: 1}}
                                                    </span>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div style="height: 100%;overflow: auto;"
                         ng-if="itemBomVm.loading == false && itemBomVm.selectConfigEditorNode.nodeType == 'NON_CONFIGURABLE_RULES'">
                        <div id="non-configurable-buttons" style="display: inline-block;padding:10px;width: 100%">
                            <div class="col-md-12" style="max-height: 25px">
                                <p style="font-size: 15px;height: 25px;" class="text-success text-center"
                                   ng-if="inclSaveMessage != '' && inclSaveMessage != null">
                                    {{inclSaveMessage}}</p>

                                <p style="font-size: 15px;height: 25px;" class="text-warning text-center"
                                   ng-if="errorSaveMessage != '' && errorSaveMessage != null">
                                    {{errorSaveMessage}}</p>
                            </div>
                        </div>
                        <div id="non-configurable-content" class="row bomRuleexclusionBody"
                             style="margin-bottom: 20px;">
                            <div class="col-sm-12" style="display: inline !important;">
                                <div class="row">
                                    <div class="" style="padding-bottom: 30px;">
                                        <table class="attribute-table" style="margin: 0 auto !important;">
                                            <thead>
                                            <tr>
                                                <th colspan="1" rowspan="4"></th>
                                            </tr>
                                            <tr>
                                                <th class="item-header att-name"
                                                    style="text-align: center !important;"
                                                    colspan="{{parentItemObj.length}}" title="{{parentItemObj.name}}">
                                                    {{parentItemObj.itemName
                                                    }}{{parentItemObj.itemName.length > 20 ?
                                                    '...' : ''}}
                                                </th>
                                            </tr>
                                            <tr>
                                                <th class="att-header att-name" ng-repeat="header in nameHeaders"
                                                    style=""
                                                    colspan="{{header.numValues}}" title="{{header.name}}">
                                                    {{header.name | limitTo: 10 }}{{header.name.length > 10 ? '...' :
                                                    ''}}
                                                </th>
                                            </tr>
                                            <tr>
                                                <th class="vertical" ng-repeat="value in finalValuesForChildren">
                                                    <div class="vertical" style=""
                                                         title="{{value.value}}">
                                                        <span>{{value.value}}</span>
                                                    </div>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr ng-repeat="val in finalValuesForParent track by $index">
                                                <td class="item-header att-value" style="font-weight: bold;">
                                                    {{val.itemName}}
                                                </td>
                                                <td ng-repeat="val1 in finalValuesForChildren track by $index"
                                                    class="sameWidth"
                                                    style="cursor: pointer;"
                                                    ng-init="title1 = val1.itemName+' '+val1.key+'('+val1.value+') - '+val.itemName"
                                                    ng-attr-title="{{bomConfigCombinations.indexOf(title1) == -1 ? title1 : 'Combination already in use we can not un-check'}}"
                                                    ng-click="createBomInlcusionExclusionObj(val1,val)"
                                                    ng-class="[checkBomItemIncludedCombination(val1,val)]">
                                                    <span ng-if="val1.configurable && val1.itemName != null">
                                                        {{val.value| limitTo:1}}{{val1.value| limitTo: 1}}
                                                    </span>
                                                    <i class="fa fa-check" ng-if="!val1.configurable"
                                                       ng-show="checkBomItemIncluded(val1,val)"></i>
                                                    <i class="fa fa-times" ng-if="!val1.configurable"
                                                       ng-show="!checkBomItemIncluded(val1,val)"></i>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div style="height: 100%;overflow: auto;"
                         ng-if="itemBomVm.loading == false && itemBomVm.selectConfigEditorNode.nodeType == 'ATTRIBUTES'">
                        <style scoped>
                            .flex-row {
                                display: -webkit-box;
                                display: -moz-box;
                                display: -ms-flexbox;
                                display: -webkit-flex;
                                display: flex;
                                margin: 10px 0;
                                flex-wrap: wrap;
                            }

                            .flex-row-center {
                                justify-content: center;
                            }

                            .flex-row > .flex-col {
                                margin-left: 10px;
                            }

                            .lov-container {
                                width: 250px;
                                min-width: 250px;
                                border: 1px solid #ddd;
                                border-radius: 5px;
                                margin-bottom: 10px;
                            }

                            .lov-container > .lov-header {
                                padding: 10px;
                                border-bottom: 1px solid #ddd;
                                display: flex;
                                word-break: break-all;
                            }

                            .lov-container > .lov-header > h5 {
                                margin: 0 !important;
                                font-size: 16px;
                                display: inline-block;
                                font-weight: 600;
                            }

                            .lov-container > .lov-header input {
                                font-size: 16px;
                            }

                            .lov-container > .lov-header > input {
                                flex-grow: 1;
                            }

                            .lov-container > .lov-header > .buttons {
                                display: inline-block;
                                flex-grow: 1;
                                text-align: right;
                            }

                            .lov-container .buttons i:hover {
                                color: #0390fd;
                                cursor: pointer;
                            }

                            .lov-container > .lov-body {
                                padding: 10px;
                                height: 200px;
                                max-height: 200px;
                                overflow-y: auto;
                            }

                            .lov-container .lov-body .lov-value {
                                cursor: pointer;
                            }

                            .lov-container > .lov-body > .lov-value > div {
                                border-bottom: 1px dotted #e3e3e3;
                                padding: 5px;
                                display: flex;
                                justify-content: left;
                            }

                            .lov-container .lov-body .lov-value .name,
                            .lov-container .lov-body .lov-value .type {
                                flex-grow: 1;
                                justify-content: left;
                                text-align: left;
                            }

                            .lov-container .lov-body .lov-value .buttons {
                                visibility: hidden;
                                text-align: right;
                            }

                            .lov-container .lov-body .lov-value:hover .buttons {
                                visibility: visible;
                            }

                            .lov-container .lov-body .lov-value.edit-mode .buttons {
                                visibility: visible;
                            }

                            .lov-container input {
                                border: 0;
                                outline: 0;
                                background: transparent;
                                border-bottom: 1px dashed lightgrey;
                            }

                            .lov-container .lov-body .lov-value.edit-mode .type {
                                border-bottom: 1px dashed lightgrey;
                                margin-right: 10px;
                                text-align: left;
                            }

                            .lov-container .lov-body .lov-value .type a {
                                text-decoration: none;
                                color: inherit;
                            }

                            .lov-container .lov-body .lov-value .type .caret {
                                border-top: 5px dashed;
                                border-right: 5px solid transparent;
                                border-left: 5px solid transparent;
                            }

                            .lov-container .lov-body .lov-value:hover {
                                background-color: #0081c2;
                                color: #fff;
                            }

                            .lov-container .lov-body .lov-value.edit-mode {
                                background-color: transparent;
                                color: inherit;
                            }

                            .lov-container .lov-body .lov-value:hover .buttons i {
                                cursor: pointer;
                            }

                            .lov-container .lov-body .lov-value:hover .buttons i {
                                color: #fff;
                            }

                            .lov-container .lov-body .lov-value.edit-mode .buttons i:hover {
                                color: #0390fd;
                            }

                            .lov-container .lov-body .lov-value.edit-mode .buttons i {
                                color: inherit;
                            }

                            .lov-container .lov-body .lov-value .name input {
                                width: 98%;
                            }

                            #lovAddButton:hover i {
                                color: #fff !important;
                            }

                        </style>
                        <p ng-if="itemBomVm.itemAttributes.length== 0" style="padding: 10px;" translate>
                            NO_CONFIGURABLE_ATTRIBUTES</p>

                        <div class="flex-row flex-row-center">

                            <div id="lov{{$index}}" class="flex-col lov-container"
                                 ng-repeat="itemAttribute in itemBomVm.itemAttributes">
                                <div class="lov-header">
                                    <span title="{{itemBomVm.newLov.newName}}">
                                        {{itemAttribute.attribute.name  }}{{itemAttribute.attribute.name.length > 24 ? '...' : ''}}</span>

                                    <div class="buttons">
                                        <i title="{{addNewValueTitle}}"
                                           class="la la-plus mr10"
                                           ng-click="itemBomVm.addAttributeValue(itemAttribute)"></i>
                                    </div>
                                </div>
                                <div class="lov-body" id="lov-body{{$index}}">
                                    <div class="lov-value" ng-repeat="value in itemAttribute.valueObjects"
                                         ng-class="{'edit-mode': value.editMode}">
                                        <div ng-if="value.editMode" style="display: flex">
                                        <span class="name" style="flex-grow: 1">
                                            <input type="text"
                                                   ng-model="value.newString"
                                                   ng-enter="value.editMode = false;value.string = value.newString;itemBomVm.applyChangesList(value,itemAttribute)"
                                                   onfocus="this.setSelectionRange(0, this.value.length)">
                                        </span> 
                                        <span class="buttons">
                                            <i title="{{saveChangesTitle}}" class="la la-check mr5"
                                               ng-click="value.editMode = false;value.newMode = false;value.string = value.newString;itemBomVm.applyChangesList(value,itemAttribute)">
                                            </i>
                                            <i title="{{lovCancel}}"
                                               ng-click="value.editMode = false;value.newString = value.string; itemBomVm.cancelChangesList(value,itemAttribute)"
                                               class="la la-times"></i>
                                        </span>
                                        </div>

                                        <div ng-if="!value.editMode">
                                                <span class="name"
                                                      ng-dblclick="value.editMode = true">{{value.string}}</span>
                                                <span class="buttons">
                                                    <i title="{{'EDIT_VALUE' | translate}}" class="la la-edit"
                                                       ng-click="itemBomVm.toDeleteValue = null;value.editMode = true"></i>
                                                    <i title="{{deleteValueTitle}}"
                                                       ng-click="itemBomVm.toDeleteValue = value;itemBomVm.deleteAttributeValue(itemAttribute)"
                                                       class="la la-times"></i>
                                                </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>