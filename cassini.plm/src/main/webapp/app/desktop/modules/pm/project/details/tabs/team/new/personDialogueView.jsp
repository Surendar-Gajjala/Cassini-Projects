<style scoped>
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
        position: sticky !important;
        top: 33px !important;
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
                       ng-model="personDialogueVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="personDialogueVm.freeTextSearch()"
                       ng-init="personDialogueVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="personDialogueVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="personDialogueVm.resetPage()"></i>

                <span style="margin-left: 8px !important;">
                        <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>

                                <span ng-if="personDialogueVm.persons.numberOfElements == 0">
                                    {{(personDialogueVm.pageable.page*personDialogueVm.pageable.size)}}
                                </span>
                                <span ng-if="personDialogueVm.persons.numberOfElements > 0">
                                    {{(personDialogueVm.pageable.page*personDialogueVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="personDialogueVm.persons.last ==false">{{((personDialogueVm.pageable.page+1)*personDialogueVm.pageable.size)}}</span>
                                <span ng-if="personDialogueVm.persons.last == true">{{personDialogueVm.persons.totalElements}}</span>

                                <span translate> OF </span>
                                            {{personDialogueVm.persons.totalElements}}
                            </span>
                        </medium>
                    </span>
                     <span class="mr10"> Page {{personDialogueVm.persons.totalElements != 0 ?
                     personDialogueVm.persons.number+1:0}} <span translate>OF</span> {{personDialogueVm.persons.totalPages}}
                    </span>
                <a href="" ng-click="personDialogueVm.previousPage()"
                   ng-class="{'disabled': personDialogueVm.persons.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="personDialogueVm.nextPage()"
                   ng-class="{'disabled': personDialogueVm.persons.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <br/>
        <table id="persons" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 80px; text-align: left">
                    <input ng-if="personDialogueVm.persons.content.length != 0" name="item" type="checkbox"
                           ng-model="personDialogueVm.selectedAll"
                           ng-click="personDialogueVm.checkAll()" ng-checked="check">
                </th>
                <th style="vertical-align: middle;">
                    Name
                </th>
                <th style="vertical-align: middle;">
                    Phone
                </th>
                <th style="vertical-align: middle;">
                    Email
                </th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="personVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">Loading persons...
                    </span>
                </td>
            </tr>
            <tr ng-if="personDialogueVm.persons.content.length == 0">
                <td colspan="12" translate>NO_PERSONS</td>
            </tr>

            <tr ng-repeat="person in personDialogueVm.persons.content">
                <td style="width: 80px; text-align: left">
                    <input type="checkbox" name="person" value="person"
                           ng-click="personDialogueVm.select(person.person)"
                           ng-model="person.person.selected">
                </td>
                <td style="width: 200px;">
                    <span ng-bind-html="person.person.fullName | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-bind-html="person.person.phoneMobile | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;">
                    <span ng-bind-html="person.person.email | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>









