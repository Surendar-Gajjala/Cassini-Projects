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
                <th style="width: 100px;" translate>PPAP_NUMBER</th>
                <th class="col-width-150" translate>TITLE</th>
                <th class="col-width-150" translate>PPAP_TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th class="col-width-150" translate>SUPPLIER_PART</th>
                <th style="width: 100px;" translate>STATUS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="supplierppapVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_PPAPS</span>
                </td>
            </tr>
            <tr ng-if="supplierppapVm.loading == false && supplierppapVm.supplierPPAPs.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_SUPPLIER_PPAPS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-if="supplierppapVm.supplierPPAPs.length > 0"
                ng-repeat="ppap in supplierppapVm.supplierPPAPs">

                <td style="width: 100px !important;">
                    <a href=""
                       ng-click="supplierppapVm.showPpapDetails(ppap)">{{ppap.number}}</a>
                </td>
                <td>
                    <span>{{ppap.name}}</span>
                </td>

                <td class="col-width-150">{{ppap.type.name}}</td>
                <td class="col-width-250">{{ppap.description}}</td>

                <td class="col-width-250">
                    <a href=""
                       ng-click="supplierppapVm.showMfrPartsDetails(ppap.mfrPart)">{{ppap.mfrPart.partName}}</a>
                </td>
                <td>
                    <ppap-status object="ppap"></ppap-status>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>