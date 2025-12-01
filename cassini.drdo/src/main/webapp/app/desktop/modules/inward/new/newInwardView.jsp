<div style="position: relative;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

        .inward-search-box {
            width: 100%;
            margin: 0;
            float: right;
        }

        tr.disabled-reviewd td {
            background-color: red !important;
        }

        .inward-search-box input {
            height: 34px !important;
            border-radius: 20px !important;
            padding-left: 20px;
        }

        .inward-search-box i.fa-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 13px;
            left: 5px;
            color: grey;
            opacity: 0.6;
        }

        .inward-search-box .clear-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 12px;
            right: 5px;
            color: #1A0505;
            opacity: 0.6;
            font-size: 16px;
            cursor: pointer;
        }

        .searchResults {
            display: none;
            position: absolute;
            top: 37px;
            left: 0;
            width: 100%;
            max-height: 350px;
            min-height: 100px;
            border: 1px solid #ddd;
            z-index: 100;
            background-color: #fff;
            overflow-y: auto;
        }

        .searchResults table th, .searchResults table td {
            padding: 5px !important;
        }

    </style>
    <div style="overflow-y: auto; overflow-x: hidden;min-height: 350px;">
        <div class="col-md-12 text-right" style="padding-right: 10px;">
            <div class="col-md-3">
                <h5>Search Gate Pass : </h5>
            </div>
            <div class="col-md-9" style="padding: 0px;">
                <div class="input-group mb15 inward-search-box">
                    <i class="fa fa-search"></i>
                    <input id="gatePassSearchBox" type="text"
                           class="form-control input-sm"
                           placeholder="Search Gate Pass"
                           onfocus="this.setSelectionRange(0, this.value.length)"
                           ng-click="newInwardVm.preventClick($event)"
                           ng-model="newInwardVm.gatePassFilter.searchQuery"
                           ng-model-options="{ debounce: 100 }"
                           ng-change="newInwardVm.performSearch()" autocomplete="off">
                    <i class="fa fa-times-circle clear-search" title="Clear search"
                       ng-show="newInwardVm.gatePassFilter.searchQuery.length > 0"
                       ng-click="newInwardVm.gatePassFilter.searchQuery = '';newInwardVm.performSearch()"></i>
                </div>

                <div id="gatePassSearchResults" class="searchResults">
                    <table class="table table-striped table-condensed highlight-row">
                        <thead>
                        <tr ng-if="newInwardVm.searchResults.length > 0">
                            <th>Gate Pass</th>
                            <th>Gate Pass Number</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="newInwardVm.searchResults.length == 0">
                            <td colspan="15" style="text-align: left;">No Gate Pass found</td>
                        </tr>
                        <tr ng-repeat="result in newInwardVm.searchResults"
                            title="Click to add gate pass"
                            ng-click="newInwardVm.addGatePass(result)">
                            <td class="text-left">
                                <i class="fa fa-plus-circle" style="padding: 0 4px;font-size: 16px;"></i>
                                {{result.gatePass.name}}
                            </td>
                            <td class="text-left">{{result.gatePassNumber}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="row" style="margin: 0;padding: 10px;" ng-if="newInwardVm.newInward.gatePass != null">
            <div>
                <h4 class="section-title" style="color: black;">Basic Info</h4>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Person</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-8">
                            <h5>{{loginPersonDetails.person.fullName}}</h5>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-8">
                            <h5><a href="" title="Click to download"
                                   ng-click="newInwardVm.downloadFile(newInwardVm.newInward.gatePass.gatePass)">
                                {{newInwardVm.newInward.gatePass.gatePass.name}}</a>
                            </h5>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass Number</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-8">
                            <h5>{{newInwardVm.newInward.gatePass.gatePassNumber}}</h5>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Gate Pass Date</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-8">
                            <h5>{{newInwardVm.newInward.gatePass.gatePassDate}}</h5>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">BOM <span class="asterisk">*</span> : </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newInwardVm.newInward.bom" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="{{newInwardVm.bomTitle}}">
                                    {{$select.selected.item.itemMaster.itemName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bom in newInwardVm.boms track by bom.id">
                                    <div ng-bind="bom.item.itemMaster.itemName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Supplier</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newInwardVm.newInward.supplier" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newInwardVm.supplierTitle}}">
                                    {{$select.selected.supplierName}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="supplier in newInwardVm.suppliers | filter: $select.search">
                                    <div ng-bind="supplier.supplierName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>Notes</span>: </label>

                        <div class="col-sm-8">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newInwardVm.newInward.notes"></textarea>
                        </div>
                    </div>

                    <attributes-view attributes="newInwardVm.newInwardAttributes"></attributes-view>
                </form>
            </div>
        </div>
    </div>
</div>
