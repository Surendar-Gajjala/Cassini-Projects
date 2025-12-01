<style scoped>
    .center {
        display: block;
        margin-left: auto;
        margin-right: auto;
        margin-top: 4%;
        width: 300px;
    }

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
        /*background-color: #fff;*/
    }

    .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
        background-color: #d6e1e0;
    }
</style>
<div class="responsive-table">
    <table class='table table-striped highlight-row' style="margin: 0;">
        <thead>
        <tr>
            <th style="width:10px" translate><i class="la la-plus" title="Add Resources" style="cursor: pointer"
                                                ng-click="operationResourcesVm.addOperationResources()"></i></th>
            <th class="col-width-150" translate>Resource</th>
            <th class="col-width-200" translate>Resource Type</th>
            <th class="col-width-100" translate>Quantity</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="actions-col sticky-col sticky-actions-col">
                <span translate>ACTIONS</span>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="operationResourcesVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_RESOURCES</span>
            </td>
        </tr>
        <tr ng-if="operationResourcesVm.loading == false && operationResourcesVm.opreationResources.length == 0">
            <td colspan="12">
                {{ 'NO_RESOURCES' | translate}}
            </td>
        </tr>
        <tr ng-if="operationResourcesVm.opreationResources.length > 0"
            ng-repeat="opreationResource in operationResourcesVm.opreationResources">
            <td style="width:10px"></td>
            <td class="col-width-150">{{opreationResource.resource}}</td>
            <td class="col-width-200">{{opreationResource.resourceTypeName}}</td>

            <td class="col-width-100">
                        <span ng-if="opreationResource.editMode == false">
                            {{opreationResource.quantity}}
                        </span>
                        <span ng-if="opreationResource.editMode == true">
                           <form>
                               <input type="number" class="form-control" style="width: 150px;"
                                      ng-model="opreationResource.quantity"/>
                           </form>
                        </span>
            </td>

            <td class="col-width-250">
                        <span ng-if="opreationResource.editMode == false">
                            {{opreationResource.description}}
                        </span>
                        <span ng-if="opreationResource.editMode == true">
                           <form>
                               <input type="text" class="form-control" style="width: 150px;"
                                      ng-model="opreationResource.description"/>
                           </form>
                        </span>
            </td>


            <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="btn-group"
                              ng-if="opreationResource.editMode == true"
                              style="margin: -1px">
                    <i title="{{ 'SAVE' | translate }}"
                       ng-click="operationResourcesVm.saveResource(opreationResource)"
                       class="la la-check">
                    </i>
                    <i title="{{ 'REMOVE' | translate }}"
                       ng-click="operationResourcesVm.onCancel(opreationResource)"
                       class="la la-times">
                    </i>
                </span>
                    <span ng-if="opreationResource.editMode == false" class="row-menu" uib-dropdown
                          dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a href="" ng-click="operationResourcesVm.editResource(opreationResource)"
                               translate>EDIT</a>
                        </li>
                        <li
                                ng-click="operationResourcesVm.deleteResource(opreationResource)">
                            <a href="" translate>REMOVE_RESOURCES</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>