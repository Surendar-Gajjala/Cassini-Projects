<div style="position: relative;">
    <style scoped>
        #desc {
            word-wrap: break-word;
            width: 50%;
            text-align: left;
        }

        #searchName {
            word-wrap: break-word;
            width: 25%;
            white-space: normal;
            text-align: left;
        }

        .modal {
            z-index: 9999 !important;
        }

        .app-side-panel {
            z-index: 9998 !important;
        }
    </style>
    <div class="responsive-table" style="padding: 20px;overflow-y: auto;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 5%"></th>
                <th style="width: 25%;" translate> NAME</th>
                <th style="width: 35%;" translate> DESCRIPTION</th>
                <th style="width: 15%;" translate> SEARCH_TYPE</th>
                <th style="width: 15%;" translate> SAVED_SEARCH_OWNER</th>
                <th style="width: 5%;" translate> ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="searchVm.loading == true">
                <td colspan="10">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5"><span translate>LOADING_SAVED_SEARCHES</span>
                        </span>
                </td>
            </tr>
            <tr ng-if="searchVm.loading == false && searchVm.savedSearches.length == 0">
                <td colspan="25"><span translate>NO_SAVED_SEARCHES</span></td>
            </tr>
            <tr ng-if="searchVm.loading == false && searchVm.savedSearches.length != 0" style="cursor: pointer;"
                title="{{showSavedSearchResultsTitle}}"
                ng-repeat="search in searchVm.savedSearches">
                <td style="width: 5%">
                    <span ng-show="search.type == 'PUBLIC'">
                        <i class="las la-share-alt" title="{{publicSearch}}"></i>
                    </span>
                    <span ng-show="search.type == 'PRIVATE'">
                        <i class="las la-user-lock" title="{{privateSearch}}"></i>
                    </span>
                </td>
                <td id="searchName">
                    <a href="" ng-click="searchVm.showSearch(search)">
                        <span ng-bind-html="search.name"></span>
                    </a>
                </td>
                <td id="desc" ng-click="searchVm.showSearch(search)" title="{{search.description}}"><span
                        ng-bind-html="search.description "></span>
                    {{search.description.length > 20 ? '...' : ''}}
                </td>
                <td ng-click="searchVm.showSearch(search)">
                    {{search.searchType}}
                </td>
                <td ng-click="searchVm.showSearch(search)">
                    {{search.ownerObject.fullName}}
                </td>
                <td style="text-align: center">
                    <i title={{deleteSavedSearchs}} class="la la-trash"
                       ng-hide="search.ownerObject.id != loginPersonDetails.person.id"
                       ng-click="searchVm.deleteSavedSearch(search)"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <br>
    <br>
</div>

