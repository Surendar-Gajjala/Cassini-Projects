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

    #project-table thead th {
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
            <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 590px;">
                <i class="fa fa-search"></i>

                <input type="text" id="freeTextSearchInput"
                       autocomplete="off"
                       class="form-control input-sm"
                       ng-model="projectSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="projectSelectVm.freeTextSearch()"
                       ng-init="projectSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="projectSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="projectSelectVm.resetPage()"></i>
                        <span style="margin-left: 0px !important;">
                            <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="projectSelectVm.projects.numberOfElements == 0">
                                    {{(projectSelectVm.pageable.page*projectSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="projectSelectVm.projects.numberOfElements > 0">
                                    {{(projectSelectVm.pageable.page*projectSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="projectSelectVm.projects.last ==false">{{((projectSelectVm.pageable.page+1)*projectSelectVm.pageable.size)}}</span>
                                <span ng-if="projectSelectVm.projects.last == true">{{projectSelectVm.projects.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{projectSelectVm.projects.totalElements}}
                            </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{projectSelectVm.projects.totalElements != 0 ? projectSelectVm.projects.number+1:0}} of {{projectSelectVm.projects.totalPages}}</span>
                <a href="" ng-click="projectSelectVm.previousPage()"
                   ng-class="{'disabled': projectSelectVm.projects.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="projectSelectVm.nextPage()"
                   ng-class="{'disabled': projectSelectVm.projects.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>

        <table id="project-table" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>NAME</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="projectSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_PROJECTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="projectSelectVm.loading == false && projectSelectVm.projects.content.length == 0">
                <td colspan="12">
                    <span translate>NO_OF_PROJECTS</span>
                </td>
            </tr>

            <tr ng-repeat="project in projectSelectVm.projects.content"
                ng-click="project.checked = !project.checked; projectSelectVm.radioChange(project, $event)"
                ng-dblclick="project.checked = !project.checked; projectSelectVm.selectRadioChange(project, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="project.checked" name="project"
                           value="project"
                           ng-click="projectSelectVm.radioChange(project, $event)"
                           ng-dblclick="projectSelectVm.selectRadioChange(project, $event)">
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="project.name | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="project.description | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
