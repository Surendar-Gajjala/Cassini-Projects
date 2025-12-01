<div>
    <div class='responsive-table'>
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th translate>INSPECTION_NUMBER</th>
                <th class="col-width-250" translate>INSPECTION_PLAN</th>
                <th style="width: 150px;" translate>PRODUCT</th>
                <th style="width: 150px" translate>ASSIGNED_TO</th>
                <th style="width: 100px;" translate>STATUS</th>
                <th class="description-column" translate>DEVIATION_SUMMARY</th>
                <th style="width: 150px" translate>NOTES</th>
                <th style="width: 150px" translate>MODIFIED_BY</th>
                <th style="width: 150px" translate>MODIFIED_DATE</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="itemInspectionVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_INSPECTIONS</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="itemInspectionVm.loading == false && itemInspectionVm.inspections.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/inspections.png" alt="" class="image">

                        <div class="message" translate>NO_INSPECTIONS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>

            <tr ng-repeat="inspection in itemInspectionVm.inspections">
                <td>
                    <a href="" ng-click="itemInspectionVm.showInspection(inspection)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <span ng-bind-html="inspection.inspectionNumber | highlightText: freeTextQuery"></span>
                    </a>
                </td>
                <td class="col-width-250">
                    <span ng-bind-html="inspection.inspectionPlan | highlightText: freeTextQuery"></span>
                </td>
                <td>
                    <span ng-bind-html="inspection.productName | highlightText: freeTextQuery"></span>
                </td>
                <td>{{inspection.assignedTo}}</td>
                <td>
                    <workflow-status workflow="inspection"></workflow-status>
                </td>
                <td>
                    <span ng-bind-html="inspection.deviationSummary | highlightText: freeTextQuery"></span>
                </td>
                <td>
                    <span ng-bind-html="inspection.notes  | highlightText: freeTextQuery"></span>
                </td>
                <td>{{inspection.modifiedBy}}</td>
                <td>{{inspection.modifiedDate}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>