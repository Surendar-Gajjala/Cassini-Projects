<div>
    <style scoped>
        table {
            table-layout: fixed;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <div style="width: 50%">
        <div class='responsive-table'>
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th style="width: 30px;">
                        <i class="la la-plus" title="{{'ADD_RESOURCE_TITLE' | translate}}"
                           style="cursor: pointer" ng-if="workOrder.status != 'FINISH'"
                           ng-click="workOrderResourcesVm.addResources()"></i>
                    </th>
                    <th class="col-width-150" translate>RESOURCE_TYPE</th>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-200"> Mfr. <span style="word-wrap: break-word; !important;"
                                                          translate>NAME</span></th>
                    <th class="col-width-150"> Mfr. <span style="word-wrap: break-word; !important;"
                                                          translate>PART_NUMBER</span></th>
                    <th class="col-width-150"> Mfr. <span style="word-wrap: break-word; !important;"
                                                          translate>SERIAL_NUMBER</span></th>
                    <th class="col-width-150"> Mfr. <span style="word-wrap: break-word; !important;"
                                                          translate>DATE</span></th>
                    <th style="width: 100px;text-align: center">
                        <span translate>ACTIONS</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="workOrderResourcesVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_RESOURCES</span>
                    </td>
                </tr>
                <tr ng-if="workOrderResourcesVm.loading == false && workOrderResourcesVm.workOrderResources.length == 0">
                    <td colspan="12"><span translate>NO_RESOURCES</span></td>
                </tr>

                <tr ng-repeat="resource in workOrderResourcesVm.workOrderResources">
                    <td></td>
                    <td class="col-width-150">{{resource.resourceName}}</td>
                    <td class="col-width-200">{{resource.resourceObject.name}}</td>
                    <td class="col-width-250">{{resource.resourceObject.description}}</td>
                    <td class="col-width-200">{{resource.resourceObject.manufacturerData.mfrName}}</td>
                    <td class="col-width-150">{{resource.resourceObject.manufacturerData.mfrPartNumber}}</td>
                    <td class="col-width-150">{{resource.resourceObject.manufacturerData.mfrSerialNumber}}</td>
                    <td class="col-width-150">{{resource.resourceObject.manufacturerData.mfrDate}}</td>

                    <td class="text-center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body
                              style="min-width: 50px" ng-if="workOrder.status != 'FINISH'">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="workOrderResourcesVm.deleteResource(resource)"
                                       translate>REMOVE_RESOURCE</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>