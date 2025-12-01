<style>
    .multiselect-parent {
        width: 100%;
    }

    .multiselect-parent .dropdown-toggle {
        width: 100%;
    }

    .multiselect-parent .dropdown-menu {
        width: 100%;
    }

    .label {
        border-radius: 3px;
        font-size: 14px;
        padding: 3px;
        background: lightgrey;
        cursor: pointer;
        color: darkblue;
    }

    .label:hover {
        background: grey !important;
        cursor: pointer;
        text-decoration: underline;
    }

    .validate-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 15px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .validate-model .modal-content {
        margin-left: auto;
        margin-right: auto;
        top: 50px;
        display: block;
        height: 80%;
        width: 70%;
    }
</style>

<div ng-if="storageTreeDetails == true" style="padding: 10px">
    <span>No storage type to see/edit details</span>
</div>
<div ng-if="storageTreeDetails == false && storageDetailsVm.object == null" style="padding: 10px">
    <span>Select an storage type to see/edit details</span>
</div>
<div ng-if="storageDetailsVm.object != null" style="padding-left: 10px;">
    <div>
        <h4 class="section-title">
            <span class="section-btn">
                <a href="" ng-click="storageDetailsVm.toggleSection('basic')" style="text-decoration: none !important;">
                    <i class="fa"
                       ng-class="{'fa-plus-square': storageDetailsVm.sections.basic, 'fa-minus-square': !storageDetailsVm.sections.basic}"></i>
                    Basic Information
                </a>
            </span>
        </h4>
    </div>
    <div class="item-details" ng-show="!storageDetailsVm.sections.basic">
        <div class="row">
            <div class="col-md-7">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label ">Name<span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="title"
                                   ng-disabled="storageDetailsVm.storageParts.length > 0"
                                   ng-change="storageDetailsVm.onChangeName(storageDetailsVm.object)"
                                   ng-model="storageDetailsVm.object.name"
                                   ng-enter="" placeholder="Enter name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label ">Description : </label>

                        <div class="col-sm-8">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-model="storageDetailsVm.object.description"
                                  title="Enter Description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label ">BOM
                            <%--<span class="asterisk"> *</span>--%>
                            :
                        </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="storageDetailsVm.object.bom" theme="bootstrap" style="width:100%"
                                       ng-disabled="storageDetailsVm.storageParts.length > 0 || (storageDetailsVm.object.bom != null && storageDetailsVm.object.children.length > 0)
                                                    || (storageDetailsVm.object.parentData != null && storageDetailsVm.object.parentData.bom != null)">
                                <ui-select-match placeholder="Select BOM">
                                    {{$select.selected.item.itemMaster.itemName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bom in storageBOMs track by bom.id">
                                    <div ng-bind="bom.item.itemMaster.itemName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Is Leaf Node :</label>

                        <div class="col-sm-8" style="padding-top: 8px;text-align: left">
                            <input type="checkbox"
                                   class="form-control input-sm"
                                   style="width: 24px;"
                                   ng-model="storageDetailsVm.object.isLeafNode"
                                   ng-disabled="storageDetailsVm.object.children.length > 0 || storageDetailsVm.storageParts.length > 0">
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="storageDetailsVm.object.isLeafNode">
                        <label class="col-sm-4 control-label"> Parts :</label>

                        <div class="col-sm-8" style="padding-top: 8px;text-align: left">
                            <button class="btn btn-md btn-default" style="width: 100%;"
                                    ng-disabled="storageDetailsVm.object.bom == null"
                                    ng-click="storageDetailsVm.selectStorageParts()">
                                Select Parts
                            </button>
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="storageDetailsVm.object.isLeafNode">
                        <label class="col-sm-4 control-label">Total Capacity :</label>

                        <div class="col-sm-8" style="padding-top: 8px;text-align: left">
                            <input type="number"
                                   class="form-control input-sm"
                                   style="width: 100%;"
                                   ng-model="storageDetailsVm.object.capacity"
                                   ng-disabled="!storageDetailsVm.object.isLeafNode">
                        </div>
                    </div>

                    <div class="form-group"
                         ng-if="storageDetailsVm.object.isLeafNode && storageDetailsVm.object.id != null && storageDetailsVm.object.id != undefined">
                        <label class="col-sm-4 control-label ">Remaining Capacity :</label>

                        <div class="col-sm-8" style="padding-top: 8px;text-align: left">
                            <input type="number"
                                   class="form-control input-sm"
                                   style="width: 100%;"
                                   ng-model="storageDetailsVm.object.remainingCapacity" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label ">On Hold Storage :</label>

                        <div class="col-sm-8" style="padding-top: 8px;text-align: left">
                            <input type="checkbox" class="form-control input-sm" style="width: 24px;"
                                   ng-model="storageDetailsVm.object.onHold"
                                   ng-disabled="storageDetailsVm.storageParts.length > 0 || storageDetailsVm.object.parentData.onHold || storageDetailsVm.object.parentData.returned"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label ">Return Storage :</label>

                        <div class="col-sm-8" style="padding-top: 8px;text-align: left">
                            <input type="checkbox" class="form-control input-sm" style="width: 24px;"
                                   ng-model="storageDetailsVm.object.returned"
                                   ng-disabled="storageDetailsVm.storageParts.length > 0 || storageDetailsVm.object.parentData.onHold || storageDetailsVm.object.parentData.returned"/>
                        </div>
                    </div>

                    <%--<div class="form-group"
                         ng-if="storageDetailsVm.object.id != null && storageDetailsVm.object.id != undefined">
                        <label class="col-sm-4 control-label ">Barcode : </label>

                        <div class="col-sm-8">
                            <img ng-show="storageDetailsVm.barcodeImageLoading == true"
                                 src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">

                            <div id="storageImageId">
                                <p style="text-align:center;margin-bottom: 0px;width: 195px">
                                    {{storageDetailsVm.object.name}}</p>
                                <img ng-src="api/drdo/storage/barcode/{{storageDetailsVm.object.id}}?bust={{storageDetailsVm.barcodeBust}}"
                                     alt="Barcode" imageonload="storageDetailsVm.barcodeImageLoaded()"
                                     style="width: 200px;height: 50px;">
                            </div>
                            <button ng-click="storageDetailsVm.printDiv('storageImageId')" style="margin-left: 75px;"
                                    title="Click to print">Print
                            </button>
                        </div>

                    </div>--%>

                </form>
            </div>
            <div class="col-xs-12 col-sm-8 col-md-5">
                <style scoped>
                    .flex-view {
                        display: -webkit-box;
                        display: -moz-box;
                        display: -ms-flexbox;
                        display: -webkit-flex;
                        display: flex;
                        margin-bottom: 10px;
                        flex-wrap: wrap;
                        justify-content: center;
                    }

                    .type-container {
                        width: 90%;
                        height: 400px;
                        border: 1px solid #ddd;
                        border-radius: 5px;
                        padding: 10px;
                        max-height: 450px;
                        overflow-y: auto;
                    }
                </style>

                <div class="flex-view" ng-if="storageDetailsVm.object.isLeafNode">
                    <div class="type-container" style="overflow-y: auto;">
                        <h5>Selected Parts to Store</h5>

                        <p ng-if="storageDetailsVm.storageItems.length == 0">No storage parts</p>

                        <p ng-if="storageDetailsVm.storageItems.length > 0" style="font-style: italic;">
                            (Section - Item Name - Item Code)
                        </p>
                        <ul ng-repeat="storageItem in storageDetailsVm.storageItems | orderBy: 'item.item.itemMaster.itemName'"
                            style="padding-left: 10px;">
                            <li>
                                <i ng-if="storageDetailsVm.storageParts.length == 0" class="fa fa-minus-circle"
                                   ng-click="storageDetailsVm.deleteStorageItem(storageItem)"
                                   title="Click to remove" style="cursor: pointer;"></i>
                                <span ng-if="storageItem.section != null">{{storageItem.section.name}}
                                    <span ng-if="storageItem.section.versity">( VSPL )</span> - </span>
                                <span ng-if="storageItem.section == null">" Common " - </span>
                                {{storageItem.item.item.itemMaster.itemName}} -
                                {{storageItem.item.item.itemMaster.itemCode}}
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br>

    <div ng-if="storageDetailsVm.object.id != null && storageDetailsVm.object.id != undefined">
        <h4 class="section-title">
            <span class="section-btn">
                <a href="" ng-click="storageDetailsVm.toggleSection('inventory')"
                   style="text-decoration: none !important;">
                    <i class="fa"
                       ng-class="{'fa-plus-square': !storageDetailsVm.sections.inventory,'fa-minus-square': storageDetailsVm.sections.inventory}"></i>
                    Inventory
                </a>
            </span>
        </h4>
    </div>
    <div ng-show="storageDetailsVm.sections.inventory">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th>Nomenclature</th>
                <th>Item Code</th>
                <th>Type</th>
                <th style="text-align: center;">Inventory</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="storageDetailsVm.storageInventory.length == 0">
                <td colspan="15">No Parts</td>
            </tr>
            <tr ng-repeat="inventory in storageDetailsVm.storageInventory | orderBy:'item.itemMaster.itemType.name'">
                <td>{{inventory.item.itemMaster.itemName}} {{inventory.item.partSpec.specName}}</td>
                <td>{{inventory.item.itemMaster.itemCode}}</td>
                <td>{{inventory.item.itemMaster.parentType.name}}</td>
                <td style="text-align: center;">
                    <a title="Click to show UPN 's"
                       class="badge badge-primary"
                       uib-popover-template="storageDetailsVm.upnPopOver.templateUrl"
                       popover-append-to-body="true"
                       popover-popup-delay="50"
                       popover-placement="top-right"
                       popover-title="UPN 's"
                       popover-trigger="'outsideClick'">{{inventory.itemInstances.length}}
                    </a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <br>

    <div ng-if="storageDetailsVm.object.id != null && storageDetailsVm.object.id != undefined">
        <h4 class="section-title">
            <span class="section-btn">
                <a href="" ng-click="storageDetailsVm.toggleSection('items')" style="text-decoration: none !important;">
                    <i class="fa"
                       ng-class="{'fa-plus-square': !storageDetailsVm.sections.items,'fa-minus-square': storageDetailsVm.sections.items}"></i>
                    <span ng-if="storageDetailsVm.object.onHold == false && storageDetailsVm.object.returned == false">Storage Part Details</span>
                    <span style="font-size: 18px;font-weight: bolder;color: black;"
                          ng-if="storageDetailsVm.object.onHold == true">On Hold Part details</span>
                    <span style="font-size: 18px;font-weight: bolder;color: black;"
                          ng-if="storageDetailsVm.object.returned == true">Returned Part details</span>
                </a>
            </span>
        </h4>
    </div>
    <div ng-show="storageDetailsVm.sections.items">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th>Nomenclature</th>
                <th>Item Code</th>
                <th>Type</th>
                <th>UPN</th>
                <th>Serial Number</th>
                <th>Quantity</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="storageDetailsVm.storageParts.length == 0">
                <td colspan="15">No Parts</td>
            </tr>
            <tr ng-repeat="storeItem in storageDetailsVm.storageParts | orderBy:'item.itemMaster.itemType.name'">
                <td>{{storeItem.item.itemMaster.itemName}}</td>
                <td>{{storeItem.item.itemMaster.itemCode}}</td>
                <td>{{storeItem.item.itemMaster.parentType.name}}</td>
                <td>
                    <span class="badge" style="font-size: 14px;"
                          ng-if="!storeItem.item.itemMaster.itemType.hasLots && storeItem.upnNumber != null"
                          ng-class="{'badge-warning':storageDetailsVm.object.onHold,
                                     'badge-danger':storageDetailsVm.object.returned,
                                     'badge-success':!storageDetailsVm.object.onHold && !storageDetailsVm.object.returned}"
                          ng-click="showUpnHistory(storeItem,'right')" title="Click to part history">{{storeItem.upnNumber}}
                    </span>
                    <span class="badge" style="font-size: 14px;"
                          ng-if="storeItem.item.itemMaster.itemType.hasLots && storeItem.upnNumber != null"
                          ng-class="{'badge-warning':storageDetailsVm.object.onHold,
                                     'badge-danger':storageDetailsVm.object.returned,
                                     'badge-success':!storageDetailsVm.object.onHold && !storageDetailsVm.object.returned}"
                          ng-click="showLotUpnHistory(storeItem,'INWARD')" title="Click to part history">{{storeItem.upnNumber}}
                    </span>
                </td>
                <td>
                    <%--<span ng-if="storeItem.manufacturer != null">{{storeItem.manufacturer.mfrCode}} - {{storeItem.oemNumber}}</span>--%>
                    <span ng-if="storeItem.item.itemMaster.itemType.hasLots">{{storeItem.lotNumber}}</span>
                    <span ng-if="!storeItem.item.itemMaster.itemType.hasLots">{{storeItem.oemNumber}}</span>
                </td>
                <td>
                    <span ng-if="storeItem.item.itemMaster.itemType.hasLots">{{storeItem.lotSize - storeItem.lotIssuedQuantity}}</span>
                    <span ng-if="!storeItem.item.itemMaster.itemType.hasLots">1</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div id="validateStorage-view" class="validate-model modal">
    <style>
        .ui-select-bootstrap > .ui-select-match > .btn {
            width: 100% !important;
        }

        .ui-select-search {
            width: 100% !important;
        }

        .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: 0px;
            z-index: 5;
            background-color: #fff;
        }
    </style>
    <div class="modal-content">
        <h3 style="text-align: center;background: lavender;height: 10%;margin: 0px;padding-top: 10px;">Validate Storage
            With BOM</h3>

        <div style="height: 10%;border: 1px solid lightgrey;text-align: center;">
            <form class="form-inline" style="margin: 5px 0px 0px -50px;display: inline-flex;">
                <div class="form-group" style="width: 300px;">
                    <label class="col-sm-5 control-label" style="text-align: right;margin-top: 5px;">Select BOM
                        : </label>

                    <div class="col-sm-7">
                        <ui-select ng-model="storageDetailsVm.selectedBom" theme="bootstrap"
                                   on-select="storageDetailsVm.validateStorageByBom($item)" style="width:100%">
                            <ui-select-match placeholder="Select BOM">
                                {{$select.selected.item.itemMaster.itemName}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="bom in storageBOMs track by bom.id">
                                <div ng-bind="bom.item.itemMaster.itemName"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>
                <%--<div>
                    <button class="btn btn-success btn-sm"
                            ng-click="storageDetailsVm.validateStorageByBom()">Validate
                    </button>
                </div>--%>
            </form>
        </div>
        <div style="height: 70%;">
            <div class="responsive-table" style="overflow-y: auto;height: 100%;">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th class="threeHundred-column">Nomenclature</th>
                        <th>Type</th>
                        <th>On Hold Locations</th>
                        <th>Return Locations</th>
                        <th>Inventory Locations</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="storageItem in storageDetailsVm.selectedStorageItems">
                        <td class="threeHundred-column">
                            <p class="level{{storageItem.bomItem.level}}"
                               ng-if="storageItem.bomItem.bomItemType != 'PART'" style="margin:0;">
                                <i ng-if="storageItem.bomItem.children.length > 0" class="mr5 fa fa-caret-down"
                                   style="color: #909090;font-size: 18px;"
                                <%--ng-class="{'fa-caret-right': (storageItem.bomItem.expanded == false || storageItem.bomItem.expanded == null || storageItem.bomItem.expanded == undefined),
                                            'fa-caret-down': storageItem.bomItem.expanded == true}"--%>></i>
                                {{storageItem.bomItem.typeRef.name}}<span ng-if="storageItem.bomItem.typeRef.versity"> ( VSPL )</span>
                            </p>

                            <p class="level{{storageItem.bomItem.level}}"
                               ng-if="storageItem.bomItem.bomItemType == 'PART'" style="margin:0;">
                                {{storageItem.bomItem.item.itemMaster.itemName}}
                                {{storageItem.bomItem.item.partSpec.specName}}
                            </p>
                        </td>
                        <td>
                            <span ng-if="storageItem.bomItem.bomItemType == 'SECTION'" class="badge badge-primary"
                                  style="font-size: 13px;">SECTION</span>
                            <span ng-if="storageItem.bomItem.bomItemType == 'SUBSYSTEM'" class="badge badge-warning"
                                  style="font-size: 13px;">SUBSYSTEM</span>
                            <span ng-if="storageItem.bomItem.bomItemType == 'UNIT'"
                                  class="badge badge-success" style="font-size: 13px;">UNIT</span>
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART'" style="font-size: 14px;">{{storageItem.bomItem.item.itemMaster.parentType.name}}</span>
                        </td>
                        <td style="text-align: center;">
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART' && storageItem.onHoldStorages.length > 0">
                                    <a title="Click to show Locations"
                                       class="badge badge-primary"
                                       uib-popover-template="storageDetailsVm.onHoldLocationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="top-right"
                                       popover-title="Locations"
                                       popover-trigger="'outsideClick'" style="z-index: 10000 !important;">{{storageItem.onHoldStorages.length}}
                                    </a>
                            </span>
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART' && storageItem.onHoldStorages.length == 0"
                                  class="badge badge-primary" style="font-size: 13px;">0</span>
                        </td>
                        <td style="text-align: center;font-size: 13px;">
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART' && storageItem.returnStorages.length > 0">
                                    <a title="Click to show Locations"
                                       class="badge badge-warning"
                                       uib-popover-template="storageDetailsVm.returnLocationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="top-right"
                                       popover-title="Locations"
                                       popover-trigger="'outsideClick'" style="z-index: 10000 !important;">{{storageItem.returnStorages.length}}
                                    </a>
                            </span>
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART' && storageItem.returnStorages.length == 0"
                                  class="badge badge-warning" style="font-size: 13px;">0</span>
                        </td>
                        <td style="text-align: center;">
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART' && storageItem.inventoryStorages.length > 0">
                                    <a title="Click to show Locations"
                                       class="badge badge-success"
                                       uib-popover-template="storageDetailsVm.inventoryLocationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="top-right"
                                       popover-title="Locations"
                                       popover-trigger="'outsideClick'"
                                       style="z-index: 10000 !important;font-size: 13px;">{{storageItem.inventoryStorages.length}}
                                    </a>
                            </span>
                            <span ng-if="storageItem.bomItem.bomItemType == 'PART' && storageItem.inventoryStorages.length == 0"
                                  class="badge badge-success" style="font-size: 13px;">0</span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="height: 10%;text-align: center;background: lightgrey;">
            <button class="btn btn-sm btn-default" ng-click="storageDetailsVm.closeValidateStorageView()"
                    style="margin-top: 5px;">Close
            </button>
        </div>
    </div>
</div>
