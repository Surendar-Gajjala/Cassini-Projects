<div class="row" ng-repeat="bomitem in prodOrderItems.content">
  <div class="col-md-12 appcontent">
    <div class="row">
      <div class="col-md-12">
        <div class="appcontent" style="top: -16px;float: left;position: absolute;left: 18px;padding: 5px 10px;"><h3 style="margin: 0;">{{bomitem.product.name}}</h3></div>
        <div style="float: right;padding: 5px 0;"><strong>Product Qty: </strong>{{bomitem.quantity}}</div>
      </div>
    </div>

    <table class="table table-striped">
      <thead>
      <tr>
        <th style="width: 150px">Item Type</th>
        <th ng-if="(!saveMode && !onViewOrder)" style="width: 250px">Item </th>
        <th style="width: 150px">Material</th>
        <th style="width: 150px">Material Qty</th>
        <th style="width: 150px">Unit Price</th>
        <th style="width: 150px">Total</th>
      </tr>
      </thead>
      <tbody>
      <tr ng-repeat="customBom in bomitem.itemBoms">
        <td style="width: 150px">{{customBom.bomItem.itemType}}</td>
        <td ng-if="(!saveMode && !onViewOrder)"> {{customBom.bomItem.bomItemObj}}</td>

        <td>

          <span ng-if="customBom.bomItem.itemType=='MATERIAL' || onViewOrder">{{customBom.material.name}}</span>

          <div ng-if="(customBom.bomItem.itemType=='TYPE' || customBom.bomItem.itemType=='CATEGORY') && !onViewOrder">
            <ui-select ng-model="customBom.material" theme="bootstrap" style="width: 100%;" title="Choose a Process" on-select="changeTypeObj(customBom,$index)">
              <ui-select-match placeholder="Choose a Material">{{$select.selected.name}}</ui-select-match>
              <ui-select-choices repeat="mat in customBom.materialList | filter: $select.search">
                <div ng-bind-html="mat.name | highlight: $select.search"></div>
              </ui-select-choices>
            </ui-select>
          </div>

        </td>
        <td style="width: 150px">{{customBom.bomItem.quantity * bomitem.quantity}}</td>
        <td style="width: 150px">{{customBom.material.unitPrice}}</td>
        <td>{{(customBom.bomItem.quantity * bomitem.quantity)*(customBom.material.unitPrice)}}</td>
      </tr>
      </tbody>
    </table>
  </div>
</div>