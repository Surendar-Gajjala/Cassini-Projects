<style>
    #storageParts-content .responsive-table {
        padding: 10px;
        bottom: 0;
        overflow: auto;
        height: 100%;
    }

    #storageParts-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .storageSearch-box {
        width: 100%;
    }

    .storageSearch-box > input {
        width: 96%;
        border-radius: 15px !important;
        padding-left: 30px;
        height: 30px;
        display: inline-block !important;
        background-color: rgb(241, 243, 244);
        border: 1px solid #ddd;
        box-shadow: none;
    }

    .storageSearch-box > input:hover {
        background-color: rgb(229, 231, 234);
    }

    .storageSearch-box > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    .storageSearch-box i.fa-search {
        z-index: 4 !important;
        position: absolute;
        margin-top: 11px;
        margin-left: 10px;
        color: grey;
        opacity: 0.5;
    }

    .storageSearch-box i.clear-search {
        margin-left: -27px;
        color: gray;
        cursor: pointer;
        z-index: 4 !important;
        position: absolute;
        margin-top: 12px;
    }
</style>
<div id="storageParts-view" style="overflow-y: hidden;">
    <div id="storageParts-search"
         style="display: flex;width: 100%;border-bottom: 1px solid lightgrey;padding-bottom: 3px;">
        <div style="display: inline-flex;width: 50%;padding: 0 10px;">
            <div style="width: 50%" ng-show="storagePartSelectionVm.showResults">
                <h5>Displaying {{storagePartSelectionVm.searchResults.numberOfElements}} of
                    {{storagePartSelectionVm.searchResults.totalElements}}</h5>
            </div>

            <div class="text-right" ng-show="storagePartSelectionVm.showResults" style="width: 50%;padding: 8px;">
                <span class="mr10">Page {{storagePartSelectionVm.searchResults.totalElements != 0 ? storagePartSelectionVm.searchResults.number+1:0}} of {{storagePartSelectionVm.searchResults.totalPages}}</span>
                <a href="" ng-click="storagePartSelectionVm.previousPage()"
                   ng-class="{'disabled': storagePartSelectionVm.searchResults.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="storagePartSelectionVm.nextPage()"
                   ng-class="{'disabled': storagePartSelectionVm.searchResults.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
        <div style="width: 50%;padding: 0 10px;">
            <div class="storageSearch-box">
                <i class="fa fa-search"></i>
                <input type="search" placeholder="Search Parts"
                       class="form-control input-sm"
                       ng-model="storagePartSelectionVm.searchFilter.searchQuery"
                       ng-model-options="{ debounce: 500 }"
                       ng-change="storagePartSelectionVm.performSearch()"
                       ng-enter="storagePartSelectionVm.performSearch()">
                <i class="fa fa-times-circle clear-search" title="Clear Seach"
                   ng-show="storagePartSelectionVm.searchFilter.searchQuery.length > 0"
                   ng-click="storagePartSelectionVm.resetSearch()"></i>
            </div>
        </div>
        <%--<div class="input-group mb15 storageSearch-box">
            <input id="newItemSearchBox" type="text" class="form-control input-sm"
                   placeholder="Search Parts add to storage" style="border-radius: 3px !important;background: white;"
                   onfocus="this.setSelectionRange(0, this.value.length)"
                   ng-click="storagePartSelectionVm.preventClick($event)"
                   ng-model="storagePartSelectionVm.searchFilter.searchQuery"
                   ng-model-options="{ debounce: 1000 }"
                   ng-change="storagePartSelectionVm.performSearch()">
                    <span class="input-group-btn">
                        <button type="button" class="btn btn-sm btn-primary"
                                style="margin-right: 10px;padding: 6px;margin-top: 3px;"
                                ng-click="storagePartSelectionVm.performSearch()">Search
                        </button>
                    </span>
        </div>--%>
    </div>


    <div id="storageParts-content" ng-show="storagePartSelectionVm.showResults">
        <div class="responsive-table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 5%;">
                        <a href="" title="Add item"
                           ng-click="storagePartSelectionVm.addAllPartsToStorage()"><i
                                class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                    </th>
                    <th class="twenty-column">Nomenclature</th>
                    <th class="thirty-column">Code</th>
                    <th class="fifteenPercent-column">Type</th>
                    <th class="thirty-column">Section</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="storagePartSelectionVm.searchResults.content.length == 0">
                    <td colspan="15" style="text-align: left;">No items found</td>
                </tr>
                <tr ng-repeat="result in storagePartSelectionVm.searchResults.content  | orderBy:'item.itemMaster.itemName'">
                    <td style="width: 5%">
                        <a href="" title="Add item"
                           ng-click="storagePartSelectionVm.addPartToStorage(result)"><i
                                class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                    </td>
                    <td class="twenty-column">
                        <span ng-bind-html="result.item.itemMaster.itemName | highlightText: storagePartSelectionVm.searchFilter.searchQuery"></span>
                    </td>
                    <td class="thirty-column">
                        <span ng-bind-html="result.item.itemMaster.itemCode | highlightText: storagePartSelectionVm.searchFilter.searchQuery"></span>
                    </td>
                    <td class="fifteenPercent-column">
                        <span ng-bind-html="result.item.itemMaster.parentType.name | highlightText: storagePartSelectionVm.searchFilter.searchQuery"></span>
                    </td>
                    <td class="thirty-column">
                        <span ng-if="result.pathCount > 1">
                            <ui-select ng-model="result.defaultSection" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Section">
                                    <span style="display: inline-flex;">{{$select.selected.name}}
                                    <span ng-if="$select.selected.versity"> ( VSPL )</span></span>
                                </ui-select-match>
                                <ui-select-null-choice></ui-select-null-choice>
                                <ui-select-choices
                                        repeat="section in result.sections | filter: $select.search">
                                    <div><span>{{section.name}}</span>
                                        <span ng-if="section.versity">( VSPL )</span>
                                    </div>
                                </ui-select-choices>
                            </ui-select>
                        </span>
                        <span ng-if="result.pathCount == 1">{{result.defaultSection.name}}
                            <span ng-if="result.defaultSection.versity">( VSPL )</span>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>


