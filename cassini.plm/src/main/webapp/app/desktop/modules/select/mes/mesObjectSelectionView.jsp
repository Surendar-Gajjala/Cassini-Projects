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

    #mesObject-table thead th {
        position: sticky;
        top : 33px;
    }

    .sidePanel-content{
        height: 515px !important;
    }

</style>
<div class="view-container" fitcontent>
    <div class="sidePanel-content no-padding" style="padding: 10px !important;">
        <div>
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 590px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="mesObjectSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="mesObjectSelectVm.freeTextSearch()"
                       ng-init="mesObjectSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="mesObjectSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="mesObjectSelectVm.resetPage()"></i>

                <span style="margin-left: 15px !important;">
                        <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="mesObjectSelectVm.mesObjects.numberOfElements == 0">
                                    {{(mesObjectSelectVm.pageable.page*mesObjectSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="mesObjectSelectVm.mesObjects.numberOfElements > 0">
                                    {{(mesObjectSelectVm.pageable.page*mesObjectSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="mesObjectSelectVm.mesObjects.last ==false">{{((mesObjectSelectVm.pageable.page+1)*mesObjectSelectVm.pageable.size)}}</span>
                                <span ng-if="mesObjectSelectVm.mesObjects.last == true">{{mesObjectSelectVm.mesObjects.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{mesObjectSelectVm.mesObjects.totalElements}}
                            </span>
                           </medium>
                    </span>
                     <span class="mr10"> Page {{mesObjectSelectVm.mesObjects.totalElements != 0 ?
                     mesObjectSelectVm.mesObjects.number+1:0}} <span translate>OF</span> {{mesObjectSelectVm.mesObjects.totalPages}}
                    </span>
                <a href="" ng-click="mesObjectSelectVm.previousPage()"
                   ng-class="{'disabled': mesObjectSelectVm.mesObjects.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="mesObjectSelectVm.nextPage()"
                   ng-class="{'disabled': mesObjectSelectVm.mesObjects.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="mesObject-table" class="table table-striped highlight-row">
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
            <tr ng-if="mesObjectSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_OBJECTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mesObjectSelectVm.loading == false && mesObjectSelectVm.mesObjects.content.length == 0">
                <td colspan="12">
                    <span translate>NO_OBJECTS</span>
                </td>
            </tr>

            <tr ng-repeat="mesObject in mesObjectSelectVm.mesObjects.content"
                ng-click="mesObject.checked = !mesObject.checked; mesObjectSelectVm.radioChange(mesObject, $event)"
                ng-dblclick="mesObject.checked = !mesObject.checked; mesObjectSelectVm.selectRadioChange(mesObject, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="mesObject.checked" name="mesObject"
                           value="mesObject"
                           ng-click="mesObjectSelectVm.radioChange(mesObject, $event)"
                           ng-dblclick="mesObjectSelectVm.selectRadioChange(mesObject, $event)">
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="mesObject.number | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="mesObject.name | highlightText: freeTextQuery"></span>
                </td>

                <td style="width: 200px;">{{mesObject.type.name}}</td>
                <td style="width: 200px;"><span
                        ng-bind-html="mesObject.description | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
