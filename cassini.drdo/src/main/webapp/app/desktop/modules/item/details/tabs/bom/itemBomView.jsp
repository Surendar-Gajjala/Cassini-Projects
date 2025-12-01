<style>
    .description {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal !important;
        text-align: left;
    }
</style>
<div class="responsive-table bom-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 24px;"></th>
            <th>Nomenclature</th>
            <th>Type</th>
            <th class="description">Description</th>
            <th>Quantity</th>
            <th>Units</th>
            <th>Drawing Number</th>
            <th style="width: 70px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%--<tr ng-if="selectedItemRevisionDetails.itemMaster.itemType.hasBom">
            <td style="background: none!important;">
                <i class="fa fa-plus-circle" title="Add New" style="cursor: pointer;font-size: 16px;color: blue"
                   ng-click="itemBomVm.addItemToBom(selectedItemRevisionDetails)"></i>
            </td>
            <td colspan="25">{{selectedItemRevisionDetails.itemMaster.itemName}} BOM</td>
        </tr>--%>
        <tr ng-if="itemBomVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Items...</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="itemBomVm.loading == false && itemBomVm.bomItems.length == 0">
            <td colspan="25">No Items</td>
        </tr>

        <tr ng-repeat="item in itemBomVm.bomItems" ng-class="{'selected': item.selected}">
            <td style="background: none !important;">
                <span ng-if="item.bomItemType == 'SECTION'" class="row-menu" uib-dropdown dropdown-append-to-body>
                   <i class="fa fa-plus-circle dropdown-toggle" uib-dropdown-toggle title="Add New"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href ng-click="itemBomVm.createBomGroupType(item,'SUBSYSTEM')">Add Sub System</a>
                            </li>
                            <li><a href ng-click="itemBomVm.createBomGroupType(item,'UNIT')">Add Unit</a></li>
                            <%--<li><a href ng-click="itemBomVm.addNewBomItem(item)">Add Part</a></li>--%>
                        </ul>
               </span>

                <span ng-if="item.bomItemType == 'SUBSYSTEM'" class="row-menu" uib-dropdown dropdown-append-to-body>
                   <i class="fa fa-plus-circle dropdown-toggle" uib-dropdown-toggle title="Add New"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href ng-click="itemBomVm.createBomGroupType(item,'UNIT')">Add Unit</a></li>
                            <li><a href ng-click="itemBomVm.addNewBomItem(item)">Add Part</a></li>
                        </ul>
               </span>

                <span ng-if="item.bomItemType == 'UNIT'">
                   <i class="fa fa-plus-circle" ng-click="itemBomVm.addNewBomItem(item)" title="Add Part"></i>
               </span>
            </td>
            <td>
                <span class="level{{item.level}}" ng-if="item.bomItemType != 'PART'"
                      title="{{item.expanded ? 'Collapse':'Expand'}}"
                      ng-click="itemBomVm.toggleNode(item)">
                    <i ng-if="item.children.length > 0" class="mr5 fa"
                       style="cursor: pointer; color: #909090;font-size: 18px;"
                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                       'fa-caret-down': item.expanded == true}"></i>
                    <span>
                        {{item.typeRef.name}}<span ng-if="item.typeRef.versity"> ( VSPL )</span>
                    </span>
                </span>
                <span class="level{{item.level}}" ng-if="item.bomItemType == 'PART'">
                    <a ui-sref="app.items.details({itemId: item.item.id})" title="Click to show Item details">
                        <span>{{item.item.itemMaster.itemName}} {{item.item.partSpec.specName}}</span>
                    </a>
                </span>
            </td>
            <td>
                <bom-group-type ng-if="item.bomItemType != 'PART'" object="item.typeRef"></bom-group-type>
                <span>{{item.item.itemMaster.parentType.name}}</span></td>
            <td class="description"><span
                    ng-bind-html="item.item.itemMaster.description | highlightText: freeTextQuery"></span></td>
            <td>
                <span ng-if="item.bomItemType == 'PART' && !item.item.itemMaster.itemType.hasLots && item.editMode == false">{{item.quantity}}</span>
                <span ng-if="item.bomItemType == 'PART' && item.item.itemMaster.itemType.hasLots && item.editMode == false">{{item.fractionalQuantity}}</span>
                <input ng-if="item.bomItemType == 'PART' && !item.item.hasBom
                              && !item.item.itemMaster.itemType.hasLots && item.editMode == true"
                       ng-model="item.quantity" type="number" class="form-control" style="width: 100px;">

                <input ng-if="item.bomItemType == 'PART' && item.item.itemMaster.itemType.parentNodeItemType == 'Part' && !item.item.hasBom
                              && item.item.itemMaster.itemType.hasLots && item.editMode == true"
                       ng-model="item.fractionalQuantity" type="number" class="form-control" style="width: 100px;">
            </td>
            <td>
                <span ng-if="item.bomItemType == 'PART'">
                    {{item.item.itemMaster.itemType.units}}
                </span>
            </td>
            <td>{{item.item.drawingNumber}}</td>
            <td style="width: 70px;text-align: center;">
                <div class="btn-group">
                    <button ng-if="item.editMode == true" class="btn btn-xs btn-success"
                            ng-click="itemBomVm.saveItem(item)">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-if="item.editMode == true && item.isNew == true" class="btn btn-xs btn-default"
                            title="Remove Item"
                            ng-click="itemBomVm.removeItem(item)">
                        <i class="fa fa-times"></i>
                    </button>
                    <button ng-if="item.editMode == true && item.isNew == false" class="btn btn-xs btn-default"
                            title="Cancel Changes"
                            ng-click="itemBomVm.cancelChanges(item)">
                        <i class="fa fa-times"></i>
                    </button>

                    <button ng-if="item.editMode == false && item.bomItemType == 'PART'" class="btn btn-xs btn-warning"
                            ng-click="itemBomVm.editItem(item)">
                        <i class="fa fa-edit"></i>
                    </button>
                    <button ng-if="item.editMode == false" title="Delete Item" class="btn btn-xs btn-danger"
                            ng-click="itemBomVm.deleteItem(item)">
                        <i class="fa fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>