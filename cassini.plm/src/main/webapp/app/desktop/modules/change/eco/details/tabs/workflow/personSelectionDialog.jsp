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

    #persons thead th {
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
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 590px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="personSelectionVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="personSelectionVm.freeTextSearch()"
                       ng-init="personSelectionVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="personSelectionVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="personSelectionVm.resetPage()"></i>
                
                  <span style="margin-left: 8px !important;">
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="personSelectionVm.persons.numberOfElements == 0">
                                    {{(personSelectionVm.pageable.page*personSelectionVm.pageable.size)}}
                                </span>
                                <span ng-if="personSelectionVm.persons.numberOfElements > 0">
                                    {{(personSelectionVm.pageable.page*personSelectionVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="personSelectionVm.persons.last ==false">{{((personSelectionVm.pageable.page+1)*personSelectionVm.pageable.size)}}</span>
                                <span ng-if="personSelectionVm.persons.last == true">{{personSelectionVm.persons.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{personSelectionVm.persons.totalElements}}
                            </span>
                        </medium>
                    </span>
                     <span class="mr10"> Page {{personSelectionVm.persons.totalElements != 0 ?
                     personSelectionVm.persons.number+1:0}} <span translate>OF</span> {{personSelectionVm.persons.totalPages}}
                    </span>
                <a href="" ng-click="personSelectionVm.previousPage()"
                   ng-class="{'disabled': personSelectionVm.persons.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="personSelectionVm.nextPage()"
                   ng-class="{'disabled': personSelectionVm.persons.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="persons" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 25px; text-align: center">
                    <input ng-if="personSelectionVm.persons.content.length > 1" name="item" type="checkbox"
                           ng-model="personSelectionVm.selectAllCheck"
                           ng-click="personSelectionVm.selectAll(check);" ng-checked="check">
                </th>
                <th style="width: 200px;" translate>NAME</th>
                <th style="width: 150px;" translate>PHONE_NUMBER</th>
                <th style="width: 200px;" translate>EMAIL</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="personSelectionVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">Loading persons...
                    </span>
                </td>
            </tr>
            <tr ng-if="personSelectionVm.loading == false && personSelectionVm.persons.content.length == 0">
                <td colspan="12" translate>NO_PERSONS</td>
            </tr>
            <tr ng-repeat="person in personSelectionVm.persons.content track by $index">
                <td style="width: 25px; text-align: center">
                    <input type="checkbox" name="person" value="person" ng-model="person.selected"
                           ng-click="personSelectionVm.selectCheck(person)">
                </td>
                <td style=" width: 200px;">
                    <span style="color: #ff9407" ng-if="person.external"
                          title="External user" ng-bind-html="person.fullName | highlightText: freeTextQuery"></span>
                    <span ng-if="!person.external" ng-bind-html="person.fullName | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-bind-html="person.phoneMobile | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;">
                    <span ng-bind-html="person.email | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
