<style>
    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: left;
    }

    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">Vaults</span>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="vaultsVm.showNewVault()">New Vault</button>
        </div>

        <free-text-search on-clear="vaultsVm.resetPage" on-search="vaultsVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="vaults{{$index}}" name="vaultSelected" ng-value="true" type="checkbox"
                                   ng-model="vault.selected" ng-click="vaultsVm.selectAll()">
                            <label for="vaults{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px">Name</th>
                    <th style="">Description</th>
                    <th style="width: 150px">Created By</th>
                    <th style="width: 150px">Created On</th>
                    <th style="width: 150px">Modified By</th>
                    <th style="width: 150px">Modified On</th>
                    <th style="width: 150px; text-align: center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="vaultsVm.loading == true">
                    <td colspan="15">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading vaults...
                        </span>
                    </td>
                </tr>

                <tr ng-if="vaultsVm.loading == false && vaultsVm.vaults.content.length == 0">
                    <td colspan="15">No vaults</td>
                </tr>

                <tr ng-repeat="vault in vaultsVm.vaults.content">
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="vault{{$index}}" name="vaultSelected" ng-value="true" type="checkbox"
                                   ng-model="vault.selected" ng-click="vaultsVm.toggleSelection(vault)">
                            <label for="vault{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a href="" ng-click="vaultsVm.showVault(vault)">{{vault.name}}</a>
                    </td>
                    <td id="td">{{vault.description}}</td>
                    <td style="width: 150px">{{vault.createdByObject.firstName}}</td>
                    <td style="width: 150px">{{vault.createdDate}}</td>
                    <td style="width: 150px">{{vault.modifiedByObject.firstName}}</td>
                    <td style="width: 150px">{{vault.modifiedDate}}</td>
                    <td style="width: 150px; text-align: center">
                        <button title="Delete this vault" class="btn btn-xs btn-danger"
                                ng-click="vaultsVm.deleteVault(vault)">
                            <i class="fa fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="vaultsVm.vaults.totalElements ==0">
                            {{(vaultsVm.pageable.page*vaultsVm.pageable.size)}}
                        </span>
                        <span ng-if="vaultsVm.vaults.totalElements > 0">
                            {{(vaultsVm.pageable.page*vaultsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="vaultsVm.vaults.last ==false">{{((vaultsVm.pageable.page+1)*vaultsVm.pageable.size)}}</span>
                        <span ng-if="vaultsVm.vaults.last == true">{{vaultsVm.vaults.totalElements}}</span>


                        <span>of</span>
                        {{vaultsVm.vaults.totalElements}}</h5>
                </div>


                <div class="text-right">
                    <span style="margin-right: 5px;margin-left: 10px;">Page</span>
                    {{vaultsVm.vaults.totalElements != 0 ? vaultsVm.vaults.number+1:0}}
                    <span>of</span>{{vaultsVm.vaults.totalPages}}
                    <a href="" ng-click="vaultsVm.previousPage()"
                       ng-class="{'disabled': vaultsVm.vaults.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="vaultsVm.nextPage()"
                       ng-class="{'disabled': vaultsVm.vaults.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
