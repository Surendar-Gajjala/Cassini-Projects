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
                        <i class="la la-plus"
                           ng-if="loginPersonDetails.external == false && declaration.status == 'OPEN'"
                           title="{{declarationMfrPartsVm.addSubstances}}"
                           style="cursor: pointer" ng-click="declarationMfrPartsVm.selectParts()"></i>
                    </th>
                    <th style="width: 20px;"></th>
                    <th style="width: 200px;" translate>PART_NUMBER</th>
                    <th class="col-width-250" translate>PART_NAME</th>
                    <th style="width: 200px;" translate>MANUFACTURER_PART_TYPE</th>
                    <th class="col-width-250" translate>MANUFACTURER</th>
                    <th style="width: 200px;" translate>LIFECYCLE</th>
                    <th style="width: 100px;text-align: center"
                        ng-hide="declaration.status == 'RECEIVED' || declaration.status == 'ACCEPTED'">
                        <span translate>ACTIONS</span>
                        <i class="fa fa-check-circle" ng-click="declarationMfrPartsVm.saveAll()"
                           ng-if="declarationMfrPartsVm.selectedDeclarationParts.length > 1"
                           title="Save"
                           style="font-size: 16px;cursor: pointer;padding: 5px;"></i>
                        <i class="fa fa-minus-circle" style="font-size: 16px;cursor: pointer;"
                           ng-click="declarationMfrPartsVm.removeAll()" title="Remove"
                           ng-if="declarationMfrPartsVm.selectedDeclarationParts.length > 1"></i>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="declarationMfrPartsVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_DECLARATION_PARTS</span>
                    </td>
                </tr>
                <tr ng-if="declarationMfrPartsVm.loading == false && declarationMfrPartsVm.declarationParts.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                            <div class="message" translate>NO_DECLARATION_PARTS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat-start="declarationPart in declarationMfrPartsVm.declarationParts">
                    <td>
                        <i class="la la-plus" title="Add Substances"
                           ng-if="declaration.supplierContact == loginPersonDetails.person.id && declaration.status == 'SUBMITTED'"
                           style="cursor: pointer" ng-click="declarationMfrPartsVm.addSubstances(declarationPart)"></i>
                    </td>
                    <td>
                        <i class="mr5 fa" ng-if="declarationPart.substances.length > 0"
                           style="cursor: pointer;"
                           title="{{declarationPart.expanded ? 'Hide substances':'Show substances'}}"
                           ng-class="{'fa-caret-right': (declarationPart.expanded == false || declarationPart.expanded == null || declarationPart.expanded == undefined),
                                      'fa-caret-down': declarationPart.expanded == true}"
                           ng-click="declarationMfrPartsVm.showSubstances(declarationPart)"></i>
                    </td>
                    <td>
                        <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-if="hasPermission('manufacturerpart','view')"
                           ng-click="declarationMfrPartsVm.showMfrPartDetails(declarationPart)">{{declarationPart.part.partNumber}}</a>
                        <span ng-if="!hasPermission('manufacturerpart','view')">{{declarationPart.part.partNumber}}</span>
                        <span class="label label-default" ng-if="declarationPart.substances.length > 0"
                              style="font-size: 12px;background-color: #e4dddd;font-style: italic;" title="Substances"
                              ng-bind-html="declarationPart.substances.length"></span>
                    </td>

                    <td class="col-width-250">{{declarationPart.part.partName}}</td>
                    <td>{{declarationPart.part.mfrPartType.name}}</td>
                    <td>
                        <span ng-if="declarationPart.editMode == true">{{declarationPart.part.manufacturerObject.name}}</span>
                        <a href="" ng-if="declarationPart.editMode == false && hasPermission('manufacturerpart','view')"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="declarationMfrPartsVm.showMfrDetails(declarationPart)">{{declarationPart.part.mfrName}}</a>
                        <span ng-if="!hasPermission('manufacturerpart','view')">{{declarationPart.part.mfrName}}</span>
                    </td>
                    <td class="col-width-250">
                        <item-status item="declarationPart.part"></item-status>
                    </td>
                    <td class="text-center"
                        ng-hide="declaration.status == 'RECEIVED' || declaration.status == 'ACCEPTED'">
                        <span class="btn-group"
                              ng-if="declarationPart.editMode == true && loginPersonDetails.external == false && declaration.status == 'OPEN'"
                              style="margin: -1px">
                            <button class="btn btn-xs btn-success" type="button" title="{{saveItemTitle}}"
                                    ng-click="declarationMfrPartsVm.createDeclarationPart(declarationPart)">
                                <i class="fa fa-check"></i>
                            </button>
                            <button ng-show="declarationMfrPartsVm.itemFlag == true" class="btn btn-xs btn-danger"
                                    type="button"
                                    title="{{cancelChangesTitle}}"
                                    ng-click="declarationMfrPartsVm.onCancel(declarationPart)">
                                <i class="fa fa-times"></i>
                            </button>
                            <button ng-show="declarationMfrPartsVm.itemFlag == false" class="btn btn-xs btn-danger"
                                    type="button"
                                    title="{{cancelChangesTitle}}"
                                    ng-click="declarationMfrPartsVm.cancelChanges(declarationPart)">
                                <i class="fa fa-times"></i>
                            </button>
                        </span>
                        <span class="row-menu"
                              ng-if="declarationPart.editMode == false && !loginPersonDetails.external && declaration.status == 'OPEN'"
                              uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <%--<li>
                                    <a href="" ng-click="declarationMfrPartsVm.editDeclarationPart(declarationPart)"
                                       translate>EDIT_PART</a>
                                </li>--%>
                                <li>
                                    <a href=""
                                       ng-click="declarationMfrPartsVm.deleteDeclarationPart(declarationPart)"
                                       translate>REMOVE_PART</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                <tr ng-if="declarationPart.expanded" ng-repeat-end="">
                    <td colspan="8" style="background: white;padding-bottom: 5px;">
                        <h5 style="width: 95%;margin: auto;padding: 5px 0" translate>BILL_OF_SUBSTANCE</h5>
                        <table class='table table-striped highlight-row'
                               style="width: 95%;margin: auto;border: 1px solid lightgrey;">
                            <thead>
                            <tr>
                                <th class="col-width-150" translate>CAS_NUMBER</th>
                                <th class="col-width-200" translate>NAME</th>
                                <%--<th class="col-width-150" translate>TYPE</th>--%>
                                <th class="col-width-250" translate>DESCRIPTION</th>
                                <th class="col-width-150" ng-if="loginPersonDetails.external == false" translate>
                                    THRESHOLD_MASS
                                </th>
                                <th>
                                    <span ng-if="!loginPersonDetails.external" translate>DECLARED_MASS</span>
                                    <span ng-if="loginPersonDetails.external" translate>MASS</span>
                                </th>
                                <th ng-if="loginPersonDetails.external" translate>UNITS</th>
                                <th style="width: 100px;text-align: center"
                                    ng-hide="declaration.status == 'RECEIVED' || declaration.status == 'ACCEPTED'"
                                    translate>ACTIONS
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="substance in declarationPart.substances">
                                <td class="col-width-150">{{substance.casNumber}}</td>
                                <td class="col-width-200">{{substance.substanceName}}</td>
                                <%--<td class="col-width-150">{{substance.substanceType}}</td>--%>
                                <td class="col-width-250">{{substance.description}}</td>
                                <td class="col-width-150" ng-if="loginPersonDetails.external == false">
                                    {{substance.thresholdMass}} ( {{substance.thresholdUnitSymbol}} )
                                </td>
                                <td style="width: 200px">
                                    <span ng-if="!substance.editMode">{{substance.mass}}
                                        <span ng-if="!loginPersonDetails.external">( {{substance.unitSymbol}} )</span>
                                    </span>
                                    <input type="text" class="form-control" ng-if="substance.editMode"
                                           ng-model="substance.mass" valid-number/>
                                </td>
                                <td style="width: 150px" ng-if="loginPersonDetails.external">
                                    <span ng-if="!substance.editMode">{{substance.unitSymbol}}</span>
                                    <ui-select ng-model="substance.uom" ng-if="substance.editMode" theme="bootstrap"
                                               style="width:145px;"
                                               on-select="substance.unitName = $item.name;substance.unitSymbol = $item.symbol">
                                        <ui-select-match placeholder="Select Unit">{{$select.selected.name}}
                                        </ui-select-match>
                                        <ui-select-choices
                                                repeat="unit.id as unit in declarationMfrPartsVm.measurementUnits | filter: $select.search">
                                            <div ng-bind="unit.name"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </td>
                                <td class="text-center"
                                    ng-hide="declaration.status == 'RECEIVED' || declaration.status == 'ACCEPTED'">
                                    <span class="btn-group"
                                          ng-if="substance.editMode == true && declaration.supplierContact == loginPersonDetails.person.id && declaration.status == 'SUBMITTED'"
                                          style="margin: -1px">
                                        <button ng-if="substance.isNew" class="btn btn-xs btn-success" type="button"
                                                title="Save"
                                                ng-click="declarationMfrPartsVm.createPartSubstance(declarationPart,substance)">
                                            <i class="fa fa-check"></i>
                                        </button>
                                        <button ng-if="!substance.isNew" class="btn btn-xs btn-success" type="button"
                                                title="Save"
                                                ng-click="declarationMfrPartsVm.updatePartSubstance(declarationPart,substance)">
                                            <i class="fa fa-check"></i>
                                        </button>
                                        <button class="btn btn-xs btn-danger" type="button"
                                                title="Remove" ng-if="substance.isNew"
                                                ng-click="declarationMfrPartsVm.removePartSubstance(declarationPart,substance)">
                                            <i class="fa fa-times"></i>
                                        </button>
                                        <button ng-if="!substance.isNew" class="btn btn-xs btn-danger" type="button"
                                                title="Cancel"
                                                ng-click="declarationMfrPartsVm.cancelPartSubstanceChanges(substance)">
                                            <i class="fa fa-times"></i>
                                        </button>
                                    </span>
                                    <span class="row-menu"
                                          ng-if="substance.editMode == false && declaration.supplierContact == loginPersonDetails.person.id && declaration.status == 'SUBMITTED'"
                                          uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                            style="z-index: 9999 !important;">
                                            <li>
                                                <a href=""
                                                   ng-click="declarationMfrPartsVm.editPartSubstance(substance)"
                                                   translate>EDIT_SUBSTANCE</a>
                                            </li>
                                            <li>
                                                <a href=""
                                                   ng-click="declarationMfrPartsVm.deletePartSubstance(declarationPart,substance)"
                                                   translate>REMOVE_SUBSTANCE</a>
                                            </li>
                                        </ul>
                                    </span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>

                <%--<tr ng-if="declarationParts.length > 0"
                    ng-repeat="declarationPart in declarationParts">
                    <td>
                        <i class="la la-plus" title="{{declarationMfrPartsVm.addSubstancesTitle}}"
                           style="cursor: pointer" ng-click="declarationMfrPartsVm.addSubstances(declarationPart)"></i>
                    </td>
                    <td>
                        <a href=""
                           ng-click="declarationMfrPartsVm.showMfrPartsDetails(declarationPart)">{{declarationPart.part.partNumber}}</a>
                    </td>

                    <td class="col-width-250">{{declarationPart.part.partName}}</td>
                    <td>{{declarationPart.part.mfrPartType.name}}</td>
                    <td>
                        <span ng-if="declarationPart.editMode == true">{{declarationPart.part.manufacturerObject.name}}</span>
                        <span ng-if="declarationPart.editMode != true">{{declarationPart.part.mfrName}}</span>
                    </td>
                    <td class="col-width-250">
                        <item-status item="declarationPart.part"></item-status>
                    </td>
                    <td class="text-center" ng-hide="external.external == true && sharedPermission == 'READ'">
                     <span class="btn-group"
                           ng-if="declarationPart.editMode == true"
                           style="margin: -1px">
                    <button class="btn btn-xs btn-success" type="button" title="{{saveItemTitle}}"
                            ng-click="declarationMfrPartsVm.createDeclarationPart(declarationPart)">
                        <i class="fa fa-check"></i>
                    </button>
                    <button ng-show="declarationMfrPartsVm.itemFlag == true" class="btn btn-xs btn-danger" type="button"
                            title="{{cancelChangesTitle}}"
                            ng-click="declarationMfrPartsVm.onCancel(declarationPart)">
                        <i class="fa fa-times"></i>
                    </button>
                     <button ng-show="declarationMfrPartsVm.itemFlag == false" class="btn btn-xs btn-danger"
                             type="button"
                             title="{{cancelChangesTitle}}"
                             ng-click="declarationMfrPartsVm.cancelChanges(declarationPart)">
                         <i class="fa fa-times"></i>
                     </button>
                </span>
                <span class="row-menu" ng-hide="declarationPart.editMode == true" uib-dropdown
                      dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li>
                            <a href="" ng-click="declarationMfrPartsVm.editDeclarationPart(declarationPart)"
                               translate>EDIT_PART</a>
                        </li>
                        <li>
                            <a href=""
                               ng-click="declarationMfrPartsVm.deleteDeclarationPart(declarationPart)"
                               translate>REMOVE_PART</a>
                        </li>
                    </ul>
                </span>
                    </td>
                </tr>--%>
                </tbody>
            </table>
        </div>
    </div>
</div>