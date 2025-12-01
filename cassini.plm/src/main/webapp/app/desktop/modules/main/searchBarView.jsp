<div>
    <style scoped>
        .search-element {
            position: absolute;
            top: 5px;
            left: 0;
            right: 0;
            margin: auto;
            max-width: 400px;
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

        .dark-mode .search-filters .dropdown-toggle {
            color: #fff;
        }

        .search-filters .dropdown-toggle {
            opacity: 0.5;
        }

        .search-filters .dropdown-toggle:hover {
            opacity: 1;
        }

        .search-filters i {
            font-weight: bold;
            text-align: right;
        }

        .search-form {
            border-radius: 3px;
            border-width: 0;
            padding-left: 15px;
            display: inline-block !important;
            height: 34px;
        }

        .search-results-container {
            min-height: 50px;
            height: 450px;
            top: 50px;
            /*border: 1px solid #ddd;*/
            background-color: var(--cassini-bg-color);
            z-index: 9998;
            display: none;
            border-top: 0;
            width: 60%;
            position: absolute;
            box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.16), 0px 3px 6px rgba(0, 0, 0, 0.22);
            left: 50%;
            transform: translate(-50%);
        }

        /* Small devices (landscape phones, 576px and up) */
        @media (min-width: 576px) {
            .search-results-container {
                width: 80%;
            }
        }

        /* Medium devices (tablets, 768px and up) */
        @media (min-width: 768px) {
            .search-results-container {
                width: 80%;
            }
        }

        /* Large devices (desktops, 992px and up) */
        @media (min-width: 992px) {
            .search-results-container {
                width: 80%;
            }
        }

        /* Extra large devices (large desktops, 1200px and up) */
        @media (min-width: 1200px) {
            .search-results-container {
                width: 60%;
            }
        }

        .search-results-container .label {
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
            padding-left: 4px;
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

        .search-results-container .results-table {
            overflow: auto !important;
        }

        .search-results-container table td {
            white-space: nowrap;
        }

        .search-results-container .table-footer {
            padding: 0 10px 0 10px;
            height: 36px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .search-results-container .table-footer > div {
            line-height: 15px;
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

        .search-tabs {
            width: 100%;
            display: inline-flex;
            height: 100%
        }

        .left-tab {
            width: 250px;
            height: 100%;
            overflow-y: auto;
            cursor: pointer;
            border-right: 1px solid #D3D7DB;
        }

        .right-tab {
            width: calc(100% - 250px);
            display: flex;
            flex-direction: column;
        }

        .right-tab div:first-child {
            flex: 2;
        }

        .left-element {
        }

        .left-element .label-default {
            color: inherit;
        }

        .left-element:hover {
            background-color: #EFEFEF;
        }

        .left-element .active-tab {
            background-color: #ddd !important;
        }

        .left-element > div > .label {
            font-size: 12px !important;
            padding: 5px !important;
            float: right;
            margin-right: 5px;
            width: 30px;
            background-color: rgb(179, 191, 206) !important;
        }

        .left-element .active-tab .label {
        }

        .search-input-container .fa.fa-search {
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
        }

        .search-input-container .search-form {
            padding-left: 30px !important;
        }

        .search-results-mask-panel {
            display: none;
            position: fixed;
            top: 50px;
            left: 0;
            bottom: 30px;
            right: 0;
            opacity: 0.5;
            background-color: #0a0a0a;
            z-index: 9997;
        }

        .search-element .clear-search {
            margin-right: 3px;
            cursor: pointer;
            margin-top: 4px !important;
            font-size: 14px;
            opacity: 0.5;
        }

        .dark-mode .search-element .clear-search {
            color: #fff;
        }

        .search-element .clear-search:hover {
            opacity: 1;
        }

        .search-filters .dropdown-menu {
            z-index: 9999;
            background-color: var(--cassini-bg-color);
            overflow-y: auto;
            overflow-x: hidden;
        }

        .no-reults-container,
        .searching-container {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
        }

        .no-reults-container > .no-results,
        .searching-container > .searching {
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            position: absolute;
            width: 100%;
        }

        .no-reults-container > .no-results > img,
        .searching-container > .searching > img {
            width: 300px;
        }

        .no-reults-container > .no-results > .no-results-message {
            font-size: 20px;
            font-weight: 400 !important;
        }

        .searching-container > .searching > .searching-message {
            font-size: 20px;
            font-weight: 300 !important;
        }

        .no-reults-container > .no-results > .no-results-suggestion {
            font-size: 14px;
            font-weight: 300 !important;
        }

        .searching-container .searching .progress {
            height: 5px;
            width: 400px;
            margin-left: auto;
            margin-right: auto;
            margin-top: 20px;
            border-radius: 0;
        }

        .lds-ring {
            width: 30px;
            height: 24px;
            display: inline-block;
        }

        .lds-ring div {
            box-sizing: border-box;
            display: block;
            position: absolute;
            width: 20px;
            height: 20px;
            margin: 8px;
            border: 3px solid #42526E;
            border-radius: 50%;
            animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
            border-color: #42526E transparent transparent transparent;
        }

        .lds-ring div:nth-child(1) {
            animation-delay: -0.45s;
        }

        .lds-ring div:nth-child(2) {
            animation-delay: -0.3s;
        }

        .lds-ring div:nth-child(3) {
            animation-delay: -0.15s;
        }

        @keyframes lds-ring {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }

        .results-table thead th {
            position: sticky;
            top: -10px;
        }

    </style>

    <div class="search-element search-input-container">
        <div class="search-filters" uib-dropdown>
            <i class="las la-times clear-search" title="Clear Search"
               ng-show="searchVm.searchText.length > 0"
               ng-click="searchVm.clearSearch()" style="">
            </i>

            <span ng-click="searchVm.preventClick($event); searchVm.showFiltersMenu();"
                  uib-dropdown
                  auto-close="outsideClick"
                  uib-dropdown-toggle>{{searchVm.filter.label}}
                <i class="fa fa-caret-down dropdown-caret"></i>
            </span>

            <ul id="filtersMenu" class="dropdown-menu">
                <li ng-click="searchVm.setFilter(allTitle)"><a href="" translate>ALL</a></li>
                <li ng-click="searchVm.setFilter(itemTitle)"><a href="" translate>ITEMS_ALL_TITLE</a></li>
                <li ng-click="searchVm.setFilter(designDataTitle)"><a href="" translate>DESIGN_DATA</a></li>
                <li ng-click="searchVm.setFilter(changesTitle)"><a href="" translate>ITEM_DETAILS_TAB_CHANGES</a></li>
                <li ng-click="searchVm.setFilter(qualityTitle)"><a href="" translate>QUALITY</a></li>
                <li ng-click="searchVm.setFilter(manufacturingTitle)"><a href="" translate>MANUFACTURING</a></li>
                <li ng-click="searchVm.setFilter(maintenanceTitle)"><a href="" translate>MAINTENANCEANDREPAIRS</a></li>
                <li ng-click="searchVm.setFilter(oemTitle)"><a href="" translate>OEM</a></li>
                <li ng-click="searchVm.setFilter(complianceTitle)"><a href="" translate>COMPLIANCE</a></li>
                <li ng-click="searchVm.setFilter(projectTitle)"><a href="" translate>NO_OF_PROJECTS</a></li>
                <li ng-click="searchVm.setFilter(programTitle)"><a href="" translate>PROGRAMS</a></li>
                <li ng-click="searchVm.setFilter(requirementDocumentTitle)"><a href="" translate>REQ_DOCUMENTS</a></li>
                <li ng-click="searchVm.setFilter(requirementTitle)"><a href="" translate>REQUIREMENTS</a></li>
                <li ng-click="searchVm.setFilter(workflowTitle)"><a href="" translate>WORKFLOW_ALL_TITLE</a></li>
                <li ng-click="searchVm.setFilter(fileTitle)"><a href="" translate>FILES</a></li>
                <li ng-click="searchVm.setFilter(documentTitle)"><a href="" translate>DOCUMENTS</a></li>
                <li ng-click="searchVm.setFilter(customObjectTitle)"><a href="" translate>CUSTOM_OBJECT_TITLE</a></li>
                <li ng-click="searchVm.setFilter(nprTitle)"><a href="" translate>NEW_PART_REQUESTS</a></li>
                <li ng-click="searchVm.setFilter(tagsTitle)"><a href="" translate>TAGS</a></li>
            </ul>
        </div>
        <i class="fa fa-search"></i>

        <form id="globalSearch">
            <input type="text"
                   autocomplete="off"
                   class="form-control input-sm search-form"
                   placeholder={{searchVm.searchTitle}}
                   onfocus="this.setSelectionRange(0, this.value.length)"
                   ng-click="searchVm.preventClick($event)"
                   ng-focus="searchVm.onFocus()"
                   ng-model="searchVm.searchText"
                   ng-model-options="{ debounce: 500 }"
                   ng-change="searchVm.performSearch()"
                   ng-enter="searchVm.performSearch()">
        </form>
    </div>

    <div id="search-results-container" class="search-results-container">
        <div class="searching-container" ng-if="searchVm.searching">
            <div class="searching">
                <img src="app/assets/images/searching.png" alt="">

                <div class="searching-message">
                    <div class="lds-ring">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                    <span translate>SEARCHING</span>
                </div>
            </div>
        </div>

        <div class="search-tabs" ng-if="!searchVm.searching && searchVm.detailsFound">
            <div class="left-tab">
                <div class="left-element" ng-click="searchVm.activateTab('ITEM');searchVm.active = itemTitle"
                     ng-show="(searchVm.searchResults.itemsCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == itemTitle)">
                    <div ng-class="{'active-tab':searchVm.active === itemTitle}" style="height: 100%;padding: 10px;">
                        {{'ITEMS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.itemsCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('NPR');searchVm.active = nprTitle"
                     ng-show="(searchVm.searchResults.nprCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == nprTitle)">
                    <div ng-class="{'active-tab':searchVm.active === nprTitle}" style="height: 100%;padding: 10px;">
                        {{'NEW_PART_REQUESTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.nprCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('TAG');searchVm.active = tagsTitle"
                     ng-show="(searchVm.searchResults.tagsCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == tagsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === tagsTitle}" style="height: 100%;padding: 10px;">
                        {{'TAGS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.tagsCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('PDM_VAULT');searchVm.active = vaultsTitle"
                     ng-show="(searchVm.searchResults.pdmVaultCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == designDataTitle)) || (searchVm.filter.label == vaultsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === vaultsTitle}" style="height: 100%;padding: 10px;">
                        {{'VAULTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.pdmVaultCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PDM_ASSEMBLY');searchVm.active = assembliesTitle"
                     ng-show="(searchVm.searchResults.pdmAssemblyCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == designDataTitle)) || (searchVm.filter.label == assembliesTitle)">
                    <div ng-class="{'active-tab':searchVm.active === assembliesTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'ASSEMBLIES' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.pdmAssemblyCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('PDM_PART');searchVm.active = partsTitle"
                     ng-show="(searchVm.searchResults.pdmPartCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == designDataTitle)) || (searchVm.filter.label == partsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === partsTitle}" style="height: 100%;padding: 10px;">
                        {{'PARTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.pdmPartCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('PDM_DRAWING');searchVm.active = drawingsTitle"
                     ng-show="(searchVm.searchResults.pdmDrawingCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == designDataTitle)) || (searchVm.filter.label == drawingsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === drawingsTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'DRAWINGS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.pdmDrawingCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('ECR');searchVm.active = ecrTitle"
                     ng-show="(searchVm.searchResults.ecrCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == ecrTitle)">
                    <div ng-class="{'active-tab':searchVm.active === ecrTitle}" style="height: 100%;padding: 10px;">
                        {{'ECRS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.ecrCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('ECO');searchVm.active = ecoTitle"
                     ng-show="(searchVm.searchResults.ecoCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == ecoTitle)">
                    <div ng-class="{'active-tab':searchVm.active === ecoTitle}" style="height: 100%;padding: 10px;">
                        {{'ECOS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.ecoCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('DCR');searchVm.active = dcrTitle"
                     ng-show="(searchVm.searchResults.dcrCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == dcrTitle)">
                    <div ng-class="{'active-tab':searchVm.active === dcrTitle}" style="height: 100%;padding: 10px;">
                        {{'DCRS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.dcrCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('DCO');searchVm.active = dcoTitle"
                     ng-show="(searchVm.searchResults.dcoCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == dcoTitle)">
                    <div ng-class="{'active-tab':searchVm.active === dcoTitle}" style="height: 100%;padding: 10px;">
                        {{'DCOS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.dcoCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('MCO');searchVm.active = mcoTitle"
                     ng-show="(searchVm.searchResults.mcoCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == mcoTitle)">
                    <div ng-class="{'active-tab':searchVm.active === mcoTitle}" style="height: 100%;padding: 10px;">
                        {{'MCOS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.mcoCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('WAIVER');searchVm.active = waiverTitle"
                     ng-show="(searchVm.searchResults.waiverCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == waiverTitle)">
                    <div ng-class="{'active-tab':searchVm.active === waiverTitle}"
                         style="height: 100%;padding: 10px;">{{'WAIVER' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.waiverCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('DEVIATION');searchVm.active = deviationTitle"
                     ng-show="(searchVm.searchResults.deviationCount> 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == changesTitle)) || (searchVm.filter.label == deviationTitle)">
                    <div ng-class="{'active-tab':searchVm.active === deviationTitle}"
                         style="height: 100%;padding: 10px;">{{'DEVIATION' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.deviationCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PRODUCTINSPECTIONPLAN');searchVm.active = productInspectionPlanTitle"
                     ng-show="(searchVm.searchResults.productInspectionPlanCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == productInspectionPlanTitle)">
                    <div ng-class="{'active-tab':searchVm.active === productInspectionPlanTitle}"
                         style="height: 100%;padding: 10px;">{{'PRODUCTINSPECTIONPLANS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.productInspectionPlanCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('MATERIALINSPECTIONPLAN');searchVm.active = materialInspectionPlanTitle"
                     ng-show="(searchVm.searchResults.materialInspectionPlanCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == materialInspectionPlanTitle)">
                    <div ng-class="{'active-tab':searchVm.active === materialInspectionPlanTitle}"
                         style="height: 100%;padding: 10px;">{{'MATERIALINSPECTIONPLANS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.materialInspectionPlanCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('ITEMINSPECTION');searchVm.active = itemInspectionTitle"
                     ng-show="(searchVm.searchResults.itemInspectionCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == itemInspectionTitle)">
                    <div ng-class="{'active-tab':searchVm.active === itemInspectionTitle}"
                         style="height: 100%;padding: 10px;">{{'ITEMINSPECTIONS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.itemInspectionCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('MATERIALINSPECTION');searchVm.active = materialInspectionTitle"
                     ng-show="(searchVm.searchResults.materialInspectionCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == materialInspectionTitle)">
                    <div ng-class="{'active-tab':searchVm.active === materialInspectionTitle}"
                         style="height: 100%;padding: 10px;">{{'MATERIALINSPECTIONS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.materialInspectionCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PROBLEMREPORT');searchVm.active = problemReportTitle"
                     ng-show="(searchVm.searchResults.problemReportCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == problemReportTitle)">
                    <div ng-class="{'active-tab':searchVm.active === problemReportTitle}"
                         style="height: 100%;padding: 10px;">{{'PROBLEMREPORTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.problemReportCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('NCR');searchVm.active = ncrTitle"
                     ng-show="(searchVm.searchResults.ncrCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == ncrTitle)">
                    <div ng-class="{'active-tab':searchVm.active === ncrTitle}"
                         style="height: 100%;padding: 10px;">{{'NCRS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.ncrCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('QCR');searchVm.active = qcrTitle"
                     ng-show="(searchVm.searchResults.qcrCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == qcrTitle)">
                    <div ng-class="{'active-tab':searchVm.active === qcrTitle}"
                         style="height: 100%;padding: 10px;">{{'QCRS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.qcrCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('PPAP');searchVm.active = ppapTitle"
                     ng-show="(searchVm.searchResults.ppapCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == ppapTitle)">
                    <div ng-class="{'active-tab':searchVm.active === ppapTitle}"
                         style="height: 100%;padding: 10px;">{{'PPAPS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.ppapCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('SUPPLIERAUDIT');searchVm.active = supplierAuditTitle"
                     ng-show="(searchVm.searchResults.supplierAuditCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == qualityTitle)) || (searchVm.filter.label == supplierAuditTitle)">
                    <div ng-class="{'active-tab':searchVm.active === supplierAuditTitle}"
                         style="height: 100%;padding: 10px;">{{'SUPPLIER_AUDITS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.supplierAuditCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('PLANT');searchVm.active = plantsTitle"
                     ng-show="(searchVm.searchResults.plantCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == plantsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === plantsTitle}"
                         style="height: 100%;padding: 10px;">{{'PLANTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.plantCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('ASSEMBLYLINE');searchVm.active = assemblyLineTitle"
                     ng-show="(searchVm.searchResults.assemblyLineCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == assemblyLineTitle)">
                    <div ng-class="{'active-tab':searchVm.active === assemblyLineTitle}"
                         style="height: 100%;padding: 10px;">{{'ASSEMBLY_LINES' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.assemblyLineCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('WORKCENTER');searchVm.active = workCentersTitle"
                     ng-show="(searchVm.searchResults.workCenterCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == workCentersTitle)">
                    <div ng-class="{'active-tab':searchVm.active === workCentersTitle}"
                         style="height: 100%;padding: 10px;">{{workCenters}}
                        <span class="label label-default">{{searchVm.searchResults.workCenterCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('MACHINE');searchVm.active = machinesTitle"
                     ng-show="(searchVm.searchResults.machineCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == machinesTitle)">
                    <div ng-class="{'active-tab':searchVm.active === machinesTitle}"
                         style="height: 100%;padding: 10px;">{{'MACHINES' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.machineCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('EQUIPMENT');searchVm.active = equipmentsTitle"
                     ng-show="(searchVm.searchResults.equipmentCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == equipmentsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === equipmentsTitle}"
                         style="height: 100%;padding: 10px;">{{equipments}}
                        <span class="label label-default">{{searchVm.searchResults.equipmentCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('INSTRUMENT');searchVm.active = instrumentsTitle"
                     ng-show="(searchVm.searchResults.instrumentCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == instrumentsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === instrumentsTitle}"
                         style="height: 100%;padding: 10px;">{{instrumnets}}
                        <span class="label label-default">{{searchVm.searchResults.instrumentCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('TOOL');searchVm.active = toolsTitle"
                     ng-show="(searchVm.searchResults.toolCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == toolsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === toolsTitle}"
                         style="height: 100%;padding: 10px;">{{'TOOLS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.toolCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('JIG');searchVm.active = jigsTitle"
                     ng-show="(searchVm.searchResults.jigCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == jigsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === jigsTitle}"
                         style="height: 100%;padding: 10px;">{{jigs}}
                        <span class="label label-default">{{searchVm.searchResults.jigCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('FIXTURE');searchVm.active = fixturesTitle"
                     ng-show="(searchVm.searchResults.fixtureCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == fixturesTitle)">
                    <div ng-class="{'active-tab':searchVm.active === fixturesTitle}"
                         style="height: 100%;padding: 10px;">{{fixtures}}
                        <span class="label label-default">{{searchVm.searchResults.fixtureCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('MATERIAL');searchVm.active = materialsTitle"
                     ng-show="(searchVm.searchResults.materialCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == materialsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === materialsTitle}"
                         style="height: 100%;padding: 10px;">{{'MATERIALS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.materialCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('MANPOWER');searchVm.active = manpowersTitle"
                     ng-show="(searchVm.searchResults.manPowerCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == manpowersTitle)">
                    <div ng-class="{'active-tab':searchVm.active === manpowersTitle}"
                         style="height: 100%;padding: 10px;">{{manpowers}}
                        <span class="label label-default">{{searchVm.searchResults.manPowerCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('SHIFT');searchVm.active = shiftsTitle"
                     ng-show="(searchVm.searchResults.shiftCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == shiftsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === shiftsTitle}"
                         style="height: 100%;padding: 10px;">{{'SHIFTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.shiftCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('OPERATION');searchVm.active = operationsTitle"
                     ng-show="(searchVm.searchResults.operationCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == manufacturingTitle)) || (searchVm.filter.label == operationsTitle)">
                    <div ng-class="{'active-tab':searchVm.active === operationsTitle}"
                         style="height: 100%;padding: 10px;">{{operations}}
                        <span class="label label-default">{{searchVm.searchResults.operationCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('ASSET');searchVm.active = assetTitle"
                     ng-show="(searchVm.searchResults.assetCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == maintenanceTitle)) || (searchVm.filter.label == assetTitle)">
                    <div ng-class="{'active-tab':searchVm.active === assetTitle}"
                         style="height: 100%;padding: 10px;">{{'ASSETS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.assetCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('METER');searchVm.active = meterTitle"
                     ng-show="(searchVm.searchResults.meterCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == maintenanceTitle)) || (searchVm.filter.label == meterTitle)">
                    <div ng-class="{'active-tab':searchVm.active === meterTitle}"
                         style="height: 100%;padding: 10px;">{{meters}}
                        <span class="label label-default">{{searchVm.searchResults.meterCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('SPAREPART');searchVm.active = sparePartTitle"
                     ng-show="(searchVm.searchResults.sparePartCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == maintenanceTitle)) || (searchVm.filter.label == sparePartTitle)">
                    <div ng-class="{'active-tab':searchVm.active === sparePartTitle}"
                         style="height: 100%;padding: 10px;">{{'SPARE_PARTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.sparePartCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('MAINTENANCEPLAN');searchVm.active = maintenancePlanTitle"
                     ng-show="(searchVm.searchResults.maintenancePlanCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == maintenanceTitle)) || (searchVm.filter.label == maintenancePlanTitle)">
                    <div ng-class="{'active-tab':searchVm.active === maintenancePlanTitle}"
                         style="height: 100%;padding: 10px;">{{maintenancePlan}}
                        <span class="label label-default">{{searchVm.searchResults.maintenancePlanCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('WORKREQUEST');searchVm.active = workRequestTitle"
                     ng-show="(searchVm.searchResults.workRequestCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == maintenanceTitle)) || (searchVm.filter.label == workRequestTitle)">
                    <div ng-class="{'active-tab':searchVm.active === workRequestTitle}"
                         style="height: 100%;padding: 10px;">{{'WORK_REQUESTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.workRequestCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('WORKORDER');searchVm.active = workOrderTitle"
                     ng-show="(searchVm.searchResults.workOrderCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == maintenanceTitle)) || (searchVm.filter.label == workOrderTitle)">
                    <div ng-class="{'active-tab':searchVm.active === workOrderTitle}"
                         style="height: 100%;padding: 10px;">{{'WORK_ORDERS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.workOrderCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('MANUFACTURER');searchVm.active = manufacturerTitle"
                     ng-show="(searchVm.searchResults.mfrCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == oemTitle)) || (searchVm.filter.label == manufacturerTitle)">
                    <div ng-class="{'active-tab':searchVm.active === manufacturerTitle}"
                         style="height: 100%;padding: 10px;">{{manufacturers}}
                        <span class="label label-default">{{searchVm.searchResults.mfrCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('MANUFACTURERPART');searchVm.active = manufacturerPartTitle"
                     ng-show="(searchVm.searchResults.mfrPartCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == oemTitle)) || (searchVm.filter.label == manufacturerPartTitle)">
                    <div ng-class="{'active-tab':searchVm.active === manufacturerPartTitle}"
                         style="height: 100%;padding: 10px;">{{'MANUFACTURER_PARTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.mfrPartCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('SUPPLIER');searchVm.active = supplierTitle"
                     ng-show="(searchVm.searchResults.supplierCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == oemTitle)) || (searchVm.filter.label == supplierTitle)">
                    <div ng-class="{'active-tab':searchVm.active === supplierTitle}"
                         style="height: 100%;padding: 10px;">{{'SUPPLIERS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.supplierCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('SUBSTANCE');searchVm.active = substanceTitle"
                     ng-show="(searchVm.searchResults.pgcSubstanceCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == complianceTitle)) || (searchVm.filter.label == substanceTitle)">
                    <div ng-class="{'active-tab':searchVm.active === substanceTitle}"
                         style="height: 100%;padding: 10px;">{{'SUBSTANCES' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.pgcSubstanceCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PGCSPECIFICATION');searchVm.active = pgcSpecificationTitle"
                     ng-show="(searchVm.searchResults.pgcSpecificationCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == complianceTitle)) || (searchVm.filter.label == pgcSpecificationTitle)">
                    <div ng-class="{'active-tab':searchVm.active === pgcSpecificationTitle}"
                         style="height: 100%;padding: 10px;">{{'SPECIFICATIONS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.pgcSpecificationCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PGCDECLARATION');searchVm.active = pgcDeclarationTitle"
                     ng-show="(searchVm.searchResults.pgcDeclarationCount > 0 && (searchVm.filter.label == allTitle || searchVm.filter.label == complianceTitle)) || (searchVm.filter.label == pgcDeclarationTitle)">
                    <div ng-class="{'active-tab':searchVm.active === pgcDeclarationTitle}"
                         style="height: 100%;padding: 10px;">{{declarations}}
                        <span class="label label-default">{{searchVm.searchResults.pgcDeclarationCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PROJECT');searchVm.active = projectTitle"
                     ng-show="(searchVm.searchResults.projectsCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == projectTitle)">
                    <div ng-class="{'active-tab':searchVm.active === projectTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'NO_OF_PROJECTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.projectsCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('PROGRAM');searchVm.active = programTitle"
                     ng-show="(searchVm.searchResults.programCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == programTitle)">
                    <div ng-class="{'active-tab':searchVm.active === programTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'PROGRAMS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.programCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.active = requirementDocumentTitle;searchVm.activateTab('REQUIREMENTDOCUMENT')"
                     ng-show="(searchVm.searchResults.requirementDocumentsCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == requirementDocumentTitle)">
                    <div ng-class="{'active-tab':searchVm.active === requirementDocumentTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'REQ_DOCUMENTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.requirementDocumentsCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.active = requirementTitle;searchVm.activateTab('REQUIREMENT')"
                     ng-show="(searchVm.searchResults.requirementsCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == requirementTitle)">
                    <div ng-class="{'active-tab':searchVm.active === requirementTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'REQUIREMENTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.requirementsCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('WORKFLOW');searchVm.active = workflowTitle"
                     ng-show="(searchVm.searchResults.workflowCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == workflowTitle)">
                    <div ng-class="{'active-tab':searchVm.active === workflowTitle}"
                         style="height: 100%;padding: 10px;">
                        Workflows
                        <span class="label label-default">{{searchVm.searchResults.workflowCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('FILE');searchVm.active = fileTitle"
                     ng-show="(searchVm.searchResults.filesCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == fileTitle)">
                    <div ng-class="{'active-tab':searchVm.active === fileTitle}" style="height: 100%;padding: 10px;">
                        {{'FILES' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.filesCount}}</span>
                    </div>
                </div>
                <div class="left-element" ng-click="searchVm.activateTab('DOCUMENT');searchVm.active = documentTitle"
                     ng-show="(searchVm.searchResults.documentCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == documentTitle)">
                    <div ng-class="{'active-tab':searchVm.active === documentTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'DOCUMENTS' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.documentCount}}</span>
                    </div>
                </div>
                <div class="left-element"
                     ng-click="searchVm.activateTab('CUSTOMOBJECT');searchVm.active = customObjectTitle"
                     ng-show="(searchVm.searchResults.customObjectCount > 0 && searchVm.filter.label == allTitle) || (searchVm.filter.label == customObjectTitle)">
                    <div ng-class="{'active-tab':searchVm.active === customObjectTitle}"
                         style="height: 100%;padding: 10px;">
                        {{'CUSTOM_OBJECT_TITLE' | translate}}
                        <span class="label label-default">{{searchVm.searchResults.customObjectCount}}</span>
                    </div>
                </div>
            </div>
            <div class="right-tab" style="">

                <%------------------------------  Items ---------------------%>
                <h5 ng-if="!searchVm.detailsFound" style="padding-left: 10px;" translate>NO_SEARCH_RESULT_FOUND</h5>

                <div class="results-table" ng-if="searchVm.active === itemTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-100"><span translate>TYPE</span></th>
                            <th class="col-width-100"><span translate>NUMBER</span></th>
                            <th class="col-width-200"><span translate>NAME</span></th>
                            <th class="col-width-250"><span translate>DESCRIPTION</span></th>
                            <th class="text-center"><span translate>REVISION</span></th>
                            <th><span translate>LIFE_CYCLE</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.items.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Items.png" alt="" class="image">

                                    <div class="message" translate>NO_ITEMS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="searchVm.searchResults.items.content.length > 0"
                            ng-repeat="item in searchVm.searchResults.items.content">
                            <td class="col-width-100">
                                <span ng-bind-html="item.itemType.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="searchVm.openItemDetails(item)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="item.itemNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200"><span
                                    ng-bind-html="item.itemName | highlightText: searchVm.searchText"></span></td>
                            <td class="col-width-250">
                                <%--<span ng-bind-html="(item.description.length > 30 ? item.description.trunc(30,
                                true) : item.description) | highlightText: searchVm.searchText"></span>--%>
                                <span ng-bind-html="item.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="text-center">{{item.latestRevisionObject.revision}}</td>
                            <td>
                                <item-status item="item.latestRevisionObject"></item-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                </div>
                <div class="results-table" ng-if="searchVm.active === nprTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-150" translate>REQUESTER</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th class="col-width-250" translate>REASON_FOR_REQUEST</th>
                            <th class="col-width-250" translate>NOTES</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.nprs.content.length == 0">
                            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/problemReport.png" alt="" class="image">

                                    <div class="message" translate>NO_PRS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>

                        </tr>

                        <tr ng-repeat="npr in searchVm.searchResults.nprs.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showNpr(npr)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="npr.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="npr.requesterObject.fullName | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{npr.description}}">
                                <span ng-bind-html="npr.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <span class="label label-outline bg-light-warning"
                                      ng-if="npr.status == 'HOLD'">PENDING</span>
                                <object-status object="npr"></object-status>
                            </td>

                            <td class="col-width-250" title="{{npr.reasonForRequest}}">
                                <span ng-bind-html="npr.reasonForRequest  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{npr.notes}}">
                                <span ng-bind-html="npr.notes  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                </div>

                <div class="results-table" ng-if="searchVm.active === tagsTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-150" translate>TAG</th>
                            <th class="col-width-150">
                                <span ng-if="searchVm.filters.objectType != ''"
                                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                        {{searchVm.filters.objectType}}
                                    <i class="fa fa-times-circle" ng-click="searchVm.clearTagObjectType()"
                                       title="{{removeTitle}}"></i>
                                </span>
                                <br>

                                <div class="dropdown" uib-dropdown style="display: inline-block">
                                    <span uib-dropdown-toggle ng-click="searchVm.showTagFiltersMenu()"><span translate>OBJECT_TYPE</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                                    <ul uib-dropdown-menu id="tag-dropdown-menu"
                                        class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                        style="max-height:250px;overflow-y: auto;">
                                        <li ng-repeat="objectType in searchVm.searchResults.tagObjectTypes"
                                            ng-click="searchVm.onSelectTagObjectType(objectType)"
                                            style="text-transform: uppercase;"><a href="">{{objectType}}</a>
                                        </li>
                                    </ul>
                                </div>
                            </th>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-250" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.tags.content.length == 0">
                            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/problemReport.png" alt="" class="image">

                                    <div class="message" translate>NO_TAGS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>

                        </tr>

                        <tr ng-repeat="tag in searchVm.searchResults.tags.content">
                            <td class="col-width-150">
                                <span ng-bind-html="tag.label | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">
                                <object-type-status object="tag"></object-type-status>
                            </td>
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showDetails(tag)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{tag.number}}</a>
                            </td>
                            <td class="col-width-250">
                                <a href="" ng-if="tag.number == null || tag.number == ''"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-click="searchVm.showDetails(tag)">{{tag.name}}
                                </a>
                                <span ng-if="tag.number != null && tag.number != ''">{{tag.name}}</span>
                            </td>

                            <td class="col-width-250" title="{{npr.reasonForRequest}}">{{tag.description}}</td>
                        </tr>
                        </tbody>
                    </table>

                </div>

                <%-------------------------  Projects -----------------------%>

                <div class="results-table" ng-if="searchVm.active === projectTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-200"><span translate>NAME</span></th>
                            <th class="col-width-250"><span translate>DESCRIPTION</span></th>
                            <th class="col-width-150"><span translate>PROJECT_MANAGER</span></th>
                            <th class="col-width-150"><span translate>PLANNED_START_DATE</span></th>
                            <th class="col-width-150"><span translate>PLANNED_FINISH_DATE</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.projects.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Project.png" alt="" class="image">

                                    <div class="message" translate>NO_PROJECTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>

                        </tr>
                        <tr ng-if="searchVm.searchResults.projects.content.length > 0"
                            ng-repeat="project in searchVm.searchResults.projects.content">
                            <td class="col-width-200">
                                <a href=""
                                   ng-click="searchVm.openProjectDetails(project)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="project.name | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-250" title="{{project.description}}">
                                <span ng-bind-html="project.description | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150"><span
                                    ng-bind-html="project.projectManagerObject.firstName | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">{{project.plannedStartDate}}</td>
                            <td class="col-width-150">{{project.plannedFinishDate}}</td>
                        </tr>
                        </tbody>
                    </table>

                </div>

                    <div class="results-table" ng-if="searchVm.active === programTitle" style="padding-top: 10px;">
                        <table class="table table-striped highlight-row" style="width: 100% !important;">
                            <thead>
                            <tr>
                                <th class="col-width-250" translate>NAME</th>
                                <th class="col-width-250" translate>TYPE</th>
                                <th class="description-column" translate>DESCRIPTION</th>
                                <th style="width: 150px" translate>PROGRAM_MANAGER</th>
                                <th style="width: 150px" translate>PROGRAM_PROGRESS</th>
                                <th style="width: 150px" translate>MODIFIED_BY</th>
                                <th style="width: 150px" translate>MODIFIED_DATE</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="searchVm.searchResults.programs.content.length == 0">
                                <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                    <div class="no-data">
                                        <img src="app/assets/no_data_images/Project.png" alt="" class="image">

                                        <div class="message" translate>NO_PROGRAMS</div>
                                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                            NO_PERMISSION_MESSAGE
                                        </div>
                                    </div>
                                </td>

                            </tr>
                            <tr ng-if="searchVm.searchResults.programs.content.length > 0"
                                ng-repeat="program in searchVm.searchResults.programs.content">
                                <td class="col-width-250">
                                    <a href="" ng-click="searchVm.showProgram(program)"
                                       ng-show="hasPermission('program','view')">
                                        <span ng-bind-html="program.name | highlightText: freeTextQuery"></span>
                                    </a>
                                <span ng-show="hasPermission('program','view') == false"
                                      title="You don't have permission to see program">
                                <span ng-bind-html="program.name | highlightText: freeTextQuery"></span>
                                </span>
                                </td>
                                <td class="col-width-250">
                                <span title="{{program.type}}"><span
                                        ng-bind-html="program.type  | highlightText: freeTextQuery"></span>
                                </span>
                                </td>
                                <td class="description-column">
                                <span title="{{program.description}}"><span
                                        ng-bind-html="program.description  | highlightText: freeTextQuery"></span>
                                </span>
                                </td>
                                <td style="width: 150px">{{program.managerFullName}}</td>
                                <td style="text-align: center">{{program.percent}}</td>
                                <td>{{program.modifiedByName}}</td>
                                <td>{{program.modifiedDate}}</td>
                            </tr>
                            </tbody>
                        </table>

                    </div>

                <%---------------------------  Changes --------------------%>
                <div class="results-table" ng-if="searchVm.active === ecoTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-100"><span translate>ECO_NUMBER</span></th>
                            <th class="col-width-200"><span translate>TITLE</span></th>
                            <th class="col-width-250"><span translate>DESCRIPTION</span></th>
                            <th class="col-width-100"><span translate>STATUS</span></th>
                            <th class="col-width-150"><span translate>WORKFLOW</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.ecos.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/ECO.png" alt="" class="image">

                                    <div class="message" translate>NO_ECO</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="searchVm.searchResults.ecos.content.length > 0"
                            ng-repeat="eco in searchVm.searchResults.ecos.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.openEcoDetails(eco)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="eco.ecoNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200"><span
                                    ng-bind-html="eco.title | highlightText: searchVm.searchText"></span></td>
                            <td class="col-width-250" title="{{eco.description}}"><span
                                    ng-bind-html="eco.description | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <workflow-status workflow="eco"></workflow-status>
                            </td>
                            <td class="col-width-150"><span
                                    ng-bind-html="eco.workflowObject.name | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <%----------------------------  Requirement Documents ----------------------%>
                <div class="results-table" ng-if="searchVm.active === requirementDocumentTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-150" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="width: 150px; text-align: center;z-index: auto !important;"
                                translate="ITEM_ALL_REVISION"></th>
                            <th style="width: 150px;z-index: auto !important;" translate="ITEM_ALL_LIFECYCLE"></th>
                            <th style="width: 150px" translate>DOCUMENT_OWNER</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.requirementDocuments.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Documents.png" alt="" class="image">

                                    <div class="message" translate>NO_REQ_DOCUMENT</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="reqDocument in searchVm.searchResults.requirementDocuments.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showReqDocument(reqDocument)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="reqDocument.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="reqDocument.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="reqDocument.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td title="{{reqDocument.description}}" class="col-width-250">
                                <span ng-bind-html="reqDocument.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td style="width: 150px; text-align: center;">
                                {{reqDocument.latestRevisionObject.revision}}
                            </td>
                            <td style="width: 150px">
                                <item-status item="reqDocument.latestRevisionObject"></item-status>
                            </td>
                            <td>{{reqDocument.latestRevisionObject.owner.fullName}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%----------------------------  Requirements ----------------------%>
                <div class="results-table" ng-if="searchVm.active === requirementTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-150" translate>TYPE</th>
                            <th class="col-width-200" translate>REQDOCUMENT</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th class="col-width-100" translate>PRIORITY</th>
                            <th class="col-width-150">
                                <span translate>ASSIGNED_TO</span>
                            </th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.requirements.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Requirements.png" alt="" class="image">

                                    <div class="message" translate>NO_REQUIREMENTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr id="{{requirement.id}}"
                            ng-repeat="requirement in searchVm.searchResults.requirements.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showRequirementDetails(requirement)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="requirement.master.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="requirement.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="requirement.master.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <a href=""
                                   ng-click="searchVm.showReqDocument(requirement.master.requirementDocumentRevision.master)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="requirement.master.requirementDocumentRevision.name | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <reqstatus object="requirement.master"></reqstatus>
                            </td>
                            <td class="col-width-100">
                                <priority object="requirement.master"></priority>
                            </td>
                            <td class="col-width-150">
                                {{requirement.assignedToObject.fullName}}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%----------------------------  Workflows ----------------------%>
                <div class="results-table" ng-if="searchVm.active === workflowTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-200"><span translate>WORKFLOW_ALL_NAME</span></th>
                            <th class="col-width-150"><span translate>WORKFLOW_TYPE</span></th>
                            <th class="col-width-250"><span translate>DESCRIPTION</span></th>
                            <th class="col-width-100"><span translate>CREATED_BY</span></th>
                            <th class="col-width-150"><span translate>CREATED_DATE</span></th>

                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.workflows.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Workflow.png" alt="" class="image">

                                    <div class="message" translate>NO_WORKFLOW</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="searchVm.searchResults.workflows.content.length > 0"
                            ng-repeat="workflow in searchVm.searchResults.workflows.content">
                            <td class="col-width-200">
                                <a href=""
                                   ng-click="searchVm.openWorkflowDetails(workflow)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="workflow.name | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-150"><span
                                    ng-bind-html="workflow.workflowType.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{workflow.description}}"><span
                                    ng-bind-html="workflow.description | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100"><span
                                    ng-bind-html="workflow.createdByObject.firstName | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150"><span
                                    ng-bind-html="workflow.createdDate | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <%----------------------------  Files --------------------------%>
                <div class="results-table" ng-if="searchVm.active === fileTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-200"><span translate>File Name</span></th>
                            <th class="col-width-200"><span translate>Object Name</span></th>
                            <th class="col-width-150"><span translate>Object Type</span></th>
                            <th class="text-center"><span translate>Version</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.files.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                                    <div class="message" translate>NO_FILES</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="searchVm.searchResults.files.content.length > 0"
                            ng-repeat="file in searchVm.searchResults.files.content">
                            <td class="col-width-200">
                                <a href=""
                                   ng-click="filePreview(file)"
                                   title="{{previewFileTitle}}">
                                    <span ng-bind-html="file.file.name | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <a href=""
                                   ng-click="searchVm.openFileDetails(file)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="file.name | highlightText: searchVm.searchText"></span>
                                    <span ng-if="file.revision !=null">({{file.revision}})</span>
                                </a>
                            </td>
                            <td class="col-width-150">
                                <object-type-status object="file"></object-type-status>
                            </td>
                            <td class="text-center">{{file.file.version}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === documentTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-250"><span translate>NAME</span></th>
                            <th class="col-width-200"><span translate>PATH</span></th>
                            <th class="text-center"><span translate>VERSION</span></th>
                            <th><span translate>LIFECYCLE</span></th>
                            <th><span translate>MODIFIED_DATE</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.documents.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                                    <div class="message" translate>NO_DOCUMENTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="searchVm.searchResults.documents.content.length > 0"
                            ng-repeat="file in searchVm.searchResults.documents.content">
                            <td class="col-width-250">
                                <a href=""
                                   ng-click="documentPreview(file)" title="{{previewFileTitle}}">
                                    <span ng-bind-html="file.name | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">{{file.filePath}}</td>
                            <td class="text-center">{{file.revision}}.{{file.version}}</td>
                            <td class="col-width-100">
                                <item-status item="file"></item-status>
                            </td>
                            <td>{{file.modifiedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="results-table"
                     ng-if="searchVm.active === productInspectionPlanTitle || searchVm.active === materialInspectionPlanTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-150" translate>TYPE</th>
                            <th class="col-width-150" ng-if="searchVm.active === productInspectionPlanTitle" translate>
                                PRODUCT
                            </th>
                            <th class="col-width-150" ng-if="searchVm.active === materialInspectionPlanTitle" translate>
                                MATERIAL
                            </th>
                            <th class="col-width-200" translate>NAME</th>
                            <th style="width: 150px;text-align: center" translate>REVISION</th>
                            <th class="col-width-100" translate>LIFECYCLE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.inspectionPlans.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Plan.png" alt="" class="image">

                                    <div class="message" translate>NO_PLANS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>

                        </tr>

                        <tr ng-repeat="inspectionPlan in searchVm.searchResults.inspectionPlans.content">
                            <td class="col-width-100">
                                <a href="" ng-click="showPlanDetails(inspectionPlan)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="inspectionPlan.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="inspectionPlan.type | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">
                                <span ng-if="searchVm.active === productInspectionPlanTitle"
                                      ng-bind-html="inspectionPlan.productName | highlightText: searchVm.searchText"></span>
                                <span ng-if="searchVm.active === materialInspectionPlanTitle"
                                      ng-bind-html="inspectionPlan.materialName | highlightText: searchVm.searchText"></span>
                            </td>
                            <td title="{{inspectionPlan.name}}" class="col-width-200">
                                <span ng-bind-html="inspectionPlan.name  | highlightText: searchVm.searchText"></span>
                                <%--{{inspectionPlan.name.length > 20 ? '...' : ''}}--%>
                            </td>
                            <td style="width: 150px;text-align: center;">{{inspectionPlan.revision}}</td>
                            <td class="col-width-100">
                                <item-status item="inspectionPlan"></item-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table"
                     ng-if="searchVm.active === itemInspectionTitle || searchVm.active === materialInspectionTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-150" translate>INSPECTION_NUMBER</th>
                            <th class="col-width-200" translate>INSPECTION_PLAN</th>
                            <th class="col-width-150" ng-if="searchVm.active === itemInspectionTitle"
                                style="width: 150px;" translate>
                                PRODUCT
                            </th>
                            <th class="col-width-150" ng-if="searchVm.active === materialInspectionTitle"
                                style="width: 150px;" translate>
                                MATERIAL
                            </th>
                            <th class="col-width-150" translate>ASSIGNED_TO</th>
                            <th class="col-width-100" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.inspections.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/inspections.png" alt="" class="image">

                                    <div class="message" translate>NO_INSPECTIONS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="inspection in searchVm.searchResults.inspections.content">
                            <td class="col-width-150">
                                <a href=""
                                   ng-click="showInspectionDetails(inspection)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="inspection.inspectionNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="inspection.inspectionPlan | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">
                                    <span ng-if="searchVm.active === itemInspectionTitle"
                                          ng-bind-html="inspection.productName  | highlightText: searchVm.searchText"></span>
                                    <span ng-if="searchVm.active === materialInspectionTitle"
                                          ng-bind-html="inspection.materialName | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">{{inspection.assignedTo}}</td>
                            <td class="col-width-100">
                                <workflow-status workflow="inspection"></workflow-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === problemReportTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 20px;"></th>
                            <th class="col-width-100" translate>PR_NUMBER</th>
                            <th class="col-width-100" translate>PRODUCT</th>
                            <th class="col-width-200" translate>PROBLEM</th>
                            <th class="col-width-150" translate>DEFECT_TYPE</th>
                            <th class="col-width-100" translate>SEVERITY</th>
                            <th class="col-width-100" translate>REPORTER_TYPE</th>
                            <th style="width: 150px" translate>REPORTED_BY</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.problemReports.content.length == 0">
                            <td colspan="8" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/problemReport.png" alt="" class="image">

                                    <div class="message" translate>NO_PRS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="problemReport in searchVm.searchResults.problemReports.content">
                            <td style="width: 20px;">
                                <i class="fa flaticon-prize3" title="Implemented"
                                   ng-if="problemReport.isImplemented"></i>
                            </td>
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="showPrDetails(problemReport)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="problemReport.prNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="problemReport.product | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="problemReport.problem  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="problemReport.failureType  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="problemReport.severity | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <reporter-type reporter-type="problemReport.reporterType"></reporter-type>
                            </td>
                            <td>{{problemReport.reportedBy || problemReport.otherReported}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === ncrTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 20px;"></th>
                            <th class="col-width-100" translate>NCR_NUMBER</th>
                            <th class="col-width-100" translate>NCR_TYPE</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-100" translate>DEFECT_TYPE</th>
                            <th class="col-width-100" translate>SEVERITY</th>
                            <th class="col-width-100" translate>REPORTED_BY</th>
                            <th class="col-width-150" translate>REPORTED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.ncrs.content.length == 0">
                            <td colspan="8" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/NCR.png" alt="" class="image">

                                    <div class="message" translate>NO_NCRS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="ncr in searchVm.searchResults.ncrs.content">
                            <td style="width: 20px;">
                                <i class="fa flaticon-prize3" title="Impletemented" ng-if="ncr.isImplemented"></i>
                            </td>
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="showNcrDetails(ncr)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="ncr.ncrNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="ncr.ncrType | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200" title="{{ncr.title}}">
                                <span ng-bind-html="ncr.title | highlightText: searchVm.searchText"></span>
                            </td>
                            <td title="{{ncr.failureType}}" class="col-width-100">
                                <span ng-bind-html="ncr.failureType | highlightText: searchVm.searchText"></span>
                            </td>
                            <td title="{{ncr.severity}}" class="col-width-100">
                                <span ng-bind-html="ncr.severity | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{ncr.reportedBy}}</td>
                            <td class="col-width-150">{{ncr.reportedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === qcrTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 20px;"></th>
                            <th class="col-width-100" translate>QCR_NUMBER</th>
                            <th class="col-width-100" translate>QCR Type</th>
                            <th class="col-width-100" translate>QCR For</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-100" translate>Quality Administrator</th>
                            <th class="col-width-100" translate>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.qcrs.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/QCR.png" alt="" class="image">

                                    <div class="message" translate>NO_QCRS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="qcr in searchVm.searchResults.qcrs.content">
                            <td style="width: 20px;">
                                <i class="fa flaticon-prize3" ng-if="qcr.isImplemented" title="Implemented"></i>
                            </td>
                            <td class="col-width-100">
                                <a href="" ng-click="showQcrDetails(qcr)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="qcr.qcrNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="qcr.qcrType | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{qcr.qcrFor}}</td>
                            <td class="col-width-200">
                                <span ng-bind-html="qcr.title  | highlightText: searchVm.searchText"></span>
                                <%--{{qcr.title.length > 22 ? '...' : ''}}--%>
                            </td>
                            <td class="col-width-100">{{qcr.qualityAdministrator}}</td>
                            <td class="col-width-100">
                                <workflow-status workflow="qcr"></workflow-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === ppapTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 100px" translate>PPAP Number</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-200" translate>PPAP Type</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="width: 150px" translate>SUPPLIER</th>
                            <th style="width: 150px" translate>Supplier Part</th>
                            <th style="width: 150px" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.qcrs.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/QCR.png" alt="" class="image">

                                    <div class="message" translate>NO_PPAPS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="ppap in searchVm.searchResults.ppaps.content">
                            <td>
                                <a href="" ng-click="searchVm.showPpap(ppap)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="ppap.number | highlightText: freeTextQuery"></span>
                                </a>
                            </td>
                            <td class="col-width-200" title="{{ppap.title}}">
                                <span ng-bind-html="ppap.name  | highlightText: freeTextQuery"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="ppap.objectType | highlightText: freeTextQuery"></span>
                            </td>

                            <td title="{{ppap.description}}" class="col-width-250">
                                <span ng-bind-html="ppap.description  | highlightText: freeTextQuery"></span>
                            </td>
                            <td class="col-width-250" ng-click="allPpapVm.showSuppierDetails(ppap)">
                                <span ng-bind-html="ppap.supplierName  | highlightText: freeTextQuery"></span>
                            </td>
                            <td class="col-width-250" ng-click="allPpapVm.showMfrPartDetails(ppap.mfrPart)">
                                <span ng-bind-html="ppap.mfrPart.partName  | highlightText: freeTextQuery"></span>
                            </td>
                            <td>
                                <ppap-status object="ppap"></ppap-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === supplierAuditTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 100px" translate>NUMBER</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-200" translate>AUDIT_TYPE</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="width: 150px" translate>ASSIGNED_TO</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.qcrs.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/QCR.png" alt="" class="image">

                                    <div class="message" translate>NO_PPAPS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="supplierAudit in searchVm.searchResults.supplierAudits.content">
                            <td>
                                <a href="" ng-click="searchVm.showSupplierAudit(supplierAudit)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="supplierAudit.number | highlightText: freeTextQuery"></span>
                                </a>
                            </td>
                            <td class="col-width-200" title="{{supplierAudit.title}}">
                                <span ng-bind-html="supplierAudit.name  | highlightText: freeTextQuery"></span>
                            </td>

                            <td title="{{supplierAudit.auditType}}" class="col-width-250">
                                <span ng-bind-html="supplierAudit.type.name  | highlightText: freeTextQuery"></span>
                            </td>
                            <td title="{{supplierAudit.description}}" class="col-width-250">
                                <span ng-bind-html="supplierAudit.description  | highlightText: freeTextQuery"></span>
                            </td>
                            <td title="{{supplierAudit.assignedTo}}" class="col-width-250">
                                <span ng-bind-html="supplierAudit.assignedToName  | highlightText: freeTextQuery"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === ecrTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 20px;"></th>
                            <th class="col-width-100" translate>ECR_NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                            <th class="col-width-100" translate>URGENCY</th>
                            <th class="col-width-100" translate>REQUESTER_TYPE</th>
                            <th class="col-width-100" translate>REQUESTED_BY</th>
                            <th class="col-width-150" translate>REQUESTED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.ecrs.content.length == 0">
                            <td colspan="9" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/ECR.png" alt="" class="image">

                                    <div class="message" translate>NO_ECRS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="ecr in searchVm.searchResults.ecrs.content">
                            <td style="width: 20px;">
                                <i class="fa fa-ils nav-icon-font" title="Implemented"
                                   ng-if="ecr.isImplemented"></i>
                            </td>
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="showEcrDetails(ecr)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="ecr.crNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="ecr.type | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="ecr.title | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250">
                                <span ng-bind-html="ecr.reasonForChange | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <%--<dcr-urgency dcr="ecr"></dcr-urgency>--%>
                                {{ecr.urgency}}
                            </td>
                            <td>
                                <reporter-type reporter-type="ecr.requesterType"></reporter-type>
                            </td>
                            <td class="col-width-100">{{ecr.requestedBy}}</td>
                            <td class="col-width-150">{{ecr.requestedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === dcoTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                            <th class="col-width-100" translate>CHANGE_ANALYST</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th class="col-width-150" translate>RELEASED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.dcos.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/DCO.png" alt="" class="image">

                                    <div class="message" translate>NO_DCOS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="dco in searchVm.searchResults.dcos.content">
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="showDcoDetails(dco)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="dco.dcoNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="dco.dcoType | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="dco.title | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{dco.reasonForChange}}">
                                <span ng-bind-html="dco.reasonForChange  | highlightText: searchVm.searchText"></span>
                                <%--{{dco.reasonForChange.length > 22 ? '...' : ''}}--%>
                            </td>
                            <td class="col-width-100">{{dco.changeAnalyst}}</td>
                            <td class="col-width-100">
                                <workflow-status workflow="dco"></workflow-status>
                            </td>
                            <td class="col-width-150">{{dco.releasedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === dcrTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 10px"></th>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                            <th class="col-width-100" translate>URGENCY</th>
                            <th class="col-width-100" translate>REQUESTED_BY</th>
                            <th class="col-width-150" translate>APPROVED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.dcrs.content.length == 0">
                            <td colspan="8" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/DCR.png" alt="" class="image">

                                    <div class="message" translate>NO_DCRS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="dcr in searchVm.searchResults.dcrs.content">
                            <td style="width: 10px">
                            <span ng-if=dcr.isImplemented title="DCR Implemented">
                                <i class="fa flaticon-prize3 nav-icon-font"></i>
                            </span>
                            </td>
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="showDcrDetails(dcr)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="dcr.crNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100"><span
                                    ng-bind-html="dcr.crType | highlightText: searchVm.searchText"></span>
                            </td>
                            <td title="{{dcr.title}}" class="col-width-200">
                                <span ng-bind-html="dcr.title  | highlightText: searchVm.searchText"></span>
                                <%--{{dcr.title.length > 20 ? '...' : ''}}--%>
                            </td>
                            <td title="{{dcr.reasonForChange}}" class="col-width-250">
                                <span ng-bind-html="dcr.reasonForChange  | highlightText: searchVm.searchText"></span>
                                <%--{{dcr.reasonForChange.length > 20 ? '...' : ''}}--%>
                            </td>
                            <td class="col-width-100">
                                <%--<dcr-urgency dcr="dcr"></dcr-urgency>--%>
                                {{dcr.urgency}}
                            </td>
                            <td class="col-width-100">{{dcr.requestedBy}}</td>
                            <td class="col-width-150">{{dcr.approvedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === mcoTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-250" translate>REASON_FOR_CHANGE</th>
                            <th class="col-width-100" translate>CHANGE_ANALYST</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th class="col-width-150" translate>RELEASED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.mcos.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/MCO.png" alt="" class="image">

                                    <div class="message" translate>NO_MCOS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="mco in searchVm.searchResults.mcos.content">
                            <td class="col-width-100">
                                <a href="" ng-click="showMcoDetails(mco)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="mco.mcoNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="mco.mcoType.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200" title="{{mco.title}}">{{mco.title}}</td>
                            <td class="col-width-250" title="{{mco.reasonForChange}}">
                                <span ng-bind-html="mco.reasonForChange | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{mco.changeAnalystObject.fullName}}</td>
                            <td class="col-width-100">
                                <workflow-status workflow="mco"></workflow-status>
                            </td>
                            <td class="col-width-150">{{mco.releasedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === deviationTitle || searchVm.active === waiverTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 30px" translate></th>
                            <th class="col-width-100" ng-if="searchVm.active == deviationTitle" translate>
                                DEVIATION_NUMBER
                            </th>
                            <th class="col-width-100" ng-if="searchVm.active == waiverTitle" translate>
                                WAIVER_NUMBER
                            </th>
                            <th class="col-width-200" translate>TITLE</th>
                            <th class="col-width-100" translate>EFFECTIVE_TYPE</th>
                            <th class="col-width-250" translate>REASON_FOR_VARIANCE</th>
                            <th class="col-width-250" translate>REQUIREMENT_DEVIATION</th>
                            <th class="col-width-100" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.variances.content.length == 0">
                            <td colspan="8" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Varience.png" alt="" class="image">

                                    <div class="message" translate>NO_VARIANCES</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="variance in searchVm.searchResults.variances.content">
                            <td style="width: 30px;">
                                <span ng-if="variance.recurring == true">
                                    <i class="fa fa-repeat" aria-hidden="true"></i>
                                </span>
                            </td>
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="showVarianceDetails(variance)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="variance.varianceNumber | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="variance.title | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{variance.effecitivityType}}</td>
                            <td class="col-width-250">{{variance.reasonForVariance}}
                            </td>
                            <td class="col-width-250">{{variance.requirementDeviation}}
                            </td>
                            <td class="col-width-100">
                                <workflow-status workflow="variance"></workflow-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === jigsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.jigs.content.length == 0">
                            <td colspan="4" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/JigsAndFixtures.png" alt="" class="image">

                                    <div class="message" translate>NO_JIGS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="jigFixture in searchVm.searchResults.jigs.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showJigsFixture(jigFixture)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="jigFixture.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="jigFixture.type | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="jigFixture.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{jigFixture.description}}">
                                <span ng-bind-html="jigFixture.description | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === fixturesTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.fixtures.content.length == 0">
                            <td colspan="4" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/JigsAndFixtures.png" alt="" class="image">

                                    <div class="message" translate>NO_FIXTURES</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="jigFixture in searchVm.searchResults.fixtures.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showJigsFixture(jigFixture)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="jigFixture.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="jigFixture.type | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="jigFixture.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{jigFixture.description}}">
                                <span ng-bind-html="jigFixture.description | highlightText: searchVm.searchText"></span>

                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === plantsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-150" translate>ADDRESS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.plants.content.length == 0">
                            <td colspan="5" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Plant.png" alt="" class="image">

                                    <div class="message" translate>NO_PLANTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="plant in searchVm.searchResults.plants.content">
                            <td class="col-width-100">
                                <a href="" ng-click="showPlanDetails(plant)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="plant.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="plant.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="plant.name | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-250" title="{{plant.description}}">
                                <span ng-bind-html="plant.description | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-150">
                                <span ng-bind-html="plant.address | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === assemblyLineTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-150" translate>NUMBER</th>
                            <th class="col-width-200" translate>TYPE</th>
                            <th class="col-width-250" translate>NAME</th>
                            <th class="col-width-200" translate>PLANT</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="width: 150px" translate>MODIFIED_BY</th>
                            <th style="width: 150px" translate>MODIFIED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="allAssemblyLineVm.loading == false && allAssemblyLineVm.assemblyLines.content.length == 0">
                            <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/AssemblyLines.png" alt="" class="image">

                                    <div class="message" translate>NO_ASSEMBLY_LINES</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-repeat="assemblyLine in searchVm.searchResults.assemblyLines.content">
                            <td class="col-width-150">
                                <a href="" ng-click="searchVm.showAssemblyLine(assemblyLine)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="assemblyLine.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="assemblyLine.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250">
                                <span ng-bind-html="assemblyLine.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">{{assemblyLine.plantName}}</td>
                            <td class="col-width-250" title="{{assemblyLine.description}}"><span
                                    ng-bind-html="assemblyLine.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td>{{assemblyLine.modifiedByObject.fullName}}</td>
                            <td>{{assemblyLine.modifiedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === workCentersTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-150" translate>PLANT</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-200" translate>LOCATION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.workCenters.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Plant.png" alt="" class="image">

                                    <div class="message" translate>NO_PLANTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="workCenter in searchVm.searchResults.workCenters.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showWorkCenter(workCenter)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="workCenter.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="workCenter.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200" title="{{workCenter.name}}">
                                <span ng-bind-html="workCenter.name  | highlightText: searchVm.searchText"></span>
                                <%--{{workCenter.name.length > 21 ? '...' : ''}}--%>
                            </td>
                            <td class="col-width-150">{{workCenter.plantName}}</td>
                            <td class="col-width-250" title="{{workCenter.description}}">
                                <span ng-bind-html="workCenter.description | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200" title="{{workCenter.location}}">
                                <span ng-bind-html="workCenter.location  | highlightText: searchVm.searchText"></span>
                                <%--{{workCenter.location.length > 21 ? '...' : ''}}--%>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="results-table" ng-if="searchVm.active === machinesTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th style="width: 150px" translate>REQUIRESMAINTENANCE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.machines.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Machine.png" alt="" class="image">

                                    <div class="message" translate>NO_MACHINES</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="machine in searchVm.searchResults.machines.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showMachine(machine)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="machine.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>

                            <td class="col-width-100">
                                <span ng-bind-html="machine.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="machine.name | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-250" title="{{machine.description}}">
                                <span ng-bind-html="machine.description  | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">
                                <span ng-if="machine.active == true" class="label label-outline bg-light-success"
                                      translate>C_ACTIVE</span>
                                <span ng-if="machine.active == false" class="label label-outline bg-light-danger"
                                      translate>C_INACTIVE</span>
                            </td>
                            <td style="width: 150px;">
                                 <span ng-if="machine.requiresMaintenance == true"
                                       class="label label-outline bg-light-success"
                                       translate>YES</span>
                                 <span ng-if="machine.requiresMaintenance == false"
                                       class="label label-outline bg-light-danger"
                                       translate>NO</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="results-table" ng-if="searchVm.active === equipmentsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th style="width: 150px" translate>REQUIRESMAINTENANCE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.equipments.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Equipment.png" alt="" class="image">

                                    <div class="message" translate>NO_EQUIPMENTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="equipment in searchVm.searchResults.equipments.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showEquipment(equipment)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="equipment.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="equipment.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="equipment.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{equipment.description}}">
                                {{equipment.description}}
                            </td>

                            <td class="col-width-100">
                                <span ng-if="equipment.active == true" class="label label-outline bg-light-success"
                                      translate>C_ACTIVE</span>
                                <span ng-if="equipment.active == false" class="label label-outline bg-light-danger"
                                      translate>C_INACTIVE</span>
                            </td>
                            <td style="width: 150px;">
                                 <span ng-if="equipment.requiresMaintenance == true"
                                       class="label label-outline bg-light-success"
                                       translate>YES</span>
                                 <span ng-if="equipment.requiresMaintenance == false"
                                       class="label label-outline bg-light-danger"
                                       translate>NO</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="results-table" ng-if="searchVm.active === instrumentsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>STATUS</th>
                            <th style="width: 150px" translate>REQUIRESMAINTENANCE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.instruments.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Instrument.png" alt="" class="image">

                                    <div class="message" translate>NO_INSTRUMENTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="instrument in searchVm.searchResults.instruments.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showInstrument(instrument)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="instrument.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>

                            <td class="col-width-100">
                                <span ng-bind-html="instrument.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="instrument.name | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-250" title="{{instrument.description}}">
                                <span ng-bind-html="instrument.description  | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">
                                <span ng-if="instrument.active == true" class="label label-outline bg-light-success"
                                      translate>C_ACTIVE</span>
                                <span ng-if="instrument.active == false" class="label label-danger"
                                      translate>C_INACTIVE</span>
                            </td>
                            <td style="width: 150px;">
                                 <span ng-if="instrument.requiresMaintenance == true"
                                       class="label label-outline bg-light-success"
                                       translate>YES</span>
                                 <span ng-if="instrument.requiresMaintenance == false"
                                       class="label label-outline bg-light-danger"
                                       translate>NO</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="results-table" ng-if="searchVm.active === toolsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.tools.content.length == 0">
                            <td colspan="4" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Tools.png" alt="" class="image">

                                    <div class="message" translate>NO_TOOLS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="tool in searchVm.searchResults.tools.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showTool(tool)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="tool.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="tool.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="tool.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{tool.description}}">
                                <span ng-bind-html="tool.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === materialsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>Measurement</th>
                            <th class="col-width-150" translate>Measurement Unit</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.materials.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Materials.png" alt="" class="image">

                                    <div class="message" translate>NO_MATERIALS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="material in searchVm.searchResults.materials.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showMaterial(material)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="material.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="material.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="material.name | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-250" title="{{material.description}}">
                                <span ng-bind-html="material.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{material.qom}}</td>
                            <td class="col-width-150">{{material.uom}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === manpowersTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.manpowers.content.length == 0">
                            <td colspan="4" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Manpower.png" alt="" class="image">

                                    <div class="message" translate>NO_MANPOWER</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="manpower in searchVm.searchResults.manpowers.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showManpower(manpower)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="manpower.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="manpower.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="manpower.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{manpower.description}}">
                                <span ng-bind-html="manpower.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="results-table" ng-if="searchVm.active === shiftsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>START_TIME</th>
                            <th class="col-width-100" translate>END_TIME</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.shifts.content.length == 0">
                            <td colspan="5" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Manpower.png" alt="" class="image">

                                    <div class="message" translate>NO_MANPOWER</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="shift in searchVm.searchResults.shifts.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showShift(shift)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="shift.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="shift.name | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-250" title="{{shift.description}}">
                                <span ng-bind-html="shift.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="shift.startTime | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">
                                <span ng-bind-html="shift.endTime | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === operationsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.loading == true">
                            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_OPERATIONS</span>
                        </span>
                            </td>
                        </tr>

                        <tr ng-if="searchVm.loading == false && searchVm.searchResults.operations.content.length == 0">
                            <td colspan="4" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Operations.png" alt="" class="image">

                                    <div class="message" translate>NO_OPERATIONS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="operation in searchVm.searchResults.operations.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showOperation(operation)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="operation.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>

                            <td class="col-width-100">
                                <span ng-bind-html="operation.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="operation.name | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-250" title="{{operation.description}}">
                                <span ng-bind-html="operation.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === manufacturerTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-200" translate>MANUFACTURER_NAME</th>
                            <th class="col-width-150" translate>MANUFACTURER_TYPE</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>LIFECYCLE</th>
                            <th class="col-width-100" translate>PHONE_NUMBER</th>
                            <th class="col-width-100" translate>CONTACT_PERSON</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.loading == false && searchVm.searchResults.manufacturers.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Manufacturers.png" alt="" class="image">

                                    <div class="message" translate>NO_MANUFACTURERS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="manufacturer in searchVm.searchResults.manufacturers.content">
                            <td class="col-width-200">
                                <a href=""
                                   ng-click="searchVm.showMfrDetails(manufacturer.id)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="manufacturer.name  | highlightText: searchVm.searchText"></span>
                                    <%--{{manufacturer.name.length > 20 ? '...' : ''}}--%>
                                </a>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="manufacturer.mfrType.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250">
                                <span title="{{manufacturer.description}}">
                                    <span ng-bind-html="manufacturer.description  | highlightText: searchVm.searchText"></span>
                                </span>
                            </td>
                            <td class="col-width-100">
                                <item-status item="manufacturer"></item-status>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="manufacturer.phoneNumber | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="manufacturer.contactPerson | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === manufacturerPartTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>PART_NUMBER</th>
                            <th class="col-width-200" translate>MANUFACTURER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>PART_NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>LIFECYCLE</th>
                            <th class="col-width-100" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.loading == false && searchVm.searchResults.manufacturerParts.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                                    <div class="message" translate>NO_MANUFACTURER_PARTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="mfrPart in searchVm.searchResults.manufacturerParts.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showMfrPartDetails(mfrPart)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"><span
                                        ng-bind-html="mfrPart.partNumber  | highlightText: searchVm.searchText"></span></a>
                            </td>
                            <td class="col-width-200">
                                <a href="" ng-click="searchVm.showMfrDetails(mfrPart.manufacturer)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="mfrPart.mfrName  | highlightText: searchVm.searchText"></span>
                                    <%--{{mfrPart.mfrName.length > 20 ? '...' : ''}}--%>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="mfrPart.mfrPartType.name  | highlightText: searchVm.searchText"></span>
                                <%--{{mfrPart.mfrPartType.name.length > 20 ? '...' : ''}}--%>
                            </td>
                            <td class="col-width-200"><span
                                    ng-bind-html="mfrPart.partName  | highlightText: searchVm.searchText"></span>
                                <%--{{mfrPart.partName.length > 20 ? '...' : ''}}--%>
                            </td>
                            <td class="col-width-250">
                                <span title="{{mfrPart.description}}">
                                    <span ng-bind-html="mfrPart.description  | highlightText: searchVm.searchText"></span>
                                </span>
                            </td>
                            <td class="col-width-100">
                                <item-status item="mfrPart"></item-status>
                            </td>
                            <td class="col-width-100">{{mfrPart.status}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === supplierTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-150" translate>ADDRESS</th>
                            <th class="col-width-150" translate>CONTACT_PERSON</th>
                            <th class="col-width-100" translate>PHONE_NUMBER</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.suppliers.content.length == 0">
                            <td colspan="5" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Supplier.png" alt="" class="image">

                                    <div class="message" translate>NO_SUPPLIERS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="supplier in searchVm.searchResults.suppliers.content">
                            <td class="col-width-200">
                                <a href="" ng-click="searchVm.showSupplier(supplier)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="supplier.name | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="supplier.supplierType.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">
                                <span ng-bind-html="supplier.address | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-150">{{supplier.contactPersonObject.fullName}}</td>
                            <td class="col-width-100">{{supplier.contactPersonObject.phoneMobile}}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === assetTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>RESOURCE_TYPE</th>
                            <th class="col-width-200" translate>RESOURCE</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th style="width: 100px;text-align: center;" translate>METERED</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="allAssetsVm.loading == false && allAssetsVm.assets.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Asset.png" alt="" class="image">

                                    <div class="message" translate>NO_ASSETS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="asset in searchVm.searchResults.assets.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showAsset(asset)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="asset.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>

                            <td class="col-width-100">
                                <span ng-bind-html="asset.resourceObject.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="asset.resourceObject.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="asset.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="asset.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{asset.description}}">
                                <span ng-bind-html="asset.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td style="width: 100px;text-align: center;">
                                <span ng-if="asset.metered == true" class="label label-outline bg-light-success"
                                      translate>YES</span>
                                <span ng-if="asset.metered == false" class="label label-outline bg-light-danger"
                                      translate>NO</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === meterTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>METER_READING_TYPE</th>
                            <th class="col-width-100" translate>METER_TYPE</th>
                            <th class="col-width-100" translate>MEASUREMENT</th>
                            <th class="col-width-100" translate>MEASUREMENT_UNIT</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.meters.content.length == 0">
                            <td colspan="8" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Meter.png" alt="" class="image">

                                    <div class="message" translate>NO_METERS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="meter in searchVm.searchResults.meters.content">

                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showMeter(meter)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="meter.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>

                            <td class="col-width-100">
                                <span ng-bind-html="meter.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="meter.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{meter.description}}">
                                <span ng-bind-html="meter.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <reading-type object="meter"></reading-type>
                            </td>
                            <td class="col-width-100">
                                <meter-type object="meter"></meter-type>
                            </td>
                            <td class="col-width-100">{{meter.measurementName}}</td>
                            <td class="col-width-100">{{meter.unitName}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === sparePartTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>MODIFIED_BY</th>
                            <th class="col-width-150" translate>MODIFIED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.spareParts.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/SpareParts.png" alt="" class="image">

                                    <div class="message" translate>NO_SPARE_PARTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="sparePart in searchVm.searchResults.spareParts.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showSparePart(sparePart)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="sparePart.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="sparePart.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="sparePart.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{sparePart.description}}">
                                <span ng-bind-html="sparePart.description  | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">{{sparePart.modifiedByObject.firstName}}</td>
                            <td class="col-width-150">{{sparePart.modifiedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === maintenancePlanTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-100" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>ASSET</th>
                            <th class="col-width-100" translate>MODIFIED_BY</th>
                            <th class="col-width-150" translate>MODIFIED_DATE</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.maintenancePlans.content.length == 0">
                            <td colspan="6" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Maintenance.png" alt="" class="image">

                                    <div class="message" translate>NO_MAINTENANCE_PLANS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="maintenancePlan in searchVm.searchResults.maintenancePlans.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showMaintenancePlan(maintenancePlan)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="maintenancePlan.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="maintenancePlan.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{maintenancePlan.description}}">
                                <span ng-bind-html="maintenancePlan.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{maintenancePlan.assetName}}</td>
                            <td class="col-width-100">{{maintenancePlan.modifiedByName}}</td>
                            <td class="col-width-150">{{maintenancePlan.modifiedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === workRequestTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-100" translate>ASSET</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-100" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>REQUESTOR</th>
                            <th class="col-width-100" translate>PRIORITY</th>
                            <th class="col-width-100" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.workRequests.content.length == 0">
                            <td colspan="8" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/WorkRequest.png" alt="" class="image">

                                    <div class="message" translate>NO_WORK_REQUESTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="workRequest in searchVm.searchResults.workRequests.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showWorkRequest(workRequest)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="workRequest.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="workRequest.type | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                {{workRequest.asset}}
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="workRequest.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{workRequest.description}}">
                                <span ng-bind-html="workRequest.description  | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">
                                {{workRequest.requestor}}
                            </td>

                            <td class="col-width-100">
                                <priority object="workRequest"></priority>
                            </td>

                            <td class="col-width-100">
                                <wo-status object="workRequest"></wo-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === workOrderTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>ASSET</th>
                            <th class="col-width-150" translate>REQUEST_PLAN</th>
                            <th class="col-width-100" translate>ASSIGNED_TO</th>
                            <th class="col-width-100" translate>PRIORITY</th>
                            <th class="col-width-100" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.workOrders.content.length == 0">
                            <td colspan="9" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/WorkOrder.png" alt="" class="image">

                                    <div class="message" translate>NO_WORK_ORDERS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="workOrder in searchVm.searchResults.workOrders.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showWorkOrder(workOrder)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="workOrder.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="workOrder.typeName | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="workOrder.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{workOrder.description}}">
                                <span ng-bind-html="workOrder.description  | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">{{workOrder.assetName}}</td>
                            <td class="col-width-150">
                                <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-click="searchVm.showWoMaintenancePlan(workOrder)">{{workOrder.planNumber}}</a>
                                <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                                   ng-click="searchVm.showWoWorkRequest(workOrder)">{{workOrder.requestNumber}}</a>
                            </td>
                            <td class="col-width-100">{{workOrder.assignedToName}}</td>
                            <td class="col-width-100">
                                <priority object="workOrder"></priority>
                            </td>
                            <td class="col-width-100">
                                <wo-status object="workOrder"></wo-status>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === substanceTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-100" translate>CAS Number</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.pgcSubstances.content.length == 0">
                            <td colspan="9" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Substance.png" alt="" class="image">

                                    <div class="message" translate>NO_SUBSTANCES</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="substance in searchVm.searchResults.pgcSubstances.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showSubstance(substance)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="substance.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="substance.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="substance.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="substance.casNumber | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{substance.description}}">
                                <span ng-bind-html="substance.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === pgcSpecificationTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>STATUS</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.pgcSpecifications.content.length == 0">
                            <td colspan="9" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Specifications.png" alt="" class="image">

                                    <div class="message" translate>NO_SPECIFICATIONS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="specification in searchVm.searchResults.pgcSpecifications.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showSpecification(specification)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="specification.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="specification.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="specification.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{specification.description}}">
                                <span ng-bind-html="specification.description  | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">
                                <span ng-if="specification.active == true" class="label label-outline bg-light-success"
                                      translate>C_ACTIVE</span>
                                <span ng-if="specification.active == false" class="label label-outline bg-light-danger"
                                      translate>C_INACTIVE</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === pgcDeclarationTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-100" translate>NUMBER</th>
                            <th class="col-width-100" translate>TYPE</th>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                            <th class="col-width-100" translate>SUPPLIER</th>
                            <th class="col-width-100" translate>CONTACT</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.pgcDeclarations.content.length == 0">
                            <td colspan="9" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Declaration.png" alt="" class="image">

                                    <div class="message" translate>NO_DECLARATIONS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="declaration in searchVm.searchResults.pgcDeclarations.content">
                            <td class="col-width-100">
                                <a href="" ng-click="searchVm.showDeclaration(declaration)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="declaration.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100">
                                <span ng-bind-html="declaration.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-200">
                                <span ng-bind-html="declaration.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{declaration.description}}">
                                <span ng-bind-html="declaration.description  | highlightText: searchVm.searchText"></span>
                            </td>

                            <td class="col-width-100">
                                {{declaration.supplierName}}
                            </td>
                            <td class="col-width-100">
                                {{declaration.supplierContactName}}
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === vaultsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.pdmVaults.content.length == 0">
                            <td colspan="2" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/cloud-storage.png" alt="" class="image">

                                    <div class="message" translate>NO_VAULTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="vault in searchVm.searchResults.pdmVaults.content">
                            <td class="col-width-200">
                                <span ng-bind-html="vault.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{vault.description}}">
                                <span ng-bind-html="vault.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === assembliesTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.pdmAssemblies.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Assembly.png" alt="" class="image">

                                    <div class="message" translate>NO_ASSEMBLIES_MESSAGE</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-repeat="assembly in searchVm.searchResults.pdmAssemblies.content">
                            <td class="col-width-200">
                                <span ng-bind-html="assembly.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{assembly.description}}">
                                <span ng-bind-html="assembly.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === partsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.pdmParts.content.length == 0">
                            <td colspan="7" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Parts.png" alt="" class="image">

                                    <div class="message" translate>NO_PDM_PARTS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="part in searchVm.searchResults.pdmParts.content">
                            <td class="col-width-200">
                                <span ng-bind-html="part.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{part.description}}">
                                <span ng-bind-html="part.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="results-table" ng-if="searchVm.active === drawingsTitle"
                     style="padding-top: 10px;">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th class="col-width-200" translate>NAME</th>
                            <th class="col-width-250" translate>DESCRIPTION</th>
                        </tr>
                        </thead>
                        <tbody>

                        <tr ng-if="searchVm.searchResults.pdmDrawings.content.length == 0">
                            <td colspan="2" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Drawings.png" alt="" class="image">

                                    <div class="message" translate>NO_DRAWINGS</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-repeat="drawing in searchVm.searchResults.pdmDrawings.content">
                            <td class="col-width-200">
                                <span ng-bind-html="drawing.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{drawing.description}}">
                                <span ng-bind-html="drawing.description  | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <%----------------------------  CustomObjects ----------------------%>
                <div class="results-table" ng-if="searchVm.active === customObjectTitle" style="padding-top: 10px;">
                    <table class="table table-striped highlight-row" style="width: 100% !important;">
                        <thead>
                        <tr>
                            <th class="col-width-100"><span translate>NUMBER</span></th>
                            <th class="col-width-100"><span translate>TYPE</span></th>
                            <th class="col-width-100"><span translate>NAME</span></th>
                            <th class="col-width-250"><span translate>DESCRIPTION</span></th>

                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="searchVm.searchResults.customObjects.content.length == 0">
                            <td colspan="4" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Requirements.png" alt="" class="image">

                                    <div class="message" translate>NO_RESULTS_FOUND</div>
                                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                        NO_PERMISSION_MESSAGE
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr ng-if="searchVm.searchResults.customObjects.content.length > 0"
                            ng-repeat="customObject in searchVm.searchResults.customObjects.content">
                            <td class="col-width-100">
                                <a href=""
                                   ng-click="searchVm.openCustomObjectDetails(customObject)"
                                   title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                    <span ng-bind-html="customObject.number | highlightText: searchVm.searchText"></span>
                                </a>
                            </td>
                            <td class="col-width-100"><span
                                    ng-bind-html="customObject.type.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-100"><span
                                    ng-bind-html="customObject.name | highlightText: searchVm.searchText"></span>
                            </td>
                            <td class="col-width-250" title="{{customObject.description}}"><span
                                    ng-bind-html="customObject.description | highlightText: searchVm.searchText"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>


                <%----------------------------  Table footer --------------------------%>
                <div class="table-footer">
                    <table-footer objects="searchVm.searchResults.items" ng-if="searchVm.active == itemTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.projects" ng-if="searchVm.active == projectTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.programs" ng-if="searchVm.active == programTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.ecos" ng-if="searchVm.active == ecoTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.requirements"
                                  ng-if="searchVm.active == requirementTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.workflows" ng-if="searchVm.active == workflowTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.inspectionPlans"
                                  ng-if="searchVm.active == productInspectionPlanTitle || searchVm.active == materialInspectionPlanTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.inspections"
                                  ng-if="searchVm.active == itemInspectionTitle || searchVm.active == materialInspectionTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.problemReports"
                                  ng-if="searchVm.active == problemReportTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.ncrs"
                                  ng-if="searchVm.active == ncrTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.qcrs"
                                  ng-if="searchVm.active == qcrTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse">
                    </table-footer>
                    <table-footer objects="searchVm.searchResults.ppaps"
                                  ng-if="searchVm.active == ppapTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.supplierAudits"
                                  ng-if="searchVm.active == supplierAuditTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.ecrs"
                                  ng-if="searchVm.active == ecrTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.dcos"
                                  ng-if="searchVm.active == dcoTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.dcrs"
                                  ng-if="searchVm.active == dcrTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.mcos"
                                  ng-if="searchVm.active == mcoTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.variances"
                                  ng-if="searchVm.active == deviationTitle || searchVm.active == waiverTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.files"
                                  ng-if="searchVm.active == fileTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.documents"
                                  ng-if="searchVm.active == documentTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.plants"
                                  ng-if="searchVm.active == plantsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.assemblyLines"
                                  ng-if="searchVm.active == assemblyLineTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.workCenters"
                                  ng-if="searchVm.active == workCentersTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.machines"
                                  ng-if="searchVm.active == machinesTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.equipments"
                                  ng-if="searchVm.active == equipmentsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.instruments"
                                  ng-if="searchVm.active == instrumentsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.tools"
                                  ng-if="searchVm.active == toolsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.jigs"
                                  ng-if="searchVm.active == jigsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.fixtures"
                                  ng-if="searchVm.active == fixturesTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.materials"
                                  ng-if="searchVm.active == materialsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.manpowers"
                                  ng-if="searchVm.active == manpowersTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.shifts"
                                  ng-if="searchVm.active == shiftsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.operations"
                                  ng-if="searchVm.active == operationsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse">
                    </table-footer>
                    <table-footer objects="searchVm.searchResults.manufacturers"
                                  ng-if="searchVm.active == manufacturerTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse">
                    </table-footer>
                    <table-footer objects="searchVm.searchResults.manufacturerParts"
                                  ng-if="searchVm.active == manufacturerPartTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.suppliers"
                                  ng-if="searchVm.active == supplierTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.assets"
                                  ng-if="searchVm.active == assetTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.meters"
                                  ng-if="searchVm.active == meterTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.spareParts"
                                  ng-if="searchVm.active == sparePartTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.maintenancePlans"
                                  ng-if="searchVm.active == maintenancePlanTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.workRequests"
                                  ng-if="searchVm.active == workRequestTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.workOrders"
                                  ng-if="searchVm.active == workOrderTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pgcSubstances"
                                  ng-if="searchVm.active == substanceTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pgcSpecifications"
                                  ng-if="searchVm.active == pgcSpecificationTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pgcDeclarations"
                                  ng-if="searchVm.active == pgcDeclarationTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pdmVaults"
                                  ng-if="searchVm.active == vaultsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pdmAssemblies"
                                  ng-if="searchVm.active == assembliesTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pdmParts"
                                  ng-if="searchVm.active == partsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.pdmDrawings"
                                  ng-if="searchVm.active == drawingsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.requirementDocuments"
                                  ng-if="searchVm.active == requirementDocumentTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.customObjects"
                                  ng-if="searchVm.active == customObjectTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.nprs"
                                  ng-if="searchVm.active == nprTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                    <table-footer objects="searchVm.searchResults.tags"
                                  ng-if="searchVm.active == tagsTitle"
                                  pageable="searchVm.pageable" previous-page="searchVm.previousPage"
                                  next-page="searchVm.nextPage" page-size="searchVm.pageSize"
                                  search-response="searchVm.searchResponse"></table-footer>
                </div>
            </div>
        </div>

        <div class="no-reults-container" ng-if="!searchVm.searching && !searchVm.detailsFound">
            <div class="no-results">
                <img src="app/assets/images/no-results-found.png" alt="">

                <div class="no-results-message" translate>NOT_FOUND_ANYTHING</div>
                <div class="no-results-suggestion" translate>TRY_SEARCHING_FOR_SOMETHING</div>
            </div>
        </div>
    </div>

    <div class="search-results-mask-panel" id="search-results-mask-panel"></div>
</div>