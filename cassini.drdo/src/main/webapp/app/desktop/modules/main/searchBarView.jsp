<div>
    <style scoped>
        .search-element {
            position: absolute;
            top: 5px;
            left: 0;
            right: 0;
            margin: auto;
            max-width: 530px;
            width: 50%;
        }

        .search-input-container {
            height: 40px;
            white-space: nowrap;
        }

        .search-filters {
            position: absolute;
            top: 8px;
            right: 15px;
            cursor: pointer;
        }

        .search-filters i {
            font-weight: bold;
            text-align: right;
        }

        .search-form {
            border-radius: 30px;
            border-width: 0;
            padding-left: 15px;
            display: inline-block !important;
        }

        .search-results-container {
            display: none;
            position: absolute;
            min-height: 50px;
            height: 440px;
            top: 50px;
            left: 0;
            right: 0;
            margin: auto;
            border: 1px solid #ddd;
            background-color: #fff;
            z-index: 100;
            border-top: 0;
        }

        .search-results-container .label {
            color: #fff !important;
        }

        .result-item {
            display: flex;
            flex-direction: column;
            border-bottom: 1px solid #ddd;
            padding: 5px;
            cursor: pointer;
        }

        .result-item:nth-child(odd) {
            background-color: rgb(242, 247, 249);
        }

        .result-item:first-child {
            border-top: 1px solid #ddd;
        }

        .result-item:last-child {
            border-bottom: 0;
        }

        .result-item-row {
            display: flex;
            flex: 1;
        }

        .result-item-col {
            flex: 1;
        }

        .result-item-col span {
            font-size: 10px;
            color: grey;
        }

        .result-item:hover {
            color: #fff !important;
            background: #3ca464; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #11998e, #3ca464); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #11998e, #3ca464);
        }

        .result-item:hover .result-item-col span {
            color: #fff;
        }

        .search-results-container .tab-content {
            border: 0;
            padding: 0;
            max-height: 346px;
            overflow-y: auto;
            border-radius: 0;
        }

        .search-results-container .nav-tabs {
            border-left: 0;
            border-right: 0;
            padding-left: 5px;
            border-top: 0;
            border-radius: 0;

            background: #00B4DB; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #0083B0, #00B4DB); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #0083B0, #00B4DB); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .search-results-container .nav-tabs .nav-item a,
        .search-results-container .nav-tabs .nav-item a:link,
        .search-results-container .nav-tabs .nav-item a:visited,
        .search-results-container .nav-tabs .nav-item a:hover,
        .search-results-container .nav-tabs .nav-item a:active {
            background-color: inherit !important;
            color: #fff !important;
        }

        .search-results-container .tab-pane {
            padding-top: 5px;
        }

        .search-results-container .nav-tabs > .uib-tab.active > a,
        .search-results-container .nav-tabs > .uib-tab.active > a:focus,
        .search-results-container .nav-tabs > .uib-tab.active > a:hover {
            border-bottom: 4px solid #f7b733 !important;
        }

        .search-results-container .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 33px;
            top: 0;
            overflow: auto;
        }

        .search-results-container .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
            background-color: #fff;
        }

        .search-results-container .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px;
            height: 25px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
            /* background-color: blue;*/
        }

        .search-results-container .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .search-results-container .table-footer > div h5 {
            margin: 0;
        }

        .search-results-container .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .search-results-container .table-footer > div > div > i {
            font-size: 16px;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }
    </style>

    <div class="search-element search-input-container">
        <input type="search"
               class="form-control input-sm search-form"
               placeholder="Search"
               onfocus="this.setSelectionRange(0, this.value.length)"
               ng-click="searchVm.preventClick($event)"
               ng-model="searchVm.searchText"
               ng-model-options="{ debounce: 500 }"
               ng-change="searchVm.performSearch()"
               ng-enter="searchVm.performSearch()">
    </div>

    <div id="search-results-container" class="search-results-container">
        <div class="responsive-table">
            <table class="table table-striped" style="table-layout: auto !important;">
                <thead>
                <tr>
                    <th class="thirty-column">Nomenclature</th>
                    <th>Section</th>
                    <th ng-if="hasPermission('permission.inward.view')">Gate Pass</th>
                    <th>UPN</th>
                    <th>Serial Number</th>
                    <th>Status</th>
                    <th>Certificate</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="searchVm.searchResults.content.length == 0">
                    <td colspan="15" style="text-align: left;">No items found</td>
                </tr>
                <tr ng-repeat="result in searchVm.searchResults.content  | orderBy:'item.itemMaster.itemName'">
                    <td>
                        <span ng-bind-html="result.item.itemMaster.itemName | highlightText: searchVm.searchText"></span>
                    </td>
                    <td>
                        <span ng-if="result.section != null" ng-bind-html="result.sectionName | highlightText: searchVm.searchText"></span>
                        <span ng-if="result.section == null">Common Section</span>
                    </td>
                    <td ng-if="hasPermission('permission.inward.view')">
                        <a href="" ng-click="searchVm.downloadGatePass(result.inward.gatePass)"
                           title="Click to download Gate Pass">
                            <span ng-bind-html="result.inward.gatePass.gatePass.name | highlightText: searchVm.searchText"></span>
                        </a>
                    </td>
                    <td>
                        <a href="" title="Click to history"
                           ng-if="!result.item.itemMaster.itemType.hasLots"
                           ng-click="showUpnHistory(result,'right')">
                            <span ng-bind-html="result.upnNumber | highlightText: searchVm.searchText"></span>
                        </a>
                        <a href="" title="Click to history"
                           ng-if="result.item.itemMaster.itemType.hasLots"
                           ng-click="showLotUpnHistory(result,'INWARD')">
                            <span ng-bind-html="result.upnNumber | highlightText: searchVm.searchText"></span>
                        </a>

                    </td>
                    <td>
                        <%--<span ng-bind-html="result.manufacturer.mfrCode | highlightText: searchVm.searchText"></span> ---%>
                        <span ng-bind-html="result.oemNumber | highlightText: searchVm.searchText"></span>
                    </td>
                    <td>
                        <item-instance-status object="result"></item-instance-status>
                    </td>
                    <td>
                        <div ng-repeat="certificate in result.certificates">
                            <a href="" ng-click="searchVm.openPropertyAttachment(certificate)">
                                <span ng-bind-html="certificate.name | highlightText: searchVm.searchText"></span>
                            </a>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div style="width: 100%;">
                <div style="width: 50%">
                    <%--<h5>Displaying {{searchVm.searchResults.numberOfElements}} of
                        {{searchVm.searchResults.totalElements}}</h5>--%>
                    <h5>
                        <span style="padding-right: 5px">Displaying</span>
                        <span ng-if="searchVm.searchResults.totalElements == 0">
                            {{(searchVm.pageable.page*searchVm.pageable.size)}}
                        </span>
                        <span ng-if="searchVm.searchResults.totalElements > 0">
                            {{(searchVm.pageable.page*searchVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="searchVm.searchResults.last == false">{{((searchVm.pageable.page+1)*searchVm.pageable.size)}}</span>
                        <span ng-if="searchVm.searchResults.last == true">{{searchVm.searchResults.totalElements}}</span>


                        <span> of </span>{{searchVm.searchResults.totalElements}}
                    </h5>
                </div>

                <div class="text-right" style="width: 50%">
                    <span class="mr10">Page {{searchVm.searchResults.totalElements != 0 ? searchVm.searchResults.number+1:0}} of {{searchVm.searchResults.totalPages}}</span>
                    <a href="" ng-click="searchVm.previousPage()"
                       ng-class="{'disabled': searchVm.searchResults.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="searchVm.nextPage()"
                       ng-class="{'disabled': searchVm.searchResults.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>