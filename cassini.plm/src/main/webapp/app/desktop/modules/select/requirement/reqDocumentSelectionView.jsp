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

    #requirements-table thead th {
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
                       ng-model="reqDocumentSelectVm.searchTerm"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="reqDocumentSelectVm.freeTextSearch()"
                       ng-init="reqDocumentSelectVm.resetPage()"
                       ng-enter="onSearch(searchTerm)">
                <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                   ng-show="reqDocumentSelectVm.searchTerm.length > 0 || filterSearch == true"
                   ng-click="reqDocumentSelectVm.resetPage()"></i>

                <span style="margin-left: 0px !important;">
                    <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="reqDocumentSelectVm.reqDocuments.numberOfElements == 0">
                                    {{(reqDocumentSelectVm.pageable.page*reqDocumentSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="reqDocumentSelectVm.reqDocuments.numberOfElements > 0">
                                    {{(reqDocumentSelectVm.pageable.page*reqDocumentSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="reqDocumentSelectVm.reqDocuments.last ==false">{{((reqDocumentSelectVm.pageable.page+1)*reqDocumentSelectVm.pageable.size)}}</span>
                                <span ng-if="reqDocumentSelectVm.reqDocuments.last == true">{{reqDocumentSelectVm.reqDocuments.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{reqDocumentSelectVm.reqDocuments.totalElements}}
                            </span>
                    </medium>
                    </span>
                     <span class="mr10"> Page {{reqDocumentSelectVm.reqDocuments.totalElements != 0 ?
                     reqDocumentSelectVm.reqDocuments.number+1:0}} <span translate>OF</span> {{reqDocumentSelectVm.reqDocuments.totalPages}}
                    </span>
                <a href="" ng-click="reqDocumentSelectVm.previousPage()"
                   ng-class="{'disabled': reqDocumentSelectVm.reqDocuments.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="reqDocumentSelectVm.nextPage()"
                   ng-class="{'disabled': reqDocumentSelectVm.reqDocuments.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>

        </div>

        <table id="requirements-table" class="table table-striped highlight-row">
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
            <tr ng-if="reqDocumentSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_OBJECTS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="reqDocumentSelectVm.loading == false && reqDocumentSelectVm.reqDocuments.content.length == 0">
                <td colspan="12">
                    <span translate>NO_OBJECTS</span>
                </td>
            </tr>

            <tr ng-repeat="reqDocument in reqDocumentSelectVm.reqDocuments.content"
                ng-click="reqDocument.checked = !reqDocument.checked; reqDocumentSelectVm.radioChange(reqDocument, $event)"
                ng-dblclick="reqDocument.checked = !reqDocument.checked; reqDocumentSelectVm.selectRadioChange(reqDocument, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="reqDocument.checked" name="reqDocument"
                           value="reqDocument"
                           ng-click="reqDocumentSelectVm.radioChange(reqDocument, $event)"
                           ng-dblclick="reqDocumentSelectVm.selectRadioChange(reqDocument, $event)">
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="reqDocument.number | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="reqDocument.name | highlightText: freeTextQuery"></span>
                </td>

                <td style="width: 200px;">{{reqDocument.type.name}}</td>
                <td style="width: 200px;"><span
                        ng-bind-html="reqDocument.description | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
