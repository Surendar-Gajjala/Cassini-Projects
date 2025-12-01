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

    #changes-table thead th {
        position: sticky;
        top: 33px;
    }

    .sidePanel-content {
        height: 515px !important;
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
                       ng-model="changeSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="changeSelectVm.freeTextSearch()"
                       ng-init="changeSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="changeSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="changeSelectVm.resetPage()"></i>

                <span style="margin-left: 10px !important;">
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>

                                <span ng-if="changeSelectVm.changes.numberOfElements == 0">
                                    {{(changeSelectVm.pageable.page*changeSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="changeSelectVm.changes.numberOfElements > 0">
                                    {{(changeSelectVm.pageable.page*changeSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="changeSelectVm.changes.last ==false">{{((changeSelectVm.pageable.page+1)*changeSelectVm.pageable.size)}}</span>
                                <span ng-if="changeSelectVm.changes.last == true">{{changeSelectVm.changes.totalElements}}</span>

                                <span translate> OF </span>
                                            {{changeSelectVm.changes.totalElements}}
                            </span>
                        </medium>
                    </span>
                     <span class="mr10"> Page {{changeSelectVm.changes.totalElements != 0 ?
                     changeSelectVm.changes.number+1:0}} <span translate>OF</span> {{changeSelectVm.changes.totalPages}}
                    </span>
                <a href="" ng-click="changeSelectVm.previousPage()"
                   ng-class="{'disabled': changeSelectVm.changes.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="changeSelectVm.nextPage()"
                   ng-class="{'disabled': changeSelectVm.changes.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="changes-table" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT
                </th>
                <th style="width: 150px;" translate>NUMBER</th>
                <th ng-if="changeSelectVm.selectAttributeDef.refSubType == null" style="width: 100px;" translate>
                    Change Type
                </th>
                <th style="width: 150px;" translate>TITLE</th>
                <th style="width: 100px;text-align: center" translate>STATUS</th>
                <th translate>REASON_FOR_CHANGE</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="changeSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_OBJECTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="changeSelectVm.loading == false && changeSelectVm.changes.content.length == 0">
                <td colspan="12" translate>NO_CHANGE_OBJECTS</td>
            </tr>

            <tr ng-repeat="changeEco in changeSelectVm.changes.content"
                ng-click="changeEco.checked = !changeEco.checked; changeSelectVm.radioChange(changeEco, $event)"
                ng-dblclick="changeEco.checked = !changeEco.checked; changeSelectVm.selectRadioChange(changeEco, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="changeEco.checked" name="changeEco" value="changeEco"
                           ng-click="changeSelectVm.radioChange(changeEco, $event)"
                           ng-dblclick="changeSelectVm.selectRadioChange(changeEco, $event)">
                </td>
                <td style="width: 150px;">
                    <span ng-if="changeEco.changeType == 'ECO'"
                          ng-bind-html="changeEco.ecoNumber | highlightText: freeTextQuery"></span>
                    <span ng-if="changeEco.changeType == 'DCO'"
                          ng-bind-html="changeEco.dcoNumber | highlightText: freeTextQuery"></span>
                    <span ng-if="changeEco.changeType == 'ECR' || changeEco.changeType == 'DCR'"
                          ng-bind-html="changeEco.crNumber | highlightText: freeTextQuery"></span>
                    <span ng-if="changeEco.changeType == 'MCO'"
                          ng-bind-html="changeEco.mcoNumber | highlightText: freeTextQuery"></span>
                    <span ng-if="changeEco.changeType == 'DEVIATION' || changeEco.changeType == 'WAIVER'"
                          ng-bind-html="changeEco.varianceNumber | highlightText: freeTextQuery"></span>
                </td>
                <td ng-if="changeSelectVm.selectAttributeDef.refSubType == null"
                    style="width: 200px;text-align: center">
                    <span ng-bind-html="changeEco.changeType | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-bind-html="changeEco.title | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 100px;text-align: center">
                    <workflow-status workflow="changeEco"></workflow-status>
                </td>
                <td>{{changeEco.reasonForChange}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

