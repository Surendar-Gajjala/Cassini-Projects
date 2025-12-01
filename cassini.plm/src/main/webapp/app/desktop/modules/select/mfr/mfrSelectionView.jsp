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

   #mfrs-table thead th {
       position: sticky;
       top : 33px;
   }
   .sidePanel-content{
        height: 515px !important;
    }
    
</style>

<div class="view-container" fitcontent>
    <div class="sidePanel-content no-padding" style="padding: 10px !important;">
        <%--<div>
            <div class="input-group input-group-sm mb15 manufacturerselection"
                 style="background-color: #FFF !important;margin-top: 18px;width: 77%;margin-left: 6px;position: absolute;">
                &lt;%&ndash;<span class="input-group-btn">
                    <button type="button" ng-click="mfrSelectVm.resetPage()" class="btn"
                            title="Clear search"
                            style="height: 30px !important;background-color: #FFF !important;border: 1px solid #EADADA">
                        <i class="fa fa-times-circle" style="font-size:16px"></i>
                    </button>
                </span>
                    <input class="form-control" type="text" ng-enter="mfrSelectVm.freeTextSearch()"
                           ng-model="mfrSelectVm.searchTerm">
                <span class="input-group-btn">
                    <button type="button" ng-click="mfrSelectVm.freeTextSearch()" class="btn"
                            title="Search"
                            style="height: 30px !important;background-color: #FFF !important;border: 1px solid #EADADA">
                        <i class="fa fa-search" style="font-size:15px"></i>
                    </button>
                </span>&ndash;%&gt;


                <input class="form-control" type="text" ng-change="mfrSelectVm.freeTextSearch()" auto-focus
                       ng-model="mfrSelectVm.searchTerm" style="width: 357px;">
                <button type="button" ng-click="mfrSelectVm.freeTextSearch()" class="btn"
                        title="Search"
                        style="height: 30px !important;background-color: #FFF !important;border: 1px solid #EADADA">
                    <i class="fa fa-search" style="font-size:15px"></i>
                </button>
                <button type="button" ng-click="mfrSelectVm.resetPage()" class="btn"
                        title="Clear Search"
                        style="height: 30px !important;background-color: #FFF !important;border: 1px solid #EADADA">
                    <i class="fa fa-times-circle" style="font-size:15px"></i>
                </button>
            </div>
            <div class="pull-right text-center">
                <span ng-if="mfrSelectVm.loading == false"><small><span translate>PAGE</span>
                    {{mfrSelectVm.manufacturers.totalElements != 0 ? mfrSelectVm.manufacturers.number+1:0}} of
                    {{mfrSelectVm.manufacturers.totalPages}}
                </small></span>
                <br>

                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default" ng-click="mfrSelectVm.previousPage()"
                            ng-disabled="mfrSelectVm.manufacturers.first">
                        <i class="fa fa-chevron-left"></i></button>
                    <button class="btn btn-xs btn-default" ng-click="mfrSelectVm.nextPage()"
                            ng-disabled="mfrSelectVm.manufacturers.last">
                        <i class="fa fa-chevron-right"></i></button>
                </div>
                <br>
                <span ng-if="mfrSelectVm.manufacturers.totalElements == 1"><small>
                    {{mfrSelectVm.manufacturers.totalElements}} Mfr
                </small></span>
                <span ng-if="mfrSelectVm.manufacturers.totalElements > 1"><small>
                    {{mfrSelectVm.manufacturers.totalElements}}
                    Mfrs
                </small></span>
            </div>
        </div>--%>

            <div>
                <div class="search-box" style="position: fixed;margin-top: -10px;background: #f9fbfe;width: 590px;">
                    <i class="fa fa-search"></i>

                    <input type="text" id="freeTextSearchInput"
                           autocomplete="off"
                           class="form-control input-sm"
                           ng-model="mfrSelectVm.searchTerm"
                           ng-model-options="{ debounce: 500 }"
                           ng-change="mfrSelectVm.freeTextSearch()"
                           ng-init="mfrSelectVm.resetPage()"
                           ng-enter="onSearch(searchTerm)">
                    <i class="la la-times clear-search" title="{{clearTitleSearch}}"
                       ng-show="mfrSelectVm.searchTerm.length > 0 || filterSearch == true"
                       ng-click="mfrSelectVm.resetPage()"></i>

                <span style="margin-left: 0px !important;">
                        <medium>
                            <span style="margin-right: 0px;">
                                <span translate>DISPLAYING</span>
                                  <span ng-if="mfrSelectVm.manufacturers.numberOfElements == 0">
                                    {{(mfrSelectVm.pageable.page*mfrSelectVm.pageable.size)}}
                                </span>
                                <span ng-if="mfrSelectVm.manufacturers.numberOfElements > 0">
                                    {{(mfrSelectVm.pageable.page*mfrSelectVm.pageable.size)+1}}
                                </span>-
                                <span ng-if="mfrSelectVm.manufacturers.last ==false">{{((mfrSelectVm.pageable.page+1)*mfrSelectVm.pageable.size)}}</span>
                                <span ng-if="mfrSelectVm.manufacturers.last == true">{{mfrSelectVm.manufacturers.totalElements}}</span>
                                 <span translate> OF </span>
                                            {{mfrSelectVm.manufacturers.totalElements}}
                            </span>
                         </medium>
                    </span>
                     <span class="mr10"> Page {{mfrSelectVm.manufacturers.totalElements != 0 ?
                     mfrSelectVm.manufacturers.number+1:0}} <span translate>OF</span> {{mfrSelectVm.manufacturers.totalPages}}
                    </span>
                    <a href="" ng-click="mfrSelectVm.previousPage()"
                       ng-class="{'disabled': mfrSelectVm.manufacturers.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="mfrSelectVm.nextPage()"
                       ng-class="{'disabled': mfrSelectVm.manufacturers.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>

            </div>

            <table id="mfrs-table" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>MANUFACTURER_NAME</th>
                <th style="width: 200px;" translate>TYPE</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
            </tr>
            </thead>
            <br><br>
            <tbody>
            <tr ng-if="mfrSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_MFRS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="mfrSelectVm.loading == false && mfrSelectVm.manufacturers.content.length == 0">
                <td colspan="12">
                    <span translate>NO_MFRS</span>
                </td>
            </tr>

            <tr ng-repeat="manufacturer in mfrSelectVm.manufacturers.content"
                ng-click="manufacturer.isChecked = !manufacturer.isChecked; mfrSelectVm.radioChange(manufacturer, $event)"
                ng-dblClick="manufacturer.isChecked = !manufacturer.isChecked; mfrSelectVm.selectRadioChange(manufacturer, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="manufacturer.isChecked" name="manufacturer" value="manufacturer"
                           ng-dblClick="mfrSelectVm.selectRadioChange(manufacturer, $event)"
                           ng-click="mfrSelectVm.radioChange(manufacturer, $event)">
                </td>
                <td style="width: 200px;"><span
                        ng-bind-html="manufacturer.name | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 200px;">{{manufacturer.mfrType.name}}</td>
                <td style="width: 200px;"><span
                        ng-bind-html="manufacturer.description | highlightText: freeTextQuery"></span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>