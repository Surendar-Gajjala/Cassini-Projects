<style scoped>

    .drop-down-menu a {
        display: block;
        position: relative;
        z-index: 0;
    }

    .drop-down-menu li {
        z-index: auto;
    }

    .drop-down-menu ul {
        z-index: 6;
    }

    .table tbody > tr > td {
        vertical-align: middle;
        padding: 0;
    }

    .actions-columns {
        width: 50px;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
        text-align: left;
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

    .img-model .closeImageInv {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImageInv:hover,
    .img-model .closeImageInv:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .img-model.modal1 {
        display: block; /* Hidden by default */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
    }

</style>

<div>
    <div>
        <div class="responsive-table" style="overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="min-width: 50px;"></th>
                    <th style="min-width: 150px;">Item Number</th>
                    <th style="min-width: 250px;">Item Name</th>
                    <th style="min-width: 250px;">Item Description</th>
                    <th style="min-width: 250px;">Item Type</th>
                    <%--<th style="width: 200px;">Boq Name</th>--%>
                    <th style="min-width: 150px;">Units</th>
                    <th style="min-width: 100px; text-align: center;">Received Qty</th>
                    <th style="min-width: 100px; text-align: center;">Inventory Qty</th>
                    <th style="min-width: 100px; text-align: center;">Issued Qty</th>
                    <th style="min-width: 100px; text-align: center;">Returned Qty</th>
                    <th style="min-width: 150px;text-align: center" class='added-column'
                        ng-repeat="inventoryAttribute in storeInventoryVm.inventoryAttributes">
                        {{inventoryAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="storeInventoryVm.removeAttribute(inventoryAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th class="actions-columns">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width: 50px;"></td>
                    <td style="text-align: center; width: 150px;">
                        <input type="text" style="height: 30px;" class="btn-sm form-control" name="Item Number"
                               ng-change="storeInventoryVm.applyFilters()" placeholder="Number"
                               ng-model="storeInventoryVm.emptyFilters.itemNumber">
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" style="height: 30px;" class="btn-sm form-control" name="Item Number"
                               ng-change="storeInventoryVm.applyFilters()" placeholder="Item Name"
                               ng-model="storeInventoryVm.emptyFilters.itemName">
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" style="height: 30px;" class="btn-sm form-control" name="Item Description"
                               ng-change="storeInventoryVm.applyFilters()" placeholder="Item Description"
                               ng-model="storeInventoryVm.emptyFilters.itemDescription">
                    </td>

                    <td style="text-align: center; width: 200px;">
                        <div class="input-group">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn-sm btn-default dropdown-toggle"
                                        style="background-color: white; border: 1px #bbbbbb solid">
                                    ItemType <span class="caret" style="margin-left: 4px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="padding: 5px; border: 1px solid #FFF;height: auto;height: auto; max-height:200px; overflow-x: auto">
                                        <div class="classification-pane">
                                            <ul id="classificationTree1" class="easyui-tree"
                                                on-select="storeInventoryVm.onSelectType"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="btn-sm form-control" name="title"
                                   style="width: auto; background-color: white; height:30px;"
                                   ng-model="storeInventoryVm.itemType.attributes.typeObject.name"
                                   readonly>
                        </div>
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" style="height: 30px;" class="btn-sm form-control" name="Units"
                               ng-change="storeInventoryVm.applyFilters()" placeholder="Units"
                               ng-model="storeInventoryVm.emptyFilters.units">
                    </td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td ng-repeat="taskAttribute in storeInventoryVm.inventoryAttributes">
                        <input ng-if="taskAttribute.dataType == 'TEXT' " type="text" style="height: 30px;"
                               class="btn-sm form-control"
                               ng-change="storeInventoryVm.attributeFilter(taskAttribute)"
                               ng-model="taskAttribute.textValue">
                        <input ng-if="taskAttribute.dataType == 'INTEGER' " type="number" style="height: 30px;"
                               class="btn-sm form-control"
                               ng-change="storeInventoryVm.attributeFilter(taskAttribute)"
                               ng-model="taskAttribute.integerValue">
                        <input type="text" ng-if="taskAttribute.objectTypeAttribute.dataType == 'DOUBLE'"
                               ng-model="taskAttribute.doubleValue" class="form-control"
                               ng-change="storeInventoryVm.attributeFilter(taskAttribute)">
                        <%--<input type="date" ng-if="taskAttribute.dataType == 'DATE'"--%>
                        <%--style="padding: 0 !important; line-height: 32px;"--%>
                        <%--ng-model="taskAttribute.dateValue"--%>
                        <%--class="form-control" id="inpDate" name="inpDate"--%>
                        <%--ng-change="storeInventoryVm.attributeFilter(taskAttribute)">--%>
                        <%--<input type="text" ng-if="taskAttribute.dataType == 'TIME'" time-picker--%>
                        <%--style="padding: 0 !important; line-height: 32px;"--%>
                        <%--ng-model="taskAttribute.timeValue"--%>
                        <%--class="form-control"--%>
                        <%--ng-change="storeInventoryVm.attributeFilter(taskAttribute)">--%>

                        <div ng-if="taskAttribute.dataType == 'LIST'"
                             style="padding-right: 0px;">
                            <select class="form-control" ng-model="taskAttribute.listValue"
                                    placeholder="select" style="padding: 6px !important;"
                                    ng-change="storeInventoryVm.attributeFilter(taskAttribute)"
                                    ng-options="value for value in taskAttribute.lov.values">
                            </select>
                        </div>
                        <div ng-if="taskAttribute.dataType == 'BOOLEAN'">
                            <select class="form-control" ng-model="taskAttribute.booleanValue"
                                    style="padding: 6px !important;"
                                    placeholder="select" ng-change="storeInventoryVm.attributeFilter(taskAttribute)"
                                    ng-options="value for value in ['true', 'false']">
                            </select>
                        </div>
                        <div ng-if="taskAttribute.dataType == 'CURRENCY'">
                            <input class="form-control" name="currencyValue" type="number"
                                   placeholder="Enter currency value"
                                   ng-change="storeInventoryVm.attributeFilter(taskAttribute)"
                                   ng-model="taskAttribute.currencyValue"/>
                        </div>
                    </td>

                    <td class="actions-col" style="width:120px; text-align: left;">
                        <div class="btn-group btn-group-md" style="margin-top: 5px;">

                            <button title="Apply Filters" type="button" class="btn btn-xs btn-success"
                                    ng-click="storeInventoryVm.applyFilters()">
                                <i class="fa fa-search"></i>
                            </button>

                            <button title="Clear Filters" type="button" class="btn btn-xs btn-default"
                                    ng-click="storeInventoryVm.resetPage()">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </td>

                </tr>
                <tr ng-if="storeInventoryVm.loading == true">
                    <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Inventory..
                            </span>
                    </td>
                </tr>
                <tr ng-if="storeInventoryVm.loading == false && storeInventoryList.content.length == 0">
                    <td colspan="15">No Inventory data</td>
                </tr>
                <tr ng-repeat="row in storeInventoryList.content" ng-if="storeInventoryVm.loading == false">
                    <td ng-if="row.stockMovementDTO.itemDTO.resourceType == 'MATERIALTYPE' "
                        style="width: 50px; text-align: center"><img ng-src="app/assets/images/cart.png"
                                                                     style=" height:16px; width:16px;"></td>
                    <td ng-if="row.stockMovementDTO.itemDTO.resourceType == 'MACHINETYPE' "
                        style="width: 50px; text-align: center"><img ng-src="app/assets/images/machine2.png"
                                                                     style="height:16px; width:16px;"></td>
                    <td style="width: 150px;">{{row.stockMovementDTO.itemDTO.itemNumber}}</td>
                    <td style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{row.stockMovementDTO.itemDTO.itemName}}">
                        {{row.stockMovementDTO.itemDTO.itemName}}
                    </td>

                    <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{row.stockMovementDTO.itemDTO.description}}">
                        <span>{{row.stockMovementDTO.itemDTO.description}}</span>

                    </td>
                    <td style="width: 200px;">{{row.stockMovementDTO.itemDTO.itemType}}</td>
                    <td style="width: 200px;">{{row.stockMovementDTO.itemDTO.units}}</td>
                    <td style="width: 200px; text-align: center">{{row.stockMovementDTO.receivedQty}}</td>
                    <td style="width: 200px; text-align: center">{{row.storeOnHand}}</td>
                    <td style="width: 200px; text-align: center">{{row.stockMovementDTO.issuedQty}}</td>
                    <td style="width: 200px; text-align: center">{{row.stockMovementDTO.returnedQty}}</td>
                    <td class="added-column" style="text-align: center"
                        ng-repeat="objectAttribute in storeInventoryVm.inventoryAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                               title="{{row[attrName].length}}">
                                <a ng-if="row[attrName].length > 0" href="">
                                    {{row[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in row[attrName]">
                                        <a href="" ng-click="storeInventoryVm.openAttachment(attachment)"
                                           title="Click to download file"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;"
                                           title="{{attachment.name}}">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="storeInventoryVm.showImage(row[attrName])"
                               title="Click to show large Image">
                                <img ng-if="row[attrName] != null"
                                     ng-src="{{row[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>


                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name" title="{{row[attrName]}}">
                            {{row[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name" title="{{row[attrName]}}">
                            {{row[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="row[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" title="{{row[attrName]}}">
                            {{row[attrName]}}
                        </span>
                    </td>

                    <div id="invModal" class="img-model modal" style="z-index: 11">
                        <span class="closeImageInv">&times;</span>
                        <img class="modal-content" id="invImg">
                    </div>
                    <td class="actions-col"></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>