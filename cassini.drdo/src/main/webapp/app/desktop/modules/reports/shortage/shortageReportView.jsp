<div class="view-container" fitcontent>
    <style scoped>
        .view-toolbar {
            padding-top: 5px;
            padding-right: 10px;
        }

        .view-content {
            position: relative;
        }

        .view-content .responsive-table {
            position: absolute;
            overflow: auto;
            height: 100%;
            padding: 10px;
        }

        .view-content .responsive-table table {
            table-layout: fixed;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
        }

        .search-box {
            width: 100%;
            margin: 0;
            float: right;
        }

        tr.disabled-reviewd td {
            background-color: red !important;
        }

        .search-box input {
            height: 34px !important;
        }

        .search-box button {
            margin-top: 3px !important;
            line-height: 0px;
            width: 75px;
            height: 34px;
            padding: 0px;
        }

        .searchResults {
            display: none;
            position: absolute;
            top: 37px;
            left: 0;
            width: 100%;
            border: 1px solid #ddd;
            z-index: 100;
            background-color: #fff;
            overflow-y: auto;
        }

        .searchResults .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 40px;
            top: 0;
            overflow: auto;
        }

        .searchResults table th, .searchResults table td {
            padding: 5px !important;
        }

        .searchResults .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .searchResults .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .searchResults .table-footer > div h5 {
            margin: 0;
        }

        .view-content .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .searchResults .table-footer > div > div > i {
            font-size: 16px;
        }

        .reports-info-panel {
            display: none;
            z-index: 9;
            width: 500px;
            position: absolute;
            top: 81px;
            left: 30px;
            background-color: #fff;
            bottom: 32px;
            border: 1px solid #ddd;
            overflow-y: auto;
        }

        .reports-info-panel .info-panel-header {
            border-bottom: 1px solid #ddd;
            padding-left: 10px;
        }

        .reports-info-panel .info-panel-header h3 {
            line-height: 50px;
            margin: 0;
        }

        .info-panel-header .close {
            position: absolute;
            right: 10px;
            top: 10px;
            width: 32px;
            line-height: 50px;
            height: 50px;
            padding-top: 7px;
            margin-right: -5px;
            opacity: 0.3;

        }

        .info-panel-header .close:hover {
            opacity: 1;
        }

        .info-panel-header .close:before, .info-panel-header .close:after {
            position: absolute;
            left: 15px;
            content: ' ';
            height: 15px;
            width: 2px;
            background-color: #333;
        }

        .info-panel-header .close:before {
            transform: rotate(45deg);
        }

        .info-panel-header .close:after {
            transform: rotate(-45deg);
        }

        .info-panel-details {
            padding: 10px;
        }

        tr.row-provisional td {
            background-color: #ec971f !important;
        }

        tr.row-returnBackPart td {
            background-color: #7f500e !important;
        }

        tr.row-revAndProv td {
            background-color: #9400D3 !important;
        }

        tr.required td {
            border-radius: 5px !important;
            border: #FF0000 3px solid !important;
        }

        .linkDisabled td {
            cursor: not-allowed !important;
            pointer-events: none !important;
            color: grey;
        }

        tr.row-highlight td {
            opacity: 4.5 !important;
            color: green !important;
        }

        .disabled-button {
            pointer-events: none;
        }

        tr.disabled-row td {
            opacity: 0.5;
            color: #c06f1d;
            pointer-events: none;
        }

        tr.disabled-reviewd td {
            color: green;
            background: red;
        }

        .disabled {
            color: #717782;
            pointer-events: none;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .dropDown-width {
            width: 13%;
            max-width: 16%;
            padding: 0 5px;
        }

        .badge {
            font-size: 14px !important;
        }

        #freeTextSearchDirective {
            width: 200px !important;
            top: 35px !important;
        }

        .search-box > input {
            width: 100% !important;
        }

        .view-toolbar .ui-select-bootstrap .ui-select-toggle > a.btn {
            position: absolute;
            height: 29px !important;
            right: 10px;
            margin-top: -5px;
            padding: 5px !important;
        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            max-height: 400px !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
            right: 0 !important;
        }
    </style>
    <div class="view-toolbar">
        <div class="row" style="display: flex;">

            <div style="width:10%;padding:0 5px;">
                <ui-select ng-model="shortRepVm.selectedBom" theme="bootstrap" style="width:100%"
                           on-select="shortRepVm.onSelectBom($item)">
                    <ui-select-match
                            placeholder="{{shortRepVm.selectBomTitle}}">
                        {{$select.selected.item.itemMaster.itemName}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="bom in shortRepVm.boms track by bom.id">
                        <div ng-bind="bom.item.itemMaster.itemName"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <div uib-dropdown auto-close="outsideClick"
                 on-toggle="toggledMissile(open)" style="cursor: pointer;width: 10%;padding:0 5px;">
                <a class="btn btn-primary" title="Select Missiles" style="width: 100%"
                   uib-dropdown-toggle>Missile
                    <span class="caret"
                          style="margin-left:5px;border-top-color: #fff cursor: pointer;float: right;margin-top: 7px;"></span></a>
                <ul class="dropdown-menu status-select"
                    uib-dropdown-menu style="cursor: pointer;margin-left: -3px;max-height: 400px;overflow-y: auto;">
                    <li>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" ng-model="shortRepVm.selectedAll"
                                       ng-change="shortRepVm.selectAllMissiles()">
                                All Missiles
                            </label>
                        </div>
                    </li>
                    <li ng-repeat="missile in shortRepVm.bomInstances track by $index">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" ng-model="missile.selected"
                                       ng-change="shortRepVm.selectMissile(missile)">
                                {{missile.item.instanceName}}
                            </label>
                        </div>
                    </li>
                </ul>
            </div>


            <%--<div class="dropDown-width" ng-show="shortRepVm.selectedBom != null">
                <ui-select ng-model="shortRepVm.bomInstance" theme="bootstrap"
                           style="width:100%"
                           on-select="shortRepVm.onSelectInstance($item)">
                    <ui-select-match
                            placeholder="{{shortRepVm.bomInstanceTitle}}">
                        {{$select.selected.item.instanceName}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="instance in shortRepVm.bomInstances | filter: $select.search">
                        <div ng-bind="instance.item.instanceName"></div>
                    </ui-select-choices>
                </ui-select>
            </div>--%>
            <div class="dropDown-width" ng-show="shortRepVm.selectedMissiles.length > 0">
                <ui-select ng-model="shortRepVm.selectedSection" theme="bootstrap"
                           style="width:100%"
                           on-select="shortRepVm.selectSection($item)">
                    <ui-select-match
                            placeholder="Select Section">
                                    <span style="display: inline-flex;">{{$select.selected.typeRef.name}}
                                    <span ng-if="$select.selected.typeRef.versity"> ( VSPL )</span></span>
                    </ui-select-match>
                    <ui-select-choices
                            repeat="section in shortRepVm.sections | filter: $select.search">
                        <div>{{section.typeRef.name}}
                            <span ng-if="section.typeRef.versity"> ( VSPL )</span></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="dropDown-width" ng-show="shortRepVm.selectedSection != null">
                <ui-select ng-model="shortRepVm.selectedSubsystem" theme="bootstrap"
                           style="width:100%"
                           on-select="shortRepVm.selectSubsystem($item)">
                    <ui-select-match
                            placeholder="Select Subsystem">
                        {{$select.selected.typeRef.name}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="subsystem in shortRepVm.subsystems | filter: $select.search">
                        <div ng-bind="subsystem.typeRef.name"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div class="dropDown-width" ng-show="shortRepVm.selectedSubsystem != null">
                <ui-select ng-model="shortRepVm.selectedUnit" theme="bootstrap"
                           style="width:100%"
                           on-select="shortRepVm.selectUnit($item)">
                    <ui-select-match
                            placeholder="Select Unit">
                        {{$select.selected.typeRef.name}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="unit in shortRepVm.units | filter: $select.search | orderBy:'typeRef.name'">
                        <div ng-bind="unit.typeRef.name"></div>
                    </ui-select-choices>
                </ui-select>
            </div>
            <div style="width:12%;padding:0 5px;" ng-show="shortRepVm.selectedMissiles.length > 0">
                <ui-select ng-model="shortRepVm.searchFilter.workCenter" theme="bootstrap"
                           style="width:100%"
                           on-select="shortRepVm.selectWorkCenter($item)">
                    <ui-select-match allow-clear="true"
                                     placeholder="Work Center">
                        {{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="workCenter in shortRepVm.workCenters | filter: $select.search">
                        <div ng-bind="workCenter"></div>
                    </ui-select-choices>
                </ui-select>
            </div>

            <%--<div class="btn-group">
                <button class="btn btn-sm btn-primary min-width"
                        ng-click="shortRepVm.showBomInfoPanel(!shortRepVm.sidePanel)">
                    BOM Info
                </button>
            </div>--%>
            <button class="btn btn-sm btn-primary" ng-if="shortRepVm.showSearchButton"
                    ng-click="shortRepVm.loadReport()">
                <i class="fa fa-search"></i>Search
            </button>
            <div uib-dropdown auto-close="outsideClick" ng-if="shortRepVm.reportItems.length > 0"
                 on-toggle="toggledMissile(open)" style="cursor: pointer;width: 6%;padding:0 5px;">
                <a class="btn btn-primary" uib-dropdown-toggle>Print</a>
                <ul class="dropdown-menu status-select"
                    uib-dropdown-menu style="cursor: pointer;margin-left: -3px;max-height: 400px;overflow-y: auto;">
                    <li ng-repeat="field in shortRepVm.fields">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" ng-model="field.selected"
                                       ng-change="shortRepVm.selectField(field)">
                                {{field.field}} - {{field.name}}
                            </label>
                        </div>
                    </li>
                    <li>
                        <button class="btn btn-xs" ng-if="shortRepVm.selectedMissiles.length > 0"
                                ng-click="shortRepVm.printReport()" title="Click to Print Report">
                            <i class="fa fa-print"
                               style="font-size: 18px;float: right;color: black;"></i>
                        </button>
                    </li>
                </ul>
            </div>
        </div>
        <free-text-search ng-if="shortRepVm.showSearchBox" on-clear="shortRepVm.resetPage"
                          search-term="shortRepVm.requestFilter.searchQuery"
                          on-search="shortRepVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding">
        <div class="responsive-table" style="padding: 10px;overflow-y: auto;height: 100%;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 50px;">Select</th>
                    <th class="threeHundred-column">Nomenclature</th>
                    <th class="hundred-column">Units</th>
                    <th class="hundred-column" ng-show="shortRepVm.searchFilter.workCenter != null">Work Center</th>
                    <th class="hundred-column">BOM Qty</th>
                    <th class="hundred-column">Stock</th>
                    <th class="hundred-column">Shortage</th>
                    <th class="twoHundred-column info header-row"
                        ng-repeat="missile in shortRepVm.selectedMissiles track by $index">
                        <div style="margin-left: 12px;">
                            <span style="margin-left: 12px;">{{missile.item.instanceName}}</span><br>
                            <span style="color: blue;">R</span> /
                            <span style="color: blue;">A</span> /
                            <span style="color: blue;">I</span> /
                            <span style="color: green;">IP</span> /
                            <span style="color: darkred">F</span> /
                            <span style="color: red">S</span>
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="shortRepVm.reportItems.length == 0">
                    <td colspan="25">No Items found</td>
                </tr>
                <tr ng-repeat="reportItem in shortRepVm.reportItems | orderBy:'item.item.itemMaster.itemName'">
                    <td style="width: 50px;">
                        <input type="checkbox" ng-model="reportItem.selected"
                               ng-change="shortRepVm.selectItem(reportItem)"/>
                    </td>
                    <td class="twoHundred-column">
                        <span title="{{reportItem.item.uniquePath}}"
                              ng-bind-html="reportItem.item.item.itemMaster.itemName | highlightText: shortRepVm.searchFilter.searchQuery"></span>
                    </td>
                    <td class="hundred-column">{{reportItem.item.item.itemMaster.itemType.units}}</td>
                    <td ng-show="shortRepVm.searchFilter.workCenter != null">
                        <span ng-bind-html="reportItem.item.workCenter | highlightText: shortRepVm.searchFilter.workCenter"></span>
                    </td>
                    <td>
                        <span class="badge badge-primary" ng-if="reportItem.item.item.itemMaster.itemType.hasLots">{{reportItem.item.fractionalQuantity}}</span>
                        <span class="badge badge-primary" ng-if="!reportItem.item.item.itemMaster.itemType.hasLots">{{reportItem.item.quantity}}</span>
                    </td>
                    <td>
                        <span class="badge badge-success">{{reportItem.stock}}</span>
                    </td>
                    <td>
                        <span class="badge badge-danger">{{reportItem.totalShortage}}</span>
                    </td>
                    <td ng-repeat="missile in shortRepVm.selectedMissiles">
                        <span class="badge badge-warning" style="font-size: 14px;"
                              title='{{reportItem.listMap[""+ missile.id].requestedDate}}'>{{(reportItem.listMap[""+ missile.id].requested)}}</span>
                        <span class="badge badge-info" style="font-size: 14px;"
                              title='{{reportItem.listMap[""+ missile.id].allocatedDate}}'>{{(reportItem.listMap[""+ missile.id].allocated)}}</span>
                        <span class="badge badge-success" style="font-size: 14px;">{{(reportItem.listMap[""+ missile.id].issued)}}</span>
                        <span class="badge badge-primary" style="font-size: 14px;">{{(reportItem.listMap[""+ missile.id].issueProcess)}}</span>
                        <span class="badge badge-dark" style="font-size: 14px;">{{(reportItem.listMap[""+ missile.id].failure)}}</span>
                        <span class="badge badge-danger" style="font-size: 14px;">{{(reportItem.listMap[""+ missile.id].shortage)}}</span>
                    </td>
                </tr>
                </tbody>
            </table>


            <table id="selectedItemsReport" class="table table-striped highlight-row" style="display: none;">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Units</th>
                    <th ng-show="shortRepVm.searchFilter.workCenter != null">Work Center</th>
                    <th>BOM Qty</th>
                    <th>Stock</th>
                    <th>Shortage</th>
                    <th ng-repeat="missile in shortRepVm.selectedMissiles track by $index">
                        {{missile.item.instanceName}}<span> ({{shortRepVm.printFieldHeading}})</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="reportItem in shortRepVm.selectedItemsToPrint | orderBy:'item.item.itemMaster.itemName'">
                    <td>
                        <span ng-bind-html="reportItem.item.item.itemMaster.itemName | highlightText: shortRepVm.searchFilter.searchQuery"></span>
                    </td>
                    <td>{{reportItem.item.item.itemMaster.itemType.units}}</td>
                    <td ng-show="shortRepVm.searchFilter.workCenter != null">
                        <span ng-bind-html="reportItem.item.workCenter | highlightText: shortRepVm.searchFilter.workCenter"></span>
                    </td>
                    <td>
                        <span ng-if="reportItem.item.item.itemMaster.itemType.hasLots">{{reportItem.item.fractionalQuantity}}</span>
                        <span ng-if="!reportItem.item.item.itemMaster.itemType.hasLots">{{reportItem.item.quantity}}</span>
                    </td>
                    <td>
                        <span>{{reportItem.stock}}</span>
                    </td>
                    <td>
                        <span>{{reportItem.totalShortage}}</span>
                    </td>
                    <td ng-repeat="missile in shortRepVm.selectedMissiles">
                        {{(reportItem.listMap[""+ missile.id].list)}}
                    </td>
                </tr>
                </tbody>
            </table>


            <table id="itemDetailsReport" class="table table-striped highlight-row" style="display: none;">
                <thead>
                <tr>
                    <th>Nomenclature</th>
                    <th>Units</th>
                    <th ng-show="shortRepVm.searchFilter.workCenter != null">Work Center</th>
                    <th>BOM Qty</th>
                    <th>Stock</th>
                    <th>Shortage</th>
                    <th ng-repeat="missile in shortRepVm.selectedMissiles track by $index">
                        {{missile.item.instanceName}}<span> ({{shortRepVm.printFieldHeading}})</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="reportItem in shortRepVm.reportItems | orderBy:'item.item.itemMaster.itemName'">
                    <td>
                        <span ng-bind-html="reportItem.item.item.itemMaster.itemName | highlightText: shortRepVm.searchFilter.searchQuery"></span>
                    </td>
                    <td>{{reportItem.item.item.itemMaster.itemType.units}}</td>
                    <td ng-show="shortRepVm.searchFilter.workCenter != null">
                        <span ng-bind-html="reportItem.item.workCenter | highlightText: shortRepVm.searchFilter.workCenter"></span>
                    </td>
                    <td>
                        <span ng-if="reportItem.item.item.itemMaster.itemType.hasLots">{{reportItem.item.fractionalQuantity}}</span>
                        <span ng-if="!reportItem.item.item.itemMaster.itemType.hasLots">{{reportItem.item.quantity}}</span>
                    </td>
                    <td>
                        <span>{{reportItem.stock}}</span>
                    </td>
                    <td>
                        <span>{{reportItem.totalShortage}}</span>
                    </td>
                    <td ng-repeat="missile in shortRepVm.selectedMissiles">{{(reportItem.listMap[""+ missile.id].list)}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>