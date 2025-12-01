<style scoped>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    table {
        table-layout: fixed;
    }

    .table-condensed {
        width: 250px !important;
    }

</style>
<div id="inwardDetailsView" class="view-container" fitcontent>
    <style scoped>

        .search-box {
            width: 95%;
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

        .search-results {
            display: none;
            position: absolute;
            top: 37px;
            right: 0 !important;
            border: 1px solid #ddd;
            z-index: 100;
            background-color: #fff;
            overflow-y: auto;
        }

        .search-results table th, .search-results table td {
            padding: 5px !important;
        }

        .search-results .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 40px;
            top: 0;
            overflow: auto;
        }

        .search-results table th, .search-results table td {
            padding: 5px !important;
        }

        .search-results .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .search-results .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .search-results .table-footer > div h5 {
            margin: 0;
        }

        .inward-info-panel {
            display: none;
            z-index: 101;
            width: 500px;
            position: absolute;
            top: 0px;
            left: 0px;
            background-color: #fff;
            bottom: 0px;
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

        .actions-column {
            width: 150px;
            text-align: center;
        }

        .actions-column i {
            cursor: pointer;
        }

        .add-column {
            width: 100px;
            text-align: center;
        }

        .add-column i {
            cursor: pointer;
        }

        .units-column {
            width: 50px;
            text-align: center;
        }

        .qty-column {
            width: 100px;
            text-align: center;
        }

        .storage-column {
            width: 150px;
            max-width: 200px;
            text-align: center;
        }

        .status-column {
            width: 100px;
            text-align: center;
        }

        .upn-column {
            width: 150px;
            min-width: 150px;
            max-width: 200px;
        }

        .oem-column {
            width: 150px;
            display: run-in;
            word-wrap: break-word;
            white-space: normal !important;
            text-align: left;
        }

        .attribute-column {
            width: 140px;
            display: run-in;
            word-wrap: break-word;
            white-space: normal !important;
            text-align: left;
        }

        .oem-column input {
            width: 90%;
        }

        .attributes-column {
            width: 125px;
            min-width: 125px;
            max-width: 175px;
            display: run-in;
            word-wrap: break-word;
            white-space: normal !important;
            text-align: left;
        }

        .attributes-column input {
            width: 90%;
        }

        tr.inward-item-row {
            background: #7474BF; /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, #348AC7, #7474BF); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, #348AC7, #7474BF); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

            color: #fff !important;
        }

        tr.inward-item-row td {
            background: transparent !important;
            color: #fff !important;
        }

        tr.inward-item-row td i {
            color: #fff !important;
        }

        tr.review-item-row {
            background: #E48080; /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, #E48080, #E48080); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, #E48080, #E48080); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

            color: black !important;
        }

        tr.review-item-row td {
            background: transparent !important;
            color: black !important;
        }

        tr.review-item-row td a {
            color: black !important;
        }

        tr.provAccept-item-row {
            background: rgba(75, 210, 19, 0.75); /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, rgba(75, 210, 19, 0.75), rgba(75, 210, 19, 0.75)); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, rgba(75, 210, 19, 0.75), rgba(75, 210, 19, 0.75)); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

            color: black !important;
        }

        tr.provAccept-item-row td {
            background: transparent !important;
            color: black !important;
        }

        tr.provAccept-item-row td a {
            color: black !important;
        }

        .input-oem-number {
            /*text-transform: uppercase;*/
        }

        .input-lot-number {
            /*text-transform: uppercase;*/
            width: 80%;
        }

        .inventory-qty-column, .allocated-qty-column, .return-qty-column {
            width: 50px;
            text-align: center;
        }

        .drawing-number-column {
            width: 150px;
            text-align: left;
        }

        .accept-icon-review {
            color: black !important;
        }

        .accept-icon {
            color: green !important;
        }

        .edit-prov-instance {
            color: black !important;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }


    </style>
    <div class="view-toolbar">
        <div class="row">
            <div class="btn-group col-md-3">
                <button class="btn btn-sm btn-primary min-width"
                        ng-click="inwardDetailsVm.showInwardInfoPanel(!inwardDetailsVm.sidePanel)">
                    Inward Info
                </button>
                <button class="btn btn-sm btn-default min-width"
                        ng-click="inwardDetailsVm.back()">Back
                </button>
                <button class="btn btn-sm btn-success min-width" ng-if="inwardDetailsVm.inward.status == 'STORE'"
                        title="{{inwardDetailsVm.buttonTitle}}"
                        ng-click="inwardDetailsVm.saveInwardItems()">{{inwardDetailsVm.buttonName}}
                </button>

            </div>
            <div class="col-md-4" style="margin-top: -10px;"><h5 style="margin: 10px">Gate Pass :
                {{inwardDetailsVm.inward.gatePass.gatePass.name}}</h5></div>
            <div class="col-md-5 pull-right" style="padding: 0px;"
                 ng-if="inwardDetailsVm.inward.status == 'STORE'">
                <div class="input-group mb15 search-box" ng-if="inwardDetailsVm.showSearchBox">
                    <input id="newItemSearchBox" type="text" class="form-control input-sm"
                           placeholder="Search items and Add to inward"
                           onfocus="this.setSelectionRange(0, this.value.length)"
                           ng-click="inwardDetailsVm.preventClick($event)"
                           ng-model="inwardDetailsVm.searchFilter.searchQuery"
                           ng-model-options="{ debounce: 1000 }"
                           ng-change="inwardDetailsVm.performSearch()"
                           ng-enter="inwardDetailsVm.performSearch()">
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-primary" style="margin-right: 10px;"
                                ng-click="inwardDetailsVm.performSearch()">Search
                        </button>
                    </span>
                </div>

                <div id="inwardDetailsSearchResults" class="search-results">
                    <div class="responsive-table">
                        <table class="table table-striped" style="table-layout: auto !important;">
                            <thead>
                            <tr>
                                <th style="width: 5%;">Add</th>
                                <th class="thirty-column">Nomenclature</th>
                                <th class="tenPercent-column">Item Code</th>
                                <th class="fifteenPercent-column">Type</th>
                                <th class="forty-column">Path</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="inwardDetailsVm.searchResults.content.length == 0">
                                <td colspan="15" style="text-align: left;">No items found</td>
                            </tr>
                            <tr ng-repeat="result in inwardDetailsVm.searchResults.content  | orderBy:'item.itemMaster.itemName'">
                                <td style="width: 10%">
                                    <a href="" title="Add item"
                                       ng-click="inwardDetailsVm.addItemToInward(result);inwardDetailsVm.preventClick($event)"><i
                                            class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                                </td>
                                <td class="thirty-column">
                                    <span ng-bind-html="result.item.itemMaster.itemName | highlightText: inwardDetailsVm.searchFilter.searchQuery"></span>
                                </td>
                                <td class="tenPercent-column">
                                    <span ng-bind-html="result.item.itemMaster.itemCode | highlightText: inwardDetailsVm.searchFilter.searchQuery"></span>
                                </td>
                                <td class="fifteenPercent-column">
                                    <span ng-bind-html="result.item.itemMaster.parentType.name | highlightText: inwardDetailsVm.searchFilter.searchQuery"></span>
                                </td>
                                <td class="forty-column">
                                    <span ng-if="result.pathCount == 1">{{result.uniquePath}}</span>
                                <span ng-if="result.pathCount > 1" style="font-weight: 600;color: grey;">
                                    {{inwardDetailsVm.inward.bom.item.itemMaster.itemCode}} / -- / {{result.uniquePath}}</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="table-footer">
                        <div style="display: inline-flex;width: 100%;">
                            <div style="width: 50%">
                                <h5>Displaying {{inwardDetailsVm.searchResults.numberOfElements}} of
                                    {{inwardDetailsVm.searchResults.totalElements}}</h5>
                            </div>

                            <div class="text-right" style="width: 50%">
                                <span class="mr10">Page {{inwardDetailsVm.searchResults.totalElements != 0 ? inwardDetailsVm.searchResults.number+1:0}} of {{inwardDetailsVm.searchResults.totalPages}}</span>
                                <a href="" ng-click="inwardDetailsVm.previousPage()"
                                   ng-class="{'disabled': inwardDetailsVm.searchResults.first}"><i
                                        class="fa fa-arrow-circle-left mr10"></i></a>
                                <a href="" ng-click="inwardDetailsVm.nextPage()"
                                   ng-class="{'disabled': inwardDetailsVm.searchResults.last}"><i
                                        class="fa fa-arrow-circle-right"></i></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="inward-info-panel" id="inwardInfoPanel" style="padding: 0px">
            <div class="info-panel-header">
                <h3>Inward Information</h3>
                <a href="" ng-click="inwardDetailsVm.showInwardInfoPanel(false)" class="close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="info-panel-details">
                <form class="form-horizontal">

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-5 control-label">
                            <span>Inward Number</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h5>{{inwardDetailsVm.inward.number}}</h5>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">

                        <label class="col-sm-5 control-label">BOM <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h5>{{inwardDetailsVm.inward.bom.item.itemMaster.itemCode}}</h5>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-5 control-label">
                            <span>Supplier</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">

                            <h5>{{inwardDetailsVm.inward.supplier.supplierName}}</h5>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-5 control-label">
                            <span>Status</span>
                            <span class="asterisk">*</span> : </label>


                        <div class="col-sm-7" style="padding: 10px;">
                            <inward-status inward="inwardDetailsVm.inward"></inward-status>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-5 control-label">
                            <span>Gate Pass</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h5>
                                <a href="" ng-click="inwardDetailsVm.downloadGatePass(inwardDetailsVm.inward.gatePass)"
                                   title="Click to download Gate Pass">
                                    {{inwardDetailsVm.inward.gatePass.gatePass.name}}</a>
                            </h5>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-5 control-label">
                            <span>Gate Pass Number</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h5>{{inwardDetailsVm.inward.gatePass.gatePassNumber}}</h5>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;">
                        <label class="col-sm-5 control-label">
                            <span>Notes</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" ng-model="inwardDetailsVm.inward.notes" rows="5"
                                      style="resize: none;"></textarea>
                        </div>
                    </div>

                    <div class="form-group" style="margin-right: 0px;"
                         ng-repeat="property in inwardDetailsVm.inwardProperties">
                        <label class="col-sm-5 control-label">
                            <span>{{property.attributeDef.name}}</span>
                            <span ng-if="property.attributeDef.required" class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <h5 ng-if="property.attributeDef.dataType == 'TEXT'">
                                {{property.value.stringValue}}
                            </h5>
                        </div>
                        <div class="col-sm-7">
                            <h5 ng-if="property.attributeDef.dataType == 'DATE'">
                                {{property.value.dateValue}}
                            </h5>
                        </div>
                        <div class="col-sm-7">
                            <h5 ng-if="property.attributeDef.dataType == 'INTEGER'">
                                {{property.value.integerValue}}
                            </h5>
                        </div>
                        <div class="col-sm-7">
                            <h5 ng-if="property.attributeDef.dataType == 'LIST'">
                                {{property.value.listValue}}
                            </h5>
                        </div>

                        <div class="col-sm-7" style="margin-top: 8px;"
                             ng-if="property.attributeDef.dataType == 'ATTACHMENT'">
                            <div ng-repeat="attachment in property.value.attachmentValues">
                                <a href="" ng-click="inwardDetailsVm.openPropertyAttachment(attachment)">
                                    {{attachment.name}}
                                </a>
                                <i style="cursor:pointer;" class="fa fa-trash" title="Click to delete"
                                   ng-click="inwardDetailsVm.deleteInwardAttachment(attachment)"></i>
                            </div>
                            <a href="" ng-click="inwardDetailsVm.editAttachment(property)"
                               ng-if="property.value.attachmentValues.length == 0 && !property.editMode">
                                Add Attachment
                            </a>

                            <div ng-if="property.editMode" style="display: inline-flex;width: 100%;">
                                <input type="file" class="form-control" style="width: 80%;padding: 5px;"
                                       accept="application/pdf"
                                       ng-file-model="property.attachmentValues"/>
                                <i class="fa fa-check-circle" style="cursor: pointer;font-size: 20px;padding: 8px 4px;"
                                   title="Save"
                                   ng-click="inwardDetailsVm.saveInwardAttachment(property)"></i>
                                <i class="fa fa-times-circle" title="Cancel"
                                   style="color:red;cursor: pointer;font-size: 20px;padding: 8px 0px;"
                                   ng-click="inwardDetailsVm.cancelChanges(property)"></i>
                            </div>
                        </div>
                    </div>

                    <div>
                        <h4 class="section-title" style="color: black;font-size:20px;">Inward History</h4>
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Status</th>
                                <th>User</th>
                                <th>Timestamp</th>
                            </tr>
                            </thead>
                            <tbody>

                            <tr ng-if="inwardDetailsVm.inward.statusHistories.length == 0">
                                <td colspan="12">No Inward History</td>
                            </tr>
                            <tr ng-repeat="history in inwardDetailsVm.inward.statusHistories">
                                <td>
                                    <inward-status inward="history"></inward-status>
                                </td>
                                <td>{{history.user.fullName}}</td>
                                <td>{{history.timestamp | date:'dd-MM-yyyy HH:mm:ss'}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
        </div>
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th class="name1-column">Nomenclature</th>
                    <th class="attributes-column">Section</th>
                    <th class="drawing-number-column">Drawing No</th>
                    <th class="units-column">Units</th>
                    <th class="qty-column">Inward Qty</th>
                    <th class="status-column">Status</th>
                    <th class="storage-column">Storage</th>
                    <th class="upn-column">UPN</th>
                    <th class="oem-column">Manufacturer</th>
                    <th class="oem-column">Serial No/Spec</th>
                    <th class="attribute-column"
                        ng-repeat="selectedAttribute in inwardDetailsVm.itemInstanceAttributes">
                        {{selectedAttribute.attributeDef.name}}
                    </th>
                    <th class="oem-column">Expiry Date</th>
                    <th class="actions-column">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="inwardDetailsVm.loading == true">
                    <td colspan="25">
                            <span style="font-size: 15px;">
                                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">Loading items...
                            </span>
                    </td>
                </tr>
                <tr ng-if="inwardDetailsVm.loading == false && inwardDetailsVm.inwardItems.length == 0">
                    <td colspan="25">No Inward Items</td>
                </tr>
                <tr ng-repeat="inwardItemRow in inwardDetailsVm.inwardItemRows"
                    ng-class="{'inward-item-row': inwardItemRow.instancesCreated && inwardItemRow.type == 'INWARDITEM',
                               'review-item-row': inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.review,
                               'provAccept-item-row': inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.provisionalAccept}">
                    <td class="name1-column">
                        <i ng-if="inwardItemRow.instances.length > 0" class="mr5 fa"
                           style="cursor: pointer; color: #909090"
                           ng-click="inwardDetailsVm.toggleRow(inwardItemRow)"
                           ng-class="{'fa-plus-square': (inwardItemRow.expanded == false || inwardItemRow.expanded == null || inwardItemRow.expanded == undefined),
                            'fa-minus-square': inwardItemRow.expanded == true}"></i>
                        <span class="level{{inwardItemRow.level}}">{{inwardItemRow.bomItem.item.itemMaster.itemName}} {{inwardItemRow.bomItem.item.partSpec.specName}}</span>
                    </td>
                    <td class="attributes-column">
                        <span ng-if="inwardItemRow.type == 'INWARDITEM' && !inwardItemRow.instancesCreated && inwardItemRow.bomItem.pathCount > 1">
                            <ui-select ng-model="inwardItemRow.bomItem.defaultSection" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Section">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-null-choice></ui-select-null-choice>
                                <ui-select-choices
                                        repeat="section in inwardItemRow.bomItem.sections | filter: $select.search">
                                    <div>
                                        <span>{{section.name}}</span>
                                        <span ng-if="section.versity">( VSPL )</span>
                                    </div>
                                </ui-select-choices>
                            </ui-select>
                        </span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEM' && !inwardItemRow.instancesCreated && inwardItemRow.bomItem.pathCount == 1">
                            {{inwardItemRow.bomItem.defaultSection.name}}<span
                                ng-if="inwardItemRow.bomItem.defaultSection.versity"> ( VSPL )</span>
                        </span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEM' && inwardItemRow.instancesCreated">{{inwardItemRow.section.name || 'Common'}}
                            <span ng-if="inwardItemRow.section.versity"> ( VSPL )</span>
                        </span>

                    </td>
                    <td class="drawing-number-column">
                        <span ng-if="inwardItemRow.type == 'INWARDITEM'">{{inwardItemRow.bomItem.item.drawingNumber}}</span>
                    </td>
                    <td class="units-column">
                        <span ng-if="inwardItemRow.type == 'INWARDITEM'">{{inwardItemRow.bomItem.item.itemMaster.itemType.units}}</span>
                    </td>
                    <td class="qty-column">
                        <input ng-if="!inwardItemRow.instancesCreated && inwardItemRow.type == 'INWARDITEM' && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               type="number" class="form-control" ng-model="inwardItemRow.quantity"
                               ng-enter="inwardDetailsVm.createInwardItem(inwardItemRow)"/>
                        <input ng-if="!inwardItemRow.instancesCreated && inwardItemRow.type == 'INWARDITEM' && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               type="number" class="form-control" ng-model="inwardItemRow.fractionalQuantity"
                               ng-enter="inwardDetailsVm.createInwardItem(inwardItemRow)"/>
                        <span ng-if="inwardItemRow.type == 'INWARDITEM' && inwardItemRow.instancesCreated == true && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                            {{inwardItemRow.quantity}}</span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEM' && inwardItemRow.instancesCreated == true && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                            {{inwardItemRow.fractionalQuantity}}</span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                            {{inwardItemRow.item.lotSize}}</span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                            {{inwardItemRow.quantity}}</span>
                    </td>
                    <td class="status-column">
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE'"
                              title="{{inwardItemRow.item.presentReason}}">
                            <item-instance-status object="inwardItemRow.item"></item-instance-status>
                        </span>
                        <%--<span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.status == 'NEW'">--</span>--%>
                    </td>
                    <td class="storage-column">
                        <span ng-if="inwardItemRow.level == 1 && inwardItemRow.item.storage != null"
                              class="badge badge-success" style="font-size: 13px;"
                              title="{{inwardItemRow.item.storagePath}}">
                            {{inwardItemRow.item.storage.name}}
                        </span>
                    </td>
                    <td class="upn-column">
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.oemNumber != null && !inwardItemRow.editMode">
                            <a href="" title="Click to history"
                               ng-if="!inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="showUpnHistory(inwardItemRow.item,'right')">{{inwardItemRow.item.upnNumber}}
                            </a>
                            <a href="" title="Click to history"
                               ng-if="inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="showLotUpnHistory(inwardItemRow.item,'INWARD')">{{inwardItemRow.item.upnNumber}}
                            </a>
                        </span>
                    </td>
                    <td class="oem-column">
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit)">

                            <ui-select ng-model="inwardItemRow.item.manufacturer" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Mfr">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="supplier in inwardDetailsVm.manufacturers | filter: $select.search">
                                    <div ng-bind="supplier.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editMode && !inwardItemRow.editAfterSubmit">{{inwardItemRow.item.manufacturer.name}}</span>
                    </td>
                    <td class="oem-column">
                        <%--<span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editMode && !inwardItemRow.editInstance
                                     && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">{{inwardItemRow.item.manufacturer.mfrCode}}
                        <span ng-if="inwardItemRow.item.manufacturer != null">- {{inwardItemRow.item.oemNumber}}</span>
                        </span>
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                            {{inwardItemRow.item.oemNumber}}
                        </span>--%>
                        <span ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editMode && !inwardItemRow.editInstance && !inwardItemRow.editAfterSubmit">
                            {{inwardItemRow.item.oemNumber}}
                            <%--<span ng-if="!inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">{{inwardItemRow.item.oemNumber}}</span>--%>
                            <%--<span ng-if="inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">{{inwardItemRow.item.oemNumber}}</span>--%>
                        </span>

                            <%--------  OLD -------------%>
                            <%--<input type="text" class="input-oem-number form-control"
                                   ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.editMode || inwardItemRow.editInstance) && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                                   ng-model="inwardItemRow.item.oemNumber"
                                   ng-change="inwardDetailsVm.updateUPN(inwardItemRow)"/>--%>

                            <%--------   NEW  ---------------------%>
                            <input type="text" class="input-oem-number form-control"
                                   ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.editMode || inwardItemRow.editInstance || inwardItemRow.editAfterSubmit) && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                                   ng-model="inwardItemRow.item.oemNumber"/>

                            <div style="display: flex" class=""
                                 ng-if="inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                                <input type="text" class="input-lot-number form-control"
                                       ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.editMode || inwardItemRow.editInstance || inwardItemRow.editAfterSubmit) && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                                   ng-model="inwardItemRow.item.oemNumber"/>

                                <button class="btn btn-xs"
                                        ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && inwardItemRow.item.lotNumber != null && inwardItemRow.item.lotNumber != ''"
                                        ng-click="inwardDetailsVm.deleteLotNumber(inwardItemRow)">
                                    <i class="fa fa-trash" style="padding: 0 4px;cursor: pointer;font-size: 16px;"></i>
                                </button>

                                <button class="btn btn-xs"
                                        ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && (inwardItemRow.item.lotNumber == null || inwardItemRow.item.lotNumber == '')"
                                        ng-click="inwardDetailsVm.generateLotNumber(inwardItemRow)">
                                    <i class="fa fa-cog" style="cursor: pointer;font-size: 16px;padding: 0 4px;"
                                   title="Click to generate Lot Number"></i>
                                </button>

                            </div>
                    </td>
                    <td class="attribute-column"
                        ng-repeat="objectAttribute in inwardDetailsVm.itemInstanceAttributes">
                        <div class="attributeTooltip"
                             ng-if="objectAttribute.attributeDef.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.attributeDef.name">
                            <%--<span ng-if="!inwardItemRow.editMode && !inwardItemRow.editInstance">
                                <a ng-if="inwardItemRow[attrName].value.attachmentValues.length > 1" href="">
                                    {{inwardItemRow[attrName].value.attachmentValues.length}} Attachments
                                </a>
                            </span>--%>

                            <span ng-if="!inwardItemRow.editAfterSubmit && !inwardItemRow.editMode && !inwardItemRow.editInstance">
                                <a ng-if="inwardItemRow[attrName].value.attachmentValues.length == 1"
                                   title="Click to download attachment" style="cursor: pointer;"
                                   ng-click="inwardDetailsVm.openPropertyAttachment(inwardItemRow[attrName].value.attachmentValues[0])">
                                    {{inwardItemRow[attrName].value.attachmentValues[0].name}}
                                </a>
                                <i class="fa fa-trash" title="Delete Attachment" style="cursor: pointer;"
                                   ng-if="inwardItemRow[attrName].value.attachmentValues.length == 1 && !inwardItemRow.editMode && hasPermission('permission.inward.storeApprove')
                                          && (inwardItemRow.item.status == 'STORE_SUBMITTED' || inwardItemRow.item.provisionalAccept || inwardItemRow.item.status == 'REVIEW')"
                                   ng-click="inwardDetailsVm.deleteObjectAttachment(inwardItemRow[attrName].value.attachmentValues,inwardItemRow.item,inwardItemRow[attrName].value.attachmentValues[0])"></i>
                            </span>

                            <div class="attributeTooltiptext"
                                 ng-if="inwardItemRow[attrName].value.attachmentValues.length > 1">
                                <ul style="padding-left: 0">
                                    <li ng-repeat="attachment in inwardItemRow[attrName].value.attachmentValues">
                                        <a href="" ng-click="inwardDetailsVm.openPropertyAttachment(attachment)"
                                           title="Click to download attachment"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;cursor: pointer">
                                            {{attachment.name}}
                                        </a>
                                        <i class="fa fa-trash" title="Delete Certificate" style="cursor: pointer;"
                                           ng-if="!inwardItemRow.editMode && hasPermission('permission.inward.storeApprove') && (inwardItemRow.item.status == 'STORE_SUBMITTED' || inwardItemRow.item.provisionalAccept || inwardItemRow.item.status == 'REVIEW')"
                                           ng-click="inwardDetailsVm.deleteObjectAttachment(inwardItemRow[attrName].value.attachmentValues,inwardItemRow.item,attachment)">
                                        </i>
                                        <i class="fa fa-trash" title="Delete Certificate" style="cursor: pointer;"
                                           ng-if="!inwardItemRow.editMode && hasPermission('permission.inward.storeApprove') && (inwardItemRow.item.status == 'ACCEPT' || inwardItemRow.item.status == 'INVENTORY')"
                                           ng-click="inwardDetailsVm.deleteAttachmentAfterSubmit(inwardItemRow[attrName].value.attachmentValues,inwardItemRow.item,attachment)"></i>
                                    </li>
                                </ul>
                            </div>
                                <input ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'ATTACHMENT'"
                                   class="form-control" name="file" multiple="true" accept="application/pdf"
                                   type="file" ng-file-model="inwardItemRow[attrName].value.attachmentValues"/>
                            <span ng-if="inwardItemRow.type != 'INWARDITEM' && inwardItemRow.editInstance
                                         && objectAttribute.attributeDef.dataType == 'ATTACHMENT' && inwardItemRow[attrName].value.attachmentValues.length == 1">
                                {{inwardItemRow[attrName].value.attachmentValues[0].name}}
                            </span>

                            <ul style="padding-left: 0" ng-if="inwardItemRow.type != 'INWARDITEM' && inwardItemRow.editInstance
                                       && objectAttribute.attributeDef.dataType == 'ATTACHMENT' && inwardItemRow[attrName].value.attachmentValues.length > 1">
                                <li ng-repeat="attachment in inwardItemRow[attrName].value.attachmentValues">
                                    <a href="" ng-click="inwardDetailsVm.openPropertyAttachment(attachment)"
                                       title="Click to download attachment"
                                       style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;cursor: pointer">
                                        {{attachment.name}}
                                    </a>
                                </li>
                            </ul>
                        </div>

                        <input ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'IMAGE'"
                               class="form-control" name="file" type="file"
                               ng-file-model="inwardItemRow[attrName].value.imageValue"/>

                        <div ng-if="objectAttribute.attributeDef.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.attributeDef.name">
                            <a href="" ng-click="inwardDetailsVm.showImage(inwardItemRow[attrName].imageValue)"
                               title="Click to download attachment">
                                <img ng-if="inwardItemRow[attrName].imageValue != null"
                                     ng-src="{{inwardItemRow[attrName].imageValue}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="myModal2" class="img-model modal">
                                <span class="closeImage1">&times;</span>
                                <img class="modal-content" id="img03">
                            </div>
                        </div>

                        <span ng-if="objectAttribute.attributeDef.dataType != 'ATTACHMENT'
                                     && objectAttribute.attributeDef.dataType != 'IMAGE' && objectAttribute.attributeDef.dataType != 'CURRENCY'"
                              ng-init="attrName = objectAttribute.attributeDef.name">
                            <span ng-if="!inwardItemRow.editAfterSubmit && !inwardItemRow.editMode && objectAttribute.attributeDef.dataType == 'TEXT'">
                                {{inwardItemRow[attrName].value.stringValue}}
                            </span>
                            <span ng-if="!inwardItemRow.editAfterSubmit && !inwardItemRow.editMode && objectAttribute.attributeDef.dataType == 'INTEGER'">
                                {{inwardItemRow[attrName].value.integerValue}}
                            </span>
                            <span ng-if="!inwardItemRow.editAfterSubmit && !inwardItemRow.editMode && objectAttribute.attributeDef.dataType == 'DATE'">
                                {{inwardItemRow[attrName].value.dateValue}}
                            </span>
                            <span ng-if="!inwardItemRow.editAfterSubmit && !inwardItemRow.editMode && objectAttribute.attributeDef.dataType == 'DOUBLE'">
                                {{inwardItemRow[attrName].value.doubleValue}}
                            </span>
                             <span ng-if="!inwardItemRow.editAfterSubmit && !inwardItemRow.editMode && objectAttribute.attributeDef.dataType == 'TIME'">
                                {{inwardItemRow[attrName].value.timeValue}}
                            </span>
                            <input ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'TEXT'"
                                   type="text" class="form-control"
                                   placeholder="Enter {{objectAttribute.attributeDef.name}}"
                                   ng-model="inwardItemRow[attrName].value.stringValue"/>
                            <input ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'INTEGER'"
                                   type="number" class="form-control"
                                   placeholder="Enter {{objectAttribute.attributeDef.name}}"
                                   ng-model="inwardItemRow[attrName].value.integerValue"/>
                            <input ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'DOUBLE'"
                                   type="text" class="form-control"
                                   placeholder="Enter {{objectAttribute.attributeDef.name}}"
                                   ng-model="inwardItemRow[attrName].value.doubleValue"/>
                            <div class="input-group mb15" style="width: 100%;margin-bottom: 0px;"
                                 ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'DATE'">
                                <input irste-date-picker type="text" class="form-control"
                                       ng-if="objectAttribute.attributeDef.name == 'Manufacture Date'"
                                       placeholder="Select Date" style="width: 77%"
                                       ng-model="inwardItemRow[attrName].value.dateValue"/>
                                <input date-time-picker type="text" class="form-control"
                                       ng-if="objectAttribute.attributeDef.name != 'Manufacture Date'"
                                       placeholder="Select Date" style="width: 77%"
                                       ng-model="inwardItemRow[attrName].value.dateValue"/>
                                <button class="btn btn-xs btn-primary" style="width: 23%">
                                    <i class="fa fa-calendar" style="font-size: 20px;padding: 5px 0px;"></i>
                                </button>
                            </div>
                            <input ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && objectAttribute.attributeDef.dataType == 'TIME'"
                                   time-picker type="text" class="form-control"
                                   placeholder="Select {{objectAttribute.attributeDef.name}}"
                                   ng-model="inwardItemRow[attrName].timeValue"/>

                        </span>

                        <span ng-init="currencyType = objectAttribute.attributeDef.name+'type'"
                              ng-if="objectAttribute.attributeDef.dataType == 'CURRENCY'"
                              ng-bind-html="inward[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.attributeDef.name"
                              ng-if="objectAttribute.attributeDef.dataType == 'CURRENCY'">
                            {{inwardItemRow[attrName].value.currencyValue}}
                        </span>
                    </td>
                    <td class="oem-column">
                        <div style="display: inline-flex;">
                            <input type="checkbox" ng-model="inwardItemRow.item.hasExpiry" class="form-control"
                                   style="width: 20px;box-shadow: none;"
                                   ng-click="inwardDetailsVm.checkExpiryDate(inwardItemRow)"
                                   ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit)"/>
                            <input future-date-picker type="text" class="form-control"
                                   ng-if="inwardItemRow.type != 'INWARDITEM' && (inwardItemRow.editMode || inwardItemRow.editAfterSubmit) && inwardItemRow.item.hasExpiry"
                                   placeholder="Select Expiry Date" style="width: 90%"
                                   ng-model="inwardItemRow.item.expiryDate"/>
                        <span ng-if="inwardItemRow.type != 'INWARDITEM' && !inwardItemRow.editMode && !imwardItemRow.editAfterSubmit">
                            {{inwardItemRow.item.expiryDate}}
                        </span>
                        </div>
                    </td>
                    <td class="actions-column">
                        <div class="btn-group"
                             ng-if="inwardDetailsVm.inward.status == 'STORE' && hasPermission('permission.inward.storeApprove')">
                            <i class="fa fa-check-circle" style="font-size: 18px;" title="Create"
                               ng-click="inwardDetailsVm.createInwardItem(inwardItemRow)"
                               ng-if="!inwardItemRow.instancesCreated">
                            </i>

                            <i class="fa fa-trash" style="font-size: 18px;color:black !important;" title="Delete"
                               ng-click="inwardDetailsVm.deleteInwardItem(inwardItemRow)"
                               ng-if="inwardItemRow.type == 'INWARDITEM' && inwardItemRow.instancesCreated">
                            </i>

                            <%--<i class="fa fa-check-square-o" style="font-size: 18px;" title="Save All"
                                   ng-click="inwardDetailsVm.saveInwardItem(inwardItemRow)"
                                   ng-if="inwardItemRow.type == 'INWARDITEM' && inwardItemRow.instancesCreated && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots">
                                </i>--%>

                            <%--<i class="fa fa-check-circle" style="font-size: 18px;padding: 0 3px;" title="Create"
                               ng-if="!inwardItemRow.instancesCreated && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.showLotAllocationPanel(inwardItemRow,'NEW')">
                            </i>--%>
                            <%--<i class="fa fa-pencil" style="font-size: 18px;padding: 0 3px;" title="Edit"
                               ng-if="inwardItemRow.type == 'INWARDITEM' && inwardItemRow.instancesCreated && inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.showLotAllocationPanel(inwardItemRow,'EDIT')">
                            </i>--%>
                            <i class="fa fa-check-circle" style="font-size: 18px;padding: 0 3px;" title="Save"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.editMode"
                               ng-click="inwardDetailsVm.saveInwardItemInstance(inwardItemRow)">
                            </i>
                            <i class="fa fa-times-circle" style="font-size: 18px;" title="Cancel Changes"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.editMode"
                               ng-click="inwardDetailsVm.cancelInwardItemInstanceEdit(inwardItemRow)">
                            </i>
                            <i class="fa fa-pencil" style="font-size: 16px;" title="Edit"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.showCheckBox && !inwardItemRow.applyAllChecked
                                      && !inwardItemRow.editMode"
                               ng-click="inwardDetailsVm.editInwardItemInstance(inwardItemRow)">
                            </i>
                            <i class="fa fa-circle-o" style="font-size: 16px;padding: 0 3px;color: black"
                               title="Click to Select"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.showApplyAll && !inwardItemRow.applyAllChecked
                                      && !inwardItemRow.editMode && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.checkApplyAll(inwardItemRow)">
                            </i>
                            <i class="fa fa-dot-circle-o" style="font-size: 16px;padding: 0 3px;color: black"
                               title="Un Select"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.showApplyAll && inwardItemRow.applyAllChecked
                                      && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.unCheckApplyAll(inwardItemRow)">
                            </i>
                            <i class="fa fa-minus-circle" title="Remove Item"
                               style="font-size: 18px;padding: 0 4px;color: black"
                               ng-if="!inwardItemRow.instancesCreated"
                               ng-click="inwardDetailsVm.removeInwardItem(inwardItemRow)">
                            </i>
                            <i class="fa fa-square-o" style="font-size: 18px;padding: 0 4px;color: black"
                               title="Click to select"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.showCheckBox  && !inwardItemRow.checkBoxChecked
                                      && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.selectInstance(inwardItemRow)">
                            </i>
                            <i class="fa fa-check-square-o" style="font-size: 18px;padding: 0 4px;color: black"
                               title="Click to un-select"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.showCheckBox  && inwardItemRow.checkBoxChecked
                                      && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.unSelectInstance(inwardItemRow)">
                            </i>

                            <i class="fa fa-check-square" style="font-size: 18px;padding: 0 4px;color: black"
                               title="Click to Apply"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.showApplyAll && inwardItemRow.applyAllChecked && inwardDetailsVm.showApplyAllButton
                                      && inwardDetailsVm.selectedInstancesToApply.length > 0 && !inwardItemRow.bomItem.item.itemMaster.itemType.hasLots"
                               ng-click="inwardDetailsVm.applyAttributesToInstances(inwardItemRow)">
                            </i>
                        </div>
                        <div class="btn-group"
                             ng-if="inwardDetailsVm.inward.status != 'STORE'">

                            <i class="fa fa-check-square-o" title="Accept All"
                               ng-if="inwardItemRow.type == 'INWARDITEM' && !inwardItemRow.accepted && hasPermission('permission.inward.SSQAGApprove')"
                               ng-hide="inwardItemRow.hideAcceptAll || inwardItemRow.pAccepted"
                               style="color: white !important;font-size: 16px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.acceptInwardItem(inwardItemRow)">
                            </i>

                            <%--<i class="fa fa-shield" title="Provisional Accept All"
                               ng-if="inwardItemRow.type == 'INWARDITEM' && !inwardItemRow.pAccepted && hasPermission('permission.inward.SSQAGApprove')"
                               ng-hide="inwardItemRow.hidePAcceptAll || inwardItemRow.accepted"
                               style="color: orange !important;font-size: 16px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.provAcceptInwardItem(inwardItemRow)">
                            </i>--%>

                            <i class="fa fa-check" title="Accept Item"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.item.status == 'STORE_SUBMITTED'
                                      || inwardItemRow.item.status == 'REVIEWED') && inwardItemRow.latest"
                               ng-hide="!hasPermission('permission.inward.SSQAGApprove')"
                               style="font-size: 16px;padding: 0px 4px;"
                               ng-class="{'accept-icon':!inwardItemRow.item.review,'accept-icon-review':inwardItemRow.item.review}"
                               ng-click="inwardDetailsVm.acceptInwardItemInstance(inwardItemRow)">
                            </i>

                            <i class="fa fa-check" title="Accept Item"
                               ng-if="(inwardItemRow.item.status == 'INVENTORY' || inwardItemRow.item.status == 'VERIFIED') && inwardItemRow.item.provisionalAccept
                                        && inwardItemRow.latest"
                               ng-hide="!hasPermission('permission.inward.SSQAGApprove')"
                               style="font-size: 16px;padding: 0px 4px;"
                               ng-class="{'accept-icon':!inwardItemRow.item.review,'accept-icon-review':inwardItemRow.item.review}"
                               ng-click="inwardDetailsVm.acceptInwardItemInstanceLater(inwardItemRow)">
                            </i>

                            <%--<i class="fa fa-shield" title="Provisional Accept Item"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.item.status == 'STORE_SUBMITTED' || inwardItemRow.item.status == 'REVIEWED') && inwardItemRow.latest"
                               ng-hide="!hasPermission('permission.inward.SSQAGApprove')"
                               style="color: orange;font-size: 16px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.provAcceptInwardItemInstance(inwardItemRow)">
                            </i>

                            <i class="fa fa-reply" title="Reject Item"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.item.status == 'STORE_SUBMITTED' || inwardItemRow.item.status == 'REVIEWED') && inwardItemRow.latest"
                               ng-hide="!hasPermission('permission.inward.SSQAGApprove')"
                               style="color: darkred;font-size: 16px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.returnItemInstance(inwardItemRow,'REJECTED')">
                            </i>

                            <i class="fa fa-eye" title="Review Item"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.item.status == 'STORE_SUBMITTED' || inwardItemRow.item.status == 'REVIEWED') && inwardItemRow.latest"
                               ng-hide="!hasPermission('permission.inward.SSQAGApprove')"
                               style="color: #001e4c;font-size: 16px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.returnItemInstance(inwardItemRow,'REVIEW')">
                            </i>--%>

                            <i class="fa fa-pencil" style="font-size: 16px;color: black;" title="Edit"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editMode
                                      && (inwardItemRow.item.status == 'STORE_SUBMITTED' || inwardItemRow.item.provisionalAccept || inwardItemRow.item.status == 'REVIEW') && inwardItemRow.latest"
                               ng-hide="!hasPermission('permission.inward.storeApprove')"
                               ng-click="inwardDetailsVm.editInwardItemInstance(inwardItemRow)">
                            </i>

                            <%--<i class="fa fa-pencil" style="font-size: 16px;color: black;" title="Edit"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editMode
                                      && (inwardItemRow.item.status == 'REVIEW')"
                               ng-hide="!hasPermission('permission.inward.storeApprove')"
                               ng-click="inwardDetailsVm.editItemInstance(inwardItemRow)">
                            </i>--%>

                            <i class="fa fa-check-circle" style="font-size: 18px;padding: 0 3px;color: black;"
                               title="Save"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.editMode"
                               ng-click="inwardDetailsVm.saveInwardItemInstance(inwardItemRow)">
                            </i>
                            <i class="fa fa-times-circle" style="font-size: 18px;color: red;" title="Cancel Changes"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.editMode"
                               ng-click="inwardDetailsVm.cancelInwardItemInstanceEdit(inwardItemRow)">
                            </i>


                            <%---------------------  Edit For Store After Store Submission  ---------------%>

                            <i class="fa fa-pencil" style="font-size: 16px;" title="Edit"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editAfterSubmit && hasPermission('permission.inward.storeApprove') && hasPermission('permission.inward.edit')
                                      && (inwardItemRow.item.status == 'ACCEPT' || inwardItemRow.item.status == 'INVENTORY')"
                               ng-click="inwardDetailsVm.editInwardItemInstanceAfterSubmit(inwardItemRow)">
                            </i>

                            <i class="fa fa-check-circle" style="font-size: 18px;padding: 0 3px;color: black;"
                               title="Save"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.editAfterSubmit"
                               ng-click="inwardDetailsVm.saveInwardItemInstanceAfterSubmit(inwardItemRow)">
                            </i>
                            <i class="fa fa-times-circle" style="font-size: 18px;color: red;" title="Cancel Changes"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.editAfterSubmit"
                               ng-click="inwardDetailsVm.cancelInwardItemInstanceEditAfterSubmit(inwardItemRow)">
                            </i>

                        </div>

                        <div class="btn-group"
                             ng-if="inwardDetailsVm.inward.status != 'STORE' && hasPermission('permission.inward.storeApprove')">

                            <i class="fa fa-home" title="Allocate Storage"
                               ng-if="inwardItemRow.type == 'INWARDITEM'"
                               ng-hide="inwardItemRow.hideAllocateAll"
                               style="color: white !important;font-size: 18px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.allocateStorageToAll(inwardItemRow)">
                            </i>


                            <i class="fa fa-home" title="Allocate Storage"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && !inwardItemRow.editMode && !inwardItemRow.editAfterSubmit && (inwardItemRow.item.status == 'ACCEPT' || inwardItemRow.item.status == 'P_ACCEPT')
                                      && (inwardItemRow.item.status != 'REVIEW' && inwardItemRow.item.status != 'REJECTED') && inwardItemRow.latest"
                               style="color: black !important;font-size: 18px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.allocateStorage(inwardItemRow)">
                            </i>

                            <i class="fa fa-home" title="Allocate Storage to Reject Item"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.status == 'REJECTED' && !inwardItemRow.hasReturnStorage && inwardItemRow.latest"
                               style="color: black !important;font-size: 18px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.addStorageToReturnedItemInstance(inwardItemRow)">
                            </i>

                            <%--<i class="fa fa-thumbs-o-up" title="Verify Item"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.status == 'INVENTORY' && inwardItemRow.item.status != 'VERIFIED' && inwardItemRow.latest"
                               style="color: black;font-size: 16px;padding: 0px 4px;"
                               ng-click="inwardDetailsVm.verifyItem(inwardItemRow)">
                            </i>

                            <i class="fa fa-thumbs-up" title="Item Verified"
                               ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && inwardItemRow.item.status == 'VERIFIED' && inwardItemRow.latest"
                               style="color: black;font-size: 16px;padding: 0px 4px;">
                            </i>--%>
                            <%--<i class="fi-burst-new"></i>--%>
                        </div>
                        <i ng-if="inwardItemRow.type == 'INWARDITEMINSTANCE' && (inwardItemRow.item.status == 'P_ACCEPT' || inwardItemRow.item.status == 'REVIEW'
                                  || inwardItemRow.item.status == 'REVIEWED' ||  inwardItemRow.item.provisionalAccept || inwardItemRow.item.status == 'REJECTED')"
                           class="fa fa-comment" title="{{inwardItemRow.item.presentReason}}">
                        </i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>