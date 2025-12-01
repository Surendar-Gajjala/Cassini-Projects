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
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th class="col-width-100" translate>NUMBER</th>
                <th class="col-width-200" translate>TITLE</th>
                <th class="col-width-200" translate>TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th class="col-width-100" translate>STATUS</th>
                <th translate>PLANNED_DATE</th>
                <th translate>COMPLETED_DATE</th>
                <th class="col-width-150" translate>PREPARED_BY</th>
                <th class="col-width-150" translate>APPROVED_BY</th>
                <th translate>DETAILS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="supplierAuditVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_SUPPLIER_AUDIT</span>
                </td>
            </tr>
            <tr ng-if="supplierAuditVm.loading == false && supplierAuditVm.supplierAudits.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_SUPPLIER_AUDITS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="supplierAudit in supplierAuditVm.supplierAudits">
                <td class="col-width-100">
                    <a href=""
                       ng-click="supplierAuditVm.showSupplierAuditDetails(supplierAudit)">{{supplierAudit.number}}</a>
                </td>
                <td class="col-width-200">{{supplierAudit.name}}</td>
                <td class="col-width-200">{{supplierAudit.type}}</td>
                <td class="col-width-250">{{supplierAudit.description}}</td>
                <td class="col-width-100">
                    <audit-plan-status object="supplierAudit"></audit-plan-status>
                </td>
                <td>{{supplierAudit.plannedStartDate}}</td>
                <td>{{supplierAudit.finishedDate}}</td>
                <td class="col-width-150">{{supplierAudit.preparedByName}}</td>
                <td class="col-width-150">
                    <div ng-repeat="approver in supplierAudit.approvers">{{approver.approverName}}</div>
                </td>
                <td>
                    <i class="fa flaticon-plan2 nav-icon-font" title="Click to show plan details"
                       ng-click="supplierAuditVm.showSupplierPlanDetails(supplierAudit)"></i>
                       <!-- <i style="color: #337ab7;font-size: 18px;font-weight: 900"
                           class="fa flaticon-plan2 nav-icon-font"></i> -->
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>