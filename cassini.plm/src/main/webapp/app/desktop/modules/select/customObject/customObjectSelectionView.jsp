<style>
    .search-box > input {
        width: 250px;
        border-radius: 3px !important;
        padding-left: 30px !important;
        height: 30px;
        display: inline-block !important;
        background-color: rgb(241, 243, 244);
        border: 1px solid #ddd;
        box-shadow: none;
    }

    .search-box > input:hover {
        background-color: rgb(229, 231, 234);
    }

    .search-box > input:focus {
        box-shadow: none;
    }

    .search-box i.fa-search {
        margin-right: -25px !important;
        z-index: 4 !important;
        margin-top: 11px;
        margin-left: 10px;
        color: grey;
        opacity: 0.5;
    }

    .search-box i.clear-search {
        margin-left: -25px;
        cursor: pointer;
        z-index: 4 !important;
        margin-top: 11px;
        font-size: 14px;
        opacity: 0.6;
    }

    .search-box > input {
        width: 250px;
        border-radius: 3px !important;
        padding-left: 30px !important;
        height: 30px;
        display: inline-block !important;
        background-color: rgb(241, 243, 244);
        border: 1px solid #ddd;
        box-shadow: none;
    }

    .search-box > input:hover {
        background-color: rgb(229, 231, 234);
    }

    .search-box > input:focus {
        box-shadow: none;
    }

    .search-box i.fa-search {
        margin-right: -25px !important;
        z-index: 4 !important;
        margin-top: 11px;
        margin-left: 10px;
        color: grey;
        opacity: 0.5;
    }

    .search-box i.clear-search {
        margin-left: -25px;
        cursor: pointer;
        z-index: 4 !important;
        margin-top: 11px;
        font-size: 14px;
        opacity: 0.6;
    }

    #customObject-table thead th {
        position: sticky;
        top: 33px;
    }

    .sidePanel-content {
        height: 515px !important
    }

</style>
<div class="view-container" fitcontent>
    <div class="sidePanel-content no-padding" style="padding: 10px !important;">
        <div>
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 570px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="customObjectSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="customObjectSelectVm.freeTextSearch()"
                       ng-init="customObjectSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="customObjectSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="customObjectSelectVm.resetPage()"></i>

                <span style="margin-left: 20px !important;">
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="customObjectSelectVm.customObjects.numberOfElements == 0">
                                    {{(customObjectSelectVm.pageable.page*customObjectSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="customObjectSelectVm.customObjects.numberOfElements > 0">
                                    {{(customObjectSelectVm.pageable.page*customObjectSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="customObjectSelectVm.customObjects.last ==false">{{((customObjectSelectVm.pageable.page+1)*customObjectSelectVm.pageable.size)}}</span>
                                <span ng-if="customObjectSelectVm.customObjects.last == true">{{customObjectSelectVm.customObjects.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{customObjectSelectVm.customObjects.totalElements}}
                            </span>
                       </medium>
                    </span>
                     <span class="mr10"> Page {{customObjectSelectVm.customObjects.totalElements != 0 ?
                     customObjectSelectVm.customObjects.number+1:0}} <span translate>OF</span> {{customObjectSelectVm.customObjects.totalPages}}
                    </span>
                <a href="" ng-click="customObjectSelectVm.previousPage()"
                   ng-class="{'disabled': customObjectSelectVm.customObjects.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="customObjectSelectVm.nextPage()"
                   ng-class="{'disabled': customObjectSelectVm.customObjects.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="customObject-table" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>NUMBER</th>
                <th style="width: 200px;" translate>NAME</th>
                <th style="width: 200px;" translate>TYPE</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="customObjectSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_OBJECTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="customObjectSelectVm.loading == false && customObjectSelectVm.customObjects.content.length == 0">
                <td colspan="12">
                    <span translate>NO_OBJECTS</span>
                </td>
            </tr>

            <tr ng-repeat="customObject in customObjectSelectVm.customObjects.content"
                ng-click="customObject.checked = !customObject.checked; customObjectSelectVm.radioChange(customObject, $event)"
                ng-dblclick="customObject.checked = !customObject.checked; customObjectSelectVm.selectRadioChange(customObject, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="customObject.checked" name="customObject"
                           value="customObject"
                           ng-click="customObjectSelectVm.radioChange(customObject, $event)"
                           ng-dblclick="customObjectSelectVm.selectRadioChange(customObject, $event)">
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="customObject.number | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="customObject.name | highlightText: freeTextQuery"></span>
                </td>

                <td style="width: 200px;">{{customObject.type.name}}</td>
                <td style="width: 200px;"><span
                        ng-bind-html="customObject.description | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
