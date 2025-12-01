<script type="text/ng-template" id="new-supplier-view-tb">
    <div>

        <button ng-show="createOrSave" class="btn btn-primary mr5 btn-sm" ng-click="createSupplier(supplier)">Create
        </button>
        <button ng-hide="createOrSave" class="btn btn-primary mr5 btn-sm" ng-click="saveSupplier(supplier)">Save
        </button>
        <button class="btn btn-primary mr5 btn-sm" ng-click="cancelSupplier(supplier)">Cancel</button>

    </div>
</script>
<br>

<div class="view-content">

    <div class="row">
        <div class="col-xs-12">
            <div class="col-xs-4">
                <form class="form-horizontal">
                    <h4 class="section-title">Basic Info</h4>
                    <br>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Phone: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.officePhone"
                                   ng-blur="validateSupplier()">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Fax: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.officeFax">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Email: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.officeEmail">
                        </div>
                    </div>
                </form>
            </div>


            <div class="col-xs-4">
                <form class="form-horizontal">
                    <h4 class="section-title">Address</h4>
                    <br>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Address: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="supplier.address.addressText"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">City: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.address.city">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">State: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="supplier.address.state" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select state">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="state in states | filter: $select.search">
                                    <div ng-bind="state.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Pincode: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.address.pincode">
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-xs-4">
                <form class="form-horizontal">

                    <h4 class="section-title">Contact</h4>
                    <br>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">First Name: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   ng-model="supplier.contactPerson.firstName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Last Name: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   ng-model="supplier.contactPerson.lastName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Phone: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   ng-model="supplier.contactPerson.phoneMobile">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Email: </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name" ng-model="supplier.contactPerson.email">
                        </div>
                    </div>
                </form>
            </div>
            <br><br>

            </form>
        </div>
        <div class="col-xs-12" ng-hide="createOrSave">
            <div class="col-xs-4">
                <h4 class="section-title">Material:</h4>
                <button class="btn btn-sm btn-success" style="width: 100px" ng-click="addMaterial()">Add
                </button>
                <br>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="">Material</th>
                        <th style="">Cost</th>
                        <th style="">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="supplier.materialSuppliers.length == 0">
                        <td colspan="3">
                            <span ng-hide="loading">No materials</span>
                        </td>
                    </tr>
                    <tr ng-repeat="material in supplier.materialSuppliers">
                        <td style="vertical-align: middle;">
                            {{ material.materialIdObject.name }}
                        </td>
                        <td style="vertical-align: middle;">
                            {{material.cost}}
                        </td>
                        <td>
                            <button title="Delete" class="btn btn-xs btn-danger"
                                    ng-click="deleteMaterialSupplier(material)">
                                <i class="fa fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>

