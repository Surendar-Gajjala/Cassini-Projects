<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>SAVED_SEARCHES</span>
        </div>

        <div class="pull-right text-center" style="padding: 2px;">
            <span ng-if="savedSearchWidgetVm.loading == false"><small>
                {{savedSearchWidgetVm.savedSearches.totalElements}}
                <span translate>SAVED_SEARCH_TITLE</span>
            </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="savedSearchWidgetVm.previousPage()"
                        ng-disabled="savedSearchWidgetVm.savedSearches.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="savedSearchWidgetVm.nextPage()"
                        ng-disabled="savedSearchWidgetVm.savedSearches.last">
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
                        <th class="col-width-200" translate>NAME</th>
                        <th class="col-width-200" translate>DESCRIPTION</th>
                        <th translate>SEARCH_TYPE</th>
                        <th translate>OBJECT_TYPE</th>
                        <th translate>SAVED_SEARCH_OWNER</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="savedSearchWidgetVm.loading == true">
                        <td colspan="7">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_SAVED_SEARCHES</span>
                           </span>
                        </td>
                    </tr>

                    <tr ng-if="savedSearchWidgetVm.loading == false && savedSearchWidgetVm.savedSearches.content.length == 0">
                        <td colspan="7" translate>NO_SAVED_SEARCHES</td>
                    </tr>
                    <tr ng-repeat="savedSearch in savedSearchWidgetVm.savedSearches.content" style="font-size: 14px;">
                        <td class="col-width-200" style="vertical-align: middle;">
                            <a href=""
                               ng-click="savedSearchWidgetVm.showSavedResult(savedSearch)">
                            <i class="las la-share-alt"
                                  title="{{savedSearchWidgetVm.public}}" ng-if="savedSearch.ownerType == 'PUBLIC'">
                             </i>
                           <i class="las la-user-lock"
                                  title="{{savedSearchWidgetVm.private}}" ng-if="savedSearch.ownerType == 'PRIVATE'">
                            </i>
                                {{savedSearch.name}}
                            </a>
                        </td>
                        <td class="col-width-200" title="{{savedSearch.description}}"><span
                                ng-bind-html="savedSearch.description "></span>
                            {{savedSearch.description.length > 20 ? '...' : ''}}
                        </td>
                        <td style="vertical-align: middle;">{{savedSearch.searchType}}</td>
                        <td style="vertical-align: middle;">
                            <object-type-status object="savedSearch"></object-type-status>
                        </td>
                        <td style="vertical-align: middle;">{{savedSearch.ownerObject.firstName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>