<div class="sidepanel-widget recently-visited-widget">
    <style scoped>
        .name-column3 {
            white-space: nowrap !important;
            text-align: left !important;
        }
    </style>

    <div class="widget-header">
        <h5 translate>RECENTLY_VISITED</h5>
    </div>
    <div class="widget-body">
        <div class='responsive-table'
             style="height: 100%;overflow:auto;width: 100%;position: relative;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr style="font-size: 14px;">
                    <th class="name-column3" translate>NAME</th>
                    <th style="width: 100px;" translate>OBJECT_TYPE</th>
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

                <tr ng-if="recentlyVisitedVm.loading == false && recentlyVisitedVm.recentlyVisited.length == 0">
                    <td colspan="10" translate>NO_RECENTLY_VISITED</td>
                </tr>
                <tr ng-repeat="recentlyVisited in recentlyVisitedVm.recentlyVisited"
                    style="font-size: 14px;">
                    <td class="name-column3" style="vertical-align: middle;">
                        <a href="" title="{{recentlyVisitedVm.detailsTitle}}"
                           style=""
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
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>