<div>
<style scoped>
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

    .notification1 {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .notification1 .badge1 {
        position: absolute;
        top: -10px;
        left: -10px;
        padding: 2px 6px;
        border-radius: 50%;
        background-color: orange;
        color: white;
        font-size: 14px;
    }

    .popover-title {
        font-size: 14px;
        font-weight: 600;
        text-align: center;
        line-height: 25px;
    }

    .popover {
        max-width: 500px;
        width: 500px;
    }

    .popover-content {
        max-height: 250px;
        overflow-y: auto;
    }

    .popover table {
        width: 497px;
        max-width: 100% !important;
    }

    .popover.bottom > .arrow::after {
        border-bottom-color: #f7f7f7;
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

    .table {
        width: 100% !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    th.number-column, td.number-column {
        width: 150px;
    }

    th.name-column2, td.name-column2 {
        width: 200px !important;
    }

    th.description-column2, td.description-column2 {
        width: 250px !important;

    }

    th.revision-column, td.revision-column {
        width: 150px;
        text-align: center;
    }

    th.lifecycle-column, td.lifecycle-column {
        width: 150px;

    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    th.modifiedby-column, td.modifiedby-column {
        width: 150px;
    }

    th.modifieddate-column, td.modifieddate-column {
        width: 150px;
    }

    .added-column {
        text-align: left;
        width: 200px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    /* The Close Button */
    .img-model .closeImage,
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

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

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        opacity: 0;
        transition: opacity 1s;
        margin-left: -25px !important;
        word-break: break-all !important;

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
        position: relative !important;
        visibility: visible;
        opacity: 1;
    }

    p.ng-binding.ng-scope {
        min-width: 200px !important;
        max-width: 200px;;
        margin-bottom: 12px;
        word-break: break-all;
        overflow: visible;
        white-space: normal;
        height: auto; /* just added this line */
    }

    /*    .JCLRLastGrip .grip {
            display: none;
        }

        .grip {
            width: 100px;
            height: 10px;
            margin-top: -10px;
            /!*background-image: url('app/assets/images/favicon.png');*!/
            margin-left: 13px;
            position: relative;
            z-index: 88;
            cursor: move !important;
        }

        .grip:hover {
            background-position: 20px 0px;
            cursor: move !important;
        }

        .dragging .grip {
            background-position: 40px 0px;
        }

        .JColResizer {
            table-layout: auto !important;
        }*/

    .JCLRLastGrip {
        display: none !important;
    }

    /* .grip {

         width: 100px;
         height: 10px;
         margin-top: -10px;
         background-image: url('app/assets/images/favicon.png');
         margin-left: 13px;
         position: relative;
         z-index: 88;
         cursor: move !important;
         background-repeat: no-repeat;

     }*/

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 4;
    }

    #freeTextSearchDirective {
        margin-right: 41px !important;
        top: 7px !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="line-height: 35px; font-weight: 600;font-size: 20px;padding-right: 10px;" translate>LOG_HISTORY_TITLE</span>
        <free-text-search on-clear="allActivitiesVm.resetPage" search-term="allActivitiesVm.searchText"
                          on-search="allActivitiesVm.freeTextSearch"></free-text-search>

        <div class="btn-group pull-right">
            <img src="app/assets/images/excel.png" ng-click="allActivitiesVm.exportExcel()"
                 title="{{'EXPORT_EXCEL' | translate}}"
                 style="width: 30px !important;height: 30px !important;cursor: pointer !important;margin: 0 10px !important;">
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row" style="width: 100% !important;"
                   id="activities"
                    >
                <thead>
                <tr>

                    <th class="number-column" translate>TYPE</th>
                    <th class="name-column2" translate style="z-index: auto !important;">DATE</th>
                    <th class="" translate style="z-index: auto !important;">ACTIVITY</th>
                    <th class="revision-column" style="z-index: auto !important;" translate>USER</th>
                    <%--<th class="lifecycle-column" style="z-index: auto !important;" translate>ACTIVITY</th>--%>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allActivitiesVm.loading">
                    <td colspan="10" translate>LOADING</td>
                </tr>

                <tr ng-if="!allActivitiesVm.loading && allActivitiesVm.logHistory.totalElements == 0">
                    <td colspan="10" translate>NO_ACTIVITIES</td>
                </tr>
                <tr ng-repeat="log in allActivitiesVm.logHistory.content">
                    <td><span ng-bind-html="log.type |highlightText: freeTextQuery"></span></td>
                    <td><span ng-bind-html="log.time"></span></td>
                    <%--<td><span ng-bind-html="log.name |highlightText: freeTextQuery"></span></td>--%>
                    <td class=""><span style="white-space: normal;word-wrap: break-word;">

                         <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                             <span ng-bind-html="log.name"></span>
                         </div>


                    </span>
                    </td>


                    <td><span ng-bind-html="log.userName |highlightText: freeTextQuery"></span></td>

                </tr>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="table-footer">
            <div>
                <div>
                    <h5><span style="padding: 5px" translate>DISPLAYING</span>
                        <span ng-if="allActivitiesVm.logHistory.totalElements ==0">{{(allActivitiesVm.pageable.page*allActivitiesVm.pageable.size)}}</span>
                        <span ng-if="allActivitiesVm.logHistory.totalElements > 0">{{(allActivitiesVm.pageable.page*allActivitiesVm.pageable.size)+1}}</span>-
                        <span ng-if="allActivitiesVm.logHistory.last ==false">{{((allActivitiesVm.pageable.page+1)*allActivitiesVm.pageable.size)}}</span>
                        <span ng-if="allActivitiesVm.logHistory.last == true">{{allActivitiesVm.logHistory.totalElements}}</span>

                        <span translate>OF</span> &nbsp;{{allActivitiesVm.logHistory.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10"><span translate>PAGE</span> {{allActivitiesVm.logHistory.totalElements != 0 ? allActivitiesVm.logHistory.number+1:0}} <sapn
                            translate>OF
                    </sapn> {{allActivitiesVm.logHistory.totalPages}}</span>
                    <a href="" ng-click="allActivitiesVm.previousPage()"
                       ng-class="{'disabled': allActivitiesVm.logHistory.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="allActivitiesVm.nextPage()"
                       ng-class="{'disabled': allActivitiesVm.logHistory.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
</div>