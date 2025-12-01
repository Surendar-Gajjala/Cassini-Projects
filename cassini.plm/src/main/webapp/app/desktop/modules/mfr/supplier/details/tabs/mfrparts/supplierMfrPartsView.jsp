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
                <th style="width: 30px;">
                    <i ng-if="(hasPermission('mfrsupplier','edit') && !loginPersonDetails.external) || (loginPersonDetails.external && sharedPermission == 'WRITE' && hasPermission('mfrsupplier','edit'))"
                       class="la la-plus"
                       title="{{supplierMfrVm.addMfrParts}}"
                       style="cursor: pointer" ng-click="selectMfrParts()"></i>
                </th>
                <th style="width: 200px;" translate>SUPPLIER_PART_NUMBER</th>
                <th style="width: 200px;" translate>MANUFACTURER_PART_NUMBER</th>
                <th class="col-width-250" translate>PART_NAME</th>
                <th style="width: 200px;" translate>MANUFACTURER_PART_TYPE</th>
                <th style="width: 100px;" translate>LIFECYCLE</th>
                <th class="col-width-250" translate>MANUFACTURER</th>
                <th ng-if="hasPermission('mfrsupplier', 'edit')" style="width: 100px; text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="supplierMfrVm.saveParts()"
                       ng-if="supplierMfrVm.selectedSupplierParts.length > 1"
                       title="Save" style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="supplierMfrVm.removeParts()" title="Remove"
                       ng-if="supplierMfrVm.selectedSupplierParts.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="supplierMfrVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_SUPPLIER_PARTS</span>
                </td>
            </tr>
            <tr ng-if="supplierMfrVm.loading == false && supplierMfrVm.supplierParts.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                        <div class="message" translate>NO_SUPPLIER_PARTS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-if="supplierMfrVm.supplierParts.length > 0"
                ng-repeat="mfrpart in supplierMfrVm.supplierParts">
                <td>

                </td>

                <td style="width: 100px !important;">
                    <span ng-if="!mfrpart.editMode">{{mfrpart.partNumber}}</span>

                    <form>
                        <input ng-if="mfrpart.editMode" type="text" class="form-control" ng-model="mfrpart.partNumber"
                               style="width: 100px;">
                    </form>
                </td>
                <td>
                    <a href=""
                       ng-click="supplierMfrVm.showMfrPartsDetails(mfrpart)">{{mfrpart.manufacturerPart.partNumber}}</a>

                </td>

                <td class="col-width-250">{{mfrpart.manufacturerPart.partName}}</td>
                <td>{{mfrpart.manufacturerPart.mfrPartType.name}}</td>
                <td style="width: 100px !important;">
                    <item-status item="mfrpart.manufacturerPart"></item-status>
                </td>
                <td class="col-width-250">
                    <a href="" ng-click="supplierMfrVm.showMfr(mfrpart)">
                        <span>{{mfrpart.manufacturerPart.mfrName}}</span>
                    </a>
                </td>


                <td class="text-center" ng-if="hasPermission('mfrsupplier', 'edit')">
                 <span class="btn-group" ng-if="mfrpart.editMode == true" style="margin: 0">
                    <button ng-show="mfrpart.isNew == true" class="btn btn-xs btn-success" type="button"
                            title="{{saveItemTitle}}"
                            ng-click="supplierMfrVm.savePart(mfrpart)">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-show="mfrpart.isNew == true" class="btn btn-xs btn-danger" type="button"
                            title="{{cancelChangesTitle}}"
                            ng-click="supplierMfrVm.removePart(mfrpart)">
                        <i class="fa fa-times"></i>
                    </button>
                      <button ng-show="mfrpart.isNew == false" class="btn btn-xs btn-success"
                              type="button" title="{{saveItemTitle}}"
                              ng-click="supplierMfrVm.updatePart(mfrpart)">
                          <i class="fa fa-check"></i>
                      </button>
                     <button ng-show="mfrpart.isNew == false" class="btn btn-xs btn-danger" type="button"
                             title="{{cancelChangesTitle}}"
                             ng-click="supplierMfrVm.cancelChanges(mfrpart)">
                         <i class="fa fa-times"></i>
                     </button>
                </span>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="mfrpart.editMode == false && ((hasPermission('mfrsupplier','edit') && !loginPersonDetails.external) || (loginPersonDetails.external && sharedPermission == 'WRITE' && hasPermission('mfrsupplier','edit')))"
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-if="mfrpart.editMode == false"
                            ng-click="supplierMfrVm.editPart(mfrpart)">
                            <a translate>EDIT_PART</a>
                        </li>
                        <li title="{{ mfrpart.assignedDeclarationPart == true ? cannotDeleteAddedDeclaration : ''}}">
                            <a ng-class="{'disabled': mfrpart.assignedDeclarationPart == true}"
                               ng-if="mfrpart.editMode == false"
                               ng-click="supplierMfrVm.deleteMfrpart(mfrpart)"
                               translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="supplier.part" object-value="mfrpart"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>