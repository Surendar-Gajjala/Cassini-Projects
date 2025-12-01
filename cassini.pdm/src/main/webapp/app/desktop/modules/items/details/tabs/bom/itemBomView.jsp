<div class="text-center">
    <div style="padding: 10px;background-color: #f9f9f9;border: 1px solid #ddd;
        width: 700px;margin-left: auto;margin-right: auto;text-align: center;
        padding-top: 15px;border-radius: 5px;">
        <span class="mr10 text-primary" style="font-weight: bold">BOM Configuration : </span>

        <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
            <input name="bomRule" value="bom.latest" id="latestRevision" type="radio" checked
                   ng-model="itemBomVm.selectedBomRule"
                   ng-change="itemBomVm.loadBom()">
            <label for="latestRevision">Latest Revision</label>
        </div>

        <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
            <input name="bomRule" value="bom.latest.released" id="latestReleasedRevision" type="radio"
                   ng-model="itemBomVm.selectedBomRule"
                   ng-change="itemBomVm.loadBom()">
            <label for="latestReleasedRevision">Latest Released Revision</label>
        </div>

        <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
            <input name="bomRule" value="bom.as.released" id="asReleasedRevision" type="radio"
                   ng-model="itemBomVm.selectedBomRule"
                   ng-change="itemBomVm.loadBom()">
            <label for="asReleasedRevision">As Released Revision</label>
        </div>
    </div>
</div>
<br>

<style scoped>
    .scrollable-table {
        overflow-y: auto !important;
        max-height: 500px !important;
    }
</style>
<div class='responsive-table bom-table'>
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
    </style>
    <table class='table table-striped'>
        <thead>
        <tr>
            <th style="width: 24px;"></th>
            <th>Item Number</th>
            <th>Item Type</th>
            <th>Description</th>
            <th style="text-align: center">Revision</th>
            <th style="text-align: center">Lifecycle Phase</th>
            <th style="text-align: center">Quantity</th>
            <th style="text-align: center">Units</th>
            <th>Ref Des</th>
            <th>Notes</th>
            <th class="actions-col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%--<tr ng-if="itemBomVm.loading == true">
            <td colspan="10">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">Loading items...
                    </span>
            </td>
        </tr>--%>
        <tr ng-if="itemBomVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5">Loading items...
            </td>
        </tr>
        <tr ng-if="itemBomVm.loading == false && itemBomVm.bomItems.length == 0">
            <td colspan="14">No items</td>
        </tr>
        <tr ng-repeat="item in itemBomVm.bomItems" ng-class="{'selected': item.selected,
                'unresolved-bom-item ': item.itemRevision.revision == '?'}">
            <td style="width: 24px; background-color: #ddd; cursor: pointer" ng-click="itemBomVm.selectItem(item)"></td>

            <td>
                <span class="level{{item.level}}">
                    <i ng-if="item.itemRevision.hasBom && item.isNew == false" class="mr5 fa"
                       style="cursor: pointer; color: #909090"
                       ng-class="{'fa-plus-square': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                       'fa-minus-square': item.expanded == true}"
                       ng-click="itemBomVm.toggleNode(item)"></i>
                    <a ui-sref="app.items.details({itemId: item.itemRevision.id})"
                       ng-if="item.itemRevision.id != undefined && item.isNew == false && item.editItemNumber == false">
                        <span ng-class="{'ml20': item.itemRevision.hasBom == false}">{{item.item.itemNumber}}</span>
                    </a>
                    <span ng-class="{'ml20': item.itemRevision.hasBom == false}"
                          ng-if="item.itemRevision.id == undefined">{{item.item.itemNumber}}</span>
                    <span ng-if="item.isNew == true && item.editItemNumber == true">
                        <div class="input-group">
                            <div class="input-group-addon" ng-click="itemBomVm.itemSelection(item)">
                                <i class="fa fa-search"></i>
                            </div>
                            <input style="width: 150px;margin: 0" type="text" class="form-control input-sm"
                                   name="itemNumber"
                                   ng-model="item.itemNumber" ng-blur="itemBomVm.findItem(item)">
                        </div>
                    </span>
                </span>
            </td>
            <td>{{item.item.itemType.name}}</td>
            <td>{{item.item.description}}</td>
            <td style="text-align: center;">
                <span class='label label-danger' style="padding-top: 6px;"
                      ng-if="item.itemRevision.revision == '?'">
                    <i class="fa fa-question" style="font-size: 14px;margin-top: 5px;"></i>
                </span>
                <span ng-if="item.itemRevision.revision != '?'">{{item.itemRevision.revision}}</span>

            </td>
            <td style="text-align: center">
                <span class='label label-danger' style="padding-top: 6px;"
                      ng-if="item.itemRevision.lifeCyclePhase.phase == '?'">
                    <i class="fa fa-question" style="font-size: 14px;margin-top: 5px;"></i>
                </span>
                <item-status ng-if="item.itemRevision.lifeCyclePhase.phase != '?'"
                             item="item.itemRevision">
                </item-status>
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
            <td style="text-align: center">{{item.item.units}}</td>
            <td>
                <span ng-if="item.isNew != true && item.editMode != true">{{item.refdes}}</span>
                <span ng-if="item.isNew == true || item.editMode == true">
                    <input style="width: 150px;margin: 0" type="text" class="form-control input-sm" name="refdes"
                           ng-model="item.newRefdes">
                </span>
            </td>
            <td>
                <span ng-if="item.isNew != true && item.editMode != true">{{item.notes}}</span>
                <span ng-if="item.isNew == true || item.editMode == true">
                    <input style="width: 150px;margin: 0" type="text" class="form-control input-sm" name="notes"
                           ng-model="item.newNotes">
                </span>
            </td>
            <td <%--class="actions-col"--%>>
                <span class="btn-group" ng-if="item.isNew == true || item.editMode == true" style="margin: 0">
                    <button class="btn btn-xs btn-default" type="button"
                            ng-click="itemBomVm.onOk(item)">
                        <i class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-xs btn-default" type="button"
                            ng-click="itemBomVm.onCancel(item)">
                        <i class="fa fa-times"></i>
                    </button>
                </span>

                <span ng-if="item.editMode == false || item.flag == true">
                    <button title="Edit this item" class="btn btn-xs btn-warning"
                            ng-click="itemBomVm.editItem(item)"
                            ng-disabled="item.disableEdit">
                        <i class="fa fa-edit"></i>
                    </button>

                    <button title="Delete this item" class="btn btn-xs btn-danger"
                            ng-click="itemBomVm.deleteItem(item)"
                            ng-disabled="item.disableEdit">
                        <i class="fa fa-trash"></i>
                    </button>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>