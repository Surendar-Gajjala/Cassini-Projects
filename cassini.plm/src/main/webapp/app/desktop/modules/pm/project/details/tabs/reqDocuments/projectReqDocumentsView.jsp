<style>
    table {
        table-layout: auto;
    }
</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 15px;">
                <i class="la la-plus" style="cursor: pointer"
                   ng-if="projectDetailsPermission && projectPercentComplete < 100"
                   ng-click="projectReqDocumentsVm.addRequirementDocuments()"></i>
            </th>
            <th style="width: 150px" translate>NUMBER</th>
            <th class="col-width-150" translate>TYPE</th>
            <th class="col-width-200" translate>NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th style="width: 150px; text-align: center;z-index: auto !important;"
                translate="ITEM_ALL_REVISION"></th>
            <th style="width: 150px;z-index: auto !important;" translate="ITEM_ALL_LIFECYCLE"></th>
            <th style="width: 150px" translate>DOCUMENT_OWNER</th>
            <th style="width: 75px;text-align: center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="projectReqDocumentsVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"> {{ 'LOADING_REQ_DOCS ' | translate }}
                        </span>
            </td>
        </tr>

        <tr ng-if="projectReqDocumentsVm.loading == false && projectReqDocumentsVm.projectReqDocuments.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/ProjectTeam.png" alt="" class="image">

                    <div class="message" translate>NO_REQ_DOCUMENT</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                        NO_PERMISSION_MESSAGE
                    </div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="projectReqDoc in projectReqDocumentsVm.projectReqDocuments">
            <td></td>
            <td style="width: 150px;">
                <a ng-if="!loginPersonDetails.external" href=""
                   ng-click="projectReqDocumentsVm.showReqDocument(projectReqDoc)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="projectReqDoc.reqDocument.master.number | highlightText: freeTextQuery"></span>
                </a>
                <span ng-if="loginPersonDetails.external"
                      ng-bind-html="projectReqDoc.reqDocument.master.number | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-150">
                <span ng-bind-html="projectReqDoc.reqDocument.master.type.name | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-200">
                <span ng-bind-html="projectReqDoc.reqDocument.master.name | highlightText: freeTextQuery"></span>
            </td>
            <td title="{{reqDocument.description}}" class="col-width-250">
                <span ng-bind-html="projectReqDoc.reqDocument.description  | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 150px; text-align: center;">
                {{projectReqDoc.reqDocument.revision}}
            </td>
            <td style="width: 150px">
                <item-status item="projectReqDoc.reqDocument"></item-status>
            </td>
            <td>{{projectReqDoc.reqDocument.owner.fullName}}</td>
            <td class="text-center">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                          ng-if="projectDetailsPermission && projectPercentComplete < 100">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                            style="z-index: 9999 !important;">
                            <li ng-click="projectReqDocumentsVm.deleteProjectReqDocument(projectReqDoc)">
                                <a href="" translate>REMOVE_REQ_DOCUMENT</a>
                            </li>
                            <plugin-table-actions context="project.requirement"
                                                  object-value="projectReqDoc"></plugin-table-actions>
                        </ul>
                    </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>