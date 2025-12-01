<div>
    <style scoped>

        .table tbody > tr > td {
            vertical-align: middle;
        }

        .drop-down-menu a {
            display: block;
            position: relative;
            z-index: 0;
        }

        .tree li {
            white-space: nowrap;
            color: #0f0202e6;
        }

        .drop-down-menu li {
            z-index: auto;
        }

        .drop-down-menu ul {
            z-index: 6;
        }

        .ui-select-match .btn {
            line-height: 14px !important;
        }

        .actions-columns {
            width: 50px;
        }

        table .ui-select-choices {
            position: absolute !important;
            top: auto !important;
            left: auto !important;
            width: auto !important;
        }

        .img-model .closeImage1 {
            position: absolute;
            top: 50px;
            right: 50px;
            color: black;
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

        .img-model .closeImage002 {
            position: absolute;
            top: 50px;
            right: 50px;
            color: black;
            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage002:hover,
        .img-model .closeImage002:focus {
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
    <div class="responsive-table" style="overflow: auto;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th></th>
                <th style="min-width: 150px;">Item Number</th>
                <th style="min-width: 250px;">Item Name</th>
                <th style="min-width: 250px;">Item Description</th>
                <th style="min-width: 250px;">Item Type</th>
                <th style="min-width: 250px;">Project Name</th>
                <th style="min-width: 250px;">Type</th>
                <th style="min-width: 250px;">Reference</th>
                <th style="min-width: 100px;">Units</th>
                <th style="min-width: 100px;">Qty</th>
                <th style="min-width: 150px;">Timestamp</th>
                <th style="min-width: 150px;text-align: center" class='added-column'
                    ng-repeat="inventoryAttribute in stockMovementVm.stockAttributes">
                    {{inventoryAttribute.name}}
                    <i class="fa fa-times-circle" style="cursor: pointer;"
                       ng-click="stockMovementVm.removeAttribute(inventoryAttribute)"
                       title="Remove this column"></i>
                </th>
                <th class="actions-columns">Actions</th>
            </tr>
            </thead>
            <tbody>

            <tr>
                <td style="width: 50px;"></td>
                <td style="text-align: center; width: 150px;">
                    <input type="text" style="height: 30px;" class="form-control btn-sm" name="Item Number"
                           ng-change="stockMovementVm.applyFilters()" placeholder="Number"
                           ng-model="stockMovementVm.emptyFilters.itemNumber">
                </td>

                <td style="text-align: center; width: 150px;">
                    <input type="text" class="form-control btn-sm" name="Store Name"
                           ng-change="stockMovementVm.applyFilters()" placeholder="Item Name"
                           ng-model="stockMovementVm.emptyFilters.itemName">
                </td>

                <td style="text-align: center; width: 150px;">
                    <input type="text" class="form-control btn-sm" name="Description"
                           ng-init="filed = description" style=""
                           ng-change="stockMovementVm.applyFilters()" placeholder="Description"
                           ng-model="stockMovementVm.emptyFilters.description">
                </td>

                <td style="text-align: center; width: 200px;">
                    <div class="input-group">
                        <div class="input-group-btn" uib-dropdown>
                            <button uib-dropdown-toggle class="btn btn-default dropdown-toggle btn-sm" type="button">
                                ItemType <span class="caret" style="margin-left: 4px;"></span>
                            </button>
                            <div class="dropdown-menu" role="menu" style="width: 280px;">
                                <div class="classification-pane"
                                     style="padding: 5px; border: 1px solid #FFF; height: auto; max-height:200px; overflow-x: auto">
                                    <ul id="classificationTree" class="easyui-tree"
                                        on-select="stockMovementVm.onSelectType"></ul>
                                </div>
                            </div>
                        </div>
                        <input type="text" class="form-control btn-sm" name="title" style="width: auto;"
                               ng-model="stockMovementVm.itemType.attributes.typeObject.name"
                               readonly>
                    </div>
                </td>

                <td style="text-align: center; width: 150px;">
                    <input type="text" class="form-control btn-sm" name="Created By"
                           ng-change="stockMovementVm.applyFilters()" placeholder="Project Name"
                           ng-model="stockMovementVm.emptyFilters.projectName">
                </td>

                <td style="text-align: center; width: 150px;">
                    <ui-select ng-model="stockMovementVm.emptyFilters.movementType1"
                               on-select="stockMovementVm.applyFilters()"
                               theme="bootstrap">
                        <ui-select-match placeholder="Select Type">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices repeat="itemType in stockMovementVm.types | filter: $select.search">
                            <div ng-bind="itemType | highlight: $select.itemType.search"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>

                <td></td>
                <td style="text-align: center; width: 150px;">
                    <input type="text" class="form-control btn-sm" name="Created By"
                           ng-change="stockMovementVm.applyFilters()" placeholder="Units"
                           ng-model="stockMovementVm.emptyFilters.units">
                </td>
                <td></td>
                <td></td>

                <td style="width: 150px;"
                    ng-repeat="requiredAttribute in stockMovementVm.stockAttributes">

                </td>

                <td class="actions-columns" style="width:120px; text-align: left;">
                    <div class="btn-group btn-group-md" style="margin-top: 5px;">

                        <button title="Apply Filters" type="button" class="btn btn-xs btn-success"
                                ng-click="stockMovementVm.applyFilters()">
                            <i class="fa fa-search"></i>
                        </button>

                        <button title="Clear Filters" type="button" class="btn btn-xs btn-default"
                                ng-click="stockMovementVm.resetPage()">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </td>

            </tr>

            <tr ng-if="stockMovementVm.loading == true">
                <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Stock Movements..
                            </span>
                </td>
            </tr>
            <tr ng-if="stockMovementVm.loading == false && storeStockMovementList.content.length == 0">
                <td colspan="15">No Stock Movement Data available to view</td>
            </tr>
            <tr ng-repeat="row in storeStockMovementList.content" ng-if="stockMovementVm.loading == false">
                <td ng-if="row.itemDTO.resourceType == 'MATERIALTYPE' " style="width: 50px; text-align: center">
                    <img ng-src="app/assets/images/cart.png" style="height:16px; width:16px;">
                </td>
                <td ng-if="row.itemDTO.resourceType == 'MACHINETYPE' " style="width: 50px; text-align: center">
                    <img ng-src="app/assets/images/machine2.png"
                         style="height:16px; width:16px;"></td>
                <td>{{row.itemDTO.itemNumber}}</td>
                <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{row.itemDTO.itemName}}">{{row.itemDTO.itemName}}
                </td>
                <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{row.itemDTO.description}}">
                    {{row.itemDTO.description}}
                </td>
                <td>{{row.itemDTO.itemType}}</td>
                <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                    title="{{row.projectObject.name}}">{{row.projectObject.name}}
                </td>
                <td>
                    <span ng-if="row.movementType == 'ISSUED'"
                          style="background-color: #28a745;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'RECEIVED'"
                      style="background-color: orange;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'LOANRECEIVED'"
                      style="background-color: #F6D155;color:white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'LOANISSUED'"
                      style="background-color: #92B558;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'LOANRETURNITEMISSUED'"
                      style="background-color: greenyellow;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'LOANRETURNITEMRECEIVED'"
                      style="background-color: rosybrown;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'ALLOCATED'"
                      style="background-color: darkolivegreen;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                <span ng-if="row.movementType == 'OPENINGBALANCE'"
                      style="background-color: #0c0b3c;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
               <span ng-if="row.movementType == 'RETURNED'"
                     style="background-color: royalblue;color: white;border-radius: 3px;padding: 2px 8px">{{row.movementType}}</span>
                </td>
                <td>{{row.reference}}</td>
                <td>{{row.itemDTO.units}}</td>
                <td style="text-align: center">{{row.quantity}}</td>
                <td>{{row.timeStamp}}</td>
                <td class="added-column" style="text-align: center"
                    ng-repeat="objectAttribute in stockMovementVm.stockAttributes">

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
                                    <a href="" ng-click="stockMovementVm.openAttachment(attachment)"
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
                        <a href="" ng-click="stockMovementVm.showImage(row[attrName])"
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

                <div id="movModal" class="img-model modal" style="z-index: 11">
                    <span class="closeImage1">&times;</span>
                    <img class="modal-content" id="movImg">
                </div>
                <td class="actions-columns"></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>