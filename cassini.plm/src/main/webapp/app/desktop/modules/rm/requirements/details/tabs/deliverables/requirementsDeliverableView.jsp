<%--
  Created by IntelliJ IDEA.
  User: swapna
  Date: 1/16/18
  Time: 3:32 PM
  To change this template use File | Settings | File Templates.
--%>
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

    /* The Close Button */
    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .active-deliverableTab {
        background-color: #2a6fa8 !important;
        color: white !important;
        font-weight: 600;
    }

    .tab-content {
        padding: 0px !important;
    }

    .round {
        width: 27%;
        min-width: 11%;
        border-radius: 26px;
        padding: 4px 5px 7px 119px;
        top: 0;
        left: 0;
        z-index: 5;
        border: 1px #e6e8ed solid;
        background-color: rgb(241, 243, 244);
        outline: none;
    }

    .round > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    #lists {
    }

    .round > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    .fa-search {
        position: relative;
        margin-top: 11px;
        color: grey;
        opacity: 0.5;
        margin-right: -28px !important;
    }

    i.fa-times-circle {
        margin-left: -25px;
        color: gray;
        cursor: pointer;
    }

    .responsive-table .dropdown-content {
        margin-left: -85px !important;
    }

    table {
        table-layout: auto;
    }

</style>
<div class="responsive-table" style="padding: 10px;overflow-y: auto"
     ng-hide="reqDeliverablesVm.specification ==true || reqDeliverablesVm.requirement ==true">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 10px;cursor: pointer"><i class="la la-plus"
                   title={{reqDeliverablesVm.addItems}}
                   ng-hide="selectedRequirement.status == 'FINISHED'"
                   ng-if="hasPermission('pgcspecification','edit') || specPermission.editPermission"
                   ng-click="showDeliverables()"></i></th>
            <th style="width: 1% !important;white-space: nowrap;" translate="ITEM_ALL_ITEM_NUMBER"></th>
            <th style="width: 150px;" translate="ITEM_ALL_ITEM_TYPE"></th>
            <th class="col-width-250" translate="ITEM_ALL_ITEM_NAME"></th>
            <th class="description-column" translate="ITEM_ALL_DESCRIPTION"></th>
            <th style="width: 75px;text-align: center" translate="ITEM_ALL_REVISION"></th>
            <th style="width: 100px;" translate="ITEM_ALL_LIFECYCLE"></th>
            <th style="width: 150px;text-align: center" translate="ITEM_ALL_RELEASED_DATE"></th>
            <th style="width: 70px;text-align: center" translate="ACTIONS"></th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="reqDeliverablesVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="reqDeliverablesVm.loading == false && reqDeliverablesVm.requirementDeliverables.length == 0">
            <td colspan="25" translate>NO_DELIVERABLES</td>
        </tr>

        <tr ng-repeat="item in reqDeliverablesVm.requirementDeliverables">
            <td></td>
            <td style="width: 1% !important;white-space: nowrap;">
                <a href="" ng-click="reqDeliverablesVm.showItem(item)"
                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                    <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span></a>
            </td>
            <td style="width: 150px;">
                <span ng-bind-html="item.itemType.name | highlightText: freeTextQuery"></span>
            </td>
            <td class="col-width-250">
                <span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
            </td>
            <td class="description-column"  title="{{item.description}}">
                <span ng-bind-html="item.description  | highlightText: freeTextQuery"></span>
            </td>
            <td style="text-align: center;width: 75px;">{{item.latestRevisionObject.revision}}</td>
            <td>
                <item-status item="item.latestRevisionObject"></item-status>
            </td>
            <td style="text-align: center">
                <span ng-if="item.itemRevision.releasedDate == null">{{'--'}}</span>
                <span>{{item.latestRevisionObject.releasedDate}}</span>
            </td>
            <td class="text-center" style="width: 70px;text-align: center">
                      <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu
                                class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="(hasPermission('pgcspecification','edit') || specPermission.editPermission) && selectedRequirement.status !='FINISHED'"
                                    ng-click="reqDeliverablesVm.deleteDeliverable(item)">
                                    <a href="" translate>DELETE_DELIVERABLE</a>
                                </li>
                            </ul>
                      </span>
            </td>

        </tr>
        </tbody>
    </table>
</div>




