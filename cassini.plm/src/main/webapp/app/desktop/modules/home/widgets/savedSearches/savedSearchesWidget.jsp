<div class="sidepanel-widget saved-searches-widget">
    <style scoped>
        a:visited, a:active, a:focus {
            text-decoration: none !important;
        }
    </style>
    <div class="widget-header">
        <h5 translate>SAVED_SEARCHES</h5>
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
                <tr ng-if="savedSearchesVm.loading == true">
                    <td colspan="10">
                       <span style="font-size: 15px;">
                           <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                class="mr5"><span translate>LOADING_SAVED_SEARCHES</span>
                       </span>
                    </td>
                </tr>

                <tr ng-if="savedSearchesVm.loading == false && savedSearchesVm.savedSearches == 0">
                    <td colspan="10" translate>NO_SAVED_SEARCHES</td>
                </tr>
                <tr ng-repeat="savedSearch in savedSearchesVm.savedSearches"
                    style="font-size: 14px;">
                    <td class="name-column3" style="vertical-align: middle;">
                        <a href="javascript:void(0);"
                           style=""
                           ng-click="savedSearchesVm.showSavedSearchResults(savedSearch)">

                            <div style="">
                                <span>
                                    <span ng-show="savedSearch.type == 'PUBLIC'" class="ng-hide">
                                        <i class="las la-share-alt" title="Public Search"></i>
                                    </span>
                                    <span ng-show="savedSearch.type == 'PRIVATE'" class="">
                                        <i class="las la-user-lock" title="Private Search"></i>
                                    </span>
                                    {{savedSearch.name | limitTo: 40}}{{savedSearch.name.length > 37 ? '...' : ''}}
                                </span>
                            </div>
                        </a>
                    </td>
                    <td style="vertical-align: middle;">
                        <object-type-status object="{type: savedSearch.objectType}"></object-type-status>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>