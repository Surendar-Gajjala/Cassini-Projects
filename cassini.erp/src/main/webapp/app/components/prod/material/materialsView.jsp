<script type="text/ng-template" id="materials-view-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="addMaterial()" style="width: 100px">New Material</button>
        <button class="btn btn-sm btn-primary" ng-click="addMaterialCategory()" style="width: 100px">New Category
        </button>
    </div>

</script>

<div class="row">
    <div class="col-md-12">
        <div style="text-align: right; margin-bottom: 20px;">
            <pagination total-items="materials.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>

            <div style="margin-top: -25px;">
                <small>Total {{materials.totalElements}} materials</small>
            </div>
        </div>

        <table class="table table-striped">
            <thead>
            <tr>
                <th>SKU</th>
                <th style="width: 150px">Type</th>
                <th style="width: 150px">Category</th>
                <th style="width: 150px">Name</th>
                <th style="width: 250px">Description</th>
                <th style="width: 250px">Suppliers</th>
                <th style="width: 150px">Units</th>
                <%--<th style="width: 150px">Unit Price</th>--%>
            </tr>
            </thead>
            <tbody ng-show="loadingMaterials">
            <td colspan="8">   <span style="font-size: 15px;">
              <img src="app/assets/images/loaders/loader9.gif" class="mr5">Loading Materials....
                </span>
            </td>
            </tbody>
            <tbody>
            <tr ng-if="materials.totalElements == 0 && loadingMaterials== false">
                <td colspan="7">There are no Materials</td>
            </tr>
            <tr ng-repeat="material in materials.content">
                <td style="width: 150px"><a href="" ng-click="showMaterial(material)">{{material.sku}}</a></td>
                <%--<td style="width: 150px">{{material.sku}}</td>--%>
                <td style="width: 150px">{{material.type.name}}</td>
                <td style="width: 150px">{{material.category.name}}</td>
                <td style="width: 150px">{{material.name}}</td>
                <td style="width: 250px">{{material.description}}</td>
                <td style="width: 250px">{{material.supName}}</td>
                <%-- <td style="width: 250px">
                     <span ng-repeat="supp in material.suppliers">
                     <span>{{supp.name}},</span></span>
                 </td>--%>
                <td style="width: 150px">{{material.units}}</td>
                <%--<td style="width: 150px">{{material.unitPrice}}</td>--%>
            </tr>
            </tbody>
        </table>
    </div>
</div>