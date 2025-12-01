<div id="newRequestView" class="view-container" fitcontent>
    <style scoped>
        .view-toolbar {
            padding-top: 5px;
            padding-right: 10px;
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

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
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

        .inward-info-panel {
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

        .inward-info-panel .info-panel-header {
            border-bottom: 1px solid #ddd;
            padding-left: 10px;
        }

        .inward-info-panel .info-panel-header h3 {
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

        input[type=radio] {
            border: 0px;
            width: 39%;
            height: 1em;
        }

        .sections-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 10; /* Sit on top */
            padding-top: 15px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .sections-model .modal-content {
            margin-left: auto;
            margin-right: auto;
            top: 111px;
            display: block;
            height: 70%;
            width: 25%;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .unit-dropDown {
            display: none;
            position: absolute;
            border: 1px solid lightgrey;
            z-index: 100;
            background-color: #fff;
            overflow-y: auto;
            padding: 5px;
            width: 93%;
            max-height: 200px;
            box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16);
            font-weight: normal;
        }
    </style>
    <div class="view-toolbar">
        <div class="row">
            <div class="btn-group">
                <button class="btn btn-sm btn-primary min-width"
                        ng-click="newRequestVm.showRequisitionInfoPanel(!newRequestVm.sidePanel)">
                    Requisition Info
                </button>
                <button class="btn btn-sm btn-default min-width"
                        ng-click="newRequestVm.back()">Back
                </button>
                <button class="btn btn-sm btn-success min-width" title="Click to save Requisition"
                        ng-click="newRequestVm.createRequisition()">Create
                </button>
                <%--<button class="btn btn-xs" ng-if="newRequestVm.selectedSection != null"
                        ng-click="newRequestVm.addItems()" title="Request Section">Add Section Items
                </button>--%>
                <%--<button ng-if="newRequestVm.requisition.bomInstance != null" class="btn btn-xs"
                        ng-click="bomVm.getInstanceRequestReport()" title="MBOM Request Report">
                    <i class="fa flaticon-stamp13"></i>
                </button>--%>
            </div>

            <div class="col-sm-6 pull-right" style="padding: 0px;">
                <div class="input-group mb15 search-box" ng-if="newRequestVm.showSearchBox">
                    <input id="newItemSearchBox" type="text" class="form-control input-sm"
                           placeholder="Search items and Add to request"
                           onfocus="this.setSelectionRange(0, this.value.length)"
                           ng-click="newRequestVm.preventClick($event)"
                           ng-model="newRequestVm.searchFilter.searchQuery"
                           ng-model-options="{ debounce: 1000 }"
                           ng-change="newRequestVm.performSearch()">
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-primary"
                                ng-click="newRequestVm.performSearch()">Search
                        </button>
                    </span>
                </div>

                <div id="newRequestSearchResults" class="searchResults">
                    <div class="responsive-table">
                        <table class="table table-striped table-condensed">
                            <thead>
                            <tr>
                                <th style="width: 50px">Add</th>
                                <th style="width: 30%;">Nomenclature</th>
                                <th style="width: 25%;">Type</th>
                                <th>Path</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="newRequestVm.searchResults.content.length == 0">
                                <td colspan="15" style="text-align: left;">No items found</td>
                            </tr>
                            <tr ng-repeat="result in newRequestVm.searchResults.content | orderBy:'item.itemMaster.itemName'">
                                <td style="width: 50px">
                                    <a href="" title="Add item"
                                       ng-click="newRequestVm.addItemToRequisition(result);newRequestVm.preventClick($event)"><i
                                            class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                                </td>
                                <td class="text-left">
                                    <span ng-bind-html="result.item.itemMaster.itemName | highlightText: newRequestVm.searchFilter.searchQuery"></span>
                                    {{result.item.partSpec.specName}}
                                </td>
                                <td class="text-left">
                                    <span ng-bind-html="result.item.itemMaster.parentType.name | highlightText: newRequestVm.searchFilter.searchQuery"></span>
                                </td>
                                <td class="text-left">{{result.namePath}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="table-footer">
                        <div style="display: inline-flex;width: 100%;">
                            <div style="width: 50%">
                                <h5>Displaying {{newRequestVm.searchResults.numberOfElements}} of
                                    {{newRequestVm.searchResults.totalElements}}</h5>
                            </div>

                            <div class="text-right" style="width: 50%">
                                <span class="mr10">Page {{newRequestVm.searchResults.totalElements != 0 ? newRequestVm.searchResults.number+1:0}} of {{newRequestVm.searchResults.totalPages}}</span>
                                <a href="" ng-click="newRequestVm.previousPage()"
                                   ng-class="{'disabled': newRequestVm.searchResults.first}"><i
                                        class="fa fa-arrow-circle-left mr10"></i></a>
                                <a href="" ng-click="newRequestVm.nextPage()"
                                   ng-class="{'disabled': newRequestVm.searchResults.last}"><i
                                        class="fa fa-arrow-circle-right"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="view-content no-padding">
        <div class="inward-info-panel" id="inwardInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>Request Information</h3>
                <a href="" ng-click="newRequestVm.showRequisitionInfoPanel(false)" class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <form class="form-horizontal">

                    <div class="form-group" style="margin-right: 0px;">

                        <label class="col-sm-4 control-label">BOM <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newRequestVm.selectedBom" theme="bootstrap" style="width:100%"
                                       on-select="newRequestVm.onSelectBom($item)">
                                <ui-select-match
                                        placeholder="{{newRequestVm.selectBomTitle}}">
                                    {{$select.selected.item.itemMaster.itemName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bom in newRequestVm.boms track by bom.id">
                                    <div ng-bind="bom.item.itemMaster.itemName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;" ng-if="newRequestVm.selectedBom != null">

                        <label class="col-sm-4 control-label">Instance <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newRequestVm.requisition.bomInstance" theme="bootstrap"
                                       style="width:100%"
                                       on-select="newRequestVm.onSelectInstance($item)">
                                <ui-select-match
                                        placeholder="{{newRequestVm.bomInstanceTitle}}">
                                    {{$select.selected.item.instanceName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="instance in newRequestVm.bomInstances | filter: $select.search">
                                    <div ng-bind="instance.item.instanceName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;"
                         ng-if="newRequestVm.requisition.bomInstance != null">

                        <label class="col-sm-4 control-label">Section <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newRequestVm.selectedSection" theme="bootstrap"
                                       style="width:100%"
                                       on-select="newRequestVm.selectSection($item)">
                                <ui-select-match
                                        placeholder="Select Section">
                                    <span style="display: inline-flex;">{{$select.selected.typeRef.name}}
                                    <span ng-if="$select.selected.typeRef.versity"> ( VSPL )</span></span>
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="section in newRequestVm.instanceSections | filter: $select.search">
                                    <div>{{section.typeRef.name}}
                                        <span ng-if="section.typeRef.versity"> ( VSPL )</span></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;"
                         ng-if="newRequestVm.selectedSection != null">

                        <label class="col-sm-4 control-label">Sub System <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newRequestVm.selectedSubsystem" theme="bootstrap"
                                       style="width:100%"
                                       on-select="newRequestVm.selectSubsystem($item)">
                                <ui-select-match
                                        placeholder="Select Subsystem">
                                    {{$select.selected.typeRef.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="section in newRequestVm.subsystems | filter: $select.search">
                                    <div ng-bind="section.typeRef.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;"
                         ng-if="newRequestVm.selectedSubsystem != null">

                        <label class="col-sm-4 control-label">Unit <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">

                            <button class="btn btn-sm" ng-click="newRequestVm.showUnits()"
                                    style="width: 100%;background: white;border: 1px solid lightgray;text-align: left;padding: 8px 17px;">
                                {{unitsTitle}}
                                <i class="fa fa-caret-down"
                                   style="float: right;padding-top: 5px;margin-right: -5px;"></i>
                            </button>
                            <div id="unitDropDown" class="unit-dropDown">
                                <ul style="list-style: none;padding:0px;">
                                    <li ng-repeat="unit in newRequestVm.units" style="padding: 5px;cursor: pointer;">
                                        <input type="checkbox" ng-model="unit.selected" style="width: 25px;"
                                               ng-change="newRequestVm.selectUnit(unit)">
                                        <span>{{unit.typeRef.name}}</span>
                                    </li>
                                </ul>
                            </div>

                            <%--<ui-select ng-model="newRequestVm.selectedUnit" theme="bootstrap"
                                       style="width:100%"
                                       on-select="newRequestVm.selectUnit($item)">
                                <ui-select-match
                                        placeholder="Select Unit">
                                    {{$select.selected.typeRef.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="section in newRequestVm.units">
                                    <div ng-bind="section.typeRef.name"></div>
                                </ui-select-choices>
                            </ui-select>--%>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-4 control-label">
                            <span>Request By</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h5>
                                {{loginPersonDetails.person.fullName}}
                            </h5>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-4 control-label">
                            <span>Purpose</span>: </label>

                        <div class="col-sm-7">
                            <textarea type="text" class="form-control" rows="3"
                                      ng-model="newRequestVm.requisition.notes"></textarea>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="responsive-table" style="padding: 10px;overflow-y: auto;height: 100%;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="twoHundred-colmn">Nomenclature</th>
                    <th class="threeHundred-column">Path</th>
                    <th>Units</th>
                    <th>BOM Quantity</th>
                    <th>Allocated Quantity</th>
                    <th>
                        Requested Qty
                    </th>
                    <th>Issued Qty</th>
                    <th>
                        <i class="fa fa-eject fa-rotate-180" title="Request All"
                           style="color: black;padding: 0 4px;cursor: pointer;"
                           ng-click="newRequestVm.requestAll()"></i>
                        Request Qty
                        <i class="fa fa-eject" title="Reset All" style="color: red;padding: 0 4px;cursor: pointer;"
                           ng-click="newRequestVm.unCheckAll()"></i>
                    </th>
                    <th style="width: 70px;">Actions
                        <i class="fa fa-minus-circle" title="Remove All Items" ng-click="newRequestVm.removeAll()"
                           style="color: black;padding: 0 4px;cursor: pointer;"></i>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="newRequestVm.requisitionItems.length == 0">
                    <td colspan="25">No Request Items</td>
                </tr>
                <tr ng-repeat="reqItem in newRequestVm.requisitionItems | orderBy:'item.item.itemMaster.itemName'">
                    <td class="twoHundred-column">{{reqItem.item.item.itemMaster.itemName}}</td>
                    <td class="threeHundred-column">{{reqItem.item.namePath}}</td>
                    <td>{{reqItem.item.item.itemMaster.itemType.units}}</td>
                    <td>
                        <span class="badge badge-primary" ng-if="reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.item.fractionalQuantity}}</span>
                        <span class="badge badge-primary" ng-if="!reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.item.quantity}}</span>
                    </td>
                    <td>
                        <span class="badge badge-secondary">{{reqItem.item.allocatedQty}}</span>
                    </td>
                    <td>
                        <span class="badge badge-warning" ng-if="reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.item.fractionalRequestedQuantity}}</span>
                        <span class="badge badge-warning" ng-if="!reqItem.item.item.itemMaster.itemType.hasLots">{{reqItem.item.requestedQuantity}}</span>
                    </td>
                    <td>
                        <span class="badge badge-success">{{reqItem.item.issuedQuantity}}</span>
                    </td>
                    <td>
                        <input ng-if="!reqItem.item.item.itemMaster.itemType.hasLots" type="number"
                               title="{{reqItem.item.canRequestMore == 0 ? 'Request Completed':''}}"
                               ng-disabled="reqItem.item.canRequestMore == 0.0" min="0"
                               class="form-control" ng-model="reqItem.quantity" style="width: 100px;"/>

                        <input ng-if="reqItem.item.item.itemMaster.itemType.hasLots" type="number"
                               title="{{reqItem.item.canRequestMore == 0.0 ? 'Request Completed':''}}"
                               ng-disabled="reqItem.item.canRequestMore == 0.0"
                               min="0"
                               class="form-control" ng-model="reqItem.fractionalQuantity" style="width: 100px;"/>
                    </td>
                    <td style="width: 70px;">
                        <div class="btn-group">
                            <i class="fa fa-minus-circle" style="font-size: 18px;"
                               ng-click="newRequestVm.removeReqItem(reqItem)"></i>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div id="requestSection-view" class="sections-model modal">
        <div class="modal-content">
            <h3 style="text-align: center;background: lavender;height: 10%;margin: 0px;">Sections</h3>

            <div style="height: 80%;">
                <div style="margin-left: auto;margin-right:auto;margin-top:2%;width: 90%;height:96%;border: 1px solid lightgrey; overflow-y: auto;">
                    <table class="table table-striped" style="box-shadow: none">
                        <thead>
                        <tr>
                            <th>Select</th>
                            <th>Section</th>
                            <%--<th>No of Items in Section</th>
                            <th>Not Yet Requested</th>--%>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="newRequestVm.instanceSections.length == 0">
                            <td colspan="15"> No Sections</td>
                        </tr>
                        <tr ng-repeat="instance in newRequestVm.instanceSections">
                            <td style="width: 25px;">
                                <input type="radio" name="section" ng-model="instance.selected"
                                       ng-disabled="instance.bomInstanceItems.length == 0"
                                       style="height:15px; width:15px; vertical-align: middle;margin: 0px;"
                                       ng-click="newRequestVm.selectSection(instance)"/>
                            </td>
                            <td style="text-align: left;">
                                <span>{{instance.typeRef.name}}<span
                                        ng-if="instance.typeRef.versity"> ( VSPL )</span></span>
                            </td>
                            <%--<th>{{instance.bomInstanceItems.length}}</th>
                            <th>{{instance.notYetRequestedItems.length}}</th>--%>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div style="height: 10%;text-align: center;background: lightgrey;">
                <button class="btn btn-sm btn-primary" ng-click="newRequestVm.addItems()"
                        style="margin-top: 5px;">Add
                </button>
                <button class="btn btn-sm btn-default" ng-click="newRequestVm.closeRequestSectionsView()"
                        style="margin-top: 5px;">Close
                </button>
            </div>
        </div>
    </div>
</div>