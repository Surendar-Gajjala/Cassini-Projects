<script type="text/ng-template" id="stores-view-tb">
    <div>
        <button class="btn btn-sm btn-success" ng-click="newStore()">New Store
        </button>
    </div>

</script>

<div class="row">
    <div class="col-md-12">
        <div style="text-align: right; margin-bottom: 20px;">
            <pagination total-items="stores.totalElements"
                        items-per-page="pageable.size"
                        max-size="5"
                        boundary-links="true"
                        ng-model="pageable.page"
                        ng-change="pageChanged()">
            </pagination>

            <div style="margin-top: -25px;">
                <small>Total {{stores.totalElements}} stores</small>
            </div>
        </div>

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 200px;">Name</th>
                <th style="width: 200px;">Description</th>
                <th style="width: 200px;">Location</th>
                <th style="width: 200px;">CreatedOn</th>
                <th class="actions-col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="loading == true">
                <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading stores...
                        </span>
                </td>
            </tr>

            <tr ng-if="loading == false && storeList.content.length == 0">
                <td colspan="11">No stores</td>
            </tr>

            <tr ng-repeat="store in stores.content">
                <td><span
                        ng-bind="store.storeName"></span>
                </td>
                <td><span ng-bind="store.description"></span></td>
                <td><span ng-bind="store.locationName"></span></td>
                <td><span ng-bind="store.createdDate"></span></td>
                <td class="actions-col">
                    <div class="btn-group">
                        <%--<button ng-if="store.editMode == true"
                                class="btn btn-xs btn-success"
                                ng-click="applyChanges(store)"><i class="fa fa-check"></i></button>
                        <button ng-if="store.editMode == true"
                                class="btn btn-xs btn-default"
                                ng-click="cancelChanges(store)"><i class="fa fa-times"></i></button>--%>


                        <%--<button &lt;%&ndash;ng-if="store.showValues == true"&ndash;%&gt; title="Edit This Store"
                                class="btn btn-xs btn-warning" ng-disabled="selectedProject.locked == true || hasPermission('permission.stores.edit') == false"
                                ng-click="editStore(store)"><i class="fa fa-edit"></i></button>--%>

                        <button <%--ng-if="store.showValues == true"--%> title="Delete This Store"
                                                                         class="btn btn-xs btn-danger"
                                                                         ng-click="deleteStore(store)"><i
                                class="fa fa-trash"></i></button>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>