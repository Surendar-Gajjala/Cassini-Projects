<style>
    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    /* The Close Button */
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
              ng-if="varianceType == 'Deviation'" translate>DEVIATION</span>

         <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
               ng-if="varianceType == 'Waiver'" translate>WAIVER</span>

        <button class="btn btn-sm new-button" ng-click="allVarianceVm.newVariance()" id="newVariance"
                ng-if="hasPermission('change','deviation','create') || hasPermission('change','waiver','create')"
                title="{{(varianceType == 'Deviation' ? 'DEVIATION_ALL_NEW_DEVIATION' : 'WAIVER_ALL_NEW_WAIVER') | translate}}">
            <i class="las la-plus" aria-hidden="true"></i>
            <span ng-if="varianceType == 'Deviation'">{{'NEW' | translate}} {{'DEVIATION_SINGULAR' | translate }}</span>
            <span ng-if="varianceType == 'Waiver'">{{'NEW' | translate}} {{'WAIVER_SINGULAR' | translate }}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allVarianceVm.showTypeAttributes()" id="attributesButton"
                    title="{{allVarianceVm.showAttributes}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
            <button class="btn btn-sm btn-default" id="preferedPageButton"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allVarianceVm.resetPage" search-term="allVarianceVm.searchText"
                          on-search="allVarianceVm.freeTextSearch"
                          filter-search="allVarianceVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px" translate></th>
                    <th style="width: 100px" ng-if="varianceType == 'Deviation'" translate>DEVIATION_NUMBER</th>
                    <th style="width: 100px" ng-if="varianceType == 'Waiver'" translate>WAIVER_NUMBER</th>
                    <!-- <th style="width: 150px" ng-if="varianceType == 'Deviation'" translate>DEVIATION_FOR</th> -->
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Deviation'">
                        <span ng-if="selectedDeviationFor != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedDeviationFor}})
                                <i class="fa fa-times-circle" ng-click="clearDeviation()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>DEVIATION_FOR</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                             </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="source in allVarianceVm.selectedDeviationFor"
                                    ng-click="onSelectDeviationFor(source)"><a
                                        href="">{{source}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Waiver'">
                        <span ng-if="selectedWaiverFor != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedWaiverFor}})
                                <i class="fa fa-times-circle" ng-click="clearWaiver()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>WAIVER_FOR</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                             </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="source in allVarianceVm.selectedWaiverFor"
                                    ng-click="onSelectWaiverFor(source)"><a
                                        href="">{{source}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th class="col-width-150" translate>TITLE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px" ng-if="varianceType == 'Waiver'" translate>EFFECTIVE_TYPE</th>
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Deviation'">
                        <span ng-if="selectedEffictivityType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedEffictivityType}})
                                <i class="fa fa-times-circle" ng-click="clearEffictivity()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>EFFECTIVE_TYPE</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                             </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="source in allVarianceVm.selectedEffictivityType"
                                    ng-click="onSelectEffictivityType(source)"><a
                                        href="">{{source}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th class="col-width-250" translate>REASON_FOR_VARIANCE</th>
                    <th class="col-width-250" translate>CURRENT_REQUIREMENT</th>
                    <th class="col-width-250" translate>REQUIREMENT_DEVIATION</th>
                    <th></th>
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Waiver'">
                        <span ng-if="selectedStatus != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedStatus}})
                                <i class="fa fa-times-circle" ng-click="clearStatus()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>STATUS</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat=" status in statuses"
                                    ng-click="onSelectStatus(status)"><a
                                        href="">{{status}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Deviation'">
                        <span ng-if="selectedStatus != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedStatus}})
                                <i class="fa fa-times-circle" ng-click="clearDeviationStatus()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>STATUS</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat=" status in statuses"
                                    ng-click="onSelectDeviationStatus(status)"><a
                                        href="">{{status}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <!-- <th style="width: 150px" ng-if="varianceType == 'Deviation'" translate>ORIGINATOR</th> -->
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Deviation'">
                        <span ng-if="selectedPerson != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedPerson.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearDeviationOriginator()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>ORIGINATOR</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;margin-top:5px;">
                                <li ng-repeat=" person in originators"
                                    ng-click="onSelectDeviationOriginator(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th style="width: 150px;z-index: auto !important;" ng-if="varianceType == 'Waiver'">
                        <span ng-if="selectedPerson != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedPerson.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearOriginator()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>ORIGINATOR</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;margin-top:5px;">
                                <li ng-repeat=" person in originators"
                                    ng-click="onSelectOriginator(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allVarianceVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allVarianceVm.removeAttribute(selectedAttribute)"
                           title={{allVarianceVm.RemoveColumnTitle}}></i>
                    </th>
                    <th style="width: 100px;text-align: center" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allVarianceVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_VARIANCES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allVarianceVm.loading == false && allVarianceVm.variances.content.length == 0">
                    <%--<td colspan="25" translate ng-if="varianceType == 'Deviation'">NO_DEVIATIONS</td>
                    <td colspan="25" translate ng-if="varianceType == 'Waiver'">NO_WAIVERS</td>--%>
                    <td colspan="25" ng-if="varianceType == 'Deviation'"
                        style="background-color: var(--cassini-bg-color)  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Deviation.png" alt="" class="image">

                            <div class="message" translate>NO_DEVIATIONS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                    <td colspan="25" ng-if="varianceType == 'Waiver'"
                        style="background-color: #f9fbfe   !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Waiver.png" alt="" class="image">

                            <div class="message" translate>NO_WAIVERS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="variance in allVarianceVm.variances.content">
                    <td style="width: 30px;">
                        <span ng-if="variance.recurring == true">
                        <i class="fa fa-repeat" title="{{allVarianceVm.recurringItem}}" aria-hidden="true"></i>
                        </span>
                    </td>
                    <td>
                        <a href=""
                           ng-click="allVarianceVm.showVariance(variance)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="variance.varianceNumber | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td>
                        <variance-for variance="variance"></variance-for>
                    </td>
                    <td class="col-width-200">
                        <span ng-bind-html="variance.title | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="variance.description  | highlightText: freeTextQuery"
                              title="{{variance.description}}"></span>
                    </td>
                    <td>{{variance.effecitivityType}}</td>
                    <td class="col-width-250">
                        <span ng-bind-html="variance.reasonForVariance  | highlightText: freeTextQuery"
                              title="{{variance.reasonForVariance}}"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="variance.currentRequirement  | highlightText: freeTextQuery"
                              title="{{variance.currentRequirement}}"></span>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="variance.requirementDeviation  | highlightText: freeTextQuery"
                              title="{{variance.requirementDeviation}}"></span>
                    </td>
                    <td style="width: 1% !important;white-space: nowrap">
                        <i ng-if='variance.startWorkflow && !variance.finishWorkflow && !variance.cancelWorkflow'
                           style='font-size: 18px;color: #ffa800' class='la la-clock-o' title="In Progress"></i>
                        <i ng-if='variance.finishWorkflow' style='font-size: 18px;color: #1CAF9A'
                           class='la la-check-circle-o' title="Finished"></i>
                        <i ng-if='variance.cancelWorkflow' style='font-size: 18px;color: #f64e60'
                           class='la la-times-circle-o' title="Rejected"></i>
                    </td>
                    <td>
                        <workflow-status-settings workflow="variance"></workflow-status-settings>
                        <span class="label label-warning" ng-if="variance.onHold">HOLD</span>
                    </td>
                    <td>{{variance.originator.fullName}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allVarianceVm.selectedAttributes">
                        <all-view-attributes object="variance" object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center" style="width: 100px;text-align: center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li title="Edit Variance"
                                    ng-if="hasPermission('change','variance','edit')"
                                    ng-click="allVarianceVm.editVariance(variance)"
                                    ng-class="{'disabled': variance.statusType == 'REJECTED' || variance.statusType == 'RELEASED'}">
                                    <a href="" translate>EDIT</a>
                                </li>

                                <li ng-if="varianceType == 'Deviation'">
                                    <a href=""
                                       ng-click="showPrintOptions(variance.id,'DEVIATION')"
                                       translate>PREVIEW_AND_PRINT</a>
                                </li>

                                <li ng-if="varianceType == 'Waiver'">
                                    <a href=""
                                       ng-click="showPrintOptions(variance.id,'WAIVER')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'CHANGE'" object="variance.id"
                                             tags-count="variance.tagsCount"></tags-button>
                                <li title="{{ variance.statusType == 'REJECTED' || variance.statusType == 'RELEASED' ? cannotDeleteReleasedVariance:''}}">
                                    <a href="" title="Delete Variance"
                                       ng-if="hasPermission('change','variance','delete')"
                                       ng-click="allVarianceVm.deleteVariance(variance)"
                                       ng-class="{'disabled': variance.statusType == 'REJECTED' || variance.statusType == 'RELEASED'}"
                                       translate>
                                        DELETE
                                    </a>
                                </li>
                                <plugin-table-actions context="variance.all"
                                                      object-value="variance"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allVarianceVm.variances" pageable="allVarianceVm.pageable"
                          previous-page="allVarianceVm.previousPage" next-page="allVarianceVm.nextPage"
                          page-size="allVarianceVm.pageSize"></table-footer>
        </div>
    </div>
</div>
