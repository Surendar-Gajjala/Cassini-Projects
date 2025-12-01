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

    #workflows-table thead th {
        position: sticky;
        top: 33px;
    }

    .sidePanel-content {
        height: 515px !important
    }

</style>
<div>
    <div class="view-container" fitcontent>
        <div class="sidePanel-content no-padding" style=";padding: 10px !important;">
            <div>
                <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 613px;">
                    <i class="fa fa-search"></i>

                    <input type="text" id="freeTextSearchInput"
                           autocomplete="off"
                           class="form-control input-sm"
                           ng-model="wrkSelectVm.searchTerm"
                           ng-model-options="{ debounce: 500 }"
                           ng-change="wrkSelectVm.freeTextSearch()"
                           ng-init="wrkSelectVm.resetPage()"
                           ng-enter="onSearch(searchTerm)">
                    <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                       ng-show="wrkSelectVm.searchTerm.length > 0 || filterSearch == true"
                       ng-click="wrkSelectVm.resetPage()"></i>

                <span style="margin-left: 45px !important;">
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="wrkSelectVm.workflows.numberOfElements == 0">
                                    {{(wrkSelectVm.pageable.page*wrkSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="wrkSelectVm.workflows.numberOfElements > 0">
                                    {{(wrkSelectVm.pageable.page*wrkSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="wrkSelectVm.workflows.last ==false">{{((wrkSelectVm.pageable.page+1)*wrkSelectVm.pageable.size)}}</span>
                                <span ng-if="wrkSelectVm.workflows.last == true">{{wrkSelectVm.workflows.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{wrkSelectVm.workflows.totalElements}}
                            </span>
                    </medium>
                    </span>
                     <span class="mr10"> Page {{wrkSelectVm.workflows.totalElements != 0 ?
                     wrkSelectVm.workflows.number+1:0}} <span translate>OF</span> {{wrkSelectVm.workflows.totalPages}}
                    </span>
                    <a href="" ng-click="wrkSelectVm.previousPage()"
                       ng-class="{'disabled': wrkSelectVm.workflows.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="wrkSelectVm.nextPage()"
                       ng-class="{'disabled': wrkSelectVm.workflows.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>

            </div>
            <table id="workflows-table" class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 50px;" translate>SELECT</th>
                    <th style="width: 200px;" translate>Number</th>
                    <th style="width: 202px;" translate>WORKFLOW_NAME</th>
                    <th translate>TYPE</th>
                </tr>
                </thead>
                <br><br>
                <tbody>
                <tr ng-if="wrkSelectVm.loading == true">
                    <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_WORKFLOW</span>
                    </span>
                    </td>
                </tr>
                <tr ng-if="wrkSelectVm.loading == false && wrkSelectVm.workflows.content.length == 0">
                    <td colspan="12"><span translate>MO_WORKFLOW</span></td>
                </tr>

                <tr ng-repeat="workflow in wrkSelectVm.workflows.content" class="highlight-row">
                    <td style="width: 40px;">
                        <input type="radio" ng-checked="workflow.checked" name="workflow" value="workflow"
                               ng-click="wrkSelectVm.radioChange(workflow)">
                    </td>
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <span ng-bind-html="workflow.master.number | highlightText: freeTextQuery"></span></td>
                    <td class="col-width-200">
                        <span ng-bind-html="workflow.name | highlightText: freeTextQuery"></span></td>
                    <td class="col-width-150">{{workflow.workflowType.name}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>


