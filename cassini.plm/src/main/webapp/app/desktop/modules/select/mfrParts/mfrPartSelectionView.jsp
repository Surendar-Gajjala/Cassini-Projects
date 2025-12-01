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

    #mfrParts-table thead th {
        position: sticky;
        top : 33px;
    }

    .sidePanel-content{
        height: 515px !important
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
                       ng-model="mfrPartSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="mfrPartSelectVm.freeTextSearch()"
                       ng-init="mfrPartSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="mfrPartSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="mfrPartSelectVm.resetPage()"></i>

                <span style="margin-left: 0px !important;">
                        <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="mfrPartSelectVm.manufacturersParts.numberOfElements == 0">
                                    {{(mfrPartSelectVm.pageable.page*mfrPartSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="mfrPartSelectVm.manufacturersParts.numberOfElements > 0">
                                    {{(mfrPartSelectVm.pageable.page*mfrPartSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="mfrPartSelectVm.manufacturersParts.last ==false">{{((mfrPartSelectVm.pageable.page+1)*mfrPartSelectVm.pageable.size)}}</span>
                                <span ng-if="mfrPartSelectVm.manufacturersParts.last == true">{{mfrPartSelectVm.manufacturersParts.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{mfrPartSelectVm.manufacturersParts.totalElements}}
                            </span>
                     </medium>
                    </span>
                     <span class="mr10"> Page {{mfrPartSelectVm.manufacturersParts.totalElements != 0 ?
                     mfrPartSelectVm.manufacturersParts.number+1:0}} <span translate>OF</span> {{mfrPartSelectVm.manufacturersParts.totalPages}}
                    </span>
                <a href="" ng-click="mfrPartSelectVm.previousPage()"
                   ng-class="{'disabled': mfrPartSelectVm.manufacturersParts.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="mfrPartSelectVm.nextPage()"
                   ng-class="{'disabled': mfrPartSelectVm.manufacturersParts.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="mfrParts-table" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 202px;" translate>PART_NUMBER</th>
                <th style="width: 202px;" translate>PART_NAME</th>
                <th style="width: 200px;" translate>TYPE</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="mfrPartSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_MFR_PARTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mfrPartSelectVm.loading == false && mfrPartSelectVm.manufacturersParts.content.length == 0">
                <td colspan="12">
                    <span translate>NO_MFR_PARTS</span>
                </td>
            </tr>

            <tr ng-repeat="manufacturerPart in mfrPartSelectVm.manufacturersParts.content"
                ng-click="manufacturerPart.checked = !manufacturerPart.checked; mfrPartSelectVm.radioChange(manufacturerPart, $event)"
                ng-dblclick="manufacturerPart.checked = !manufacturerPart.checked; mfrPartSelectVm.selectRadioChange(manufacturerPart, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="manufacturerPart.checked" name="manufacturerPart"
                           value="manufacturerPart"
                           ng-click="mfrPartSelectVm.radioChange(manufacturerPart, $event)"
                           ng-dblclick="mfrPartSelectVm.selectRadioChange(manufacturerPart, $event)">
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="manufacturerPart.partNumber | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="manufacturerPart.partName | highlightText: freeTextQuery"></span>
                </td>

                <td style="width: 200px;">{{manufacturerPart.mfrPartType.name}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
