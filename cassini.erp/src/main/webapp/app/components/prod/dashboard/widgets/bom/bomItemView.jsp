<div>
<div class="panel panel-default panel-alt widget-messaging" style="height: 350px">
  <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
    <div class="row" style="padding-right: 10px;">
      <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">BOM ITEM</div>
      <div style="float:right;padding-right: 15px;padding-top: 12px;">
        <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewBomItem();">
          <i class="glyphicon glyphicon-plus"></i> Add BOMITEM
        </button>
      </div>
    </div>
  </div>
  <div class="panel-body" style=" min-height: 250px; max-height: 300px; overflow: auto">

    <table class="table table-striped">
      <thead>
      <tr>
        <th style="padding-right: 28px;">Actions</th>
        <th>Item type</th>
        <th>Item Type Obj </th>
        <th>Quantity</th>
        </tr>
      </thead>
      <tbody>
      <tr ng-if="bomItemList.length == 0">
        <td colspan="7">No Records</td>
      </tr>
      <tr data-ng-repeat="bomItem in bomItemList track by $index">
        <td>
          <div class="btn-group" dropdown ng-hide="bomItem.editMode">
            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
              <i class="fa fa-cog fa-fw"></i></span>
            </button>
            <ul class="dropdown-menu" role="menu">
              <li><a href="" ng-click="bomItemShowEditMode(bomItem)">Edit</a></li>
              <li><a href="" ng-click="bomItemRemoveItem($index,bomItem);">Delete</a></li>
            </ul>
          </div>
          <div class="btn-group" ng-show="bomItem.editMode">
            <button type="button" class="btn btn-sm btn-success" ng-click="bomItemAcceptChanges(bomItem)"><i
                    class="fa fa-check"></i></button>
            <button type="button" class="btn btn-sm btn-default" ng-click="bomItemHideEditMode(bomItem,$index);"><i
                    class="fa fa-times"></i></button>
          </div>
        </td>
        <td>
          <span ng-hide="bomItem.editMode">{{bomItem.itemType}}</span>

          <div ng-show="bomItem.editMode">
            <ui-select ng-model="bomItem.itemType" th1eme="bootstrap" style="width: 115px;"
                       title="Choose a Type"  on-select="changeBomItemTypeObj(bomItem,$index)">
              <ui-select-match placeholder="Choose a Type">{{$select.selected}}
              </ui-select-match>
              <ui-select-choices repeat="bomItemType in bomItemTypeList | filter: $select.search">
                <div ng-bind-html="bomItemType | highlight: $select.search"></div>
              </ui-select-choices>
            </ui-select>
          </div>

        </td>
        <td>
          <span ng-show="bomItem.hideMaterialTypeMode">{{bomItem.obj}}</span>

          <div ng-show="bomItem.showMaterialTypeMode">
            <ui-select ng-model="bomItem.obj" theme="bootstrap" style="width: 155px;"
                       title="Choose Type">
              <ui-select-match placeholder="Choose Material Type">{{$select.selected.name}}
              </ui-select-match>
              <ui-select-choices repeat="materialType in materialTypes | filter: $select.search">
                <div ng-bind-html="materialType.name | highlight: $select.search"></div>
              </ui-select-choices>
            </ui-select>
          </div>

          <span ng-show="bomItem.hideMaterialCatMode">{{bomItem.obj}}</span>
          <div ng-show="bomItem.showMaterialCatMode">
            <div class="input-group">
              <input class="form-control" type="text" placeholder="Select category..." disabled
                     ng-model="bomItem.obj.name" style="width:100px;">

              <div class="input-group-btn" dropdown>
                <button dropdown-toggle class="btn btn-success dropdown-toggle" type="button">
                  Categories <span class="caret" style="margin-left: 5px;"></span>
                </button>
                <div class="dropdown-menu" role="menu" id="materialCategoriesDropdown">
                  <div id="materialCategoriesTree{{$index}}" class="easyui-tree"
                       style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 500px;height: 400px;">
                    categories
                  </div>
                </div>
              </div>
            </div>
          </div>

          <span ng-show="bomItem.hideMaterialMode">{{bomItem.obj}}</span>
          <button ng-show="bomItem.showMaterialMode" class="btn btn-primary mr5 btn-sm" ng-click="selectMaterial(bomItem)">Select</button>
        </td>
        <td>
          <span ng-hide="bomItem.editMode">{{bomItem.quantity}}</span>
          <input ng-show="bomItem.editMode" placeholder="Enter Qty"  type="text"
                 data-ng-model="bomItem.quantity">
        </td>

      </tr>
      </tbody>
    </table>
  </div>
</div>
</div>