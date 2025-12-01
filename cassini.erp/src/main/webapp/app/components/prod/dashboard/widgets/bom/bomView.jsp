<div class="panel panel-default panel-alt widget-messaging" style="height: 350px">
  <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
    <div class="row" style="padding-right: 10px;">
      <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">BOM</div>
      <div style="float:right;padding-right: 15px;padding-top: 12px;">
        <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewBom();">
          <i class="glyphicon glyphicon-plus"></i> Add BOM
        </button>
      </div>
    </div>
  </div>
  <div class="panel-body" style=" min-height: 250px; max-height: 300px; overflow: auto">

    <table class="table table-striped">
      <thead>
      <tr>
        <th></th>
        <th style="padding-right: 28px;">Actions</th>
        <th>Name</th>
        <th>Description</th>
        <th>Type</th>
        <th>Type Obj</th>
      </tr>
      </thead>
      <tbody>
      <tr ng-if="bomList.length == 0">
        <td colspan="6">No Records</td>
      </tr>

      <tr data-ng-repeat="bom in bomList track by $index" class="order-row">

        <td style="background-color:#F2F7F9" colspan="6"
            ng-include="bom.showDetails ? 'app/components/prod/dashboard/widgets/bom/bomItemView.jsp' : null">
        </td>

        <td class="actions-col" ng-if="!bom.showDetails">
            <span ng-click="toggleBomDetails(bom);" style="cursor: pointer">
                <span title="Show Details" ng-if="bom.detailsOpen == false"><i class="fa fa-plus-circle mr5"></i></span>
                <span title="Hide Details" ng-if="bom.detailsOpen == true"><i class="fa fa-minus-circle mr5"></i></span>
            </span>
        </td>
        <td ng-if="!bom.showDetails">
        <div class="btn-group" dropdown ng-hide="bom.editMode">
            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
              <i class="fa fa-cog fa-fw"></i></span>
            </button>
            <ul class="dropdown-menu" role="menu">
              <li><a href="" ng-click="showEditMode(bom)">Edit</a></li>
              <li><a href="" ng-click="removeItem($index,bom);">Delete</a></li>
            </ul>
          </div>
          <div class="btn-group" ng-show="bom.editMode">
            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(bom)"><i
                    class="fa fa-check"></i></button>
            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(bom,$index);"><i
                    class="fa fa-times"></i></button>
          </div>
        </td>
        <td ng-if="!bom.showDetails">
          <span ng-hide="bom.editMode">{{bom.name}}</span>
          <input ng-show="bom.editMode" placeholder="Enter name"  type="text"
                 data-ng-model="bom.name">
        </td>
        <td ng-if="!bom.showDetails">
          <span ng-hide="bom.editMode">{{bom.description}}</span>
          <input ng-show="bom.editMode" placeholder="Enter description" type="text"
                 data-ng-model="bom.description">
        </td>
        <td ng-if="!bom.showDetails">
          <span ng-hide="bom.editMode">{{bom.type}}</span>

          <div ng-show="bom.editMode">
            <ui-select ng-model="bom.type" th1eme="bootstrap" style="width: 115px;"
                       title="Choose a Type"  on-select="changeTypeObj(bom,$index)">
              <ui-select-match placeholder="Choose a Type">{{$select.selected}}
              </ui-select-match>
              <ui-select-choices repeat="bomType in bomTypeList | filter: $select.search">
                <div ng-bind-html="bomType | highlight: $select.search"></div>
              </ui-select-choices>
            </ui-select>
          </div>

        </td>
        <td ng-if="!bom.showDetails">
          <span ng-show="bom.hideProductTypeMode">{{bom.obj}}</span>

          <div ng-show="bom.showProductTypeMode">
            <ui-select ng-model="bom.obj" theme="bootstrap" style="width: 155px;"
                       title="Choose Type">
              <ui-select-match placeholder="Choose Product Type">{{$select.selected.name}}
              </ui-select-match>
              <ui-select-choices repeat="productType in productTypes | filter: $select.search">
                <div ng-bind-html="productType.name | highlight: $select.search"></div>
              </ui-select-choices>
            </ui-select>
          </div>

          <span ng-show="bom.hideProductCatMode">{{bom.obj}}</span>
          <div ng-show="bom.showProductCatMode" style="width: 120px;">
            <div class="input-group">
              <input class="form-control" type="text" placeholder="Select category..." disabled
                     ng-model="bom.obj.name" style="width:100px;">

              <div class="input-group-btn" dropdown>
                <button dropdown-toggle class="btn btn-success dropdown-toggle" type="button">
                  Categories <span class="caret" style="margin-left: 5px;"></span>
                </button>
                <div class="dropdown-menu" role="menu" id="productCategoriesDropdown">
                  <div id="productCategoriesTree{{$index}}" class="easyui-tree"
                       style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 200px;height: 200px;">
                    categories
                  </div>
                </div>
              </div>
            </div>
          </div>

          <span ng-show="bom.hideProductMode">{{bom.obj}}</span>
          <button ng-show="bom.showProductMode" class="btn btn-primary mr5 btn-sm" ng-click="selectProduct(bom)">Select</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</div>
