<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th style="width: 30px;" ng-if="canAddRequest">
                <i class="la la-plus" style="cursor: pointer"
                   title="{{addChangeRequestTitle}}"
                   ng-click="addChangeRequests()"></i>
            </th>
            <th style="width: 150px" translate>NUMBER</th>
            <th style="width: 150px" translate>TYPE</th>
            <th style="width: 150px" translate>TITLE</th>
            <th style="width: 150px" translate>REASON_FOR_CHANGE</th>
            <th style="width: 150px" translate>URGENCY</th>
            <th style="width: 150px" translate>ORIGINATOR</th>
            <th style="width: 150px" translate>STATUS</th>
            <th style="width: 150px" translate>CHANGE_ANALYST</th>
            <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                ACTIONS
            </th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_CHANGE_REQUESTS</span>
            </td>
        </tr>
        <tr ng-if="loading == false && requestItems.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/changeRequest.png" alt="" class="image">

                    <div class="message">{{ 'NO_CHANGE_REQUEST' | translate}} </div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="item in requestItems">
            <td ng-if="canAddRequest"></td>
            <td style="width: 150px;">
                <a href=""
                   ng-click="showChangeRequest(item)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="item.crNumber | highlightText: freeTextQuery"></span>
                </a>
            </td>
            <td>{{item.type }}{{item.type.length > 17 ? '...' : ''}}</td>
            <td>{{item.title }}{{item.title.length > 17 ? '...' : ''}}</td>
            <td>{{item.reasonForChange}}</td>
            <td>
                <%--<dcr-urgency dcr="item"></dcr-urgency>--%>
                {{item.urgency}}
            </td>
            <td>{{item.originator}}</td>
            <td>
                <workflow-status workflow="item"></workflow-status>
            </td>
            <td>{{item.changeAnalyst}}</td>
            <td class="text-center actions-col sticky-col sticky-actions-col">
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px" ng-if="hasPermission">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li><a ng-click="deleteItem(item)"
                               ng-class="{'disabled':!canDeleteRequest}" translate>REMOVE_CHANGE_REQUEST</a>
                        </li>
                        <plugin-table-actions context="object.changerequest" object-value="item"></plugin-table-actions>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>