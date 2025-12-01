<div ng-if="productLowInventory.content.length == 0" style="padding: 20px">
  <span>Inventory levels are good</span>
</div>

<div ng-if="productLowInventory.content.length > 0">
  <table class="table table-striped" ng-if="content != 0">
    <thead>
    <tr>
      <th> Product Name</th>
      <th>Inventory</th>
      <th class="text-right">Restock Level</th>
    </tr>
    </thead>
    <tbody>
    <tr data-ng-repeat="productInventory in productLowInventory">
      <td>
       {{productInventory.product.name}}
      </td>
      <td>
        {{productInventory.inventory}}
      </td>
      <td>
        {{productInventory.threshold}}
      </td>
    </tr>
    </tbody>
  </table>
</div>
