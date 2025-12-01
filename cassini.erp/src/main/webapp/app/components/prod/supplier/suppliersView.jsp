<script type="text/ng-template" id="suppliers-view-tb">
  <div>
    <button class="btn btn-sm btn-primary" ng-click="addMaterialSupplier()" style="width: 100px">New Supplier</button>
  </div>

</script>

<div class="row">
  <div class="col-md-12">
    <div style="text-align: right; margin-bottom: 20px;">
      <pagination total-items="suppliers.totalElements"
                  items-per-page="pageable.size"
                  max-size="5"
                  boundary-links="true"
                  ng-model="pageable.page"
                  ng-change="pageChanged()">
      </pagination>

      <div style="margin-top: -25px;">
        <small>Total {{suppliers.totalElements}} suppliers</small>
      </div>
    </div>

    <table class="table table-striped">
      <thead>
      <tr>
        <th style="width: 150px">Name</th>
        <th style="width: 250px">Address</th>
        <th style="width: 150px">Phone</th>
        <th style="width: 150px">Fax</th>
        <th style="width: 150px">Email</th>
        <th style="width: 150px">Contact</th>
        </tr>
      </thead>
      <tbody>
      <tr ng-if="suppliers.totalElements == 0">
        <td colspan="7">No Suppliers</td>
      </tr>
      <tr ng-repeat="supplier in suppliers.content">
        <td style="width: 150px"><a href="" ng-click="showSupplier(supplier)">{{supplier.name}}</a></td>
        <td>
          <span>{{supplier.address.addressText}},</span>
          <span>{{supplier.address.city}}, </span>
          <span>{{supplier.address.state.name}} - </span>
          <span>{{supplier.address.pincode}}</span>
        </td>
        <td style="width: 150px">{{supplier.officePhone}}</td>
        <td style="width: 150px">{{supplier.officeFax}}</td>
        <td style="width: 150px">{{supplier.officeEmail}}</td>
        <td>
        <span>{{supplier.contactPerson.firstName}} </span>
        <span>{{supplier.contactPerson.lastName}} </span>
        </td>

      </tr>
      </tbody>
    </table>
  </div>
</div>