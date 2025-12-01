<script type="text/ng-template" id="materials-view-tb">
    <div>
        <button class="btn btn-sm btn-primary" ng-click="addMaterialPO()" style="width: 110px">New Material PO</button>
    </div>

</script>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-8 col-md-offset-2">
        <div style="text-align: right; margin-bottom: 20px;">
            <pagination total-items="materialPOs.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>

            <div style="margin-top: -25px;">
                <small>Total {{materialPOs.totalElements}} material POs</small>
            </div>
        </div>

        <table class="table table-striped">
            <thead>
            <tr>
                <th>Material PO</th>
                <th style="width: 150px">Supplier</th>
                <th style="width: 150px">Status</th>
                <th style="width: 150px">Order Total</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="materials.totalElements == 0">
                <td colspan="7">No Material Purchase Orders</td>
            </tr>
            <tr ng-repeat="material in materialPOs.content">
                <td style="width: 150px"><a href="" ng-click="showMaterialPO(material)">{{material.orderNumber}}</a></td>
                <td style="width: 150px">{{material.supplier.name}}</td>
                <td style="width: 150px">{{material.status}}</td>
                <td style="width: 150px">{{material.orderTotal}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>