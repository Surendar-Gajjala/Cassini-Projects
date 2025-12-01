<div style="position: relative;">
    <style>
        .search-form {
            height: 26px;
            border-radius: 15px;
        }

        .search-box {
            width: 100%;
            margin: 0;
            float: right;
        }

        tr.disabled-reviewd td {
            background-color: red !important;
        }

        .search-box input {
            height: 34px !important;
            border-radius: 5px !important;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .bomType-search-box {
            width: 100%;
            margin: 0;
            float: right;
        }

        .bomType-search-box input {
            height: 34px !important;
            border-radius: 20px !important;
            padding-left: 20px;
        }

        .bomType-search-box i.fa-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 13px;
            left: 5px;
            color: grey;
            opacity: 0.6;
        }

        .bomType-search-box .clear-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 12px;
            right: 5px;
            color: #1A0505;
            opacity: 0.6;
            font-size: 16px;
            cursor: pointer;
        }
    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 0px 20px;">
        <div class="col-md-12" style="border-bottom: 1px solid grey;padding: 7px;">
            <div class="col-md-6" style="padding-left: 0px;">
                <div class="input-group mb15 bomType-search-box">
                    <i class="fa fa-search"></i>
                    <input id="gatePassSearchBox" type="text"
                           class="form-control input-sm"
                           placeholder="Search {{addBomGroupTypeVm.placeholderTitle}}"
                           ng-model="addBomGroupTypeVm.sectionFilter.name"
                           ng-change="addBomGroupTypeVm.searchBomTypes()">
                    <i class="fa fa-times-circle clear-search" title="Clear search"
                       ng-show="addBomGroupTypeVm.sectionFilter.name.length > 0"
                       ng-click="addBomGroupTypeVm.sectionFilter.name = '';addBomGroupTypeVm.loadSectionBomGroups()"></i>
                </div>
            </div>
            <div class="col-md-3" style="padding: 10px 0px;">
                <span>Displaying {{addBomGroupTypeVm.searchResults.numberOfElements}} of {{addBomGroupTypeVm.searchResults.totalElements}}</span>
            </div>
            <div class="col-md-3 text-right" style="padding: 10px 0px;">
                <span class="mr10">Page {{addBomGroupTypeVm.searchResults.totalElements != 0 ? addBomGroupTypeVm.searchResults.number+1:0}} of {{addBomGroupTypeVm.searchResults.totalPages}}</span>
                <a href="" ng-click="addBomGroupTypeVm.previousPage()"
                   ng-class="{'disabled': addBomGroupTypeVm.searchResults.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="addBomGroupTypeVm.nextPage()"
                   ng-class="{'disabled': addBomGroupTypeVm.searchResults.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
        <div class="col-md-12 text-right" style="padding: 0px;">
            <div class="responsive-table">
                <table class="table table-striped table-condensed">
                    <thead>
                    <tr>
                        <th style="width: 50px">Add</th>
                        <th>Type</th>
                        <th>Code</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="addBomGroupTypeVm.searchResults.content.length == 0">
                        <td colspan="15" style="text-align: left;">No {{addBomGroupTypeVm.placeholderTitle}}s found</td>
                    </tr>
                    <tr ng-repeat="result in addBomGroupTypeVm.searchResults.content | orderBy: 'name'">
                        <td style="width: 50px;text-align: left;">
                            <a href="" title="Add item"
                               ng-click="addBomGroupTypeVm.addSectionToBom(result)"><i
                                    class="fa fa-plus-circle" style="font-size: 18px;"></i></a>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.name | highlightText: addBomGroupTypeVm.sectionFilter.name"></span>
                            <span ng-if="result.versity" style="padding-left: 5px;">( Versity )</span>
                        </td>
                        <td class="text-left">
                            <span ng-bind-html="result.code | highlightText: addBomGroupTypeVm.sectionFilter.name"></span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>