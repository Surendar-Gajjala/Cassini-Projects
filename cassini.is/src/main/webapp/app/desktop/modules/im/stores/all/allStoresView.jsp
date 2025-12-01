<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="storesVm.newStore()"
                ng-disabled="selectedProject.locked == true || hasPermission('permission.stores.new') == false">New
            Store
        </button>
        <%--<p class="blink" ng-if="storesVm.showSearchMode == true"
           style="color: blue;text-align: center;margin-top: -25px;font-size: 16px;">View is in search mode</p>--%>
        <free-text-search on-clear="storesVm.resetPage" on-search="storesVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="pull-right text-center" style="margin-right: 10px;">
            <span ng-if="storesVm.loading == false"><small>Page {{storesVm.storeList.number+1}} of
                {{storesVm.storeList.totalPages}}
            </small></span>
            <br>

            <div class="btn-group" style="margin-bottom: 0">
                <button class="btn btn-xs btn-default" ng-click='storesVm.previousPage()'
                        ng-disabled="storesVm.storeList.first"><i class="fa fa-chevron-left"></i></button>
                <button class="btn btn-xs btn-default" ng-click='storesVm.nextPage()'
                        ng-disabled="storesVm.storeList.last"><i
                        class="fa fa-chevron-right"></i></button>
            </div>
            <br>
            <span ng-if="storesVm.storeList.totalElements == 1"><small>{{storesVm.storeList.totalElements}} Store
            </small></span>
            <span ng-if="storesVm.storeList.totalElements > 1"><small>{{storesVm.storeList.totalElements}} Stores
            </small></span>
        </div>
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 200px;">Name</th>
                    <th style="width: 200px;">Description</th>
                    <th class="actions-col">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="storesVm.loading == true">
                    <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading stores...
                        </span>
                    </td>
                </tr>

                <tr ng-if="storesVm.loading == false && storesVm.storeList.content.length == 0">
                    <td colspan="11">No stores</td>
                </tr>

                <tr ng-repeat="store in storesVm.storeList.content">
                    <td style="width: 200px"><a href="" ng-click="storesVm.showStore(store)"><span
                            ng-bind-html="store.name | highlightText: freeTextQuery"></span></a></td>
                    <td>
                        <span ng-if="store.showValues == true;" style="width: 200px;"><span
                                ng-bind-html="store.description | highlightText: freeTextQuery"></span></span>
                        <input ng-if="store.editMode == true" type="text" class="form-control input-sm"
                               ng-model="store.newDescription">
                    </td>
                    <td class="actions-col">
                        <div class="btn-group">
                            <button ng-if="store.editMode == true"
                                    class="btn btn-xs btn-success"
                                    ng-click="storesVm.applyChanges(store)"><i class="fa fa-check"></i></button>
                            <button ng-if="store.editMode == true"
                                    class="btn btn-xs btn-default"
                                    ng-click="storesVm.cancelChanges(store)"><i class="fa fa-times"></i></button>


                            <button ng-if="store.showValues == true" title="Edit This Store"
                                    class="btn btn-xs btn-warning"
                                    ng-disabled="selectedProject.locked == true || hasPermission('permission.stores.edit') == false"
                                    ng-click="storesVm.editStore(store)"><i class="fa fa-edit"></i></button>

                            <button ng-if="store.showValues == true" title="Delete This Store"
                                    class="btn btn-xs btn-danger"
                                    ng-disabled="selectedProject.locked == true || hasPermission('permission.stores.delete') == false"
                                    ng-click="storesVm.deleteStore(store)"><i class="fa fa-trash"></i></button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
