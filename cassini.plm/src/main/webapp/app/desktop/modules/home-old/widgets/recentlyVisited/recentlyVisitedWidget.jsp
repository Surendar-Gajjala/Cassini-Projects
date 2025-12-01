<div>
    <style scoped>
        .name-column3 {
            word-wrap: normal !important;
            white-space: normal !important;
            text-align: left !important;
        }
    </style>

    <div class="panel panel-default panel-alt widget-messaging">
        <div class="panel-heading" style="">

            <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
                <span style="font-weight: bold;" translate>RECENTLY_VISITED</span>
            </div>

            <div class="pull-right text-center" style="padding: 2px;">
            <span ng-if="recentlyVisitedVm.loading == false"><small>
                {{recentlyVisitedVm.recentlyVisitedObjects.totalElements}}
                <span translate>RECENTLY_VISITED</span>
            </small></span>

                <div class="btn-group" style="">
                    <button class="btn btn-xs btn-default"
                            ng-click="recentlyVisitedVm.previousPage()"
                            ng-disabled="recentlyVisitedVm.recentlyVisitedObjects.first">
                        <i class="fa fa-chevron-left"></i>
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="recentlyVisitedVm.nextPage()"
                            ng-disabled="recentlyVisitedVm.recentlyVisitedObjects.last">
                        <i class="fa fa-chevron-right"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="panel-body">
            <div class="widget-panel" style="max-height: 400px; min-height: 400px;">

                <div class="responsive-table" style="padding: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr style="font-size: 14px;">
                            <th style="width: 30px"></th>
                            <th class="name-column3" translate>NAME</th>
                            <th style="width: 125px;" translate>OBJECT_TYPE</th>
                            <th style="width: 150px;" translate>VISITED_DATE</th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr ng-if="recentlyVisitedVm.loading == true">
                            <td colspan="10">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_RECENTLY_VISITED</span>
                           </span>
                            </td>
                        </tr>

                        <tr ng-if="recentlyVisitedVm.loading == false && recentlyVisitedVm.recentlyVisitedObjects.content.length == 0">
                            <td colspan="10" translate>NO_RECENTLY_VISITED</td>
                        </tr>
                        <tr ng-repeat="recentlyVisited in recentlyVisitedVm.recentlyVisitedObjects.content"
                            style="font-size: 14px;">
                            <td style="width: 30px;">
                                <a href="">
                                    <i class="fa fa-paperclip"
                                       title="{{recentlyVisitedVm.showFileTitle}}" ng-if="recentlyVisited.hasFiles">
                                    </i>
                                    <i ng-if="recentlyVisited.configurable"
                                       title="{{recentlyVisitedVm.configurableItem}}" class="fa fa-cog"
                                       aria-hidden="true"></i>
                                    <i ng-if="recentlyVisited.configured" title="{{recentlyVisitedVm.configuredItem}}"
                                       class="fa fa-cogs"
                                       aria-hidden="true"></i>
                                    <i class="fa fa-sitemap"
                                       title="{{recentlyVisitedVm.itemBom}}" ng-if="recentlyVisited.hasBom">
                                    </i>
                                </a>
                            </td>
                            <td class="name-column3" style="vertical-align: middle;">
                                <a href="" title="{{recentlyVisitedVm.detailsTitle}}"
                                   style="white-space: normal;word-wrap: break-word;"
                                   ng-click="recentlyVisitedVm.showObjectDetails(recentlyVisited)">

                                    <div style="">
                                    <span>
                                        {{recentlyVisited.name | limitTo: 40}}{{recentlyVisited.name.length > 37 ? '...' : ''}}
                                    </span>
                                    </div>
                                </a>
                            </td>
                            <td style="vertical-align: middle;">
                                <object-type-status object="recentlyVisited"></object-type-status>
                            </td>
                            <%--<td style="vertical-align: middle;">{{recentlyVisited.visitedDate}}</td>--%>
                            <td style="vertical-align: middle;">
                                {{recentlyVisited.visitedDate}}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>