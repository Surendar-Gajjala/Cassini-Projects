<div>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 20px;">
                    <i class="la la-plus dropdown-toggle" ng-if="qcrCaPaVm.showAddButton"
                       ng-click="qcrCaPaVm.addCapa()" title="{{addCapaTitle}}"
                       style="cursor: pointer"></i>
                </th>
                <th class="" translate>ROOT_CAUSE_ANALYSIS</th>
                <th style="width: 150px" translate>CORRECTIVE_ACTION</th>
                <th translate>PREVENTIVE_ACTION</th>
                <th translate>CREATED_BY</th>
                <th translate>CREATED_DATE</th>
                <th translate>CAPA_NOTES</th>
                <th translate>AUDIT_BY</th>
                <th translate>AUDIT_DATE</th>
                <th translate>AUDIT_RESULT</th>
                <th translate>AUDIT_NOTES</th>
                <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;">
                    <span translate>ACTIONS</span>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="qcrCaPaVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_CAPA</span>
                </td>
            </tr>
            <tr ng-if="qcrCaPaVm.loading == false && qcrCaPaVm.capas.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/CAPA.png" alt="" class="image">

                        <div class="message" translate>NO_CAPA</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="capa in qcrCaPaVm.capas">
                <td></td>
                <td title="{{capa.rootCauseAnalysis}}">{{capa.rootCauseAnalysis}}
                </td>
                <td title="{{capa.correctiveAction}}">{{capa.correctiveAction}}
                </td>
                <td title="{{capa.preventiveAction}}">{{capa.preventiveAction}}
                </td>
                <td>{{capa.createdByObject.fullName}}</td>
                <td>{{capa.createdDate}}</td>
                <td title="{{capa.capaNotes}}">{{capa.capaNotes}}
                </td>
                <%--<td>

                </td>--%>
                <td>{{capa.auditedByObject.fullName}}</td>
                <td>{{capa.auditDate}}</td>
                <td>
                    <checklist-status status="capa.result"></checklist-status>
                </td>
                <td title="{{capa.auditNotes}}">
                    <span>{{capa.auditNotes}}</span>
                </td>
                <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      ng-if="!qcr.released && capa.latest && capa.result == 'NONE'" style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="qcrCaPaVm.editCaPa(capa,'EDIT')">
                            <a translate>EDIT_CAPA</a>
                        </li>
                        <li ng-click="qcrCaPaVm.editCaPa(capa,'AUDIT')">
                            <a translate>AUDIT</a>
                        </li>
                        <li ng-click="qcrCaPaVm.deleteCaPa(capa)">
                            <a translate>DELETE_CAPA</a>
                        </li>
                        <plugin-table-actions context="qcr.capa" object-value="capa"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>