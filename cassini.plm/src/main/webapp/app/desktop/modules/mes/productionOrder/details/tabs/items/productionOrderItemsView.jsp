<div>
    <style scoped>
        table {
            table-layout: fixed;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }

        .project-progress {
            margin-bottom: 10px;
        }

        .project-progress .progress-label {
            font-weight: 300;
            color: #707d91;
        }

        .project-progress .progress-percent {
            width: 30px;
            margin-left: 20px;
            margin-top: -8px;
            font-weight: 400;
            text-align: right;
        }

        .ml8 {
            margin-left: 8px;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <i class="la la-plus" title="Add Items"
                       ng-if="hasPermission('productionorder','edit') && !productionOrder.approved && !productionOrder.rejected"
                       ng-click="productionOrderItemsVm.addItems()"></i>
                </th>
                <th translate>NUMBER</th>
                <th class="col-width-200" translate>NAME</th>
                <th style="width: 50px;text-align: center">Qty</th>
                <th translate>SERIAL_NUMBER</th>
                <th translate>BATCH_NUMBER</th>
                <th translate>DESCRIPTION</th>
                <th translate>STATUS</th>
                <th translate>START_DATE</th>
                <th translate>FINISH_DATE</th>
                <th class="actions-col" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="productionOrderItemsVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_ITEMS</span>
                </td>
            </tr>
            <tr ng-if="productionOrderItemsVm.loading == false && productionOrderItemsVm.productionOrderItems.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_ITEMS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="productionOrderItem in productionOrderItemsVm.productionOrderItems">
                <td></td>
                <td style="width: 1% !important;white-space: nowrap;">
                    <span class="level{{productionOrderItem.level}}">
                        <i ng-if="productionOrderItem.children.length > 0 && productionOrderItem.editMode == false"
                           class="fa" style="cursor: pointer;margin-right: 5px;"
                           ng-class="{'fa-caret-right': (productionOrderItem.expanded == false || productionOrderItem.expanded == null || productionOrderItem.expanded == undefined),
                                      'fa-caret-down': productionOrderItem.expanded == true}"
                           ng-click="productionOrderItemsVm.toggleNode(productionOrderItem)"></i>
                        <a href="" ng-click="productionOrderItemsVm.showDetails(productionOrderItem)"
                           title="Click to show details"
                           ng-bind-html="productionOrderItem.number | highlightText: productionOrderItemsVm.searchText"
                           ng-class="{'ml8': productionOrderItem.children.length == 0}"></a>
                    </span>
                </td>
                <td class="col-width-200">
                    <input type="text" class="form-control" ng-model="productionOrderItem.name"
                           placeholder="Enter name"
                           ng-if="productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'"/>
                    <span ng-if="productionOrderItem.objectType == 'PRODUCTIONORDERITEM'">{{productionOrderItem.name}} - {{productionOrderItem.revision}}</span>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'">{{productionOrderItem.name}}</span>
                </td>
                <td style="width: 50px;text-align: center">
                    <span ng-if="productionOrderItem.objectType == 'PRODUCTIONORDERITEM' && !productionOrderItem.editMode">
                        {{productionOrderItem.quantityProduced}}
                    </span>
                    <input type="text" class="form-control" ng-model="productionOrderItem.quantityProduced"
                           placeholder="Enter quantity" style="width: 100px;"
                           ng-if="productionOrderItem.editMode && productionOrderItem.objectType == 'PRODUCTIONORDERITEM'"
                           numbers-only/>
                    <span ng-if="productionOrderItem.objectType == 'MBOMINSTANCE'">1</span>
                </td>
                <td class="col-width-200">
                    <input type="text" class="form-control" ng-model="productionOrderItem.serialNumber"
                           placeholder="Enter serial number"
                           ng-if="productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'"/>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'PRODUCTIONORDERITEM'">{{productionOrderItem.serialNumber}}</span>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'">{{productionOrderItem.serialNumber}}</span>
                </td>
                <td class="col-width-200">
                    <input type="text" class="form-control" ng-model="productionOrderItem.batchNumber"
                           placeholder="Enter batch number"
                           ng-if="productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'"/>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'PRODUCTIONORDERITEM'">{{productionOrderItem.batchNumber}}</span>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'">{{productionOrderItem.batchNumber}}</span>
                </td>
                <td class="col-width-300">
                    <input type="text" class="form-control" ng-model="productionOrderItem.description"
                           placeholder="Enter description"
                           ng-if="productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'"/>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'PRODUCTIONORDERITEM'">{{productionOrderItem.description}}</span>
                    <span ng-if="!productionOrderItem.editMode && productionOrderItem.objectType == 'MBOMINSTANCE'">{{productionOrderItem.description}}</span>
                </td>
                <td>{{productionOrderItem.status}}</td>
                <td>{{productionOrderItem.startDate}}</td>
                <td>{{productionOrderItem.finishDate}}</td>
                <td class="text-center">
                    <span class="btn-group" ng-if="productionOrderItem.editMode == true" style="margin: 0">
                        <i title="{{'SAVE' | translate}}" style="cursor: pointer"
                           ng-click="productionOrderItemsVm.saveItem(productionOrderItem)"
                           class="la la-check">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="productionOrderItem.isNew" style="cursor: pointer"
                           ng-click="productionOrderItemsVm.removeItem(productionOrderItem)"
                           class="la la-times">
                        </i>
                        <i title="{{'CANCEL' | translate}}" ng-if="!productionOrderItem.isNew" style="cursor: pointer"
                           ng-click="productionOrderItemsVm.cancelChanges(productionOrderItem)"
                           class="la la-times">
                        </i>
                    </span>
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                          ng-if="productionOrderItem.editMode == false && hasPermission('productionorder','edit') && !productionOrder.approved && !productionOrder.rejected">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="hasPermission('productionorder','edit')">
                            <a href=""
                               ng-click="productionOrderItemsVm.editItem(productionOrderItem)"
                               translate>EDIT</a>
                        </li>
                        <li ng-if="hasPermission('productionorder','delete')">
                            <a href=""
                               ng-click="productionOrderItemsVm.deleteItem(productionOrderItem)">
                                <span translate>DELETE</span>
                            </a>
                        </li>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>