<div>
    <style scoped>
        table {
            table-layout: auto !important;
        }
    </style>
    <div class='responsive-table' style="padding: 10px;">
        <table class='table table-striped highlight-row' style="width: 100%;table-layout: fixed;">
            <thead>
            <tr>
                <th style="width: 100px" translate>NUMBER</th>
                <th style="width: 150px" translate>TYPE</th>
                <th style="width: 150px" translate>NAME</th>
                <th style="width: 200px" translate>DESCRIPTION</th>
                <th style="width: 100px;text-align: center;" translate>REVISION</th>
                <th style="width: 100px;text-align: center;" translate>LIFECYCLE</th>
                <th style="width: 100px;" translate>STATUS</th>
                <th style="width: 100px;" translate>CREATED_BY</th>
                <th style="width: 100px;" translate>CREATED_DATE</th>
                <th style="width: 100px;" translate>MODIFIED_BY</th>
                <th style="width: 100px;" translate>MODIFIED_DATE</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="mbomWhereUsedVm.loading == true">
                <td colspan="15"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                     class="mr5">
                    <span translate>LOADING_BOPS</span></td>
            </tr>
            <tr ng-if="mbomWhereUsedVm.loading == false && mbomWhereUsedVm.bops.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Changes.png" alt="" class="image">

                        <div class="message" translate>NO_BOPS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="bop in mbomWhereUsedVm.bops">
                <td class="col-width-100">
                    <a href="" ng-click="mbomWhereUsedVm.showBOP(bop)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="bop.number | highlightText: freeTextQuery"></span>
                    </a>
                </td>
                <td class="col-width-150">
                    <span ng-bind-html="bop.typeName | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-200">
                    <span ng-bind-html="bop.name | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-250" title="{{bop.description}}">
                    <span ng-bind-html="bop.description  | highlightText: freeTextQuery"></span>
                </td>
                <td class="col-width-100" style="text-align: center">
                    {{bop.revision}}
                </td>
                <td class="col-width-100" style="text-align: center">
                    <item-status item="bop"></item-status>
                </td>
                <td>
                    <workflow-status workflow="bop"></workflow-status>
                    <span class="label label-warning" ng-if="bop.onHold">HOLD</span>
                </td>
                <td>{{bop.createdByName}}</td>
                <td>{{bop.createdDate}}</td>
                <td>{{bop.modifiedByName}}</td>
                <td>{{bop.modifiedDate}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>