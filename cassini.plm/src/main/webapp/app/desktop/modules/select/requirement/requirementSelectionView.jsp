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

    #reqs-table thead th {
        position: sticky;
        top : 33px;
    }

    .sidePanel-content{
        height: 515px !important
    }

</style>
<div class="view-container" fitcontent>
    <div class="sidePanel-content no-padding" style=";padding: 10px !important;">
        <div>
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 590px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="requirementSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="requirementSelectVm.freeTextSearch()"
                       ng-init="requirementSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="requirementSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="requirementSelectVm.resetPage()"></i>

                <span style="margin-left: 0px !important;">
                        <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="requirementSelectVm.requirements.numberOfElements == 0">
                                    {{(requirementSelectVm.pageable.page*requirementSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="requirementSelectVm.requirements.numberOfElements > 0">
                                    {{(requirementSelectVm.pageable.page*requirementSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="requirementSelectVm.requirements.last ==false">{{((requirementSelectVm.pageable.page+1)*requirementSelectVm.pageable.size)}}</span>
                                <span ng-if="requirementSelectVm.requirements.last == true">{{requirementSelectVm.requirements.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{requirementSelectVm.requirements.totalElements}}
                            </span>
                       </medium>
                    </span>
                     <span class="mr10"> Page {{requirementSelectVm.requirements.totalElements != 0 ?
                     requirementSelectVm.requirements.number+1:0}} <span translate>OF</span> {{requirementSelectVm.requirements.totalPages}}
                    </span>
                <a href="" ng-click="requirementSelectVm.previousPage()"
                   ng-class="{'disabled': requirementSelectVm.requirements.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="requirementSelectVm.nextPage()"
                   ng-class="{'disabled': requirementSelectVm.requirements.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="reqs-table" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>NUMBER</th>
                <th style="width: 200px;" translate>TYPE</th>
                <th style="width: 200px;" translate>NAME</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="requirementSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_ITEMS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="requirementSelectVm.loading == false && requirementSelectVm.requirements.content.length == 0">
                <td colspan="12"><span translate>NO_ITEMS</span></td>
            </tr>

            <tr ng-repeat="requirement in requirementSelectVm.requirements.content" class="highlight-row">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="requirement.checked" name="requirement" value="requirement"
                           ng-click="requirementSelectVm.radioChange(requirement)">
                </td>
                <td><span
                        ng-bind-html="requirement.number | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;">{{requirement.type.name}}</td>
                <td><span
                        ng-bind-html="requirement.name | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>





