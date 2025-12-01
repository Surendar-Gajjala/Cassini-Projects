<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 20px;" ng-if="!qcrReleased">
                <i class="la la-plus dropdown-toggle" ng-click="qcrProblemSourcesVm.addProblemSources()"
                   style="cursor: pointer" title="{{addProblemSourcesTitle}}"></i>
            </th>
            <th style="width: 150px;">
                <span ng-if="qcrFor == 'PR'" translate>PR_NUMBER</span>
                <span ng-if="qcrFor == 'NCR'" translate>NCR_NUMBER</span>
            </th>
            <th style="width: 150px" translate>TYPE</th>
            <th style="width: 150px;">
                <span ng-if="qcrFor == 'PR'" translate>PRODUCT</span>
                <span ng-if="qcrFor == 'NCR'" translate>TITLE</span>
            </th>
            <th style="width: 150px;" ng-if="qcrFor == 'PR'" translate>PROBLEM</th>
            <th ng-if="qcrFor == 'NCR'" translate>DESCRIPTION</th>
            <th translate>DEFECT_TYPE</th>
            <th translate>SEVERITY</th>
            <th translate>DISPOSITION</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="qcrProblemSourcesVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_PROBLEM_SOURCES</span>
                        </span>
            </td>
        </tr>
        <tr ng-if="qcrProblemSourcesVm.loading == false && qcrProblemSourcesVm.problemSources.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/ProblemSources.png" alt="" class="image">

                    <div class="message" translate>NO_PRS_SOURCES</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="problemSource in qcrProblemSourcesVm.problemSources track by $index">
            <td ng-if="!qcrReleased"></td>
            <td>
                <a ng-if="qcrFor == 'PR'" ui-sref="app.pqm.pr.details({problemReportId:problemSource.pr.id})"
                   ng-click="showSource()" title="{{clickToShowDetails}}">
                    {{problemSource.pr.prNumber}}
                </a>
                <a ng-if="qcrFor == 'NCR'" ui-sref="app.pqm.ncr.details({ncrId:problemSource.ncr.id})"
                   ng-click="showSource()" title="{{clickToShowDetails}}">
                    {{problemSource.ncr.ncrNumber}}
                </a>
            </td>
            <td>
                <span ng-if="qcrFor == 'PR'">{{problemSource.pr.prType.name}}</span>
                <span ng-if="qcrFor == 'NCR'">{{problemSource.ncr.ncrType.name}}</span>
            </td>
            <td>
                <span ng-if="qcrFor == 'PR'">{{problemSource.prDto.product}}</span>
                <span ng-if="qcrFor == 'NCR'" title="{{problemSource.ncr.title}}">
                    {{problemSource.ncr.title}}
                </span>
            </td>
            <td ng-if="qcrFor == 'PR'" title="{{problemSource.pr.problem}}">
                {{problemSource.pr.problem}}
            </td>
            <td ng-if="qcrFor == 'NCR'" title="{{problemSource.ncr.description}}">
                {{problemSource.ncr.description}}
            </td>
            <td>
                <span ng-if="qcrFor == 'PR'">{{problemSource.pr.failureType}}</span>
                <span ng-if="qcrFor == 'NCR'">{{problemSource.ncr.failureType}}</span>
            </td>
            <td>
                <span ng-if="qcrFor == 'PR'">{{problemSource.pr.severity}}</span>
                <span ng-if="qcrFor == 'NCR'">{{problemSource.ncr.severity}}</span>
            </td>
            <td>
                <span ng-if="qcrFor == 'PR'">{{problemSource.pr.disposition}}</span>
                <span ng-if="qcrFor == 'NCR'">{{problemSource.ncr.disposition}}</span>
            </td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu"
                      ng-if="hasPermission('qcr','edit') && !qcrReleased" uib-dropdown
                      dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="qcrProblemSourcesVm.deleteItem(problemSource)">
                            <a translate>REMOVE_SOURCE</a>
                        </li>
                        <plugin-table-actions context="qcr.problemSource" object-value="problemSource"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>