<%--
<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 0;
        top: 40px;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
        background-color: #fff;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>
<div class="view-container" fitcontent>
    <style scoped>

        .popover {
            min-width: 20% !important;
            max-width: 20% !important;
            z-index: 10000 !important;
        }

        #leftSidePanelContent {
            overflow-y: hidden !important;
        }

        #freeTextSearchDirective {
            top: 50px !important;
        }

        .storageSearch-box > input {
            width: 50%;
            border-radius: 15px !important;
            padding-left: 30px;
            height: 35px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            box-shadow: none;
        }

        .storageSearch-box > input:hover {
            background-color: rgb(229, 231, 234);
        }

        .storageSearch-box > input:focus {
            border: 1px solid #ddd !important;
            box-shadow: none;
        }

        .storageSearch-box i.fa-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 13px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
        }

        .storageSearch-box i.clear-search {
            margin-left: -27px;
            color: gray;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 12px;
        }

        .storageSearch-box {
            width: 95%;
            margin: auto;
        }
    </style>
    <div class="view-content no-padding">
        &lt;%&ndash;<div style="display: inline-flex;height: 93%;width: 100%;">
            <div style="width: 30%;overflow-y: auto;">
                <div class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <ul id="bomStructure" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div style="width: 70%;border-left: 3px solid lightgrey;">
                <h4 ng-if="storagePartSelectionVm.selectedBomType == null">
                    Please select Section/Sub System/Unit to add parts to storage
                </h4>

                <div class="storageSearch-box" ng-if="storagePartSelectionVm.selectedBomType != null"
                     style="width:100%;text-align: right;">
                    <i class="fa fa-search"></i>
                    <input type="search"
                           class="form-control input-sm"
                           ng-model="storagePartSelectionVm.searchText"
                           ng-model-options="{ debounce: 1000 }"
                           ng-change="storagePartSelectionVm.freeTextSearch(storagePartSelectionVm.searchText)"
                           ng-init="showClear()"
                           ng-enter="storagePartSelectionVm.freeTextSearch(searchTerm)">
                    <i class="fa fa-times-circle clear-search" title="Clear"
                       ng-show="storagePartSelectionVm.searchText.length > 0"
                       ng-click="storagePartSelectionVm.searchText = null;storagePartSelectionVm.resetPage()"></i>
                </div>

                &lt;%&ndash; <free-text-search on-clear="storagePartSelectionVm.resetPage"
                                   search-term="storagePartSelectionVm.searchText"
                                   on-search="storagePartSelectionVm.freeTextSearch"></free-text-search>&ndash;%&gt;

                <div class="responsive-table" style="height: 91% !important;overflow-y: auto;"
                     ng-if="storagePartSelectionVm.selectedBomType != null">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 50px;">
                                <i class="fa fa-plus-square" title="Click to add all" style="cursor: pointer;"
                                   ng-click="storagePartSelectionVm.addAllPartsToStorage()"></i>
                            </th>
                            <th class="twoFifty-column">Nomenclature</th>
                            <th style="width: 100px;">Item Code</th>
                            <th class="hundred-column">Section</th>
                            <th class="oneFifty-column">Type</th>
                            <th class="hundred-column">Locations</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="storagePartSelectionVm.loading == true">
                            <td colspan="25">
                                <span style="font-size: 15px;">
                                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                         class="mr5">
                                    <span>Loading Parts...</span>
                                </span>
                            </td>
                        </tr>

                        <tr ng-if="storagePartSelectionVm.loading == false && (storagePartSelectionVm.uniqueParts.length == 0 && storagePartSelectionVm.commonParts.length == 0)">
                            <td colspan="25">No Parts to Storage</td>
                        </tr>

                        <tr ng-repeat="part in storagePartSelectionVm.uniqueParts | orderBy:'item.itemMaster.itemName'">
                            <td style="width: 50px;">
                                <i class="fa fa-plus-circle" title="Click to add"
                                   ng-click="storagePartSelectionVm.addPartToStorage(part)"></i>
                            </td>
                            <td class="twoFifty-column">{{part.item.itemMaster.itemName}}</td>
                            <td class="hundred-column">{{part.item.itemMaster.itemCode}}</td>
                            <td style="width: 150px;">
                                <span ng-if="part.pathCount > 1">
                                    <ui-select ng-model="part.defaultSection" theme="bootstrap" style="width:100%">
                                        <ui-select-match placeholder="Select Type">
                                            {{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-null-choice></ui-select-null-choice>
                                        <ui-select-choices
                                                repeat="section in part.sections">
                                            <div ng-bind="section"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </span>
                                <span ng-if="part.pathCount == 1">{{part.defaultSection}}</span>
                            </td>
                            <td class="oneFifty-column">{{part.item.itemMaster.parentType.name}}</td>
                            <td class="hundred-column">
                                <span ng-if="part.storages.length > 1">
                                    <a title="Click to show Locations"
                                       class="badge badge-secondary"
                                       uib-popover-template="storagePartSelectionVm.locationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="top-right"
                                       popover-title="Locations"
                                       popover-trigger="'outsideClick'" style="z-index: 10000 !important;">{{part.storages.length}}
                                    </a>
                                </span>
                                </span>
                                <span ng-if="part.storages.length == 1">{{part.storages[0].name}}</span>
                            </td>
                        </tr>

                        <tr ng-repeat="part in storagePartSelectionVm.commonParts | orderBy:'item.itemMaster.itemName'">
                            <td>
                                <i class="fa fa-plus-circle"
                                   ng-click="storagePartSelectionVm.addPartToStorage(part)"></i>
                            </td>
                            <td>{{part.item.itemMaster.itemName}}</td>
                            <td>{{part.item.itemMaster.itemCode}}</td>
                            <td style="width: 150px;">
                                <span ng-if="part.pathCount > 1">
                                    <ui-select ng-model="part.defaultSection" theme="bootstrap" style="width:100%">
                                        <ui-select-match placeholder="Select Type">
                                            {{$select.selected}}
                                        </ui-select-match>
                                        <ui-select-null-choice></ui-select-null-choice>
                                        <ui-select-choices
                                                repeat="section in part.sections">
                                            <div ng-bind="section"></div>
                                        </ui-select-choices>
                                    </ui-select>
                                </span>
                                <span ng-if="part.pathCount == 1">{{part.defaultSection}}</span>
                            </td>
                            <td>{{part.item.itemMaster.parentType.name}}</td>
                            <td>
                                <span ng-if="part.storages.length > 1">
                                    <a title="Click to show Locations"
                                       class="badge badge-secondary"
                                       uib-popover-template="storagePartSelectionVm.locationsPopover.templateUrl"
                                       popover-append-to-body="true"
                                       popover-popup-delay="50"
                                       popover-placement="top-right"
                                       popover-title="Locations"
                                       popover-trigger="'outsideClick'"
                                       style="z-index: 10000 !important;">{{part.storages.length}}
                                    </a>
                                </span>
                                <span ng-if="part.storages.length == 1">{{part.storages[0].name}}</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>&ndash;%&gt;

    </div>
</div>--%>
