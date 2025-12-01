<style>
    #td {
        word-wrap: break-word;
        width: 400px;
        white-space: normal;
        text-align: left;
    }

    .responsive-table .dropdown-content {
        margin-left: -45px !important;
    }

    table {
        table-layout: auto;
    }
</style>
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 40px;">
                <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                      ng-if="(hasPermission || (loginPersonDetails.person.id == projectInfo.projectManager && external.external== true))"
                      ng-hide="projectPercentComplete == 100">
                        <i class="la la-plus dropdown-toggle" style="padding-left: 3px;" ng-click="addItems()" title="{{ 'ADD_ITEM' | translate }}" <%--uib-dropdown-toggle--%>></i>
                        <%--<ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-click="addItems()" translate>ADD_ITEM</a>
                            </li>
                        </ul>--%>
                </span>
            </th>
            <th translate="ITEM_ALL_ITEM_NUMBER"></th>
            <th translate="ITEM_ALL_ITEM_TYPE"></th>
            <th class="col-width-250" translate="ITEM_ALL_ITEM_NAME"></th>
            <th class="description-column" translate="ITEM_ALL_DESCRIPTION"></th>
            <th style="width: 100px;text-align: center" translate="ITEM_ALL_REVISION"></th>
            <th style="width: 100px;" translate="ITEM_ALL_LIFECYCLE"></th>
            <th style="width: 70px;" ng-if="hasPermission" translate="ACTIONS"></th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="loading == false && itemReferences.length == 0">
            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Items.png" alt="" class="image">

                    <div class="message" translate>NO_ITEMS</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>

        <tr ng-repeat="item in itemReferences">
            <td></td>
            <td><a href="" ng-click="showItem(item)"
                   ng-if="!loginPersonDetails.external"
                   title="{{clickToShowItem}}">{{item.plmItem.itemNumber}}</a>
                <span ng-if="loginPersonDetails.external">{{item.plmItem.itemNumber}}</span>
            </td>
            <td>{{item.plmItem.itemType.name}}</td>
            <td class="col-width-250">{{item.plmItem.itemName}}</td>
            <td class="description-column" title="{{item.plmItem.description}}"><span
                    ng-bind-html="item.plmItem.description  | highlightText: freeTextQuery"></span>
            </td>
            <td style="width: 100px;text-align: center">{{item.item.revision}}</td>
            <td>
                <item-status item="item.item"></item-status>
            </td>
            <td class="text-center" ng-if="hasPermission">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-hide="projectPercentComplete == 100"
                                    ng-click="deleteItemReference(item)">
                                    <a translate>REMOVE_ITEM</a>
                                </li>
                                <plugin-table-actions context="object.reference" object-value="item"></plugin-table-actions>
                            </ul>
                    </span>
            </td>

        </tr>
        </tbody>
    </table>
</div>