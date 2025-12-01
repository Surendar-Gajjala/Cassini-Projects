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
                    <i class="la la-plus" title="{{specSubstanceVm.addSubstances}}"
                       style="cursor: pointer" ng-click="specSubstanceVm.selectSubstances()"></i>
                </th>
                <th style="width: 200px;" translate>NUMBER</th>
                <th class="col-width-250" translate>NAME</th>
                <th style="width: 200px;" translate>TYPE</th>
                <th style="width: 200px;" translate>CAS_NUMBER</th>
                <%--<th class="col-width-250" translate>THRESHOLD_PPM</th>--%>
                <th style="width: 200px;" translate>THRESHOLD_MASS</th>
                <th style="width: 200px;" translate>UNITS</th>
                <th style="width: 100px;text-align: center">
                    <span translate>ACTIONS</span>
                    <i class="fa fa-check-circle" ng-click="specSubstanceVm.saveAll()"
                       ng-if="specSubstanceVm.selectedSubstances.length > 1"
                       title="Save"
                       style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                    <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                       ng-click="specSubstanceVm.removeAll()" title="Remove"
                       ng-if="specSubstanceVm.selectedSubstances.length > 1"></i>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="specSubstanceVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_SUBSTANCES</span>
                </td>
            </tr>
            <tr ng-if="specSubstanceVm.loading == false && specSubstances.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Substance.png" alt="" class="image">

                        <div class="message" translate>NO_SUBSTANCES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-if="specSubstances.length > 0"
                ng-repeat="specSubstance in specSubstances">
                <td></td>
                <td>
                    <a href=""
                       ng-click="specSubstanceVm.showSubstanceDetails(specSubstance)">{{specSubstance.substance.number}}</a>
                </td>

                <td class="col-width-250">{{specSubstance.substance.name}}</td>
                <td>{{specSubstance.substance.type.name}}</td>
                <td class="col-width-250">
                    {{specSubstance.substance.casNumber}}
                </td>
                <%--<td>
                    <span ng-if="specSubstance.editMode == false">
                        {{specSubstance.thresholdPpm}}
                    </span>
                 <span ng-if="specSubstance.editMode == true">
                    <input type="number" class="form-control" style="width: 150px;"
                           ng-model="specSubstance.thresholdPpm"/>
                </span>
                </td>--%>
                <td>
                    <span ng-if="specSubstance.editMode == false">
                        {{specSubstance.thresholdMass}}
                    </span>
                    <span ng-if="specSubstance.editMode == true">
                       <form>
                           <input type="number" class="form-control" style="width: 150px;"
                                  ng-model="specSubstance.thresholdMass"/>
                       </form>
                    </span>
                </td>
                <td>
                     <span ng-if="specSubstance.editMode == false">
                        {{specSubstance.unitSymbol}}
                    </span>
                    <ui-select ng-model="specSubstance.uom" ng-if="specSubstance.editMode" theme="bootstrap"
                               style="width:100%;"
                               on-select="specSubstance.unitName = $item.name;specSubstance.unitSymbol = $item.symbol">
                        <ui-select-match placeholder="Select Unit">{{$select.selected.name}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="unit.id as unit in specSubstanceVm.measurementUnits | filter: $select.search">
                            <div ng-bind="unit.name"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>
                <td class="text-center" ng-hide="external.external == true && sharedPermission == 'READ'">
                     <span class="btn-group"
                           ng-if="specSubstance.editMode == true"
                           style="margin: -1px">
                    <i title="{{ 'SAVE' | translate }}"
                       ng-click="specSubstanceVm.createSubstance(specSubstance)"
                       class="la la-check">
                    </i>
                    <i ng-show="specSubstanceVm.itemFlag == true"
                       title="{{ 'REMOVE' | translate }}"
                       ng-click="specSubstanceVm.onCancel(specSubstance)"
                       class="la la-times">
                    </i>
                     <i ng-show="specSubstanceVm.itemFlag == false"
                        title="{{ 'CANCEL_CHANGES' | translate }}"
                        ng-click="specSubstanceVm.cancelChanges(specSubstance)"
                        class="la la-times">
                     </i>
                </span>
                <span class="row-menu" ng-hide="specSubstance.editMode == true" uib-dropdown
                      dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a href="" ng-click="specSubstanceVm.editSpecSubstance(specSubstance)"
                               translate>EDIT_SUBSTANCE</a>
                        </li>
                        <li>
                            <a href=""
                               ng-click="specSubstanceVm.deleteSpecSubstance(specSubstance)"
                               translate>REMOVE_SUBSTANCE</a>
                        </li>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>